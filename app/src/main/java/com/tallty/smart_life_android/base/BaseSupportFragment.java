package com.tallty.smart_life_android.base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tallty.smart_life_android.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by kang on 16/7/5.
 */
public class BaseSupportFragment extends SupportFragment {
    private static final String TAG = "Fragmentation";

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                        break;
                }
                return true;
            }
        });
    }
}
