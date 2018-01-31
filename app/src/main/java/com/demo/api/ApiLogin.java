package com.demo.api;

import com.demo.model.AppConfigMain;
import com.demo.model.login.ApiLoginParam;
import com.demo.model.login.LoginMain;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiLogin {

    private OnApiResponseListener listener;
    private ApiLoginParam param;

    public ApiLogin(ApiLoginParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<LoginMain> getDepartment = RestService.getInstance().restInterface.userLogin(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<LoginMain>() {

            @Override
            public void onResponse(Call<LoginMain> call, Response<LoginMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<LoginMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

