package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Address;
import com.tallty.smart_life_android.model.Commodity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * 购物车-支付订单
 */
public class PayOrder extends BaseBackFragment {
    // 结算数据
    private ArrayList<Commodity> selected_commodities = new ArrayList<>();
    private float total_price = 0.0f;
    private Address order_address = new Address();

    private TextView order_price_text;

    public static PayOrder newInstance(float total_price,
                                       ArrayList<Commodity> selected_commodities,
                                       Address order_address) {
        Bundle args = new Bundle();
        args.putFloat(Const.TOTAL_PRICE, total_price);
        args.putSerializable(Const.OBJECT_List, selected_commodities);
        args.putSerializable(Const.OBJECT, order_address);
        PayOrder fragment = new PayOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            total_price = args.getFloat(Const.TOTAL_PRICE);
            selected_commodities = (ArrayList<Commodity>) args.getSerializable(Const.OBJECT_List);
            order_address = (Address) args.getSerializable(Const.OBJECT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_pay_order;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("支付订单");
    }

    @Override
    protected void initView() {
        order_price_text = getViewById(R.id.order_price);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        order_price_text.setText("RMB "+total_price);
    }

    @Override
    public void onClick(View v) {

    }
}
