package com.example.administrator.myclass.Utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by Administrator on 2017/9/27.
 */

public class countDownUtil {

    //60秒倒计时
    public static void countDawnTime(final Button btnCode) {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnCode.setText(String.valueOf(millisUntilFinished/1000));
                btnCode.setClickable(false);
            }

            @Override
            public void onFinish() {
                btnCode.setText("获取验证码");
                btnCode.setClickable(true);
            }
        }.start();


    }
}
