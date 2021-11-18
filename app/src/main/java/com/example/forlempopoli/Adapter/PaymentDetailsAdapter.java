package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.forlempopoli.Entity.request.PaymentDetailsRequest;
import com.example.forlempopoli.R;
import java.util.List;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.ViewHolder> {
    List<PaymentDetailsRequest> paymentDetailsRequestList;
    Context context;

     public PaymentDetailsAdapter(List<PaymentDetailsRequest> paymentDetailsRequestList, Context context) {
       this.paymentDetailsRequestList=paymentDetailsRequestList;
       this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_details_adapter, viewGroup, false);
        return new  PaymentDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.order_id.setText(paymentDetailsRequestList.get(i).getSmBillNo());
        viewHolder.order_date.setText(paymentDetailsRequestList.get(i).getSmBillDate());
        viewHolder.total_price.setText(paymentDetailsRequestList.get(i).getSmFinalAmt());
        viewHolder.taxable_amt.setText(paymentDetailsRequestList.get(i).getSmTaxableAmt());
        viewHolder.cgst.setText(paymentDetailsRequestList.get(i).getSmCgstAmt());
        viewHolder.sgst.setText(paymentDetailsRequestList.get(i).getSmSgstAmt());
        viewHolder.igst.setText(paymentDetailsRequestList.get(i).getSmIgstAmt());

    }

    @Override
    public int getItemCount() {
        return paymentDetailsRequestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id, order_date, taxable_amt, cgst, sgst,igst,total_price,tvBillDetails;

        ViewHolder(View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            order_date = itemView.findViewById(R.id.order_date);
            taxable_amt = itemView.findViewById(R.id.taxable_amt);
            cgst = itemView.findViewById(R.id.cgst);
            sgst = itemView.findViewById(R.id.sgst);
            igst = itemView.findViewById(R.id.igst);
            total_price = itemView.findViewById(R.id.total_price);
            tvBillDetails = itemView.findViewById(R.id.tvBillDetails);
            tvBillDetails.setPaintFlags(tvBillDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}


