package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditDetailsRequest {

    @SerializedName("pending_balance")
    @Expose
    private Integer pendingBalance;
    @SerializedName("total_balance")
    @Expose
    private String totalBalance;
    @SerializedName("acc_limit")
    @Expose
    private String accLimit;

    public Integer getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(Integer pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getAccLimit() {
        return accLimit;
    }

    public void setAccLimit(String accLimit) {
        this.accLimit = accLimit;
    }
}
