package com.tallty.smart_life_android.fragment.cart;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.ConfirmOrderAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 购物车-确认订单
 */
public class ConfirmOrder extends BaseBackFragment {
    // 数据
    private ArrayList<CartItem> selected_cart_items = new ArrayList<>();
    private float total_price = 0.0f;
    private Contact order_contact = new Contact();
    private ArrayList<Contact> contacts = new ArrayList<>();
    private static final String EMPTY_ADDRESS = "请选择收货地址";
    private boolean has_address = false;

    private TextView submit_btn;
    private RelativeLayout link_to_addresses;
    private TextView order_address_text;
    private RecyclerView recyclerView;
    private TextView total_price_text;

    public static ConfirmOrder newInstance(ArrayList<CartItem> selected_commodities, float total_price) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT_List, selected_commodities);
        args.putFloat(Const.TOTAL_PRICE, total_price);
        ConfirmOrder fragment = new ConfirmOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            selected_cart_items = (ArrayList<CartItem>) args.getSerializable(Const.OBJECT_List);
            total_price = args.getFloat(Const.TOTAL_PRICE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_confirm_order;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("确认订单");
    }

    @Override
    protected void initView() {
        submit_btn = getViewById(R.id.submit_order);
        link_to_addresses = getViewById(R.id.link_to_addresses);
        order_address_text = getViewById(R.id.order_contact);
        total_price_text = getViewById(R.id.total_price);
        recyclerView = getViewById(R.id.submit_order_list);
    }

    @Override
    protected void setListener() {
        submit_btn.setOnClickListener(this);
        link_to_addresses.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        // 合计
        total_price_text.setText("￥ "+total_price);
        // 列表
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        ConfirmOrderAdapter adapter = new ConfirmOrderAdapter(R.layout.item_cart_list, selected_cart_items);
        recyclerView.setAdapter(adapter);
        // 获取默认地址
        getDefaultAddress();
    }


    private void getDefaultAddress() {
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getContacts().enqueue(new Callback<ContactList>() {
            @Override
            public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    contacts.clear();
                    contacts.addAll(response.body().getContacts());
                    // 整理数据
                    int cache_contact_id = sharedPre.getInt(Const.CONTACT_ID, 0);
                    for (int i = 0; i < contacts.size(); i++)  {
                        if (contacts.get(i).getId() == cache_contact_id) {
                            order_contact = contacts.get(i);
                            contacts.get(i).setChecked(true);
                            setOrderAddress(order_contact);
                            break;
                        } else {
                            if (contacts.get(i).isDefault()) {
                                order_contact = contacts.get(i);
                                contacts.get(i).setChecked(true);
                                setOrderAddress(order_contact);
                                break;
                            }
                        }
                    }
                } else {
                    order_address_text.setText(EMPTY_ADDRESS);
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<ContactList> call, Throwable t) {
                hideProgress();
                order_address_text.setText(EMPTY_ADDRESS);
                showToast(showString(R.string.network_error));
            }
        });
    }

    // 设置订单地址
    private void setOrderAddress(Contact contact) {
        has_address = true;
        order_address_text.setText(
            contact.getArea() +
            contact.getStreet() +
            contact.getCommunity() +
            contact.getAddress()
        );
    }

    // 缓存已选地址
    private void cacheAddress(Contact contact) {
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putInt(Const.CONTACT_ID, contact.getId());
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link_to_addresses:
                startForResult(SelectAddress.newInstance(contacts), REQ_CODE);
                break;
            case R.id.submit_order:
                if (has_address) {
                    start(PayOrder.newInstance(total_price, selected_cart_items, order_contact));
                } else {
                    showToast(EMPTY_ADDRESS);
                }
                break;
        }
    }

    @Override
    protected void onFragmentPop() {
        super.onFragmentPop();
    }

    /**
     * startForResult:
     * 目标Fragment调用setFragmentResult()后，在其出栈时，会回调该方法
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            order_contact = (Contact) data.getSerializable(Const.OBJECT);
            if (order_contact != null) {
                setOrderAddress(order_contact);
                cacheAddress(order_contact);
            }
        }
    }
}
