package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Activity.PdfSamplingDataActivity;
import com.example.forlempopoli.Entity.request.SamplingRequest;
import com.example.forlempopoli.Entity.response.SamplingPdfResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SamplingDataAdapter extends RecyclerView.Adapter<SamplingDataAdapter.ViewHolder>  {
    List<SamplingRequest> samplingRequestList;
    Context context;
    private AppSharedPreference mAppSharedPrefernce;
    String smid;
    String resulturl;
    int pos;
    private static final int STORAGE_PERMISSION_CODE = 12;
     public SamplingDataAdapter(List<SamplingRequest> samplingRequestList, Context context) {
       this.samplingRequestList=samplingRequestList;
       this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sampling_data_adapter, viewGroup, false);
        mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(context);
        return new  SamplingDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvOrder_id.setText(samplingRequestList.get(i).getSsmEntryNo());
        viewHolder.tvOrderdate.setText(samplingRequestList.get(i).getSsmEntryDate());
        viewHolder.tvQty_pieces.setText(samplingRequestList.get(i).getSsmTotalQtyPcs());
        viewHolder.tvTaxable_amt.setText(samplingRequestList.get(i).getSsmTaxableAmt());
        viewHolder.tvCgst.setText(samplingRequestList.get(i).getSsmCgstAmt());
        viewHolder.tvSgst.setText(samplingRequestList.get(i).getSsmSgstAmt());
        viewHolder.tvTotal_price.setText(samplingRequestList.get(i).getSsmFinalAmt());

    }

    @Override
    public int getItemCount() {
        return samplingRequestList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener/*, com.example.forlempopoli.Adapter.ViewHolder*/ {
        TextView tvOrder_id,tvOrderdate,tvQty_pieces, tvTaxable_amt,tvCgst, tvSgst,tvTotal_price, tvInvoice,tvBillDetails;
        RecyclerView ll_data;

        ViewHolder(View itemView) {
            super(itemView);
            tvOrder_id= itemView.findViewById(R.id.tvOrder_id);
            tvOrderdate = itemView.findViewById(R.id.tvOrderdate);
            tvQty_pieces= itemView.findViewById(R.id.tvQty_pieces);
            tvTaxable_amt = itemView.findViewById(R.id.tvTaxable_amt);
            tvSgst = itemView.findViewById(R.id.tvSgst);
            tvCgst= itemView.findViewById(R.id.tvCgst);
            tvTotal_price= itemView.findViewById(R.id.tvTotal_price);
            tvInvoice = itemView.findViewById(R.id.tvInvoice);
            ll_data = itemView.findViewById(R.id.ll_data);
            tvBillDetails = itemView.findViewById(R.id.tvBillDetails);
            tvBillDetails.setPaintFlags(tvBillDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvInvoice.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvInvoice:
                     pos = getAdapterPosition();
                     smid = samplingRequestList.get(pos).getSsmId();
                     pdfDetails();

                    break;
            }
        }

        public void pdfDetails() {
            final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
            Call<SamplingPdfResponse> call = apiInterface.samplingprintPdf(getHashMap());
            call.enqueue(new Callback<SamplingPdfResponse>() {
                @Override
                public void onResponse(Call<SamplingPdfResponse> call, Response<SamplingPdfResponse> response) {
                    if (response.code() == 200 && response.message().equals("OK")) {
                        if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                            // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            resulturl = response.body().getResultUrl();
                            Intent intent = new Intent(context, PdfSamplingDataActivity.class);
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
                                // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                System.out.println("404" + message);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Some Thing Went Wrong");
                            Toast.makeText(context, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SamplingPdfResponse> call, Throwable t) {
                    //ringProgressDialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("onFailure getMessage" + t.getMessage());
                }
            });
        }
        private HashMap<String, Object> getHashMap() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
            map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            map.put("ssm_id", smid);
            return map;
        }
    }
}


