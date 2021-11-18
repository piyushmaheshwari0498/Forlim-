package com.example.forlempopoli.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forlempopoli.Adapter.ProductAdapter;
import com.example.forlempopoli.Db.connection.DatabaseHelper;
import com.example.forlempopoli.Entity.request.ProductRequest;
import com.example.forlempopoli.Entity.response.ProductResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.AutoFitGridLayoutManager;
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

public class GlobalSearchActivity extends AppCompatActivity implements ProductAdapter.HomeCallBack {

    SearchView searchView;
    AppUtils appUtils;

    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    RecyclerView product_rV;
    ProductAdapter productAdapter;
    List<ProductRequest> productRequestList;

    private DatabaseHelper mDatabaseHelper;
    AppSharedPreference appSharedPreference;

    Parcelable state;
    private int cart_count = 0;
    ProgressDialog ringProgressDialog;
    String searched;
    ImageView back_button;

    @Override
    protected void onPause() {
        super.onPause();
        state = product_rV.getLayoutManager().onSaveInstanceState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        searchView = findViewById(R.id.searchView);
        back_button = findViewById(R.id.back_button);
        product_rV = findViewById(R.id.product_rV);
        no_data_text = findViewById(R.id.no_data_text);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        no_data_text = findViewById(R.id.no_data_text);
//        ll_Data_Found = findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);
        img_info = findViewById(R.id.img_info);
        retry_btn = findViewById(R.id.retry_btn);

        appSharedPreference = AppSharedPreference.getAppSharedPreference(this.getApplicationContext());
        appUtils = new AppUtils();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                searched = query;
                if (!appUtils.isConnected(GlobalSearchActivity.this)) {
                    //no_internet();
                } else {
                    if (query.isEmpty()) {
                        //empty layout
                    } else {
                        ringProgressDialog = new ProgressDialog(GlobalSearchActivity.this);
                        ringProgressDialog.setMessage("Loading...");
                        ringProgressDialog.setCanceledOnTouchOutside(false);
//                        showLoading();
                        showProductDetails(query);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
//                Log.e("item", String.valueOf(categoryAdapter.getItemCount()));
//                Log.d(" onQueryTextChange item", String.valueOf(categoryAdapter.getItemCount()));
                searched = query;
                if (!appUtils.isConnected(GlobalSearchActivity.this)) {
                  //  no_internet();
                } else {
                    if (query.isEmpty()) {
                        //empty layout
                    } else {
                        /*ringProgressDialog = new ProgressDialog(GlobalSearchActivity.this);
                        ringProgressDialog.setMessage("Loading...");
                        ringProgressDialog.setCanceledOnTouchOutside(false);
                        showLoading();*/
                        showProductDetails(query);
                    }
                }
                return false;
            }
        });

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        product_rV.setLayoutManager(layoutManager);

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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void getCartCount() {
        cart_count = mDatabaseHelper.getCartCount(appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
    }

    private void showProductDetails(String query) {

        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductResponse> call = apiInterface.getGlobalProductDetails(getMap(query));
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        //dismissLoading();
                        productRequestList = response.body().getData();
                        if (productRequestList != null) {
                            productAdapter = new ProductAdapter(productRequestList, GlobalSearchActivity.this, GlobalSearchActivity.this);
                            product_rV.setLayoutManager(new LinearLayoutManager(GlobalSearchActivity.this));
//                            productAdapter.setStateRestorationPolicy
//                                    (RecyclerView.Adapter.StateRestorationPolicy.PREVENT);
//                            product_rV.setFocusable(false);
//                            productAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                            product_rV.setAdapter(productAdapter);
                            product_rV.setHasFixedSize(true);
                            product_rV.setItemViewCacheSize(50);
                            product_rV.setDrawingCacheEnabled(true);
                            product_rV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
                            AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(GlobalSearchActivity.this, 500);
                            product_rV.setLayoutManager(autolayoutManager);
                        } else {
                            //searchView.setEnabled(false);
                            swipeRefreshLayout.setVisibility(View.GONE);
                            product_rV.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        System.out.println("response  Not Found" + response.code());
                        //System.out.println("response msg"+response.message());
                        dismissLoading();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Log.e("error_message",message);
                            if (!appUtils.isConnected(GlobalSearchActivity.this)) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                            Toast.makeText(GlobalSearchActivity.this, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(GlobalSearchActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                        System.out.println("response" + response.code());
                        //System.out.println("response"+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                dismissLoading();
                if (!appUtils.isConnected(GlobalSearchActivity.this)) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                System.out.println("onFailure" + t.getMessage());
            }
        });
    }

    private HashMap<String, Object> getMap(String query) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acct_id", appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("art_name",query);
        return map;
    }

    void showLoading(){
        ringProgressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void dismissLoading(){
        ringProgressDialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(this)) {
            //dismissLoading();
            // searchView.setEnabled(false);
//            ll_Data_Found.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            product_rV.setVisibility(View.GONE);
            ll_NoData_Found.setVisibility(View.VISIBLE);
            retry_btn.setVisibility(View.VISIBLE);
            img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
            no_data_text.setText(getResources().getString(R.string.no_internet));
            //  Custom_Toast.warning(getContext(), getResources().getString(R.string.no_internet));
        } else {
//            searchView.setEnabled(true);
//            ll_Data_Found.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            product_rV.setVisibility(View.VISIBLE);
            ll_NoData_Found.setVisibility(View.GONE);
            showProductDetails(searched);
        }
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }

    public void no_internet() {
//        ll_Data_Found.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        product_rV.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        no_data_text.setText(getResources().getString(R.string.no_internet));
    }

    public void something_wrong() {
//        ll_Data_Found.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        product_rV.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
        no_data_text.setText(getResources().getString(R.string.something_wrong));
    }

    @Override
    public void updateCartCount(Context context) {
        cart_count = cart_count + 1;
        invalidateOptionsMenu();
    }
}