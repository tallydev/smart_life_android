package com.tallty.smart_life_android.ui;

import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.presenter.ILoadingPresenter;
import com.tallty.smart_life_android.presenter.LoadingPresenter;
import com.tallty.smart_life_android.view.ILoadingView;

public class LoadingActivity extends BaseActivity implements ILoadingView {
    private ILoadingPresenter iLoadingPresenter;
    private SimpleDraweeView loadingImage;

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
        iLoadingPresenter = new LoadingPresenter(this);
        iLoadingPresenter.onShowImage();
    }

    @Override
    public void showImage(String uri) {
        Uri imageUri = Uri.parse(uri);
        loadingImage.setImageURI(imageUri);
    }
}
