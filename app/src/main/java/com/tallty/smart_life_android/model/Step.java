package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by kang on 16/6/25.
 * 步数
 */

@Table("count")
public class Step implements Serializable {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey (AssignType.AUTO_INCREMENT)
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @Column("date")
    @SerializedName("date")
    @Expose
    private String date;

    @Column ("count")
    @SerializedName("count")
    @Expose
    private String count;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
