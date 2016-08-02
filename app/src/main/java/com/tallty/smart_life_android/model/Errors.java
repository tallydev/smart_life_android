package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/8/2.
 * 错误
 */

public class Errors {

    @SerializedName("password")
    @Expose
    private List<String> password = new ArrayList<String>();

    @SerializedName("phone")
    @Expose
    private List<String> phone = new ArrayList<String>();

    @SerializedName("sms_token")
    @Expose
    private List<String> sms_token = new ArrayList<String>();

    /**
     *
     * @return
     * The password
     */
    public List<String> getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(List<String> password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The phone
     */
    public List<String> getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getSms_token() {
        return sms_token;
    }

    public void setSms_token(List<String> sms_token) {
        this.sms_token = sms_token;
    }
}