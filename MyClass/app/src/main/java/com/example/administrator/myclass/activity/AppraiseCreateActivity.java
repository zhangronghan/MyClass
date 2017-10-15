package com.example.administrator.myclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;

/**
 * Created by Administrator on 2017/10/15.
 */

public class AppraiseCreateActivity extends BaseActivity{
    private Toolbar mToolbar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_appraise_careate;
    }

    @Override
    protected void initViews() {
        mToolbar= (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void initData() {
        mToolbar.setNavigationIcon(R.drawable.ic_appraise_back);

    }


}
