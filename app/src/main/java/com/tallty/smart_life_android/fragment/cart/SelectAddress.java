package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.SelectAddressAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.fragment.me.ManageAddresses;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public static SelectAddress newInstance() {
        Bundle args = new Bundle();
        SelectAddress fragment = new SelectAddress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_address;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("选择收货地址");
        toolbar.inflateMenu(R.menu.addresses_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_addresses_manager:
                        startForResult(ManageAddresses.newInstance(), REQ_CODE);
                        break;
                }
                return true;
            }
        });
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
        refreshContacts();
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
                startForResult(AddressFormFragment.newInstance(new Contact()), REQ_CODE);
                break;
        }
    }

    private void refreshContacts() {
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getContacts().enqueue(new Callback<ContactList>() {
            @Override
            public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    contacts.clear();
                    contacts.addAll(response.body().getContacts());
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(showString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<ContactList> call, Throwable t) {
                hideProgress();
                showToast(showString(R.string.network_error));
            }
        });
    }

    /**
     * startForResult:
     * 响应Fragment的返回数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Contact new_contact = (Contact) data.getSerializable(Const.OBJECT);
            String tag = data.getString(Const.STRING);
            if (new_contact != null) {
                // 新建地址
                contacts.add(new_contact);
                adapter.notifyItemInserted(contacts.size()-1);
            } else if ("manage".equals(tag)){
                // 管理地址
                refreshContacts();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
