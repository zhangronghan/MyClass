package com.example.administrator.myclass.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.myclass.Listener.OnAgreeClickListener;
import com.example.administrator.myclass.Listener.OnItemClickListener;
import com.example.administrator.myclass.R;
import com.example.administrator.myclass.Utils.BaseFunction;
import com.example.administrator.myclass.Utils.GlideCircleTransform;
import com.example.administrator.myclass.data.ActivityVote;
import com.example.administrator.myclass.data.ActivityVoteGroup;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MyVoteDetailAdapter extends RecyclerView.Adapter<VoteDetailViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<ActivityVote> mActivityVoteList;
    private OnItemClickListener mOnItemClickListener;
    private OnAgreeClickListener mAgreeClickListener;
    private ActivityVoteGroup activityVoteGroup;
    private int total;

    public MyVoteDetailAdapter(Context context, List<ActivityVote> mActivityVoteList, OnAgreeClickListener mAgreeClickListener, ActivityVoteGroup activityVoteGroup) {
        this.mContext = context;
        this.mActivityVoteList = mActivityVoteList;
        this.mAgreeClickListener = mAgreeClickListener;
        this.activityVoteGroup = activityVoteGroup;
    }

    @Override
    public VoteDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vote_detail, parent, false);
        VoteDetailViewHolder viewHolder = new VoteDetailViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VoteDetailViewHolder holder, final int position) {
        ActivityVote activityVote = mActivityVoteList.get(position);
        getImageFromBmob(activityVote, holder.mIvImageheader);
        holder.mTvWriter.setText(activityVote.getJoinActivityName());
        holder.mTvTitle.setText(activityVote.getJoinActivityTitle());
        holder.itemView.setTag(position);

        int num = activityVote.getAgreeNum();
        holder.mTvAgreeNum.setText(num + " 票");

        total = activityVoteGroup.getVoteNum();
        float percent = (float) num / total;
        holder.mProgressBar.setProgress((int) (percent * 100));
        Log.e("WWW", "NUM:" + num);

        holder.mIvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAgreeClickListener.OnItemAgree(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mActivityVoteList == null ? 0 : mActivityVoteList.size();
    }

    public void refresh(List<ActivityVote> activityVoteList) {
        mActivityVoteList = activityVoteList;
        notifyDataSetChanged();
    }

    public void refresh(List<ActivityVote> list, int i) {
        mActivityVoteList = list;
        this.total = i;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    private void getImageFromBmob(ActivityVote activityVote, final ImageView ivImageheader) {
        final String imageName = BaseFunction.getImageNameToDate(activityVote.getImageHeader());

        BmobFile bmobFile = new BmobFile(imageName, "", activityVote.getImageHeader());
        File file = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());

        bmobFile.download(file, new DownloadFileListener() {
            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    /*Bitmap bitmap = BitmapFactory.decodeFile(savePath);
                    ivImageheader.setImageBitmap(bitmap);*/

                    Glide.with(mContext).load(savePath)
                            .transform(new GlideCircleTransform(mContext))
                            .into(ivImageheader);

                    Log.e("AAA", "下载成功" + savePath);
                } else {
                    Log.e("AAA", "下载失败" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });

    }


}
