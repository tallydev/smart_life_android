package com.tallty.smart_life_android;

import java.util.HashMap;

/**
 * Created by kang on 16/8/3.
 * 通用常量集合、静态变量集合
 * (传递数据,作为key使用)
 */

public class Const {
    public static final String PLATFORM = "android";
    /**
     * 通用数据
     */
    public static final String BUNDLE = "bundle";
    public static final String OBJECT = "object";
    public static final String OBJECT_List = "object_list";
    public static final String INT = "int";
    public static final String STRING = "string";
    public static final String ARRAY = "array";

    /**
     * 计步服务消息传递控制常量
     */
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
    public static final int REQUEST_SERVER = 2;
    public static final int SET_STEP = 3;

    /**
     * SharedPreferences & DB
     **/
    // DB
    public static final String DB_NAME = "smart_life";
    // SharedPreferences
    public static final String EMPTY_STRING = "";
    // User
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USER_AVATAR = "user_avatar";
    public static final String PHONE_PATTEN = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";
    // Contact
    public static final String CONTACT_ID = "contact_ID";
    public static final String CONTACT_AREA = "contact_area";
    public static final String CONTACT_STREET = "contact_street";
    public static final String CONTACT_COMMUNITY = "contact_community";
    public static final String CONTACT_ADDRESS = "contact_address";
    public static final String CONTACT_PHONE = "contact_phone";
    public static final String CONTACT_NAME = "contact_name";

    /**
     * 创建Fragment
     */
    public static final String FRAGMENT_NAME = "BackFragmentTitle";
    public static final String TOTAL_PRICE = "BackFragmentTotalPrice";

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
            put("XPSS", "精品超市");
            put("ZNJJ", "智能家居");
        }
    };

    /**
     * 购物车相关
     */
    public static final String SALE_OFF = "sale_off";

    /**
     * 收货地址相关
     */
    public static final String DELETE_ADDRESS = "delete_address";
    public static final String EDIT_ADDRESS = "edit_address";
    public static final String SET_ADDRESS_DEFAULT = "set_address_default";

    /**
     * 社区相关
     */
    public static final String PROVINCES = "community_provinces";
    public static final String CITIES = "community_cities";
    public static final String AREAS = "community_areas";
    public static final String COMMUNITIES = "community_communities";
    public static final String VILLAGES = "community_villages";

    /**
     * 订单相关
     */
    public static final String CANCEL_ORDER = "cancel_order";
    public static final String DELETE_ORDER = "delete_order";
    public static final String SERVICE_ORDER = "service_order";
    public static final String PAY_ORDER = "pay_order";

    /**
     * 推送相关
     */
    public static final String JPUSH = "jpush_notification";
    public static final String PUSH_TITLE = "jpush_title";
    public static final String PUSH_TIME = "jpush_time";
    public static final String PUSH_IMAGES = "jpush_images";
}
