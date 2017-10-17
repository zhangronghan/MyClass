package com.example.administrator.myclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.data.ClassGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 */

public class MyJoinRecyclerViewAdapter extends RecyclerView.Adapter<MyJoinViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<ClassGroup> mList;
    private OnItemClickListener mOnItemClickListener=null;

    public MyJoinRecyclerViewAdapter(Context mContext,List<ClassGroup> mList) {
        this.mContext=mContext;
        this.mList=mList;
    }

    @Override
    public MyJoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_join,parent,false);
        MyJoinViewHolder viewHolder=new MyJoinViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyJoinViewHolder holder, int position) {
        ClassGroup mGroup=mList.get(position);

        //获得图片路径并设置头像
        BaseFunction.getImageFromBmob(mGroup,holder.ivHeader);

        holder.tvTitle.setText(mGroup.getGroupName());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);

    }


    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }


    public void refreshData(List<ClassGroup> groupList) {
        mList=groupList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
