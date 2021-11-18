package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.forlempopoli.Entity.request.CountryDatum;
import com.example.forlempopoli.R;

import java.util.List;

public class CustomCountryAdapter extends ArrayAdapter<CountryDatum> {
    Context context;
    List<CountryDatum> countryRequestList;
    LayoutInflater inflter;
    public CustomCountryAdapter(Context applicationContext, List<CountryDatum> countryRequestList) {
        super(applicationContext,countryRequestList.size());
        this.context = applicationContext;
        this.countryRequestList = countryRequestList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return countryRequestList.size();
    }

    @Override
    public CountryDatum getItem(int position) {
        return countryRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_countryspinner_items, null);
        TextView textViewCountry = (TextView) view.findViewById(R.id.textViewCountry);
        textViewCountry.setText(countryRequestList.get(i).getCountryName());
        return view;
    }
}
