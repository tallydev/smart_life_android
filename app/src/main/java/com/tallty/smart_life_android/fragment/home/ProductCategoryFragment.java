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
import com.tallty.smart_life_android.adapter.ProductCategoryAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Categories;
import com.tallty.smart_life_android.model.Category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页 - 精品超市 - 分类
 */
public class ProductCategoryFragment extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private String title;
    private RecyclerView recyclerView;
    private ProductCategoryAdapter adapter;
    private ArrayList<Category> categories = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

    public static ProductCategoryFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        ProductCategoryFragment fragment = new ProductCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(Const.FRAGMENT_NAME);
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
        getCategories();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ProductCategoryAdapter(R.layout.item_common_image, categories);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                start(ProductFragment.newInstance("商品列表", categories.get(i).getId()));
            }
        });
        // 加载更多
        adapter.setOnLoadMoreListener(this);
    }

    private void getCategories() {
        Engine
            .authService(shared_token, shared_phone)
            .getProductCategories(current_page, per_page)
            .enqueue(new Callback<Categories>() {
                @Override
                public void onResponse(Call<Categories> call, Response<Categories> response) {
                    if (response.isSuccessful()) {
                        current_page = response.body().getCurrentPage();
                        total_pages = response.body().getTotalPages();
                        adapter.addData(response.body().getCategories());
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreFail();
                    }
                }

                @Override
                public void onFailure(Call<Categories> call, Throwable t) {
                    adapter.loadMoreFail();
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
                    getCategories();
                }
            }
        }, 1000);
    }
}
