package com.tallty.smart_life_android.fragment.Pop;


import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.MyWheelViewAdapter;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.wx.wheelview.widget.WheelView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 新建收货地址
 */
public class AddressDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView cancel_btn;
    private TextView confirm_btn;
    private WheelView areaWheel;
    private WheelView streetWheel;
    private WheelView communityWheel;
    // tag
    private String caller;

    public static AddressDialogFragment newInstance(String caller) {
        Bundle args = new Bundle();
        args.putString("调用者", caller);
        AddressDialogFragment fragment = new AddressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            caller = args.getString("调用者");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // 窗体大小,动画,弹出方向
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.windowAnimations = R.style.dialogStyle;
        getDialog().getWindow().setAttributes(layoutParams);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity()); // still has a little space between dialog and screen.
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_address_dialog);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_dialog, container, false);
        initView(view);
        setListener();
        processLogic();

        return view;
    }

    private void initView(View view) {
        cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        areaWheel = (WheelView) view.findViewById(R.id.wheel_view_community);
        streetWheel = (WheelView) view.findViewById(R.id.wheel_view_street);
        communityWheel = (WheelView) view.findViewById(R.id.wheel_view_area);
    }

    private void setListener() {
        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
    }

    private void processLogic() {
        // 样式
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 16;
        style.textSize = 14;
        style.holoBorderColor = Color.parseColor("#E7E7E7");
        // 社区
        areaWheel.setWheelAdapter(new MyWheelViewAdapter(getActivity().getApplicationContext()));
        areaWheel.setSkin(WheelView.Skin.Holo);
        areaWheel.setWheelData(communityDatas());
        areaWheel.setStyle(style);
        // 街道
        streetWheel.setWheelAdapter(new MyWheelViewAdapter(getActivity().getApplicationContext()));
        streetWheel.setSkin(WheelView.Skin.Holo);
        streetWheel.setWheelData(streetDatas().get(communityDatas().get(areaWheel.getSelection())));
        streetWheel.setStyle(style);
        areaWheel.join(streetWheel);
        areaWheel.joinDatas(streetDatas());
        // 小区
        communityWheel.setWheelAdapter(new MyWheelViewAdapter(getActivity().getApplicationContext()));
        communityWheel.setSkin(WheelView.Skin.Holo);
        communityWheel.setWheelData(
                areaDatas().get(
                        streetDatas().get(communityDatas().get(areaWheel.getSelection()))
                        .get(streetWheel.getSelection())
                )
        );
        communityWheel.setStyle(style);
        streetWheel.join(communityWheel);
        streetWheel.joinDatas(areaDatas());
    }

    // 整理联动数据
    private List<String> communityDatas() {
        String[] strings = {"五华区", "西山区"};
        return Arrays.asList(strings);
    }

    private HashMap<String, List<String>> streetDatas() {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        String[] strings = {"五华区", "西山区"};
        String[] s1 = {"大观街道", "丰宁街道", "红云街道", "黑林铺街道", "华山街道", "护国街道", "莲华街道", "龙翔街道", "普吉街道", "西翥街道"};
        String[] s2 = {"前卫街道"};
        String[][] ss = {s1, s2};
        for (int i = 0; i < strings.length; i++) {
            map.put(strings[i], Arrays.asList(ss[i]));
        }
        return map;
    }

    private HashMap<String, List<String>> areaDatas() {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        String[] strings = {"大观街道", "丰宁街道", "红云街道", "黑林铺街道", "华山街道", "护国街道", "莲华街道", "龙翔街道", "普吉街道", "西翥街道" , "前卫街道"};
        String[] s1 = {""};
        String[] s2 = {""};
        String[] s3 = {"安康园", "彩信园", "红云小区", "金域蓝湾", "文明花园", "远洋风景"};
        String[] s4 = {""};
        String[] s5 = {""};
        String[] s6 = {""};
        String[] s7 = {""};
        String[] s8 = {""};
        String[] s9 = {""};
        String[] s10 = {""};
        String[] s11 = {"广福城"};
        String[][] ss = {s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11};
        for (int i = 0; i < strings.length; i++) {
            map.put(strings[i], Arrays.asList(ss[i]));
        }
        return map;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.confirm_btn:
                Bundle bundle = new Bundle();
                bundle.putString("小区",
                        areaWheel.getSelectionItem().toString()+
                        streetWheel.getSelectionItem().toString()+
                        communityWheel.getSelectionItem().toString()
                );
                bundle.putString(Const.CONTACT_AREA, areaWheel.getSelectionItem().toString());
                bundle.putString(Const.CONTACT_STREET, streetWheel.getSelectionItem().toString());
                bundle.putString(Const.CONTACT_COMMUNITY, communityWheel.getSelectionItem().toString());
                EventBus.getDefault().post(new ConfirmDialogEvent(getDialog(), caller, bundle));
                break;
        }
    }
}
