package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.GlideCircleTransform;
import com.tallty.smart_life_android.model.SportRankItem;

import java.util.List;

/**
 * Created by kang on 16/7/18.
 * 首页-健康达人-适配器
 */

public class SportRankAdapter extends BaseQuickAdapter<SportRankItem, BaseViewHolder>{
    public SportRankAdapter(int layoutResId, List<SportRankItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SportRankItem sportRankItem) {
        baseViewHolder
            .setText(R.id.rank_index, ""+sportRankItem.getIndex())
            .setText(R.id.rank_name, sportRankItem.getNickname())
            .setText(R.id.now_step, ""+sportRankItem.getCount());
        Glide
            .with(mContext)
            .load(sportRankItem.getAvatar())
            .placeholder(R.drawable.user_default)
            .transform(new GlideCircleTransform(mContext))
            .into((ImageView) baseViewHolder.getView(R.id.rank_photo));
    }
}
