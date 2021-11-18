package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryAddressRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("acct_name")
    @Expose
    private String acctName;
    @SerializedName("acct_mob")
    @Expose
    private String acctMob;
    @SerializedName("acct_address")
    @Expose
    private String acctAddress;
    @SerializedName("acct_pin_code")
    @Expose
    private String acctPinCode;
    @SerializedName("acct_city_id")
    @Expose
    private String acctCityId;
    @SerializedName("acct_state_id")
    @Expose
    private String acctStateId;
    @SerializedName("acct_country_id")
    @Expose
    private String acctCountryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("acct_id")
    @Expose
    private String acctId;
    @SerializedName("acct_acc_id")
    @Expose
    private String acctAccId;

    @SerializedName("acct_del_status")
    @Expose
    private String default_address_status;

    public String isDefault_address_status() {
        return default_address_status;
    }

    public void setDefault_address_status(String default_address_status) {
        this.default_address_status = default_address_status;
    }

        private String isSelected;

    public String isSelected() {
        return isSelected;
    }

    public void setSelected(String selected) {
        isSelected = selected;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctMob() {
        return acctMob;
    }

    public void setAcctMob(String acctMob) {
        this.acctMob = acctMob;
    }

    public String getAcctAddress() {
        return acctAddress;
    }

    public void setAcctAddress(String acctAddress) {
        this.acctAddress = acctAddress;
    }

    public String getAcctPinCode() {
        return acctPinCode;
    }

    public void setAcctPinCode(String acctPinCode) {
        this.acctPinCode = acctPinCode;
    }

    public String getAcctCityId() {
        return acctCityId;
    }

    public void setAcctCityId(String acctCityId) {
        this.acctCityId = acctCityId;
    }

    public String getAcctStateId() {
        return acctStateId;
    }

    public void setAcctStateId(String acctStateId) {
        this.acctStateId = acctStateId;
    }

    public String getAcctCountryId() {
        return acctCountryId;
    }

    public void setAcctCountryId(String acctCountryId) {
        this.acctCountryId = acctCountryId;
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

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getAcctAccId() {
        return acctAccId;
    }

    public void setAcctAccId(String acctAccId) {
        this.acctAccId = acctAccId;
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

    public String getaPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setaPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    @Override
    public String toString() {
        return "DeliveryAddressRequest{" +
                "aPIACCESSKEY='" + aPIACCESSKEY + '\'' +
                ", accId='" + accId + '\'' +
                ", acctName='" + acctName + '\'' +
                ", acctMob='" + acctMob + '\'' +
                ", acctAddress='" + acctAddress + '\'' +
                ", acctPinCode='" + acctPinCode + '\'' +
                ", acctCityId='" + acctCityId + '\'' +
                ", acctStateId='" + acctStateId + '\'' +
                ", acctCountryId='" + acctCountryId + '\'' +
                ", countryName='" + countryName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", stateName='" + stateName + '\'' +
                ", acctId='" + acctId + '\'' +
                ", acctAccId='" + acctAccId + '\'' +
                ", default_address_status=" + default_address_status +
                '}';
    }
}
