package com.tallty.smart_life_android.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
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
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.CLIPBOARD_SERVICE;

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
            PackageInfo pi=context.getPackageManager().getPackageInfo("com.tallty.smart_life_android", 0);
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
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.tallty.smart_life_android", 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // APP 是否在运行
    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // APP 是否是在前台显示
    public static boolean isAppForeground(Context context) {
        // Get the Activity Manager
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        // Get a list of running tasks, we are only interested in the last one,
        // the top most so we give a 1 as parameter so we only get the topmost.
        List<ActivityManager.RunningAppProcessInfo> task = manager.getRunningAppProcesses();

        // Get the info we need for comparison.
        ComponentName componentInfo = task.get(0).importanceReasonComponent;

        // Check if it matches our package name.
        if(componentInfo.getPackageName().equals("com.tallty.smart_life_android"))
            return true;

        // If not then our app is not on the foreground.
        return false;
    }

    // 获取剪切板信息
    public static String getClipboardData(Context context) {
        ClipboardManager cm = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        ClipData cd = cm.getPrimaryClip();
        if (cd == null) return "";
        return cd.getItemAt(0).getText().toString();
    }

    // 清空剪切板
    public static void setClipboardData(Context context, String string) {
        ClipboardManager cm = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", string); //文本型数据 clipData 的构造方法。
        cm.setPrimaryClip(clipData); // 将 字符串 str 保存 到剪贴板。
    }
}
