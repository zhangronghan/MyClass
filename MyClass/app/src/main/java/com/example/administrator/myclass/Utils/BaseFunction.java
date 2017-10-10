package com.example.administrator.myclass.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/10/7.
 */

public class BaseFunction {

    /**
     * EditText获取焦点并显示软键盘
     * */
    public static void showSoftInputFromWindow(final Activity activity,EditText edtSpec) {
        edtSpec.setFocusable(true);
        edtSpec.setFocusableInTouchMode(true);
        edtSpec.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }


    public static ProgressDialog showProgressDialog(Context context, String s) {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(s);

        return progressDialog;
    }
}
