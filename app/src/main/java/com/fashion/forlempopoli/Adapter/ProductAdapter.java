package com.fashion.forlempopoli.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fashion.forlempopoli.Activity.ProductImageActivity;
import com.fashion.forlempopoli.Db.connection.DatabaseHelper;
import com.fashion.forlempopoli.Db.connection.ITable;
import com.fashion.forlempopoli.Entity.request.ProductRequest;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.Custom_Toast;
import com.fashion.forlempopoli.diffUtil.ProductDiffUtil;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<ProductRequest> productRequestList;
    Context context;
    String image_URL = "http://forlimpopoli.in/beta_2/public/uploads/articles/";
    String image_not_found_URL = "http://forlimpopoli.in/beta_2/public/assets/img/";
    String photopath;
    DatabaseHelper mydb;
    //    List<ProductRequest> productListFiltered;
//    List<ProductRequest> originalList;
    List<ProductRequest> oldList = Collections.emptyList();
    boolean ischecked = true;
    boolean isAscchecked = true;
    PhotoViewAttacher mAttacher;
    String customer_type;
    Vibrator vibe;
    ArrayList<String> garmentPath;
    String garmentImg;
    List<ProductRequest> filteredList;
    private int[] productQuantities;
    private HomeCallBack homeCallBack;
    private AppSharedPreference mAppSharedPrefernce;
    private DecimalFormat df, percantageFormatter;

    public ProductAdapter(List<ProductRequest> productRequestList, Context context, HomeCallBack mCallBackus) {
        this.productRequestList = productRequestList;
        this.context = context;
        productQuantities = new int[productRequestList.size()];
        mydb = DatabaseHelper.getInstance(context.getApplicationContext());
        this.homeCallBack = mCallBackus;
//        this.productListFiltered = productRequestList;
//        this.originalList = productRequestList;
        garmentPath = new ArrayList<>();
        df = new DecimalFormat("#.##");
        percantageFormatter = new DecimalFormat("#");
        vibe = (Vibrator)
                context.getSystemService(Context.VIBRATOR_SERVICE);
        ProductDiffUtil productDiffUtil = new ProductDiffUtil(oldList, productRequestList);
        DiffUtil.DiffResult diffUtilResult = DiffUtil.calculateDiff(productDiffUtil);
        this.oldList = productRequestList;
        diffUtilResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_adpter_layout, viewGroup, false);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(context);
        //garmentPath.clear();
        return new ViewHolder(v);
    }

//    public setData(List<ProductRequest> newdata){
//        ProductDiffUtil productDiffUtil = new ProductDiffUtil(originalList,newdata);
//        DiffUtil.DiffResult diffUtilResult = DiffUtil.calculateDiff(productDiffUtil);
//        originalList = newdata;
//        diffUtilResult.dispatchUpdatesTo(this);
//    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        String mtrs = String.valueOf(oldList.get(i).getArtQtyMtrs());
        if (!mtrs.isEmpty()) {
            String clothesIdObtained = String.valueOf(oldList.get(i).getArtId());

            int countOfProduct = mydb.getCountOfProduct
                    (clothesIdObtained, mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));

            boolean isExist = mydb.getifProductExist
                    (clothesIdObtained, mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));

            if (isExist) {
                viewHolder.addCartBtn.setEnabled(false);
                viewHolder.addCartBtn.setText("Added");
            }
//            System.out.println("image_URL product" + image_URL + photopath);
            mAttacher = new PhotoViewAttacher(viewHolder.image);
            viewHolder.tvSavingPrice.setVisibility(View.GONE);
            // System.out.println("checked"+ischecked);
            if (ischecked) {
                viewHolder.tvPrice.setVisibility(View.VISIBLE);
                viewHolder.tvofferPrice.setVisibility(View.VISIBLE);
                viewHolder.tvSavingPrice.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvPrice.setVisibility(View.GONE);
                viewHolder.tvofferPrice.setVisibility(View.GONE);
                viewHolder.tvSavingPrice.setVisibility(View.GONE);
            }

            /*for (ProductRequest str : oldList) {
                //for(int asc: list){
                System.out.println("asc : "+str);
            }
*/
            viewHolder.productName.setText(oldList.get(i).getArtName());
            viewHolder.tvwidth.setText("Width: "+oldList.get(i).getArtWidth());
            viewHolder.tvArticleNo.setText(oldList.get(i).getArtNo());

            if(oldList.get(i).getCatName().toLowerCase().contains("shirting")){
                viewHolder.tvMeters.setText("[ 1.6 m ]");
            }else if(oldList.get(i).getCatName().toLowerCase().contains("suiting")){
                viewHolder.tvMeters.setText("[ 1.3 m ]");
            }else{
                viewHolder.tvMeters.setText("[ 1.0 m ]");
            }
//            viewHolder.tvMeters.setText("[ " + oldList.get(i).getArtQtyMtrs() + " m ]");

            photopath = oldList.get(i).getArtPhoto();
//        Log.d("getGarmentPhoto ", String.valueOf(garmentPath.size()));
            // garmentPath.add(image_URL + productListFiltered.get(i).getGarmentPhoto());
            garmentImg = image_URL + oldList.get(i).getGarmentPhoto();
//        Log.d("getGarmentPhoto ", image_URL + productListFiltered.get(i).getGarmentPhoto());
//            Log.d("price", String.valueOf(oldList.get(i).getArtOfferPrice()));
//            Log.d("photopath", photopath);
//            Log.d("garmentImg", String.valueOf(garmentImg));

//            float saving_amount = PatternClass.savingAmount(Float.parseFloat(oldList.get(i).getArtOfferPrice()),
//                    oldList.get(i).getArtSellingPriceAmt());
//            if (!oldList.get(i).getArtOfferPrice().equals("0.00")) {
//                viewHolder.tvofferPrice.setText("₹" + oldList.get(i).getArtOfferPrice());
//                viewHolder.tvSavingPrice.setText("You're saving ₹" + percantageFormatter.format(saving_amount));
//                viewHolder.tvofferPrice.setTextColor(context.getResources().getColor(R.color.red));
//                viewHolder.tvPrice.setText("₹" + oldList.get(i).getArtSellingPriceAmt());
//                viewHolder.tvPrice.setPaintFlags(viewHolder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            } else {
                viewHolder.tvofferPrice.setText("₹" + oldList.get(i).getArtSellingPriceAmt());
                viewHolder.tvofferPrice.setTextColor(context.getResources().getColor(R.color.red));
                viewHolder.tvPrice.setText("");
                viewHolder.tvSavingPrice.setText("");
//            viewHolder.tvSavingPrice.setVisibility(View.GONE);
//            }

            String url = image_URL + photopath;
            String not_found_url = image_not_found_URL + photopath;
            if (!url.contains("no-photo.jpg")) {
//            Picasso.get().load(image_URL + photopath).int(viewHolder.image);
                Glide.with(context)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.no_image_found)
                        .into(viewHolder.image);
                viewHolder.tvviewArtImage.setVisibility(View.VISIBLE);
            } else {
                Glide.with(context)
                        .load(not_found_url)
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_error_outline_white_24dp)
                        .into(viewHolder.image);

                viewHolder.tvviewArtImage.setVisibility(View.GONE);
                // viewHolder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image_found));
            }
        }
    }

    @Override
    public int getItemCount() {
        return productRequestList.size();
    }

   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
                filteredList = new ArrayList<>();
                Filter.FilterResults filterResults;
                if (charString.isEmpty()) {
                    filteredList = productRequestList;
                } else {

                    for (ProductRequest row : productRequestList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name  match

                        if (row.getArtNo().toLowerCase().trim().contains(charString.toLowerCase().trim())) {
                            System.out.println("row added:ProductAdapter char " +charString);
                            System.out.println("row data  "+row.getArtNo());
                            filteredList.add(row);
                        } */
    /*else if (row.getArtName().toLowerCase().trim().contains(charString.toLowerCase().trim())) {
                            System.out.println("row added:ProductAdapter" +charStrings);
                            filteredList.add(row);
                        }*/
    /*
                    }
                    //productListFiltered = filteredList;
                }
                filterResults = new FilterResults();
                filterResults.values = filteredList;
                productRequestList = (ArrayList<ProductRequest>) filterResults.values;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productRequestList = (ArrayList<ProductRequest>) filterResults.values;
                oldList = productRequestList;
                System.out.println("row added:ProductAdapter size"+ oldList.size());
                notifyDataSetChanged();
            }
        };
    }*/

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public void setIsAscchecked(boolean ischecked) {
        this.isAscchecked = ischecked;
    }

    @Override
    public String toString() {
        return "ProductAdapter{" +
                "ischecked=" + ischecked +
                '}';
    }

    private void showBottomSheetDialog(ArrayList<String> photo) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.product_images_bottom_sheet);

        ViewPager viewPager;
        LinearLayout sliderDotspanel;
        int dotscount;
        ImageView[] dots;

        bottomSheetDialog.show();


        Log.d("garmentPath", String.valueOf(garmentPath.size()));
        ProductViewPagerAdapter viewPagerAdapter = new ProductViewPagerAdapter(context, photo);

        viewPager = (ViewPager) bottomSheetDialog.findViewById(R.id.pager);

        sliderDotspanel = (LinearLayout) bottomSheetDialog.findViewById(R.id.SliderDots);


        //   for (int i = 0;i<garmentPath.size();i++) {
        viewPager.setAdapter(viewPagerAdapter);
        //  }
        //  viewPager.setCurrentItem(1);

//        Log.d("viewPagerAdagetCount()", String.valueOf(viewPagerAdapter.getCount()));
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                int index = viewPager.getCurrentItem();
//                if (state == ViewPager.SCROLL_STATE_IDLE) {
//
//                    if (index == 0)
//                        viewPager.setCurrentItem(viewPagerAdapter.getCount() - 2, false);
//                    else if (index == viewPagerAdapter.getCount() - 1)
//                        viewPager.setCurrentItem(1, false);
//                }
            }
        });

    }

    // this interface creates for call the invalidateoptionmenu() for refresh the menu item
    public interface HomeCallBack {
        void updateCartCount(Context context);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView productName, tvArticleNo, tvMeters, tvPrice, tvofferPrice, update_quantity_dialog,tvwidth, tvSavingPrice;
        ImageView tvviewImage, tvviewArtImage;
        MaterialButton addCartBtn;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            productName = itemView.findViewById(R.id.productName);
            tvwidth = itemView.findViewById(R.id.tvwidth);
            tvArticleNo = itemView.findViewById(R.id.tvArticleNo);
            tvMeters = itemView.findViewById(R.id.tvMeters);
            tvofferPrice = itemView.findViewById(R.id.tvofferPrice);
            tvPrice = itemView.findViewById(R.id.tvSalePrice);
            tvviewImage = itemView.findViewById(R.id.tvviewImage);
            tvviewArtImage = itemView.findViewById(R.id.tvviewArtImage);
            addCartBtn = itemView.findViewById(R.id.addCartBtn);
            tvSavingPrice = itemView.findViewById(R.id.tvSavingPrice);
            addCartBtn.setOnClickListener(this);
            tvviewImage.setOnClickListener(this);
            tvviewArtImage.setOnClickListener(this);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addCartBtn:
                    customer_type = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_CUSTOMER_TYPE);
//                    System.out.println("customer_type" + customer_type);
                    if (customer_type.equals("0")) {
                        // TODO: 21-08-2020 Step 1: -Get Data Of Selected "Add" Row
                        ProductRequest mproductRequest = oldList.get(getAdapterPosition());

                        String clothesIdObtained = String.valueOf(mproductRequest.getArtId());

//                        double offerPrice = Double.parseDouble(mproductRequest.getArtOfferPrice());
                        double sellingprice = mproductRequest.getArtSellingPriceAmt();
                        double totalPrice = 0;
                        double cgstAmount = 0;
                        double sgstAmount = 0;
                        if(mproductRequest.getCatName().toLowerCase().contains("shirting")){
                            mproductRequest.setArtQtyMtrs(1.6);
                        }else if(mproductRequest.getCatName().toLowerCase().contains("suiting")){
                            mproductRequest.setArtQtyMtrs(1.3);
                        }else{
                            mproductRequest.setArtQtyMtrs(1.0);
                        }
                        System.out.println("getArtQtyMtrs" + mproductRequest.getArtQtyMtrs());
                        Log.d("offerPrice", String.valueOf(sellingprice));
//                        if (offerPrice != 0.00) {
//                            /*totalPrice = (offerPrice * mproductRequest.getArtQtyMtrs()) + cgstAmount + sgstAmount;
//                            cgstAmount = (offerPrice * mproductRequest.getArtQtyMtrs() * 0.025);
//                            sgstAmount = (offerPrice * mproductRequest.getArtQtyMtrs() * 0.025);*/
//                            cgstAmount = (offerPrice * mproductRequest.getArtQtyMtrs() * mproductRequest.getArtCgstPer()) / 100;
//                            cgstAmount = Double.valueOf(df.format(cgstAmount));
//                            sgstAmount = (offerPrice * mproductRequest.getArtQtyMtrs() * mproductRequest.getArtSgstPer()) / 100;
//                            sgstAmount = Double.valueOf(df.format(sgstAmount));
//                            totalPrice = (offerPrice * mproductRequest.getArtQtyMtrs()) + cgstAmount + sgstAmount;
//                            System.out.println("total_price product:" + totalPrice);
//                        } else {
                            cgstAmount = (mproductRequest.getArtSellingPriceAmt() * mproductRequest.getArtQtyMtrs() * mproductRequest.getArtCgstPer()) / 100;
                            cgstAmount = Double.valueOf(df.format(cgstAmount));
                            sgstAmount = (mproductRequest.getArtSellingPriceAmt() * mproductRequest.getArtQtyMtrs() * mproductRequest.getArtSgstPer()) / 100;
                            sgstAmount = Double.valueOf(df.format(sgstAmount));
                            totalPrice = (mproductRequest.getArtSellingPriceAmt() * mproductRequest.getArtQtyMtrs()) + cgstAmount + sgstAmount;
                            System.out.println("total_price product:" + totalPrice);
                            // System.out.println("sgstAmount" + sgstAmount);
//                            totalPrice = (sellingprice * mproductRequest.getArtQtyMtrs()) + cgstAmount + sgstAmount;
//                            cgstAmount = (sellingprice * mproductRequest.getArtQtyMtrs() * 0.025);
//                            sgstAmount = (sellingprice * mproductRequest.getArtQtyMtrs() * 0.025);
//                        }

                        //    cgstAmount = Double.valueOf(df.format(cgstAmount));


                        // sgstAmount = Double.valueOf(df.format(sgstAmount));
//                        System.out.println("clothesIdObtained API" + mproductRequest.getArtId());
//                        System.out.println("clothesIdObtained LIst" + clothesIdObtained);
//                        System.out.println("sellingprice" + sellingprice);
//                        System.out.println("offerPrice" + offerPrice);
//                        System.out.println("sgstAmount" + sgstAmount);
//                        System.out.println("cgstAmount" + cgstAmount);
//                        System.out.println("total_price product:" + totalPrice);

                        int clothQuantity = 1;

                        // TODO: 21-08-2020  Check Product Exits Before Adding To Cart
                        int countOfProduct = mydb.getCountOfProduct(clothesIdObtained, mAppSharedPrefernce.getString
                                (Constants.INTENT_KEYS.KEY_ACCOUNT_ID));

                        // TODO: 21-08-2020 if count of product=0 insert new Product.
                        int isExist = mydb.getCurrentProduct(clothesIdObtained, mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                        if (countOfProduct == 0) {
                            System.out.println("Product Count is 0");
                            ContentValues mContentValues = new ContentValues();
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.ACC_ID, mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID, clothesIdObtained);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT, clothQuantity);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE, mproductRequest.getArtSellingPriceAmt());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED, "0");
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS, mproductRequest.getArtQtyMtrs());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS, mproductRequest.getArtQtyMtrs());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID, mproductRequest.getArtCatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID, mproductRequest.getArtScatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER, mproductRequest.getArtNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH, mproductRequest.getArtWidth());
//                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE, mproductRequest.getArtOfferPrice());
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
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, totalPrice);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL, totalPrice);
                            long insertId = mydb.insertCartDetails(mContentValues);
                            if (insertId > 0) {
                                homeCallBack.updateCartCount(context);
                                Custom_Toast.add_cart_toast(context, context.getResources().getString(R.string.added_cart));
                                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                                addCartBtn.setEnabled(false);
                                addCartBtn.setText("Added");
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.not_addedd_cart), Toast.LENGTH_SHORT).show();
                            }
                        }


                        // TODO: 29-12-2019 If count of product =1 update existing product
                        else if (countOfProduct == 1) {
                            System.out.println("Product Count is 1");
                            Toast.makeText(context, context.getResources().getString(R.string.already_cart), Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("Product Count is : " + countOfProduct);
                        }
                    }
                    else {
                        ProductRequest mproductRequest = oldList.get(getAdapterPosition());
                        String clothesIdObtained = String.valueOf(mproductRequest.getArtId());
                        //double totalPrice = mproductRequest.getArtSellingPriceAmt() + igstAmount;

//                        double offerPrice = Double.parseDouble(mproductRequest.getArtOfferPrice());
                        double sellingprice = mproductRequest.getArtSellingPriceAmt();
                        double totalPrice = 0;
                        double igstAmount = 0;
//                        if (offerPrice != 0.00) {
//                            igstAmount = (offerPrice * mproductRequest.getArtIgstPer()) / 100;
//                            totalPrice = (offerPrice * mproductRequest.getArtQtyMtrs()) + igstAmount;
//                            igstAmount = Double.valueOf(df.format(igstAmount));
//                            System.out.println("igstAmount else" + igstAmount);
//                        } else {

                            igstAmount = (sellingprice * mproductRequest.getArtIgstPer()) / 100;
                            totalPrice = (sellingprice * mproductRequest.getArtQtyMtrs()) + igstAmount;
                            igstAmount = Double.valueOf(df.format(igstAmount));
                            System.out.println("igstAmount else" + igstAmount);
//                        }

                        System.out.println("clothesIdObtained" + clothesIdObtained);

                        if(mproductRequest.getCatName().toLowerCase().contains("shirting")){
                            mproductRequest.setArtQtyMtrs(1.6);
                        }else if(mproductRequest.getCatName().toLowerCase().contains("suiting")){
                            mproductRequest.setArtQtyMtrs(1.3);
                        }else{
                            mproductRequest.setArtQtyMtrs(1.0);
                        }


                        System.out.println("total_price product1 else" + totalPrice);
                        System.out.println("getArtQtyMtrs" + mproductRequest.getArtQtyMtrs());
                        int clothQuantity = 1;
                        int countOfProduct = mydb.getCountOfProduct(clothesIdObtained, mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                        if (countOfProduct == 0) {
                            ContentValues mContentValues = new ContentValues();
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.ACC_ID, mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_DETAILS_ID, clothesIdObtained);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ORDER_UNIT, clothQuantity);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SELLING_PRICE, mproductRequest.getArtSellingPriceAmt());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IS_ORDER_PLACED, "0");
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_QUANTITY_METERS, mproductRequest.getArtQtyMtrs());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_MIN_ART_QUANTITY_METERS, mproductRequest.getArtQtyMtrs());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CATEGORY_ID, mproductRequest.getArtCatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SUB_CATEGORY_ID, mproductRequest.getArtScatId());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NUMBER, mproductRequest.getArtNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_WIDTH, mproductRequest.getArtWidth());
//                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_OFFER_PRICE, mproductRequest.getArtOfferPrice());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_STATUS, mproductRequest.getArtStatus());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_NAME, mproductRequest.getArtName());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_SHADE_NO, mproductRequest.getArtShadeNo());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_STOCK_TYPE, mproductRequest.getArtStockType());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_COMPOSITION, mproductRequest.getArtComposition());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_PHOTO, mproductRequest.getArtPhoto());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_ART_GARMENT_PHOTO, mproductRequest.getGarmentPhoto());
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_IGST, igstAmount);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_CUSTOMER_TYPE, customer_type);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_TOTAL_PRICE, totalPrice);
                            mContentValues.put(ITable.CLOTHES_ORDER_DETAILS.COL_PER_ITEM_TOTAL, totalPrice);
                            long insertId = mydb.insertCartDetails(mContentValues);
                            if (insertId > 0) {
                                homeCallBack.updateCartCount(context);
//                                Toast.makeText(context, context.getResources().getString(R.string.added_cart), Toast.LENGTH_SHORT).show();
                                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                                Custom_Toast.add_cart_toast(context, context.getResources().getString(R.string.added_cart));
                                addCartBtn.setEnabled(false);
                                addCartBtn.setText("Added");
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.not_addedd_cart), Toast.LENGTH_SHORT).show();
                            }
                        } else if (countOfProduct == 1) {
                            Toast.makeText(context, context.getResources().getString(R.string.added_cart), Toast.LENGTH_SHORT).show();
                            addCartBtn.setEnabled(false);
                            addCartBtn.setText("Added");
                        }
                    }
                    break;
                case R.id.tvviewImage:
                    ArrayList<String> artPhoto = new ArrayList<>();
                    artPhoto.add(image_URL + productRequestList.get(getAdapterPosition()).getGarmentPhoto());
//                    String url = image_URL + photopath;
//                    if (!url.contains("no-photo.jpg"))
//                    Log.d("artPhoto", String.valueOf(artPhoto.size()));
                    if (!artPhoto.contains("no-photo.jpg")) {
                      /*  Intent intent = new Intent(context, ProductImageActivity.class);
                        intent.putExtra("position", artPhoto);
                        System.out.println("artPhoto" + artPhoto);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);*/
                        showBottomSheetDialog(artPhoto);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.image_not_found), Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.tvviewArtImage:
                    ArrayList<String> artPhoto2 = new ArrayList<>();
                    artPhoto2.add(image_URL + productRequestList.get(getAdapterPosition()).getArtPhoto());
                    String url = image_URL + productRequestList.get(getAdapterPosition()).getArtPhoto();
                    //if (!url.contains("no-photo.jpg"))
//                    Log.d("artPhoto", String.valueOf(artPhoto2.size()));
                    //  if (!url.contains("no-photo.jpg")) {
                    Intent intent = new Intent(context, ProductImageActivity.class);
                    intent.putExtra("position", url);
//                    System.out.println("artPhoto" + url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    /*} else {
                        Toast.makeText(context, context.getResources().getString(R.string.image_not_found), Toast.LENGTH_LONG).show();
                    }*/
                    break;
            }
        }
    }
}


