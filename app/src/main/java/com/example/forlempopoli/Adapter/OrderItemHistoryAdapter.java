package com.example.forlempopoli.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Entity.request.OrderItemRequest;
import com.example.forlempopoli.R;
import java.util.List;

public class OrderItemHistoryAdapter extends RecyclerView.Adapter<OrderItemHistoryAdapter.ViewHolder>  {
    List<OrderItemRequest> orderItemRequestList;
    Context context;
    private View mView;
    public OrderItemHistoryAdapter(List<OrderItemRequest> orderItemRequestList, Context context) {
       this.orderItemRequestList=orderItemRequestList;
       this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_invoice_item_layout, viewGroup, false);
        View insertedView = LayoutInflater.from(context).inflate(R.layout.order_invoice_item_layout, viewGroup,false);
        mView=insertedView;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_name.setText(orderItemRequestList.get(i).getFabricName());
        viewHolder.tv_quantity.setText(orderItemRequestList.get(i).getStQtyMtrs());
        viewHolder.tv_price.setText(orderItemRequestList.get(i).getStSellingPriceAmt());

    }

    @Override
    public int getItemCount() {
        return orderItemRequestList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_name,tv_quantity,tv_price;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);

        }
    }
}


