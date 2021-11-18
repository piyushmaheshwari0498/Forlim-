package com.example.forlempopoli;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.forlempopoli.Activity.CartActivity;
import com.example.forlempopoli.Activity.GlobalSearchActivity;
import com.example.forlempopoli.Activity.InvoiceActivity;
import com.example.forlempopoli.Activity.NotificationPanelActivity;
import com.example.forlempopoli.Activity.SearchActivity;
import com.example.forlempopoli.Db.connection.DatabaseHelper;
import com.example.forlempopoli.Entity.request.CreditDetailsRequest;
import com.example.forlempopoli.Entity.response.CreditDetailsResponse;
import com.example.forlempopoli.Fragment.Change_Password_Fragment;
import com.example.forlempopoli.Fragment.CustomerLedgerFragment;
import com.example.forlempopoli.Fragment.HomeFragment;
import com.example.forlempopoli.Fragment.OrderStatusFragment;
import com.example.forlempopoli.Fragment.PaymentDetailsFragment;
import com.example.forlempopoli.Fragment.ProfileViewPagerFragment;
import com.example.forlempopoli.Fragment.SaleReturnFragment;
import com.example.forlempopoli.Fragment.SamplingDataFragment;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.AppUtils;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.Converter;
import com.example.forlempopoli.Utilities.Custom_Toast;
import com.example.forlempopoli.Utilities.HideKeyboard;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction transaction;
    AppSharedPreference mappSharedPreference;
    MaterialToolbar toolbar;
    Fragment fragment;
    String credit_days;
    String credit_limit;
    String credit_amount;
    DatabaseHelper mydb;
    HideKeyboard hideKeyboard;
    AppUtils appUtils;
    DatabaseHelper mDatabaseHelper;
    boolean doubleBackToExitPressedOnce = false;
    TextView mTextView, nav_header1, nav_headercredit,nav_headerpending;
    ImageView imageViewIcon;
    List<CreditDetailsRequest> list;
    private AppBarConfiguration mAppBarConfiguration;
    private int clickedNavItem = 0;
    private int cart_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        mTextView = findViewById(R.id.mTextView);

        imageViewIcon = findViewById(R.id.imageViewIcon);

//        toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
//        toolbar.setContentInsetsAbsolute(0, 0);
//        toolbar.setTitle(R.string.app_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));
        }
        mappSharedPreference = AppSharedPreference.getAppSharedPreference(this);
        mydb = DatabaseHelper.getInstance(MainActivity.this);
        hideKeyboard = new HideKeyboard();
        appUtils = new AppUtils();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_header1 = headerView.findViewById(R.id.nav_header1);
        nav_headerpending = headerView.findViewById(R.id.nav_header2);
        nav_headercredit = headerView.findViewById(R.id.nav_header3);
        loadragment(new HomeFragment());

        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, 100)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 100)
                        .build());
        /*
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        mappSharedPreference.checkLogin(this);

        hideKeyboard.hide(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (!appUtils.isConnected(MainActivity.this)) {
            Toasty.warning(this, getResources().getString(R.string.no_internet));

        } else {
            mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());
            getCartCount();
            fetchCreditDetails();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void loadragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        hideKeyboard.hide(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        this.doubleBackToExitPressedOnce = false;

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            toolbar.setTitle(R.string.app_title);
            fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            if (!appUtils.isConnected(MainActivity.this)) {
                Toasty.warning(this, getResources().getString(R.string.no_internet));

            } else {
                fetchCreditDetails();
            }
        } else if (id == R.id.nav_profile) {
            toolbar.setTitle(getResources().getString(R.string.menu_profile));
            fragment = new ProfileViewPagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_order_status) {
            toolbar.setTitle(getResources().getString(R.string.menu_order_status));
            fragment = new OrderStatusFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_invoice) {
            Intent intent = new Intent(MainActivity.this, InvoiceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sale_return) {
            toolbar.setTitle(getResources().getString(R.string.menu_sale_return_details));
            fragment = new SaleReturnFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_payment_details) {
            toolbar.setTitle(getResources().getString(R.string.menu_payment));
            fragment = new PaymentDetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_sampling_data) {
            toolbar.setTitle(getResources().getString(R.string.menu_sampling_data));
            fragment = new SamplingDataFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_reports) {
            toolbar.setTitle(getResources().getString(R.string.menu_reports));
            fragment = new CustomerLedgerFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_change_password) {
            toolbar.setTitle(getResources().getString(R.string.change_password));
            fragment = new Change_Password_Fragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.nav_host_fragment, fragment)
//                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_exit) {
            clickedNavItem = R.id.nav_exit;
            alertbox();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard.hide(this);
        return true;
    }

    public void alertbox() {
        /*final AlertDialog alertDialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
        alertDialogBuilder.setTitle(getString(R.string.want_to_logout));
//        alertDialogBuilder.setMessage(getString(R.string.want_to_logout));

        alertDialogBuilder.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
//                    mappSharedPreference.putBooleanValue(Constants.IS_LOGGED_IN, false);
//                    mappSharedPreference.clearAllValues();
                    mappSharedPreference.logoutUser(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (Exception ex) {
                    ex.getMessage();
                }

            }
        });

        alertDialogBuilder.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.black);
                alertDialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.black);
            }
        });
        alertDialogBuilder.show();*/

        // Way 2
        /*new MaterialAlertDialogBuilder(MainActivity.this, R.style.MyMaterialAlertDialog)
                .setTitle(getResources().getString(R.string.want_to_logout))
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mappSharedPreference.logoutUser(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("NO", *//* listener = *//* null)
                .show();*/

        //Way 3
        showBottomSheetDialog();
        return;
    }

    private void showBottomSheetDialog() {
         BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.logout_bottom_sheet);

        MaterialButton logout_yes = bottomSheetDialog.findViewById(R.id.btn_logout_yes);
        MaterialButton logout_no = bottomSheetDialog.findViewById(R.id.btn_logout_no);

        bottomSheetDialog.show();

        logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                mappSharedPreference.logoutUser(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        logout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Copy is Clicked ", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        return;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // System.out.println("Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // System.out.println("Potrait");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clothes_cart, menu);
        MenuItem menuItem2 = menu.findItem(R.id.cart_action);
        MenuItem notificaitonmenuItem = menu.findItem(R.id.notification_action);
        MenuItem searchmenuItem = menu.findItem(R.id.search_action);
        searchmenuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, 0, R.drawable.ic_baseline_search_24));
        menuItem2.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.shopping_bag));
        notificaitonmenuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, 0, R.drawable.ic_baseline_notifications_24));
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent mintent;
        switch (item.getItemId()) {
            case R.id.search_action:
                mintent = new Intent(MainActivity.this, GlobalSearchActivity.class);
                startActivity(mintent);
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.cart_action:
                if (cart_count < 1) {
                    Toast.makeText(this, "There is no item in cart", Toast.LENGTH_SHORT).show();
                } else {
                    mintent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(mintent);
                }
                break;
            case R.id.notification_action:
                mintent = new Intent(MainActivity.this, NotificationPanelActivity.class);
                startActivity(mintent);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!appUtils.isConnected(MainActivity.this)) {
            Toasty.warning(this, getResources().getString(R.string.something_wrong));

        } else {
            getCartCount();
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!appUtils.isConnected(MainActivity.this)) {
            Toasty.warning(this, getResources().getString(R.string.something_wrong));

        } else {
            getCartCount();
        }
        invalidateOptionsMenu();

        this.doubleBackToExitPressedOnce = false;
    }

    private void getCartCount() {
        cart_count = mDatabaseHelper.getCartCount(mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
    }

    private void fetchCreditDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<CreditDetailsResponse> call = apiInterface.getCreditDetails(getHashMap());
        call.enqueue(new Callback<CreditDetailsResponse>() {
            @Override
            public void onResponse(Call<CreditDetailsResponse> call, Response<CreditDetailsResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    // System.out.println("success" + response.body().getStatusMessage());
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        list = response.body().getData();
                        if (!list.isEmpty()) {
                            // TODO: 19-08-2020 Saving Data to SharedPreference
                            System.out.println("getAccLimit" + list.get(0).getAccLimit());
                            System.out.println("getPendingBalance" + list.get(0).getPendingBalance());
                            System.out.println("getTotalBalance" + list.get(0).getTotalBalance());

                            mappSharedPreference.putStringValue(Constants.INTENT_KEYS.KEY_CREDIT_LIMIT_AMOUNT,
                                    String.valueOf(list.get(0).getAccLimit()));

                            mappSharedPreference.putStringValue(Constants.INTENT_KEYS.KEY_PENDING_BAL,
                                    String.valueOf(list.get(0).getPendingBalance()));

                            mappSharedPreference.putStringValue(Constants.INTENT_KEYS.KEY_TOTAL_BALANCE,
                                    String.valueOf(list.get(0).getTotalBalance()));

                            Log.d("KEY_PENDING_BAL", String.valueOf(mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_PENDING_BAL)));
                            credit_days = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_CREDIT_DAYS);
                            credit_limit = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_PENDING_BAL);
                            String username = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_PERSON_NAME);

                            nav_header1.setText("Hi, " + username);

                            float pending_bal = Math.abs(Float.parseFloat(credit_limit));
                            if (!mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACC_CR_LIMIT).equals("LIMIT")) {
                                System.out.println("no limit or no days");
                                SpannableString s = new SpannableString
                                        (getString(R.string.balance_credit_days) + " " + credit_days);
                                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, s.length(), 0);
                                nav_headerpending.setText(s);
                                nav_headercredit.setText(list.get(0).getAccLimit());
                            } else {
                                SpannableString s = new SpannableString
                                        (getString(R.string.balance_credit_days) + "  " + credit_days + "\n" +
                                                "Payable Amount : "+ " " + pending_bal);
                                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, s.length(), 0);
                                nav_headerpending.setText(s);

                                SpannableString credit_string = new SpannableString
                                        ("Credit Limit : "+ " " + list.get(0).getAccLimit());
                                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, s.length(), 0);
//                                nav_headercredit.setText(credit_string);

                            }
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errormessage = jsonObject.getString("message");
                            System.out.println("msg" + errormessage);
                            Log.d("error message", errormessage);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Custom_Toast.warning(MainActivity.this,
                                getResources().getString(R.string.something_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<CreditDetailsResponse> call, Throwable t) {
                System.out.println("msg" + t.getMessage());
                Log.d("error message", t.getMessage());
                Custom_Toast.warning(MainActivity.this, t.getMessage());

            }
        });
    }


    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }


}
