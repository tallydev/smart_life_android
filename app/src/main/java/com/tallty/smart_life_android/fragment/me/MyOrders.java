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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyOrdersAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.event.ManageOrderEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.cart.PayOrder;
import com.tallty.smart_life_android.fragment.home.ProductShowFragment;
import com.tallty.smart_life_android.model.Order;
import com.tallty.smart_life_android.model.Orders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-我的订单
 */
public class MyOrders extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener{
    private RecyclerView recyclerView;
    private MyOrdersAdapter adapter;
    private String state = "";
    // 数据
    private List<Order> orders = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

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
            state = args.getString(Const.STRING);
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
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this);
    }

    private void getOrders() {
        showProgress("正在加载...");
        Engine.authService(shared_token, shared_phone)
            .getOrders(current_page, per_page, state)
            .enqueue(new Callback<Orders>() {
                @Override
                public void onResponse(Call<Orders> call, Response<Orders> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        current_page = response.body().getCurrentPage();
                        total_pages = response.body().getTotalPages();
                        orders.clear();
                        orders.addAll(response.body().getOrders());
                        adapter.notifyDataSetChanged();
                    } else {
                        showToast("加载失败");
                        try {
                            Log.d(App.TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (current_page >= total_pages) {
                    adapter.loadMoreEnd();
                } else {
                    current_page++;
                    Engine.authService(shared_token, shared_phone)
                        .getOrders(current_page, per_page, state)
                        .enqueue(new Callback<Orders>() {
                            @Override
                            public void onResponse(Call<Orders> call, Response<Orders> response) {
                                if (response.isSuccessful()) {
                                    current_page = response.body().getCurrentPage();
                                    total_pages = response.body().getTotalPages();
                                    adapter.addData(response.body().getOrders());
                                    adapter.loadMoreComplete();
                                } else {
                                    adapter.loadMoreFail();
                                }
                            }

                            @Override
                            public void onFailure(Call<Orders> call, Throwable t) {
                                adapter.loadMoreFail();
                            }
                    });
                }
            }
        }, 1000);
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

    // 查看商品详情
    @Subscribe
    public void onTransferDataEvent(TransferDataEvent event) {
        if ("order_product".equals(event.tag)) {
            int id = event.bundle.getInt("product_id");
            EventBus.getDefault().post(new StartBrotherEvent(ProductShowFragment.newInstance(id)));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
