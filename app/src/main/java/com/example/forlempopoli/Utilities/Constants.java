package com.example.forlempopoli.Utilities;

public class Constants {
     public static final String SHARED_PREFRENCE_NAME = "AppData";
     public static final String IS_LOGGED_IN = "login";
     public static final int RESPONSE_CODE = 200;// http response code
     public static final int RESPONSE_CODE1 = 201;// http response code
     public static final int RESPONSE_CODE_UNAUTHORISED = 203;// http response code
     public static final int RESPONSE_STATUS = 1;//server response code
     public static final int RESPONSE_ERROR = 500;

     public static final String BASE_URL = "http://forlimpopoli.in/beta_2mobileapi/";

     public interface SHARED_PREFERENCE_KEYS {
          public  String KEY_MOBILE_NUM = "mobile_num";
     }

     public interface INTENT_KEYS {
          public  String KEY_ACCOUNT_ID = "account_id";
          public  String KEY_ACCOUNT_PERSON_NAME = "account_person_name";
          public  String KEY_ACCOUNT_NAME = "account_name";
          public  String KEY_CATEGORY_ID = "category_id";
          public  String KEY_SM_ID = "sm_id";
          public  String KEY_AM_ID = "am_id";
          public  String KEY_CUSTOMER_TYPE = "customer_type";
          public  String KEY_CATEGORY_NAME = "category_name";
          public  String KEY_SUB_CATEGORY_ID = "sub_category_id";
          public  String KEY_FILTER_ARTICLE_NO = "filter_article_no";
          public  String KEY_FILTER_PRICE = "filter_price";
          public  String KEY_STATUS = "status";
          public  String KEY_CREDIT_DAYS = "credit_days";
          public  String KEY_CREDIT_LIMIT_AMOUNT = "credit_limit_amt";
          public  String KEY_ACC_CR_LIMIT = "acc_cr_limit";
          public  String KEY_PENDING_BAL = "acc_pending_balance";
          public  String KEY_TOTAL_BALANCE = "total_balance";
//          public  String KEY_DEFAULT_ADDRESS = "default_address";
          public  String KEY_DEFAULT_ADDRESS_ID = "default_address_id";
          public int REQUEST_CODE_FOR_MESSAGE=78;

     }
}
