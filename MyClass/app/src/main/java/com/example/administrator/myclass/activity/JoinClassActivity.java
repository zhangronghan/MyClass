package com.example.administrator.myclass.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.adapter.MyJoinRecyclerViewAdapter;
import com.example.administrator.myclass.data.ClassGroup;
import com.example.administrator.myclass.data.MyIntentData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/10/12.
 */

public class JoinClassActivity extends BaseActivity {
    private EditText edtSearch;
    private TextView tvSearch;
    private RecyclerView mRecyclerView;
    private ImageView ivBack;
    private MyJoinRecyclerViewAdapter mJoinRecyclerViewAdapter;
    private List<ClassGroup> mGroupList;
    private List<ClassGroup> mGroupClickList = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_joinin_class;
    }

    @Override
    protected void initViews() {
        edtSearch = (EditText) findViewById(R.id.edt_search);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ivBack = (ImageView) findViewById(R.id.iv_back);


        mJoinRecyclerViewAdapter = new MyJoinRecyclerViewAdapter(this, mGroupList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mJoinRecyclerViewAdapter);

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = edtSearch.getText().toString();
                if (searchContent.equals("")) {
                    showToast("不能为空");

                } else {
                    for (int i = 0; i < mGroupList.size(); i++) {
                        if (mGroupList.get(i).getGroupName().equals(searchContent)) {
                            List<ClassGroup> mSearchGroup = new ArrayList<>();
                            mSearchGroup.add(mGroupList.get(i));
                            mGroupClickList = mSearchGroup;
                            mJoinRecyclerViewAdapter.refreshData(mSearchGroup);
                        }
                    }
                }

            }
        });

        mJoinRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(JoinClassActivity.this, ConfirmJoinActivity.class);
                intent.putExtra(MyIntentData.CONFIRMJOIN, mGroupClickList.get(position));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void initData() {
        searchAllGroup();

        edtSearch.setCompoundDrawables(drawable_phone(R.drawable.ic_search), null, null, null);
        edtSearch.setHintTextColor(Color.parseColor("#8a8a8a"));


    }

    /*
     * 查询bmob中所有的班级
     * */
    private void searchAllGroup() {
        final ProgressDialog dialog = BaseFunction.showProgressDialog(JoinClassActivity.this, "加载中....");
        dialog.show();

        BmobQuery<ClassGroup> group = new BmobQuery<>();
        group.findObjects(new FindListener<ClassGroup>() {
            @Override
            public void done(List<ClassGroup> list, BmobException e) {
                if (e == null) {
                    mGroupList = list;
                    mGroupClickList = list;
                    mJoinRecyclerViewAdapter.refreshData(mGroupList);
                    dialog.dismiss();

                } else {
                    showToast("查询数据出现异常");
                    Log.e("AAA", e.getMessage());
                    dialog.dismiss();
                }
            }
        });
    }


    //设置EditText左边的图标
    private Drawable drawable_phone(int item) {
        Drawable drawable = getResources().getDrawable(item);
        drawable.setBounds(0, 0, 48, 48);
        return drawable;
    }


}
