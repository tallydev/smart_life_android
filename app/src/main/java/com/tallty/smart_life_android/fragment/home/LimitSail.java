package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeLimitSailAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;

import java.util.ArrayList;

/**
 * 首页-限量销售
 */
public class LimitSail extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView recyclerView;

    private ArrayList<String> names = new ArrayList<String>() {
        {
            add("西双版纳生态无眼凤梨新鲜直达");add("蜂蜜");
            add("蜂蜜");add("蜂蜜");
        }
    };
    private ArrayList<String> prices = new ArrayList<String>() {
        {
            add(" 今日价: 66.00");add(" 今日价: 40.00");add(" 今日价: 40.00");add(" 今日价: 40.00");
        }
    };
    private ArrayList<Integer> photos = new ArrayList<Integer>() {
        {
            add(R.drawable.limi_sail_one);add(R.drawable.on_sail);add(R.drawable.on_sail);
            add(R.drawable.on_sail);
        }
    };

    public static LimitSail newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        LimitSail fragment = new LimitSail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_limit_sail;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        recyclerView = getViewById(R.id.limit_sail_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        // 初始化列表
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        HomeLimitSailAdapter adapter = new HomeLimitSailAdapter(context, names, prices, photos);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}