package com.fashion.forlempopoli.Db.connection;

public interface ITable {

    public interface CLOTHES_ORDER_DETAILS {
        String TABLE_NAME = "tbl_cloths_order_details";
        String COL_ID = "_id";
        String ACC_ID = "acc_id";
        String COL_ART_DETAILS_ID = "art_details_id";
        String COL_CATEGORY_ID ="art_cat_id";
        String COL_SUB_CATEGORY_ID="art_scat_id";
        String COL_ART_NUMBER="art_no";
        String COL_ART_NAME="art_name";
        String COL_SHADE_NO="art_shade_no";
        String COL_ART_WIDTH="art_width";
        String COL_SELLING_PRICE="art_selling_price_amt";
        String COL_OFFER_PRICE="art_offer_price";
        String COL_STOCK_TYPE="art_stock_type";
        String COL_ART_STATUS="art_status";
        String COL_ART_COMPOSITION="art_composition";
        String COL_ART_PHOTO="art_photo";
        String COL_ART_GARMENT_PHOTO="garment_photo";
        String COL_ORDER_UNIT = "order_unit";
        String COL_TOTAL_PRICE = "total_price";
        String COL_IS_ORDER_PLACED = "is_order_placed";
        String COL_CUSTOMER_TYPE = "customer_type";
        String COL_ART_QUANTITY_METERS = "art_quantity_meters";
        String COL_MIN_ART_QUANTITY_METERS = "min_art_quantity_meters";
        String COL_CGST = "cgst";
        String COL_SGST = "sgst";
        String COL_IGST = "igst";
        String COL_PER_ITEM_TOTAL = "per_item_total";

        String CREATE_TABLE_QUERY =
                " CREATE TABLE " +
                        CLOTHES_ORDER_DETAILS.TABLE_NAME +
                        " ( " +
                        CLOTHES_ORDER_DETAILS.COL_ID + " integer primary key autoincrement ," +
                        CLOTHES_ORDER_DETAILS.ACC_ID + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_NUMBER + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_NAME + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_SHADE_NO + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_WIDTH + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_STATUS + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_PHOTO + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO + " text ," +
                        CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE + " real ," +
                        CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_CGST + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_SGST + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_IGST + " integer ," +
                        CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL + " real ," +
                        CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS + " TEXT ," +
                        CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS + " TEXT" +
                        " ) ";


    }

}
