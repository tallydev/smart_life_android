package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.NewsSort;
import com.tallty.smart_life_android.utils.DpUtil;

import java.util.List;

/**
 * Created by kang on 2017/3/14.
 * 政府直通车分类适配器
 */

public class NewsSortAdapter extends BaseQuickAdapter<NewsSort, BaseViewHolder> {

    public NewsSortAdapter(int layoutResId, List<NewsSort> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsSort governmentSort) {
        ImageView imageView = baseViewHolder.getView(R.id.image_list_item_image);
        if (governmentSort.getImage().isEmpty()) {
            imageView.setMaxHeight(DpUtil.dip2px(mContext, 160));
        } else {
            imageView.setMaxHeight(4000);
        }

        Glide.with(mContext)
                .load(governmentSort.getImage())
                .error(R.drawable.image_error)
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);
    }
}
