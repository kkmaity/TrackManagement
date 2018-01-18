package com.demo.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kamal on 01/18/2018.
 */

public class NotificationMain {
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ResponseData")
    @Expose
    private List<NotificationData> responseData = null;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationData> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<NotificationData> responseData) {
        this.responseData = responseData;
    }

}
