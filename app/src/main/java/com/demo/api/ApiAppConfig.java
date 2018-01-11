package com.demo.api;

import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiAppConfig {

    private OnApiResponseListener listener;
    private AppConfigParam param;

    public ApiAppConfig(AppConfigParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<AppConfigMain> getDepartment = RestService.getInstance().restInterface.appconfig(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<AppConfigMain>() {

            @Override
            public void onResponse(Call<AppConfigMain> call, Response<AppConfigMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<AppConfigMain> call, Throwable t) {
                    listener.onError();

            }
        });
    }
}

