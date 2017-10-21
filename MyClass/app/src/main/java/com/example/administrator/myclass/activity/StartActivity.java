package com.example.administrator.myclass.activity;

import android.content.Intent;
import android.os.SystemClock;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.SharePreUtil;

/**
 * Created by Administrator on 2017/9/12.
 */

public class StartActivity extends BaseActivity{
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_start;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                if(SharePreUtil.getFirstRun(StartActivity.this)){
                    GuideActivity();
                    SharePreUtil.setFirstRun(StartActivity.this,false);
                } else {
                    enterRegisterActivity();
                }
            }
        }).start();
    }

    private void enterRegisterActivity() {
        startActivity(new Intent(StartActivity.this,RegisterActivity.class));
        finish();
    }

    private void GuideActivity() {
        startActivity(new Intent(StartActivity.this,GuideActivity.class));
        finish();
    }


}
