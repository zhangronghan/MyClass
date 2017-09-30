package com.example.administrator.myclass.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.countDownUtil;
import com.example.administrator.myclass.data.MyUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/9/13.
 */

public class LoginActivity extends BaseActivity {
    private ImageView mIvBack;
    private EditText mEdtPhone;
    private EditText mEdtCode;
    private Button mBtnCode;
    private EditText mEdtPass;
    private EditText mEdtConfirmPass;
    private Button mBtnLogin;
    private Switch mSwitch;
    private EditText mEdtUserName;

    //是否为管理员 默认为false
    private boolean isManager=false;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEdtPhone = (EditText) findViewById(R.id.edt_phone);
        mEdtCode = (EditText) findViewById(R.id.edt_code);
        mBtnCode = (Button) findViewById(R.id.btn_code);
        mEdtPass = (EditText) findViewById(R.id.edt_pass);
        mEdtConfirmPass = (EditText) findViewById(R.id.edt_confirmPass);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mSwitch= (Switch) findViewById(R.id.my_Switch);
        mEdtUserName= (EditText) findViewById(R.id.edt_username);

    }

    @Override
    protected void initListener() {
        //点击注册按钮
        LoginSituation();

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //倒计时
                countDownUtil.countDawnTime(mBtnCode);

            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isManager=true;
                } else {
                    isManager=false;
                }

            }
        });



    }

    @Override
    protected void initData() {
        mEdtPhone.setHintTextColor(Color.WHITE);
        mEdtCode.setHintTextColor(Color.WHITE);
        mEdtPass.setHintTextColor(Color.WHITE);
        mEdtConfirmPass.setHintTextColor(Color.WHITE);
        mEdtUserName.setHintTextColor(Color.WHITE);


        mEdtPhone.setCompoundDrawables(drawable_phone(R.drawable.ic_myphone_left),null,null,null);
        mEdtPass.setCompoundDrawables(drawable_phone(R.drawable.ic_pass_left),null,null,null);
        mEdtConfirmPass.setCompoundDrawables(drawable_phone(R.drawable.ic_pass_left),null,null,null);
        mEdtCode.setCompoundDrawables(drawable_phone(R.drawable.ic_code_left),null,null,null);
        mEdtUserName.setCompoundDrawables(drawable_phone(R.drawable.ic_user_left),null,null,null);
    }
    //设置EditText左边的图标
    private Drawable drawable_phone(int item) {
        Drawable drawable=getResources().getDrawable(item);
        drawable.setBounds(0,0,64,64);
        return drawable;
    }


    private void LoginSituation() {

            mBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone=mEdtPhone.getText().toString();
                    String code=mEdtCode.getText().toString();
                    String password=mEdtPass.getText().toString();
                    String confirmPassword=mEdtConfirmPass.getText().toString();
                    String username=mEdtUserName.getText().toString();

                    //弹出圆形进度条
                    final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("注册中.....");

                    progressDialog.show();

                    Pattern pattern=Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                    Matcher matcher=pattern.matcher(phone);
                    boolean isMatchSuccess =matcher.matches();

                    if(phone.length() == 0 || password.length() == 0){
                        progressDialog.cancel();
                        showToast("手机号或者密码不能为空");
                    } else if(username.length() == 0){
                        progressDialog.cancel();
                        showToast("昵称不能为空");
                    } else if(code.length() == 0){
                        progressDialog.cancel();
                        showToast("请输入验证码");
                    } else if(confirmPassword.length() == 0){
                        progressDialog.cancel();
                        showToast("请输入确认密码");
                    } else if(!password.equals(confirmPassword)){
                        progressDialog.cancel();
                        showToast("确认密码不一致");
                    } else if(!isMatchSuccess){
                        progressDialog.cancel();
                        showToast("请输入正确的手机号");
                    } else {
                        MyUser myUser=new MyUser();
                        myUser.setUsername(username);
                        myUser.setMobilePhoneNumber(phone);
                        myUser.setPassword(password);
                        myUser.setStudent(!isManager);
                        myUser.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if(e==null){
                                    progressDialog.cancel();
                                    showToast("注册成功");
                                    //返回registerActivity
                                    finish();
                                } else {
                                    progressDialog.cancel();
                                    showToast("注册失败,该用户可能已经存在");
                                    Log.e("AAA",e.toString());
                                }
                            }
                        });

                    }

                }
            });
        }
    }




