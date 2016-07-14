package com.tallty.smart_life_android.fragment.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.custom.GlideCircleTransform;
import com.tallty.smart_life_android.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kang on 16/6/20.
 * 个人中心
 */
public class MeFragment extends BaseLazyMainFragment {
    private RelativeLayout profile;
    private ImageView photo;
    private TextView name;

    private View order;
    private ImageView order_icon;
    private TextView order_text;

    private LinearLayout wait_pay;
    private LinearLayout wait_transport;

    private View healthy;
    private ImageView healthy_icon;
    private TextView healthy_text;

    private View sport;
    private ImageView sport_icon;
    private TextView sport_text;

    private View appointment;
    private ImageView appointment_icon;
    private TextView appointment_text;

    private View service;
    private ImageView service_icon;
    private TextView service_text;


    public static MeFragment newInstance() {
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("我的");
    }

    @Override
    protected void initView() {
        profile = getViewById(R.id.me_profile);
        photo = getViewById(R.id.user_photo);
        name = getViewById(R.id.user_name);

        order = getViewById(R.id.me_order);
        order_icon = (ImageView) order.findViewById(R.id.cell_icon);
        order_text = (TextView) order.findViewById(R.id.cell_text);

        wait_pay = getViewById(R.id.wait_pay);
        wait_transport = getViewById(R.id.wait_transport);

        healthy = getViewById(R.id.me_healthy);
        healthy_icon = (ImageView) healthy.findViewById(R.id.cell_icon);
        healthy_text = (TextView) healthy.findViewById(R.id.cell_text);

        sport = getViewById(R.id.me_sport);
        sport_icon = (ImageView) sport.findViewById(R.id.cell_icon);
        sport_text = (TextView) sport.findViewById(R.id.cell_text);

        appointment = getViewById(R.id.me_appointment);
        appointment_icon = (ImageView) appointment.findViewById(R.id.cell_icon);
        appointment_text = (TextView) appointment.findViewById(R.id.cell_text);

        service = getViewById(R.id.contact_service);
        service_icon = (ImageView) service.findViewById(R.id.cell_icon);
        service_text = (TextView) service.findViewById(R.id.cell_text);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        setListener();
        setView();
    }

    private void setListener() {
        profile.setOnClickListener(this);
        order.setOnClickListener(this);
        wait_pay.setOnClickListener(this);
        wait_transport.setOnClickListener(this);
        healthy.setOnClickListener(this);
        sport.setOnClickListener(this);
        appointment.setOnClickListener(this);
        service.setOnClickListener(this);
    }

    private void setView() {
        Glide.with(context).load(R.drawable.user_photo).transform(new GlideCircleTransform(context)).into(photo);
        name.setText("Loda");

        order_icon.setImageResource(R.drawable.me_order);
        order_text.setText("我的订单");

        healthy_icon.setImageResource(R.drawable.me_healthy);
        healthy_text.setText("我的健康");

        sport_icon.setImageResource(R.drawable.me_sport);
        sport_text.setText("我的运动");

        appointment_icon.setImageResource(R.drawable.me_appointment);
        appointment_text.setText("我的预约");

        service_icon.setImageResource(R.drawable.contact_service);
        service_text.setText("联系客服");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_profile:
                EventBus.getDefault().post(new StartBrotherEvent(Profile.newInstance("账户管理")));
                break;
            case R.id.me_order:
                EventBus.getDefault().post(new StartBrotherEvent(MyOrders.newInstance("我的订单")));
                break;
            case R.id.wait_pay:
                showToast("待付款");
                break;
            case R.id.wait_transport:
                showToast("待配送");
                break;
            case R.id.me_healthy:
                showToast("我的健康");
                break;
            case R.id.me_sport:
                showToast("我的运动");
                break;
            case R.id.me_appointment:
                EventBus.getDefault().post(new StartBrotherEvent(MyAppointments.newInstance("我的预约")));
                break;
            case R.id.contact_service:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+"087164589208"));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
