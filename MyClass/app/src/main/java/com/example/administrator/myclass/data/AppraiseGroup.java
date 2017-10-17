package com.example.administrator.myclass.data;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/10/15.
 */

public class AppraiseGroup extends BmobObject implements Serializable{
    private String createName;      //创建评优的人
    private String appraiseTitle;  //评优的标题
    private int voteNum;       //投票总数

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getAppraiseTitle() {
        return appraiseTitle;
    }

    public void setAppraiseTitle(String appraiseTitle) {
        this.appraiseTitle = appraiseTitle;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
