package com.csp.hogwarts.net.requests;

import com.google.gson.annotations.Expose;

import java.util.regex.Pattern;

public class VerifyMobileReq {
    public static final String URL = "/auth/verify-mobile";
    public String mobile;
    public VerifyMobileReq(String mobile) {
        this.mobile = mobile;
    }
    public boolean isValid(){
        return Pattern.matches("^[0-9]{10}$", mobile.trim());
    }
}
