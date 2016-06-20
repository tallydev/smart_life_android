package com.tallty.smart_life_android.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.fragment.CartFragment;
import com.tallty.smart_life_android.fragment.HealthyFragment;
import com.tallty.smart_life_android.fragment.HomeFragment;
import com.tallty.smart_life_android.fragment.MeFragment;
import com.tallty.smart_life_android.fragment.PeopleFragment;

public class MainActivity extends BaseActivity {
    private ImageView tab_home;
    private ImageView tab_healthy;
    private ImageView tab_people;
    private ImageView tab_cart;
    private ImageView tab_me;

    // fragment注入容器
    private FrameLayout ly_content;
    // fragment管理器
    private FragmentManager fragmentManager;
    // 声明五个fragment
    private HomeFragment homeFragment;
    private HealthyFragment healthyFragment;
    private PeopleFragment peopleFragment;
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
        tab_people = getViewById(R.id.tab_menu_people);
        tab_cart = getViewById(R.id.tab_menu_cart);
        tab_me = getViewById(R.id.tab_menu_me);

        ly_content = getViewById(R.id.ly_content);
    }

    @Override
    protected void setListener() {
        tab_home.setOnClickListener(this);
        tab_healthy.setOnClickListener(this);
        tab_people.setOnClickListener(this);
        tab_cart.setOnClickListener(this);
        tab_me.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 进入MainActivity是,模拟点击Home按钮
        tab_home.performClick();
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
            case R.id.tab_menu_people:
                setTabUnselected();
                tab_people.setSelected(true);
                if (peopleFragment == null) {
                    peopleFragment = new PeopleFragment();
                    fTransaction.add(R.id.ly_content, peopleFragment);
                } else {
                    fTransaction.show(peopleFragment);
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
    }

    // 点击tab时,先把所有tab按钮置为未选中状态,然后把点击的状态置为选中
    private void setTabUnselected() {
        tab_home.setSelected(false);
        tab_healthy.setSelected(false);
        tab_people.setSelected(false);
        tab_cart.setSelected(false);
        tab_me.setSelected(false);
    }

    // 点击tab时,先把所有fragment隐藏,然后显示点击的fragment
    private void hideAllFragment(FragmentTransaction f) {
        if (homeFragment != null) f.hide(homeFragment);
        if (healthyFragment != null) f.hide(healthyFragment);
        if (peopleFragment != null) f.hide(peopleFragment);
        if (cartFragment != null) f.hide(cartFragment);
        if (meFragment != null) f.hide(meFragment);
    }
}
