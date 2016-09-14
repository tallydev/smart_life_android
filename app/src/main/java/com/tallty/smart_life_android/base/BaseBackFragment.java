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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.model.Appointment;
import com.tallty.smart_life_android.utils.SnackbarUtil;
import com.tallty.smart_life_android.utils.ToastUtil;

import java.io.IOException;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kang on 16/6/21.
 * 自定义滑动返回功能的fragment基类
 * 使用SwipeBackFragment开源库
 */
public abstract class BaseBackFragment extends SwipeBackFragment implements View.OnClickListener {
    protected Context context;
    protected SharedPreferences sharedPre;
    protected String shared_phone;
    protected String shared_token;
    // UI
    private View view;
    protected Toolbar toolbar;
    protected TextView toolbar_title;
    private ProgressDialog progressDialog;
    // startForResultFragment使用
    protected static final int REQ_CODE = 0;
    protected static final String RESULT_DATA = "data";
    protected static final String RESULT_POSITION = "position";
    // 接口
    private OnAppointListener onAppointListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sharedPre = context.getSharedPreferences("SmartLife", Context.MODE_PRIVATE);
        shared_phone = sharedPre.getString(Const.USER_PHONE, Const.EMPTY_STRING);
        shared_token = sharedPre.getString(Const.USER_TOKEN, Const.EMPTY_STRING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout_id = getFragmentLayout();
        view = inflater.inflate(layout_id, container, false);
        // toolbar
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        initBackToolbar(toolbar);
        initToolbar(toolbar, toolbar_title);
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
        // 滑动过程监听
        getSwipeBackLayout().addSwipeListener(new SwipeBackLayout.OnSwipeListener() {
            @Override
            public void onDragStateChange(int state) {
                if (state == 1) {
                    hideSoftInput();
                } else if (state == 2) {
                    // 退出fragment时执行
                    onFragmentPop();
                }

            }

            @Override
            public void onEdgeTouch(int oritentationEdgeFlag) {

            }

            @Override
            public void onDragScrolled(float scrollPercent) {

            }
        });
    }

    // fragment 添加退出时的逻辑 (滑动退出,点击返回退出)
    protected void onFragmentPop() {}
    // 获取布局文件id
    public abstract int getFragmentLayout();
    // 处理toolbar
    public abstract void initToolbar(Toolbar toolbar, TextView toolbar_title);
    // find UI
    protected abstract void initView();
    // 设置监听
    protected abstract void setListener();
    // 转场动画完成后执行(可选)(耗时的逻辑)
    protected abstract void afterAnimationLogic();

    // ========================回调接口==============================

    public interface OnAppointListener {
        void onSuccess(Appointment appointment);
        void onFail(String errorMsg);
        void onError();
    }

    // ========================通用逻辑==============================

    /**
     * 设置toolbar的返回按钮
     */
    protected void initBackToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_28dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏软键盘
                hideSoftInput();
                // 出栈
                pop();
                // 退出时执行
                onFragmentPop();
            }
        });
        // 调试使用
        // initToolbarMenu(toolbar);
    }

    /**
     * 设置toolbar的菜单按钮
     * 调试Fragment时使用,添加fragment栈层级菜单
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
                        _mActivity.logFragmentStackHierarchy(App.TAG);
                }
                return true;
            }
        });
    }

    /**
     * 处理物理返回键功能
     */
    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

    /**
     * 通用预约
     */
    protected void submitAppointmentListener(String type, int count, OnAppointListener listener) {
        this.onAppointListener = listener;

        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).submitAppointment(type, count).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (201 == response.code()) {
                    onAppointListener.onSuccess(response.body());
                    hideProgress();
                } else {
                    try {
                        onAppointListener.onFail(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    hideProgress();
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                onAppointListener.onError();
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });
    }


    // ===========================工具方法============================
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
