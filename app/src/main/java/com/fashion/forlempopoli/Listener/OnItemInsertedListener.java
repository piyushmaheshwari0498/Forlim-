package com.fashion.forlempopoli.Listener;


import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnItemInsertedListener {
    void  onItemInserted(DeliveryAddressRequest deliveryAddressRequest, int position);

}
