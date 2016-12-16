package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.SwitchTabFragment;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.fragment.me.MyOrders;
import com.tallty.smart_life_android.model.Order;

import org.greenrobot.eventbus.EventBus;

import static android.R.attr.data;
import static android.R.attr.order;

/**
 * 购物车-支付订单
 */
public class PayOrder extends BaseBackFragment {
    // 结算数据
    private Order order = new Order();
    // UI
    private TextView order_seq;
    private TextView order_postage;
    private TextView order_price_text;
    private TextView pay_btn;
    private TextView order_total_price;

    public static PayOrder newInstance(Order order) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, order);
        PayOrder fragment = new PayOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            order = (Order) args.getSerializable(Const.OBJECT);
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
        order_seq = getViewById(R.id.order_number);
        order_postage = getViewById(R.id.order_postage);
        order_price_text = getViewById(R.id.order_price);
        pay_btn = getViewById(R.id.pay_now);
        order_total_price = getViewById(R.id.order_total_price);
    }

    @Override
    protected void setListener() {
        pay_btn.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        showData();
    }

    @Override
    protected void onFragmentPop() {
        super.onFragmentPop();
        popTo(MainFragment.class, false, new Runnable() {
            @Override
            public void run() {
                // 通知MainFragment切换CartFragment
                EventBus.getDefault().post(new SwitchTabFragment(4));
                EventBus.getDefault().post(new StartBrotherEvent(MyOrders.newInstance()));
            }
        });
    }

    private void showData() {
        order_price_text.setText("RMB " + (order.getPrice() - order.getPostage()));
        order_postage.setText("+ ￥ " + order.getPostage());
        order_seq.setText(order.getSeq());
        order_total_price.setText("￥ " + order.getPrice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_now:
                Pingpp.createPayment(_mActivity, String.valueOf(data));
                break;
        }
    }
}
