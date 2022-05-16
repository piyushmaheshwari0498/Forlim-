package com.fashion.forlempopoli.Activity.payment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fashion.forlempopoli.Adapter.PlaceCartListAdapter;
import com.fashion.forlempopoli.Db.connection.DatabaseHelper;
import com.fashion.forlempopoli.Db.connection.ITable;
import com.fashion.forlempopoli.Entity.request.E_ClothOrderDetail;
import com.fashion.forlempopoli.Entity.request.InsertOrderRequest;
import com.fashion.forlempopoli.Entity.response.DataResponse;
import com.fashion.forlempopoli.Entity.response.E_OrderPlaceResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.MainActivity;
import com.fashion.forlempopoli.Model.M_Clothes_Order_Details;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.fashion.forlempopoli.databinding.ActivityPaymentBinding;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.paynimo.android.payment.CustomButton;
import com.paynimo.android.payment.CustomTextView;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;
import com.paynimo.android.payment.util.Constant;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    AppSharedPreference mAppSharedPrefernce;
    String amount;
    String acc_id;
    DatabaseHelper mDatabaseHelper;
    List<M_Clothes_Order_Details> e_clothOrderDetailList;
    List<M_Clothes_Order_Details> mClothesOrderDetailsList;
    Date date;
    PlaceCartListAdapter cartAdapter;
    HashMap<String,RequestBody> itemMap ;
    RequestBody apiAccessKey,status_code,account_id,final_amt,daterb,
            errorMsg,statusMsg,sm_id,identifier_id,bank_selection_code,rference_identifier;
    SimpleDateFormat formatter;
    private static final String TAG = "message";
    String statusCode,statusMessage,errorMessage,dateTime,merchantTransactionIdentifier,
            identifier,bankSelectionCode,bankReferenceIdentifier;
    String sm,acc,finalamt,da;
    private String SCHEMECODE = "FIRST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Confirm Payment");
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(PaymentActivity.this);
        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        mClothesOrderDetailsList = new ArrayList<>();
        e_clothOrderDetailList = new ArrayList<>();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("data"));

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());
        acc_id = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);


        amount = getIntent().getStringExtra("amount");
        binding.tvorderAmount.setText(getString(R.string.Rs)+amount);
        binding.tvorderDate.setText(date.toString());
        binding.btnPay.setText("Pay " + getString(R.string.Rs)+amount);



        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                smId = invoiceRequestList.get(pos).getSmId();
                // System.out.println("smId tvPayNow"+smId);

//                finalamt = invoiceRequestList.get(pos).getSmFinalAmt();
                // System.out.println("tvPayNow finalamt"+finalamt);

                Intent intent = new Intent("data");
                intent.putExtra("acc_id", acc_id);
                intent.putExtra("sm_id", sm);
                intent.putExtra("sm_final_amt", "1");
                intent.putExtra("date", formatter.format(date));
                LocalBroadcastManager.getInstance(PaymentActivity.this).sendBroadcast(intent);

                try {
                    sendOrderDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getCartData();

        //After Successful Payment
        /*try {

            sendOrderDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("else order add " + mAppSharedPrefernce.getString
                (Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));
        System.out.println("else order add ");*/
    }

    public void getCartData() {
        mClothesOrderDetailsList = mDatabaseHelper.getAllCartProduct(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        if (!mClothesOrderDetailsList.isEmpty()) {
            cartAdapter = new PlaceCartListAdapter(PaymentActivity.this,mClothesOrderDetailsList,
                    R.layout.placecart_adapter_layout);
            binding.orderRV.setLayoutManager(new LinearLayoutManager(this));
            binding.orderRV.setAdapter(cartAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                cancel_payment_dialog();
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    public void cancel_payment_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.paynimo_dialog_two_button);

        CustomTextView textcredit = dialog.findViewById(R.id.paynimo_custom_dialog_text);
        CustomButton ButtonCancel = dialog.findViewById(R.id.paynimo_custom_dialog_ButtonCancel);
        CustomButton ButtonOK = dialog.findViewById(R.id.paynimo_custom_dialog_ButtonOK);

        textcredit.setText(getString(R.string.paymnet_cancel_hint));
        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private void sendOrderDetails() throws JSONException {
        e_clothOrderDetailList = mDatabaseHelper.getAllClothItems(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));

        if (e_clothOrderDetailList.isEmpty()) {
            Toast.makeText(PaymentActivity.this, "No Item In Cart", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            List<E_ClothOrderDetail> orderDetailList = new ArrayList<>();

            for (int i = 0; i < e_clothOrderDetailList.size(); i++) {
                // TODO: 8/26/2020  need to check where qty meters in Api from backend Team
                double totalQuanityMtrs = (e_clothOrderDetailList.get(i).getOrder_unit() * e_clothOrderDetailList.get(i).getQty_mteres());
                System.out.println("CartActivity totalQuanityMtrs" + totalQuanityMtrs);
                E_ClothOrderDetail clothOrderDetail = new E_ClothOrderDetail();
                clothOrderDetail.setArtId(String.valueOf(e_clothOrderDetailList.get(i).getArt_id()));
                clothOrderDetail.setQtyPcs(String.valueOf(e_clothOrderDetailList.get(i).getOrder_unit()));
                clothOrderDetail.setQtyMtrs(e_clothOrderDetailList.get(i).getQty_mteres());
                clothOrderDetail.setTotalQtyMtrs(String.valueOf(totalQuanityMtrs));
                clothOrderDetail.setSellingPrice(String.valueOf(e_clothOrderDetailList.get(i).getArt_selling_price_amt()));
//                if (e_clothOrderDetailList.get(i).getArt_offer_price() != 0)
//                    clothOrderDetail.setSellingPrice(String.valueOf(e_clothOrderDetailList.get(i).getArt_offer_price()));
//                else {
//                clothOrderDetail.setSellingPrice(String.valueOf(e_clothOrderDetailList.get(i).getArt_selling_price_amt()));
//                }
//                clothOrderDetail.setOfferprice(String.valueOf(e_clothOrderDetailList.get(i).getArt_offer_price()));
                orderDetailList.add(clothOrderDetail);

            }
            final InsertOrderRequest minsertOrderRequest = new InsertOrderRequest();
            minsertOrderRequest.setAPIACCESSKEY("ZkC6BDUzxz");
            minsertOrderRequest.setClientId(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            minsertOrderRequest.setOrderData(orderDetailList);
            minsertOrderRequest.setDelivery_addr_id(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));

            final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
            Call<E_OrderPlaceResponse> call = apiInterface.placeorder(minsertOrderRequest);//mJsonObject
            call.enqueue(new Callback<E_OrderPlaceResponse>() {
                @Override
                public void onResponse(Call<E_OrderPlaceResponse> call, Response<E_OrderPlaceResponse> response) {

                    if (response.code() == 200 && response.message().equals("OK")) {
                        if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {

//                            Toast.makeText(getApplicationContext(), "Order Successfully Placed", Toast.LENGTH_LONG).show();
                            navigateToPayementActivity();
                            sm = response.body().getAm_id();
//                            invalidateOptionsMenu();
                            //navigateToOrderActivity();
                           /* Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                            startActivity(i);*/
//                            showSuccessAlertDialogButtonClicked();

                        }
                    } else {
                        if (response.code() == 404 && response.message().equals("Not Found")) {
                            //                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = response.errorBody().toString();
                            Log.d("ordererror",response.toString());
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Something went wrong!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<E_OrderPlaceResponse> call, Throwable t) {
                    Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Order onFailure" + t.getMessage());
                    //ringProgressDialog.dismiss();
                }
            });
        }
    }

    public int getRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(1000);
    }

    private void navigateToPayementActivity() {
        // Creating Checkout Object
        Checkout checkout = new Checkout();
        checkout.setMerchantIdentifier("L619165"); //where T1234 is the MERCHANT CODE, update it with Merchant Code provided by TPSL
        checkout.setTransactionIdentifier("TXN"+acc_id); //where TXN001 is the Merchant Transaction Identifier, it should be different for each transaction (alphanumeric value, no special character allowed)
        checkout.setTransactionReference("ORD"+getRandomNumber()); //where ORD0001 is the Merchant Transaction Reference number
        checkout.setTransactionType(com.paynimo.android.payment.PaymentActivity.TRANSACTION_TYPE_SALE);//Transaction Type
        checkout.setTransactionSubType(com.paynimo.android.payment.PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);//Transaction Subtype
//        checkout.setPaymentMethodType(com.paynimo.android.payment.PaymentActivity.PAYMENT_METHOD_UPI);//Transaction Subtype
        checkout.setTransactionCurrency("INR"); //Currency Type
        checkout.setTransactionAmount("1"); //Transaction Amount
        checkout.setTransactionDateTime(formatter.format(date)); //Transaction Date

        // setting Consumer fields values
        checkout.setConsumerIdentifier(""); //Consumer Identifier, default value "", set this value as application user name if you want Instrument Vaulting, SI on Cards. Consumer ID should be alpha-numeric value with no space
        checkout.setConsumerAccountNo(""); //Account Number, default value "". For eMandate, you can set this value here or can be set later in SDK.
        // setting Consumer Cart Item
        /*
      // Above Cart Item has following fields

ProductID //SCHEME CODE, update it with Scheme Code provided by TPSL
ProductAmount //amount of the Product should be same as Transaction amount
ProductSurchargeOrDiscountAmount //Surcharge Or Discount amount, default value 0.0
CommisionAmount	//Commision Amount on the Product, default value 0.0
ProductSKU   //default ""
ProductReference  //default ""
ProductDescriptor  //Additional details for Product, default ""
ProductProviderIdentifier  //Product provider Id, default ""

       */
        for(int i=0;i<mClothesOrderDetailsList.size();i++){
            checkout.addCartItem(SCHEMECODE, String.valueOf(mClothesOrderDetailsList.get(i).getPer_item_total()),
                    "0.0", "0.0",
                    mClothesOrderDetailsList.get(i).getArt_no(), "Cloth",
                    mClothesOrderDetailsList.get(i).getArt_name()+" "
                            +String.valueOf(mClothesOrderDetailsList.get(i).getQty_mteres())+" meters",
                    "");
        }
//        checkout.addCartItem("ProductID", "ProductAmount", "ProductSurchargeOrDiscountAmount", "CommisionAmount",
//                "ProductSKU", "ProductReference", "ProductDescriptor", "ProductProviderID");

//        A consumer can have multiple Cart items, in this case the sum of all cart items
//        ProductAmount and CommisionAmount must be equal to the
//        checkout.setTransactionAmount (amount);
        checkout.setTransactionAmount("1"); //Transaction amount
//        If you want additional details of transaction, you can set those parameters in Checkout object as follows:
        checkout.setCartDescription("{name:User1}{accountNo:1234567890}");
// ACTION Y for SI enabled from merchant end and N for SI disabled
        checkout.setPaymentInstructionAction("N");
// Amount type -> Fixed - F, Maximum - M
        checkout.setPaymentInstructionType("F");
        checkout.setPaymentInstructionFrequency("ADHO");
// Max amount
//        checkout.setPaymentInstructionLimit("1000.00");
// Payment Frequency
// DAIL - Daily, Week - Weekly, MNTH - Monthly,
//        QURT - Quarterly, MIAN - Semi annually, YEAR - Yearly, BIMN - Bi-monthly,
//                ADHO - As and when presented
        /*checkout.setPaymentInstructionFrequency("ADHO");
// Debit start date, format -> 'DD-MM-YYYY'
        checkout.setPaymentInstructionStartDateTime("12-03-2021");
// Debit end date, format -> 'DD-MM-YYYY'
        checkout.setPaymentInstructionEndDateTime("12-03-2041");
// Account Holder Name (optional)
        checkout.setConsumerAccountHolderName("Rahul");
// Account Type (optional)
// Saving - Saving, Current - Current, Cash Credit - CC
        checkout.setConsumerAccountType("CC");
// Bank IFSC Code (optional)
        checkout.setPaymentInstrumentIFSC("ICIC0000015");
// Set Debit Date Flag (optional)
// Flag Y for setting mandate Debit Date
// Flag N/”” for not setting Debit Date
        checkout.setPaymentInstructionDebitFlag("Y");
// Set Debit Date (optional)
// If Flag Y set for DebitFlag then set Debit Day here in DD format
        checkout.setPaymentInstructionDebitDay("05");*/
        checkout.setPaymentInstructionDebitFlag("Y");
        checkout.setConsumerEmailID ("test@gmail.com"); //Consumer Email ID
        checkout.setConsumerMobileNumber ("7620656789"); //Consumer Mobile Number
        checkout.setConsumerPan ("BYTUI1144J"); //Consumer PAN
        checkout.setConsumerPhoneNumber ("02225458585"); //Consumer Phone Number

        Intent authIntent = PaymentModesActivity.Factory.getAuthorizationIntent(this, true);
        // Checkout Object
        Log.d("Checkout-Request Object", checkout.getMerchantRequestPayload().toString());
        authIntent.putExtra(Constant.ARGUMENT_DATA_CHECKOUT, checkout);
        // Public Key
        authIntent.putExtra(com.paynimo.android.payment.PaymentActivity.EXTRA_PUBLIC_KEY, "1234-6666-6789-56");
        // Requested Payment Mode
        authIntent.putExtra(com.paynimo.android.payment.PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE,
                com.paynimo.android.payment.PaymentActivity.PAYMENT_METHOD_DEFAULT);
        PaymentModesActivity.Settings settings = new PaymentModesActivity.Settings();
        authIntent.putExtra(Constant.ARGUMENT_DATA_SETTING, settings);
        startActivityForResult(authIntent, com.paynimo.android.payment.PaymentActivity.REQUEST_CODE);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.paynimo.android.payment.PaymentActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == com.paynimo.android.payment.PaymentActivity.RESULT_OK) {
                // Log.d(TAG, "Result Code :" + RESULT_OK);
                System.out.println( "Checkout-resultCode :" + resultCode);
                if (data != null) {
                    try {
                        Checkout checkout_res = (Checkout) data.getSerializableExtra(Constant.ARGUMENT_DATA_CHECKOUT);
//                        System.out.println("Checkout Response Obj onActivityResult" + checkout_res.getMerchantResponsePayload().toString());
                        String transactionSubType = checkout_res.getMerchantRequestPayload().getTransaction().getSubType();
                        System.out.println("Checkout-Payment type => " + transactionSubType);
//
                        statusCode=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode();
                        System.out.println("Checkout-StatusCode "+statusCode);
                        amount=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount();
                        System.out.println("amount"+amount);

                        dateTime=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getDateTime();
                        System.out.println("dateTime"+dateTime);

                        merchantTransactionIdentifier=checkout_res.getMerchantResponsePayload().getMerchantTransactionIdentifier();
                        System.out.println("Checkout-merchantTransactionIdentifier "+merchantTransactionIdentifier);

                        identifier=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getIdentifier();
                        System.out.println("Checkout-identifier "+identifier);

                        bankSelectionCode=checkout_res.getMerchantResponsePayload().getPaymentMethod().getBankSelectionCode();
                        System.out.println("Checkout-bankSelectionCode "+bankSelectionCode);

                        errorMessage = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getErrorMessage();
                        System.out.println("Checkout-errorMessage " + errorMessage);

                        bankReferenceIdentifier=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getBankReferenceIdentifier();
                        System.out.println("Checkout-bankReferenceIdentifier "+bankReferenceIdentifier);
                        //Transaction Completed and Got SUCCESS
                        //SALES_DEBIT
                        if (statusCode.equalsIgnoreCase(com.paynimo.android.payment.PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                            Toast.makeText(getApplicationContext(), "Your Transaction Status - Payment Successful", Toast.LENGTH_SHORT).show();
                            statusMessage = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusMessage();
                            System.out.println("Checkout-statusMessage " + statusMessage);
                            System.out.println("Checkout-sendDataToServer");
                        }
                        else {
                            statusMessage = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusMessage();
                            System.out.println("Checkout-statusMessage else fail" + statusMessage);

                            System.out.println("Checkout-Bank Error Failure");
                            // some error from bank side
                            Log.d("Checkout-TRANSACTION STATUS=>", "FAILURE");
                            Toast.makeText(getApplicationContext(),"Transaction Status - Payment Unsuccessful" , Toast.LENGTH_SHORT).show();
                        }
                        sendDataToServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                        Log.d("Checkout-TRANSACTION Exception=>", e.toString());
                    }
                }
            } else if (resultCode == com.paynimo.android.payment.PaymentActivity.RESULT_ERROR) {
                Log.d("Checkout-", "Checkout-got an error"+ com.paynimo.android.payment.PaymentActivity.RESULT_ERROR);
                if (data.hasExtra(com.paynimo.android.payment.PaymentActivity.RETURN_ERROR_CODE) && data.hasExtra(com.paynimo.android.payment.PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                    String error_code = (String) data.getStringExtra(com.paynimo.android.payment.PaymentActivity.RETURN_ERROR_CODE);
                    String error_desc = (String) data.getStringExtra(com.paynimo.android.payment.PaymentActivity.RETURN_ERROR_DESCRIPTION);
                    Log.d("Checkout-", "Checkout-gotanerror ::"+ error_desc);
                    Toast.makeText(getApplicationContext(), " Error : " + error_code + " ---\n " + error_desc, Toast.LENGTH_LONG).show();
                    Log.d("Checkout-" + " Code=>", error_code);
                    Log.d("Checkout-" + " Desc=>", error_desc);
                    FirebaseCrashlytics.getInstance().log(error_code+"::"+error_desc);
                }
            }
            else if (resultCode == com.paynimo.android.payment.PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Your Transaction Aborted by User", Toast.LENGTH_LONG).show();
                Log.d("Checkout-", "Checkout-Userpressedbackbutton"+ com.paynimo.android.payment.PaymentActivity.RESULT_CANCELED);
            }
        }
    }

    public BroadcastReceiver mMessageReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    acc = intent.getStringExtra("acc_id");
                    System.out.println("acc"+acc);

                    sm = intent.getStringExtra("sm_id");
                    System.out.println("sm"+sm);

                    finalamt = intent.getStringExtra("sm_final_amt");
                    System.out.println("finalamt"+finalamt);

                    da = intent.getStringExtra("date");
                    System.out.println("da"+da);
                }
            };

    public void sendDataToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {
                    //constants
                    URL url = new URL("http://forlimpopoli.in/beta_2mobileapi/api/Customer_master.php/mobile_api/");

                    apiAccessKey = RequestBody.create(MultipartBody.FORM, "ZkC6BDUzxz");
                    sm_id= RequestBody.create(MultipartBody.FORM, sm);
                    status_code = RequestBody.create(MultipartBody.FORM, statusCode);
                    account_id = RequestBody.create(MultipartBody.FORM, acc);
                    final_amt = RequestBody.create(MultipartBody.FORM, amount);
                    daterb = RequestBody.create(MultipartBody.FORM, da);
                    errorMsg = RequestBody.create(MultipartBody.FORM, errorMessage);
                    statusMsg = RequestBody.create(MultipartBody.FORM, statusMessage);
                    identifier_id= RequestBody.create(MultipartBody.FORM, identifier);
                    bank_selection_code= RequestBody.create(MultipartBody.FORM, bankSelectionCode);
                    rference_identifier= RequestBody.create(MultipartBody.FORM, bankReferenceIdentifier);

                    itemMap = new HashMap<>();
                    itemMap.put("API_ACCESS_KEY", apiAccessKey);
                    itemMap.put("sm_id", sm_id);
                    itemMap.put("statusCode", status_code);
                    itemMap.put("acc_id", account_id);
                    itemMap.put("sm_final_amt",final_amt );
                    itemMap.put("paydate", daterb);
                    itemMap.put("error_msg", errorMsg);
                    itemMap.put("status_msg", statusMsg);
                    itemMap.put("identifier", identifier_id);
                    itemMap.put("bank_selection_code", bank_selection_code);
                    itemMap.put("bank_reference_identifier", rference_identifier);

                    String message = itemMap.toString();
                    System.out.println("jsonObject"+message);
                    if(conn!=null) {
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000 /*milliseconds*/);
                        conn.setConnectTimeout(15000 /*milliseconds */);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setFixedLengthStreamingMode(message.getBytes().length);

                        //make some HTTP header nicety
                        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                        //open
                        conn.connect();
                    }
                    //setup send
                    if (os != null&&conn!=null) {
                        os = new BufferedOutputStream(conn.getOutputStream());
                        os.write(message.getBytes());
                        //clean up
                        os.flush();
                    }
                    //do somehting with response
                    if (is != null&&conn!=null) {
                        is = conn.getInputStream();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //clean up
                    if (os != null) {
                        try {
                            os.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(conn!=null){
                        conn.disconnect();
                    }
                }
                final ApiInterface apiInterface= RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
                Call<DataResponse> call = apiInterface.sendData(itemMap);
                call.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        if (response.code() == 200 && response.message().equals("OK")) {
                            if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                                System.out.println("InvoiceActivity OK "+ response.code());
                               /* try {
                                    sendOrderDetails();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                                for (M_Clothes_Order_Details orderDetails : e_clothOrderDetailList) {
                                    ContentValues mContentValues = new ContentValues();
                                    mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED, "1");
                                    mDatabaseHelper.updateProductPlaceStatus(mContentValues,
                                            String.valueOf(orderDetails.getArt_id()), mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                                }
                                System.out.println("Response " + response.body().toString());
                                showSuccessAlertDialogButtonClicked();
                            }
                        }
                        else{
                            showErrorAlertDialogButtonClicked();
                        }
//                       else {
//                            if (response.code() == 404 && response.message().equals("Not Found")) {
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                                        String message = jsonObject.getString("message");
//                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                                        System.out.println("InvoiceActivity NotFound " + response.code());
//                                    } catch (JSONException | IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    System.out.println("InvoiceActivity NotFound " + response.code());
//                           }
//                            else {
//                                Toast.makeText(InvoiceActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
//                            }
//                        }
                    }
                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        //ringProgressDialog.dismiss();
                        System.out.println("InvoiceActivity onFailure "+ t.getMessage());
                        Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }).start();
    }

    public void showSuccessAlertDialogButtonClicked()
    {
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
//        builder.setTitle("Name");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.success_alert,
                        null);
        builder.setView(customLayout);
        builder.setCancelable(false);

        Button okayBtn = customLayout.findViewById(R.id.btnokay);

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    public void showErrorAlertDialogButtonClicked()
    {
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
//        builder.setTitle("Name");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.success_error_alert,
                        null);
        builder.setView(customLayout);
        builder.setCancelable(false);

        Button okayBtn = customLayout.findViewById(R.id.btnokay);


        // create and show
        // the alert dialog
        AlertDialog dialog = builder.create();

        dialog.show();
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/
                dialog.dismiss();
                navigateToPayementActivity();
            }
        });

    }

}