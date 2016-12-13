package com.tallty.smart_life_android.adapter;

import android.content.Context;
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
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/20.
 * 购物车-适配器
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartListViewHolder>{
    private Context context;
    private List<CartItem> commodities;
    private String tag;

    // 默认,购物车列表构造方法
    public CartListAdapter(Context context, ArrayList<CartItem> commodities){
        this.context = context;
        this.commodities = commodities;
    }
    // 提交订单列表构造方法
    public CartListAdapter(Context context, ArrayList<CartItem> commodities, String tag){
        this.context = context;
        this.commodities = commodities;
        this.tag = tag;
    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CartListViewHolder holder, int position) {
        CartItem cartItem = commodities.get(position);
        // Global
        Glide.with(context).load(cartItem.getThumb()).into(holder.photo);
        holder.name.setText(cartItem.getName());
        holder.price.setText("￥ " + cartItem.getPrice());

        // Custom
        if ("提交订单".equals(tag)){
            holder.check_box.setChecked(true);
            holder.check_box.setClickable(false);
            holder.add.setVisibility(View.INVISIBLE);
            holder.reduce.setVisibility(View.INVISIBLE);
            holder.count_price.setVisibility(View.GONE);
            holder.order_item_count.setVisibility(View.VISIBLE);
            holder.order_item_count.setText("x "+ cartItem.getCount());
        } else{
            holder.check_box.setChecked(cartItem.isChecked());
            holder.count.setText(""+ cartItem.getCount());
            float total = cartItem.getPrice()* cartItem.getCount();
            holder.count_price.setText("小计:￥ "+ formatTotalPrice(total));
            // 设置监听
            setButtonListener(holder, position, cartItem);
        }
    }

    /**
     * format total price
     */
    private String formatTotalPrice(float total) {
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        //format 返回的是字符串
        return decimalFormat.format(total);
    }

    @Override
    public void onBindViewHolder(CartListViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }else{
            CartItem cartItem = (CartItem) payloads.get(0);
            holder.check_box.setChecked(cartItem.isChecked());
            holder.count.setText(""+ cartItem.getCount());
            holder.count_price.setText("小计:￥ "+ cartItem.getPrice()* cartItem.getCount());
            // 设置监听
            setButtonListener(holder, position, cartItem);
        }
    }

    @Override
    public int getItemCount() {
        return commodities.size();
    }

    /**
     * item 监听事件
     * @param holder
     * @param position
     * @param cartItem
     */
    private void setButtonListener(final CartListViewHolder holder, final int position, final CartItem cartItem) {
        final int count = cartItem.getCount();
        // CheckBox
        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setChecked(holder.check_box.isChecked());
                EventBus.getDefault().post(new CartUpdateItem(position, cartItem));
            }
        });
        // 加
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setCount(count+1);
                EventBus.getDefault().post(new CartUpdateItem(position, cartItem));
            }
        });
        // 减
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1){
                    cartItem.setCount(count-1);
                    EventBus.getDefault().post(new CartUpdateItem(position, cartItem));
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
        // 提交订单
        private TextView order_item_count;

        CartListViewHolder(View itemView) {
            super(itemView);
            check_box = (CheckBox) itemView.findViewById(R.id.cart_list_select_btn);
            photo = (ImageView) itemView.findViewById(R.id.cart_item_photo);
            name = (TextView) itemView.findViewById(R.id.cart_item_name);
            add = (Button) itemView.findViewById(R.id.cart_list_add);
            reduce = (Button) itemView.findViewById(R.id.cart_list_reduce);
            count = (TextView) itemView.findViewById(R.id.cart_list_count);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            count_price = (TextView) itemView.findViewById(R.id.cart_item_total);

            order_item_count = (TextView) itemView.findViewById(R.id.order_item_count);
        }
    }
}
