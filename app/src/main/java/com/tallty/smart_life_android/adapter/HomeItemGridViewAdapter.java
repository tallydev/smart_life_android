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
import com.tallty.smart_life_android.custom.MyGridView;
import com.tallty.smart_life_android.holder.BaseViewHolder;
import com.tallty.smart_life_android.model.Home;
import com.tallty.smart_life_android.model.HomeBlock;

/**
 * Created by kang on 16/6/23.
 * 首页itemView中的GridView适配器
 */
public class HomeItemGridViewAdapter extends BaseAdapter {
    private Context context;
    private HomeBlock block;

    public HomeItemGridViewAdapter(Context context, HomeBlock block) {
        this.context = context;
        this.block = block;
    }

    @Override
    public int getCount() {
        if (block.getSubIcons() == null) return 0;
        int count = block.getSubTitles().size();
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

        if (position < block.getSubTitles().size()) {
            text.setText(block.getSubTitles().get(position));
            if ( position < block.getSubIcons().size() ) {
                Glide
                        .with(context)
                        .load(block.getSubIcons().get(position))
                        .centerCrop().into(icon);
            } else {
                icon.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}


