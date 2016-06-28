package com.tallty.smart_life_android.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.MyGridView;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by kang on 16/6/28.
 * 首页RecyclerView ViewHolder
 */
public class HomeViewHolder extends RecyclerView.ViewHolder {
    // 模板类型
    private static final int IS_NORMAL = 0;
    private static final int IS_STEPS = 1;
    private static final int IS_PRODUCT = 2;
    // 正常布局
    public TextView textView;
    public ImageView imageView;
    public MyGridView gridView;
    // "健身达人"布局
    public ImageView weather;
    public TextView date;
    public TextView rank;
    public TextView steps;
    // 新品上市
    public LinearLayout timerLayout;
    public CountdownView countdownView;

    public int viewType;

    public HomeViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
        textView = (TextView) itemView.findViewById(R.id.item_home_main_title);
        imageView = (ImageView) itemView.findViewById(R.id.item_home_main_image);
        gridView = (MyGridView) itemView.findViewById(R.id.item_home_main_gridView);
        if (viewType == IS_STEPS) {
            weather = (ImageView) itemView.findViewById(R.id.step_weather);
            date = (TextView) itemView.findViewById(R.id.steps_date);
            rank = (TextView) itemView.findViewById(R.id.steps_rank);
            steps = (TextView) itemView.findViewById(R.id.steps_number);
        } else if (viewType == IS_PRODUCT) {
            timerLayout = (LinearLayout) itemView.findViewById(R.id.timer_ly);
            countdownView = (CountdownView) itemView.findViewById(R.id.home_timer);
        } else if (viewType == IS_NORMAL) {

        }
    }
}
