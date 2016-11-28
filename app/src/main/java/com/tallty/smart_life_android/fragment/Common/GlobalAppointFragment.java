package com.tallty.smart_life_android.fragment.Common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;
import com.tallty.smart_life_android.model.Appointment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 首页-社区活动-活动详情
 * 首页-社区活动-新品上市
 */
public class GlobalAppointFragment extends BaseBackFragment {
    private String fragmentTitle;
    private int imageId;
    private String appointType;
    private Boolean isSingle;
    private String btn_text;
    // 详情图
    private ImageView detail_image;
    // 多人的操作
    private View countLayout;
    private TextView add;
    private TextView reduce;
    private TextView number;
    private TextView countAppointBtn;
    // 单人的操作
    private View singleLayout;
    private TextView singleAppointBtn;
    // 计数
    private int count = 1;

    public static GlobalAppointFragment newInstance(String title,
                                                    int imageId,
                                                    String appointType,
                                                    String btn_text,
                                                    Boolean isSingle) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putInt(Const.INT, imageId);
        args.putString(Const.STRING, appointType);
        args.putBoolean("是否是单人", isSingle);
        args.putString("按钮文本", btn_text);
        GlobalAppointFragment fragment = new GlobalAppointFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fragmentTitle = args.getString(Const.FRAGMENT_NAME);
            imageId = args.getInt(Const.INT);
            appointType = args.getString(Const.STRING);
            isSingle = args.getBoolean("是否是单人");
            btn_text = args.getString("按钮文本");
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_global_appoint;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(fragmentTitle);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        detail_image = getViewById(R.id.detail_image);

        singleLayout = getViewById(R.id.single_action_include);
        singleAppointBtn = (TextView) singleLayout.findViewById(R.id.appoint_btn);

        countLayout = getViewById(R.id.count_action_include);
        add = (TextView) countLayout.findViewById(R.id.count_add);
        reduce = (TextView) countLayout.findViewById(R.id.count_reduce);
        number = (TextView) countLayout.findViewById(R.id.number);
        countAppointBtn = (TextView) countLayout.findViewById(R.id.apply_btn);
    }

    @Override
    protected void setListener() {
        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        countAppointBtn.setOnClickListener(this);
        singleAppointBtn.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        Glide.with(context).load(imageId).into(detail_image);
        if (isSingle) {
            singleLayout.setVisibility(View.VISIBLE);
            singleAppointBtn.setText(btn_text);
        } else {
            countLayout.setVisibility(View.VISIBLE);
            countAppointBtn.setText(btn_text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.count_add:
                number.setText(String.valueOf(++count));
                break;
            case R.id.count_reduce:
                if (count > 1){
                    number.setText(String.valueOf(--count));
                }else{
                    number.setText(String.valueOf(count));
                }
                break;
            case R.id.apply_btn:
                HintDialogFragment fragment_one = HintDialogFragment.newInstance(
                        "报名后由<慧生活>服务专员和您电话联系,请保持手机畅通.", appointType);
                fragment_one.show(getActivity().getFragmentManager(), "HintDialog");
                break;
            case R.id.appoint_btn:
                HintDialogFragment fragment_two = HintDialogFragment.newInstance(
                        "报名后由<慧生活>服务专员和您电话联系,请保持手机畅通.", appointType);
                fragment_two.show(getActivity().getFragmentManager(), "HintDialog");
                break;
        }
    }

    /**
     * 接收预约确认事件
     * @param event
     */
    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();

        if (event.caller != null) {
            submitAppointmentListener(event.caller, count, new OnAppointListener() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
