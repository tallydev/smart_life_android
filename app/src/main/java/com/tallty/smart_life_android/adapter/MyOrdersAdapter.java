package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 账户管理-我的订单
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder> {
    private Context context;
    private List<Order> orders = new ArrayList<>();

    public MyOrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public MyOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyOrdersViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_my_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(MyOrdersViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.number.setText("订单号："+order.getNumber());
        holder.time.setText(order.getTime());
        holder.state.setText(order.getState());
        holder.pay_way.setText(order.getPayWay());
        holder.price.setText("￥"+order.getPrice());
        // 加载商品列表
        holder.commodities.setAdapter(new MyOrdersCommodityAdapter(context, order.getCommodities()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView time;
        private TextView state;
        private TextView pay_way;
        private TextView price;
        private RecyclerView commodities;


        MyOrdersViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.order_number);
            time = (TextView) itemView.findViewById(R.id.order_time);
            state = (TextView) itemView.findViewById(R.id.order_state);
            pay_way = (TextView) itemView.findViewById(R.id.order_pay_way);
            price = (TextView) itemView.findViewById(R.id.order_price);
            commodities = (RecyclerView) itemView.findViewById(R.id.order_commodity_list);
        }
    }
}
