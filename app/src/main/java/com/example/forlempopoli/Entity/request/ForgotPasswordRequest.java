package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {
    @SerializedName("API_ACCESS_KEY")
    @Expose
    private String aPIACCESSKEY;
    @SerializedName("loginid")
    @Expose
    private String loginid;

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

}
