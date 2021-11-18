package com.example.forlempopoli.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.forlempopoli.Adapter.CustomCityAdapter;
import com.example.forlempopoli.Adapter.CustomCountryAdapter;
import com.example.forlempopoli.Adapter.CustomStateAdapter;
import com.example.forlempopoli.Entity.request.CityDatum;
import com.example.forlempopoli.Entity.request.CountryDatum;
import com.example.forlempopoli.Entity.request.RegisterData;
import com.example.forlempopoli.Entity.request.StateDatum;
import com.example.forlempopoli.Entity.response.RegisterResponse;
import com.example.forlempopoli.Entity.response.SpinnerRegisterResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.LoginActivity;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.GSTValidation;
import com.example.forlempopoli.Utilities.PatternClass;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    EditText etCompanyName, etName, etEmailID, etMobileNo, etPassword, etCpwd, etGstin;
    Spinner spCity, spState, spCountry;
    String companyname, name, email, mobileno, password, confirmpwd, gstnin;
    private List<CityDatum> cityRequestList;
    private List<StateDatum> stateRequestList;
    private List<CountryDatum> countryRequestList;
    String selectedCityId, selectedStateId, selectedCountryId;
    String cityname, statename, countryname;
    String selectedCityName, selectedStateName, selectedCountryName;
    Button btn_register;
    AppUtils appUtils;

    TextInputLayout companyInputLayout, nameInputLayout, emailInputLayout, mobileInputLayout,
            gstInputLayout, passwordInputLayout, confrimPasswordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //setTitle(Html.fromHtml("<small>Register</small>"));
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Register</small></font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCompanyName = findViewById(R.id.etCompanyName);
        etName = findViewById(R.id.etName);
        etEmailID = findViewById(R.id.etEmailID);
        etMobileNo = findViewById(R.id.etMobileNo);
        etPassword = findViewById(R.id.etPassword);
        etCpwd = findViewById(R.id.etCpwd);
        etGstin = findViewById(R.id.etGstin);
        spCity = findViewById(R.id.spCity);
        spState = findViewById(R.id.spState);
        spCountry = findViewById(R.id.spCountry);
        btn_register = findViewById(R.id.btn_register);

        appUtils = new AppUtils();

        companyInputLayout = findViewById(R.id.company_name_layout);
        nameInputLayout = findViewById(R.id.owner_name_layout);
        emailInputLayout = findViewById(R.id.email_layout);
        mobileInputLayout = findViewById(R.id.mobile_layout);
        gstInputLayout = findViewById(R.id.gst_layout);
        passwordInputLayout = findViewById(R.id.password_layout);
        confrimPasswordInputLayout = findViewById(R.id.confirm_password_layout);

        getRegisterdetailsSpinner();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyname = etCompanyName.getText().toString().trim();
                name = etName.getText().toString().trim();
                email = etEmailID.getText().toString().trim();
                mobileno = etMobileNo.getText().toString().trim();
                gstnin = etGstin.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                confirmpwd = etCpwd.getText().toString().trim();
                try {
                    if (!appUtils.isConnected(RegistrationActivity.this)) {
                        Custom_Toast.warning(RegistrationActivity.this, getString(R.string.no_internet));
                    } else if (!validateCompany() | !validateName() | !validateEmail()
                            | !validateMobile() | !validatePassword() | !validateConfirmPassword() | !validateGST()) {
                        return;
                    }
                    addRegisterDetails();

                } catch (Exception e) {
                    Log.e("registerError", e.getLocalizedMessage());
                }

            }
        });
    }

    private void setcityAddapter() {
        if (getApplicationContext() != null) {
            CustomCityAdapter customCityAdapter = new CustomCityAdapter(getApplicationContext(), cityRequestList);
            spCity.setAdapter(customCityAdapter);
            setCityObatainedToSpinner();
        }
    }

    private void setstateAddapter() {
        if (getApplicationContext() != null) {
            CustomStateAdapter customStateAdapter = new CustomStateAdapter(getApplicationContext(), stateRequestList);
            spState.setAdapter(customStateAdapter);
            setStateObatainedToSpinner();
        }
    }

    private void setcountryAddapter() {
        if (getApplicationContext() != null) {
            CustomCountryAdapter customCountryAdapter = new CustomCountryAdapter(getApplicationContext(), countryRequestList);
            spCountry.setAdapter(customCountryAdapter);
            setCountryObatainedToSpinner();
        }
    }

    private boolean validateCompany() {
        if (companyname.isEmpty()) {
            companyInputLayout.setError("Please Enter Company Name");
            return false;
        } else {
            companyInputLayout.setError(null);
            companyInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateName() {
        if (name.isEmpty()) {
            nameInputLayout.setError("Please Enter Owner Name");
            return false;
        } else {
            nameInputLayout.setError(null);
            nameInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {

        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            emailInputLayout.setError("Please Enter Email Address");
            return false;
        } else if (!email.matches(checkEmail)) {
            emailInputLayout.setError("Please Enter Valid Email Address");
            return false;
        } else {
            emailInputLayout.setError(null);
            emailInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMobile() {

        if (mobileno.isEmpty()) {
            mobileInputLayout.setError("Please Enter Mobile Number");
            return false;
        } else if (!PatternClass.isValidPhone(mobileno)) {
            mobileInputLayout.setError("Please Enter Valid Mobile Number");
            return false;
        } else {
            mobileInputLayout.setError(null);
            mobileInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        password = etPassword.getText().toString().trim();
        if (password.isEmpty()) {
            passwordInputLayout.setError("Please Enter New Password");
            return false;
        }
        else if(!PatternClass.isValidPassword(password)){
            passwordInputLayout.setError("Password should contain atleast 4 characters");
            return false;
        }
        else {
            passwordInputLayout.setError(null);
            passwordInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {

        if (confirmpwd.isEmpty()) {
            confrimPasswordInputLayout.setError("Please Enter Confirm Password");
            return false;
        } else if (!password.equals(confirmpwd)) {
            confrimPasswordInputLayout.setError("Confirm Password should be matching");
            return false;
        } else {
            confrimPasswordInputLayout.setError(null);
            confrimPasswordInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateGST() throws Exception {

        if (gstnin.isEmpty()) {
            gstInputLayout.setError("Please Enter GST Number");
            return false;
        } else if (!GSTValidation.validGSTIN(gstnin)) {
            gstInputLayout.setError("Please Enter Valid GST Number");
            return false;
        } else {
            gstInputLayout.setError(null);
            gstInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void addRegisterDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<RegisterResponse> call = apiInterface.getRegisterDetails(getHashMap());
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    System.out.println("success" + response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg = response.body().getMessage();
                        Toast.makeText(RegistrationActivity.this, msg, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getRegisterdetailsSpinner() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerRegisterResponse> call = apiInterface.getRegisterSpinnerData();
        call.enqueue(new Callback<SpinnerRegisterResponse>() {
            @Override
            public void onResponse(Call<SpinnerRegisterResponse> call, Response<SpinnerRegisterResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
//                        SpinnerRegisterResponse spinnerRegisterResponse = response.body();
//                        cityRequestList=spinnerRegisterResponse.getCityData();
                        RegisterData registerData = response.body().getData();
                        cityRequestList = registerData.getCity();
                        setcityAddapter();
                        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedCityId = cityRequestList.get(pos).getCityId();
                                selectedCityName = cityRequestList.get(pos).getCityName();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        //stateRequestList=spinnerRegisterResponse.getStateData();
                        stateRequestList = registerData.getState();
                        setstateAddapter();
                        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedStateId = stateRequestList.get(pos).getStateId();
                                selectedStateName = stateRequestList.get(pos).getStateName();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        // countryRequestList=spinnerRegisterResponse.getCountryData();
                        countryRequestList = registerData.getCountry();
                        setcountryAddapter();
                        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selectedCountryId = countryRequestList.get(pos).getCountryId();
                                selectedCountryName = countryRequestList.get(pos).getCountryName();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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
            public void onFailure(Call<SpinnerRegisterResponse> call, Throwable t) {

            }
        });
    }

    private void setCityObatainedToSpinner() {
        for (int i = 0; i < cityRequestList.size(); i++) {
            if (cityname != null) {
                if (cityname.equals(cityRequestList.get(i).getCityName()))
                    System.out.println("city: spinner" + cityname);
                {
                    int position = i;
                    selectedCityId = cityRequestList.get(i).getCityId();

                    spCity.setSelection(position);

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
                    spState.setSelection(position);
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
                    spCountry.setSelection(position);
                }
            }

        }
    }

    private HashMap<String, Object> getHashMap() {
        companyname = etCompanyName.getText().toString();
        name = etName.getText().toString();
        email = etEmailID.getText().toString();
        password = etPassword.getText().toString();
        confirmpwd = etCpwd.getText().toString();
        mobileno = etMobileNo.getText().toString();
        gstnin = etGstin.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("companyname", companyname);
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("cpassword", confirmpwd);
        map.put("mobile", mobileno);
        map.put("gstin", gstnin);
        map.put("cityname", selectedCityId);
        map.put("statename", selectedStateId);
        map.put("countryname", selectedCountryId);
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