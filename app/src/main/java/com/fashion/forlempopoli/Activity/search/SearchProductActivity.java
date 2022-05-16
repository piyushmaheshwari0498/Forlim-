package com.fashion.forlempopoli.Activity.search;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fashion.forlempopoli.Activity.ProductImageActivity;
import com.fashion.forlempopoli.Adapter.AritcleAdapter;
import com.fashion.forlempopoli.Db.connection.DatabaseHelper;
import com.fashion.forlempopoli.Db.connection.ITable;
import com.fashion.forlempopoli.Entity.request.ProductRequest;
import com.fashion.forlempopoli.Entity.response.ProductResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.Custom_Toast;
import com.fashion.forlempopoli.Utilities.PatternClass;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.fashion.forlempopoli.databinding.ActivitySearchProductBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //    ProgressDialog progressDialog;
    DatabaseHelper mydb;
    ActivitySearchProductBinding binding;
    AppSharedPreference appSharedPreference;
    List<ProductRequest> productRequestList;
    double rate;
    double qty;
    double minqty;
    double amount;
    android.hardware.Camera.Parameters params;
    String customer_type;
    Vibrator vibe;
    AppUtils appUtils;
    List<ProductRequest> productspinnerList;
    AritcleAdapter articleAdapter;
    int selectedId;
    String selectedart;
    String image_URL = "http://forlimpopoli.in/beta_mobile/public/uploads/articles/";
    String image_not_found_URL = "http://forlimpopoli.in/beta_mobile/public/assets/img/";
    private boolean firstDetected = false;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private String barcodeData;
    private double cgstAmount = 0;
    private double sgstAmount = 0;
    private double igstAmount = 0;
    private DecimalFormat df;
    private String photopath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setTitle("Search Product");
//        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        df = new DecimalFormat("#.##");
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        appSharedPreference = AppSharedPreference.getAppSharedPreference(this);
        customer_type = appSharedPreference.getString(Constants.INTENT_KEYS.KEY_CUSTOMER_TYPE);
        appUtils = new AppUtils();
        productspinnerList = new ArrayList<>();
        productRequestList = new ArrayList<>();
        mydb = DatabaseHelper.getInstance(this);
//        progressDialog = new ProgressDialog(this);

        binding.etqty.setEnabled(false);
        binding.addCartBtn.setEnabled(false);

        if (customer_type.equals("0")) {
            binding.llCgst.setVisibility(View.VISIBLE);
        } else {
            binding.llIgst.setVisibility(View.VISIBLE);
        }



        if (!firstDetected) {
            binding.btnScan.setText("Start Scanning");
            initialiseDetectorsAndSources();
            cameraSource.stop();
            firstDetected = false;
        } else {
            binding.btnScan.setText("Stop Scanning");
        }

        binding.addCartBtn.setVisibility(View.VISIBLE);

        if (!appUtils.isConnected(SearchProductActivity.this)) {
            Custom_Toast.warning(SearchProductActivity.this, getString(R.string.no_internet));
        } else {
            getSpinnerData();

        }
//        binding.btnScan.setText("Start Scanning");

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = image_URL + photopath;
                  if (!url.contains("no-photo.jpg")) {
                      Intent intent = new Intent(SearchProductActivity.this, ProductImageActivity.class);
                      intent.putExtra("position", url);
                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(intent);
                  }else{
                      Toast.makeText(SearchProductActivity.this,"Image not available",Toast.LENGTH_SHORT).show();
                  }
            }
        });

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("isScanning", String.valueOf(firstDetected));
                if (!appUtils.isConnected(SearchProductActivity.this)) {
                    Custom_Toast.warning(SearchProductActivity.this, getString(R.string.no_internet));
                } else {
                    if (firstDetected) {
                        cameraSource.stop();
                        firstDetected = false;
                        binding.btnScan.setText("Start Scanning");
                    } else {
                        try {
                            if (ActivityCompat.checkSelfPermission(SearchProductActivity.this, Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED) {

                                cameraSource.start(binding.surfaceView.getHolder());

                                initialiseDetectorsAndSources();
                            } else {
                                ActivityCompat.requestPermissions(SearchProductActivity.this, new
                                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        firstDetected = true;
                        binding.btnScan.setText("Stop Scanning");
                    }
                }
            }
        });

        binding.etqty.setFilters(new InputFilter[]{new PatternClass.DecimalDigitsInputFilter2(2)});
//        if (!binding.etqty.getText().toString().isEmpty()) {
        binding.etqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (charSequence != null) {
                Log.d("charSequence", String.valueOf(charSequence));
                // binding.etqty.setText(charSequence);


                if (!charSequence.toString().isEmpty()) {
                    qty = Double.parseDouble(String.valueOf(charSequence));
                    if(qty != 0.0) {
//                        if (qty < 0.0) {
//                            Toast.makeText(SearchProductActivity.this, "Qty Cannot be less then Minimum Qty",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                        binding.etqty.setText(String.valueOf(charSequence));
//                        amount = rate * qty;
//                        binding.etamount.setText("" + amount);

                            if (customer_type.equals("0")) {
                                binding.tvcgst.setText("" + productRequestList.get(0).getArtCgstPer());
                                binding.tvsgst.setText("" + productRequestList.get(0).getArtSgstPer());

                                cgstAmount = (rate * qty * productRequestList.get(0).getArtCgstPer() / 100);
                                System.out.println("CGST FORMULA : " +
                                        rate +
                                        "*" +
                                        qty +
                                        "*" +
                                        cgstAmount);
                                cgstAmount = Double.parseDouble(df.format(cgstAmount));
                                System.out.println("updateProduct cgst : " + cgstAmount);

                                sgstAmount = (rate * qty * productRequestList.get(0).getArtSgstPer() / 100);
                                System.out.println("SGST FORMULA : " +
                                        rate +
                                        "*" +
                                        qty +
                                        "*" +
                                        sgstAmount);
                                sgstAmount = Double.parseDouble(df.format(sgstAmount));
                                System.out.println("sgstAmount  :" + sgstAmount);

                                amount = (rate * qty) + cgstAmount + sgstAmount;
                                binding.etamount.setText("₹" + df.format(amount));
                                binding.tvsgst.setText("" + df.format(sgstAmount));
                                binding.tvcgst.setText("" + df.format(cgstAmount));
                            } else {
                                binding.tvigst.setText("" + productRequestList.get(0).getArtIgstPer());
                                igstAmount = (rate * qty * productRequestList.get(0).getArtIgstPer() / 100);
                                igstAmount = Double.parseDouble(df.format(igstAmount));
                                System.out.println("igstAmount else" + igstAmount);

                                amount = (rate * qty) + igstAmount;
                                binding.etamount.setText("₹" + df.format(amount));
                                binding.tvigst.setText("" + df.format(igstAmount));

                            }
//                        }
                    }
                    else{
                        Toast.makeText(SearchProductActivity.this, "Qty Cannot be less then Minimum Qty",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.etamount.setText("₹0.0");
                }
//                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                    binding.etqty.setText(""+qty);
            }
        });
//        }


        binding.addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qty >= 0.10) {
                    if (customer_type.equals("0")) {
                        // TODO: 21-08-2020 Step 1: -Get Data Of Selected "Add" Row
                        ProductRequest mproductRequest = productRequestList.get(0);

                        String clothesIdObtained = String.valueOf(mproductRequest.getArtId());
                        int clothQuantity = 1;

                        if (mproductRequest.getCatName().toLowerCase().contains("shirting")) {
                            mproductRequest.setArtQtyMtrs(1.6);
                            minqty = 1.6;
                        } else if (mproductRequest.getCatName().toLowerCase().contains("suiting")) {
                            mproductRequest.setArtQtyMtrs(1.3);
                            minqty = 1.3;
                        } else {
                            mproductRequest.setArtQtyMtrs(1.0);
                            minqty = 1.0;
                        }

                        Log.e("clothesIdObtained", clothesIdObtained);
                        // TODO: 21-08-2020  Check Product Exits Before Adding To Cart
                        int countOfProduct = mydb.getCountOfProduct(clothesIdObtained,
                                appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));

                        // TODO: 21-08-2020 if count of product=0 insert new Product.
                        int isExist = mydb.getCurrentProduct(clothesIdObtained, appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                        if (countOfProduct == 0) {
                            System.out.println("Product Count is 0");
                            ContentValues mContentValues = new ContentValues();
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.ACC_ID, appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID, clothesIdObtained);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT, clothQuantity);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE, mproductRequest.getArtSellingPriceAmt());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED, "0");
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS, qty);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS, mproductRequest.getArtQtyMtrs());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID, mproductRequest.getArtCatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID, mproductRequest.getArtScatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER, mproductRequest.getArtNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH, mproductRequest.getArtWidth());
//                        mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE, mproductRequest.getArtOfferPrice());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_STATUS, mproductRequest.getArtStatus());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SHADE_NO, mproductRequest.getArtShadeNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE, mproductRequest.getArtStockType());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION, mproductRequest.getArtComposition());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_PHOTO, mproductRequest.getArtPhoto());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO, mproductRequest.getGarmentPhoto());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CGST, cgstAmount);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SGST, sgstAmount);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE, customer_type);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, amount);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL, amount);
                            long insertId = mydb.insertCartDetails(mContentValues);
                            if (insertId > 0) {
//                            homeCallBack.updateCartCount(context);
                                Custom_Toast.add_cart_toast(SearchProductActivity.this, getResources().getString(R.string.added_cart));
                                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                                binding.addCartBtn.setEnabled(false);
                                binding.addCartBtn.setText("Added");
                            } else {
                                Toast.makeText(SearchProductActivity.this, getResources().getString(R.string.not_addedd_cart), Toast.LENGTH_SHORT).show();
                            }
                        }


                        // TODO: 29-12-2019 If count of product =1 update existing product
                        else if (countOfProduct == 1) {
                            System.out.println("Product Count is 1");
                            Toast.makeText(SearchProductActivity.this, getResources().getString(R.string.already_cart), Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("Product Count is : " + countOfProduct);
                        }
                    } else {
                        ProductRequest mproductRequest = productRequestList.get(0);
                        String clothesIdObtained = String.valueOf(mproductRequest.getArtId());
                        //double totalPrice = mproductRequest.getArtSellingPriceAmt() + igstAmount;
                        System.out.println("clothesIdObtained" + clothesIdObtained);

                        if (mproductRequest.getCatName().toLowerCase().contains("shirting")) {
                            mproductRequest.setArtQtyMtrs(1.6);
                            minqty = 1.6;
                        } else if (mproductRequest.getCatName().toLowerCase().contains("suiting")) {
                            mproductRequest.setArtQtyMtrs(1.3);
                            minqty = 1.3;
                        } else {
                            mproductRequest.setArtQtyMtrs(1.0);
                            minqty = 1.0;
                        }

                        System.out.println("total_price product1 else" + amount);
                        int clothQuantity = 1;
                        int countOfProduct = mydb.getCountOfProduct(clothesIdObtained, appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                        if (countOfProduct == 0) {
                            ContentValues mContentValues = new ContentValues();
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.ACC_ID, appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID, clothesIdObtained);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT, clothQuantity);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE, mproductRequest.getArtSellingPriceAmt());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED, "0");
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS, qty);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS, mproductRequest.getArtQtyMtrs());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID, mproductRequest.getArtCatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID, mproductRequest.getArtScatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER, mproductRequest.getArtNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH, mproductRequest.getArtWidth());
//                        mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE, mproductRequest.getArtOfferPrice());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_STATUS, mproductRequest.getArtStatus());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SHADE_NO, mproductRequest.getArtShadeNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE, mproductRequest.getArtStockType());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION, mproductRequest.getArtComposition());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_PHOTO, mproductRequest.getArtPhoto());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO, mproductRequest.getGarmentPhoto());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IGST, igstAmount);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE, customer_type);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, amount);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL, amount);
                            long insertId = mydb.insertCartDetails(mContentValues);
                            if (insertId > 0) {
//                            homeCallBack.updateCartCount(context);
//                            Toast.makeText(SearchProductActivity.this, context.getResources().getString(R.string.added_cart), Toast.LENGTH_SHORT).show();
                                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                                Custom_Toast.add_cart_toast(SearchProductActivity.this, getResources().getString(R.string.added_cart));
                                binding.addCartBtn.setEnabled(false);
                                binding.addCartBtn.setText("Added");
                            } else {
                                Toast.makeText(SearchProductActivity.this, getResources().getString(R.string.not_addedd_cart), Toast.LENGTH_SHORT).show();
                            }
                        } else if (countOfProduct == 1) {
                            Toast.makeText(SearchProductActivity.this, getResources().getString(R.string.added_cart), Toast.LENGTH_SHORT).show();
                            binding.addCartBtn.setEnabled(false);
                            binding.addCartBtn.setText("Added");
                        }
                    }
                }else{
                    Toast.makeText(SearchProductActivity.this, "Qty Cannot be less then Minimum Qty",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    private void initialiseDetectorsAndSources() {
//        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1900, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        binding.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(SearchProductActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(binding.surfaceView.getHolder());
                        initialiseDetectorsAndSources();
                        firstDetected = true;
                    } else {
                        ActivityCompat.requestPermissions(SearchProductActivity.this, new
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
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && firstDetected) {
                    cameraSource.stop();
                    firstDetected = false;
                    binding.btnScan.setText("Start Scanning");
                    binding.spnDesign.post(new Runnable() {
                        @Override
                        public void run() {
//                            Log.d("barcodeData if",barcodeData);
                            if (barcodes.valueAt(0).email != null) {
                                binding.spnDesign.removeCallbacks(null);
                                binding.spnDesign.setText(barcodes.valueAt(0).email.address);

                            } else {
                                binding.spnDesign.setText(barcodes.valueAt(0).displayValue);
                            }

                        }
                    });

                    showProductDetails(binding.spnDesign.getText().toString().trim());
                    /*if (barcodes.valueAt(0).email != null) {
//                        barcodeText.removeCallbacks(null);
                        barcodeData = barcodes.valueAt(0).email.address;
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        Log.d("barcodeData if",barcodeData);
                        barcode_text.setText(barcodeData);
//                        Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();
                    } else {
                        barcodeData = barcodes.valueAt(0).displayValue;
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                        Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();
                        Log.d("barcodeData else",barcodeData);
                        barcode_text.setText(barcodeData);
                    }*/
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void showProductDetails(String query) {
//        progressDialog.setTitle("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        System.out.println("query " + query);
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductResponse> call = apiInterface.getGlobalProductDetails(getMap(query));
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                progressDialog.dismiss();
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        productRequestList = response.body().getData();
                        System.out.println("productRequestList " + productRequestList.toString());
                        if (productRequestList.size() != 0) {

                            if(productRequestList.get(0).getArtNo() != null){
//                                if (!productRequestList.get(0).getArtOfferPrice().equals("0.00")) {
//                                    binding.tvofferPrice.setText("" + productRequestList.get(0).getArtOfferPrice());
//                                    rate = Double.parseDouble(productRequestList.get(0).getArtOfferPrice());
//                                    binding.tvSalePrice.setText("" + productRequestList.get(0).getArtSellingPriceAmt());
//                                    binding.tvSalePrice.setPaintFlags(binding.tvSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                                } else {
                                    binding.tvofferPrice.setVisibility(View.GONE);
                                    binding.tvSalePrice.setText("₹" + df.format(productRequestList.get(0).getArtSellingPriceAmt()));
                                    rate = Double.parseDouble(String.valueOf(productRequestList.get(0).getArtSellingPriceAmt()));
//                                }

                                if(productRequestList.get(0).getCatName().toLowerCase().contains("shirting")){
                                    productRequestList.get(0).setArtQtyMtrs(1.6);
                                    minqty = 1.6;
                                }else if(productRequestList.get(0).getCatName().toLowerCase().contains("suiting")){
                                    productRequestList.get(0).setArtQtyMtrs(1.3);
                                    minqty = 1.3;
                                }else{
                                    productRequestList.get(0).setArtQtyMtrs(1.0);
                                    minqty = 1.0;
                                }

                                binding.productName.setText(productRequestList.get(0).getArtName());
                                binding.etqty.setText(String.valueOf(minqty));
                                binding.etwidth.setText(productRequestList.get(0).getArtWidth());

                                int countOfProduct = mydb.getCountOfProduct(productRequestList.get(0).getArtId(),
                                        appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                                if (productRequestList.get(0).getTotal_stock() > 0.0) {
                                    binding.etbalance.setText(String.valueOf(productRequestList.get(0).getTotal_stock()));

                                    if (countOfProduct == 0) {
                                        binding.addCartBtn.setEnabled(true);
                                        binding.addCartBtn.setText("Add");
                                        binding.etqty.setEnabled(true);
                                    } else {
                                        binding.addCartBtn.setEnabled(false);
                                        binding.etqty.setEnabled(false);
                                        binding.addCartBtn.setText("Added");
                                    }
                                } else {
                                    binding.etbalance.setText("Out of stock");
                                    binding.addCartBtn.setEnabled(false);
                                    binding.etqty.setEnabled(false);
                                    binding.addCartBtn.setText("Out of stock");
                                }

                                qty = productRequestList.get(0).getArtQtyMtrs();

                                if (customer_type.equals("0")) {
                                    binding.tvcgst.setText("" + productRequestList.get(0).getArtCgstPer());
                                    binding.tvsgst.setText("" + productRequestList.get(0).getArtSgstPer());

                                    cgstAmount = (rate * qty * productRequestList.get(0).getArtCgstPer() / 100);
                                    System.out.println("CGST FORMULA : " +


                                            rate +
                                            "*" +
                                            qty +
                                            "*" +
                                            productRequestList.get(0).getArtCgstPer());
                                    cgstAmount = Double.parseDouble(df.format(cgstAmount));
                                    System.out.println("updateProduct cgst : " + cgstAmount);

                                    sgstAmount = (rate * qty * productRequestList.get(0).getArtSgstPer() / 100);
                                    System.out.println("SGST FORMULA : " +
                                            rate +
                                            "*" +
                                            qty +
                                            "*" +
                                            productRequestList.get(0).getArtSgstPer());
                                    sgstAmount = Double.parseDouble(df.format(sgstAmount));
                                    System.out.println("sgstAmount  :" + sgstAmount);

                                    amount = (rate * qty) + cgstAmount + sgstAmount;
                                    binding.etamount.setText("₹" + df.format(amount));

                                    binding.tvsgst.setText("" + df.format(sgstAmount));
                                    binding.tvcgst.setText("" + df.format(cgstAmount));

                                }
                                else {
                                    binding.tvigst.setText("" + productRequestList.get(0).getArtIgstPer());
                                    igstAmount = (rate * qty * productRequestList.get(0).getArtIgstPer());
                                    igstAmount = Double.parseDouble(df.format(igstAmount));
                                    System.out.println("igstAmount else" + igstAmount);

                                    amount = (rate * qty) + igstAmount;

                                    binding.tvigst.setText("" + df.format(igstAmount));
                                }
                                photopath = productRequestList.get(0).getArtPhoto();
                                String url = image_URL + photopath;
                                String not_found_url = image_not_found_URL + photopath;
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                                System.out.println("imageurl " + url);
                                if (!url.contains("no-photo.jpg")) {
                               /* Glide.with(SearchProductActivity.this)
                                        .load(url)
                                        .centerCrop()
                                        .placeholder(R.drawable.progress_animation)
                                        .apply(requestOptions)
                                        .error(R.drawable.ic_baseline_error_outline_24)
                                        .into(binding.imageView);*/

                                    Glide.with(SearchProductActivity.this)
//                                        .asGif()
                                            .load(url)
                                            .override(3000, 250) // resizes the image to these dimensions (in pixel)
                                            .placeholder(R.drawable.progress_animation)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .skipMemoryCache(true)
                                            .priority(Priority.IMMEDIATE)
                                            .error(R.drawable.no_image_found)
                                            .into(binding.imageView);
                                } else {
                                    Glide.with(SearchProductActivity.this)
                                            .load(not_found_url)
                                            .override(3000, 250) // resizes the image to these dimensions (in pixel)
                                            .placeholder(R.drawable.progress_animation)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .skipMemoryCache(true)
                                            .priority(Priority.IMMEDIATE)
                                            .error(R.drawable.no_image_found)
                                            .into(binding.imageView);
                                }
//                            binding.addCartBtn.setEnabled(true);

                                binding.llShow.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(SearchProductActivity.this, "No Record Found\nPlease Scan Again", Toast.LENGTH_SHORT).show();
                                System.out.println("productRequestList error " + productRequestList.toString());
                                binding.llShow.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(SearchProductActivity.this, "No Record Found\nPlease Scan Again", Toast.LENGTH_SHORT).show();
                            System.out.println("productRequestList error " + productRequestList.toString());
                            binding.llShow.setVisibility(View.GONE);
                        }
                    }
                } else {

                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        System.out.println("response  Not Found" + response.code());
                        //System.out.println("response msg"+response.message());
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Log.e("error_message", message);
                           /* if (!appUtils.isConnected(SearchProductActivity.this)) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }*/
                            Toast.makeText(SearchProductActivity.this, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
//                        Toast.makeText(SearchProductActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                        System.out.println("response" + response.code());
                        initialiseDetectorsAndSources();
                        firstDetected = true;
                        binding.btnScan.setText("Stop Scanning");
                        //System.out.println("response"+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
              /*  dismissLoading();
                progressDialogHide();
                if (!appUtils.isConnected(GlobalSearchActivity.this)) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }*/
//                progressDialog.dismiss();
                System.out.println("onFailure" + t.getMessage());
//                initialiseDetectorsAndSources();
            }
        });
    }

    private HashMap<String, Object> getMap(String query) {
        Log.d("mapquery",query);
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
//        map.put("acct_id", appSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("art_name", query);
        return map;
    }

    public void getSpinnerData() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductResponse> call = apiInterface.getArticleList(getMap());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.body().getStatusCode() == 1) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        productspinnerList = response.body().getData();
                        //City Data
                        articleAdapter = new AritcleAdapter(SearchProductActivity.this,
                                R.layout.custom_spinner_item, productspinnerList);
                        binding.spnDesign.setThreshold(1);
                        binding.spnDesign.setAdapter(articleAdapter);

                        if(productspinnerList.size() != 0){
                            binding.spnDesign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                    ProductRequest productRequest = (ProductRequest) adapterView.getItemAtPosition(pos);
                                    selectedId = Integer.parseInt(productRequest.getArtId());
                                    selectedart = productRequest.getArtNo().replaceAll("\\s+"," ");
                                    Log.e("selectedId", String.valueOf(productRequest.getArtId()));
                                    Log.e("selectedart", selectedart);

                                    if (!appUtils.isConnected(SearchProductActivity.this)) {
                                        Custom_Toast.warning(SearchProductActivity.this, getString(R.string.no_internet));
                                    } else {
                                        showProductDetails(selectedart);
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.d("errormessage", response.message());
                    if (response.body().getStatusCode() == 0) {
                        Log.d("error message", response.message());
                    } else {
                        Custom_Toast.warning(SearchProductActivity.this, getResources().getString(R.string.something_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        return map;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}