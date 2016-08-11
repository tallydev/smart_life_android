package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Contact;

/**
 * Created by kang on 16/7/25.
 * 收货地址-设置默认地址事件
 */

public class SetDefaultAddress {
    private int position;
    private Contact contact;

    public SetDefaultAddress(int position, Contact contact){
        this.position = position;
        this.contact = contact;
    }

    public int getPosition() {
        return position;
    }

    public Contact getContact() {
        return contact;
    }
}
