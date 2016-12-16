package com.tallty.smart_life_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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
        baseViewHolder
                .setText(R.id.order_number, "订单编号：" + order.getSeq())
                .setText(R.id.order_time, "下单时间：" + order.getTime())
                .setText(R.id.order_state, order.getStateAlias())
                .setText(R.id.order_price_and_postage, "￥ " + order.getPrice());


        RecyclerView cartItems = baseViewHolder.getView(R.id.order_commodity_list);
        // 加载订单的商品列表
        cartItems.setAdapter(new MyOrdersCommodityAdapter(R.layout.item_my_orders_commodity, order.getCartItems()));

        // 订单处理事件
        Button pay_button = baseViewHolder.getView(R.id.order_pay_button);
        Button common_button = baseViewHolder.getView(R.id.order_common_button);
        if ("unpaid".equals(order.getState())) {
            pay_button.setVisibility(View.GONE);
        }
        if ("已完成".equals(order.getState())) {
            common_button.setText("删除订单");
        }

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 支付订单
                EventBus.getDefault().post(new ManageOrderEvent(position, order, Const.PAY_ORDER));
            }
        });

        common_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消订单
                EventBus.getDefault().post(new ManageOrderEvent(position, order, Const.CANCEL_ORDER));
            }
        });
    }
}