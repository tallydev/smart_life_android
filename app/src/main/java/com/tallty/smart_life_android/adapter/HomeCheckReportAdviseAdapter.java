package com.tallty.smart_life_android.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Advise;

import java.util.List;

/**
 * Created by kang on 2016/12/16.
 * 健康建议 - 适配器
 */

public class HomeCheckReportAdviseAdapter extends BaseQuickAdapter<Advise, BaseViewHolder> {
    public HomeCheckReportAdviseAdapter(int layoutResId, List<Advise> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Advise advise) {
        baseViewHolder
                .setText(R.id.check_report_title, advise.getTitle()+"：")
                .setText(R.id.check_report_advise, advise.getAdvise());
        TextView title = baseViewHolder.getView(R.id.check_report_title);
        if ("".equals(advise.getTitle())) {
            title.setVisibility(View.GONE);
        }
    }
}
