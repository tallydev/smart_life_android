package com.tallty.smart_life_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.presenter.ILoadingPresenter;
import com.tallty.smart_life_android.presenter.LoadingPresenter;
import com.tallty.smart_life_android.view.ILoadingView;

public class LoadingActivity extends BaseActivity implements ILoadingView {
    private ILoadingPresenter iLoadingPresenter;
    private SimpleDraweeView loadingImage;
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
        Uri imageUri = Uri.parse(uri);
        loadingImage.setImageURI(imageUri);
    }

    @Override
    public void changeAty() {
        timer = new CountDownTimer(3000, 1000) {
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
        super.onDestroy();
    }
}
