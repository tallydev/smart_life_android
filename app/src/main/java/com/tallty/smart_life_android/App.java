package com.tallty.smart_life_android;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tallty.smart_life_android.Engine.Engine;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kang on 16/6/14.
 * Application
 */
public class App extends Application{
    private static App sInstance;
    // 网络请求实例
    private String baseUrl = "http://220.163.125.158:8081/";
    private Engine noHeaderEngine;
    private Engine headerEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Logger.init();
        setNoHeaderEngine();
    }

    public static App getInstance() {
        return sInstance;
    }

    /**
     * 判断是否是主线程
     */
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

    /**
     * 无Header数据请求服务
     */
    public Engine noHeaderEngine() {
        return noHeaderEngine;
    }

    /**
     * 有Header数据请求服务
     */
    public Engine headerEngine() {
        return headerEngine;
    }

    // 设置无header数据的通用链接
    private void setNoHeaderEngine() {
        noHeaderEngine = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Engine.class);
    }

    // 设置有header数据的通用链接
    // (登录成功时,初始化)
    public void setHeaderEngine(final String phone, final String token) {
        // 定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", token)
                        .addHeader("X-User-Phone", phone)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        // 创建Retrofit实例
        headerEngine = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Engine.class);
    }
}
