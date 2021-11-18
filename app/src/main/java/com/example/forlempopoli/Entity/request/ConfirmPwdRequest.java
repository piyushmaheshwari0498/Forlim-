package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmPwdRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("new_password")
    @Expose
    private String newPassword;
    @SerializedName("cnew_password")
    @Expose
    private String cnewPassword;
    @SerializedName("acc_id")
    @Expose
    private String accId;

    public String getAPIACCESSKEY() {
        return aPIACCESSKEY;
    }

    public void setAPIACCESSKEY(String aPIACCESSKEY) {
        this.aPIACCESSKEY = aPIACCESSKEY;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCnewPassword() {
        return cnewPassword;
    }

    public void setCnewPassword(String cnewPassword) {
        this.cnewPassword = cnewPassword;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}
