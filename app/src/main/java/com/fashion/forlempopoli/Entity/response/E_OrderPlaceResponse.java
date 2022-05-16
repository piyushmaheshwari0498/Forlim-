package com.fashion.forlempopoli.Entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class E_OrderPlaceResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("StatusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("am_id")
    @Expose
    private String am_id;
    public Integer getStatusCode() {
        return statusCode;
    }

    public String getAm_id() {
        return am_id;
    }

    public void setAm_id(String am_id) {
        this.am_id = am_id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "E_OrderPlaceResponse{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", message='" + message + '\'' +
                ", am_id='" + am_id + '\'' +
                '}';
    }
}