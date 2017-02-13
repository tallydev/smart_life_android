package com.tallty.smart_life_android.fragment.home;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.gson.JsonElement;
import com.pgyersdk.update.PgyUpdateManager;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeRecyclerAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.base.BaseMainFragment;
import com.tallty.smart_life_android.custom.MyRecyclerView;
import com.tallty.smart_life_android.event.PayEvent;
import com.tallty.smart_life_android.event.ShowSnackbarEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.SwitchTabFragment;
import com.tallty.smart_life_android.event.TabReselectedEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.Common.GlobalAppointFragment;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.fragment.community.CommunityFragment;
import com.tallty.smart_life_android.fragment.me.MyOrders;
import com.tallty.smart_life_android.holder.HomeViewHolder;
import com.tallty.smart_life_android.holder.NetworkImageBannerHolder;
import com.tallty.smart_life_android.model.Activities;
import com.tallty.smart_life_android.model.Activity;
import com.tallty.smart_life_android.model.Banner;
import com.tallty.smart_life_android.model.CartList;
import com.tallty.smart_life_android.model.Home;
import com.tallty.smart_life_android.model.ServiceTel;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.service.StepService;
import com.tallty.smart_life_android.utils.DbUtils;
import com.tallty.smart_life_android.utils.GlobalUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends BaseMainFragment implements OnItemClickListener, Handler.Callback{
    private String shared_token;
    private String shared_phone;
    private CountDownTimer delayTimer;
    private int versionCode;
    /**
     * 静态变量
     **/
    public static int cartCount = 0;
    public static ArrayList<HashMap<String, String>> tels = new ArrayList<>();
    // 计步器相关
    private ServiceConnection conn;
    private static final int TIME_INTERVAL = 1000;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    public static int step = 0;
    public static String rank = "0";
    public static String server_today = "";
    private String DB_NAME = "smart_life";
    // 计时器: 15分钟上传步数
    private static final int uploadStepInterval = 900000;
    private UploadStepTimer timer;
    // 组件
    private ConvenientBanner<String> banner;
    private MyRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    // 运动达人的ViewHolder
    private HomeViewHolder homeViewHolder = null;
    // banner图数据
    private ArrayList<Banner> banners = new ArrayList<>();
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
    private List<String> images = new ArrayList<String>() {
        {
            add(getDrawablePath(R.drawable.smart_healthy));
            add(getDrawablePath(R.drawable.fitness_people));
            add(getDrawablePath(R.drawable.community_activity));
            add(getDrawablePath(R.drawable.smart_home));
            add(getDrawablePath(R.drawable.come_service));
            add(getDrawablePath(R.drawable.community_it));
            add(getDrawablePath(R.drawable.image_placeholder));
            add(getDrawablePath(R.drawable.supermarket));
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
        versionCode = GlobalUtils.getVersionCode(_mActivity);
        Log.i(App.TAG, "APP 版本码: "+versionCode);
        EventBus.getDefault().register(this);
        banner = getViewById(R.id.home_banner);
        recyclerView = getViewById(R.id.home_recycler);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        shared_token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
        shared_phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        // 计步器相关
        delayHandler = new Handler(this);

        // 应用不在前台运行, 如果是点击通知栏, 显示推送
        Bundle bundle = _mActivity.getIntent().getBundleExtra(Const.HOME_BUNDLE);
        if (bundle != null) {
            EventBus.getDefault().post(new StartBrotherEvent(NotificationDetailFragment.newInstance(bundle)));
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        setList();
        // 设置计步服务
        setupService();
        // 设置步数上传计时器(15分钟倒计时: 每分钟判断整点并获取首页数据, 结束上传步数)
        setUploadStepTimer();
        // 进入页面, 延时3秒, 先上传一次步数, 然后再获取首页信息(优化首页信息的实时性)
        delayUploadStep();
        // 获取购物车数量
        getCartCount();
        // 绑定用户到【电子猫眼】服务, 以获取监控推送
        bindUserToNotification();
        // 获取客服电话
        getServiceTels();
    }

    /**
     * 绑定电子猫眼监控服务
     */
    private void bindUserToNotification() {
        final String registrationID = JPushInterface.getRegistrationID(_mActivity);
        // 注册id 为空, 重新注册
        if (registrationID.isEmpty()) {
            JPushInterface.init(_mActivity);
            return;
        }
        // 设置别名
        JPushInterface.setAlias(_mActivity, shared_phone, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i != 0) return;
                Engine.noAuthService().bindNotification(shared_phone, registrationID)
                        .enqueue(new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                if (response.isSuccessful()) {
                                    Log.i(App.TAG, "绑定电子猫眼成功");
                                    Log.d(App.TAG, "别名:" + shared_phone + " , " + "registrationID" + registrationID);
                                } else
                                    Log.i(App.TAG, "绑定电子猫眼失败");
                            }

                            @Override
                            public void onFailure(Call<JsonElement> call, Throwable t) {
                                Log.i(App.TAG, "绑定电子猫眼错误");
                            }
                        });
            }
        });
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

            }

            @Override
            public void onFinish() {
                delayTimer = null;
                String current_date = GlobalUtils.getTodayDate();
                Log.d(App.TAG, "首次进入页面,上传步数任务,并获取首页数据"+current_date+","+step);
                Engine
                    .authService(shared_token, shared_phone)
                    .uploadStep(current_date, step, Const.PLATFORM, versionCode)
                    .enqueue(new Callback<Step>() {
                        @Override
                        public void onResponse(Call<Step> call, Response<Step> response) {
                            if (response.isSuccessful()) {
                                Log.d(App.TAG, "首次进入页面,上传步数成功");
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
            // 到达整点, 保存
            processClockStep();
            // 每分钟获取一次首页数据
            getHomeData();
        }

        @Override
        public void onFinish() {
            // 每分钟结束, 计时器正常结束时,取消上一个计时器,上传步数,开始新的计时器
            timer.cancel();
            uploadStep();
            setUploadStepTimer();
        }
    }

    // ========================业务逻辑=========================
    /**
     * 时间整点业务, 处理步数
     */
    private void processClockStep() {
        String time = GlobalUtils.getNowTime();
        String hour = time.substring(0, 2);
        String minute = time.substring(3);
        Log.i(App.TAG, "一分钟轮询"+time);

        // 每个小时存储步数
        if ("55".equals(minute)) {
            // 保存当前一小时内的步数
            saveStepByHour(hour);
            // 清除当前小时以后的步数以及不是今天的步数
            deleteWrongHourStep(hour);
        }

        // 如果是 00:00, 清除数据
        // (提前两分钟容错, 防止15分钟结束刚好是00:00,
        // 会造成先上传步数,再清零, 第二天服务器步数会比计步器大的情况)
        if ("23:59".equals(time) || "00:00".equals(time) || "00:01".equals(time)) {
            // 清除昨天的逐小时步数
            deleteWrongHourStep("00");
            // 清除计步器步数
            resetPedometer(0);
            Log.d(App.TAG, "迎来新的一天");
        }
    }

    /**
     * 保存当前一小时内的步数
     */
    private void saveStepByHour(String hour) {
        int last_hour = Integer.parseInt(hour) - 1;
        last_hour = last_hour < 0 ? 0 : last_hour;
        String last_hour_str;
        float last_hour_step = 0.0f;
        // 找到之前最近一次不为0的记录
        for (int h = last_hour; h >= 0; h--) {
            last_hour_str = h < 10 ? "0"+h : String.valueOf(h);
            last_hour_step = sharedPre.getFloat(last_hour_str, 0.0f);
            if (last_hour_step > 0) {
                break;
            }
        }
        // 保存差值
        float interval = step - last_hour_step;
        interval = interval >= 0.0f ? interval : 0.0f;
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putFloat(hour, interval);
        editor.apply();
        Log.i(App.TAG, hour + "点的步数以保存: " + interval);
    }

    // 清除当前小时以后的步数以及不是今天的步数 (保证当前是最新的)
    private void deleteWrongHourStep(String hour) {
        SharedPreferences.Editor editor = sharedPre.edit();
        int next_hour = Integer.parseInt(hour) + 1;
        String key;
        for(int i = next_hour; i < 24; i++) {
            key = i < 10 ? "0" + i : String.valueOf(i);
            editor.putFloat(key, 0.0f);
        }
        editor.apply();
        Log.i(App.TAG, hour + "点之后逐小时步数被清空了");
    }

    // 重置计步器步数
    private void resetPedometer(int step) {
        // 清除计步器步数
        if (messenger != null) {
            try {
                Message msg = Message.obtain(null, Const.SET_STEP);
                Bundle bundle = new Bundle();
                bundle.putInt("step", step);
                msg.setData(bundle);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(App.TAG, "清除步数失败, messenger == null");
        }
    }

    // 获取首页信息
    private void getHomeData() {
        Engine.authService(shared_token, shared_phone).getHomeData().enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                if (response.isSuccessful()) {
                    // 更新健步达人浮窗
                    if (response.body().getFitness() != null) {
                        rank = response.body().getFitness().get("rank");
                        server_today = response.body().getFitness().get("today");
                        if (homeViewHolder == null) {
                            homeViewHolder = (HomeViewHolder) recyclerView.findViewHolderForAdapterPosition(1);
                        } else {
                            homeViewHolder.rank.setText(rank);
                            homeViewHolder.date.setText(server_today);
                        }
                    }
                    Log.i(App.TAG, "获取首页信息成功");
                    Log.i(App.TAG, "浮窗排名: "+rank);
                } else {
                    Log.w(App.TAG, "获取首页信息失败");
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Log.e(App.TAG, "链接服务器失败");
                Log.e(App.TAG, t.getMessage()+"");
            }
        });
    }

    // 获取首页Banners
    private void getHomeBanners() {
        Engine.authService(shared_token, shared_phone)
            .getHomeBanners()
            .enqueue(new Callback<Home>() {
                @Override
                public void onResponse(Call<Home> call, Response<Home> response) {
                    if (response.isSuccessful()) {
                        banners.clear();
                        banners.addAll(response.body().getBanners());
                        setBanner();

                        // 更新新品上市倒计时
                        homeRecyclerAdapter.setCountDownTimer(response.body().getNewer().get("end_time"));
                        images.set(6, response.body().getNewer().get("url"));
                        homeRecyclerAdapter.notifyItemChanged(6);
                    }
                }

                @Override
                public void onFailure(Call<Home> call, Throwable t) {

                }
        });
    }

    // 获取客服电话列表
    private void getServiceTels() {
        Engine.authService(shared_token, shared_phone).getTels().enqueue(new Callback<ServiceTel>() {
            @Override
            public void onResponse(Call<ServiceTel> call, Response<ServiceTel> response) {
                if (response.isSuccessful()) {
                    tels = response.body().getTels();
                }
            }

            @Override
            public void onFailure(Call<ServiceTel> call, Throwable t) {

            }
        });
    }

    /**
     * 获取购物车数量
     **/
    private void getCartCount() {
        Engine.authService(shared_token, shared_phone)
            .getCartList(1, 100)
            .enqueue(new Callback<CartList>() {
                @Override
                public void onResponse(Call<CartList> call, Response<CartList> response) {
                    if (response.isSuccessful()) {
                        CartList cartList = response.body();
                        cartCount = cartList.getCartItems().size();
                    }
                }

                @Override
                public void onFailure(Call<CartList> call, Throwable t) {
                }
            });
    }

    private void setBanner() {
        List<String> networkImages = new ArrayList<>();
        for (Banner banner : banners) {
            networkImages.add(banner.getBannerImage());
        }
        banner.setPages(new CBViewHolderCreator<NetworkImageBannerHolder>() {
            @Override
            public NetworkImageBannerHolder createHolder() {
                return new NetworkImageBannerHolder();
            }
        }, networkImages)
        .setPageIndicator(new int[] {R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
        .setOnItemClickListener(this);
    }

    private void setList() {
        recyclerView.setLayoutManager(layoutManager);
        homeRecyclerAdapter = new HomeRecyclerAdapter(_mActivity, titles, images, itemButtons, itemIcons);
        recyclerView.setAdapter(homeRecyclerAdapter);
        // ScrollView嵌套RecyclerView,设置屏幕从顶部开始
        recyclerView.setFocusable(false);
    }

    private void setUploadStepTimer() {
        timer = new UploadStepTimer(uploadStepInterval, 60000);
        timer.start();
    }

    private void uploadStep() {
        String current_date = GlobalUtils.getTodayDate();
        /**
         * 上传步数, 如果是新的一天, 重置步数后再上传
         * 如果是新的一天, 重置数据
         */
        DbUtils.createDb(_mActivity, DB_NAME);
        ArrayList list = DbUtils.getQueryByWhere(Step.class, "date", new String[]{current_date});
        if (list.size() == 0 || list.isEmpty()) {
            // 查不到今天的记录 => 新的一天
            // 清除昨天的逐小时步数
            deleteWrongHourStep("00");
            // 清除计步器步数
            resetPedometer(0);
        }

        Log.d(App.TAG, "开始上传步数任务"+current_date+","+step);
        Engine
            .authService(shared_token, shared_phone)
            .uploadStep(current_date, step, Const.PLATFORM, versionCode)
            .enqueue(new Callback<Step>() {
                @Override
                public void onResponse(Call<Step> call, Response<Step> response) {
                    if (response.isSuccessful()) {
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

    /**
     * 启动计步器服务
     * 发送消息: 【MSG_FROM_CLIENT】
     * 作用: 启动计步服务, 【MSG_FROM_CLIENT】 消息为催化剂,
     *      通知StepService发送: 步数 + 【MSG_FROM_SERVER】消息
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

        Intent intent = new Intent(_mActivity, StepService.class);
        _mActivity.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        _mActivity.startService(intent);
    }

    /**
     * 获取计步器数据
     * 接收消息: 【MSG_FROM_SERVER】|| 【REQUEST_SERVER】
     * if 【MSG_FROM_SERVER】: 1、接收计步服务发来的数据 -> 处理业务逻辑
     *                        2、延时 TIME_INTERVAL 秒发送【REQUEST_SERVER】
     * if 【REQUEST_SERVER】: 发送【MSG_FROM_CLIENT】通知计步服务发送: 步数 + 【MSG_FROM_SERVER】消息
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
     *                      |MSG_FROM_SERVER             | 延时 TIME_INTERVAL 秒
     *                      | +                          | +
     *                      |step                        | REQUEST_SERVER 消息 -> Home(handleMessage) 接收 REQUEST_SERVER -> 发送 CLIENT 消息
     *                      |                            |
     *                      V                            |
     *                      |                            |
     *                      Home(handleMessage) ---->-----
     *                      |
     *                      |
     *                      步数(TIME_INTERVAL秒获得一次数据)
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
                } else {
                    homeViewHolder.steps.setText("" + step);
                    homeViewHolder.rank.setText(rank);
                    homeViewHolder.date.setText(server_today);
                }
                // 延时1s 发送 REQUEST_SERVER 消息
                delayHandler.sendEmptyMessageDelayed(Const.REQUEST_SERVER, TIME_INTERVAL);
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
        Banner banner = banners.get(position);
        String detailImageUrl = banner.getBannerDetailImage();
        int contentId = banner.getTypeId();

        switch (banner.getBannerType()) {
            case "图片详情":
                if (banner.getBannerDetailImage().isEmpty()) return;
                EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance(banner.getTitle(), detailImageUrl)));
                break;
            case "精品超市":
                if (banner.getTypeId() == 0) return;
                EventBus.getDefault().post(new StartBrotherEvent(ProductShowFragment.newInstance(contentId)));
                break;
            case "限量销售":
                if (banner.getTypeId() == 0) return;
                EventBus.getDefault().post(new StartBrotherEvent(PromotionShowFragment.newInstance(contentId)));
                break;
            case "社区活动":
                if (banner.getTypeId() == 0) return;
                getActivities(contentId);
                break;
        }
    }

    // 获取社区活动详情
    private void getActivities(final int activity_id) {
        showProgress("载入中...");
        Engine.authService(shared_token, shared_phone).getActivities().enqueue(new Callback<Activities>() {
            @Override
            public void onResponse(Call<Activities> call, Response<Activities> response) {
                if (response.isSuccessful()) {
                    for (Activity activity : response.body().getActivities()) {
                        if (activity.getId() == activity_id) {
                            EventBus.getDefault().post(new StartBrotherEvent(
                                    GlobalAppointFragment.newInstance("活动详情", activity.getDetail_image(), activity.getId(), "我要报名", false)
                            ));
                            break;
                        }
                    }
                } else {
                    showToast("载入失败");
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<Activities> call, Throwable t) {
                hideProgress();
                showToast("载入失败, 请检查手机网络");
            }
        });
    }

    // 推送init 为异步任务, 延时三秒绑定用户
    private void delayBindUserToNotification() {
        delayTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                delayTimer = null;
                bindUserToNotification();
            }
        };
        delayTimer.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取banner
        getHomeBanners();
        banner.startTurning(5000);
        // 检查更新
        PgyUpdateManager.register(_mActivity);
        // 检测推送注册情况
        if (JPushInterface.getRegistrationID(_mActivity) == null) {
            JPushInterface.init(App.getInstance());
            // init 为异步任务, 延时三秒绑定用户
            delayBindUserToNotification();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _mActivity.unbindService(conn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (timer != null)
            timer.onFinish();
        if (delayTimer != null)
            delayTimer.onFinish();
    }


    //  ====================== 订阅事件 ========================
    @Subscribe
    public void onTabSelectedEvent(TabReselectedEvent event) {
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

    /**
     * 显示推送详情
     * @param event
     */
    @Subscribe
    public void onShowNotification(final TransferDataEvent event) {
        if (Const.JPUSH.equals(event.tag)) {
            confirmDialog("收到门铃告警!", "查看详情", "忽略", new BaseBackFragment.OnConfirmDialogListener() {
                @Override
                public void onConfirm(DialogInterface dialog, int which) {
                    // 显示详情页面
                    EventBus.getDefault().post(new StartBrotherEvent(NotificationDetailFragment.newInstance(event.bundle)));
                    // 消费掉本次通知
                    int notificationId = event.bundle.getInt(Const.NOTIFICATION_ID);
                    NotificationManager nm = (NotificationManager) _mActivity.getSystemService(NOTIFICATION_SERVICE);
                    nm.cancel(notificationId);
                }

                @Override
                public void onCancel(DialogInterface dialog, int which) {

                }
            });
        }
    }

    /**
     * 显示分享详情
     */
    @Subscribe
    public void onDisplayShareDetail(final TransferDataEvent event) {

    }
}
