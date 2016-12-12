package com.tallty.smart_life_android.adapter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.CartUpdateItem;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kang on 2016/12/12.
 * 购物车 - 适配器
 */

public class CartAdapter extends BaseQuickAdapter<CartItem, BaseViewHolder>{
    public CartAdapter(int layoutResId, List<CartItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CartItem cartItem) {
        final int count = cartItem.getCount();
        final int position = baseViewHolder.getAdapterPosition();


        Glide.with(mContext).load(cartItem.getThumb()).into((ImageView) baseViewHolder.getView(R.id.cart_list_photo));
        baseViewHolder
            .setText(R.id.cart_list_name, cartItem.getName())
            .setText(R.id.cart_list_count, ""+count)
            .setText(R.id.cart_list_price, "￥ " + cartItem.getPrice())
            .setText(R.id.cart_list_count_price, "小计:￥ "+ formatTotalPrice(cartItem.getPrice()* count));

        // 单选
        final CheckBox checkBox = baseViewHolder.getView(R.id.cart_list_select_btn);
        checkBox.setChecked(cartItem.isChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setChecked(checkBox.isChecked());
                EventBus.getDefault().post(new CartUpdateItem(position, cartItem));
                Log.d(App.TAG, "你点击的是"+position);
                Log.d(App.TAG, "checkBox状态"+checkBox.isChecked());
            }
        });
        // 数量操作
        Button addBtn = baseViewHolder.getView(R.id.cart_list_add);
        Button reduceBtn = baseViewHolder.getView(R.id.cart_list_reduce);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setCount(count+1);
                EventBus.getDefault().post(new CartUpdateItem(position, cartItem));
            }
        });
        reduceBtn.setOnClickListener(new View.OnClickListener() {
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
     * format total price
     */
    private String formatTotalPrice(float total) {
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        //format 返回的是字符串
        return decimalFormat.format(total);
    }
}
