package com.example.administrator.myclass.fragment;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.example.administrator.myclass.Base.BaseFragment;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.SharePreUtil;
import com.example.administrator.myclass.View.MyToggleButton;

/**
 * Created by Administrator on 2017/10/3.
 */

public class MyInfoFragment extends BaseFragment {
    private MyToggleButton mToggleButton;
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initViews() {
        mToggleButton= (MyToggleButton) super.rootView.findViewById(R.id.toggleButton);


    }

    @Override
    protected void initListener() {
        mToggleButton.setOnToggleChanged(new MyToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                showToast(on+"");
                setNightMode(on);
            }
        });

    }

    @Override
    protected void initData() {
        mToggleButton.setToggleOn(false);


    }

    private void setNightMode(boolean on) {
        //获取当前模式
        int currentNightMode=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //将是否为夜间模式保存在SharedPreferences
        SharePreUtil.setNightMode(getContext(),currentNightMode == Configuration.UI_MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        boolean b=SharePreUtil.isNightMode(getContext());
        Log.e("VVV","111   "+b);
        SharePreUtil.setNightMode(getContext(),on);

        //startActivity(new Intent(getContext(),MyInfoFragment.class));
//        getActivity().overridePendingTransition(R.anim.animo_alph_close,R.anim.animo_alph_close);

    }


}
