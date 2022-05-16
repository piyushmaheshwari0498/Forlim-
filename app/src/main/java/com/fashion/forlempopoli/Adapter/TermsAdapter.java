package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fashion.forlempopoli.Activity.PDFActivity;
import com.fashion.forlempopoli.Entity.request.InvoiceRequest;
import com.fashion.forlempopoli.Entity.request.OrderItemRequest;
import com.fashion.forlempopoli.Entity.response.PdfResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.Model.Terms;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.fashion.forlempopoli.terms.TermsDetailsActivity;

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

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {
    List<Terms> invoiceRequestList;
    Context context;
    private View mView;

    public TermsAdapter(List<Terms> invoiceRequestList, Context context) {
        this.invoiceRequestList = invoiceRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.term_adapter_layout, viewGroup, false);
        View insertedView = LayoutInflater.from(context).inflate(R.layout.order_invoice_item_layout, viewGroup, false);
        mView = insertedView;

        return new TermsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_term_name.setText(invoiceRequestList.get(i).getTerm_name());
    }

    @Override
    public int getItemCount() {
        return invoiceRequestList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_term_name;
        CardView card_view;

        ViewHolder(View itemView) {
            super(itemView);
            tv_term_name = itemView.findViewById(R.id.tv_term_name);
            card_view = itemView.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.card_view:
                    Intent intent = new Intent(context, TermsDetailsActivity.class);
                    intent.putExtra("terms", invoiceRequestList.get(getAdapterPosition()).getTerm_name());
                    intent.putExtra("header", invoiceRequestList.get(getAdapterPosition()).getTerm_header());
                    intent.putExtra("content", invoiceRequestList.get(getAdapterPosition()).getTerm_content());
                    context.startActivity(intent);
                    break;
            }
        }

    }
}


