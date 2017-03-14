package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 2017/3/14.
 * 政府直通车 - 新闻分类 - 新闻
 */

public class News implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("news_sort_id")
    @Expose
    private int news_sort_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("created_at")
    @Expose
    private String createdTime;

    @SerializedName("updated_at")
    @Expose
    private String updatedTime;

    @SerializedName("news_cover")
    @Expose
    private String newsThumb;

    @SerializedName("news_detail")
    @Expose
    private String newsDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNews_sort_id() {
        return news_sort_id;
    }

    public void setNews_sort_id(int news_sort_id) {
        this.news_sort_id = news_sort_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getNewsThumb() {
        return newsThumb;
    }

    public void setNewsThumb(String newsThumb) {
        this.newsThumb = newsThumb;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
