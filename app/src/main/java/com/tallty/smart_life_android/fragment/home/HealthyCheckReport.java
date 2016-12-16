package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeCheckReportAdapter;
import com.tallty.smart_life_android.adapter.HomeCheckReportAdviseAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.model.Advise;
import com.tallty.smart_life_android.model.Report;
import com.tallty.smart_life_android.model.ReportList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by kang on 16/7/12.
 * 首页-智慧健康-健康报告
 */
public class HealthyCheckReport extends BaseBackFragment {
    private String mName;
    // UI
    private MyRecyclerView myRecyclerView;
    private HomeCheckReportAdapter adapter;
    private MyRecyclerView adviseRecyclerView;
    private HomeCheckReportAdviseAdapter adviseAdapter;
    // Data
    private List<Report> reports = new ArrayList<>();
    private List<Advise> advises = new ArrayList<>();
    private boolean noChecked = false;

    public static HealthyCheckReport newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
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
            mName = args.getString(Const.FRAGMENT_NAME);
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
        adviseRecyclerView = getViewById(R.id.check_report_advise);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        initAdviseList();
        getCheckReport();
    }

    private void initList() {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new HomeCheckReportAdapter(R.layout.item_home_check_report, reports);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                start(HealthyCheckReportShow.newInstance(reports.get(i)));
            }
        });
    }

    private void initAdviseList() {
        adviseRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adviseAdapter = new HomeCheckReportAdviseAdapter(R.layout.item_check_report_advise, advises);
        adviseRecyclerView.setAdapter(adviseAdapter);
    }

    public void getCheckReport() {
        // 获取报告
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getCheckReport().enqueue(new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    reports.clear();
                    reports.addAll(response.body().getItems());
                    adapter.notifyDataSetChanged();
                    // 显示健康建议
                    noChecked = response.body().getDate() == null;
                    showAdvises(reports);
                } else {
                    showToast(showString(R.string.response_error));
                }

            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });
    }

    private void showAdvises(List<Report> reports) {
        for (Report report : reports) {
            if ("normal".equals(report.getState())) continue;
            String title = "";
            if ("high".equals(report.getState())) {
                title = report.getAlias() + "偏高";
            } else if ("low".equals(report.getState())){
                title = report.getAlias() + "偏低";
            }
            Advise advise = new Advise(title, report.getAdvise());
            advises.add(advise);
        }
        if (advises.size() == 0) {
            Advise advise = new Advise("", "身体棒极了，希望继续保持");
            advises.clear();
            advises.add(advise);
        }
        if (noChecked) {
            Advise advise = new Advise("", "您还没有体检过，赶紧预约体检吧");
            advises.clear();
            advises.add(advise);
        }
        adviseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }
}
