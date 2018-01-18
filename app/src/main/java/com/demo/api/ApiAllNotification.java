package com.demo.api;



import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.notification.NotificationData;
import com.demo.model.notification.NotificationMain;
import com.demo.model.notification.NotificationParam;
import com.demo.restservice.APIHelper;
import com.demo.restservice.OnApiResponseListener;
import com.demo.restservice.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiAllNotification {

    private OnApiResponseListener listener;
    private NotificationParam param;

    public ApiAllNotification(NotificationParam param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {
        Call<NotificationMain> getDepartment = RestService.getInstance().restInterface.notification(param);

        APIHelper.enqueueWithRetry(getDepartment,new Callback<NotificationMain>() {

            @Override
            public void onResponse(Call<NotificationMain> call, Response<NotificationMain> response) {
                if(response.code() == 200 && response !=null){
                    listener.onSuccess( response.body());
                }else{
                    listener.onError();
                }

            }

            @Override
            public void onFailure(Call<NotificationMain> call, Throwable t) {
                listener.onError();

            }
        });
    }
}