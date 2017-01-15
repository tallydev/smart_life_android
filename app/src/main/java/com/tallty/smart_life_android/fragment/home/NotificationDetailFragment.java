package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.NotificationAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;

import java.util.ArrayList;

/**
 * 电子猫眼告警详情展示页面
 */
public class NotificationDetailFragment extends BaseBackFragment {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private String title= "";
    private String time = "";
    private ArrayList<String> images;

    public static NotificationDetailFragment newInstance(Bundle bundle) {
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(Const.PUSH_TITLE);
            time = args.getString(Const.PUSH_TIME);
            images = args.getStringArrayList(Const.PUSH_IMAGES);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_common_list;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(title);
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.common_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initList();
    }

    private void initList() {
        // 整理数据
        ArrayList<String> list = new ArrayList<>();
        for (String image : images) {
            list.add(image);
        }
        // 加载
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new NotificationAdapter(R.layout.item_fragment_notification, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
