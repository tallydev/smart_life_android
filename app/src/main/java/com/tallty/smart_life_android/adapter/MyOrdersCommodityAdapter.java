package com.tallty.smart_life_android.adapter;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.home.ProductShowFragment;
import com.tallty.smart_life_android.model.CartItem;

import org.greenrobot.eventbus.EventBus;

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
    protected void convert(BaseViewHolder baseViewHolder, final CartItem cartItem) {
        Glide.with(mContext)
                .load(cartItem.getThumb())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_error)
                .into((ImageView) baseViewHolder.getView(R.id.order_commodity_image));

        String delete_str = "￥ "+cartItem.getOriginalPrice();
        SpannableString spannableString = new SpannableString(delete_str);
        spannableString.setSpan(new StrikethroughSpan(), 0, delete_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if ("sale_off".equals(cartItem.getState())){
            baseViewHolder
                .setText(R.id.order_commodity_name, "商品已经下架")
                .setText(R.id.order_commodity_price, "")
                .setText(R.id.order_commodity_count, "")
                .setText(R.id.order_commodity_original_price, "")
                .setVisible(R.id.order_commodity_original_price, false);
        } else {
            baseViewHolder
                .setText(R.id.order_commodity_name, cartItem.getName())
                .setText(R.id.order_commodity_price, "￥ "+ cartItem.getPrice())
                .setText(R.id.order_commodity_count, "x "+ cartItem.getCount())
                .setText(R.id.order_commodity_original_price, spannableString)
                .setVisible(R.id.order_commodity_original_price, cartItem.getPrice() != cartItem.getOriginalPrice());
        }

        RelativeLayout productLayout = baseViewHolder.getView(R.id.order_product_layout);
        productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 没过期 ,可查看详情
                if (!"sale_off".equals(cartItem.getState())) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("product_id", cartItem.getProductId());
                    EventBus.getDefault().post(new TransferDataEvent(bundle, "order_product"));
                }
            }
        });

    }
}
