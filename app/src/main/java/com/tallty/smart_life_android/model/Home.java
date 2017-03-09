package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kang on 16/8/12.
 * 首页信息
 */

public class Home implements Serializable {
    @SerializedName("fitness")
    @Expose
    private HashMap<String, String> fitness = new HashMap<>();

    @SerializedName("newer")
    @Expose
    private HashMap<String, String> newer = new HashMap<>();

    @SerializedName("product")
    @Expose
    private HashMap<String, String> product = new HashMap<>();

    @SerializedName("banners")
    @Expose
    private ArrayList<Banner> banners = new ArrayList<>();

    @SerializedName("home_blocks")
    @Expose
    private ArrayList<HomeBlock> blocks = new ArrayList<>();


    public HashMap<String, String> getFitness() {
        return fitness;
    }

    public void setFitness(HashMap<String, String> fitness) {
        this.fitness = fitness;
    }

    public HashMap<String, String> getNewer() {
        return newer;
    }

    public void setNewer(HashMap<String, String> newer) {
        this.newer = newer;
    }

    public HashMap<String, String> getProduct() {
        return product;
    }

    public void setProduct(HashMap<String, String> product) {
        this.product = product;
    }

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public ArrayList<HomeBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<HomeBlock> blocks) {
        this.blocks = blocks;
    }
}

