package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeCheckReportAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;

import java.util.ArrayList;


/**
 * Created by kang on 16/7/12.
 * 首页-智慧健康-健康报告
 */
public class HealthyCheckReport extends BaseBackFragment {
    private String mName;
    // UI
    private MyRecyclerView myRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView suggest_image;
    // Data
    private ArrayList<String> projects = new ArrayList<String>(){
        {
            add("BMI");add("舒张压");add("收缩压");add("脉搏");add("血糖");add("胆固醇");add("尿酸");
        }
    };
    private ArrayList<String> results = new ArrayList<String>(){
        {
            add("26.00");add("75mmHg");add("120mmHg");add("80次/分");add("7.3mmoL/L");add("2.2mmoL/L");add("323umol/L");
        }
    };
    private ArrayList<String> ranges = new ArrayList<String>() {
        {
            add("18.5-24.99");add("60-90mmHg");add("90-140mmHg");add("60-100次/分");add("3.9-6.1mmoL/L");add("3-5.2mmoL/L");add("男性: 149-416umoL/L"+"\n"+"女性: 89-357umoL/L  ");
        }
    };
    private ArrayList<Integer> status = new ArrayList<Integer>(){
        {
            add(1);add(0);add(0);add(0);add(1);add(-1);add(0);
        }
    };

    public static HealthyCheckReport newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.TOOLBAR_TITLE, title);
        HealthyCheckReport fragment = new HealthyCheckReport();
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
        return R.layout.fragment_healthy_check_report;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
        myRecyclerView = getViewById(R.id.check_report_list);
        layoutManager = new LinearLayoutManager(context);
        suggest_image = getViewById(R.id.suggest_image);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        myRecyclerView.setLayoutManager(layoutManager);
        HomeCheckReportAdapter adapter = new HomeCheckReportAdapter(context, projects, results, ranges, status);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(myRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                start(HealthyCheckReportShow.newInstance(vh.itemView.getTag().toString()));
            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, int position) {

            }
        });

        Glide.with(context).load(R.drawable.check_report_suggest).into(suggest_image);
    }

    @Override
    public void onClick(View v) {

    }
}
