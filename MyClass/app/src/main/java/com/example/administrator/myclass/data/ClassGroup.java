package com.example.administrator.myclass.data;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/7.
 */

public class ClassGroup extends BmobObject{
    //班级id 选择bmob给的objectId
    private String groupName; //班级名
    private List<String> manager; //管理者（包括群主）
    private List<String> users;   //用户
    private byte[] imageHeader;  //班级头像

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getManager() {
        return manager;
    }

    public void setManager(List<String> manager) {
        this.manager = manager;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public byte[] getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(byte[] imageHeader) {
        this.imageHeader = imageHeader;
    }


}
