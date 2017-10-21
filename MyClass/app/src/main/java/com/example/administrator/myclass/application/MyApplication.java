package com.example.administrator.myclass.application;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.example.administrator.myclass.Utils.SharePreUtil;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/9/12.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //bmob初始化
        Bmob.initialize(this, "9161541abea92bb57a5256bd64cbbe64");

        setNightMode();

    }

    private void setNightMode() {
        boolean nightMode= SharePreUtil.isNightMode(this);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }


}
