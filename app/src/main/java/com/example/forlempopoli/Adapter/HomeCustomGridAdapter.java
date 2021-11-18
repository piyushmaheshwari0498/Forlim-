package com.example.forlempopoli.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.forlempopoli.Entity.request.Home_Page;
import com.example.forlempopoli.R;

import java.util.List;

public class HomeCustomGridAdapter extends BaseAdapter {
    private List<Home_Page> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private int[] Imageid;

    public HomeCustomGridAdapter(Context aContext, List<Home_Page> listData, int[] Imageid) {
        this.context = aContext;
        this.listData = listData;
        this.Imageid = Imageid;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_home_grid, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView_icon);
            holder.Name = (TextView) convertView.findViewById(R.id.Name);
//            holder.bg_color=convertView.findViewById(R.id.bg_color);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Home_Page home_page = this.listData.get(position);
        holder.Name.setText(home_page.getName());

        holder.imageView.setImageResource(Imageid[position]);

        //holder.bg_color.setBackgroundTintList(ColorStateList.valueOf(home_page.getBg_color()));

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView Name;
        FrameLayout bg_color;
    }

}
