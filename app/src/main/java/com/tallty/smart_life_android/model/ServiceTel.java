package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kang on 2017/2/10.
 * 客服电话
 */

public class ServiceTel implements Serializable {
    @SerializedName ("customer_services")
    @Expose
    private ArrayList<HashMap<String, String>> tels = new ArrayList<>();

    public ArrayList<HashMap<String, String>> getTels() {
        return tels;
    }

    public void setTels(ArrayList<HashMap<String, String>> tels) {
        this.tels = tels;
    }
}
