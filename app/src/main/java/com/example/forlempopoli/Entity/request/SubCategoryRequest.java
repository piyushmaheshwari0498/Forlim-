package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("scat_id")
    @Expose
    private String scatId;
    @SerializedName("scat_name")
    @Expose
    private String scatName;
    @SerializedName("scat_photo")
    @Expose
    private String scatPhoto;

    @SerializedName("art_count")
    @Expose
    private String art_count;

    public String getScatId() {
        return scatId;
    }

    public void setScatId(String scatId) {
        this.scatId = scatId;
    }

    public String getScatName() {
        return scatName;
    }

    public void setScatName(String scatName) {
        this.scatName = scatName;
    }

    public String getScatPhoto() {
        return scatPhoto;
    }

    public void setScatPhoto(String scatPhoto) {
        this.scatPhoto = scatPhoto;
    }
    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getArt_count() {
        return art_count;
    }

    public void setArt_count(String art_count) {
        this.art_count = art_count;
    }
}
