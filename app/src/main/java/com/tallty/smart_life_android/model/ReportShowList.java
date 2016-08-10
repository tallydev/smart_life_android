package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kang on 16/8/10.
 * 健康报告-单项历史数据列表
 */

public class ReportShowList implements Serializable{
    @SerializedName("total_pages")
    @Expose
    private int total_pages;

    @SerializedName("current_page")
    @Expose
    private int current_page;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("alias")
    @Expose
    private String alias;

    @SerializedName("hint")
    @Expose
    private String hint;

    @SerializedName("list")
    @Expose
    private List<ReportShowItem> list;


    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public List<ReportShowItem> getList() {
        return list;
    }

    public void setList(List<ReportShowItem> list) {
        this.list = list;
    }
}
