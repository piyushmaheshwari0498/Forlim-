package com.example.forlempopoli.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.forlempopoli.Entity.request.CustomerTypeRequest;
import com.example.forlempopoli.Model.CustomerType;
import com.example.forlempopoli.R;
import java.util.List;

public class CustomerTypeAdapter extends BaseAdapter {
    Context context;
    List<CustomerTypeRequest> customerTypeList;
    LayoutInflater inflter;
    public CustomerTypeAdapter(Context applicationContext, List<CustomerTypeRequest> customerTypeList) {
        this.context = applicationContext;
        this.customerTypeList = customerTypeList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return customerTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_customerspinner_items, null);
        TextView textViewCity = (TextView) view.findViewById(R.id.textViewCustomer);
        textViewCity.setText(customerTypeList.get(i).getAccType());
        return view;
    }
}
