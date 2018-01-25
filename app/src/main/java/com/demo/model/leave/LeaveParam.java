package com.demo.model.leave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamal on 01/16/2018.
 */

public class LeaveParam {
    @SerializedName("ApiKey")
    @Expose
    private String apiKey;
    @SerializedName("userid")
    @Expose
    private String userid;

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

}
