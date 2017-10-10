package com.example.administrator.myclass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myclass.R;

/**
 * Created by Administrator on 2017/10/6.
 */

public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private int[] icon;
    private String[] iconName;

    public MyGridViewAdapter(Context mContext, int[] icon, String[] iconName) {
        this.mContext=mContext;
        this.icon=icon;
        this.iconName=iconName;
    }

    @Override
    public int getCount() {
        return icon.length;
    }

    @Override
    public Object getItem(int position) {
        return icon[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            View view= LayoutInflater.from(mContext).inflate(R.layout.gridview_item,parent,false);
            ImageView image= (ImageView) view.findViewById(R.id.grideview_item_image);
            TextView name= (TextView) view.findViewById(R.id.grideview_item_text);

            image.setImageResource(icon[position]);
            name.setText(iconName[position]);

            return view;
        }

        return convertView;
    }



}
