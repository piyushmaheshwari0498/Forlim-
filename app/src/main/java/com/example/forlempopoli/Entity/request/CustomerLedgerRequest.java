package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerLedgerRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("acc_id")
    @Expose
    private Integer accId;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;

    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public Integer getAccId() {
        return accId;
    }

    public void setAccId(Integer accId) {
        this.accId = accId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
