package com.bornfire.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BGLS_ORG_BRANCH")
public class BGLS_ORG_BRANCH_ENTITY {
	@Id
	private String branchCode;
	private String add1;
	private String add2;
	private String branchHead;
	private String branch_name;
	private String city;
	private String contPerson;
	private String country;
	private String delFlg;
	private String designation;
	private String entityFlg;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date entryTime;
	private String entryUser;
	private String fax;
	private String landLine;
	private String mailId;
	private String mobile;
	private String modifyFlg;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date modifyTime;
	private String modifyUser;
	private String photo;
	private String picNo;
	private String remarks;
	private String state;
	private String swiftCode;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private Date verifyTime;
	private String verifyUser;
	private String website;
	private String zipCode;
	private String encoded_key;
	

	public String getEncoded_key() {
		return encoded_key;
	}

	public void setEncoded_key(String encoded_key) {
		this.encoded_key = encoded_key;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAdd1() {
		return add1;
	}

	public void setAdd1(String add1) {
		this.add1 = add1;
	}

	public String getAdd2() {
		return add2;
	}

	public void setAdd2(String add2) {
		this.add2 = add2;
	}

	public String getBranchHead() {
		return branchHead;
	}

	public void setBranchHead(String branchHead) {
		this.branchHead = branchHead;
	}


	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContPerson() {
		return contPerson;
	}

	public void setContPerson(String contPerson) {
		this.contPerson = contPerson;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEntityFlg() {
		return entityFlg;
	}

	public void setEntityFlg(String entityFlg) {
		this.entityFlg = entityFlg;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getLandLine() {
		return landLine;
	}

	public void setLandLine(String landLine) {
		this.landLine = landLine;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getModifyFlg() {
		return modifyFlg;
	}

	public void setModifyFlg(String modifyFlg) {
		this.modifyFlg = modifyFlg;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPicNo() {
		return picNo;
	}

	public void setPicNo(String picNo) {
		this.picNo = picNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	

	public BGLS_ORG_BRANCH_ENTITY(String branchCode, String add1, String add2, String branchHead, String branch_name,
			String city, String contPerson, String country, String delFlg, String designation, String entityFlg,
			Date entryTime, String entryUser, String fax, String landLine, String mailId, String mobile,
			String modifyFlg, Date modifyTime, String modifyUser, String photo, String picNo, String remarks,
			String state, String swiftCode, Date verifyTime, String verifyUser, String website, String zipCode,
			String encoded_key) {
		super();
		this.branchCode = branchCode;
		this.add1 = add1;
		this.add2 = add2;
		this.branchHead = branchHead;
		this.branch_name = branch_name;
		this.city = city;
		this.contPerson = contPerson;
		this.country = country;
		this.delFlg = delFlg;
		this.designation = designation;
		this.entityFlg = entityFlg;
		this.entryTime = entryTime;
		this.entryUser = entryUser;
		this.fax = fax;
		this.landLine = landLine;
		this.mailId = mailId;
		this.mobile = mobile;
		this.modifyFlg = modifyFlg;
		this.modifyTime = modifyTime;
		this.modifyUser = modifyUser;
		this.photo = photo;
		this.picNo = picNo;
		this.remarks = remarks;
		this.state = state;
		this.swiftCode = swiftCode;
		this.verifyTime = verifyTime;
		this.verifyUser = verifyUser;
		this.website = website;
		this.zipCode = zipCode;
		this.encoded_key = encoded_key;
	}

	@Override
	public String toString() {
		return "BGLS_ORG_BRANCH_ENTITY [branchCode=" + branchCode + ", add1=" + add1 + ", add2=" + add2
				+ ", branchHead=" + branchHead + ", branchName=" + branch_name + ", city=" + city + ", contPerson="
				+ contPerson + ", country=" + country + ", delFlg=" + delFlg + ", designation=" + designation
				+ ", entityFlg=" + entityFlg + ", entryTime=" + entryTime + ", entryUser=" + entryUser + ", fax=" + fax
				+ ", landLine=" + landLine + ", mailId=" + mailId + ", mobile=" + mobile + ", modifyFlg=" + modifyFlg
				+ ", modifyTime=" + modifyTime + ", modifyUser=" + modifyUser + ", photo=" + photo + ", picNo=" + picNo
				+ ", remarks=" + remarks + ", state=" + state + ", swiftCode=" + swiftCode + ", verifyTime="
				+ verifyTime + ", verifyUser=" + verifyUser + ", website=" + website + ", zipCode=" + zipCode
				+ ", encoded_key=" + encoded_key + "]";
	}

	public BGLS_ORG_BRANCH_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}
}
