package com.example.administrator.myclass.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.data.AppraiseGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class MyAppraiseRecyclerViewAdapter extends RecyclerView.Adapter<MyAppraiseViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<AppraiseGroup> mList;
    private OnItemClickListener mOnItemClickListener=null;

    public MyAppraiseRecyclerViewAdapter(Context context, List<AppraiseGroup> mList) {
        this.mContext = context;
        this.mList=mList;
    }

    @Override
    public MyAppraiseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_appraise,parent,false);
        view.setOnClickListener(this);
        MyAppraiseViewHolder holder=new MyAppraiseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAppraiseViewHolder holder, int position) {
        AppraiseGroup appraiseGroup=mList.get(position);

        holder.tvFirstWord.setText(appraiseGroup.getAppraiseTitle().substring(0,1));
        holder.tvTitle.setText(appraiseGroup.getAppraiseTitle());
        holder.itemView.setTag(position);

        GradientDrawable myGrad= (GradientDrawable) holder.tvFirstWord.getBackground();
        myGrad.setColor(BaseFunction.getFirstWordColor());

    }

    @Override
    public int getItemCount() {
        return mList==null ? 0:mList.size();
    }

    public void refresh(List<AppraiseGroup> appraiseGroupList) {
        mList=appraiseGroupList;
        notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

}
