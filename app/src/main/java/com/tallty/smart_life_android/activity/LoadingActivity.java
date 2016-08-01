package com.tallty.smart_life_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;

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

        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                LoadingActivity.this.finish();
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        loadingImage.setImageDrawable(null);
        super.onDestroy();
    }
}
