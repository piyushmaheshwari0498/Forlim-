package com.example.forlempopoli.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.forlempopoli.Adapter.SamplingDataAdapter;
import com.example.forlempopoli.Entity.request.SamplingRequest;
import com.example.forlempopoli.Entity.response.SamplingResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SamplingDataFragment extends Fragment {
    RecyclerView sampling_data_rV;
    SamplingDataAdapter samplingDataAdapter;
    List<SamplingRequest> samplingRequestList;
    private AppSharedPreference mAppSharedPrefernce;
    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    AppUtils appUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_sampling_data, container, false);
       sampling_data_rV=view.findViewById(R.id.sampling_data_rV);
       mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(getActivity());
        appUtils = new AppUtils();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        no_data_text = view.findViewById(R.id.no_data_text);
        ll_Data_Found = view.findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = view.findViewById(R.id.ll_NoData_Found);
        img_info = view.findViewById(R.id.img_info);
        retry_btn = view.findViewById(R.id.retry_btn);

        refreshItems();

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshItems();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
       return view;
    }
    public void samplingdatadata() {

        final ApiInterface apiInterface= RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SamplingResponse> call = apiInterface.showSamplingdata(getHashMap());
        call.enqueue(new Callback<SamplingResponse>() {
            @Override
            public void onResponse(Call<SamplingResponse> call, Response<SamplingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    System.out.println("Success"+response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        // ringProgressDialog.dismiss();
                        samplingRequestList= response.body().getData();
                        if(!samplingRequestList.isEmpty()) {
                            samplingDataAdapter = new SamplingDataAdapter(samplingRequestList, getActivity());
                            sampling_data_rV.setLayoutManager(new LinearLayoutManager(getActivity()));
                            sampling_data_rV.setAdapter(samplingDataAdapter);
                        }
                        else {
                            ll_Data_Found.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }

                    }
                }
                else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            if (!appUtils.isConnected(getContext())) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!appUtils.isConnected(getContext())) {
                            no_internet();
                        } else {
                            Log.e("in", "else");
                            something_wrong();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<SamplingResponse> call, Throwable t) {
                //ringProgressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                if (!appUtils.isConnected(getContext())) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
            }
        });
    }

    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("acc_id",mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(getContext())) {
            // ringProgressDialog.dismiss();
            ll_Data_Found.setVisibility(View.GONE);
            ll_NoData_Found.setVisibility(View.VISIBLE);
            retry_btn.setVisibility(View.VISIBLE);
            img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
            no_data_text.setText(getResources().getString(R.string.no_internet));
            //  Custom_Toast.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            ll_Data_Found.setVisibility(View.VISIBLE);
            ll_NoData_Found.setVisibility(View.GONE);
            samplingdatadata();
        }
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }

    public void no_internet() {
        ll_Data_Found.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        no_data_text.setText(getResources().getString(R.string.no_internet));
    }

    public void something_wrong() {
        ll_Data_Found.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
        no_data_text.setText(getResources().getString(R.string.something_wrong));
    }
}