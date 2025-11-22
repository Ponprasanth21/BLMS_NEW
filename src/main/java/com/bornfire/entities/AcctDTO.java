package com.bornfire.entities;

public class AcctDTO {
    private String acctNum;
    private String acctName;

    public AcctDTO(String acctNum, String acctName) {
        this.acctNum = acctNum;
        this.acctName = acctName;
    }

    public String getAcctNum() { return acctNum; }
    public String getAcctName() { return acctName; }
}
