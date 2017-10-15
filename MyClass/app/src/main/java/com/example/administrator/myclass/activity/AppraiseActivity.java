package com.example.administrator.myclass.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;

/**
 * Created by Administrator on 2017/10/14.
 */

public class AppraiseActivity extends BaseActivity{
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_appraise;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_apppreise_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_create_appraise:
                startActivity(new Intent(AppraiseActivity.this,AppraiseCreateActivity.class));
                break;

            case R.id.item_delete_appraise:
                showToast("删除评优");
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }





}
