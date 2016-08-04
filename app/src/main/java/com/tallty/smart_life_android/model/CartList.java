package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kang on 16/8/4.
 * 购物车列表
 */

public class CartList implements Serializable {
    public CartList() {

    }

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("current_page")
    @Expose
    private int currentPage;

    @SerializedName("cart_items")
    @Expose
    private ArrayList<CartItem> cartItems = new ArrayList<>();

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

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
