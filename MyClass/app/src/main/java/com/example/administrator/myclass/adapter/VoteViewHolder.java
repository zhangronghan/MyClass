package com.example.administrator.myclass.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myclass.R;

/**
 * Created by Administrator on 2017/10/17.
 */

public class VoteViewHolder extends RecyclerView.ViewHolder{
    public TextView mTvNum;
    public TextView mTvTitle;

    public VoteViewHolder(View itemView) {
        super(itemView);

        mTvNum= (TextView) itemView.findViewById(R.id.tv_Num);
        mTvTitle= (TextView) itemView.findViewById(R.id.tv_title);
    }

}
