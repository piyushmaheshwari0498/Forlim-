package com.fashion.forlempopoli.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fashion.forlempopoli.Adapter.CartAdapter;
import com.fashion.forlempopoli.Db.connection.DatabaseHelper;
import com.fashion.forlempopoli.Listener.OnCartUpdatedListener;
import com.fashion.forlempopoli.MainActivity;
import com.fashion.forlempopoli.Model.M_Clothes_Order_Details;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.Custom_Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartActivity extends AppCompatActivity implements OnCartUpdatedListener {
    public LinearLayout proceed_to_book;
    AppSharedPreference mappSharedPrefernce;
    MenuItem menuItem;
    CartAdapter cartAdapter;
    MaterialToolbar toolbar;
    String status;
    double total_amount;
    Integer credit_limit;
    String selected_cr_limit;
    AppUtils appUtils;
    LinearLayout proceed_layout, ll_NoData_Found;
    MaterialButton ctn_btn;
    private RecyclerView recycler_view_cart;
    private TextView grand_total_cart, item_cart;
    private DatabaseHelper mDatabaseHelper;
    private int cart_count = 0;
    private List<M_Clothes_Order_Details> mClothesOrderDetailsList;
    private double totalPrice;
    private double price, cgst, sgst;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //setTitle(Html.fromHtml("<font color='#FFFFFF'><small>My Cart</small></font>"));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Cart");
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        mappSharedPrefernce = AppSharedPreference.getAppSharedPreference(this.getApplicationContext());
        recycler_view_cart = findViewById(R.id.recycler_view_cart);
        grand_total_cart = findViewById(R.id.grand_total_cart);
        proceed_to_book = findViewById(R.id.proceed_to_book);
        item_cart = findViewById(R.id.item_cart);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);
        proceed_layout = findViewById(R.id.proceed_layout);
        ctn_btn = findViewById(R.id.ctn_btn);

        appUtils = new AppUtils();

        if (!appUtils.isConnected(CartActivity.this)) {
            Toasty.warning(CartActivity.this, getResources().getString(R.string.no_internet));

        } else {
            mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());
            getCartData();
        }

        ctn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(CartActivity.this)) {
                    Toasty.warning(CartActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    Intent mintent = new Intent(CartActivity.this, MainActivity.class);
                    mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mintent);
                    finish();
                }
            }
        });
        proceed_to_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appUtils.isConnected(CartActivity.this)) {
                    Toasty.warning(CartActivity.this, getResources().getString(R.string.no_internet));

                } else {
                    status = mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_STATUS);
//                    System.out.println("status" + status);
                    credit_limit = Integer.valueOf(mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_PENDING_BAL));
//                    System.out.println("limit" + credit_limit);
                    total_amount = totalPrice;
//                    System.out.println("amount" + total_amount);
                    selected_cr_limit = mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACC_CR_LIMIT);
//                    System.out.println("selected_cr_limit" + selected_cr_limit);

                    if (status.equals("1")) {
                        switch (selected_cr_limit) {
                            case "LIMIT":
//                                System.out.println("LIMIT");
                                if (total_amount > credit_limit) {
                                    Custom_Toast.long_warning(CartActivity.this,
                                            "Your credit Limit is crossed.You can't place the order.");
                                } else {
                                    navigateToOrderActivity();
                                }
                                break;
                            case "NOLIMIT":
//                                System.out.println("NOLIMIT");
                                navigateToOrderActivity();
                                break;
                            case "VIP":
//                                System.out.println("VIP");
                                navigateToOrderActivity();
                                break;
                        }
                    } else {
//                        System.out.println("else");
                        Toast.makeText(CartActivity.this, "Contact with administrator", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getCartData() {
        mClothesOrderDetailsList = mDatabaseHelper.getAllCartProduct(mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        if (!mClothesOrderDetailsList.isEmpty()) {
            cartAdapter = new CartAdapter(mClothesOrderDetailsList, CartActivity.this, CartActivity.this);
            recycler_view_cart.setLayoutManager(new LinearLayoutManager(this));
            recycler_view_cart.setAdapter(cartAdapter);
            updateTotalPrice();
            getCartCount();
            updateCgst();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getCartCount() {
        cart_count = mDatabaseHelper.getCartCount(mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        if (cart_count == 0) {
            ll_NoData_Found.setVisibility(View.VISIBLE);
            recycler_view_cart.setVisibility(View.GONE);
            proceed_layout.setVisibility(View.GONE);
        } else {
            item_cart.setText(String.valueOf("(" + cart_count + " items)"));
        }
    }

    private void updateTotalPrice() {
        totalPrice = mDatabaseHelper.getTotalCartPrice(mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        if (totalPrice == 0) {
            proceed_to_book.setEnabled(false);
        }
        grand_total_cart.setText("₹" + totalPrice);
    }

    private void updateCgst() {
        price = mDatabaseHelper.getPrice(mappSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        cgst = (price * 2.5) / 100;
        System.out.println("cgst" + cgst);
        sgst = (price * 2.5) / 100;
        System.out.println("sgst" + sgst);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* getMenuInflater().inflate(R.menu.menu_clothes_cart, menu);
        menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(this, cart_count, R.drawable.shopping_bag));*/
        return true;
    }


    private void navigateToOrderActivity() {
        Intent i = new Intent(CartActivity.this, DeliveryAddressActivity.class);
        Log.e("total_amt", String.valueOf(totalPrice));
        i.putExtra("total_amt",String.valueOf(totalPrice));
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
           /* case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "There is no item in cart", Toast.LENGTH_SHORT).show();
                }
                break;*/
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCartUpdated() {
        getCartCount();
        invalidateOptionsMenu();
        updateTotalPrice();
        updateCgst();
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
