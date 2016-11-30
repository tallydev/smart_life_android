package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Activity;

import java.util.ArrayList;

/**
 * Created by kang on 2016/11/23.
 * 首页 - 社区活动 - 适配器
 */

public class CommunityActivityAdapter extends RecyclerView.Adapter<CommunityActivityAdapter.ActivityViewHolder> {
    private ArrayList<Activity> activities;
    private Context context;

    public CommunityActivityAdapter(ArrayList<Activity> activities, Context context) {
        this.activities = activities;
        this.context = context;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_community_activity, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        String url = activities.get(position).getImage();
        Glide.with(context).load(url).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        ActivityViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.community_activity_item_image);
        }
    }
}
