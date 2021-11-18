package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendDataInvoiceRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("sm_id")
    @Expose
    private String smId;
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("sm_final_amt")
    @Expose
    private String smFinalAmt;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("paydate")
    @Expose
    private String paydate;

    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getSmId() {
        return smId;
    }

    public void setSmId(String smId) {
        this.smId = smId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getSmFinalAmt() {
        return smFinalAmt;
    }

    public void setSmFinalAmt(String smFinalAmt) {
        this.smFinalAmt = smFinalAmt;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }
}
