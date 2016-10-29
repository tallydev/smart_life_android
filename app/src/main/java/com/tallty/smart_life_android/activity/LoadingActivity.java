package com.tallty.smart_life_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.utils.DbUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoadingActivity extends BaseActivity {
    private ImageView loadingImage;
    private CountDownTimer timer;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        loadingImage = getViewById(R.id.loading_image);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Glide.with(this)
                .load(R.drawable.loading)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(loadingImage);

        // 检查是否为新的一天, 并处理步数
        checkStepWithDate();
        // 3秒进入应用
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                LoadingActivity.this.finish();
            }
        };
        timer.start();
    }

    /**
     * 检测今天是不是新的一天
     */
    private void checkStepWithDate() {
        String current_date = getTodayDate();
        DbUtils.createDb(this, Const.DB_NAME);
        //获取当天的数据，用于展示
        ArrayList list = DbUtils.getQueryByWhere(Step.class, "date", new String[]{current_date});
        if (list.size() == 0 || list.isEmpty()) {
            // 查不到今天的记录 => 新的一天, 清除数据
            clearSharedStep();
        } else {
            Log.i(App.TAG, "不是新的一天");
        }
    }

    /**
     * 清楚sharePreferences保存的逐小时步数
     */
    public void clearSharedStep() {
        String sharedKey;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                sharedKey = "0" + i;
            } else {
                sharedKey = String.valueOf(i);
            }
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putFloat(sharedKey, 0.0f);
            editor.apply();
        }
        Log.i(App.TAG, "旧的逐小时步数被清空了");
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        loadingImage.setImageDrawable(null);
    }
}
