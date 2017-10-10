package com.example.administrator.myclass.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.administrator.myclass.Base.BaseFragment;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.GlideImageLoader;
import com.example.administrator.myclass.View.MyCodeDialog;
import com.example.administrator.myclass.adapter.MyGridViewAdapter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/3.
 */

//轮播图可参考魅族的
public class MyMainFragment extends BaseFragment {
    private GridView mGridView;
    private Banner mBanner;
    private List<String> mImageList=new ArrayList<>();
    private MyGridViewAdapter mGridViewAdapter;
    private ImageView ivCode;
    private int[] icon={R.drawable.ic_item_inform,R.drawable.ic_item_data,
            R.drawable.ic_item_appraise,R.drawable.ic_item_poll,
            R.drawable.ic_item_photo,R.drawable.ic_item_check};
    private String[] iconName={"通知","资料","评优","活动投票","班级相册","查看考勤"};


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews() {
        mBanner= (Banner) super.rootView.findViewById(R.id.banner);
        mGridView= (GridView) super.rootView.findViewById(R.id.gv_gridView);
        ivCode= (ImageView) super.rootView.findViewById(R.id.iv_code);

    }

    @Override
    protected void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showToast("通知");
                        break;

                    case 1:
                        showToast("资料");
                        break;

                    case 2:
                        showToast("评优");
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
                Bitmap bitmap=encodeAsBitmap("zhang张");  //这里是获取图片Bitmap，也可以传入其他参数到Dialog中
                MyCodeDialog.Builder dialogBuild = new MyCodeDialog.Builder(getContext());
                dialogBuild.setImage(bitmap);
                MyCodeDialog dialog = dialogBuild.create();
                dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                dialog.show();


            }
        });


    }


    @Override
    protected void initData() {
        mImageList.add("http://pic39.nipic.com/20140312/18085061_092729513131_2.jpg");
        mImageList.add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
        mImageList.add("http://pic27.nipic.com/20130319/10415779_103704478000_2.jpg");

        mBanner.setImages(mImageList).setImageLoader(new GlideImageLoader()).start();


        mGridViewAdapter =new MyGridViewAdapter(getContext(),icon,iconName);
        mGridView.setAdapter(mGridViewAdapter);

    }

    /**
     * 生成二维码
     * */
    private Bitmap encodeAsBitmap(String str) {
        Bitmap bitmap=null;
        BitMatrix result=null;
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try {
            result=multiFormatWriter.encode(str, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            bitmap=barcodeEncoder.createBitmap(result);

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }




}
