package com.fashion.forlempopoli.Interface;

import com.fashion.forlempopoli.Entity.request.InsertOrderRequest;
import com.fashion.forlempopoli.Entity.request.SaleReturnPdfResponse;
import com.fashion.forlempopoli.Entity.response.CreditDetailsResponse;
import com.fashion.forlempopoli.Entity.response.CustomerTypeResponse;
import com.fashion.forlempopoli.Entity.response.AddDeliveryResponse;
import com.fashion.forlempopoli.Entity.response.AscendingOrderResponse;
import com.fashion.forlempopoli.Entity.response.BankBillingResponse;
import com.fashion.forlempopoli.Entity.response.BillingResponse;
import com.fashion.forlempopoli.Entity.response.CategoryResponse;
import com.fashion.forlempopoli.Entity.response.ConfirmPwdResponse;
import com.fashion.forlempopoli.Entity.response.CustomerLegerResponse;
import com.fashion.forlempopoli.Entity.response.DataResponse;
import com.fashion.forlempopoli.Entity.response.DeleteDeliveryAddressResponse;
import com.fashion.forlempopoli.Entity.response.DeliveryAddressResponse;
import com.fashion.forlempopoli.Entity.response.DetailsOrderResponse;
import com.fashion.forlempopoli.Entity.response.E_OrderPlaceResponse;
import com.fashion.forlempopoli.Entity.response.ForgotPasswordResponse;
import com.fashion.forlempopoli.Entity.response.LoginResponse;
import com.fashion.forlempopoli.Entity.response.OrderDataResponse;
import com.fashion.forlempopoli.Entity.response.InvoiceResponse;
import com.fashion.forlempopoli.Entity.response.PdfResponse;
import com.fashion.forlempopoli.Entity.response.ProductResponse;
import com.fashion.forlempopoli.Entity.response.RegisterResponse;
import com.fashion.forlempopoli.Entity.response.SaleReturnResponse;
import com.fashion.forlempopoli.Entity.response.SamplingPdfResponse;
import com.fashion.forlempopoli.Entity.response.SamplingResponse;
import com.fashion.forlempopoli.Entity.response.SpinnerBankBillingResponse;
import com.fashion.forlempopoli.Entity.response.SpinnerRegisterResponse;
import com.fashion.forlempopoli.Entity.response.SubCategoryResponse;
import com.fashion.forlempopoli.Entity.response.UpdateBankBillingResponse;
import com.fashion.forlempopoli.Entity.response.UpdateBillingResponse;
import com.fashion.forlempopoli.Entity.response.UpdateDeliveryAddressResponse;
import com.fashion.forlempopoli.Entity.response.Upt_Default_Deilvery_AddressResponse;
import com.fashion.forlempopoli.Entity.response.VerifyResponse;
import java.util.HashMap;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiInterface {
    //Login
    @POST("api/user/login")
    Call<LoginResponse> getLoginDetails(@Body HashMap<String, Object> loginResponse);

    //Credit Details
    @POST("api/user/balance_details")
//    @HTTP(method = "GET", path = "api/user/balance_details", hasBody = true)
    Call<CreditDetailsResponse> getCreditDetails(@Body HashMap<String, Object> creditRespnse);

    //Register
    @POST("api/user/register")
    Call<RegisterResponse> getRegisterDetails(@Body HashMap<String, Object> registerResponse);

    //Spinner registerb
    @GET("api/user/registration_details")
    Call<SpinnerRegisterResponse> getRegisterSpinnerData();

    //Fogot Password
    @POST("api/user/forgost_pass_st1")
    Call<ForgotPasswordResponse> getForgotpwdDetails(@Body HashMap<String, Object> forgotpwdResponse);

    //Verify Password
    @POST("api/user/forgost_pass_st2")
    Call<VerifyResponse> getVerifypwdDetails(@Body HashMap<String, Object> forgotpwdResponse);

    //Change Password
    @POST("api/user/forgost_pass_st3")
    Call<ConfirmPwdResponse> getConfirmpwdDetails(@Body HashMap<String, Object> forgotpwdResponse);

    //Category
//    @POST("api/fabric/category")
//    Call<CategoryResponse> getCategoryDetails(@Body HashMap<String, Object> CategoryResponse);

    @POST("api/fabric/category_selected_show")
    Call<CategoryResponse> getCategoryDetails(@Body HashMap<String, Object> CategoryResponse);

    //SubCategory
    @POST("api/fabric/subcategory")
    Call<SubCategoryResponse> getSubCategoryDetails(@Body HashMap<String, Object> SubCategoryResponse);

    //Product
    @POST("api/fabric/article")
    Call<ProductResponse> getProductDetails(@Body HashMap<String, Object> ProductResponse);

    //Global Product search
    @POST("api/article/global_search")
    Call<ProductResponse> getGlobalProductDetails(@Body HashMap<String, Object> ProductResponse);

    //Confirm Order
    @POST("api/fabric/order")
    Call<E_OrderPlaceResponse>placeorder(@Body InsertOrderRequest insertFoodItems);

    @POST("api/fabric/order_payment")
    Call<E_OrderPlaceResponse>testplaceorder(@Body InsertOrderRequest insertFoodItems);

    //Order Data
    @POST("api/fabric/order_data")
    Call<OrderDataResponse> getClothsOrderData(@Body HashMap<String, Object> DataResponse);

    //Order Details
    @POST("api/fabric/order_details")
    Call<DetailsOrderResponse> getClothsOrderDetails(@Body HashMap<String, Object> DataResponse);

    //Spinner data Address details
    @GET("api/fabric/customer_billingdropdown")
    Call<SpinnerBankBillingResponse> getBankBillingDetailsSpinner();

    //Spinner customerType
    @GET("api/user/acct_type_details")
    Call<CustomerTypeResponse> getAccountTypeSpinner();

    //bank & billing details
    @POST("api/fabric/customer_bank_bill_show")
    Call<BankBillingResponse> getbankbilling(@Body HashMap<String, Object> bankbillingdetailsResponse);

    //Update bank & billing details
    @POST("api/fabric/customer_bank_bill_update")
    Call<UpdateBankBillingResponse> updatebankbillingDetails(@Body HashMap<String, Object> updatebankbillingdetailsResponse);

    //Add Delivery details
    @POST("api/fabric/customer_delivery_insert")
    Call<AddDeliveryResponse> insertDeliverydetails(@Body HashMap<String, Object> deliverydetailsResponse);

    /*Profile Tab
    insert,update,delete Delivery Address fragment
    */
    //Show Delivery Address details
    @POST("api/fabric/customer_delivery_show")
    Call<DeliveryAddressResponse>showDeliveryAddr(@Body HashMap<String, Object> bankbillingdetailsResponse);

    //Update Delivery Address  details
    @Multipart
    @POST("api/fabric/customer_delivery_update")
    Call<UpdateDeliveryAddressResponse> updateDeliveryAddress(@PartMap() Map<String, RequestBody> partMap);

    //Delete Delivery Address  details
    @POST("api/fabric/customer_delivery_delete")
    Call<DeleteDeliveryAddressResponse> deleteDeliveryAddress(@Body HashMap<String, Object> deletebDeliveryAddressResponse);

    //Update Default Deilvery Address
    @POST("api/fabric/delivery_default_address_update")
    Call<Upt_Default_Deilvery_AddressResponse> updateDefaultDeliveryAddress(@Body HashMap<String, String> updateDeliveryAddress);

    /*Delivery Address Activity
    insert,update billing details
     */
    //Show billing address
    @POST("api/fabric/customer_bill_show")
    Call<BillingResponse> showCustomerbillingAddr(@Body HashMap<String, Object>showCustomerbillingResponse);
    //Update billing address
    @POST("api/fabric/customer_bill_update")
    Call<UpdateBillingResponse> updateCustomerbillingAddr(@Body HashMap<String, Object> updateCustomerbillingResponse);


    //Invoice
    @POST("api/fabric/order_sales_master_trans_data")
    Call<InvoiceResponse>showInvoicedata(@Body HashMap<String, Object> invoicedataResponse);

    //Pdf print
    @POST("api/fabric/pdf_print")
    Call<PdfResponse>printPdf(@Body HashMap<String, Object> printPdfResponse);

    //Customer ledger
    @POST("api/fabric/customer_ledger_print")
    Call<CustomerLegerResponse>customerLedgerDetails(@Body HashMap<String, Object> customerLedgerResponse);

    //Show Sampling data
    @POST("api/fabric/sampling_sale_data")
    Call<SamplingResponse>showSamplingdata(@Body HashMap<String, Object> SamplingdataResponse);

    //Pdf Samplingdata
    @POST("api/fabric/sampling_pdf_print")
    Call<SamplingPdfResponse>samplingprintPdf(@Body HashMap<String, Object> printPdfResponse);

    //Sort by Ascending Order
    @POST("api/fabric/article")
    Call<AscendingOrderResponse>sortByAscendingOrder(@Body HashMap<String, Object> printPdfResponse);

    //Send data to server
    @Multipart
    @POST("api/fabric/mobile_api")
    Call<DataResponse>sendData(@PartMap() HashMap<String, RequestBody> body);

    //Payment Details
    @POST("api/fabric/final_bill_details")
    Call<InvoiceResponse>paymentDetails(@Body HashMap<String, Object> paymentResponse);

    //Sale Return
    @POST("api/fabric/sale_return_details")
    Call<SaleReturnResponse>getsaleReturn(@Body HashMap<String, Object> salerturnResponse);

    //SaleReturn Pdf
    @POST("api/fabric/pdf_print_srt")
    Call<SaleReturnPdfResponse> saleReturnPdf(@Body HashMap<String, Object> pdfResponse);

    @POST("api/article/article_dropdown")
    Call<ProductResponse> getArticleList(@Body HashMap<String, Object> pdfResponse);


}
