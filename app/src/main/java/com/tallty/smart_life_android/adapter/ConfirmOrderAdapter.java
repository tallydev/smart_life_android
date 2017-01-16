package com.tallty.smart_life_android.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.utils.ArithUtils;
import com.tallty.smart_life_android.utils.GlobalUtils;

import java.util.List;

/**
 * Created by kang on 16/7/20.
 * 购物车-适配器
 */

public class ConfirmOrderAdapter extends BaseQuickAdapter<CartItem, BaseViewHolder> {
    public ConfirmOrderAdapter(int layoutResId, List<CartItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CartItem cartItem) {
        final int count = cartItem.getCount();
        CheckBox checkBox = baseViewHolder.getView(R.id.cart_item_check_box);
        checkBox.setChecked(true);
        checkBox.setClickable(false);
        baseViewHolder
            .setText(R.id.cart_item_name, cartItem.getName())
            .setVisible(R.id.cart_item_count, false)
            .setVisible(R.id.cart_item_add, false)
            .setVisible(R.id.cart_item_reduce, false)
            .setText(R.id.cart_item_price, "￥ " + cartItem.getPrice())
            .setVisible(R.id.order_item_count, true)
            .setText(R.id.order_item_count, "x " + count)
            .setText(R.id.cart_item_total, "小计:￥ "+ ArithUtils.round(cartItem.getPrice() * count));
        Glide
            .with(mContext)
            .load(cartItem.getThumb())
            .error(R.drawable.image_error)
            .placeholder(R.drawable.image_placeholder)
            .into((ImageView) baseViewHolder.getView(R.id.cart_item_photo));
    }
}
