package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.holder.BaseViewHolder;

/**
 * Created by kang on 16/6/23.
 * 首页item中的GridView适配器
 */
public class HomeItemGridViewAdapter extends BaseAdapter {
    private Context context;
    private Integer[] icons;
    private String[] texts;
    private GridView gridView;

    private int height;

    public HomeItemGridViewAdapter(Context context, GridView gridView, Integer[] icons, String[] texts) {
        this.context = context;
        this.icons = icons;
        this.texts = texts;
        this.gridView = gridView;
    }
    @Override
    public int getCount() {
        int count = icons.length;
        int remainder = count % 2;
        int value = 0;
        if (count <= 2) {
            value = 2;
        } else if (count>2 && remainder == 0) {
            value = count;
        } else if (count>2 && remainder != 0) {
            value = 2 - remainder + count;
        }
        return value;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_grid_view, parent, false);
//            height = convertView.getHeight();
//            int col_num = icons.length / 2 + icons.length % 2;
//            gridView.setMinimumHeight(col_num*height);
        }
        // 找到组件
        ImageView icon = BaseViewHolder.get(convertView, R.id.home_item_girdView_icon);
        TextView text = BaseViewHolder.get(convertView, R.id.home_item_girdView_text);
        // 赋值
        if (position+1 <= icons.length) {
            Glide.with(context).load(icons[position]).centerCrop().into(icon);
            text.setText(texts[position]);
        } else {
            Glide.with(context).load(R.mipmap.ic_launcher).centerCrop().into(icon);
            text.setText("...");
        }

        return convertView;
    }
}
