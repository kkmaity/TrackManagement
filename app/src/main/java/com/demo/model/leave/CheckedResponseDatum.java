package com.demo.model.leave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamal on 01/16/2018.
 */

public class CheckedResponseDatum {

    private  ResponseDatum responseDatum;
    private boolean checked;


    public CheckedResponseDatum(ResponseDatum responseDatum, boolean checked) {
        this.responseDatum = responseDatum;
        this.checked = checked;
    }

    public ResponseDatum getResponseDatum() {
        return responseDatum;
    }

    public void setResponseDatum(ResponseDatum responseDatum) {
        this.responseDatum = responseDatum;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
