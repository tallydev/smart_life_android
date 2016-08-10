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
import com.tallty.smart_life_android.model.ReportShowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/13.
 * 首页-智慧健康-健康报告-详情 适配器
 */
public class HomeCheckReportShowAdapter extends RecyclerView.Adapter<HomeCheckReportShowAdapter.CheckReportShowViewHolder> {
    private Context context;
    private List<ReportShowItem> items;

    public HomeCheckReportShowAdapter(Context context, List<ReportShowItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public CheckReportShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_check_report_show, parent, false);
        return new CheckReportShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CheckReportShowViewHolder holder, int position) {
        ReportShowItem item = items.get(position);

        holder.date.setText(item.getDate());
        if (item.getValue() == 0.0) {
            holder.result.setText("——");
        } else {
            holder.result.setText(item.getValue()+"");
        }

        if ("low".equals(item.getState())) {
            holder.status.setText("{fa-caret-down}");
        } else if ("high".equals(item.getState())) {
            holder.status.setText("{fa-caret-up}");
        }
        if (position % 2 != 0) {
            holder.report_layout.setBackgroundResource(R.color.item_gray_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CheckReportShowViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView result;
        private LinearLayout report_layout;
        private IconTextView status;

        public CheckReportShowViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.report_show_date);
            result = (TextView) itemView.findViewById(R.id.report_show_result);
            status = (IconTextView) itemView.findViewById(R.id.report_show_status);
            report_layout = (LinearLayout) itemView.findViewById(R.id.item_report_show_layout);
        }
    }
}
