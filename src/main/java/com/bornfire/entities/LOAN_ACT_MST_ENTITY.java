package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="LOAN_ACCOUNT_MASTER_TBL")
public class LOAN_ACT_MST_ENTITY {

	private String	encoded_key;
	@Id
	private String	id;
	private String	account_holdertype;
	private String	account_holderkey;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	creation_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	approved_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	last_modified_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	closed_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	last_account_appraisaldate;
	private String	account_state;
	private String	account_substate;
	private String	product_typekey;
	private String	loan_name;
	private String	payment_method;
	private String	assigned_branchkey;
	private BigDecimal	loan_amount;
	private BigDecimal	interest_rate;
	private BigDecimal	penalty_rate;
	private BigDecimal	accrued_interest;
	private BigDecimal	accrued_penalty;
	private BigDecimal	principal_due;
	private BigDecimal	principal_paid;
	private BigDecimal	principal_balance;
	private BigDecimal	interest_due;
	private BigDecimal	interest_paid;
	private BigDecimal	interest_balance;
	private BigDecimal	interest_fromarrearsbalance;
	private BigDecimal	interest_fromarrearsdue;
	private BigDecimal	interest_fromarrearspaid;
	private BigDecimal	fees_due;
	private BigDecimal	fees_paid;
	private BigDecimal	fees_balance;
	private BigDecimal	penalty_due;
	private BigDecimal	penalty_paid;
	private BigDecimal	penalty_balance;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	expected_disbursementdate;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	disbursement_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	first_repaymentdate;
	private BigDecimal	grace_period;
	private BigDecimal	repayment_installments;
	private BigDecimal	repayment_periodcount;
	private BigDecimal	days_late;
	private BigDecimal	days_inarrears;
	private String	repayment_schedule_method;
	private String	currency_code;
	private String	sale_processedbyvgid;
	private String	sale_processedfor;
	private String	sale_referredby;
	private String	employment_status;
	private String	job_title;
	private String	employer_name;
	private BigDecimal	tuscore;
	private BigDecimal	tuprobability;
	private String	tufullname;
	private String	tureason1;
	private String	tureason2;
	private String	tureason3;
	private String	tureason4;
	private BigDecimal	disposable_income;
	private BigDecimal	manualoverride_amount;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	manualoverride_expiry_date;
	private BigDecimal	cpfees;
	private BigDecimal	deposit_amount;
	private BigDecimal	total_product_price;
	private String	retailer_name;
	private String	retailer_branch;
	private String	vg_application_id;
	private String	contract_signed;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	date_of_first_call;
	private String	last_call_outcome;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	asondate;
	private String	disbursement_flg;
	private String	interest_flg;
	private String	fees_flg;
	private String	recovery_flg;
	private String	interest_demand_flg;
	private String	fees_demand_flg;
	private String	booking_flg;
	private String	entity_flg;
	private String	auth_flg;
	private String	modify_flg;
	private String	del_flg;
	private String	entry_user;
	private String	modify_user;
	private String	auth_user;
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	private Date	entry_time;
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	private Date	modify_time;
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	private Date	auth_time;
	
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getAuth_flg() {
		return auth_flg;
	}
	public void setAuth_flg(String auth_flg) {
		this.auth_flg = auth_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getAuth_user() {
		return auth_user;
	}
	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public Date getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(Date auth_time) {
		this.auth_time = auth_time;
	}
	public String getEncoded_key() {
		return encoded_key;
	}

	public void setEncoded_key(String encoded_key) {
		this.encoded_key = encoded_key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount_holdertype() {
		return account_holdertype;
	}

	public void setAccount_holdertype(String account_holdertype) {
		this.account_holdertype = account_holdertype;
	}

	public String getAccount_holderkey() {
		return account_holderkey;
	}

	public void setAccount_holderkey(String account_holderkey) {
		this.account_holderkey = account_holderkey;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Date getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(Date approved_date) {
		this.approved_date = approved_date;
	}

	public Date getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Date last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public Date getClosed_date() {
		return closed_date;
	}

	public void setClosed_date(Date closed_date) {
		this.closed_date = closed_date;
	}

	public Date getLast_account_appraisaldate() {
		return last_account_appraisaldate;
	}

	public void setLast_account_appraisaldate(Date last_account_appraisaldate) {
		this.last_account_appraisaldate = last_account_appraisaldate;
	}

	public String getAccount_state() {
		return account_state;
	}

	public void setAccount_state(String account_state) {
		this.account_state = account_state;
	}

	public String getAccount_substate() {
		return account_substate;
	}

	public void setAccount_substate(String account_substate) {
		this.account_substate = account_substate;
	}

	public String getProduct_typekey() {
		return product_typekey;
	}

	public void setProduct_typekey(String product_typekey) {
		this.product_typekey = product_typekey;
	}

	public String getLoan_name() {
		return loan_name;
	}

	public void setLoan_name(String loan_name) {
		this.loan_name = loan_name;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getAssigned_branchkey() {
		return assigned_branchkey;
	}

	public void setAssigned_branchkey(String assigned_branchkey) {
		this.assigned_branchkey = assigned_branchkey;
	}

	public BigDecimal getLoan_amount() {
		return loan_amount;
	}

	public void setLoan_amount(BigDecimal loan_amount) {
		this.loan_amount = loan_amount;
	}

	public BigDecimal getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(BigDecimal interest_rate) {
		this.interest_rate = interest_rate;
	}

	public BigDecimal getPenalty_rate() {
		return penalty_rate;
	}

	public void setPenalty_rate(BigDecimal penalty_rate) {
		this.penalty_rate = penalty_rate;
	}

	public BigDecimal getAccrued_interest() {
		return accrued_interest;
	}

	public void setAccrued_interest(BigDecimal accrued_interest) {
		this.accrued_interest = accrued_interest;
	}

	public BigDecimal getAccrued_penalty() {
		return accrued_penalty;
	}

	public void setAccrued_penalty(BigDecimal accrued_penalty) {
		this.accrued_penalty = accrued_penalty;
	}

	public BigDecimal getPrincipal_due() {
		return principal_due;
	}

	public void setPrincipal_due(BigDecimal principal_due) {
		this.principal_due = principal_due;
	}

	public BigDecimal getPrincipal_paid() {
		return principal_paid;
	}

	public void setPrincipal_paid(BigDecimal principal_paid) {
		this.principal_paid = principal_paid;
	}

	public BigDecimal getPrincipal_balance() {
		return principal_balance;
	}

	public void setPrincipal_balance(BigDecimal principal_balance) {
		this.principal_balance = principal_balance;
	}

	public BigDecimal getInterest_due() {
		return interest_due;
	}

	public void setInterest_due(BigDecimal interest_due) {
		this.interest_due = interest_due;
	}

	public BigDecimal getInterest_paid() {
		return interest_paid;
	}

	public void setInterest_paid(BigDecimal interest_paid) {
		this.interest_paid = interest_paid;
	}

	public BigDecimal getInterest_balance() {
		return interest_balance;
	}

	public void setInterest_balance(BigDecimal interest_balance) {
		this.interest_balance = interest_balance;
	}

	public BigDecimal getInterest_fromarrearsbalance() {
		return interest_fromarrearsbalance;
	}

	public void setInterest_fromarrearsbalance(BigDecimal interest_fromarrearsbalance) {
		this.interest_fromarrearsbalance = interest_fromarrearsbalance;
	}

	public BigDecimal getInterest_fromarrearsdue() {
		return interest_fromarrearsdue;
	}

	public void setInterest_fromarrearsdue(BigDecimal interest_fromarrearsdue) {
		this.interest_fromarrearsdue = interest_fromarrearsdue;
	}

	public BigDecimal getInterest_fromarrearspaid() {
		return interest_fromarrearspaid;
	}

	public void setInterest_fromarrearspaid(BigDecimal interest_fromarrearspaid) {
		this.interest_fromarrearspaid = interest_fromarrearspaid;
	}

	public BigDecimal getFees_due() {
		return fees_due;
	}

	public void setFees_due(BigDecimal fees_due) {
		this.fees_due = fees_due;
	}

	public BigDecimal getFees_paid() {
		return fees_paid;
	}

	public void setFees_paid(BigDecimal fees_paid) {
		this.fees_paid = fees_paid;
	}

	public BigDecimal getFees_balance() {
		return fees_balance;
	}

	public void setFees_balance(BigDecimal fees_balance) {
		this.fees_balance = fees_balance;
	}

	public BigDecimal getPenalty_due() {
		return penalty_due;
	}

	public void setPenalty_due(BigDecimal penalty_due) {
		this.penalty_due = penalty_due;
	}

	public BigDecimal getPenalty_paid() {
		return penalty_paid;
	}

	public void setPenalty_paid(BigDecimal penalty_paid) {
		this.penalty_paid = penalty_paid;
	}

	public BigDecimal getPenalty_balance() {
		return penalty_balance;
	}

	public void setPenalty_balance(BigDecimal penalty_balance) {
		this.penalty_balance = penalty_balance;
	}

	public Date getExpected_disbursementdate() {
		return expected_disbursementdate;
	}

	public void setExpected_disbursementdate(Date expected_disbursementdate) {
		this.expected_disbursementdate = expected_disbursementdate;
	}

	public Date getDisbursement_date() {
		return disbursement_date;
	}

	public void setDisbursement_date(Date disbursement_date) {
		this.disbursement_date = disbursement_date;
	}

	public Date getFirst_repaymentdate() {
		return first_repaymentdate;
	}

	public void setFirst_repaymentdate(Date first_repaymentdate) {
		this.first_repaymentdate = first_repaymentdate;
	}

	public BigDecimal getGrace_period() {
		return grace_period;
	}

	public void setGrace_period(BigDecimal grace_period) {
		this.grace_period = grace_period;
	}

	public BigDecimal getRepayment_installments() {
		return repayment_installments;
	}

	public void setRepayment_installments(BigDecimal repayment_installments) {
		this.repayment_installments = repayment_installments;
	}

	public BigDecimal getRepayment_periodcount() {
		return repayment_periodcount;
	}

	public void setRepayment_periodcount(BigDecimal repayment_periodcount) {
		this.repayment_periodcount = repayment_periodcount;
	}

	public BigDecimal getDays_late() {
		return days_late;
	}

	public void setDays_late(BigDecimal days_late) {
		this.days_late = days_late;
	}

	public BigDecimal getDays_inarrears() {
		return days_inarrears;
	}

	public void setDays_inarrears(BigDecimal days_inarrears) {
		this.days_inarrears = days_inarrears;
	}

	public String getRepayment_schedule_method() {
		return repayment_schedule_method;
	}

	public void setRepayment_schedule_method(String repayment_schedule_method) {
		this.repayment_schedule_method = repayment_schedule_method;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getSale_processedbyvgid() {
		return sale_processedbyvgid;
	}

	public void setSale_processedbyvgid(String sale_processedbyvgid) {
		this.sale_processedbyvgid = sale_processedbyvgid;
	}

	public String getSale_processedfor() {
		return sale_processedfor;
	}

	public void setSale_processedfor(String sale_processedfor) {
		this.sale_processedfor = sale_processedfor;
	}

	public String getSale_referredby() {
		return sale_referredby;
	}

	public void setSale_referredby(String sale_referredby) {
		this.sale_referredby = sale_referredby;
	}

	public String getEmployment_status() {
		return employment_status;
	}

	public void setEmployment_status(String employment_status) {
		this.employment_status = employment_status;
	}

	public String getJob_title() {
		return job_title;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	public String getEmployer_name() {
		return employer_name;
	}

	public void setEmployer_name(String employer_name) {
		this.employer_name = employer_name;
	}

	public BigDecimal getTuscore() {
		return tuscore;
	}

	public void setTuscore(BigDecimal tuscore) {
		this.tuscore = tuscore;
	}

	public BigDecimal getTuprobability() {
		return tuprobability;
	}

	public void setTuprobability(BigDecimal tuprobability) {
		this.tuprobability = tuprobability;
	}

	public String getTufullname() {
		return tufullname;
	}

	public void setTufullname(String tufullname) {
		this.tufullname = tufullname;
	}

	public String getTureason1() {
		return tureason1;
	}

	public void setTureason1(String tureason1) {
		this.tureason1 = tureason1;
	}

	public String getTureason2() {
		return tureason2;
	}

	public void setTureason2(String tureason2) {
		this.tureason2 = tureason2;
	}

	public String getTureason3() {
		return tureason3;
	}

	public void setTureason3(String tureason3) {
		this.tureason3 = tureason3;
	}

	public String getTureason4() {
		return tureason4;
	}

	public void setTureason4(String tureason4) {
		this.tureason4 = tureason4;
	}

	public BigDecimal getDisposable_income() {
		return disposable_income;
	}

	public void setDisposable_income(BigDecimal disposable_income) {
		this.disposable_income = disposable_income;
	}

	public BigDecimal getManualoverride_amount() {
		return manualoverride_amount;
	}

	public void setManualoverride_amount(BigDecimal manualoverride_amount) {
		this.manualoverride_amount = manualoverride_amount;
	}

	public Date getManualoverride_expiry_date() {
		return manualoverride_expiry_date;
	}

	public void setManualoverride_expiry_date(Date manualoverride_expiry_date) {
		this.manualoverride_expiry_date = manualoverride_expiry_date;
	}

	public BigDecimal getCpfees() {
		return cpfees;
	}

	public void setCpfees(BigDecimal cpfees) {
		this.cpfees = cpfees;
	}

	public BigDecimal getDeposit_amount() {
		return deposit_amount;
	}

	public void setDeposit_amount(BigDecimal deposit_amount) {
		this.deposit_amount = deposit_amount;
	}

	public BigDecimal getTotal_product_price() {
		return total_product_price;
	}

	public void setTotal_product_price(BigDecimal total_product_price) {
		this.total_product_price = total_product_price;
	}

	public String getRetailer_name() {
		return retailer_name;
	}

	public void setRetailer_name(String retailer_name) {
		this.retailer_name = retailer_name;
	}

	public String getRetailer_branch() {
		return retailer_branch;
	}

	public void setRetailer_branch(String retailer_branch) {
		this.retailer_branch = retailer_branch;
	}

	public String getVg_application_id() {
		return vg_application_id;
	}

	public void setVg_application_id(String vg_application_id) {
		this.vg_application_id = vg_application_id;
	}

	public String getContract_signed() {
		return contract_signed;
	}

	public void setContract_signed(String contract_signed) {
		this.contract_signed = contract_signed;
	}

	public Date getDate_of_first_call() {
		return date_of_first_call;
	}

	public void setDate_of_first_call(Date date_of_first_call) {
		this.date_of_first_call = date_of_first_call;
	}

	public String getLast_call_outcome() {
		return last_call_outcome;
	}

	public void setLast_call_outcome(String last_call_outcome) {
		this.last_call_outcome = last_call_outcome;
	}

	public Date getAsondate() {
		return asondate;
	}

	public void setAsondate(Date asondate) {
		this.asondate = asondate;
	}

	public String getDisbursement_flg() {
		return disbursement_flg;
	}

	public void setDisbursement_flg(String disbursement_flg) {
		this.disbursement_flg = disbursement_flg;
	}

	public String getInterest_flg() {
		return interest_flg;
	}

	public void setInterest_flg(String interest_flg) {
		this.interest_flg = interest_flg;
	}

	public String getFees_flg() {
		return fees_flg;
	}

	public void setFees_flg(String fees_flg) {
		this.fees_flg = fees_flg;
	}

	public String getRecovery_flg() {
		return recovery_flg;
	}

	public void setRecovery_flg(String recovery_flg) {
		this.recovery_flg = recovery_flg;
	}

	public String getInterest_demand_flg() {
		return interest_demand_flg;
	}

	public void setInterest_demand_flg(String interest_demand_flg) {
		this.interest_demand_flg = interest_demand_flg;
	}

	public String getFees_demand_flg() {
		return fees_demand_flg;
	}

	public void setFees_demand_flg(String fees_demand_flg) {
		this.fees_demand_flg = fees_demand_flg;
	}

	public String getBooking_flg() {
		return booking_flg;
	}

	public void setBooking_flg(String booking_flg) {
		this.booking_flg = booking_flg;
	}

	

	public LOAN_ACT_MST_ENTITY(String encoded_key, String id, String account_holdertype, String account_holderkey,
			Date creation_date, Date approved_date, Date last_modified_date, Date closed_date,
			Date last_account_appraisaldate, String account_state, String account_substate, String product_typekey,
			String loan_name, String payment_method, String assigned_branchkey, BigDecimal loan_amount,
			BigDecimal interest_rate, BigDecimal penalty_rate, BigDecimal accrued_interest, BigDecimal accrued_penalty,
			BigDecimal principal_due, BigDecimal principal_paid, BigDecimal principal_balance, BigDecimal interest_due,
			BigDecimal interest_paid, BigDecimal interest_balance, BigDecimal interest_fromarrearsbalance,
			BigDecimal interest_fromarrearsdue, BigDecimal interest_fromarrearspaid, BigDecimal fees_due,
			BigDecimal fees_paid, BigDecimal fees_balance, BigDecimal penalty_due, BigDecimal penalty_paid,
			BigDecimal penalty_balance, Date expected_disbursementdate, Date disbursement_date,
			Date first_repaymentdate, BigDecimal grace_period, BigDecimal repayment_installments,
			BigDecimal repayment_periodcount, BigDecimal days_late, BigDecimal days_inarrears,
			String repayment_schedule_method, String currency_code, String sale_processedbyvgid,
			String sale_processedfor, String sale_referredby, String employment_status, String job_title,
			String employer_name, BigDecimal tuscore, BigDecimal tuprobability, String tufullname, String tureason1,
			String tureason2, String tureason3, String tureason4, BigDecimal disposable_income,
			BigDecimal manualoverride_amount, Date manualoverride_expiry_date, BigDecimal cpfees,
			BigDecimal deposit_amount, BigDecimal total_product_price, String retailer_name, String retailer_branch,
			String vg_application_id, String contract_signed, Date date_of_first_call, String last_call_outcome,
			Date asondate, String disbursement_flg, String interest_flg, String fees_flg, String recovery_flg,
			String interest_demand_flg, String fees_demand_flg, String booking_flg, String entity_flg, String auth_flg,
			String modify_flg, String del_flg, String entry_user, String modify_user, String auth_user, Date entry_time,
			Date modify_time, Date auth_time) {
		super();
		this.encoded_key = encoded_key;
		this.id = id;
		this.account_holdertype = account_holdertype;
		this.account_holderkey = account_holderkey;
		this.creation_date = creation_date;
		this.approved_date = approved_date;
		this.last_modified_date = last_modified_date;
		this.closed_date = closed_date;
		this.last_account_appraisaldate = last_account_appraisaldate;
		this.account_state = account_state;
		this.account_substate = account_substate;
		this.product_typekey = product_typekey;
		this.loan_name = loan_name;
		this.payment_method = payment_method;
		this.assigned_branchkey = assigned_branchkey;
		this.loan_amount = loan_amount;
		this.interest_rate = interest_rate;
		this.penalty_rate = penalty_rate;
		this.accrued_interest = accrued_interest;
		this.accrued_penalty = accrued_penalty;
		this.principal_due = principal_due;
		this.principal_paid = principal_paid;
		this.principal_balance = principal_balance;
		this.interest_due = interest_due;
		this.interest_paid = interest_paid;
		this.interest_balance = interest_balance;
		this.interest_fromarrearsbalance = interest_fromarrearsbalance;
		this.interest_fromarrearsdue = interest_fromarrearsdue;
		this.interest_fromarrearspaid = interest_fromarrearspaid;
		this.fees_due = fees_due;
		this.fees_paid = fees_paid;
		this.fees_balance = fees_balance;
		this.penalty_due = penalty_due;
		this.penalty_paid = penalty_paid;
		this.penalty_balance = penalty_balance;
		this.expected_disbursementdate = expected_disbursementdate;
		this.disbursement_date = disbursement_date;
		this.first_repaymentdate = first_repaymentdate;
		this.grace_period = grace_period;
		this.repayment_installments = repayment_installments;
		this.repayment_periodcount = repayment_periodcount;
		this.days_late = days_late;
		this.days_inarrears = days_inarrears;
		this.repayment_schedule_method = repayment_schedule_method;
		this.currency_code = currency_code;
		this.sale_processedbyvgid = sale_processedbyvgid;
		this.sale_processedfor = sale_processedfor;
		this.sale_referredby = sale_referredby;
		this.employment_status = employment_status;
		this.job_title = job_title;
		this.employer_name = employer_name;
		this.tuscore = tuscore;
		this.tuprobability = tuprobability;
		this.tufullname = tufullname;
		this.tureason1 = tureason1;
		this.tureason2 = tureason2;
		this.tureason3 = tureason3;
		this.tureason4 = tureason4;
		this.disposable_income = disposable_income;
		this.manualoverride_amount = manualoverride_amount;
		this.manualoverride_expiry_date = manualoverride_expiry_date;
		this.cpfees = cpfees;
		this.deposit_amount = deposit_amount;
		this.total_product_price = total_product_price;
		this.retailer_name = retailer_name;
		this.retailer_branch = retailer_branch;
		this.vg_application_id = vg_application_id;
		this.contract_signed = contract_signed;
		this.date_of_first_call = date_of_first_call;
		this.last_call_outcome = last_call_outcome;
		this.asondate = asondate;
		this.disbursement_flg = disbursement_flg;
		this.interest_flg = interest_flg;
		this.fees_flg = fees_flg;
		this.recovery_flg = recovery_flg;
		this.interest_demand_flg = interest_demand_flg;
		this.fees_demand_flg = fees_demand_flg;
		this.booking_flg = booking_flg;
		this.entity_flg = entity_flg;
		this.auth_flg = auth_flg;
		this.modify_flg = modify_flg;
		this.del_flg = del_flg;
		this.entry_user = entry_user;
		this.modify_user = modify_user;
		this.auth_user = auth_user;
		this.entry_time = entry_time;
		this.modify_time = modify_time;
		this.auth_time = auth_time;
	}
	
	
	@Override
	public String toString() {
		return "LOAN_ACT_MST_ENTITY [encoded_key=" + encoded_key + ", id=" + id + ", account_holdertype="
				+ account_holdertype + ", account_holderkey=" + account_holderkey + ", creation_date=" + creation_date
				+ ", approved_date=" + approved_date + ", last_modified_date=" + last_modified_date + ", closed_date="
				+ closed_date + ", last_account_appraisaldate=" + last_account_appraisaldate + ", account_state="
				+ account_state + ", account_substate=" + account_substate + ", product_typekey=" + product_typekey
				+ ", loan_name=" + loan_name + ", payment_method=" + payment_method + ", assigned_branchkey="
				+ assigned_branchkey + ", loan_amount=" + loan_amount + ", interest_rate=" + interest_rate
				+ ", penalty_rate=" + penalty_rate + ", accrued_interest=" + accrued_interest + ", accrued_penalty="
				+ accrued_penalty + ", principal_due=" + principal_due + ", principal_paid=" + principal_paid
				+ ", principal_balance=" + principal_balance + ", interest_due=" + interest_due + ", interest_paid="
				+ interest_paid + ", interest_balance=" + interest_balance + ", interest_fromarrearsbalance="
				+ interest_fromarrearsbalance + ", interest_fromarrearsdue=" + interest_fromarrearsdue
				+ ", interest_fromarrearspaid=" + interest_fromarrearspaid + ", fees_due=" + fees_due + ", fees_paid="
				+ fees_paid + ", fees_balance=" + fees_balance + ", penalty_due=" + penalty_due + ", penalty_paid="
				+ penalty_paid + ", penalty_balance=" + penalty_balance + ", expected_disbursementdate="
				+ expected_disbursementdate + ", disbursement_date=" + disbursement_date + ", first_repaymentdate="
				+ first_repaymentdate + ", grace_period=" + grace_period + ", repayment_installments="
				+ repayment_installments + ", repayment_periodcount=" + repayment_periodcount + ", days_late="
				+ days_late + ", days_inarrears=" + days_inarrears + ", repayment_schedule_method="
				+ repayment_schedule_method + ", currency_code=" + currency_code + ", sale_processedbyvgid="
				+ sale_processedbyvgid + ", sale_processedfor=" + sale_processedfor + ", sale_referredby="
				+ sale_referredby + ", employment_status=" + employment_status + ", job_title=" + job_title
				+ ", employer_name=" + employer_name + ", tuscore=" + tuscore + ", tuprobability=" + tuprobability
				+ ", tufullname=" + tufullname + ", tureason1=" + tureason1 + ", tureason2=" + tureason2
				+ ", tureason3=" + tureason3 + ", tureason4=" + tureason4 + ", disposable_income=" + disposable_income
				+ ", manualoverride_amount=" + manualoverride_amount + ", manualoverride_expiry_date="
				+ manualoverride_expiry_date + ", cpfees=" + cpfees + ", deposit_amount=" + deposit_amount
				+ ", total_product_price=" + total_product_price + ", retailer_name=" + retailer_name
				+ ", retailer_branch=" + retailer_branch + ", vg_application_id=" + vg_application_id
				+ ", contract_signed=" + contract_signed + ", date_of_first_call=" + date_of_first_call
				+ ", last_call_outcome=" + last_call_outcome + ", asondate=" + asondate + ", disbursement_flg="
				+ disbursement_flg + ", interest_flg=" + interest_flg + ", fees_flg=" + fees_flg + ", recovery_flg="
				+ recovery_flg + ", interest_demand_flg=" + interest_demand_flg + ", fees_demand_flg=" + fees_demand_flg
				+ ", booking_flg=" + booking_flg + ", entity_flg=" + entity_flg + ", auth_flg=" + auth_flg
				+ ", modify_flg=" + modify_flg + ", del_flg=" + del_flg + ", entry_user=" + entry_user
				+ ", modify_user=" + modify_user + ", auth_user=" + auth_user + ", entry_time=" + entry_time
				+ ", modify_time=" + modify_time + ", auth_time=" + auth_time + "]";
	}
	public LOAN_ACT_MST_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}

}
