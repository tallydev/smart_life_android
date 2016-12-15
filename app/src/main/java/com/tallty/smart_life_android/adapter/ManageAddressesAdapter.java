package com.tallty.smart_life_android.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ManageAddressEvent;
import com.tallty.smart_life_android.model.Contact;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kang on 2016/12/14.
 * 管理收货地址适配器
 */

public class ManageAddressesAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {
    public ManageAddressesAdapter(int layoutResId, List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Contact contact) {
        baseViewHolder
                .setText(R.id.item_manage_address_name, contact.getName())
                .setText(R.id.item_manage_address_phone, contact.getPhone())
                .setChecked(R.id.item_manage_address_set_default, contact.isDefault())
                .setText(R.id.item_manage_address_detail, contact.getArea() +
                        contact.getStreet() +
                        contact.getCommunity() +
                        contact.getAddress());
        // 事件
        Button editBtn = baseViewHolder.getView(R.id.item_manage_address_edit);
        Button deleteBtn = baseViewHolder.getView(R.id.item_manage_address_delete);
        CheckBox setDefault = baseViewHolder.getView(R.id.item_manage_address_set_default);
        final int position = baseViewHolder.getAdapterPosition();
        // 删除
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageAddressEvent(position, contact, Const.DELETE_ADDRESS));
            }
        });
        // 编辑
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageAddressEvent(position, contact, Const.EDIT_ADDRESS));
            }
        });
        // 设置默认地址
        setDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ManageAddressEvent(position, contact, Const.SET_ADDRESS_DEFAULT));
            }
        });
    }
}
