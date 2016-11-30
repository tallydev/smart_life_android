package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeProductAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.model.ProductList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页-限量销售
 */
public class ProductFragment extends BaseBackFragment {
    private String fragmentTitle;
    private RecyclerView recyclerView;
    private HomeProductAdapter adapter;
    // 数据
    private ArrayList<Product> products = new ArrayList<>();

    public static ProductFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
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
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_product;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(fragmentTitle);
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.limit_sail_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        setList();
    }

    private void setList() {
        showProgress("正在加载...");
        Engine.noAuthService().getProductList(1, 20).enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.isSuccessful()) {
                    // 商品列表
                    ProductList productList = response.body();
                    products.clear();
                    products.addAll(productList.getProducts());
                    // 加载列表
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new HomeProductAdapter(context, products);
                    recyclerView.setAdapter(adapter);
                    hideProgress();
                } else {
                    hideProgress();
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
    public void onClick(View v) {

    }
}