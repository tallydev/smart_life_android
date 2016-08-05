package com.tallty.smart_life_android;

/**
 * Created by kang on 16/8/3.
 * 通用常量集合
 * (传递数据,作为key使用)
 */

public class Const {
    /**
     * 通用数据
     */
    public static final String BUNDLE = "bundle";
    public static final String OBJECT = "object";
    public static final String OBJECT_List = "object_list";
    public static final String INT = "int";


    /**
     * SharedPreferences
     **/
    public static final String EMPTY_STRING = "";
    // User
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USER_AVATAR = "user_avatar";
    // Address
    public static final String ADDRESS_AREA = "address_area";
    public static final String ADDRESS_DETAIL = "address_detail";
    public static final String ADDRESS_PHONE = "address_phone";
    public static final String ADDRESS_NAME = "address_name";


    /**
     * 创建Fragment
     */
    public static final String TOOLBAR_TITLE = "BackFragmentTitle";
    public static final String TOTAL_PRICE = "BackFragmentTotalPrice";
    public static final String FROM = "from_fragment";

    /**
     * 调用MyAddress的不同处理
     */
    public static final int FROM_PROFILE = 2;
    public static final int FROM_ORDER = 3;
}
