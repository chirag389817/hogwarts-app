package com.csp.hogwarts.net.requests;

import java.util.regex.Pattern;

public class VerifyOtpReq {
    public static final String URL = "/auth/verify-otp";
    public String verificationId;
    public String otp;

    public VerifyOtpReq(String verificationId, String otp) {
        this.verificationId = verificationId;
        this.otp = otp;
    }

    public boolean isValidOtp(){
        return Pattern.matches("^[0-9]{4}$", otp.trim());
    }
}
