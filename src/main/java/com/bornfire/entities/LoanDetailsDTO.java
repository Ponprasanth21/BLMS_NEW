package com.bornfire.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

public class LoanDetailsDTO {
    private Long id;
    private String accountHolderName;

    public LoanDetailsDTO(Long id, String accountHolderName) {
        this.id = id;
        this.accountHolderName = accountHolderName;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

    
}
