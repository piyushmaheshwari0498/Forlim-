package com.fashion.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsertOrderRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("order_data")
    @Expose
    private List<E_ClothOrderDetail> orderData = null;

//    delivery_id
    @SerializedName("delivery_addr_id")
    @Expose
    private String delivery_addr_id;


    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<E_ClothOrderDetail> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<E_ClothOrderDetail> orderData) {
        this.orderData = orderData;
    }

    public String getDelivery_addr_id() {
        return delivery_addr_id;
    }

    public void setDelivery_addr_id(String delivery_addr_id) {
        this.delivery_addr_id = delivery_addr_id;
    }

}
