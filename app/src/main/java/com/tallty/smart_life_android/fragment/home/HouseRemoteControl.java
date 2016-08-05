package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;

/**
 * 首页-智慧家居-远程控制
 */
public class HouseRemoteControl extends BaseBackFragment {
    private String mName;

    private TextView experience;
    private ImageView detail_image1;
    private ImageView detail_image2;

    public static HouseRemoteControl newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(Const.TOOLBAR_TITLE, title);
        HouseRemoteControl fragment = new HouseRemoteControl();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(Const.TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_house_remote_control;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        experience = getViewById(R.id.appointment_experience);
        detail_image1 = getViewById(R.id.remote_control_image1);
        detail_image2 = getViewById(R.id.remote_control_image2);

//        try{
//            InputStream inputStream = context.getAssets().open("remote_control.jpg");
//            detail_image.setInputStream(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void setListener() {
        experience.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        Glide.with(context).load(R.drawable.remote_control_01).into(detail_image1);
        Glide.with(context).load(R.drawable.remote_control_02).into(detail_image2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appointment_experience:
                HintDialogFragment fragment = HintDialogFragment.newInstance(
                        "预约后由<慧生活>服务专员和您电话联系,请保持手机畅通.", mName);
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
                break;
        }
    }

    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
        showToast("预约成功");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
