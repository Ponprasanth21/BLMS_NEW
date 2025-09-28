package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ValueGenerationType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BGLS_LMS_SCHEMES")
public class BglsLmsSchemesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNIQUE_ID")
    private Long unique_id;
     
    @Override
	public String toString() {
		return "BglsLmsSchemesEntity [unique_id=" + unique_id + ", id=" + id + ", encodedKey=" + encodedKey
				+ ", activated=" + activated + ", creationDate=" + creationDate + ", defaultGracePeriod="
				+ defaultGracePeriod + ", defaultLoanAmount=" + defaultLoanAmount + ", defaultNumInstallments="
				+ defaultNumInstallments + ", defaultRepaymentPeriodCount=" + defaultRepaymentPeriodCount
				+ ", gracePeriodType=" + gracePeriodType + ", interestCalculationMethod=" + interestCalculationMethod
				+ ", interestType=" + interestType + ", repaymentScheduleMethod=" + repaymentScheduleMethod
				+ ", lastModifiedDate=" + lastModifiedDate + ", maxGracePeriod=" + maxGracePeriod + ", maxLoanAmount="
				+ maxLoanAmount + ", maxNumInstallments=" + maxNumInstallments + ", maxPenaltyRate=" + maxPenaltyRate
				+ ", minGracePeriod=" + minGracePeriod + ", minLoanAmount=" + minLoanAmount + ", minNumInstallments="
				+ minNumInstallments + ", minPenaltyRate=" + minPenaltyRate + ", defaultPenaltyRate="
				+ defaultPenaltyRate + ", productDescription=" + productDescription + ", productName=" + productName
				+ ", repaymentPeriodUnit=" + repaymentPeriodUnit + ", prepaymentAcceptance=" + prepaymentAcceptance
				+ ", interestApplicationMethod=" + interestApplicationMethod + ", allowArbitraryFees="
				+ allowArbitraryFees + ", defaultPrincipalRepaymentInterval=" + defaultPrincipalRepaymentInterval
				+ ", loanPenaltyCalculationMethod=" + loanPenaltyCalculationMethod + ", loanPenaltyGracePeriod="
				+ loanPenaltyGracePeriod + ", repaymentCurrencyRounding=" + repaymentCurrencyRounding
				+ ", roundingRepaymentScheduleMethod=" + roundingRepaymentScheduleMethod
				+ ", prepaymentRecalculationMethod=" + prepaymentRecalculationMethod
				+ ", principalPaidInstallmentStatus=" + principalPaidInstallmentStatus + ", daysInYear=" + daysInYear
				+ ", scheduleInterestDaysCountMethod=" + scheduleInterestDaysCountMethod + ", repaymentAllocationOrder="
				+ repaymentAllocationOrder + ", idGeneratorType=" + idGeneratorType + ", idPattern=" + idPattern
				+ ", accountingMethod=" + accountingMethod + ", accountLinkingEnabled=" + accountLinkingEnabled
				+ ", linkableSavingsProductKey=" + linkableSavingsProductKey + ", autoLinkAccounts=" + autoLinkAccounts
				+ ", autoCreateLinkedAccounts=" + autoCreateLinkedAccounts + ", repaymentScheduleEditOptions="
				+ repaymentScheduleEditOptions + ", scheduleDueDatesMethod=" + scheduleDueDatesMethod
				+ ", paymentMethod=" + paymentMethod + ", fixedDaysOfMonth=" + fixedDaysOfMonth
				+ ", shortMonthHandlingMethod=" + shortMonthHandlingMethod + ", taxesOnInterestEnabled="
				+ taxesOnInterestEnabled + ", taxSourceKey=" + taxSourceKey + ", taxCalculationMethod="
				+ taxCalculationMethod + ", futurePaymentsAcceptance=" + futurePaymentsAcceptance
				+ ", taxesOnFeesEnabled=" + taxesOnFeesEnabled + ", taxesOnPenaltyEnabled=" + taxesOnPenaltyEnabled
				+ ", applyInterestOnPrepaymentMethod=" + applyInterestOnPrepaymentMethod
				+ ", repaymentElementsRoundingMethod=" + repaymentElementsRoundingMethod
				+ ", elementsRecalculationMethod=" + elementsRecalculationMethod + ", dormancyPeriodDays="
				+ dormancyPeriodDays + ", lockPeriodDays=" + lockPeriodDays + ", cappingMethod=" + cappingMethod
				+ ", cappingConstraintType=" + cappingConstraintType + ", cappingPercentage=" + cappingPercentage
				+ ", cappingApplyAccruedChargesBeforeLocking=" + cappingApplyAccruedChargesBeforeLocking
				+ ", settlementOptions=" + settlementOptions + ", offsetPercentage=" + offsetPercentage
				+ ", accountInitialState=" + accountInitialState + ", maxNumberOfDisbursementTranches="
				+ maxNumberOfDisbursementTranches + ", latePaymentsRecalculationMethod="
				+ latePaymentsRecalculationMethod + ", amortizationMethod=" + amortizationMethod
				+ ", interestRateSettingsKey=" + interestRateSettingsKey + ", lineOfCreditRequirement="
				+ lineOfCreditRequirement + ", productSecuritySettingsKey=" + productSecuritySettingsKey
				+ ", defaultFirstRepaymentDueDateOffset=" + defaultFirstRepaymentDueDateOffset
				+ ", minFirstRepaymentDueDateOffset=" + minFirstRepaymentDueDateOffset
				+ ", maxFirstRepaymentDueDateOffset=" + maxFirstRepaymentDueDateOffset
				+ ", interestAccruedAccountingMethod=" + interestAccruedAccountingMethod + ", loanProductType="
				+ loanProductType + ", forIndividuals=" + forIndividuals + ", forPureGroups=" + forPureGroups
				+ ", forHybridGroups=" + forHybridGroups + ", forAllBranches=" + forAllBranches
				+ ", principalPaymentSettingsKey=" + principalPaymentSettingsKey + ", repaymentReschedulingMethod="
				+ repaymentReschedulingMethod + ", interestBalanceCalculationMethod=" + interestBalanceCalculationMethod
				+ ", arrearsSettingsKey=" + arrearsSettingsKey + ", redrawSettingsKey=" + redrawSettingsKey
				+ ", allowCustomRepaymentAllocation=" + allowCustomRepaymentAllocation + ", accrueLateInterest="
				+ accrueLateInterest + ", interestAccrualCalculation=" + interestAccrualCalculation + ", category="
				+ category + ", currencyCode=" + currencyCode + ", fourEyesPrincipleLoanApproval="
				+ fourEyesPrincipleLoanApproval + ", addition=" + addition + ", entryUser=" + entryUser
				+ ", modifyUser=" + modifyUser + ", verifyUser=" + verifyUser + ", entryTime=" + entryTime
				+ ", modifyTime=" + modifyTime + ", verifyTime=" + verifyTime + ", delFlg=" + delFlg + ", entityFlg="
				+ entityFlg + ", modifyFlg=" + modifyFlg + ", verifyFlg=" + verifyFlg + ", getEncodedKey()="
				+ getEncodedKey() + ", getId()=" + getId() + ", getActivated()=" + getActivated()
				+ ", getCreationDate()=" + getCreationDate() + ", getDefaultGracePeriod()=" + getDefaultGracePeriod()
				+ ", getDefaultLoanAmount()=" + getDefaultLoanAmount() + ", getDefaultNumInstallments()="
				+ getDefaultNumInstallments() + ", getDefaultRepaymentPeriodCount()=" + getDefaultRepaymentPeriodCount()
				+ ", getGracePeriodType()=" + getGracePeriodType() + ", getInterestCalculationMethod()="
				+ getInterestCalculationMethod() + ", getInterestType()=" + getInterestType()
				+ ", getRepaymentScheduleMethod()=" + getRepaymentScheduleMethod() + ", getLastModifiedDate()="
				+ getLastModifiedDate() + ", getMaxGracePeriod()=" + getMaxGracePeriod() + ", getMaxLoanAmount()="
				+ getMaxLoanAmount() + ", getMaxNumInstallments()=" + getMaxNumInstallments() + ", getMaxPenaltyRate()="
				+ getMaxPenaltyRate() + ", getMinGracePeriod()=" + getMinGracePeriod() + ", getMinLoanAmount()="
				+ getMinLoanAmount() + ", getMinNumInstallments()=" + getMinNumInstallments() + ", getMinPenaltyRate()="
				+ getMinPenaltyRate() + ", getDefaultPenaltyRate()=" + getDefaultPenaltyRate()
				+ ", getProductDescription()=" + getProductDescription() + ", getProductName()=" + getProductName()
				+ ", getRepaymentPeriodUnit()=" + getRepaymentPeriodUnit() + ", getPrepaymentAcceptance()="
				+ getPrepaymentAcceptance() + ", getInterestApplicationMethod()=" + getInterestApplicationMethod()
				+ ", getAllowArbitraryFees()=" + getAllowArbitraryFees() + ", getDefaultPrincipalRepaymentInterval()="
				+ getDefaultPrincipalRepaymentInterval() + ", getLoanPenaltyCalculationMethod()="
				+ getLoanPenaltyCalculationMethod() + ", getLoanPenaltyGracePeriod()=" + getLoanPenaltyGracePeriod()
				+ ", getRepaymentCurrencyRounding()=" + getRepaymentCurrencyRounding()
				+ ", getRoundingRepaymentScheduleMethod()=" + getRoundingRepaymentScheduleMethod()
				+ ", getPrepaymentRecalculationMethod()=" + getPrepaymentRecalculationMethod()
				+ ", getPrincipalPaidInstallmentStatus()=" + getPrincipalPaidInstallmentStatus() + ", getDaysInYear()="
				+ getDaysInYear() + ", getScheduleInterestDaysCountMethod()=" + getScheduleInterestDaysCountMethod()
				+ ", getRepaymentAllocationOrder()=" + getRepaymentAllocationOrder() + ", getIdGeneratorType()="
				+ getIdGeneratorType() + ", getIdPattern()=" + getIdPattern() + ", getAccountingMethod()="
				+ getAccountingMethod() + ", getAccountLinkingEnabled()=" + getAccountLinkingEnabled()
				+ ", getLinkableSavingsProductKey()=" + getLinkableSavingsProductKey() + ", getAutoLinkAccounts()="
				+ getAutoLinkAccounts() + ", getAutoCreateLinkedAccounts()=" + getAutoCreateLinkedAccounts()
				+ ", getRepaymentScheduleEditOptions()=" + getRepaymentScheduleEditOptions()
				+ ", getScheduleDueDatesMethod()=" + getScheduleDueDatesMethod() + ", getPaymentMethod()="
				+ getPaymentMethod() + ", getFixedDaysOfMonth()=" + getFixedDaysOfMonth()
				+ ", getShortMonthHandlingMethod()=" + getShortMonthHandlingMethod() + ", getTaxesOnInterestEnabled()="
				+ getTaxesOnInterestEnabled() + ", getTaxSourceKey()=" + getTaxSourceKey()
				+ ", getTaxCalculationMethod()=" + getTaxCalculationMethod() + ", getFuturePaymentsAcceptance()="
				+ getFuturePaymentsAcceptance() + ", getTaxesOnFeesEnabled()=" + getTaxesOnFeesEnabled()
				+ ", getTaxesOnPenaltyEnabled()=" + getTaxesOnPenaltyEnabled()
				+ ", getApplyInterestOnPrepaymentMethod()=" + getApplyInterestOnPrepaymentMethod()
				+ ", getRepaymentElementsRoundingMethod()=" + getRepaymentElementsRoundingMethod()
				+ ", getElementsRecalculationMethod()=" + getElementsRecalculationMethod()
				+ ", getDormancyPeriodDays()=" + getDormancyPeriodDays() + ", getLockPeriodDays()="
				+ getLockPeriodDays() + ", getCappingMethod()=" + getCappingMethod() + ", getCappingConstraintType()="
				+ getCappingConstraintType() + ", getCappingPercentage()=" + getCappingPercentage()
				+ ", getCappingApplyAccruedChargesBeforeLocking()=" + getCappingApplyAccruedChargesBeforeLocking()
				+ ", getSettlementOptions()=" + getSettlementOptions() + ", getOffsetPercentage()="
				+ getOffsetPercentage() + ", getAccountInitialState()=" + getAccountInitialState()
				+ ", getMaxNumberOfDisbursementTranches()=" + getMaxNumberOfDisbursementTranches()
				+ ", getLatePaymentsRecalculationMethod()=" + getLatePaymentsRecalculationMethod()
				+ ", getAmortizationMethod()=" + getAmortizationMethod() + ", getInterestRateSettingsKey()="
				+ getInterestRateSettingsKey() + ", getLineOfCreditRequirement()=" + getLineOfCreditRequirement()
				+ ", getProductSecuritySettingsKey()=" + getProductSecuritySettingsKey()
				+ ", getDefaultFirstRepaymentDueDateOffset()=" + getDefaultFirstRepaymentDueDateOffset()
				+ ", getMinFirstRepaymentDueDateOffset()=" + getMinFirstRepaymentDueDateOffset()
				+ ", getMaxFirstRepaymentDueDateOffset()=" + getMaxFirstRepaymentDueDateOffset()
				+ ", getInterestAccruedAccountingMethod()=" + getInterestAccruedAccountingMethod()
				+ ", getLoanProductType()=" + getLoanProductType() + ", getForIndividuals()=" + getForIndividuals()
				+ ", getForPureGroups()=" + getForPureGroups() + ", getForHybridGroups()=" + getForHybridGroups()
				+ ", getForAllBranches()=" + getForAllBranches() + ", getPrincipalPaymentSettingsKey()="
				+ getPrincipalPaymentSettingsKey() + ", getRepaymentReschedulingMethod()="
				+ getRepaymentReschedulingMethod() + ", getInterestBalanceCalculationMethod()="
				+ getInterestBalanceCalculationMethod() + ", getArrearsSettingsKey()=" + getArrearsSettingsKey()
				+ ", getRedrawSettingsKey()=" + getRedrawSettingsKey() + ", getAllowCustomRepaymentAllocation()="
				+ getAllowCustomRepaymentAllocation() + ", getAccrueLateInterest()=" + getAccrueLateInterest()
				+ ", getInterestAccrualCalculation()=" + getInterestAccrualCalculation() + ", getCategory()="
				+ getCategory() + ", getCurrencyCode()=" + getCurrencyCode() + ", getFourEyesPrincipleLoanApproval()="
				+ getFourEyesPrincipleLoanApproval() + ", getAddition()=" + getAddition() + ", getEntryUser()="
				+ getEntryUser() + ", getModifyUser()=" + getModifyUser() + ", getVerifyUser()=" + getVerifyUser()
				+ ", getEntryTime()=" + getEntryTime() + ", getModifyTime()=" + getModifyTime() + ", getVerifyTime()="
				+ getVerifyTime() + ", getDelFlg()=" + getDelFlg() + ", getEntityFlg()=" + getEntityFlg()
				+ ", getModifyFlg()=" + getModifyFlg() + ", getVerifyFlg()=" + getVerifyFlg() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	@Column(name = "ID")
    private String id;

    @Column(name = "ENCODEDKEY")
    private String encodedKey;
    
    @Column(name = "ACTIVATED")
    private String activated;

    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CREATIONDATE")
    private Date creationDate;

    @Column(name = "DEFAULTGRACEPERIOD")
    private Integer defaultGracePeriod;

    @Column(name = "DEFAULTLOANAMOUNT")
    private BigDecimal defaultLoanAmount;

    @Column(name = "DEFAULTNUMINSTALLMENTS")
    private Integer defaultNumInstallments;

    @Column(name = "DEFAULTREPAYMENTPERIODCOUNT")
    private Integer defaultRepaymentPeriodCount;

    @Column(name = "GRACEPERIODTYPE")
    private String gracePeriodType;

    @Column(name = "INTERESTCALCULATIONMETHOD")
    private String interestCalculationMethod;

    @Column(name = "INTERESTTYPE")
    private String interestType;

    @Column(name = "REPAYMENTSCHEDULEMETHOD")
    private String repaymentScheduleMethod;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "LASTMODIFIEDDATE")
    private Date lastModifiedDate;

    @Column(name = "MAXGRACEPERIOD")
    private Integer maxGracePeriod;

    @Column(name = "MAXLOANAMOUNT")
    private BigDecimal maxLoanAmount;

    @Column(name = "MAXNUMINSTALLMENTS")
    private Integer maxNumInstallments;

    @Column(name = "MAXPENALTYRATE")
    private BigDecimal maxPenaltyRate;

    @Column(name = "MINGRACEPERIOD")
    private Integer minGracePeriod;

    @Column(name = "MINLOANAMOUNT")
    private BigDecimal minLoanAmount;

    @Column(name = "MINNUMINSTALLMENTS")
    private Integer minNumInstallments;

    @Column(name = "MINPENALTYRATE")
    private BigDecimal minPenaltyRate;

    @Column(name = "DEFAULTPENALTYRATE")
    private BigDecimal defaultPenaltyRate;

    @Column(name = "PRODUCTDESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCTNAME")
    private String productName;

    @Column(name = "REPAYMENTPERIODUNIT")
    private String repaymentPeriodUnit;

    @Column(name = "PREPAYMENTACCEPTANCE")
    private String prepaymentAcceptance;

    @Column(name = "INTERESTAPPLICATIONMETHOD")
    private String interestApplicationMethod;

    @Column(name = "ALLOWARBITRARYFEES")
    private String allowArbitraryFees;

    @Column(name = "DEFAULTPRINCIPALREPAYMENTINTERVAL")
    private Integer defaultPrincipalRepaymentInterval;

    @Column(name = "LOANPENALTYCALCULATIONMETHOD")
    private String loanPenaltyCalculationMethod;

    @Column(name = "LOANPENALTYGRACEPERIOD")
    private Integer loanPenaltyGracePeriod;

    @Column(name = "REPAYMENTCURRENCYROUNDING")
    private String repaymentCurrencyRounding;

    @Column(name = "ROUNDINGREPAYMENTSCHEDULEMETHOD")
    private String roundingRepaymentScheduleMethod;

    @Column(name = "PREPAYMENTRECALCULATIONMETHOD")
    private String prepaymentRecalculationMethod;

    @Column(name = "PRINCIPALPAIDINSTALLMENTSTATUS")
    private String principalPaidInstallmentStatus;

    @Column(name = "DAYSINYEAR")
    private Integer daysInYear;

    @Column(name = "SCHEDULEINTERESTDAYSCOUNTMETHOD")
    private String scheduleInterestDaysCountMethod;

    @Column(name = "REPAYMENTALLOCATIONORDER")
    private String repaymentAllocationOrder;

    @Column(name = "IDGENERATORTYPE")
    private String idGeneratorType;

    @Column(name = "IDPATTERN")
    private String idPattern;

    @Column(name = "ACCOUNTINGMETHOD")
    private String accountingMethod;

    @Column(name = "ACCOUNTLINKINGENABLED")
    private String accountLinkingEnabled;

    @Column(name = "LINKABLESAVINGSPRODUCTKEY")
    private String linkableSavingsProductKey;

    @Column(name = "AUTOLINKACCOUNTS")
    private String autoLinkAccounts;

    @Column(name = "AUTOCREATELINKEDACCOUNTS")
    private String autoCreateLinkedAccounts;

    @Column(name = "REPAYMENTSCHEDULEEDITOPTIONS")
    private String repaymentScheduleEditOptions;

    @Column(name = "SCHEDULEDUEDATESMETHOD")
    private String scheduleDueDatesMethod;

    @Column(name = "PAYMENTMETHOD")
    private String paymentMethod;

    @Column(name = "FIXEDDAYSOFMONTH")
    private Integer fixedDaysOfMonth;

    @Column(name = "SHORTMONTHHANDLINGMETHOD")
    private String shortMonthHandlingMethod;

    @Column(name = "TAXESONINTERESTENABLED")
    private String taxesOnInterestEnabled;

    @Column(name = "TAXSOURCEKEY")
    private String taxSourceKey;

    @Column(name = "TAXCALCULATIONMETHOD")
    private String taxCalculationMethod;

    @Column(name = "FUTUREPAYMENTSACCEPTANCE")
    private String futurePaymentsAcceptance;

    @Column(name = "TAXESONFEESENABLED")
    private String taxesOnFeesEnabled;

    @Column(name = "TAXESONPENALTYENABLED")
    private String taxesOnPenaltyEnabled;

    @Column(name = "APPLYINTERESTONPREPAYMENTMETHOD")
    private String applyInterestOnPrepaymentMethod;

    @Column(name = "REPAYMENTELEMENTSROUNDINGMETHOD")
    private String repaymentElementsRoundingMethod;

    @Column(name = "ELEMENTSRECALCULATIONMETHOD")
    private String elementsRecalculationMethod;

    @Column(name = "DORMANCYPERIODDAYS")
    private Integer dormancyPeriodDays;

    @Column(name = "LOCKPERIODDAYS")
    private Integer lockPeriodDays;

    @Column(name = "CAPPINGMETHOD")
    private String cappingMethod;

    @Column(name = "CAPPINGCONSTRAINTTYPE")
    private String cappingConstraintType;

    @Column(name = "CAPPINGPERCENTAGE")
    private BigDecimal cappingPercentage;

    @Column(name = "CAPPINGAPPLYACCRUEDCHARGESBEFORELOCKING")
    private String cappingApplyAccruedChargesBeforeLocking;

    @Column(name = "SETTLEMENTOPTIONS")
    private String settlementOptions;

    @Column(name = "OFFSETPERCENTAGE")
    private BigDecimal offsetPercentage;

    @Column(name = "ACCOUNTINITIALSTATE")
    private String accountInitialState;

    @Column(name = "MAXNUMBEROFDISBURSEMENTTRANCHES")
    private Integer maxNumberOfDisbursementTranches;

    @Column(name = "LATEPAYMENTSRECALCULATIONMETHOD")
    private String latePaymentsRecalculationMethod;

    @Column(name = "AMORTIZATIONMETHOD")
    private String amortizationMethod;

    @Column(name = "INTERESTRATESETTINGSKEY")
    private String interestRateSettingsKey;

    @Column(name = "LINEOFCREDITREQUIREMENT")
    private String lineOfCreditRequirement;

    @Column(name = "PRODUCTSECURITYSETTINGSKEY")
    private String productSecuritySettingsKey;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DEFAULTFIRSTREPAYMENTDUEDATEOFFSET")
    private Date defaultFirstRepaymentDueDateOffset;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "MINFIRSTREPAYMENTDUEDATEOFFSET")
    private Date minFirstRepaymentDueDateOffset;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "MAXFIRSTREPAYMENTDUEDATEOFFSET")
    private Date maxFirstRepaymentDueDateOffset;

    @Column(name = "INTERESTACCRUEDACCOUNTINGMETHOD")
    private String interestAccruedAccountingMethod;

    @Column(name = "LOANPRODUCTTYPE")
    private String loanProductType;

    @Column(name = "FORINDIVIDUALS")
    private String forIndividuals;

    @Column(name = "FORPUREGROUPS")
    private String forPureGroups;

    @Column(name = "FORHYBRIDGROUPS")
    private String forHybridGroups;

    @Column(name = "FORALLBRANCHES")
    private String forAllBranches;

    @Column(name = "PRINCIPALPAYMENTSETTINGSKEY")
    private String principalPaymentSettingsKey;

    @Column(name = "REPAYMENTRESCHEDULINGMETHOD")
    private String repaymentReschedulingMethod;

    @Column(name = "INTERESTBALANCECALCULATIONMETHOD")
    private String interestBalanceCalculationMethod;

    @Column(name = "ARREARSSETTINGSKEY")
    private String arrearsSettingsKey;

    @Column(name = "REDRAWSETTINGSKEY")
    private String redrawSettingsKey;

    @Column(name = "ALLOWCUSTOMREPAYMENTALLOCATION")
    private String allowCustomRepaymentAllocation;

    @Column(name = "ACCRUELATEINTEREST")
    private String accrueLateInterest;

    @Column(name = "INTERESTACCRUALCALCULATION")
    private String interestAccrualCalculation;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CURRENCYCODE")
    private String currencyCode;

    @Column(name = "FOUREYESPRINCIPLELOANAPPROVAL")
    private String fourEyesPrincipleLoanApproval;

    @Column(name = "ADDITION")
    private String addition;

    @Column(name = "ENTRY_USER")
    private String entryUser;

    @Column(name = "MODIFY_USER")
    private String modifyUser;

    @Column(name = "VERIFY_USER")
    private String verifyUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENTRY_TIME")
    private Date entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VERIFY_TIME")
    private Date verifyTime;

    @Column(name = "DEL_FLG")
    private String delFlg;

    @Column(name = "ENTITIY_FLG")
    private String entityFlg;

    @Column(name = "MODIFY_FLG")
    private String modifyFlg;

    @Column(name = "VERIFY_FLG")
    private String verifyFlg;

	public String getEncodedKey() {
		return encodedKey;
	}

	public void setEncodedKey(String encodedKey) {
		this.encodedKey = encodedKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivated() {
		return activated;
	}

	public void setActivated(String activated) {
		this.activated = activated;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getDefaultGracePeriod() {
		return defaultGracePeriod;
	}

	public void setDefaultGracePeriod(Integer defaultGracePeriod) {
		this.defaultGracePeriod = defaultGracePeriod;
	}

	public BigDecimal getDefaultLoanAmount() {
		return defaultLoanAmount;
	}

	public void setDefaultLoanAmount(BigDecimal defaultLoanAmount) {
		this.defaultLoanAmount = defaultLoanAmount;
	}

	public Integer getDefaultNumInstallments() {
		return defaultNumInstallments;
	}

	public void setDefaultNumInstallments(Integer defaultNumInstallments) {
		this.defaultNumInstallments = defaultNumInstallments;
	}

	public Integer getDefaultRepaymentPeriodCount() {
		return defaultRepaymentPeriodCount;
	}

	public void setDefaultRepaymentPeriodCount(Integer defaultRepaymentPeriodCount) {
		this.defaultRepaymentPeriodCount = defaultRepaymentPeriodCount;
	}

	public String getGracePeriodType() {
		return gracePeriodType;
	}

	public void setGracePeriodType(String gracePeriodType) {
		this.gracePeriodType = gracePeriodType;
	}

	public String getInterestCalculationMethod() {
		return interestCalculationMethod;
	}

	public void setInterestCalculationMethod(String interestCalculationMethod) {
		this.interestCalculationMethod = interestCalculationMethod;
	}

	public String getInterestType() {
		return interestType;
	}

	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}

	public String getRepaymentScheduleMethod() {
		return repaymentScheduleMethod;
	}

	public void setRepaymentScheduleMethod(String repaymentScheduleMethod) {
		this.repaymentScheduleMethod = repaymentScheduleMethod;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Integer getMaxGracePeriod() {
		return maxGracePeriod;
	}

	public void setMaxGracePeriod(Integer maxGracePeriod) {
		this.maxGracePeriod = maxGracePeriod;
	}

	public BigDecimal getMaxLoanAmount() {
		return maxLoanAmount;
	}

	public void setMaxLoanAmount(BigDecimal maxLoanAmount) {
		this.maxLoanAmount = maxLoanAmount;
	}

	public Integer getMaxNumInstallments() {
		return maxNumInstallments;
	}

	public void setMaxNumInstallments(Integer maxNumInstallments) {
		this.maxNumInstallments = maxNumInstallments;
	}

	public BigDecimal getMaxPenaltyRate() {
		return maxPenaltyRate;
	}

	public void setMaxPenaltyRate(BigDecimal maxPenaltyRate) {
		this.maxPenaltyRate = maxPenaltyRate;
	}

	public Integer getMinGracePeriod() {
		return minGracePeriod;
	}

	public void setMinGracePeriod(Integer minGracePeriod) {
		this.minGracePeriod = minGracePeriod;
	}

	public BigDecimal getMinLoanAmount() {
		return minLoanAmount;
	}

	public void setMinLoanAmount(BigDecimal minLoanAmount) {
		this.minLoanAmount = minLoanAmount;
	}

	public Integer getMinNumInstallments() {
		return minNumInstallments;
	}

	public void setMinNumInstallments(Integer minNumInstallments) {
		this.minNumInstallments = minNumInstallments;
	}

	public BigDecimal getMinPenaltyRate() {
		return minPenaltyRate;
	}

	public void setMinPenaltyRate(BigDecimal minPenaltyRate) {
		this.minPenaltyRate = minPenaltyRate;
	}

	public BigDecimal getDefaultPenaltyRate() {
		return defaultPenaltyRate;
	}

	public void setDefaultPenaltyRate(BigDecimal defaultPenaltyRate) {
		this.defaultPenaltyRate = defaultPenaltyRate;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRepaymentPeriodUnit() {
		return repaymentPeriodUnit;
	}

	public void setRepaymentPeriodUnit(String repaymentPeriodUnit) {
		this.repaymentPeriodUnit = repaymentPeriodUnit;
	}

	public String getPrepaymentAcceptance() {
		return prepaymentAcceptance;
	}

	public void setPrepaymentAcceptance(String prepaymentAcceptance) {
		this.prepaymentAcceptance = prepaymentAcceptance;
	}

	public String getInterestApplicationMethod() {
		return interestApplicationMethod;
	}

	public void setInterestApplicationMethod(String interestApplicationMethod) {
		this.interestApplicationMethod = interestApplicationMethod;
	}

	public String getAllowArbitraryFees() {
		return allowArbitraryFees;
	}

	public void setAllowArbitraryFees(String allowArbitraryFees) {
		this.allowArbitraryFees = allowArbitraryFees;
	}

	public Integer getDefaultPrincipalRepaymentInterval() {
		return defaultPrincipalRepaymentInterval;
	}

	public void setDefaultPrincipalRepaymentInterval(Integer defaultPrincipalRepaymentInterval) {
		this.defaultPrincipalRepaymentInterval = defaultPrincipalRepaymentInterval;
	}

	public String getLoanPenaltyCalculationMethod() {
		return loanPenaltyCalculationMethod;
	}

	public void setLoanPenaltyCalculationMethod(String loanPenaltyCalculationMethod) {
		this.loanPenaltyCalculationMethod = loanPenaltyCalculationMethod;
	}

	public Integer getLoanPenaltyGracePeriod() {
		return loanPenaltyGracePeriod;
	}

	public void setLoanPenaltyGracePeriod(Integer loanPenaltyGracePeriod) {
		this.loanPenaltyGracePeriod = loanPenaltyGracePeriod;
	}

	public String getRepaymentCurrencyRounding() {
		return repaymentCurrencyRounding;
	}

	public void setRepaymentCurrencyRounding(String repaymentCurrencyRounding) {
		this.repaymentCurrencyRounding = repaymentCurrencyRounding;
	}

	public String getRoundingRepaymentScheduleMethod() {
		return roundingRepaymentScheduleMethod;
	}

	public void setRoundingRepaymentScheduleMethod(String roundingRepaymentScheduleMethod) {
		this.roundingRepaymentScheduleMethod = roundingRepaymentScheduleMethod;
	}

	public String getPrepaymentRecalculationMethod() {
		return prepaymentRecalculationMethod;
	}

	public void setPrepaymentRecalculationMethod(String prepaymentRecalculationMethod) {
		this.prepaymentRecalculationMethod = prepaymentRecalculationMethod;
	}

	public String getPrincipalPaidInstallmentStatus() {
		return principalPaidInstallmentStatus;
	}

	public void setPrincipalPaidInstallmentStatus(String principalPaidInstallmentStatus) {
		this.principalPaidInstallmentStatus = principalPaidInstallmentStatus;
	}

	public Integer getDaysInYear() {
		return daysInYear;
	}

	public void setDaysInYear(Integer daysInYear) {
		this.daysInYear = daysInYear;
	}

	public String getScheduleInterestDaysCountMethod() {
		return scheduleInterestDaysCountMethod;
	}

	public void setScheduleInterestDaysCountMethod(String scheduleInterestDaysCountMethod) {
		this.scheduleInterestDaysCountMethod = scheduleInterestDaysCountMethod;
	}

	public String getRepaymentAllocationOrder() {
		return repaymentAllocationOrder;
	}

	public void setRepaymentAllocationOrder(String repaymentAllocationOrder) {
		this.repaymentAllocationOrder = repaymentAllocationOrder;
	}

	public String getIdGeneratorType() {
		return idGeneratorType;
	}

	public void setIdGeneratorType(String idGeneratorType) {
		this.idGeneratorType = idGeneratorType;
	}

	public String getIdPattern() {
		return idPattern;
	}

	public void setIdPattern(String idPattern) {
		this.idPattern = idPattern;
	}

	public String getAccountingMethod() {
		return accountingMethod;
	}

	public void setAccountingMethod(String accountingMethod) {
		this.accountingMethod = accountingMethod;
	}

	public String getAccountLinkingEnabled() {
		return accountLinkingEnabled;
	}

	public void setAccountLinkingEnabled(String accountLinkingEnabled) {
		this.accountLinkingEnabled = accountLinkingEnabled;
	}

	public String getLinkableSavingsProductKey() {
		return linkableSavingsProductKey;
	}

	public void setLinkableSavingsProductKey(String linkableSavingsProductKey) {
		this.linkableSavingsProductKey = linkableSavingsProductKey;
	}

	public String getAutoLinkAccounts() {
		return autoLinkAccounts;
	}

	public void setAutoLinkAccounts(String autoLinkAccounts) {
		this.autoLinkAccounts = autoLinkAccounts;
	}

	public String getAutoCreateLinkedAccounts() {
		return autoCreateLinkedAccounts;
	}

	public void setAutoCreateLinkedAccounts(String autoCreateLinkedAccounts) {
		this.autoCreateLinkedAccounts = autoCreateLinkedAccounts;
	}

	public String getRepaymentScheduleEditOptions() {
		return repaymentScheduleEditOptions;
	}

	public void setRepaymentScheduleEditOptions(String repaymentScheduleEditOptions) {
		this.repaymentScheduleEditOptions = repaymentScheduleEditOptions;
	}

	public String getScheduleDueDatesMethod() {
		return scheduleDueDatesMethod;
	}

	public void setScheduleDueDatesMethod(String scheduleDueDatesMethod) {
		this.scheduleDueDatesMethod = scheduleDueDatesMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getFixedDaysOfMonth() {
		return fixedDaysOfMonth;
	}

	public void setFixedDaysOfMonth(Integer fixedDaysOfMonth) {
		this.fixedDaysOfMonth = fixedDaysOfMonth;
	}

	public String getShortMonthHandlingMethod() {
		return shortMonthHandlingMethod;
	}

	public void setShortMonthHandlingMethod(String shortMonthHandlingMethod) {
		this.shortMonthHandlingMethod = shortMonthHandlingMethod;
	}

	public String getTaxesOnInterestEnabled() {
		return taxesOnInterestEnabled;
	}

	public void setTaxesOnInterestEnabled(String taxesOnInterestEnabled) {
		this.taxesOnInterestEnabled = taxesOnInterestEnabled;
	}

	public String getTaxSourceKey() {
		return taxSourceKey;
	}

	public void setTaxSourceKey(String taxSourceKey) {
		this.taxSourceKey = taxSourceKey;
	}

	public String getTaxCalculationMethod() {
		return taxCalculationMethod;
	}

	public void setTaxCalculationMethod(String taxCalculationMethod) {
		this.taxCalculationMethod = taxCalculationMethod;
	}

	public String getFuturePaymentsAcceptance() {
		return futurePaymentsAcceptance;
	}

	public void setFuturePaymentsAcceptance(String futurePaymentsAcceptance) {
		this.futurePaymentsAcceptance = futurePaymentsAcceptance;
	}

	public String getTaxesOnFeesEnabled() {
		return taxesOnFeesEnabled;
	}

	public void setTaxesOnFeesEnabled(String taxesOnFeesEnabled) {
		this.taxesOnFeesEnabled = taxesOnFeesEnabled;
	}

	public String getTaxesOnPenaltyEnabled() {
		return taxesOnPenaltyEnabled;
	}

	public void setTaxesOnPenaltyEnabled(String taxesOnPenaltyEnabled) {
		this.taxesOnPenaltyEnabled = taxesOnPenaltyEnabled;
	}

	public String getApplyInterestOnPrepaymentMethod() {
		return applyInterestOnPrepaymentMethod;
	}

	public void setApplyInterestOnPrepaymentMethod(String applyInterestOnPrepaymentMethod) {
		this.applyInterestOnPrepaymentMethod = applyInterestOnPrepaymentMethod;
	}

	public String getRepaymentElementsRoundingMethod() {
		return repaymentElementsRoundingMethod;
	}

	public void setRepaymentElementsRoundingMethod(String repaymentElementsRoundingMethod) {
		this.repaymentElementsRoundingMethod = repaymentElementsRoundingMethod;
	}

	public String getElementsRecalculationMethod() {
		return elementsRecalculationMethod;
	}

	public void setElementsRecalculationMethod(String elementsRecalculationMethod) {
		this.elementsRecalculationMethod = elementsRecalculationMethod;
	}

	public Integer getDormancyPeriodDays() {
		return dormancyPeriodDays;
	}

	public void setDormancyPeriodDays(Integer dormancyPeriodDays) {
		this.dormancyPeriodDays = dormancyPeriodDays;
	}

	public Integer getLockPeriodDays() {
		return lockPeriodDays;
	}

	public void setLockPeriodDays(Integer lockPeriodDays) {
		this.lockPeriodDays = lockPeriodDays;
	}

	public String getCappingMethod() {
		return cappingMethod;
	}

	public void setCappingMethod(String cappingMethod) {
		this.cappingMethod = cappingMethod;
	}

	public String getCappingConstraintType() {
		return cappingConstraintType;
	}

	public void setCappingConstraintType(String cappingConstraintType) {
		this.cappingConstraintType = cappingConstraintType;
	}

	public BigDecimal getCappingPercentage() {
		return cappingPercentage;
	}

	public void setCappingPercentage(BigDecimal cappingPercentage) {
		this.cappingPercentage = cappingPercentage;
	}

	public String getCappingApplyAccruedChargesBeforeLocking() {
		return cappingApplyAccruedChargesBeforeLocking;
	}

	public void setCappingApplyAccruedChargesBeforeLocking(String cappingApplyAccruedChargesBeforeLocking) {
		this.cappingApplyAccruedChargesBeforeLocking = cappingApplyAccruedChargesBeforeLocking;
	}

	public String getSettlementOptions() {
		return settlementOptions;
	}

	public void setSettlementOptions(String settlementOptions) {
		this.settlementOptions = settlementOptions;
	}

	public BigDecimal getOffsetPercentage() {
		return offsetPercentage;
	}

	public void setOffsetPercentage(BigDecimal offsetPercentage) {
		this.offsetPercentage = offsetPercentage;
	}

	public String getAccountInitialState() {
		return accountInitialState;
	}

	public void setAccountInitialState(String accountInitialState) {
		this.accountInitialState = accountInitialState;
	}

	public Integer getMaxNumberOfDisbursementTranches() {
		return maxNumberOfDisbursementTranches;
	}

	public void setMaxNumberOfDisbursementTranches(Integer maxNumberOfDisbursementTranches) {
		this.maxNumberOfDisbursementTranches = maxNumberOfDisbursementTranches;
	}

	public String getLatePaymentsRecalculationMethod() {
		return latePaymentsRecalculationMethod;
	}

	public void setLatePaymentsRecalculationMethod(String latePaymentsRecalculationMethod) {
		this.latePaymentsRecalculationMethod = latePaymentsRecalculationMethod;
	}

	public String getAmortizationMethod() {
		return amortizationMethod;
	}

	public void setAmortizationMethod(String amortizationMethod) {
		this.amortizationMethod = amortizationMethod;
	}

	public String getInterestRateSettingsKey() {
		return interestRateSettingsKey;
	}

	public void setInterestRateSettingsKey(String interestRateSettingsKey) {
		this.interestRateSettingsKey = interestRateSettingsKey;
	}

	public String getLineOfCreditRequirement() {
		return lineOfCreditRequirement;
	}

	public void setLineOfCreditRequirement(String lineOfCreditRequirement) {
		this.lineOfCreditRequirement = lineOfCreditRequirement;
	}

	public String getProductSecuritySettingsKey() {
		return productSecuritySettingsKey;
	}

	public void setProductSecuritySettingsKey(String productSecuritySettingsKey) {
		this.productSecuritySettingsKey = productSecuritySettingsKey;
	}

	public Date getDefaultFirstRepaymentDueDateOffset() {
		return defaultFirstRepaymentDueDateOffset;
	}

	public void setDefaultFirstRepaymentDueDateOffset(Date defaultFirstRepaymentDueDateOffset) {
		this.defaultFirstRepaymentDueDateOffset = defaultFirstRepaymentDueDateOffset;
	}

	public Date getMinFirstRepaymentDueDateOffset() {
		return minFirstRepaymentDueDateOffset;
	}

	public void setMinFirstRepaymentDueDateOffset(Date minFirstRepaymentDueDateOffset) {
		this.minFirstRepaymentDueDateOffset = minFirstRepaymentDueDateOffset;
	}

	public Date getMaxFirstRepaymentDueDateOffset() {
		return maxFirstRepaymentDueDateOffset;
	}

	public void setMaxFirstRepaymentDueDateOffset(Date maxFirstRepaymentDueDateOffset) {
		this.maxFirstRepaymentDueDateOffset = maxFirstRepaymentDueDateOffset;
	}

	public String getInterestAccruedAccountingMethod() {
		return interestAccruedAccountingMethod;
	}

	public void setInterestAccruedAccountingMethod(String interestAccruedAccountingMethod) {
		this.interestAccruedAccountingMethod = interestAccruedAccountingMethod;
	}

	public String getLoanProductType() {
		return loanProductType;
	}

	public void setLoanProductType(String loanProductType) {
		this.loanProductType = loanProductType;
	}

	public String getForIndividuals() {
		return forIndividuals;
	}

	public void setForIndividuals(String forIndividuals) {
		this.forIndividuals = forIndividuals;
	}

	public String getForPureGroups() {
		return forPureGroups;
	}

	public void setForPureGroups(String forPureGroups) {
		this.forPureGroups = forPureGroups;
	}

	public String getForHybridGroups() {
		return forHybridGroups;
	}

	public void setForHybridGroups(String forHybridGroups) {
		this.forHybridGroups = forHybridGroups;
	}

	public String getForAllBranches() {
		return forAllBranches;
	}

	public void setForAllBranches(String forAllBranches) {
		this.forAllBranches = forAllBranches;
	}

	public String getPrincipalPaymentSettingsKey() {
		return principalPaymentSettingsKey;
	}

	public void setPrincipalPaymentSettingsKey(String principalPaymentSettingsKey) {
		this.principalPaymentSettingsKey = principalPaymentSettingsKey;
	}

	public String getRepaymentReschedulingMethod() {
		return repaymentReschedulingMethod;
	}

	public void setRepaymentReschedulingMethod(String repaymentReschedulingMethod) {
		this.repaymentReschedulingMethod = repaymentReschedulingMethod;
	}

	public String getInterestBalanceCalculationMethod() {
		return interestBalanceCalculationMethod;
	}

	public void setInterestBalanceCalculationMethod(String interestBalanceCalculationMethod) {
		this.interestBalanceCalculationMethod = interestBalanceCalculationMethod;
	}

	public String getArrearsSettingsKey() {
		return arrearsSettingsKey;
	}

	public void setArrearsSettingsKey(String arrearsSettingsKey) {
		this.arrearsSettingsKey = arrearsSettingsKey;
	}

	public String getRedrawSettingsKey() {
		return redrawSettingsKey;
	}

	public void setRedrawSettingsKey(String redrawSettingsKey) {
		this.redrawSettingsKey = redrawSettingsKey;
	}

	public String getAllowCustomRepaymentAllocation() {
		return allowCustomRepaymentAllocation;
	}

	public void setAllowCustomRepaymentAllocation(String allowCustomRepaymentAllocation) {
		this.allowCustomRepaymentAllocation = allowCustomRepaymentAllocation;
	}

	public String getAccrueLateInterest() {
		return accrueLateInterest;
	}

	public void setAccrueLateInterest(String accrueLateInterest) {
		this.accrueLateInterest = accrueLateInterest;
	}

	public String getInterestAccrualCalculation() {
		return interestAccrualCalculation;
	}

	public void setInterestAccrualCalculation(String interestAccrualCalculation) {
		this.interestAccrualCalculation = interestAccrualCalculation;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getFourEyesPrincipleLoanApproval() {
		return fourEyesPrincipleLoanApproval;
	}

	public void setFourEyesPrincipleLoanApproval(String fourEyesPrincipleLoanApproval) {
		this.fourEyesPrincipleLoanApproval = fourEyesPrincipleLoanApproval;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
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

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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
    
    
}
