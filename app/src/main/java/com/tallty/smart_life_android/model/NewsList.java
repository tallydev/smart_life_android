package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 2017/3/14.
 * 新闻列表模型
 */

public class NewsList implements Serializable {
    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("current_page")
    @Expose
    private int currentPage;

    @SerializedName("news")
    @Expose
    private ArrayList<News> news = new ArrayList<>();

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }
}
