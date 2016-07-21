package com.tallty.smart_life_android.fragment.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.CartListAdapter;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.StartBrotherEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by kang on 16/6/20.
 * 购物车
 */
public class CartFragment extends BaseLazyMainFragment {
    private TextView pay;
    private RecyclerView recyclerView;
    private CartListAdapter adapter;
    private CheckBox select_all;
    private AlertDialog.Builder builder = null;
    // 数据
    private ArrayList<Boolean> checked = new ArrayList<Boolean>(){
        {
            add(false); add(false);
        }
    };
    private ArrayList<Integer> photo_urls = new ArrayList<Integer>(){
        {
            add(R.drawable.limi_sail_one); add(R.drawable.on_sail);
        }
    };
    private ArrayList<String> names = new ArrayList<String>(){
        {
            add("西双版纳生态无眼凤梨");add("西双版纳生态蜂蜜");
        }
    };
    private ArrayList<Integer> counts = new ArrayList<Integer>(){
        {
            add(1);add(3);
        }
    };
    private ArrayList<Float> prices = new ArrayList<Float>(){
        {
            add(66.00f);add(88.00f);
        }
    };


    public static CartFragment newInstance() {
        Bundle args = new Bundle();

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("购物车");
    }

    @Override
    protected void initView() {
        pay = getViewById(R.id.pay);
        recyclerView = getViewById(R.id.cart_list);
        select_all = getViewById(R.id.select_all_btn);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        pay.setOnClickListener(this);
        select_all.setOnClickListener(this);

        processRecyclerVIew();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                EventBus.getDefault().post(new StartBrotherEvent(ConfirmOrder.newInstance("确认订单")));
                break;
            case R.id.select_all_btn:
                if (select_all.isChecked()){
                    adapter.selectAll();
                } else {
                    adapter.unSelectAll();
                }
                break;
        }
    }

    private void processRecyclerVIew(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CartListAdapter(context, checked, photo_urls, names, counts, prices);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                showToast("点击了"+position);
            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, final int position) {
                // Use the Builder class for convenient dialog construction
                builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialogTheme);
                builder.setMessage("确定删除该商品吗?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                adapter.removeItem(position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        }).create();
                builder.show();
            }
        });
    }
}
