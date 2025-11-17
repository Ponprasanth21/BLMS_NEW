package com.bornfire.entities;

import javax.persistence.*;

@Entity
@Table(name = "LOAN_MASTER")
public class LoanMaster {

    @Id
    private Long id;

    private String loanNo;
    private String customerName;
    private Double amount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

    
}
