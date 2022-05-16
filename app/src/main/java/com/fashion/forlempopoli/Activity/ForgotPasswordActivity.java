package com.fashion.forlempopoli.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fashion.forlempopoli.Entity.response.ForgotPasswordResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.Custom_Toast;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText etMobileNumber;
    TextInputLayout inputLayoutMobileNumber;
    Button submitBtn;
    String mobileno;
    AppUtils appUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Forgot Password</small></font>"));
        //setTitle(Html.fromHtml("<small>Forgot Password</small>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        etMobileNumber = findViewById(R.id.etMobileNo);
        inputLayoutMobileNumber = findViewById(R.id.mobileInputLayout);
        submitBtn = findViewById(R.id.submitBtn);
        appUtils = new AppUtils();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btn");
                try {
                    if (!appUtils.isConnected(ForgotPasswordActivity.this)) {
                        Custom_Toast.warning(ForgotPasswordActivity.this, getString(R.string.no_internet));
                    } else if (validate()) {
                        forgetpassword();
                    }

                } catch (Exception e) {
                    Log.e("loginError", e.getMessage());
                }
            }
        });
        etMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                inputLayoutMobileNumber.setError("");

            }
        });
    }

    private void forgetpassword() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ForgotPasswordResponse> call = apiInterface.getForgotpwdDetails(getHashMap());
        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 &&
                            response.body().getStatusMessage().equals("Success")) {
                        String accountId = response.body().getAccId();
                        System.out.println("accountid" + accountId);
                        String msg = response.body().getMessage();
                        System.out.println("msg"+msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyActivity.class);
                        intent.putExtra(Constants.INTENT_KEYS.KEY_ACCOUNT_ID, accountId);
                        startActivity(intent);
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Log.d("error",message);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if (etMobileNumber.getText().toString().equals("")) {
                                inputLayoutMobileNumber.setError("Please Enter Mobile Number");
                            } else if (etMobileNumber.getText().length() != 10) {
                                Log.e("mob", String.valueOf(etMobileNumber.getText()));
                                inputLayoutMobileNumber.setError("Please Enter 10 digit Mobile Number");
                            } else if (response.code() == 404) {
                                //Mobile Number Condition
                                inputLayoutMobileNumber.setError("Please Enter Correct Mobile Number");
                            } else {
                                inputLayoutMobileNumber.setError("");
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {

            }
        });
    }

    private boolean validate() {
        String MobilePattern = "[0-9]{10}";
        mobileno = etMobileNumber.getText().toString().trim();
        System.out.println("validate" + mobileno);

        if (mobileno.isEmpty()) {
//            Custom_Toast.error(getApplicationContext(), "Invalid Mobile Number.Please Re-Enter the Mobile Number");
            inputLayoutMobileNumber.setError("Please Enter Mobile Number");

            return false;
        } else if (!mobileno.matches(MobilePattern)) {
            inputLayoutMobileNumber.setError("Please Enter 10 digit Mobile Number");
//            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            inputLayoutMobileNumber.setError("");
        }

        return true;
    }

    private HashMap<String, Object> getHashMap() {
        mobileno = etMobileNumber.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("loginid", mobileno);
        System.out.println("indos" + mobileno);
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