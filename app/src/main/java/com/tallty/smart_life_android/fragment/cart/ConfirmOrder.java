package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.ConfirmOrderAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.Contact;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 购物车-确认订单
 */
public class ConfirmOrder extends BaseBackFragment {
    // 数据
    private ArrayList<CartItem> selected_cart_items = new ArrayList<>();
    private float total_price = 0.0f;
    private Contact order_contact = new Contact();

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
        // 设置订单地址
        setDefaultAddress();
        // 列表
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        ConfirmOrderAdapter adapter = new ConfirmOrderAdapter(R.layout.item_cart_list, selected_cart_items);
        recyclerView.setAdapter(adapter);
    }

    // 设置订单地址, 获取SharedPreferences保存的默认地址
    private void setDefaultAddress(){
        String area = sharedPre.getString(Const.CONTACT_AREA, Const.EMPTY_STRING);
        String street = sharedPre.getString(Const.CONTACT_STREET, Const.EMPTY_STRING);
        String community = sharedPre.getString(Const.CONTACT_COMMUNITY, Const.EMPTY_STRING);
        String address = sharedPre.getString(Const.CONTACT_ADDRESS, Const.EMPTY_STRING);
        String name = sharedPre.getString(Const.CONTACT_NAME, Const.EMPTY_STRING);
        String phone = sharedPre.getString(Const.CONTACT_PHONE, Const.EMPTY_STRING);

        if (Const.EMPTY_STRING.equals(name) && Const.EMPTY_STRING.equals(phone)){
            order_address_text.setText("选择收货地址");
        }else{
            // 保存到对象
            order_contact.setName(name);
            order_contact.setPhone(phone);
            order_contact.setArea(area);
            order_contact.setStreet(street);
            order_contact.setCommunity(community);
            order_contact.setAddress(address);
            // 显示
            order_address_text.setText(area+street+community+address);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link_to_addresses:
                startForResult(SelectAddress.newInstance(Const.FROM_ORDER), REQ_CODE);
                break;
            case R.id.submit_order:
                EventBus.getDefault().post(new StartBrotherEvent(PayOrder
                        .newInstance(total_price, selected_cart_items, order_contact)));
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
            // 显示
            if (order_contact != null){
                order_address_text.setText(
                    order_contact.getArea() +
                    order_contact.getStreet() +
                    order_contact.getCommunity() +
                    order_contact.getAddress()
                );
            }
        } else {
            // 当用户直接点击返回时
            // 获取SharedPreferences保存的默认地址
            setDefaultAddress();
        }
    }
}
