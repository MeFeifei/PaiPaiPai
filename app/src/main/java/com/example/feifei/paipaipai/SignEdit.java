package com.example.feifei.paipaipai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DB.UserDAO;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import entity.Person;

public class SignEdit extends AppCompatActivity {
    private static String LOG_TAG = "Sign Edit Activity";
    private Person mPerson;
    private AutoCompleteTextView username;
    private EditText usercode;
    private String phoneNumber,userName,userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_edit);

        mPerson = new Person();

        //以下为接收用户注册的手机号
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        mPerson.setPhone(phoneNumber);

        username = (AutoCompleteTextView) findViewById(R.id.sign_edit_username);
        usercode = (EditText) findViewById(R.id.sign_edit_psd);
        Button bt_save = (Button) findViewById(R.id.save_button);


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePerson(mPerson);
            }
        });
    }
    //保存Person 对象到云数据库
    public void savePerson(Person person){
        userName = username.getText().toString();
        userCode = usercode.getText().toString();
        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userCode)){
            person.setUsername(userName);
            person.setPassword(userCode);
            person.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        saveSuccess();
                    }
                    else {
                        Toast.makeText(SignEdit.this,"保存失败!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this,"用户名密码不能为空！",Toast.LENGTH_LONG).show();
        }
    }
    //保存成功
    private void saveSuccess(){
        Log.i(LOG_TAG,"注册账号为"+phoneNumber);
        UserDAO userDAO = new UserDAO(this);
        userDAO.insert(mPerson);
        Intent intent = new Intent(SignEdit.this,Login.class);
        startActivity(intent);

    }

}
