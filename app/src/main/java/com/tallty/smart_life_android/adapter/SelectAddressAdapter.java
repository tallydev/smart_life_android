package com.tallty.smart_life_android.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Contact;

import java.util.List;

/**
 * Created by kang on 2016/12/14.
 * 收货地址适配器
 */

public class SelectAddressAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {
    public SelectAddressAdapter(int layoutResId, List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Contact contact) {
        baseViewHolder
            .setText(R.id.item_address_name, contact.getName())
            .setText(R.id.item_address_phone, contact.getPhone())
            .setVisible(R.id.item_address_default_text, contact.isDefault())
            .setText(R.id.item_address_detail, contact.getArea() +
                    contact.getStreet() +
                    contact.getCommunity() +
                    contact.getAddress());
    }
}
