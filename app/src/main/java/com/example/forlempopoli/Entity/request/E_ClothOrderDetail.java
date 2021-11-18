package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class E_ClothOrderDetail {
    @SerializedName("art_id")
    @Expose
    private String artId;
    @SerializedName("qty_pcs")
    @Expose
    private String qtyPcs;
    @SerializedName("qty_mtrs")
    @Expose
    private double qtyMtrs;
    @SerializedName("total_qty_mtrs")
    @Expose
    private String totalQtyMtrs;
    @SerializedName("selling_price")
    @Expose
    private String sellingPrice;
    @SerializedName("offer_price")
    @Expose
    private String offerprice;
    private double cgst;
    public E_ClothOrderDetail() {
    }

    public E_ClothOrderDetail(String artId, String qtyPcs, String sellingPrice) {
        this.artId = artId;
        this.qtyPcs = qtyPcs;
        this.sellingPrice = sellingPrice;
    }


    public String getArtId() {
        return artId;
    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public String getQtyPcs() {
        return qtyPcs;
    }

    public void setQtyPcs(String qtyPcs) {
        this.qtyPcs = qtyPcs;
    }

    public double getQtyMtrs() {
        return qtyMtrs;
    }

    public void setQtyMtrs(double qtyMtrs) {
        this.qtyMtrs = qtyMtrs;
    }

    public String getTotalQtyMtrs() {
        return totalQtyMtrs;
    }

    public void setTotalQtyMtrs(String totalQtyMtrs) {
        this.totalQtyMtrs = totalQtyMtrs;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public String getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(String offerprice) {
        this.offerprice = offerprice;
    }
}
