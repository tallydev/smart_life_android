package com.tallty.smart_life_android.Engine;

import com.tallty.smart_life_android.model.Appointment;
import com.tallty.smart_life_android.model.AppointmentList;
import com.tallty.smart_life_android.model.CartList;
import com.tallty.smart_life_android.model.ProductList;
import com.tallty.smart_life_android.model.SportData;
import com.tallty.smart_life_android.model.SportRank;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.model.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    // 更新用户信息(不包括头像)
    @FormUrlEncoded
    @PUT("user_info")
    Call<User> updateUser(@Header("X-User-Token") String token,
                          @Header("X-User-Phone") String phone,
                          @FieldMap(encoded = true) Map<String, String> fields);

    // 更新用户头像
    @Multipart
    @PUT("user_info")
    Call<User> updateUserPhoto(@Part MultipartBody.Part file);

    // 查询用户信息
    @GET("user_info")
    Call<User> getUser();

    // 获取商品列表
    @GET("products")
    Call<ProductList> getProductList(@Query("page") Integer page,
                                     @Query("per_page") Integer per_page);

    // *********************************************************************************************

    // 获取购物车列表
    @GET("cart_items")
    Call<CartList> getCartList(@Query("page") Integer page,
                               @Query("per_page") Integer per_page);

    // *********************************************************************************************

    // 上传运动步数
    @FormUrlEncoded
    @POST("sports")
    Call<Step> uploadStep(@Field("sport[date]") String date, @Field("sport[count]") int count);

    // 基于时间线的运动统计信息
    @GET("sports/{timeLine}")
    Call<SportData> getSportsData(@Path("timeLine") String timeLine);

    // 基于时间线的运动rank信息
    @GET("ranks/{timeLine}")
    Call<SportRank> getSportRanks(@Path("timeLine") String timeLine);

    // *********************************************************************************************

    // 提交预约
    @FormUrlEncoded
    @POST("appointments")
    Call<Appointment> submitAppointment(@Field("appointment[type]") String type,
                                        @Field("appointment[count]") int count);

    // 预约列表
    @GET("appointments")
    Call<AppointmentList> getAppointments(@Query("page") int page,
                                          @Query("per_page") int per_page);
}