package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.holder.BaseViewHolder;

import java.util.List;

/**
 * Created by kang on 16/6/29.
 * 社区-网上缴费
 */
public class CommunityGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> icons;
    private List<String> names;

    public CommunityGridViewAdapter(Context context, List<Integer> icons, List<String> names) {
        this.context = context;
        this.icons = icons;
        this.names = names;
    }

    @Override
    public int getCount() {
        return icons.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_community_grrid_view, parent, false);
        }

        ImageView icon = BaseViewHolder.get(convertView, R.id.item_community_icon);
        TextView name = BaseViewHolder.get(convertView, R.id.item_community_name);

        Glide.with(context).load(icons.get(position)).into(icon);
        name.setText(names.get(position));

        return convertView;
    }
}
