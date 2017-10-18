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
import com.example.administrator.myclass.data.ActivityVoteGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MyVoteAdapter extends RecyclerView.Adapter<VoteViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<ActivityVoteGroup> mList;
    private OnItemClickListener mOnItemClickListener=null;


    public MyVoteAdapter(Context context, List<ActivityVoteGroup> mList) {
        mContext = context;
        this.mList = mList;
    }

    @Override
    public VoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vote, parent, false);
        VoteViewHolder viewHolder = new VoteViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoteViewHolder holder, int position) {
        ActivityVoteGroup activityVoteGroup = mList.get(position);
        holder.mTvNum.setText(String.valueOf(position+1));
        holder.mTvTitle.setText(activityVoteGroup.getActivityTitle());
        holder.itemView.setTag(position);

        GradientDrawable myGrad = (GradientDrawable) holder.mTvNum.getBackground();
        myGrad.setColor(BaseFunction.getFirstWordColor());


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void refresh(List<ActivityVoteGroup> activityVoteGroupList) {
        mList=activityVoteGroupList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener !=null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }


}
