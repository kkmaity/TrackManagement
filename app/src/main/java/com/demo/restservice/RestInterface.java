package com.demo.restservice;


import com.demo.utils.Constant;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestInterface {

    String BASE_URL = Constant.BASE_URL;


    @FormUrlEncoded
    @POST("api/users/appinfo")
    Call<ResponseBody> appinfo(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("all_info.txt")
    Call<ResponseBody> appinfospalsh(@FieldMap Map<String, String> params);

    @POST("all_info.txt")
    Call<ResponseBody> getSearchData(@FieldMap Map<String, String> params);

    @Multipart
    @POST("api/users/uploadPrescriptionForSamplePickUp")
    Call<ResponseBody> uploadPrescriptionForSamplePickUp(
            @Part("user_family_id") RequestBody user_family_id,
            @Part("pincode") RequestBody pincode,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("book_by") RequestBody book_by,
            @Part("user_id") RequestBody user_id,
            @Part("book_from") RequestBody book_from,
            @Part List<MultipartBody.Part> files);






}
