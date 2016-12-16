package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeCheckReportShowAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.model.Report;
import com.tallty.smart_life_android.model.ReportShowItem;
import com.tallty.smart_life_android.model.ReportShowList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/7/13.
 * 首页-智慧健康-健康报告-详细数据
 */
public class HealthyCheckReportShow extends BaseBackFragment {
    private Report report;
    // UI
    private MyRecyclerView myRecyclerView;
    private HomeCheckReportShowAdapter adapter;
    private LineChartView chart;
    private TextView chart_name;
    // 数据
    private List<ReportShowItem> reportShowItems = new ArrayList<>();


    public static HealthyCheckReportShow newInstance(Report report) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, report);
        HealthyCheckReportShow fragment = new HealthyCheckReportShow();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            report = (Report) args.getSerializable(Const.OBJECT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_healthy_check_report_show;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(report.getAlias());
    }

    @Override
    protected void initView() {
        myRecyclerView = getViewById(R.id.check_report_show_list);
        chart = getViewById(R.id.check_line_chart);
        chart_name = getViewById(R.id.chart_name);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void afterAnimationLogic() {
        chart_name.setText(report.getAlias());
        initList();
        getHistoryData();
    }

    private void initList() {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new HomeCheckReportShowAdapter(R.layout.item_home_check_report_show, reportShowItems);
        myRecyclerView.setAdapter(adapter);
    }

    // 获取单项历史数据
    public void getHistoryData() {
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getReportHistory(report.getName()).enqueue(new Callback<ReportShowList>() {
            @Override
            public void onResponse(Call<ReportShowList> call, Response<ReportShowList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    reportShowItems.clear();
                    reportShowItems.addAll(response.body().getList());
                    adapter.notifyDataSetChanged();
                    // 图表
                    if (!reportShowItems.isEmpty() && isAdded())
                        setChart();
                } else {
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<ReportShowList> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });
    }

    @Override
    public void onClick(View v) {}


    private void setChart() {
        String[] labels = new String[reportShowItems.size()];
        float[] values = new float[reportShowItems.size()];
        int max = 0;
        for (int i = 0; i < reportShowItems.size(); i++) {
            labels[i] = reportShowItems.get(i).getDate().substring(5);
            values[i] = reportShowItems.get(i).getValue();
            max = max > reportShowItems.get(i).getValue() ? max : (int) reportShowItems.get(i).getValue();
            max += max / 5;
        }

        LineSet dataSet = new LineSet(labels, values);

        dataSet.setColor(showColor(R.color.white))
                .setFill(showColor(R.color.transparent))
                .setDotsColor(showColor(R.color.white))
                .setThickness(4);

        chart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(0, max)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(showColor(R.color.orange))
                .setXAxis(false)
                .setYAxis(false)
                .getChartAnimation();

        chart.addData(dataSet);
        chart.show();
    }
}
