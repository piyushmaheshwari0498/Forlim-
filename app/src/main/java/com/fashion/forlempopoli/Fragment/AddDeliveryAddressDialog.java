package com.fashion.forlempopoli.Fragment;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.fashion.forlempopoli.Adapter.CityAdapter;
import com.fashion.forlempopoli.Adapter.CountryAdapter;
import com.fashion.forlempopoli.Adapter.StateAdapter;
import com.fashion.forlempopoli.Entity.request.CityRequest;
import com.fashion.forlempopoli.Entity.request.CountryRequest;
import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;
import com.fashion.forlempopoli.Entity.request.SpinnerBankBillingData;
import com.fashion.forlempopoli.Entity.request.StateRequest;
import com.fashion.forlempopoli.Entity.response.AddDeliveryResponse;
import com.fashion.forlempopoli.Entity.response.SpinnerBankBillingResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.Listener.OnItemInsertedListener;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeliveryAddressDialog extends DialogFragment {
    Dialog dialog;
    EditText etName,etMobileNo,etDeliveryAddress,etPincode;
    Spinner spnCity,spnState,spnCountry;
    private List<CityRequest> cityRequestList;
    private List<StateRequest> stateRequestList;
    private List<CountryRequest> countryRequestList;
    String selectedCityId="",selectedStateId="",selectedCountryId="";
    String cityname,statename,countryname,acct_name,acct_mob,acct_pin_code,acct_address;
    Button saveBtn;
    private OnItemInsertedListener onItemInsertedListener;
    int i;
    private AppSharedPreference mAppSharedPrefernce;
    DeliveryAddressRequest deliveryAddressRequest;
    String selectedCityName,selectedStateName,selectedCountryName;
    TextView tvDeliveryDetails;

    public AddDeliveryAddressDialog(String name,String address,String mobile,
                                    String pincode,String cityId,String stateId, String countryId) {
        acct_name = name;
        acct_address =address;
        acct_mob = mobile;
        acct_pin_code = pincode;
        selectedCityId = cityId;
        selectedStateId = stateId;
        selectedCountryId = countryId;

    }

    public void onStart()
    {
        super.onStart();
        dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    public void AddDeliveryAddressDialog(int i)
    {
     this.i=i;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_deliveryaddr_dialog, container, false);
        mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(getActivity());
        etName=view.findViewById(R.id.etName);
        etMobileNo=view.findViewById(R.id.etMobileNo);
        etDeliveryAddress=view.findViewById(R.id.etDeliveryAddress);
        etPincode=view.findViewById(R.id.etPincode);
        spnCity=view.findViewById(R.id.spnCity);
        spnState=view.findViewById(R.id.spnState);
        spnCountry=view.findViewById(R.id.spnCountry);
        saveBtn=view.findViewById(R.id.saveBtn);
        tvDeliveryDetails=view.findViewById(R.id.tvDeliveryDetails);
        tvDeliveryDetails.setPaintFlags(tvDeliveryDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        getSpinner();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAddressDetails();
            }
        });

        return view;
    }
    private void setcityAddapter(){
        if (getActivity()!=null) {
            CityAdapter customCityAdapter1 = new CityAdapter(getActivity(), cityRequestList);
            spnCity.setAdapter(customCityAdapter1);
            setCityObatainedToSpinner();
        }
    }
    private void setstateAddapter(){
        if (getActivity()!=null) {
            StateAdapter stateAdapter1 = new StateAdapter(getActivity(), stateRequestList);
            spnState.setAdapter(stateAdapter1);
            setStateObatainedToSpinner();
        }
    }
    private void setcountryAddapter(){
        if (getActivity()!=null) {
            CountryAdapter countryAdapter1 = new CountryAdapter(getActivity(), countryRequestList);
            spnCountry.setAdapter(countryAdapter1);
            setCountryObatainedToSpinner();
        }
    }

    public void getSpinner(){
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerBankBillingResponse> call = apiInterface.getBankBillingDetailsSpinner();
        call.enqueue(new Callback<SpinnerBankBillingResponse>() {
            @Override
            public void onResponse(Call<SpinnerBankBillingResponse> call, Response<SpinnerBankBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        SpinnerBankBillingData spinnerBankBillingData = response.body().getData();
                        cityRequestList=spinnerBankBillingData.getCity();
                        CityRequest mCityRequest=new CityRequest();
                        mCityRequest.setCityId("0");
                        mCityRequest.setCityName("Please Select City Name");
                        cityRequestList.add(0,mCityRequest);
                        setcityAddapter();
                        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {
                                selectedCityId=cityRequestList.get(pos).getCityId();
                                selectedCityName=cityRequestList.get(pos).getCityName();
                            }
                            public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                        });
                        stateRequestList=spinnerBankBillingData.getState();
                        StateRequest mstateRequest=new StateRequest();
                        mstateRequest.setStateId("0");
                        mstateRequest.setStateName("Please Select State Name");
                        stateRequestList.add(0,mstateRequest);
                        setstateAddapter();
                        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {
                                selectedStateId=stateRequestList.get(pos).getStateId();
                                selectedStateName=stateRequestList.get(pos).getStateName();
                            }
                            public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                        });
                        countryRequestList=spinnerBankBillingData.getCountry();
                        CountryRequest mcountryRequest=new CountryRequest();
                        mcountryRequest.setCountryId("0");
                        mcountryRequest.setCountryName("Please Select Country Name");
                        countryRequestList.add(0,mcountryRequest);
                        setcountryAddapter();
                        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {
                                selectedCountryId=countryRequestList.get(pos).getCountryId();
                                selectedCountryName=countryRequestList.get(pos).getCountryName();
                            }
                            public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                        });
                    }
                    else {
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

    private void setCityObatainedToSpinner(){
        for(int i=0;i<cityRequestList.size();i++)
        {
            if(cityname!=null){
                if(cityname.equals(cityRequestList.get(i).getCityName()))
                    System.out.println("bank spinner city:"+cityname);
                {
                    int position=i;
                    selectedCityId=cityRequestList.get(i).getCityId();
                    spnCity.setSelection(i);

                }
            }
        }
    }
    private void setStateObatainedToSpinner(){
        for(int i=0;i<stateRequestList.size();i++) {
            if (statename != null) {
                if (statename.equals(stateRequestList.get(i).getStateName())) {
                    int position = i;
                    selectedStateId = stateRequestList.get(i).getStateId();
                    spnState.setSelection(i);
                }
            }
        }
    }
    private void setCountryObatainedToSpinner(){
        for(int i=0;i<countryRequestList.size();i++)
        {
            if(countryname!=null){
                if(countryname.equals(countryRequestList.get(i).getCountryName()))
                {
                    int position=i;
                    selectedCountryId=countryRequestList.get(i).getCountryId();
                    spnCountry.setSelection(i);
                }
            }

        }
    }
    public void insertAddressDetails() {
        acct_name=etName.getText().toString();
        acct_mob=etMobileNo.getText().toString();
        acct_address=etDeliveryAddress.getText().toString();
        acct_pin_code=etPincode.getText().toString();

        deliveryAddressRequest=new DeliveryAddressRequest();
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
                        if(onItemInsertedListener!=null) {
                            onItemInsertedListener.onItemInserted(deliveryAddressRequest,i);
                        }
                     //   dialog.dismiss();
                        String msg= response.body().getMessage();
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                    }
                }
                else {
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
                System.out.println("AddCDCActivity.onFailure"+t.toString());
            }
        });
    }

    private HashMap<String, Object> getMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("acc_id",mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("acct_name",acct_name);
        map.put("acct_mob",acct_mob);
        map.put("acct_address",acct_address);
        map.put("acct_pin_code",acct_pin_code);
        map.put("acct_city_id",selectedCityId);
        map.put("acct_state_id",selectedStateId);
        map.put("acct_country_id",selectedCountryId);

        return map;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setOnItemInsertedListener(OnItemInsertedListener onItemInsertedListener) {
        this.onItemInsertedListener=onItemInsertedListener;
    }
}
