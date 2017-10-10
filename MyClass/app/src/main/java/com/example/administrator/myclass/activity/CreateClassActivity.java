package com.example.administrator.myclass.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.myclass.Base.BaseActivity;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.View.ImageViewPlus;
import com.example.administrator.myclass.data.ClassGroup;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/10/4.
 */

public class CreateClassActivity extends BaseActivity{
    private static final int GET_IMAGE = 1001;
    private ImageView ivBack;
    private TextView mTvCreateClass;
    private ImageViewPlus mIvHeader;
    private Spinner mSpinnerTeam;
    private EditText mEdtSpec;
    private Spinner mSpinnerYear;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] itemArray;
    private Bitmap bmp;

    //mSpinnerTeam 选中的值
    private int spinnerItem;

    //mSpinnerYear 选中的值
    private String spinnerYear;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_class;
    }

    @Override
    protected void initViews() {
        ivBack= (ImageView) findViewById(R.id.iv_back);
        mTvCreateClass = (TextView) findViewById(R.id.tv_createClass);
        mIvHeader = (ImageViewPlus) findViewById(R.id.iv_class_image);
        mSpinnerTeam = (Spinner) findViewById(R.id.spinner_team);
        mEdtSpec= (EditText) findViewById(R.id.edt_special);
        mSpinnerYear= (Spinner) findViewById(R.id.spinner_year);

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSpinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0){
                    spinnerItem=0;
                } else {
                    spinnerItem=Integer.valueOf(mSpinnerTeam.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //修改头像
        mIvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_IMAGE);
            }
        });


        mSpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0){
                    spinnerYear=null;
                } else {
                    spinnerYear=mSpinnerYear.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mTvCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String special=mEdtSpec.getText().toString();
                String year=spinnerYear.substring(2,4);

                BmobUser bmobUser=BmobUser.getCurrentUser();
                String username=bmobUser.getUsername();
                List<String> managerList=new ArrayList<String>();
                managerList.add(username);
                byte[] headerImage=bitmapTransformByte(bmp);

                if(spinnerItem != 0 && spinnerYear !=null){
                    ClassGroup myClassGroup=new ClassGroup();
                    myClassGroup.setGroupName(year + special + String.valueOf(spinnerItem)+"班");
                    myClassGroup.setManager(managerList);
                    myClassGroup.setImageHeader(headerImage);


                    final ProgressDialog progressDialog=BaseFunction.showProgressDialog(CreateClassActivity.this,"创建中....");
                    progressDialog.show();
                    myClassGroup.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){
                                progressDialog.cancel();
                                showToast("创建成功");
                                finish();
                            } else {
                                showToast("创建失败");
                                progressDialog.cancel();
                                Log.e("GROUP_SAVE",e.getMessage());
                            }

                        }
                    });
                    //showToast(year + special + String.valueOf(spinnerItem)+"班"+"  "+groupId);

                } else {
                    showToast("请填写完");
                }


            }
        });

    }



    @Override
    protected void initData() {
        BaseFunction.showSoftInputFromWindow(this,mEdtSpec);
        initSpinnerAdapter();



    }

    private byte[] bitmapTransformByte(Bitmap bmp) {
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,output);
        return output.toByteArray();
    }

    /**
     * 初始化mSpinnerYear里的Item
     * */
    private void initSpinnerAdapter() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        itemArray=new String[]{"选择入学年份",String.valueOf(year-2),String.valueOf(year-1),
                String.valueOf(year),String.valueOf(year+2),String.valueOf(year+2)};
        spinnerAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,itemArray);
        mSpinnerYear.setAdapter(spinnerAdapter);

    }

    /**
     * 判断EditText的内容是否为中文
     * 使用正则表达式
     *
     * @param special*/
    private boolean MatchEditText(String special) {
        Pattern pattern=Pattern.compile("/^[u4e00-u9fa5]+$/");
        Matcher matcher=pattern.matcher(special);
        return matcher.matches();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_IMAGE){
            if(data !=null){
                Uri uri=data.getData();
                ContentResolver cr=this.getContentResolver();
                try {
                    Bitmap bitmap= BitmapFactory.decodeStream(cr.openInputStream(uri));
                    bmp=bitmap;
                    mIvHeader.setImageBitmap(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }




    }


}
