package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ShowSnackbarEvent;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.CycleFragment;
import com.tallty.smart_life_android.fragment.home.FiveCatEye;
import com.tallty.smart_life_android.fragment.home.FiveRemoteControl;
import com.tallty.smart_life_android.fragment.home.FourDetail;
import com.tallty.smart_life_android.fragment.home.OneCheckReport;
import com.tallty.smart_life_android.fragment.home.OneOrderCheck;
import com.tallty.smart_life_android.fragment.home.TwoMoreData;
import com.tallty.smart_life_android.holder.HomeViewHolder;

import org.greenrobot.eventbus.EventBus;

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
    // 模板类型
    private static final int IS_NORMAL = 0;
    private static final int IS_STEPS = 1;
    private static final int IS_PRODUCT = 2;
    // banner图数据
    private Integer[] imagesUrl = { R.drawable.banner_one, R.drawable.community_activity };

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
        // "健身达人"布局
        else if (viewType == IS_STEPS) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_steps, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_STEPS);
            return holder;
        }
        // "新品上市"布局
        else if (viewType == IS_PRODUCT) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_home_timer, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_PRODUCT);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder viewHolder, int position) {
        // 公用布局: 标题图片按钮
        viewHolder.textView.setText("— "+titles.get(position)+" —");
        Glide.with(context).load(images.get(position)).into(viewHolder.imageView);
        // 设置item里面的GridView
        if (buttons[position].length == 1) {
            viewHolder.gridView.setHorizontalSpacing(0);
        }
        viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, icons[position], buttons[position]));
        viewHolder.gridView.setTag(position);
        gridItemClickListener(viewHolder);

        // "健身达人"布局
        if (titles.get(position).equals("健身达人") && viewHolder.viewType == IS_STEPS) {
            Glide.with(context).load(R.drawable.step_weather).into(viewHolder.weather);
            viewHolder.rank.setText("1");
            viewHolder.steps.setText("0");
            // 当前日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date date = new Date(System.currentTimeMillis());
            String str = simpleDateFormat.format(date);
            viewHolder.date.setText(str);
        } else if (titles.get(position).equals("新品上市") && viewHolder.viewType == IS_PRODUCT) {
            viewHolder.countdownView.start(15550000);
            viewHolder.countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.start(95000000);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (titles.get(position).equals("健身达人")) {
            return IS_STEPS;
        } else if (titles.get(position).equals("新品上市")) {
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
                        EventBus.getDefault().post(new StartBrotherEvent(OneOrderCheck.newInstance("预约体检")));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new StartBrotherEvent(OneCheckReport.newInstance("健康报告")));
                    } else if (position == 2) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("待确认"));
                    }
                }
                // 健身达人
                else if (tag == 1) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(TwoMoreData.newInstance("健身达人")));
                    }
                }
                // 市政大厅
                else if (tag == 2) {
                    if (position == 0) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    } else if (position == 2) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    } else if (position == 3) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    } else if (position == 4) {
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    } else if (position == 5){
                        EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
                    }
                }
                // 社区活动
                else if (tag == 3) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(FourDetail.newInstance("社区活动")));
                    }
                }
                // 智慧家居
                else if (tag == 4) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(FiveRemoteControl.newInstance("施耐德智能家居")));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new StartBrotherEvent(FiveCatEye.newInstance("电子猫眼")));
                    }
                }
                // 社区IT
                else if (tag == 5) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(CycleFragment.newInstance(1, "IT学堂")));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new StartBrotherEvent(CycleFragment.newInstance(1, "在线冲印")));
                    } else if (position == 2) {
                        EventBus.getDefault().post(new StartBrotherEvent(CycleFragment.newInstance(1, "IT服务")));
                    } else if (position == 3) {
                        EventBus.getDefault().post(new StartBrotherEvent(CycleFragment.newInstance(1, "社区IT-更多")));
                    }
                }
                // 新品上市
                else if (tag == 6) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(CycleFragment.newInstance(1, "我要预约")));
                    }
                }
                // 限量发售
                else if (tag == 7) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(CycleFragment.newInstance(1, "更多臻品")));
                    }
                }
            }
        });
    }
}
