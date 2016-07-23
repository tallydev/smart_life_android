package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.model.Commodity;
import com.tallty.smart_life_android.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/20.
 * 购物车-适配器
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartListViewHolder>{
    private Context context;
    private ArrayList<Commodity> commodities;

    public CartListAdapter(Context context, ArrayList<Commodity> commodities){
        this.context = context;
        this.commodities = commodities;
    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CartListViewHolder holder, int position) {
        Commodity commodity = commodities.get(position);
        holder.check_box.setChecked(commodity.isChecked());
        Glide.with(context).load(commodity.getPhoto_id()).into(holder.photo);
        holder.name.setText(commodity.getName());
        holder.count.setText(""+commodity.getCount());
        holder.price.setText("￥ " + commodity.getPrice());
        holder.count_price.setText("小计:￥ "+ commodity.getPrice()*commodity.getCount());
        // 设置监听
        setButtonListener(holder, position, commodity);
    }

    @Override
    public int getItemCount() {
        return commodities.size();
    }

    /**
     * item 监听事件
     * @param holder
     * @param position
     * @param commodity
     */
    private void setButtonListener(final CartListViewHolder holder, final int position, final Commodity commodity) {
        final int count = commodity.getCount();
        // CheckBox
        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commodity.setChecked(holder.check_box.isChecked());
                EventBus.getDefault().post(new CartUpdateItem(position, commodity));
            }
        });
        // 加
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commodity.setCount(count+1);
                EventBus.getDefault().post(new CartUpdateItem(position, commodity));
            }
        });
        // 减
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1){
                    commodity.setCount(count-1);
                    EventBus.getDefault().post(new CartUpdateItem(position, commodity));
                }else{
                    ToastUtil.show("真的不能再少啦");
                }
            }
        });
    }


    /**
     * ViewHolder
     */
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
