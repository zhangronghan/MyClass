package com.example.administrator.myclass.data;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/7.
 */

public class ClassGroup extends BmobObject implements Serializable{
    //班级id 选择bmob给的objectId
    private String groupName; //班级名
    private String manager; //管理者（包括群主）
    private String imageHeaderUri;  //班级头像

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getImageHeaderUri() {
        return imageHeaderUri;
    }

    public void setImageHeaderUri(String imageHeaderUri) {
        this.imageHeaderUri = imageHeaderUri;
    }
}
