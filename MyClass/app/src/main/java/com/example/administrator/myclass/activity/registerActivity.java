package com.example.administrator.myclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.countDownUtil;
import com.example.administrator.myclass.data.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/9/12.
 */

public class RegisterActivity extends BaseActivity {
    private EditText mEdtPhone;
    private EditText mEdtPass;
    private Button mBtnReg;
    private Button mBtnCode;
    private TextView tvCode;
    private TextView tvLog;

    //判断是否使用密码输入
    private boolean isPasswordRegister =true;



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        mEdtPhone = (EditText) findViewById(R.id.edt_phone);
        mEdtPass = (EditText) findViewById(R.id.edt_pass);
        mBtnReg = (Button) findViewById(R.id.btn_Reg);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvLog = (TextView) findViewById(R.id.tv_Log);
        mBtnCode= (Button) findViewById(R.id.btn_code);

    }

    @Override
    protected void initListener() {
        tvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPasswordRegister){
                    mBtnCode.setVisibility(View.VISIBLE);
                    mEdtPass.setHint("输入验证码");
                    mEdtPhone.setHint("输入手机号");
                    tvCode.setText("密码登录");
                    isPasswordRegister =!isPasswordRegister;
                    mEdtPass.setCompoundDrawables(drawable_phone(R.drawable.ic_code_left),null,null,null);
                    mEdtPhone.setCompoundDrawables(drawable_phone(R.drawable.ic_myphone_left),null,null,null);
                    mEdtPass.setText("");
                    mEdtPhone.setText("");

                    mBtnCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //倒计时
                            countDownUtil.countDawnTime(mBtnCode);
                        }
                    });

                } else {
                    mBtnCode.setVisibility(View.GONE);
                    mEdtPass.setHint("输入密码");
                    mEdtPhone.setHint("输入用户名");
                    tvCode.setText("验证码登录");
                    isPasswordRegister =!isPasswordRegister;
                    mEdtPass.setText("");
                    mEdtPass.setCompoundDrawables(drawable_phone(R.drawable.ic_pass_left),null,null,null);
                    mEdtPhone.setCompoundDrawables(drawable_phone(R.drawable.ic_user_left),null,null,null);
                }

            }
        });

        mEdtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mEdtPass.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            if(s.length() > 0){
                                mBtnReg.setBackgroundColor(Color.parseColor("#0071f3"));
                                mBtnReg.setClickable(true);
                            } else {
                                mBtnReg.setClickable(false);
                            }
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(s.length() > 0){
                                mBtnReg.setBackgroundColor(Color.parseColor("#0071f3"));
                                mBtnReg.setClickable(true);
                            } else {
                                mBtnReg.setBackgroundColor(Color.parseColor("#a1a1a1"));
                                mBtnReg.setClickable(false);
                            }

                        }
                    });
                }
            }
        });

        mBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPasswordRegister){
                    usePasswordRegister();
                } else {
                    useCodeRegister();
                }
            }
        });



    }

    //使用密码登录
    private void usePasswordRegister() {
        MyUser myUser=new MyUser();
        String phone=mEdtPhone.getText().toString();
        String password=mEdtPass.getText().toString();
        myUser.setMobilePhoneNumber(phone);
        myUser.setPassword(password);

        myUser.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    showToast("登录成功");
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                } else {
                    showToast("登录失败");
                    Log.e("AAA",e.toString());
                }
            }
        });
    }

    //使用验证码登录
    private void useCodeRegister() {
        showToast("使用验证码登录");
    }


    @Override
    protected void initData() {
        //判断是否处于登录状态
        BmobUser bmobUser=BmobUser.getCurrentUser();
        if(bmobUser !=null){
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            finish();
        } else {
            showToast("请先登录");
        }

        //设置背景透明度
        mBtnReg.getBackground().setAlpha(210);
        mEdtPhone.setHintTextColor(Color.WHITE);
        mEdtPass.setHintTextColor(Color.WHITE);

        mEdtPhone.setCompoundDrawables(drawable_phone(R.drawable.ic_myphone_left),null,null,null);
        mEdtPass.setCompoundDrawables(drawable_phone(R.drawable.ic_pass_left),null,null,null);

    }

    //设置EditText左边的图标
    private Drawable drawable_phone(int item) {
        Drawable drawable=getResources().getDrawable(item);
        drawable.setBounds(0,0,64,64);
        return drawable;
    }





}
