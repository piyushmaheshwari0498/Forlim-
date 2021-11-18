package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomertypeData {
    @SerializedName("acc_type")
    @Expose
    private List<CustomerTypeRequest> accType = null;

    public List<CustomerTypeRequest> getAccType() {
        return accType;
    }

    public void setAccType(List<CustomerTypeRequest> accType) {
        this.accType = accType;
    }
}
