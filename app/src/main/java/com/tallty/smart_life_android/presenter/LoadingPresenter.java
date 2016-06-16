package com.tallty.smart_life_android.presenter;

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
    public void onLoading() {
        String uri = "res:///"+R.drawable.loading;
        iLoadingView.showImage(uri);
        // 根据登录情况处理
        iLoadingView.changeAty();
    }
}
