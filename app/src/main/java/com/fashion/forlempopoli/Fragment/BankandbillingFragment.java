package com.fashion.forlempopoli.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fashion.forlempopoli.Adapter.CityAdapter;
import com.fashion.forlempopoli.Adapter.CountryAdapter;
import com.fashion.forlempopoli.Adapter.StateAdapter;
import com.fashion.forlempopoli.Entity.request.BankBillingRequest;
import com.fashion.forlempopoli.Entity.request.CityRequest;
import com.fashion.forlempopoli.Entity.request.CountryRequest;
import com.fashion.forlempopoli.Entity.request.SpinnerBankBillingData;
import com.fashion.forlempopoli.Entity.request.StateRequest;
import com.fashion.forlempopoli.Entity.response.BankBillingResponse;
import com.fashion.forlempopoli.Entity.response.SpinnerBankBillingResponse;
import com.fashion.forlempopoli.Entity.response.UpdateBankBillingResponse;
import com.fashion.forlempopoli.Interface.ApiInterface;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Sharedpreference.AppSharedPreference;
import com.fashion.forlempopoli.Utilities.AppUtils;
import com.fashion.forlempopoli.Utilities.Constants;
import com.fashion.forlempopoli.Utilities.GSTValidation;
import com.fashion.forlempopoli.Utilities.PatternClass;
import com.fashion.forlempopoli.Utilities.RetrofitBuilder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankandbillingFragment extends Fragment {
    Button button_Save;
    MaterialButton editView,button_cancel;
    EditText etbankName, etaccountName, etaccountNo, etbranchName, etifscCode, etgstNo, etBillingAddr, etPincode,
            etcustomerName, etcontactPerson, etmobileNo, ettelephoneNo1, ettelephoneNo2, etwhatsappNo, etwebsite, etpan;
    TextView tvCustomer, tvBank, tvBilling;
    AutoCompleteTextView spCity;
    TextInputLayout inputSpCity,inputspState,inputspCountry;
    AutoCompleteTextView  spCountry, spState;
    String selectedCityId, selectedStateId, selectedCountryId;
    String cityname, statename, countryname, customertype, bank_name, acc_name, acc_no, branch_name, ifsc_code, gst_no, pincode, billing_addr, telephone1,
            telephone2, whatsapp_no, website, pan, customer_name, contact_person, mobile_no;
    String cityid, countryid, stateid;
    List<BankBillingRequest> bankBillingRequestList;
    String addr, bankname, accname, accno, branchname, ifsc, gst, pin,
            customername, acc_contact_person, acc_mobile, acc_tel_1, acc_tel_2, acc_whatsapp, acc_website, acc_pan;
    TextView tvCustomerType;
    CardView ll_bank_details;
    int i;
    BankBillingRequest mbankBillingRequest;
    SpinnerBankBillingData spinnerBankBillingData;
    CityAdapter customCityAdapter;
    StateAdapter stateAdapter;
    CountryAdapter countryAdapter;
    String selectedCityName = "Please Select City",
            selectedStateName = "Please Select State",
            selectedCountryName = "Please Select Country";
    TextView tv_contact_name,tvcustomerName, tv_contact_no;
    TextView tv_telephone_1, tv_telephone_2, tv_whatsapp, tv_website, tv_pan;
    TextView tv_bank_name, tv_acc_name, tv_acc_no, tv_branch_name, tv_ifsc_code;
    TextView tv_address, tv_city, tv_state, tv_country, tv_gst;

    TextInputLayout inputContactName, inputContactNo, inputtelephone1, inputtelephone2, inputwhatsapp, inputwebsite, inputemail,
            inputpan, inputbankName, inputaccName, inputaccNo, inputBranch, inputIfsc, inputAddress, inputGST, inputpincode;
    AppUtils appUtils = new AppUtils();
    private List<CityRequest> cityRequestList;
    private List<StateRequest> stateRequestList;
    private List<CountryRequest> countryRequestList;
    private AppSharedPreference mAppSharedPrefernce;
    ProgressDialog ringProgressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bank_billing, container, false);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(getActivity());
        appUtils = new AppUtils();

        ll_bank_details = view.findViewById(R.id.ll_bank_details);
        inputContactName = view.findViewById(R.id.inputetcontactPerson);
        inputContactNo = view.findViewById(R.id.inputetmobileNo);
        inputtelephone1 = view.findViewById(R.id.inputettelephoneNo1);
        inputtelephone2 = view.findViewById(R.id.inputettelephoneNo2);
        inputwhatsapp = view.findViewById(R.id.inputetwhatsappNo);
        inputwebsite = view.findViewById(R.id.inputetwebsite);
        inputpan = view.findViewById(R.id.inputetpan);
        inputbankName = view.findViewById(R.id.inputetbanname);
        inputaccName = view.findViewById(R.id.inputetaccoutnnum);
        inputaccNo = view.findViewById(R.id.inputetaccno);
        inputBranch = view.findViewById(R.id.inputetbranchname);
        inputIfsc = view.findViewById(R.id.inputetifsc);
        inputAddress = view.findViewById(R.id.inputetaddress);
        inputpincode = view.findViewById(R.id.inputetpincode);
        inputGST = view.findViewById(R.id.inputetgst);


        tvcustomerName = view.findViewById(R.id.tvcustomerName);
        tv_contact_name = view.findViewById(R.id.tv_contact_name);
        tv_contact_no = view.findViewById(R.id.tv_contact_no);
        tv_telephone_1 = view.findViewById(R.id.tv_telephone_1);
        tv_telephone_2 = view.findViewById(R.id.tv_telephone_2);
        tv_whatsapp = view.findViewById(R.id.tv_whatsapp);
        tv_website = view.findViewById(R.id.tv_website);
        tv_pan = view.findViewById(R.id.tv_pan);

        tv_bank_name = view.findViewById(R.id.tv_bank_name);
        tv_acc_name = view.findViewById(R.id.tv_acc_name);
        tv_acc_no = view.findViewById(R.id.tv_acc_no);
        tv_branch_name = view.findViewById(R.id.tv_branch_name);
        tv_ifsc_code = view.findViewById(R.id.tv_ifsc_code);

        tv_address = view.findViewById(R.id.tv_address);
        tv_city = view.findViewById(R.id.tv_city);
        tv_state = view.findViewById(R.id.tv_state);
        tv_country = view.findViewById(R.id.tv_country);
        tv_gst = view.findViewById(R.id.tv_gst);

        button_Save = view.findViewById(R.id.button_Save);
        button_cancel = view.findViewById(R.id.button_cancel);
        etbankName = view.findViewById(R.id.bankName);
        etaccountName = view.findViewById(R.id.accountName);
        etaccountNo = view.findViewById(R.id.accountNo);
        etbranchName = view.findViewById(R.id.branchName);
        etifscCode = view.findViewById(R.id.ifscCode);
        etgstNo = view.findViewById(R.id.gstNo);
        etBillingAddr = view.findViewById(R.id.etBillingAddr);

        tvCustomerType = view.findViewById(R.id.tvCustomerType);
        etPincode = view.findViewById(R.id.etPincode);
        spState = view.findViewById(R.id.spState);
        spCountry = view.findViewById(R.id.spCountry);
        spCity = view.findViewById(R.id.spCity);
        inputSpCity = view.findViewById(R.id.inputSpCity);
        inputspState = view.findViewById(R.id.inputspState);
        inputspCountry = view.findViewById(R.id.inputspCountry);
        editView = view.findViewById(R.id.editView);
        etcustomerName = view.findViewById(R.id.etcustomerName);
        etcontactPerson = view.findViewById(R.id.etcontactPerson);
        etmobileNo = view.findViewById(R.id.etmobileNo);
        ettelephoneNo1 = view.findViewById(R.id.ettelephoneNo1);
        ettelephoneNo2 = view.findViewById(R.id.ettelephoneNo2);
        etwhatsappNo = view.findViewById(R.id.etwhatsappNo);
        etwebsite = view.findViewById(R.id.etwebsite);
        etpan = view.findViewById(R.id.etpan);
        tvCustomer = view.findViewById(R.id.tvCustomer);
        tvBank = view.findViewById(R.id.tvBank);
        tvBilling = view.findViewById(R.id.tvBilling);

        tv_website.setMovementMethod(LinkMovementMethod.getInstance());

        ringProgressDialog = new ProgressDialog(getContext());

//        tvCustomer.setPaintFlags(tvCustomer.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tvBank.setPaintFlags(tvCustomer.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tvBilling.setPaintFlags(tvBilling.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(getContext())) {
                    Toasty.warning(getContext(), getContext().getResources().getString(R.string.no_internet));

                } else {
                    try {
                        System.out.println("updateBankBillingdetails");
                        if (!validatetelephone1() | !validatetelephone2() | !validatewhatsapp() | !validatePan()
                                | !validatebankName() | !validateHolderName() | !validateaccountNumber()
                                | !validatebranchName() | !validatefsc() | !validateaddress() | !validatepincode()
                                /*| !validateGST()*/) {
                            return;
                        }
                        updateBankBillingdetails();
                    } catch (Exception e) {
                        Log.e("registerError", e.getLocalizedMessage());
                    }
                }
            }
        });
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(getContext())) {
                    Toasty.warning(getContext(), getContext().getResources().getString(R.string.no_internet));

                } else {
                    setFieldsEnable();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!appUtils.isConnected(getContext())) {
                    Toasty.warning(getContext(), getContext().getResources().getString(R.string.no_internet));

                } else {
                    setFieldsDisable();
                    showBankBillingdetails();
                }
            }
        });

        if (!appUtils.isConnected(getContext())) {
            Toasty.warning(getContext(), getContext().getResources().getString(R.string.no_internet));

        } else {
            ringProgressDialog.setMessage("Loading...");
            ringProgressDialog.setCanceledOnTouchOutside(false);
            ringProgressDialog.show();
            getBankBillingSpinner();
            setFieldsDisable();
            setListenersForSpinners();
        }
        return view;
    }

    /*private void restLoader(){
        tv_bank_name.resetLoader();
        tv_acc_name.resetLoader();
        tv_acc_no.resetLoader();
        tv_branch_name.resetLoader();
        tv_ifsc_code.resetLoader();
        tv_gst.resetLoader();
        tv_address.resetLoader();
        tv_telephone_1.resetLoader();
        tv_whatsapp.resetLoader();
        tv_website.resetLoader();
        tv_pan.resetLoader();
        tvcustomerName.resetLoader();
        tv_contact_name.resetLoader();
        tv_contact_no.resetLoader();
        tv_city.resetLoader();
        tv_state.resetLoader();
    }*/

    private void setFieldsDisable() {
        tv_contact_name.setVisibility(View.VISIBLE);
        tv_contact_no.setVisibility(View.VISIBLE);
        tv_pan.setVisibility(View.VISIBLE);
        tv_telephone_1.setVisibility(View.VISIBLE);
        tv_telephone_2.setVisibility(View.GONE);
        tv_whatsapp.setVisibility(View.VISIBLE);
        tv_website.setVisibility(View.VISIBLE);

        etcontactPerson.setVisibility(View.GONE);
        etpan.setVisibility(View.GONE);
        etmobileNo.setVisibility(View.GONE);
        ettelephoneNo1.setVisibility(View.GONE);
        ettelephoneNo2.setVisibility(View.GONE);
        etwhatsappNo.setVisibility(View.GONE);
        etwebsite.setVisibility(View.GONE);

        inputContactName.setVisibility(View.GONE);
        inputpan.setVisibility(View.GONE);
        inputContactNo.setVisibility(View.GONE);
        inputtelephone1.setVisibility(View.GONE);
        inputtelephone2.setVisibility(View.GONE);
        inputwhatsapp.setVisibility(View.GONE);
        etwebsite.setVisibility(View.GONE);

        tv_acc_name.setVisibility(View.VISIBLE);
        tv_acc_no.setVisibility(View.VISIBLE);
        tv_bank_name.setVisibility(View.VISIBLE);
        tv_branch_name.setVisibility(View.VISIBLE);
        tv_ifsc_code.setVisibility(View.VISIBLE);

        etaccountName.setVisibility(View.GONE);
        etaccountNo.setVisibility(View.GONE);
        etbankName.setVisibility(View.GONE);
        etbranchName.setVisibility(View.GONE);
        etifscCode.setVisibility(View.GONE);

        inputaccName.setVisibility(View.GONE);
        inputaccNo.setVisibility(View.GONE);
        inputbankName.setVisibility(View.GONE);
        inputBranch.setVisibility(View.GONE);
        inputIfsc.setVisibility(View.GONE);


        tv_address.setVisibility(View.VISIBLE);
        tv_city.setVisibility(View.VISIBLE);
        tv_state.setVisibility(View.VISIBLE);
        tv_country.setVisibility(View.GONE);

        inputSpCity.setVisibility(View.GONE);
        inputspState.setVisibility(View.GONE);
        inputspCountry.setVisibility(View.GONE);
        etPincode.setVisibility(View.GONE);
        etBillingAddr.setVisibility(View.GONE);
        etgstNo.setVisibility(View.GONE);
        tv_gst.setVisibility(View.VISIBLE);

        inputpincode.setVisibility(View.GONE);
        inputAddress.setVisibility(View.GONE);
        inputGST.setVisibility(View.GONE);

        editView.setVisibility(View.VISIBLE);
        button_Save.setVisibility(View.GONE);
        button_cancel.setVisibility(View.GONE);
        etbankName.setEnabled(false);
        spCity.setEnabled(false);
        spState.setEnabled(false);
        spCountry.setEnabled(false);
        tvCustomerType.setEnabled(false);
        etPincode.setEnabled(false);
        etaccountName.setEnabled(false);
        etaccountNo.setEnabled(false);
        etbranchName.setEnabled(false);
        etifscCode.setEnabled(false);
        etgstNo.setEnabled(false);
        etcustomerName.setEnabled(false);
        etcontactPerson.setEnabled(false);
        etmobileNo.setEnabled(false);
        ettelephoneNo1.setEnabled(false);
        ettelephoneNo2.setEnabled(false);
        etwhatsappNo.setEnabled(false);
        etwebsite.setEnabled(false);
        etpan.setEnabled(false);
        etBillingAddr.setEnabled(false);
    }

    private void setFieldsEnable() {
        etgstNo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        tv_contact_name.setVisibility(View.GONE);
        tv_contact_no.setVisibility(View.GONE);
        tv_pan.setVisibility(View.GONE);
        tv_telephone_1.setVisibility(View.GONE);
        tv_telephone_2.setVisibility(View.GONE);
        tv_whatsapp.setVisibility(View.GONE);
        tv_website.setVisibility(View.GONE);


        etcontactPerson.setVisibility(View.VISIBLE);
        etpan.setVisibility(View.VISIBLE);
        etmobileNo.setVisibility(View.VISIBLE);
        ettelephoneNo1.setVisibility(View.VISIBLE);
        ettelephoneNo2.setVisibility(View.VISIBLE);
        etwhatsappNo.setVisibility(View.VISIBLE);
        etwebsite.setVisibility(View.VISIBLE);

        inputContactName.setVisibility(View.VISIBLE);
        inputpan.setVisibility(View.VISIBLE);
        inputContactNo.setVisibility(View.VISIBLE);
        inputtelephone1.setVisibility(View.VISIBLE);
        inputtelephone2.setVisibility(View.VISIBLE);
        inputwhatsapp.setVisibility(View.VISIBLE);
        etwebsite.setVisibility(View.VISIBLE);

        tv_acc_name.setVisibility(View.GONE);
        tv_acc_no.setVisibility(View.GONE);
        tv_bank_name.setVisibility(View.GONE);
        tv_branch_name.setVisibility(View.GONE);
        tv_ifsc_code.setVisibility(View.GONE);

        inputaccName.setVisibility(View.VISIBLE);
        inputaccNo.setVisibility(View.VISIBLE);
        inputbankName.setVisibility(View.VISIBLE);
        inputBranch.setVisibility(View.VISIBLE);
        inputIfsc.setVisibility(View.VISIBLE);

        etaccountName.setVisibility(View.VISIBLE);
        etaccountNo.setVisibility(View.VISIBLE);
        etbankName.setVisibility(View.VISIBLE);
        etbranchName.setVisibility(View.VISIBLE);
        etifscCode.setVisibility(View.VISIBLE);

        tv_address.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        tv_country.setVisibility(View.GONE);

        inputSpCity.setVisibility(View.VISIBLE);
        inputspState.setVisibility(View.VISIBLE);
        inputspCountry.setVisibility(View.VISIBLE);
        inputpincode.setVisibility(View.VISIBLE);
        inputAddress.setVisibility(View.VISIBLE);
        inputGST.setVisibility(View.VISIBLE);
        tv_gst.setVisibility(View.GONE);


        etPincode.setVisibility(View.VISIBLE);
        etBillingAddr.setVisibility(View.VISIBLE);
        etgstNo.setVisibility(View.VISIBLE);

        editView.setVisibility(View.GONE);
        button_Save.setVisibility(View.VISIBLE);
        button_cancel.setVisibility(View.VISIBLE);
        etbankName.setEnabled(true);
        spCity.setEnabled(true);
        spState.setEnabled(true);
        spCountry.setEnabled(true);
        etPincode.setEnabled(true);
        etaccountName.setEnabled(true);
        etaccountNo.setEnabled(true);
        etbranchName.setEnabled(true);
        etifscCode.setEnabled(true);
        etgstNo.setEnabled(false);
        etcontactPerson.setEnabled(false);
        etmobileNo.setEnabled(false);
        ettelephoneNo1.setEnabled(true);
        ettelephoneNo2.setEnabled(true);
        etwhatsappNo.setEnabled(true);
        etwebsite.setEnabled(true);
        etpan.setEnabled(true);
        etBillingAddr.setEnabled(true);
    }

    public boolean  validatetelephone1() {
        String tel1 = ettelephoneNo1.getText().toString().trim();
        if (tel1.isEmpty()) {
            return true;
        }
        if (!PatternClass.isValidPhone(tel1)) {
            inputtelephone1.setError("Please Enter Valid Telephone Number");
            return false;
        } else {
            inputtelephone1.setError(null);
            inputtelephone1.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatetelephone2() {
        String tel2 = ettelephoneNo2.getText().toString().trim();
        if (tel2.isEmpty()) {
            return true;
        }
        if (!PatternClass.isValidPhone(tel2)) {
            inputtelephone2.setError("Please Enter Valid Telephone Number");
            return false;
        } else {
            inputtelephone2.setError(null);
            inputtelephone2.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatewhatsapp() {
        String whatsapp = etwhatsappNo.getText().toString().trim();
        if (whatsapp.isEmpty()) {
            return true;
        }
        if (!PatternClass.isValidPhone(whatsapp)) {
            inputwhatsapp.setError("Please Enter Valid Whatsapp Number");
            return false;
        } else {
            inputwhatsapp.setError(null);
            inputwhatsapp.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatebankName() {
        String bankName = etbankName.getText().toString().trim();
        if (bankName.isEmpty()) {
            inputbankName.setError("Please Enter Bank Name");
            return false;
        } else {
            inputbankName.setError(null);
            inputbankName.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateHolderName() {
        String accHolderName = etaccountName.getText().toString().trim();
        if (accHolderName.isEmpty()) {
            inputaccName.setError("Please Enter Account Holder Name");
            return false;
        } else {
            inputaccName.setError(null);
            inputaccName.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateaccountNumber() {
        String accNo = etaccountNo.getText().toString().trim();
        if (accNo.isEmpty()) {
            inputaccNo.setError("Please Enter Account Number");
            return false;
        } else {
            inputaccNo.setError(null);
            inputaccNo.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatebranchName() {
        String branchName = etbranchName.getText().toString().trim();
        if (branchName.isEmpty()) {
            inputBranch.setError("Please Enter Branch Name");
            return false;
        } else {
            inputBranch.setError(null);
            inputBranch.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatefsc() {
        String ifsc = etifscCode.getText().toString().trim();
        if (ifsc.isEmpty()) {
            inputIfsc.setError("Please Enter IFSC Code");
            return false;
        } else {
            inputIfsc.setError(null);
            inputIfsc.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateaddress() {
        String address = etBillingAddr.getText().toString().trim();
        if (address.isEmpty()) {
            inputAddress.setError("Please Enter Address");
            return false;
        } else {
            inputAddress.setError(null);
            inputAddress.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatepincode() {
        String pincode = etPincode.getText().toString().trim();
        if (pincode.isEmpty()) {
            inputpincode.setError("Please Enter Pincode");
            return false;
        } else {
            inputpincode.setError(null);
            inputpincode.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateGST() throws Exception {
        String gst = etgstNo.getText().toString().trim();
        if (gst.isEmpty()) {
            inputGST.setError("Please Enter GST Number");
            return false;
        } else if (!GSTValidation.validGSTIN(gst)) {
            inputGST.setError("Please Enter Valid GST Number");
            return false;
        } else {
            inputGST.setError(null);
            inputGST.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePan() {
        String pan = etpan.getText().toString().trim();
        if (PatternClass.isValidPan(pan)) {
            inputpan.setError("Please Enter Valid Pan Number");
            return false;
        } else {
            inputpan.setError(null);
            inputpan.setErrorEnabled(false);
            return true;
        }
    }

    public void BankandbillingFragment(int i) {
        this.i = i;
    }

    public void showBankBillingdetails() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<BankBillingResponse> call = apiInterface.getbankbilling(getMap());
        call.enqueue(new Callback<BankBillingResponse>() {
            @Override
            public void onResponse(Call<BankBillingResponse> call, Response<BankBillingResponse> response) {
                ringProgressDialog.dismiss();
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        bankBillingRequestList = response.body().getData();
                        if (bankBillingRequestList != null && !bankBillingRequestList.isEmpty()) {
//                            restLoader();
                            bank_name = bankBillingRequestList.get(i).getAccBankName();
                            etbankName.setText(bank_name);
                            tv_bank_name.setText(bank_name);

                            acc_name = bankBillingRequestList.get(i).getAccBankAccName();
                            etaccountName.setText(acc_name);
                            tv_acc_name.setText(acc_name);

                            acc_no = bankBillingRequestList.get(i).getAccBankAccNo();
                            etaccountNo.setText(acc_no);
                            tv_acc_no.setText(acc_no);

                            branch_name = bankBillingRequestList.get(i).getAccBankBranchName();
                            etbranchName.setText(branch_name);
                            tv_branch_name.setText(branch_name);

                            ifsc_code = bankBillingRequestList.get(i).getAccBankIfscCode();
                            etifscCode.setText(ifsc_code);
                            tv_ifsc_code.setText(ifsc_code);

                            gst_no = bankBillingRequestList.get(i).getAccGst();
                            etgstNo.setText(gst_no);
                            tv_gst.setText("GST Number : " + gst_no);

                            pincode = bankBillingRequestList.get(i).getAccPinCode();
                            etPincode.setText(pincode);

                            billing_addr = bankBillingRequestList.get(i).getAccAddress();
                            etBillingAddr.setText(billing_addr);
                            tv_address.setText(billing_addr);

                            telephone1 = bankBillingRequestList.get(i).getAccTel1();
                            telephone2 = bankBillingRequestList.get(i).getAccTel2();
                            ettelephoneNo2.setText(telephone2);
                            ettelephoneNo1.setText(telephone1);
                            if(!telephone1.isEmpty() && !telephone2.isEmpty()){
                                tv_telephone_1.setText("Tel : " + telephone1 + ", " + telephone2);
                            }
                            else if (!telephone1.isEmpty()){
                                tv_telephone_1.setText("Tel : " + telephone1);
                            }
                            else if (!telephone2.isEmpty()){
                                tv_telephone_1.setText("Tel : " + telephone2);
                            }
                            else {
                                tv_telephone_1.setVisibility(View.GONE);
                            }
                            // tv_telephone_2.setText(telephone2);
                            whatsapp_no = bankBillingRequestList.get(i).getAccWhatsapp();
                            etwhatsappNo.setText(whatsapp_no);
                            if(!whatsapp_no.isEmpty()) {
                                tv_whatsapp.setText("Whatsapp Number : " + whatsapp_no);
                            }
                            else {
                                tv_whatsapp.setVisibility(View.GONE);
                            }

                            website = bankBillingRequestList.get(i).getAccWebsite();
                            if(website.isEmpty()) {
                                tv_website.setVisibility(View.GONE);
                            }
                            else {
                                etwebsite.setText(website);
                                tv_website.setText(website);
                                Linkify.addLinks(tv_website, Linkify.ALL);
                            }

                            pan = bankBillingRequestList.get(i).getAccPan();
                            if(pan.isEmpty()){
                                tv_pan.setVisibility(View.GONE);
                            }else {
                                etpan.setText(pan);
                                tv_pan.setText("Pan Number : " + pan);
                            }

                            customer_name = mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_NAME);
                            etcustomerName.setText(customer_name);
                            tvcustomerName.setText(customer_name);

                            contact_person = bankBillingRequestList.get(i).getAccContactPerson();
                            etcontactPerson.setText(contact_person);
                            tv_contact_name.setText(contact_person);

                            mobile_no = bankBillingRequestList.get(i).getAccMobile();
                            etmobileNo.setText(mobile_no);
                            //tv_contact_no.setText(mobile_no);

                            tv_contact_no.setText("Phone numbers : " + mobile_no);

                            cityname = bankBillingRequestList.get(i).getCityName();
                            tv_city.setText(bankBillingRequestList.get(i).getCityName() + " " + pincode);
                            cityid = bankBillingRequestList.get(i).getAccCityId();


                            System.out.println("city addr" + cityname);
                            statename = bankBillingRequestList.get(i).getStateName();
                            stateid = bankBillingRequestList.get(i).getAccStateId();

                            countryname = bankBillingRequestList.get(i).getCountryName();
                            countryid = bankBillingRequestList.get(i).getAccCountryId();

                            tv_state.setText(statename + ", " + countryname);

                            customertype = bankBillingRequestList.get(i).getAccTypeName();
                            tvCustomerType.setText(customertype);
                            System.out.println("customer type" + customertype);

                            setSelectedValuesForSpinner();
                            setSelectedValueForCountrySpinner();
                            setSelectedValueForStateSpinner();
                        }
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BankBillingResponse> call, Throwable t) {
                ringProgressDialog.dismiss();
                Log.e("profile failure",t.getMessage());
            }
        });
    }

    private void setSelectedValuesForSpinner() {
        spCity.setText(getSelctedCityPosition());
    }

    private String getSelctedCityPosition() {
        String selectedPosition = "";
        if (cityRequestList != null) {
            for (int i = 0; i < cityRequestList.size(); i++) {
                if (cityRequestList.get(i).getCityId().equals(cityid)) {
                    selectedPosition = cityRequestList.get(i).getCityName();
                    System.out.println("Spinner DeliveryAddress City Name get " +selectedPosition);
                    break;
                }
            }
        }
        return selectedPosition;
    }

    private void setSelectedValueForCountrySpinner() {
        spCountry.setText(getSelctedCountryPosition());
    }

    private String getSelctedCountryPosition() {
        String selectedPosition = "";
        if (countryRequestList != null) {
            for (int i = 0; i < countryRequestList.size(); i++) {
                if (countryRequestList.get(i).getCountryId().equals(countryid)) {
                    selectedPosition = countryRequestList.get(i).getCountryName();
                    break;
                }
            }
        }
        return selectedPosition;
    }

    private void setSelectedValueForStateSpinner() {
        spState.setText(getSelctedStatePosition());
    }

    private String getSelctedStatePosition() {
        String selectedPosition = "0";
        if (stateRequestList != null) {
            for (int i = 0; i < stateRequestList.size(); i++) {
                if (stateRequestList.get(i).getStateId().equals(stateid)) {
                    selectedPosition = stateRequestList.get(i).getStateName();
                    break;
                }
            }
        }
        return selectedPosition;
    }

    public void getBankBillingSpinner() {
        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<SpinnerBankBillingResponse> call = apiInterface.getBankBillingDetailsSpinner();
        call.enqueue(new Callback<SpinnerBankBillingResponse>() {
            @Override
            public void onResponse(Call<SpinnerBankBillingResponse> call, Response<SpinnerBankBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        spinnerBankBillingData = response.body().getData();
                        CountryRequest mCountryRequest = new CountryRequest();
                        mCountryRequest.setCountryName("Please Select Country");
                        mCountryRequest.setCountryId("0");

                        StateRequest stateRequest = new StateRequest();
                        stateRequest.setStateId("0");
                        stateRequest.setStateName("Please Select State");

                        CityRequest cityRequest = new CityRequest();
                        cityRequest.setCityId("0");
                        cityRequest.setCityName("Please Select City");

                        cityRequestList = spinnerBankBillingData.getCity();
                        stateRequestList = spinnerBankBillingData.getState();
                        countryRequestList = spinnerBankBillingData.getCountry();

                        countryRequestList.add(0, mCountryRequest);
                        stateRequestList.add(0, stateRequest);
                        cityRequestList.add(0, cityRequest);

                        setAdaptersForSpinners();
                    } else {
                        if (response.code() == 404 && response.message().equals("Not Found")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String errormessage = jsonObject.getString("message");
                                System.out.println("msg" + errormessage);
                                Toast.makeText(getActivity(), errormessage, Toast.LENGTH_LONG).show();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SpinnerBankBillingResponse> call, Throwable t) {

            }
        });
    }

    private void setAdaptersForSpinners() {
        if (getActivity() != null) {
            customCityAdapter = new CityAdapter(getActivity(), cityRequestList);
            spCity.setAdapter(customCityAdapter);

            stateAdapter = new StateAdapter(getActivity(), stateRequestList);
            spState.setAdapter(stateAdapter);

            countryAdapter = new CountryAdapter(getActivity(), countryRequestList);
            spCountry.setAdapter(countryAdapter);
        }
        showBankBillingdetails();
    }

    private void setListenersForSpinners() {
       /* spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCityId = cityRequestList.get(i).getCityId();
                System.out.println("Spinner DeliveryAddress City Id : " + selectedCityId);
                selectedCityName = cityRequestList.get(i).getCityName();
                System.out.println("Spinner DeliveryAddress City Name : " + selectedCityName);
                spCity.setText(selectedCityName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCityName = cityname;
            }
        });*/

        spCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                selectedCityName = cityRequestList.get(pos).getCityName();
                selectedCityId = cityRequestList.get(pos).getCityId();
                System.out.println("Spinner DeliveryAddress City Name : " + selectedCityName);
                System.out.println("Spinner DeliveryAddress City Id : " + selectedCityId);
                spCity.setText(selectedCityName);
            }
        });

        /*spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedStateId = stateRequestList.get(pos).getStateId();
                selectedStateName = stateRequestList.get(pos).getStateName();
                spState.setText(selectedStateName);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedStateName = statename;
            }
        });*/
        spState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                selectedStateId = stateRequestList.get(pos).getStateId();
                selectedStateName = stateRequestList.get(pos).getStateName();
                System.out.println("Spinner DeliveryAddress State Name : " + selectedStateName);
                System.out.println("Spinner DeliveryAddress State Id : " + selectedStateId);
                spState.setText(selectedStateName);
            }
        });
       /* spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCountryId = countryRequestList.get(pos).getCountryId();
                selectedCountryName = countryRequestList.get(pos).getCountryName();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountryName = countryname;
            }
        });*/
        spCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                selectedCountryId = countryRequestList.get(pos).getCountryId();
                selectedCountryName = countryRequestList.get(pos).getCountryName();
                System.out.println("Spinner DeliveryAddress Country Name : " + selectedCountryName);
                System.out.println("Spinner DeliveryAddress Country Id : " + selectedCountryId);
                spCountry.setText(selectedCountryName);
            }
        });

    }

    public void updateBankBillingdetails() {

        bankname = etbankName.getText().toString();
        addr = etBillingAddr.getText().toString();
        accname = etaccountName.getText().toString();
        accno = etaccountNo.getText().toString();
        branchname = etbranchName.getText().toString();
        ifsc = etifscCode.getText().toString();
        gst = etgstNo.getText().toString();
        pin = etPincode.getText().toString();
        customername = etcustomerName.getText().toString();
        acc_contact_person = etcontactPerson.getText().toString();
        acc_mobile = etmobileNo.getText().toString();
        acc_tel_1 = ettelephoneNo1.getText().toString();
        acc_tel_2 = ettelephoneNo2.getText().toString();
        acc_whatsapp = etwhatsappNo.getText().toString();
        acc_website = etwebsite.getText().toString();
        acc_pan = etpan.getText().toString();

        mbankBillingRequest = new BankBillingRequest();
        mbankBillingRequest.setAccBankName(bankname);
        mbankBillingRequest.setAccAddress(addr);
        mbankBillingRequest.setAccBankAccName(accname);
        mbankBillingRequest.setAccBankAccNo(accno);
        mbankBillingRequest.setAccBankBranchName(branchname);
        mbankBillingRequest.setAccBankIfscCode(ifsc);
        mbankBillingRequest.setAccGst(gst);
        mbankBillingRequest.setAccPinCode(pin);
        mbankBillingRequest.setAccContactPerson(acc_contact_person);
        mbankBillingRequest.setAccMobile(acc_mobile);
        mbankBillingRequest.setAccTel1(acc_tel_1);
        mbankBillingRequest.setAccTel2(acc_tel_2);
        mbankBillingRequest.setAccWhatsapp(acc_whatsapp);
        mbankBillingRequest.setAccWebsite(acc_website);
        mbankBillingRequest.setAccPan(acc_pan);
        mbankBillingRequest.setCityName(selectedCityName);
        Log.d("cityname in update", selectedCityName);
        mbankBillingRequest.setCountryName(selectedCountryName);
        mbankBillingRequest.setStateName(selectedStateName);


        ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<UpdateBankBillingResponse> call = apiInterface.updatebankbillingDetails(getHashMap());
        call.enqueue(new Callback<UpdateBankBillingResponse>() {
            @Override
            public void onResponse(Call<UpdateBankBillingResponse> call, Response<UpdateBankBillingResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        String msg = response.body().getMessage();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        System.out.println("msg" + msg);
                        setFieldsDisable();
                        getBankBillingSpinner();
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateBankBillingResponse> call, Throwable t) {
                System.out.println("onFailure BankBilling" + t.getMessage());
            }
        });
    }

    private HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        map.put("acc_bank_name", bankname);
        map.put("acc_bank_acc_name", accname);
        map.put("acc_bank_branch_name", branchname);
        map.put("acc_bank_ifsc_code", ifsc);
        map.put("acc_gst", gst);
        map.put("acc_pin_code", pin);
        map.put("acc_address", addr);
        map.put("acc_bank_acc_no", accno);
        map.put("acc_city_id", selectedCityId);
        map.put("acc_state_id", selectedStateId);
        map.put("acc_country_id", selectedCountryId);
        map.put("acc_type", customertype);
        map.put("acc_name", customername);
        map.put("acc_contact_person", acc_contact_person);
        map.put("acc_mobile", acc_mobile);
        map.put("acc_tel_1", acc_tel_1);
        map.put("acc_tel_2", acc_tel_2);
        map.put("acc_whatsapp", acc_whatsapp);
        map.put("acc_website", acc_website);
        map.put("acc_pan", acc_pan);

        return map;
    }

    private HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
        map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
        return map;
    }

}