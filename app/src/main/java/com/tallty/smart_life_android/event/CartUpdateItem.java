package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Commodity;

/**
 * Created by kang on 16/7/23.
 * 购物车-更新指定Item
 */

public class CartUpdateItem {
    public int position;
    public Commodity commodity;

    public CartUpdateItem(int position, Commodity commodity){
        this.position = position;
        this.commodity = commodity;
    }
}
