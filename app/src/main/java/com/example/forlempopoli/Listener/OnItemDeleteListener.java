package com.example.forlempopoli.Listener;


import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnItemDeleteListener {
    void  onItemDeleted(DeliveryAddressRequest deliveryAddressRequest,int position);

}
