package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.pingplusplus.android.Pingpp;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.PayEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.SwitchTabFragment;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.fragment.me.MyOrders;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.Order;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 购物车-支付订单
 */
public class PayOrder extends BaseBackFragment {
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    // 结算数据
    private Order order = new Order();
    private String payWay = "";
    // UI
    private TextView order_seq;
    private TextView order_postage;
    private TextView order_price_text;
    private TextView pay_btn;
    private TextView order_total_price;
    private RadioGroup pay_radio_group;

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
        EventBus.getDefault().register(this);
        order_seq = getViewById(R.id.order_number);
        order_postage = getViewById(R.id.order_postage);
        order_price_text = getViewById(R.id.order_price);
        pay_btn = getViewById(R.id.pay_now);
        order_total_price = getViewById(R.id.order_total_price);
        pay_radio_group = getViewById(R.id.pay_radio_group);
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
        if (findFragment(MyOrders.class) == null) {
            popTo(MainFragment.class, false, new Runnable() {
                @Override
                public void run() {
                    // 通知MainFragment切换CartFragment
                    EventBus.getDefault().post(new SwitchTabFragment(4));
                    EventBus.getDefault().post(new StartBrotherEvent(MyOrders.newInstance()));
                }
            });
        }
    }

    private void showData() {
        order_price_text.setText("￥ " + (order.getPrice() - order.getPostage()));
        order_postage.setText("+ ￥ " + order.getPostage());
        order_seq.setText(order.getSeq());
        order_total_price.setText("￥ " + order.getPrice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_now:
                if (pay_radio_group.getCheckedRadioButtonId() == R.id.weixin_pay) {
                    showToast("微信支付暂未开通");
                    payWay = CHANNEL_WECHAT;
                } else if (pay_radio_group.getCheckedRadioButtonId() == R.id.alipay){
                    payWay = CHANNEL_ALIPAY;
                    processPayOrder();
                }
                break;
        }
    }

    private void processPayOrder() {
        showProgress("准备支付中...");
        int amount = (int) (order.getPrice() * 100);
        String body = "";
        for (CartItem item : order.getCartItems()) {
            String str = "【" + item.getName() + "x" + item.getCount() + "】";
            body += str;
        }
        Map<String, String> fields = new HashMap<>();
        fields.put("subject", order.getSeq());
        fields.put("body", body);
        Engine.authService(shared_token, shared_phone).getPayCharge(payWay, 1, fields)
            .enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        Log.d(App.TAG, String.valueOf(response.body().getAsJsonObject()));
                        Pingpp.createPayment(getActivity(), String.valueOf(response.body().getAsJsonObject()));
                    } else {
                        showToast("支付失败");
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    hideProgress();
                    showToast("网络错误");
                }
            });
    }

    /**
     * 处理支付回调事件
     */
    @Subscribe
    private void onPayEvent(PayEvent event) {
        if ("success".equals(event.getResult())) {
            // "success" - 支付成功
            popTo(MainFragment.class, false, new Runnable() {
                @Override
                public void run() {
                    // 通知MainFragment切换CartFragment
                    EventBus.getDefault().post(new SwitchTabFragment(4));
                    EventBus.getDefault().post(new StartBrotherEvent(MyOrders.newInstance()));
                }
            });
        } else if ("fail".equals(event.getResult())) {
            // "fail"    - 支付失败
            showToast("支付失败");
        } else if ("cancel".equals(event.getResult())) {
            // "cancel"  - 取消支付
            pop();
        } else if ("invalid".equals(event.getResult())) {
            // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
            showToast("未安装微信客户端");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
