package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.MyGridView;
import com.tallty.smart_life_android.holder.HomeBannerHolderView;
import com.tallty.smart_life_android.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kang on 16/6/22.
 * 首页瀑布流RecyclerView适配器
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> implements OnItemClickListener {
    private Context context;

    private List<String> titles;
    private List<Integer> images;
    private String[][] buttons;
    private Integer[][] icons;

    private static final int IS_NORMAL = 0;
    private static final int IS_HEADER = 1;

    // banner图数据
    private Integer[] imagesUrl = {
            R.drawable.banner_one, R.drawable.community_activity
    };

    // 构造函数
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
            viewHolder.textView.setText("— "+titles.get(position-1)+" —");
            Glide.with(context).load(images.get(position-1)).into(viewHolder.imageView);
            // 设置item里面的GridView
            viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, viewHolder.gridView, icons[position-1], buttons[position-1]));
            // 给图片设置tag
//             viewHolder.imageView.setTag(R.id.image_tag, position-1);
//             viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                     int i = (int) v.getTag(R.id.image_tag);
//                     ToastUtil.show(images.get(i));
//                 }
//             });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        } else {
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
     * ViewHolder
     */
    class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        MyGridView gridView;

        ConvenientBanner banner;

        int viewType;

        public HomeViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == IS_HEADER) {
                banner = (ConvenientBanner) itemView.findViewById(R.id.homeBanner);
            } else if (viewType == IS_NORMAL) {
                textView = (TextView) itemView.findViewById(R.id.item_home_list_title);
                imageView = (ImageView) itemView.findViewById(R.id.item_home_list_image);
                gridView = (MyGridView) itemView.findViewById(R.id.item_home_list_gridView);
            }
        }
    }
}
