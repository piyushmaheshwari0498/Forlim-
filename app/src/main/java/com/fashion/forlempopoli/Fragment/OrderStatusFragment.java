package com.fashion.forlempopoli.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fashion.forlempopoli.Adapter.OrderDataAdapter;
import com.fashion.forlempopoli.Entity.request.OrderDataRequest;
import com.fashion.forlempopoli.Entity.response.OrderDataResponse;
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

public class OrderStatusFragment extends Fragment {
    RecyclerView orderData_rV;
    OrderDataAdapter orderDataAdapter;
    List<OrderDataRequest> orderDataRequestList;
    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    AppUtils appUtils;
    SearchView searchView;
    private AppSharedPreference mappSharedPreference;
    ProgressDialog ringProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);
        orderData_rV = view.findViewById(R.id.orderData_rV);
        mappSharedPreference = AppSharedPreference.getAppSharedPreference(getActivity());
        appUtils = new AppUtils();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        no_data_text = view.findViewById(R.id.no_data_text);
        ll_Data_Found = view.findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = view.findViewById(R.id.ll_NoData_Found);
        img_info = view.findViewById(R.id.img_info);
        retry_btn = view.findViewById(R.id.retry_btn);
        searchView = view.findViewById(R.id.searchView);

        ringProgressDialog = new ProgressDialog(getActivity());
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted

                Log.d(" onQueryTextSubmit item", String.valueOf(orderDataAdapter.getItemCount()));
                if (!appUtils.isConnected(getContext())) {
                    no_internet();

                } else {
                    if (query.isEmpty()) {
                        refreshItems();
                    } else {
                        getFilterData(query);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
//                Log.e("item", String.valueOf(categoryAdapter.getItemCount()));
//                Log.d(" onQueryTextChange item", String.valueOf(categoryAdapter.getItemCount()));
                if (!appUtils.isConnected(getContext())) {
                    no_internet();
                } else {
                    if (query.isEmpty()) {
                        refreshItems();
                    } else {
                        getFilterData(query);
                    }
                }
                return false;
            }
        });
        return view;
    }

    private void getFilterData(String query) {

        if (orderDataAdapter != null) {
            orderDataAdapter.getFilter().filter(query);

            if (orderDataAdapter.getItemCount() != 0) {
                Log.d("filter count", String.valueOf(orderDataAdapter.getItemCount()));
                ll_Data_Found.setVisibility(View.VISIBLE);
                ll_NoData_Found.setVisibility(View.GONE);
//                AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(getContext(), 500);
//                orderData_rV.setLayoutManager(autolayoutManager);

            } else {
                ll_Data_Found.setVisibility(View.GONE);
                ll_NoData_Found.setVisibility(View.VISIBLE);
                retry_btn.setVisibility(View.GONE);
                img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                no_data_text.setText(getResources().getString(R.string.no_record_found));
            }
        }
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(getContext())) {
            ringProgressDialog.dismiss();
//            ringProgressDialog.dismiss();
            searchView.setVisibility(View.GONE);
            ll_Data_Found.setVisibility(View.GONE);
            ll_NoData_Found.setVisibility(View.VISIBLE);
            retry_btn.setVisibility(View.VISIBLE);
            img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
            no_data_text.setText(getResources().getString(R.string.no_internet));
            //  Custom_Toast.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
            searchView.setVisibility(View.VISIBLE);
            ll_Data_Found.setVisibility(View.VISIBLE);
            ll_NoData_Found.setVisibility(View.GONE);
            getOrderData();
        }
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }

    private void getOrderData() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<OrderDataResponse> call = apiInterface.getClothsOrderData(getHashMap());
        call.enqueue(new Callback<OrderDataResponse>() {
            @Override
            public void onResponse(Call<OrderDataResponse> call, Response<OrderDataResponse> response) {
                ringProgressDialog.dismiss();
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        orderDataRequestList = response.body().getData();

                        if (!orderDataRequestList.isEmpty()) {
                            orderDataAdapter = new OrderDataAdapter(orderDataRequestList, getActivity());
                            orderData_rV.setLayoutManager(new LinearLayoutManager(getActivity()));
                            orderData_rV.setAdapter(orderDataAdapter);
                        } else {
                            searchView.setVisibility(View.GONE);
                            ll_Data_Found.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            if (!appUtils.isConnected(getContext())) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDataResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                if (!appUtils.isConnected(getContext())) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("client_id", mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

    public void no_internet() {
        searchView.setVisibility(View.GONE);
        ll_Data_Found.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        no_data_text.setText(getResources().getString(R.string.no_internet));
    }

    public void something_wrong() {
        searchView.setVisibility(View.GONE);
        ll_Data_Found.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
        no_data_text.setText(getResources().getString(R.string.something_wrong));
    }
}