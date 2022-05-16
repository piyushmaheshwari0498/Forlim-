package com.fashion.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("acc_id")
    @Expose
    private String accId;

    @SerializedName("acc_type")
    @Expose
    private String accType;
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
    @SerializedName("acc_type_name")
    @Expose
    private String accTypeName;

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
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

    public String getAccTypeName() {
        return accTypeName;
    }

    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
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
