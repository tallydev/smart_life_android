package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.CartListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.model.Address;
import com.tallty.smart_life_android.model.Commodity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 购物车-确认订单
 */
public class ConfirmOrder extends BaseBackFragment {
    private String mName;
    // 数据
    private ArrayList<Commodity> selected_commodities = new ArrayList<>();
    private float total_price = 0.0f;
    private Address order_address = new Address();

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView submit_btn;
    private RelativeLayout link_to_addresses;
    private TextView order_address_text;
    private RecyclerView recyclerView;
    private TextView total_price_text;

    public static ConfirmOrder newInstance(String title, ArrayList<Commodity> selected_commodities, float total_price) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        args.putSerializable(OBJECTS, selected_commodities);
        args.putFloat(TOTAL_PRICE, total_price);
        ConfirmOrder fragment = new ConfirmOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
            selected_commodities = (ArrayList<Commodity>) args.getSerializable(OBJECTS);
            total_price = args.getFloat(TOTAL_PRICE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_confirm_order;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        submit_btn = getViewById(R.id.submit_order);
        link_to_addresses = getViewById(R.id.link_to_addresses);
        order_address_text = getViewById(R.id.order_address);
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
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        // 合计
        total_price_text.setText("￥ "+total_price);
        // 设置订单地址
        setDefaultAddress();
        // 列表
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CartListAdapter adapter = new CartListAdapter(context, selected_commodities, "提交订单");
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    // 设置订单地址, 获取SharedPreferences保存的默认地址
    private void setDefaultAddress(){
        String area = sharedPre.getString(ADDRESS_AREA, EMPTY_STRING);
        String address_detail = sharedPre.getString(ADDRESS_DETAIL, EMPTY_STRING);
        String name = sharedPre.getString(ADDRESS_NAME, EMPTY_STRING);
        String phone = sharedPre.getString(ADDRESS_PHONE, EMPTY_STRING);

        if (EMPTY_STRING.equals(area) && EMPTY_STRING.equals(address_detail)){
            order_address_text.setText("选择收货地址");
        }else{
            // 保存到对象
            order_address.setName(name);
            order_address.setPhone(phone);
            order_address.setArea(area);
            order_address.setDetail(address_detail);
            // 显示
            order_address_text.setText(area+address_detail);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link_to_addresses:
                startForResult(MyAddress.newInstance("收货地址"), REQ_CODE);
                break;
            case R.id.submit_order:
                EventBus.getDefault().post(new StartBrotherEvent(PayOrder.newInstance("支付订单", total_price)));
                break;
        }
    }

    /**
     * startForResult:
     * 目标Fragment调用setFragmentResult()后，在其出栈时，会回调该方法
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_YES) {
            order_address = (Address) data.getSerializable(ADDRESS);
            // 显示
            if (order_address != null){
                order_address_text.setText(order_address.getArea()+order_address.getDetail());
            }
        }
    }
}
