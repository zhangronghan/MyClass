package com.example.administrator.myclass.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.adapter.MyAppraiseRecyclerViewAdapter;
import com.example.administrator.myclass.data.AppraiseGroup;
import com.example.administrator.myclass.data.MyIntentData;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/10/14.
 */

public class AppraiseActivity extends BaseActivity{
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MyAppraiseRecyclerViewAdapter mAppraiseRecyclerViewAdapter;
    private List<AppraiseGroup> mAppraiseGroupList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_appraise;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        mAppraiseRecyclerViewAdapter=new MyAppraiseRecyclerViewAdapter(this, mAppraiseGroupList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAppraiseRecyclerViewAdapter);
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

        mAppraiseRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(AppraiseActivity.this,AppraiseDetailActivity.class);
                intent.putExtra(MyIntentData.APPRAISE_DATA,mAppraiseGroupList.get(position));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void initData() {
        mToolbar.setNavigationIcon(R.drawable.ic_appraise_back);


        getDataFromBmob();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromBmob();
    }

    /**
    * 从bmob中获取评优数据
    */
    private void getDataFromBmob() {
        final Dialog dialog= BaseFunction.showProgressDialog(this,"加载中....");
        dialog.show();
        BmobQuery<AppraiseGroup> bmobQuery=new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<AppraiseGroup>() {
            @Override
            public void done(List<AppraiseGroup> list, BmobException e) {
                if(e==null){
                    mAppraiseGroupList =list;
                    mAppraiseRecyclerViewAdapter.refresh(mAppraiseGroupList);
                    dialog.dismiss();
                } else {
                    showToast("加载出错");
                    dialog.dismiss();
                    Log.e("AAA","GET_DATA: "+e.getMessage());
                }
            }
        });

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
                final EditText edtTitle=new EditText(this);
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("输入标题").setView(edtTitle);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title=edtTitle.getText().toString();
                        if(title.equals("")){
                            showToast("请输入标题");
                        } else {
                            Intent intent=new Intent(AppraiseActivity.this,AppraiseCreateActivity.class);
                            intent.putExtra(MyIntentData.APPRAISE_TITLE,title);
                            startActivity(intent);
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

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
