package com.fashion.forlempopoli.Entity.response;

import com.fashion.forlempopoli.Entity.request.SpinnerBankBillingData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpinnerBankBillingResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("StatusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private SpinnerBankBillingData data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public SpinnerBankBillingData getData() {
        return data;
    }

    public void setData(SpinnerBankBillingData data) {
        this.data = data;
    }

}
