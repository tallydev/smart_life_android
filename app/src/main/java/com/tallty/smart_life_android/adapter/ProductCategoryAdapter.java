package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Category;
import com.tallty.smart_life_android.utils.DpUtil;

import java.util.List;

/**
 * Created by kang on 2016/12/13.
 * 首页 - 精品超市分类 - 适配器
 */

public class ProductCategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public ProductCategoryAdapter(int layoutResId, List<Category> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Category category) {
        ImageView imageView = baseViewHolder.getView(R.id.image_list_item_image);
        if (category.getThumb().isEmpty()) {
            imageView.setMaxHeight(DpUtil.dip2px(mContext, 160));
        } else {
            imageView.setMaxHeight(4000);
        }

        Glide.with(mContext)
                .load(category.getThumb())
                .error(R.drawable.image_error)
                .into(imageView);
    }
}
