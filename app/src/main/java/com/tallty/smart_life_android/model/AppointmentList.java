package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kang on 16/7/28.
 * 预约列表
 */

public class AppointmentList implements Serializable {

    @SerializedName ("total_pages")
    @Expose
    private int total_pages;

    @SerializedName ("current_page")
    @Expose
    private int current_page;

    @SerializedName ("appointments")
    @Expose
    private List<Appointment> appointments;


    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
