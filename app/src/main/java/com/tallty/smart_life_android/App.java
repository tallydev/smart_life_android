package com.tallty.smart_life_android;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.Engine.Engine;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kang on 16/6/14.
 * Application
 */
public class App extends Application{
    private static App sInstance;
    private Context context;
    // 网络请求实例
    private Engine noHeaderEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        Logger.init();
        setLoginEngine();
    }

    public static App getInstance() {
        return sInstance;
    }

    // 判断是否是主线程
    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    // 设置登录链接
    private void setLoginEngine() {
        noHeaderEngine = new Retrofit.Builder()
                .baseUrl("http://220.163.125.158:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }

    /**
     * 返回登录链接
     */
    public Engine getNoHeaderEngine() {
        return noHeaderEngine;
    }
}
