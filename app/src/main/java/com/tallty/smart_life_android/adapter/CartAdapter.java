package com.tallty.smart_life_android.adapter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.utils.ArithUtils;
import com.tallty.smart_life_android.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kang on 2016/12/12.
 * 购物车 - 适配器
 */

public class CartAdapter extends BaseQuickAdapter<CartItem, BaseViewHolder>{
    public CartAdapter(int layoutResId, List<CartItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CartItem cartItem) {
        // 库存小于已选数量, 显示库存数量
        final int count = cartItem.getCount() > cartItem.getStock() ? cartItem.getStock() : cartItem.getCount();
        final int position = baseViewHolder.getAdapterPosition();
        Button addBtn = baseViewHolder.getView(R.id.cart_item_add);
        Button reduceBtn = baseViewHolder.getView(R.id.cart_item_reduce);
        ImageView imageView = baseViewHolder.getView(R.id.cart_item_photo);
        final CheckBox checkBox = baseViewHolder.getView(R.id.cart_item_check_box);

        Glide.with(mContext).load(cartItem.getThumb()).into(imageView);
        baseViewHolder
            .setText(R.id.cart_item_name, cartItem.getName())
            .setText(R.id.cart_item_count, ""+count)
            .setText(R.id.cart_item_price, "￥ " + cartItem.getPrice())
            .setText(R.id.cart_item_total, "小计:￥ "+ ArithUtils.round(cartItem.getPrice()* count));

        // 单选
        checkBox.setChecked(cartItem.isChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setChecked(checkBox.isChecked());
                EventBus.getDefault().post(new CartUpdateItem(position, cartItem, "check"));
            }
        });
        // 数量操作
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count + 1 > cartItem.getStock()) {
                    ToastUtil.show("库存不足");
                } else {
                    cartItem.setCount(count+1);
                    EventBus.getDefault().post(new CartUpdateItem(position, cartItem, "add"));
                }
            }
        });
        reduceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1){
                    cartItem.setCount(count-1);
                    EventBus.getDefault().post(new CartUpdateItem(position, cartItem, "reduce"));
                }else{
                    ToastUtil.show("真的不能再少啦");
                }
            }
        });
        // 查看商品
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("product_id", cartItem.getProductId());
                EventBus.getDefault().post(new TransferDataEvent(bundle, "cart_product"));
            }
        });

        // 处理失效商品
        if ("sale_off".equals(cartItem.getState())) {
            checkBox.setClickable(false);
            checkBox.setChecked(false);
            addBtn.setVisibility(View.INVISIBLE);
            reduceBtn.setVisibility(View.INVISIBLE);
            baseViewHolder
                .setBackgroundColor(R.id.cart_item_layout, ContextCompat.getColor(mContext, R.color.item_gray_bg))
                .setVisible(R.id.cart_item_total, false)
                .setVisible(R.id.cart_item_count, false)
                .setText(R.id.cart_item_price, cartItem.getStateAlias());
        }

        // 处理库存不足商品
        if (cartItem.getStock() == 0) {
            checkBox.setClickable(false);
            checkBox.setChecked(false);
            addBtn.setVisibility(View.INVISIBLE);
            reduceBtn.setVisibility(View.INVISIBLE);
            baseViewHolder
                    .setBackgroundColor(R.id.cart_item_layout, ContextCompat.getColor(mContext, R.color.item_gray_bg))
                    .setVisible(R.id.cart_item_total, false)
                    .setVisible(R.id.cart_item_count, false)
                    .setText(R.id.cart_item_price, "库存不足");
        }
    }
}
