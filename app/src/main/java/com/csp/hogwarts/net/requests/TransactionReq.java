package com.csp.hogwarts.net.requests;

import java.time.LocalDateTime;

public class TransactionReq {
    public static final String URL = "/transaction";
    public String loanId;
    public double amount;
    public String note;
    public LocalDateTime dateTime;
    public LocalDateTime postedOn;

    public TransactionReq(String loanId, double amount, String note, LocalDateTime dateTime, LocalDateTime postedOn) {
        this.loanId = loanId;
        this.amount = amount;
        this.note = note;
        this.dateTime = dateTime;
        this.postedOn=postedOn;
    }
}
