package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Activity;

import java.util.List;

/**
 * Created by kang on 2016/11/23.
 * 首页 - 社区活动 - 适配器
 */

public class CommunityActivityAdapter extends BaseQuickAdapter<Activity, BaseViewHolder> {

    public CommunityActivityAdapter(int layoutResId, List<Activity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Activity activity) {
        ImageView imageView = baseViewHolder.getView(R.id.community_activity_item_image);
        imageView.setMaxHeight(4000);

        Glide.with(mContext)
            .load(activity.getImage())
            .crossFade()
            .into(imageView);
    }
}
