package com.fashion.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("loginid")
    @Expose
    private String loginid;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("acc_id")
    @Expose
    private String accId;
    @SerializedName("acc_name")
    @Expose
    private String accName;
    @SerializedName("acc_contact_person")
    @Expose
    private String accContactPerson;
    @SerializedName("acc_grp_id")
    @Expose
    private String accGrpId;
    @SerializedName("acc_mobile")
    @Expose
    private String accMobile;
    @SerializedName("acc_country_id")
    @Expose
    private String accCountryId;
    @SerializedName("acc_state_id")
    @Expose
    private String accStateId;
    @SerializedName("acc_city_id")
    @Expose
    private String accCityId;
    @SerializedName("acc_gst")
    @Expose
    private String accGst;
    @SerializedName("acc_email")
    @Expose
    private String accEmail;
    @SerializedName("acc_refer_mobile")
    @Expose
    private String accReferMobile;
    @SerializedName("acc_type")
    @Expose
    private String accType;
    @SerializedName("acc_cr_days")
    @Expose
    private String accCrDays;
    @SerializedName("acc_cr_limit")
    @Expose
    private String accCrLimit;
    @SerializedName("acc_limit")
    @Expose
    private Integer accLimit;
    @SerializedName("acc_status")
    @Expose
    private String accStatus;
    @SerializedName("acc_status_name")
    @Expose
    private String accStatusName;
    @SerializedName("total_balance")
    @Expose
    private Integer totalBalance;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccContactPerson() {
        return accContactPerson;
    }

    public void setAccContactPerson(String accContactPerson) {
        this.accContactPerson = accContactPerson;
    }

    public String getAccGrpId() {
        return accGrpId;
    }

    public void setAccGrpId(String accGrpId) {
        this.accGrpId = accGrpId;
    }

    public String getAccMobile() {
        return accMobile;
    }

    public void setAccMobile(String accMobile) {
        this.accMobile = accMobile;
    }

    public String getAccCountryId() {
        return accCountryId;
    }

    public void setAccCountryId(String accCountryId) {
        this.accCountryId = accCountryId;
    }

    public String getAccStateId() {
        return accStateId;
    }

    public void setAccStateId(String accStateId) {
        this.accStateId = accStateId;
    }

    public String getAccCityId() {
        return accCityId;
    }

    public void setAccCityId(String accCityId) {
        this.accCityId = accCityId;
    }

    public String getAccGst() {
        return accGst;
    }

    public void setAccGst(String accGst) {
        this.accGst = accGst;
    }

    public String getAccEmail() {
        return accEmail;
    }

    public void setAccEmail(String accEmail) {
        this.accEmail = accEmail;
    }

    public String getAccReferMobile() {
        return accReferMobile;
    }

    public void setAccReferMobile(String accReferMobile) {
        this.accReferMobile = accReferMobile;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccCrDays() {
        return accCrDays;
    }

    public void setAccCrDays(String accCrDays) {
        this.accCrDays = accCrDays;
    }

    public String getAccCrLimit() {
        return accCrLimit;
    }

    public void setAccCrLimit(String accCrLimit) {
        this.accCrLimit = accCrLimit;
    }

    public Integer getAccLimit() {
        return accLimit;
    }

    public void setAccLimit(Integer accLimit) {
        this.accLimit = accLimit;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public String getAccStatusName() {
        return accStatusName;
    }

    public void setAccStatusName(String accStatusName) {
        this.accStatusName = accStatusName;
    }

    public Integer getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Integer totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
