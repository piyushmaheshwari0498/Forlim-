package com.fashion.forlempopoli.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.fashion.forlempopoli.Entity.request.ProductRequest;

import java.util.List;

public class ProductDiffUtil extends DiffUtil.Callback {

    List<ProductRequest> oldList;
    List<ProductRequest> newLiist;

    public ProductDiffUtil(List<ProductRequest> oldList,List<ProductRequest> newLiist) {
        this.oldList = oldList;
        this.newLiist = newLiist;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newLiist.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getArtId().equals(newLiist.get(newItemPosition).getArtId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(!oldList.get(oldItemPosition).getArtId().equals(newLiist.get(newItemPosition).getArtId())){
            return false;
        }
       /* else if(!oldList.get(oldItemPosition).getGarmentPhoto().equals(newLiist.get(newItemPosition).getGarmentPhoto())){
            return false;
        }
        else if(!oldList.get(oldItemPosition).getArtPhoto().equals(newLiist.get(newItemPosition).getArtPhoto())){
            return false;
        }*/
//        else if(!oldList.get(oldItemPosition).getArtOfferPrice().equals(newLiist.get(newItemPosition).getArtOfferPrice())){
//            return false;
//        }

        else if(!oldList.get(oldItemPosition).getArtSellingPriceAmt().equals(newLiist.get(newItemPosition).getArtSellingPriceAmt())){
            return false;
        }
        else
            return true;

    }
}
