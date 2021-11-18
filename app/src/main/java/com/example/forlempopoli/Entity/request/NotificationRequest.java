package com.example.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationRequest {
    @SerializedName("notify_id")
    @Expose
    private String notifyId;
    @SerializedName("notify_message")
    @Expose
    private String notifyName;

    public NotificationRequest(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }
}
