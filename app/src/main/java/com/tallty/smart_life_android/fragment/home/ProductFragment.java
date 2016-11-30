package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeProductAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Product;

import java.util.ArrayList;

/**
 * 首页-限量销售
 */
public class ProductFragment extends BaseBackFragment {
    private String mName;

    private RecyclerView recyclerView;
    private HomeProductAdapter adapter;

    // 数据
    private ArrayList<Product> products = new ArrayList<>();

    private ArrayList<Integer> tag = new ArrayList<Integer>() {
        {
            add(0);
            add(1);
            add(2);
        }
    };
    private ArrayList<String> titles = new ArrayList<String>() {
        {
            add("西双版纳生态无眼凤梨新鲜直达\n"+"每件Kg (5-6个)");
            add("西双版纳野生蜂蜜（一年一季）\n"+"每件500克");
            add("天然放养土鸡蛋儿童老人首选\n"+"每件20枚");
        }
    };
    private ArrayList<Float> prices = new ArrayList<Float>() {
        {
            add(66.00f);add(50.00f);add(50.00f);
        }
    };
    private ArrayList<Integer> thumbs = new ArrayList<Integer>() {
        {
            add(R.drawable.product_pineapple_one);
            add(R.drawable.product_honey_one);
            add(R.drawable.product_egg_one);
        }
    };
    private ArrayList<Integer> strings = new ArrayList<Integer>() {
        {
            add(R.string.product_pineapple_description);
            add(R.string.product_honey_description);
            add(R.string.product_egg_description);
        }
    };

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
            mName = args.getString(Const.FRAGMENT_NAME);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_product;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
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
        // TODO: 16/8/4 假数据
        for (int i=0; i<titles.size();i++) {
            Product product = new Product();
            product.setTitle(titles.get(i));
            product.setPrice(prices.get(i));
            product.setThumbId(thumbs.get(i));
            product.setStringId(strings.get(i));
            product.setTag(tag.get(i));
            products.add(product);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HomeProductAdapter(context, products);
        recyclerView.setAdapter(adapter);

//        showProgress(showString(R.string.progress_normal));
//        Engine.noAuthService().getProductList(1, 10).enqueue(new Callback<ProductList>() {
//            @Override
//            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
//                if (response.code() == 200) {
//                    // 商品列表
//                    ProductList productList = response.body();
//                    products = productList.getProducts();
//                    // 加载列表
//                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                    adapter = new HomeProductAdapter(context, products);
//                    recyclerView.setAdapter(adapter);
//
//                    hideProgress();
//                } else {
//                    hideProgress();
//                    showToast(showString(R.string.response_error));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductList> call, Throwable t) {
//                hideProgress();
//                showToast(showString(R.string.network_error));
//            }
//        });
    }

    @Override
    public void onClick(View v) {

    }
}