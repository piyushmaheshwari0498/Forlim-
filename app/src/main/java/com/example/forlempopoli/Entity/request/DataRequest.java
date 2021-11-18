package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataRequest {
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("sm_id")
    @Expose
    private String smId;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("mobile_final_amt")
    @Expose
    private String mobileFinalAmt;
    @SerializedName("paydate")
    @Expose
    private String paydate;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("status_msg")
    @Expose
    private String statusMsg;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getSmId() {
        return smId;
    }

    public void setSmId(String smId) {
        this.smId = smId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMobileFinalAmt() {
        return mobileFinalAmt;
    }

    public void setMobileFinalAmt(String mobileFinalAmt) {
        this.mobileFinalAmt = mobileFinalAmt;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
