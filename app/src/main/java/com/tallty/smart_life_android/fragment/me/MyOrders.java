package com.tallty.smart_life_android.fragment.me;


import android.net.Uri;
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
import com.tallty.smart_life_android.model.CartItem;
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
    // 临时数据
    // 订单的两个商品
    private String thumb[] = {"",""};
    // 调用接口时去掉
    private Integer images[] = {R.drawable.product_pineapple_one, R.drawable.product_honey_one};
    private String name[] = {"西双版纳生态无眼凤梨","西双版纳生态蜂蜜"};
    private int count[] = {2,1};
    private float price[] = {10.00f, 100.00f};
    // 订单信息
    private String numbers[] = {"201607221234","201607231234"};
    private String times[] = {"2016-07-22", "2016-07-23"};
    private String states[] = {"待发货", "已发货"};
    private String pay_way[] = {"支付宝", "微信"};
    private float prices[] = {120.00f, 100.00f};


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
        // 整理数据
        for (int i=0;i<numbers.length;i++) {
            Order order = new Order();
            order.setNumber(numbers[i]);
            order.setTime(times[i]);
            order.setState(states[i]);
            order.setPayWay(pay_way[i]);
            order.setPrice(prices[i]);
            List<CartItem> commodities = new ArrayList<>();
            for (int j=i;j<count.length;j++) {
                CartItem cartItem = new CartItem();
                cartItem.setCount(count[j]);
                // 调接口时去掉
                cartItem.setImage_id(images[j]);
                cartItem.setName(name[j]);
                cartItem.setPrice(price[j]);
                commodities.add(cartItem);
            }
            order.setCommodities(commodities);
            orders.add(order);
        }
        // 加载列表
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyOrdersAdapter(context, orders);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
