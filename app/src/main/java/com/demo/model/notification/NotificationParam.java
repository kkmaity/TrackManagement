package com.demo.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class NotificationParam {

    @SerializedName("ApiKey")
    @Expose
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
