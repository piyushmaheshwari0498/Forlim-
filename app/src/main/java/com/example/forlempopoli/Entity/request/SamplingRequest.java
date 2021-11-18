package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SamplingRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("acc_id")
    @Expose
    private Integer accId;
    @SerializedName("ssm_id")
    @Expose
    private String ssmId;
    @SerializedName("ssm_entry_no")
    @Expose
    private String ssmEntryNo;
    @SerializedName("ssm_entry_date")
    @Expose
    private String ssmEntryDate;
    @SerializedName("ssm_total_qty_pcs")
    @Expose
    private String ssmTotalQtyPcs;
    @SerializedName("ssm_sub_amt")
    @Expose
    private String ssmSubAmt;
    @SerializedName("ssm_taxable_amt")
    @Expose
    private String ssmTaxableAmt;
    @SerializedName("ssm_sgst_amt")
    @Expose
    private String ssmSgstAmt;
    @SerializedName("ssm_cgst_amt")
    @Expose
    private String ssmCgstAmt;
    @SerializedName("ssm_igst_amt")
    @Expose
    private String ssmIgstAmt;
    @SerializedName("sampling_sales_balance")
    @Expose
    private String samplingSalesBalance;
    @SerializedName("ssm_final_amt")
    @Expose
    private String ssmFinalAmt;

    public String getSsmId() {
        return ssmId;
    }

    public void setSsmId(String ssmId) {
        this.ssmId = ssmId;
    }

    public String getSsmEntryNo() {
        return ssmEntryNo;
    }

    public void setSsmEntryNo(String ssmEntryNo) {
        this.ssmEntryNo = ssmEntryNo;
    }

    public String getSsmEntryDate() {
        return ssmEntryDate;
    }

    public void setSsmEntryDate(String ssmEntryDate) {
        this.ssmEntryDate = ssmEntryDate;
    }

    public String getSsmTotalQtyPcs() {
        return ssmTotalQtyPcs;
    }

    public void setSsmTotalQtyPcs(String ssmTotalQtyPcs) {
        this.ssmTotalQtyPcs = ssmTotalQtyPcs;
    }

    public String getSsmSubAmt() {
        return ssmSubAmt;
    }

    public void setSsmSubAmt(String ssmSubAmt) {
        this.ssmSubAmt = ssmSubAmt;
    }

    public String getSsmTaxableAmt() {
        return ssmTaxableAmt;
    }

    public void setSsmTaxableAmt(String ssmTaxableAmt) {
        this.ssmTaxableAmt = ssmTaxableAmt;
    }

    public String getSsmSgstAmt() {
        return ssmSgstAmt;
    }

    public void setSsmSgstAmt(String ssmSgstAmt) {
        this.ssmSgstAmt = ssmSgstAmt;
    }

    public String getSsmCgstAmt() {
        return ssmCgstAmt;
    }

    public void setSsmCgstAmt(String ssmCgstAmt) {
        this.ssmCgstAmt = ssmCgstAmt;
    }

    public String getSsmIgstAmt() {
        return ssmIgstAmt;
    }

    public void setSsmIgstAmt(String ssmIgstAmt) {
        this.ssmIgstAmt = ssmIgstAmt;
    }

    public String getSamplingSalesBalance() {
        return samplingSalesBalance;
    }

    public void setSamplingSalesBalance(String samplingSalesBalance) {
        this.samplingSalesBalance = samplingSalesBalance;
    }

    public String getSsmFinalAmt() {
        return ssmFinalAmt;
    }

    public void setSsmFinalAmt(String ssmFinalAmt) {
        this.ssmFinalAmt = ssmFinalAmt;
    }
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

}
