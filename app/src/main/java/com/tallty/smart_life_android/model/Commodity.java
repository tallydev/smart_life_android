package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/7/23.
 * 商品
 */

public class Commodity implements Serializable{

    public Commodity(){

    }

    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName ("checked")
    @Expose
    private boolean checked;

    @SerializedName ("name")
    @Expose
    private String name;

    @SerializedName ("price")
    @Expose
    private float price;

    @SerializedName ("photo_id")
    @Expose
    private int photo_id;

    @SerializedName ("count")
    @Expose
    private int count;



    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
