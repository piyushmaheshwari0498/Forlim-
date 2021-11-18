package com.example.forlempopoli.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forlempopoli.Activity.PDFActivity;
import com.example.forlempopoli.Entity.request.InvoiceRequest;
import com.example.forlempopoli.Entity.request.OrderItemRequest;
import com.example.forlempopoli.Entity.response.PdfResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;
import com.paynimo.android.payment.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    List<InvoiceRequest> invoiceRequestList;
    Context context;
    String smid;
    String smId;
    String resulturl, acc_id;
    int pos;
    Date date;
    SimpleDateFormat formatter;
    List<OrderItemRequest> orderRequestList;
    String orderno, orderdate, finalamt, subamt;
    private AppSharedPreference mAppSharedPrefernce;
    private View mView;

    public InvoiceAdapter(List<InvoiceRequest> invoiceRequestList, Context context) {
        this.invoiceRequestList = invoiceRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_invoice_adapter, viewGroup, false);
        View insertedView = LayoutInflater.from(context).inflate(R.layout.order_invoice_item_layout, viewGroup, false);
        mView = insertedView;
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(context);
        acc_id = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID);
        return new InvoiceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_order_id.setText(invoiceRequestList.get(i).getSmBillNo());
        viewHolder.tv_order_date.setText(invoiceRequestList.get(i).getSmBillDate());
        viewHolder.tv_total_price.setText(invoiceRequestList.get(i).getSmFinalAmt());
        viewHolder.tv_taxable_amt.setText(invoiceRequestList.get(i).getSmTaxableAmt());
        viewHolder.tv_cgst.setText(invoiceRequestList.get(i).getSmCgstAmt());
        viewHolder.tv_sgst.setText(invoiceRequestList.get(i).getSmSgstAmt());

        orderno = invoiceRequestList.get(i).getSmBillNo();
        orderdate = invoiceRequestList.get(i).getSmBillDate();
        subamt = invoiceRequestList.get(i).getSmSubAmt();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());
        orderRequestList = invoiceRequestList.get(i).getTrans();
        viewHolder.ll_data.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.ll_data.setAdapter(new OrderItemHistoryAdapter(orderRequestList, context));
        viewHolder.ll_data.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public int getItemCount() {
        return invoiceRequestList.size();
    }

    private void navigateToPayementActivity() {
        // Creating Checkout Object
        Checkout checkout = new Checkout();
        checkout.setMerchantIdentifier("T619165"); //where T1234 is the MERCHANT CODE, update it with Merchant Code provided by TPSL
        checkout.setTransactionIdentifier(smId); //where TXN001 is the Merchant Transaction Identifier, it should be different for each transaction (alphanumeric value, no special character allowed)
        checkout.setTransactionReference(orderno); //where ORD0001 is the Merchant Transaction Reference number
        checkout.setTransactionType(PaymentActivity.TRANSACTION_TYPE_SALE);//Transaction Type
        checkout.setTransactionSubType(PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);//Transaction Subtype
        checkout.setTransactionCurrency("INR"); //Currency Type
        checkout.setTransactionAmount(finalamt); //Transaction Amount
        checkout.setTransactionDateTime(formatter.format(date)); //Transaction Date
        // setting Consumer fields values
        checkout.setConsumerIdentifier(""); //Consumer Identifier, default value "", set this value as application user name if you want Instrument Vaulting, SI on Cards. Consumer ID should be alpha-numeric value with no space
        checkout.setConsumerAccountNo(""); //Account Number, default value "". For eMandate, you can set this value here or can be set later in SDK.

        // setting Consumer Cart Item
        /*
      // Above Cart Item has following fields

ProductID //SCHEME CODE, update it with Scheme Code provided by TPSL
ProductAmount //amount of the Product should be same as Transaction amount
ProductSurchargeOrDiscountAmount //Surcharge Or Discount amount, default value 0.0
CommisionAmount	//Commision Amount on the Product, default value 0.0
ProductSKU   //default ""
ProductReference  //default ""
ProductDescriptor  //Additional details for Product, default ""
ProductProviderIdentifier  //Product provider Id, default ""

       */
        checkout.addCartItem("ProductID", "ProductAmount", "ProductSurchargeOrDiscountAmount", "CommisionAmount",
                "ProductSKU", "ProductReference", "ProductDescriptor", "ProductProviderID");


//        A consumer can have multiple Cart items, in this case the sum of all cart items
//        ProductAmount and CommisionAmount must be equal to the
//        checkout.setTransactionAmount (amount);
        checkout.setTransactionAmount(finalamt); //Transaction amount

//        If you want additional details of transaction, you can set those parameters in Checkout object as follows:
        checkout.setCartDescription("{name:User1}{accountNo:1234567890}");


// ACTION Y for SI enabled from merchant end and N for SI disabled
        checkout.setPaymentInstructionAction("Y");
// Amount type -> Fixed - F, Maximum - M
        checkout.setPaymentInstructionType("F");
// Max amount
//        checkout.setPaymentInstructionLimit("1000.00");
// Payment Frequency
// DAIL - Daily, Week - Weekly, MNTH - Monthly,
//        QURT - Quarterly, MIAN - Semi annually, YEAR - Yearly, BIMN - Bi-monthly,
//                ADHO - As and when presented

        /*checkout.setPaymentInstructionFrequency("ADHO");
// Debit start date, format -> 'DD-MM-YYYY'
        checkout.setPaymentInstructionStartDateTime("12-03-2021");
// Debit end date, format -> 'DD-MM-YYYY'
        checkout.setPaymentInstructionEndDateTime("12-03-2041");
// Account Holder Name (optional)
        checkout.setConsumerAccountHolderName("Rahul");
// Account Type (optional)
// Saving - Saving, Current - Current, Cash Credit - CC
        checkout.setConsumerAccountType("CC");
// Bank IFSC Code (optional)
        checkout.setPaymentInstrumentIFSC("ICIC0000015");
// Set Debit Date Flag (optional)
// Flag Y for setting mandate Debit Date
// Flag N/”” for not setting Debit Date
        checkout.setPaymentInstructionDebitFlag("Y");
// Set Debit Date (optional)
// If Flag Y set for DebitFlag then set Debit Day here in DD format
        checkout.setPaymentInstructionDebitDay("05");

        checkout.setConsumerEmailID ("test@gmail.com"); //Consumer Email ID
        checkout.setConsumerMobileNumber ("7620656789"); //Consumer Mobile Number
        checkout.setConsumerPan ("BYTUI1144J"); //Consumer PAN
        checkout.setConsumerPhoneNumber ("02225458585"); //Consumer Phone Number*/


        Intent authIntent = PaymentModesActivity.Factory.getAuthorizationIntent(context, true);
        // Checkout Object
        Log.d("Checkout Request Object", checkout.getMerchantRequestPayload().toString());
        System.out.println("Checkout Request Object" + checkout.getMerchantRequestPayload().toString());

        authIntent.putExtra(Constant.ARGUMENT_DATA_CHECKOUT, checkout);
        // Public Key
        authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY, "1234-6666-6789-56");
        // Requested Payment Mode
        authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE, PaymentActivity.PAYMENT_METHOD_DEFAULT);
        PaymentModesActivity.Settings settings = new PaymentModesActivity.Settings();
        authIntent.putExtra(Constant.ARGUMENT_DATA_SETTING, settings);
        ((Activity) context).startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_order_id, tv_order_date, tv_total_price, tv_taxable_amt, tv_cgst,
                tv_sgst, tvInvoice, tvPayNow, tvBillDetails;
        RecyclerView ll_data;

        ViewHolder(View itemView) {
            super(itemView);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            tv_taxable_amt = itemView.findViewById(R.id.tv_taxable_amt);
            tvInvoice = itemView.findViewById(R.id.tvInvoice);
            tv_sgst = itemView.findViewById(R.id.tv_sgst);
            tv_cgst = itemView.findViewById(R.id.tv_cgst);
            tvPayNow = itemView.findViewById(R.id.tvPayNow);
            ll_data = itemView.findViewById(R.id.ll_data);
            tvBillDetails = itemView.findViewById(R.id.tvBillDetails);
            tvBillDetails.setPaintFlags(tvBillDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvInvoice.setOnClickListener(this);
            tvPayNow.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvInvoice:
                    pos = getAdapterPosition();
                    smid = invoiceRequestList.get(pos).getSmId();
                    pdfDetails();
                    break;
                case R.id.tvPayNow:
                    pos = getAdapterPosition();
                    smId = invoiceRequestList.get(pos).getSmId();
                    // System.out.println("smId tvPayNow"+smId);

                    finalamt = invoiceRequestList.get(pos).getSmFinalAmt();
                    // System.out.println("tvPayNow finalamt"+finalamt);

                    Intent intent = new Intent("data");
                    intent.putExtra("acc_id", acc_id);
                    intent.putExtra("sm_id", smId);
                    intent.putExtra("sm_final_amt", finalamt);
                    intent.putExtra("date", formatter.format(date));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    navigateToPayementActivity();
                    break;
            }
        }

        public void pdfDetails() {
            final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
            Call<PdfResponse> call = apiInterface.printPdf(getHashMap());
            call.enqueue(new Callback<PdfResponse>() {
                @Override
                public void onResponse(Call<PdfResponse> call, Response<PdfResponse> response) {
                    if (response.code() == 200 && response.message().equals("OK")) {
                        if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                            // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            resulturl = response.body().getResultUrl();
                            Intent intent = new Intent(context, PDFActivity.class);
                            intent.putExtra("position", pos);
                            intent.putExtra(Constants.INTENT_KEYS.KEY_SM_ID, smid);
                            intent.putExtra("request_pdf", resulturl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            System.out.println("url" + resulturl);

                        }
                    } else {
                        if (response.code() == 404 && response.message().equals("Not Found")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                System.out.println("404" + message);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Something Went Wrong");
                            Toast.makeText(context, "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PdfResponse> call, Throwable t) {
                    //ringProgressDialog.dismiss();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("onFailure getMessage" + t.getMessage());
                }
            });
        }

        private HashMap<String, Object> getHashMap() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
            map.put("acc_id", acc_id);
            // System.out.println("acc_id"+mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            map.put("sm_id", smid);
            //  System.out.println("sm_id"+sm_id);
            return map;
        }
    }
}


