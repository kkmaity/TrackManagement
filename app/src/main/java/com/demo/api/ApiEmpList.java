package com.demo.api;

import com.demo.model.attendanceStatus.ApiAttendanceStatusParam;
import com.demo.model.attendanceStatus.AttendanceStatusMain;
import com.demo.model.emplist.ApiEmpListParam;
import com.demo.model.emplist.EmpListMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiEmpList {

    private OnApiResponseListener listener;
    private ApiEmpListParam param;

    public ApiEmpList(ApiEmpListParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<EmpListMain> getDepartment = RestService.getInstance().restInterface.getemployeeList(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<EmpListMain>() {

            @Override
            public void onResponse(Call<EmpListMain> call, Response<EmpListMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<EmpListMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

