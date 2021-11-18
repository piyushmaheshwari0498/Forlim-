package com.example.forlempopoli.Adapter;

import static com.example.forlempopoli.Fragment.DeliveryAddressFragment.deliveryAddressAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Entity.request.CityRequest;
import com.example.forlempopoli.Entity.request.CountryRequest;
import com.example.forlempopoli.Entity.request.DeliveryAddrData;
import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;
import com.example.forlempopoli.Entity.request.SpinnerBankBillingData;
import com.example.forlempopoli.Entity.request.StateRequest;
import com.example.forlempopoli.Entity.response.SpinnerBankBillingResponse;
import com.example.forlempopoli.Entity.response.UpdateDeliveryAddressResponse;
import com.example.forlempopoli.Entity.response.Upt_Default_Deilvery_AddressResponse;
import com.example.forlempopoli.Fragment.Delete_Dialog;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.Listener.OnItemDeleteListener;
import com.example.forlempopoli.Listener.OnRVClickListener;
import com.example.forlempopoli.Listener.OpenEditDialogListener;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.PatternClass;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.ViewHolder> {
    List<DeliveryAddressRequest> deliveryAddressRequestList;
    Context context;
    FragmentManager fragmentManager;
    String selectedCityId = "", selectedStateId = "", selectedCountryId = "";
    String cityname, statename, countryname, acct_name, acct_mob, acct_pin_code, acct_address;
    RequestBody apiAccessKey, account_id, acct_id, account_name, account_mob, account_address, account_pin_code, acct_city_id,
            acct_state_id, acct_country_id;
    RequestBody default_address_id;
    String cityId, stateId, countryId;
    DeliveryAddressRequest deliveryAddressRequest;
    String selectedCityName, selectedStateName, selectedCountryName;
    AppUtils appUtils;
    CityAdapter customCityAdapter2;
    StateAdapter stateAdapter2;
    CountryAdapter countryAdapter2;
    DeliveryAddressRequest addressRequest;
    String name, mobile, address, pincode;
    String acctId;
    //    private List<CityDatum> cityRequests;
//    private List<StateDatum> stateRequests;
//    private List<CountryDatum> countryRequests;
    ViewHolder holder;
    int lastSelectedPosition = -1;
    int defaultAddressPosition = 0;
    private OpenEditDialogListener openEditDialogListener;
    private AppSharedPreference mAppSharedPrefernce;
    private List<CityRequest> cityRequestList;
    private List<StateRequest> stateRequestList;
    private List<CountryRequest> countryRequestList;
    private OnRVClickListener onRadioChangeListener;

    public DeliveryAddressAdapter(List<DeliveryAddressRequest> deliveryAddressRequestList,
                                  Context context, FragmentManager fragmentManager) {
        this.deliveryAddressRequestList = deliveryAddressRequestList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(this.context);
        appUtils = new AppUtils();

    }

    @NonNull
    @Override
    public DeliveryAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_address, viewGroup, false);
        return new DeliveryAddressAdapter.ViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final DeliveryAddressAdapter.ViewHolder viewHolder, final int i) {
        holder = viewHolder;
//        name = viewHolder.etName.getText().toString().trim();
//        mobile = viewHolder.etMobileNo.getText().toString().trim();
//        address = viewHolder.etDeliveryAddress.getText().toString().trim();
//        pincode = viewHolder.etPincode.getText().toString().trim();

//        Log.d("addr adapter",deliveryAddressRequestList.get(i).getAcctName());
        viewHolder.tv_name.setText(deliveryAddressRequestList.get(i).getAcctName());
        viewHolder.tv_mobileNo.setText(deliveryAddressRequestList.get(i).getAcctMob());
        viewHolder.tv_deliveryAddr.setText(deliveryAddressRequestList.get(i).getAcctAddress());
//        Log.e("getCityName", deliveryAddressRequestList.get(i).getCityName());
//        Log.e("getAcctPinCode", deliveryAddressRequestList.get(i).getAcctPinCode());
        viewHolder.tv_cityName.setText(deliveryAddressRequestList.get(i).getCityName() + " - " +
                deliveryAddressRequestList.get(i).getAcctPinCode());
        viewHolder.tv_stateName.setText(deliveryAddressRequestList.get(i).getStateName());
        viewHolder.tv_countryName.setText(deliveryAddressRequestList.get(i).getCountryName());
        viewHolder.setFieldsDisable();

        viewHolder.default_address.setChecked(lastSelectedPosition == i);

        Log.e("default", String.valueOf(deliveryAddressRequestList.get(i).isDefault_address_status()));
        //For Deafult Address Selection from Databse
//        mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID,
//                deliveryAddressRequestList.get(i).getAcctId());

        if(deliveryAddressRequestList.get(i).isDefault_address_status().equals("1")) {
            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID,
                deliveryAddressRequestList.get(i).getAcctId());
            if (deliveryAddressRequestList.get(i).getAcctId().equals
                    (mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID))) {
                defaultAddressPosition = i;
                    viewHolder.default_address.setChecked(true);
            }
        }

        //For Default Button
        if (lastSelectedPosition == i)
            viewHolder.ll_default.setVisibility(View.VISIBLE);
        else
            viewHolder.ll_default.setVisibility(View.GONE);


    }

    public void getSpinner(AutoCompleteTextView spncity, AutoCompleteTextView
            spnstate, AutoCompleteTextView spncountry) {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerBankBillingResponse> call = apiInterface.getBankBillingDetailsSpinner();
        call.enqueue(new Callback<SpinnerBankBillingResponse>() {
            @Override
            public void onResponse(Call<SpinnerBankBillingResponse> call, Response<SpinnerBankBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        SpinnerBankBillingData registerData = response.body().getData();
                        cityRequestList = registerData.getCity();
                        //   if(getActivity()!=null) {
                        customCityAdapter2 = new CityAdapter(context, cityRequestList);
                        spncity.setAdapter(customCityAdapter2);
                        //  }
                        //if(cityname!=null) {
                        cityname = addressRequest.getCityName();
                        //  }

                       /* spncity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedCityId = cityRequests.get(pos).getCityId();
                                selectedCityName = cityRequests.get(pos).getCityName();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                        setcityAddapter(spncity);
                        spncity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                Log.e("cityId", cityRequestList.get(pos).getCityId());
                                selectedCityId = cityRequestList.get(pos).getCityId();
                                selectedCityName = cityRequestList.get(pos).getCityName();
                                spncity.setText(selectedCityName);
                            }
                        });

                        for (int i = 0; i < cityRequestList.size(); i++) {
                            if (cityname != null) {
                                if (cityname.equals(cityRequestList.get(i).getCityName())) {
                                    spncity.setText(cityRequestList.get(i).getCityName());
                                }
                            }
                        }

                        stateRequestList = registerData.getState();
                        //  if(getActivity()!=null){
                        stateAdapter2 = new StateAdapter(context, stateRequestList);
                        spnstate.setAdapter(stateAdapter2);
                        // }
                        //   if(statename!=null) {
                        statename = addressRequest.getStateName();
                        //   }
                        /*spnstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedStateId = stateRequests.get(pos).getStateId();
                                selectedStateName = stateRequests.get(pos).getStateName();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                        setstateAddapter(spnstate);
                        spnstate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                selectedStateId = stateRequestList.get(pos).getStateId();
                                selectedStateName = stateRequestList.get(pos).getStateName();
                                spnstate.setText(selectedStateName);
                            }
                        });
                        for (int i = 0; i < stateRequestList.size(); i++) {
                            if (statename != null) {
                                if (statename.equals(stateRequestList.get(i).getStateName())) {
                                    spnstate.setText(stateRequestList.get(i).getStateName());
                                }
                            }
                        }
                        countryRequestList = registerData.getCountry();
                        //     if(getActivity()!=null) {
                        countryAdapter2 = new CountryAdapter(context, countryRequestList);
                        spncountry.setAdapter(countryAdapter2);
                        //      }
                        //  if(countryname!=null) {
                        countryname = addressRequest.getCountryName();
                        setcountryAddapter(spncountry);
                        //   }
                       /* spncountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedCountryId = countryRequests.get(pos).getCountryId();
                                selectedCountryName = countryRequests.get(pos).getCountryName();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/
                        spncountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                selectedCountryId = countryRequestList.get(pos).getCountryId();
                                selectedCountryName = countryRequestList.get(pos).getCountryName();
                                spncountry.setText(selectedCountryName);
                            }
                        });
                        for (int i = 0; i < countryRequestList.size(); i++) {
                            if (countryname != null) {
                                if (countryname.equals(countryRequestList.get(i).getCountryName())) {
                                    spncountry.setText(countryRequestList.get(i).getCountryName());
                                }
                            }

                        }
                    } else {
                        if (response.body().getStatusCode() == 0) {
                            if (response.body().getStatusMessage().equals("Fail")) {
                                Toast.makeText(context, "Wrong enter details!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Some thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SpinnerBankBillingResponse> call, Throwable t) {

            }
        });
    }

    private void setcityAddapter(AutoCompleteTextView city) {
        if (context != null) {
            CityAdapter customCityAdapter1 = new CityAdapter(context, cityRequestList);
            city.setAdapter(customCityAdapter1);
            setCityObatainedToSpinner(city);
        }
    }

    private void setstateAddapter(AutoCompleteTextView state) {
        if (context != null) {
            StateAdapter stateAdapter1 = new StateAdapter(context, stateRequestList);
            state.setAdapter(stateAdapter1);
            setStateObatainedToSpinner(state);
        }
    }

    private void setcountryAddapter(AutoCompleteTextView country) {
        if (context != null) {
            CountryAdapter countryAdapter1 = new CountryAdapter(context, countryRequestList);
            country.setAdapter(countryAdapter1);
            setCountryObatainedToSpinner(country);
        }
    }

    private void setCityObatainedToSpinner(AutoCompleteTextView city) {
        for (int i = 0; i < cityRequestList.size(); i++) {
            if (cityname != null) {
                if (cityname.equals(cityRequestList.get(i).getCityName()))
                    System.out.println("bank spinner city:" + cityname);
                {
                    selectedCityId = cityRequestList.get(i).getCityId();
                    selectedCityName = cityRequestList.get(i).getCityName();
                    city.setText(selectedCityName);

                }
            }
        }
    }

    private void setStateObatainedToSpinner(AutoCompleteTextView state) {
        for (int i = 0; i < stateRequestList.size(); i++) {
            if (statename != null) {
                if (statename.equals(stateRequestList.get(i).getStateName())) {
                    int position = i;
                    selectedStateId = stateRequestList.get(i).getStateId();
                    selectedStateName = stateRequestList.get(i).getStateName();
                    state.setText(selectedStateName);
                }
            }
        }
    }

    private void setCountryObatainedToSpinner(AutoCompleteTextView country) {
        for (int i = 0; i < countryRequestList.size(); i++) {
            if (countryname != null) {
                if (countryname.equals(countryRequestList.get(i).getCountryName())) {
                    int position = i;
                    selectedCountryId = countryRequestList.get(i).getCountryId();
                    selectedCountryName = countryRequestList.get(i).getCountryName();
                    country.setText(selectedCountryName);
                }
            }

        }
    }

    public void onOpenEditDialogListener(OpenEditDialogListener openEditDialogListener) {
        this.openEditDialogListener = openEditDialogListener;
    }

    @Override
    public int getItemCount() {
        return deliveryAddressRequestList.size();
    }

    public void updateDeliveryAddress(int pos, String accountRequest, String etName, String
            etMobile, String etAddres, String etPincode) {
        acctId = accountRequest;
        name = etName;
        mobile = etMobile;
        address = etAddres;
        pincode = etPincode;
        String accountId = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);
        System.out.println("accountId deliveryaddr" + accountId);
        apiAccessKey = RequestBody.create(MultipartBody.FORM, "ZkC6BDUzxz");
        account_id = RequestBody.create(MultipartBody.FORM, accountId);
        account_name = RequestBody.create(MultipartBody.FORM, name);
        acct_id = RequestBody.create(MultipartBody.FORM, acctId);
        account_mob = RequestBody.create(MultipartBody.FORM, mobile);
        account_address = RequestBody.create(MultipartBody.FORM, address);
        account_pin_code = RequestBody.create(MultipartBody.FORM, pincode);
        acct_city_id = RequestBody.create(MultipartBody.FORM, selectedCityId);
        acct_country_id = RequestBody.create(MultipartBody.FORM, selectedCountryId);
        acct_state_id = RequestBody.create(MultipartBody.FORM, selectedStateId);

        deliveryAddressRequest = new DeliveryAddressRequest();
        deliveryAddressRequest.setAcctId(acctId);
        deliveryAddressRequest.setAcctName(name);
        deliveryAddressRequest.setAcctMob(mobile);
        deliveryAddressRequest.setAcctAddress(address);
        deliveryAddressRequest.setAcctPinCode(pincode);
        deliveryAddressRequest.setCityName(cityname);
        deliveryAddressRequest.setStateName(statename);
        deliveryAddressRequest.setCountryName(countryname);

        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<UpdateDeliveryAddressResponse> call = apiInterface.updateDeliveryAddress(getHashMap());
        call.enqueue(new Callback<UpdateDeliveryAddressResponse>() {
            @Override
            public void onResponse(Call<UpdateDeliveryAddressResponse> call, Response<UpdateDeliveryAddressResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg = response.body().getMessage();
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        DeliveryAddrData deliveryAddrData = response.body().getData();

                        if (!selectedCityName.isEmpty() &&
                                !selectedCountryName.isEmpty() &&
                                !selectedStateName.isEmpty()) {
                            deliveryAddressRequest.setAcctName(deliveryAddrData.getAcctName());
                            deliveryAddressRequest.setAcctMob(deliveryAddrData.getAcctMob());
                            deliveryAddressRequest.setAcctAddress(deliveryAddrData.getAcctAddress());
                            deliveryAddressRequest.setAcctPinCode(deliveryAddrData.getAcctPinCode());
                            deliveryAddressRequest.setCityName(selectedCityName);
                            deliveryAddressRequest.setCountryName(selectedCountryName);
                            deliveryAddressRequest.setStateName(selectedStateName);
                        }
                        deliveryAddressAdapter.notifyItemChanged(pos);

                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            System.out.println("message" + message);
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateDeliveryAddressResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Edit_Delivery_dialog_onFailure" + t.toString());
            }
        });
    }

    private Map<String, RequestBody> getHashMap() {
        java.util.Map<java.lang.String, okhttp3.RequestBody> map = new HashMap<>();
        map.put("API_ACCESS_KEY", apiAccessKey);
        map.put("acc_id", account_id);
        map.put("acct_id", acct_id);
        map.put("acct_name", account_name);
        map.put("acct_mob", account_mob);
        map.put("acct_address", account_address);
        map.put("acct_pin_code", account_pin_code);
        map.put("acct_city_id", acct_city_id);
        map.put("acct_state_id", acct_state_id);
        map.put("acct_country_id", acct_country_id);
        return map;
    }

    public void updateDefaultDeliveryAddress(int pos) {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<Upt_Default_Deilvery_AddressResponse> call = apiInterface.updateDefaultDeliveryAddress(getupt_default_addrHashMap());
        call.enqueue(new Callback<Upt_Default_Deilvery_AddressResponse>() {
            @Override
            public void onResponse(Call<Upt_Default_Deilvery_AddressResponse> call, Response<Upt_Default_Deilvery_AddressResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg = response.body().getMessage();
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        //deliveryAddressAdapter.notifyItemChanged(pos);

                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            System.out.println("message" + message);
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Upt_Default_Deilvery_AddressResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Upt_Default_Deilvery_AddressResponse " + t.toString());
            }
        });
    }

    private HashMap<String, String> getupt_default_addrHashMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("acct_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));
        return map;
    }

    public void refreshBlockOverlay(int position) {
        notifyItemChanged(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RadioButton default_address;
        //EditText name,mobileNo,deliveryAddr,pincode,cityName,stateName,countryName;
        TextView textview_edit, tv_name, tv_mobileNo, tv_deliveryAddr, tv_cityName, tv_stateName, tv_countryName;
        MaterialButton deletebtn, editView;
        MaterialButton cancelBtn, saveBtn;
        MaterialButton btn_default;
        LinearLayout ll_edit, ll_save, ll_city, ll_state, ll_country, ll_default;
        TextInputEditText etName, etMobileNo, etDeliveryAddress, etPincode;
        TextInputLayout inputetName, inputetMobileNo, inputetDeliveryAddress, inputetPincode;
        int i;
        AutoCompleteTextView spnCity, spnState, spnCountry;
        TextInputLayout inputspnCity, inputspnState, inputspnCountry;
        CardView address_cardview;

        ViewHolder(View itemView) {
            super(itemView);
            textview_edit = itemView.findViewById(R.id.textview_edit);

            inputetName = itemView.findViewById(R.id.inputName);
            inputetMobileNo = itemView.findViewById(R.id.inputNMobile);
            inputetDeliveryAddress = itemView.findViewById(R.id.inputAddress);
            inputetPincode = itemView.findViewById(R.id.inputPincode);
            default_address = (RadioButton) itemView.findViewById(R.id.default_address);
            address_cardview = itemView.findViewById(R.id.address_cardview);
//            name = itemView.findViewById(R.id.name);
//            mobileNo = itemView.findViewById(R.id.mobileNo);
//            deliveryAddr = itemView.findViewById(R.id.deliveryAddr);
//            pincode = itemView.findViewById(R.id.pincode);
//            cityName = itemView.findViewById(R.id.cityName);
//            stateName = itemView.findViewById(R.id.stateName);
//            countryName = itemView.findViewById(R.id.countryName);

            etName = itemView.findViewById(R.id.etName);
            etMobileNo = itemView.findViewById(R.id.etMobileNo);
            etDeliveryAddress = itemView.findViewById(R.id.etDeliveryAddress);
            etPincode = itemView.findViewById(R.id.etPincode);

            spnCity = itemView.findViewById(R.id.spnCity);
            spnState = itemView.findViewById(R.id.spnState);
            spnCountry = itemView.findViewById(R.id.spnCountry);

            inputspnCity = itemView.findViewById(R.id.inputspnCity);
            inputspnState = itemView.findViewById(R.id.inputspnState);
            inputspnCountry = itemView.findViewById(R.id.inputspnCountry);

            ll_city = itemView.findViewById(R.id.ll_city);
            ll_state = itemView.findViewById(R.id.ll_state);
            ll_country = itemView.findViewById(R.id.ll_country);
            ll_edit = itemView.findViewById(R.id.ll_edit);
            ll_save = itemView.findViewById(R.id.ll_save);
            ll_default = itemView.findViewById(R.id.ll_default);

            cancelBtn = itemView.findViewById(R.id.cancelBtn);
            saveBtn = itemView.findViewById(R.id.saveBtn);
            editView = itemView.findViewById(R.id.editView);
            deletebtn = itemView.findViewById(R.id.deletebtn);
            btn_default = itemView.findViewById(R.id.btn_default);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_mobileNo = itemView.findViewById(R.id.tv_mobile);
            tv_deliveryAddr = itemView.findViewById(R.id.tv_address);
            tv_cityName = itemView.findViewById(R.id.tv_city);
            tv_stateName = itemView.findViewById(R.id.tv_state);
            tv_countryName = itemView.findViewById(R.id.tv_country);

            editView.setOnClickListener(this);
            deletebtn.setOnClickListener(this);

            cancelBtn.setOnClickListener(this);
            saveBtn.setOnClickListener(this);

            btn_default.setOnClickListener(this);

            address_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("selected", String.valueOf(defaultAddressPosition));
                    deliveryAddressRequestList.get(defaultAddressPosition).setDefault_address_status("0");
                    mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID, null);
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    deliveryAddressRequestList.get(lastSelectedPosition).setSelected("1");
                    ll_default.setVisibility(View.VISIBLE);
                }
            });
           /* default_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    Toast.makeText(context, "selected address is " +
                                    deliveryAddressRequestList.get(i).getAcctId(),
                            Toast.LENGTH_LONG).show();
                    deliveryAddressRequestList.get(lastSelectedPosition).setSelected(deliveryAddressRequestList.get(lastSelectedPosition).getAcctId());
                    ll_default.setVisibility(View.VISIBLE);
                }
            });
*/
            /*default_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

//                    notifyDataSetChanged();
                    if (compoundButton.isChecked()){
                        lastSelectedPosition = getAdapterPosition();
                        Toast.makeText(context, "selected address is " +
                                        deliveryAddressRequestList.get(i).getAcctId(),
                                Toast.LENGTH_LONG).show();
                        deliveryAddressRequestList.get(lastSelectedPosition).setSelected(deliveryAddressRequestList.get(lastSelectedPosition).getAcctId());

                        ll_default.setVisibility(View.VISIBLE);
                    }

                }
            });*/
        }

        private void setFieldsDisable() {
            tv_name.setVisibility(View.VISIBLE);
            tv_deliveryAddr.setVisibility(View.VISIBLE);
            tv_mobileNo.setVisibility(View.VISIBLE);
            tv_cityName.setVisibility(View.VISIBLE);
            tv_stateName.setVisibility(View.VISIBLE);
            tv_countryName.setVisibility(View.VISIBLE);
            textview_edit.setVisibility(View.GONE);
            default_address.setVisibility(View.VISIBLE);

            inputetName.setVisibility(View.GONE);
            inputetDeliveryAddress.setVisibility(View.GONE);
            inputetMobileNo.setVisibility(View.GONE);
            inputetPincode.setVisibility(View.GONE);
            ll_city.setVisibility(View.GONE);
            ll_state.setVisibility(View.GONE);
            ll_country.setVisibility(View.GONE);
            etName.setText(null);
            etDeliveryAddress.setText(null);
            etPincode.setText(null);
            etMobileNo.setText(null);
            ll_save.setVisibility(View.GONE);
            ll_edit.setVisibility(View.VISIBLE);
            address_cardview.setClickable(true);
//            mobileNo.setEnabled(false);
//            deliveryAddr.setEnabled(false);
//            pincode.setEnabled(false);
//            cityName.setEnabled(false);
//            stateName.setEnabled(false);
//            countryName.setEnabled(false);
        }

        private void setFieldsEnable() {
            textview_edit.setVisibility(View.VISIBLE);
            etName.setText(deliveryAddressRequestList.get(i).getAcctName());
            etMobileNo.setText(deliveryAddressRequestList.get(i).getAcctMob());
            etDeliveryAddress.setText(deliveryAddressRequestList.get(i).getAcctAddress());
            etPincode.setText(deliveryAddressRequestList.get(i).getAcctPinCode());
            inputetName.setVisibility(View.VISIBLE);
            inputetDeliveryAddress.setVisibility(View.VISIBLE);
            inputetMobileNo.setVisibility(View.VISIBLE);
            inputetPincode.setVisibility(View.VISIBLE);
            ll_city.setVisibility(View.VISIBLE);
            ll_state.setVisibility(View.VISIBLE);
            ll_country.setVisibility(View.VISIBLE);
            default_address.setVisibility(View.GONE);

            tv_name.setVisibility(View.GONE);
            tv_mobileNo.setVisibility(View.GONE);
            tv_deliveryAddr.setVisibility(View.GONE);
            tv_cityName.setVisibility(View.GONE);
            tv_stateName.setVisibility(View.GONE);
            tv_countryName.setVisibility(View.GONE);
            ll_edit.setVisibility(View.GONE);
            ll_save.setVisibility(View.VISIBLE);
            ll_default.setVisibility(View.GONE);


//            mobileNo.setEnabled(false);
//            deliveryAddr.setEnabled(false);
//            pincode.setEnabled(false);
//            cityName.setEnabled(false);
//            stateName.setEnabled(false);
//            countryName.setEnabled(false);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_default:
                    i = getAdapterPosition();
                    mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID,
                            String.valueOf(deliveryAddressRequestList.get(i).getAcctId()));
                    Log.d("selected", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID));
//                    Toast.makeText(context, "selected address is " +
//                                    mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_DEFAULT_ADDRESS_ID),
//                            Toast.LENGTH_LONG).show();
                    ll_default.setVisibility(View.GONE);
                    updateDefaultDeliveryAddress(lastSelectedPosition);
                    break;
                case R.id.editView:
                    i = getAdapterPosition();
                    Log.e("editView I", String.valueOf(i));
                    addressRequest = deliveryAddressRequestList.get(i);
                    Log.d("deliveryAddressRequest", deliveryAddressRequestList.get(i).getAcctName());
                    setFieldsEnable();
                    System.out.println("position" + i);
                    getSpinner(spnCity, spnState, spnCountry);
                    address_cardview.setClickable(false);
                    break;
                case R.id.deletebtn:
                    i = getAdapterPosition();
                    Delete_Dialog delete_dialog = new Delete_Dialog(deliveryAddressRequestList.get(i), i);
                    Log.e("delebtn I", String.valueOf(i));
                    delete_dialog.show(fragmentManager, "MyDialog");
                    delete_dialog.setOnItemDeleteListener(new OnItemDeleteListener() {
                        @Override
                        public void onItemDeleted(DeliveryAddressRequest deliveryAddressRequest, int position) {
                            deliveryAddressRequestList.remove(position);
                            deliveryAddressAdapter.notifyItemRemoved(position);
                        }
                    });
                    break;

                case R.id.cancelBtn:
                    setFieldsDisable();
                    break;

                case R.id.saveBtn:
                    i = getAdapterPosition();
                    if (!appUtils.isConnected(context)) {
                        Toasty.warning(context, context.getResources().getString(R.string.no_internet));
                    } else {
                        if (!validateName() | !validateAddress() | !validateMobile() | !validatePincode()
                                | !validateCity() | !validateState() | !validateCountry()) {
                            return;
                        }

                        Log.d("i value", String.valueOf(i));
                        if (deliveryAddressRequestList.get(i).getAcctId() != null) {
                            String name = etName.getText().toString().trim();
                            String mob = etMobileNo.getText().toString().trim();
                            String add = etDeliveryAddress.getText().toString().trim();
                            String pin = etPincode.getText().toString().trim();
                            updateDeliveryAddress(i, deliveryAddressRequestList.get(i).getAcctId(), name, mob, add, pin);
//                        deliveryAddressAdapter.notifyItemChanged(i);
//                        notifyDataSetChanged();
//                        notifyAll();

                            System.out.println("getAcctId " + deliveryAddressRequestList.get(i).getAcctId());
                        } else {
                            Toast.makeText(context, "Cannot Update Details !!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;
            }
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
            } else if (!PatternClass.isValidPhone(mob)) {
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
                Custom_Toast.warning(context, "City");
                return false;
            } else {
                return true;
            }
        }

        public boolean validateState() {
            if (selectedStateId.equals("0")) {
                Custom_Toast.warning(context, "StateId");
                return false;
            } else {
                return true;
            }
        }

        public boolean validateCountry() {
            if (selectedCountryId.equals("0")) {
                Custom_Toast.warning(context, "Country");
                return false;
            } else {
                return true;
            }
        }
    }
}