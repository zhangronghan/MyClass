package com.example.administrator.myclass.fragment;

import com.example.administrator.myclass.Base.BaseFragment;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/3.
 */

//轮播图可参考魅族的
public class MyMainFragment extends BaseFragment {
    private Banner mBanner;
    private List<String> mImageList=new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews() {
        mBanner= (Banner) super.rootView.findViewById(R.id.banner);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mImageList.add("http://pic39.nipic.com/20140312/18085061_092729513131_2.jpg");
        mImageList.add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
        mImageList.add("http://pic27.nipic.com/20130319/10415779_103704478000_2.jpg");

        mBanner.setImages(mImageList).setImageLoader(new GlideImageLoader()).start();

    }





}
