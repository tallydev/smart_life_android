package com.tallty.smart_life_android.fragment.cart;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收货地址-新建地址
 */
public class AddressFormFragment extends BaseBackFragment {
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_area;
    private EditText edit_detail;
    private TextView save_address;
    // 数据
    private Contact contact;
    private String contact_area;
    private String contact_street;
    private String contact_community;

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
        toolbar_title.setText("新建收货地址");
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
            // 提交成功: 返回数据
            final Contact new_contact = new Contact();
            new_contact.setName(name);
            new_contact.setPhone(phone);
            new_contact.setArea(contact_area);
            new_contact.setStreet(contact_street);
            new_contact.setCommunity(contact_community);
            new_contact.setAddress(detail);
            // 向服务器提交新联系人
            showProgress(showString(R.string.progress_normal));
            Engine.authService(shared_token, shared_phone)
                    .createContact(name, phone, area, contact_street, contact_community, detail, false)
                    .enqueue(new Callback<ContactList>() {
                        @Override
                        public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                            if (response.isSuccessful()) {
                                // 新地址创建成功, 传递给上级, 并退出当前页
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Const.OBJECT, new_contact);
                                setFragmentResult(RESULT_OK, bundle);
                                hideProgress();
                                hideSoftInput();
                                pop();
                            } else {
                                hideProgress();
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
        contact_area = event.data.getString(Const.CONTACT_AREA);
        contact_street = event.data.getString(Const.CONTACT_STREET);
        contact_community = event.data.getString(Const.CONTACT_COMMUNITY);
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
