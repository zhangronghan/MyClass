package com.example.administrator.myclass.data;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/9/28.
 */

//用户名、手机号使用bmobuser本身的列
public class MyUser extends BmobUser{
    private boolean isStudent;
    private byte[] image;

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
