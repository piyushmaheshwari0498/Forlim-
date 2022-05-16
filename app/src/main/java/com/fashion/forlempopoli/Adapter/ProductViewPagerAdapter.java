package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fashion.forlempopoli.R;

import java.util.ArrayList;

public class ProductViewPagerAdapter extends PagerAdapter {
    ArrayList<String> url;
    private Context context;
    private LayoutInflater layoutInflater;
//    private int images;
    private String images;

    public ProductViewPagerAdapter(Context context, ArrayList<String> url) {
        this.context = context;
//        this.url = new ArrayList<>();
        this.url = url;
    }

    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_product_viewpager, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Log.d("url garmentPath", url.get(position));
        Glide.with(context)
                .load(url.get(position))
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_image_found)
                .into(imageView);
//        ViewPager vp = (ViewPager) container;
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//
//        ViewPager vp = (ViewPager) container;
//        View view = (View) object;
        container.removeView((View) object);

    }

}
