package com.tallty.smart_life_android.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.model.Product;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by kang on 2017/1/10.
 * 限量销售适配器
 */

public class PromotionAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    private boolean isCancel = true;
    private Handler mHandler = new Handler();
    private Timer mTimer;
    private final List<Product> products = new ArrayList<>();

    public PromotionAdapter(int layoutResId, List<Product> data) {
        super(layoutResId, data);
        this.products.addAll(data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, Product product) {
        final CountdownView countdownView = baseViewHolder.getView(R.id.group_buy_timer);
        // 富文本显示
        int str_length = String.valueOf(product.getSales()).length();
        SpannableString spannableString = new SpannableString(product.getSales()+"件已付款");
        spannableString.setSpan(new AbsoluteSizeSpan(16, true), 0, str_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.orange)), 0, str_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String state = product.isPromotionEnable() ? "还剩：" : "已结束";
        baseViewHolder
                .setText(R.id.promotion_text, state)
                .setText(R.id.promotion_people_count, spannableString);
        // 显示详情图
        Glide.with(mContext)
                .load(product.getProductBanners().get(0).getUrl())
                .placeholder(R.drawable.product_placeholder)
                .error(R.drawable.image_error)
                .into((ImageView) baseViewHolder.getView(R.id.promotion_image));
        // 设置倒计时
        if (product.isPromotionEnable()) {
            countdownView.start(getCountDownMills(product.getEndTime()));
            countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.stop();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Const.INT, baseViewHolder.getAdapterPosition());
                    EventBus.getDefault().post(new TransferDataEvent(bundle, "团购结束"));
                }
            });
        } else {
            baseViewHolder
                .setVisible(R.id.promotion_state, true)
                .setVisible(R.id.promotion_text, false)
                .setVisible(R.id.group_buy_timer, false);
        }

    }

    // 获取截止日期距现在的时间间隔毫秒数
    private long getCountDownMills(String time) {
        if (time == null) return 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000+08:00'");
        try {
            Date date = format.parse(time);
            return date.getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 获取截止日期的毫秒数
    private long getEndTimeMills(String endTime) {
        if (endTime == null) return System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.000+08:00'");
        try {
            Date date = format.parse(endTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void startRefreshTime() {
        if (!isCancel) return;

        if (null != mTimer) {
            mTimer.cancel();
        }

        isCancel = false;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mRefreshTimeRunnable);
            }
        }, 0, 10);
    }

    public void cancelRefreshTime() {
        isCancel = true;
        if (null != mTimer) {
            mTimer.cancel();
        }
        mHandler.removeCallbacks(mRefreshTimeRunnable);
    }

    private Runnable mRefreshTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (products.size() == 0) return;

            synchronized (products) {
                long currentTime = System.currentTimeMillis();

                int position = 0;
                for (Product product : products) {
                    if (currentTime >= getEndTimeMills(product.getEndTime())) {
                        // 倒计时结束
                        products.remove(product);
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(position);
                    }
                    position++;
                }
            }
        }
    };
}
