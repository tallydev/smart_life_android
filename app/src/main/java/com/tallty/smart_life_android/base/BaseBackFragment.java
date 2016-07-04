package com.tallty.smart_life_android.base;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tallty.smart_life_android.R;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by kang on 16/7/4.
 * 滑动退出fragment
 */
public class BaseBackFragment extends SwipeBackFragment {
    private static final String TAG = "Fragmentation";

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        initToolbarMenu(toolbar);
    }

    private void initToolbarMenu(Toolbar toolbar) {

    }
}
