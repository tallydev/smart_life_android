package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Contact;

/**
 * Created by kang on 16/7/25.
 * 收货地址-选择地址事件
 */

public class SelectAddress {
    private Contact contact;
    private int position;

    public SelectAddress(int position, Contact contact) {
        this.contact = contact;
        this.position = position;
    }

    public Contact getContact() {
        return contact;
    }

    public int getPosition() {
        return position;
    }
}
