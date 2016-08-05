package com.tallty.smart_life_android.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by kang on 16/6/25.
 * 步数表
 */

@Table("step")
public class Step {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey (AssignType.AUTO_INCREMENT)
    private int id;

    @Column("today")
    private String today;

    @Column ("step")
    private String step;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
