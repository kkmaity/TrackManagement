package com.demo.model.attendence_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class AttendenceHistoryMain {

    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ResponseData")
    @Expose
    private List<ResponseAttendenceHisDatum> responseData = null;

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

    public List<ResponseAttendenceHisDatum> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseAttendenceHisDatum> responseData) {
        this.responseData = responseData;
    }
}
