package com.fashion.forlempopoli.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.fashion.forlempopoli.Adapter.ProductAdapter;
import com.fashion.forlempopoli.Db.connection.DatabaseHelper;
import com.fashion.forlempopoli.Entity.request.ProductRequest;
import com.fashion.forlempopoli.Entity.response.ProductResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.AutoFitGridLayoutManager;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.fashion.forlempopoli.Utilities.TransparentProgressDialog;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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
    NestedScrollView nested_scroll;
    ProductAdapter productAdapter;
    List<ProductRequest> productRequestList;

    private DatabaseHelper mDatabaseHelper;
    AppSharedPreference appSharedPreference;

    Parcelable state;
    private int cart_count = 0;
    ProgressDialog ringProgressDialog;
    String searched;
    ImageView back_button;
    ImageView barcode_button;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    Dialog dialog;
    TransparentProgressDialog transparentProgressDialog;

    @Override
    protected void onPause() {
        super.onPause();
        state = product_rV.getLayoutManager().onSaveInstanceState();
        cameraSource.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        searchView = findViewById(R.id.searchView);
        back_button = findViewById(R.id.back_button);
        barcode_button = findViewById(R.id.barcode_button);
        product_rV = findViewById(R.id.product_rV);
        nested_scroll = findViewById(R.id.nested_scroll);
        no_data_text = findViewById(R.id.no_data_text);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        no_data_text = findViewById(R.id.no_data_text);
//        ll_Data_Found = findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);
        img_info = findViewById(R.id.img_info);
        retry_btn = findViewById(R.id.retry_btn);

        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.forlim_loader, "");

        appSharedPreference = AppSharedPreference.getAppSharedPreference(this.getApplicationContext());
        appUtils = new AppUtils();
        dialog = new Dialog(GlobalSearchActivity.this);

        barcode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // custom dialog
                dialog.setContentView(R.layout.barcode_scanner_dialog);
                dialog.setTitle("Scan Barcode");

                toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
                surfaceView = dialog.findViewById(R.id.surface_view);
                barcodeText = dialog.findViewById(R.id.barcode_text);

                barcodeText.setVisibility(View.GONE);

                initialiseDetectorsAndSources();

                dialog.show();
            }
        });



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
        dialog.dismiss();
        showLoading();
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductResponse> call = apiInterface.getGlobalProductDetails(getMap(query));
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        dismissLoading();
//                        progressDialogHide();
                        productRequestList = response.body().getData();
                        System.out.println("productRequestList "+productRequestList.toString());
                        if (productRequestList.size() != 0) {
                            nested_scroll.setVisibility(View.VISIBLE);
                            ll_NoData_Found.setVisibility(View.GONE);

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
                            System.out.println("productRequestList error "+productRequestList.toString());
                            //searchView.setEnabled(false);
                            nested_scroll.setVisibility(View.GONE);
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
                        progressDialogHide();
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
                progressDialogHide();
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

        nested_scroll.setVisibility(View.GONE);
        ll_NoData_Found.setVisibility(View.VISIBLE);
        retry_btn.setVisibility(View.VISIBLE);
        img_info.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        no_data_text.setText(getResources().getString(R.string.no_internet));
    }

    public void something_wrong() {
//        ll_Data_Found.setVisibility(View.GONE);

        nested_scroll.setVisibility(View.GONE);
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


    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(2000, 2000)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(GlobalSearchActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(GlobalSearchActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                 Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                dialog.dismiss();
//                Log.d("barcodeData if",barcodes.toString());
                if (barcodes.size() != 0) {
                    if (barcodes.valueAt(0).email != null) {
//                        barcodeText.removeCallbacks(null);
                        barcodeData = barcodes.valueAt(0).email.address;
                        searchView.setQuery(barcodeData,true);
                        showProductDetails(barcodeData);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        Log.d("barcodeData if",barcodeData);
                        Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();
                    } else {
                        barcodeData = barcodes.valueAt(0).displayValue;
                        searchView.setQuery(barcodeData,true);
                        showProductDetails(barcodeData);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();
                        Log.d("barcodeData else",barcodeData);
                    }

                    searchView.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }
            }
        });
    }

    /**
     * Show Progress Dialog
     */
    private void progressDialogShow() {
        transparentProgressDialog.show();
    }

    /**
     * Hide Progress dialog
     */
    private void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }

}