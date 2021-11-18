package com.example.forlempopoli.Listener;

import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnRadioItemCheckListener {

    void onItemChecked(DeliveryAddressRequest deliveryAddressRequest);
    void onItemUnChecked(DeliveryAddressRequest deliveryAddressRequest);


}
