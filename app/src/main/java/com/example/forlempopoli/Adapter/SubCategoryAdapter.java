package com.example.forlempopoli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.forlempopoli.Activity.ProductActivity;
import com.example.forlempopoli.Entity.request.SubCategoryRequest;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> implements Filterable {
    List<SubCategoryRequest> subCategoryRequestList;
    Context context;
    String image_URL = "http://forlimpopoli.in/beta_mobile/public/uploads/category/";
    String image_not_found_URL = "http://forlimpopoli.in/beta_mobile/public/assets/img/";
    String photopath;
    String categoryID;
    // private SubCategoryAdapter.HomeCallBack homeCallBack;
    List<SubCategoryRequest> subCategoryListFiltered;
    List<SubCategoryRequest> originalList;

    public SubCategoryAdapter(List<SubCategoryRequest> subCategoryRequestList,
                              Context context, String categoryID) {
        this.subCategoryRequestList = subCategoryRequestList;
        this.context = context;
        this.categoryID = categoryID;
        this.subCategoryListFiltered = subCategoryRequestList;
        this.originalList = subCategoryRequestList;
        // this.homeCallBack = mCallBackus;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_category_adpter_layout, viewGroup, false);
        return new SubCategoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryAdapter.ViewHolder viewHolder, final int i) {
        //    homeCallBack.updateCartCount(context);
        viewHolder.subcategoryName.setText(subCategoryListFiltered.get(i).getScatName());
        viewHolder.subcategoryCount.setText("(" + subCategoryListFiltered.get(i).getArt_count() + ")");
        photopath = subCategoryListFiltered.get(i).getScatPhoto();

        String url = image_URL + photopath;
        String not_found_url = image_not_found_URL + photopath;

        if (!url.contains("no-photo.jpg")) {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(viewHolder.imageView);
        } else {
            Glide.with(context)
                    .load(not_found_url)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(viewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return subCategoryListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
//                System.out.println("Filter " + charString);
                List<SubCategoryRequest> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = originalList;
                } else {

                    for (SubCategoryRequest row : subCategoryRequestList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name  match
                        if (row.getScatName().toLowerCase().startsWith(charString.toLowerCase())) {
//                            System.out.println("row added:subcategory");
                            filteredList.add(row);
                        }
                    }
                    // subCategoryListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subCategoryListFiltered = (ArrayList<SubCategoryRequest>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // this interface creates for call the invalidateoptionmenu() for refresh the menu item
    public interface HomeCallBack {
        void updateCartCount(Context context);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView subcategoryName;
        TextView subcategoryCount;
        LinearLayout ll_Subcategory;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            subcategoryName = itemView.findViewById(R.id.subcategoryName);
            subcategoryCount = itemView.findViewById(R.id.subcategoryCount);
            ll_Subcategory = itemView.findViewById(R.id.ll_Subcategory);
            ll_Subcategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_Subcategory:
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra(Constants.INTENT_KEYS.KEY_CATEGORY_ID, categoryID);
                    intent.putExtra(Constants.INTENT_KEYS.KEY_SUB_CATEGORY_ID, subCategoryListFiltered.get(getAdapterPosition()).getScatId());
                    intent.putExtra(Constants.INTENT_KEYS.KEY_CATEGORY_NAME, subCategoryListFiltered.get(getAdapterPosition()).getScatName());
                    context.startActivity(intent);
            }
        }
    }
}