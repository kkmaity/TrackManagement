package com.demo.api;

import com.demo.model.start_attendence.ApiAttendenceStartParam;
import com.demo.model.start_attendence.AttendenceStartMain;
import com.demo.model.stop_attendence.ApiAttendenceStopParam;
import com.demo.model.stop_attendence.AttendenceStopMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiAttendenceStop {

    private OnApiResponseListener listener;
    private ApiAttendenceStopParam param;

    public ApiAttendenceStop(ApiAttendenceStopParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<AttendenceStopMain> getDepartment = RestService.getInstance().restInterface.attendance_stop(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<AttendenceStopMain>() {

            @Override
            public void onResponse(Call<AttendenceStopMain> call, Response<AttendenceStopMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<AttendenceStopMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

