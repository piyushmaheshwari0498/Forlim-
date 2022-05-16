package com.fashion.forlempopoli.Entity.response;

import com.fashion.forlempopoli.Entity.request.PaymentDetailsRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentDetailsResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("StatusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private List<PaymentDetailsRequest> data = null;

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

    public List<PaymentDetailsRequest> getData() {
        return data;
    }

    public void setData(List<PaymentDetailsRequest> data) {
        this.data = data;
    }
}
