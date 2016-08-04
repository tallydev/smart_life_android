package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.CartItem;

/**
 * Created by kang on 16/7/23.
 * 购物车-更新指定Item
 */

public class CartUpdateItem {
    public int position;
    public CartItem cartItem;

    public CartUpdateItem(int position, CartItem cartItem){
        this.position = position;
        this.cartItem = cartItem;
    }
}
