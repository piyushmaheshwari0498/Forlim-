package com.fashion.forlempopoli.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fashion.forlempopoli.Activity.CustomerLegerActivity;
import com.fashion.forlempopoli.Entity.response.CustomerLegerResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerLedgerFragment extends Fragment {
    TextInputEditText etFromDate, etToDate;
    TextInputLayout fromdateInputLayout, todateInputLayout;
    Calendar calendar;
    int mDay, mMonth, mYear;
    DatePickerDialog datePickerDialog;
    String fromdate, todate;
    Button btn_save;
    String resulturl;
    AppUtils appUtils;
    private AppSharedPreference mappSharedPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_ledger, container, false);
        etFromDate = view.findViewById(R.id.etFromDate);
        etToDate = view.findViewById(R.id.etToDate);
        btn_save = view.findViewById(R.id.btn_save);
        fromdateInputLayout = view.findViewById(R.id.fromdateInputLayout);
        todateInputLayout = view.findViewById(R.id.todateInputLayout);

        mappSharedPreference = AppSharedPreference.getAppSharedPreference(getActivity());
        appUtils = new AppUtils();

        mappSharedPreference = AppSharedPreference.getAppSharedPreference(getActivity());


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateFrom() | !validateTo()){
                    return;
                }
                reportsDetails();
            }
        });

        showDatePicker();

        return view;
    }

    public void showDatePicker(){
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int newMonth = monthOfYear + 1;
                                String monthObtained = newMonth < 10 ? "0" + newMonth : String.valueOf(newMonth);
                                String dayObtained = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                String displayDate = dayObtained + "-" + monthObtained + "-" + year;
                                etFromDate.setText(displayDate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int newMonth = monthOfYear + 1;
                                String monthObtained = newMonth < 10 ? "0" + newMonth : String.valueOf(newMonth);
                                String dayObtained = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                String displaydate = dayObtained + "-" + monthObtained + "-" + year;
                                etToDate.setText(displaydate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    public boolean validateFrom(){
        fromdate = etFromDate.getText().toString();
        if(fromdate.isEmpty()){
            fromdateInputLayout.setError("Please Select From Date");
            return false;
        }
        else {
            fromdateInputLayout.setError(null);
            fromdateInputLayout.setEnabled(false);
            return true;
        }
    }

    public boolean validateTo(){
        todate = etToDate.getText().toString();
        if(todate.isEmpty()){
            todateInputLayout.setError("Please Select To Date");
            return false;
        }
        else {
            todateInputLayout.setError(null);
            todateInputLayout.setEnabled(false);
            return true;
        }
    }

    private void reportsDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<CustomerLegerResponse> call = apiInterface.customerLedgerDetails(getMap());
        call.enqueue(new Callback<CustomerLegerResponse>() {
            @Override
            public void onResponse(Call<CustomerLegerResponse> call, Response<CustomerLegerResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        resulturl = response.body().getResultUrl();
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resulturl));
//                        startActivity(browserIntent);
                        Intent intent = new Intent(getActivity(), CustomerLegerActivity.class);
                        intent.putExtra("request_pdf", resulturl);
                        intent.putExtra("fromdate", fromdate);
                        intent.putExtra("todate", todate);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        // ringProgressDialog.dismiss();
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
            public void onFailure(Call<CustomerLegerResponse> call, Throwable t) {
                // ringProgressDialog.dismiss();
                System.out.println("onFailure" + t.getMessage());
            }
        });
    }

    private HashMap<String, Object> getMap() {
        fromdate = etFromDate.getText().toString();
        todate = etToDate.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("from_date", fromdate);
        map.put("to_date", todate);
        return map;
    }

    @Override
    public void onResume() {
        super.onResume();
        etFromDate.setEnabled(true);
        etToDate.setEnabled(true);
        showDatePicker();
    }

}