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
import com.tallty.smart_life_android.fragment.MainFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                if ("success".equals(result)) {
                    // "success" - 支付成功

                } else if ("fail".equals(result)) {
                    // "fail"    - 支付失败
                } else if ("cancel".equals(result)) {
                    // "cancel"  - 取消支付

                } else if ("invalid".equals(result)) {
                    // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）

                }

                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                Log.i(App.TAG, "结果:===》"+result);
                Log.i(App.TAG, "错误:===》"+errorMsg);
                Log.i(App.TAG, "额外:===》"+extraMsg);
                Log.i(App.TAG, data.getExtras().getInt("code") + "==========");
            }
        }
    }
}
