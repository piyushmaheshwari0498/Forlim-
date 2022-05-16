package com.fashion.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetailsRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("sm_id")
    @Expose
    private String smId;
    @SerializedName("sm_bill_no")
    @Expose
    private String smBillNo;
    @SerializedName("sm_bill_date")
    @Expose
    private String smBillDate;
    @SerializedName("acc_name")
    @Expose
    private String accName;
    @SerializedName("sm_taxable_amt")
    @Expose
    private String smTaxableAmt;
    @SerializedName("sm_sgst_amt")
    @Expose
    private String smSgstAmt;
    @SerializedName("sm_cgst_amt")
    @Expose
    private String smCgstAmt;
    @SerializedName("sm_igst_amt")
    @Expose
    private String smIgstAmt;
    @SerializedName("sm_final_amt")
    @Expose
    private String smFinalAmt;
    @SerializedName("mobile_final_amt")
    @Expose
    private String mobileFinalAmt;


    public String getSmBillNo() {
        return smBillNo;
    }

    public void setSmBillNo(String smBillNo) {
        this.smBillNo = smBillNo;
    }

    public String getSmBillDate() {
        return smBillDate;
    }

    public void setSmBillDate(String smBillDate) {
        this.smBillDate = smBillDate;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getSmTaxableAmt() {
        return smTaxableAmt;
    }

    public void setSmTaxableAmt(String smTaxableAmt) {
        this.smTaxableAmt = smTaxableAmt;
    }

    public String getSmSgstAmt() {
        return smSgstAmt;
    }

    public void setSmSgstAmt(String smSgstAmt) {
        this.smSgstAmt = smSgstAmt;
    }

    public String getSmCgstAmt() {
        return smCgstAmt;
    }

    public void setSmCgstAmt(String smCgstAmt) {
        this.smCgstAmt = smCgstAmt;
    }

    public String getSmIgstAmt() {
        return smIgstAmt;
    }

    public void setSmIgstAmt(String smIgstAmt) {
        this.smIgstAmt = smIgstAmt;
    }

    public String getSmFinalAmt() {
        return smFinalAmt;
    }

    public void setSmFinalAmt(String smFinalAmt) {
        this.smFinalAmt = smFinalAmt;
    }

    public String getMobileFinalAmt() {
        return mobileFinalAmt;
    }

    public void setMobileFinalAmt(String mobileFinalAmt) {
        this.mobileFinalAmt = mobileFinalAmt;
    }

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
}
