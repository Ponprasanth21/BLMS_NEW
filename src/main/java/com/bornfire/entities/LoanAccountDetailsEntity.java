package com.bornfire.entities;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "LOAN_ACCOUNT_DETAILS")
public class LoanAccountDetailsEntity {

    @Id
    private String loan_id;

    private String encoded_key;
    private String holder_key;
    private String holder_type;
    private String acc_state;
    private String acc_sub_state;
    private String reschedule_key;
    private String branch_key;
    private String user_key;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date close_date;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date locked_date;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date create_date;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date approved_date;

    private BigDecimal fees_due;
    private BigDecimal fees_paid;
    private BigDecimal grace_period;
    private String grace_type;

    private String int_calc_method;
    private String int_type;
    private String repay_schedule;
    private String int_apply_method;
    private String pay_method;
    private String int_freq;

    private BigDecimal int_bal;
    private BigDecimal int_paid;
    private BigDecimal int_rate;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date mod_date;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date arrears_date;

    private BigDecimal loan_amt;
    private BigDecimal periodic_pay;
    private String loan_group_key;
    private String loan_name;
    private String notes;

    private BigDecimal penalty_due;
    private BigDecimal penalty_paid;
    private BigDecimal prin_bal;
    private BigDecimal prin_paid;
    private String product_key;

    private BigDecimal repay_inst;
    private BigDecimal repay_period_cnt;
    private String repay_period_unit;
    private BigDecimal acc_idx;
    private String migration_key;
    private String centre_key;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date appraisal_date;

    private BigDecimal prin_interval;
    private BigDecimal prin_due;
    private BigDecimal int_due;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date int_review_date;

    private BigDecimal accrue_late_int;
    private BigDecimal int_spread;
    private String int_source;
    private String int_review_unit;
    private BigDecimal int_review_cnt;
    private BigDecimal accrued_int;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date int_applied_date;

    private BigDecimal fees_bal;
    private BigDecimal penalty_bal;
    private String due_date_method;
    private BigDecimal custom_schedule;
    private String fixed_day;
    private String short_month_method;

    private BigDecimal tax_rate;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date tax_review_date;

    private BigDecimal penalty_rate;
    private String penalty_calc_method;
    private BigDecimal accrued_penalty;

    private String activation_key;
    private String credit_key;
    private String locked_ops;
    private String int_commission;
    private String first_repay_offset;
    private String prin_pay_key;
    private String int_bal_calc_method;
    private String disburse_key;

    private BigDecimal arrears_tolerance;
    private BigDecimal accrue_after_maturity;
    private String prepay_calc_method;
    private String prin_paid_status;
    private String elem_calc_method;
    private String late_pay_calc_method;
    private BigDecimal prepay_int_method;
    private String allow_offset;
    private BigDecimal future_pay_accept;
    private String redraw_bal;
    private BigDecimal prepay_accept;

    private BigDecimal int_arr_accrued;
    private BigDecimal int_arr_due;
    private BigDecimal int_arr_paid;
    private BigDecimal int_arr_bal;

    private String int_round_ver;
    private String arrears_set_key;
    private BigDecimal hold_bal;
    private String addition;
	public String getLoan_id() {
		return loan_id;
	}
	public void setLoan_id(String loan_id) {
		this.loan_id = loan_id;
	}
	public String getEncoded_key() {
		return encoded_key;
	}
	public void setEncoded_key(String encoded_key) {
		this.encoded_key = encoded_key;
	}
	public String getHolder_key() {
		return holder_key;
	}
	public void setHolder_key(String holder_key) {
		this.holder_key = holder_key;
	}
	public String getHolder_type() {
		return holder_type;
	}
	public void setHolder_type(String holder_type) {
		this.holder_type = holder_type;
	}
	public String getAcc_state() {
		return acc_state;
	}
	public void setAcc_state(String acc_state) {
		this.acc_state = acc_state;
	}
	public String getAcc_sub_state() {
		return acc_sub_state;
	}
	public void setAcc_sub_state(String acc_sub_state) {
		this.acc_sub_state = acc_sub_state;
	}
	public String getReschedule_key() {
		return reschedule_key;
	}
	public void setReschedule_key(String reschedule_key) {
		this.reschedule_key = reschedule_key;
	}
	public String getBranch_key() {
		return branch_key;
	}
	public void setBranch_key(String branch_key) {
		this.branch_key = branch_key;
	}
	public String getUser_key() {
		return user_key;
	}
	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}
	public Date getClose_date() {
		return close_date;
	}
	public void setClose_date(Date close_date) {
		this.close_date = close_date;
	}
	public Date getLocked_date() {
		return locked_date;
	}
	public void setLocked_date(Date locked_date) {
		this.locked_date = locked_date;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getApproved_date() {
		return approved_date;
	}
	public void setApproved_date(Date approved_date) {
		this.approved_date = approved_date;
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
	public BigDecimal getGrace_period() {
		return grace_period;
	}
	public void setGrace_period(BigDecimal grace_period) {
		this.grace_period = grace_period;
	}
	public String getGrace_type() {
		return grace_type;
	}
	public void setGrace_type(String grace_type) {
		this.grace_type = grace_type;
	}
	public String getInt_calc_method() {
		return int_calc_method;
	}
	public void setInt_calc_method(String int_calc_method) {
		this.int_calc_method = int_calc_method;
	}
	public String getInt_type() {
		return int_type;
	}
	public void setInt_type(String int_type) {
		this.int_type = int_type;
	}
	public String getRepay_schedule() {
		return repay_schedule;
	}
	public void setRepay_schedule(String repay_schedule) {
		this.repay_schedule = repay_schedule;
	}
	public String getInt_apply_method() {
		return int_apply_method;
	}
	public void setInt_apply_method(String int_apply_method) {
		this.int_apply_method = int_apply_method;
	}
	public String getPay_method() {
		return pay_method;
	}
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}
	public String getInt_freq() {
		return int_freq;
	}
	public void setInt_freq(String int_freq) {
		this.int_freq = int_freq;
	}
	public BigDecimal getInt_bal() {
		return int_bal;
	}
	public void setInt_bal(BigDecimal int_bal) {
		this.int_bal = int_bal;
	}
	public BigDecimal getInt_paid() {
		return int_paid;
	}
	public void setInt_paid(BigDecimal int_paid) {
		this.int_paid = int_paid;
	}
	public BigDecimal getInt_rate() {
		return int_rate;
	}
	public void setInt_rate(BigDecimal int_rate) {
		this.int_rate = int_rate;
	}
	public Date getMod_date() {
		return mod_date;
	}
	public void setMod_date(Date mod_date) {
		this.mod_date = mod_date;
	}
	public Date getArrears_date() {
		return arrears_date;
	}
	public void setArrears_date(Date arrears_date) {
		this.arrears_date = arrears_date;
	}
	public BigDecimal getLoan_amt() {
		return loan_amt;
	}
	public void setLoan_amt(BigDecimal loan_amt) {
		this.loan_amt = loan_amt;
	}
	public BigDecimal getPeriodic_pay() {
		return periodic_pay;
	}
	public void setPeriodic_pay(BigDecimal periodic_pay) {
		this.periodic_pay = periodic_pay;
	}
	public String getLoan_group_key() {
		return loan_group_key;
	}
	public void setLoan_group_key(String loan_group_key) {
		this.loan_group_key = loan_group_key;
	}
	public String getLoan_name() {
		return loan_name;
	}
	public void setLoan_name(String loan_name) {
		this.loan_name = loan_name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
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
	public BigDecimal getPrin_bal() {
		return prin_bal;
	}
	public void setPrin_bal(BigDecimal prin_bal) {
		this.prin_bal = prin_bal;
	}
	public BigDecimal getPrin_paid() {
		return prin_paid;
	}
	public void setPrin_paid(BigDecimal prin_paid) {
		this.prin_paid = prin_paid;
	}
	public String getProduct_key() {
		return product_key;
	}
	public void setProduct_key(String product_key) {
		this.product_key = product_key;
	}
	public BigDecimal getRepay_inst() {
		return repay_inst;
	}
	public void setRepay_inst(BigDecimal repay_inst) {
		this.repay_inst = repay_inst;
	}
	public BigDecimal getRepay_period_cnt() {
		return repay_period_cnt;
	}
	public void setRepay_period_cnt(BigDecimal repay_period_cnt) {
		this.repay_period_cnt = repay_period_cnt;
	}
	public String getRepay_period_unit() {
		return repay_period_unit;
	}
	public void setRepay_period_unit(String repay_period_unit) {
		this.repay_period_unit = repay_period_unit;
	}
	public BigDecimal getAcc_idx() {
		return acc_idx;
	}
	public void setAcc_idx(BigDecimal acc_idx) {
		this.acc_idx = acc_idx;
	}
	public String getMigration_key() {
		return migration_key;
	}
	public void setMigration_key(String migration_key) {
		this.migration_key = migration_key;
	}
	public String getCentre_key() {
		return centre_key;
	}
	public void setCentre_key(String centre_key) {
		this.centre_key = centre_key;
	}
	public Date getAppraisal_date() {
		return appraisal_date;
	}
	public void setAppraisal_date(Date appraisal_date) {
		this.appraisal_date = appraisal_date;
	}
	public BigDecimal getPrin_interval() {
		return prin_interval;
	}
	public void setPrin_interval(BigDecimal prin_interval) {
		this.prin_interval = prin_interval;
	}
	public BigDecimal getPrin_due() {
		return prin_due;
	}
	public void setPrin_due(BigDecimal prin_due) {
		this.prin_due = prin_due;
	}
	public BigDecimal getInt_due() {
		return int_due;
	}
	public void setInt_due(BigDecimal int_due) {
		this.int_due = int_due;
	}
	public Date getInt_review_date() {
		return int_review_date;
	}
	public void setInt_review_date(Date int_review_date) {
		this.int_review_date = int_review_date;
	}
	public BigDecimal getAccrue_late_int() {
		return accrue_late_int;
	}
	public void setAccrue_late_int(BigDecimal accrue_late_int) {
		this.accrue_late_int = accrue_late_int;
	}
	public BigDecimal getInt_spread() {
		return int_spread;
	}
	public void setInt_spread(BigDecimal int_spread) {
		this.int_spread = int_spread;
	}
	public String getInt_source() {
		return int_source;
	}
	public void setInt_source(String int_source) {
		this.int_source = int_source;
	}
	public String getInt_review_unit() {
		return int_review_unit;
	}
	public void setInt_review_unit(String int_review_unit) {
		this.int_review_unit = int_review_unit;
	}
	public BigDecimal getInt_review_cnt() {
		return int_review_cnt;
	}
	public void setInt_review_cnt(BigDecimal int_review_cnt) {
		this.int_review_cnt = int_review_cnt;
	}
	public BigDecimal getAccrued_int() {
		return accrued_int;
	}
	public void setAccrued_int(BigDecimal accrued_int) {
		this.accrued_int = accrued_int;
	}
	public Date getInt_applied_date() {
		return int_applied_date;
	}
	public void setInt_applied_date(Date int_applied_date) {
		this.int_applied_date = int_applied_date;
	}
	public BigDecimal getFees_bal() {
		return fees_bal;
	}
	public void setFees_bal(BigDecimal fees_bal) {
		this.fees_bal = fees_bal;
	}
	public BigDecimal getPenalty_bal() {
		return penalty_bal;
	}
	public void setPenalty_bal(BigDecimal penalty_bal) {
		this.penalty_bal = penalty_bal;
	}
	public String getDue_date_method() {
		return due_date_method;
	}
	public void setDue_date_method(String due_date_method) {
		this.due_date_method = due_date_method;
	}
	public BigDecimal getCustom_schedule() {
		return custom_schedule;
	}
	public void setCustom_schedule(BigDecimal custom_schedule) {
		this.custom_schedule = custom_schedule;
	}
	public String getFixed_day() {
		return fixed_day;
	}
	public void setFixed_day(String fixed_day) {
		this.fixed_day = fixed_day;
	}
	public String getShort_month_method() {
		return short_month_method;
	}
	public void setShort_month_method(String short_month_method) {
		this.short_month_method = short_month_method;
	}
	public BigDecimal getTax_rate() {
		return tax_rate;
	}
	public void setTax_rate(BigDecimal tax_rate) {
		this.tax_rate = tax_rate;
	}
	public Date getTax_review_date() {
		return tax_review_date;
	}
	public void setTax_review_date(Date tax_review_date) {
		this.tax_review_date = tax_review_date;
	}
	public BigDecimal getPenalty_rate() {
		return penalty_rate;
	}
	public void setPenalty_rate(BigDecimal penalty_rate) {
		this.penalty_rate = penalty_rate;
	}
	public String getPenalty_calc_method() {
		return penalty_calc_method;
	}
	public void setPenalty_calc_method(String penalty_calc_method) {
		this.penalty_calc_method = penalty_calc_method;
	}
	public BigDecimal getAccrued_penalty() {
		return accrued_penalty;
	}
	public void setAccrued_penalty(BigDecimal accrued_penalty) {
		this.accrued_penalty = accrued_penalty;
	}
	public String getActivation_key() {
		return activation_key;
	}
	public void setActivation_key(String activation_key) {
		this.activation_key = activation_key;
	}
	public String getCredit_key() {
		return credit_key;
	}
	public void setCredit_key(String credit_key) {
		this.credit_key = credit_key;
	}
	public String getLocked_ops() {
		return locked_ops;
	}
	public void setLocked_ops(String locked_ops) {
		this.locked_ops = locked_ops;
	}
	public String getInt_commission() {
		return int_commission;
	}
	public void setInt_commission(String int_commission) {
		this.int_commission = int_commission;
	}
	public String getFirst_repay_offset() {
		return first_repay_offset;
	}
	public void setFirst_repay_offset(String first_repay_offset) {
		this.first_repay_offset = first_repay_offset;
	}
	public String getPrin_pay_key() {
		return prin_pay_key;
	}
	public void setPrin_pay_key(String prin_pay_key) {
		this.prin_pay_key = prin_pay_key;
	}
	public String getInt_bal_calc_method() {
		return int_bal_calc_method;
	}
	public void setInt_bal_calc_method(String int_bal_calc_method) {
		this.int_bal_calc_method = int_bal_calc_method;
	}
	public String getDisburse_key() {
		return disburse_key;
	}
	public void setDisburse_key(String disburse_key) {
		this.disburse_key = disburse_key;
	}
	public BigDecimal getArrears_tolerance() {
		return arrears_tolerance;
	}
	public void setArrears_tolerance(BigDecimal arrears_tolerance) {
		this.arrears_tolerance = arrears_tolerance;
	}
	public BigDecimal getAccrue_after_maturity() {
		return accrue_after_maturity;
	}
	public void setAccrue_after_maturity(BigDecimal accrue_after_maturity) {
		this.accrue_after_maturity = accrue_after_maturity;
	}
	public String getPrepay_calc_method() {
		return prepay_calc_method;
	}
	public void setPrepay_calc_method(String prepay_calc_method) {
		this.prepay_calc_method = prepay_calc_method;
	}
	public String getPrin_paid_status() {
		return prin_paid_status;
	}
	public void setPrin_paid_status(String prin_paid_status) {
		this.prin_paid_status = prin_paid_status;
	}
	public String getElem_calc_method() {
		return elem_calc_method;
	}
	public void setElem_calc_method(String elem_calc_method) {
		this.elem_calc_method = elem_calc_method;
	}
	public String getLate_pay_calc_method() {
		return late_pay_calc_method;
	}
	public void setLate_pay_calc_method(String late_pay_calc_method) {
		this.late_pay_calc_method = late_pay_calc_method;
	}
	public BigDecimal getPrepay_int_method() {
		return prepay_int_method;
	}
	public void setPrepay_int_method(BigDecimal prepay_int_method) {
		this.prepay_int_method = prepay_int_method;
	}
	public String getAllow_offset() {
		return allow_offset;
	}
	public void setAllow_offset(String allow_offset) {
		this.allow_offset = allow_offset;
	}
	public BigDecimal getFuture_pay_accept() {
		return future_pay_accept;
	}
	public void setFuture_pay_accept(BigDecimal future_pay_accept) {
		this.future_pay_accept = future_pay_accept;
	}
	public String getRedraw_bal() {
		return redraw_bal;
	}
	public void setRedraw_bal(String redraw_bal) {
		this.redraw_bal = redraw_bal;
	}
	public BigDecimal getPrepay_accept() {
		return prepay_accept;
	}
	public void setPrepay_accept(BigDecimal prepay_accept) {
		this.prepay_accept = prepay_accept;
	}
	public BigDecimal getInt_arr_accrued() {
		return int_arr_accrued;
	}
	public void setInt_arr_accrued(BigDecimal int_arr_accrued) {
		this.int_arr_accrued = int_arr_accrued;
	}
	public BigDecimal getInt_arr_due() {
		return int_arr_due;
	}
	public void setInt_arr_due(BigDecimal int_arr_due) {
		this.int_arr_due = int_arr_due;
	}
	public BigDecimal getInt_arr_paid() {
		return int_arr_paid;
	}
	public void setInt_arr_paid(BigDecimal int_arr_paid) {
		this.int_arr_paid = int_arr_paid;
	}
	public BigDecimal getInt_arr_bal() {
		return int_arr_bal;
	}
	public void setInt_arr_bal(BigDecimal int_arr_bal) {
		this.int_arr_bal = int_arr_bal;
	}
	public String getInt_round_ver() {
		return int_round_ver;
	}
	public void setInt_round_ver(String int_round_ver) {
		this.int_round_ver = int_round_ver;
	}
	public String getArrears_set_key() {
		return arrears_set_key;
	}
	public void setArrears_set_key(String arrears_set_key) {
		this.arrears_set_key = arrears_set_key;
	}
	public BigDecimal getHold_bal() {
		return hold_bal;
	}
	public void setHold_bal(BigDecimal hold_bal) {
		this.hold_bal = hold_bal;
	}
	public String getAddition() {
		return addition;
	}
	public void setAddition(String addition) {
		this.addition = addition;
	}

    
    
}
