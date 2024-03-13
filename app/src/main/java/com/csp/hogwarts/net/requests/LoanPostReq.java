package com.csp.hogwarts.net.requests;

public class LoanPostReq {
    public static final String URL = "/loan";
    public String name;
    public String mobile;
    public boolean isAnonymous;

    public LoanPostReq(String name, String mobile, boolean isAnonymous) {
        this.name = name;
        this.mobile = mobile;
        this.isAnonymous = isAnonymous;
    }
}
