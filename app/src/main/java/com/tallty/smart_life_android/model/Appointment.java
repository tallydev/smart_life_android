package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/7/28.
 * 预约
 */

public class Appointment implements Serializable {

    public Appointment() {

    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("idname")
    @Expose
    private String number;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("appointment_type")
    @Expose
    private String appointmentType;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("state_alias")
    @Expose
    private String stateAlias;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAlias() {
        return stateAlias;
    }

    public void setStateAlias(String stateAlias) {
        this.stateAlias = stateAlias;
    }
}
