package com.bornfire.entities;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MULTIPLE_TRANSACTION_HISTORY_TBL")
public class MULTIPLE_TRANSACTION_HISTORY_ENTITY {
	private String	transaction_id;
	private String	names;
	private String	reference;
	private String	mobile_number;
	private BigDecimal	amount;
	private BigDecimal	allocated_amount;
	private Date	trans_time;
	private String	status;
	private String	auth_flg;
	private Date	auth_time;
	private String	auth_user;
	private String	del_flg;
	private String	entity_flg;
	private Date	entry_time;
	private String	entry_user;
	private String	modify_flg;
	private Date	modify_time;
	private String	modify_user;
	@Id
	private String	srl_no;
	private String	ref_transaction_id;
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAllocated_amount() {
		return allocated_amount;
	}
	public void setAllocated_amount(BigDecimal allocated_amount) {
		this.allocated_amount = allocated_amount;
	}
	public Date getTrans_time() {
		return trans_time;
	}
	public void setTrans_time(Date trans_time) {
		this.trans_time = trans_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuth_flg() {
		return auth_flg;
	}
	public void setAuth_flg(String auth_flg) {
		this.auth_flg = auth_flg;
	}
	public Date getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(Date auth_time) {
		this.auth_time = auth_time;
	}
	public String getAuth_user() {
		return auth_user;
	}
	public void setAuth_user(String auth_user) {
		this.auth_user = auth_user;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public Date getEntry_time() {
		return entry_time;
	}
	public void setEntry_time(Date entry_time) {
		this.entry_time = entry_time;
	}
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getSrl_no() {
		return srl_no;
	}
	public void setSrl_no(String srl_no) {
		this.srl_no = srl_no;
	}
	public String getRef_transaction_id() {
		return ref_transaction_id;
	}
	public void setRef_transaction_id(String ref_transaction_id) {
		this.ref_transaction_id = ref_transaction_id;
	}
	public MULTIPLE_TRANSACTION_HISTORY_ENTITY(String transaction_id, String names, String reference,
			String mobile_number, BigDecimal amount, BigDecimal allocated_amount, Date trans_time, String status,
			String auth_flg, Date auth_time, String auth_user, String del_flg, String entity_flg, Date entry_time,
			String entry_user, String modify_flg, Date modify_time, String modify_user, String srl_no,
			String ref_transaction_id) {
		super();
		this.transaction_id = transaction_id;
		this.names = names;
		this.reference = reference;
		this.mobile_number = mobile_number;
		this.amount = amount;
		this.allocated_amount = allocated_amount;
		this.trans_time = trans_time;
		this.status = status;
		this.auth_flg = auth_flg;
		this.auth_time = auth_time;
		this.auth_user = auth_user;
		this.del_flg = del_flg;
		this.entity_flg = entity_flg;
		this.entry_time = entry_time;
		this.entry_user = entry_user;
		this.modify_flg = modify_flg;
		this.modify_time = modify_time;
		this.modify_user = modify_user;
		this.srl_no = srl_no;
		this.ref_transaction_id = ref_transaction_id;
	}
	public MULTIPLE_TRANSACTION_HISTORY_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
}
