package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.SetDefaultAddress;
import com.tallty.smart_life_android.model.Contact;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by kang on 16/7/24.
 * 收货地址-适配器
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder>{
    private Context context;
    private List<Contact> contacts;

    public AddressListAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public AddressListAdapter.AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, final int position) {
        final Contact contact = contacts.get(position);

        holder.name.setText(contact.getName());
        holder.address.setText(
                contact.getArea() +
                contact.getStreet() +
                contact.getCommunity() +
                contact.getAddress()
        );
        holder.phone.setText(contact.getPhone());

        if (contact.isDefault()){
            holder.isDefault.setText("默认地址");
        }else{
            holder.isDefault.setText("设为默认地址");
            holder.isDefault.setTextColor(ContextCompat.getColor(context, R.color.global_text));
            holder.isDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置默认地址操作
                    EventBus.getDefault().post(new SetDefaultAddress(position, contact));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private TextView phone;
        private TextView isDefault;

        public AddressViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_address_name);
            address = (TextView) itemView.findViewById(R.id.item_address_detail);
            phone = (TextView) itemView.findViewById(R.id.item_address_phone);
            isDefault = (TextView) itemView.findViewById(R.id.item_address_default);
        }
    }
}
