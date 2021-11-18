package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.forlempopoli.Entity.request.DetailsOrderRequest;
import com.example.forlempopoli.R;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    public static List<DetailsOrderRequest> detailsOrderRequestList;
    Context context;
    String image_URL = "http://forlimpopoli.in/beta_2/public/uploads/articles/";
    String image_not_found_URL = "http://forlimpopoli.in/beta_mobile/public/assets/img/";
    String photopath;
    String status;

    public OrderDetailsAdapter(List<DetailsOrderRequest> detailsOrderRequestList, Context context) {
        OrderDetailsAdapter.detailsOrderRequestList = detailsOrderRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_details_adpter_layout, viewGroup, false);
        return new OrderDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailsAdapter.ViewHolder viewHolder, final int i) {
        // viewHolder.fabricName.setText(detailsOrderRequestList.get(i).getFabricName());
        viewHolder.categoryName.setText(detailsOrderRequestList.get(i).getCatName());
        viewHolder.subcategoryName.setText(detailsOrderRequestList.get(i).getScatName());
        viewHolder.width.setText(detailsOrderRequestList.get(i).getAtWidth());
        viewHolder.quantityMeters.setText(detailsOrderRequestList.get(i).getAtTotalQtyMtrs());
        viewHolder.cgst.setText(detailsOrderRequestList.get(i).getAtCgstAmt());
        viewHolder.sgst.setText(detailsOrderRequestList.get(i).getAtSgstAmt());
        viewHolder.igst.setText(detailsOrderRequestList.get(i).getAtIgstAmt());
        viewHolder.articleNo.setText(detailsOrderRequestList.get(i).getArtNo());
        viewHolder.tvStatus.setText(detailsOrderRequestList.get(i).getAtOrderStatus());
        viewHolder.totalAmount.setText("₹" + detailsOrderRequestList.get(i).getAtTotalAmt());
        photopath = detailsOrderRequestList.get(i).getArtPhoto();

//        Picasso.get().load(image_URL+photopath).into(viewHolder.image);

        String not_found_url = image_not_found_URL + photopath;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        if (!image_URL.contains("no-photo.jpg")) {
            Glide.with(context)
                    .load(image_URL)
                    .placeholder(R.drawable.progress_animation)
                    .apply(requestOptions)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(viewHolder.image);
        } else {
            Glide.with(context)
                    .load(not_found_url)
                    .placeholder(R.drawable.progress_animation)
                    .apply(requestOptions)
                    .error(R.drawable.ic_error_outline_white_24dp)
                    .into(viewHolder.image);
        }

        status = detailsOrderRequestList.get(i).getAtOrderStatus();
        if (status.equals("CONFIRM")) {
            viewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            viewHolder.tvStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return detailsOrderRequestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView /*fabricName,*/categoryName, subcategoryName, shadeNo, width, quantityPieces, quantityMeters, cgst,
                sgst, igst, totalAmount, articleNo, tvStatus, tvPriceDetails;
        Button btnContinue;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            //fabricName = itemView.findViewById(R.id.fabricName);
            categoryName = itemView.findViewById(R.id.categoryName);
            subcategoryName = itemView.findViewById(R.id.subcategoryName);
            width = itemView.findViewById(R.id.width);
            quantityMeters = itemView.findViewById(R.id.quantityMeters);
            cgst = itemView.findViewById(R.id.cgst);
            sgst = itemView.findViewById(R.id.sgst);
            igst = itemView.findViewById(R.id.igst);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            image = itemView.findViewById(R.id.image);
            articleNo = itemView.findViewById(R.id.articleNo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPriceDetails = itemView.findViewById(R.id.tvPriceDetails);
            tvPriceDetails.setPaintFlags(tvPriceDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}