package com.fashion.forlempopoli.Db.connection;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fashion.forlempopoli.Model.M_Clothes_Order_Details;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ForlemPopoli";
    private static DatabaseHelper mDatabaseHelper;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ITable.CLOTHES_ORDER_DETAILS.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }

    //Clothes Order
    public long insertCartDetails(ContentValues argContentValues) {
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
        long insertId = mSqLiteDatabase.insert(ITable.CLOTHES_ORDER_DETAILS.TABLE_NAME, null, argContentValues);
        mSqLiteDatabase.close();
        return insertId;
    }

    @SuppressLint("Range")
    public int getCountOfProduct(String argFoodId, String account_id) {
        String count = "0";
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        Cursor mCursor = mSqLiteDatabase.rawQuery(
                "select count(*) as count from tbl_cloths_order_details " +
                        "where art_details_id = ? and is_order_placed = ? and  acc_id = ?",
                new String[]{argFoodId, "0", account_id});
        if (mCursor != null && mCursor.getCount() > 0) {
            if (mCursor.moveToFirst()) {
                while (!mCursor.isAfterLast()) {
                    count = mCursor.getString(mCursor.getColumnIndex("count"));
                    // do what ever you want here
//                    System.out.println("Count Obtained " + count);
                    mCursor.moveToNext();
                }
            }
            mCursor.close();
            mSqLiteDatabase.close();
        }
        return Integer.parseInt(count);
    }

    public boolean getifProductExist(String argFoodId, String account_id) {
        boolean count = false;
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        Cursor mCursor = mSqLiteDatabase.rawQuery(
                " SELECT art_details_id FROM tbl_cloths_order_details " +
                        "where art_details_id = ? and is_order_placed = ? and  acc_id = ?",
                new String[]{argFoodId, "0", account_id});
        if (mCursor.getCount() > 0) {
            count = true;
        }
        else {
            count = false;
        }

            mCursor.close();
            mSqLiteDatabase.close();

        return count;
    }


    @SuppressLint("Range")
    public int getCartCount(String account_id) {
        String count = "0";
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        String[] whereClauseArguments = {"0", account_id};
        try {
            Cursor mCursor = mSqLiteDatabase.rawQuery("select sum(order_unit) " +
                    "as count from tbl_cloths_order_details where is_order_placed = ? and  acc_id = ? ", whereClauseArguments);
            if (mCursor != null && mCursor.getCount() > 0) {
                if (mCursor.moveToFirst()) {

                    while (!mCursor.isAfterLast()) {
                        count = mCursor.getString(mCursor.getColumnIndex("count"));
                        if (count == null) {
                            count = "0";
                        }
                        // do what ever you want here
                        //   System.out.println("Cart Count Obtained food getCartCount"+count);
                        mCursor.moveToNext();
                    }
                }
                mCursor.close();
                mSqLiteDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            count = "0";
        }
        return Integer.valueOf(count);
    }

    public int getCurrentProduct(String argClothId,String account_id) {
        String count = "0";
        List<M_Clothes_Order_Details> mClothesOrderDetailsList = new ArrayList<>();
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        Cursor mCursor = mSqLiteDatabase.rawQuery("select * from tbl_cloths_order_details where art_details_id = ? and is_order_placed = ? and  acc_id = ?", new String[]{argClothId, "1",account_id});
        if (mCursor != null && mCursor.getCount() > 0) {
            if (mCursor.moveToFirst()) {
                while (!mCursor.isAfterLast()) {
                    int art_id, art_cat_id, art_scat_id, art_no, art_width, art_selling_price_amt, art_offer_price, art_status, order_unit, is_order_placed;
                    String art_name, art_shade_no, art_stock_type, art_composition, art_photo, garment_photo, total_price;
                    @SuppressLint("Range")
                    M_Clothes_Order_Details mClothesOrderDetails = new M_Clothes_Order_Details(
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.ACC_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_STATUS)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SHADE_NO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_PHOTO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CGST)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SGST)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_IGST)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL)));
                    mClothesOrderDetailsList.add(mClothesOrderDetails);
                    mClothesOrderDetailsList.add(mClothesOrderDetails);
//                    System.out.println("Count Obtained " + count);
                    mCursor.moveToNext();
                }
            }
        }
        return mCursor.getCount();
    }


    public int updateProductQuanitity(ContentValues argContentValues, String argClothId, String account_id) {
        String count = "0";
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
        String whereClause = ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID + " = ?  AND "
                + ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED + " = ? AND " + ITable.CLOTHES_ORDER_DETAILS.ACC_ID + " = ?";
        String[] argumnets = new String[]{argClothId, "0", account_id};
        int updatedRows = mSqLiteDatabase.update(ITable.CLOTHES_ORDER_DETAILS.TABLE_NAME, argContentValues, whereClause, argumnets);
        return updatedRows;
    }

    public List<M_Clothes_Order_Details> getAllCartProduct(String acc_id) {
        String count = "0";
        List<M_Clothes_Order_Details> mCartList = new ArrayList<>();
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        Cursor mCursor = mSqLiteDatabase.rawQuery("select * from tbl_cloths_order_details where is_order_placed = ? and acc_id = ? ", new String[]{"0", acc_id});
        if (mCursor != null && mCursor.getCount() > 0) {
            if (mCursor.moveToFirst()) {
                while (!mCursor.isAfterLast()) {
//                    Log.d("getCOL_ART_DETAILS_ID",ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID);

                    @SuppressLint("Range") M_Clothes_Order_Details mClothesOrderDetails = new M_Clothes_Order_Details(
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.ACC_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_STATUS)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SHADE_NO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_PHOTO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CGST)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SGST)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_IGST)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL)));
                    mCartList.add(mClothesOrderDetails);

                    System.out.println("Count Obtained getAllCartProduct" + mCartList.size());
                    mCursor.moveToNext();
                }
            }
        }
        return mCartList;
    }

    @SuppressLint("Range")
    public double getTotalCartPrice(String acc_id) {
        String count = "0";
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        try {
            Cursor mCursor =
                    mSqLiteDatabase.rawQuery("select sum(total_price) as count from tbl_cloths_order_details where is_order_placed = ? and acc_id = ?", new String[]{"0", acc_id});
            if (mCursor != null && mCursor.getCount() > 0) {
                if (mCursor.moveToFirst()) {
                    while (!mCursor.isAfterLast()) {
                        count = mCursor.getString(mCursor.getColumnIndex("count"));
                        if (count == null) {
                            count = "0";
                        }
                        // do what ever you want here
//                        System.out.println("Cart Count Obtained getTotalCartPrice" + count);
                        mCursor.moveToNext();
                    }
                }
                mCursor.close();
                mSqLiteDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            count = "0";
        }
        return Double.parseDouble((count));
    }

    public int removeProductFromCart(String argClothId, String acc_id) {
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
//        Log.d("db removeId",argClothId);
//        Log.d("db removeAccId",acc_id);
        String whereClause = ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID + " = ?  AND "
                + ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED + " = ? AND "
                + ITable.CLOTHES_ORDER_DETAILS.ACC_ID + " = ?";
        String[] whereClauseArgumnets = {argClothId, "0", acc_id};
        int noOfRowsDeleted = mSqLiteDatabase.delete(ITable.CLOTHES_ORDER_DETAILS.TABLE_NAME, whereClause, whereClauseArgumnets);
        return noOfRowsDeleted;

    }
  /*  public long insertOrderId(ContentValues contentValues)
    {
        SQLiteDatabase mSqLiteDatabase=getWritableDatabase();
        long insertId=mSqLiteDatabase.insert(ITable.ORDER.TABLE_NAME ,null,contentValues);
        mSqLiteDatabase.close();
        return insertId;
    }
    public int updateClothOrderId(long orderId,String[] clothDetailsId*//*,String options*//*)
    {
        ContentValues mContentValues=new ContentValues();
        mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_ID,orderId);

        String count="0";
        SQLiteDatabase mSqLiteDatabase=getWritableDatabase();
        String whereClause=ITable.CLOTHES_ORDER_DETAILS.COL_ID*//*+" IN ("+options+")"*//*;
        String [] argumnets=clothDetailsId;
        int updatedRows= mSqLiteDatabase.update(ITable.CLOTHES_ORDER_DETAILS.TABLE_NAME,mContentValues,whereClause,argumnets);

        return updatedRows;
    }*/

    public int updateProductPlaceStatus(ContentValues argContentValues, String argClothId, String acc_id) {
        String count = "0";
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
        String whereClause = ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID + " = ? AND " + ITable.CLOTHES_ORDER_DETAILS.ACC_ID + " = ?";
        String[] argumnets = new String[]{argClothId, acc_id};
        int updatedRows = mSqLiteDatabase.update(ITable.CLOTHES_ORDER_DETAILS.TABLE_NAME, argContentValues, whereClause, argumnets);

        return updatedRows;
    }


    public List<M_Clothes_Order_Details> getAllClothItems(String acc_id) {
        String count = "0";
        List<M_Clothes_Order_Details> mclothOrderDetailList = new ArrayList<>();
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        Cursor mCursor = mSqLiteDatabase.rawQuery("select * from tbl_cloths_order_details where is_order_placed = ? AND acc_id = ?",
                new String[]{"0", acc_id});
        if (mCursor != null && mCursor.getCount() > 0) {
            if (mCursor.moveToFirst()) {
                while (!mCursor.isAfterLast()) {
                    @SuppressLint("Range") M_Clothes_Order_Details mClothesOrderDetails = new M_Clothes_Order_Details(
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.ACC_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_STATUS)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SHADE_NO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_PHOTO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO)),
                            mCursor.getString(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CGST)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_SGST)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_IGST)),
                            mCursor.getInt(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS)),
                            mCursor.getDouble(mCursor.getColumnIndex(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL)));
                    mclothOrderDetailList.add(mClothesOrderDetails);

                    System.out.println("Count Obtained Cloth Cart getAllFoodItems" + count);
                    mCursor.moveToNext();
                }
            }
        }
        return mclothOrderDetailList;
    }

    @SuppressLint("Range")
    public double getPrice(String acc_id) {
        String count = "0";
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        try {
            Cursor mCursor = mSqLiteDatabase.rawQuery("select sum(art_selling_price_amt*art_quantity_meters) as count from tbl_cloths_order_details where is_order_placed =? and acc_id = ?", new String[]{"0", acc_id});
            if (mCursor != null && mCursor.getCount() > 0) {
                if (mCursor.moveToFirst()) {
                    while (!mCursor.isAfterLast()) {
                        count = mCursor.getString(mCursor.getColumnIndex("count"));
                        if (count == null) {
                            count = "0";
                        }
                        // do what ever you want here
//                        System.out.println("getPrice" + count);
                        mCursor.moveToNext();
                    }
                }
                mCursor.close();
                mSqLiteDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            count = "0";
        }
        return Double.parseDouble((count));
    }
}
