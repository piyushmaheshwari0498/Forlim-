package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaleReturnRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("sm_bill_no")
    @Expose
    private String smBillNo;
    @SerializedName("sm_bill_date")
    @Expose
    private String smBillDate;
    @SerializedName("acc_name")
    @Expose
    private String accName;
    @SerializedName("srm_taxable_amt")
    @Expose
    private String srmTaxableAmt;
    @SerializedName("srm_sgst_amt")
    @Expose
    private String srmSgstAmt;
    @SerializedName("srm_cgst_amt")
    @Expose
    private String srmCgstAmt;
    @SerializedName("srm_igst_amt")
    @Expose
    private String srmIgstAmt;
    @SerializedName("srm_final_amt")
    @Expose
    private String srmFinalAmt;
    @SerializedName("srm_id")
    @Expose
    private String srmId;
    @SerializedName("srm_entry_no")
    @Expose
    private String srmEntryNo;
    @SerializedName("srm_entry_date")
    @Expose
    private String srmEntryDate;
    @SerializedName("trans")
    @Expose
    private List<SaleReturnItemRequest> trans = null;

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

    public String getSrmTaxableAmt() {
        return srmTaxableAmt;
    }

    public void setSrmTaxableAmt(String srmTaxableAmt) {
        this.srmTaxableAmt = srmTaxableAmt;
    }

    public String getSrmSgstAmt() {
        return srmSgstAmt;
    }

    public void setSrmSgstAmt(String srmSgstAmt) {
        this.srmSgstAmt = srmSgstAmt;
    }

    public String getSrmCgstAmt() {
        return srmCgstAmt;
    }

    public void setSrmCgstAmt(String srmCgstAmt) {
        this.srmCgstAmt = srmCgstAmt;
    }

    public String getSrmIgstAmt() {
        return srmIgstAmt;
    }

    public void setSrmIgstAmt(String srmIgstAmt) {
        this.srmIgstAmt = srmIgstAmt;
    }

    public String getSrmFinalAmt() {
        return srmFinalAmt;
    }

    public void setSrmFinalAmt(String srmFinalAmt) {
        this.srmFinalAmt = srmFinalAmt;
    }

    public String getSrmId() {
        return srmId;
    }

    public void setSrmId(String srmId) {
        this.srmId = srmId;
    }

    public String getSrmEntryNo() {
        return srmEntryNo;
    }

    public void setSrmEntryNo(String srmEntryNo) {
        this.srmEntryNo = srmEntryNo;
    }

    public String getSrmEntryDate() {
        return srmEntryDate;
    }

    public void setSrmEntryDate(String srmEntryDate) {
        this.srmEntryDate = srmEntryDate;
    }

    public List<SaleReturnItemRequest> getTrans() {
        return trans;
    }

    public void setTrans(List<SaleReturnItemRequest> trans) {
        this.trans = trans;
    }
    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

}
