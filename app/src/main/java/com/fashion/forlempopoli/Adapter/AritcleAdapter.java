package com.fashion.forlempopoli.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fashion.forlempopoli.Entity.request.ProductRequest;
import com.fashion.forlempopoli.R;

import java.util.ArrayList;
import java.util.List;

public class AritcleAdapter extends ArrayAdapter<ProductRequest> implements Filterable {
    private final Context mContext;
    List<ProductRequest> mProductRequests;
    List<ProductRequest> mCityRequestListAll;
    List<ProductRequest> mProductRequestsuggestion;
    private final int mLayoutResourceId;

    public AritcleAdapter(Context context, int resource, List<ProductRequest> cityRequests) {
        super(context, 0, cityRequests);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mProductRequests = cityRequests;
        this.mCityRequestListAll = new ArrayList<>(cityRequests);
        this.mProductRequestsuggestion = new ArrayList<>();
    }

    public int getCount() {
        return mProductRequests.size();
    }

    public ProductRequest getItem(int position) {
        return mProductRequests.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            ProductRequest cityRequest = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.text_spinner);
            if(cityRequest != null) {
                name.setText(cityRequest.getArtNo().replaceAll("\\s+", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return cityFilter;
    }

    private Filter cityFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ProductRequest cityRequest = (ProductRequest) resultValue;
//            Log.d("getArtNo",cityRequest.getArtId());
//            Log.d("getArtNo",cityRequest.getArtNo());
//            Log.d("getArtNo",cityRequest.getArtNo().replaceAll("\\s+", ""));
            return cityRequest.getArtNo().replaceAll("\\s+", "");
//            return cityRequest.getArtNo();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                mProductRequestsuggestion.clear();
                for (ProductRequest cityRequest : mCityRequestListAll) {
                    if (cityRequest.getArtNo().replaceAll("\\s+", "").toLowerCase().
                            contains(charSequence.toString().replaceAll("\\s+", "-").toLowerCase())) {
//                        cityRequest.setArtNo(cityRequest.getArtNo().replaceAll("-"," "));
//                        cityRequest.setArtId(cityRequest.getArtId());
                        mProductRequestsuggestion.add(cityRequest);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mProductRequestsuggestion;
                filterResults.count = mProductRequestsuggestion.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ProductRequest> tempValues = (ArrayList<ProductRequest>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ProductRequest cityObj : tempValues) {
                    add(cityObj);
                }
                notifyDataSetChanged();
            } else {
                clear();
                for (ProductRequest cityObj : mCityRequestListAll) {
                    add(cityObj);
                }
                notifyDataSetChanged();
            }
        }
    };
}
