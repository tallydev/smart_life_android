package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/7/29.
 * 订单
 */

public class Order implements Serializable {

    public Order() {

    }

    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName ("number")
    @Expose
    private String number;

    @SerializedName ("time")
    @Expose
    private String time;

    @SerializedName ("state")
    @Expose
    private String state;

    @SerializedName ("pay_way")
    @Expose
    private String payWay;

    @SerializedName ("price")
    @Expose
    private float price;

    @SerializedName("Commodities")
    @Expose
    private List<Commodity> commodities = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }
}
