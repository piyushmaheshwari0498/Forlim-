package com.fashion.forlempopoli.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fashion.forlempopoli.Adapter.CategoryAdapter;
import com.fashion.forlempopoli.Entity.request.CategoryRequest;
import com.fashion.forlempopoli.Entity.response.CategoryResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.AutoFitGridLayoutManager;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.HideKeyboard;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    List<CategoryRequest> categoryRequestList;
    RecyclerView category_rV;
    CategoryAdapter categoryAdapter;
    ProgressDialog ringProgressDialog;
    AppSharedPreference mappSharedPreference;

    HideKeyboard hideKeyboard;
    AppUtils appUtils;
    View view;
    String credit_days;
    String credit_limit;
    String credit_amount;
    TextView tvCreditLimit;
    View rootView;
    LinearLayout ll_limit;
    LinearLayout ll_Data_Found;
    LinearLayout ll_NoData_Found;
    ImageView img_info;
    ImageView img_more;
    TextView no_data_text;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialButton retry_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        category_rV = view.findViewById(R.id.category_rV);

        rootView = view.findViewById(R.id.root_layout);
        tvCreditLimit = view.findViewById(R.id.tvCreditLimit);
        ll_limit = view.findViewById(R.id.ll_limit);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        no_data_text = view.findViewById(R.id.no_data_text);
        ll_Data_Found = view.findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = view.findViewById(R.id.ll_NoData_Found);
        img_info = view.findViewById(R.id.img_info);
        img_more = view.findViewById(R.id.img_more);
        retry_btn = view.findViewById(R.id.retry_btn);

        appUtils = new AppUtils();
        //tvName=view.findViewById(R.id.tvName);
        mappSharedPreference = AppSharedPreference.getAppSharedPreference(getActivity());
        /*String name =mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_PERSON_NAME);
        tvName.setText("Welcome "+ name);*/
        ringProgressDialog = new ProgressDialog(getActivity());
        ringProgressDialog.setMessage("Loading...");
        ringProgressDialog.setCanceledOnTouchOutside(false);
        ringProgressDialog.show();

        hideKeyboard = new HideKeyboard();

        hideKeyboard.hideKeyboardFrom(getContext(), view);


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

        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.setContentView(R.layout.payment_info_dialog);

                TextView textcredit = (TextView) dialog.findViewById(R.id.tv_credit);
                TextView textpending = (TextView) dialog.findViewById(R.id.tv_pending);
                TextView textpayable = (TextView) dialog.findViewById(R.id.tv_payable);
                TextView textpaid = (TextView) dialog.findViewById(R.id.tv_paid);

                textcredit.setText("₹"+mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_CREDIT_LIMIT_AMOUNT));
                textpending.setText("₹"+mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_PENDING_BAL));
                textpayable.setText("₹"+mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_TOTAL_BALANCE));
                textpaid.setText("₹"+mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_VOUCHER_AMT));

                ImageView dialogButton = (ImageView) dialog.findViewById(R.id.img_close);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


       /* LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        category_rV.setLayoutManager(layoutManager);*/


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        view.clearFocus();
        refreshItems();
    }


    private void getFilterData(String query) {

        if (categoryAdapter != null) {
            categoryAdapter.getFilter().filter(query);

            if (categoryAdapter.getItemCount() != 0) {
                Log.d("filter count", String.valueOf(categoryAdapter.getItemCount()));
                ll_Data_Found.setVisibility(View.VISIBLE);
                ll_NoData_Found.setVisibility(View.GONE);
                AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(getContext(), 500);
                category_rV.setLayoutManager(autolayoutManager);

            } else {
                ll_Data_Found.setVisibility(View.GONE);
                ll_NoData_Found.setVisibility(View.VISIBLE);
                retry_btn.setVisibility(View.GONE);
                img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                no_data_text.setText(getResources().getString(R.string.no_record_found));
            }
        }
    }

    void refreshItems() {
        // Load items
        // ...
        fetchCredit();
        if (!appUtils.isConnected(getContext())) {
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
            showcategoryDetails();
        }
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }

    private void showcategoryDetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<CategoryResponse> call = apiInterface.getCategoryDetails(getMap());
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        ringProgressDialog.dismiss();
                        categoryRequestList = response.body().getData();

//                        category_rV.setLayoutManager(new LinearLayoutManager(getActivity()));
                        if (!categoryRequestList.isEmpty()) {
                            // set a GridLayoutManager with default vertical orientation and 3 number of columns
//                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);

                            categoryAdapter = new CategoryAdapter(categoryRequestList, getActivity());
                            category_rV.setAdapter(categoryAdapter);
//                            category_rV.setLayoutManager(gridLayoutManager);
                            /*AutoFitGridLayoutManager autolayoutManager = new AutoFitGridLayoutManager(getActivity(), 500) {
                                @Override
                                public boolean canScrollVertically() {
                                    return true;
                                }
                            };

                            category_rV.setLayoutManager(autolayoutManager);*/
                        } else {
                            ll_Data_Found.setVisibility(View.GONE);
                            ll_NoData_Found.setVisibility(View.VISIBLE);
                            retry_btn.setVisibility(View.GONE);
                            img_info.setImageDrawable(getResources().getDrawable(R.drawable.not_data));
                            no_data_text.setText(getResources().getString(R.string.data_not_found));
                        }
                        fetchCredit();
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        ringProgressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Log.d("response else", message);
                            if (!appUtils.isConnected(getContext())) {
                                no_internet();
                            } else {
                                Log.e("in", "else");
                                something_wrong();
                            }
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!appUtils.isConnected(getContext())) {
                            no_internet();
                        } else {
                            Log.e("in", "else");
                            something_wrong();
                        }
                        //  Toast.makeText(getActivity( ), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                if (!appUtils.isConnected(getContext())) {
                    no_internet();
                } else {
                    Log.e("in", "else");
                    something_wrong();
                }
                System.out.println("onFailure" + t.getMessage());
            }
        });
    }

    public void fetchCredit(){
        credit_days = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_CREDIT_DAYS);
        credit_limit = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_PENDING_BAL);
        credit_amount = mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_CREDIT_LIMIT_AMOUNT);
        Log.e("limit home ", String.valueOf(credit_limit));


        if (!credit_limit.isEmpty()) {
            float pending_bal = Math.abs(Float.parseFloat(credit_limit));
            float credit_amt = Math.abs(Float.parseFloat(credit_amount));
            if (mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACC_CR_LIMIT).equals("LIMIT")) {
                Log.e("pending_bal", String.valueOf(pending_bal));
                Log.e("credit_amt", String.valueOf(credit_amt));
                if (pending_bal <= 0  | pending_bal  >= 0) {
                    ll_limit.setVisibility(View.VISIBLE);
                    SpannableString s = new SpannableString
                            (getString(R.string.balance_credit_days) + " " + credit_days + "\n" +
                                    "Pending Amount"+ " " + pending_bal);
                    s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
                    tvCreditLimit.setText(s);
                }
            } else {
                ll_limit.setVisibility(View.GONE);
            }

        }
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

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mappSharedPreference.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }
}
