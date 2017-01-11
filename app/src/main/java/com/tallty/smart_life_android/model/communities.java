package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kang on 2017/1/11.
 * 社区列表
 * 省 - 市 - 区 - 街道 - 小区
 */

public class Communities implements Serializable {
    // 省
    @SerializedName ("provinces")
    @Expose
    private ArrayList<String> provinces;

    // 省 => [市, ...]
    @SerializedName ("citys")
    @Expose
    private HashMap<String, ArrayList<String>> cities;

    // 市 => [街道, ...]
    @SerializedName ("districts")
    @Expose
    private HashMap<String, ArrayList<String>> districts;

    // 街道 => [小区id, ...]
    @SerializedName ("subdistricts")
    @Expose
    private HashMap<String, ArrayList<String>> village;
}
