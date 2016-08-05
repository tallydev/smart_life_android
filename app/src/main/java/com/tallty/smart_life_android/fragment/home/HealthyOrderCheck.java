package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by kang on 16/7/5.
 * 首页->智慧健康-预约体检
 */
public class HealthyOrderCheck extends BaseBackFragment {
    private String mName;

    private CoordinatorLayout order_layout;
    private ImageView banner;
    private ImageView tips;
    private TextView order;

    public static HealthyOrderCheck newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.TOOLBAR_TITLE, title);
        HealthyOrderCheck fragment = new HealthyOrderCheck();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取数据:getArguments()
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(Const.TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_healthy_order_check;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        order_layout = getViewById(R.id.order_layout);
        banner = getViewById(R.id.order_check_banner);
        tips = getViewById(R.id.order_check_text);
        order = getViewById(R.id.order_btn);
    }

    @Override
    protected void setListener() {
        order.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        // 加载图片
        Glide.with(context).load(R.drawable.order_check_top).skipMemoryCache(true).into(banner);
        Glide.with(context).load(R.drawable.order_check_text).skipMemoryCache(true).into(tips);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_btn:
                HintDialogFragment fragment = HintDialogFragment.newInstance(
                        "预约后由<慧生活>服务专员和您电话确认体检日期和体检项目,请保持手机畅通.", "预约体检");
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
                break;
        }
    }

    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
        showToast("预约成功");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
