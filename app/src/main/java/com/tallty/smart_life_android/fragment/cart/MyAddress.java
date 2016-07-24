package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.AddressListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Address;

import java.util.ArrayList;

/**
 * 购物车-确认订单-收货地址
 */
public class MyAddress extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView new_address_text;
    private RecyclerView recyclerView;
    private AddressListAdapter adapter;
    // 临时data
    private boolean checkeds[] = {true, false};
    private String names[] = {"Stark", "Mark"};
    private String phones[] = {"15216666666", "15217777777"};
    private String areas[] = {"XX区", "XX区"};
    private String details[] = {"XX街道XX (小区名称) XX栋XX单元XXX室", "XX街道XX (小区名称) XX栋XX单元XXX室"};
    private boolean default_address[] = {true, false};
    // 实例列表
    private ArrayList<Address> addresses = new ArrayList<>();

    public static MyAddress newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        MyAddress fragment = new MyAddress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_address;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        new_address_text = getViewById(R.id.new_address);
        recyclerView = getViewById(R.id.address_list);
    }

    @Override
    protected void setListener() {
        new_address_text.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        // 载入列表
        processList();
    }

    private void processList() {
        for (int i = 0; i < names.length; i++){
            Address address = new Address();
            address.setChecked(checkeds[i]);
            address.setName(names[i]);
            address.setPhone(phones[i]);
            address.setArea(areas[i]);
            address.setDetail(details[i]);
            address.setDefaultAddress(default_address[i]);
            addresses.add(address);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AddressListAdapter(context, addresses);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_address:
                break;
        }
    }
}
