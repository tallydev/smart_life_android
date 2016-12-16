package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyOrdersAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Order;
import com.tallty.smart_life_android.model.Orders;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-我的订单
 */
public class MyOrders extends BaseBackFragment {
    private RecyclerView recyclerView;
    private MyOrdersAdapter adapter;
    // 数据
    private List<Order> orders = new ArrayList<>();
    private int current_page = 1;
    private int total_pages = 1;

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
        initList();
        getOrders();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new MyOrdersAdapter(R.layout.item_my_orders ,orders);
        recyclerView.setAdapter(adapter);
    }

    private void getOrders() {
        showProgress("正在加载...");
        Engine.authService(shared_token, shared_phone).getOrders(1, 10).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    orders.clear();
                    orders.addAll(response.body().getOrders());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("加载失败");
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                hideProgress();
                showToast("网络连接错误");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
