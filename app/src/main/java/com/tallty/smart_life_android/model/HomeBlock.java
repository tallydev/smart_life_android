package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 2017/3/8.
 * 首页模块
 */

public class HomeBlock implements Serializable {

    public HomeBlock(String title, String image, ArrayList<String> subTitles, ArrayList<Integer> subIcons) {
        this.title = title;
        this.image = image;
        this.subTitles = subTitles;
        this.subIcons = subIcons;
    }

    public HomeBlock(String title, String image) {
        this.title = title;
        this.image = image;
    }

    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName ("title")
    @Expose
    private String title;

    @SerializedName ("home_block_cover")
    @Expose
    private String image;

    @SerializedName ("sub_titles")
    @Expose
    private ArrayList<String> subTitles = new ArrayList<>();

    @SerializedName ("sub_icons")
    @Expose
    private ArrayList<Integer> subIcons = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getSubTitles() {
        return subTitles;
    }

    public void setSubTitles(ArrayList<String> subTitles) {
        this.subTitles = subTitles;
    }

    public ArrayList<Integer> getSubIcons() {
        return subIcons;
    }

    public void setSubIcons(ArrayList<Integer> subIcons) {
        this.subIcons = subIcons;
    }
}
