package com.example.administrator.myclass.activity;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;

/**
 * Created by Administrator on 2017/9/12.
 */

public class GuideActivity extends BaseActivity {
    private ViewFlipper mViewFlipper;
    private GestureDetector mGestureDetector;
    private GestureDetectorListener mGestureDetectorListener;
    private android.widget.ImageView mIvImage1;
    private android.widget.ImageView mIvImage3;
    private Button btnNext;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews() {
        mViewFlipper= (ViewFlipper) findViewById(R.id.viewFlipper);
        mIvImage1 = (ImageView) findViewById(R.id.iv_image1);
        mIvImage3 = (ImageView) findViewById(R.id.iv_image3);
        btnNext= (Button) findViewById(R.id.btn_next);
        mGestureDetectorListener=new GestureDetectorListener();
        mGestureDetector=new GestureDetector(this,mGestureDetectorListener);
    }

    @Override
    protected void initListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() > e2.getX()){
                if(mViewFlipper.getCurrentView() != mIvImage3){
                    mViewFlipper.showNext();
                }
                return true;
            }
            if(e1.getX() < e2.getX()){
                if(mViewFlipper.getCurrentView() != mIvImage1){
                    mViewFlipper.showPrevious();
                }
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }



}
