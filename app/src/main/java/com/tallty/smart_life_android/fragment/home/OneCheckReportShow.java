package com.tallty.smart_life_android.fragment.home;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
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
    private LineChartView chart;
    private TextView chart_name;

    private ArrayList<String> dates = new ArrayList<String>() {
        {
            add("2016.06.01");add("2016.05.28");add("2016.05.26");add("2016.05.24");add("2016.05.20");
            add("2016.05.18");add("2016.05.16");add("2016.05.12");add("2016.05.8");add("2016.05.2");
            add("2016.04.18");add("2016.04.16");
        }
    };
    private ArrayList<Float> results = new ArrayList<Float>() {
        {
            add(20.20f);add(26.00f);add(22.80f);add(21.00f);add(20.00f);
            add(14.80f);add(18.30f);add(24.00f);add(19.70f);add(18.32f);
            add(14.80f);add(18.30f);
        }
    };
    private ArrayList<Integer> status = new ArrayList<Integer>() {
        {
            add(0);add(1);add(0);add(0);add(0);
            add(-1);add(0);add(1);add(0);add(0);
            add(-1);add(0);
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
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
        }
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
        chart = getViewById(R.id.check_line_chart);
        chart_name = getViewById(R.id.chart_name);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        chart_name.setText(mName);
        // 设置图表
        initChart();
        // 初始化列表
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        myRecyclerView.setLayoutManager(layoutManager);
        HomeCheckReportShowAdapter adapter = new HomeCheckReportShowAdapter(context, dates, results, status);
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    private void initChart() {
        String[] labels = new String[6];
        float[] values = new float[6];

        int j = 0;
        for (int i = 0; i < dates.size(); i+=2) {
            labels[j] = dates.get(i).substring(5);
            values[j] = results.get(i);
            j++;
        }

        LineSet dataSet = new LineSet(labels, values);

        dataSet.setColor(getResources().getColor(R.color.white))
                .setFill(getResources().getColor(R.color.transparent))
                .setDotsColor(getResources().getColor(R.color.white))
                .setThickness(4)
                .endAt(6);

        chart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(10, 30)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(getResources().getColor(R.color.orange))
                .setXAxis(false)
                .setYAxis(false)
                .getChartAnimation();

        chart.addData(dataSet);
        chart.show();
    }
}
