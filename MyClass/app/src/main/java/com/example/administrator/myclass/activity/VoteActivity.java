package com.example.administrator.myclass.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.adapter.MyVoteAdapter;
import com.example.administrator.myclass.data.ActivityVoteGroup;
import com.example.administrator.myclass.data.MyIntentData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by Administrator on 2017/10/17.
 */

public class VoteActivity extends BaseActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private Button mBtnDel;
    private MyVoteAdapter mVoteAdapter;
    private List<ActivityVoteGroup> mActivityVoteGroupList = new ArrayList<>();
    private boolean firstPressBack = true;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_vote;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mBtnDel = (Button) findViewById(R.id.btn_del);

        mVoteAdapter = new MyVoteAdapter(this, mActivityVoteGroupList);
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
                Intent intent = new Intent(VoteActivity.this, VoteDetailActivity.class);
                intent.putExtra(MyIntentData.VOTE_DETAIL, mActivityVoteGroupList.get(position));
                startActivity(intent);
            }
        });


        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = BaseFunction.showProgressDialog(VoteActivity.this, "删除中....");
                dialog.show();

                //获得选中的Item
                List<ActivityVoteGroup> mActivityGroup = mVoteAdapter.getSelectedItem();
                if (mActivityGroup != null) {
                    List<BmobObject> activityList = new ArrayList<BmobObject>();
                    for (int i = 0; i < mActivityGroup.size(); i++) {
                        String objectId = mActivityGroup.get(i).getObjectId();
                        ActivityVoteGroup group = new ActivityVoteGroup();
                        group.setObjectId(objectId);
                        activityList.add(group);
                    }

                    List<String> selectPositionList = mVoteAdapter.getSelectTitleArr();
                    for (int i = 0; i < selectPositionList.size(); i++) {
                        Log.e("SSS", selectPositionList.get(i) + "");
                        for (int k = 0; k < mActivityVoteGroupList.size(); k++) {
                            if(mActivityVoteGroupList.get(k).getActivityTitle().equals(selectPositionList.get(i).toString())){
                                mActivityVoteGroupList.remove(k);
                            }

                        }
                    }
                    for(int i=0;i<mActivityVoteGroupList.size() ;i++){
                        Log.e("SSS","AAA:"+mActivityVoteGroupList.get(i).getActivityTitle());
                    }

                    new BmobBatch().deleteBatch(activityList).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if(e==null){
                                mVoteAdapter.refresh(mActivityVoteGroupList);
                                showToast("删除成功");
                                dialog.dismiss();
                            } else {
                                showToast("删除失败");
                                dialog.dismiss();
                            }
                        }
                    });

                } else {
                    showToast("请选择");
                }

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
        final Dialog dialog = BaseFunction.showProgressDialog(this, "获取中....");
        dialog.show();

        BmobQuery<ActivityVoteGroup> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<ActivityVoteGroup>() {
            @Override
            public void done(List<ActivityVoteGroup> list, BmobException e) {
                if (e == null) {
                    mActivityVoteGroupList = list;
                    mVoteAdapter.refresh(mActivityVoteGroupList);
                    dialog.dismiss();
                } else {
                    showToast("数据获取出错，请重新进入");
                    Log.e("AAA", "getData:" + e.getMessage());
                    dialog.dismiss();
                }
            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (firstPressBack) {
                return super.onKeyDown(keyCode, event);
            } else {
                mBtnDel.setVisibility(View.GONE);
                mVoteAdapter.setCheckBoxVisible(false);
                firstPressBack = true;
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_vote_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //创建活动
            case R.id.item_vote_create:
                final EditText edtTitle = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("输入标题").setView(edtTitle);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = edtTitle.getText().toString();
                        if (title.equals("")) {
                            showToast("请输入标题");
                        } else {
                            Intent intent = new Intent(VoteActivity.this, VoteCreateActivity.class);
                            intent.putExtra(MyIntentData.VOTE_TITLE, title);
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

            //删除
            case R.id.item_vote_delete:
                firstPressBack = false;
                mBtnDel.setVisibility(View.VISIBLE);
                mVoteAdapter.setCheckBoxVisible(true);

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
