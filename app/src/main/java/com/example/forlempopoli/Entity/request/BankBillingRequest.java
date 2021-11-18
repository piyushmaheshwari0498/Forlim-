package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankBillingRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("acc_bank_name")
    @Expose
    private String accBankName;
    @SerializedName("acc_bank_acc_name")
    @Expose
    private String accBankAccName;
    @SerializedName("acc_bank_acc_no")
    @Expose
    private String accBankAccNo;
    @SerializedName("acc_bank_branch_name")
    @Expose
    private String accBankBranchName;
    @SerializedName("acc_bank_ifsc_code")
    @Expose
    private String accBankIfscCode;
    @SerializedName("acc_type")
    @Expose
    private String accType;
    @SerializedName("acc_type_name")
    @Expose
    private String accTypeName;
    @SerializedName("acc_gst")
    @Expose
    private String accGst;
    @SerializedName("acc_address")
    @Expose
    private String accAddress;
    @SerializedName("acc_city_id")
    @Expose
    private String accCityId;
    @SerializedName("acc_state_id")
    @Expose
    private String accStateId;
    @SerializedName("acc_country_id")
    @Expose
    private String accCountryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("acc_pin_code")
    @Expose
    private String accPinCode;
    @SerializedName("acc_tel_1")
    @Expose
    private String accTel1;
    @SerializedName("acc_tel_2")
    @Expose
    private String accTel2;
    @SerializedName("acc_whatsapp")
    @Expose
    private String accWhatsapp;
    @SerializedName("acc_website")
    @Expose
    private String accWebsite;
    @SerializedName("acc_pan")
    @Expose
    private String accPan;
    @SerializedName("acc_contact_person")
    @Expose
    private String accContactPerson;
    @SerializedName("acc_mobile")
    @Expose
    private String accMobile;

    public String getAccBankName() {
        return accBankName;
    }

    public void setAccBankName(String accBankName) {
        this.accBankName = accBankName;
    }

    public String getAccBankAccName() {
        return accBankAccName;
    }

    public void setAccBankAccName(String accBankAccName) {
        this.accBankAccName = accBankAccName;
    }

    public String getAccBankAccNo() {
        return accBankAccNo;
    }

    public void setAccBankAccNo(String accBankAccNo) {
        this.accBankAccNo = accBankAccNo;
    }

    public String getAccBankBranchName() {
        return accBankBranchName;
    }

    public void setAccBankBranchName(String accBankBranchName) {
        this.accBankBranchName = accBankBranchName;
    }

    public String getAccBankIfscCode() {
        return accBankIfscCode;
    }

    public void setAccBankIfscCode(String accBankIfscCode) {
        this.accBankIfscCode = accBankIfscCode;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccTypeName() {
        return accTypeName;
    }

    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
    }

    public String getAccGst() {
        return accGst;
    }

    public void setAccGst(String accGst) {
        this.accGst = accGst;
    }

    public String getAccAddress() {
        return accAddress;
    }

    public void setAccAddress(String accAddress) {
        this.accAddress = accAddress;
    }

    public String getAccCityId() {
        return accCityId;
    }

    public void setAccCityId(String accCityId) {
        this.accCityId = accCityId;
    }

    public String getAccStateId() {
        return accStateId;
    }

    public void setAccStateId(String accStateId) {
        this.accStateId = accStateId;
    }

    public String getAccCountryId() {
        return accCountryId;
    }

    public void setAccCountryId(String accCountryId) {
        this.accCountryId = accCountryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAccPinCode() {
        return accPinCode;
    }

    public void setAccPinCode(String accPinCode) {
        this.accPinCode = accPinCode;
    }

    public String getAccTel1() {
        return accTel1;
    }

    public void setAccTel1(String accTel1) {
        this.accTel1 = accTel1;
    }

    public String getAccTel2() {
        return accTel2;
    }

    public void setAccTel2(String accTel2) {
        this.accTel2 = accTel2;
    }

    public String getAccWhatsapp() {
        return accWhatsapp;
    }

    public void setAccWhatsapp(String accWhatsapp) {
        this.accWhatsapp = accWhatsapp;
    }

    public String getAccWebsite() {
        return accWebsite;
    }

    public void setAccWebsite(String accWebsite) {
        this.accWebsite = accWebsite;
    }

    public String getAccPan() {
        return accPan;
    }

    public void setAccPan(String accPan) {
        this.accPan = accPan;
    }

    public String getAccContactPerson() {
        return accContactPerson;
    }

    public void setAccContactPerson(String accContactPerson) {
        this.accContactPerson = accContactPerson;
    }

    public String getAccMobile() {
        return accMobile;
    }

    public void setAccMobile(String accMobile) {
        this.accMobile = accMobile;
    }
    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}
