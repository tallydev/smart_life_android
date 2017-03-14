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
    private String source = "";
    private String title = "";
    private boolean isUrl = true;
    private WebView web_view;

    public static WebViewFragment newInstance(String source, String title, boolean isUrl) {
        Bundle args = new Bundle();
        args.putString(Const.STRING, source);
        args.putString(Const.FRAGMENT_NAME, title);
        args.putBoolean(Const.BOOLEAN, isUrl);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            source = args.getString(Const.STRING);
            title = args.getString(Const.FRAGMENT_NAME);
            isUrl = args.getBoolean(Const.BOOLEAN);
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
        web_view = getViewById(R.id.web_view);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        loadWebContent();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 通过连接加载页面
     */
    private void loadWebContent() {
        web_view.getSettings().setJavaScriptEnabled(true);
        if (isUrl) {
            web_view.loadUrl(source);
        } else {
            // TODO: 2017/3/15 这里可能需要显示一个网页
            String htmlText = "<!DOCTYPE html>\n" +
                    "<html lang=\"zh-CN\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\">\n" +
                    "    <title>新闻详情</title>\n" +
                    "    <style>img { width: 100%; }</style>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                        source +
                    "  </body>\n" +
                    "</html>" + "";

            web_view.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);
        }
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
}
