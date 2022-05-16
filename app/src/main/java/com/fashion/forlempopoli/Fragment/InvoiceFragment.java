package com.fashion.forlempopoli.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fashion.forlempopoli.Adapter.InvoiceAdapter;
import com.fashion.forlempopoli.Entity.request.InvoiceRequest;
import com.fashion.forlempopoli.Entity.response.InvoiceResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceFragment extends Fragment {
    RecyclerView invoice_rV;
    InvoiceAdapter invoiceAdapter;
    private AppSharedPreference mAppSharedPrefernce;
    List<InvoiceRequest> invoiceRequestList;
    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    AppUtils appUtils;
    ProgressDialog ringProgressDialog;

    public InvoiceFragment() {
        // Required empty public constructor
    }


    public static InvoiceFragment newInstance(String param1, String param2) {
        InvoiceFragment fragment = new InvoiceFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);

        invoice_rV= view.findViewById(R.id.invoice_rV);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        no_data_text = view.findViewById(R.id.no_data_text);
        ll_Data_Found = view.findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = view.findViewById(R.id.ll_NoData_Found);
        img_info = view.findViewById(R.id.img_info);
        retry_btn = view.findViewById(R.id.retry_btn);
        appUtils = new AppUtils();
        mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(getContext());
        ringProgressDialog = new ProgressDialog(getContext());
        ringProgressDialog.setMessage("Loading...");
        ringProgressDialog.setCanceledOnTouchOutside(false);
        ringProgressDialog.show();

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

    public void invoiceHistory() {
        final ApiInterface apiInterface= RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<InvoiceResponse> call = apiInterface.paymentDetails(getMap());

        call.enqueue(new Callback<InvoiceResponse>() {
            @Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {
                ringProgressDialog.dismiss();
                if (response.code() == 200 && response.message().equals("OK")) {
                    System.out.println("Success"+response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        // ringProgressDialog.dismiss();
                        invoiceRequestList= response.body().getData();
                        /*invoiceRequestList = new ArrayList<>();
                        invoiceRequestList.add(new InvoiceRequest
                                (mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID),"4.00","501"));
                        invoiceRequestList.add(new InvoiceRequest
                                (mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID),"8.00","502"));*/
                        if(!invoiceRequestList.isEmpty()) {
                            invoiceAdapter = new InvoiceAdapter(invoiceRequestList, getContext());
                            invoice_rV.setLayoutManager(new LinearLayoutManager(getContext()));
                            invoice_rV.setAdapter(invoiceAdapter);
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
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                if (!appUtils.isConnected(getContext())) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("acc_id",mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(getContext())) {
            ringProgressDialog.dismiss();
            ll_Data_Found.setVisibility(View.GONE);
            ll_NoData_Found.setVisibility(View.VISIBLE);
            retry_btn.setVisibility(View.VISIBLE);
            img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
            no_data_text.setText(getResources().getString(R.string.no_internet));
            //  Custom_Toast.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            ll_Data_Found.setVisibility(View.VISIBLE);
            ll_NoData_Found.setVisibility(View.GONE);
            invoiceHistory();
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