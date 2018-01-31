package com.demo.model;

/**
 * Created by root on 21/1/18.
 */

public class OutDoorHistory {

    private String jobid;
    private String category;
    private String challan_no;
    private String box_no;
    private String  description;
    private String  startTime;
    private String  endTime;
    private String job_status;

    public OutDoorHistory(String jobid, String category, String challan_no, String box_no, String description, String startTime, String endTime, String job_status) {
        this.jobid = jobid;
        this.category = category;
        this.challan_no = challan_no;
        this.box_no = box_no;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.job_status = job_status;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChallan_no() {
        return challan_no;
    }

    public void setChallan_no(String challan_no) {
        this.challan_no = challan_no;
    }

    public String getBox_no() {
        return box_no;
    }

    public void setBox_no(String box_no) {
        this.box_no = box_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }
}
