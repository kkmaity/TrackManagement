package com.demo.api;

import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.attendanceStatus.ApiAttendanceStatusParam;
import com.demo.model.attendanceStatus.AttendanceStatusMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiAttendanceStatus {

    private OnApiResponseListener listener;
    private ApiAttendanceStatusParam param;

    public ApiAttendanceStatus(ApiAttendanceStatusParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<AttendanceStatusMain> getDepartment = RestService.getInstance().restInterface.attendanceStatus(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<AttendanceStatusMain>() {

            @Override
            public void onResponse(Call<AttendanceStatusMain> call, Response<AttendanceStatusMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<AttendanceStatusMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

