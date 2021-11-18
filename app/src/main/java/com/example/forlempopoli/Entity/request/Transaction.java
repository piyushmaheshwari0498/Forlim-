package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("st_id")
    @Expose
    private String stId;
    @SerializedName("st_sm_id")
    @Expose
    private String stSmId;
    @SerializedName("st_customer_id")
    @Expose
    private String stCustomerId;
    @SerializedName("st_art_id")
    @Expose
    private String stArtId;
    @SerializedName("st_blm_id")
    @Expose
    private String stBlmId;
    @SerializedName("st_fabric_id")
    @Expose
    private String stFabricId;
    @SerializedName("st_cat_id")
    @Expose
    private String stCatId;
    @SerializedName("st_scat_id")
    @Expose
    private String stScatId;
    @SerializedName("st_color_id")
    @Expose
    private String stColorId;
    @SerializedName("st_shade_no")
    @Expose
    private String stShadeNo;
    @SerializedName("st_width")
    @Expose
    private String stWidth;
    @SerializedName("st_hsn_code")
    @Expose
    private String stHsnCode;
    @SerializedName("st_desc")
    @Expose
    private String stDesc;
    @SerializedName("st_qty_pcs")
    @Expose
    private String stQtyPcs;
    @SerializedName("st_qty_mtrs")
    @Expose
    private String stQtyMtrs;
    @SerializedName("st_total_qty_mtrs")
    @Expose
    private String stTotalQtyMtrs;
    @SerializedName("st_cost_price")
    @Expose
    private String stCostPrice;
    @SerializedName("st_sub_amt")
    @Expose
    private String stSubAmt;
    @SerializedName("st_disc_per")
    @Expose
    private String stDiscPer;
    @SerializedName("st_disc_amt")
    @Expose
    private String stDiscAmt;
    @SerializedName("st_taxable_amt")
    @Expose
    private String stTaxableAmt;
    @SerializedName("st_sgst_per")
    @Expose
    private String stSgstPer;
    @SerializedName("st_cgst_per")
    @Expose
    private String stCgstPer;
    @SerializedName("st_igst_per")
    @Expose
    private String stIgstPer;
    @SerializedName("st_sgst_amt")
    @Expose
    private String stSgstAmt;
    @SerializedName("st_cgst_amt")
    @Expose
    private String stCgstAmt;
    @SerializedName("st_igst_amt")
    @Expose
    private String stIgstAmt;
    @SerializedName("st_total_amt")
    @Expose
    private String stTotalAmt;
    @SerializedName("st_srt_qty_pcs")
    @Expose
    private String stSrtQtyPcs;
    @SerializedName("st_srt_qty_mtrs")
    @Expose
    private String stSrtQtyMtrs;
    @SerializedName("st_srt_total_qty_mtrs")
    @Expose
    private String stSrtTotalQtyMtrs;
    @SerializedName("st_selling_price_per")
    @Expose
    private String stSellingPricePer;
    @SerializedName("st_selling_price_amt")
    @Expose
    private String stSellingPriceAmt;
    @SerializedName("st_set_off")
    @Expose
    private String stSetOff;
    @SerializedName("st_sale_confirm")
    @Expose
    private String stSaleConfirm;
    @SerializedName("st_order_status")
    @Expose
    private String stOrderStatus;
    @SerializedName("fabric_name")
    @Expose
    private String fabricName;

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStSmId() {
        return stSmId;
    }

    public void setStSmId(String stSmId) {
        this.stSmId = stSmId;
    }

    public String getStCustomerId() {
        return stCustomerId;
    }

    public void setStCustomerId(String stCustomerId) {
        this.stCustomerId = stCustomerId;
    }

    public String getStArtId() {
        return stArtId;
    }

    public void setStArtId(String stArtId) {
        this.stArtId = stArtId;
    }

    public String getStBlmId() {
        return stBlmId;
    }

    public void setStBlmId(String stBlmId) {
        this.stBlmId = stBlmId;
    }

    public String getStFabricId() {
        return stFabricId;
    }

    public void setStFabricId(String stFabricId) {
        this.stFabricId = stFabricId;
    }

    public String getStCatId() {
        return stCatId;
    }

    public void setStCatId(String stCatId) {
        this.stCatId = stCatId;
    }

    public String getStScatId() {
        return stScatId;
    }

    public void setStScatId(String stScatId) {
        this.stScatId = stScatId;
    }

    public String getStColorId() {
        return stColorId;
    }

    public void setStColorId(String stColorId) {
        this.stColorId = stColorId;
    }

    public String getStShadeNo() {
        return stShadeNo;
    }

    public void setStShadeNo(String stShadeNo) {
        this.stShadeNo = stShadeNo;
    }

    public String getStWidth() {
        return stWidth;
    }

    public void setStWidth(String stWidth) {
        this.stWidth = stWidth;
    }

    public String getStHsnCode() {
        return stHsnCode;
    }

    public void setStHsnCode(String stHsnCode) {
        this.stHsnCode = stHsnCode;
    }

    public String getStDesc() {
        return stDesc;
    }

    public void setStDesc(String stDesc) {
        this.stDesc = stDesc;
    }

    public String getStQtyPcs() {
        return stQtyPcs;
    }

    public void setStQtyPcs(String stQtyPcs) {
        this.stQtyPcs = stQtyPcs;
    }

    public String getStQtyMtrs() {
        return stQtyMtrs;
    }

    public void setStQtyMtrs(String stQtyMtrs) {
        this.stQtyMtrs = stQtyMtrs;
    }

    public String getStTotalQtyMtrs() {
        return stTotalQtyMtrs;
    }

    public void setStTotalQtyMtrs(String stTotalQtyMtrs) {
        this.stTotalQtyMtrs = stTotalQtyMtrs;
    }

    public String getStCostPrice() {
        return stCostPrice;
    }

    public void setStCostPrice(String stCostPrice) {
        this.stCostPrice = stCostPrice;
    }

    public String getStSubAmt() {
        return stSubAmt;
    }

    public void setStSubAmt(String stSubAmt) {
        this.stSubAmt = stSubAmt;
    }

    public String getStDiscPer() {
        return stDiscPer;
    }

    public void setStDiscPer(String stDiscPer) {
        this.stDiscPer = stDiscPer;
    }

    public String getStDiscAmt() {
        return stDiscAmt;
    }

    public void setStDiscAmt(String stDiscAmt) {
        this.stDiscAmt = stDiscAmt;
    }

    public String getStTaxableAmt() {
        return stTaxableAmt;
    }

    public void setStTaxableAmt(String stTaxableAmt) {
        this.stTaxableAmt = stTaxableAmt;
    }

    public String getStSgstPer() {
        return stSgstPer;
    }

    public void setStSgstPer(String stSgstPer) {
        this.stSgstPer = stSgstPer;
    }

    public String getStCgstPer() {
        return stCgstPer;
    }

    public void setStCgstPer(String stCgstPer) {
        this.stCgstPer = stCgstPer;
    }

    public String getStIgstPer() {
        return stIgstPer;
    }

    public void setStIgstPer(String stIgstPer) {
        this.stIgstPer = stIgstPer;
    }

    public String getStSgstAmt() {
        return stSgstAmt;
    }

    public void setStSgstAmt(String stSgstAmt) {
        this.stSgstAmt = stSgstAmt;
    }

    public String getStCgstAmt() {
        return stCgstAmt;
    }

    public void setStCgstAmt(String stCgstAmt) {
        this.stCgstAmt = stCgstAmt;
    }

    public String getStIgstAmt() {
        return stIgstAmt;
    }

    public void setStIgstAmt(String stIgstAmt) {
        this.stIgstAmt = stIgstAmt;
    }

    public String getStTotalAmt() {
        return stTotalAmt;
    }

    public void setStTotalAmt(String stTotalAmt) {
        this.stTotalAmt = stTotalAmt;
    }

    public String getStSrtQtyPcs() {
        return stSrtQtyPcs;
    }

    public void setStSrtQtyPcs(String stSrtQtyPcs) {
        this.stSrtQtyPcs = stSrtQtyPcs;
    }

    public String getStSrtQtyMtrs() {
        return stSrtQtyMtrs;
    }

    public void setStSrtQtyMtrs(String stSrtQtyMtrs) {
        this.stSrtQtyMtrs = stSrtQtyMtrs;
    }

    public String getStSrtTotalQtyMtrs() {
        return stSrtTotalQtyMtrs;
    }

    public void setStSrtTotalQtyMtrs(String stSrtTotalQtyMtrs) {
        this.stSrtTotalQtyMtrs = stSrtTotalQtyMtrs;
    }

    public String getStSellingPricePer() {
        return stSellingPricePer;
    }

    public void setStSellingPricePer(String stSellingPricePer) {
        this.stSellingPricePer = stSellingPricePer;
    }

    public String getStSellingPriceAmt() {
        return stSellingPriceAmt;
    }

    public void setStSellingPriceAmt(String stSellingPriceAmt) {
        this.stSellingPriceAmt = stSellingPriceAmt;
    }

    public String getStSetOff() {
        return stSetOff;
    }

    public void setStSetOff(String stSetOff) {
        this.stSetOff = stSetOff;
    }

    public String getStSaleConfirm() {
        return stSaleConfirm;
    }

    public void setStSaleConfirm(String stSaleConfirm) {
        this.stSaleConfirm = stSaleConfirm;
    }

    public String getStOrderStatus() {
        return stOrderStatus;
    }

    public void setStOrderStatus(String stOrderStatus) {
        this.stOrderStatus = stOrderStatus;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }
}
