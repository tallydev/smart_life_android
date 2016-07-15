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
import com.tallty.smart_life_android.custom.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 首页-智慧家居-电子猫眼
 */
public class HouseCatEye extends BaseBackFragment {
    private String mName;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView experience;
    private ImageView detail_image1;
    private ImageView detail_image2;
    private ImageView detail_image3;

    public static HouseCatEye newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TOOLBAR_TITLE, title);
        HouseCatEye fragment = new HouseCatEye();
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
        return R.layout.fragment_house_cat_eye;
    }

    @Override
    protected void initView() {
        toolbar = getViewById(R.id.toolbar);
        toolbar_title = getViewById(R.id.toolbar_title);
        experience = getViewById(R.id.appointment_experience);
        detail_image1 = getViewById(R.id.cat_eye_image1);
        detail_image2 = getViewById(R.id.cat_eye_image2);
        detail_image3 = getViewById(R.id.cat_eye_image3);

//        try{
//            InputStream inputStream = context.getAssets().open("cat_eye.jpg");
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
        initBackToolbar(toolbar);
        toolbar_title.setText(mName);
        Glide.with(context).load(R.drawable.cat_eye_01).into(detail_image1);
        Glide.with(context).load(R.drawable.cat_eye_02).into(detail_image2);
        Glide.with(context).load(R.drawable.cat_eye_03).into(detail_image3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appointment_experience:
                setSnackBar(experience,
                        "预约后由<慧生活>服务专员和您电话联系,请保持手机畅通.",
                        100000, R.layout.snackbar_icon, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                experience.setText("申请成功");
                                experience.setClickable(false);
                            }
                        });
                break;
        }
    }
}
