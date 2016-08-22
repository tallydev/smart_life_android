package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/6/15.
 * 用户
 */
public class User implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("authentication_token")
    @Expose
    private String token;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("birth")
    @Expose
    private String birth;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("slogan")
    @Expose
    private String slogan;

    @SerializedName("identity_card")
    @Expose
    private String idCard;

    @SerializedName("bind_phone")
    @Expose
    private String bindPhone;

    @SerializedName("pay_password")
    @Expose
    private String payPassword;

    // 业务相关参数(登录、注册错误信息)
    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("errors")
    @Expose
    private Errors errors;


    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return "male".equals(sex) ? "男" : "女";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getIdCard() {
        return  idCard;
    }

    public void setIdCard(String idCard) {
        this. idCard = idCard;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getError() {
        return error;
    }

    public Errors getErrors() {
        return errors;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
