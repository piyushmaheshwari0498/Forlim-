package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsOrderRequest {
    @SerializedName("at_id")
    @Expose
    private String atId;
    @SerializedName("at_am_id")
    @Expose
    private String atAmId;
    @SerializedName("at_customer_id")
    @Expose
    private String atCustomerId;
    @SerializedName("at_art_id")
    @Expose
    private String atArtId;
    @SerializedName("at_blm_id")
    @Expose
    private String atBlmId;
    @SerializedName("at_fabric_id")
    @Expose
    private String atFabricId;
    @SerializedName("at_cat_id")
    @Expose
    private String atCatId;
    @SerializedName("at_scat_id")
    @Expose
    private String atScatId;
    @SerializedName("at_width")
    @Expose
    private String atWidth;
    @SerializedName("at_hsn_code")
    @Expose
    private String atHsnCode;
    @SerializedName("at_desc")
    @Expose
    private String atDesc;
    @SerializedName("at_qty_pcs")
    @Expose
    private String atQtyPcs;
    @SerializedName("at_qty_mtrs")
    @Expose
    private String atQtyMtrs;
    @SerializedName("at_total_qty_mtrs")
    @Expose
    private String atTotalQtyMtrs;
    @SerializedName("at_issue_pcs")
    @Expose
    private String atIssuePcs;
    @SerializedName("at_issue_qty_mtrs")
    @Expose
    private String atIssueQtyMtrs;
    @SerializedName("at_issue_total_qty_mtrs")
    @Expose
    private String atIssueTotalQtyMtrs;
    @SerializedName("at_cost_price")
    @Expose
    private String atCostPrice;
    @SerializedName("at_sub_amt")
    @Expose
    private String atSubAmt;
    @SerializedName("at_disc_per")
    @Expose
    private String atDiscPer;
    @SerializedName("at_disc_amt")
    @Expose
    private String atDiscAmt;
    @SerializedName("at_taxable_amt")
    @Expose
    private String atTaxableAmt;
    @SerializedName("at_sgst_per")
    @Expose
    private String atSgstPer;
    @SerializedName("at_cgst_per")
    @Expose
    private String atCgstPer;
    @SerializedName("at_igst_per")
    @Expose
    private String atIgstPer;
    @SerializedName("at_sgst_amt")
    @Expose
    private String atSgstAmt;
    @SerializedName("at_cgst_amt")
    @Expose
    private String atCgstAmt;
    @SerializedName("at_igst_amt")
    @Expose
    private String atIgstAmt;
    @SerializedName("at_total_amt")
    @Expose
    private String atTotalAmt;
    @SerializedName("at_srt_qty_pcs")
    @Expose
    private String atSrtQtyPcs;
    @SerializedName("at_srt_qty_mtrs")
    @Expose
    private String atSrtQtyMtrs;
    @SerializedName("at_srt_total_qty_mtrs")
    @Expose
    private String atSrtTotalQtyMtrs;
    @SerializedName("at_selling_price_per")
    @Expose
    private String atSellingPricePer;
    @SerializedName("at_selling_price_amt")
    @Expose
    private String atSellingPriceAmt;
    @SerializedName("at_set_off")
    @Expose
    private String atSetOff;
    @SerializedName("at_sale_confirm")
    @Expose
    private String atSaleConfirm;
    @SerializedName("at_order_status")
    @Expose
    private String atOrderStatus;
    @SerializedName("art_no")
    @Expose
    private String artNo;
    @SerializedName("art_name")
    @Expose
    private String artName;
    @SerializedName("art_photo")
    @Expose
    private String artPhoto;
    @SerializedName("garment_photo")
    @Expose
    private String garmentPhoto;
    @SerializedName("art_composition")
    @Expose
    private String artComposition;
    @SerializedName("art_keyword")
    @Expose
    private String artKeyword;
    @SerializedName("fabric_name")
    @Expose
    private String fabricName;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("scat_name")
    @Expose
    private String scatName;
    @SerializedName("blm_item_code")
    @Expose
    private String blmItemCode;

    public String getAtId() {
        return atId;
    }

    public void setAtId(String atId) {
        this.atId = atId;
    }

    public String getAtAmId() {
        return atAmId;
    }

    public void setAtAmId(String atAmId) {
        this.atAmId = atAmId;
    }

    public String getAtCustomerId() {
        return atCustomerId;
    }

    public void setAtCustomerId(String atCustomerId) {
        this.atCustomerId = atCustomerId;
    }

    public String getAtArtId() {
        return atArtId;
    }

    public void setAtArtId(String atArtId) {
        this.atArtId = atArtId;
    }

    public String getAtBlmId() {
        return atBlmId;
    }

    public void setAtBlmId(String atBlmId) {
        this.atBlmId = atBlmId;
    }

    public String getAtFabricId() {
        return atFabricId;
    }

    public void setAtFabricId(String atFabricId) {
        this.atFabricId = atFabricId;
    }

    public String getAtCatId() {
        return atCatId;
    }

    public void setAtCatId(String atCatId) {
        this.atCatId = atCatId;
    }

    public String getAtScatId() {
        return atScatId;
    }

    public void setAtScatId(String atScatId) {
        this.atScatId = atScatId;
    }

    public String getAtWidth() {
        return atWidth;
    }

    public void setAtWidth(String atWidth) {
        this.atWidth = atWidth;
    }

    public String getAtHsnCode() {
        return atHsnCode;
    }

    public void setAtHsnCode(String atHsnCode) {
        this.atHsnCode = atHsnCode;
    }

    public String getAtDesc() {
        return atDesc;
    }

    public void setAtDesc(String atDesc) {
        this.atDesc = atDesc;
    }

    public String getAtQtyPcs() {
        return atQtyPcs;
    }

    public void setAtQtyPcs(String atQtyPcs) {
        this.atQtyPcs = atQtyPcs;
    }

    public String getAtQtyMtrs() {
        return atQtyMtrs;
    }

    public void setAtQtyMtrs(String atQtyMtrs) {
        this.atQtyMtrs = atQtyMtrs;
    }

    public String getAtTotalQtyMtrs() {
        return atTotalQtyMtrs;
    }

    public void setAtTotalQtyMtrs(String atTotalQtyMtrs) {
        this.atTotalQtyMtrs = atTotalQtyMtrs;
    }

    public String getAtIssuePcs() {
        return atIssuePcs;
    }

    public void setAtIssuePcs(String atIssuePcs) {
        this.atIssuePcs = atIssuePcs;
    }

    public String getAtIssueQtyMtrs() {
        return atIssueQtyMtrs;
    }

    public void setAtIssueQtyMtrs(String atIssueQtyMtrs) {
        this.atIssueQtyMtrs = atIssueQtyMtrs;
    }

    public String getAtIssueTotalQtyMtrs() {
        return atIssueTotalQtyMtrs;
    }

    public void setAtIssueTotalQtyMtrs(String atIssueTotalQtyMtrs) {
        this.atIssueTotalQtyMtrs = atIssueTotalQtyMtrs;
    }

    public String getAtCostPrice() {
        return atCostPrice;
    }

    public void setAtCostPrice(String atCostPrice) {
        this.atCostPrice = atCostPrice;
    }

    public String getAtSubAmt() {
        return atSubAmt;
    }

    public void setAtSubAmt(String atSubAmt) {
        this.atSubAmt = atSubAmt;
    }

    public String getAtDiscPer() {
        return atDiscPer;
    }

    public void setAtDiscPer(String atDiscPer) {
        this.atDiscPer = atDiscPer;
    }

    public String getAtDiscAmt() {
        return atDiscAmt;
    }

    public void setAtDiscAmt(String atDiscAmt) {
        this.atDiscAmt = atDiscAmt;
    }

    public String getAtTaxableAmt() {
        return atTaxableAmt;
    }

    public void setAtTaxableAmt(String atTaxableAmt) {
        this.atTaxableAmt = atTaxableAmt;
    }

    public String getAtSgstPer() {
        return atSgstPer;
    }

    public void setAtSgstPer(String atSgstPer) {
        this.atSgstPer = atSgstPer;
    }

    public String getAtCgstPer() {
        return atCgstPer;
    }

    public void setAtCgstPer(String atCgstPer) {
        this.atCgstPer = atCgstPer;
    }

    public String getAtIgstPer() {
        return atIgstPer;
    }

    public void setAtIgstPer(String atIgstPer) {
        this.atIgstPer = atIgstPer;
    }

    public String getAtSgstAmt() {
        return atSgstAmt;
    }

    public void setAtSgstAmt(String atSgstAmt) {
        this.atSgstAmt = atSgstAmt;
    }

    public String getAtCgstAmt() {
        return atCgstAmt;
    }

    public void setAtCgstAmt(String atCgstAmt) {
        this.atCgstAmt = atCgstAmt;
    }

    public String getAtIgstAmt() {
        return atIgstAmt;
    }

    public void setAtIgstAmt(String atIgstAmt) {
        this.atIgstAmt = atIgstAmt;
    }

    public String getAtTotalAmt() {
        return atTotalAmt;
    }

    public void setAtTotalAmt(String atTotalAmt) {
        this.atTotalAmt = atTotalAmt;
    }

    public String getAtSrtQtyPcs() {
        return atSrtQtyPcs;
    }

    public void setAtSrtQtyPcs(String atSrtQtyPcs) {
        this.atSrtQtyPcs = atSrtQtyPcs;
    }

    public String getAtSrtQtyMtrs() {
        return atSrtQtyMtrs;
    }

    public void setAtSrtQtyMtrs(String atSrtQtyMtrs) {
        this.atSrtQtyMtrs = atSrtQtyMtrs;
    }

    public String getAtSrtTotalQtyMtrs() {
        return atSrtTotalQtyMtrs;
    }

    public void setAtSrtTotalQtyMtrs(String atSrtTotalQtyMtrs) {
        this.atSrtTotalQtyMtrs = atSrtTotalQtyMtrs;
    }

    public String getAtSellingPricePer() {
        return atSellingPricePer;
    }

    public void setAtSellingPricePer(String atSellingPricePer) {
        this.atSellingPricePer = atSellingPricePer;
    }

    public String getAtSellingPriceAmt() {
        return atSellingPriceAmt;
    }

    public void setAtSellingPriceAmt(String atSellingPriceAmt) {
        this.atSellingPriceAmt = atSellingPriceAmt;
    }

    public String getAtSetOff() {
        return atSetOff;
    }

    public void setAtSetOff(String atSetOff) {
        this.atSetOff = atSetOff;
    }

    public String getAtSaleConfirm() {
        return atSaleConfirm;
    }

    public void setAtSaleConfirm(String atSaleConfirm) {
        this.atSaleConfirm = atSaleConfirm;
    }

    public String getAtOrderStatus() {
        return atOrderStatus;
    }

    public void setAtOrderStatus(String atOrderStatus) {
        this.atOrderStatus = atOrderStatus;
    }

    public String getArtNo() {
        return artNo;
    }

    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtPhoto() {
        return artPhoto;
    }

    public void setArtPhoto(String artPhoto) {
        this.artPhoto = artPhoto;
    }

    public String getGarmentPhoto() {
        return garmentPhoto;
    }

    public void setGarmentPhoto(String garmentPhoto) {
        this.garmentPhoto = garmentPhoto;
    }

    public String getArtComposition() {
        return artComposition;
    }

    public void setArtComposition(String artComposition) {
        this.artComposition = artComposition;
    }

    public String getArtKeyword() {
        return artKeyword;
    }

    public void setArtKeyword(String artKeyword) {
        this.artKeyword = artKeyword;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getScatName() {
        return scatName;
    }

    public void setScatName(String scatName) {
        this.scatName = scatName;
    }

    public String getBlmItemCode() {
        return blmItemCode;
    }

    public void setBlmItemCode(String blmItemCode) {
        this.blmItemCode = blmItemCode;
    }

}
