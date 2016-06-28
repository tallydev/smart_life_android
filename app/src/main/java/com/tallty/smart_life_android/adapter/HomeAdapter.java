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
import com.tallty.smart_life_android.holder.HomeViewHolder;
import com.tallty.smart_life_android.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kang on 16/6/22.
 * 首页瀑布流RecyclerView适配器
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private Context context;
    // 数据源
    private List<String> titles;
    private List<Integer> images;
    private String[][] buttons;
    private Integer[][] icons;
    private Integer step;
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
    public HomeAdapter(Context context, List<String> titles, List<Integer> images,
                       String[][] buttons, Integer[][] icons, Integer step) {
        this.context = context;
        this.titles = titles;
        this.buttons = buttons;
        this.icons = icons;
        this.images = images;
        this.step = step;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        HomeViewHolder holder;
        // "健身达人"布局
        if (viewType == IS_STEPS) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_steps, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_STEPS);
            return holder;
        }
        // 正常布局
        else if (viewType == IS_NORMAL) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_main, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_NORMAL);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder viewHolder, int position) {
        // 设置item里面的GridView
        if (buttons[position].length == 1) {
            viewHolder.gridView.setHorizontalSpacing(0);
        }
        viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, icons[position], buttons[position]));
        viewHolder.gridView.setTag(position);
        gridItemClickListener(viewHolder);
        // 正常布局
        if (viewHolder.viewType == IS_NORMAL) {
            viewHolder.textView.setText("— "+titles.get(position)+" —");
            Glide.with(context).load(images.get(position)).into(viewHolder.imageView);
        }
        // "健身达人"布局
        else if (titles.get(position).equals("健身达人") && viewHolder.viewType == IS_STEPS) {
            viewHolder.steps_title.setText("— "+titles.get(position)+" —");
            Glide.with(context).load(images.get(position)).into(viewHolder.steps_image);
            Glide.with(context).load(R.drawable.step_weather).into(viewHolder.weather);
            // 当前日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            Date date = new Date(System.currentTimeMillis());
            String str = simpleDateFormat.format(date);
            viewHolder.date.setText(str);
            viewHolder.rank.setText("1");
            viewHolder.steps.setText(String.valueOf(step));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (titles.get(position).equals("健身达人")) {
            return IS_STEPS;
        } else {
            return IS_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    /**
     * RecyclerView 点击事件
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
                        ToastUtil.show(text.getText());
                    } else if (position == 1) {
                        ToastUtil.show(text.getText());
                    } else if (position == 2) {
                        ToastUtil.show(text.getText());
                    } else if (position == 3) {
                        ToastUtil.show(text.getText());
                    }
                }
                // 健身达人
                else if (tag == 1) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    }
                }
                // 市政大厅
                else if (tag == 2) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    } else if (position == 1) {
                        ToastUtil.show(text.getText());
                    } else if (position == 2) {
                        ToastUtil.show(text.getText());
                    } else if (position == 3) {
                        ToastUtil.show(text.getText());
                    } else if (position == 4) {
                        ToastUtil.show(text.getText());
                    } else if (position == 5){
                        ToastUtil.show(text.getText());
                    }
                }
                // 社区活动
                else if (tag == 3) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    }
                }
                // 智慧家居
                else if (tag == 4) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    } else if (position == 1) {
                        ToastUtil.show(text.getText());
                    }
                }
                // 社区IT
                else if (tag == 5) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    } else if (position == 1) {
                        ToastUtil.show(text.getText());
                    } else if (position == 2) {
                        ToastUtil.show(text.getText());
                    } else if (position == 3) {
                        ToastUtil.show(text.getText());
                    }
                }
                // 新品上市
                else if (tag == 6) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    }
                }
                // 限量发售
                else if (tag == 7) {
                    if (position == 0) {
                        ToastUtil.show(text.getText());
                    }
                }
            }
        });
    }
}
