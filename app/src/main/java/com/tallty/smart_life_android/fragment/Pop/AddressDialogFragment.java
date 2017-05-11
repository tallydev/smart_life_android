package com.tallty.smart_life_android.fragment.Pop;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.activity.MainActivity;
import com.tallty.smart_life_android.adapter.MyWheelViewAdapter;
import com.tallty.smart_life_android.base.BaseDialogFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.model.CommunityObject;
import com.tallty.smart_life_android.model.CommunityVillage;
import com.tallty.smart_life_android.utils.ToastUtil;
import com.wx.wheelview.widget.WheelView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新建收货地址
 */
public class AddressDialogFragment extends BaseDialogFragment {
    private TextView cancel_btn;
    private TextView confirm_btn;
    private WheelView<String> streetWheel;
    private WheelView<String> villageWheel;
    // tag
    private String tag;
    private String area;

    // 保存社区id
    private List<Integer> streetIds = new ArrayList<>();

    // 整理联动数据
    private List<String> streetData = new ArrayList<>();
    private HashMap<String, List<String>> villageData = new HashMap<>();

    public static AddressDialogFragment newInstance(String tag, String area) {
        Bundle args = new Bundle();
        args.putString("调用者", tag);
        args.putString("市级区", area);
        AddressDialogFragment fragment = new AddressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            tag = args.getString("调用者");
            area = args.getString("市级区");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_address_dialog;
    }

    protected void initView(View view) {
        cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        streetWheel = (WheelView<String>) view.findViewById(R.id.wheel_street);
        villageWheel = (WheelView<String>) view.findViewById(R.id.wheel_village);
    }

    protected void setListener() {
        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
    }

    protected void processLogic() {
        if (area == null) {
            dismiss();
            ToastUtil.show("请先选择省市区");
        } else {
            // 设置样式
            setWheelViewStyle();
            // 整理数据
            tidyData();
        }
    }

    private void tidyData() {
        // 情况1: 街道列表为空
        if (MainActivity.streets == null) {
            dismiss();
            ToastUtil.show("社区列表为空");
        } else {
            // 小区hash
            List<String> streets = MainActivity.streets.get(area);
            if (streets.isEmpty()) {
                dismiss();
                ToastUtil.show("还未开通"+area+"的街道");
            } else {
                makeWheelData(streets);
                // 关联数据
                bindDataToWheelView();
            }
        }
    }

    /**
     * @param streets 已选地区的所有街道
     * 结果:
     *      1、封装【街道选择器】的数据 ==> List<String>
     *      2、封装【小区选择器】的数据 ==> HashMap<街道, 小区列表>
     *      3、封装【街道选择器】的街道id ==> List<int>
     *
     */
    private void makeWheelData(List<String> streets) {
        for (String street : streets) {
            // 获取街道的名称 & 社区的id
            street = street.replace("@*@", ",");
            String[] arr = street.split(",");
            int streetId = Integer.valueOf(arr[1]);
            String streetName = arr[0];
            // 保存街道
            streetData.add(streetName);
            streetIds.add(streetId);
            // 获取街道对应的小区列表
            for (CommunityObject object : MainActivity.communities) {
                if (object.getId() != streetId) continue;
                // 保存社区中的所有小区
                List<String> villageNames = new ArrayList<>();
                for (CommunityVillage village : object.getVillages()) {
                    villageNames.add(village.getName());
                }
                // 街道包含的小区可能为空, wheelView 的数据不能为空
                if (villageNames.isEmpty()) {
                    villageNames.add("");
                    villageData.put(streetName, villageNames);
                } else {
                    villageData.put(streetName, villageNames);
                }
            }
        }
    }

    private void setWheelViewStyle() {
        // 样式
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 16;
        style.textSize = 14;
        style.holoBorderColor = Color.parseColor("#E7E7E7");
        // 街道
        streetWheel.setStyle(style);
        streetWheel.setWheelAdapter(new MyWheelViewAdapter(context));
        streetWheel.setSkin(WheelView.Skin.Holo);
        // 小区
        villageWheel.setWheelAdapter(new MyWheelViewAdapter(context));
        villageWheel.setSkin(WheelView.Skin.Holo);
        villageWheel.setStyle(style);
    }

    private void bindDataToWheelView() {
        // 街道
        streetWheel.setWheelData(streetData);
        // 小区
        villageWheel.setWheelData(villageData.get(streetData.get(streetWheel.getSelection())));
        // 关联数据
        streetWheel.join(villageWheel);
        streetWheel.joinDatas(villageData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.confirm_btn:
                if (villageWheel.getSelectionItem().isEmpty()) {
                    ToastUtil.show("本社区没有可选的小区");
                } else {
                    String[] select_items = {streetWheel.getSelectionItem(), villageWheel.getSelectionItem()};
                    int select_street_id = streetIds.get(streetWheel.getCurrentPosition());

                    Bundle bundle = new Bundle();
                    bundle.putStringArray(Const.ARRAY, select_items);
                    bundle.putInt(Const.INT, select_street_id);
                    EventBus.getDefault().post(new ConfirmDialogEvent(getDialog(), tag, bundle));
                }
                break;
        }
    }
}
