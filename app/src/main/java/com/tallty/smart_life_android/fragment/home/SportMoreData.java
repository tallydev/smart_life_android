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
import com.tallty.smart_life_android.App;
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
import com.tallty.smart_life_android.utils.Apputils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页-健步达人-更多数据
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
    private TextView rank_position;
    private TextView rank_load_more;
    // 列表
    private MyRecyclerView recyclerView;
    private HomeSportRankAdapter adapter;
    // rank列表控制
    private boolean isLoadRank = false;
    private Integer total_pages = 1;
    private Integer current_page = 1;
    private Integer per_page = 20;
    // 切换加载控制
    private boolean isLoadDay = false;
    private boolean isLoadWeek = false;
    private boolean isLoadMonth = false;
    private boolean isLoadYear = false;
    private static final String DAY = "daily";
    private static final String WEEK = "weekly";
    private static final String MONTH = "monthly";
    private static final String YEAR = "yearly";
    private String now_timeline = "";
    // 图表数据
    private ArrayList<SportDetail> sportChartData = new ArrayList<>();
    // rank数据
    private ArrayList<SportRankItem> sportRankItems = new ArrayList<>();
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
        rank_position = getViewById(R.id.rank_position);

        recyclerView = getViewById(R.id.step_rank);
        rank_load_more = getViewById(R.id.rank_load_more);
    }

    @Override
    protected void setListener() {
        tab_day.setOnClickListener(this);
        tab_week.setOnClickListener(this);
        tab_month.setOnClickListener(this);
        tab_year.setOnClickListener(this);
        rank_load_more.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        tab_day.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_day:
                now_timeline = DAY;
                current_page = 1;
                tabSelectedTask(tab_day, chartDay, isLoadDay);
                isLoadDay = true;
                break;
            case R.id.tab_week:
                now_timeline = WEEK;
                current_page = 1;
                tabSelectedTask(tab_week, chartWeek, isLoadWeek);
                isLoadWeek = true;
                break;
            case R.id.tab_month:
                now_timeline = MONTH;
                current_page = 1;
                tabSelectedTask(tab_month, chartMonth, isLoadMonth);
                isLoadMonth = true;
                break;
            case R.id.tab_year:
                now_timeline = YEAR;
                current_page = 1;
                tabSelectedTask(tab_year, chartYear, isLoadYear);
                isLoadYear = true;
                break;
            case R.id.rank_load_more:
                rank_load_more.setText("加载中…");
                rank_load_more.setClickable(false);
                getMoreRank();
                break;
        }
    }

    /**
     * tab按钮点击任务
     * @param tab
     * @param chart
     * @param isLoad
     */
    private void tabSelectedTask(TextView tab, LineChartView chart, boolean isLoad) {
        chartTabReset();
        // 重置加载更多按钮
        resetRankMoreBtn("点击加载更多");
        tab.setSelected(true);
        chart.setVisibility(View.VISIBLE);
        if (DAY.equals(now_timeline)) {
            // 每日步数, 先上传最新的步数, 再载入图表和列表
            updateStepAndInitChartRank(chart, isLoad);
        } else {
            initChartAndRank(chart, isLoad);
        }
    }

    private void updateStepAndInitChartRank(final LineChartView chart,
                                            final boolean isLoad) {
        String current_date = getTodayDate();
        int versionCode = Apputils.getVersionCode(_mActivity);
        // 步数少于服务器的步数,会上传失败
        Log.i(App.TAG, "开始上传步数任务"+current_date+","+ step);
        Engine
            .authService(shared_token, shared_phone)
            .uploadStep(current_date, step, "android", versionCode)
            .enqueue(new Callback<Step>() {
                @Override
                public void onResponse(Call<Step> call, Response<Step> response) {
                    if (response.isSuccessful()) {
                        Log.i(App.TAG, "上传步数成功"+response.body().getCount());
                    } else {
                        Log.i(App.TAG, "上传步数失败");
                    }
                    // 载入图表和列表
                    initChartAndRank(chart, isLoad);
                }

                @Override
                public void onFailure(Call<Step> call, Throwable t) {
                    Log.d(App.TAG, "上传步数链接服务器失败");
                }
            });
    }

    /**
     * 初始化图表和rank
     * @param chart
     */
    private void initChartAndRank(final LineChartView chart, final boolean isLoad) {
        showProgress(showString(R.string.progress_normal));
        // 获取时间段运动图表数据和个人统计信息
        Engine.authService(shared_token, shared_phone).getSportsData(now_timeline).enqueue(new Callback<SportData>() {
            @Override
            public void onResponse(Call<SportData> call, Response<SportData> response) {
                if (response.isSuccessful()) {
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
                    initRankList();
                } else {
                    hideProgress();
                    showToast(showString(R.string.response_error));
                    Log.d(App.TAG, "初始化chart&rank列表,请求错误"+response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<SportData> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
                Log.d(App.TAG, "初始化chart&rank列表,网络错误"+now_timeline);
            }
        });
    }

    /**
     * 获取rank接口数据,载入rank列表
     */
    private void initRankList() {
        // 获取rank数据
        Engine.authService(shared_token, shared_phone)
                .getSportRanks(now_timeline, current_page, per_page)
                .enqueue(new Callback<SportRank>() {
            @Override
            public void onResponse(Call<SportRank> call, Response<SportRank> response) {
                if (response.isSuccessful()) {
                    SportRank sportRank = response.body();
                    sportRankItems.clear();
                    sportRankItems.addAll(sportRank.getTop());

                    total_pages = sportRank.getTotal_pages();
                    current_page = sportRank.getCurrent_page();

                    // 是否显示点击加载更多按钮
                    if (current_page < total_pages) {
                        rank_load_more.setVisibility(View.VISIBLE);
                        current_page++;
                    }

                    // 更新列表
                    if (isLoadRank) {
                        // 以后采取更新列表
                        Log.d(App.TAG, "开始更新列表");
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
                    Log.d(App.TAG, "初始化rank列表,请求错误"+current_page+"/"+now_timeline);
                }
            }

            @Override
            public void onFailure(Call<SportRank> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.response_error));
                Log.d(App.TAG, "初始化rank列表,网络错误");
            }
        });
    }

    /**
     * 操作列表
     */
    private void setRankList(ArrayList<SportRankItem> sportRankItems) {
        Log.d(App.TAG, "初始化列表");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HomeSportRankAdapter(_mActivity, sportRankItems);
        recyclerView.setAdapter(adapter);

        // 点击加载更多功能
        // 使用 RecyclerView 本身帮助方法无效 (列表响应不了滚动)
//        recyclerView.addOnScrollListener(new LoadMoreRecyclerScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                if (currentPage < total_pages) {
//                    current_page = currentPage;
//                    initRankList(now_timeline);
//                    Logger.d("加载了更多数据");
//                }
//            }
//        });
    }

    /**
     * 点击加载更多rank数据
     */
    private void getMoreRank() {
        Engine.authService(shared_token, shared_phone)
            .getSportRanks(now_timeline, current_page, per_page)
            .enqueue(new Callback<SportRank>() {
                @Override
                public void onResponse(Call<SportRank> call, Response<SportRank> response) {
                    if (response.isSuccessful()) {
                        SportRank sportRank = response.body();
                        sportRankItems.addAll(sportRank.getTop());
                        total_pages = sportRank.getTotal_pages();
                        current_page = sportRank.getCurrent_page();
                        // 点击加载更多
                        adapter.notifyDataSetChanged();
                        resetRankMoreBtn("点击加载更多");
                    } else {
                        resetRankMoreBtn("点击重新加载");
                    }
                    // 显示与数量控制
                    if (current_page < total_pages) {
                        rank_load_more.setVisibility(View.VISIBLE);
                        current_page++;
                    } else {
                        rank_load_more.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<SportRank> call, Throwable t) {
                    resetRankMoreBtn("点击重新加载");
                    if (current_page < total_pages) {
                        rank_load_more.setVisibility(View.VISIBLE);
                        current_page++;
                    } else {
                        rank_load_more.setVisibility(View.GONE);
                    }
                }
            });
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
            rank_position.setText(sportInfo.getRank()+"");
            rank_percent.setText(Math.round(sportInfo.getRankPercent() * 100) + "%");
        }
    }

    // 设置图表数据(日)
    private void setDayChartData(LineChartView chart, boolean isLoad) {
        String[] labels = new String[24];
        float[] counts = new float[24];
        // 当前key
        String sharedKey;
        float nowStep;
        // 差值
        int max = 0;
        // 去除24小时数据
        for (int i = 0; i < 24; i++) {
            sharedKey = i < 10 ? "0" + i : String.valueOf(i);
            // 每一小时的步数
            nowStep = sharedPre.getFloat(sharedKey, 0.0f);
            // 保存图表数据
            labels[i] = sharedKey + ":00";
            counts[i] = nowStep;
            max = max > counts[i] ? max : (int) counts[i];
            Log.d(App.TAG, sharedKey + "=====>" + counts[i]);
        }
        // 增加一定比例的最大值, 使图表不会顶住天
        max += (max / 4);

        Log.d(App.TAG, Arrays.toString(counts) +"");
        // 载入图表
        if (!isLoad) {
            loadChart(chart, labels, counts, max);
        }
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
        if (!isLoad) {
            loadChart(chart, labels, counts, max);
        }
    }

    /**
     * 载入运动图表
     * @param chart
     * @param labels
     * @param datas
     * @param max
     */
    private void loadChart(LineChartView chart, String[] labels, float[] datas, int max) {
        if (!isAdded()) return ;
        Log.d(App.TAG, "实例化图表");
        chart.reset();
        LineSet dataSet = new LineSet(labels, datas);

        dataSet.setColor(showColor(R.color.white))
                .setFill(showColor(R.color.transparent))
                .setDotsColor(showColor(R.color.white))
                .setThickness(4);

        chart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(0, max)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(showColor(R.color.orange))
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

    // 重置点击加载更多按钮
    private void resetRankMoreBtn(String text) {
        rank_load_more.setVisibility(View.GONE);
        rank_load_more.setClickable(true);
        rank_load_more.setText(text);
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
