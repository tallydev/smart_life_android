package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.GlideCircleTransform;
import com.tallty.smart_life_android.utils.DpUtil;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/26.
 * 我的-账户管理
 */

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileViewHolder>{
    private Context context;
    private ArrayList<String> keys;
    private ArrayList<String> values;
    // cell类型
    private static final int SHOW_PHOTO = 0;
    private static final int SHOW_NORMAL = 1;

    public ProfileListAdapter(Context context, ArrayList<String> keys, ArrayList<String> values) {
        this.context = context;
        this.keys = keys;
        this.values = values;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SHOW_NORMAL) {
            return new ProfileViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_profile, parent, false), SHOW_NORMAL);
        } else if (viewType == SHOW_PHOTO) {
            return new ProfileViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_profile_photo, parent, false), SHOW_PHOTO);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.key.setText(keys.get(position));
        if (holder.viewType == SHOW_NORMAL) {
            holder.value.setText(values.get(position));
            if (keys.get(position).equals("收货地址")) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px(context, 40)
                );
                params.setMargins(0,0,0,DpUtil.dip2px(context, 4));
                holder.layout.setLayoutParams(params);
                holder.line.setVisibility(View.INVISIBLE);
            } else if (keys.get(position).equals("设置支付密码")) {
                holder.line.setVisibility(View.INVISIBLE);
            }
        } else if (holder.viewType == SHOW_PHOTO) {
            Glide.with(context).load(values.get(position))
                    .transform(new GlideCircleTransform(context)).into(holder.photo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return SHOW_PHOTO;
        } else {
            return SHOW_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layout;
        private TextView key;
        private TextView value;
        private ImageView photo;
        private View line;

        private int viewType;

        public ProfileViewHolder(View itemView, int viewType) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_profile_layout);
            key = (TextView) itemView.findViewById(R.id.item_profile_key);
            this.viewType = viewType;
            if (viewType == SHOW_NORMAL) {
                value = (TextView) itemView.findViewById(R.id.item_profile_value);
                line = itemView.findViewById(R.id.profile_line);
            } else if (viewType == SHOW_PHOTO) {
                photo = (ImageView) itemView.findViewById(R.id.item_profile_photo);
            }
        }
    }
}
