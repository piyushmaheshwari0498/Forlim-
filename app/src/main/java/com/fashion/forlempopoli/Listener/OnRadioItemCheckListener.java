package com.fashion.forlempopoli.Listener;

import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnRadioItemCheckListener {

    void onItemChecked(DeliveryAddressRequest deliveryAddressRequest);
    void onItemUnChecked(DeliveryAddressRequest deliveryAddressRequest);


}
