package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 16/8/8.
 * 运动排名
 */

public class SportRank implements Serializable {
    @SerializedName("total_pages")
    @Expose
    private int total_pages;

    @SerializedName("current_page")
    @Expose
    private int current_page;

    @SerializedName("top")
    @Expose
    private ArrayList<SportRankItem> top = new ArrayList<>();

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

    public ArrayList<SportRankItem> getTop() {
        return top;
    }

    public void setTop(ArrayList<SportRankItem> top) {
        this.top = top;
    }
}
