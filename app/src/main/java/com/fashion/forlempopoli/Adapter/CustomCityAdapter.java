package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.fashion.forlempopoli.Entity.request.CityDatum;
import com.fashion.forlempopoli.R;

import java.util.List;

public class CustomCityAdapter extends ArrayAdapter<CityDatum> {
    Context context;
    List<CityDatum> cityRequestList;
    LayoutInflater inflter;
    public CustomCityAdapter(Context applicationContext, List<CityDatum> cityRequestList) {
        super(applicationContext,cityRequestList.size());
        this.context = applicationContext;
        this.cityRequestList = cityRequestList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return cityRequestList.size();
    }

    @Override
    public CityDatum getItem(int position) {
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
        Log.e("cityId",cityRequestList.get(i).getCityId());
        textViewCity.setText(cityRequestList.get(i).getCityName());
        return view;
    }
}
