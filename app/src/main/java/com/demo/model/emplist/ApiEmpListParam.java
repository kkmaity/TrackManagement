package com.demo.model.emplist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamal on 01/12/2018.
 */

public class ApiEmpListParam {
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
