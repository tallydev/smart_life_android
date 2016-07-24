package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Address;

/**
 * Created by kang on 16/7/25.
 * 收货地址-设置默认地址事件
 */

public class SetDefaultAddress {
    private int position;
    private Address address;

    public SetDefaultAddress(int position, Address address){
        this.position = position;
        this.address = address;
    }

    public int getPosition() {
        return position;
    }

    public Address getAddress() {
        return address;
    }
}
