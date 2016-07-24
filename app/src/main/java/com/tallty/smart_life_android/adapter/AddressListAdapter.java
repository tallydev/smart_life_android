package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.home.LimitSailShow;
import com.tallty.smart_life_android.model.Address;
import com.tallty.smart_life_android.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.tallty.smart_life_android.R.id.contact_service;
import static com.tallty.smart_life_android.R.id.line;

/**
 * Created by kang on 16/7/24.
 * 收货地址-适配器
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder>{
    private Context context;
    private ArrayList<Address> addresses;

    public AddressListAdapter(Context context, ArrayList<Address> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public AddressListAdapter.AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, final int position) {
        Address address = addresses.get(position);
        holder.checkBox.setChecked(address.isChecked());
        holder.name.setText(address.getName());
        holder.address.setText(address.getArea()+address.getDetail());
        holder.phone.setText(address.getPhone());
        if (address.isDefaultAddress()){
            holder.isDefault.setText("默认地址");
        }else{
            holder.isDefault.setText("设为默认地址");
            holder.isDefault.setTextColor(context.getResources().getColor(R.color.global_text));
            holder.isDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置默认地址操作
                    ToastUtil.show("设置默认地址->"+position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView name;
        private TextView address;
        private TextView phone;
        private TextView isDefault;

        public AddressViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_address_check_box);
            name = (TextView) itemView.findViewById(R.id.item_address_name);
            address = (TextView) itemView.findViewById(R.id.item_address_detail);
            phone = (TextView) itemView.findViewById(R.id.item_address_phone);
            isDefault = (TextView) itemView.findViewById(R.id.item_address_default);
        }
    }
}
