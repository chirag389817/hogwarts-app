package com.csp.hogwarts.net.responses;

import com.csp.hogwarts.db.model.Transaction;

import java.util.List;

public class LoanGetRes {
    public String loanId;
    public String name;
    public String mobile;
    public double balance;
    public List<Transaction> transactions;
}
