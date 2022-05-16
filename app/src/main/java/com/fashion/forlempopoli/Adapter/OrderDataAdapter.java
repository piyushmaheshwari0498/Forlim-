package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fashion.forlempopoli.Activity.OrderDetailsActivity;
import com.fashion.forlempopoli.Entity.request.OrderDataRequest;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Utilities.Constants;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDataAdapter extends RecyclerView.Adapter<OrderDataAdapter.ViewHolder> implements Filterable {
    //public static List<OrderDataRequest> orderDataRequestList;
    Context context;
    String date1;
    Date date;

    List<OrderDataRequest> orderListFiltered;
    List<OrderDataRequest> originalList;
    private DecimalFormat df;

    public OrderDataAdapter(List<OrderDataRequest> orderDataRequestList, Context context/*,String smId*/) {
      //  this.orderDataRequestList = orderDataRequestList;
        this.context = context;
        df = new DecimalFormat("#");
        this.orderListFiltered = orderDataRequestList;
        this.originalList = orderDataRequestList;

    }

    @NonNull
    @Override
    public OrderDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orderdata_adpter_layout, viewGroup, false);
        return new OrderDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDataAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tvOrderNo.setText(orderListFiltered.get(i).getAmBillNo());
        date1=orderListFiltered.get(i).getAmBillDate();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = inputFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String outputString = outputFormat.format(date);
        double itemCount = Double.parseDouble(orderListFiltered.get(i).getAmTotalQtyPcs());
        viewHolder.tvorderDate.setText(outputString);
        viewHolder.tvQuantityMeters.setText("Total " +orderListFiltered.get(i).getAmTotalQtyMtrs());
        viewHolder.tv_items.setText("("+df.format(itemCount)+" Items)");
        viewHolder.tvTotalAmount.setText("₹" + orderListFiltered.get(i).getAmFinalAmt());

    }

    @Override
    public int getItemCount() {
        return orderListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
                System.out.println("charString To Filter " + charString);
                List<OrderDataRequest> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = originalList;
                    System.out.println("row empty size" + filteredList.size());
                } else {

                    for (OrderDataRequest row : originalList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name  match
                        System.out.println("charString adapter " + charString);
                        if (row.getAmBillNo().toString().toLowerCase().contains(charString.toString().toLowerCase()))
                        {
                            System.out.println("row added");
                            filteredList.add(row);

                        }

                    }
                    //categoryListFiltered = filteredList;
                    System.out.println("row added size" + filteredList.size());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderListFiltered = (List<OrderDataRequest>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tvOrderNo, tvorderDate,tvQuantityMeters, tvTotalAmount,view_Btn,tv_items;
        ViewHolder(View itemView) {
            super(itemView);
            tvOrderNo = itemView.findViewById(R.id.tvOrderNo);
            tvorderDate = itemView.findViewById(R.id.tvorderDate);
            tvQuantityMeters = itemView.findViewById(R.id.tvQuantityMeters);
            tv_items = itemView.findViewById(R.id.tv_items);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            view_Btn = itemView.findViewById(R.id.view_Btn);
            view_Btn.setPaintFlags(view_Btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            view_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, OrderDetailsActivity.class);
                    System.out.println("orderdata amid"+orderListFiltered.get(getAdapterPosition()).getAmId());

                    intent.putExtra(Constants.INTENT_KEYS.KEY_AM_ID,orderListFiltered.get(getAdapterPosition()).getAmId());
                    intent.putExtra("order_id",orderListFiltered.get(getAdapterPosition()).getAmBillNo());
                    intent.putExtra("order_date",tvorderDate.getText().toString().trim());
                    intent.putExtra("order_amount",tvTotalAmount.getText().toString().trim());
                    intent.putExtra("order_items",tv_items.getText().toString().trim());

                    context.startActivity(intent);
                }
            });
        }
    }
}