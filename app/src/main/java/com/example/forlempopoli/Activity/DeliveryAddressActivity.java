package com.example.forlempopoli.Activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Adapter.CityAdapter;
import com.example.forlempopoli.Adapter.CountryAdapter;
import com.example.forlempopoli.Adapter.StateAdapter;
import com.example.forlempopoli.Db.connection.DatabaseHelper;
import com.example.forlempopoli.Db.connection.ITable;
import com.example.forlempopoli.Entity.request.BillingRequest;
import com.example.forlempopoli.Entity.request.CityRequest;
import com.example.forlempopoli.Entity.request.CountryRequest;
import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;
import com.example.forlempopoli.Entity.request.E_ClothOrderDetail;
import com.example.forlempopoli.Entity.request.InsertOrderRequest;
import com.example.forlempopoli.Entity.request.SpinnerBankBillingData;
import com.example.forlempopoli.Entity.request.StateRequest;
import com.example.forlempopoli.Entity.response.BillingResponse;
import com.example.forlempopoli.Entity.response.E_OrderPlaceResponse;
import com.example.forlempopoli.Entity.response.SpinnerBankBillingResponse;
import com.example.forlempopoli.Entity.response.UpdateBillingResponse;
import com.example.forlempopoli.Fragment.DeliveryAddressFragment;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.MainActivity;
import com.example.forlempopoli.Model.M_Clothes_Order_Details;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.GSTValidation;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddressActivity extends AppCompatActivity {
    Button btnSave;
    int i;
    MaterialButton editView, changeView, doneView;
    RecyclerView rv_delivery_addr;
    List<DeliveryAddressRequest> deliveryAddressRequestList;
    Spinner spCity, spState, spCountry;
    List<BillingRequest> billingRequestList;
    String cityname, statename, countryname, customertype, gst_no, pincode, billing_addr;
    String cityid, stateid, countryid;
    String selectedCityId, selectedStateId, selectedCountryId;
    String addr, pin, gst;
    TextView tvCustomerType;
    SpinnerBankBillingData spinnerBankBillingData;
    CityAdapter customCityAdapter;
    StateAdapter stateAdapter;
    CountryAdapter countryAdapter;
    BillingRequest billingRequest;
    //   TextView tvbillingDetails;
    TextView grand_total_cart;
    String selectedCityName = "Please Select City",
            selectedStateName = "Please Select State",
            selectedCountryName = "Please Select Country";
    LinearLayout deliveryConfirmBtn;
    EditText etBillingAddr, etPincode, gstNo;
    TextView tv_address, tv_city, tv_state, tv_country, tv_gst;
    TextInputLayout inputAddress, inputGST, inputpincode;
    AppUtils appUtils = new AppUtils();
    CardView ll_frame;
    MaterialToolbar toolbar;
    private AppSharedPreference mAppSharedPrefernce;
    private List<CityRequest> cityRequestList;
    private List<StateRequest> stateRequestList;
    private List<CountryRequest> countryRequestList;
    private DatabaseHelper mDatabaseHelper;

    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_delivery);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(getApplicationContext());
        // setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Delivery Address Details</small></font>"));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confirm Delivery");
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        appUtils = new AppUtils();

        ll_frame = findViewById(R.id.ll_frame);
        doneView = findViewById(R.id.doneView);
        editView = findViewById(R.id.editView);
        changeView = findViewById(R.id.changeView);
        rv_delivery_addr = findViewById(R.id.rv_delivery_addr);
        etBillingAddr = findViewById(R.id.etBillingAddr);
        tvCustomerType = findViewById(R.id.tvCustomerType);
        etPincode = findViewById(R.id.etPincode);
        spState = findViewById(R.id.spState);
        spCountry = findViewById(R.id.spCountry);
        spCity = findViewById(R.id.spCity);
        gstNo = findViewById(R.id.gstNo);
        btnSave = findViewById(R.id.saveView);
        grand_total_cart = findViewById(R.id.grand_total_cart);

        tv_address = findViewById(R.id.tv_address);
        tv_city = findViewById(R.id.tv_city);
        tv_state = findViewById(R.id.tv_state);
        tv_country = findViewById(R.id.tv_country);
        tv_gst = findViewById(R.id.tv_gst);

        inputAddress = findViewById(R.id.inputetaddress);
        inputpincode = findViewById(R.id.inputetpincode);
        inputGST = findViewById(R.id.inputetgst);

        //tvbillingDetails = findViewById(R.id.tvbillingDetails);
        deliveryConfirmBtn = findViewById(R.id.deliveryConfirmBtn);
        //  tvbillingDetails.setPaintFlags(tvbillingDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Intent intent = getIntent();
//        Log.e("total_amt", intent.getStringExtra("total_amt"));
        grand_total_cart.setText("₹" + intent.getStringExtra("total_amt"));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        DeliveryAddressFragment deliveryAddressFragment = new DeliveryAddressFragment();
        mFragmentTransaction.add(R.id.frame, deliveryAddressFragment);
        mFragmentTransaction.commit();
        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());

        ll_frame.setVisibility(View.VISIBLE);

        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(DeliveryAddressActivity.this)) {
                    Toasty.warning(DeliveryAddressActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    setFieldsEnable();
                }
            }
        });

        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(DeliveryAddressActivity.this)) {
                    Toasty.warning(DeliveryAddressActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    ll_frame.setVisibility(View.VISIBLE);
                    changeView.setVisibility(View.GONE);
                    doneView.setVisibility(View.VISIBLE);
                }
            }
        });
        doneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(DeliveryAddressActivity.this)) {
                    Toasty.warning(DeliveryAddressActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    ll_frame.setVisibility(View.GONE);
                    changeView.setVisibility(View.VISIBLE);
                    doneView.setVisibility(View.GONE);
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(DeliveryAddressActivity.this)) {
                    Toasty.warning(DeliveryAddressActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    try {
                        System.out.println("updateBankBillingdetails");
                        if (!validateaddress() | !validatepincode() /*| !validateGST()*/) {
                            return;
                        }
                        updateBillingDetails();
                    } catch (Exception e) {
                        Log.e("registerError", e.getLocalizedMessage());
                    }
                }
            }
        });

        if (!appUtils.isConnected(DeliveryAddressActivity.this)) {
            Toasty.warning(DeliveryAddressActivity.this, getResources().getString(R.string.no_internet));

        } else {
            getBillingdetailsSpinner();
            setFieldsDisable();
            setListenersForSpinners();
        }

        deliveryConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(DeliveryAddressActivity.this)) {
                    Toasty.warning(DeliveryAddressActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    if (mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID).isEmpty()) {
                        System.out.println("order add " + mAppSharedPrefernce.getString
                                (Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));
                        Custom_Toast.warning(DeliveryAddressActivity.this, "Please Select Delivery Address");

                    } else {
                        try {

                            sendOrderDetails();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("else order add " + mAppSharedPrefernce.getString
                                (Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));
                        System.out.println("else order add ");
                    }

                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //DETERMINE WHO STARTED THIS ACTIVITY
        /*final String sender=this.getIntent().getExtras().getString("SENDER_KEY");

        //IF ITS THE FRAGMENT THEN RECEIVE DATA
        if(sender != null)
        {
            this.receiveData();
            Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();

        }*/
    }

    /*
       RECEIVE DATA FROM FRAGMENT
        */
    private void receiveData() {
        //RECEIVE DATA VIA INTENT
       /* Intent i = getIntent();
        String name = i.getStringExtra("NAME_KEY");
//        int year = i.getIntExtra("YEAR_KEY",0);

        if(name!= null) {
            //SET DATA TO TEXTVIEWS
            Log.e("Receivedname", name);
        }*/
    }

    public boolean validateaddress() {
        String address = etBillingAddr.getText().toString().trim();
        if (address.isEmpty()) {
            inputAddress.setError("Please Enter Address");
            return false;
        } else {
            inputAddress.setError(null);
            inputAddress.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatepincode() {
        String pincode = etPincode.getText().toString().trim();
        if (pincode.isEmpty()) {
            inputpincode.setError("Please Enter Pincode");
            return false;
        } else {
            inputpincode.setError(null);
            inputpincode.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateGST() throws Exception {
        String gst = gstNo.getText().toString().trim();
        if (gst.isEmpty()) {
            inputGST.setError("Please Enter GST Number");
            return false;
        } else if (!GSTValidation.validGSTIN(gst)) {
            inputGST.setError("Please Enter Valid GST Number");
            return false;
        } else {
            inputGST.setError(null);
            inputGST.setErrorEnabled(false);
            return true;
        }
    }

    private void sendOrderDetails() throws JSONException {
        final List<M_Clothes_Order_Details> e_clothOrderDetailList =
                mDatabaseHelper.getAllClothItems(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));

        if (e_clothOrderDetailList.isEmpty()) {
            Toast.makeText(DeliveryAddressActivity.this, "No Item In Cart", Toast.LENGTH_SHORT).show();
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
                if (e_clothOrderDetailList.get(i).getArt_offer_price() != 0)
                    clothOrderDetail.setSellingPrice(String.valueOf(e_clothOrderDetailList.get(i).getArt_offer_price()));
                else {
                    clothOrderDetail.setSellingPrice(String.valueOf(e_clothOrderDetailList.get(i).getArt_selling_price_amt()));
                }
//                clothOrderDetail.setOfferprice(String.valueOf(e_clothOrderDetailList.get(i).getArt_offer_price()));
                orderDetailList.add(clothOrderDetail);

            }
            final InsertOrderRequest minsertOrderRequest = new InsertOrderRequest();
            minsertOrderRequest.setAPIACCESSKEY("ZkC6BDUzxz");
            minsertOrderRequest.setClientId(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            minsertOrderRequest.setOrderData(orderDetailList);
            minsertOrderRequest.setDelivery_addr_id(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));

            final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
            Call<E_OrderPlaceResponse> call = apiInterface.getClothsOrderDetails(minsertOrderRequest);//mJsonObject
            call.enqueue(new Callback<E_OrderPlaceResponse>() {
                @Override
                public void onResponse(Call<E_OrderPlaceResponse> call, Response<E_OrderPlaceResponse> response) {
                    System.out.println("Response " + response.body().toString());
                    if (response.code() == 200 && response.message().equals("OK")) {
                        if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {

                            for (M_Clothes_Order_Details orderDetails : e_clothOrderDetailList) {
                                ContentValues mContentValues = new ContentValues();
                                mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED, "1");
                                mDatabaseHelper.updateProductPlaceStatus(mContentValues,
                                        String.valueOf(orderDetails.getArt_id()), mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                            }
                            Toast.makeText(getApplicationContext(), "Order Successfully Placed", Toast.LENGTH_LONG).show();
//                            invalidateOptionsMenu();
                            //navigateToOrderActivity();
                            Intent i = new Intent(DeliveryAddressActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    } else {
                        if (response.code() == 404 && response.message().equals("Not Found")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DeliveryAddressActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<E_OrderPlaceResponse> call, Throwable t) {
                    Toast.makeText(DeliveryAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("DeliveryAddress onFailure" + t.getMessage());
                    //ringProgressDialog.dismiss();
                }
            });
        }
    }

    public void DeliveryAddressActivity(int i) {
        this.i = i;
    }

    private void setFieldsDisable() {
        etBillingAddr.setEnabled(false);
        tvCustomerType.setEnabled(false);
        spCity.setEnabled(false);
        spState.setEnabled(false);
        spCountry.setEnabled(false);
        etPincode.setEnabled(false);
        gstNo.setEnabled(false);

        tv_address.setVisibility(View.VISIBLE);
        tv_city.setVisibility(View.VISIBLE);
        tv_state.setVisibility(View.VISIBLE);
        tv_country.setVisibility(View.GONE);

        spCity.setVisibility(View.GONE);
        spCountry.setVisibility(View.GONE);
        spState.setVisibility(View.GONE);
        etPincode.setVisibility(View.GONE);
        etBillingAddr.setVisibility(View.GONE);
        gstNo.setVisibility(View.GONE);
        tv_gst.setVisibility(View.VISIBLE);

        inputpincode.setVisibility(View.GONE);
        inputAddress.setVisibility(View.GONE);
        inputGST.setVisibility(View.GONE);

        editView.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
    }

    private void setFieldsEnable() {
        gstNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_country.setVisibility(View.GONE);

        spCity.setVisibility(View.VISIBLE);
        spCountry.setVisibility(View.VISIBLE);
        spState.setVisibility(View.VISIBLE);
        inputpincode.setVisibility(View.VISIBLE);
        inputAddress.setVisibility(View.VISIBLE);
        inputGST.setVisibility(View.VISIBLE);
        tv_gst.setVisibility(View.GONE);


        etPincode.setVisibility(View.VISIBLE);
        etBillingAddr.setVisibility(View.VISIBLE);
        gstNo.setVisibility(View.VISIBLE);

        etBillingAddr.setEnabled(true);
        spCity.setEnabled(true);
        spState.setEnabled(true);
        spCountry.setEnabled(true);
        etPincode.setEnabled(true);
        gstNo.setEnabled(false);
        editView.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
    }

    public void showBillingdetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<BillingResponse> call = apiInterface.showCustomerbillingAddr(getMap());
        call.enqueue(new Callback<BillingResponse>() {
            @Override
            public void onResponse(Call<BillingResponse> call, Response<BillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        //ringProgressDialog.dismiss();
                        billingRequestList = response.body().getData();
                        if (billingRequestList != null && !billingRequestList.isEmpty()) {

                            gst_no = billingRequestList.get(i).getAccGst();
                            gstNo.setText(gst_no);
                            tv_gst.setText(gst_no);

                            pincode = billingRequestList.get(i).getAccPinCode();
                            etPincode.setText(pincode);

                            billing_addr = billingRequestList.get(i).getAccAddress();
                            etBillingAddr.setText(billing_addr);
                            tv_address.setText(billing_addr);

                            cityname = billingRequestList.get(i).getCityName();
                            System.out.println("cityname :" + cityname);
                            cityid = billingRequestList.get(i).getAccCityId();

                            tv_city.setText(cityname + " " + pincode);

                            statename = billingRequestList.get(i).getStateName();
                            stateid = billingRequestList.get(i).getAccStateId();

                            countryname = billingRequestList.get(i).getCountryName();
                            countryid = billingRequestList.get(i).getAccCountryId();

                            tv_state.setText(statename + ", " + countryname);

                            customertype = billingRequestList.get(i).getAccTypeName();
                            System.out.println("customertype" + customertype);
                            tvCustomerType.setText(customertype);

                            setSelectedValuesForSpinner();
                            setSelectedValueForCountrySpinner();
                            setSelectedValueForStateSpinner();
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BillingResponse> call, Throwable t) {

            }
        });
    }

    public void backalertBox() {
        new MaterialAlertDialogBuilder(DeliveryAddressActivity.this, R.style.MyMaterialAlertDialog)
                .setTitle(getResources().getString(R.string.want_to_cancel))
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAppSharedPrefernce.logoutUser(DeliveryAddressActivity.this);
                        Intent intent = new Intent(DeliveryAddressActivity.this, CartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("NO", /* listener = */ null)
                .show();

    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    private void setSelectedValuesForSpinner() {
        spCity.setSelection(getSelctedCityPosition());
    }

    private int getSelctedCityPosition() {
        int selectedPosition = 0;
        if (cityRequestList != null) {
            for (int i = 0; i < cityRequestList.size(); i++) {
                if (cityRequestList.get(i).getCityId().equals(cityid)) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        return selectedPosition;
    }

    private void setSelectedValueForCountrySpinner() {
        spCountry.setSelection(getSelctedCountryPosition());
    }

    private int getSelctedCountryPosition() {
        int selectedPosition = 0;
        if (countryRequestList != null) {
            for (int i = 0; i < countryRequestList.size(); i++) {
                if (countryRequestList.get(i).getCountryId().equals(countryid)) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        return selectedPosition;
    }

    private void setSelectedValueForStateSpinner() {
        spState.setSelection(getSelctedStatePosition());
    }

    private int getSelctedStatePosition() {
        int selectedPosition = 0;
        if (stateRequestList != null) {
            for (int i = 0; i < stateRequestList.size(); i++) {
                if (stateRequestList.get(i).getStateId().equals(stateid)) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        return selectedPosition;
    }

    public void getBillingdetailsSpinner() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerBankBillingResponse> call = apiInterface.getBankBillingDetailsSpinner();
        call.enqueue(new Callback<SpinnerBankBillingResponse>() {
            @Override
            public void onResponse(Call<SpinnerBankBillingResponse> call, Response<SpinnerBankBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        spinnerBankBillingData = response.body().getData();

                        CountryRequest mCountryRequest = new CountryRequest();
                        mCountryRequest.setCountryName("Please Select Country");
                        mCountryRequest.setCountryId("0");

                        StateRequest stateRequest = new StateRequest();
                        stateRequest.setStateId("0");
                        stateRequest.setStateName("Please Select State");

                        CityRequest cityRequest = new CityRequest();
                        cityRequest.setCityId("0");
                        cityRequest.setCityName("Please Select City");

                        cityRequestList = spinnerBankBillingData.getCity();
                        stateRequestList = spinnerBankBillingData.getState();
                        countryRequestList = spinnerBankBillingData.getCountry();

                        countryRequestList.add(0, mCountryRequest);
                        stateRequestList.add(0, stateRequest);
                        cityRequestList.add(0, cityRequest);

                        setAdaptersForSpinners();

                    } else {
                        if (response.body().getStatusCode() == 0) {
                            if (response.body().getStatusMessage().equals("Fail")) {
                                Toast.makeText(getApplicationContext(), "Wrong enter details!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SpinnerBankBillingResponse> call, Throwable t) {

            }
        });
    }

    private void setAdaptersForSpinners() {
        if (getApplicationContext() != null) {
            customCityAdapter = new CityAdapter(getApplicationContext(), cityRequestList);
            spCity.setAdapter(customCityAdapter);

            stateAdapter = new StateAdapter(getApplicationContext(), stateRequestList);
            spState.setAdapter(stateAdapter);

            countryAdapter = new CountryAdapter(getApplicationContext(), countryRequestList);
            spCountry.setAdapter(countryAdapter);
        }
        showBillingdetails();
    }

    private void setListenersForSpinners() {
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCityId = cityRequestList.get(i).getCityId();
                System.out.println("Spinner DeliveryAddress City Id : " + selectedCityId);
                selectedCityName = cityRequestList.get(i).getCityName();
                System.out.println("Spinner DeliveryAddress City Name : " + selectedCityName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedStateId = stateRequestList.get(pos).getStateId();
                selectedStateName = stateRequestList.get(pos).getStateName();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCountryId = countryRequestList.get(pos).getCountryId();
                selectedCountryName = countryRequestList.get(pos).getCountryName();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateBillingDetails() {
        billingRequest = new BillingRequest();
        billingRequest.setCityName(selectedCityName);
        billingRequest.setStateName(selectedStateName);
        billingRequest.setCountryName(selectedCountryName);

        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<UpdateBillingResponse> call = apiInterface.updateCustomerbillingAddr(getUpdateHashMap());
        call.enqueue(new Callback<UpdateBillingResponse>() {
            @Override
            public void onResponse(Call<UpdateBillingResponse> call, Response<UpdateBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    System.out.println("Success" + response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg = response.body().getMessage();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        setFieldsDisable();
                        showBillingdetails();
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateBillingResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, Object> getUpdateHashMap() {
        gst = gstNo.getText().toString();
        pin = etPincode.getText().toString();
        addr = etBillingAddr.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("acc_gst", gst);
        map.put("acc_pin_code", pin);
        map.put("acc_address", addr);
        map.put("acc_city_id", selectedCityId);
        map.put("acc_state_id", selectedStateId);
        map.put("acc_country_id", selectedCountryId);
        map.put("acc_type", customertype);
        return map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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