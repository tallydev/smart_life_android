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
import com.google.gson.JsonElement;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.AlarmsAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.model.Alarm;
import com.tallty.smart_life_android.model.Alarms;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 电子猫眼,警报列表
 */
public class AlarmsFragment extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView recyclerView;
    private TextView makeAlarmText;
    private AlarmsAdapter adapter;
    private ArrayList<Alarm> alarms = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

    public static AlarmsFragment newInstance() {
        Bundle args = new Bundle();
        AlarmsFragment fragment = new AlarmsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_alarms;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("电子猫眼");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.alarms_list);
        makeAlarmText = getViewById(R.id.make_alarm_btn);
    }

    @Override
    protected void setListener() {
        makeAlarmText.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        fetchAlarms();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_alarm_btn:
                callThePolice();
                break;
        }
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new AlarmsAdapter(R.layout.item_home_alarm, alarms);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Bundle bundle = new Bundle();
                Alarm alarm = alarms.get(i);
                ArrayList<String> images = new ArrayList<>();
                for (int j = 0; j < alarm.getImages().size(); j++) {
                    images.add(alarm.getImages().get(j).get("url"));
                }
                bundle.putString(Const.PUSH_TITLE, alarm.getTitle());
                bundle.putString(Const.PUSH_TIME, alarm.getTitle());
                bundle.putStringArrayList(Const.PUSH_IMAGES, images);
                start(NotificationDetailFragment.newInstance(bundle));
            }
        });
    }

    private void fetchAlarms() {
        Engine
            .noAuthService()
            .getAlarmsHistory(current_page, per_page, "18288240215")
            .enqueue(new Callback<Alarms>() {
                @Override
                public void onResponse(Call<Alarms> call, Response<Alarms> response) {
                    if (response.isSuccessful()) {
                        total_pages++;
                        adapter.addData(response.body().getAlarms());
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreFail();
                    }
                }

                @Override
                public void onFailure(Call<Alarms> call, Throwable t) {
                    hideProgress();
                    adapter.loadMoreFail();
                }
        });
    }

    private void callThePolice() {
        // TODO: 2017/3/15 我要出警功能
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
                    fetchAlarms();
                }
            }
        }, 1000);
    }
}
