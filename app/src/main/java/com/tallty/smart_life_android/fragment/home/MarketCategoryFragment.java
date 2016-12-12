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
import com.tallty.smart_life_android.adapter.ImageListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Activities;
import com.tallty.smart_life_android.model.Activity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页 - 精品超市 - 分类
 */
public class MarketCategoryFragment extends BaseBackFragment {
    private String title;
    private RecyclerView recyclerView;

    public static MarketCategoryFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.FRAGMENT_NAME, title);
        MarketCategoryFragment fragment = new MarketCategoryFragment();
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
        getActivities();
    }

    private void getActivities() {
        // TODO: 2016/12/12 获取商品类别
        showProgress("载入中...");
        Engine.authService(shared_token, shared_phone).getActivities().enqueue(new Callback<Activities>() {
            @Override
            public void onResponse(Call<Activities> call, Response<Activities> response) {
                if (response.isSuccessful()) {
                    setList(response.body().getActivities());
                } else {
                    showToast("载入失败");
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<Activities> call, Throwable t) {
                hideProgress();
                showToast("载入失败, 请检查手机网络");
            }
        });
    }

    private void setList(final ArrayList<Activity> categories) {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        ImageListAdapter adapter = new ImageListAdapter(R.layout.item_community_activity, categories);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                start(ProductFragment.newInstance("商品列表"));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
