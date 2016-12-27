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
import com.tallty.smart_life_android.adapter.CommunityActivityAdapter;
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
public class ProductCategoryFragment extends BaseBackFragment {
    private String title;
    private RecyclerView recyclerView;
    private ProductCategoryAdapter adapter;
    private ArrayList<Category> categories = new ArrayList<>();

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
        return R.layout.fragment_market_category;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(title);
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.categories_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        getActivities();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ProductCategoryAdapter(R.layout.item_community_activity, categories);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                start(ProductFragment.newInstance("商品列表", categories.get(i).getId()));
            }
        });
    }

    private void getActivities() {
        showProgress("正在加载...");
        Engine.noAuthService().getProductCategories().enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                if (response.isSuccessful()) {
                    categories.clear();
                    categories.addAll(response.body().getCategories());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("加载失败");
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                hideProgress();
                showToast("链接错误, 请检查手机网络");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
