package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Commodity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 我的订单-商品列表
 */

public class MyOrdersCommodityAdapter extends RecyclerView.Adapter<MyOrdersCommodityAdapter.OrderCommodityViewHolder>{
    private Context context;
    private List<Commodity> commodities = new ArrayList<>();

    public MyOrdersCommodityAdapter(Context context, List<Commodity> commodities) {
        this.context = context;
        this.commodities = commodities;
    }

    @Override
    public OrderCommodityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderCommodityViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_my_orders_commodity, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderCommodityViewHolder holder, int position) {
        Commodity commodity = commodities.get(position);
        Glide.with(context).load(commodity.getPhoto_id()).skipMemoryCache(true).into(holder.image);
        holder.name.setText(commodity.getName());
        holder.price.setText("￥ "+commodity.getPrice());
        holder.count.setText("x "+commodity.getCount());
    }

    @Override
    public int getItemCount() {
        return commodities.size();
    }

    class OrderCommodityViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView price;
        private TextView count;

        OrderCommodityViewHolder(View itemView) {
            super(itemView);
                image = (ImageView) itemView.findViewById(R.id.order_commodity_image);
                name = (TextView) itemView.findViewById(R.id.order_commodity_name);
                price = (TextView) itemView.findViewById(R.id.order_commodity_price);
                count = (TextView) itemView.findViewById(R.id.order_commodity_count);
        }
    }
}
