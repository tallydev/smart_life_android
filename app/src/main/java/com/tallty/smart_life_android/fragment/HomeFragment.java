package com.tallty.smart_life_android.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeAdapter;
import com.tallty.smart_life_android.base.BaseFragment;
import com.tallty.smart_life_android.custom.PedometerConstant;
import com.tallty.smart_life_android.holder.HomeBannerHolderView;
import com.tallty.smart_life_android.holder.HomeViewHolder;
import com.tallty.smart_life_android.service.StepService;
import com.tallty.smart_life_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnItemClickListener, Handler.Callback{
    // 计步器相关
    private long TIME_INTERVAL = 500;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    // 组件
    private ConvenientBanner banner;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    // banner图数据
    private Integer[] imagesUrl = { R.drawable.banner_one, R.drawable.community_activity };
    // 列表数据
    private Integer step = 0;
    private List<String> titles = new ArrayList<String>() {
        {
            add("智慧健康");
            add("健身达人");
            add("市政大厅");
            add("社区活动");
            add("智慧家居");
            add("社区IT");
            add("新品上市");
            add("限量发售");
        }
    };
    private List<Integer> images = new ArrayList<Integer>() {
        {
            add(R.drawable.smart_healthy);
            add(R.drawable.fitness_people);
            add(R.drawable.government);
            add(R.drawable.community_activity);
            add(R.drawable.smart_home);
            add(R.drawable.community_it);
            add(R.drawable.new_product);
            add(R.drawable.on_sail);
        }
    };
    private String[][] itemButtons = {
        {"预约体检", "健康报告", "预约专家", "更多"},
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
    HomeViewHolder homeViewHolder;

    // 构造函数
    public HomeFragment() {

    }

    /**
     * 启动计步器服务
     */
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, PedometerConstant.MSG_FROM_CLIENT);
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

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        banner = getViewById(R.id.home_banner);
        recyclerView = getViewById(R.id.home_recycler);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 计步器相关
        delayHandler = new Handler(this);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic() {
        setBanner();
        setList();
        // 设置计步服务
        setupService();
    }

    @Override
    public void onClick(View v) {

    }

    // ========================业务逻辑=========================
    private void setBanner() {
        List<Integer> networkImages = Arrays.asList(imagesUrl);
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new HomeBannerHolderView();
            }
        }, networkImages)
                .setPageIndicator(new int[] {R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
                .setOnItemClickListener(this);
    }

    private void setList() {
        recyclerView.setLayoutManager(layoutManager);
        HomeAdapter homeAdapter = new HomeAdapter(context, titles, images, itemButtons, itemIcons, step);
        recyclerView.setAdapter(homeAdapter);
        // ScrollView嵌套RecyclerView,设置屏幕从顶部开始
        recyclerView.setFocusable(false);
    }

    private void setupService() {
        Intent intent = new Intent(context, StepService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        context.startService(intent);
    }

    /**
     * 获取计步器数据
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case PedometerConstant.MSG_FROM_SERVER:
                // 更新首页视图
                step = msg.getData().getInt("step");
                homeViewHolder = (HomeViewHolder) recyclerView.findViewHolderForAdapterPosition(1);
                if (homeViewHolder != null) {
                    homeViewHolder.steps.setText(String.valueOf(step));
                }
                // 计步器数据: msg.getData().getInt("step")
                delayHandler.sendEmptyMessageDelayed(PedometerConstant.REQUEST_SERVER,TIME_INTERVAL);
                break;
            case PedometerConstant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, PedometerConstant.MSG_FROM_CLIENT);
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
}
