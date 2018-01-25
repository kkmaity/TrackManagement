package com.demo.api;

import com.demo.model.registration.RegistrationMain;
import com.demo.model.registration.ApiRegistrationParam;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRegistration {

    private OnApiResponseListener listener;
    private ApiRegistrationParam param;

    public ApiRegistration(ApiRegistrationParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
       /* Call<RegistrationMain> getDepartment = RestService.getInstance().restInterface.userRegister(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<RegistrationMain>() {

            @Override
            public void onResponse(Call<RegistrationMain> call, Response<RegistrationMain> response) {
                if(response.code() == 200 && response !=null){
                        listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<RegistrationMain> call, Throwable t) {
                    listener.onError();

            }
        });*/
    }
}

