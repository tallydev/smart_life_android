package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

import me.yokeyword.fragmentation.SwipeBackLayout;

/**
 * Created by kang on 16/7/5.
 * 首页->预约体检
 */
public class OrderCheckFragment extends BaseBackFragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView banner;
    private ImageView text;

    public static OrderCheckFragment newInstance() {
        Bundle args = new Bundle();
        // 添加数据到args
        OrderCheckFragment fragment = new OrderCheckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取数据:getArguments()
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_order_check;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        banner = getViewById(R.id.order_check_banner);
        text = getViewById(R.id.order_check_text);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initToolbarNav(toolbar);
        toolbar_title.setText("预约体检");
        // 加载图片
        Glide.with(context).load(R.drawable.order_check_top).into(banner);
        Glide.with(context).load(R.drawable.order_check_text).into(text);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
