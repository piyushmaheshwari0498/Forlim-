package com.example.forlempopoli.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.forlempopoli.Activity.ConfirmPasswordActivity;
import com.example.forlempopoli.Entity.response.ConfirmPwdResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.LoginActivity;
import com.example.forlempopoli.MainActivity;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Change_Password_Fragment extends Fragment {
    TextInputEditText etNewPassword, etConfirmPassword;
    Button btnsave;
    String newpwd, confirmpwd, accountId;
    AppUtils appUtils;
    TextInputLayout passwordInputLayout, confrimPasswordInputLayout;
    AppSharedPreference mappSharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change__password, container, false);

        appUtils = new AppUtils();
        mappSharedPreference = AppSharedPreference.getAppSharedPreference(getActivity());

        accountId = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);

        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);
        confrimPasswordInputLayout = view.findViewById(R.id.confrimPasswordInputLayout);

        etNewPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);

        btnsave = view.findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btn");
                try {
                    if (!appUtils.isConnected(getContext())) {
                        Custom_Toast.warning(getContext(), getString(R.string.no_internet));
                    } else if (validate()) {
                        getConfirmpwd();
                    }

                } catch (Exception e) {
                    Log.e("loginError", e.getLocalizedMessage());
                }
            }
        });

        return  view;
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

                        Custom_Toast.add_cart_toast(getContext(), "Password Successfully Changed"); ;
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getContext(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ConfirmPwdResponse> call, Throwable t) {

            }
        });
    }

    private boolean validate() {
        newpwd = Objects.requireNonNull(etNewPassword.getText()).toString();
        confirmpwd = Objects.requireNonNull(etConfirmPassword.getText()).toString();

        if (newpwd.isEmpty()) {
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
}