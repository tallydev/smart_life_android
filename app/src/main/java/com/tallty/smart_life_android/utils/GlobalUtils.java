package com.tallty.smart_life_android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.R;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kang on 2016/10/27.
 * app 工具类
 */

public class GlobalUtils {

    public static String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    // 获取版本号
    public static String getVersion(Context context)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知版本";
        }
    }


    // 获取版本号(内部识别号)
    public static int getVersionCode(Context context)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
