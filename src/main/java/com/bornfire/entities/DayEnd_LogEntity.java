package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "DAYEND_LOG")
public class DayEnd_LogEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "day_end_log_seq")
    @SequenceGenerator(
        name = "day_end_log_seq",
        sequenceName = "DAY_END_LOG_TABLE_SEQ", // Oracle sequence name
        allocationSize = 1 
    )
    private Long logId;
    private String processName;
    private String stepDesc;
    private String status;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getStepDesc() {
		return stepDesc;
	}
	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public DayEnd_LogEntity(Long logId, String processName, String stepDesc, String status, Date createdAt) {
		super();
		this.logId = logId;
		this.processName = processName;
		this.stepDesc = stepDesc;
		this.status = status;
		this.createdAt = createdAt;
	}
	public DayEnd_LogEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "DayEnd_LogEntity [logId=" + logId + ", processName=" + processName + ", stepDesc=" + stepDesc
				+ ", status=" + status + ", createdAt=" + createdAt + "]";
	}
	
    
    

}
