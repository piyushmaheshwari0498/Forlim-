package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fashion.forlempopoli.Activity.PDFActivity;
import com.fashion.forlempopoli.Entity.request.InvoiceRequest;
import com.fashion.forlempopoli.Entity.request.OrderItemRequest;
import com.fashion.forlempopoli.Entity.response.PdfResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    List<InvoiceRequest> invoiceRequestList;
    Context context;
    String smid;
    String smId;
    String resulturl, acc_id;
    int pos;
    Date date;
    SimpleDateFormat formatter;
    List<OrderItemRequest> orderRequestList;
    String orderno, orderdate, finalamt, subamt;
    private AppSharedPreference mAppSharedPrefernce;
    private View mView;

    public InvoiceAdapter(List<InvoiceRequest> invoiceRequestList, Context context) {
        this.invoiceRequestList = invoiceRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_invoice_adapter, viewGroup, false);
        View insertedView = LayoutInflater.from(context).inflate(R.layout.order_invoice_item_layout, viewGroup, false);
        mView = insertedView;
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(context);
        acc_id = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);
        return new InvoiceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_order_id.setText(invoiceRequestList.get(i).getSmBillNo());
        viewHolder.tv_order_date.setText(invoiceRequestList.get(i).getSmBillDate());
        viewHolder.tv_total_price.setText(invoiceRequestList.get(i).getSmFinalAmt());
        viewHolder.tv_taxable_amt.setText(invoiceRequestList.get(i).getSmTaxableAmt());
        viewHolder.tv_cgst.setText(invoiceRequestList.get(i).getSmCgstAmt());
        viewHolder.tv_sgst.setText(invoiceRequestList.get(i).getSmSgstAmt());

        orderno = invoiceRequestList.get(i).getSmBillNo();
        orderdate = invoiceRequestList.get(i).getSmBillDate();
        subamt = invoiceRequestList.get(i).getSmSubAmt();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());
        orderRequestList = invoiceRequestList.get(i).getTrans();
        viewHolder.ll_data.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.ll_data.setAdapter(new OrderItemHistoryAdapter(orderRequestList, context));
        viewHolder.ll_data.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public int getItemCount() {
        return invoiceRequestList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_order_id, tv_order_date, tv_total_price, tv_taxable_amt, tv_cgst,
                tv_sgst, tvInvoice, tvPayNow, tvBillDetails;
        RecyclerView ll_data;

        ViewHolder(View itemView) {
            super(itemView);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            tv_taxable_amt = itemView.findViewById(R.id.tv_taxable_amt);
            tvInvoice = itemView.findViewById(R.id.tvInvoice);
            tv_sgst = itemView.findViewById(R.id.tv_sgst);
            tv_cgst = itemView.findViewById(R.id.tv_cgst);
            tvPayNow = itemView.findViewById(R.id.tvPayNow);
            ll_data = itemView.findViewById(R.id.ll_data);
            tvBillDetails = itemView.findViewById(R.id.tvBillDetails);
            tvBillDetails.setPaintFlags(tvBillDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvInvoice.setOnClickListener(this);
            tvPayNow.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvInvoice:
                    pos = getAdapterPosition();
                    smid = invoiceRequestList.get(pos).getSmId();
                    pdfDetails();
                    break;
                case R.id.tvPayNow:
                    pos = getAdapterPosition();
                    smId = invoiceRequestList.get(pos).getSmId();
                    // System.out.println("smId tvPayNow"+smId);

                    finalamt = invoiceRequestList.get(pos).getSmFinalAmt();
                    // System.out.println("tvPayNow finalamt"+finalamt);

                   /* Intent intent = new Intent("data");
                    intent.putExtra("acc_id", acc_id);
                    intent.putExtra("sm_id", smId);
                    intent.putExtra("sm_final_amt", finalamt);
                    intent.putExtra("date", formatter.format(date));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    navigateToPayementActivity();*/
                    break;
            }
        }

        public void pdfDetails() {
            final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
            Call<PdfResponse> call = apiInterface.printPdf(getHashMap());
            call.enqueue(new Callback<PdfResponse>() {
                @Override
                public void onResponse(Call<PdfResponse> call, Response<PdfResponse> response) {
                    if (response.code() == 200 && response.message().equals("OK")) {
                        if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                            // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            resulturl = response.body().getResultUrl();
                            Intent intent = new Intent(context, PDFActivity.class);
                            intent.putExtra("position", pos);
                            intent.putExtra(Constants.INTENT_KEYS.KEY_SM_ID, smid);
                            intent.putExtra("request_pdf", resulturl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            System.out.println("url" + resulturl);

                        }
                    } else {
                        if (response.code() == 404 && response.message().equals("Not Found")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                System.out.println("404" + message);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Something Went Wrong");
                            Toast.makeText(context, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PdfResponse> call, Throwable t) {
                    //ringProgressDialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("onFailure getMessage" + t.getMessage());
                }
            });
        }

        private HashMap<String, Object> getHashMap() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
            map.put("acc_id", acc_id);
            // System.out.println("acc_id"+mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            map.put("sm_id", smid);
            //  System.out.println("sm_id"+sm_id);
            return map;
        }
    }
}


