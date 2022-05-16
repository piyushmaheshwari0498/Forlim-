package com.fashion.forlempopoli.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.fashion.forlempopoli.Model.M_Clothes_Order_Details;
import com.fashion.forlempopoli.R;

import java.text.DecimalFormat;
import java.util.List;

public class PlaceCartListAdapter extends RecyclerView.Adapter<PlaceCartListAdapter.MyView> {

    Context context;
    String image_URL = "https://staunchexim.com/img/product_thumb/";
//    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    // Constructor for adapter class
    // which takes a list of String type
    int resourceId;
    private final List<M_Clothes_Order_Details> list;
    double cgstAmount, sgstAmount, igstAmount;
    private final DecimalFormat df;
    TextView textViewTotal;
    TextView tvproduct_amount;
    TextView cart_product_quantity;

    public PlaceCartListAdapter(Context context, List<M_Clothes_Order_Details> horizontalList,
                                int resourceId) {
        this.context = context;
        this.list = horizontalList;
        this.resourceId = resourceId;
        df = new DecimalFormat("#.##");
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(resourceId, parent, false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @SuppressLint("RecyclerView")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyView holder, int position) {

        // Set the text of each item of
        // Recycler view with the list items
        tvproduct_amount.setVisibility(View.VISIBLE);
        holder.textViewTitle.setText(list.get(position).getArt_no().trim());
        textViewTotal.setText(context.getString(R.string.Rs)+String.valueOf(list.get(position).getArt_selling_price_amt()));
        tvproduct_amount.setText(context.getString(R.string.Rs)+String.valueOf(list.get(position).getPer_item_total()));
        cart_product_quantity.setText(String.valueOf(list.get(position).getQty_mteres())+" meters");

        String url = image_URL + list.get(position).getArt_photo();

    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
    }

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder {

        // Text View
        TextView textViewTitle;
        CardView cardView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            textViewTitle = view.findViewById(R.id.product_name);
            textViewTotal = view.findViewById(R.id.tvproduct_price);
            tvproduct_amount = view.findViewById(R.id.tvproduct_amount);
            cart_product_quantity = view.findViewById(R.id.tvqty);

            cardView = view.findViewById(R.id.card_view);


        }

    }
}

