package com.demo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AppConfigMain {
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ResponseData")
    @Expose
    private ResponseAppConfigData responseData;

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

    public ResponseAppConfigData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseAppConfigData responseData) {
        this.responseData = responseData;
    }
}
