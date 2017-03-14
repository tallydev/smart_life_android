package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.News;

import java.util.List;

/**
 * Created by kang on 2017/3/14.
 * 新闻列表适配器
 */

public class NewsListAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    public NewsListAdapter(int layoutResId, List<News> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, News news) {
        baseViewHolder
                .setText(R.id.news_title, news.getTitle())
                .setText(R.id.news_time, news.getCreatedTime());
        Glide.with(mContext)
                .load(news.getNewsThumb())
                .error(R.drawable.image_error)
                .placeholder(R.drawable.image_placeholder)
                .into((ImageView) baseViewHolder.getView(R.id.news_thumb));
    }
}
