package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kang on 2017/1/11.
 * 社区列表
 * 省 - 市 - 区 - 街道 - 小区
 */

public class Communities implements Serializable {
    // 省
    @SerializedName ("provinces")
    @Expose
    private List<String> provinces;

    // 省 => [市, ...]
    @SerializedName ("citys")
    @Expose
    private HashMap<String, List<String>> cities;

    // 市 => [区, ...]
    @SerializedName ("districts")
    @Expose
    private HashMap<String, List<String>> areas;

    // 区 => [街道(社区)id, ...]
    @SerializedName ("subdistricts")
    @Expose
    private HashMap<String, List<String>> streets;

    public List<String> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<String> provinces) {
        this.provinces = provinces;
    }

    public HashMap<String, List<String>> getCities() {
        return cities;
    }

    public void setCities(HashMap<String, List<String>> cities) {
        this.cities = cities;
    }

    public HashMap<String, List<String>> getAreas() {
        return areas;
    }

    public void setAreas(HashMap<String, List<String>> areas) {
        this.areas = areas;
    }

    public HashMap<String, List<String>> getStreets() {
        return streets;
    }

    public void setStreets(HashMap<String, List<String>> streets) {
        this.streets = streets;
    }
}
