package com.tallty.smart_life_android.fragment.home;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.GroupBuyAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.model.ProductList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 限量销售商品列表
 */
public class GroupBuyFragment extends BaseBackFragment implements
        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {
    // 组件 & 适配器
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private GroupBuyAdapter adapter;
    // 数据
    private ArrayList<Product> products = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 5;
    // 刷新
    private boolean isRefresh = false;

    public static GroupBuyFragment newInstance() {
        Bundle args = new Bundle();

        GroupBuyFragment fragment = new GroupBuyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_group_buy;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("限量销售");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.group_buy_list);
        refreshLayout = getViewById(R.id.group_buy_refresh_layout);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initRefreshLayout();
        initList();
        getGroupBuyProducts();
    }

    private void initRefreshLayout() {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        refreshLayout.setProgressViewOffset(true, 0, 150);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        refreshLayout.setColorSchemeColors(showColor(R.color.orange), Color.GREEN, Color.RED, Color.YELLOW);
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        refreshLayout.setDistanceToTriggerSync(100);
        // 设定下拉圆圈的背景
        refreshLayout.setProgressBackgroundColorSchemeColor(showColor(R.color.white));
        // 设置圆圈的大小
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置下拉刷新的监听
        refreshLayout.setOnRefreshListener(this);
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new GroupBuyAdapter(R.layout.item_group_buy, products);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(adapter);
        // 加载更多
        adapter.setOnLoadMoreListener(this);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                showToast(i+"");
            }
        });
    }

    public void getGroupBuyProducts() {
        Engine.noAuthService().getProductList(current_page, per_page)
            .enqueue(new Callback<ProductList>() {
                @Override
                public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                    if (response.isSuccessful()) {
                        current_page = response.body().getCurrentPage();
                        total_pages = response.body().getTotalPages();
                        // 商品列表
                        products.clear();
                        products.addAll(response.body().getProducts());
                        // todo 添加end_time
                        int i = 10;
                        for (Product product : products) {
                            product.setCountDown(995550000);
                            i++;
                        }
                        adapter.notifyDataSetChanged();
                        // 刷新逻辑
                        if (isRefresh) {
                            isRefresh = false;
                            refreshLayout.setRefreshing(false);
                        }
                    } else {
                        showToast(showString(R.string.response_error));
                    }
                }

                @Override
                public void onFailure(Call<ProductList> call, Throwable t) {
                    showToast(showString(R.string.network_error));
                }
            });
    }

    @Override
    public void onClick(View v) {

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
                Engine.noAuthService().getProductList(current_page, per_page)
                    .enqueue(new Callback<ProductList>() {
                        @Override
                        public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                            if (response.isSuccessful()) {
                                current_page = response.body().getCurrentPage();
                                total_pages = response.body().getTotalPages();
                                // 商品列表
                                adapter.addData(response.body().getProducts());
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreFail();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductList> call, Throwable t) {
                            adapter.loadMoreFail();
                        }
                    });
            }
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        getGroupBuyProducts();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != adapter) {
            adapter.startRefreshTime();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != adapter) {
            adapter.cancelRefreshTime();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != adapter) {
            adapter.cancelRefreshTime();
        }
    }
}
