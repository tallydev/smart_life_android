package com.tallty.smart_life_android.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.R;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private String content;

    public HomeFragment() {

    }

    @SuppressLint ("ValidFragment")
    public HomeFragment(String content) {
        this.content = content;
        Logger.d(content);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.txt_content);
        textView.setText(content);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
