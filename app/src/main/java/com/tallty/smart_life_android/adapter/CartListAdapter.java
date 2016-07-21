package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.annotation.BoolRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/20.
 * 购物车-适配器
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartListViewHolder> implements View.OnClickListener{
    private Context context;
    private ArrayList<Integer> photo_urls;
    private ArrayList<Boolean> checked;
    private ArrayList<String> names;
    private ArrayList<Integer> counts;
    private ArrayList<Float> prices;

    public CartListAdapter(Context context, ArrayList<Boolean> checked, ArrayList<Integer> photo_urls,
                           ArrayList<String> names, ArrayList<Integer> counts, ArrayList<Float> prices) {
        this.context = context;
        this.checked = checked;
        this.photo_urls = photo_urls;
        this.names = names;
        this.counts = counts;
        this.prices = prices;

    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CartListViewHolder holder, int position) {
        holder.check_box.setChecked(checked.get(position));
        Glide.with(context).load(photo_urls.get(position)).into(holder.photo);
        holder.name.setText(names.get(position));
        holder.add.setOnClickListener(this);
        holder.reduce.setOnClickListener(this);
        holder.count.setText(String.valueOf(counts.get(position)));
        holder.price.setText("￥ "+String.valueOf(prices.get(position)));

        float total = prices.get(position) * counts.get(position);
        holder.count_price.setText("小计:￥ "+String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    // 删除item
    public void removeItem(int position){
        photo_urls.remove(position);
        names.remove(position);
        counts.remove(position);
        prices.remove(position);
        notifyItemRemoved(position);
    }

    // 全选
    public void selectAll(){
        for (int i = 0; i < checked.size(); i++){
            checked.set(i, true);
        }
        notifyDataSetChanged();
    }

    // 全不选
    public void unSelectAll(){
        for (int i = 0; i < checked.size(); i++){
            checked.set(i, false);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    class CartListViewHolder extends RecyclerView.ViewHolder {
        private CheckBox check_box;
        private ImageView photo;
        private TextView name;
        private Button add;
        private Button reduce;
        private TextView count;
        private TextView price;
        private TextView count_price;

        CartListViewHolder(View itemView) {
            super(itemView);
            check_box = (CheckBox) itemView.findViewById(R.id.select_btn);
            photo = (ImageView) itemView.findViewById(R.id.cart_list_photo);
            name = (TextView) itemView.findViewById(R.id.cart_list_name);
            add = (Button) itemView.findViewById(R.id.cart_list_add);
            reduce = (Button) itemView.findViewById(R.id.cart_list_reduce);
            count = (TextView) itemView.findViewById(R.id.cart_list_count);
            price = (TextView) itemView.findViewById(R.id.cart_list_price);
            count_price = (TextView) itemView.findViewById(R.id.cart_list_count_price);
        }
    }
}
