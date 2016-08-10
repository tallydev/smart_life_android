package com.tallty.smart_life_android.fragment.community;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;
import com.tallty.smart_life_android.model.Appointment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 社区 - 上门服务
 */
public class ComeService extends BaseBackFragment {
    private ImageView service_image;
    private TextView appointment;

    public static ComeService newInstance() {
        Bundle args = new Bundle();

        ComeService fragment = new ComeService();
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
        return R.layout.fragment_come_service;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("上门服务");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        service_image = getViewById(R.id.come_service_image);
        appointment = getViewById(R.id.come_service_appointment);
    }

    @Override
    protected void setListener() {
        appointment.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        Glide.with(context).load(R.drawable.community_service).into(service_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.come_service_appointment:
                HintDialogFragment fragment = HintDialogFragment.newInstance(
                        "预约后由<慧生活>服务专员和您电话联系,请保持手机畅通.", "社区活动");
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
                break;
        }
    }

    /**
     * 订阅事件: 确认了dialog
     * @param event
     */
    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();

        submitAppointmentListener("SMFW", 1, new OnAppointListener() {
            @Override
            public void onSuccess(Appointment appointment) {
                showToast(showString(R.string.appoint_success));
            }

            @Override
            public void onFail(String errorMsg) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


}
