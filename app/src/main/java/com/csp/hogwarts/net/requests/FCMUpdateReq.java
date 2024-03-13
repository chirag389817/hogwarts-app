package com.csp.hogwarts.net.requests;

import androidx.annotation.NonNull;

public class FCMUpdateReq {
    public static final String URL = "/user/update-fcm";
    public String  fcmToken;

    public FCMUpdateReq(@NonNull String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
