package com.tallty.smart_life_android.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;
import com.tallty.smart_life_android.custom.PedometerConstant;
import com.tallty.smart_life_android.fragment.CartFragment;
import com.tallty.smart_life_android.fragment.HealthyFragment;
import com.tallty.smart_life_android.fragment.HomeFragment;
import com.tallty.smart_life_android.fragment.MeFragment;
import com.tallty.smart_life_android.fragment.PeopleFragment;
import com.tallty.smart_life_android.service.StepService;

public class MainActivity extends BaseActivity implements Handler.Callback {
    // 计步器相关
    private long TIME_INTERVAL = 500;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    // tab
    private ImageView tab_home;
    private ImageView tab_healthy;
    private ImageView tab_people;
    private ImageView tab_cart;
    private ImageView tab_me;
    // fragment管理器
    private FragmentManager fragmentManager;
    // 声明五个fragment
    private HomeFragment homeFragment;
    private HealthyFragment healthyFragment;
    private PeopleFragment peopleFragment;
    private CartFragment cartFragment;
    private MeFragment meFragment;
    // 存储
    SharedPreferences.Editor editor;

    // 启动计步器服务
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, PedometerConstant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

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

        editor = sp.edit();
        delayHandler = new Handler(this);
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
        // 设置服务
        setupService();
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
        fTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 获取计步器数据
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case PedometerConstant.MSG_FROM_SERVER:
                // 计步器数据: msg.getData().getInt("step")
                delayHandler.sendEmptyMessageDelayed(PedometerConstant.REQUEST_SERVER,TIME_INTERVAL);
                break;
            case PedometerConstant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, PedometerConstant.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
