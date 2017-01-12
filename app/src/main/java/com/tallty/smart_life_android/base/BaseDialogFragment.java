package com.tallty.smart_life_android.base;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.ToastUtil;

/**
 * Created by kang on 2017/1/12.
 * 底部弹出 base dialog fragment
 */

public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener {
    protected Context context;
    private ProgressDialog progressDialog;
    private CountDownTimer timer;

    @Override
    public void onStart() {
        super.onStart();
        // 窗体大小,动画,弹出方向
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.windowAnimations = R.style.dialogStyle;
            getDialog().getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity()); // still has a little space between dialog and screen.
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_address_dialog);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayout(), container, false);
        initView(view);
        setListener();
        processLogic();

        return view;
    }

    protected abstract int getLayout();

    protected abstract void initView(View view);

    protected abstract void setListener();

    protected abstract void processLogic();

    /**
     * 显示载入匡
     */
    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        // 增加timeout
        setTimerCancel();
        timer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                hideProgress();
                ToastUtil.show("加载超时, 请稍后重试");
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }

    /**
     * 隐藏载入匡
     */
    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                setTimerCancel();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }
    }

    /**
     * 取消计时器
     */
    private void setTimerCancel() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
