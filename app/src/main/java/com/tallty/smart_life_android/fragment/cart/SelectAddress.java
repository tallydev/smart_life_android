package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.SelectAddressAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.model.Contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 购物车-确认订单-收货地址
 */
public class SelectAddress extends BaseBackFragment {
    private TextView new_address_text;
    private RecyclerView recyclerView;
    private SelectAddressAdapter adapter;
    // 地址列表
    private List<Contact> contacts = new ArrayList<>();
    private int prePosition;

    public static SelectAddress newInstance(ArrayList<Contact> contacts) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT_List, contacts);
        SelectAddress fragment = new SelectAddress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            contacts.clear();
            contacts.addAll((Collection<? extends Contact>) args.getSerializable(Const.OBJECT_List));
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_address;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("收货地址");

    }

    @Override
    protected void initView() {
        new_address_text = getViewById(R.id.new_address);
        recyclerView = getViewById(R.id.address_list);
    }

    @Override
    protected void setListener() {
        new_address_text.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initList();
    }

    private void initList() {
        // 整理数据
        for (int i = 0; i < contacts.size(); i++)  {
            if (contacts.get(i).isChecked()) {
                contacts.get(i).setChecked(true);
                prePosition = i;
            }
        }
        adapter = new SelectAddressAdapter(R.layout.item_select_address, contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                // 把选中的地址回传给上一个页面
                selectAddress(i);
            }
        });
    }

    public void selectAddress(int position) {
        contacts.get(prePosition).setChecked(false);
        contacts.get(position).setChecked(true);
        adapter.notifyItemChanged(prePosition);
        adapter.notifyItemChanged(position);
        prePosition = position;
        // 把选中的地址回传给上一个页面
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.OBJECT, contacts.get(position));
        setFragmentResult(RESULT_OK, bundle);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                pop();
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_address:
                startForResult(NewAddressFragment.newInstance(), REQ_CODE);
                break;
        }
    }

    /**
     * startForResult:
     * 响应NewAddressFragment的返回数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Contact new_contact = (Contact) data.getSerializable(Const.OBJECT);
            if (new_contact != null) {
                contacts.add(new_contact);
                adapter.notifyItemInserted(contacts.size()-1);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
