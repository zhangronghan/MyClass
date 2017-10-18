package com.example.administrator.myclass.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.Listener.OnAgreeClickListener;
import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.adapter.MyVoteDetailAdapter;
import com.example.administrator.myclass.data.ActivityVote;
import com.example.administrator.myclass.data.ActivityVoteGroup;
import com.example.administrator.myclass.data.MyIntentData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by Administrator on 2017/10/17.
 */

public class VoteDetailActivity extends BaseActivity implements OnAgreeClickListener {
    private Toolbar mToolbar;
    private TextView mTvTitle;
    private RecyclerView mRecyclerView;
    private MyVoteDetailAdapter mVoteDetailAdapter;
    private ActivityVoteGroup activityVoteGroup;
    private List<ActivityVote> mActivityVoteList = new ArrayList<>();
    private BmobRealTimeData bmobRealTimeData;     //Bmob实时同步
    private BmobUser bmobUser = BmobUser.getCurrentUser();
    private int total;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_vote_detail;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        activityVoteGroup = (ActivityVoteGroup) getIntent().getSerializableExtra(MyIntentData.VOTE_DETAIL);
        mVoteDetailAdapter = new MyVoteDetailAdapter(this, mActivityVoteList, this,activityVoteGroup);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mVoteDetailAdapter);
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

        mVoteDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(VoteDetailActivity.this,VoteContentActivity.class);
                intent.putExtra(MyIntentData.VOTE_CONTENT,mActivityVoteList.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        mToolbar.setNavigationIcon(R.drawable.ic_appraise_back);

        mTvTitle.setText(activityVoteGroup.getActivityTitle());
        getDataFromBmob();

        syncData();


    }

    private void syncData() {
        bmobRealTimeData = new BmobRealTimeData();
        bmobRealTimeData.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null && bmobRealTimeData.isConnected()){
                    bmobRealTimeData.subTableUpdate("ActivityVoteGroup");
                    Log.e("AAA", "连接成功");
                } else {
                    Log.e("AAA", "连接失败");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                JSONObject data = jsonObject.optJSONObject("data");
                total=data.optInt("voteNum");

                Log.e("QQQ", jsonObject + "");
                Log.e("QQQ",data.optString("voteNum"));

            }
        });

    }

    private void getDataFromBmob() {
        final Dialog dialog = BaseFunction.showProgressDialog(this, "");
        dialog.show();

        BmobQuery<ActivityVote> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("activityTitle", activityVoteGroup.getActivityTitle());
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<ActivityVote>() {
            @Override
            public void done(List<ActivityVote> list, BmobException e) {
                if (e == null) {
                    mActivityVoteList = list;
                    mVoteDetailAdapter.refresh(mActivityVoteList);
                    dialog.dismiss();
                } else {
                    showToast("加载数据出错");
                    Log.e("AAA", "getData:" + e.getMessage());
                    dialog.dismiss();
                }
            }
        });


    }


    @Override
    public void OnItemAgree(int position) {
        Dialog dialog = BaseFunction.showProgressDialog(this, "");
        dialog.show();


        decideVote(position, mActivityVoteList, dialog);


    }

    /**
     * 判断是否已投票
     */
    private void decideVote(int position, List<ActivityVote> activityVoteList, Dialog dialog) {
        boolean voting = true;   //默认没有投票

        for (int i = 0; i < activityVoteList.size(); i++) {
            List<String> mList = activityVoteList.get(position).getAgreeName();
            if (mList == null) {
                mList = new ArrayList<>();
            }
            if ( !activityVoteList.get(position).getActivityTitle().equals(activityVoteList.get(i).getActivityTitle())) {
                continue;
            }

            for (int k = 0; k < mList.size(); k++) {
                if (bmobUser.getUsername().equals(mList.get(k).toString())) {
                    showToast("你已投票");
                    dialog.dismiss();
                    voting = false;
                    return;
                }

            }
        }
        if (voting) {
            joinVote(position, activityVoteList.get(position).getAgreeName(), dialog);
        }


    }

    private void joinVote(int position, List<String> agreeName, final Dialog dialog) {
        if (agreeName == null) {
            agreeName = new ArrayList<>();
        }
        int num = mActivityVoteList.get(position).getAgreeNum() + 1;
        ActivityVote activityVote = new ActivityVote();
        agreeName.add(bmobUser.getUsername());
        activityVote.setAgreeName(agreeName);
        activityVote.setAgreeNum(num);

        activityVote.update(mActivityVoteList.get(position).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateActivityVoteGroup(dialog);
                } else {
                    dialog.dismiss();
                    Log.e("AAA", "VOTE" + e.getMessage());
                }
            }
        });

    }

    private void updateActivityVoteGroup(final Dialog dialog) {
        int num = activityVoteGroup.getVoteNum();
        final ActivityVoteGroup mActivityVoteGroup = new ActivityVoteGroup();
        mActivityVoteGroup.setVoteNum(num + 1);
        mActivityVoteGroup.update(activityVoteGroup.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    showToast("投票成功");
                    getUpdateData(activityVoteGroup.getActivityTitle());
                    dialog.dismiss();
                } else {
                    showToast("投票失败");
                    dialog.dismiss();
                    Log.e("AAA", "vote" + e.getMessage());
                }
            }
        });

    }

    private void getUpdateData(String activityTitle) {
        BmobQuery<ActivityVote> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("activityTitle",activityTitle);
        bmobQuery.setLimit(20);
        bmobQuery.findObjects(new FindListener<ActivityVote>() {
            @Override
            public void done(List<ActivityVote> list, BmobException e) {
                if(e==null){
                    mVoteDetailAdapter.refresh(list,total);
                } else{
                    Log.e("AAA","getAllData"+e.getMessage());
                }
            }
        });

    }


}
