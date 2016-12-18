package com.tallty.smart_life_android.fragment.me;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyOrdersAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ManageOrderEvent;
import com.tallty.smart_life_android.fragment.cart.PayOrder;
import com.tallty.smart_life_android.model.Order;
import com.tallty.smart_life_android.model.Orders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
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
    private String sort = "";
    // 数据
    private List<Order> orders = new ArrayList<>();
    private int current_page = 1;
    private int total_pages = 1;

    public static MyOrders newInstance(String sort) {
        Bundle args = new Bundle();
        args.putString(Const.STRING, sort);
        MyOrders fragment = new MyOrders();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            sort = args.getString(Const.STRING);
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
        EventBus.getDefault().register(this);
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
                    Collections.reverse(orders);
                    processOrderSort();
                } else {
                    showToast("加载失败");
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                hideProgress();
                showToast("网络连接错误");
                Log.d(App.TAG, t.getLocalizedMessage());
            }
        });
    }

    private void processOrderSort() {
        List<Order> cache = new ArrayList<>();

        switch (sort) {
            case "all":
                for (Order order : orders) {
                    if (!("canceled".equals(order.getState())))
                        cache.add(order);
                }
                orders.clear();
                orders.addAll(cache);
                break;
            case "unpaid":
                for (Order order : orders) {
                    if ("unpaid".equals(order.getState()))
                        cache.add(order);
                }
                orders.clear();
                orders.addAll(cache);
                break;
            case "untransport":
                for (Order order : orders) {
                    if ("paid".equals(order.getState()))
                        cache.add(order);
                }
                orders.clear();
                orders.addAll(cache);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 订单处理事件
     * @param event
     */
    @Subscribe
    public void onManageOrderEvent(final ManageOrderEvent event) {
        switch (event.getAction()) {
            case Const.PAY_ORDER:
                payOrder(event.getOrder(), event.getPosition());
                break;
            case Const.CANCEL_ORDER:
                confirmDialog("确认取消此订单吗？", new OnConfirmDialogListener() {
                    @Override
                    public void onConfirm(DialogInterface dialog, int which) {
                        deleteOrder(event.getOrder(), event.getPosition());
                    }

                    @Override
                    public void onCancel(DialogInterface dialog, int which) {

                    }
                });
                break;
            case Const.DELETE_ORDER:
                confirmDialog("确认删除此订单吗？", new OnConfirmDialogListener() {
                    @Override
                    public void onConfirm(DialogInterface dialog, int which) {
                        deleteOrder(event.getOrder(), event.getPosition());
                    }

                    @Override
                    public void onCancel(DialogInterface dialog, int which) {

                    }
                });
                break;
            case Const.SERVICE_ORDER:
                contactService(event.getOrder(), event.getPosition());
                break;
        }
    }

    // 取消订单 || 删除订单
    private void deleteOrder(final Order order, final int position) {
        showProgress("正在取消订单...");
        Engine.authService(shared_token, shared_phone).deleteOrder(order.getId())
            .enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        orders.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, orders.size());
                    } else {
                        showToast("取消订单失败");
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    hideProgress();
                    showToast("网络连接错误");
                }
            });
    }

    // 支付订单
    private void payOrder(Order order, int position) {
        start(PayOrder.newInstance(order));
    }

    // 联系客服
    private void contactService(Order order, int position) {
        PackageManager pm = _mActivity.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CALL_PHONE","com.tallty.smart_life_android"));
        if (permission) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"087164589208"));
            startActivity(intent);
        } else {
            showToast("应用无拨打电话权限,请设置应用权限后尝试");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
