package com.csp.hogwarts.net.requests;

public class SignInReq {
    public static final String URL = "/auth/login";
    public String userId;
    public String password;

    public SignInReq(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
