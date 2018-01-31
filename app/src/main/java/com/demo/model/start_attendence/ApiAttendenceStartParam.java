package com.demo.model.start_attendence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 12/1/18.
 */

public class ApiAttendenceStartParam {

    @SerializedName("ApiKey")
    @Expose
    private String apiKey;

    @SerializedName("userid")
    @Expose
    private String userid;

    @SerializedName("startLat")
    @Expose
    private String startLat;
    @SerializedName("startLong")
    @Expose
    private String startLong;


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

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }
}
