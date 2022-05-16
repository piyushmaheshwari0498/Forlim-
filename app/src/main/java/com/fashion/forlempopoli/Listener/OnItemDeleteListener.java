package com.fashion.forlempopoli.Listener;


import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnItemDeleteListener {
    void  onItemDeleted(DeliveryAddressRequest deliveryAddressRequest,int position);

}
