package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Product;

import java.util.List;

/**
 * Created by kang on 2016/12/7.
 * 首页 - 精品超市商品列表adapter
 */

public class ProductListAdapter extends BaseQuickAdapter<Product, BaseViewHolder>{

    public ProductListAdapter(int layoutResId, List<Product> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Product product) {
        baseViewHolder
            .setText(R.id.product_name, product.getTitle())
            .setText(R.id.product_price, "￥ "+product.getPrice());
        ImageView imageView = baseViewHolder.getView(R.id.product_photo);
        imageView.setMaxHeight(1000);
        Glide
            .with(mContext)
            .load(product.getThumb())
            .placeholder(R.drawable.product_placeholder)
            .error(R.drawable.image_error)
            .into(imageView);
    }
}
