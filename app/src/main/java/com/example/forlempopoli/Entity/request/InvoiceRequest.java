package com.example.forlempopoli.Entity.request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InvoiceRequest {
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
    @SerializedName("sm_total_qty_pcs")
    @Expose
    private String smTotalQtyPcs;
    @SerializedName("sm_total_qty_mtrs")
    @Expose
    private String smTotalQtyMtrs;
    @SerializedName("sm_sub_amt")
    @Expose
    private String smSubAmt;
    @SerializedName("sm_disc_amt")
    @Expose
    private String smDiscAmt;
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
    @SerializedName("sm_other_amt")
    @Expose
    private String smOtherAmt;
    @SerializedName("sm_round_off_amt")
    @Expose
    private String smRoundOffAmt;
    @SerializedName("sm_final_amt")
    @Expose
    private String smFinalAmt;
    @SerializedName("order_status_name")
    @Expose
    private String orderStatusName;
    @SerializedName("sm_id")
    @Expose
    private String smId;
    @SerializedName("trans")
    @Expose
    private List<OrderItemRequest> trans = null;

    public InvoiceRequest(String accId, String smSubAmt,String smId) {
        this.accId = accId;
        this.smSubAmt = smSubAmt;
        this.smId = smId;
    }

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

    public String getSmTotalQtyPcs() {
        return smTotalQtyPcs;
    }

    public void setSmTotalQtyPcs(String smTotalQtyPcs) {
        this.smTotalQtyPcs = smTotalQtyPcs;
    }

    public String getSmTotalQtyMtrs() {
        return smTotalQtyMtrs;
    }

    public void setSmTotalQtyMtrs(String smTotalQtyMtrs) {
        this.smTotalQtyMtrs = smTotalQtyMtrs;
    }

    public String getSmSubAmt() {
        return smSubAmt;
    }

    public void setSmSubAmt(String smSubAmt) {
        this.smSubAmt = smSubAmt;
    }

    public String getSmDiscAmt() {
        return smDiscAmt;
    }

    public void setSmDiscAmt(String smDiscAmt) {
        this.smDiscAmt = smDiscAmt;
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

    public String getSmOtherAmt() {
        return smOtherAmt;
    }

    public void setSmOtherAmt(String smOtherAmt) {
        this.smOtherAmt = smOtherAmt;
    }

    public String getSmRoundOffAmt() {
        return smRoundOffAmt;
    }

    public void setSmRoundOffAmt(String smRoundOffAmt) {
        this.smRoundOffAmt = smRoundOffAmt;
    }

    public String getSmFinalAmt() {
        return smFinalAmt;
    }

    public void setSmFinalAmt(String smFinalAmt) {
        this.smFinalAmt = smFinalAmt;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getSmId() {
        return smId;
    }

    public void setSmId(String smId) {
        this.smId = smId;
    }

    public List<OrderItemRequest> getTrans() {
        return trans;
    }

    public void setTrans(List<OrderItemRequest> trans) {
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
