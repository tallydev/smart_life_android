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
import com.tallty.smart_life_android.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 我的订单-商品列表
 */

public class MyOrdersCommodityAdapter extends RecyclerView.Adapter<MyOrdersCommodityAdapter.OrderCommodityViewHolder>{
    private Context context;
    private List<CartItem> commodities = new ArrayList<>();

    public MyOrdersCommodityAdapter(Context context, List<CartItem> commodities) {
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
        CartItem cartItem = commodities.get(position);
        // 调用接口时修改
//        Glide.with(context).load(cartItem.getThumb()).skipMemoryCache(true).into(holder.image);
        Glide.with(context).load(cartItem.getImage_id()).skipMemoryCache(true).into(holder.image);
        holder.name.setText(cartItem.getName());
        holder.price.setText("￥ "+ cartItem.getPrice());
        holder.count.setText("x "+ cartItem.getCount());
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
