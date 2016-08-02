package com.tallty.smart_life_android.Engine;

import com.tallty.smart_life_android.model.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by kang on 16/8/1.
 * base_url: http://220.163.125.158:8081
 * 业务接口
 */

public interface Engine {
    // 登录
    @FormUrlEncoded
    @POST("users/sign_in")
    Call<User> login(@Field("user[phone]") String phone,
                     @Field("user[password]") String password);

    // 获取验证码
    @FormUrlEncoded
    @POST("sms_tokens/register")
    Call<HashMap<String, String>> getSms(@Field("sms_token[phone]") String phone);

    // 注册
    @FormUrlEncoded
    @POST("users")
    Call<User> registerUser(@Field("user[phone]") String phone,
                            @Field("user[password]") String password,
                            @Field("user[sms_token]") String sms);

    // 更新用户信息
    @FormUrlEncoded
    @PUT("user_info")
    Call<User> updateUser(@Header("X-User-Token") String token,
                          @Header("X-User-Phone") String phone,
                          @FieldMap(encoded = true) Map<String, String> fields);

    // 查询用户信息
    @GET("user_info")
    Call<User> getUser();
}