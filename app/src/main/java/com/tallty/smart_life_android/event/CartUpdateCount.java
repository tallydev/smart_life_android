package com.tallty.smart_life_android.event;

/**
 * Created by kang on 16/7/22.
 * 购物车-设置"合计总价"事件
 */

public class CartUpdateCount {
    public float price;
    public Boolean isAdd;

    public CartUpdateCount(float price, Boolean isAdd){
        this.price = price;
        this.isAdd = isAdd;
    }
}
