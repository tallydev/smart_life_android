package com.tallty.smart_life_android.presenter;

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.view.ILoadingView;

/**
 * Created by kang on 16/6/15.
 *
 */
public class LoadingPresenter implements ILoadingPresenter {
    private ILoadingView iLoadingView;

    public LoadingPresenter(ILoadingView iLoadingView) {
        this.iLoadingView = iLoadingView;
    }

    @Override
    public void onShowImage() {
        String uri = "res:///"+R.drawable.loading;
        iLoadingView.showImage(uri);
    }
}
