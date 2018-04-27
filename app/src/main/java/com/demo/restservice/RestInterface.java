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
import com.demo.model.start_attendence.ApiAttendenceStartParam;
import com.demo.model.start_attendence.AttendenceStartMain;
import com.demo.model.stop_attendence.ApiAttendenceStopParam;
import com.demo.model.stop_attendence.AttendenceStopMain;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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


    @Multipart
    @POST("emp_track/api/outdoorworkStart.php")
    Call<ResponseBody> outdoorworkStart(
            @Part("ApiKey") RequestBody ApiKey,
            @Part("userid") RequestBody userid,
            @Part("job_category") RequestBody job_category,
            @Part("challan_no") RequestBody challan_no,
            @Part("challan_date") RequestBody challan_date,
            @Part("hospital_id") RequestBody hospital_id,
            @Part("doctor_id") RequestBody doctor_id,
            @Part("invoice_number") RequestBody invoice_number,
            @Part("invoice_date") RequestBody invoice_date,
            @Part("mode_of_transport") RequestBody mode_of_transport,
            @Part("office_bike_id") RequestBody office_bike_id,
            @Part("expense") RequestBody expense,
            @Part("startLat") RequestBody startLat,
            @Part("startLong") RequestBody startLong,
            @Part MultipartBody.Part picture1,
            @Part MultipartBody.Part picture2,
            @Part MultipartBody.Part picture3,
            @Part MultipartBody.Part picture4,
            @Part MultipartBody.Part picture5);

    @Multipart
    @POST("emp_track/api/update_outdoor_work.php")
    Call<ResponseBody> update_outdoor_work(
            @Part("ApiKey") RequestBody ApiKey,
            @Part("jobId") RequestBody userid,
            @Part("description") RequestBody job_category,
            @Part List<MultipartBody.Part> files);

}

    /*@Multipart
    @POST("api/doctors/update_outdoor_work.php")
    Call<ResponseBody> update_outdoor_work(
            @Part("type") RequestBody type,
            @Part("reference_number") RequestBody reference_number,
            @Part("user_family_id") RequestBody user_family_id,
            @Part("upload_by") RequestBody upload_by,
            @Part("purpose") RequestBody purpose,
            @Part("is_virtual") RequestBody is_virtual,
            @Part("can_delete") RequestBody can_delete,
            @Part("chief_complaints") RequestBody chief_complaints,
            @Part("examination_and_findings") RequestBody examination_and_findings,
            @Part("medicine_list") RequestBody medicine_list,
            @Part("has_signature") RequestBody has_signature,
            @Part List<MultipartBody.Part> files);

*/