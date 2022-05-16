package com.fashion.forlempopoli.Entity.request;

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
    @SerializedName("voucher_amt")
    @Expose
    private String voucher_amt;

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

    public String getVoucher_amt() {
        return voucher_amt;
    }

    public void setVoucher_amt(String voucher_amt) {
        this.voucher_amt = voucher_amt;
    }

    @Override
    public String toString() {
        return "CreditDetailsRequest{" +
                "pendingBalance=" + pendingBalance +
                ", totalBalance='" + totalBalance + '\'' +
                ", accLimit='" + accLimit + '\'' +
                ", voucher_amt='" + voucher_amt + '\'' +
                '}';
    }
}
