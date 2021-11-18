package com.example.forlempopoli.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.forlempopoli.Adapter.OrderDataAdapter;
import com.example.forlempopoli.Adapter.OrderDetailsAdapter;
import com.example.forlempopoli.Entity.request.DetailsOrderRequest;
import com.example.forlempopoli.Entity.request.OrderDataRequest;
import com.example.forlempopoli.Entity.response.DetailsOrderResponse;
import com.example.forlempopoli.Entity.response.OrderDataResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    RecyclerView orderDetails_rV;
    String amid;
    OrderDetailsAdapter orderDetailsAdapter;
    Button btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        orderDetails_rV=findViewById(R.id.orderDetails_rV);
        btnContinue=findViewById(R.id.btnContinue);
        Intent intent = getIntent();
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Bill Details : "+intent.getStringExtra("order_id")+"</small></font>"));
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        Intent receiveIntent=getIntent();
        amid=receiveIntent.getStringExtra(Constants.INTENT_KEYS.KEY_AM_ID);
        System.out.println("amid"+amid);
        getOrderDetails();

    }
    private void getOrderDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<DetailsOrderResponse> call = apiInterface.getClothsOrderDetails(getHashMap());
        call.enqueue(new Callback<DetailsOrderResponse>() {
            @Override
            public void onResponse(Call<DetailsOrderResponse> call, Response<DetailsOrderResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        List<DetailsOrderRequest> detailsOrderRequestList=response.body().getData();
                        orderDetailsAdapter=new OrderDetailsAdapter(detailsOrderRequestList,OrderDetailsActivity.this);
                        orderDetails_rV.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
                        orderDetails_rV.setAdapter(orderDetailsAdapter);
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
                        Toast.makeText(OrderDetailsActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsOrderResponse> call, Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private HashMap<String, Object> getHashMap() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("am_id",amid);
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
