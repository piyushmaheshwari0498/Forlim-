package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Entity.request.OrderItemRequest;
import com.example.forlempopoli.Entity.request.SaleReturnItemRequest;
import com.example.forlempopoli.Entity.request.SaleReturnRequest;
import com.example.forlempopoli.R;

import java.util.List;

public class SaleReturnItemHistoryAdapter extends RecyclerView.Adapter<SaleReturnItemHistoryAdapter.ViewHolder>  {
    List<SaleReturnItemRequest> saleReturnItemRequestList;
    Context context;
    private View mView;
    public SaleReturnItemHistoryAdapter(List<SaleReturnItemRequest> saleReturnItemRequestList, Context context) {
       this.saleReturnItemRequestList=saleReturnItemRequestList;
       this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sale_return_item_layout, viewGroup, false);
        View insertedView = LayoutInflater.from(context).inflate(R.layout.sale_return_item_layout, viewGroup,false);
        mView=insertedView;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(saleReturnItemRequestList.get(i).getFabricName());
        viewHolder.quantity.setText(saleReturnItemRequestList.get(i).getSrtQtyMtrs());
        viewHolder.price.setText(saleReturnItemRequestList.get(i).getSrtSellingPriceAmt());

    }

    @Override
    public int getItemCount() {
        return saleReturnItemRequestList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView name,quantity,price;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);

        }
    }
}


