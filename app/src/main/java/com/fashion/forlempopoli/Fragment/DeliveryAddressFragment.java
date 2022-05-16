package com.fashion.forlempopoli.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fashion.forlempopoli.Adapter.CityAdapter;
import com.fashion.forlempopoli.Adapter.CountryAdapter;
import com.fashion.forlempopoli.Adapter.DeliveryAddressAdapter;
import com.fashion.forlempopoli.Adapter.StateAdapter;
import com.fashion.forlempopoli.Entity.request.CityRequest;
import com.fashion.forlempopoli.Entity.request.CountryRequest;
import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;
import com.fashion.forlempopoli.Entity.request.SpinnerBankBillingData;
import com.fashion.forlempopoli.Entity.request.StateRequest;
import com.fashion.forlempopoli.Entity.response.AddDeliveryResponse;
import com.fashion.forlempopoli.Entity.response.DeliveryAddressResponse;
import com.fashion.forlempopoli.Entity.response.SpinnerBankBillingResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.Listener.OnItemEditedListener;
import com.fashion.forlempopoli.Listener.OpenEditDialogListener;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.Custom_Toast;
import com.fashion.forlempopoli.Utilities.PatternClass;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddressFragment extends Fragment /*implements OnRadioItemCheckListener*/ {
    DeliveryAddressAdapter deliveryAddressAdapter;
    Button buttonAdd;
    public RecyclerView rv_delivery_addr;
    List<DeliveryAddressRequest> deliveryAddressRequestList;
    Map<String,DeliveryAddressRequest> deleiveryRequestListSelected=new HashMap<>();
    AddDeliveryAddressDialog addDeliveryAddressDialog;
    Edit_Delivery_address_Dialog delivery_address_dialog;
    LinearLayout ll_add_address;
    MaterialButton save_btn, cancel_btn;
    TextInputEditText etName, etMobileNo, etDeliveryAddress, etPincode;
    TextInputLayout inputetName, inputetMobileNo, inputetDeliveryAddress, inputetPincode;
    AutoCompleteTextView spnCity, spnState, spnCountry;
    TextInputLayout inputspnCity, inputspnState, inputspnCountry;
    String selectedCityId = "", selectedStateId = "", selectedCountryId = "";
    String cityname, statename, countryname, acct_name, acct_mob, acct_pin_code, acct_address;
    int i;
    DeliveryAddressRequest deliveryAddressRequest;
    String selectedCityName, selectedStateName, selectedCountryName;
    AppUtils appUtils;
    private AppSharedPreference mAppSharedPrefernce;
    private List<CityRequest> cityRequestList;
    private List<StateRequest> stateRequestList;
    private List<CountryRequest> countryRequestList;

    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    int selectedPosition = 0;
    ProgressDialog ringProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_address, container, false);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(getActivity());
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);

        appUtils = new AppUtils();

        ll_add_address = view.findViewById(R.id.ll_add_address);
        rv_delivery_addr = view.findViewById(R.id.rv_delivery_addr);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        no_data_text = view.findViewById(R.id.no_data_text);
        ll_Data_Found = view.findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = view.findViewById(R.id.ll_NoData_Found);
        img_info = view.findViewById(R.id.img_info);
        retry_btn = view.findViewById(R.id.retry_btn);

        inputetName = view.findViewById(R.id.inputName);
        inputetMobileNo = view.findViewById(R.id.inputNMobile);
        inputetDeliveryAddress = view.findViewById(R.id.inputAddress);
        inputetPincode = view.findViewById(R.id.inputPincode);


        etName = view.findViewById(R.id.etName);
        etMobileNo = view.findViewById(R.id.etMobileNo);
        etDeliveryAddress = view.findViewById(R.id.etDeliveryAddress);
        etPincode = view.findViewById(R.id.etPincode);

        spnCity = view.findViewById(R.id.spnCity);
        spnState = view.findViewById(R.id.spnState);
        spnCountry = view.findViewById(R.id.spnCountry);

        inputspnCity = view.findViewById(R.id.inputspnCity);
        inputspnState = view.findViewById(R.id.inputspnState);
        inputspnCountry = view.findViewById(R.id.inputspnCountry);

        save_btn = view.findViewById(R.id.save_btn);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        buttonAdd = view.findViewById(R.id.buttonAdd);

//        buttonAdd.setPaintFlags(buttonAdd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ll_add_address.setVisibility(View.GONE);

        if (!appUtils.isConnected(getActivity())) {
            Toasty.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            getSpinner();
        }
        ringProgressDialog = new ProgressDialog(getActivity());
        ringProgressDialog.setMessage("Loading...");
        ringProgressDialog.setCanceledOnTouchOutside(false);
        ringProgressDialog.show();

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

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(getContext())) {
                    Toasty.warning(getContext(), getResources().getString(R.string.no_internet));
                } else {
                    if (!validateName() | ! validateAddress() | !validateMobile()| !validatePincode()
                            | !validateCity() | !validateState() | !validateCountry()) {
                        return;
                    }

                    insertAddressDetails();

                }

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_add_address.setVisibility(View.GONE);
                clearFeilds();
                buttonAdd.setVisibility(View.VISIBLE);
            }
        });



        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*addDeliveryAddressDialog=new AddDeliveryAddressDialog();
                addDeliveryAddressDialog.show(getParentFragmentManager(),"Dialog");
                addDeliveryAddressDialog.setOnItemInsertedListener(new OnItemInsertedListener() {
                    @Override
                    public void onItemInserted(DeliveryAddressRequest deliveryAddressRequest, int position) {
                        //System.out.println("account Name : "+deliveryAddressRequest.getAcctName());
                        if(deliveryAddressRequest!=null) {
                            deliveryAddressRequestList.add(position,deliveryAddressRequest);
                            deliveryAddressAdapter.notifyItemInserted(position);
                       }
                        alertbox();
                    }
                });*/
                ll_add_address.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);
            }
        });

        /*if (!appUtils.isConnected(getContext())) {
            Toasty.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            deliveryaddressDetails();
        }
         */

        /*rv_delivery_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton = view.findViewById(R.id.default_address);

            }
        });*/
        return view;
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(getContext())) {
            ringProgressDialog.dismiss();
            ll_Data_Found.setVisibility(View.GONE);
            ll_NoData_Found.setVisibility(View.VISIBLE);
            retry_btn.setVisibility(View.VISIBLE);
            img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
            no_data_text.setText(getResources().getString(R.string.no_internet));
            //  Custom_Toast.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            ll_Data_Found.setVisibility(View.VISIBLE);
            ll_NoData_Found.setVisibility(View.GONE);
            deliveryaddressDetails();
        }
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }


    public boolean validateName() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
//            Custom_Toast.warning(getContext(), "City");
            inputetName.setError("Please Enter Name");
            return false;
        } else {
            inputetName.setError(null);
            inputetName.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateAddress() {
        String add = etDeliveryAddress.getText().toString().trim();
        if (add.isEmpty()) {
//            Custom_Toast.warning(getContext(), "City");
            inputetDeliveryAddress.setError("Please Enter Address");
            return false;
        } else {
            inputetDeliveryAddress.setError(null);
            inputetDeliveryAddress.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePincode() {
        String pin = etPincode.getText().toString().trim();
        if (pin.isEmpty()) {
//            Custom_Toast.warning(getContext(), "City");
            inputetPincode.setError("Please Enter Address");
            return false;
        } else {
            inputetPincode.setError(null);
            inputetPincode.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateMobile() {
        String mob = etMobileNo.getText().toString().trim();
        if (mob.isEmpty()) {
//            Custom_Toast.warning(getContext(), "City");
            inputetMobileNo.setError("Please Enter Mobile Number");
            return false;
        }
        else if (!PatternClass.isValidPhone(mob)) {
//            Custom_Toast.warning(getContext(), "City");
            inputetMobileNo.setError("Please Enter Mobile Number");
            return false;
        } else {
            inputetMobileNo.setError(null);
            inputetMobileNo.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateCity() {
        if (selectedCityId.equals("0")) {
            Custom_Toast.warning(getContext(), "City");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateState() {
        if (selectedStateId.equals("0")) {
            Custom_Toast.warning(getContext(), "StateId");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateCountry() {
        if (selectedCountryId.equals("0")) {
            Custom_Toast.warning(getContext(), "Country");
            return false;
        } else {
            return true;
        }
    }

    public void alertbox() {
        AlertDialog alertDialogBuilder = new AlertDialog.Builder(getActivity()).create();
        alertDialogBuilder.setTitle("SuccessFully Added!");
        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_check_circle_24));
        alertDialogBuilder.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deliveryaddressDetails();
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
        return;
    }

    public void deliveryaddressDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<DeliveryAddressResponse> call = apiInterface.showDeliveryAddr(getHashMap());
        call.enqueue(new Callback<DeliveryAddressResponse>() {
            @Override
            public void onResponse(Call<DeliveryAddressResponse> call, Response<DeliveryAddressResponse> response) {
                ringProgressDialog.dismiss();
                if (response.code() == 200 && response.message().equals("OK")) {
                    System.out.println("Success" + response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        deliveryAddressRequestList = response.body().getData();

                        Log.d("deliveryAddressList",deliveryAddressRequestList.toString());
                        if (!deliveryAddressRequestList.isEmpty()) {
                            setAdapter();

                        }
                        else {
                            ll_Data_Found.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                           // Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            if (!appUtils.isConnected(getContext())) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        if (!appUtils.isConnected(getContext())) {
                            no_internet();
                        } else {
                            Log.e("in", "else");
                            something_wrong();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryAddressResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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

    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    private void setAdapter() {
        ll_Data_Found.setVisibility(View.VISIBLE);
        ll_NoData_Found.setVisibility(View.GONE);
        deliveryAddressAdapter = new DeliveryAddressAdapter(deliveryAddressRequestList, getActivity(),
                getChildFragmentManager());
        rv_delivery_addr.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_delivery_addr.setAdapter(deliveryAddressAdapter);

        deliveryAddressAdapter.onOpenEditDialogListener(new OpenEditDialogListener() {
            @Override
            public void onEditDialog(DeliveryAddressRequest mdeliveryAddressRequest, int position) {
                if (!appUtils.isConnected(getContext())) {
                    Toasty.warning(getContext(), getResources().getString(R.string.no_internet));
                } else {
                    delivery_address_dialog = new Edit_Delivery_address_Dialog(deliveryAddressRequestList.get(position), position);
                    delivery_address_dialog.show(getParentFragmentManager(), "MyDialog");

                    delivery_address_dialog.setOnItemEditedListener(new OnItemEditedListener() {
                        @Override
                        public void onItemEdited(DeliveryAddressRequest data, int position) {
                            Log.d("delivery dialog Item", " Edited Item" + position);
                            deliveryAddressRequestList.set(position, data);
                            deliveryAddressAdapter.notifyItemChanged(position);
                        }
                    });
                }
            }
        });
    }

    private void setcityAddapter() {
        if (getActivity() != null) {
            CityAdapter customCityAdapter1 = new CityAdapter(getActivity(), cityRequestList);
            spnCity.setAdapter(customCityAdapter1);
            setCityObatainedToSpinner();
        }
    }

    private void setstateAddapter() {
        if (getActivity() != null) {
            StateAdapter stateAdapter1 = new StateAdapter(getActivity(), stateRequestList);
            spnState.setAdapter(stateAdapter1);
            setStateObatainedToSpinner();
        }
    }

    private void setcountryAddapter() {
        if (getActivity() != null) {
            CountryAdapter countryAdapter1 = new CountryAdapter(getActivity(), countryRequestList);
            spnCountry.setAdapter(countryAdapter1);
            setCountryObatainedToSpinner();
        }
    }

    public void getSpinner() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerBankBillingResponse> call = apiInterface.getBankBillingDetailsSpinner();
        call.enqueue(new Callback<SpinnerBankBillingResponse>() {
            @Override
            public void onResponse(Call<SpinnerBankBillingResponse> call, Response<SpinnerBankBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        SpinnerBankBillingData spinnerBankBillingData = response.body().getData();
                        cityRequestList = spinnerBankBillingData.getCity();
                        CityRequest mCityRequest = new CityRequest();
                        mCityRequest.setCityId("0");
                        mCityRequest.setCityName("Please Select City Name");
                        cityRequestList.add(0, mCityRequest);
                        setcityAddapter();
                       /* spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedCityId = cityRequestList.get(pos).getCityId();
                                selectedCityName = cityRequestList.get(pos).getCityName();
                                spnCity.setText(selectedCityName);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                        spnCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                selectedCityId = cityRequestList.get(pos).getCityId();
                                selectedCityName = cityRequestList.get(pos).getCityName();
                                spnCity.setText(selectedCityName);
                            }
                        });
                        stateRequestList = spinnerBankBillingData.getState();
                        StateRequest mstateRequest = new StateRequest();
                        mstateRequest.setStateId("0");
                        mstateRequest.setStateName("Please Select State Name");
                        stateRequestList.add(0, mstateRequest);
                        setstateAddapter();
                        /*spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedStateId = stateRequestList.get(pos).getStateId();
                                selectedStateName = stateRequestList.get(pos).getStateName();
                                spnState.setText(selectedStateName);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                        spnState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                selectedStateId = stateRequestList.get(pos).getStateId();
                                selectedStateName = stateRequestList.get(pos).getStateName();
                                spnState.setText(selectedStateName);
                            }
                        });
                        countryRequestList = spinnerBankBillingData.getCountry();
                        CountryRequest mcountryRequest = new CountryRequest();
                        mcountryRequest.setCountryId("0");
                        mcountryRequest.setCountryName("Please Select Country Name");
                        countryRequestList.add(0, mcountryRequest);
                        setcountryAddapter();
                        /*spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedCountryId = countryRequestList.get(pos).getCountryId();
                                selectedCountryName = countryRequestList.get(pos).getCountryName();
                                spnCountry.setText(selectedCountryName);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/
                        spnCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                selectedCountryId = countryRequestList.get(pos).getCountryId();
                                selectedCountryName = countryRequestList.get(pos).getCountryName();
                                spnCountry.setText(selectedCountryName);
                            }
                        });
                    } else {
                        if (response.body().getStatusCode() == 0) {
                            if (response.body().getStatusMessage().equals("Fail")) {
                                Toast.makeText(getActivity(), "Wrong enter details!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SpinnerBankBillingResponse> call, Throwable t) {

            }
        });
    }

    private void setCityObatainedToSpinner() {
        for (int i = 0; i < cityRequestList.size(); i++) {
            if (cityname != null) {
                if (cityname.equals(cityRequestList.get(i).getCityName()))
                    System.out.println("bank spinner city:" + cityname);
                {
                    selectedCityId = cityRequestList.get(i).getCityId();
                    selectedCityName = cityRequestList.get(i).getCityName();
                    spnCity.setText(selectedCityName);

                }
            }
        }
    }

    private void setStateObatainedToSpinner() {
        for (int i = 0; i < stateRequestList.size(); i++) {
            if (statename != null) {
                if (statename.equals(stateRequestList.get(i).getStateName())) {
                    int position = i;
                    selectedStateId = stateRequestList.get(i).getStateId();
                    selectedStateName = stateRequestList.get(i).getStateName();
                    spnState.setText(selectedStateName);
                }
            }
        }
    }

    private void setCountryObatainedToSpinner() {
        for (int i = 0; i < countryRequestList.size(); i++) {
            if (countryname != null) {
                if (countryname.equals(countryRequestList.get(i).getCountryName())) {
                    int position = i;
                    selectedCountryId = countryRequestList.get(i).getCountryId();
                    selectedCountryName = countryRequestList.get(i).getCountryName();
                    spnCountry.setText(selectedCountryName);
                }
            }

        }
    }

    public void clearFeilds() {
        etName.setText(null);
        etMobileNo.setText(null);
        etPincode.setText(null);
        etDeliveryAddress.setText(null);
        spnCity.setSelection(0);
        spnCountry.setSelection(0);
        spnState.setSelection(0);
    }

    public void insertAddressDetails() {
        acct_name = etName.getText().toString();
        acct_mob = etMobileNo.getText().toString();
        acct_address = etDeliveryAddress.getText().toString();
        acct_pin_code = etPincode.getText().toString();

        deliveryAddressRequest = new DeliveryAddressRequest();
        deliveryAddressRequest.setAcctName(acct_name);
        deliveryAddressRequest.setAcctMob(acct_mob);
        deliveryAddressRequest.setAcctAddress(acct_address);
        deliveryAddressRequest.setAcctPinCode(acct_pin_code);
        deliveryAddressRequest.setCityName(selectedCityName);
        deliveryAddressRequest.setCountryName(selectedCountryName);
        deliveryAddressRequest.setStateName(selectedStateName);

        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<AddDeliveryResponse> call = apiInterface.insertDeliverydetails(getMap());
        call.enqueue(new Callback<AddDeliveryResponse>() {
            @Override
            public void onResponse(Call<AddDeliveryResponse> call, Response<AddDeliveryResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        ll_add_address.setVisibility(View.GONE);
                        deliveryaddressDetails();
                        clearFeilds();
                        buttonAdd.setVisibility(View.VISIBLE);
                        //   dialog.dismiss();
                        String msg = response.body().getMessage();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDeliveryResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("AddCDCActivity.onFailure" + t.toString());
            }
        });
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("acct_name", acct_name);
        map.put("acct_mob", acct_mob);
        map.put("acct_address", acct_address);
        map.put("acct_pin_code", acct_pin_code);
        map.put("acct_city_id", selectedCityId);
        map.put("acct_state_id", selectedStateId);
        map.put("acct_country_id", selectedCountryId);

        return map;
    }

    /*@Override
    public void onItemChecked(DeliveryAddressRequest deliveryAddressRequest) {
        deleiveryRequestListSelected.put(deliveryAddressRequest.getAcctId(),deliveryAddressRequest);
    }

    @Override
    public void onItemUnChecked(DeliveryAddressRequest deliveryAddressRequest) {
        deleiveryRequestListSelected.remove(deliveryAddressRequest.getAcctId());
    }*/
}