package com.tallty.smart_life_android.Engine;

import com.tallty.smart_life_android.model.Activities;
import com.tallty.smart_life_android.model.Appointment;
import com.tallty.smart_life_android.model.AppointmentList;
import com.tallty.smart_life_android.model.CartItem;
import com.tallty.smart_life_android.model.CartList;
import com.tallty.smart_life_android.model.Categories;
import com.tallty.smart_life_android.model.Category;
import com.tallty.smart_life_android.model.Contact;
import com.tallty.smart_life_android.model.ContactList;
import com.tallty.smart_life_android.model.Home;
import com.tallty.smart_life_android.model.Product;
import com.tallty.smart_life_android.model.ProductList;
import com.tallty.smart_life_android.model.ReportList;
import com.tallty.smart_life_android.model.ReportShowList;
import com.tallty.smart_life_android.model.SportData;
import com.tallty.smart_life_android.model.SportRank;
import com.tallty.smart_life_android.model.Step;
import com.tallty.smart_life_android.model.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.DELETE;
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

public interface DataAPI {
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

    // 重置
    @FormUrlEncoded
    @POST("user_info/reset")
    Call<User> resetPassword(@Field("user[phone]") String phone,
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

    // *********************************************************************************************
    // 商品分类
    @GET("product_sorts")
    Call<Categories> getProductCategories();

    // 根据类别查看商品
    @GET("products/sort")
    Call<ProductList> getProductsBycategory(@Query("page") int page,
                                            @Query("per_page") int per_page,
                                            @Query("product_sort_id") int category_id);

    // 获取商品列表
    @GET("products")
    Call<ProductList> getProductList(@Query("page") int page,
                                     @Query("per_page") int per_page);

    // 获取商品详情
    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    // 获取购物车列表
    @GET("cart_items")
    Call<CartList> getCartList(@Query("page") int page,
                               @Query("per_page") int per_page);

    // 添加商品到购物车
    @FormUrlEncoded
    @POST("cart_items")
    Call<CartItem> addProductToCart(@Field("cart_item[product_id]") int product_id,
                                    @Field("cart_item[count]") int count);

    // 删除购物车的一个商品
    @DELETE("cart_items/{id}")
    Call<CartItem> deleteCartItem(@Path("id") int id);

    // 联系人列表
    @GET("contacts")
    Call<ContactList> getContacts();

    // 新增联系人
    @FormUrlEncoded
    @POST("contacts")
    Call<ContactList> createContact(@Field("contact[name]") String name,
                                    @Field("contact[phone]") String phone,
                                    @Field("contact[area]") String area,
                                    @Field("contact[street]") String street,
                                    @Field("contact[community]") String community,
                                    @Field("contact[address]") String address,
                                    @Field("contact[is_default]") boolean is_default);

    // 删除联系人
    @DELETE("contacts/{id}")
    Call<Contact> deleteContact(@Path("id") int contact_id);

    // *********************************************************************************************

    // 上传运动步数
    @FormUrlEncoded
    @POST("sports")
    Call<Step> uploadStep(@Field("sport[date]") String date,
                          @Field("sport[count]") int count,
                          @Field("sport[platform]") String platform,
                          @Field("sport[version]") int version);

    // 基于时间线的运动统计信息
    @GET("sports/{timeLine}")
    Call<SportData> getSportsData(@Path("timeLine") String timeLine);

    // 基于时间线的运动rank信息
    @GET("ranks/{timeLine}")
    Call<SportRank> getSportRanks(@Path("timeLine") String timeLine,
                                  @Query("page") Integer page,
                                  @Query("per_page") Integer per_page);

    // *********************************************************************************************

    // 提交预约首页特定的服务或活动
    @FormUrlEncoded
    @POST("appointments")
    Call<Appointment> submitAppointment(@Field("appointment[type]") String type,
                                        @Field("appointment[count]") int count);

    // 我的预约列表
    @GET("appointments")
    Call<AppointmentList> getAppointments(@Query("page") int page,
                                          @Query("per_page") int per_page);

    // *********************************************************************************************

    // 健康报告
    @GET("reports")
    Call<ReportList> getCheckReport();

    // 单项历史数据
    @GET("reports/{name}")
    Call<ReportShowList> getReportHistory(@Path("name") String name);

    // *********************************************************************************************

    // 获取首页信息
    @GET("home")
    Call<Home> getHomeData();

    // *********************************************************************************************
    // 社区活动列表
    @GET("activity/sqhds")
    Call<Activities> getActivities();

    // 报名社区活动
    @FormUrlEncoded
    @POST("activity/sqhds/{id}/appoint")
    Call<Appointment> applyCommunityActivity(@Path("id") int activity_id,
                                             @Field("appointment[count]") int count);
}