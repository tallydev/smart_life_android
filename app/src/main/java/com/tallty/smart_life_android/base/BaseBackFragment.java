package com.tallty.smart_life_android.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.SnackbarUtil;
import com.tallty.smart_life_android.utils.ToastUtil;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by kang on 16/6/21.
 * 自定义滑动返回功能的fragment基类
 * 使用SwipeBackFragment开源库
 */
public abstract class BaseBackFragment extends SwipeBackFragment implements View.OnClickListener {
    private static final String TAG = "Fragmentation";
    private View view;

    protected Context context;
    protected SharedPreferences sharedPre;
    // SharedPreferences数据key
    protected static final String ADDRESS_AREA = "address_area";
    protected static final String ADDRESS_DETAIL = "address_detail";
    protected static final String ADDRESS_PHONE = "address_phone";
    protected static final String ADDRESS_NAME = "address_NAME";
    // 用于SharedPreferences取值
    protected static final String EMPTY_STRING = " ";
    // startForResultFragment使用
    protected static final int REQ_CODE = 0;
    protected static final String RESULT_DATA = "data";
    protected static final String RESULT_POSITION = "position";
    // startBrotherFragment使用
    protected static final String TOOLBAR_TITLE = "BackFragmentTitle";
    protected static final String OBJECTS = "BackFragmentObjects";
    protected static final String ADDRESS = "BackFragmentAddress";
    protected static final String TOTAL_PRICE = "BackFragmentTotalPrice";
    protected static final String NORMAL_DATA = "normal_data";
    // 调用MyAddress的不同处理
    protected static final int FROM_PROFILE = 2;
    protected static final int FROM_ORDER = 3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout_id = getFragmentLayout();
        view = inflater.inflate(layout_id, container, false);
        context = getActivity().getApplicationContext();
        sharedPre = context.getSharedPreferences("SmartLife", Context.MODE_PRIVATE);
        // 引用组件
        initView();
        return attachToSwipeBack(view);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        // 设置监听器
        setListener();
        // 入场动画结束后执行  优化,防动画卡顿
        afterAnimationLogic();
    }

    /**
     * 设置toolbar的返回按钮
     */
    protected void initBackToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_28dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 出栈
                pop();
            }
        });
        // 调试Fragment时使用,添加fragment栈层级菜单
//        initToolbarMenu(toolbar);
    }

    /**
     * 设置toolbar的菜单按钮
     */
    private void initToolbarMenu(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.hierarchy);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        // 调试使用,release版本去除
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                }
                return true;
            }
        });
    }

    // 获取布局文件id
    public abstract int getFragmentLayout();
    // find UI
    protected abstract void initView();
    // 设置监听
    protected abstract void setListener();
    // 转场动画完成后执行(可选)(耗时的逻辑)
    protected abstract void afterAnimationLogic();

    /**
     * 处理物理返回键功能
     */
    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    /**
     * 全局查找View
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) view.findViewById(id);
    }

    /**
     * 显示Toast
     */
    public void showToast(String text) {
        ToastUtil.show(text);
    }

    /**
     * 初始化显示Snackbar
     * 调用
     */
    public void setSnackBar(View layout, String text, int duration, int layoutId, final View.OnClickListener listener) {
        Snackbar snackbar = SnackbarUtil.IndefiniteSnackbar(
                layout, text, duration,
                getResources().getColor(R.color.white), getResources().getColor(R.color.orange))
                .setActionTextColor(getResources().getColor(R.color.white));

        if (listener != null) {
            snackbar.setAction("确定", listener);
        }

        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setMaxLines(4);
        textView.setTextSize(12);
        textView.setLineSpacing(18, 1);

        if (layoutId != 0) {
            SnackbarUtil.SnackbarAddView(snackbar, layoutId, 0);
        }

        snackbar.show();
    }
}
