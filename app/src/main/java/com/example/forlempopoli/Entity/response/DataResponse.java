package com.example.forlempopoli.Entity.response;

import com.example.forlempopoli.Entity.request.DataRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("StatusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataRequest data;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataRequest getData() {
        return data;
    }

    public void setData(DataRequest data) {
        this.data = data;
    }
}
