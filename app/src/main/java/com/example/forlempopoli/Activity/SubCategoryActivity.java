package com.example.forlempopoli.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.forlempopoli.Adapter.SubCategoryAdapter;
import com.example.forlempopoli.Db.connection.DatabaseHelper;
import com.example.forlempopoli.Entity.request.SubCategoryRequest;
import com.example.forlempopoli.Entity.response.SubCategoryResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.AutoFitGridLayoutManager;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.Converter;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity /*implements SubCategoryAdapter.HomeCallBack */ {
    ProgressDialog ringProgressDialog;
    SubCategoryAdapter subCategoryAdapter;
    List<SubCategoryRequest> subCategoryRequestList;
    RecyclerView subcategory_rV;
    String categoryID, category_name;
//    SearchView search;
    MaterialToolbar toolbar;
    TextView no_data_text;
    AppUtils appUtils;
    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    AppSharedPreference mappSharedPrefernce;
    boolean scroll_down = false;
    private int cart_count = 0;
    private DatabaseHelper mDatabaseHelper;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);


        mappSharedPrefernce = AppSharedPreference.getAppSharedPreference(this.getApplicationContext());
        Intent recieveIntent = getIntent();
        appUtils = new AppUtils();
        category_name = String.valueOf(recieveIntent.getStringExtra(Constants.INTENT_KEYS.KEY_CATEGORY_NAME));
        //setTitle(Html.fromHtml("<font color=\"black\">" + category_name + "</font>"));
        //((AppCompatActivity)SubCategoryActivity.this).getSupportActionBar().setTitle(category_name);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.logo_1);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category_name);
        //status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        no_data_text = findViewById(R.id.no_data_text);
        ll_Data_Found = findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);
        img_info = findViewById(R.id.img_info);
        retry_btn = findViewById(R.id.retry_btn);

        no_data_text = findViewById(R.id.no_data_text);
        subcategory_rV = findViewById(R.id.subcategory_rV);

        //ViewCompat.setNestedScrollingEnabled(subcategory_rV, true);

        Intent intent = getIntent();
        categoryID = String.valueOf(intent.getStringExtra(Constants.INTENT_KEYS.KEY_CATEGORY_ID));
        System.out.println("category" + categoryID);
        ringProgressDialog = new ProgressDialog(SubCategoryActivity.this);
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

        final int[] state = new int[1];

        subcategory_rV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state[0] = newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && (state[0] == 0 || state[0] == 2)) {
                    hideViews();
                } else if (dy < -10) {
                    showViews();
                }
            }
        });
       /* subcategory_rV.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (scroll_down) {
                    toolbar.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 70) {
                    //scroll down
                    scroll_down = true;

                } else if (dy < -5) {
                    //scroll up
                    scroll_down = false;
                }
            }
        });*/

       /* subcategory_rV.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });
*/

        if (!appUtils.isConnected(SubCategoryActivity.this)) {
            Toasty.warning(this, getResources().getString(R.string.no_internet));
            ringProgressDialog.dismiss();

        } else {
            mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());
            getCartCount();
            refreshItems();
        }

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        subcategory_rV.setLayoutManager(layoutManager);

        subcategory_rV.setHasFixedSize(true);
        subcategory_rV.setItemViewCacheSize(20);
        subcategory_rV.setDrawingCacheEnabled(true);
        subcategory_rV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @SuppressLint("RestrictedApi")
    private void hideViews() {
        //toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        getSupportActionBar().setShowHideAnimationEnabled(true);
        toolbar.setVisibility(View.GONE);


    }

    @SuppressLint("RestrictedApi")
    private void showViews() {
        // toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        getSupportActionBar().setShowHideAnimationEnabled(true);
        toolbar.setVisibility(View.VISIBLE);

    }

    /*@Override
    public void updateCartCount(Context context) {
        cart_count = cart_count+1;
        invalidateOptionsMenu();
    }*/

    private void getFilterData(String query) {
        if (subCategoryAdapter != null) {
            subCategoryAdapter.getFilter().filter(query);
            /*if (subCategoryAdapter.getItemCount() != 0) {
                subcategory_rV.setVisibility(View.VISIBLE);
                no_data_text.setVisibility(View.GONE);
                AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(this, 500);
                subcategory_rV.setLayoutManager(autolayoutManager);
            } else {
                subcategory_rV.setVisibility(View.GONE);
                no_data_text.setVisibility(View.VISIBLE);
            }*/
            if (subCategoryAdapter.getItemCount() != 0) {
                Log.d("filter count", String.valueOf(subCategoryAdapter.getItemCount()));
                ll_Data_Found.setVisibility(View.VISIBLE);
                ll_NoData_Found.setVisibility(View.GONE);
                AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(this, 500);
                subcategory_rV.setLayoutManager(autolayoutManager);

            } else {
                ll_Data_Found.setVisibility(View.GONE);
                ll_NoData_Found.setVisibility(View.VISIBLE);
                retry_btn.setVisibility(View.GONE);
                img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                no_data_text.setText(getResources().getString(R.string.no_record_found));
            }
        }
    }

    private void showSubcategoryDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SubCategoryResponse> call = apiInterface.getSubCategoryDetails(getMap());
        call.enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        ringProgressDialog.dismiss();
                        subCategoryRequestList = response.body().getData();

                        if (!subCategoryRequestList.isEmpty()) {
                            subCategoryAdapter = new SubCategoryAdapter(subCategoryRequestList, SubCategoryActivity.this, categoryID);
//                        subcategory_rV.setLayoutManager(new LinearLayoutManager(SubCategoryActivity.this));
                            subcategory_rV.setAdapter(subCategoryAdapter);
                            subcategory_rV.setHasFixedSize(true);
                            subcategory_rV.setItemViewCacheSize(50);
                            subcategory_rV.setDrawingCacheEnabled(true);
                            subcategory_rV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
                            AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(SubCategoryActivity.this, 500);
                            subcategory_rV.setLayoutManager(autolayoutManager);
                        } else {
                            ll_Data_Found.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        ringProgressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            if (!appUtils.isConnected(SubCategoryActivity.this)) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                            Toast.makeText(SubCategoryActivity.this, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!appUtils.isConnected(SubCategoryActivity.this)) {
                            no_internet();
                        } else {
                            Log.e("in", "else");
                            something_wrong();
                        }
                        Toast.makeText(SubCategoryActivity.this, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                if (!appUtils.isConnected(SubCategoryActivity.this)) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                System.out.println("onFailure" + t.getMessage());
            }
        });
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("category_id", categoryID);
        return map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent mintent;
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.cart_action) {
            if (cart_count < 1) {
                Toast.makeText(this, "There is no item in cart", Toast.LENGTH_SHORT).show();
            } else {
                mintent = new Intent(SubCategoryActivity.this, CartActivity.class);
                startActivity(mintent);
            }
        } else if (item.getItemId() == R.id.search_action) {
            mintent = new Intent(SubCategoryActivity.this, GlobalSearchActivity.class);
            startActivity(mintent);
        }
        return super.onOptionsItemSelected(item);
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

    private void getCartCount() {
        cart_count = mDatabaseHelper.getCartCount(mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clothes_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        MenuItem notiItem = menu.findItem(R.id.notification_action);
        notiItem.setVisible(false);
        MenuItem searchmenuItem = menu.findItem(R.id.search_action);
        menuItem.setIcon(Converter.convertLayoutToImage(SubCategoryActivity.this, cart_count, R.drawable.shopping_bag));
        searchmenuItem.setIcon(Converter.convertLayoutToImage(SubCategoryActivity.this, 0, R.drawable.ic_baseline_search_24));

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!appUtils.isConnected(SubCategoryActivity.this)) {
            Toasty.warning(this, getResources().getString(R.string.no_internet));
            ringProgressDialog.dismiss();
        } else {
            getCartCount();
            refreshItems();
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!appUtils.isConnected(SubCategoryActivity.this)) {
            Toasty.warning(this, getResources().getString(R.string.no_internet));
            ringProgressDialog.dismiss();
        } else {
            getCartCount();
            refreshItems();
        }
        invalidateOptionsMenu();
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(this)) {
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
            showSubcategoryDetails();
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
