package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.ConfirmDialogEvent;
import com.tallty.smart_life_android.fragment.Pop.AddressDialogFragment;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收货地址-新建或更新
 */
public class AddressFormFragment extends BaseBackFragment {
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_area;
    private EditText edit_detail;
    private TextView save_address;
    // 新建或更新维护的同一对象
    private Contact contact = new Contact();

    public static AddressFormFragment newInstance(Contact contact) {
        Bundle args = new Bundle();
        args.putSerializable(Const.OBJECT, contact);
        AddressFormFragment fragment = new AddressFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            contact = (Contact) args.getSerializable(Const.OBJECT);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_new_address;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        if (contact.getName() != null)
            toolbar_title.setText("编辑收货地址");
        else
            toolbar_title.setText("新增收货地址");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        edit_name = getViewById(R.id.address_name);
        edit_phone = getViewById(R.id.address_phone);
        edit_area = getViewById(R.id.address_area);
        edit_detail = getViewById(R.id.address_detail);
        save_address = getViewById(R.id.save_address);
    }

    @Override
    protected void setListener() {
        edit_area.setOnClickListener(this);
        save_address.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        edit_name.setText(contact.getName());
        edit_phone.setText(contact.getPhone());
        String area = contact.getArea() == null ?  "" : contact.getArea();
        String street = contact.getStreet() == null ? "" : contact.getStreet();
        String community = contact.getCommunity() == null ? "" : contact.getCommunity();
        edit_area.setText(area + " " + street + " " + community);
        edit_detail.setText(contact.getAddress());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_area:
                AddressDialogFragment fragment = AddressDialogFragment.newInstance("新建收货地址");
                fragment.show(getActivity().getFragmentManager(), "HintDialog");
                break;
            case R.id.save_address:
                // 验证
                beginSave();
                break;
        }
    }

    private void beginSave() {
        edit_name.setError(null);
        edit_phone.setError(null);
        edit_area.setError(null);
        edit_detail.setError(null);

        String name = edit_name.getText().toString();
        String phone = edit_phone.getText().toString();
        String detail = edit_detail.getText().toString();
        String area = edit_area.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (detail.isEmpty()) {
            cancel = true;
            focusView = edit_detail;
            edit_detail.setError("请填写详细地址");
        }
        if (area.isEmpty()) {
            cancel = true;
            focusView = edit_area;
            edit_area.setError("请填写小区");
        }
        if (phone.isEmpty()) {
            cancel = true;
            focusView = edit_phone;
            edit_phone.setError("请填写联系电话");
        } else if (!isPhoneValid(phone)) {
            cancel = true;
            focusView = edit_phone;
            edit_phone.setError("联系电话格式错误");
        }
        if (name.isEmpty()) {
            cancel = true;
            focusView = edit_name;
            edit_name.setError("请填写收货人姓名");
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 数据验证通过, 保存到对象中
            contact.setName(name);
            contact.setPhone(phone);
            contact.setAddress(detail);
            // 表单数据
            Map<String, String> fields = new HashMap<>();
            fields.put("contact[name]", contact.getName());
            fields.put("contact[phone]", contact.getPhone());
            fields.put("contact[area]", contact.getArea());
            fields.put("contact[street]", contact.getStreet());
            fields.put("contact[community]", contact.getCommunity());
            fields.put("contact[address]", contact.getAddress());
            // 更新或新建
            if (0 == contact.getId())
                createContact(fields);
            else
                updateContact(fields);
        }
    }

    // 新增联系人
    private void createContact(Map<String, String> fields) {
        showProgress("正在保存...");
        Engine.authService(shared_token, shared_phone)
            .createContact(fields).enqueue(new Callback<ContactList>() {
                @Override
                public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        // 新地址创建成功, 传递给上级, 并退出当前页
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Const.OBJECT, contact);
                        setFragmentResult(RESULT_OK, bundle);
                        hideSoftInput();
                        pop();
                    } else {
                        showToast("保存失败");
                        try {
                            Log.d(App.TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ContactList> call, Throwable t) {
                    hideProgress();
                    showToast(showString(R.string.network_error));
                }
            });
    }

    // 更新联系人
    private void updateContact(Map<String, String> fields) {
        showProgress("正在保存...");
        Engine.authService(shared_token, shared_phone)
                .updateContact(contact.getId(), contact.isDefault(), fields)
                .enqueue(new Callback<ContactList>() {
                    @Override
                    public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                        hideProgress();
                        if (response.isSuccessful()) {
                            // 更新成功, 传递给上级, 并退出当前页
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Const.OBJECT, contact);
                            bundle.putString(Const.STRING, "update");
                            setFragmentResult(RESULT_OK, bundle);
                            hideSoftInput();
                            pop();
                        } else {
                            showToast("保存失败");
                        }
                    }
                    @Override
                    public void onFailure(Call<ContactList> call, Throwable t) {
                        hideProgress();
                        showToast(showString(R.string.network_error));
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 接收事件: 选择小区地址
     * @param event
     */
    @Subscribe
    public void onConfirmDialogEvnet(ConfirmDialogEvent event) {
        event.dialog.dismiss();
        edit_area.setText(event.data.getString("小区"));
        contact.setArea(event.data.getString(Const.CONTACT_AREA));
        contact.setStreet(event.data.getString(Const.CONTACT_STREET));
        contact.setCommunity(event.data.getString(Const.CONTACT_COMMUNITY));
    }

    /**
     * 验证手机号格式
     */
    private boolean isPhoneValid(String phone) {
        boolean flag;
        try{
            Pattern pattern = Pattern.compile(Const.PHONE_PATTEN);
            Matcher matcher = pattern.matcher(phone);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
}
