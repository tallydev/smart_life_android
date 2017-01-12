package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 2017/1/12.
 * 社区列表 - subdistricts 社区对象
 */

public class CommunityObject implements Serializable {
    @SerializedName ("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("province")
    @Expose
    private String province;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("district")
    @Expose
    private String area;

    @SerializedName("subdistrict")
    @Expose
    private String street;

    @SerializedName("communities")
    @Expose
    private ArrayList<CommunityVillage> villages;

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public ArrayList<CommunityVillage> getVillages() {
        return villages;
    }

    public void setVillages(ArrayList<CommunityVillage> villages) {
        this.villages = villages;
    }
}
