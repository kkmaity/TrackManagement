package com.demo.api;

import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.model.registration.ApiRegistrationParam;
import com.demo.model.registration.RegistrationMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiAttendenceHistory {

    private OnApiResponseListener listener;
    private ApiAttendenceHistoryParam param;

    public ApiAttendenceHistory(ApiAttendenceHistoryParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<AttendenceHistoryMain> getDepartment = RestService.getInstance().restInterface.attendance_history(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<AttendenceHistoryMain>() {

            @Override
            public void onResponse(Call<AttendenceHistoryMain> call, Response<AttendenceHistoryMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<AttendenceHistoryMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

