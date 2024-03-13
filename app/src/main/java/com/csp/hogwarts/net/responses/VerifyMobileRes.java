package com.csp.hogwarts.net.responses;

import com.google.gson.annotations.Expose;

public class VerifyMobileRes {
    public boolean otpSent;
    public String userId;
    public String verificationId;
}
