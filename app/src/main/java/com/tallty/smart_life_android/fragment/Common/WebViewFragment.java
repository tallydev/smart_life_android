package com.tallty.smart_life_android.fragment.Common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.News;

/**
 * 通用WebView Fragment
 */
public class WebViewFragment extends BaseBackFragment {
    private News news;
    private String sortName = "新闻动态";
    private WebView web_view;

    public static WebViewFragment newInstance(News news, String sortName) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, news);
        args.putString(Const.STRING, sortName);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            news = (News) args.getSerializable(Const.OBJECT);
            sortName = args.getString(Const.STRING);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_web_view;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("新闻详情");
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
        String htmlText = "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\">\n" +
                "  <title>新闻详情</title>\n" +
                "  <style>img { max-width: 100%; }</style> " +
                "</head>\n" +
                "\n" +
                "<body style=\"padding: 0;margin: 0;overflow: hidden;\">\n" +
                "  <div style=\"position: relative;padding: 20px 15px 15px;background-color: #fff;\">\n" +
                "    <h2 style=\"font-size:22px;margin:0px 0px 10px;line-height: 1.4;font-weight: 400;\">"+
                       news.getTitle() +
                "    </h2>\n" +
                "    <p style=\"margin:0 0 20px;font-size:14px;line-height:2;color: #8c8c8c;\">"+
                       news.getCreatedTime() + "&nbsp;&nbsp;" + sortName +
                "    </p>\n" +
                "    <p style=\"line-height: 25.6px; white-space: normal; text-align: center;margin:0;\">\n<p>\n" +
                     news.getContent()  +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
        web_view.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);
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
