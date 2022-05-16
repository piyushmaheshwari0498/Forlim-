package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fashion.forlempopoli.Entity.request.CityRequest;
import com.fashion.forlempopoli.R;

import java.util.List;

public class CityAdapter extends ArrayAdapter<CityRequest> {
    Context context;
    List<CityRequest> cityRequestList;
    LayoutInflater inflter;
    public CityAdapter(Context applicationContext, List<CityRequest> cityRequestList) {
        super(applicationContext, cityRequestList.size());
        this.context = applicationContext;
        this.cityRequestList = cityRequestList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return cityRequestList.size();
    }

    @Override
    public CityRequest getItem(int position) {
        return cityRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_cityspinner_items, null);
        TextView textViewCity = (TextView) view.findViewById(R.id.textViewCity);
        textViewCity.setText(cityRequestList.get(i).getCityName());
        return view;
    }
}
