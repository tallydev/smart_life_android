package com.tallty.smart_life_android.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;

/**
 * Created by kang on 16/6/22.
 * 首页banner图HolderView
 * 使用Glide来显示网络图片
 */
public class NetworkImageBannerHolder implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context)
                .load(data)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_error)
                .into(imageView);
    }
}
