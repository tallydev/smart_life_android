package com.tallty.smart_life_android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tallty.smart_life_android.R;

/**
 * Created by kang on 16/6/20.
 * 购物车
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    public CartFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
