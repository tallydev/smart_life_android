package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/8/10.
 * 首页-健康报告列表
 */

public class ReportList implements Serializable {
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("items")
    @Expose
    private List<Report> items = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Report> getItems() {
        return items;
    }

    public void setItems(List<Report> items) {
        this.items = items;
    }
}
