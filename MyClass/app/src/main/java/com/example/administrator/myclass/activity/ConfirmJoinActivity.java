package com.example.administrator.myclass.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.View.ImageViewPlus;
import com.example.administrator.myclass.data.ClassGroup;
import com.example.administrator.myclass.data.MyIntentData;
import com.example.administrator.myclass.data.MyUser;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/10/13.
 */

public class ConfirmJoinActivity extends BaseActivity {
    private ImageView mIvBack;
    private ImageViewPlus mIvGroupHeader;
    private TextView mTvGroupName;
    private Button mBtnJoin;
    private List<ClassGroup> mGroupList;
    private ClassGroup mGroup;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_confirm_join;
    }

    @Override
    protected void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvGroupHeader = (ImageViewPlus) findViewById(R.id.iv_groupHeader);
        mTvGroupName = (TextView) findViewById(R.id.tv_groupName);
        mBtnJoin = (Button) findViewById(R.id.btn_join);

    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog= BaseFunction.showProgressDialog(ConfirmJoinActivity.this,"请求中....");
                dialog.show();

                BmobUser bmobUser=BmobUser.getCurrentUser();
                MyUser user=new MyUser();
                user.setGroupName(mGroup.getGroupName());
                user.setGroupId(mGroup.getObjectId());
                user.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            showToast("加入成功");
                            dialog.dismiss();
                            Intent intent=new Intent(ConfirmJoinActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("AAA",e.getMessage());
                            showToast("加入失败");
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void initData() {
        mGroup = (ClassGroup) getIntent().getSerializableExtra(MyIntentData.CONFIRMJOIN);
        mTvGroupName.setText(mGroup.getGroupName());
        BaseFunction.getImageFromBmob(mGroup,mIvGroupHeader);

    }



}
