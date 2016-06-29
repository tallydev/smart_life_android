package com.tallty.smart_life_android.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.fragment.CartFragment;
import com.tallty.smart_life_android.fragment.CommunityFragment;
import com.tallty.smart_life_android.fragment.HealthyFragment;
import com.tallty.smart_life_android.fragment.HomeFragment;
import com.tallty.smart_life_android.fragment.MeFragment;

public class MainActivity extends BaseActivity {
    // tab
    private ImageView tab_home;
    private ImageView tab_healthy;
    private ImageView tab_community;
    private ImageView tab_cart;
    private ImageView tab_me;
    // fragment管理器
    private FragmentManager fragmentManager;
    // 声明五个fragment
    private HomeFragment homeFragment;
    private HealthyFragment healthyFragment;
    private CommunityFragment communityFragment;
    private CartFragment cartFragment;
    private MeFragment meFragment;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
        // 初始化FragmentManager
        fragmentManager = getFragmentManager();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tab_home = getViewById(R.id.tab_menu_home);
        tab_healthy = getViewById(R.id.tab_menu_healthy);
        tab_community = getViewById(R.id.tab_menu_community);
        tab_cart = getViewById(R.id.tab_menu_cart);
        tab_me = getViewById(R.id.tab_menu_me);
    }

    @Override
    protected void setListener() {
        tab_home.setOnClickListener(this);
        tab_healthy.setOnClickListener(this);
        tab_community.setOnClickListener(this);
        tab_cart.setOnClickListener(this);
        tab_me.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 进入MainActivity是,模拟点击Home按钮
        tab_home.performClick();
    }

    // 点击tab时,先把所有tab按钮置为未选中状态,然后把点击的状态置为选中
    private void setTabUnselected() {
        tab_home.setSelected(false);
        tab_healthy.setSelected(false);
        tab_community.setSelected(false);
        tab_cart.setSelected(false);
        tab_me.setSelected(false);
    }

    // 点击tab时,先把所有fragment隐藏,然后显示点击的fragment
    private void hideAllFragment(FragmentTransaction f) {
        if (homeFragment != null) f.hide(homeFragment);
        if (healthyFragment != null) f.hide(healthyFragment);
        if (communityFragment != null) f.hide(communityFragment);
        if (cartFragment != null) f.hide(cartFragment);
        if (meFragment != null) f.hide(meFragment);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fTransaction);

        switch (v.getId()) {
            case R.id.tab_menu_home:
                setTabUnselected();
                tab_home.setSelected(true);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fTransaction.add(R.id.ly_content, homeFragment);
                } else {
                    fTransaction.show(homeFragment);
                }
                break;
            case R.id.tab_menu_healthy:
                setTabUnselected();
                tab_healthy.setSelected(true);
                if (healthyFragment == null) {
                    healthyFragment = new HealthyFragment();
                    fTransaction.add(R.id.ly_content, healthyFragment);
                } else {
                    fTransaction.show(healthyFragment);
                }
                break;
            case R.id.tab_menu_community:
                setTabUnselected();
                tab_community.setSelected(true);
                if (communityFragment == null) {
                    communityFragment = new CommunityFragment();
                    fTransaction.add(R.id.ly_content, communityFragment);
                } else {
                    fTransaction.show(communityFragment);
                }
                break;
            case R.id.tab_menu_cart:
                setTabUnselected();
                tab_cart.setSelected(true);
                if (cartFragment == null) {
                    cartFragment = new CartFragment();
                    fTransaction.add(R.id.ly_content, cartFragment);
                } else {
                    fTransaction.show(cartFragment);
                }
                break;
            case R.id.tab_menu_me:
                setTabUnselected();
                tab_me.setSelected(true);
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    fTransaction.add(R.id.ly_content, meFragment);
                } else {
                    fTransaction.show(meFragment);
                }
                break;
        }
        fTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }
}
