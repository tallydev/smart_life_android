package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.MyGridView;
import com.tallty.smart_life_android.holder.HomeBannerHolderView;
import com.tallty.smart_life_android.utils.DpUtil;
import com.tallty.smart_life_android.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kang on 16/6/22.
 * 首页瀑布流RecyclerView适配器
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> implements OnItemClickListener {
    private Context context;
    // 数据源
    private List<String> titles;
    private List<Integer> images;
    private String[][] buttons;
    private Integer[][] icons;
    // 模板类型
    private static final int IS_HEADER = 0;
    private static final int IS_NORMAL = 1;
    private static final int IS_STEPS = 2;
    private static final int IS_PRODUCT = 3;
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
    public HomeAdapter(Context context, List<String> titles, List<Integer> images, String[][] buttons, Integer[][] icons) {
        this.context = context;
        this.titles = titles;
        this.buttons = buttons;
        this.icons = icons;
        this.images = images;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        HomeViewHolder holder;
        if (viewType == IS_HEADER) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_main_header, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_HEADER);
            return holder;
        } else if (viewType == IS_NORMAL) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_main, viewGroup, false);
            holder = new HomeViewHolder(itemView, IS_NORMAL);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder viewHolder, int position) {
        if (position == 0 && viewHolder.viewType == IS_HEADER) {
            List<Integer> networkImages = Arrays.asList(imagesUrl);
            viewHolder.banner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new HomeBannerHolderView();
                }
            }, networkImages)
                    .setPageIndicator(new int[] {R.mipmap.banner_indicator, R.mipmap.banner_indicator_focused})
                    .setOnItemClickListener(this);
        } else if (position != 0 && viewHolder.viewType == IS_NORMAL) {
            int i = position - 1;
            viewHolder.textView.setText("— "+titles.get(i)+" —");
            Glide.with(context).load(images.get(i)).into(viewHolder.imageView);
            // 设置item里面的GridView
            viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, viewHolder.gridView, icons[i], buttons[i]));
            viewHolder.gridView.setTag(i);
            gridItemClickListener(viewHolder);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        }
//        else if (position == 1){
//            return IS_NORMAL;
//        }
//        else if (position == 2) {
//            return IS_STEPS;
//        } else if (position == 3) {
//            return IS_PRODUCT;
//        }
        else {
            return IS_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return titles.size() + 1;
    }

    /**
     * 这是header的布局
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(HomeViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    /**
     * banner点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        ToastUtil.show("点击了第"+position+"个");
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

    /**
     * ViewHolder
     */
    class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        MyGridView gridView;
        LinearLayout recyclerItemLayout;

        ConvenientBanner banner;

        int viewType;

        public HomeViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == IS_HEADER) {
                banner = (ConvenientBanner) itemView.findViewById(R.id.homeBanner);
            } else if (viewType == IS_NORMAL) {
                recyclerItemLayout = (LinearLayout) itemView.findViewById(R.id.home_item_list_ly);
                textView = (TextView) itemView.findViewById(R.id.item_home_list_title);
                imageView = (ImageView) itemView.findViewById(R.id.item_home_list_image);
                gridView = (MyGridView) itemView.findViewById(R.id.item_home_list_gridView);
            }
        }
    }
}
