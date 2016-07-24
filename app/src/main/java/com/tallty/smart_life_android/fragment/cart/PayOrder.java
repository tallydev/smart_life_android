package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Address;
import com.tallty.smart_life_android.model.Commodity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayOrder extends BaseBackFragment {
    private String mName;
    // 结算数据
    private ArrayList<Commodity> selected_commodities = new ArrayList<>();
    private float total_price = 0.0f;
    private Address order_address = new Address();

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView order_price_text;

    public static PayOrder newInstance(String title, float total_price,
                                       ArrayList<Commodity> selected_commodities,
                                       Address order_address) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        args.putFloat(TOTAL_PRICE, total_price);
        args.putSerializable(OBJECTS, selected_commodities);
        args.putSerializable(ADDRESS, order_address);
        PayOrder fragment = new PayOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
            total_price = args.getFloat(TOTAL_PRICE);
            selected_commodities = (ArrayList<Commodity>) args.getSerializable(OBJECTS);
            order_address = (Address) args.getSerializable(ADDRESS);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_pay_order;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        order_price_text = getViewById(R.id.order_price);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);

        order_price_text.setText("RMB "+total_price);
    }

    @Override
    public void onClick(View v) {

    }
}
