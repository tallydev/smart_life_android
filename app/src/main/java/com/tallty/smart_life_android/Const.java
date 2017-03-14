package com.tallty.smart_life_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * 订单相关
     */
    public static final String CANCEL_ORDER = "cancel_order";
    public static final String DELETE_ORDER = "delete_order";
    public static final String SERVICE_ORDER = "service_order";
    public static final String PAY_ORDER = "pay_order";
    public static final String TYPE_NORMAL = "type_normal";
    public static final String TYPE_PROMOTION = "type_promotion";

    /**
     * 推送相关
     */
    public static final String JPUSH = "jpush_notification";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String PUSH_TITLE = "jpush_title";
    public static final String PUSH_TIME = "jpush_time";
    public static final String PUSH_IMAGES = "jpush_images";
    public static final String HOME_BUNDLE = "home_bundle";

    /**
     * 分享相关
     */
    public static final String SHARE = "app_share";

    /**
     * 首页模块
     */
    // 类别
    public static String BLOCK_HEALTHY = "智慧健康";
    public static String BLOCK_SPORT = "健步达人";
    public static String BLOCK_ACTIVITY = "社区活动";
    public static String BLOCK_HOME = "智慧家居";
    public static String BLOCK_SERVICE = "上门服务";
    public static String BLOCK_GOVERNMENT = "政府直通车";
    public static String BLOCK_LIMIT = "限量发售";
    public static String BLOCK_MARKET = "精品超市";
    // 模块
    public static List<String> HOME_BLOCK_TITLES = new ArrayList<String>() {
        {
            add(BLOCK_HEALTHY);
            add(BLOCK_SPORT);
            add(BLOCK_ACTIVITY);
            add(BLOCK_HOME);
            add(BLOCK_SERVICE);
            add(BLOCK_GOVERNMENT);
            add(BLOCK_LIMIT);
            add(BLOCK_MARKET);
        }
    };

    public static HashMap<String, ArrayList<String>> HOME_BLOCK_SUBTITLES = new HashMap<String, ArrayList<String>>() {
        {
            put("智慧健康", new ArrayList<String>(){{ add("预约体检"); add("健康报告"); add("预约专家"); }});
            put(BLOCK_SPORT, new ArrayList<String>(){{ add("更多数据"); }});
            put(BLOCK_ACTIVITY, new ArrayList<String>(){{ add("活动详情"); }});
            put(BLOCK_HOME, new ArrayList<String>(){{ add("远程控制"); add("电子猫眼"); }});
            put(BLOCK_SERVICE, new ArrayList<String>(){{ add("上门服务"); }});
            put(BLOCK_GOVERNMENT, new ArrayList<String>(){{ add("更多内容"); }});
            put(BLOCK_LIMIT, new ArrayList<String>(){{ add("我要参团"); }});
            put(BLOCK_MARKET, new ArrayList<String>(){{ add("更多臻品"); }});
        }
    };

    public static HashMap<String, ArrayList<Integer>> HOME_BLOCK_SUBICONS = new HashMap<String, ArrayList<Integer>>() {
        {
            put("智慧健康", new ArrayList<Integer>(){{ add(R.mipmap.smart_healthy_one); add(R.mipmap.smart_healthy_two); add(R.mipmap.smart_healthy_three); }});
            put(BLOCK_SPORT, new ArrayList<Integer>(){{ add(R.mipmap.fitness_people_one); }});
            put(BLOCK_ACTIVITY, new ArrayList<Integer>(){{ add(R.mipmap.community_activity_one); }});
            put(BLOCK_HOME, new ArrayList<Integer>(){{ add(R.mipmap.smart_home_one); add(R.mipmap.smart_home_two); }});
            put(BLOCK_SERVICE, new ArrayList<Integer>(){{ add(R.mipmap.service_one); }});
            put(BLOCK_GOVERNMENT, new ArrayList<Integer>(){{ add(R.mipmap.more_icon); }});
            put(BLOCK_LIMIT, new ArrayList<Integer>(){{ add(R.mipmap.supermarket_one); }});
            put(BLOCK_MARKET, new ArrayList<Integer>(){{ add(R.mipmap.more_icon); }});
        }
    };
}
