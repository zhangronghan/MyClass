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
import com.example.administrator.myclass.adapter.MyVoteAdapter;
import com.example.administrator.myclass.data.ActivityVoteGroup;
import com.example.administrator.myclass.data.MyIntentData;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/10/17.
 */

public class VoteActivity extends BaseActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MyVoteAdapter mVoteAdapter;
    private List<ActivityVoteGroup> mActivityVoteGroupList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_vote;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);


        mVoteAdapter=new MyVoteAdapter(this,mActivityVoteGroupList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mVoteAdapter);
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

        mVoteAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(VoteActivity.this,VoteDetailActivity.class);
                intent.putExtra(MyIntentData.VOTE_DETAIL,mActivityVoteGroupList.get(position));
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

    private void getDataFromBmob() {
        final Dialog dialog= BaseFunction.showProgressDialog(this,"获取中....");
        dialog.show();

        BmobQuery<ActivityVoteGroup> bmobQuery=new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<ActivityVoteGroup>() {
            @Override
            public void done(List<ActivityVoteGroup> list, BmobException e) {
                if(e==null){
                    mActivityVoteGroupList=list;
                    mVoteAdapter.refresh(mActivityVoteGroupList);
                    dialog.dismiss();
                } else {
                    showToast("获取数据出错，请重新进入");
                    Log.e("AAA","getData:"+e.getMessage());
                    dialog.dismiss();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_vote_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_vote_create:
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
                            Intent intent=new Intent(VoteActivity.this,VoteCreateActivity.class);
                            intent.putExtra(MyIntentData.VOTE_TITLE,title);
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

            case R.id.item_vote_delete:
                showToast("删除活动");
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
