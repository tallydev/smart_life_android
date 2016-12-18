package com.tallty.smart_life_android.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.CartItem;

import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 我的订单-商品列表
 */

public class MyOrdersCommodityAdapter extends BaseQuickAdapter<CartItem, BaseViewHolder>{

    public MyOrdersCommodityAdapter(int layoutResId, List<CartItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CartItem cartItem) {
        Glide.with(mContext).load(cartItem.getThumb())
                .into((ImageView) baseViewHolder.getView(R.id.order_commodity_image));
        String delete_str = "￥ "+cartItem.getOriginalPrice();
        SpannableString spannableString = new SpannableString(delete_str);
        spannableString.setSpan(new StrikethroughSpan(), 0, delete_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        baseViewHolder
                .setText(R.id.order_commodity_name, cartItem.getName())
                .setText(R.id.order_commodity_price, "￥ "+ cartItem.getPrice())
                .setText(R.id.order_commodity_count, "x "+ cartItem.getCount())
                .setText(R.id.order_commodity_original_price, spannableString)
                .setVisible(R.id.order_commodity_original_price, cartItem.getPrice() != cartItem.getOriginalPrice());
    }
}
