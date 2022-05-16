package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.fashion.forlempopoli.Entity.request.StateDatum;
import com.fashion.forlempopoli.R;

import java.util.List;

public class CustomStateAdapter extends ArrayAdapter<StateDatum> {
    Context context;
    List<StateDatum> stateRequestList;
    LayoutInflater inflter;
    public CustomStateAdapter(Context applicationContext, List<StateDatum> stateRequestList) {
        super(applicationContext,stateRequestList.size());
        this.context = applicationContext;
        this.stateRequestList = stateRequestList;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return stateRequestList.size();
    }

    @Override
    public StateDatum getItem(int position) {
        return stateRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_statespinner_items, null);
        TextView textViewState = (TextView) view.findViewById(R.id.textViewState);
        textViewState.setText(stateRequestList.get(i).getStateName());
        return view;
    }
}
