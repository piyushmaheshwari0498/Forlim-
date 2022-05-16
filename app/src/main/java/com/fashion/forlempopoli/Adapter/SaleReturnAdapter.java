package com.fashion.forlempopoli.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fashion.forlempopoli.Activity.SaleReturnPdfActivity;
import com.fashion.forlempopoli.Entity.request.SaleReturnItemRequest;
import com.fashion.forlempopoli.Entity.request.SaleReturnPdfResponse;
import com.fashion.forlempopoli.Entity.request.SaleReturnRequest;
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

public class SaleReturnAdapter extends RecyclerView.Adapter<SaleReturnAdapter.ViewHolder> {
    List<SaleReturnRequest> saleReturnRequestList;
    Context context;
    private AppSharedPreference mAppSharedPrefernce;
    private View mView;
    String acc_id;
    Date date;
    SimpleDateFormat formatter;
    List<SaleReturnItemRequest> saleReturnItemRequestList;
    int pos;
    String resulturl;
    String srmid;

     public SaleReturnAdapter(List<SaleReturnRequest> saleReturnRequestList, Context context) {
       this.saleReturnRequestList=saleReturnRequestList;
       this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sale_return_adapter, viewGroup, false);
        View insertedView = LayoutInflater.from(context).inflate(R.layout.sale_return_item_layout, viewGroup,false);
        mView=insertedView;
        mAppSharedPrefernce= AppSharedPreference.getAppSharedPreference(context);
        acc_id=mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);

        return new  SaleReturnAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_order_id.setText(saleReturnRequestList.get(i).getSmBillNo());
        viewHolder.tv_order_date.setText(saleReturnRequestList.get(i).getSmBillDate());

        formatter= new SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());

        saleReturnItemRequestList=saleReturnRequestList.get(i).getTrans();
        viewHolder.item_sale_rV.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.item_sale_rV.setAdapter(new SaleReturnItemHistoryAdapter(saleReturnItemRequestList,context));
    }

    @Override
    public int getItemCount() {
        return saleReturnRequestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_order_id, tv_order_date, tv_total_price,tv_taxable_amt,tv_cgst,tv_sgst,tvInvoice;
        RecyclerView item_sale_rV;

        ViewHolder(View itemView) {
            super(itemView);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            tv_taxable_amt = itemView.findViewById(R.id.tv_taxable_amt);
            tvInvoice = itemView.findViewById(R.id.tvInvoice);
            tv_sgst = itemView.findViewById(R.id.tv_sgst);
            tv_cgst = itemView.findViewById(R.id.tv_cgst);
            item_sale_rV = itemView.findViewById(R.id.item_sale_rV);
            tvInvoice.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvInvoice:
                    pos = getAdapterPosition();
                    srmid = saleReturnRequestList.get(pos).getSrmId();
                    pdfDetails();
                    break;
            }
        }
        public void pdfDetails() {
            final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
            Call<SaleReturnPdfResponse> call = apiInterface.saleReturnPdf(getHashMap());
            call.enqueue(new Callback<SaleReturnPdfResponse>() {
                @Override
                public void onResponse(Call<SaleReturnPdfResponse> call, Response<SaleReturnPdfResponse> response) {
                    if (response.code() == 200 && response.message().equals("OK")) {
                        if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                            resulturl = response.body().getResultUrl();
                            Intent intent = new Intent(context, SaleReturnPdfActivity.class);
                            intent.putExtra(Constants.INTENT_KEYS.KEY_SM_ID,srmid);
                            intent.putExtra("position", pos);
                            intent.putExtra("request_pdf",resulturl);
                            System.out.println("adapter sale resulturl"+resulturl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                    else {
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
                public void onFailure(Call<SaleReturnPdfResponse> call, Throwable t) {
                    //ringProgressDialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("onFailure getMessage" + t.getMessage());
                }
            });
        }
        private HashMap<String, Object> getHashMap() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
            map.put("acc_id",acc_id);
            // System.out.println("acc_id"+mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            map.put("srm_id",srmid);
            //  System.out.println("sm_id"+sm_id);
            return map;
        }

    }
}


