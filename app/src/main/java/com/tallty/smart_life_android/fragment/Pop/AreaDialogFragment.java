package com.tallty.smart_life_android.fragment.Pop;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.adapter.MyWheelViewAdapter;
import com.tallty.smart_life_android.base.BaseDialogFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.model.Communities;
import com.tallty.smart_life_android.model.CommunitiesResponse;
import com.tallty.smart_life_android.utils.ToastUtil;
import com.wx.wheelview.widget.WheelView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 省市区选择弹窗
 */
public class AreaDialogFragment extends BaseDialogFragment {
    private TextView cancel_btn;
    private TextView confirm_btn;
    private WheelView<String> provinceWheel;
    private WheelView<String> cityWheel;
    private WheelView<String> areaWheel;
    // tag
    private String tag;
    private String confirmText;


    // 整理联动数据
    private List<String> provinceDatas;
    private HashMap<String, List<String>> cityDatas;
    private HashMap<String, List<String>> areaDatas;


    public static AreaDialogFragment newInstance(String tag, String confirmText) {
        Bundle args = new Bundle();
        args.putString("标签", tag);
        args.putString("确认文本", confirmText);
        AreaDialogFragment fragment = new AreaDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tag = args.getString("标签");
            confirmText = args.getString("确认文本");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_area_dialog;
    }

    @Override
    protected void initView(View view) {
        cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        provinceWheel = (WheelView<String>) view.findViewById(R.id.wheel_province);
        cityWheel = (WheelView<String>) view.findViewById(R.id.wheel_city);
        areaWheel = (WheelView<String>) view.findViewById(R.id.wheel_area);
    }

    @Override
    protected void setListener() {
        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        confirm_btn.setText(confirmText);
        // 设置样式
        setWheelViewStyle();
        // 获取数据
        fetchData();
    }

    private void setWheelViewStyle() {
        // 样式
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 16;
        style.textSize = 14;
        style.holoBorderColor = Color.parseColor("#E7E7E7");
        // 省份
        provinceWheel.setStyle(style);
        provinceWheel.setWheelAdapter(new MyWheelViewAdapter(context));
        provinceWheel.setSkin(WheelView.Skin.Holo);
        // 市
        cityWheel.setWheelAdapter(new MyWheelViewAdapter(context));
        cityWheel.setSkin(WheelView.Skin.Holo);
        cityWheel.setStyle(style);
        // 区
        areaWheel.setWheelAdapter(new MyWheelViewAdapter(context));
        areaWheel.setSkin(WheelView.Skin.Holo);
        areaWheel.setStyle(style);
    }

    private void fetchData() {
        // 获取数据
        provinceDatas = MainActivity.provinces;
        cityDatas = MainActivity.cities;
        areaDatas = MainActivity.areas;
        // 确保数据不为空
        if (provinceDatas.isEmpty()) {
            fetchCommunities();
        } else {
            // 关联数据
            bindDataToWheelView();
        }
    }

    private void bindDataToWheelView() {
        // 省数据
        provinceWheel.setWheelData(provinceDatas);
        // 市数据
        cityWheel.setWheelData(
            cityDatas.get(
                provinceDatas.get(provinceWheel.getSelection())
            )
        );
        // 区数据
        areaWheel.setWheelData(
            areaDatas.get(
                cityDatas.get(provinceDatas.get(provinceWheel.getSelection()))
                        .get(cityWheel.getSelection())
            )
        );
        // 关联数据
        provinceWheel.join(cityWheel);
        provinceWheel.joinDatas(cityDatas);
        cityWheel.join(areaWheel);
        cityWheel.joinDatas(areaDatas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.confirm_btn:
                Bundle bundle = new Bundle();
                String[] select_items = {
                        provinceWheel.getSelectionItem(),
                        cityWheel.getSelectionItem(),
                        areaWheel.getSelectionItem()
                };
                bundle.putStringArray(Const.ARRAY, select_items);
                // 发送事件
                EventBus.getDefault().post(new ConfirmDialogEvent(getDialog(), tag, bundle));
                break;
        }
    }

    /**
     * 获取社区地址列表
     */
    private void fetchCommunities() {
        showProgress("正在获取社区列表...");
        Engine.noAuthService().getCommunities().enqueue(new Callback<CommunitiesResponse>() {
            @Override
            public void onResponse(Call<CommunitiesResponse> call, Response<CommunitiesResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    Log.i(App.TAG, response.body().getList2().getProvinces().toString());
                    Communities communities = response.body().getList2();
                    // 保存到MainActivity的静态变量中
                    MainActivity.communities.clear();
                    MainActivity.communities.addAll(response.body().getSubdistricts());
                    MainActivity.provinces.clear();
                    MainActivity.provinces.addAll(communities.getProvinces());
                    MainActivity.cities = communities.getCities();
                    MainActivity.areas = communities.getAreas();
                    MainActivity.streets = communities.getStreets();
                    // 保存到临时变量中
                    provinceDatas = communities.getProvinces();
                    cityDatas = communities.getAreas();
                    areaDatas = communities.getAreas();
                    // 关联数据
                    if (provinceDatas.isEmpty()) {
                        dismiss();
                        ToastUtil.show("社区列表为空");
                    } else {
                        bindDataToWheelView();
                    }
                } else {
                    ToastUtil.show("获取社区列表失败,请稍后重试");
                }
            }

            @Override
            public void onFailure(Call<CommunitiesResponse> call, Throwable t) {
                hideProgress();
                ToastUtil.show("服务器连接错误");
            }
        });
    }
}
