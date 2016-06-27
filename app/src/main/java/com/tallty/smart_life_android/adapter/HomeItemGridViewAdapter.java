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
import com.tallty.smart_life_android.custom.MyGridView;
import com.tallty.smart_life_android.holder.BaseViewHolder;

/**
 * Created by kang on 16/6/23.
 * 首页item中的GridView适配器
 */
public class HomeItemGridViewAdapter extends BaseAdapter {
    private Context context;
    private Integer[] icons;
    private String[] texts;
    private MyGridView gridView;

    public HomeItemGridViewAdapter(Context context, MyGridView gridView, Integer[] icons, String[] texts) {
        this.context = context;
        this.icons = icons;
        this.texts = texts;
        this.gridView = gridView;
    }
    @Override
    public int getCount() {
        int count = texts.length;
        if (count % 2 == 0) {
            return count;
        } else {
            return count + 1;
        }
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
        }
        // 找到组件
        ImageView icon = BaseViewHolder.get(convertView, R.id.home_item_girdView_icon);
        TextView text = BaseViewHolder.get(convertView, R.id.home_item_girdView_text);
        // 赋值
        if (position <= texts.length - 1) {
            text.setText(texts[position].equals("更多") ? "• • •" : texts[position]);
            if (position <= icons.length - 1 ) {
                Glide.with(context).load(icons[position]).centerCrop().into(icon);
            } else {
                icon.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}


