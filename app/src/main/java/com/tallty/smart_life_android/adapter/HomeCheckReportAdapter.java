package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/12.
 * 首页-智慧健康-健康报告 适配器
 */
public class HomeCheckReportAdapter extends RecyclerView.Adapter<HomeCheckReportAdapter.CheckReportViewHolder> {
    private Context context;
    private List<Report> reports = new ArrayList<>();

    public HomeCheckReportAdapter(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
    }

    @Override
    public CheckReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_check_report, parent, false);
        return new CheckReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CheckReportViewHolder holder, int position) {
        Report report = reports.get(position);
        //  设置标签,方便点击时获取
        holder.itemView.setTag(report.getAlias());
        holder.project.setText(report.getAlias());
        if (report.getValue() == 0.0) {
            holder.result.setText("——");
        } else {
            holder.result.setText(report.getValue()+"");
        }
        holder.range.setText(report.getHint());
        // 高低
        if ("low".equals(report.getState())) {
            holder.status.setText("{fa-caret-down}");
        } else if ("high".equals(report.getState())) {
            holder.status.setText("{fa-caret-up}");
        }
        // 斑马线背景
        if (position % 2 != 0) {
            holder.report_layout.setBackgroundResource(R.color.item_gray_bg);
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class CheckReportViewHolder extends RecyclerView.ViewHolder {
        private TextView project;
        private TextView result;
        private TextView range;
        private LinearLayout report_layout;
        private IconTextView status;

        CheckReportViewHolder(View itemView) {
            super(itemView);
            project = (TextView) itemView.findViewById(R.id.report_project);
            result = (TextView) itemView.findViewById(R.id.report_result);
            range = (TextView) itemView.findViewById(R.id.report_range);
            status = (IconTextView) itemView.findViewById(R.id.report_status);
            report_layout = (LinearLayout) itemView.findViewById(R.id.item_report_layout);
        }
    }
}
