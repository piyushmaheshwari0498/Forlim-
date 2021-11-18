package com.example.forlempopoli.Listener;
import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;

public interface OnItemEditedListener {
  void  onItemEdited(DeliveryAddressRequest data, int position);
}
