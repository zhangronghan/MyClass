package com.example.administrator.myclass.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.Listener.OnAgreeClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.adapter.MyAppraiseDetailAdapter;
import com.example.administrator.myclass.data.Appraise;
import com.example.administrator.myclass.data.AppraiseGroup;
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
 * Created by Administrator on 2017/10/16.
 */

public class AppraiseDetailActivity extends BaseActivity implements OnAgreeClickListener {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mTvTitle;
    private MyAppraiseDetailAdapter mAppraiseDetailAdapter;
    private List<Appraise> mAppraiseList = new ArrayList<>();
    private BmobRealTimeData bmobRealTimeData;     //Bmob实时同步
    private BmobUser bmobUser = BmobUser.getCurrentUser();
    private AppraiseGroup appraiseGroup;
    private int total;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_appraise_detail;
    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvTitle = (TextView) findViewById(R.id.tv_title);


        appraiseGroup = (AppraiseGroup) getIntent().getSerializableExtra(MyIntentData.APPRAISE_DATA);
        mAppraiseDetailAdapter = new MyAppraiseDetailAdapter(this, mAppraiseList, this, appraiseGroup);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAppraiseDetailAdapter);
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
        mTvTitle.setText(appraiseGroup.getAppraiseTitle());

        getAppraiseItemData(appraiseGroup);

        SyncData();

    }

    private void SyncData() {
        bmobRealTimeData = new BmobRealTimeData();
        bmobRealTimeData.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if(e==null && bmobRealTimeData.isConnected()){
                    bmobRealTimeData.subTableUpdate("AppraiseGroup");
                    Log.e("AAA", "连接成功");
                } else {
                    Log.e("AAA","连接失败");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                JSONObject data = jsonObject.optJSONObject("data");
                total=data.optInt("voteNum");
                Log.e("AAA","voteNum:"+total);
            }
        });



    }

    private void getAppraiseItemData(AppraiseGroup appraiseGroup) {
        final Dialog dialog = BaseFunction.showProgressDialog(this, "加载中....");
        dialog.show();

        BmobQuery<Appraise> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("appraiseTitle", appraiseGroup.getAppraiseTitle());
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<Appraise>() {
            @Override
            public void done(List<Appraise> list, BmobException e) {
                if (e == null) {
                    mAppraiseList = list;
                    mAppraiseDetailAdapter.refresh(list);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    showToast("读取数据出错");
                    Log.e("AAA", "GETDATA: " + e.getMessage());
                }

            }
        });

    }


    @Override
    public void OnItemAgree(int position) {
        Dialog dialog = BaseFunction.showProgressDialog(this, "");
        dialog.show();

        bmobRealTimeData.subRowUpdate("Appraise", mAppraiseList.get(position).getObjectId());

        decideVote(position, mAppraiseList, dialog);

    }

    /**
     * 判断是否已投票
     *
     * @param position
     * @param appraiseList
     * @param dialog
     */
    private void decideVote(int position, List<Appraise> appraiseList, Dialog dialog) {
        boolean voting = true;   //默认没有投票
        for (int i = 0; i < appraiseList.size(); i++) {
            List<String> mList = appraiseList.get(i).getAgreeName();
            if (mList == null) {
                mList = new ArrayList<>();
            }
            if (!appraiseList.get(position).getAppraiseTitle().equals(appraiseList.get(i).getAppraiseTitle())) {
                continue;
            }
            for (int k = 0; k < mList.size(); k++) {
                if (bmobUser.getUsername().equals(mList.get(k).toString())) {
                    Log.e("ZZZ", mList.get(k).toString());
                    showToast("你已投票");
                    dialog.dismiss();
                    voting = false;
                    return;
                }
            }
        }
        if (voting) {
            joinVote(position, appraiseList.get(position).getAgreeName(), dialog);
        }

    }


    /**
     * 投票
     *
     * @param position
     * @param mAgreeList
     * @param dialog
     */
    private void joinVote(final int position, List<String> mAgreeList, final Dialog dialog) {
        if (mAgreeList == null) {
            mAgreeList = new ArrayList<>();
        }
        int num = (mAppraiseList.get(position).getAgreeNum()) + 1;
        mAgreeList.add(bmobUser.getUsername());
        Appraise appraise = new Appraise();
        appraise.setAgreeNum(num);
        appraise.setAgreeName(mAgreeList);
        appraise.update(mAppraiseList.get(position).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    getVoteNum(dialog);
                } else {
                    dialog.dismiss();
                    Log.e("AAA", "VOTE" + e.getMessage());
                }
            }
        });

    }

    /**
     * 更新投票人数
     */
    private void getVoteNum(final Dialog dialog) {
        int num = appraiseGroup.getVoteNum();
        AppraiseGroup mAppraiseGroup = new AppraiseGroup();
        mAppraiseGroup.setVoteNum(num + 1);
        mAppraiseGroup.update(appraiseGroup.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showToast("投票成功");
                    getUpdateData(appraiseGroup.getAppraiseTitle());
                    dialog.dismiss();
                } else {
                    showToast("投票失败");
                    dialog.dismiss();
                    Log.e("AAA", "vote" + e.getMessage());
                }
            }
        });

    }

    private void getUpdateData(String appraiseTitle) {
        BmobQuery<Appraise> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("appraiseTitle", appraiseTitle);
        bmobQuery.setLimit(20);
        bmobQuery.findObjects(new FindListener<Appraise>() {
            @Override
            public void done(List<Appraise> list, BmobException e) {
                if (e == null) {
                    mAppraiseDetailAdapter.refresh(list,total);
                } else {
                    Log.e("AAA", "getAllData"+e.getMessage());
                }
            }
        });

    }


}

