package com.fashion.forlempopoli.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Toast;
import com.fashion.forlempopoli.Adapter.OrderDataAdapter;
import com.fashion.forlempopoli.Entity.request.OrderDataRequest;
import com.fashion.forlempopoli.Entity.response.OrderDataResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDataActivity extends AppCompatActivity {
   private AppSharedPreference mappSharedPreference;
   RecyclerView  orderData_rV;
   OrderDataAdapter orderDataAdapter;
   String smId;
    List<OrderDataRequest> orderDataRequestList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_data);
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Order Items</small></font>"));

        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        orderData_rV=findViewById(R.id.orderData_rV);
        mappSharedPreference=AppSharedPreference.getAppSharedPreference(OrderDataActivity.this);
        getOrderData();
    }
    private void getOrderData() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<OrderDataResponse> call = apiInterface.getClothsOrderData(getHashMap());
        call.enqueue(new Callback<OrderDataResponse>() {
            @Override
            public void onResponse(Call<OrderDataResponse> call, Response<OrderDataResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        orderDataRequestList=response.body().getData();

                        orderDataAdapter=new OrderDataAdapter(orderDataRequestList,OrderDataActivity.this);
                        orderData_rV.setLayoutManager(new LinearLayoutManager(OrderDataActivity.this));
                        orderData_rV.setAdapter(orderDataAdapter);
                    }
                }
                else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(OrderDataActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDataResponse> call, Throwable t) {
                Toast.makeText(OrderDataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, Object> getHashMap() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("client_id",mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
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
