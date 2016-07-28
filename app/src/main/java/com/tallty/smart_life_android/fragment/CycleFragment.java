package com.tallty.smart_life_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;

/**
 * Created by kang on 16/7/5.
 * 测试用: 循环BackFragment
 */
public class CycleFragment extends BaseBackFragment{
    private static final String ARG_NUMBER = "arg_number";
    private static final String ARG_NAME = "arg_name";

    private int mNumber;
    private String mName;

    public static CycleFragment newInstance(int number, String name) {
        CycleFragment fragment = new CycleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mNumber = args.getInt(ARG_NUMBER);
            mName = args.getString(ARG_NAME);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_cycle;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText(mName);
    }

    @Override
    protected void initView() {
//        mTvName = getViewById(R.id.tv_name);
//        mBtnNext = getViewById(R.id.btn_next);
//        mBtnNextWithFinish = getViewById(R.id.btn_next_with_finish);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void afterAnimationLogic() {
//        String title = "循环Fragment" + mNumber;
//

//        mTvName.setText(title + "\n可滑动返回");
//        mBtnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                start(CycleFragment.newInstance(mNumber + 1));
//            }
//        });
//        mBtnNextWithFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startWithPop(CycleFragment.newInstance(mNumber + 1));
//            }
//        });
    }

    @Override
    public void onClick(View v) {

    }
}
