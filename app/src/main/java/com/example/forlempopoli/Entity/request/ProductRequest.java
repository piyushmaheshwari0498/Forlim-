package com.example.forlempopoli.Entity.request;

import android.util.Log;

import com.example.forlempopoli.Utilities.PatternClass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductRequest{
    /*Comparator for sorting the list by Artno*/
    public static Comparator<ProductRequest> Artno = new Comparator<ProductRequest>() {
        public int compare(ProductRequest s1, ProductRequest s2) {
            String artno1 = s1.getArt_name_no();
            String artno2 = s2.getArt_name_no();
            /*For ascending order*/
            return artno1.compareTo(artno2);

        }
    };
    // Using Art Id because Art Name is not getting sort, maybe because of blank space or '-'
    public static Comparator<ProductRequest> DescendingArtNo = new Comparator<ProductRequest>() {
        public int compare(ProductRequest s1, ProductRequest s2) {
            String artno1 = s1.getArt_name_no();
            String artno2 = s2.getArt_name_no();
            /*For ascending order*/
            return artno2.compareTo(artno1);

        }
    };
// Using Art Id because Art Name is not getting sort, maybe because of blank space or '-'
    public static Comparator<ProductRequest> AscArtName = new Comparator<ProductRequest>() {
        public int compare(ProductRequest lhs, ProductRequest rhs) {
            /*For ascending order*/

//            Log.d("asc",lhs.getArt_name_no());
            return PatternClass.removeNonAlphanumeric(lhs.getArt_name_no()).
                    compareTo(PatternClass.removeNonAlphanumeric(rhs.getArt_name_no()));

        }
    };
    public static Comparator<ProductRequest> DesArtName = new Comparator<ProductRequest>() {
        public int compare(ProductRequest lhs, ProductRequest rhs) {
            /*For ascending order*/
//            Log.d("desc",lhs.getArt_name_no());
            return PatternClass.removeNonAlphanumeric(rhs.getArt_name_no()).
                    compareTo(PatternClass.removeNonAlphanumeric(lhs.getArt_name_no()));

        }
    };

    public static Comparator<ProductRequest> AscPrice = new Comparator<ProductRequest>() {
        public int compare(ProductRequest lhs, ProductRequest rhs) {
            /*For ascending order*/

//            Log.d("asc",lhs.getArt_name_no());
            return lhs.getArtOfferPrice().compareTo(rhs.getArtOfferPrice());

        }
    };

    public static Comparator<ProductRequest> DescPrice = new Comparator<ProductRequest>() {
        public int compare(ProductRequest lhs, ProductRequest rhs) {
            /*For ascending order*/

//            Log.d("asc",lhs.getArt_name_no());
            return lhs.getArtSellingPriceAmt().compareTo(rhs.getArtSellingPriceAmt());

        }
    };

    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;
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
    private Integer artSellingPriceAmt;
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
    @SerializedName("art_keyword")
    @Expose
    private String artKeyword;
    @SerializedName("art_photo")
    @Expose
    private String artPhoto;
    @SerializedName("garment_photo")
    @Expose
    private String garmentPhoto;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("scat_name")
    @Expose
    private String scatName;
    @SerializedName("art_sgst_per")
    @Expose
    private Double artSgstPer;
    @SerializedName("art_cgst_per")
    @Expose
    private Double artCgstPer;
    @SerializedName("art_igst_per")
    @Expose
    private Double artIgstPer;
    @SerializedName("art_qty_mtrs")
    @Expose
    private Double artQtyMtrs;

    @SerializedName("art_no_cnt")
    @Expose
    private String art_name_no;

    public String getArt_name_no() {
        return art_name_no;
    }

    public void setArt_name_no(String art_name_no) {
        this.art_name_no = art_name_no;
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

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

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

    public Integer getArtSellingPriceAmt() {
        return artSellingPriceAmt;
    }

    public void setArtSellingPriceAmt(Integer artSellingPriceAmt) {
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

    public String getArtKeyword() {
        return artKeyword;
    }

    public void setArtKeyword(String artKeyword) {
        this.artKeyword = artKeyword;
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

    public Double getArtSgstPer() {
        return artSgstPer;
    }

    public void setArtSgstPer(Double artSgstPer) {
        this.artSgstPer = artSgstPer;
    }

    public Double getArtCgstPer() {
        return artCgstPer;
    }

    public void setArtCgstPer(Double artCgstPer) {
        this.artCgstPer = artCgstPer;
    }

    public Double getArtIgstPer() {
        return artIgstPer;
    }

    public void setArtIgstPer(Double artIgstPer) {
        this.artIgstPer = artIgstPer;
    }

    public Double getArtQtyMtrs() {
        return artQtyMtrs;
    }

    public void setArtQtyMtrs(Double artQtyMtrs) {
        this.artQtyMtrs = artQtyMtrs;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "artNo='" + artNo + '\'' +
                '}';
    }
}
