package com.tallty.smart_life_android.fragment.me;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.custom.GlideCircleTransform;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.fragment.home.HealthyCheckReport;
import com.tallty.smart_life_android.fragment.home.SportMoreData;
import com.tallty.smart_life_android.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    // 数据
    private User user = new User();


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
        EventBus.getDefault().register(this);

        profile = getViewById(R.id.me_profile);
        profile.setClickable(false);
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
        // 查询用户信息
        mApp.headerEngine().getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if (response.code() == 200) {
                    profile.setClickable(true);
                    Glide.with(context).load(user.getAvatar())
                            .placeholder(R.drawable.user_default)
                            .transform(new GlideCircleTransform(context)).into(photo);
                    name.setText(user.getNickname());
                } else {
                    showToast("获取用户信息失败");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showToast(context.getString(R.string.network_error));
            }
        });
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
                EventBus.getDefault().post(new StartBrotherEvent(ProfileFragment.newInstance()));
                break;
            case R.id.me_order:
                EventBus.getDefault().post(new StartBrotherEvent(MyOrders.newInstance()));
                break;
            case R.id.wait_pay:
                showToast("待付款");
                break;
            case R.id.wait_transport:
                showToast("待配送");
                break;
            case R.id.me_healthy:
                EventBus.getDefault().post(new StartBrotherEvent(HealthyCheckReport.newInstance("健康报告")));
                break;
            case R.id.me_sport:
                EventBus.getDefault().post(new StartBrotherEvent(SportMoreData.newInstance("健身达人")));
                break;
            case R.id.me_appointment:
                EventBus.getDefault().post(new StartBrotherEvent(MyAppointments.newInstance()));
                break;
            case R.id.contact_service:
                PackageManager pm = context.getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CALL_PHONE","com.tallty.smart_life_android"));
                if (permission) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"087164589208"));
                    startActivity(intent);
                } else {
                    setSnackBar(service,
                            "应用无拨打电话权限,请设置应用权限后尝试",
                            100000, R.layout.snackbar_icon, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                }

                break;
        }
    }

    /**
     * 订阅事件: TabSelectedEvent
     * Tab Me 按钮被重复点击时执行的操作
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position == MainFragment.ME)
            Log.d("tab-reselected", "个人中心被重复点击了");
    }

    /**
     * 订阅事件: TransferDataEvent
     * 接收profile回传的nickname, avatar
     * 更新账户管理信息
     */
    @Subscribe
    public void onTransferDataEvent(TransferDataEvent event) {
        // 更新UI
        if (event.tag.equals("ProfileFragment")) {
            Glide.with(context).load(event.bundle.getString("user_avatar"))
                    .placeholder(R.drawable.user_default)
                    .transform(new GlideCircleTransform(context)).into(photo);
            name.setText(event.bundle.getString("user_nickname"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
