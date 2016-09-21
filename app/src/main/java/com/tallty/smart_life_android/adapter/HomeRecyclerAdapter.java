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
import com.tallty.smart_life_android.fragment.home.CommunityIt;
import com.tallty.smart_life_android.fragment.home.CountOrder;
import com.tallty.smart_life_android.fragment.home.HealthyCheckReport;
import com.tallty.smart_life_android.fragment.home.HealthyOrderCheck;
import com.tallty.smart_life_android.fragment.home.HomeFragment;
import com.tallty.smart_life_android.fragment.home.HouseCatEye;
import com.tallty.smart_life_android.fragment.home.HouseRemoteControl;
import com.tallty.smart_life_android.fragment.home.LimitSail;
import com.tallty.smart_life_android.fragment.home.SportMoreData;
import com.tallty.smart_life_android.holder.HomeViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        // "新品上市"布局
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
        if ("健步达人".equals(titles.get(position)) && viewHolder.viewType == IS_STEPS) {
            Glide.with(context).load(R.drawable.step_weather).into(viewHolder.weather);
            viewHolder.rank.setText("1");
            viewHolder.steps.setText("0");
            // 当前日期 (改用首页调用接口获取)
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
//            Date date = new Date(System.currentTimeMillis());
//            String str = simpleDateFormat.format(date);
//            viewHolder.date.setText(str);
            System.out.println();
        } else if ("新品上市".equals(titles.get(position)) && viewHolder.viewType == IS_PRODUCT) {
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
            EventBus.getDefault().post(new StartBrotherEvent(HealthyOrderCheck.newInstance("预约体检")));
        }
        // 健步达人
        else if (position == 1) {
            EventBus.getDefault().post(new StartBrotherEvent(SportMoreData.newInstance("健步达人", HomeFragment.step)));
        }
        // 市政大厅
        else if (position == 2) {
            EventBus.getDefault().post(new ShowSnackbarEvent("即将上线，敬请期待"));
        }
        // 社区活动
        else if (position == 3) {
            EventBus.getDefault().post(new StartBrotherEvent(CountOrder.newInstance("社区活动", R.drawable.four_detail)));
        }
        // 智慧家居
        else if (position == 4) {
            EventBus.getDefault().post(new StartBrotherEvent(HouseRemoteControl.newInstance("施耐德智能家居")));
        }
        // 社区IT
        else if (position == 5) {
            EventBus.getDefault().post(new StartBrotherEvent(CommunityIt.newInstance("IT学堂")));
        }
        // 新品上市
        else if (position == 6) {
            EventBus.getDefault().post(new StartBrotherEvent(CountOrder.newInstance("新品上市", R.drawable.new_product_detail)));
        }
        // 限量发售
        else if (position == 7) {
            EventBus.getDefault().post(new StartBrotherEvent(LimitSail.newInstance("限量销售")));
        }
    }

    public void setCountDownTimer(String time) {
        Log.d(App.TAG, "时间"+time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = format.parse(time);
            countDownSecond = date.getTime() - System.currentTimeMillis();
            Log.d(App.TAG, "时间"+date.getTime() +"");
            Log.d(App.TAG, "当前"+System.currentTimeMillis() +"");
            Log.d(App.TAG, "差值"+countDownSecond +"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ("健步达人".equals(titles.get(position))) {
            return IS_STEPS;
        } else if ("新品上市".equals(titles.get(position))) {
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
                        EventBus.getDefault().post(new StartBrotherEvent(HealthyOrderCheck.newInstance("预约体检")));
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
                        EventBus.getDefault().post(new StartBrotherEvent(CountOrder.newInstance("社区活动", R.drawable.four_detail)));
                    }
                }
                // 智慧家居
                else if (tag == 4) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(HouseRemoteControl.newInstance("施耐德智能家居")));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new StartBrotherEvent(HouseCatEye.newInstance("电子猫眼")));
                    }
                }
                // 社区IT
                else if (tag == 5) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(CommunityIt.newInstance("IT学堂")));
                    } else if (position == 1) {
                        EventBus.getDefault().post(new StartBrotherEvent(CommunityIt.newInstance("在线冲印")));
                    } else if (position == 2) {
                        EventBus.getDefault().post(new StartBrotherEvent(CommunityIt.newInstance("IT服务")));
                    } else if (position == 3) {
                        EventBus.getDefault().post(new StartBrotherEvent(CommunityIt.newInstance("更多服务")));
                    }
                }
                // 新品上市
                else if (tag == 6) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(CountOrder.newInstance("新品上市", R.drawable.new_product_detail)));
                    }
                }
                // 限量发售
                else if (tag == 7) {
                    if (position == 0) {
                        EventBus.getDefault().post(new StartBrotherEvent(LimitSail.newInstance("限量销售")));
                    }
                }
            }
        });
    }
}
