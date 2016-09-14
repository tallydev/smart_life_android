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
import com.tallty.smart_life_android.event.ShowSnackbarEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.holder.BannerHolderView;
import com.tallty.smart_life_android.holder.HomeViewHolder;
import com.tallty.smart_life_android.model.Home;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.service.StepService;
import com.tallty.smart_life_android.utils.ToastUtil;

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
    // 计步器相关
    private ServiceConnection conn;
    private static final int TIME_INTERVAL = 1000;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    public static int step = 0;
    public static int rank = 0;
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
    private Integer[] imagesUrl = { R.drawable.banner_one, R.drawable.banner_two };
    // 列表数据
    private List<String> titles = new ArrayList<String>() {
        {
            add("智慧健康");add("健步达人");add("市政大厅");add("社区活动");
            add("智慧家居");add("社区IT");add("新品上市");add("限量发售");
        }
    };
    private List<Integer> images = new ArrayList<Integer>() {
        {
            add(R.drawable.smart_healthy);add(R.drawable.fitness_people);add(R.drawable.government);
            add(R.drawable.community_activity);add(R.drawable.smart_home);add(R.drawable.community_it);
            add(R.drawable.new_product);add(R.drawable.on_sail);
        }
    };
    private String[][] itemButtons = {
        {"预约体检", "健康报告", "预约专家"},
        {"更多数据"},
        {"天气查询", "违章查询", "公积金", "医保卡", "预约办证", "更多查询"},
        {"活动详情"},
        {"远程控制", "电子猫眼"},
        {"IT学堂", "在线冲印", "IT服务", "更多"},
        {"我要预约"},
        {"更多臻品"}
    };
    private Integer[][] itemIcons = {
        {R.mipmap.smart_healthy_one, R.mipmap.smart_healthy_two, R.mipmap.smart_healthy_three},
        {R.mipmap.fitness_people_one},
        {R.mipmap.goverment_one, R.mipmap.goverment_two, R.mipmap.goverment_three, R.mipmap.goverment_four, R.mipmap.goverment_five, R.mipmap.goverment_six},
        {R.mipmap.community_activity_one},
        {R.mipmap.smart_home_one, R.mipmap.smart_home_two},
        {R.mipmap.community_it_one, R.mipmap.community_it_two, R.mipmap.community_it_three},
        {R.mipmap.new_product_one},
        {R.mipmap.on_sail_one}
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
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        setBanner();
        setList();
        // 设置计步服务
        setupService();
        // 设置步数上传计时器(15分钟)
        setUploadStepTimer();
        // 获取首页信息,更新列表
        getHomeData();
    }


    /**
     * 自定义计时器
     */
    private class UploadStepTimer extends CountDownTimer {
        UploadStepTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 每分钟判断一次时间
            // 整点 ? 保存步数 : continue
            String time = getNowTime();
            Log.i(App.TAG, "一分钟轮询"+time);
            if ("00".equals(time.substring(3))) {
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.putFloat(time.substring(0, 2), (float) step);
                editor.apply();
                Log.d(App.TAG, "整点"+time.substring(3)+"保存了"+time.substring(0, 2)+"步数"+step);
            }
        }

        @Override
        public void onFinish() {
            // 计时器正常结束时,取消上一个计时器,上传步数,开始新的计时器
            timer.cancel();
            uploadStep();
            setUploadStepTimer();
        }
    }

    // ========================业务逻辑=========================
    private void setBanner() {
        List<Integer> networkImages = Arrays.asList(imagesUrl);
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolderView();
            }
        }, networkImages)
                .setPageIndicator(new int[] {R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
                .setOnItemClickListener(this);
    }

    private void getHomeData() {
        Engine.authService(shared_token, shared_phone).getHomeData().enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.code() == 200) {
                    rank = response.body().getFitness().get("rank");
                    homeRecyclerAdapter.setCountDownTimer(response.body().getNewer().get("end_time"));
                    homeRecyclerAdapter.notifyItemChanged(6);
                    Log.d(App.TAG, "更新了倒计时");
                } else {
                    Log.d(App.TAG, "获取首页信息失败");
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Log.d(App.TAG, "链接服务器失败");
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
                    homeViewHolder.rank.setText(""+rank);
                    Log.d(App.TAG, homeViewHolder+"");
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

    @Override
    public void onItemClick(int position) {
        ToastUtil.show("点击了第"+position+"个banner");
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
    }

    /**
     * 订阅事件
     */
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
