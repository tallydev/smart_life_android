package com.tallty.smart_life_android.fragment.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeRecyclerAdapter;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.event.ClearDayStepEvent;
import com.tallty.smart_life_android.event.ShowSnackbarEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.holder.BannerHolderView;
import com.tallty.smart_life_android.holder.HomeViewHolder;
import com.tallty.smart_life_android.model.Home;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.service.StepService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends BaseLazyMainFragment implements OnItemClickListener, Handler.Callback{
    private String shared_token;
    private String shared_phone;
    private CountDownTimer delayTimer;
    // 计步器相关
    private ServiceConnection conn;
    private static final int TIME_INTERVAL = 1000;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    public static int step = 0;
    public static String rank = "0";
    public static String server_today = "";
    // 计时器: 15分钟上传步数
    private static final int uploadStepInterval = 900000;
    private UploadStepTimer timer;
    // 组件
    private ConvenientBanner<Integer> banner;
    private MyRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    // 运动达人的ViewHolder
    private HomeViewHolder homeViewHolder = null;
    // banner图数据
    private Integer[] imagesUrl = { R.drawable.banner_one, R.drawable.banner_two, R.drawable.banner_three};
    // 列表数据
    private List<String> titles = new ArrayList<String>() {
        {
            add("智慧健康");
            add("健步达人");
            add("社区活动");
            add("智慧家居");
            add("上门服务");
            add("社区IT");
            add("限量发售");
            add("精品超市");
        }
    };
    private List<Integer> images = new ArrayList<Integer>() {
        {
            add(R.drawable.smart_healthy);
            add(R.drawable.fitness_people);
            add(R.drawable.community_activity);
            add(R.drawable.smart_home);
            add(R.drawable.come_service);
            add(R.drawable.community_it);
            add(R.drawable.on_sail);
            add(R.drawable.supermarket);
        }
    };
    private String[][] itemButtons = {
        {"预约体检", "健康报告", "预约专家"},
        {"更多数据"},
        {"活动详情"},
        {"远程控制", "电子猫眼"},
        {"上门服务"},
        {"IT学堂", "在线冲印", "IT服务"},
        {"我要参团"},
        {"更多臻品"}
    };
    private Integer[][] itemIcons = {
        {R.mipmap.smart_healthy_one, R.mipmap.smart_healthy_two, R.mipmap.smart_healthy_three},
        {R.mipmap.fitness_people_one},
        {R.mipmap.community_activity_one},
        {R.mipmap.smart_home_one, R.mipmap.smart_home_two},
        {R.mipmap.service_one},
        {R.mipmap.community_it_one, R.mipmap.community_it_two, R.mipmap.community_it_three},
        {R.mipmap.supermarket_one},
        {R.mipmap.more_icon}
    };


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        banner = getViewById(R.id.home_banner);
        recyclerView = getViewById(R.id.home_recycler);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 计步器相关
        delayHandler = new Handler(this);

        EventBus.getDefault().register(this);
        shared_token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
        shared_phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        setBanner();
        setList();
        // 设置计步服务
        setupService();
        // 设置步数上传计时器(15分钟倒计时: 每分钟判断整点并获取首页数据, 结束上传步数)
        setUploadStepTimer();
        // 计入页面, 延时3秒上传一次步数, 然后再获取首页信息
        delayUploadStep();
    }

    /**
     * 因为计步服务使用handleMessage来传递step数据
     * 进入页面, 可能还没有接受到计步器发来的步数, 因此也无法使用接口上传步数
     * 导致【健步达人】浮窗排名为空
     * 解决: 延时三秒, 以确保已接受了step, 然后先上传步数, 再获取首页信息
     */
    private void delayUploadStep() {
        delayTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(App.TAG, "首次进入首页,先上传step,再获取HomeData, 倒计时"+ millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                String current_date = getTodayDate();
                Log.d(App.TAG, "首次进入页面,上传步数任务,并获取首页数据"+current_date+","+step);
                Engine.authService(shared_token, shared_phone).uploadStep(current_date, step).enqueue(new Callback<Step>() {
                    @Override
                    public void onResponse(Call<Step> call, Response<Step> response) {
                        if (response.code() == 201) {
                            // 获取首页信息,更新列表
                            getHomeData();
                        } else {
                            Log.d(App.TAG, "上传步数失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<Step> call, Throwable t) {
                        Log.d(App.TAG, "上传步数链接服务器失败");
                    }
                });
            }
        };
        delayTimer.start();
    }


    /**
     * 自定义计时器
     * 每分钟判断一次时间, 是否是整点
     * 整点 ? 保存步数 : continue
     * 并获取一次首页数据
     */
    private class UploadStepTimer extends CountDownTimer {
        UploadStepTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String time = getNowTime();
            Log.i(App.TAG, "一分钟轮询"+time);

            if ("00".equals(time.substring(3))) {
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.putFloat(time.substring(0, 2), (float) step);
                editor.apply();
                Log.d(App.TAG, "整点"+time.substring(3)+"保存了"+time.substring(0, 2)+"步数"+step);
            }
            // 每分钟获取一次首页数据
            getHomeData();
        }

        @Override
        public void onFinish() {
            // 15分钟结束, 计时器正常结束时,取消上一个计时器,上传步数,开始新的计时器
            timer.cancel();
            uploadStep();
            setUploadStepTimer();
        }
    }

    // ========================业务逻辑=========================
    private void setBanner() {
        List<Integer> networkImages = Arrays.asList(imagesUrl);
        banner.setPages(
            new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new BannerHolderView();
                }
            }, networkImages
        )
        .setPageIndicator(new int[] {R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
        .setOnItemClickListener(this);
    }

    private void getHomeData() {
        Engine.authService(shared_token, shared_phone).getHomeData().enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.code() == 200) {
                    if (response.body().getFitness() != null) {
                        rank = response.body().getFitness().get("rank");
                        server_today = response.body().getFitness().get("today");
                    }
                    // 更新健步达人浮窗
                    if (homeViewHolder == null) {
                        homeViewHolder = (HomeViewHolder) recyclerView.findViewHolderForAdapterPosition(1);
                    } else {
                        homeViewHolder.rank.setText(rank);
                        homeViewHolder.date.setText(server_today);
                    }
                    // 更新新品上市倒计时
                    homeRecyclerAdapter.setCountDownTimer(response.body().getNewer().get("end_time"));
                    homeRecyclerAdapter.notifyItemChanged(6);

                    Log.i(App.TAG, rank+"排名");
                    Log.d(App.TAG, "更新了倒计时");
                } else {
                    Log.d(App.TAG, "获取首页信息失败");
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Log.d(App.TAG, "链接服务器失败");
                Log.e(App.TAG, t.getMessage()+"");
            }
        });
    }

    private void setList() {
        recyclerView.setLayoutManager(layoutManager);
        homeRecyclerAdapter = new HomeRecyclerAdapter(context, titles, images, itemButtons, itemIcons);
        recyclerView.setAdapter(homeRecyclerAdapter);
        // ScrollView嵌套RecyclerView,设置屏幕从顶部开始
        recyclerView.setFocusable(false);
    }

    private void setUploadStepTimer() {
        timer = new UploadStepTimer(uploadStepInterval, 60000);
        timer.start();
    }

    private void uploadStep() {
        String current_date = getTodayDate();
        Log.d(App.TAG, "开始上传步数任务"+current_date+","+step);
        Engine.authService(shared_token, shared_phone).uploadStep(current_date, step).enqueue(new Callback<Step>() {
            @Override
            public void onResponse(Call<Step> call, Response<Step> response) {
                if (response.code() == 201) {
                    Log.d(App.TAG, "上传步数成功"+response.body().getCount());
                } else {
                    Log.d(App.TAG, "上传步数失败");
                }
            }

            @Override
            public void onFailure(Call<Step> call, Throwable t) {
                Log.d(App.TAG, "上传步数链接服务器失败");
            }
        });
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }


    /**
     * 启动计步器服务
     * 发送消息: 【MSG_FROM_CLIENT】
     * 作用: 启动计步服务, 【MSG_FROM_CLIENT】 消息为催化剂,
     *      通知StepService发送: 步数 + 【MSG_FROM_Server】消息
     */
    private void setupService() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    messenger = new Messenger(service);
                    Message msg = Message.obtain(null, Const.MSG_FROM_CLIENT);
                    msg.replyTo = mGetReplyMessenger;
                    messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(context, StepService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        context.startService(intent);
    }

    /**
     * 获取计步器数据
     * 接收消息: 【MSG_FROM_SERVER】|| 【REQUEST_SERVER】
     * if 【MSG_FROM_SERVER】: 1、接收计步服务发来的数据 -> 处理业务逻辑
     *                        2、延时0.5秒发送【REQUEST_SERVER】
     * if 【REQUEST_SERVER】: 发送【MSG_FROM_CLIENT】通知计步服务发送: 步数 + 【MSG_FROM_Server】消息
     *
     * 逻辑图:
     *
     *                      setupService()
     *                      |
     *                      |CLIENT消息
     *                      |
     *                      V
     *                      |
     *                      StepService -------<----------
     *                      |                            |
     *                      |                            |
     *                      |SERVER                      | 延时 0.5 s
     *                      | +                          | +
     *                      |step                        | CLIENT消息
     *                      |                            |
     *                      V                            |
     *                      |                            |
     *                      Home(handleMessage) ---->-----
     *                      |
     *                      |
     *                      步数(0.5秒获得一次数据)
     *
     *
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Const.MSG_FROM_SERVER:
                // 保存步数
                step = msg.getData().getInt("step");
                // 更新首页视图
                if (homeViewHolder == null) {
                    homeViewHolder = (HomeViewHolder) recyclerView.findViewHolderForAdapterPosition(1);
                    Log.d(App.TAG, homeViewHolder+"");
                } else {
                    homeViewHolder.steps.setText(""+step);
                    homeViewHolder.rank.setText(rank);
                    homeViewHolder.date.setText(server_today);
                }
                // 延时1s 发送 REQUEST_SERVER 消息
                delayHandler.sendEmptyMessageDelayed(Const.REQUEST_SERVER,TIME_INTERVAL);
                break;
            case Const.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Const.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * banner 图点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        if (1 == position) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unbindService(conn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        timer.onFinish();
        delayTimer.onFinish();
    }

    // 订阅事件
    /**
     * 清空逐小时步数
     */
    @Subscribe
    public void onClearSahredStep(ClearDayStepEvent event) {
        String sharedKey;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                sharedKey = "0" + i;
            } else {
                sharedKey = String.valueOf(i);
            }
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putFloat(sharedKey, 0.0f);
            editor.apply();
        }
        Log.i(App.TAG, "旧的逐小时步数被清空了");
    }

    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        // Tab Home按钮被重复点击时执行的操作
        if (event.position == MainFragment.HOME) {
            Log.d(App.TAG, "首页被重复点击了");
        }
    }

    @Subscribe
    public void onShowSnackbar(ShowSnackbarEvent event) {
        setSnackBar(recyclerView, event.text, 100000, R.layout.snackbar_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
