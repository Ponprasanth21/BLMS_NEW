package com.bornfire.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;

@Entity
@Table(name = "BGLS_GENERAL_LED")
public class GeneralLedgerEntity {
    @Column(name="GL_CODE")
    private String glCode;
    @Column(name="GL_DESCRIPTION")
    private String glDescription;
    @Column(name="REMARKS")
    private String remarks;
    @Column(name="DEL_FLG")
    private String delFlg;
    @Column(name="MODIFY_FLG")
    private String modifyFlg;
    private String branch_id;
    private String branch_desc;
    @Id
    private String glsh_code;
    private String glsh_desc;
    private String crncy_code;
    private String bal_sheet_group;
    private String seq_order;
    private String gl_type;
    private String gl_type_description;
    private String module;
    private String entity_flg;
    private String entry_user;
    private String modify_user;
    private String verify_flg;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	entry_time;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	modify_time;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	verify_time;
    private String total_balance;
    private String no_acct_opened;
    private String no_acct_closed;
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public String getGlDescription() {
		return glDescription;
	}
	public void setGlDescription(String glDescription) {
		this.glDescription = glDescription;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public String getBranch_desc() {
		return branch_desc;
	}
	public void setBranch_desc(String branch_desc) {
		this.branch_desc = branch_desc;
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
	public String getCrncy_code() {
		return crncy_code;
	}
	public void setCrncy_code(String crncy_code) {
		this.crncy_code = crncy_code;
	}
	public String getBal_sheet_group() {
		return bal_sheet_group;
	}
	public void setBal_sheet_group(String bal_sheet_group) {
		this.bal_sheet_group = bal_sheet_group;
	}
	public String getSeq_order() {
		return seq_order;
	}
	public void setSeq_order(String seq_order) {
		this.seq_order = seq_order;
	}
	public String getGl_type() {
		return gl_type;
	}
	public void setGl_type(String gl_type) {
		this.gl_type = gl_type;
	}
	public String getGl_type_description() {
		return gl_type_description;
	}
	public void setGl_type_description(String gl_type_description) {
		this.gl_type_description = gl_type_description;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
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
	public String getVerify_flg() {
		return verify_flg;
	}
	public void setVerify_flg(String verify_flg) {
		this.verify_flg = verify_flg;
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
	public Date getVerify_time() {
		return verify_time;
	}
	public void setVerify_time(Date verify_time) {
		this.verify_time = verify_time;
	}
	public String getTotal_balance() {
		return total_balance;
	}
	public void setTotal_balance(String total_balance) {
		this.total_balance = total_balance;
	}
	public String getNo_acct_opened() {
		return no_acct_opened;
	}
	public void setNo_acct_opened(String no_acct_opened) {
		this.no_acct_opened = no_acct_opened;
	}
	public String getNo_acct_closed() {
		return no_acct_closed;
	}
	public void setNo_acct_closed(String no_acct_closed) {
		this.no_acct_closed = no_acct_closed;
	}
	public GeneralLedgerEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
