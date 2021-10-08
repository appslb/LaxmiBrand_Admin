package com.e.laxmibrand_admin.retrofit;

import androidx.annotation.RawRes;

import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.beans.GetAllCategoryResponse;
import com.e.laxmibrand_admin.beans.LoginResponse;
import com.e.laxmibrand_admin.beans.Products;
import com.e.laxmibrand_admin.beans.UploadImageResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetroInterface {

    @Multipart
    @POST("admin/login")
    Call<LoginResponse> verify_login(@Part ("email") RequestBody email,
                                     @Part ("password") RequestBody password);


    @Multipart
    @POST("admin/category/insert")
    Call<BaseResponse> insert_category(
                                       @Part("category_name") RequestBody cat_name,
                                       @Part("is_active") RequestBody is_active,
    @Part MultipartBody.Part file
    );


    @Multipart
    @POST("admin/category/update")
    Call<BaseResponse> update_category(
            @Part("category_id") RequestBody cat_id,

            @Part("category_name") RequestBody cat_name,
            @Part("is_active") RequestBody is_active,
            @Part MultipartBody.Part file
    );


    @POST("user/save_order")
    Call<BaseResponse> place_order(
            @Body JsonObject orderDetails
    );


    @POST("user/save_device")
    Call<BaseResponse> save_device(@Body JsonObject userdetails);

    @POST("admin/category/list/1")
    Call<ResponseBody> getAllCategory ();

    @POST("admin/category/list/json")
    Call<ResponseBody> getAllACategory(@Query("pageno") String pageno);

    @POST
    Call<ResponseBody> getCatNew(@Url String url);

    @POST("admin/faq/insert")
    Call<BaseResponse> insert_faq(@Body JsonObject body);

    @POST("admin/faq/delete")
    Call<ResponseBody> deleteFAQ(@Body JsonObject deleteFAQ);

    @POST("admin/faq/list")
    Call<ResponseBody> getFaqList();

    @POST("admin/upload/update_aboutus_data")
    Call<BaseResponse> about_us(@Body JsonObject about_us_desc);
    @POST("admin/upload/update_contactus_data")
    Call<BaseResponse> contact_us(@Body JsonObject contact_us_details);

    @POST("admin/faq/update")
    Call<BaseResponse> update_faq(@Body JsonObject body);

    @POST("admin/faq/detail")
    Call<ResponseBody> getFaqDetail(@Body JsonObject bod);

    @POST("admin/offer/insert")
    Call<BaseResponse> insert_offer(@Body JsonObject body);

    @POST("admin/offer/list")
    Call<ResponseBody> getOfferList();

    @POST("admin/offer/detail")
    Call<ResponseBody> getOfferDetail(@Body JsonObject bod);

    @POST("admin/order/list")
    Call<ResponseBody> getOrderList();

    @POST("user/product/list")
    Call<ResponseBody> getUserProductList(@Body JsonObject body);

    @POST("admin/product/list/2")
    Call<ResponseBody> getAllProducts();

    @POST
    Call<ResponseBody> getProductsByPage(@Url String url,
    @Body JsonObject body);

    @POST
    Call<ResponseBody> getOrderNew(@Url String url);


    @POST
    Call<ResponseBody> getCategoryByPage(@Url String url);

    @Multipart
    @POST("admin/upload/advertisement_slider")
    Call<BaseResponse> addAdvertisementSliderImages(@Part List<MultipartBody.Part> file,@Part List<MultipartBody.Part> file1);

    @GET("admin/whatsappgrplink")
    Call<BaseResponse> getWhatsappGroupLink();

    @Multipart
    @POST("admin/upload/promotional_banner")
    Call<BaseResponse> addPromotionalBanner(@Part List<MultipartBody.Part> file,@Part List<MultipartBody.Part> file1);


    @POST("admin/product/detail")
    Call<ResponseBody> getProductDetail(@Body JsonObject detail);


    @POST("admin/whatsappgrplink/edit")
    Call<BaseResponse> addwhatsapplink(@Body JsonObject link);

    @Multipart
    @POST("admin/product/update")
    Call<BaseResponse> updateProductDetail(@Part List<MultipartBody.Part> file, @Part("pdt_id") RequestBody pdt_id,

                                           @Part("pdt_name") RequestBody pdt_name,
                                           @Part("category_id") RequestBody category_id,
                                           @Part("pdt_discount_display") RequestBody pdt_discount_display,
                                           @Part("pdt_about") RequestBody pdt_about,
                                           @Part("pdt_storage_uses") RequestBody pdt_storage_uses,
                                           @Part("pdt_other_info") RequestBody pdt_other_info,
                                           @Part("is_active") RequestBody is_active,
                                           @Part("var_id") RequestBody var_id,
                                           @Part List<MultipartBody.Part> str1,
                                           @Part List<MultipartBody.Part> str2,
                                           @Part List<MultipartBody.Part> str3,
                                           @Part List<MultipartBody.Part> str4
    );




    @POST("admin/product/delete")
    Call<ResponseBody> deleteProductDetail(@Body JsonObject deleteProduct);
    @POST("admin/category/delete")
    Call<ResponseBody> deleteCategory(@Body JsonObject deleteCategory);
    /*
    @Field("image\"; filename=\"pp.png\" "
    @Part("image[]") RequestBody file,
    @Part List<MultipartBody.Part> file,
*/
   // @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/product/store")
    Call<BaseResponse> addNewProduct(@Part List<MultipartBody.Part> file,
                                     @Part("pdt_name") RequestBody pdt_name,
                                     @Part("category_id") RequestBody category_id,
                                     @Part("pdt_discount_display") RequestBody pdt_discount_display,
                                     @Part("pdt_about") RequestBody pdt_about,
                                     @Part("pdt_storage_uses") RequestBody pdt_storage_uses,
                                     @Part("pdt_other_info") RequestBody pdt_other_info,
                                     @Part("is_active") RequestBody is_active,
                                     @Part("var_id") RequestBody var_id,
                                     @Part List<MultipartBody.Part> str1,
                                     @Part List<MultipartBody.Part> str2,
                                     @Part List<MultipartBody.Part> str3,
                                     @Part List<MultipartBody.Part> str4


    );


    @Multipart
    @POST("admin/upload/images")
    Call<UploadImageResponse> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);





}


