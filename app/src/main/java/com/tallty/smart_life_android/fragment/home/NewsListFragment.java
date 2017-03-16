package com.tallty.smart_life_android.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.NewsListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.fragment.Common.WebViewFragment;
import com.tallty.smart_life_android.model.News;
import com.tallty.smart_life_android.model.NewsList;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 2017/3/14.
 * 新闻列表
 */

public class NewsListFragment extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private String title = "新闻列表";

    private RecyclerView recyclerView;
    private NewsListAdapter adapter;

    private int sortId = 1;
    private ArrayList<News> newsList = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

    public static NewsListFragment newInstance(String title, int sortId) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putInt(Const.INT, sortId);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(Const.FRAGMENT_NAME);
            sortId = args.getInt(Const.INT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_common_list;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(title);
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.common_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        fetchNews();
    }

    @Override
    public void onClick(View v) {

    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new NewsListAdapter(R.layout.item_home_news, newsList);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(adapter);
        // 点击事件
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                start(WebViewFragment.newInstance(newsList.get(i), newsList.get(i).getTitle()));
            }
        });
        // 加载更多
        adapter.setOnLoadMoreListener(this);
    }

    public void fetchNews() {
        Engine
            .authService(shared_token, shared_phone)
            .getNewsBySortId(sortId, current_page, per_page)
            .enqueue(new Callback<NewsList>() {
                @Override
                public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                    if (response.isSuccessful()) {
                        current_page = response.body().getCurrentPage();
                        total_pages = response.body().getTotalPages();
                        adapter.addData(response.body().getNews());
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreFail();
                    }
                }

                @Override
                public void onFailure(Call<NewsList> call, Throwable t) {
                    adapter.loadMoreFail();
                }
            });
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (current_page >= total_pages) {
                    adapter.loadMoreEnd();
                } else {
                    current_page++;
                    fetchNews();
                }
            }
        }, 1000);
    }
}
