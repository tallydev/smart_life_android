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
import com.tallty.smart_life_android.adapter.ProductListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.model.ProductList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页-限量销售
 */
public class ProductFragment extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private String fragmentTitle;
    private int categoryId = 0;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    // 数据
    private ArrayList<Product> products = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

    public static ProductFragment newInstance(String title, int categoryId) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        args.putInt(Const.INT, categoryId);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fragmentTitle = args.getString(Const.FRAGMENT_NAME);
            categoryId = args.getInt(Const.INT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_common_list;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(fragmentTitle);
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
        fetchProducts();
    }

    private void initList() {
        // 初次加载列表
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ProductListAdapter(R.layout.item_home_product, products);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(adapter);
        // 点击事件
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                start(ProductShowFragment.newInstance(products.get(i)));
            }
        });
        // 加载更多
        adapter.setOnLoadMoreListener(this);
    }

    private void fetchProducts() {
        showProgress("正在加载...");
        Engine.noAuthService().getProductsByCategory(current_page, per_page, categoryId)
                .enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    current_page = response.body().getCurrentPage();
                    total_pages = response.body().getTotalPages();
                    // 商品列表
                    products.clear();
                    products.addAll(response.body().getProducts());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
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
                    Engine.noAuthService().getProductsByCategory(current_page, per_page, categoryId)
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
    public void onClick(View v) {

    }
}