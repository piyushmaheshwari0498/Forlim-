package com.example.forlempopoli.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.forlempopoli.Entity.response.VerifyResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {
//    EditText etOtpNumber,etAccountID;
    Button btnSubmit;
    AppUtils appUtils;
    String otpnumber;
    String accountId;
    TextInputEditText etAccountID;

    in.aabhasjindal.otptextview.OtpTextView OtpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Verify OTP</small></font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        Intent receiveIntent = getIntent();
        accountId = receiveIntent.getStringExtra(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);
        System.out.println("account"+accountId);
//        etOtpNumber=findViewById(R.id.etOtpNumber);
        OtpTextView = findViewById(R.id.otp_view);
        etAccountID=findViewById(R.id.etAccountID);
        otpnumber = OtpTextView.getOTP();
        btnSubmit=findViewById(R.id.btnSubmit);
        appUtils=new AppUtils();
        etAccountID.setText(accountId);
        etAccountID.setEnabled(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!appUtils.isConnected(VerifyActivity.this))
                    {
                        Custom_Toast.warning(VerifyActivity.this, getString(R.string.no_internet));
                    }
                    else if (validate()){
                        getVerifyDetails();
                    }

                } catch (Exception e) {
                    Log.e("loginError", e.getMessage());
                }
            }
        });
    }

    private void getVerifyDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<VerifyResponse> call = apiInterface.getVerifypwdDetails(getHashMap());
        call.enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg=response.body().getMessage();
                        Custom_Toast.success(VerifyActivity.this,msg);
                        Intent i=new Intent(VerifyActivity.this,ConfirmPasswordActivity.class);
                        startActivity(i);
                    }
                }
                else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Custom_Toast.warning(VerifyActivity.this,message);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(VerifyActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {

            }
        });
    }
    private boolean validate()
    {
        otpnumber = OtpTextView.getOTP();
        if(otpnumber.isEmpty())
        {
            Custom_Toast.error(getApplicationContext(),"Invalid OTP Number.Please Re-Enter the OTP");
            return false;
        }
        return true;
    }
    private HashMap<String, Object> getHashMap() {
        otpnumber = OtpTextView.getOTP();
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("otp",otpnumber);
        map.put("acc_id",accountId);
        return map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

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
