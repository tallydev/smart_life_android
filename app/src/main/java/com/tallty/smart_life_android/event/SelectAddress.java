package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Address;

/**
 * Created by kang on 16/7/25.
 * 收货地址-选择地址事件
 */

public class SelectAddress {
    private Address address;
    private int position;

    public SelectAddress(int position, Address address) {
        this.address = address;
        this.position = position;
    }

    public Address getAddress() {
        return address;
    }

    public int getPosition() {
        return position;
    }
}
