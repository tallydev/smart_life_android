package com.tallty.smart_life_android.adapter;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Order;

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
    protected void convert(BaseViewHolder baseViewHolder, Order order) {
        baseViewHolder
                .setText(R.id.order_number, "订单编号：" + order.getSeq())
                .setText(R.id.order_time, "下单时间：" + order.getTime())
                .setText(R.id.order_state, order.getStateAlias())
                .setText(R.id.order_pay_way, order.getPayWay())
                .setText(R.id.order_price, "￥ " + order.getPrice());

        RecyclerView cartItems = baseViewHolder.getView(R.id.order_commodity_list);
        // 加载订单的商品列表
        cartItems.setAdapter(new MyOrdersCommodityAdapter(R.layout.item_my_orders_commodity, order.getCartItems()));
    }
}