package com.demo.model.stop_attendence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 12/1/18.
 */

public class ResponseData {

    @SerializedName("stopTime")
    @Expose
    private String stopTime;

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }
}
