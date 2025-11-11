package com.bornfire.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BGLS_TRANSACTION_ACCOUNTS")

public class BglsTransactionAccountsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bgls_acc_seq")
	@javax.persistence.SequenceGenerator(name = "bgls_acc_seq", sequenceName = "BGLS_TRANSACTION_ACCOUNTS_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;


    @Column(name = "GL_CODE", length = 50)
    private String glCode;

    @Column(name = "GL_DESC", length = 100)
    private String glDesc;

    @Column(name = "GLSH_CODE", length = 100)
    private String glshCode;

    @Column(name = "GLSH_DESC", length = 100)
    private String glshDesc;

    @Column(name = "SCHM_CODE", length = 50)
    private String schmCode;

    @Column(name = "SCHM_DESC", length = 100)
    private String schmDesc;

    @Column(name = "INTEREST_INCOME", length = 100)
    private String interestIncome;

    @Column(name = "INTEREST_RECIVABLE", length = 100)
    private String interestReceivable;

    @Column(name = "FEES_INCOME", length = 100)
    private String feesIncome;

    @Column(name = "PENALTY_INCOME", length = 50)
    private String penaltyIncome;

    @Column(name = "COLLECTION_ACCOUNT", length = 50)
    private String collectionAccount;

    @Column(name = "LOAN_PARKING_ACCOUNT", length = 100)
    private String loanParkingAccount;

    @Column(name = "ENTRY_USER", length = 25)
    private String entryUser;

    @Column(name = "MODIFY_USER", length = 25)
    private String modifyUser;

    @Column(name = "VERIFY_USER", length = 25)
    private String verifyUser;

    @Column(name = "ENTRY_TIME")
    private java.util.Date entryTime;

    @Column(name = "VERIFY_TIME")
    private java.util.Date verifyTime;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "MODIFY_FLG", length = 1)
    private String modifyFlg;

    @Column(name = "VERIFY_FLG", length = 1)
    private String verifyFlg;

    @Column(name = "PRODUCT_KEY", length = 100)
    private String productKey;

    @Column(name = "ENTITY_FLG", length = 1)
    private String entityFlg;

	public Long getId() {
		return id;
	}

	public void Long(Long id) {
		this.id = id;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getGlDesc() {
		return glDesc;
	}

	public void setGlDesc(String glDesc) {
		this.glDesc = glDesc;
	}

	public String getGlshCode() {
		return glshCode;
	}

	public void setGlshCode(String glshCode) {
		this.glshCode = glshCode;
	}

	public String getGlshDesc() {
		return glshDesc;
	}

	public void setGlshDesc(String glshDesc) {
		this.glshDesc = glshDesc;
	}

	public String getSchmCode() {
		return schmCode;
	}

	public void setSchmCode(String schmCode) {
		this.schmCode = schmCode;
	}

	public String getSchmDesc() {
		return schmDesc;
	}

	public void setSchmDesc(String schmDesc) {
		this.schmDesc = schmDesc;
	}

	public String getInterestIncome() {
		return interestIncome;
	}

	public void setInterestIncome(String interestIncome) {
		this.interestIncome = interestIncome;
	}

	public String getInterestReceivable() {
		return interestReceivable;
	}

	public void setInterestReceivable(String interestReceivable) {
		this.interestReceivable = interestReceivable;
	}

	public String getFeesIncome() {
		return feesIncome;
	}

	public void setFeesIncome(String feesIncome) {
		this.feesIncome = feesIncome;
	}

	public String getPenaltyIncome() {
		return penaltyIncome;
	}

	public void setPenaltyIncome(String penaltyIncome) {
		this.penaltyIncome = penaltyIncome;
	}

	public String getCollectionAccount() {
		return collectionAccount;
	}

	public void setCollectionAccount(String collectionAccount) {
		this.collectionAccount = collectionAccount;
	}

	public String getLoanParkingAccount() {
		return loanParkingAccount;
	}

	public void setLoanParkingAccount(String loanParkingAccount) {
		this.loanParkingAccount = loanParkingAccount;
	}

	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public java.util.Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(java.util.Date entryTime) {
		this.entryTime = entryTime;
	}

	public java.util.Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(java.util.Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getModifyFlg() {
		return modifyFlg;
	}

	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}

	public String getVerifyFlg() {
		return verifyFlg;
	}

	public void setVerifyFlg(String verifyFlg) {
		this.verifyFlg = verifyFlg;
	}

	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}
    
    
}
