package com.demo.restservice;


import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.attendanceStatus.ApiAttendanceStatusParam;
import com.demo.model.attendanceStatus.AttendanceStatusMain;
import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.model.emplist.ApiEmpListParam;
import com.demo.model.emplist.EmpListMain;
import com.demo.model.leave.AppliedLeaveList;
import com.demo.model.leave.LeaveParam;
import com.demo.model.login.ApiLoginParam;
import com.demo.model.login.LoginMain;
import com.demo.model.notification.NotificationMain;
import com.demo.model.notification.NotificationParam;
import com.demo.model.registration.RegistrationMain;
import com.demo.model.registration.ApiRegistrationParam;
import com.demo.model.start_attendence.ApiAttendenceStartParam;
import com.demo.model.start_attendence.AttendenceStartMain;
import com.demo.model.stop_attendence.ApiAttendenceStopParam;
import com.demo.model.stop_attendence.AttendenceStopMain;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    /*@POST("emp_track/api/userRegister.php")
    Call<RegistrationMain> userRegister(@Body ApiRegistrationParam params);
*/
    @POST("emp_track/api/userLogin.php")
    Call<LoginMain> userLogin(@Body ApiLoginParam params);

    @POST("emp_track/api/attendance_history.php")
    Call<AttendenceHistoryMain> attendance_history(@Body ApiAttendenceHistoryParam params);

    @POST("emp_track/api/attendanceStart.php")
    Call<AttendenceStartMain> attendance_start(@Body ApiAttendenceStartParam params);

    @POST("emp_track/api/attendanceStop.php")
    Call<AttendenceStopMain> attendance_stop(@Body ApiAttendenceStopParam params);

    @POST("emp_track/api/attendanceStatus.php")
    Call<AttendanceStatusMain> attendanceStatus(@Body ApiAttendanceStatusParam params);

    @POST("emp_track/api/getemployeeList.php")
    Call<EmpListMain> getemployeeList(@Body ApiEmpListParam params);


    @POST("emp_track/api/leave_application_list.php")
    Call<AppliedLeaveList> leave_application_list(@Body LeaveParam params);

    @POST("emp_track/api/notification.php")
    Call<NotificationMain> notification(@Body NotificationParam params);

    



    @Multipart
    @POST("emp_track/api/userRegister.php")
    Call<RegistrationMain> userRegister(
            @Part("ApiKey") RequestBody user_family_id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password,
            @Part("deviceToken") RequestBody deviceToken,
            @Part MultipartBody.Part files);





}
