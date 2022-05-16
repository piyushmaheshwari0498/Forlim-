package com.fashion.forlempopoli.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fashion.forlempopoli.Db.connection.DatabaseHelper;
import com.fashion.forlempopoli.Db.connection.ITable;
import com.fashion.forlempopoli.Listener.OnCartUpdatedListener;
import com.fashion.forlempopoli.Model.M_Clothes_Order_Details;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.PatternClass;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public static List<M_Clothes_Order_Details> mClothesOrderDetailsList;
    Context context;
    DatabaseHelper mydb;
    String photopath;
    String image_URL = "http://forlimpopoli.in/beta_2/public/uploads/articles/";
    String image_not_found_URL = "http://forlimpopoli.in/beta_2/public/assets/img/";
    String customer_type;
    double cgstAmount, sgstAmount, igstAmount;
    double productQuantityInMeter;
    Vibrator vibe;
    private final OnCartUpdatedListener homeCallBack;
    private final int[] productQuantities;
    private final double[] productQuantityInMeters;
    private final NumberFormat formatter;
    private final DecimalFormat df;
    private AppSharedPreference mAppSharedPrefernce;
    EditText cart_mtr_product_quantity_tv;
    double inputqty;
    int pos;

    public CartAdapter(List<M_Clothes_Order_Details> productsArray, Context context,
                       OnCartUpdatedListener mCallBackus) {
        mClothesOrderDetailsList = productsArray;
        this.context = context;
        this.homeCallBack = mCallBackus;
        productQuantities = new int[productsArray.size()];
        productQuantityInMeters = new double[productsArray.size()];
        mydb = DatabaseHelper.getInstance(context.getApplicationContext());
        formatter = new DecimalFormat("#0.00");
        df = new DecimalFormat("#.##");
        vibe = (Vibrator)
                context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_adapter_layout, viewGroup, false);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(context);

        return new ViewHolder(v, new MyCustomEditTextListener());
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position = pos;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(charSequence.length() != 0 && !charSequence.toString().isEmpty()) {
                if(charSequence.length() == 0 && charSequence.toString().equals("0.0") ){
                    charSequence = "0.10";
                    mClothesOrderDetailsList.get(position).setQty_mteres(Double.parseDouble(charSequence.toString().trim()));
                    inputqty = mClothesOrderDetailsList.get(position).getQty_mteres();
                }else {
                    mClothesOrderDetailsList.get(position).setQty_mteres(Double.parseDouble(charSequence.toString().trim()));
                    inputqty = mClothesOrderDetailsList.get(position).getQty_mteres();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvName1.setText(mClothesOrderDetailsList.get(i).getArt_name());
        if (mClothesOrderDetailsList.get(i).getArt_offer_price() != 0.00) {
            viewHolder.tvRate.setText("₹" + mClothesOrderDetailsList.get(i).getArt_offer_price());
        } else {
            viewHolder.tvRate.setText("₹" + mClothesOrderDetailsList.get(i).getArt_selling_price_amt());
        }
        cart_mtr_product_quantity_tv.setText(""+formatter.format(mClothesOrderDetailsList.get(i).getQty_mteres()).replaceAll("\\s+",""));
        viewHolder.tvarticleNo.setText(mClothesOrderDetailsList.get(i).getArt_no());
        viewHolder.tvwidth.setText("Width: "+mClothesOrderDetailsList.get(i).getArt_width());
        viewHolder.tvcgst.setText(formatter.format(mClothesOrderDetailsList.get(i).getCgst()));
        viewHolder.tvsgst.setText(formatter.format(mClothesOrderDetailsList.get(i).getSgst()));
        viewHolder.tvigst.setText(formatter.format(mClothesOrderDetailsList.get(i).getIgst()));
        viewHolder.tvTotal.setText("₹" + mClothesOrderDetailsList.get(i).getTotal_price());

        customer_type = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_CUSTOMER_TYPE);
        System.out.println("customer_type" + customer_type);
        productQuantityInMeters[i] = mClothesOrderDetailsList.get(i).getQty_mteres();
        productQuantities[i] = mClothesOrderDetailsList.get(i).getOrder_unit();

        photopath = mClothesOrderDetailsList.get(i).getArt_photo();
        System.out.println("cart artphoto" + photopath);


        /*if (!url.contains("no-photo.jpg")) {
            Picasso.get().load(image_URL + photopath).into(viewHolder.imageView);
        } else {
            Picasso.get().load(image_not_found_URL + photopath).into(viewHolder.imageView);
        }*/
        String url = image_URL + photopath;
        String not_found_url = image_not_found_URL + photopath;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        if (!url.contains("no-photo.jpg")) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .apply(requestOptions)
                    .error(R.drawable.no_image_found)
                    .into(viewHolder.imageView);
        } else {
            Glide.with(context)
                    .load(not_found_url)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .apply(requestOptions)
                    .error(R.drawable.ic_error_outline_white_24dp)
                    .into(viewHolder.imageView);
        }
        if (customer_type.equals("0")) {
            viewHolder.ll_cgst.setVisibility(View.VISIBLE);
            viewHolder.ll_igst.setVisibility(View.GONE);
        } else {
            viewHolder.ll_cgst.setVisibility(View.GONE);
            viewHolder.ll_igst.setVisibility(View.VISIBLE);
        }


//        double productQuantityInMeter = productQuantityInMeters[i];
//        Log.d("productQuantityInMeter", String.valueOf(productQuantityInMeter));

        viewHolder.cart_mtr_increment.setVisibility(View.VISIBLE);
        if (productQuantityInMeters[i] <= 0.10) {
            viewHolder.cart_mtr_decrement.setVisibility(View.GONE);
            viewHolder.cart_mtr_delete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cart_mtr_decrement.setVisibility(View.VISIBLE);
            viewHolder.cart_mtr_delete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mClothesOrderDetailsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName1, tvRate, /*cart_product_quantity_tv, cart_mtr_product_quantity_tv,*/ tvarticleNo,
                tvcgst, tvsgst, tvigst, tvTotal,tvwidth;
        LinearLayout ll_cgst, ll_igst;
        MaterialButton tvRemove;
        ImageView cart_mtr_decrement, cart_mtr_increment, cart_mtr_delete, imageView;
        LinearLayout ll_submitqty;
        MaterialButton btn_qtysubmit;

        ViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            tvName1 = itemView.findViewById(R.id.tvName1);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvwidth = itemView.findViewById(R.id.tvwidth);
            cart_mtr_decrement = itemView.findViewById(R.id.cart_mtr_decrement);
            cart_mtr_increment = itemView.findViewById(R.id.cart_mtr_increment);
            cart_mtr_delete = itemView.findViewById(R.id.cart_mtr_delete);
            cart_mtr_product_quantity_tv = itemView.findViewById(R.id.cart_mtr_product_quantity_tv);
            ll_submitqty = itemView.findViewById(R.id.ll_submitqty);
            btn_qtysubmit = itemView.findViewById(R.id.btn_qtysubmit);
            //cart_product_quantity_tv = itemView.findViewById(R.id.cart_product_quantity_tv);
            tvarticleNo = itemView.findViewById(R.id.tvarticleNo);
            tvRemove = itemView.findViewById(R.id.tvRemove);
            imageView = itemView.findViewById(R.id.imageView);
            tvcgst = itemView.findViewById(R.id.tvcgst);
            tvsgst = itemView.findViewById(R.id.tvsgst);
            tvigst = itemView.findViewById(R.id.tvigst);
            ll_cgst = itemView.findViewById(R.id.ll_cgst);
            ll_igst = itemView.findViewById(R.id.ll_igst);
            tvTotal = itemView.findViewById(R.id.tvTotal);

            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertbox();
                }
            });

            cart_mtr_product_quantity_tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ll_submitqty.setVisibility(View.VISIBLE);
                    cart_mtr_decrement.setVisibility(View.GONE);
                    cart_mtr_increment.setVisibility(View.GONE);
                    cart_mtr_delete.setVisibility(View.GONE);
                    return false;
                }
            });

            cart_mtr_product_quantity_tv.setFilters(new InputFilter[]{new PatternClass.DecimalDigitsInputFilter2(2)});
            cart_mtr_product_quantity_tv.addTextChangedListener(myCustomEditTextListener);


            cart_mtr_increment.setOnClickListener(this);
            cart_mtr_decrement.setOnClickListener(this);
            cart_mtr_delete.setOnClickListener(this);
            btn_qtysubmit.setOnClickListener(this);

            cart_mtr_product_quantity_tv.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        //do here your stuff f
                        pos = getAdapterPosition();
                    if(cart_mtr_product_quantity_tv.getText().toString().equals("") | cart_mtr_product_quantity_tv.getText().toString().isEmpty()){
                        Toast.makeText(context,"Enter Qty",Toast.LENGTH_SHORT).show();
                    }else {
//                        inputqty = Integer.parseInt(cart_product_quantity.getText().toString().trim());
                        Log.e("increment inputqty", String.valueOf(inputqty));
                        if (inputqty != 0.10) {
                          /*  if (inputqty < mClothesOrderDetailsList.get(getAdapterPosition()).getMin_qty_mteres()) {
                                Toast.makeText(context, "Qty Cannot be less then Minimum Qty", Toast.LENGTH_SHORT).show();
                            } else {*/
                                productQuantityInMeters[getAdapterPosition()] = inputqty;
                                cart_mtr_product_quantity_tv.setText(formatter.format(inputqty));
                                M_Clothes_Order_Details mClothesOrderDetails3 = mClothesOrderDetailsList.get(pos);
                                updateProduct(mClothesOrderDetails3, inputqty);
                                ll_submitqty.setVisibility(View.GONE);
                                cart_mtr_increment.setVisibility(View.VISIBLE);
//                                cart_mtr_product_quantity_tv.setFocusable(false);
                                if (productQuantityInMeters[getAdapterPosition()] <= 0.10) {
                                    cart_mtr_decrement.setVisibility(View.GONE);
                                    cart_mtr_delete.setVisibility(View.VISIBLE);
                                } else {
                                    cart_mtr_decrement.setVisibility(View.VISIBLE);
                                    cart_mtr_delete.setVisibility(View.GONE);
                                }
//                            }
                        } else {
                            Toast.makeText(context, "Qty cannot be 0", Toast.LENGTH_SHORT).show();
                            mClothesOrderDetailsList.get(getAdapterPosition()).setQty_mteres(0.10);
                            cart_mtr_product_quantity_tv.setText(formatter.format(mClothesOrderDetailsList.get(getAdapterPosition()).getQty_mteres()));
                            productQuantityInMeters[getAdapterPosition()] = mClothesOrderDetailsList.get(getAdapterPosition()).getQty_mteres();
                            /*cart_mtr_product_quantity_tv.setText
                                    (formatter.format(mClothesOrderDetailsList.get(getAdapterPosition()).getQty_mteres()));*/
                            ll_submitqty.setVisibility(View.GONE);
                            cart_mtr_increment.setVisibility(View.VISIBLE);
//                            cart_mtr_product_quantity_tv.setFocusable(false);
                            if (productQuantityInMeters[getAdapterPosition()] <= 0.10) {
                                cart_mtr_decrement.setVisibility(View.GONE);
                                cart_mtr_delete.setVisibility(View.VISIBLE);
                            } else {
                                cart_mtr_decrement.setVisibility(View.VISIBLE);
                                cart_mtr_delete.setVisibility(View.GONE);
                            }
                        }
                    }

                        return true;
                    }
                    return false;
                }
            });

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cart_mtr_increment:
                    System.out.println("Adapter Position cart_mtr_increment " + getAdapterPosition());
                    System.out.println("Product Quanity InMeters " + productQuantityInMeters[getAdapterPosition()]);
                    double productQuanitityInMeter = productQuantityInMeters[getAdapterPosition()] + 0.10;
                    productQuantityInMeters[getAdapterPosition()] = productQuanitityInMeter;
                    System.out.println("Product Quanity InMeters " + productQuanitityInMeter);
                    M_Clothes_Order_Details mClothesOrderDetails1 = mClothesOrderDetailsList.get(getAdapterPosition());
                    double clothQuantityInMeters = productQuantityInMeters[getAdapterPosition()];
                    updateProduct(mClothesOrderDetails1, productQuanitityInMeter);
                    break;
                case R.id.cart_mtr_decrement:
                    double productQuantityInMeter = productQuantityInMeters[getAdapterPosition()];
                    if (getAdapterPosition() != -1) {
                        if (productQuantityInMeter >= 0.10) {
//                            if (productQuantityInMeter >= 0.10) {
                                productQuanitityInMeter = productQuantityInMeters[getAdapterPosition()] - 0.10;
                                productQuantityInMeters[getAdapterPosition()] = productQuanitityInMeter;
                                M_Clothes_Order_Details m_clothes_order_details3 = mClothesOrderDetailsList.get(getAdapterPosition());
                                double clothQuantityForDecrement = productQuantityInMeters[getAdapterPosition()];
                                updateProductIfDecremneted(m_clothes_order_details3, clothQuantityForDecrement);
//                            }
                        }
                    }
                    break;
                case R.id.cart_mtr_delete:
                    alertbox();
                    break;
                case R.id.btn_qtysubmit:
//                    myCustomEditTextListener.updatePosition(getAdapterPosition());
                    double QuantityInMeter = productQuantityInMeters[getAdapterPosition()];
                    pos = getAdapterPosition();
                    if(cart_mtr_product_quantity_tv.getText().toString().equals("") | cart_mtr_product_quantity_tv.getText().toString().isEmpty()){
                        Toast.makeText(context,"Enter Qty",Toast.LENGTH_SHORT).show();
                    }else{
//                        inputqty = Integer.parseInt(cart_product_quantity.getText().toString().trim());
                        Log.e("increment inputqty", String.valueOf(inputqty));
                        if(inputqty != 0.10){
                          /*  if (inputqty < mClothesOrderDetailsList.get(getAdapterPosition()).getMin_qty_mteres()){
                                Toast.makeText(context,"Qty Cannot be less then Minimum Qty",Toast.LENGTH_SHORT).show();
                            }else {*/
                                productQuantityInMeters[getAdapterPosition()] = inputqty;
                                cart_mtr_product_quantity_tv.setText(formatter.format(inputqty));
                                M_Clothes_Order_Details mClothesOrderDetails3 = mClothesOrderDetailsList.get(pos);
                                updateProduct(mClothesOrderDetails3, inputqty);
                                ll_submitqty.setVisibility(View.GONE);
                                cart_mtr_increment.setVisibility(View.VISIBLE);
//                                cart_mtr_product_quantity_tv.setFocusable(false);
                                if (productQuantityInMeters[getAdapterPosition()] <=
                                        0.10) {
                                    cart_mtr_decrement.setVisibility(View.GONE);
                                   cart_mtr_delete.setVisibility(View.VISIBLE);
                                } else {
                                    cart_mtr_decrement.setVisibility(View.VISIBLE);
                                    cart_mtr_delete.setVisibility(View.GONE);
                                }
//                            }
                        }else{
                            Toast.makeText(context,"Qty cannot be 0",Toast.LENGTH_SHORT).show();
                            mClothesOrderDetailsList.get(getAdapterPosition()).setQty_mteres(0.10);
                            cart_mtr_product_quantity_tv.setText(formatter.format(mClothesOrderDetailsList.get(getAdapterPosition()).getQty_mteres()));
                            productQuantityInMeters[getAdapterPosition()] = mClothesOrderDetailsList.get(getAdapterPosition()).getQty_mteres();
                            /*cart_mtr_product_quantity_tv.setText
                                    (formatter.format(mClothesOrderDetailsList.get(getAdapterPosition()).getQty_mteres()));*/
                            ll_submitqty.setVisibility(View.GONE);
                            cart_mtr_increment.setVisibility(View.VISIBLE);
//                            cart_mtr_product_quantity_tv.setFocusable(false);
                            if (productQuantityInMeters[getAdapterPosition()] <= 0.10) {
                                cart_mtr_decrement.setVisibility(View.GONE);
                                cart_mtr_delete.setVisibility(View.VISIBLE);
                            } else {
                                cart_mtr_decrement.setVisibility(View.VISIBLE);
                                cart_mtr_delete.setVisibility(View.GONE);
                            }
                        }

                    }
                    break;
            }
        }

        private void updateProduct(M_Clothes_Order_Details mClothesOrderDetails, double clothQuantityInMeters) {
            double clothPrice = 0;
            double productQuantityInMeter = productQuantityInMeters[getAdapterPosition()];
            Log.e("Increment Meter", String.valueOf(clothQuantityInMeters));
            int price;

//            if (mClothesOrderDetails.getArt_offer_price() != 0) {
//                price = mClothesOrderDetails.getArt_offer_price();
//            } else {
                price = mClothesOrderDetails.getArt_selling_price_amt();
//            }
            if (customer_type.equals("0")) {
                if (productQuantities[getAdapterPosition()] > 0) {
                    //clothPrice = mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()] * productQuantityInMeters[getAdapterPosition()];
                    ll_cgst.setVisibility(View.VISIBLE);
                    ll_igst.setVisibility(View.GONE);
                    cgstAmount = (price * productQuantityInMeters[getAdapterPosition()] * 0.025);
                    System.out.println("CGST FORMULA : " +
                            price +
                            "*" +
                            productQuantityInMeters[getAdapterPosition()] +
                            "*" +
                            "0.025");
                    cgstAmount = Double.parseDouble(df.format(cgstAmount));
                    System.out.println("updateProduct cgst : " + cgstAmount);
                    sgstAmount = (price * productQuantityInMeters[getAdapterPosition()] * 0.025);
                    System.out.println("SGST FORMULA : " +
                            price +
                            "*" +
                            productQuantityInMeters[getAdapterPosition()] +
                            "*" +
                            "0.025");
                    sgstAmount = Double.parseDouble(df.format(sgstAmount));
                    System.out.println("sgstAmount  :" + sgstAmount);
                    clothPrice = (price * productQuantities[getAdapterPosition()] *
                            productQuantityInMeters[getAdapterPosition()]) + cgstAmount + sgstAmount;
                    System.out.println("CLOTH PRICE FORMULA : " +
                            price +
                            "*" +
                            productQuantityInMeters[getAdapterPosition()] +
                            "+" +
                            cgstAmount +
                            "+" +
                            sgstAmount +
                            "");

                    System.out.println("updateProduct clothPrice cart:" + clothPrice);
                }

            } else {
                if (productQuantities[getAdapterPosition()] > 0) {
                    //clothPrice = mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()] * productQuantityInMeters[getAdapterPosition()];
                    igstAmount = (price * productQuantityInMeters[getAdapterPosition()] * 0.05);
                    igstAmount = Double.parseDouble(df.format(igstAmount));
                    System.out.println("igstAmount else" + igstAmount);
                    clothPrice = (price * productQuantities[getAdapterPosition()] *
                            productQuantityInMeters[getAdapterPosition()]) + igstAmount;
                    ll_cgst.setVisibility(View.GONE);
                    ll_igst.setVisibility(View.VISIBLE);
                    if (productQuantityInMeters[getAdapterPosition()] >= 0.10) {
                        cart_mtr_decrement.setVisibility(View.VISIBLE);
                        cart_mtr_delete.setVisibility(View.GONE);
                    } else {
                        cart_mtr_decrement.setVisibility(View.GONE);
                        cart_mtr_delete.setVisibility(View.VISIBLE);
                    }
                }
            }

            ContentValues mContentValues = new ContentValues();
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS, clothQuantityInMeters);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, clothPrice);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CGST, cgstAmount);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SGST, sgstAmount);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IGST, igstAmount);
            long updatedRows = mydb.updateProductQuanitity(mContentValues, String.valueOf(mClothesOrderDetails.getArt_id()), mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            if (updatedRows > 0) {
                // Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
                cart_mtr_product_quantity_tv.setText(" " + formatter.format(clothQuantityInMeters));
                tvcgst.setText(" " + formatter.format(cgstAmount));
                tvsgst.setText(" " + formatter.format(sgstAmount));
                tvigst.setText(" " + formatter.format(igstAmount));
                tvTotal.setText(" " + formatter.format(clothPrice));
                cart_mtr_decrement.setVisibility(View.VISIBLE);
                cart_mtr_delete.setVisibility(View.GONE);
                homeCallBack.onCartUpdated();
               /* if (productQuantityInMeter <= 1.20) {

                    notifyDataSetChanged();
                }
                else{
                    cart_mtr_decrement.setVisibility(View.GONE);
                    cart_mtr_delete.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }*/
            } else {
                Toast.makeText(context, "Item not updated successfully", Toast.LENGTH_SHORT).show();
            }

        }

        private void updateProductIfDecremneted(M_Clothes_Order_Details mClothesOrderDetails, double clothQuantity) {
            double clothPrice = 0;
            double productQuantityInMeter = productQuantityInMeters[getAdapterPosition()];
            Log.e("Decremneted Meter", String.valueOf(productQuantityInMeter));
            int price;

            if (mClothesOrderDetails.getArt_offer_price() != 0) {
                price = mClothesOrderDetails.getArt_offer_price();
            } else {
                price = mClothesOrderDetails.getArt_selling_price_amt();
            }
            // TODO  18/11/2020 decrement selling price till minimum meter
            if (productQuantityInMeter <= 0.10) {
                cart_mtr_decrement.setVisibility(View.GONE);
                cart_mtr_delete.setVisibility(View.VISIBLE);
            } else {
                cart_mtr_decrement.setVisibility(View.VISIBLE);
                cart_mtr_delete.setVisibility(View.GONE);
            }
            if (customer_type.equals("0")) {
                if (productQuantityInMeters[getAdapterPosition()] >= 0.10) {
                    // clothPrice = mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()]*productQuantityInMeters[getAdapterPosition()];
                    ll_cgst.setVisibility(View.VISIBLE);
                    ll_igst.setVisibility(View.GONE);
                    cgstAmount = (price * productQuantityInMeters[getAdapterPosition()] * 0.025);
                    cgstAmount = Double.parseDouble(df.format(cgstAmount));
                    System.out.println("Decremneted cgst" + cgstAmount);
                    System.out.println("CGST updateProductIfDecremneted : " +
                            price +
                            "*" +
                            productQuantityInMeters[getAdapterPosition()] +
                            "*" +
                            "0.025");
                    sgstAmount = (price * productQuantityInMeters[getAdapterPosition()] * 0.025);
                    sgstAmount = Double.parseDouble(df.format(sgstAmount));
                    System.out.println("sgst Decremneted" + sgstAmount);
                    clothPrice = (price * productQuantities[getAdapterPosition()] *
                            productQuantityInMeters[getAdapterPosition()]) + cgstAmount + sgstAmount;
                    System.out.println("Decremnet clothPrice" + clothPrice);
                }
//         else {
//          clothPrice = mClothesOrderDetails.getArt_selling_price_amt() + cgstAmount +sgstAmount;
//          }
            } else {
                if (productQuantityInMeters[getAdapterPosition()] >= 0.10) {
                    igstAmount = (price * productQuantityInMeters[getAdapterPosition()] * 0.05);
                    igstAmount = Double.valueOf(df.format(igstAmount));
                    System.out.println("igstAmount else" + igstAmount);
                    clothPrice = (price * productQuantities[getAdapterPosition()] *
                            productQuantityInMeters[getAdapterPosition()]) + igstAmount;
                    ll_cgst.setVisibility(View.GONE);
                    ll_igst.setVisibility(View.VISIBLE);
                }

            }
            ContentValues mContentValues = new ContentValues();
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, clothPrice);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS, clothQuantity);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CGST, cgstAmount);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SGST, sgstAmount);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IGST, igstAmount);

            long updatedRows = mydb.updateProductQuanitity(mContentValues, String.valueOf(mClothesOrderDetails.getArt_id()), mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            if (updatedRows > 0) {
                // Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
                cart_mtr_product_quantity_tv.setText(" " + formatter.format(clothQuantity));
                tvcgst.setText(" " + formatter.format(cgstAmount));
                tvsgst.setText(" " + formatter.format(sgstAmount));
                tvigst.setText(" " + formatter.format(igstAmount));
                tvTotal.setText(" " + formatter.format(clothPrice));
                homeCallBack.onCartUpdated();
            } else {
                Toast.makeText(context, "Item not updated successfully", Toast.LENGTH_SHORT).show();
            }
        }

        public void alertbox() {
            final AlertDialog alertDialogBuilder = new AlertDialog.Builder(context).create();
            alertDialogBuilder.setTitle("Delete Item");
            alertDialogBuilder.setMessage("Are you sure want to delete this item?");

            alertDialogBuilder.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    M_Clothes_Order_Details m_clothes_order_details = mClothesOrderDetailsList.get(getAdapterPosition());
                    Log.d("removie art id",String.valueOf(m_clothes_order_details.getArt_id()));
                    int noOfRowsRemoved = mydb.removeProductFromCart(String.valueOf(m_clothes_order_details.getArt_id()), mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                    if (noOfRowsRemoved >= 1) {
                        mClothesOrderDetailsList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyDataSetChanged();
                        homeCallBack.onCartUpdated();
                    } else {
                        M_Clothes_Order_Details m_clothes_order_details1 = mClothesOrderDetailsList.get(getAdapterPosition());
                        int clothQuantityForDecrement = productQuantities[getAdapterPosition()];
                        deleteProduct(m_clothes_order_details1, clothQuantityForDecrement);
                    }

                    vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                }
            });

            alertDialogBuilder.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onShow(DialogInterface arg0) {
                    alertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.blue);
                    alertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.black);
                }
            });
            alertDialogBuilder.show();
            return;
        }

        private void deleteProduct(M_Clothes_Order_Details mClothesOrderDetails, int clothQuantity) {
            double clothPrice = 0;

            if (customer_type.equals("0")) {
                if (productQuantities[getAdapterPosition()] > 0) {
                    //clothPrice = mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()] * productQuantityInMeters[getAdapterPosition()];
                    ll_cgst.setVisibility(View.VISIBLE);
                    ll_igst.setVisibility(View.GONE);
                    cgstAmount = (mClothesOrderDetails.getArt_selling_price_amt() * productQuantityInMeters[getAdapterPosition()] * 0.025);
                    cgstAmount = Double.parseDouble(df.format(cgstAmount));
                    sgstAmount = (mClothesOrderDetails.getArt_selling_price_amt() * productQuantityInMeters[getAdapterPosition()] * 0.025);
                    sgstAmount = Double.parseDouble(df.format(sgstAmount));
                    clothPrice = (mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()] *
                            productQuantityInMeters[getAdapterPosition()]) + cgstAmount + sgstAmount;

                    System.out.println("delete clothPrice" + clothPrice);
                }

            } else {
                if (productQuantities[getAdapterPosition()] > 0) {
                    //clothPrice = mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()] * productQuantityInMeters[getAdapterPosition()];
                    igstAmount = (mClothesOrderDetails.getArt_selling_price_amt() * productQuantityInMeters[getAdapterPosition()] * 0.05);
                    igstAmount = Double.parseDouble(df.format(igstAmount));
                    System.out.println("igstAmount else" + igstAmount);
                    clothPrice = mClothesOrderDetails.getArt_selling_price_amt() * productQuantities[getAdapterPosition()] * productQuantityInMeters[getAdapterPosition()]
                            + igstAmount;
                    ll_cgst.setVisibility(View.GONE);
                    ll_igst.setVisibility(View.VISIBLE);

                } else {
                    clothPrice = mClothesOrderDetails.getArt_selling_price_amt() + igstAmount;
                }
            }
            ContentValues mContentValues = new ContentValues();
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT, clothQuantity);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, clothPrice);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CGST, cgstAmount);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SGST, sgstAmount);
            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IGST, igstAmount);
            long updatedRows = mydb.updateProductQuanitity(mContentValues, String.valueOf(mClothesOrderDetails.getArt_id()), mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            if (updatedRows > 0) {
                // Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
                // tvRate.setText(formatter.format(clothPrice));
                //cart_product_quantity_tv.setText(" "+clothQuantity);
                homeCallBack.onCartUpdated();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Item not updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

