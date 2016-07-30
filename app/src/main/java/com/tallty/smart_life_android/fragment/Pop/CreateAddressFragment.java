package com.tallty.smart_life_android.fragment.Pop;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAddressFragment extends DialogFragment implements View.OnClickListener {
    private TextView cancel_btn;
    private TextView confirm_btn;

    public CreateAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity()); // still has a little space between dialog and screen.
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_create_address);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_address, container, false);
        cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        cancel_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 窗体大小,动画,弹出方向
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = layoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.windowAnimations = R.style.dialogStyle;
        getDialog().getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.confirm_btn:
                dismiss();
                ToastUtil.show("确认了");
                break;
        }
    }
}
