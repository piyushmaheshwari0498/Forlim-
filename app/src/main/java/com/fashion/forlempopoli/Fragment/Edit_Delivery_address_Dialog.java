package com.fashion.forlempopoli.Fragment;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.fashion.forlempopoli.Adapter.CustomCityAdapter;
import com.fashion.forlempopoli.Adapter.CustomCountryAdapter;
import com.fashion.forlempopoli.Adapter.CustomStateAdapter;
import com.fashion.forlempopoli.Entity.request.CityDatum;
import com.fashion.forlempopoli.Entity.request.CountryDatum;
import com.fashion.forlempopoli.Entity.request.DeliveryAddrData;
import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;
import com.fashion.forlempopoli.Entity.request.RegisterData;
import com.fashion.forlempopoli.Entity.request.StateDatum;
import com.fashion.forlempopoli.Entity.response.SpinnerRegisterResponse;
import com.fashion.forlempopoli.Entity.response.UpdateDeliveryAddressResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.Listener.OnItemEditedListener;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Delivery_address_Dialog  extends DialogFragment {
    Dialog dialog;
    EditText etname,etmobile,etpincode,deliveryaddr;
    Spinner spncity,spnstate,spncountry;
    private AppSharedPreference mAppSharedPrefernce;
    String name,mobile,address,pincode;
    ImageView savebtn;
    private List<CityDatum> cityRequests;
    private List<StateDatum> stateRequests;
    private List<CountryDatum> countryRequests;
    String cityname,statename,countryname;
    String cityId,stateId,countryId;
    String selectedCityName="",selectedStateName="",selectedCountryName="";
    String acctId;
    private final DeliveryAddressRequest addressRequest;
    int i;
    RequestBody apiAccessKey,account_id,acct_id,acct_name,acct_mob,acct_address,acct_pin_code,acct_city_id,
            acct_state_id,acct_country_id;
    DeliveryAddressRequest deliveryAddressRequest;
    private OnItemEditedListener onItemEditedListener;
    CustomCityAdapter customCityAdapter2;
    CustomStateAdapter stateAdapter2;
    CustomCountryAdapter countryAdapter2;
    ProgressDialog ringProgressDialog;
    String accountId;
    TextView tvEditDeliveryDetails;
    List<DeliveryAddressRequest> deliveryAddressRequestList;

    public Edit_Delivery_address_Dialog(DeliveryAddressRequest addressRequest, int i) {
        this.addressRequest=addressRequest;
        this.acctId=addressRequest.getAcctId();
        this.i=i;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            ringProgressDialog = new ProgressDialog(getActivity());
            ringProgressDialog.setMessage("Loading...");
            ringProgressDialog.show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.edit_delivery_address_dialog, container, false);
        mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(getActivity());
        etname=itemView.findViewById(R.id.etname);
        etmobile=itemView.findViewById(R.id.etmobile);
        etpincode=itemView.findViewById(R.id.etpincode);
        deliveryaddr=itemView.findViewById(R.id.deliveryaddr);
        spncity=itemView.findViewById(R.id.spinnercity);
        spnstate=itemView.findViewById(R.id.spinnerstate);
        spncountry=itemView.findViewById(R.id.spinnercountry);
        savebtn=itemView.findViewById(R.id.savebtn);
        tvEditDeliveryDetails=itemView.findViewById(R.id.tvEditDeliveryDetails);
        tvEditDeliveryDetails.setPaintFlags(tvEditDeliveryDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        etname.setText(deliveryAddressRequestList.get(i).getAcctName());
        etmobile.setText(deliveryAddressRequestList.get(i).getAcctMob());
        etpincode.setText(deliveryAddressRequestList.get(i).getAcctPinCode());
        deliveryaddr.setText(deliveryAddressRequestList.get(i).getAcctAddress());
        getSpinner();
        savebtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(deliveryAddressRequestList.get(i).getAcctId()!=null)
                 {
                     updateDeliveryAddress(deliveryAddressRequestList.get(i).getAcctId());
                     System.out.println("getAcctId"+deliveryAddressRequestList.get(i).getAcctId());
                 }
                 else{
                     Toast.makeText(getContext(), "Cannot Update Details !!", Toast.LENGTH_SHORT).show();
                 }
             }
         });
        return itemView;
    }

    public void updateDeliveryAddress(String accountRequest)  {
        acctId=accountRequest;
        name=etname.getText().toString();
        mobile=etmobile.getText().toString();
        address=deliveryaddr.getText().toString();
        pincode=etpincode.getText().toString();
        accountId=mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);
        System.out.println("accountId deliveryaddr"+accountId);
        apiAccessKey = RequestBody.create(MultipartBody.FORM, "ZkC6BDUzxz");
        account_id = RequestBody.create(MultipartBody.FORM, accountId);
        acct_name = RequestBody.create(MultipartBody.FORM, name);
        acct_id = RequestBody.create(MultipartBody.FORM,acctId);
        acct_mob = RequestBody.create(MultipartBody.FORM, mobile);
        acct_address = RequestBody.create(MultipartBody.FORM, address);
        acct_pin_code = RequestBody.create(MultipartBody.FORM, pincode);
        acct_city_id = RequestBody.create(MultipartBody.FORM, cityId);
        acct_country_id = RequestBody.create(MultipartBody.FORM, countryId);
        acct_state_id = RequestBody.create(MultipartBody.FORM, stateId);

        deliveryAddressRequest=new DeliveryAddressRequest();
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
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                      DeliveryAddrData deliveryAddrData=response.body().getData();
             if(onItemEditedListener!=null) {
                  if(!selectedCityName.isEmpty() && !selectedCountryName.isEmpty() && !selectedStateName.isEmpty()) {
                      deliveryAddressRequest.setAcctName(deliveryAddrData.getAcctName());
                      deliveryAddressRequest.setAcctMob(deliveryAddrData.getAcctMob());
                      deliveryAddressRequest.setAcctAddress(deliveryAddrData.getAcctAddress());
                      deliveryAddressRequest.setAcctPinCode(deliveryAddrData.getAcctPinCode());
                      deliveryAddressRequest.setCityName(selectedCityName);
                      deliveryAddressRequest.setCountryName(selectedCountryName);
                      deliveryAddressRequest.setStateName(selectedStateName);
                        }
                           onItemEditedListener.onItemEdited(deliveryAddressRequest,i);
                      }
                      dismiss();
                    }
                }
                else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            System.out.println("message"+message);
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
            public void onFailure(Call<UpdateDeliveryAddressResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Edit_Delivery_dialog_onFailure" + t.toString());
            }
        });
    }

    public void getSpinner(){
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerRegisterResponse> call = apiInterface.getRegisterSpinnerData();
        call.enqueue(new Callback<SpinnerRegisterResponse>() {
            @Override
            public void onResponse(Call<SpinnerRegisterResponse> call, Response<SpinnerRegisterResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        ringProgressDialog.dismiss();
                        RegisterData registerData=response.body().getData();
                        cityRequests=registerData.getCity();
                        //   if(getActivity()!=null) {
                        customCityAdapter2 = new CustomCityAdapter(getActivity(), cityRequests);
                        spncity.setAdapter(customCityAdapter2);
                      //  }
                         //if(cityname!=null) {
                        cityname = addressRequest.getCityName();
                        //  }

                        spncity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {
                                cityId=cityRequests.get(pos).getCityId();
                                selectedCityName=cityRequests.get(pos).getCityName();
                            }
                            public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                        });
                        for(int i=0;i<cityRequests.size();i++)
                        {
                            if(cityname!=null){
                                if(cityname.equals(cityRequests.get(i).getCityName()))
                                {
                                    spncity.setSelection(i);
                                }
                            }
                        }

                        stateRequests=registerData.getState();
                      //  if(getActivity()!=null){
                        stateAdapter2 = new CustomStateAdapter(getActivity(), stateRequests);
                        spnstate.setAdapter(stateAdapter2);
                       // }
                      //   if(statename!=null) {
                        statename = addressRequest.getStateName();
                      //   }
                        spnstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {
                                stateId=stateRequests.get(pos).getStateId();
                                selectedStateName=stateRequests.get(pos).getStateName();
                            }
                            public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                        });
                        for(int i=0;i<stateRequests.size();i++) {
                            if (statename != null) {
                                if (statename.equals(stateRequests.get(i).getStateName())) {
                                    spnstate.setSelection(i);
                                }
                            }
                        }
                        countryRequests=registerData.getCountry();
                     //     if(getActivity()!=null) {
                        countryAdapter2 = new CustomCountryAdapter(getActivity(), countryRequests);
                        spncountry.setAdapter(countryAdapter2);
                     //      }
                       //  if(countryname!=null) {
                        countryname = addressRequest.getCountryName();
                       //   }
                        spncountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                            {
                                countryId=countryRequests.get(pos).getCountryId();
                                selectedCountryName=countryRequests.get(pos).getCountryName();
                            }
                            public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                        });
                        for(int i=0;i<countryRequests.size();i++)
                        {
                            if(countryname!=null){
                                if(countryname.equals(countryRequests.get(i).getCountryName()))
                                {
                                    spncountry.setSelection(i);
                                }
                            }

                        }
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
            public void onFailure(Call<SpinnerRegisterResponse> call, Throwable t) {

            }
        });
    }

    private Map<String, RequestBody> getHashMap() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("API_ACCESS_KEY",apiAccessKey);
        map.put("acc_id",account_id);
        map.put("acct_id",acct_id);
        map.put("acct_name",acct_name);
        map.put("acct_mob",acct_mob);
        map.put("acct_address",acct_address);
        map.put("acct_pin_code",acct_pin_code);
        map.put("acct_city_id",acct_city_id);
        map.put("acct_state_id",acct_state_id);
        map.put("acct_country_id",acct_country_id);
        return map;
    }
    public void setOnItemEditedListener(OnItemEditedListener onItemEditedListener) {
        this.onItemEditedListener=onItemEditedListener;
    }

}
