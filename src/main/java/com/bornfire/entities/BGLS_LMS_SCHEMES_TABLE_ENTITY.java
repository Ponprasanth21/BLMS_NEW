package com.bornfire.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BGLS_LMS_SCHEMES_TABLE")
public class BGLS_LMS_SCHEMES_TABLE_ENTITY {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "PRODUCT_CATEGORY")
    private String productCategory;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "STATE")
    private String state;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_AVAILABILITY")
    private String productAvailability;

    @Column(name = "AVAILABLE_TO")
    private String availableTo;

    @Column(name = "BRANCHES")
    private String branches;

    @Column(name = "ID_TYPE")
    private String idType;

    @Column(name = "INITIAL_ACCOUNT_STATE")
    private String initialAccountState;

    @Column(name = "LOAN_AMOUNT_CONSTRAINTS")
    private String loanAmountConstraints;

    @Column(name = "ACCOUNTS_MANAGED_UNDER_CREDIT")
    private String accountsManagedUnderCredit;

    @Column(name = "INTEREST_CALC_METHOD")
    private String interestCalcMethod;

    @Column(name = "INTEREST_TYPE")
    private String interestType;

    @Column(name = "ACCRUED_INT_AFTER_MATURITY")
    private String accruedIntAfterMaturity;

    @Column(name = "INTEREST_RATE_CHARGED")
    private String interestRateCharged;

    @Column(name = "INTEREST_RATE_CONSTRAINTS")
    private BigDecimal interestRateConstraints;

    @Column(name = "DAYS_IN_YEAR")
    private Integer daysInYear;

    @Column(name = "PAYMENT_INTERVAL_METHOD")
    private String paymentIntervalMethod;

    @Column(name = "INSTALLMENT_CONSTRAINTS")
    private BigDecimal installmentConstraints;

    @Column(name = "FIRST_DUE_DATE_OFFSET")
    private Integer firstDueDateOffset;

    @Column(name = "COLLECT_PRINCIPAL_EVERY")
    private Integer collectPrincipalEvery;

    @Column(name = "GRACE_PERIOD")
    private String gracePeriod;

    @Column(name = "ROUND_OFF_REPAY_SCHEDULE")
    private String roundOffRepaySchedule;

    @Column(name = "REPAYMENT_SCHEDULE_EDIT")
    private String repaymentScheduleEdit;

    @Column(name = "PREPAYMENT_ACCEPTANCE")
    private String prepaymentAcceptance;

    @Column(name = "ACCEPT_PREPAY_FUTURE_INT")
    private String acceptPrepayFutureInt;

    @Column(name = "REPAYMENT_ALLOCATION_ORDER")
    private String repaymentAllocationOrder;

    @Column(name = "ARREARS_TOLERANCE_PERIOD")
    private Integer arrearsTolerancePeriod;

    @Column(name = "ARREARS_DAY_CALC_FROM")
    private String arrearsDayCalcFrom;

    @Column(name = "ARREARS_TOLERANCE_AMT")
    private BigDecimal arrearsToleranceAmt;

    @Column(name = "WITH_A_FLOOR")
    private BigDecimal withAFloor;

    @Column(name = "PENALTY_CALC_METHOD")
    private String penaltyCalcMethod;

    @Column(name = "PENALTY_TOLERANCE_PERIOD")
    private Integer penaltyTolerancePeriod;

    @Column(name = "PENALTY_RATE_CONSTRAINTS")
    private BigDecimal penaltyRateConstraints;

    @Column(name = "CLOSE_DORMANT_ACCOUNTS")
    private String closeDormantAccounts;

    @Column(name = "LOCK_ARREARS_ACCOUNT")
    private String lockArrearsAccount;

    @Column(name = "CAP_CHARGES")
    private String capCharges;

    @Column(name = "ALLOW_ARBITARY_FEES")
    private String allowArbitraryFees;

    @Column(name = "FEE1_NAME")
    private String fee1Name;

    @Column(name = "FEE1_ID_TYPE")
    private String fee1IdType;

    @Column(name = "FEE1_PAYMENT")
    private String fee1Payment;

    @Column(name = "FEE1_AMORT_PROFILE")
    private String fee1AmortProfile;

    @Column(name = "FEE1_TYPE")
    private String fee1Type;

    @Column(name = "FEE1_AMOUNT")
    private BigDecimal fee1Amount;

    @Column(name = "FEE2_NAME")
    private String fee2Name;

    @Column(name = "FEE2_ID_TYPE")
    private String fee2IdType;

    @Column(name = "FEE2_PAYMENT")
    private String fee2Payment;

    @Column(name = "FEE2_AMORT_PROFILE")
    private String fee2AmortProfile;

    @Column(name = "FEE2_TYPE")
    private String fee2Type;

    @Column(name = "FEE2_AMOUNT")
    private BigDecimal fee2Amount;

    @Column(name = "FEE3_NAME")
    private String fee3Name;

    @Column(name = "FEE3_ID_TYPE")
    private String fee3IdType;

    @Column(name = "FEE3_PAYMENT")
    private String fee3Payment;

    @Column(name = "FEE3_AMORT_PROFILE")
    private String fee3AmortProfile;

    @Column(name = "FEE3_APPLICATION")
    private String fee3Application;

    @Column(name = "FEE3_TYPE")
    private String fee3Type;

    @Column(name = "FEE3_AMOUNT")
    private BigDecimal fee3Amount;

    @Column(name = "FEE4_NAME")
    private String fee4Name;

    @Column(name = "FEE4_ID_TYPE")
    private String fee4IdType;

    @Column(name = "FEE4_PAYMENT")
    private String fee4Payment;

    @Column(name = "FEE4_AMORT_PROFILE")
    private String fee4AmortProfile;

    @Column(name = "FEE4_TYPE")
    private String fee4Type;

    @Column(name = "FEE4_AMOUNT")
    private BigDecimal fee4Amount;

    @Column(name = "FEE5_NAME")
    private String fee5Name;

    @Column(name = "FEE5_ID_TYPE")
    private String fee5IdType;

    @Column(name = "FEE5_PAYMENT")
    private String fee5Payment;

    @Column(name = "FEE5_AMORT_PROFILE")
    private String fee5AmortProfile;

    @Column(name = "FEE5_APPLICATION")
    private String fee5Application;

    @Column(name = "FEE5_TYPE")
    private String fee5Type;

    @Column(name = "FEE5_AMOUNT")
    private BigDecimal fee5Amount;

    @Column(name = "FEE6_NAME")
    private String fee6Name;

    @Column(name = "FEE6_ID_TYPE")
    private String fee6IdType;

    @Column(name = "FEE6_PAYMENT")
    private String fee6Payment;

    @Column(name = "FEE6_AMORT_PROFILE")
    private String fee6AmortProfile;

    @Column(name = "FEE6_TYPE")
    private String fee6Type;

    @Column(name = "FEE6_AMOUNT")
    private BigDecimal fee6Amount;

    @Column(name = "ENABLE_LINKING")
    private String enableLinking;

    @Column(name = "LINKED_DEPOSIT_PRODUCT")
    private String linkedDepositProduct;

    @Column(name = "SETTLEMENT_OPTIONS")
    private String settlementOptions;

    @Column(name = "ENABLE_GUARANTORS")
    private String enableGuarantors;

    @Column(name = "ENABLE_COLLATERALS")
    private String enableCollaterals;

    @Column(name = "REQUIRED_SECURITIES")
    private String requiredSecurities;

    @Column(name = "USING_TEMPLATE")
    private String usingTemplate;

    @Column(name = "ACCRUED_INT_POST_FREQ")
    private String accruedIntPostFreq;

    @Column(name = "REPAYMENT_INT_CALC")
    private String repaymentIntCalc;

    @Column(name = "REPAYMENT_MADE_EVERY")
    private Integer repaymentMadeEvery;

    @Column(name = "ROUND_OFF_REPAY_CURRENCY")
    private String roundOffRepayCurrency;

    @Column(name = "NON_WORKING_DAYS_RESCHED")
    private String nonWorkingDaysResched;

    @Column(name = "NON_WORKING_DAYS_ARREARS")
    private String nonWorkingDaysArrears;

    @Column(name = "PENALTY_RATE_CHANGE")
    private String penaltyRateChange;

    @Column(name = "DEPOSIT_ACCOUNT_OPTION")
    private String depositAccountOption;
    
    @Column(name = "ADJUST_PAYMENT_DATES", length = 5)
    private String adjustPaymentDates = "N";

    @Column(name = "ADJUST_PRINCIPAL_SCHEDULE", length = 5)
    private String adjustPrincipalSchedule = "N";

    @Column(name = "ADJUST_INTEREST_SCHEDULE", length = 5)
    private String adjustInterestSchedule = "N";

    @Column(name = "ADJUST_FEE_SCHEDULE", length = 5)
    private String adjustFeeSchedule = "N";

    @Column(name = "ADJUST_PENALTY_SCHEDULE", length = 5)
    private String adjustPenaltySchedule = "N";

    @Column(name = "CONFIGURE_PAYMENT_HOLIDAYS", length = 5)
    private String configurePaymentHolidays = "N";
    
    @Column(name = "AUTO_SET_SETTLEMENT_ACCT", length = 5)
    private String autoSetSettlementAcct = "N";
    
    @Column(name = "AUTO_CREATE_SETTLEMENT_ACCT", length = 5)
    private String autoCreateSettlementAcct = "N";
    
    

    @Column(name = "ENTRY_USER")
    private String entryUser;

    @Column(name = "MODIFY_USER")
    private String modifyUser;

    @Column(name = "VERIFY_USER")
    private String verifyUser;

    @Column(name = "ENTRY_TIME")
    @Temporal(TemporalType.DATE)
    private Date entryTime;

    @Column(name = "VERIFY_TIME")
    @Temporal(TemporalType.DATE)
    private Date verifyTime;

    @Column(name = "DEL_FLG")
    private String delFlg;

    @Column(name = "ENTITIY_FLG")
    private String entityFlg;

    @Column(name = "MODIFY_FLG")
    private String modifyFlg;

    @Column(name = "VERIFY_FLG")
    private String verifyFlg;
    
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductAvailability() {
		return productAvailability;
	}

	public void setProductAvailability(String productAvailability) {
		this.productAvailability = productAvailability;
	}

	public String getAvailableTo() {
		return availableTo;
	}

	public void setAvailableTo(String availableTo) {
		this.availableTo = availableTo;
	}

	public String getBranches() {
		return branches;
	}

	public void setBranches(String branches) {
		this.branches = branches;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getInitialAccountState() {
		return initialAccountState;
	}

	public void setInitialAccountState(String initialAccountState) {
		this.initialAccountState = initialAccountState;
	}

	public String getLoanAmountConstraints() {
		return loanAmountConstraints;
	}

	public void setLoanAmountConstraints(String loanAmountConstraints) {
		this.loanAmountConstraints = loanAmountConstraints;
	}

	public String getAccountsManagedUnderCredit() {
		return accountsManagedUnderCredit;
	}

	public void setAccountsManagedUnderCredit(String accountsManagedUnderCredit) {
		this.accountsManagedUnderCredit = accountsManagedUnderCredit;
	}

	public String getInterestCalcMethod() {
		return interestCalcMethod;
	}

	public void setInterestCalcMethod(String interestCalcMethod) {
		this.interestCalcMethod = interestCalcMethod;
	}

	public String getInterestType() {
		return interestType;
	}

	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}

	public String getAccruedIntAfterMaturity() {
		return accruedIntAfterMaturity;
	}

	public void setAccruedIntAfterMaturity(String accruedIntAfterMaturity) {
		this.accruedIntAfterMaturity = accruedIntAfterMaturity;
	}

	public String getInterestRateCharged() {
		return interestRateCharged;
	}

	public void setInterestRateCharged(String interestRateCharged) {
		this.interestRateCharged = interestRateCharged;
	}

	public BigDecimal getInterestRateConstraints() {
		return interestRateConstraints;
	}

	public void setInterestRateConstraints(BigDecimal interestRateConstraints) {
		this.interestRateConstraints = interestRateConstraints;
	}

	public Integer getDaysInYear() {
		return daysInYear;
	}

	public void setDaysInYear(Integer daysInYear) {
		this.daysInYear = daysInYear;
	}

	public String getPaymentIntervalMethod() {
		return paymentIntervalMethod;
	}

	public void setPaymentIntervalMethod(String paymentIntervalMethod) {
		this.paymentIntervalMethod = paymentIntervalMethod;
	}

	public BigDecimal getInstallmentConstraints() {
		return installmentConstraints;
	}

	public void setInstallmentConstraints(BigDecimal installmentConstraints) {
		this.installmentConstraints = installmentConstraints;
	}

	public Integer getFirstDueDateOffset() {
		return firstDueDateOffset;
	}

	public void setFirstDueDateOffset(Integer firstDueDateOffset) {
		this.firstDueDateOffset = firstDueDateOffset;
	}

	public Integer getCollectPrincipalEvery() {
		return collectPrincipalEvery;
	}

	public void setCollectPrincipalEvery(Integer collectPrincipalEvery) {
		this.collectPrincipalEvery = collectPrincipalEvery;
	}

	
	public String getGracePeriod() {
		return gracePeriod;
	}

	public void setGracePeriod(String gracePeriod) {
		this.gracePeriod = gracePeriod;
	}

	public String getRoundOffRepaySchedule() {
		return roundOffRepaySchedule;
	}

	public void setRoundOffRepaySchedule(String roundOffRepaySchedule) {
		this.roundOffRepaySchedule = roundOffRepaySchedule;
	}

	public String getRepaymentScheduleEdit() {
		return repaymentScheduleEdit;
	}

	public void setRepaymentScheduleEdit(String repaymentScheduleEdit) {
		this.repaymentScheduleEdit = repaymentScheduleEdit;
	}

	public String getPrepaymentAcceptance() {
		return prepaymentAcceptance;
	}

	public void setPrepaymentAcceptance(String prepaymentAcceptance) {
		this.prepaymentAcceptance = prepaymentAcceptance;
	}

	public String getAcceptPrepayFutureInt() {
		return acceptPrepayFutureInt;
	}

	public void setAcceptPrepayFutureInt(String acceptPrepayFutureInt) {
		this.acceptPrepayFutureInt = acceptPrepayFutureInt;
	}

	public String getRepaymentAllocationOrder() {
		return repaymentAllocationOrder;
	}

	public void setRepaymentAllocationOrder(String repaymentAllocationOrder) {
		this.repaymentAllocationOrder = repaymentAllocationOrder;
	}

	public Integer getArrearsTolerancePeriod() {
		return arrearsTolerancePeriod;
	}

	public void setArrearsTolerancePeriod(Integer arrearsTolerancePeriod) {
		this.arrearsTolerancePeriod = arrearsTolerancePeriod;
	}

	public String getArrearsDayCalcFrom() {
		return arrearsDayCalcFrom;
	}

	public void setArrearsDayCalcFrom(String arrearsDayCalcFrom) {
		this.arrearsDayCalcFrom = arrearsDayCalcFrom;
	}

	public BigDecimal getArrearsToleranceAmt() {
		return arrearsToleranceAmt;
	}

	public void setArrearsToleranceAmt(BigDecimal arrearsToleranceAmt) {
		this.arrearsToleranceAmt = arrearsToleranceAmt;
	}

	public BigDecimal getWithAFloor() {
		return withAFloor;
	}

	public void setWithAFloor(BigDecimal withAFloor) {
		this.withAFloor = withAFloor;
	}

	public String getPenaltyCalcMethod() {
		return penaltyCalcMethod;
	}

	public void setPenaltyCalcMethod(String penaltyCalcMethod) {
		this.penaltyCalcMethod = penaltyCalcMethod;
	}

	public Integer getPenaltyTolerancePeriod() {
		return penaltyTolerancePeriod;
	}

	public void setPenaltyTolerancePeriod(Integer penaltyTolerancePeriod) {
		this.penaltyTolerancePeriod = penaltyTolerancePeriod;
	}

	public BigDecimal getPenaltyRateConstraints() {
		return penaltyRateConstraints;
	}

	public void setPenaltyRateConstraints(BigDecimal penaltyRateConstraints) {
		this.penaltyRateConstraints = penaltyRateConstraints;
	}

	public String getCloseDormantAccounts() {
		return closeDormantAccounts;
	}

	public void setCloseDormantAccounts(String closeDormantAccounts) {
		this.closeDormantAccounts = closeDormantAccounts;
	}

	public String getLockArrearsAccount() {
		return lockArrearsAccount;
	}

	public void setLockArrearsAccount(String lockArrearsAccount) {
		this.lockArrearsAccount = lockArrearsAccount;
	}

	public String getCapCharges() {
		return capCharges;
	}

	public void setCapCharges(String capCharges) {
		this.capCharges = capCharges;
	}

	public String getAllowArbitraryFees() {
		return allowArbitraryFees;
	}

	public void setAllowArbitraryFees(String allowArbitraryFees) {
		this.allowArbitraryFees = allowArbitraryFees;
	}

	public String getFee1Name() {
		return fee1Name;
	}

	public void setFee1Name(String fee1Name) {
		this.fee1Name = fee1Name;
	}

	public String getFee1IdType() {
		return fee1IdType;
	}

	public void setFee1IdType(String fee1IdType) {
		this.fee1IdType = fee1IdType;
	}

	public String getFee1Payment() {
		return fee1Payment;
	}

	public void setFee1Payment(String fee1Payment) {
		this.fee1Payment = fee1Payment;
	}

	public String getFee1AmortProfile() {
		return fee1AmortProfile;
	}

	public void setFee1AmortProfile(String fee1AmortProfile) {
		this.fee1AmortProfile = fee1AmortProfile;
	}

	public String getFee1Type() {
		return fee1Type;
	}

	public void setFee1Type(String fee1Type) {
		this.fee1Type = fee1Type;
	}

	public BigDecimal getFee1Amount() {
		return fee1Amount;
	}

	public void setFee1Amount(BigDecimal fee1Amount) {
		this.fee1Amount = fee1Amount;
	}

	public String getFee2Name() {
		return fee2Name;
	}

	public void setFee2Name(String fee2Name) {
		this.fee2Name = fee2Name;
	}

	public String getFee2IdType() {
		return fee2IdType;
	}

	public void setFee2IdType(String fee2IdType) {
		this.fee2IdType = fee2IdType;
	}

	public String getFee2Payment() {
		return fee2Payment;
	}

	public void setFee2Payment(String fee2Payment) {
		this.fee2Payment = fee2Payment;
	}

	public String getFee2AmortProfile() {
		return fee2AmortProfile;
	}

	public void setFee2AmortProfile(String fee2AmortProfile) {
		this.fee2AmortProfile = fee2AmortProfile;
	}

	public String getFee2Type() {
		return fee2Type;
	}

	public void setFee2Type(String fee2Type) {
		this.fee2Type = fee2Type;
	}

	public BigDecimal getFee2Amount() {
		return fee2Amount;
	}

	public void setFee2Amount(BigDecimal fee2Amount) {
		this.fee2Amount = fee2Amount;
	}

	public String getFee3Name() {
		return fee3Name;
	}

	public void setFee3Name(String fee3Name) {
		this.fee3Name = fee3Name;
	}

	public String getFee3IdType() {
		return fee3IdType;
	}

	public void setFee3IdType(String fee3IdType) {
		this.fee3IdType = fee3IdType;
	}

	public String getFee3Payment() {
		return fee3Payment;
	}

	public void setFee3Payment(String fee3Payment) {
		this.fee3Payment = fee3Payment;
	}

	public String getFee3AmortProfile() {
		return fee3AmortProfile;
	}

	public void setFee3AmortProfile(String fee3AmortProfile) {
		this.fee3AmortProfile = fee3AmortProfile;
	}

	public String getFee3Application() {
		return fee3Application;
	}

	public void setFee3Application(String fee3Application) {
		this.fee3Application = fee3Application;
	}

	public String getFee3Type() {
		return fee3Type;
	}

	public void setFee3Type(String fee3Type) {
		this.fee3Type = fee3Type;
	}

	public BigDecimal getFee3Amount() {
		return fee3Amount;
	}

	public void setFee3Amount(BigDecimal fee3Amount) {
		this.fee3Amount = fee3Amount;
	}

	public String getFee4Name() {
		return fee4Name;
	}

	public void setFee4Name(String fee4Name) {
		this.fee4Name = fee4Name;
	}

	public String getFee4IdType() {
		return fee4IdType;
	}

	public void setFee4IdType(String fee4IdType) {
		this.fee4IdType = fee4IdType;
	}

	public String getFee4Payment() {
		return fee4Payment;
	}

	public void setFee4Payment(String fee4Payment) {
		this.fee4Payment = fee4Payment;
	}

	public String getFee4AmortProfile() {
		return fee4AmortProfile;
	}

	public void setFee4AmortProfile(String fee4AmortProfile) {
		this.fee4AmortProfile = fee4AmortProfile;
	}

	public String getFee4Type() {
		return fee4Type;
	}

	public void setFee4Type(String fee4Type) {
		this.fee4Type = fee4Type;
	}

	public BigDecimal getFee4Amount() {
		return fee4Amount;
	}

	public void setFee4Amount(BigDecimal fee4Amount) {
		this.fee4Amount = fee4Amount;
	}

	public String getFee5Name() {
		return fee5Name;
	}

	public void setFee5Name(String fee5Name) {
		this.fee5Name = fee5Name;
	}

	public String getFee5IdType() {
		return fee5IdType;
	}

	public void setFee5IdType(String fee5IdType) {
		this.fee5IdType = fee5IdType;
	}

	public String getFee5Payment() {
		return fee5Payment;
	}

	public void setFee5Payment(String fee5Payment) {
		this.fee5Payment = fee5Payment;
	}

	public String getFee5AmortProfile() {
		return fee5AmortProfile;
	}

	public void setFee5AmortProfile(String fee5AmortProfile) {
		this.fee5AmortProfile = fee5AmortProfile;
	}

	public String getFee5Application() {
		return fee5Application;
	}

	public void setFee5Application(String fee5Application) {
		this.fee5Application = fee5Application;
	}

	public String getFee5Type() {
		return fee5Type;
	}

	public void setFee5Type(String fee5Type) {
		this.fee5Type = fee5Type;
	}

	public BigDecimal getFee5Amount() {
		return fee5Amount;
	}

	public void setFee5Amount(BigDecimal fee5Amount) {
		this.fee5Amount = fee5Amount;
	}

	public String getFee6Name() {
		return fee6Name;
	}

	public void setFee6Name(String fee6Name) {
		this.fee6Name = fee6Name;
	}

	public String getFee6IdType() {
		return fee6IdType;
	}

	public void setFee6IdType(String fee6IdType) {
		this.fee6IdType = fee6IdType;
	}

	public String getFee6Payment() {
		return fee6Payment;
	}

	public void setFee6Payment(String fee6Payment) {
		this.fee6Payment = fee6Payment;
	}

	public String getFee6AmortProfile() {
		return fee6AmortProfile;
	}

	public void setFee6AmortProfile(String fee6AmortProfile) {
		this.fee6AmortProfile = fee6AmortProfile;
	}

	public String getFee6Type() {
		return fee6Type;
	}

	public void setFee6Type(String fee6Type) {
		this.fee6Type = fee6Type;
	}

	public BigDecimal getFee6Amount() {
		return fee6Amount;
	}

	public void setFee6Amount(BigDecimal fee6Amount) {
		this.fee6Amount = fee6Amount;
	}

	public String getEnableLinking() {
		return enableLinking;
	}

	public void setEnableLinking(String enableLinking) {
		this.enableLinking = enableLinking;
	}

	public String getLinkedDepositProduct() {
		return linkedDepositProduct;
	}

	public void setLinkedDepositProduct(String linkedDepositProduct) {
		this.linkedDepositProduct = linkedDepositProduct;
	}

	public String getSettlementOptions() {
		return settlementOptions;
	}

	public void setSettlementOptions(String settlementOptions) {
		this.settlementOptions = settlementOptions;
	}

	public String getEnableGuarantors() {
		return enableGuarantors;
	}

	public void setEnableGuarantors(String enableGuarantors) {
		this.enableGuarantors = enableGuarantors;
	}

	public String getEnableCollaterals() {
		return enableCollaterals;
	}

	public void setEnableCollaterals(String enableCollaterals) {
		this.enableCollaterals = enableCollaterals;
	}

	public String getRequiredSecurities() {
		return requiredSecurities;
	}

	public void setRequiredSecurities(String requiredSecurities) {
		this.requiredSecurities = requiredSecurities;
	}

	public String getUsingTemplate() {
		return usingTemplate;
	}

	public void setUsingTemplate(String usingTemplate) {
		this.usingTemplate = usingTemplate;
	}

	public String getAccruedIntPostFreq() {
		return accruedIntPostFreq;
	}

	public void setAccruedIntPostFreq(String accruedIntPostFreq) {
		this.accruedIntPostFreq = accruedIntPostFreq;
	}

	public String getRepaymentIntCalc() {
		return repaymentIntCalc;
	}

	public void setRepaymentIntCalc(String repaymentIntCalc) {
		this.repaymentIntCalc = repaymentIntCalc;
	}

	public Integer getRepaymentMadeEvery() {
		return repaymentMadeEvery;
	}

	public void setRepaymentMadeEvery(Integer repaymentMadeEvery) {
		this.repaymentMadeEvery = repaymentMadeEvery;
	}

	public String getRoundOffRepayCurrency() {
		return roundOffRepayCurrency;
	}

	public void setRoundOffRepayCurrency(String roundOffRepayCurrency) {
		this.roundOffRepayCurrency = roundOffRepayCurrency;
	}

	public String getNonWorkingDaysResched() {
		return nonWorkingDaysResched;
	}

	public void setNonWorkingDaysResched(String nonWorkingDaysResched) {
		this.nonWorkingDaysResched = nonWorkingDaysResched;
	}

	public String getNonWorkingDaysArrears() {
		return nonWorkingDaysArrears;
	}

	public void setNonWorkingDaysArrears(String nonWorkingDaysArrears) {
		this.nonWorkingDaysArrears = nonWorkingDaysArrears;
	}

	

	public String getPenaltyRateChange() {
		return penaltyRateChange;
	}

	public void setPenaltyRateChange(String penaltyRateChange) {
		this.penaltyRateChange = penaltyRateChange;
	}

	public String getDepositAccountOption() {
		return depositAccountOption;
	}

	public void setDepositAccountOption(String depositAccountOption) {
		this.depositAccountOption = depositAccountOption;
	}
	
	

	public String getAdjustPaymentDates() {
		return adjustPaymentDates;
	}

	public void setAdjustPaymentDates(String adjustPaymentDates) {
		this.adjustPaymentDates = adjustPaymentDates;
	}

	public String getAdjustPrincipalSchedule() {
		return adjustPrincipalSchedule;
	}

	public void setAdjustPrincipalSchedule(String adjustPrincipalSchedule) {
		this.adjustPrincipalSchedule = adjustPrincipalSchedule;
	}

	public String getAdjustInterestSchedule() {
		return adjustInterestSchedule;
	}

	public void setAdjustInterestSchedule(String adjustInterestSchedule) {
		this.adjustInterestSchedule = adjustInterestSchedule;
	}

	public String getAdjustFeeSchedule() {
		return adjustFeeSchedule;
	}

	public void setAdjustFeeSchedule(String adjustFeeSchedule) {
		this.adjustFeeSchedule = adjustFeeSchedule;
	}

	public String getAdjustPenaltySchedule() {
		return adjustPenaltySchedule;
	}

	public void setAdjustPenaltySchedule(String adjustPenaltySchedule) {
		this.adjustPenaltySchedule = adjustPenaltySchedule;
	}

	public String getConfigurePaymentHolidays() {
		return configurePaymentHolidays;
	}

	public void setConfigurePaymentHolidays(String configurePaymentHolidays) {
		this.configurePaymentHolidays = configurePaymentHolidays;
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

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
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

	public String getAutoSetSettlementAcct() {
		return autoSetSettlementAcct;
	}

	public void setAutoSetSettlementAcct(String autoSetSettlementAcct) {
		this.autoSetSettlementAcct = autoSetSettlementAcct;
	}

	public String getAutoCreateSettlementAcct() {
		return autoCreateSettlementAcct;
	}

	public void setAutoCreateSettlementAcct(String autoCreateSettlementAcct) {
		this.autoCreateSettlementAcct = autoCreateSettlementAcct;
	}
	
	

	
    
}

