package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="ASPIRA_LOAN_REPAYMENT_TABLE")
public class ASPIRA_LOAN_REPAYMENT_ENTITY {
	    @Id
	    private String encodedkey;
	    private String assignedbranchkey;
	    private String assigneduserkey;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date duedate;
	    private BigDecimal interestdue;
	    private BigDecimal interestpaid;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date lastpaiddate;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date lastpenaltyapplieddate;
	    private String notes;
	    private String parentaccountkey;
	    private BigDecimal principaldue;
	    private BigDecimal principalpaid;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date repaiddate;
	    private String state;
	    private String assignedcentrekey;
	    private BigDecimal feesdue;
	    private BigDecimal feespaid;
	    private BigDecimal penaltydue;
	    private BigDecimal penaltypaid;
	    private BigDecimal taxinterestdue;
	    private BigDecimal taxinterestpaid;
	    private BigDecimal taxfeesdue;
	    private BigDecimal taxfeespaid;
	    private BigDecimal taxpenaltydue;
	    private BigDecimal taxpenaltypaid;
	    private BigDecimal organizationcommissiondue;
	    private BigDecimal fundersinterestdue;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date creationdate;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date lastmodifieddate;
	    private String additions;
	    private String entity_flg;
	    private String auth_flg;
	    private String modify_flg;
	    private String del_flg;
	    private String entry_user;
	    private String modify_user;
	    private String auth_user;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date entry_time;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date modify_time;
	    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	    private Date auth_time;
		public String getEncodedkey() {
			return encodedkey;
		}
		public void setEncodedkey(String encodedkey) {
			this.encodedkey = encodedkey;
		}
		public String getAssignedbranchkey() {
			return assignedbranchkey;
		}
		public void setAssignedbranchkey(String assignedbranchkey) {
			this.assignedbranchkey = assignedbranchkey;
		}
		public String getAssigneduserkey() {
			return assigneduserkey;
		}
		public void setAssigneduserkey(String assigneduserkey) {
			this.assigneduserkey = assigneduserkey;
		}
		public Date getDuedate() {
			return duedate;
		}
		public void setDuedate(Date duedate) {
			this.duedate = duedate;
		}
		public BigDecimal getInterestdue() {
			return interestdue;
		}
		public void setInterestdue(BigDecimal interestdue) {
			this.interestdue = interestdue;
		}
		public BigDecimal getInterestpaid() {
			return interestpaid;
		}
		public void setInterestpaid(BigDecimal interestpaid) {
			this.interestpaid = interestpaid;
		}
		public Date getLastpaiddate() {
			return lastpaiddate;
		}
		public void setLastpaiddate(Date lastpaiddate) {
			this.lastpaiddate = lastpaiddate;
		}
		public Date getLastpenaltyapplieddate() {
			return lastpenaltyapplieddate;
		}
		public void setLastpenaltyapplieddate(Date lastpenaltyapplieddate) {
			this.lastpenaltyapplieddate = lastpenaltyapplieddate;
		}
		public String getNotes() {
			return notes;
		}
		public void setNotes(String notes) {
			this.notes = notes;
		}
		public String getParentaccountkey() {
			return parentaccountkey;
		}
		public void setParentaccountkey(String parentaccountkey) {
			this.parentaccountkey = parentaccountkey;
		}
		public BigDecimal getPrincipaldue() {
			return principaldue;
		}
		public void setPrincipaldue(BigDecimal principaldue) {
			this.principaldue = principaldue;
		}
		public BigDecimal getPrincipalpaid() {
			return principalpaid;
		}
		public void setPrincipalpaid(BigDecimal principalpaid) {
			this.principalpaid = principalpaid;
		}
		public Date getRepaiddate() {
			return repaiddate;
		}
		public void setRepaiddate(Date repaiddate) {
			this.repaiddate = repaiddate;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getAssignedcentrekey() {
			return assignedcentrekey;
		}
		public void setAssignedcentrekey(String assignedcentrekey) {
			this.assignedcentrekey = assignedcentrekey;
		}
		public BigDecimal getFeesdue() {
			return feesdue;
		}
		public void setFeesdue(BigDecimal feesdue) {
			this.feesdue = feesdue;
		}
		public BigDecimal getFeespaid() {
			return feespaid;
		}
		public void setFeespaid(BigDecimal feespaid) {
			this.feespaid = feespaid;
		}
		public BigDecimal getPenaltydue() {
			return penaltydue;
		}
		public void setPenaltydue(BigDecimal penaltydue) {
			this.penaltydue = penaltydue;
		}
		public BigDecimal getPenaltypaid() {
			return penaltypaid;
		}
		public void setPenaltypaid(BigDecimal penaltypaid) {
			this.penaltypaid = penaltypaid;
		}
		public BigDecimal getTaxinterestdue() {
			return taxinterestdue;
		}
		public void setTaxinterestdue(BigDecimal taxinterestdue) {
			this.taxinterestdue = taxinterestdue;
		}
		public BigDecimal getTaxinterestpaid() {
			return taxinterestpaid;
		}
		public void setTaxinterestpaid(BigDecimal taxinterestpaid) {
			this.taxinterestpaid = taxinterestpaid;
		}
		public BigDecimal getTaxfeesdue() {
			return taxfeesdue;
		}
		public void setTaxfeesdue(BigDecimal taxfeesdue) {
			this.taxfeesdue = taxfeesdue;
		}
		public BigDecimal getTaxfeespaid() {
			return taxfeespaid;
		}
		public void setTaxfeespaid(BigDecimal taxfeespaid) {
			this.taxfeespaid = taxfeespaid;
		}
		public BigDecimal getTaxpenaltydue() {
			return taxpenaltydue;
		}
		public void setTaxpenaltydue(BigDecimal taxpenaltydue) {
			this.taxpenaltydue = taxpenaltydue;
		}
		public BigDecimal getTaxpenaltypaid() {
			return taxpenaltypaid;
		}
		public void setTaxpenaltypaid(BigDecimal taxpenaltypaid) {
			this.taxpenaltypaid = taxpenaltypaid;
		}
		public BigDecimal getOrganizationcommissiondue() {
			return organizationcommissiondue;
		}
		public void setOrganizationcommissiondue(BigDecimal organizationcommissiondue) {
			this.organizationcommissiondue = organizationcommissiondue;
		}
		public BigDecimal getFundersinterestdue() {
			return fundersinterestdue;
		}
		public void setFundersinterestdue(BigDecimal fundersinterestdue) {
			this.fundersinterestdue = fundersinterestdue;
		}
		public Date getCreationdate() {
			return creationdate;
		}
		public void setCreationdate(Date creationdate) {
			this.creationdate = creationdate;
		}
		public Date getLastmodifieddate() {
			return lastmodifieddate;
		}
		public void setLastmodifieddate(Date lastmodifieddate) {
			this.lastmodifieddate = lastmodifieddate;
		}
		public String getAdditions() {
			return additions;
		}
		public void setAdditions(String additions) {
			this.additions = additions;
		}
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
		public ASPIRA_LOAN_REPAYMENT_ENTITY(String encodedkey, String assignedbranchkey, String assigneduserkey,
				Date duedate, BigDecimal interestdue, BigDecimal interestpaid, Date lastpaiddate,
				Date lastpenaltyapplieddate, String notes, String parentaccountkey, BigDecimal principaldue,
				BigDecimal principalpaid, Date repaiddate, String state, String assignedcentrekey, BigDecimal feesdue,
				BigDecimal feespaid, BigDecimal penaltydue, BigDecimal penaltypaid, BigDecimal taxinterestdue,
				BigDecimal taxinterestpaid, BigDecimal taxfeesdue, BigDecimal taxfeespaid, BigDecimal taxpenaltydue,
				BigDecimal taxpenaltypaid, BigDecimal organizationcommissiondue, BigDecimal fundersinterestdue,
				Date creationdate, Date lastmodifieddate, String additions, String entity_flg, String auth_flg,
				String modify_flg, String del_flg, String entry_user, String modify_user, String auth_user,
				Date entry_time, Date modify_time, Date auth_time) {
			super();
			this.encodedkey = encodedkey;
			this.assignedbranchkey = assignedbranchkey;
			this.assigneduserkey = assigneduserkey;
			this.duedate = duedate;
			this.interestdue = interestdue;
			this.interestpaid = interestpaid;
			this.lastpaiddate = lastpaiddate;
			this.lastpenaltyapplieddate = lastpenaltyapplieddate;
			this.notes = notes;
			this.parentaccountkey = parentaccountkey;
			this.principaldue = principaldue;
			this.principalpaid = principalpaid;
			this.repaiddate = repaiddate;
			this.state = state;
			this.assignedcentrekey = assignedcentrekey;
			this.feesdue = feesdue;
			this.feespaid = feespaid;
			this.penaltydue = penaltydue;
			this.penaltypaid = penaltypaid;
			this.taxinterestdue = taxinterestdue;
			this.taxinterestpaid = taxinterestpaid;
			this.taxfeesdue = taxfeesdue;
			this.taxfeespaid = taxfeespaid;
			this.taxpenaltydue = taxpenaltydue;
			this.taxpenaltypaid = taxpenaltypaid;
			this.organizationcommissiondue = organizationcommissiondue;
			this.fundersinterestdue = fundersinterestdue;
			this.creationdate = creationdate;
			this.lastmodifieddate = lastmodifieddate;
			this.additions = additions;
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
			return "ASPIRA_LOAN_REPAYMENT_ENTITY [encodedkey=" + encodedkey + ", assignedbranchkey=" + assignedbranchkey
					+ ", assigneduserkey=" + assigneduserkey + ", duedate=" + duedate + ", interestdue=" + interestdue
					+ ", interestpaid=" + interestpaid + ", lastpaiddate=" + lastpaiddate + ", lastpenaltyapplieddate="
					+ lastpenaltyapplieddate + ", notes=" + notes + ", parentaccountkey=" + parentaccountkey
					+ ", principaldue=" + principaldue + ", principalpaid=" + principalpaid + ", repaiddate="
					+ repaiddate + ", state=" + state + ", assignedcentrekey=" + assignedcentrekey + ", feesdue="
					+ feesdue + ", feespaid=" + feespaid + ", penaltydue=" + penaltydue + ", penaltypaid=" + penaltypaid
					+ ", taxinterestdue=" + taxinterestdue + ", taxinterestpaid=" + taxinterestpaid + ", taxfeesdue="
					+ taxfeesdue + ", taxfeespaid=" + taxfeespaid + ", taxpenaltydue=" + taxpenaltydue
					+ ", taxpenaltypaid=" + taxpenaltypaid + ", organizationcommissiondue=" + organizationcommissiondue
					+ ", fundersinterestdue=" + fundersinterestdue + ", creationdate=" + creationdate
					+ ", lastmodifieddate=" + lastmodifieddate + ", additions=" + additions + ", entity_flg="
					+ entity_flg + ", auth_flg=" + auth_flg + ", modify_flg=" + modify_flg + ", del_flg=" + del_flg
					+ ", entry_user=" + entry_user + ", modify_user=" + modify_user + ", auth_user=" + auth_user
					+ ", entry_time=" + entry_time + ", modify_time=" + modify_time + ", auth_time=" + auth_time + "]";
		}
		public ASPIRA_LOAN_REPAYMENT_ENTITY() {
			super();
			// TODO Auto-generated constructor stub
		}
	    

}
