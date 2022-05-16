package com.fashion.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDataRequest {
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("acc_name")
    @Expose
    private String accName;
    @SerializedName("am_id")
    @Expose
    private String amId;
    @SerializedName("am_bill_no")
    @Expose
    private String amBillNo;
    @SerializedName("am_bill_date")
    @Expose
    private String amBillDate;
    @SerializedName("am_total_qty_pcs")
    @Expose
    private String amTotalQtyPcs;
    @SerializedName("am_total_qty_mtrs")
    @Expose
    private String amTotalQtyMtrs;
    @SerializedName("am_final_amt")
    @Expose
    private String amFinalAmt;
    @SerializedName("am_create_date")
    @Expose
    private String amCreateDate;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAmId() {
        return amId;
    }

    public void setAmId(String amId) {
        this.amId = amId;
    }

    public String getAmBillNo() {
        return amBillNo;
    }

    public void setAmBillNo(String amBillNo) {
        this.amBillNo = amBillNo;
    }

    public String getAmBillDate() {
        return amBillDate;
    }

    public void setAmBillDate(String amBillDate) {
        this.amBillDate = amBillDate;
    }

    public String getAmTotalQtyPcs() {
        return amTotalQtyPcs;
    }

    public void setAmTotalQtyPcs(String amTotalQtyPcs) {
        this.amTotalQtyPcs = amTotalQtyPcs;
    }

    public String getAmTotalQtyMtrs() {
        return amTotalQtyMtrs;
    }

    public void setAmTotalQtyMtrs(String amTotalQtyMtrs) {
        this.amTotalQtyMtrs = amTotalQtyMtrs;
    }

    public String getAmFinalAmt() {
        return amFinalAmt;
    }

    public void setAmFinalAmt(String amFinalAmt) {
        this.amFinalAmt = amFinalAmt;
    }

    public String getAmCreateDate() {
        return amCreateDate;
    }

    public void setAmCreateDate(String amCreateDate) {
        this.amCreateDate = amCreateDate;
    }

}
