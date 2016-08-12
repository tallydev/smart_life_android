package com.tallty.smart_life_android.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.SnackbarUtil;
import com.tallty.smart_life_android.utils.ToastUtil;

/**
 * Created by kang on 16/7/5.
 * 基本逻辑fragment
 * 懒加载, (耗时操作)
 * 使用时, 直接继承此类即可
 */
public abstract class BaseLazyMainFragment extends BaseFragment implements View.OnClickListener {
    protected static String TAG;
    protected Context context;
    protected SharedPreferences sharedPre;
    protected String shared_phone;
    protected String shared_token;
    // 状态
    private boolean mInited = false;
    private Bundle mSavedInstanceState;
    // 布局
    private View view;
    // 载入框
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        context = getActivity();
        sharedPre = context.getSharedPreferences("SmartLife", Context.MODE_PRIVATE);
        shared_phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        shared_token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
        TAG = getTag();
        // 拦截调用
        fragmentInterceptor();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layout_id = getFragmentLayout();
        view = inflater.inflate(layout_id, container, false);
        // 初始化ToolBar
        Toolbar toolbar = getViewById(R.id.toolbar);
        TextView toolbar_title = getViewById(R.id.toolbar_title);
        // 调试使用
        // initToolbarMenu(toolbar);
        initToolBar(toolbar, toolbar_title);
        // 引用组件
        initView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            if (!isHidden()) {
                mInited = true;
                initLazyView(null);
            }
        } else {
            // isSupportHidden()仅在saveInstanceState!=null时有意义,是库帮助记录Fragment状态的方法
            if (!isSupportHidden()) {
                mInited = true;
                initLazyView(savedInstanceState);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!mInited && !hidden) {
            mInited = true;
            initLazyView(mSavedInstanceState);
        }
    }

    // fragment  拦截器
    protected void fragmentInterceptor() {
        // 供子类使用
    }
    // 获取布局文件
    public abstract int getFragmentLayout();
    // 初始化toolbar
    protected abstract void initToolBar(Toolbar toolbar, TextView title);
    // 初始化视图组件
    protected abstract void initView();
    // 懒加载
    protected abstract void initLazyView(@Nullable Bundle savedInstanceState);


    /**
     * 处理回退事件
     */
    @Override
    public boolean onBackPressedSupport() {
        // 任务移到后台执行
        _mActivity.moveTaskToBack(true);
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
     * 显示载入匡
     */
    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 隐藏载入匡
     */
    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    /**
     * 获取字符串资源
     */
    public String showString(int ResId) {
        return getActivity().getApplicationContext().getString(ResId);
    }

    /**
     * 获取颜色资源
     */
    public int showColor(int ResId) {
        return getActivity().getApplicationContext().getResources().getColor(R.color.alpha_white);
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
