package com.demo.api;

import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.leave.AppliedLeaveList;
import com.demo.model.leave.LeaveParam;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiLeaveHistory {

    private OnApiResponseListener listener;
    private LeaveParam param;

    public ApiLeaveHistory(LeaveParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<AppliedLeaveList> getDepartment = RestService.getInstance().restInterface.leave_application_list(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<AppliedLeaveList>() {

            @Override
            public void onResponse(Call<AppliedLeaveList> call, Response<AppliedLeaveList> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<AppliedLeaveList> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

