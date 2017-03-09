package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ShowSnackbarEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.Common.GlobalAppointFragment;
import com.tallty.smart_life_android.fragment.home.CommunityActivityFragment;
import com.tallty.smart_life_android.fragment.home.HealthyCheckReport;
import com.tallty.smart_life_android.fragment.home.HomeFragment;
import com.tallty.smart_life_android.fragment.home.ProductCategoryFragment;
import com.tallty.smart_life_android.fragment.home.PromotionFragment;
import com.tallty.smart_life_android.fragment.home.SportFragment;
import com.tallty.smart_life_android.holder.HomeViewHolder;
import com.tallty.smart_life_android.model.HomeBlock;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by kang on 16/6/22.
 * 首页瀑布流RecyclerView适配器
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private Context context;
    // 所有模块
    private HashMap<String, HomeBlock> allBlocks;
    // 当前社区包含的模块
    private ArrayList<HomeBlock> nowBlocks;
    private long countDownSecond;

    /**
     * @param context
     * @param nowBlocks 当前社区包含模块
     */
    public HomeRecyclerAdapter(Context context, ArrayList<HomeBlock> nowBlocks) {
        this.context = context;
        this.allBlocks = getAllBlocks();
        this.nowBlocks = nowBlocks;
        this.countDownSecond = 648000;

    }

    private HashMap<String, HomeBlock> getAllBlocks() {
        // 保存所有的模块
        HashMap<String, HomeBlock> allBlocks = new HashMap<>();
        HomeBlock homeBlock;
        String title;
        for (int i = 0; i < Const.HOME_BLOCK_TITLES.size(); i++) {
            title = Const.HOME_BLOCK_TITLES.get(i);
            homeBlock = new HomeBlock(
                    title,
                    "",
                    Const.HOME_BLOCK_SUBTITLES.get(title),
                    Const.HOME_BLOCK_SUBICONS.get(title)
            );
            allBlocks.put(title, homeBlock);
        }
        return allBlocks;
    }

    @Override
    public int getItemViewType(int position) {
        switch (nowBlocks.get(position).getTitle()) {
            case "健步达人":
                return HomeViewHolder.IS_STEPS;
            case "限量发售":
                return HomeViewHolder.IS_PRODUCT;
            default:
                return HomeViewHolder.IS_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return nowBlocks.size();
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        HomeViewHolder holder;
        View itemView;
        // 正常布局
        if (viewType == HomeViewHolder.IS_NORMAL) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_main, viewGroup, false);
            holder = new HomeViewHolder(itemView, HomeViewHolder.IS_NORMAL);
            return holder;
        }
        // "健步达人"布局
        else if (viewType == HomeViewHolder.IS_STEPS) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_steps, viewGroup, false);
            holder = new HomeViewHolder(itemView, HomeViewHolder.IS_STEPS);
            return holder;
        }
        // "限量发售"布局
        else if (viewType == HomeViewHolder.IS_PRODUCT) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_timer, viewGroup, false);
            holder = new HomeViewHolder(itemView, HomeViewHolder.IS_PRODUCT);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder viewHolder, final int position) {
        // 筛选: 显示包含title的模块
        HomeBlock netData = nowBlocks.get(position);
        HomeBlock block = allBlocks.get(netData.getTitle());
        Log.i(App.TAG, block.getSubIcons()+"");
        if (block == null) return;
        // 公用布局: 标题图片按钮
        viewHolder.textView.setText("— "+ block.getTitle() +" —");
        Glide.with(context)
            .load(netData.getImage())
            .error(R.drawable.image_error)
            .placeholder(R.drawable.product_placeholder)
            .into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageClickListener(position);
            }
        });

        // 设置item里面的GridView
        if (block.getSubIcons().size() == 1) {
            viewHolder.gridView.setHorizontalSpacing(0);
        }
        viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, block));
        // set tag
        viewHolder.gridView.setTag(block.getTitle());
        gridItemClickListener(viewHolder);

        // "健步达人"布局
        if (viewHolder.viewType == HomeViewHolder.IS_STEPS) {
            Glide.with(context)
                .load(R.drawable.step_weather)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.image_placeholder)
                .into(viewHolder.weather);
            viewHolder.rank.setText("1");
            viewHolder.steps.setText("0");
        }

        // 限量发售布局
        else if (viewHolder.viewType == HomeViewHolder.IS_PRODUCT) {
            // 设置倒计时
            viewHolder.countdownView.start(countDownSecond);
            viewHolder.countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.stop();
                }
            });
        }
    }

    private void setImageClickListener(int position) {
        String title = Const.HOME_BLOCK_TITLES.get(position);
        // 智慧健康
        if (Const.BLOCK_HEALTHY.equals(title)) {
            String url = "http://elive.clfsj.com:8989/images/healthy_description.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("智慧健康", url)));
        }
        // 健步达人
        else if (Const.BLOCK_SPORT.equals(title)) {
            EventBus.getDefault().post(new StartBrotherEvent(SportFragment.newInstance("健步达人", HomeFragment.step)));
        }
        // 社区活动
        else if (Const.BLOCK_ACTIVITY.equals(title)) {
            EventBus.getDefault().post(new StartBrotherEvent(CommunityActivityFragment.newInstance("社区活动")));
        }
        // 智慧家居
        else if (Const.BLOCK_HOME.equals(title)) {
            String url = "http://elive.clfsj.com:8989/images/smart_home_house.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("智能家居", url, "ZNJJ", "预约体验", true)));
        }
        // 上门服务
        else if (Const.BLOCK_SERVICE.equals(title)) {
            String url = "http://elive.clfsj.com:8989/images/come_service.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("上门服务", url, "SMFW", "我要预约", true)));
        }
        // 社区IT
        else if (Const.BLOCK_IT.equals(title)) {
            String url = "http://elive.clfsj.com:8989/images/community_it_class.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("IT学堂", url, "ITXT", "我要报名", true)));
        }
        // 限量发售
        else if (Const.BLOCK_LIMIT.equals(title)) {
            EventBus.getDefault().post(new StartBrotherEvent(PromotionFragment.newInstance()));
        }
        // 精品超市
        else if (Const.BLOCK_MARKET.equals(title)) {
            EventBus.getDefault().post(new StartBrotherEvent(ProductCategoryFragment.newInstance("精品超市")));
        }
    }

    public void setCountDownTimer(String time) {
        if (time.isEmpty() || "".equals(time)) return;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000+08:00'");
        try {
            Date date = format.parse(time);
            countDownSecond = date.getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            countDownSecond = 0;
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     */
    private void gridItemClickListener(HomeViewHolder viewHolder) {
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = (String) parent.getTag();
                // 智慧健康
                if (Const.BLOCK_HEALTHY.equals(tag)) {
                    if (position == 0) {
                        String url = "http://elive.clfsj.com:8989/images/healthy_order_check.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("预约体检", url, "ZHJK", "我要预约", true)));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new StartBrotherEvent(HealthyCheckReport.newInstance("健康报告")));
                    } else if (position == 2) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    }
                }
                // 健步达人
                else if (Const.BLOCK_SPORT.equals(tag)) {
                    if (position == 0)
                        EventBus.getDefault().post(new StartBrotherEvent(SportFragment.newInstance("健步达人", HomeFragment.step)));
                }
                // 社区活动
                else if (Const.BLOCK_ACTIVITY.equals(tag)) {
                    if (position == 0)
                        EventBus.getDefault().post(new StartBrotherEvent(CommunityActivityFragment.newInstance("社区活动")));
                }
                // 智慧家居
                else if (Const.BLOCK_HOME.equals(tag)) {
                    if (position == 0) {
                        String url = "http://elive.clfsj.com:8989/images/smart_home_house.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("智能家居", url, "ZNJJ", "预约体验", true)));
                    } else if (position == 1) {
                        String url = "http://elive.clfsj.com:8989/images/smart_home_cat_eye.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("电子猫眼", url, "DZMY", "预约体验", true)));
                    }
                }
                // 上门服务
                else if (Const.BLOCK_SERVICE.equals(tag)) {
                    if (position == 0) {
                        String url = "http://elive.clfsj.com:8989/images/come_service.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("上门服务", url, "SMFW", "我要预约", true)));
                    }
                }
                // 社区IT
                else if (Const.BLOCK_IT.equals(tag)) {
                    if (position == 0) {
                        String url = "http://elive.clfsj.com:8989/images/community_it_class.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("IT学堂", url, "ITXT", "我要报名", true)));
                    } else if (position == 1) {
                        String url = "http://elive.clfsj.com:8989/images/community_it_print_online.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("在线冲印", url, "ZXCY", "微我", true)));
                    } else if (position == 2) {
                        String url = "http://elive.clfsj.com:8989/images/community_it_service.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("IT服务", url, "ITFW", "我要预约", true)));
                    }
                }
                // 限量发售
                else if (Const.BLOCK_LIMIT.equals(tag)) {
                    if (position == 0)
                        EventBus.getDefault().post(new StartBrotherEvent(PromotionFragment.newInstance()));
                }
                // 精品超市
                else if (Const.BLOCK_MARKET.equals(tag)) {
                    if (position == 0)
                        EventBus.getDefault().post(new StartBrotherEvent(ProductCategoryFragment.newInstance("商品分类")));
                }
            }
        });
    }
}
