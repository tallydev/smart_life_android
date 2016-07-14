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
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.fragment.home.LimitSailShow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/14.
 * 首页-限量销售-适配器
 */
public class HomeLimitSailAdapter extends RecyclerView.Adapter<HomeLimitSailAdapter.ProductViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<Integer> photos;
    private Context context;

    public HomeLimitSailAdapter(Context context, ArrayList<String> names,
                                ArrayList<String> prices, ArrayList<Integer> photos) {
        this.context = context;
        this.names = names;
        this.prices = prices;
        this.photos = photos;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_limit_sail, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Glide.with(context).load(photos.get(position)).into(holder.photo);
        holder.name.setText(names.get(position));
        holder.price.setText(prices.get(position));
        if (position == getItemCount()-1) {
            holder.line.setVisibility(View.GONE);
        }
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(LimitSailShow.newInstance("商品详情")));
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView name;
        private TextView price;
        private Button detail;
        private View line;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_name);
            price = (TextView) itemView.findViewById(R.id.product_price);
            detail = (Button) itemView.findViewById(R.id.detail_btn);
            photo = (ImageView) itemView.findViewById(R.id.product_photo);
            line = itemView.findViewById(R.id.product_line);
        }
    }
}
