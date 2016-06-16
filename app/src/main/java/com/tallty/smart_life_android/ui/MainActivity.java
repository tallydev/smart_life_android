package com.tallty.smart_life_android.ui;

import android.os.Bundle;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        showActionBar(false);
    }
}
