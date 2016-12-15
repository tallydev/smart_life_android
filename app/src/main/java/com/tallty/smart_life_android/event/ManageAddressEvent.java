package com.tallty.smart_life_android.event;

import com.tallty.smart_life_android.model.Contact;

/**
 * Created by kang on 16/7/25.
 * 地址管理实践
 */

public class ManageAddressEvent {
    private int position;
    private Contact contact;
    private String action;

    public ManageAddressEvent(int position, Contact contact, String action) {
        this.position = position;
        this.contact = contact;
        this.action = action;
    }

    public int getPosition() {
        return position;
    }

    public Contact getContact() {
        return contact;
    }

    public String getAction() {
        return action;
    }
}
