package com.example.administrator.myclass.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseFragment;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.Utils.GlideImageLoader;
import com.example.administrator.myclass.View.ImageViewPlus;
import com.example.administrator.myclass.View.MyCodeDialog;
import com.example.administrator.myclass.activity.AppraiseActivity;
import com.example.administrator.myclass.adapter.MyGridViewAdapter;
import com.example.administrator.myclass.data.ClassGroup;
import com.example.administrator.myclass.data.MyUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/10/3.
 */

//轮播图可参考魅族的
public class MyMainFragment extends BaseFragment {
    private GridView mGridView;
    private Banner mBanner;
    private ImageViewPlus mIvGroupHeader;
    private TextView mTvGroupName;
    private List<String> mImageList = new ArrayList<>();
    private MyGridViewAdapter mGridViewAdapter;
    private ImageView ivCode;
    private int[] icon = {R.drawable.ic_item_inform, R.drawable.ic_item_data,
            R.drawable.ic_item_appraise, R.drawable.ic_item_poll,
            R.drawable.ic_item_photo, R.drawable.ic_item_check};
    private String[] iconName = {"通知", "资料", "评优", "活动投票", "班级相册", "查看考勤"};
    private String groupId;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews() {
        mBanner = (Banner) super.rootView.findViewById(R.id.banner);
        mGridView = (GridView) super.rootView.findViewById(R.id.gv_gridView);
        ivCode = (ImageView) super.rootView.findViewById(R.id.iv_code);
        mIvGroupHeader = (ImageViewPlus) super.rootView.findViewById(R.id.iv_groupHeader);
        mTvGroupName = (TextView) super.rootView.findViewById(R.id.tv_groupName);

    }

    @Override
    protected void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showToast("通知");
                        break;

                    case 1:
                        showToast("资料");
                        break;

                    case 2:
                        startActivity(new Intent(getActivity(),AppraiseActivity.class));
                        break;

                    case 3:
                        showToast("活动投票");
                        break;

                    case 4:
                        showToast("班级相册");
                        break;

                    case 5:
                        showToast("查看考勤");
                        break;

                    default:
                        break;

                }
            }
        });

        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(groupId)){
                    showToast("请先加入班级");
                } else {
                    Bitmap bitmap = encodeAsBitmap(groupId);  //这里是获取图片Bitmap，也可以传入其他参数到Dialog中
                    MyCodeDialog.Builder dialogBuild = new MyCodeDialog.Builder(getContext());
                    dialogBuild.setImage(bitmap);
                    MyCodeDialog dialog = dialogBuild.create();
                    dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                    dialog.show();
                }

            }
        });


    }


    @Override
    protected void initData() {
        mImageList.add("http://pic39.nipic.com/20140312/18085061_092729513131_2.jpg");
        mImageList.add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
        mImageList.add("http://pic27.nipic.com/20130319/10415779_103704478000_2.jpg");

        mBanner.setImages(mImageList).setImageLoader(new GlideImageLoader()).start();

        mGridViewAdapter = new MyGridViewAdapter(getContext(), icon, iconName);
        mGridView.setAdapter(mGridViewAdapter);

        BmobUser bmobUser = BmobUser.getCurrentUser();
        BmobQuery<MyUser> userQuery = new BmobQuery<>();
        final BmobQuery<ClassGroup> query = new BmobQuery<>();
        Log.e("AAA", "zhang" + bmobUser.getObjectId());
        //先读取MyUser表，得到Groupid
        userQuery.getObject(bmobUser.getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {

                    //由Groupid得到班级头像和班级名
                    query.getObject(myUser.getGroupId(), new QueryListener<ClassGroup>() {
                        @Override
                        public void done(ClassGroup classGroup, BmobException e) {
                            if (e == null) {
                                BaseFunction.getImageFromBmob(classGroup, mIvGroupHeader);
                                mTvGroupName.setText(classGroup.getGroupName());
                                groupId = classGroup.getObjectId();
                            } else {
                                Log.e("AAA", "MyUser:" + e.getMessage());
                            }
                        }
                    });

                } else {
                    Log.e("AAA", "11" + e.getMessage());
                }
            }
        });


    }

    /**
     * 生成二维码
     */
    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;

    }


}
