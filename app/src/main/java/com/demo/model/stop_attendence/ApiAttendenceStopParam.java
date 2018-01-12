package com.demo.model.stop_attendence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 12/1/18.
 */

public class ApiAttendenceStopParam {

    @SerializedName("ApiKey")
    @Expose
    private String apiKey;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("endLat")
    @Expose
    private String endLat;
    @SerializedName("endLong")
    @Expose
    private String endLong;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }
}
