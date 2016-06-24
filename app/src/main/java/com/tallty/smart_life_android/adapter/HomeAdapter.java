package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.MyGridView;
import com.tallty.smart_life_android.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/6/22.
 * 首页瀑布流RecyclerView适配器
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private String[] titles;
    private Integer[] images;
    private String[][] buttons;
    private Integer[][] icons;

    private Context context;

    public HomeAdapter(Context context, String[] titles, Integer[] images, String[][] buttons, Integer[][] icons) {
        this.context = context;
        this.titles = titles;
        this.buttons = buttons;
        this.icons = icons;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_one_btn,
                viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textView.setText("— "+titles[position]+" —");
        Glide.with(context).load(images[position]).into(viewHolder.imageView);
        // 设置item里面的GridView
        viewHolder.gridView.setAdapter(new HomeItemGridViewAdapter(context, viewHolder.gridView, icons[position], buttons[position]));
        // 给图片设置tag
        viewHolder.imageView.setTag(R.id.image_tag, position);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) v.getTag(R.id.image_tag);
                ToastUtil.show(images[i]);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    /**
     * ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        MyGridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_home_list_title);
            imageView = (ImageView) itemView.findViewById(R.id.item_home_list_image);
            gridView = (MyGridView) itemView.findViewById(R.id.item_home_list_gridView);
        }
    }
}
