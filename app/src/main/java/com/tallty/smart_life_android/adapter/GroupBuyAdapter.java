package com.tallty.smart_life_android.adapter;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by kang on 2017/1/10.
 * 限量销售适配器
 */

public class GroupBuyAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    private boolean isCancel = true;
    private Handler mHandler = new Handler();
    private Timer mTimer;
    private final List<Product> products = new ArrayList<>();

    public GroupBuyAdapter(int layoutResId, List<Product> data) {
        super(layoutResId, data);
        this.products.addAll(data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Product product) {
        CountdownView countdownView = baseViewHolder.getView(R.id.group_buy_timer);
        // 富文本显示
        int str_length = String.valueOf(product.getSales()).length();
        SpannableString spannableString = new SpannableString(product.getSales()+"件已付款");
        spannableString.setSpan(new AbsoluteSizeSpan(16, true), 0, str_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.orange)), 0, str_length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        baseViewHolder.setText(R.id.group_buy_people, spannableString);
        // 设置倒计时
        countdownView.start(product.getCountDown());
        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                cv.restart();
            }
        });
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
                    if (currentTime >= product.getEndTime()) {
                        // 倒计时结束
                        product.setCountDown(0);
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
