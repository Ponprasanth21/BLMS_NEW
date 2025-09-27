package com.bornfire.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BGLS_HOLIDAY_MASTER")
public class BglsHolidayMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming RECORD_SRL is the primary key
    @Column(name = "RECORD_SRL")
    private Integer recordSrl;

    @Column(name = "ORGN")
    private String orgn;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "CAL_YEAR")
    private String calYear;

    @Column(name = "CAL_MONTH")
    private String calMonth;

    @Temporal(TemporalType.DATE)
    @Column(name = "RECORD_DATE")
    private Date recordDate;

    @Column(name = "HOLIDAY_DESC")
    private String holidayDesc;

    @Column(name = "HOLIDAY_REMARKS")
    private String holidayRemarks;

    @Column(name = "HOLIDAY_FLG")
    private String holidayFlg;

    @Column(name = "ENTRY_USER")
    private String entryUser;

    @Column(name = "MODIFY_USER")
    private String modifyUser;

    @Column(name = "AUTH_USER")
    private String authUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENTRY_TIME")
    private Date entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AUTH_TIME")
    private Date authTime;

    @Column(name = "DEL_FLG")
    private String delFlg;

    @Column(name = "ENTITY_FLG")
    private String entityFlg;

    @Column(name = "MODIFY_FLG")
    private String modifyFlg;

    @Column(name = "RECORD_STATUS")
    private String recordStatus;

    // Getters and Setters
    public Integer getRecordSrl() {
        return recordSrl;
    }

    public void setRecordSrl(Integer recordSrl) {
        this.recordSrl = recordSrl;
    }

    public String getOrgn() {
        return orgn;
    }

    public void setOrgn(String orgn) {
        this.orgn = orgn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCalYear() {
        return calYear;
    }

    public void setCalYear(String calYear) {
        this.calYear = calYear;
    }

    public String getCalMonth() {
        return calMonth;
    }

    public void setCalMonth(String calMonth) {
        this.calMonth = calMonth;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getHolidayDesc() {
        return holidayDesc;
    }

    public void setHolidayDesc(String holidayDesc) {
        this.holidayDesc = holidayDesc;
    }

    public String getHolidayRemarks() {
        return holidayRemarks;
    }

    public void setHolidayRemarks(String holidayRemarks) {
        this.holidayRemarks = holidayRemarks;
    }

    public String getHolidayFlg() {
        return holidayFlg;
    }

    public void setHolidayFlg(String holidayFlg) {
        this.holidayFlg = holidayFlg;
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

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
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

    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
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

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }
}