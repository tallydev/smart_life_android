package com.tallty.smart_life_android.fragment.me;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.tallty.smart_life_android.event.ManageAddressEvent;
import com.tallty.smart_life_android.fragment.cart.AddressFormFragment;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int updatePosition = -1;
    private int defaultPosition = -1;

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
        EventBus.getDefault().register(this);
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
                startForResult(AddressFormFragment.newInstance(new Contact()), REQ_CODE);
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
                    // 获取默认地址的position
                    for (int i = 0; i < contacts.size(); i++) {
                        if (contacts.get(i).isDefault()) {
                            defaultPosition = i;
                            break;
                        }
                    }
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

    private void deleteContact(final int position, final Contact contact) {
        showProgress("正在删除...");
        Engine.authService(shared_token, shared_phone)
            .deleteContact(contacts.get(position).getId())
            .enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        contacts.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, contacts.size());
                        // 如果删除的是默认地址, 则取第一个地址为默认地址
                        if (contacts.size() > 0 && contact.isDefault()) {
                            contacts.get(0).setDefault(true);
                            contacts.get(0).setChecked(true);
                            adapter.notifyItemChanged(0);
                        }
                    } else {
                        showToast("删除失败,请重试");
                    }
                }
                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    hideProgress();
                    showToast(showString(R.string.network_error));
                }
            });
    }

    private void setAddressDefault(final Contact contact, final int position) {
        showProgress("正在更新...");
        Map<String, String> fields = new HashMap<>();
        Engine.authService(shared_token, shared_phone)
            .updateContact(contact.getId(), true, fields)
            .enqueue(new Callback<ContactList>() {
                @Override
                public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        contacts.get(defaultPosition).setDefault(false);
                        adapter.notifyItemChanged(defaultPosition);
                        contacts.get(position).setDefault(true);
                        adapter.notifyItemChanged(position);
                        defaultPosition = position;
                    } else {
                        showToast("更新失败");
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
     * 响应 AddressFormFragment 返回的新增地址
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Contact new_contact = (Contact) data.getSerializable(Const.OBJECT);
            if (new_contact != null) {
                if (updatePosition > 0) {
                    // 更新
                    contacts.set(updatePosition, new_contact);
                    adapter.notifyItemChanged(updatePosition);
                    updatePosition = -1;
                } else {
                    // 新增
                    contacts.add(new_contact);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    // 编辑地址、删除地址、设置默认地址
    @Subscribe
    public void onManageAddressEvent(final ManageAddressEvent event) {
        switch (event.getAction()) {
            case Const.EDIT_ADDRESS:
                updatePosition = event.getPosition();
                startForResult(AddressFormFragment.newInstance(event.getContact()), REQ_CODE);
                break;
            case Const.DELETE_ADDRESS:
                confirmDialog("确认删除此收货地址吗", new OnConfirmDialogListener() {
                    @Override
                    public void onConfirm(DialogInterface dialog, int which) {
                        deleteContact(event.getPosition(), event.getContact());
                    }

                    @Override
                    public void onCancel(DialogInterface dialog, int which) {

                    }
                });
                break;
            case Const.SET_ADDRESS_DEFAULT:
                setAddressDefault(event.getContact(), event.getPosition());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
