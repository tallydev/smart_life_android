package com.tallty.smart_life_android.fragment.home;


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
public class CountOrder extends BaseBackFragment {
    private String mName;
    private int imageId;

    private int count = 1;

    private ImageView detail_image;
    private TextView count_text;
    private TextView add;
    private TextView reduce;
    private TextView number;
    private TextView apply;

    public static CountOrder newInstance(String title, int imageId) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putInt("image", imageId);
        CountOrder fragment = new CountOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(Const.FRAGMENT_NAME);
            imageId = args.getInt("image");
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_count_order;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        detail_image = getViewById(R.id.detail_image);
        count_text = getViewById(R.id.count_text);
        add = getViewById(R.id.add);
        reduce = getViewById(R.id.reduce);
        number = getViewById(R.id.number);
        apply = getViewById(R.id.apply);
    }

    @Override
    protected void setListener() {
        add.setOnClickListener(this);
        reduce.setOnClickListener(this);
        apply.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        if (mName.equals("新品上市")) {
            count_text.setText("预约人数: ");
            apply.setText("我要预约");
        }
        Glide.with(context).load(imageId).into(detail_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                number.setText(String.valueOf(++count));
                break;
            case R.id.reduce:
                if (count > 1){
                    number.setText(String.valueOf(--count));
                }else{
                    number.setText(String.valueOf(count));
                }
                break;
            case R.id.apply:
                HintDialogFragment fragment = HintDialogFragment.newInstance(
                        "报名后由<慧生活>服务专员和您电话联系,请保持手机畅通.", mName);
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
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

        String appointType = null;

        if (!mName.isEmpty() && "社区活动".equals(mName)) {
            appointType = "SQHD";
        }
        if (!mName.isEmpty() && "新品上市".equals(mName)) {
            appointType = "XPSS";
        }

        if (appointType != null) {
            submitAppointmentListener(appointType, count, new OnAppointListener() {
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
