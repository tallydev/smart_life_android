package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Alarm;

import java.util.List;

/**
 * Created by kang on 2017/3/15.
 * 电子猫眼适配器
 */

public class AlarmsAdapter extends BaseQuickAdapter<Alarm, BaseViewHolder> {

    public AlarmsAdapter(int layoutResId, List<Alarm> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Alarm alarm) {
        baseViewHolder
                .setVisible(R.id.alarm_dot, alarm.isUnread())
                .setText(R.id.alarm_title, alarm.getTitle())
                .setText(R.id.alarm_time, alarm.getTime());

        String imageUrl = alarm.getImages().get(0) == null ? "" : alarm.getImages().get(0).get("url");
        Glide
            .with(mContext)
            .load(imageUrl)
            .error(R.drawable.image_error)
            .placeholder(R.drawable.image_placeholder)
            .into((ImageView) baseViewHolder.getView(R.id.alarm_thumb));
    }
}
