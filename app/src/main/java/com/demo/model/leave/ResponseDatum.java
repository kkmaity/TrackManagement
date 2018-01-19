package com.demo.model.leave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamal on 01/16/2018.
 */

public class ResponseDatum {
    @SerializedName("leaveID")
    @Expose
    private String leaveID;

    @SerializedName("leave_start_date")
    @Expose
    private String leaveStartDate;
    @SerializedName("leave_end_date")
    @Expose
    private String leaveEndDate;
    @SerializedName("leave_status")
    @Expose
    private String leaveStatus;
    @SerializedName("apply_date")
    @Expose
    private String applyDate;
    @SerializedName("leave_type")
    @Expose
    private String leaveType;

    public String getLeaveStartDate() {
        return leaveStartDate;
    }

    public void setLeaveStartDate(String leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public String getLeaveEndDate() {
        return leaveEndDate;
    }

    public void setLeaveEndDate(String leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public void setLeaveID(String leaveID) {
        this.leaveID = leaveID;
    }
}
