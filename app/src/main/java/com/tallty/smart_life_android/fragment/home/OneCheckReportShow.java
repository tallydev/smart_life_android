package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeCheckReportShowAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kang on 16/7/13.
 * 首页-智慧健康-健康报告-详细数据
 */
public class OneCheckReportShow extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private MyRecyclerView myRecyclerView;

    private ArrayList<String> dates = new ArrayList<String>() {
        {
            add("2016.06.01");add("2016.05.28");add("2016.05.26");add("2016.05.24");add("2016.05.20");
            add("2016.05.18");add("2016.05.16");add("2016.05.12");add("2016.05.8");add("2016.05.2");
        }
    };
    private ArrayList<String> results = new ArrayList<String>() {
        {
            add("22.00");add("26.00");add("22.80");add("21.00");add("20.00");
            add("14.80");add("18.30");add("24.00");add("19.70");add("18.32");
        }
    };
    private ArrayList<Integer> status = new ArrayList<Integer>() {
        {
            add(0);add(1);add(0);add(0);add(0);
            add(-1);add(0);add(1);add(0);add(0);
        }
    };

    public static OneCheckReportShow newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        OneCheckReportShow fragment = new OneCheckReportShow();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mName = args.getString(TOOLBAR_TITLE);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_one_check_report_show;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        myRecyclerView = getViewById(R.id.check_report_show_list);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        // 初始化列表
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        myRecyclerView.setLayoutManager(layoutManager);
        HomeCheckReportShowAdapter adapter = new HomeCheckReportShowAdapter(context, dates, results, status);
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
