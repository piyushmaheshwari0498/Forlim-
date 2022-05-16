package com.fashion.forlempopoli.Listener;
import com.fashion.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnItemEditedListener {
  void  onItemEdited(DeliveryAddressRequest data, int position);
}
