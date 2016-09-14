package com.tallty.smart_life_android.Engine;

import android.util.Log;

import com.tallty.smart_life_android.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kang on 16/8/13.
 * Retrofit 网络引擎
 */

public class Engine {
    private static final String baseUrl = "http://elive.clfsj.com:8081/";
    private static DataAPI authService = null;
    private static DataAPI noAuthService = null;

    // 未鉴权的网络服务
    public static DataAPI noAuthService() {
        if (noAuthService == null) {
            setNoAuthService();
            Log.d(App.TAG, "初始化了NoAuth");
        }
        return noAuthService;
    }

    // 鉴权的网络服务
    public static DataAPI authService(String token, String phone) {
        if (authService == null) {
            setAuthService(token, phone);
            Log.d(App.TAG, "初始化了Auth");
        }
        return authService;
    }


    private static void setAuthService(final String token, final String phone) {
        // 定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("X-User-Token", token)
                        .addHeader("X-User-Phone", phone)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        // 创建Retrofit实例
        authService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(DataAPI.class);
    }

    private static void setNoAuthService() {
        // 定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        noAuthService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(DataAPI.class);
    }
}
