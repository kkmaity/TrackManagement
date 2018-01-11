package com.demo.restservice;


import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.model.login.ApiLoginParam;
import com.demo.model.login.LoginMain;
import com.demo.model.registration.RegistrationMain;
import com.demo.model.registration.ApiRegistrationParam;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestInterface {

    String BASE_URL = "http://173.214.180.212/";

    @POST("emp_track/api/appconfig.php")
    Call<AppConfigMain> appconfig(@Body AppConfigParam params);

    @POST("emp_track/api/userRegister.php")
    Call<RegistrationMain> userRegister(@Body ApiRegistrationParam params);

    @POST("emp_track/api/userLogin.php")
    Call<LoginMain> userLogin(@Body ApiLoginParam params);

    @POST("emp_track/api/attendance_history.php")
    Call<AttendenceHistoryMain> attendance_history(@Body ApiAttendenceHistoryParam params);


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
