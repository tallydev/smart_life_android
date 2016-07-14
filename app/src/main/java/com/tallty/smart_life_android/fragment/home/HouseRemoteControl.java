package com.tallty.smart_life_android.fragment.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

import java.io.IOException;
import java.io.InputStream;

/**
 * 首页-智慧家居-远程控制
 */
public class HouseRemoteControl extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView detail_image1;
    private ImageView detail_image2;

    public static HouseRemoteControl newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        HouseRemoteControl fragment = new HouseRemoteControl();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString(TOOLBAR_TITLE);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_house_remote_control;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
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

    }

    @Override
    protected void afterAnimationLogic() {
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        Glide.with(context).load(R.drawable.remote_control_01).into(detail_image1);
        Glide.with(context).load(R.drawable.remote_control_02).into(detail_image2);
    }

    @Override
    public void onClick(View v) {

    }
}
