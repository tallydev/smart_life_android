package com.tallty.smart_life_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.presenter.ILoadingPresenter;
import com.tallty.smart_life_android.presenter.LoadingPresenter;
import com.tallty.smart_life_android.view.ILoadingView;

public class LoadingActivity extends BaseActivity implements ILoadingView {
    private ILoadingPresenter iLoadingPresenter;
    private ImageView loadingImage;
    private TextView timeText;
    private CountDownTimer timer;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        loadingImage = getViewById(R.id.loading_image);
        timeText = getViewById(R.id.loading_time);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 初始化presenter
        iLoadingPresenter = new LoadingPresenter(this);
        iLoadingPresenter.onLoading();
    }

    @Override
    public void showImage(String uri) {
        Glide.with(this)
                .load(R.drawable.loading)
                .centerCrop()
                .into(loadingImage);
    }

    @Override
    public void changeAty() {
        timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText(millisUntilFinished/1000 + "秒");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
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
