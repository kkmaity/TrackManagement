package com.demo.api;

import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.model.start_attendence.ApiAttendenceStartParam;
import com.demo.model.start_attendence.AttendenceStartMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiAttendenceStart {

    private OnApiResponseListener listener;
    private ApiAttendenceStartParam param;

    public ApiAttendenceStart(ApiAttendenceStartParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<AttendenceStartMain> getDepartment = RestService.getInstance().restInterface.attendance_start(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<AttendenceStartMain>() {

            @Override
            public void onResponse(Call<AttendenceStartMain> call, Response<AttendenceStartMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<AttendenceStartMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

