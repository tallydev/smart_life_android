package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyOrdersAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心-我的订单
 */
public class MyOrders extends BaseBackFragment {
    private RecyclerView recyclerView;
    private MyOrdersAdapter adapter;
    // 数据
    private List<Order> orders = new ArrayList<>();

    public static MyOrders newInstance() {
        Bundle args = new Bundle();

        MyOrders fragment = new MyOrders();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_orders;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("我的订单");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.my_orders_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        // TODO: 2016/11/30 获取我的订单
        // 加载列表
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyOrdersAdapter(context, orders);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
