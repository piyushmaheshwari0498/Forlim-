package com.example.forlempopoli.Entity.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datanum {

    @SerializedName("art_id")
    @Expose
    private String artId;
    @SerializedName("art_cat_id")
    @Expose
    private String artCatId;
    @SerializedName("art_scat_id")
    @Expose
    private String artScatId;
    @SerializedName("art_no")
    @Expose
    private String artNo;
    @SerializedName("art_name")
    @Expose
    private String artName;
    @SerializedName("art_shade_no")
    @Expose
    private String artShadeNo;
    @SerializedName("art_width")
    @Expose
    private String artWidth;
    @SerializedName("art_selling_price_amt")
    @Expose
    private String artSellingPriceAmt;
    @SerializedName("art_offer_price")
    @Expose
    private String artOfferPrice;
    @SerializedName("art_stock_type")
    @Expose
    private String artStockType;
    @SerializedName("art_status")
    @Expose
    private String artStatus;
    @SerializedName("art_composition")
    @Expose
    private String artComposition;
    @SerializedName("art_photo")
    @Expose
    private String artPhoto;
    @SerializedName("garment_photo")
    @Expose
    private String garmentPhoto;

    public String getArtId() {
        return artId;
    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public String getArtCatId() {
        return artCatId;
    }

    public void setArtCatId(String artCatId) {
        this.artCatId = artCatId;
    }

    public String getArtScatId() {
        return artScatId;
    }

    public void setArtScatId(String artScatId) {
        this.artScatId = artScatId;
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

    public String getArtShadeNo() {
        return artShadeNo;
    }

    public void setArtShadeNo(String artShadeNo) {
        this.artShadeNo = artShadeNo;
    }

    public String getArtWidth() {
        return artWidth;
    }

    public void setArtWidth(String artWidth) {
        this.artWidth = artWidth;
    }

    public String getArtSellingPriceAmt() {
        return artSellingPriceAmt;
    }

    public void setArtSellingPriceAmt(String artSellingPriceAmt) {
        this.artSellingPriceAmt = artSellingPriceAmt;
    }

    public String getArtOfferPrice() {
        return artOfferPrice;
    }

    public void setArtOfferPrice(String artOfferPrice) {
        this.artOfferPrice = artOfferPrice;
    }

    public String getArtStockType() {
        return artStockType;
    }

    public void setArtStockType(String artStockType) {
        this.artStockType = artStockType;
    }

    public String getArtStatus() {
        return artStatus;
    }

    public void setArtStatus(String artStatus) {
        this.artStatus = artStatus;
    }

    public String getArtComposition() {
        return artComposition;
    }

    public void setArtComposition(String artComposition) {
        this.artComposition = artComposition;
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

}