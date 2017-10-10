package com.example.administrator.myclass.application;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/9/12.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //bmob初始化
        Bmob.initialize(this, "433f4d72362971bd4a9d286110ebb1f1");


    }


}
