package com.tallty.smart_life_android.event;

/**
 * Created by kang on 16/7/22.
 * 事件: 购物车-选择商品
 */

public class CartCheckBox {
    public Boolean isChecked;
    public Boolean isCheckedAll;
    public float item_total_price;

    public CartCheckBox(Boolean isChecked, float item_total_price, Boolean isCheckedAll){
        this.isChecked = isChecked;
        this.item_total_price = item_total_price;
        this.isCheckedAll = isCheckedAll;
    }
}
