package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ShowSnackbarEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.Common.GlobalAppointFragment;
import com.tallty.smart_life_android.fragment.home.CommunityActivityFragment;
import com.tallty.smart_life_android.fragment.home.HealthyCheckReport;
import com.tallty.smart_life_android.fragment.home.HomeFragment;
import com.tallty.smart_life_android.fragment.home.MarketCategoryFragment;
import com.tallty.smart_life_android.fragment.home.ProductFragment;
import com.tallty.smart_life_android.fragment.home.SportMoreData;
import com.tallty.smart_life_android.holder.HomeViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by kang on 16/6/22.
 * 首页瀑布流RecyclerView适配器
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private Context context;
    // 数据源
    private List<String> titles;
    private List<Integer> images;
    private String[][] buttons;
    private Integer[][] icons;
    private long countDownSecond;
    // 模板类型
    private static final int IS_NORMAL = 0;
    private static final int IS_STEPS = 1;
    private static final int IS_PRODUCT = 2;

    /**
     * 构造器
     * @param context
     * @param titles
     * @param images
     * @param buttons
     * @param icons
     */
    public HomeRecyclerAdapter(Context context, List<String> titles, List<Integer> images,
                               String[][] buttons, Integer[][] icons) {
        this.context = context;
        this.titles = titles;
        this.buttons = buttons;
        this.icons = icons;
        this.images = images;
        this.countDownSecond = 648000;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        HomeViewHolder holder;
        View itemView;
        // 正常布局
        if (viewType == IS_NORMAL) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_main, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_NORMAL);
            return holder;
        }
        // "健步达人"布局
        else if (viewType == IS_STEPS) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_steps, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_STEPS);
            return holder;
        }
        // "限量发售"布局
        else if (viewType == IS_PRODUCT) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_timer, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_PRODUCT);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder viewHolder, final int position) {
        // 公用布局: 标题图片按钮
        viewHolder.textView.setText("— "+titles.get(position)+" —");
        Glide.with(context).load(images.get(position)).into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageClickListener(position);
            }
        });
        // 设置item里面的GridView
        if (buttons[position].length == 1) {
            viewHolder.gridView.setHorizontalSpacing(0);
        }
        viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, icons[position], buttons[position]));
        viewHolder.gridView.setTag(position);
        gridItemClickListener(viewHolder);

        // "健步达人"布局
        if (viewHolder.viewType == IS_STEPS) {
            Glide.with(context).load(R.drawable.step_weather).into(viewHolder.weather);
            viewHolder.rank.setText("1");
            viewHolder.steps.setText("0");
        }
        // 限量发售布局
        else if (viewHolder.viewType == IS_PRODUCT) {
            // 设置倒计时
            viewHolder.countdownView.start(countDownSecond);
            viewHolder.countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.restart();
                }
            });
        }
    }

    private void setImageClickListener(int position) {
        // 智慧健康
        if (position == 0) {
            String url = "http://elive.clfsj.com:8989/images/healthy_description.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("智慧健康", url)));
        }
        // 健步达人
        else if (position == 1) {
            EventBus.getDefault().post(new StartBrotherEvent(SportMoreData.newInstance("健步达人", HomeFragment.step)));
        }
        // 社区活动
        else if (position == 2) {
            EventBus.getDefault().post(new StartBrotherEvent(CommunityActivityFragment.newInstance("社区活动")));
        }
        // 智慧家居
        else if (position == 3) {
            String url = "http://elive.clfsj.com:8989/images/smart_home_house.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("智能家居", url, "ZNJJ", "预约体验", true)));
        }
        // 上门服务
        else if (position == 4) {
            String url = "http://elive.clfsj.com:8989/images/come_service.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("上门服务", url, "SMFW", "我要预约", true)));
        }
        // 社区IT
        else if (position == 5) {
            String url = "http://elive.clfsj.com:8989/images/community_it_class.jpg";
            EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("IT学堂", url, "ITXT", "我要报名", true)));
        }
        // 限量发售
        else if (position == 6) {
            EventBus.getDefault().post(new StartBrotherEvent(ProductFragment.newInstance("限量销售")));
        }
        // 精品超市
        else if (position == 7) {
            EventBus.getDefault().post(new StartBrotherEvent(MarketCategoryFragment.newInstance("精品超市")));
        }
    }

    public void setCountDownTimer(String time) {
        Log.d(App.TAG, "设置倒计时时间为: "+time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = format.parse(time);
            countDownSecond = date.getTime() - System.currentTimeMillis();
//            Log.d(App.TAG, "限量发售: 时间"+date.getTime() +"");
//            Log.d(App.TAG, "限量发售: 当前"+System.currentTimeMillis() +"");
//            Log.d(App.TAG, "限量发售: 差值"+countDownSecond +"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ("健步达人".equals(titles.get(position))) {
            return IS_STEPS;
        } else if ("限量发售".equals(titles.get(position))) {
             return IS_PRODUCT;
        } else {
            return IS_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    /**
     * 点击事件
     */
    private void gridItemClickListener(HomeViewHolder viewHolder) {
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int tag = (int) parent.getTag();
                TextView text = (TextView) view.findViewById(R.id.home_item_girdView_text);
                // 智慧健康
                if (tag == 0) {
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
                else if (tag == 1) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(SportMoreData.newInstance("健步达人", HomeFragment.step)));
                    }
                }
                // 社区活动
                else if (tag == 2) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(CommunityActivityFragment.newInstance("社区活动")));
                    }
                }
                // 智慧家居
                else if (tag == 3) {
                    if (position == 0) {
                        String url = "http://elive.clfsj.com:8989/images/smart_home_house.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("智能家居", url, "ZNJJ", "预约体验", true)));
                    } else if (position == 1) {
                        String url = "http://elive.clfsj.com:8989/images/smart_home_cat_eye.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("电子猫眼", url, "DZMY", "预约体验", true)));
                    }
                }
                // 上门服务
                else if (tag == 4) {
                    if (position == 0) {
                        String url = "http://elive.clfsj.com:8989/images/come_service.jpg";
                        EventBus.getDefault().post(new StartBrotherEvent(GlobalAppointFragment.newInstance("上门服务", url, "SMFW", "我要预约", true)));
                    }
                }
                // 社区IT
                else if (tag == 5) {
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
                else if (tag == 6) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(ProductFragment.newInstance("限量销售")));
                    }
                }
                // 精品超市
                else if (tag == 7) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(MarketCategoryFragment.newInstance("商品分类")));
                    }
                }
            }
        });
    }
}
