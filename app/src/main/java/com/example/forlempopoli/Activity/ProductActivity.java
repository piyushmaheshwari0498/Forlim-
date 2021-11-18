package com.example.forlempopoli.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.forlempopoli.Utilities.Converter;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.HomeCallBack {
    String category, subCategory;
    ProgressDialog ringProgressDialog;
    RecyclerView product_rV;
    ProductAdapter productAdapter;
    List<ProductRequest> productRequestList;
    Toolbar toolbar;
    TextView no_data_text;
    ImageView tvfilter_action;
    AppUtils appUtils;
    String category_name;
    //    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;
    AppSharedPreference appSharedPreference;
    Parcelable state;
    TextView tv_data_count;
    //ArrayList<ProductRequest> arraylist;
    int[] scroll_state;
    boolean toogleisChecked = true;
    private int cart_count = 0;
    private DatabaseHelper mDatabaseHelper;
    private boolean sortAscending = true;
    private boolean sortPriceAscending = true;

    @Override
    protected void onPause() {
        super.onPause();
        state = product_rV.getLayoutManager().onSaveInstanceState();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        product_rV = findViewById(R.id.product_rV);
        no_data_text = findViewById(R.id.no_data_text);
        tvfilter_action = findViewById(R.id.tvfilter_action);
        appSharedPreference = AppSharedPreference.getAppSharedPreference(this.getApplicationContext());
        Intent recieveIntent = getIntent();
        category_name = String.valueOf(recieveIntent.getStringExtra(Constants.INTENT_KEYS.KEY_CATEGORY_NAME));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(category_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        tv_data_count = findViewById(R.id.tv_data_count);
        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        Intent receiveIntent = getIntent();
        category = receiveIntent.getStringExtra(Constants.INTENT_KEYS.KEY_CATEGORY_ID);
        subCategory = receiveIntent.getStringExtra(Constants.INTENT_KEYS.KEY_SUB_CATEGORY_ID);

        appUtils = new AppUtils();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        no_data_text = findViewById(R.id.no_data_text);
//        ll_Data_Found = findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);
        img_info = findViewById(R.id.img_info);
        retry_btn = findViewById(R.id.retry_btn);


        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        product_rV.setLayoutManager(layoutManager);


        ringProgressDialog = new ProgressDialog(ProductActivity.this);
        ringProgressDialog.setMessage("Loading...");
        ringProgressDialog.setCanceledOnTouchOutside(false);
        showLoading();

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

        scroll_state = new int[1];

        product_rV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scroll_state[0] = newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && (scroll_state[0] == 0 || scroll_state[0] == 2)) {
                    hideViews();
                } else if (dy < -10) {
                    showViews();
                }
            }
        });

        tvfilter_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

    }

    private void showBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product_filter);

        ChipGroup group = bottomSheetDialog.findViewById(R.id.group);
        ChipGroup group_price = bottomSheetDialog.findViewById(R.id.group_price);
        TextView apply_filter = bottomSheetDialog.findViewById(R.id.apply_filter);
        SwitchCompat switchAB = bottomSheetDialog.findViewById(R.id.switchAB);

        Chip ascchip = bottomSheetDialog.findViewById(R.id.option_1);
        Chip descchip = bottomSheetDialog.findViewById(R.id.option_2);
        Chip ascPricechip = bottomSheetDialog.findViewById(R.id.option_high_pice);
        Chip descPricechip = bottomSheetDialog.findViewById(R.id.option_low_price);

        bottomSheetDialog.show();

        group.setSingleSelection(true);

        if(sortAscending){
            ascchip.setChecked(true);
        }
        else {
            descchip.setChecked(true);
        }

        if(sortPriceAscending){
            ascPricechip.setChecked(true);
        }
        else {
            descPricechip.setChecked(true);
        }
        if(toogleisChecked){
            switchAB.setChecked(true);
        }

        group.setOnCheckedChangeListener((chipGroup, id) -> {
            Chip chip = ((Chip) chipGroup.getChildAt(chipGroup.getCheckedChipId()));
            if (chip != null) {
                Log.d("chip checked", String.valueOf(chip.getChipText()));
                Toast.makeText(this, "Chip is " + chip.getChipText(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < chipGroup.getChildCount(); ++i) {
                    chipGroup.getChildAt(i).setClickable(true);

                }
                chip.setClickable(false);
            }
        });

        group_price.setOnCheckedChangeListener((chipGroup, id) -> {
            Chip chip = ((Chip) chipGroup.getChildAt(chipGroup.getCheckedChipId()));
            if (chip != null) {
                Log.d("chip checked", String.valueOf(chip.getChipText()));
                Toast.makeText(this, "Chip is " + chip.getChipText(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < chipGroup.getChildCount(); ++i) {
                    chipGroup.getChildAt(i).setClickable(true);

                }
                chip.setClickable(false);
            }
        });

        switchAB.setChecked(true);
        toogleisChecked = switchAB.isChecked();
        switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("isChecked" + isChecked);
                toogleisChecked = isChecked;
            }
        });

        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                ArrayList<Integer> list = new ArrayList<Integer>();
                if (group.getCheckedChipId() == R.id.option_1) {
                    //Ascending
                    sortAscending = true;
                    Collections.sort(productRequestList, ProductRequest.AscArtName);
                    for (ProductRequest str : productRequestList) {
                        System.out.println("asc : " + str);
                    }
                    productAdapter.notifyDataSetChanged();
                } else if (group.getCheckedChipId() == R.id.option_2) {
                    //Descending
                    sortAscending = false;
                    Collections.sort(productRequestList, ProductRequest.DesArtName);
                    for (ProductRequest str : productRequestList) {
                        System.out.println("desc : " + str);
                    }
                    productAdapter.notifyDataSetChanged();
                }

                if (group_price.getCheckedChipId() == R.id.option_high_pice) {
                    //Ascending
                    sortPriceAscending = true;
                    Collections.sort(productRequestList, ProductRequest.AscPrice);
                    for (ProductRequest str : productRequestList) {
                        System.out.println("asc : " + str);
                    }
                    productAdapter.notifyDataSetChanged();
                } else if (group_price.getCheckedChipId() == R.id.option_low_price) {
                    //Descending
                    sortPriceAscending = false;
                    Collections.sort(productRequestList, ProductRequest.DescPrice);
                    for (ProductRequest str : productRequestList) {
                        System.out.println("desc : " + str);
                    }
                    productAdapter.notifyDataSetChanged();
                }

                if (productAdapter != null) {
                    productAdapter.setIschecked(toogleisChecked);
                    productAdapter.notifyDataSetChanged();
                }


            }
        });

    }

    void showLoading() {
        ringProgressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void dismissLoading() {
        ringProgressDialog.dismiss();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    private void getCartCount() {
        cart_count = mDatabaseHelper.getCartCount(appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
    }

    private void showProductDetails() {

        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductResponse> call = apiInterface.getProductDetails(getMap());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        dismissLoading();
                        productRequestList = response.body().getData();

                        if (productRequestList != null) {
                            Collections.sort(productRequestList, ProductRequest.AscArtName);
                            productAdapter = new ProductAdapter(productRequestList, ProductActivity.this, ProductActivity.this);
                            product_rV.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
//                            productAdapter.setStateRestorationPolicy
//                                    (RecyclerView.Adapter.StateRestorationPolicy.PREVENT);
//                            product_rV.setFocusable(false);
//                            productAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                            product_rV.setAdapter(productAdapter);
                            product_rV.setHasFixedSize(true);
                            product_rV.setItemViewCacheSize(50);
                            product_rV.setDrawingCacheEnabled(true);
                            product_rV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
                            AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(ProductActivity.this, 500);
                            product_rV.setLayoutManager(autolayoutManager);
                            tv_data_count.setText(String.valueOf(productRequestList.size()) + " Articles Found");
                            if (productAdapter != null) {
                                Log.d("isChecked refresh", String.valueOf(toogleisChecked));
                                productAdapter.setIschecked(toogleisChecked);
                                productAdapter.notifyDataSetChanged();
                            }
                            if(sortAscending){
                                Collections.sort(productRequestList, ProductRequest.AscArtName);
                            }else {
                                Collections.sort(productRequestList, ProductRequest.DesArtName);
                            }

                            if(sortPriceAscending){
                                Collections.sort(productRequestList, ProductRequest.AscPrice);
                            }else {
                                Collections.sort(productRequestList, ProductRequest.DescPrice);
                            }
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
                            Log.e("error_message", message);
                            if (!appUtils.isConnected(ProductActivity.this)) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                            Toast.makeText(ProductActivity.this, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ProductActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                        System.out.println("response" + response.code());
                        //System.out.println("response"+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                dismissLoading();
                if (!appUtils.isConnected(ProductActivity.this)) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                System.out.println("onFailure" + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!appUtils.isConnected(ProductActivity.this)) {
            Custom_Toast.warning(ProductActivity.this, getString(R.string.no_internet));
            product_rV.setVisibility(View.GONE);
            no_data_text.setVisibility(View.VISIBLE);
            no_data_text.setText(getString(R.string.no_internet));
        } else {
            getMenuInflater().inflate(R.menu.menu_clothes_cart, menu);
//            getMenuInflater().inflate(R.menu.menu_search, menu);
           /* getMenuInflater().inflate(R.menu.menu_toggle, menu);
            getMenuInflater().inflate(R.menu.menu_sort, menu);*/
            MenuItem menuItem = menu.findItem(R.id.cart_action);
            MenuItem searchItem = menu.findItem(R.id.search_action);
            MenuItem notiitem = menu.findItem(R.id.notification_action);
            notiitem.setVisible(false);
            menuItem.setIcon(Converter.convertLayoutToImage(ProductActivity.this, cart_count, R.drawable.shopping_bag));
            searchItem.setIcon(Converter.convertLayoutToImage(ProductActivity.this, 0, R.drawable.ic_baseline_search_24));

//            MenuItem searchViewItem = menu.findItem(R.id.action_search);
            /*searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!appUtils.isConnected(getApplicationContext())) {
                        product_rV.setVisibility(View.GONE);
                        no_data_text.setVisibility(View.VISIBLE);
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
                    if (!appUtils.isConnected(getApplicationContext())) {
                        product_rV.setVisibility(View.GONE);
                        no_data_text.setVisibility(View.VISIBLE);
                    } else {
                        if (query.isEmpty()) {
                            refreshItems();
                        } else {
                            getFilterData(query);
                        }
                    }
                    return false;
                }
            });*/
           /* MenuItem switchId = menu.findItem(R.id.switchId);
            MenuItem notiMenu = menu.findItem(R.id.notification_action);
            notiMenu.setVisible(false);
            switchId.setActionView(R.layout.switch_layout);

            SwitchCompat switchAB = switchId.getActionView().findViewById(R.id.switchAB);
            switchAB.setChecked(true);
            toogleisChecked = switchAB.isChecked();
            switchAB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    System.out.println("isChecked" + isChecked);
                    toogleisChecked = isChecked;
                    if (productAdapter != null) {
                        productAdapter.setIschecked(isChecked);
                        productAdapter.notifyDataSetChanged();
                    }
                }
            });*/
        }
        // return super.onCreateOptionsMenu(menu);

        // MenuItem menuSort = menu.findItem(R.id.action_sort);
        return true;
    }

  /*  private void getFilterData(String query) {
        if (productAdapter != null) {
            productAdapter.getFilter().filter(query);
            if (productAdapter.getItemCount() != 0) {
                Log.d("filter count", String.valueOf(productAdapter.getItemCount()));
//                ll_Data_Found.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                product_rV.setVisibility(View.VISIBLE);
                ll_NoData_Found.setVisibility(View.GONE);
                AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(this, 500);
                product_rV.setLayoutManager(autolayoutManager);
                tv_data_count.setText(productAdapter.getItemCount() + " Articles Found");

            } else {
//                ll_Data_Found.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.GONE);
                product_rV.setVisibility(View.GONE);
                ll_NoData_Found.setVisibility(View.VISIBLE);
                retry_btn.setVisibility(View.GONE);
                img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                no_data_text.setText(getResources().getString(R.string.no_record_found));
                tv_data_count.setText("0 Articles Found");
            }
        }
    }*/

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("category_id", category);
        map.put("subcategory_id", subCategory);
        map.put("sort", "DESC");
        return map;
    }


    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "There is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mintent = new Intent(ProductActivity.this, CartActivity.class);
                    startActivity(mintent);
                }
                break;
           /* case R.id.asending_sort:
//             Sorting on Artno property
                System.out.println("ArtNum Sorting:");
                Collections.sort(productRequestList, ProductRequest.Artno);
                for (ProductRequest str : productRequestList) {
                    System.out.println(str);
                }
                productAdapter.notifyDataSetChanged();
                break;
            case R.id.descending_sort:
//                Sorting on Artno Descending property
                System.out.println("ArtNum Descending_sort:");
                Collections.sort(productRequestList, ProductRequest.DescendingArtNo);
                for (ProductRequest str : productRequestList) {
                    System.out.println(str);
                }
                productAdapter.notifyDataSetChanged();
                break;*/
            case R.id.search_action:
                Intent intent = new Intent(ProductActivity.this, GlobalSearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void updateCartCount(Context context) {
        cart_count = cart_count + 1;
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!appUtils.isConnected(ProductActivity.this)) {
            ringProgressDialog.dismiss();
            Custom_Toast.warning(ProductActivity.this, getString(R.string.no_internet));
            product_rV.setVisibility(View.GONE);
            no_data_text.setVisibility(View.VISIBLE);
            no_data_text.setText(getString(R.string.no_internet));
        } else {
            getCartCount();
            //refreshItems();
            product_rV.getLayoutManager().onRestoreInstanceState(state);
        }
//        product_rV.setHasFixedSize(true);
//        product_rV.setItemViewCacheSize(20);
//        product_rV.setDrawingCacheEnabled(true);
//        product_rV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        invalidateOptionsMenu();
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

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        super.onBackPressed();
    }

    void refreshItems() {
        // Load items
        // ...
        if (!appUtils.isConnected(this)) {
            dismissLoading();
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
            showProductDetails();


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
}



