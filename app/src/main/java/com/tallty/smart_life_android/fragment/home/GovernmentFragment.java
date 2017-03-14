package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.GovernmentAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.model.GovernmentSort;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 政府直通车
 */
public class GovernmentFragment extends BaseBackFragment {

    // 组件 & 适配器
    private RecyclerView recyclerView;
    private GovernmentAdapter adapter;
    // 数据
    private ArrayList<GovernmentSort> sorts  = new ArrayList<>();

    public static GovernmentFragment newInstance() {
        Bundle args = new Bundle();
        GovernmentFragment fragment = new GovernmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_common_list;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("政府直通车");
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
        getGovernmentSorts();
    }

    @Override
    public void onClick(View v) {

    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new GovernmentAdapter(R.layout.item_common_image, sorts);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
    }

    private void getGovernmentSorts() {
        showProgress("正在加载...");
        Engine
            .authService(shared_token, shared_phone)
            .getGovernmentSorts()
            .enqueue(new Callback<HashMap<String, ArrayList<GovernmentSort>>>() {
                @Override
                public void onResponse(Call<HashMap<String, ArrayList<GovernmentSort>>> call,
                                       Response<HashMap<String, ArrayList<GovernmentSort>>> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        sorts.clear();
                        sorts.addAll(response.body().get("news_sorts"));
                        adapter.notifyDataSetChanged();
                    } else {
                        showToast("加载失败");
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, ArrayList<GovernmentSort>>> call, Throwable t) {
                    hideProgress();
                    showToast("链接错误, 请检查手机网络");
                }
        });
    }
}
