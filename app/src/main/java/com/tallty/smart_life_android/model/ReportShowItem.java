package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/8/10.
 * 健康报告-单项历史数据-item
 */

public class ReportShowItem implements Serializable {
    @SerializedName ("date")
    @Expose
    private String date;

    @SerializedName("value")
    @Expose
    private float value;

    @SerializedName("state")
    @Expose
    private String state;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
