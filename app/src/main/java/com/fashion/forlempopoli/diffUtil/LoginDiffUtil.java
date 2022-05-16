package com.fashion.forlempopoli.diffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.fashion.forlempopoli.Entity.request.LoginRequest;

import java.util.List;

public class LoginDiffUtil extends DiffUtil.Callback {

    List<LoginRequest> oldList;
    List<LoginRequest> newLiist;

    public LoginDiffUtil(List<LoginRequest> oldList, List<LoginRequest> newLiist) {
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
        return oldList.get(oldItemPosition).getAccId().equals(newLiist.get(newItemPosition).getAccId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(!oldList.get(oldItemPosition).getAccId().equals(newLiist.get(newItemPosition).getAccId())){
            return false;
        }
       /* else if(!oldList.get(oldItemPosition).getGarmentPhoto().equals(newLiist.get(newItemPosition).getGarmentPhoto())){
            return false;
        }
        else if(!oldList.get(oldItemPosition).getArtPhoto().equals(newLiist.get(newItemPosition).getArtPhoto())){
            return false;
        }*/
        else if(!oldList.get(oldItemPosition).getTotalBalance().equals(newLiist.get(newItemPosition).getTotalBalance())){
            return false;
        }
        else
            return true;

    }
}
