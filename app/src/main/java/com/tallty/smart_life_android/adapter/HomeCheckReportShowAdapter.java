package com.tallty.smart_life_android.adapter;

import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.ReportShowItem;

import java.util.List;

/**
 * Created by kang on 16/7/13.
 * 首页-智慧健康-健康报告-详情 适配器
 */
public class HomeCheckReportShowAdapter extends BaseQuickAdapter<ReportShowItem, BaseViewHolder>{

    public HomeCheckReportShowAdapter(int layoutResId, List<ReportShowItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ReportShowItem reportShowItem) {
        LinearLayout report_layout = baseViewHolder.getView(R.id.item_report_show_layout);
        if (baseViewHolder.getAdapterPosition() % 2 != 0) {
            report_layout.setBackgroundResource(R.color.item_gray_bg);
        }
        String status = "";
        if ("low".equals(reportShowItem.getState())) {
            status = "{fa-caret-down}";
        } else if ("high".equals(reportShowItem.getState())) {
            status = "{fa-caret-up}";
        }
        baseViewHolder
            .setText(R.id.report_show_date, reportShowItem.getDate())
            .setText(R.id.report_show_result, reportShowItem.getValue() == 0.0 ? "——" : reportShowItem.getValue()+"")
            .setText(R.id.report_show_status, status);
    }
}