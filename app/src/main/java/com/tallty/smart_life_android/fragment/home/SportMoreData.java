package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeSportRankAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.model.SportData;
import com.tallty.smart_life_android.model.SportDetail;
import com.tallty.smart_life_android.model.SportInfo;
import com.tallty.smart_life_android.model.SportRank;
import com.tallty.smart_life_android.model.SportRankItem;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.service.StepCreator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页-健身达人-更多数据
 */
public class SportMoreData extends BaseBackFragment {
    private String mName;
    // 组件
    private TextView tab_day;
    private TextView tab_week;
    private TextView tab_month;
    private TextView tab_year;
    private LineChartView chartDay;
    private LineChartView chartWeek;
    private LineChartView chartMonth;
    private LineChartView chartYear;
    private TextView now_step;
    private TextView avg_step;
    private TextView now_time;
    private TextView total_step;
    private TextView rank_percent;
    // 列表
    private MyRecyclerView recyclerView;
    private HomeSportRankAdapter adapter;
    // 切换加载控制
    private boolean isLoadDay = false;
    private boolean isLoadWeek = false;
    private boolean isLoadMonth = false;
    private boolean isLoadYear = false;
    // 图表数据
    private ArrayList<SportDetail> sportChartData = new ArrayList<>();
    // rank数据
    private ArrayList<SportRankItem> sportRankItems = new ArrayList<>();
    // 列表控制
    private boolean isLoadRank = false;
    // 步数
    private int step = 0;


    public static SportMoreData newInstance(String title, int step) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putInt(Const.INT, step);
        SportMoreData fragment = new SportMoreData();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mName = args.getString(Const.FRAGMENT_NAME);
        step = args.getInt(Const.INT);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_sport_more_data;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
        tab_day = getViewById(R.id.tab_day);
        tab_week = getViewById(R.id.tab_week);
        tab_month = getViewById(R.id.tab_month);
        tab_year = getViewById(R.id.tab_year);

        chartDay = getViewById(R.id.chart_day);
        chartWeek = getViewById(R.id.chart_week);
        chartMonth = getViewById(R.id.chart_month);
        chartYear = getViewById(R.id.chart_year);

        now_step = getViewById(R.id.now_step);
        avg_step = getViewById(R.id.avg_step);
        now_time = getViewById(R.id.now_time);
        total_step = getViewById(R.id.total_step);
        rank_percent = getViewById(R.id.rank_percent);

        recyclerView = getViewById(R.id.step_rank);
    }

    @Override
    protected void setListener() {
        tab_day.setOnClickListener(this);
        tab_week.setOnClickListener(this);
        tab_month.setOnClickListener(this);
        tab_year.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        tab_day.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_day:
                tabSelectedTask(tab_day, chartDay, "daily", isLoadDay);
                isLoadDay = true;
                break;
            case R.id.tab_week:
                tabSelectedTask(tab_week, chartWeek, "weekly", isLoadWeek);
                isLoadWeek = true;
                break;
            case R.id.tab_month:
                tabSelectedTask(tab_month, chartMonth, "monthly", isLoadMonth);
                isLoadMonth = true;
                break;
            case R.id.tab_year:
                tabSelectedTask(tab_year, chartYear, "yearly", isLoadYear);
                isLoadYear = true;
                break;
        }
    }

    /**
     * tab按钮点击任务
     * @param tab
     * @param chart
     * @param timeLine
     * @param isLoad
     */
    private void tabSelectedTask(TextView tab, LineChartView chart, String timeLine, boolean isLoad) {
        chartTabReset();
        tab.setSelected(true);
        chart.setVisibility(View.VISIBLE);
        if (timeLine.equals("daily")) {
            // 每日步数, 先上传最新的步数, 再载入图表和列表
            updateStepAndInitChartRank(timeLine, chart, isLoad);
        } else {
            initChartAndRank(timeLine, chart, isLoad);
        }

    }

    private void updateStepAndInitChartRank(final String timeLine,
                                            final LineChartView chart, final boolean isLoad) {
        String current_date = getTodayDate();

        Log.d(TAG, "开始上传步数任务"+current_date+","+ step);
        Engine.authService(shared_token, shared_phone).uploadStep(current_date, step).enqueue(new Callback<Step>() {
            @Override
            public void onResponse(Call<Step> call, Response<Step> response) {
                if (response.code() == 201) {
                    Log.d(TAG, "上传步数成功"+response.body().getCount());
                    // 载入图表和列表
                    initChartAndRank(timeLine, chart, isLoad);
                } else {
                    Log.d(TAG, "上传步数失败");
                }
            }

            @Override
            public void onFailure(Call<Step> call, Throwable t) {
                Log.d(TAG, "上传步数链接服务器失败");
            }
        });
    }

    /**
     * 初始化图表和rank
     * @param timeLine
     * @param chart
     */
    private void initChartAndRank(final String timeLine, final LineChartView chart, final boolean isLoad) {
        showProgress(showString(R.string.progress_normal));
        // 获取时间段运动图表数据和个人统计信息
        Engine.authService(shared_token, shared_phone).getSportsData(timeLine).enqueue(new Callback<SportData>() {
            @Override
            public void onResponse(Call<SportData> call, Response<SportData> response) {
                if (response.code() == 200) {
                    // 获取图表信息
                    sportChartData = response.body().getDetail();
                    // 加载个人统计信息
                    setPersonInfo(response.body().getSelf());
                    // 加载图表
                    if (sportChartData.isEmpty()) {
                        // 整理每日图表数据
                        setDayChartData(chart, isLoad);
                    } else {
                        // 整理周、月、年图表数据
                        setChartData(chart, isLoad);
                    }
                    // 加载rank
                    initRankList(timeLine);
                } else {
                    hideProgress();
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<SportData> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });
    }

    /**
     * 获取rank接口数据,载入rank列表
     * @param timeLine
     */
    private void initRankList(String timeLine) {
        // 获取rank数据
        Engine.authService(shared_token, shared_phone).getSportRanks(timeLine).enqueue(new Callback<SportRank>() {
            @Override
            public void onResponse(Call<SportRank> call, Response<SportRank> response) {
                if (response.code() == 200) {
                    SportRank sportRank = response.body();
                    sportRankItems.clear();
                    sportRankItems.addAll(sportRank.getTop());

                    int total_pages = sportRank.getTotal_pages();
                    int current_page = sportRank.getCurrent_page();
                    // 更新列表

                    if (isLoadRank) {
                        // 以后采取更新列表
                        Log.d("运动", "开始更新列表");
                        adapter.notifyDataSetChanged();
                    } else {
                        // 第一次初始化列表
                        setRankList(sportRankItems);
                        isLoadRank = true;
                    }

                    hideProgress();
                } else {
                    hideProgress();
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<SportRank> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.response_error));
            }
        });
    }

    /**
     * 操作列表
     */
    private void setRankList(ArrayList<SportRankItem> sportRankItems) {
        Log.d("运动", "初始化列表");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HomeSportRankAdapter(context, sportRankItems);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置个人统计信息
     */
    private void setPersonInfo(SportInfo sportInfo) {
        // 当前时间
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        if (sportInfo != null) {
            now_step.setText(sportInfo.getTodayCount()+"步");
            avg_step.setText("平均步数: "+ sportInfo.getAvgCount());
            now_time.setText("今天: "+sdf.format(date));
            total_step.setText(sportInfo.getCount()+"");
            rank_percent.setText(sportInfo.getRankPercent() * 100 + "%");
        }
    }

    // 设置图表数据(日)
    private void setDayChartData(LineChartView chart, boolean isLoad) {
        String[] labels = new String[24];
        float[] counts = new float[24];

        String sharedKey;
        int max = 0;

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                sharedKey = "0" + i;
            } else {
                sharedKey = String.valueOf(i);
            }
            labels[i] = sharedKey + ":00";
            counts[i] = sharedPre.getFloat(sharedKey, 0.0f);
            max = max > counts[i] ? max : (int) counts[i];
        }

        // 增加一定比例的最大值, 使图表不会顶住天
        max += (max / 4);

        // 载入图表
        if (!isLoad)
            loadChart(chart, labels, counts, max);
    }

    // 设置图表数据(周, 月, 年)
    private void setChartData(LineChartView chart, boolean isLoad) {
        int max = 0;
        String[] labels = new String[sportChartData.size()];
        float[] counts = new float[sportChartData.size()];

        for (int i = 0; i < sportChartData.size(); i++) {
            SportDetail sportDetail = sportChartData.get(i);
            labels[i] = sportDetail.getTag();
            counts[i] = sportDetail.getCount();

            max = sportDetail.getCount() > max ? sportDetail.getCount() : max;
        }
        // 载入图表
        if (!isLoad)
            loadChart(chart, labels, counts, max);
    }

    /**
     * 载入运动图表
     * @param chart
     * @param labels
     * @param datas
     * @param max
     */
    private void loadChart(LineChartView chart, String[] labels, float[] datas, int max) {
        Log.d("chart", "实例化图表");
        chart.reset();
        LineSet dataSet = new LineSet(labels, datas);

        dataSet.setColor(getResources().getColor(R.color.white))
                .setFill(getResources().getColor(R.color.transparent))
                .setDotsColor(getResources().getColor(R.color.white))
                .setThickness(4);

        chart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(0, max)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(getResources().getColor(R.color.orange))
                .setXAxis(false)
                .setYAxis(false)
                .getChartAnimation();

        chart.addData(dataSet);
        chart.show();
    }

    // 重置tab的状态
    private void chartTabReset() {
        tab_day.setSelected(false);
        tab_week.setSelected(false);
        tab_month.setSelected(false);
        tab_year.setSelected(false);

        chartDay.setVisibility(View.GONE);
        chartWeek.setVisibility(View.GONE);
        chartMonth.setVisibility(View.GONE);
        chartYear.setVisibility(View.GONE);
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
