package com.tallty.smart_life_android.fragment.cart;


import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.tallty.smart_life_android.adapter.SelectAddressAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.event.SetDefaultAddress;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 购物车-确认订单-收货地址
 */
public class SelectAddress extends BaseBackFragment {
    // 调用者
    private int from;
    // UI
    private TextView new_address_text;
    private RecyclerView recyclerView;
    private SelectAddressAdapter adapter;
    // 地址列表
    private List<Contact> contacts = new ArrayList<>();
    // 默认地址position
    private int defaultContactListPosition = -1;


    public static SelectAddress newInstance(int from) {
        Bundle args = new Bundle();
        args.putInt(Const.FROM, from);
        SelectAddress fragment = new SelectAddress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            from = args.getInt(Const.FROM);
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
        EventBus.getDefault().register(this);

        new_address_text = getViewById(R.id.new_address);
        recyclerView = getViewById(R.id.address_list);
    }

    @Override
    protected void setListener() {
        new_address_text.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        // 载入数据并加载列表
        loadContactData();
    }

    private void loadContactData() {
        showProgress(showString(R.string.progress_normal));
        Engine.authService(shared_token, shared_phone).getContacts().enqueue(new Callback<ContactList>() {
            @Override
            public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    contacts.clear();
                    contacts.addAll(response.body().getContacts());
                    // 整理数据
                    for (int i = 0; i < contacts.size(); i++) {
                        if (contacts.get(i).isDefault()) {
                            // 存储默认地址
                            saveDefaultAddress(contacts.get(i));
                            // 设置为选择状态
                            contacts.get(i).setChecked(true);
                            // 保存默认地址下标,用于设置默认地址功能
                            defaultContactListPosition = i;
                        } else {
                            // 非默认的,设置选择状态为false
                            contacts.get(i).setChecked(false);
                        }
                    }
                    // 载入列表
                    setList();
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

    // 更新SharedPreferences中的默认地址
    // 已存在,则不保存
    private void saveDefaultAddress(Contact contact) {
        if (sharedPre.getInt(Const.CONTACT_ID, -1) != contact.getId() ) {
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putInt(Const.CONTACT_ID, contact.getId());
            editor.putString(Const.CONTACT_PHONE, contact.getPhone());
            editor.putString(Const.CONTACT_NAME, contact.getName());
            editor.putString(Const.CONTACT_AREA, contact.getArea());
            editor.putString(Const.CONTACT_STREET, contact.getStreet());
            editor.putString(Const.CONTACT_COMMUNITY, contact.getCommunity());
            editor.putString(Const.CONTACT_ADDRESS, contact.getAddress());
            editor.apply();
        }
    }

    private void setList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SelectAddressAdapter(R.layout.item_select_address, contacts);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) throws ParseException {
                // 把选中的地址回传给上一个页面
                selectAddress(position);
            }

            @Override
            public void onItemLongPress(RecyclerView.ViewHolder vh, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity, R.style.CustomAlertDialogTheme);
                builder.setMessage("确认删除吗")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteContact(position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    public void selectAddress(int position) {
        Contact cache_contact;
        // 取消原来的选中地址
        cache_contact = contacts.get(defaultContactListPosition);
        cache_contact.setChecked(false);
        contacts.set(defaultContactListPosition, cache_contact);
        adapter.notifyItemChanged(defaultContactListPosition);

        // 设置新的选中地址 && 重置 defaultContactListPosition 为新的position
        cache_contact = contacts.get(position);
        cache_contact.setChecked(true);
        contacts.set(position, cache_contact);
        adapter.notifyItemChanged(position);
        defaultContactListPosition = position;

        // 把选中的地址回传给上一个页面
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.OBJECT, cache_contact);
        setFragmentResult(RESULT_OK, bundle);
        if (from == Const.FROM_ORDER){
            pop();
        }
    }

    private void deleteContact(final int position) {
        showProgress("正在删除...");
        Engine.authService(shared_token, shared_phone)
                .deleteContact(contacts.get(position).getId())
                .enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    boolean is_removed = false;
                    if (contacts.size() > 1 && contacts.get(position).isDefault()) {
                        contacts.remove(position);
                        is_removed = true;
                        // 如果删除的是默认地址, 则取第一个地址为默认地址
                        contacts.get(0).setDefault(true);
                        contacts.get(0).setChecked(true);
                        // 更新本地默认地址
                        saveDefaultAddress(contacts.get(0));
                    } else if (contacts.size() <= 1) {
                        // 当删除的地址为最后一条时,置空shared中的默认地址
                        Contact contact = new Contact();
                        saveDefaultAddress(contact);
                    }

                    if (!is_removed) {
                        contacts.remove(position);
                    }
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, contacts.size() - position);
                    hideProgress();
                } else {
                    hideProgress();
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
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 接收事件: SetDefaultAddress
     * 设置默认地址
     */
    @Subscribe
    public void onSetDefaultAddress(SetDefaultAddress event) {
        // 更新SharedPreferences中的默认地址
        saveDefaultAddress(event.getContact());

        // 取消原来的默认地址
        Contact cache_contact = contacts.get(defaultContactListPosition);
        cache_contact.setDefault(false);
        cache_contact.setChecked(false);
        contacts.set(defaultContactListPosition, cache_contact);
        adapter.notifyItemChanged(defaultContactListPosition);

        // 设置新的默认地址 && 重置 defaultContactListPosition 为新的position
        cache_contact = contacts.get(event.getPosition());
        cache_contact.setDefault(true);
        cache_contact.setChecked(true);
        contacts.set(event.getPosition(), cache_contact);
        adapter.notifyItemChanged(event.getPosition());

        // 更新默认地址下标
        defaultContactListPosition = event.getPosition();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
