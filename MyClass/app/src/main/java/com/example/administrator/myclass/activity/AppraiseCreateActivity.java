package com.example.administrator.myclass.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.data.Appraise;
import com.example.administrator.myclass.data.AppraiseGroup;
import com.example.administrator.myclass.data.GetImagePath;
import com.example.administrator.myclass.data.MyIntentData;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/10/15.
 */

public class AppraiseCreateActivity extends BaseActivity {
    private static final int GET_IMAGE = 1001;
    private ImageView mIvBack;
    private TextView mTvFinishAppraise;
    private ImageView mIvAppraisedImage;
    private EditText mEdtAppraisedName;
    private EditText mEdtIntroduce;
    private TextView mTvTitle;
    private Button btnNext;
    private String imageUrl;
    private String title;

/*
    private Boolean isNameNotEmpty=false; //mEdtAppraisedName 是否为空
    private Boolean isIntroduceNotEmpty=false;  //mEdtIntroduce 是否为空
*/

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_appraise_careate;
    }

    @Override
    protected void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvFinishAppraise = (TextView) findViewById(R.id.tv_finish_appraise);
        mIvAppraisedImage = (ImageView) findViewById(R.id.iv_appraisedImage);
        mEdtAppraisedName = (EditText) findViewById(R.id.edt_appraisedName);
        mEdtIntroduce = (EditText) findViewById(R.id.edt_introduce);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        btnNext = (Button) findViewById(R.id.btn_next);

    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvAppraisedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_IMAGE);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = BaseFunction.showProgressDialog(AppraiseCreateActivity.this, "执行中....");
                dialog.show();

                final String name = mEdtAppraisedName.getText().toString();
                final String introduce = mEdtIntroduce.getText().toString();

                if (!TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(introduce)) {

                    final BmobFile bmobFile = new BmobFile(new File(imageUrl));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Appraise appraise = new Appraise();
                                appraise.setAppraiseTitle(title);
                                appraise.setAppraisedName(name);
                                appraise.setIntroduce(introduce);
                                appraise.setImageHeader(bmobFile.getFileUrl());

                                appraise.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            showToast("保存成功,请添加下一个");
                                            dialog.dismiss();

                                            Intent intent = new Intent(AppraiseCreateActivity.this, AppraiseCreateActivity.class);
                                            intent.putExtra(MyIntentData.APPRAISE_TITLE, title);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            showToast("保存失败");
                                            dialog.dismiss();
                                        }
                                    }
                                });

                            } else {
                                Log.e("AAA", e.getMessage());
                            }
                        }
                    });

                } else {
                    dialog.dismiss();
                    showToast("姓名或介绍不能为空");
                }


            }
        });

        mTvFinishAppraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = BaseFunction.showProgressDialog(AppraiseCreateActivity.this, "加载中....");
                dialog.show();

                final String name = mEdtAppraisedName.getText().toString();
                final String introduce = mEdtIntroduce.getText().toString();
                if (!TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(introduce)) {

                    final BmobFile bmobFile = new BmobFile(new File(imageUrl));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Appraise appraise = new Appraise();
                                appraise.setAppraiseTitle(title);
                                appraise.setAppraisedName(name);
                                appraise.setIntroduce(introduce);
                                appraise.setImageHeader(bmobFile.getFileUrl());

                                appraise.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            execureCreateOperate(dialog);

                                            finish();
                                        } else {
                                            showToast("保存失败");
                                            Log.e("AAA","SAVE"+e.getMessage());
                                            dialog.dismiss();
                                        }
                                    }
                                });

                            } else {
                                Log.e("AAA","upload:"+e.getMessage());
                            }
                        }
                    });
                } else {
                    dialog.dismiss();
                    showToast("姓名或介绍不能为空");

                }

            }

        });
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra(MyIntentData.APPRAISE_TITLE);
        mTvTitle.setText("创建评优：" + title);


    }

    /**
     * 执行创建操作
     * */
    private void execureCreateOperate(final Dialog dialog) {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        AppraiseGroup appraiseGroup = new AppraiseGroup();
        appraiseGroup.setAppraiseTitle(title);
        appraiseGroup.setCreateName(bmobUser.getUsername());
        appraiseGroup.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    showToast("创建成功");
                    dialog.dismiss();
                    finish();
                } else {
                    showToast("创建失败");
                    dialog.dismiss();
                    Log.e("AAA", "mTvFinishAppraise: " + e.getMessage());
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    mIvAppraisedImage.setImageBitmap(bitmap);
                    imageUrl = GetImagePath.getPath(AppraiseCreateActivity.this, data.getData());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
