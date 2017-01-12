package com.tallty.smart_life_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.PayEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.model.CommunitiesResponse;
import com.tallty.smart_life_android.model.CommunityObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    public static boolean isForeground = false;
    public static ArrayList<CommunityObject> communities = new ArrayList<>();
    public static List<String> provinces = new ArrayList<>();
    public static HashMap<String, List<String>> cities = new HashMap<>();
    public static HashMap<String, List<String>> areas = new HashMap<>();
    public static HashMap<String, List<String>> streets = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }

        // 加载图标
        Iconify.with(new FontAwesomeModule());
        PingppLog.DEBUG = true;
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                String result = data.getExtras().getString("pay_result");
                EventBus.getDefault().post(new PayEvent(result));
                Log.i(App.TAG, "结果:===》"+result);
                Log.i(App.TAG, "错误:===》"+data.getExtras().getString("error_msg"));
                Log.i(App.TAG, "额外:===》"+data.getExtras().getString("extra_msg"));
            }
        }
    }
}
