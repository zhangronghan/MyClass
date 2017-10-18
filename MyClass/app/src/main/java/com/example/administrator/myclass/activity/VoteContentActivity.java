package com.example.administrator.myclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.data.ActivityVote;
import com.example.administrator.myclass.data.MyIntentData;

/**
 * Created by Administrator on 2017/10/18.
 */

public class VoteContentActivity extends BaseActivity{
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TextView mTvContent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_vote_content;
    }

    @Override
    protected void initViews() {
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        tvTitle= (TextView) findViewById(R.id.tv_title);
        mTvContent= (TextView) findViewById(R.id.tv_content);

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

        ActivityVote activityVote= (ActivityVote) getIntent().getSerializableExtra(MyIntentData.VOTE_CONTENT);

        tvTitle.setText(activityVote.getJoinActivityTitle());
        mTvContent.setText(activityVote.getContent());

    }
}
