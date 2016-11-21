package com.example.feifei.paipaipai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DB.UserDAO;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import entity.Person;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView phone;
    private EditText password;
    private Button bt_login;
    private TextView newUser;
    private Person mPerson;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化操作
        initBmob();
        bt_login.setEnabled(false);
        mPerson = new Person();
        userDAO = new UserDAO(this);
        checkCameraPermission();
    }

    //初始化Bmob
    private void initBmob(){
        //设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("0c1dce79b63ccc5018d40dac0aa34603")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);

        phone = (AutoCompleteTextView) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        bt_login = (Button) findViewById(R.id.login_button);
        newUser = (TextView) findViewById(R.id.sign_txt);

        bt_login.setOnClickListener(this);
        newUser.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        String userName = phone.getText().toString();
        String userCode = password.getText().toString();
        switch (v.getId()){
            case R.id.login_button:
                if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userCode)) {
                    doLogin();
                }else {
                    Toast.makeText(this,"账号密码不能为空",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.sign_txt:
                doSign();
                break;
        }
    }
    //登录操作入口
    private void doLogin(){
        String userName = phone.getText().toString();
        String userCode = password.getText().toString();
        doQuery(userName,userCode);

    }
    //跳转到注册界面
    private void doSign(){
        Intent intent =new Intent();
        intent.setClass(Login.this,SignActivity.class);
        startActivity(intent);
    }
    //查询账号密码是否存在
    private void doQuery(final String phoneNum, final String psd){
        String bql ="select * from Person where phone ='"+phoneNum+"'and password='"+psd+"'";
        new BmobQuery<Person>().doSQLQuery(bql, new SQLQueryListener<Person>() {
            @Override
            public void done(BmobQueryResult<Person> result, BmobException e) {
                if(e ==null){
                    //int count = result.getCount();//这里得到符合条件的记录数
                    List<Person> list = result.getResults();
                    if(list!=null && list.size()>0){
                        loginSuccess();
                    }else{
                        loginFail("账号或者密码错误!");
                    }
                }else{
                    loginFail(e.getMessage()+e.getErrorCode());
                }
            }
        });
    }

    private void readPersonFromBmob(Person mPerson){

    }
    /*登录成功
     */
    private void loginSuccess(){
        mPerson.setPhone(phone.getText().toString());
        mPerson.setPassword(password.getText().toString());
        UserDAO userDAO = new UserDAO(this);
        try {
            if (userDAO.personIsExist(mPerson.getPhone())){
                Log.i("Login","person is exist in db");
                userDAO.read(mPerson);
            }else {
                Log.i("Login","person is not exist in db");
                userDAO.insert(mPerson);
            }
            Intent intent = new Intent(Login.this,Index.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",mPerson);
            intent.putExtras(bundle);
            startActivity(intent);
            Log.i("登录成功，登录账号为：",mPerson.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Login Error:",e.getMessage());
        }
        this.finish();
    }
    //登录失败
    private void loginFail(String s){
        password.setText("");
        Log.i("Login Failed",s);
        Toast.makeText(this,"登录失败",Toast.LENGTH_LONG).show();

    }
    /*保存数据到本地数据库，方便读取*/
    private boolean savePerson(){
        mPerson.setPhone(phone.getText().toString());
        mPerson.setPassword(password.getText().toString());
        try {
            userDAO.update(mPerson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error！！！","保存信息到本地出错");
            return false;
        }
    }

    //视频录制需要的权限(相机，录音，外部存储)
    private String[] VIDEO_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //未权限集合
    private List<String> NO_VIDEO_PERMISSION = new ArrayList<String>();

    /**
     * 检测摄像头权限，具备相关权限才能继续
     */
    private void checkCameraPermission() {
        NO_VIDEO_PERMISSION.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//当前版本大于23检测，小于的直接跳转
            for (int i = 0; i < VIDEO_PERMISSION.length; i++) {//存在未授权的权限就添加到集合
                if (ActivityCompat.checkSelfPermission(this, VIDEO_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                    NO_VIDEO_PERMISSION.add(VIDEO_PERMISSION[i]);
                }
            }
            if (NO_VIDEO_PERMISSION.size() == 0) {//有未授权的权限就申请该权限
                setBt_login();
            } else {
                ActivityCompat.requestPermissions(this, NO_VIDEO_PERMISSION.toArray(new String[NO_VIDEO_PERMISSION.size()]), REQUEST_CAMERA);
            }
        } else {
            setBt_login();
        }
    }

    private static final int REQUEST_CAMERA = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            boolean flag = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Toast.makeText(this, "已授权", Toast.LENGTH_SHORT).show();
                setBt_login();
            } else {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void setBt_login(){
        bt_login.setEnabled(true);
    }

}
