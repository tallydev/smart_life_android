package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 2017/1/11.
 * 所有社区列表 接口返回数据
 */

public class CommunitiesResponse implements Serializable {
    @SerializedName ("subdistricts")
    @Expose
    private ArrayList<CommunityObject> subdistricts;

    @SerializedName ("list")
    @Expose
    private ArrayList<Object> list;

    @SerializedName ("list2")
    @Expose
    private Communities list2;

    public ArrayList<CommunityObject> getSubdistricts() {
        return subdistricts;
    }

    public void setSubdistricts(ArrayList<CommunityObject> subdistricts) {
        this.subdistricts = subdistricts;
    }

    public ArrayList<Object> getList() {
        return list;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
    }

    public Communities getList2() {
        return list2;
    }

    public void setList2(Communities list2) {
        this.list2 = list2;
    }
}
