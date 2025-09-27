package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="LOAN_REPAYMENT_TBL")
public class LOAN_REPAYMENT_ENTITY {
	@Id
	private String	encoded_key;
	private String	parent_account_key;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	due_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	last_paid_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	repaid_date;
	private String	payment_state;
	private String	is_payment_holiday;
	private BigDecimal	principal_exp;
	private BigDecimal	principal_paid;
	private BigDecimal	principal_due;
	private BigDecimal	interest_exp;
	private BigDecimal	interest_paid;
	private BigDecimal	interest_due;
	private BigDecimal	fee_exp;
	private BigDecimal	fee_paid;
	private BigDecimal	fee_due;
	private BigDecimal	penalty_exp;
	private BigDecimal	penalty_paid;
	private BigDecimal	penalty_due;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date	asondate;
	private String del_flg;
	private String	entity_flg;
	private String	auth_flg;
	private String	modify_flg;
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

	public String getParent_account_key() {
		return parent_account_key;
	}

	public void setParent_account_key(String parent_account_key) {
		this.parent_account_key = parent_account_key;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public Date getLast_paid_date() {
		return last_paid_date;
	}

	public void setLast_paid_date(Date last_paid_date) {
		this.last_paid_date = last_paid_date;
	}

	public Date getRepaid_date() {
		return repaid_date;
	}

	public void setRepaid_date(Date repaid_date) {
		this.repaid_date = repaid_date;
	}

	public String getPayment_state() {
		return payment_state;
	}

	public void setPayment_state(String payment_state) {
		this.payment_state = payment_state;
	}

	public String getIs_payment_holiday() {
		return is_payment_holiday;
	}

	public void setIs_payment_holiday(String is_payment_holiday) {
		this.is_payment_holiday = is_payment_holiday;
	}

	public BigDecimal getPrincipal_exp() {
		return principal_exp;
	}

	public void setPrincipal_exp(BigDecimal principal_exp) {
		this.principal_exp = principal_exp;
	}

	public BigDecimal getPrincipal_paid() {
		return principal_paid;
	}

	public void setPrincipal_paid(BigDecimal principal_paid) {
		this.principal_paid = principal_paid;
	}

	public BigDecimal getPrincipal_due() {
		return principal_due;
	}

	public void setPrincipal_due(BigDecimal principal_due) {
		this.principal_due = principal_due;
	}

	public BigDecimal getInterest_exp() {
		return interest_exp;
	}

	public void setInterest_exp(BigDecimal interest_exp) {
		this.interest_exp = interest_exp;
	}

	public BigDecimal getInterest_paid() {
		return interest_paid;
	}

	public void setInterest_paid(BigDecimal interest_paid) {
		this.interest_paid = interest_paid;
	}

	public BigDecimal getInterest_due() {
		return interest_due;
	}

	public void setInterest_due(BigDecimal interest_due) {
		this.interest_due = interest_due;
	}

	public BigDecimal getFee_exp() {
		return fee_exp;
	}

	public void setFee_exp(BigDecimal fee_exp) {
		this.fee_exp = fee_exp;
	}

	public BigDecimal getFee_paid() {
		return fee_paid;
	}

	public void setFee_paid(BigDecimal fee_paid) {
		this.fee_paid = fee_paid;
	}

	public BigDecimal getFee_due() {
		return fee_due;
	}

	public void setFee_due(BigDecimal fee_due) {
		this.fee_due = fee_due;
	}

	public BigDecimal getPenalty_exp() {
		return penalty_exp;
	}

	public void setPenalty_exp(BigDecimal penalty_exp) {
		this.penalty_exp = penalty_exp;
	}

	public BigDecimal getPenalty_paid() {
		return penalty_paid;
	}

	public void setPenalty_paid(BigDecimal penalty_paid) {
		this.penalty_paid = penalty_paid;
	}

	public BigDecimal getPenalty_due() {
		return penalty_due;
	}

	public void setPenalty_due(BigDecimal penalty_due) {
		this.penalty_due = penalty_due;
	}

	public Date getAsondate() {
		return asondate;
	}

	public void setAsondate(Date asondate) {
		this.asondate = asondate;
	}

	public String getDel_flg() {
		return del_flg;
	}

	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}

	public LOAN_REPAYMENT_ENTITY(String encoded_key, String parent_account_key, Date due_date, Date last_paid_date,
			Date repaid_date, String payment_state, String is_payment_holiday, BigDecimal principal_exp,
			BigDecimal principal_paid, BigDecimal principal_due, BigDecimal interest_exp, BigDecimal interest_paid,
			BigDecimal interest_due, BigDecimal fee_exp, BigDecimal fee_paid, BigDecimal fee_due,
			BigDecimal penalty_exp, BigDecimal penalty_paid, BigDecimal penalty_due, Date asondate, String del_flg) {
		super();
		this.encoded_key = encoded_key;
		this.parent_account_key = parent_account_key;
		this.due_date = due_date;
		this.last_paid_date = last_paid_date;
		this.repaid_date = repaid_date;
		this.payment_state = payment_state;
		this.is_payment_holiday = is_payment_holiday;
		this.principal_exp = principal_exp;
		this.principal_paid = principal_paid;
		this.principal_due = principal_due;
		this.interest_exp = interest_exp;
		this.interest_paid = interest_paid;
		this.interest_due = interest_due;
		this.fee_exp = fee_exp;
		this.fee_paid = fee_paid;
		this.fee_due = fee_due;
		this.penalty_exp = penalty_exp;
		this.penalty_paid = penalty_paid;
		this.penalty_due = penalty_due;
		this.asondate = asondate;
		this.del_flg = del_flg;
	}

	public LOAN_REPAYMENT_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
}
