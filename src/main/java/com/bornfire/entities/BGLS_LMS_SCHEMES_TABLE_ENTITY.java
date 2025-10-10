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

    @Column(name = "FEE1_APPLICATION")
    private String fee1Application;
    
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
    
    @Column(name = "FEE2_APPLICATION")
    private String fee2Application;

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
    
    @Column(name = "FEE4_APPLICATION")
    private String fee4Application;

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
    
    @Column(name = "FEE6_APPLICATION")
    private String fee6Application;
    
    @Column(name = "FEE7_NAME")
    private String fee7Name;

    @Column(name = "FEE7_ID_TYPE")
    private String fee7IdType;

    @Column(name = "FEE7_PAYMENT")
    private String fee7Payment;

    @Column(name = "FEE7_AMORT_PROFILE")
    private String fee7AmortProfile;

    @Column(name = "FEE7_APPLICATION")
    private String fee7Application;

    @Column(name = "FEE7_TYPE")
    private String fee7Type;

    @Column(name = "FEE7_AMOUNT")
    private BigDecimal fee7Amount;

    @Column(name = "FEE8_NAME")
    private String fee8Name;

    @Column(name = "FEE8_ID_TYPE")
    private String fee8IdType;

    @Column(name = "FEE8_PAYMENT")
    private String fee8Payment;

    @Column(name = "FEE8_AMORT_PROFILE")
    private String fee8AmortProfile;

    @Column(name = "FEE8_APPLICATION")
    private String fee8Application;

    @Column(name = "FEE8_TYPE")
    private String fee8Type;

    @Column(name = "FEE8_AMOUNT")
    private BigDecimal fee8Amount;

    @Column(name = "FEE9_NAME")
    private String fee9Name;

    @Column(name = "FEE9_ID_TYPE")
    private String fee9IdType;

    @Column(name = "FEE9_PAYMENT")
    private String fee9Payment;

    @Column(name = "FEE9_AMORT_PROFILE")
    private String fee9AmortProfile;

    @Column(name = "FEE9_APPLICATION")
    private String fee9Application;

    @Column(name = "FEE9_TYPE")
    private String fee9Type;

    @Column(name = "FEE9_AMOUNT")
    private BigDecimal fee9Amount;

    @Column(name = "FEE10_NAME")
    private String fee10Name;

    @Column(name = "FEE10_ID_TYPE")
    private String fee10IdType;

    @Column(name = "FEE10_PAYMENT")
    private String fee10Payment;

    @Column(name = "FEE10_AMORT_PROFILE")
    private String fee10AmortProfile;

    @Column(name = "FEE10_APPLICATION")
    private String fee10Application;

    @Column(name = "FEE10_TYPE")
    private String fee10Type;

    @Column(name = "FEE10_AMOUNT")
    private BigDecimal fee10Amount;

    @Column(name = "FEE11_NAME")
    private String fee11Name;

    @Column(name = "FEE11_ID_TYPE")
    private String fee11IdType;

    @Column(name = "FEE11_PAYMENT")
    private String fee11Payment;

    @Column(name = "FEE11_AMORT_PROFILE")
    private String fee11AmortProfile;

    @Column(name = "FEE11_APPLICATION")
    private String fee11Application;

    @Column(name = "FEE11_TYPE")
    private String fee11Type;

    @Column(name = "FEE11_AMOUNT")
    private BigDecimal fee11Amount;

    @Column(name = "FEE12_NAME")
    private String fee12Name;

    @Column(name = "FEE12_ID_TYPE")
    private String fee12IdType;

    @Column(name = "FEE12_PAYMENT")
    private String fee12Payment;

    @Column(name = "FEE12_AMORT_PROFILE")
    private String fee12AmortProfile;

    @Column(name = "FEE12_APPLICATION")
    private String fee12Application;

    @Column(name = "FEE12_TYPE")
    private String fee12Type;

    @Column(name = "FEE12_AMOUNT")
    private BigDecimal fee12Amount;

    @Column(name = "FEE13_NAME")
    private String fee13Name;

    @Column(name = "FEE13_ID_TYPE")
    private String fee13IdType;

    @Column(name = "FEE13_PAYMENT")
    private String fee13Payment;

    @Column(name = "FEE13_AMORT_PROFILE")
    private String fee13AmortProfile;

    @Column(name = "FEE13_APPLICATION")
    private String fee13Application;

    @Column(name = "FEE13_TYPE")
    private String fee13Type;

    @Column(name = "FEE13_AMOUNT")
    private BigDecimal fee13Amount;

    @Column(name = "FEE14_NAME")
    private String fee14Name;

    @Column(name = "FEE14_ID_TYPE")
    private String fee14IdType;

    @Column(name = "FEE14_PAYMENT")
    private String fee14Payment;

    @Column(name = "FEE14_AMORT_PROFILE")
    private String fee14AmortProfile;

    @Column(name = "FEE14_APPLICATION")
    private String fee14Application;

    @Column(name = "FEE14_TYPE")
    private String fee14Type;

    @Column(name = "FEE14_AMOUNT")
    private BigDecimal fee14Amount;

    @Column(name = "FEE15_NAME")
    private String fee15Name;

    @Column(name = "FEE15_ID_TYPE")
    private String fee15IdType;

    @Column(name = "FEE15_PAYMENT")
    private String fee15Payment;

    @Column(name = "FEE15_AMORT_PROFILE")
    private String fee15AmortProfile;

    @Column(name = "FEE15_APPLICATION")
    private String fee15Application;

    @Column(name = "FEE15_TYPE")
    private String fee15Type;

    @Column(name = "FEE15_AMOUNT")
    private BigDecimal fee15Amount;

    @Column(name = "FEE16_NAME")
    private String fee16Name;

    @Column(name = "FEE16_ID_TYPE")
    private String fee16IdType;

    @Column(name = "FEE16_PAYMENT")
    private String fee16Payment;

    @Column(name = "FEE16_AMORT_PROFILE")
    private String fee16AmortProfile;

    @Column(name = "FEE16_APPLICATION")
    private String fee16Application;

    @Column(name = "FEE16_TYPE")
    private String fee16Type;

    @Column(name = "FEE16_AMOUNT")
    private BigDecimal fee16Amount;
    
    @Column(name = "FEE17_NAME")
    private String fee17Name;

    @Column(name = "FEE17_ID_TYPE")
    private String fee17IdType;

    @Column(name = "FEE17_PAYMENT")
    private String fee17Payment;

    @Column(name = "FEE17_AMORT_PROFILE")
    private String fee17AmortProfile;

    @Column(name = "FEE17_APPLICATION")
    private String fee17Application;

    @Column(name = "FEE17_TYPE")
    private String fee17Type;

    @Column(name = "FEE17_AMOUNT")
    private BigDecimal fee17Amount;



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
	
	

	public String getFee1Application() {
		return fee1Application;
	}

	public void setFee1Application(String fee1Application) {
		this.fee1Application = fee1Application;
	}

	public String getFee2Application() {
		return fee2Application;
	}

	public void setFee2Application(String fee2Application) {
		this.fee2Application = fee2Application;
	}

	public String getFee4Application() {
		return fee4Application;
	}

	public void setFee4Application(String fee4Application) {
		this.fee4Application = fee4Application;
	}

	public String getFee6Application() {
		return fee6Application;
	}

	public void setFee6Application(String fee6Application) {
		this.fee6Application = fee6Application;
	}

	public String getFee7Name() {
		return fee7Name;
	}

	public void setFee7Name(String fee7Name) {
		this.fee7Name = fee7Name;
	}

	public String getFee7IdType() {
		return fee7IdType;
	}

	public void setFee7IdType(String fee7IdType) {
		this.fee7IdType = fee7IdType;
	}

	public String getFee7Payment() {
		return fee7Payment;
	}

	public void setFee7Payment(String fee7Payment) {
		this.fee7Payment = fee7Payment;
	}

	public String getFee7AmortProfile() {
		return fee7AmortProfile;
	}

	public void setFee7AmortProfile(String fee7AmortProfile) {
		this.fee7AmortProfile = fee7AmortProfile;
	}

	public String getFee7Application() {
		return fee7Application;
	}

	public void setFee7Application(String fee7Application) {
		this.fee7Application = fee7Application;
	}

	public String getFee7Type() {
		return fee7Type;
	}

	public void setFee7Type(String fee7Type) {
		this.fee7Type = fee7Type;
	}

	public BigDecimal getFee7Amount() {
		return fee7Amount;
	}

	public void setFee7Amount(BigDecimal fee7Amount) {
		this.fee7Amount = fee7Amount;
	}

	public String getFee8Name() {
		return fee8Name;
	}

	public void setFee8Name(String fee8Name) {
		this.fee8Name = fee8Name;
	}

	public String getFee8IdType() {
		return fee8IdType;
	}

	public void setFee8IdType(String fee8IdType) {
		this.fee8IdType = fee8IdType;
	}

	public String getFee8Payment() {
		return fee8Payment;
	}

	public void setFee8Payment(String fee8Payment) {
		this.fee8Payment = fee8Payment;
	}

	public String getFee8AmortProfile() {
		return fee8AmortProfile;
	}

	public void setFee8AmortProfile(String fee8AmortProfile) {
		this.fee8AmortProfile = fee8AmortProfile;
	}

	public String getFee8Application() {
		return fee8Application;
	}

	public void setFee8Application(String fee8Application) {
		this.fee8Application = fee8Application;
	}

	public String getFee8Type() {
		return fee8Type;
	}

	public void setFee8Type(String fee8Type) {
		this.fee8Type = fee8Type;
	}

	public BigDecimal getFee8Amount() {
		return fee8Amount;
	}

	public void setFee8Amount(BigDecimal fee8Amount) {
		this.fee8Amount = fee8Amount;
	}

	public String getFee9Name() {
		return fee9Name;
	}

	public void setFee9Name(String fee9Name) {
		this.fee9Name = fee9Name;
	}

	public String getFee9IdType() {
		return fee9IdType;
	}

	public void setFee9IdType(String fee9IdType) {
		this.fee9IdType = fee9IdType;
	}

	public String getFee9Payment() {
		return fee9Payment;
	}

	public void setFee9Payment(String fee9Payment) {
		this.fee9Payment = fee9Payment;
	}

	public String getFee9AmortProfile() {
		return fee9AmortProfile;
	}

	public void setFee9AmortProfile(String fee9AmortProfile) {
		this.fee9AmortProfile = fee9AmortProfile;
	}

	public String getFee9Application() {
		return fee9Application;
	}

	public void setFee9Application(String fee9Application) {
		this.fee9Application = fee9Application;
	}

	public String getFee9Type() {
		return fee9Type;
	}

	public void setFee9Type(String fee9Type) {
		this.fee9Type = fee9Type;
	}

	public BigDecimal getFee9Amount() {
		return fee9Amount;
	}

	public void setFee9Amount(BigDecimal fee9Amount) {
		this.fee9Amount = fee9Amount;
	}

	public String getFee10Name() {
		return fee10Name;
	}

	public void setFee10Name(String fee10Name) {
		this.fee10Name = fee10Name;
	}

	public String getFee10IdType() {
		return fee10IdType;
	}

	public void setFee10IdType(String fee10IdType) {
		this.fee10IdType = fee10IdType;
	}

	public String getFee10Payment() {
		return fee10Payment;
	}

	public void setFee10Payment(String fee10Payment) {
		this.fee10Payment = fee10Payment;
	}

	public String getFee10AmortProfile() {
		return fee10AmortProfile;
	}

	public void setFee10AmortProfile(String fee10AmortProfile) {
		this.fee10AmortProfile = fee10AmortProfile;
	}

	public String getFee10Application() {
		return fee10Application;
	}

	public void setFee10Application(String fee10Application) {
		this.fee10Application = fee10Application;
	}

	public String getFee10Type() {
		return fee10Type;
	}

	public void setFee10Type(String fee10Type) {
		this.fee10Type = fee10Type;
	}

	public BigDecimal getFee10Amount() {
		return fee10Amount;
	}

	public void setFee10Amount(BigDecimal fee10Amount) {
		this.fee10Amount = fee10Amount;
	}

	public String getFee11Name() {
		return fee11Name;
	}

	public void setFee11Name(String fee11Name) {
		this.fee11Name = fee11Name;
	}

	public String getFee11IdType() {
		return fee11IdType;
	}

	public void setFee11IdType(String fee11IdType) {
		this.fee11IdType = fee11IdType;
	}

	public String getFee11Payment() {
		return fee11Payment;
	}

	public void setFee11Payment(String fee11Payment) {
		this.fee11Payment = fee11Payment;
	}

	public String getFee11AmortProfile() {
		return fee11AmortProfile;
	}

	public void setFee11AmortProfile(String fee11AmortProfile) {
		this.fee11AmortProfile = fee11AmortProfile;
	}

	public String getFee11Application() {
		return fee11Application;
	}

	public void setFee11Application(String fee11Application) {
		this.fee11Application = fee11Application;
	}

	public String getFee11Type() {
		return fee11Type;
	}

	public void setFee11Type(String fee11Type) {
		this.fee11Type = fee11Type;
	}

	public BigDecimal getFee11Amount() {
		return fee11Amount;
	}

	public void setFee11Amount(BigDecimal fee11Amount) {
		this.fee11Amount = fee11Amount;
	}

	public String getFee12Name() {
		return fee12Name;
	}

	public void setFee12Name(String fee12Name) {
		this.fee12Name = fee12Name;
	}

	public String getFee12IdType() {
		return fee12IdType;
	}

	public void setFee12IdType(String fee12IdType) {
		this.fee12IdType = fee12IdType;
	}

	public String getFee12Payment() {
		return fee12Payment;
	}

	public void setFee12Payment(String fee12Payment) {
		this.fee12Payment = fee12Payment;
	}

	public String getFee12AmortProfile() {
		return fee12AmortProfile;
	}

	public void setFee12AmortProfile(String fee12AmortProfile) {
		this.fee12AmortProfile = fee12AmortProfile;
	}

	public String getFee12Application() {
		return fee12Application;
	}

	public void setFee12Application(String fee12Application) {
		this.fee12Application = fee12Application;
	}

	public String getFee12Type() {
		return fee12Type;
	}

	public void setFee12Type(String fee12Type) {
		this.fee12Type = fee12Type;
	}

	public BigDecimal getFee12Amount() {
		return fee12Amount;
	}

	public void setFee12Amount(BigDecimal fee12Amount) {
		this.fee12Amount = fee12Amount;
	}

	public String getFee13Name() {
		return fee13Name;
	}

	public void setFee13Name(String fee13Name) {
		this.fee13Name = fee13Name;
	}

	public String getFee13IdType() {
		return fee13IdType;
	}

	public void setFee13IdType(String fee13IdType) {
		this.fee13IdType = fee13IdType;
	}

	public String getFee13Payment() {
		return fee13Payment;
	}

	public void setFee13Payment(String fee13Payment) {
		this.fee13Payment = fee13Payment;
	}

	public String getFee13AmortProfile() {
		return fee13AmortProfile;
	}

	public void setFee13AmortProfile(String fee13AmortProfile) {
		this.fee13AmortProfile = fee13AmortProfile;
	}

	public String getFee13Application() {
		return fee13Application;
	}

	public void setFee13Application(String fee13Application) {
		this.fee13Application = fee13Application;
	}

	public String getFee13Type() {
		return fee13Type;
	}

	public void setFee13Type(String fee13Type) {
		this.fee13Type = fee13Type;
	}

	public BigDecimal getFee13Amount() {
		return fee13Amount;
	}

	public void setFee13Amount(BigDecimal fee13Amount) {
		this.fee13Amount = fee13Amount;
	}

	public String getFee14Name() {
		return fee14Name;
	}

	public void setFee14Name(String fee14Name) {
		this.fee14Name = fee14Name;
	}

	public String getFee14IdType() {
		return fee14IdType;
	}

	public void setFee14IdType(String fee14IdType) {
		this.fee14IdType = fee14IdType;
	}

	public String getFee14Payment() {
		return fee14Payment;
	}

	public void setFee14Payment(String fee14Payment) {
		this.fee14Payment = fee14Payment;
	}

	public String getFee14AmortProfile() {
		return fee14AmortProfile;
	}

	public void setFee14AmortProfile(String fee14AmortProfile) {
		this.fee14AmortProfile = fee14AmortProfile;
	}

	public String getFee14Application() {
		return fee14Application;
	}

	public void setFee14Application(String fee14Application) {
		this.fee14Application = fee14Application;
	}

	public String getFee14Type() {
		return fee14Type;
	}

	public void setFee14Type(String fee14Type) {
		this.fee14Type = fee14Type;
	}

	public BigDecimal getFee14Amount() {
		return fee14Amount;
	}

	public void setFee14Amount(BigDecimal fee14Amount) {
		this.fee14Amount = fee14Amount;
	}

	public String getFee15Name() {
		return fee15Name;
	}

	public void setFee15Name(String fee15Name) {
		this.fee15Name = fee15Name;
	}

	public String getFee15IdType() {
		return fee15IdType;
	}

	public void setFee15IdType(String fee15IdType) {
		this.fee15IdType = fee15IdType;
	}

	public String getFee15Payment() {
		return fee15Payment;
	}

	public void setFee15Payment(String fee15Payment) {
		this.fee15Payment = fee15Payment;
	}

	public String getFee15AmortProfile() {
		return fee15AmortProfile;
	}

	public void setFee15AmortProfile(String fee15AmortProfile) {
		this.fee15AmortProfile = fee15AmortProfile;
	}

	public String getFee15Application() {
		return fee15Application;
	}

	public void setFee15Application(String fee15Application) {
		this.fee15Application = fee15Application;
	}

	public String getFee15Type() {
		return fee15Type;
	}

	public void setFee15Type(String fee15Type) {
		this.fee15Type = fee15Type;
	}

	public BigDecimal getFee15Amount() {
		return fee15Amount;
	}

	public void setFee15Amount(BigDecimal fee15Amount) {
		this.fee15Amount = fee15Amount;
	}

	public String getFee16Name() {
		return fee16Name;
	}

	public void setFee16Name(String fee16Name) {
		this.fee16Name = fee16Name;
	}

	public String getFee16IdType() {
		return fee16IdType;
	}

	public void setFee16IdType(String fee16IdType) {
		this.fee16IdType = fee16IdType;
	}

	public String getFee16Payment() {
		return fee16Payment;
	}

	public void setFee16Payment(String fee16Payment) {
		this.fee16Payment = fee16Payment;
	}

	public String getFee16AmortProfile() {
		return fee16AmortProfile;
	}

	public void setFee16AmortProfile(String fee16AmortProfile) {
		this.fee16AmortProfile = fee16AmortProfile;
	}

	public String getFee16Application() {
		return fee16Application;
	}

	public void setFee16Application(String fee16Application) {
		this.fee16Application = fee16Application;
	}

	public String getFee16Type() {
		return fee16Type;
	}

	public void setFee16Type(String fee16Type) {
		this.fee16Type = fee16Type;
	}

	public BigDecimal getFee16Amount() {
		return fee16Amount;
	}

	public void setFee16Amount(BigDecimal fee16Amount) {
		this.fee16Amount = fee16Amount;
	}
	
	

	public String getFee17Name() {
		return fee17Name;
	}

	public void setFee17Name(String fee17Name) {
		this.fee17Name = fee17Name;
	}

	public String getFee17IdType() {
		return fee17IdType;
	}

	public void setFee17IdType(String fee17IdType) {
		this.fee17IdType = fee17IdType;
	}

	public String getFee17Payment() {
		return fee17Payment;
	}

	public void setFee17Payment(String fee17Payment) {
		this.fee17Payment = fee17Payment;
	}

	public String getFee17AmortProfile() {
		return fee17AmortProfile;
	}

	public void setFee17AmortProfile(String fee17AmortProfile) {
		this.fee17AmortProfile = fee17AmortProfile;
	}

	public String getFee17Application() {
		return fee17Application;
	}

	public void setFee17Application(String fee17Application) {
		this.fee17Application = fee17Application;
	}

	public String getFee17Type() {
		return fee17Type;
	}

	public void setFee17Type(String fee17Type) {
		this.fee17Type = fee17Type;
	}

	public BigDecimal getFee17Amount() {
		return fee17Amount;
	}

	public void setFee17Amount(BigDecimal fee17Amount) {
		this.fee17Amount = fee17Amount;
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

