package com.tallty.smart_life_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseSupportFragment;
import com.tallty.smart_life_android.custom.TabBar;
import com.tallty.smart_life_android.custom.TabBarTab;
import com.tallty.smart_life_android.event.StartBrotherEvent;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.fragment.cart.CartFragment;
import com.tallty.smart_life_android.fragment.community.CommunityFragment;
import com.tallty.smart_life_android.fragment.healthy.HealthyFragment;
import com.tallty.smart_life_android.fragment.home.HomeFragment;
import com.tallty.smart_life_android.fragment.me.MeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by kang on 16/7/5.
 * MainActivity 五个tab页面的父节点
 */
public class MainFragment extends BaseSupportFragment {
    private static final int REQ_MSG = 10;

    public static final int HOME = 0;
    public static final int HEALTHY = 1;
    public static final int COMMUNITY = 2;
    public static final int CART = 3;
    public static final int ME = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];

    private TabBar mTabBar;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (savedInstanceState == null) {
            mFragments[HOME] = HomeFragment.newInstance();
            mFragments[HEALTHY] = HealthyFragment.newInstance();
            mFragments[COMMUNITY] = CommunityFragment.newInstance();
            mFragments[CART] = CartFragment.newInstance();
            mFragments[ME] = MeFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, HOME,
                    mFragments[HOME],
                    mFragments[HEALTHY],
                    mFragments[COMMUNITY],
                    mFragments[CART],
                    mFragments[ME]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[HOME] = findChildFragment(HomeFragment.class);
            mFragments[HEALTHY] = findChildFragment(HealthyFragment.class);
            mFragments[COMMUNITY] = findChildFragment(CommunityFragment.class);
            mFragments[CART] = findChildFragment(CartFragment.class);
            mFragments[ME] = findChildFragment(MeFragment.class);
        }

        initView(view);
        return view;
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        mTabBar = (TabBar) view.findViewById(R.id.bottomBar);

        mTabBar
                .addItem(new TabBarTab(_mActivity, R.mipmap.home_normal))
                .addItem(new TabBarTab(_mActivity, R.mipmap.healthy_normal))
                .addItem(new TabBarTab(_mActivity, R.mipmap.community_normal))
                .addItem(new TabBarTab(_mActivity, R.mipmap.cart_normal))
                .addItem(new TabBarTab(_mActivity, R.mipmap.me_normal));

        mTabBar.setOnTabSelectedListener(new TabBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
