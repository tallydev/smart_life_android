package com.tallty.smart_life_android.model;

/**
 * Created by kang on 16/6/15.
 *
 */
public class User implements IUser{
    String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
