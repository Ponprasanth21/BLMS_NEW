package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BGLS_TRANSACTION_ACCOUNTS")
public class Transaction_accounts_entity {
	@Id
	private String id;
	private String gl_code;
	private String gl_desc;
	private String glsh_code;
	private String glsh_desc;
	private String schm_code;
	private String schm_desc;
	private String interest_income;
	private String interest_recivable;
	private String fees_income;
	private String penalty_income;
	private String collection_account;
	private String loan_parking_account;
	private String entity_flg;
	private String verify_flg;
	private String modify_flg;
	private String del_flg;
	private String entry_user;
	private String modify_user;
	private String verify_user;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date entry_time;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date verify_time;
	private String product_key;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGl_code() {
		return gl_code;
	}
	public void setGl_code(String gl_code) {
		this.gl_code = gl_code;
	}
	public String getGl_desc() {
		return gl_desc;
	}
	public void setGl_desc(String gl_desc) {
		this.gl_desc = gl_desc;
	}
	public String getGlsh_code() {
		return glsh_code;
	}
	public void setGlsh_code(String glsh_code) {
		this.glsh_code = glsh_code;
	}
	public String getGlsh_desc() {
		return glsh_desc;
	}
	public void setGlsh_desc(String glsh_desc) {
		this.glsh_desc = glsh_desc;
	}
	public String getSchm_code() {
		return schm_code;
	}
	public void setSchm_code(String schm_code) {
		this.schm_code = schm_code;
	}
	public String getSchm_desc() {
		return schm_desc;
	}
	public void setSchm_desc(String schm_desc) {
		this.schm_desc = schm_desc;
	}
	public String getInterest_income() {
		return interest_income;
	}
	public void setInterest_income(String interest_income) {
		this.interest_income = interest_income;
	}
	public String getInterest_recivable() {
		return interest_recivable;
	}
	public void setInterest_recivable(String interest_recivable) {
		this.interest_recivable = interest_recivable;
	}
	public String getFees_income() {
		return fees_income;
	}
	public void setFees_income(String fees_income) {
		this.fees_income = fees_income;
	}
	public String getPenalty_income() {
		return penalty_income;
	}
	public void setPenalty_income(String penalty_income) {
		this.penalty_income = penalty_income;
	}
	public String getCollection_account() {
		return collection_account;
	}
	public void setCollection_account(String collection_account) {
		this.collection_account = collection_account;
	}
	public String getLoan_parking_account() {
		return loan_parking_account;
	}
	public void setLoan_parking_account(String loan_parking_account) {
		this.loan_parking_account = loan_parking_account;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getVerify_flg() {
		return verify_flg;
	}
	public void setVerify_flg(String verify_flg) {
		this.verify_flg = verify_flg;
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
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}
	public String getProduct_key() {
		return product_key;
	}
	public void setProduct_key(String product_key) {
		this.product_key = product_key;
	}
	public Transaction_accounts_entity(String id, String gl_code, String gl_desc, String glsh_code, String glsh_desc,
			String schm_code, String schm_desc, String interest_income, String interest_recivable, String fees_income,
			String penalty_income, String collection_account, String loan_parking_account, String entity_flg,
			String verify_flg, String modify_flg, String del_flg, String entry_user, String modify_user,
			String verify_user, Date entry_time, Date verify_time, String product_key) {
		super();
		this.id = id;
		this.gl_code = gl_code;
		this.gl_desc = gl_desc;
		this.glsh_code = glsh_code;
		this.glsh_desc = glsh_desc;
		this.schm_code = schm_code;
		this.schm_desc = schm_desc;
		this.interest_income = interest_income;
		this.interest_recivable = interest_recivable;
		this.fees_income = fees_income;
		this.penalty_income = penalty_income;
		this.collection_account = collection_account;
		this.loan_parking_account = loan_parking_account;
		this.entity_flg = entity_flg;
		this.verify_flg = verify_flg;
		this.modify_flg = modify_flg;
		this.del_flg = del_flg;
		this.entry_user = entry_user;
		this.modify_user = modify_user;
		this.verify_user = verify_user;
		this.entry_time = entry_time;
		this.verify_time = verify_time;
		this.product_key = product_key;
	}
	public Transaction_accounts_entity() {
		super();
		// TODO Auto-generated constructor stub
	}
}
