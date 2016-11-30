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
import com.tallty.smart_life_android.fragment.home.ProductShowFragment;
import com.tallty.smart_life_android.model.Product;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/14.
 * 首页-限量销售-适配器
 */
public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder> {
    private ArrayList<Product> products;

    private Context context;

    public HomeProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_limit_sail, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product product = products.get(position);

//        Glide.with(context).load(product.getThumb()).into(holder.photo);
        Glide.with(context).load(product.getThumbId()).into(holder.photo);
        holder.name.setText(product.getTitle());
        holder.price.setText("今日价格: "+product.getPrice()+"0");

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartBrotherEvent(ProductShowFragment.newInstance(product)));
            }
        });

        if (position == getItemCount()-1) {
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView name;
        private TextView price;
        private Button detail;
        private View line;

        ProductViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_name);
            price = (TextView) itemView.findViewById(R.id.product_price);
            detail = (Button) itemView.findViewById(R.id.detail_btn);
            photo = (ImageView) itemView.findViewById(R.id.product_photo);
            line = itemView.findViewById(R.id.product_line);
        }
    }
}
