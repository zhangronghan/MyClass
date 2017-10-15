package com.example.administrator.myclass.data;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/9/28.
 */

//用户名、手机号使用bmobuser本身的列
public class MyUser extends BmobUser{
    private boolean isStudent; //是否为学生
    private String imagePath; //头像路径
    private String groupId;   //加入的班级Id
    private String groupName; //加入的班级名

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
