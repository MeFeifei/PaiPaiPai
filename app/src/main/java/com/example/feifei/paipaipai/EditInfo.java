package com.example.feifei.paipaipai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import DB.UserDAO;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import entity.Person;

public class EditInfo extends AppCompatActivity implements View.OnClickListener{
    private static String LOG_TAG = "EditInfo Activity";

    private Person mPerson;
    private EditText userName,gender,personalSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        initView();
        mPerson = (Person) getIntent().getSerializableExtra("user");
        initData();
    }
    //初始化对象
    private void initView(){
        ((TextView)findViewById(R.id.title_center)).setText("编辑个人资料");
        (findViewById(R.id.title_left_tv)).setVisibility(View.VISIBLE);

        userName = (EditText) findViewById(R.id.nickname_edit);
        gender = (EditText) findViewById(R.id.gender_edit);
        personalSign = (EditText) findViewById(R.id.signal_edit);
        TextView saveInfo = (TextView) findViewById(R.id.save_edit);

        findViewById(R.id.title_left).setOnClickListener(this);
        saveInfo.setOnClickListener(this);
    }
    //初始化数据
    private void initData(){
        userName.setText(mPerson.getUsername());
        gender.setText(mPerson.getGender());
        personalSign.setText(mPerson.getPersonalSign());
    }
    /**
     * 设置Person 的值
     */
    private void setData(){
        mPerson.setUsername(userName.getText().toString());
        mPerson.setGender(gender.getText().toString());
        mPerson.setPersonalSign(personalSign.getText().toString());
    }

    /**
     * 结束Activity
     */
    private void finishActivity(){
        Intent intent = new Intent(this,Index.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",mPerson);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    /**
     * 保存成功
     */
    private void saveSuccess(){
        Log.i(LOG_TAG,"保存信息成功");
        finishActivity();
    }

    /**
     * 保存失败
     * @param s 出错信息
     */
    private void saveFail(String s){
        Log.i(LOG_TAG,s);
    }

    /**
     * 更新本地数据库
     * @param mPerson
     */
    private void updatePerson(Person mPerson){
        UserDAO userDAO = new UserDAO(this);
        try {
            if (TextUtils.isEmpty(userName.getText().toString())){
                Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            }else {
                setData();
                if (userDAO.update(mPerson)) {
                    Intent intent = new Intent(EditInfo.this, Index.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mPerson);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Log.i(LOG_TAG, "保存到本地数据库成功" + mPerson.getUsername());
                    saveSuccess();
                } else {
                    Log.i(LOG_TAG, "保存到本地数据库失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveFail(e.getMessage());
        }
    }

    /**
     * 更新云端数据库
     * @param mPerson
     */
    private void updatePersonToBmob(Person mPerson){
        mPerson.setValue("username",userName);
        mPerson.setValue("gender",gender);
        mPerson.setValue("personalSign",personalSign);
//        mPerson.setUsername(nickName);
//        mPerson.setGender(gender);
//        mPerson.setPersonalSign(personalSign);
        mPerson.update(mPerson.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    saveSuccess();
                }else {
                    saveFail(e.getMessage()+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finishActivity();
              break;
            case R.id.save_edit:
                updatePerson(mPerson);
        }

    }
}
