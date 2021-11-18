package com.example.forlempopoli.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.forlempopoli.Adapter.InvoiceAdapter;
import com.example.forlempopoli.Entity.request.InvoiceRequest;
import com.example.forlempopoli.Entity.response.DataResponse;
import com.example.forlempopoli.Entity.response.InvoiceResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.model.Checkout;
import com.paynimo.android.payment.util.Constant;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity  {
    RecyclerView invoice_rV;
    InvoiceAdapter invoiceAdapter;
    private AppSharedPreference mAppSharedPrefernce;
    List<InvoiceRequest> invoiceRequestList;
    private static final String TAG = "message";
    String statusCode,statusMessage,errorMessage,amount,dateTime,merchantTransactionIdentifier,
            identifier,bankSelectionCode,bankReferenceIdentifier;
    String sm,acc,finalamt,da;
    HashMap<String,RequestBody> itemMap ;
    RequestBody apiAccessKey,status_code,account_id,final_amt,date,
            errorMsg,statusMsg,sm_id,identifier_id,bank_selection_code,rference_identifier;
    MaterialToolbar toolbar;
    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    AppUtils appUtils;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        invoice_rV=findViewById(R.id.invoice_rV);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Invoice");
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        appUtils = new AppUtils();
        mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(InvoiceActivity.this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("data"));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        no_data_text = findViewById(R.id.no_data_text);
        ll_Data_Found = findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);
        img_info = findViewById(R.id.img_info);
        retry_btn = findViewById(R.id.retry_btn);

        refreshItems();

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshItems();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

    }

    public void invoiceHistory() {
        final ApiInterface apiInterface= RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<InvoiceResponse> call = apiInterface.paymentDetails(getMap());

        call.enqueue(new Callback<InvoiceResponse>() {
            @Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    System.out.println("Success"+response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        // ringProgressDialog.dismiss();
                        invoiceRequestList= response.body().getData();
                        /*invoiceRequestList = new ArrayList<>();
                        invoiceRequestList.add(new InvoiceRequest
                                (mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID),"4.00","501"));
                        invoiceRequestList.add(new InvoiceRequest
                                (mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID),"8.00","502"));*/
                        if(!invoiceRequestList.isEmpty()) {
                            invoiceAdapter = new InvoiceAdapter(invoiceRequestList, InvoiceActivity.this);
                            invoice_rV.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                            invoice_rV.setAdapter(invoiceAdapter);
                        }
                         else {
                            ll_Data_Found.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }
                    }
                }
                else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(InvoiceActivity.this, message, Toast.LENGTH_LONG).show();
                            if (!appUtils.isConnected(InvoiceActivity.this)) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!appUtils.isConnected(InvoiceActivity.this)) {
                            no_internet();
                        } else {
                            Log.e("in", "else");
                            something_wrong();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {
                //ringProgressDialog.dismiss();
                if (!appUtils.isConnected(InvoiceActivity.this)) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                Toast.makeText(InvoiceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("acc_id",mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(this)) {
           // ringProgressDialog.dismiss();
            ll_Data_Found.setVisibility(View.GONE);
            ll_NoData_Found.setVisibility(View.VISIBLE);
            retry_btn.setVisibility(View.VISIBLE);
            img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
            no_data_text.setText(getResources().getString(R.string.no_internet));
            //  Custom_Toast.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            ll_Data_Found.setVisibility(View.VISIBLE);
            ll_NoData_Found.setVisibility(View.GONE);
            invoiceHistory();
        }
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }

    public void no_internet() {
        ll_Data_Found.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        no_data_text.setText(getResources().getString(R.string.no_internet));
    }

    public void something_wrong() {
        ll_Data_Found.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
        no_data_text.setText(getResources().getString(R.string.something_wrong));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PaymentActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == PaymentActivity.RESULT_OK) {
                // Log.d(TAG, "Result Code :" + RESULT_OK);
                System.out.println( "resultCode :" + resultCode);
                if (data != null) {
                    try {
                        Checkout checkout_res = (Checkout) data.getSerializableExtra(Constant.ARGUMENT_DATA_CHECKOUT);
//                        System.out.println("Checkout Response Obj onActivityResult" + checkout_res.getMerchantResponsePayload().toString());
                        String transactionSubType = checkout_res.getMerchantRequestPayload().getTransaction().getSubType();
                        System.out.println("Payment type => " + transactionSubType);
//
                        statusCode=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode();
                        System.out.println("StatusCode"+statusCode);
                        amount=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount();
                        System.out.println("amount"+amount);

                        dateTime=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getDateTime();
                        System.out.println("dateTime"+dateTime);

                        merchantTransactionIdentifier=checkout_res.getMerchantResponsePayload().getMerchantTransactionIdentifier();
                        System.out.println("merchantTransactionIdentifier"+merchantTransactionIdentifier);

                        identifier=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getIdentifier();
                        System.out.println("identifier"+identifier);

                        bankSelectionCode=checkout_res.getMerchantResponsePayload().getPaymentMethod().getBankSelectionCode();
                        System.out.println("bankSelectionCode"+bankSelectionCode);

                        errorMessage = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getErrorMessage();
                        System.out.println("errorMessage" + errorMessage);

                        bankReferenceIdentifier=checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getBankReferenceIdentifier();
                        System.out.println("bankReferenceIdentifier"+bankReferenceIdentifier);
                        //Transaction Completed and Got SUCCESS
                        //SALES_DEBIT
                        if (statusCode.equalsIgnoreCase(PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                            Toast.makeText(getApplicationContext(), "Your Transaction Status - Payment Successful", Toast.LENGTH_SHORT).show();
                            statusMessage = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusMessage();
                            System.out.println("statusMessage" + statusMessage);
                            sendDataToServer();
                            System.out.println("sendDataToServer");
                        }
                        else {

                            statusMessage = checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusMessage();
                            System.out.println("statusMessage else fail" + statusMessage);

                            sendDataToServer();
                            System.out.println("Bank Error Failure");
                            // some error from bank side
                            Log.v("TRANSACTION STATUS=>", "FAILURE");
                            Toast.makeText(getApplicationContext(),"Transaction Status - Payment Unsuccessful" , Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == PaymentActivity.RESULT_ERROR) {
                Log.d(TAG, "got an error"+PaymentActivity.RESULT_ERROR);
                if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) && data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                    String error_code = (String) data.getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                    String error_desc = (String) data.getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);
                    Toast.makeText(getApplicationContext(), " Got error :" + error_code + "--- " + error_desc, Toast.LENGTH_LONG).show();
                    Log.d(TAG + " Code=>", error_code);
                    Log.d(TAG + " Desc=>", error_desc);
                }
            }
            else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Your Transaction Aborted by User", Toast.LENGTH_LONG).show();
                Log.d(TAG, "User pressed back button"+PaymentActivity.RESULT_CANCELED);
            }
        }
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
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
                   date = RequestBody.create(MultipartBody.FORM, da);
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
                    itemMap.put("paydate", date);
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
                            }
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
                        Toast.makeText(InvoiceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        }).start();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // System.out.println("Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //   System.out.println("Potrait");
        }
    }
}