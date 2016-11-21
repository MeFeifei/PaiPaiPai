package com.example.feifei.paipaipai;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import entity.Person;

public class SignActivity extends AppCompatActivity implements View.OnClickListener{
//    private String phoneNumber = "15700086304";
    private static String sMSModel = "短信验证";
    private AutoCompleteTextView phoneNum;
    private EditText code;
    private Button bt_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        //短信功能初始化

        phoneNum= (AutoCompleteTextView) findViewById(R.id.sign_phone);
        code = (EditText) findViewById(R.id.sign_psd);

        bt_code = (Button) findViewById(R.id.sign_bt_psd);
        Button bt_sign = (Button) findViewById(R.id.sign_button);

        bt_code.setOnClickListener(this);
        bt_sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_bt_psd:
                verifyNumExist();
                break;
            case R.id.sign_button:
                verifySmsCode();
                break;

        }
    }
    // 验证该手机号码是否已被注册，若是则返回，反则下一步
    private void verifyNumExist(){
        String phoneNumber = phoneNum.getText().toString();
        //查询账号是否存在
        String bql ="select * from Person where phone ='"+phoneNumber+"'";
        new BmobQuery<Person>().doSQLQuery(bql, new SQLQueryListener<Person>() {
            @Override
            public void done(BmobQueryResult<Person> result, BmobException e) {
                if(e ==null){
                    List<Person> list = result.getResults();
                    if(list!=null && list.size()>0){
                        numExist();
                    }else{
                        numNotExist();
                    }
                }else{
                    numQueryError();
                }
            }
        });
    }
    //若账号不存在
    private void numNotExist(){
        String phoneNumber = phoneNum.getText().toString();
        if(!TextUtils.isEmpty(phoneNumber)){
            requestSMSCode();
            bt_code.setEnabled(false);
            bt_code.setBackgroundColor(Color.GRAY);
        }
        else {
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_LONG).show();
        }
    }
    //若账号已经存在
    private void numExist(){
        Toast.makeText(this,"该号码已被注册",Toast.LENGTH_LONG).show();
        phoneNum.setText("");
        code.setShowSoftInputOnFocus(false);
        phoneNum.setShowSoftInputOnFocus(true);//设置光标位置？
    }
    //账号查询出现错误
    private void numQueryError(){
        Toast.makeText(this,"账号查询出现未知错误",Toast.LENGTH_LONG).show();
    }
    // 请求短信验证码
    private void requestSMSCode() {
        String phoneNumber = phoneNum.getText().toString();
        BmobSMS.requestSMSCode(phoneNumber, sMSModel,new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if(ex==null){//验证码发送成功
                    Log.i("验证码发送成功","短信id："+smsId);//用于查询本次短信发送详情
                }else{
                    Log.i("短信验证失败","errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage());
                }
            }
        });
    }
    //验证短信验证码
    private void verifySmsCode(){
        String number = phoneNum.getText().toString();
        String smsCode = code.getText().toString();
        if(!TextUtils.isEmpty(number)&&!TextUtils.isEmpty(smsCode)){
            BmobSMS.verifySmsCode(number,smsCode, new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    if(ex==null){//短信验证码已验证成功
                        Log.i("Success","短信验证成功");
                        goSignEdit();
                    }else{
                        Log.i("Fail","验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                    }
                }
            });
        }else{
            Log.i("WRAN","请输入手机号和验证码");
        }
    }
    //跳转到注册信息编辑界面
    private void goSignEdit(){
        String phoneNumber = phoneNum.getText().toString();
        Intent intent = new Intent();
        intent.setClass(SignActivity.this,SignEdit.class);
        intent.putExtra("phoneNumber",phoneNumber);
        startActivity(intent);
    }
}
