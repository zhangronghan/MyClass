package com.example.administrator.myclass.Base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/10/3.
 */

public abstract class BaseFragment extends Fragment {
    private View rootView;
    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView=inflater.inflate(getLayoutRes(),container,false);
        }

        initViews();
        initListener();
        initData();
        return rootView;
    }
    public abstract int getLayoutRes();

    protected abstract void initViews();

    protected abstract void initListener();

    protected abstract void initData();

    public void showToast(String msg){
        if(mToast == null){
            mToast=Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
