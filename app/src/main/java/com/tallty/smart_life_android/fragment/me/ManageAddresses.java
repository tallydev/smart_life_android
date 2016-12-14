package com.tallty.smart_life_android.fragment.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.ManageAddressesAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.fragment.cart.NewAddressFragment;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 管理收货地址
 */
public class ManageAddresses extends BaseBackFragment {
    // UI
    private TextView new_address_text;
    private RecyclerView recyclerView;
    private ManageAddressesAdapter adapter;
    // 地址列表
    private List<Contact> contacts = new ArrayList<>();

    public static ManageAddresses newInstance() {
        Bundle args = new Bundle();

        ManageAddresses fragment = new ManageAddresses();
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
        return R.layout.fragment_manage_addresses;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("管理收货地址");
    }

    @Override
    protected void initView() {
        new_address_text = getViewById(R.id.new_address_text);
        recyclerView = getViewById(R.id.addresses);
    }

    @Override
    protected void setListener() {
        new_address_text.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        loadAddresses();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_address_text:
                startForResult(NewAddressFragment.newInstance(), REQ_CODE);
                break;
        }
    }

    private void initList() {
        adapter = new ManageAddressesAdapter(R.layout.item_manage_addresses, contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(adapter);
    }

    private void loadAddresses() {
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getContacts().enqueue(new Callback<ContactList>() {
            @Override
            public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    contacts.clear();
                    contacts.addAll(response.body().getContacts());
                    // 显示数据
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
     * 响应NewAddressFragment的返回数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Contact new_contact = (Contact) data.getSerializable(Const.OBJECT);
            if (new_contact != null) {
                contacts.add(new_contact);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
