package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryAddrData {
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
}
