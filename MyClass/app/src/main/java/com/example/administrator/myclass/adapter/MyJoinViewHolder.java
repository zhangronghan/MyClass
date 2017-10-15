package com.example.administrator.myclass.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myclass.R;

/**
 * Created by Administrator on 2017/10/12.
 */

class MyJoinViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivHeader;
    public TextView tvTitle;

    public MyJoinViewHolder(View itemView) {
        super(itemView);
        ivHeader= (ImageView) itemView.findViewById(R.id.iv_header);
        tvTitle= (TextView) itemView.findViewById(R.id.tv_title);
    }


}
