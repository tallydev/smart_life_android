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
import com.tallty.smart_life_android.model.Commodity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 购物车-确认订单
 */
public class ConfirmOrder extends BaseBackFragment {
    private String mName;
    private ArrayList<Commodity> selected_commodities;
    private float total_price;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView submit_btn;
    private RelativeLayout link_to_address;
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
        link_to_address = getViewById(R.id.link_to_address);
        total_price_text = getViewById(R.id.total_price);
        recyclerView = getViewById(R.id.submit_order_list);
    }

    @Override
    protected void setListener() {
        submit_btn.setOnClickListener(this);
        link_to_address.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);

        total_price_text.setText("￥ "+total_price);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CartListAdapter adapter = new CartListAdapter(context, selected_commodities, "提交订单");
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link_to_address:
                EventBus.getDefault().post(new StartBrotherEvent(MyAddress.newInstance("收货地址")));
                break;
            case R.id.submit_order:
                EventBus.getDefault().post(new StartBrotherEvent(PayOrder.newInstance("支付订单", total_price)));
                break;
        }
    }
}
