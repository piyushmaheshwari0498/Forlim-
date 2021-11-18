package com.example.forlempopoli.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.forlempopoli.Entity.request.DeliveryAddressRequest;
import com.example.forlempopoli.Entity.response.DeleteDeliveryAddressResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.Listener.OnItemDeleteListener;
import com.example.forlempopoli.Listener.OnItemInsertedListener;
import com.example.forlempopoli.LoginActivity;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delete_Dialog extends DialogFragment {
    Button btnyes,btn_no;
    String acct_id;
    int i;
    List<DeliveryAddressRequest> deliveryAddressRequestList;
    private OnItemDeleteListener onItemDeleteListener;
    DeliveryAddressRequest addressRequest;
    public Delete_Dialog(DeliveryAddressRequest addressRequest, int i) {
        this.addressRequest=addressRequest;
        this.acct_id=addressRequest.getAcctId();
        this.deliveryAddressRequestList = new ArrayList<>();
        this.deliveryAddressRequestList.add(addressRequest);
        this.i=i;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.delete_delivery_address_dialog, container, false);
          btnyes=itemView.findViewById(R.id.btnyes);
          btn_no=itemView.findViewById(R.id.btn_no);

          btnyes.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  Log.e("i", String.valueOf(i));
                  Log.e("acct_id", String.valueOf(acct_id));
                  Log.e("addressRequest", addressRequest.getAcctName());

              deleteAddressDetails(deliveryAddressRequestList.get(i).getAcctId());
              dismiss();
              }
          });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             dismiss();
            }

        });
        return itemView;
    }
    public void deleteAddressDetails(String accountRequest) {
        acct_id=accountRequest;
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<DeleteDeliveryAddressResponse> call = apiInterface.deleteDeliveryAddress(getMap());
        call.enqueue(new Callback<DeleteDeliveryAddressResponse>() {
            @Override
            public void onResponse(Call<DeleteDeliveryAddressResponse> call, Response<DeleteDeliveryAddressResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
//                        String msg= response.body().getMessage();
//                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                        if(onItemDeleteListener!=null) {
                            onItemDeleteListener.onItemDeleted(deliveryAddressRequestList.get(i),i);
                        }
                    }
                }
                else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errormessage = jsonObject.getString("message");
                            System.out.println("msg"+errormessage);
                            Toast.makeText(getActivity(), errormessage, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteDeliveryAddressResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("onFailure"+t.toString());
            }
        });
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY","ZkC6BDUzxz");
        map.put("acct_id",acct_id);
        return map;
    }
    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener=onItemDeleteListener;
    }
}
