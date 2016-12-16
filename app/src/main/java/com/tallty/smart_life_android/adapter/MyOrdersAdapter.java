package com.tallty.smart_life_android.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ManageOrderEvent;
import com.tallty.smart_life_android.model.Order;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 账户管理-我的订单
 */

public class MyOrdersAdapter extends BaseQuickAdapter<Order, BaseViewHolder>{

    public MyOrdersAdapter(int layoutResId, List<Order> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Order order) {
        final int position = baseViewHolder.getAdapterPosition();
        RecyclerView cartItems = baseViewHolder.getView(R.id.order_commodity_list);
        Button pay_button = baseViewHolder.getView(R.id.order_pay_button);
        Button cancel_button = baseViewHolder.getView(R.id.order_cancel_button);
        Button delete_button = baseViewHolder.getView(R.id.order_delete_button);
        Button logistics_button = baseViewHolder.getView(R.id.order_logistics_button);
        TextView price_and_postage = baseViewHolder.getView(R.id.order_price_and_postage);
        // 显示
        cartItems.setAdapter(new MyOrdersCommodityAdapter(R.layout.item_my_orders_commodity, order.getCartItems()));
        baseViewHolder
                .setText(R.id.order_number, "订单编号：" + order.getSeq())
                .setText(R.id.order_time, "下单时间：" + order.getTime())
                .setText(R.id.order_state, order.getStateAlias())
                .setText(R.id.order_pay_way, order.getPayWay());

        // 富文本显示订单金额
        int price_length = String.valueOf(order.getPrice()).length();
        SpannableString spannableString = new SpannableString("合计：￥ " + order.getPrice() + " (含运费￥" + order.getPostage() + ")");
        spannableString.setSpan(new AbsoluteSizeSpan(16, true), 5, 5 + price_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.orange)), 5, 5 + price_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        price_and_postage.setText(spannableString);

        // 显示按钮
        switch (order.getState()) {
            case "unpaid":
                pay_button.setVisibility(View.VISIBLE);
                cancel_button.setVisibility(View.VISIBLE);
                break;
            case "canceled":
                cancel_button.setVisibility(View.VISIBLE);
                break;
            case "paid":
                break;
        }

        // 事件处理
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageOrderEvent(position, order, Const.PAY_ORDER));
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageOrderEvent(position, order, Const.CANCEL_ORDER));
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageOrderEvent(position, order, Const.DELETE_ORDER));
            }
        });
        logistics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageOrderEvent(position, order, Const.LOGISTICS_ORDER));
            }
        });
    }
}