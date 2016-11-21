package com.example.feifei.paipaipai;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import DB.UserDAO;
import DB.VideoDAO;
import entity.Person;

public class Index extends AppCompatActivity implements View.OnClickListener{
    private static String LOG_TAG = "Index Activity";

    private Person mPerson;

    //UI Object
    private TextView txt_video,txt_record,txt_info,title;
    private FloatingActionButton fab;

    //Fragment Object
    private Fragment fg_video,fg_record,fg_info;
    private FragmentManager  fragmentManager;

    public String getVideoPath(){
        return mPerson.getVideoPath();
    }
    public Person getPerson(){return mPerson;}
    public ArrayList<String> getVideoPathList(){return mPerson.getVideoPathList();}
    public ArrayList<String> getVideoNameList(){return mPerson.getVideoNameList();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //Fragment
        fragmentManager=getFragmentManager();
        bindViews();

        mPerson = (Person) getIntent().getSerializableExtra("user");
        initData();
    }


    //初始化数据
    private void initData(){
        UserDAO userDAO = new UserDAO(this);

        if (!TextUtils.isEmpty(mPerson.getPhone())){   //判断mPerson是否为空
            Log.i(LOG_TAG,"读取成功，账号为"+mPerson.getPhone()+"!");
            if (TextUtils.isEmpty(mPerson.getUsername())){//如果用户名为空就让用户填写个人信息
                editInfo();
            }else if (!TextUtils.isEmpty(mPerson.getVideoPath())){  //录制完成执行
                readVideoList();
                txt_record.performClick();
            }else {
                userDAO.read(mPerson);
                readVideoList();
                txt_info.performClick();
            }
        }else {
            Log.i(LOG_TAG,"读取信息失败");
        }
    }
    //初始化对象
    private void bindViews(){
        txt_video= (TextView) findViewById(R.id.txt_video);
        txt_record= (TextView) findViewById(R.id.txt_record);
        txt_info= (TextView) findViewById(R.id.txt_info);
        title= (TextView) findViewById(R.id.title_center);
        fab = (FloatingActionButton) findViewById(R.id.info_fab);
        ImageView exit = (ImageView) findViewById(R.id.exit);


        txt_video.setOnClickListener(this);
        txt_record.setOnClickListener(this);
        txt_info.setOnClickListener(this);
        exit.setOnClickListener(this);

    }
    //重置底部按钮选择
    private void setSelected(){
        txt_video.setSelected(false);
        txt_record.setSelected(false);
        txt_info.setSelected(false);
    }
    //隐藏所有fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg_video != null)fragmentTransaction.hide(fg_video);
        if(fg_record !=null)fragmentTransaction.hide(fg_record);
        if(fg_info !=null)fragmentTransaction.hide(fg_info);
    }

    //开始拍照
    public void takePicture(){
        Intent it = new Intent(this,RecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",mPerson);
        it.putExtras(bundle);
        startActivity(it);
        this.finish();
    }

    //读取视频表信息
    private void readVideoList(){
        VideoDAO videoDAO = new VideoDAO(this);
        videoDAO.getVideoList(mPerson);
    }
    //开始修改个人信息
    private void editInfo(){
        Intent i = new Intent(Index.this,EditInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",mPerson);
        i.putExtras(bundle);
        startActivity(i);
        finishActivity();
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.txt_video:
                fab.hide();
                setSelected();
                txt_video.setSelected(true);
                title.setText("视频列表");
                if(fg_video == null){
                    fg_video = new VideoList_Fragment(this);
                    fTransaction.add(R.id.home_content,fg_video);
                }else{
                    fTransaction.show(fg_video);
                }
                break;
            case R.id.txt_record:
                fab.show();
                setSelected();
                txt_record.setSelected(true);
                title.setText("录制视频");
                if(fg_record == null){
                    fg_record = new RecordFragment();
                    fTransaction.add(R.id.home_content,fg_record);
                }else{
                    fTransaction.show(fg_record);
                }

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Index.this,"准备上传视频",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.txt_info:
                setSelected();
                txt_info.setSelected(true);
                fab.show();
                title.setText("个人资料");
                if(fg_info == null){
                    fg_info = new InfoFragment();
                    fTransaction.add(R.id.home_content,fg_info);

                }else{
                    fTransaction.show(fg_info);
                }


                fab.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        editInfo();
                    }
                });

                break;
            case R.id.exit:
                Intent intent = new Intent(Index.this,Login.class);
                startActivity(intent);
                finishActivity();
                break;

        }
        fTransaction.commit();
    }
    private void finishActivity(){
        this.finish();
    }
}
