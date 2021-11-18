package com.example.forlempopoli.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.forlempopoli.Entity.response.ConfirmPwdResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.LoginActivity;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPasswordActivity extends AppCompatActivity {
    EditText editTextAccount;
    TextInputEditText etNewPassword, etConfirmPassword;
    Button btnsave;
    String newpwd, confirmpwd, accountId;
    AppUtils appUtils;
    TextInputLayout passwordInputLayout, confrimPasswordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Confirm Password</small></font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appUtils = new AppUtils();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        editTextAccount = findViewById(R.id.editTextAccount);

        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confrimPasswordInputLayout = findViewById(R.id.confrimPasswordInputLayout);

        etNewPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnsave = findViewById(R.id.btnsave);

        passwordInputLayout.setEndIconVisible(false);
        confrimPasswordInputLayout.setEndIconVisible(false);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btn");
                try {
                    if (!appUtils.isConnected(ConfirmPasswordActivity.this)) {
                        Custom_Toast.warning(ConfirmPasswordActivity.this, getString(R.string.no_internet));
                    } else if (validate()) {
                        getConfirmpwd();
                    }

                } catch (Exception e) {
                    Log.e("loginError", e.getLocalizedMessage());
                }
            }
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setEndIconVisible(!Objects.requireNonNull(etNewPassword.getText()).toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confrimPasswordInputLayout.setEndIconVisible(!Objects.requireNonNull(etConfirmPassword.getText()).toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                passwordInputLayout.setError("");

            }
        });

        confrimPasswordInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                etConfirmPassword.setError("");

            }
        });
    }

    private void getConfirmpwd() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ConfirmPwdResponse> call = apiInterface.getConfirmpwdDetails(getHashMap());
        call.enqueue(new Callback<ConfirmPwdResponse>() {
            @Override
            public void onResponse(Call<ConfirmPwdResponse> call, Response<ConfirmPwdResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg = response.body().getMessage();
                        Log.d("OTP",msg);
                        Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ConfirmPasswordActivity.this, LoginActivity.class);
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
                        Toast.makeText(ConfirmPasswordActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConfirmPwdResponse> call, Throwable t) {

            }
        });
    }

    private boolean validate() {
        accountId = editTextAccount.getText().toString();
        newpwd = Objects.requireNonNull(etNewPassword.getText()).toString();
        confirmpwd = Objects.requireNonNull(etConfirmPassword.getText()).toString();

        if (accountId.isEmpty()) {
            Log.d("validate if 1","empty");
            Custom_Toast.warning(getApplicationContext(), "Please Enter the Account Id");
            return false;
        } else if (newpwd.isEmpty()) {
//            Custom_Toast.error(getApplicationContext(), "Invalid details.Please Re-Enter the New password");
            passwordInputLayout.setError("Please Enter the New password");
            return false;
        } else if (confirmpwd.isEmpty()) {
            confrimPasswordInputLayout.setError("Please Enter the Confirm password");
//            Custom_Toast.error(getApplicationContext(), "Invalid details.Please Re-Enter the Confirm password");
            return false;
        }
        else if (!newpwd.equals(confirmpwd)) {
            confrimPasswordInputLayout.setError("Confirm password should match");
//            Custom_Toast.error(getApplicationContext(), "Invalid details.Please Re-Enter the Confirm password");
            return false;
        }
        return true;
    }

    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", accountId);
        System.out.println("accountId" + accountId);
        map.put("new_password", newpwd);
        map.put("cnew_password", confirmpwd);
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
