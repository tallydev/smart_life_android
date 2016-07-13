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

import java.util.ArrayList;

/**
 * Created by kang on 16/7/12.
 * 健康报告列表适配器
 */
public class HomeCheckReportAdapter extends RecyclerView.Adapter<HomeCheckReportAdapter.CheckReportViewHolder> {
    private Context context;
    private ArrayList<String> projects;
    private ArrayList<String> results;
    private ArrayList<String> ranges;
    private ArrayList<Integer> status;

    public HomeCheckReportAdapter(Context context, ArrayList<String> projects,
                                  ArrayList<String> results, ArrayList<String> ranges,
                                  ArrayList<Integer> status) {
        this.context = context;
        this.projects = projects;
        this.results = results;
        this.ranges = ranges;
        this.status = status;
    }

    @Override
    public CheckReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_check_report, parent, false);
        return new CheckReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CheckReportViewHolder holder, int position) {
        //  设置标签,方便点击时获取
        holder.itemView.setTag(projects.get(position));
        holder.project.setText(projects.get(position));
        holder.result.setText(results.get(position));
        holder.range.setText(ranges.get(position));
        if (status.get(position) == -1) {
            holder.status.setText("{fa-caret-down}");
        } else if (status.get(position) == 1) {
            holder.status.setText("{fa-caret-up}");
        }
        if (position % 2 != 0) {
            holder.report_layout.setBackgroundResource(R.color.item_gray_bg);
        }
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class CheckReportViewHolder extends RecyclerView.ViewHolder {
        private TextView project;
        private TextView result;
        private TextView range;
        private LinearLayout report_layout;
        private IconTextView status;

        public CheckReportViewHolder(View itemView) {
            super(itemView);
            project = (TextView) itemView.findViewById(R.id.report_project);
            result = (TextView) itemView.findViewById(R.id.report_result);
            range = (TextView) itemView.findViewById(R.id.report_range);
            status = (IconTextView) itemView.findViewById(R.id.report_status);
            report_layout = (LinearLayout) itemView.findViewById(R.id.item_report_layout);
        }
    }
}
