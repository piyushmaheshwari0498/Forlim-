package com.example.forlempopoli.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Adapter.PaymentDetailsAdapter;
import com.example.forlempopoli.Entity.request.PaymentDetailsRequest;
import com.example.forlempopoli.Entity.response.PaymentDetailsResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsFragment extends Fragment {
    RecyclerView rv_payment_details;
    PaymentDetailsAdapter mpaymentDetailsAdapter;
    List<PaymentDetailsRequest> paymentDetailsRequestList;
    String smid;
    private AppSharedPreference mAppSharedPrefernce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_details, container, false);
        rv_payment_details = view.findViewById(R.id.rv_payment_details);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(getActivity());
//        getPaymentDetails();
        return view;
    }

   /* private void getPaymentDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<PaymentDetailsResponse> call = apiInterface.paymentDetails(getHashMap());
        call.enqueue(new Callback<PaymentDetailsResponse>() {
            @Override
            public void onResponse(Call<PaymentDetailsResponse> call, Response<PaymentDetailsResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        paymentDetailsRequestList = response.body().getData();
                        smid = paymentDetailsRequestList.get(0).getSmId();
                        System.out.println("getSmId payment" + smid);
                        mpaymentDetailsAdapter = new PaymentDetailsAdapter(paymentDetailsRequestList, getActivity());
                        rv_payment_details.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_payment_details.setAdapter(mpaymentDetailsAdapter);
                    }
                } else {
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
            public void onFailure(Call<PaymentDetailsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        System.out.println(" Payment acc_id" + mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }
}