package com.tallty.smart_life_android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ClearDayStepEvent;
import com.tallty.smart_life_android.fragment.MainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    private SharedPreferences sharedPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }

        EventBus.getDefault().register(this);
        // 加载图标
        Iconify.with(new FontAwesomeModule());
        sharedPre = this.getSharedPreferences("SmartLife", Context.MODE_PRIVATE);
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
        EventBus.getDefault().unregister(this);
    }

    /**
     * 清空每天的逐小时步数
     */
    @Subscribe
    public void onClearSahredStep(ClearDayStepEvent event) {
        String sharedKey;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                sharedKey = "0" + i;
            } else {
                sharedKey = String.valueOf(i);
            }
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putFloat(sharedKey, 0.0f);
            editor.apply();
        }
        Log.i(App.TAG, "旧的逐小时步数被清空了");
    }
}
