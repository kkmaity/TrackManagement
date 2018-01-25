package com.demo.model.attendanceStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamal on 01/12/2018.
 */

public class ResponseData {
    @SerializedName("isUserWorkStarted")
    @Expose
    private String isUserWorkStarted;
    @SerializedName("startTime")
    @Expose
    private String startTime;

    public String getIsUserWorkStarted() {
        return isUserWorkStarted;
    }

    public void setIsUserWorkStarted(String isUserWorkStarted) {
        this.isUserWorkStarted = isUserWorkStarted;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
