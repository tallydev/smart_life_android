package com.tallty.smart_life_android.fragment.me;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyAppointmentsAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.model.Appointment;
import com.tallty.smart_life_android.model.AppointmentList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人中心-我的预约
 */
public class MyAppointments extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView recyclerView;
    private MyAppointmentsAdapter adapter;
    // 数据
    private List<Appointment> appointments = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

    public static MyAppointments newInstance() {
        Bundle args = new Bundle();

        MyAppointments fragment = new MyAppointments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_appointments;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("我的预约");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.appointments_list);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        fetchAppointments();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new MyAppointmentsAdapter(R.layout.item_my_appointments, appointments);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                confirmDialog("确认删除吗", new OnConfirmDialogListener() {
                    @Override
                    public void onConfirm(DialogInterface dialog, int which) {
                        appointments.remove(i);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, appointments.size() - i);
                    }

                    @Override
                    public void onCancel(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    private void fetchAppointments() {
        // 获取数据
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone)
            .getAppointments(current_page, per_page)
            .enqueue(new Callback<AppointmentList>() {
                @Override
                public void onResponse(Call<AppointmentList> call, Response<AppointmentList> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        current_page = response.body().getCurrent_page();
                        total_pages = response.body().getTotal_pages();
                        appointments.clear();
                        appointments.addAll(response.body().getAppointments());
                        adapter.notifyDataSetChanged();
                    } else {
                        showToast(showString(R.string.response_error));
                    }
                }

                @Override
                public void onFailure(Call<AppointmentList> call, Throwable t) {
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
                    Engine.authService(shared_token, shared_phone)
                            .getAppointments(current_page, per_page)
                            .enqueue(new Callback<AppointmentList>() {
                                @Override
                                public void onResponse(Call<AppointmentList> call, Response<AppointmentList> response) {
                                    if (response.isSuccessful()) {
                                        current_page = response.body().getCurrent_page();
                                        total_pages = response.body().getTotal_pages();
                                        adapter.addData(response.body().getAppointments());
                                        adapter.loadMoreComplete();
                                    } else {
                                        adapter.loadMoreFail();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AppointmentList> call, Throwable t) {
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
