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
import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.CartCheckBox;
import com.tallty.smart_life_android.event.CartUpdateCount;
import com.tallty.smart_life_android.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/20.
 * 购物车-适配器
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartListViewHolder>{
    private Context context;
    private ArrayList<Integer> photo_urls;
    private ArrayList<String> names;
    private ArrayList<Integer> counts;
    private ArrayList<Float> prices;

    private ArrayList<Boolean> select_items = new ArrayList<>();
    private ArrayList<Float> item_totals = new ArrayList<>();


    public CartListAdapter(Context context, ArrayList<Integer> photo_urls,
                           ArrayList<String> names, ArrayList<Integer> counts, ArrayList<Float> prices) {
        this.context = context;
        this.photo_urls = photo_urls;
        this.names = names;
        this.counts = counts;
        this.prices = prices;
        // 购物车初始状态: 都不选中
        for(int i = 0; i < names.size(); i++){
            this.select_items.add(false);
        }

    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CartListViewHolder holder, int position) {
        // 初始总价格
        item_totals.add(position, prices.get(position) * counts.get(position));
        // 商品信息
        /**
         * setChecked()方法会触发OnCheckedChangeListener
         */
        holder.check_box.setChecked(select_items.get(position));
        Glide.with(context).load(photo_urls.get(position)).into(holder.photo);
        holder.name.setText(names.get(position));
        holder.count.setText(String.valueOf(counts.get(position)));
        holder.price.setText("￥ " + String.valueOf(prices.get(position)));
        holder.count_price.setText("小计:￥ "+String.valueOf(item_totals.get(position)));
        // CheckBox 点击事件
        setHolderCheckBoxListener(holder, position);
        // 加减按钮点击事件
        setHolderCountListener(holder, position);
        Logger.d("走了普通的onBindViewHolder");
    }

    @Override
    public void onBindViewHolder(CartListViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty()){
            // 当点击数量操作时, 只更新itemView视图的 totalPrice , count
            // 不更新CheckBox
            item_totals.set(position, prices.get(position) * counts.get(position));
            holder.count.setText(String.valueOf(counts.get(position)));
            holder.count_price.setText("小计:￥ "+String.valueOf(item_totals.get(position)));
            Logger.d("走了payloads的onBindViewHolder");
        }else{
            super.onBindViewHolder(holder, position, payloads);
        }
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
        int position;
        for (position = 0; position < select_items.size(); position++){
            select_items.set(position, true);
        }
        notifyDataSetChanged();
    }

    // 全不选
    public void unSelectAll(){
        int position;
        for (position = 0; position < select_items.size(); position++){
            select_items.set(position, false);
        }
        notifyDataSetChanged();
    }

    /**
     * 事件: CartCheckBox
     * 触发: 改变商品选中状态时触发
     * 说明: 商品选中状态变化时, 以【小计】为处理单位
     * @params isChecked  【商品是否被选中】
     * @params item_total_price  【商品价格小计】
     * @params isCheckedAll  【所有商品是否都被选中】
     */
    private void setHolderCheckBoxListener(final CartListViewHolder holder, final int position) {
        holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 改变CheckBox状态
                select_items.set(position, isChecked);
                // 逻辑
                String info = isChecked ? "选中商品"+names.get(position) : "取消选中商品"+names.get(position);
                Log.d("=====>", info+" && 发送商品小计: "+String.valueOf(item_totals.get(position)));

                if(select_items.contains(false)){
                    EventBus.getDefault().post(new CartCheckBox(isChecked, item_totals.get(position), false));
                }else{
                    EventBus.getDefault().post(new CartCheckBox(isChecked, item_totals.get(position), true));
                }
            }
        });
    }

    /**
     * 事件: CartUpdateCount
     * 条件: 商品选中时
     * 触发: 数量修改时触发
     * 说明: 商品数量变化,以【单价】为处理单位
     * @params price  【商品单价】
     * @params isAdd  【加/减】
     */
    private void setHolderCountListener(final CartListViewHolder holder, final int position) {
        // 数量增加
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_items.get(position)) {
                    Log.d("增加选中商品数量--&&--", "发送商品单价: "+String.valueOf(prices.get(position)));
                    EventBus.getDefault().post(new CartUpdateCount(prices.get(position), true));
                }
                Log.d("增加选中商品数量---->", String.valueOf(prices.get(position)));
                counts.set(position, counts.get(position) + 1);
                /**
                 * 记录易错点: 当调用 notifyItemChanged(position); 时, 会刷新item
                 * CheckBox的数据也会变化, 会自动调用setOnClickedChangeListener,
                 * 造成,点击【加】【减】,都会自动调用一次CheckBox点击事件,
                 * 使用CheckBox的setOnClickListener则不受影响,但【全选】改变CheckBox状态,却又不能触发点击
                 * 解决方案:(终于解决了)
                 * *使用: notifyItemChanged(int position, Object payload)方法来更新item局部视图
                 * *避免更新item全部视图,造成setChecked被调用,从而造成重复发送选择事件
                 *
                 */
                notifyItemChanged(position, counts.get(position));
            }
        });
        // 数量减少
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counts.get(position) > 1) {
                    if (select_items.get(position)) {
                        Log.d("减少选中商品数量--&&--", "发送商品单价: "+String.valueOf(prices.get(position)));
                        EventBus.getDefault().post(new CartUpdateCount(prices.get(position), false));
                    }
                    Log.d("减少选中商品数量------>", String.valueOf(prices.get(position)));
                    counts.set(position, counts.get(position) - 1);
                    notifyItemChanged(position, counts.get(position));
                } else {
                    ToastUtil.show("已经不能再少了哦");
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
