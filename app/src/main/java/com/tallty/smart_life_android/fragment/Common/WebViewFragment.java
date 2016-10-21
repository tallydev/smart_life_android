package com.tallty.smart_life_android.fragment.Common;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * 通用WebView Fragment
 */
public class WebViewFragment extends BaseBackFragment {
    private String url = "";
    private String title = "";
    private WebView web_view;

    public static WebViewFragment newInstance(String url, String title) {
        Bundle args = new Bundle();
        args.putString(Const.STRING, url);
        args.putString(Const.FRAGMENT_NAME, title);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            url = args.getString(Const.STRING);
            title = args.getString(Const.FRAGMENT_NAME);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_web_view;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(title);
    }

    @Override
    protected void initView() {
        createWebView();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        configWebView();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onFragmentPop() {
        super.onFragmentPop();
        clearWebViewResource();
    }

    /**
     * 创建webView
     */
    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void createWebView() {
        RelativeLayout webView_container = getViewById(R.id.web_view_container);
        web_view = new WebView(App.getInstance());
        web_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        web_view.setScrollBarSize(0);
        webView_container.addView(web_view);
    }

    /**
     * 设置webView
     */
    private void configWebView() {
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.loadUrl(url);
        // 进度
        showProgress("载入中...");
        // 设置webView进度
        web_view.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    hideProgress();
                }
            }
        });
        //在webView里打开新链接
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 清除webView
     */
    public void clearWebViewResource() {
        if (web_view != null) {
            Log.d(App.TAG,"Clear webview's resources");
            web_view.removeAllViews();
            // in android 5.1(sdk:21) we should invoke this to avoid memory leak
            // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
            // android-5.1-webview-memory-leak.html)
            ((ViewGroup) web_view.getParent()).removeView(web_view);
            web_view.setTag(null);
            web_view.clearHistory();
            web_view.destroy();
            web_view = null;
        }
    }
}
