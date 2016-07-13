package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * 首页-健身达人-更多数据
 */
public class TwoMoreData extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView tab_day;
    private TextView tab_week;
    private TextView tab_month;
    private TextView tab_year;
    private LineChartView chart;
    private ImageView rank_image;

    private String[] label_one = {"8:00","9:00","10:00","11:00","12:00","13:00"};
    private float[] data_one = {123f,160f,90f,100f,140f,100f};
    private String[] label_two = {"周一","周二","周三","周四","周五","周六"};
    private float[] data_two = {3232f,6409f,4560f,7809f,5600f,9045f};
    private String[] label_three = {"一月","二月","三月","四月","五月","六月"};
    private float[] data_three = {10f,15f,9f,13f,14f,8f};
    private String[] label_four = {"2014年","2015年","2016年"};
    private float[] data_four = {60f,100f,50f};


    public static TwoMoreData newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        TwoMoreData fragment = new TwoMoreData();
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
        return R.layout.fragment_two_more_data;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);

        tab_day = getViewById(R.id.tab_day);
        tab_week = getViewById(R.id.tab_week);
        tab_month = getViewById(R.id.tab_month);
        tab_year = getViewById(R.id.tab_year);

        chart = getViewById(R.id.chart_one);
        rank_image = getViewById(R.id.step_rank_image);
    }

    @Override
    protected void setListener() {
        tab_day.setOnClickListener(this);
        tab_week.setOnClickListener(this);
        tab_month.setOnClickListener(this);
        tab_year.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        Glide.with(context).load(R.drawable.step_rank).into(rank_image);
        toolbar_title.setText(mName);
        tab_day.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_day:
                chartTabReset();
                tab_day.setSelected(true);
                chart.setVisibility(View.VISIBLE);
                initChart(chart, label_one, data_one, 0, 200);
                break;
            case R.id.tab_week:
                chartTabReset();
                tab_week.setSelected(true);
                initChart(chart, label_two, data_two, 0, 12000);
                break;
            case R.id.tab_month:
                chartTabReset();
                tab_month.setSelected(true);
                initChart(chart, label_three, data_three, 0, 18);
                break;
            case R.id.tab_year:
                chartTabReset();
                tab_year.setSelected(true);
                initChart(chart, label_four, data_four, 0, 120);
                break;
        }
    }

    // 设置图表数据
    private void initChart(LineChartView chart, String[] labels, float[] datas, int min, int max) {
        chart.reset();
        LineSet dataSet = new LineSet(labels, datas);

        dataSet.setColor(getResources().getColor(R.color.white))
                .setFill(getResources().getColor(R.color.transparent))
                .setDotsColor(getResources().getColor(R.color.white))
                .setThickness(4);

        chart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(min, max)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(getResources().getColor(R.color.orange))
                .setXAxis(false)
                .setYAxis(false)
                .getChartAnimation();

        chart.addData(dataSet);
        chart.show();

    }

    // 重置tab的状态
    private void chartTabReset() {
        tab_day.setSelected(false);
        tab_week.setSelected(false);
        tab_month.setSelected(false);
        tab_year.setSelected(false);
    }
}
