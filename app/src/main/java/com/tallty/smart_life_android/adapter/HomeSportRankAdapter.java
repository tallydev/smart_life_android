package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.custom.GlideCircleTransform;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/18.
 * 首页-健康达人-适配器
 */

public class HomeSportRankAdapter extends RecyclerView.Adapter<HomeSportRankAdapter.RankViewHolder> {
    private ArrayList<String> urls;
    private ArrayList<String> names;
    private ArrayList<Integer> numbers;
    private ArrayList<Integer> states;
    private ArrayList<Integer> praise_counts;
    private Context context;

    public HomeSportRankAdapter( Context context, ArrayList<String> urls,
                                 ArrayList<String> names, ArrayList<Integer> numbers,
                                 ArrayList<Integer> states, ArrayList<Integer> praise_counts) {
        this.context = context;
        this.urls = urls;
        this.names = names;
        this.numbers = numbers;
        this.states = states;
        this.praise_counts = praise_counts;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_step_rank, parent, false);
        return new RankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, final int position) {
        holder.index.setText(String.valueOf(position+1));
        Glide.with(context).load(urls.get(position))
                .transform(new GlideCircleTransform(context))
                .into(holder.photo);
        holder.name.setText(names.get(position));
        holder.step_number.setText(String.valueOf(numbers.get(position)));
        holder.praise_count.setText(String.valueOf(praise_counts.get(position)));
        // 0: 没有赞, 1: 有他人的赞, 2: 我的赞
        if (0 == states.get(position)) {
            holder.praise.setBackgroundResource(R.drawable.praise_empty);
        } else if (1 == states.get(position)) {
            holder.praise.setBackgroundResource(R.drawable.praise_gray);
        } else if (2 == states.get(position)) {
            holder.praise.setBackgroundResource(R.drawable.praise_orange);
        }

        if (getItemCount() == position + 1)
            holder.line.setBackgroundResource(R.color.white);

        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/7/19 点赞
                changeData(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void changeData(int position) {
        int count = praise_counts.get(position);
        if (states.get(position) == 0) {
            states.set(position, 2);
            praise_counts.set(position, ++count);
        } else if (states.get(position) == 1) {
            states.set(position, 2);
            praise_counts.set(position, ++count);
        } else {
            if (count > 1) {
                states.set(position, 1);
                praise_counts.set(position, --count);
            } else {
                states.set(position, 0);
                praise_counts.set(position, --count);
            }
        }
        notifyItemChanged(position);
    }

    class RankViewHolder extends RecyclerView.ViewHolder {
        private TextView index;
        private ImageView photo;
        private TextView name;
        private TextView step_number;
        private Button praise;
        private TextView praise_count;
        private View line;

        public RankViewHolder(View itemView) {
            super(itemView);
            index = (TextView) itemView.findViewById(R.id.rank_index);
            photo = (ImageView) itemView.findViewById(R.id.rank_photo);
            name = (TextView) itemView.findViewById(R.id.rank_name);
            step_number = (TextView) itemView.findViewById(R.id.step_number);
            praise = (Button) itemView.findViewById(R.id.praise_button);
            praise_count = (TextView) itemView.findViewById(R.id.praise_count);
            line = itemView.findViewById(R.id.line);
        }
    }
}
