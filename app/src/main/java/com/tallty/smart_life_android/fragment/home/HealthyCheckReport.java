package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeCheckReportAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
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
    // Data
    private List<Report> reports = new ArrayList<>();


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
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        // 获取报告
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getCheckReport().enqueue(new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                if (response.isSuccessful()) {
                    reports = response.body().getItems();
                    // 加载列表
                    setList();
                    hideProgress();
                } else {
                    hideProgress();
                    showToast(showString(R.string.response_error));
                }

            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });

        // 健康建议
    }

    private void setList() {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new HomeCheckReportAdapter(_mActivity, reports);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(myRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                start(HealthyCheckReportShow.newInstance(reports.get(position)));
            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
