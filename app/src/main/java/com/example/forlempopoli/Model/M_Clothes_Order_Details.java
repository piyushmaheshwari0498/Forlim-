package com.example.forlempopoli.Model;


public class M_Clothes_Order_Details {
    int id,acc_id,art_id,art_cat_id,art_scat_id,art_width,art_selling_price_amt,art_offer_price,art_status,order_unit,is_order_placed,
    customer_type;
    String art_no,art_name,art_shade_no,art_stock_type,art_composition,art_photo,garment_photo,total_price;
    double qty_mteres,cgst,sgst,igst,per_item_total;

    public M_Clothes_Order_Details() {
    }

    public M_Clothes_Order_Details(int id, int acc_id,int art_id, int art_cat_id, int art_scat_id, String art_no, int art_width, int art_selling_price_amt, int art_offer_price, int art_status, int order_unit, int is_order_placed,  String art_name, String art_shade_no, String art_stock_type, String art_composition, String art_photo, String garment_photo, String total_price
     ,double cgst, double sgst, double igst,int customer_type,double qty_mteres,double per_item_total) {
        this.id = id;
        this.acc_id = acc_id;
        this.art_id = art_id;
        this.art_cat_id = art_cat_id;
        this.art_scat_id = art_scat_id;
        this.art_width = art_width;
        this.art_selling_price_amt = art_selling_price_amt;
        this.art_offer_price = art_offer_price;
        this.art_status = art_status;
        this.order_unit = order_unit;
        this.is_order_placed = is_order_placed;
        this.cgst = cgst;
        this.sgst = sgst;
        this.igst = igst;
        this.art_no = art_no;
        this.art_name = art_name;
        this.art_shade_no = art_shade_no;
        this.art_stock_type = art_stock_type;
        this.art_composition = art_composition;
        this.art_photo = art_photo;
        this.garment_photo = garment_photo;
        this.total_price = total_price;
        this.customer_type = customer_type;
        this.qty_mteres = qty_mteres;
        this.per_item_total = per_item_total;
    }


    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArt_id() {
        return art_id;
    }

    public void setArt_id(int art_id) {
        this.art_id = art_id;
    }

    public int getArt_cat_id() {
        return art_cat_id;
    }

    public void setArt_cat_id(int art_cat_id) {
        this.art_cat_id = art_cat_id;
    }

    public int getArt_scat_id() {
        return art_scat_id;
    }

    public void setArt_scat_id(int art_scat_id) {
        this.art_scat_id = art_scat_id;
    }

    public String getArt_no() {
        return art_no;
    }

    public void setArt_no(String art_no) {
        this.art_no = art_no;
    }

    public int getArt_width() {
        return art_width;
    }

    public void setArt_width(int art_width) {
        this.art_width = art_width;
    }

    public int getArt_selling_price_amt() {
        return art_selling_price_amt;
    }

    public void setArt_selling_price_amt(int art_selling_price_amt) {
        this.art_selling_price_amt = art_selling_price_amt;
    }

    public int getArt_offer_price() {
        return art_offer_price;
    }

    public void setArt_offer_price(int art_offer_price) {
        this.art_offer_price = art_offer_price;
    }

    public int getArt_status() {
        return art_status;
    }

    public void setArt_status(int art_status) {
        this.art_status = art_status;
    }

    public int getOrder_unit() {
        return order_unit;
    }

    public void setOrder_unit(int order_unit) {
        this.order_unit = order_unit;
    }

    public int getIs_order_placed() {
        return is_order_placed;
    }

    public void setIs_order_placed(int is_order_placed) {
        this.is_order_placed = is_order_placed;
    }

    public String getArt_name() {
        return art_name;
    }

    public void setArt_name(String art_name) {
        this.art_name = art_name;
    }

    public String getArt_shade_no() {
        return art_shade_no;
    }

    public void setArt_shade_no(String art_shade_no) {
        this.art_shade_no = art_shade_no;
    }

    public String getArt_stock_type() {
        return art_stock_type;
    }

    public void setArt_stock_type(String art_stock_type) {
        this.art_stock_type = art_stock_type;
    }

    public String getArt_composition() {
        return art_composition;
    }

    public void setArt_composition(String art_composition) {
        this.art_composition = art_composition;
    }

    public String getArt_photo() {
        return art_photo;
    }

    public void setArt_photo(String art_photo) {
        this.art_photo = art_photo;
    }

    public String getGarment_photo() {
        return garment_photo;
    }

    public void setGarment_photo(String garment_photo) {
        this.garment_photo = garment_photo;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public double getQty_mteres() {
        return qty_mteres;
    }

    public void setQty_mteres(double qty_mteres) {
        this.qty_mteres = qty_mteres;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public int getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(int customer_type) {
        this.customer_type = customer_type;
    }

    public double getPer_item_total() {
        return per_item_total;
    }

    public void setPer_item_total(double per_item_total) {
        this.per_item_total = per_item_total;
    }

    @Override
    public String toString() {
        return "M_Clothes_Order_Details{" +
                "id=" + id +
                ", acc_id=" + acc_id +
                ", art_id=" + art_id +
                ", art_cat_id=" + art_cat_id +
                ", art_scat_id=" + art_scat_id +
                ", art_width=" + art_width +
                ", art_selling_price_amt=" + art_selling_price_amt +
                ", art_offer_price=" + art_offer_price +
                ", art_status=" + art_status +
                ", order_unit=" + order_unit +
                ", is_order_placed=" + is_order_placed +
                ", customer_type=" + customer_type +
                ", art_no='" + art_no + '\'' +
                ", art_name='" + art_name + '\'' +
                ", art_shade_no='" + art_shade_no + '\'' +
                ", art_stock_type='" + art_stock_type + '\'' +
                ", art_composition='" + art_composition + '\'' +
                ", art_photo='" + art_photo + '\'' +
                ", garment_photo='" + garment_photo + '\'' +
                ", total_price='" + total_price + '\'' +
                ", qty_mteres=" + qty_mteres +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", igst=" + igst +
                ", per_item_total=" + per_item_total +
                '}';
    }
}
