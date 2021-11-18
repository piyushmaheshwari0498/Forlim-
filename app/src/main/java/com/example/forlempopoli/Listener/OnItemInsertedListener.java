package com.example.forlempopoli.Listener;


import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnItemInsertedListener {
    void  onItemInserted(DeliveryAddressRequest deliveryAddressRequest, int position);

}
