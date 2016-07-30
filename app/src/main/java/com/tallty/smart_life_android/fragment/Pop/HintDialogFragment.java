package com.tallty.smart_life_android.fragment.Pop;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kang on 16/7/30.
 * 全局提示弹窗
 */
public class HintDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView cancel_btn;
    private TextView confirm_btn;
    private ImageView hint_image;
    private TextView hint_text;

    private String hint;
    private String caller;

    public static HintDialogFragment newInstance(String hint, String caller) {
        Bundle args = new Bundle();
        args.putString("提示", hint);
        args.putString("调用者", caller);
        HintDialogFragment fragment = new HintDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            hint = args.getString("提示");
            caller = args.getString("调用者");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity()); // still has a little space between dialog and screen.
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_hint_dialog);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hint_dialog, container, false);
        
        cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        hint_image = (ImageView) view.findViewById(R.id.hint_icon);
        hint_text = (TextView) view.findViewById(R.id.hint_text);

        Glide.with(getActivity()).load(R.drawable.snackbar_icon).into(hint_image);
        hint_text.setText(hint);
        
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
                EventBus.getDefault().post(new ConfirmDialogEvent(getDialog(), caller));
                break;
        }
    }
}
