package com.fashion.forlempopoli;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fashion.forlempopoli.Activity.ForgotPasswordActivity;
import com.fashion.forlempopoli.Activity.RegistrationActivity;
import com.fashion.forlempopoli.Entity.request.CreditDetailsRequest;
import com.fashion.forlempopoli.Entity.request.LoginRequest;
import com.fashion.forlempopoli.Entity.response.CreditDetailsResponse;
import com.fashion.forlempopoli.Entity.response.LoginResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.Custom_Toast;
import com.fashion.forlempopoli.Utilities.HideKeyboard;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.fashion.forlempopoli.terms.TermsActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvForgotpwd, tvRegister,terms;
    TextInputEditText etMobileNo, etPassword;
    Button btnLogin;
    AppUtils appUtils;
     ProgressDialog ringProgressDialog;
    String mobileno, password;
    List<LoginRequest> list;
    List<CreditDetailsRequest> creditDetailsRequestList;
    String status;
    TextInputLayout mobileLayout, passwordLayout;
    HideKeyboard hideKeyboard;
    private AppSharedPreference mAppSharedPrefernce;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvForgotpwd = findViewById(R.id.tvForgotpwd);
        terms = findViewById(R.id.terms);
        tvRegister = findViewById(R.id.tvRegister);
        etPassword = findViewById(R.id.etPassword);
        etMobileNo = findViewById(R.id.etMobileNo);
        mobileLayout = findViewById(R.id.mobileInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        btnLogin = findViewById(R.id.btnLogin);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(this);
        appUtils = new AppUtils();

        hideKeyboard = new HideKeyboard();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
          ringProgressDialog = new ProgressDialog(LoginActivity.this);


        passwordLayout.setEndIconVisible(!etPassword.getText().toString().equals(""));

        etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    //do here your stuff f
                    if (!appUtils.isConnected(LoginActivity.this)) {
                        Custom_Toast.warning(LoginActivity.this, getString(R.string.no_internet));
                    } else {
                        ringProgressDialog.setMessage("Loading...");
                        ringProgressDialog.show();
                        hideKeyboard.hide(LoginActivity.this);
                        addLoginDetails();
                    }
                    return true;
                }
                return false;
            }
        });

        etMobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mobileLayout.setError("");

            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                passwordLayout.setError("");

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout.setEndIconVisible(!etPassword.getText().toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnLogin.setOnClickListener(this);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, TermsActivity.class);
                startActivity(i);
            }
        });

        tvForgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        if (mAppSharedPrefernce.getBooleanValue(Constants.IS_LOGGED_IN)) {
            Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mIntent);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                try {
                    if (!appUtils.isConnected(LoginActivity.this)) {
                        Custom_Toast.warning(LoginActivity.this, getString(R.string.no_internet));
                    } else {
                        ringProgressDialog.setMessage("Loading...");
                        ringProgressDialog.show();

                        hideKeyboard.hide(LoginActivity.this);
                        if (!validateMobile() | !validatePassword()) {
                            ringProgressDialog.dismiss();
                            return;
                        }
                        addLoginDetails();
                    }

                } catch (Exception e) {
                    Log.e("loginError", e.getMessage());
                }
        }
    }

    /*public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }*/

    public boolean validateMobile() {
        String mob = etMobileNo.getText().toString().trim();

        if (mob.isEmpty()) {
            mobileLayout.setError("Please Enter Mobile Number");
            return false;
        } else if (mob.length() != 10) {
            Log.e("mob", mob);
            mobileLayout.setError("Please Enter 10 digit Mobile Number");
            return false;
        } else {
            mobileLayout.setError(null);
            mobileLayout.setErrorEnabled(false);
            return true;
        }

    }

    public boolean validatePassword() {
        String pass = etPassword.getText().toString().trim();
        if (pass.isEmpty()) {
            passwordLayout.setError("Please Enter Password");
            return false;
        } else {
            passwordLayout.setError(null);
            passwordLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void addLoginDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.getLoginDetails(getHashMap());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                status = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_STATUS);
                System.out.println("status" + status);
                if (response.code() == 200 && response.message().equals("OK")) {
                    // System.out.println("success" + response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        ringProgressDialog.dismiss();
                        list = response.body().getData();
                        if (!list.isEmpty()) {
                            // TODO: 19-08-2020 Saving Data to SharedPreference
                            mAppSharedPrefernce.putStringValue(Constants.SHARED_PREFERENCE_KEYS.KEY_MOBILE_NUM, list.get(0).getAccMobile());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_ACCOUNT_ID, list.get(0).getAccId());
                            System.out.println("getAccId login" + list.get(0).getAccId());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_ACCOUNT_NAME, list.get(0).getAccName());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_ACCOUNT_PERSON_NAME, list.get(0).getAccContactPerson());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_CUSTOMER_TYPE, list.get(0).getAccType());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_CREDIT_DAYS, list.get(0).getAccCrDays());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_STATUS, list.get(0).getAccStatus());
                            System.out.println("getAccStatus" + list.get(0).getAccStatus());
                            //System.out.println("getAccLimit"+list.get(0).getAccLimit());
                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_ACC_CR_LIMIT, list.get(0).getAccCrLimit());
                            //System.out.println("getcrlimit"+list.get(0).getAccCrLimit());
                            mAppSharedPrefernce.putBooleanValue(Constants.IS_LOGGED_IN, true);

                            Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mIntent);

                            fetchCreditDetails();
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            ringProgressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errormessage = jsonObject.getString("message");
                            System.out.println("msg" + errormessage);
                            Log.d("error message", errormessage);
                            if (errormessage.toLowerCase().contains("password")) {
                                passwordLayout.setError("Please Enter Valid Password");
                            } else if (errormessage.toLowerCase().contains("loginid")) {
                                mobileLayout.setError("Please Enter Valid Mobile Number");
                            }
                            else{
                                Custom_Toast.warning(LoginActivity.this,
                                        getResources().getString(R.string.contact_admin));
                            }

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ringProgressDialog.dismiss();
                        Custom_Toast.warning(LoginActivity.this,
                                getResources().getString(R.string.something_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                Custom_Toast.warning(LoginActivity.this, t.getMessage());

            }
        });
    }


    private HashMap<String, Object> getHashMap() {
        mobileno = etMobileNo.getText().toString();
        password = etPassword.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("loginid", mobileno);
        map.put("password", password);
        return map;
    }

    private void fetchCreditDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<CreditDetailsResponse> call = apiInterface.getCreditDetails(getCreditHashMap());
        call.enqueue(new Callback<CreditDetailsResponse>() {
            @Override
            public void onResponse(Call<CreditDetailsResponse> call, Response<CreditDetailsResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    // System.out.println("success" + response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        creditDetailsRequestList = response.body().getData();
                        if (!list.isEmpty()) {
                            // TODO: 19-08-2020 Saving Data to SharedPreference
                            System.out.println("getAccLimit" + creditDetailsRequestList.get(0).getAccLimit());
                            System.out.println("getPendingBalance" + creditDetailsRequestList.get(0).getPendingBalance());
                            System.out.println("getTotalBalance" + creditDetailsRequestList.get(0).getTotalBalance());
                            System.out.println("getVoucher_amt" + creditDetailsRequestList.get(0).getVoucher_amt());

                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_CREDIT_LIMIT_AMOUNT,
                                    String.valueOf(creditDetailsRequestList.get(0).getAccLimit()));

                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_PENDING_BAL,
                                    String.valueOf(creditDetailsRequestList.get(0).getPendingBalance()));

                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_TOTAL_BALANCE,
                                    String.valueOf(creditDetailsRequestList.get(0).getTotalBalance()));

                            mAppSharedPrefernce.putStringValue(Constants.INTENT_KEYS.KEY_VOUCHER_AMT,
                                    String.valueOf(creditDetailsRequestList.get(0).getVoucher_amt()));

                            Log.d("KEY_PENDING_BAL", String.valueOf(mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_PENDING_BAL)));
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errormessage = jsonObject.getString("message");
                            System.out.println("msg" + errormessage);
                            Log.d("error message", errormessage);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Custom_Toast.warning(LoginActivity.this,
                                getResources().getString(R.string.something_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<CreditDetailsResponse> call, Throwable t) {
                System.out.println("msg" + t.getMessage());
                Log.d("error message", t.getMessage());
                Custom_Toast.warning(LoginActivity.this, t.getMessage());

            }
        });
    }


    private HashMap<String, Object> getCreditHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // System.out.println("Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //  System.out.println("Potrait");
        }
    }

}
