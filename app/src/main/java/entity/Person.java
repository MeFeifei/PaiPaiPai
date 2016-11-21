package entity;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by feifei on 16/10/19.
 */

public class Person extends BmobObject  implements Serializable{
    private String phone;
    private String username;
    private String password;
    private String gender;
    private String personalSign;
    private String email ;
    private boolean isLogin;
    private String videoPath;
    private ArrayList<String> videoPathList;
    private String videoName;
    private ArrayList<String> videoNameList;

    public ArrayList<String> getVideoPathList() {
        return videoPathList;
    }

    public void setVideoPathList(ArrayList<String> videoPathList) {
        this.videoPathList = videoPathList;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public ArrayList<String> getVideoNameList() {
        return videoNameList;
    }

    public void setVideoNameList(ArrayList<String> videoNameList) {
        this.videoNameList = videoNameList;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() { return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonalSign() {
        return personalSign;
    }

    public void setPersonalSign(String personalSign) {
        this.personalSign = personalSign;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAllInfo(String username,String password,String phone,String gender,String personalSign,String email,String videoPath){
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.personalSign = personalSign;
        this.email = email;
        this.videoPath = videoPath;
    }

    public String[] getAllInfo(){
        String[] ss ={username,password,phone,gender,personalSign,email,videoPath};
        return ss;
    }
}
