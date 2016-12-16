package com.tallty.smart_life_android.adapter;

import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Report;

import java.util.List;

/**
 * Created by kang on 16/7/12.
 * 首页-智慧健康-健康报告 适配器
 */
public class HomeCheckReportAdapter extends BaseQuickAdapter<Report, BaseViewHolder>{

    public HomeCheckReportAdapter(int layoutResId, List<Report> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Report report) {
        LinearLayout report_layout = baseViewHolder.getView(R.id.item_report_layout);
        if (baseViewHolder.getAdapterPosition() % 2 != 0) {
            report_layout.setBackgroundResource(R.color.item_gray_bg);
        }
        String status = "";
        if ("low".equals(report.getState())) {
            status = "{fa-caret-down}";
        } else if ("high".equals(report.getState())) {
            status = "{fa-caret-up}";
        }
        baseViewHolder
            .setText(R.id.report_project, report.getAlias())
            .setText(R.id.report_result, report.getValue() == 0.0 ? "——" : report.getValue()+"")
            .setText(R.id.report_range, report.getHint())
            .setText(R.id.report_status, status);
    }
}