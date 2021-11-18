package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.forlempopoli.Activity.SubCategoryActivity;
import com.example.forlempopoli.Entity.request.CategoryRequest;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {
    //  public static List<CategoryRequest> categoryRequestList;
    Context context;
    String image_URL = "http://forlimpopoli.in/beta_mobile/public/uploads/category/";
    String image_not_found_URL = "http://forlimpopoli.in/beta_mobile/public/assets/img/";
    String photopath;
    List<CategoryRequest> categoryListFiltered;
    List<CategoryRequest> originalList;

    public CategoryAdapter(List<CategoryRequest> categoryRequestList, Context context) {
        //  this.categoryRequestList = categoryRequestList;
        this.context = context;
        this.categoryListFiltered = categoryRequestList;
        this.originalList = categoryRequestList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_adpter_layout, viewGroup, false);
        /*int height = viewGroup.getMeasuredHeight() / 4;
        int width = viewGroup.getMeasuredWidth();

        v.setLayoutParams(new RecyclerView.LayoutParams(width,height));*/
        return new CategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.categoryName.setText(categoryListFiltered.get(i).getCatName());
        photopath = categoryListFiltered.get(i).getCatPhoto();


        String url = image_URL + photopath;
        String not_found_url = image_not_found_URL + photopath;
        if (!url.contains("no-photo.jpg")) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(viewHolder.imageView_icon);
        } else {
            Glide.with(context)
                    .load(not_found_url)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(viewHolder.imageView_icon);
        }
//        System.out.println("image_URL category" + image_URL + photopath);
    }

    @Override
    public int getItemCount() {
        return categoryListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
//                System.out.println("charString To Filter " + charString);
                List<CategoryRequest> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = originalList;
                    System.out.println("row empty size" + filteredList.size());
                } else {

                    for (CategoryRequest row : originalList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name  match
//                        System.out.println("charString adapter " + charString);
                        if (row.getCatName().toLowerCase().contains(charString.toLowerCase())) {
//                            System.out.println("row added");
                            filteredList.add(row);

                        }

                    }
                    //categoryListFiltered = filteredList;
//                    System.out.println("row added size" + filteredList.size());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryListFiltered = (List<CategoryRequest>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView_icon;
        TextView categoryName;
        CardView rr_category;

        ViewHolder(View itemView) {
            super(itemView);
            imageView_icon = itemView.findViewById(R.id.imageView_icon);
            categoryName = itemView.findViewById(R.id.categoryName);
            rr_category = itemView.findViewById(R.id.rr_category);
            rr_category.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rr_category:
                    Intent intent = new Intent(context, SubCategoryActivity.class);
                    intent.putExtra(Constants.INTENT_KEYS.KEY_CATEGORY_ID, categoryListFiltered.get(getAdapterPosition()).getCatId());
                    intent.putExtra(Constants.INTENT_KEYS.KEY_CATEGORY_NAME, categoryListFiltered.get(getAdapterPosition()).getCatName());
                    context.startActivity(intent);
            }
        }
    }


}