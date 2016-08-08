package com.tallty.smart_life_android.base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by kang on 16/7/5.
 * 自定义Fragment基类
 * 使用Fragmentation开源库
 */
public class BaseFragment extends SupportFragment {
    protected static final String TAG = "Fragmentation";
    protected static final App mApp = App.getInstance();

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        // 调试使用,release版本要去除掉
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                        break;
                }
                return true;
            }
        });
    }
}
