package com.example.administrator.myclass.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.adapter.MyViewPagerFragmentAdapter;
import com.example.administrator.myclass.data.ClassGroup;
import com.example.administrator.myclass.data.MyIntentData;
import com.example.administrator.myclass.data.MyUser;
import com.example.administrator.myclass.fragment.MyFriendFragment;
import com.example.administrator.myclass.fragment.MyInfoFragment;
import com.example.administrator.myclass.fragment.MyMainFragment;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends BaseActivity {
    private static final int CODE_SCAN = 1001;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    List<Fragment> mFragmentList = new ArrayList<>();
    private MyViewPagerFragmentAdapter mViewPagerFragmentAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;

    }

    @Override
    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_radioGroup);

    }

    @Override
    protected void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main:
                        mViewPager.setCurrentItem(0);
                        break;

                    case R.id.rb_friend:
                        mViewPager.setCurrentItem(1);
                        break;

                    case R.id.rb_me:
                        mViewPager.setCurrentItem(2);
                        break;

                    default:
                        break;

                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.rb_main);
                        break;

                    case 1:
                        mRadioGroup.check(R.id.rb_friend);
                        break;

                    case 2:
                        mRadioGroup.check(R.id.rb_me);
                        break;

                    default:
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void initData() {
        setSupportActionBar(mToolbar);

        mFragmentList.add(new MyMainFragment());
        mFragmentList.add(new MyFriendFragment());
        mFragmentList.add(new MyInfoFragment());

        mViewPagerFragmentAdapter = new MyViewPagerFragmentAdapter(getSupportFragmentManager(), mFragmentList);

        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
    }

    //=========================(begin)Toolbar右上角弹出菜单==========================
    //创建右上角弹出菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_main_menu_right_top, menu);
        return true;
    }

    //右上角弹出菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popup_create:
                //创建班级
                startActivity(new Intent(MainActivity.this, CreateClassActivity.class));
                break;
            case R.id.popup_join:
                //加入班级
                startActivity(new Intent(MainActivity.this, JoinClassActivity.class));
                break;
            case R.id.popup_scan:
                //扫一扫
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), CODE_SCAN);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //=========================(end)Toolbar右上角弹出菜单==========================


    //重写onMenuOpened()方法，通过反射使 icon 和 title 同时可见。
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        return super.onMenuOpened(featureId, menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_SCAN) {
            if (data != null) {
                final String scanResult = data.getStringExtra(Intents.Scan.RESULT);

                BmobUser user = BmobUser.getCurrentUser();
                BmobQuery<MyUser> query = new BmobQuery<>();
                query.getObject(user.getObjectId(), new QueryListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            BmobQuery<ClassGroup> mGroup=new BmobQuery<ClassGroup>();
                            mGroup.getObject(scanResult, new QueryListener<ClassGroup>() {
                                @Override
                                public void done(ClassGroup classGroup, BmobException e) {
                                    if(e==null){
                                        Intent intent = new Intent(MainActivity.this, ConfirmJoinActivity.class);
                                        intent.putExtra(MyIntentData.CONFIRMJOIN, classGroup);
                                        startActivity(intent);

                                    } else {
                                        Log.e("AAA", e.getMessage());
                                    }
                                }
                            });

                        } else {
                            Log.e("AAA", e.getMessage());
                        }
                    }
                });

            } else {

            }
        }

    }
}
