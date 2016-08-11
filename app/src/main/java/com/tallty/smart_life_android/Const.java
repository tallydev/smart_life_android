package com.tallty.smart_life_android;

import java.util.HashMap;

/**
 * Created by kang on 16/8/3.
 * 通用常量集合、静态变量集合
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
    public static final String STRING = "string";

    /**
     * 计步服务消息传递控制常量
     */
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
    public static final int REQUEST_SERVER = 2;

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
    // Contact
    public static final String ADDRESS_AREA = "address_area";
    public static final String ADDRESS_DETAIL = "address_detail";
    public static final String ADDRESS_PHONE = "address_phone";
    public static final String ADDRESS_NAME = "address_name";

    /**
     * 创建Fragment
     */
    public static final String FRAGMENT_NAME = "BackFragmentTitle";
    public static final String TOTAL_PRICE = "BackFragmentTotalPrice";
    public static final String FROM = "from_fragment";

    /**
     * 调用MyAddress的不同处理
     */
    public static final int FROM_PROFILE = 2;
    public static final int FROM_ORDER = 3;

    /**
     * 预约相关
     */
    public static final HashMap<String, String> APPOINTMENT_TYPES
            = new HashMap<String, String>(){
        {
            put("ZHJK", "智慧健康");
            put("DZMY", "电子猫眼");
            put("ITFW", "IT服务");
            put("ITXT", "IT学堂");
            put("SQHD", "社区活动");
            put("XPSS", "新品上市");
            put("ZNJJ", "智能家居");
        }
    };
}
