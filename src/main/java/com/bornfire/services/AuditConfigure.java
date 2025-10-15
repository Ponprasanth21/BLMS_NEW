package com.bornfire.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bornfire.config.SequenceGenerator;
import com.bornfire.entities.*;
import com.bornfire.entities.AuditTablePojo;

@Service
public class AuditConfigure {
	
	    @Autowired
	    BGLSAuditTable_Rep BGLSAuditTable_Rep;
		
		@Autowired
		SessionFactory sessionFactory;
		
		@Autowired
		private SequenceGenerator sequence;
		
		@Autowired
		BGLSBusinessTable_Rep BGLSBusinessTable_Rep;
		

		//user audit method
		public List<AuditTablePojo> getauditListLocal(Date fromdate1) {
		    List<BGLSAuditTable> auditList = BGLSAuditTable_Rep.getauditListLocal(fromdate1);
		    List<AuditTablePojo> auditPojoList = new ArrayList<>();

		    for (BGLSAuditTable ipsAudit : auditList) {
		        boolean isUpdated = false;

		        for (AuditTablePojo existingPojo : auditPojoList) {
		            String auditRefNo = existingPojo.getAudit_ref_no();
		            String remarks = existingPojo.getRemarks();
		            String ipsAuditNo = ipsAudit.getAudit_ref_no();

		            if (auditRefNo != null && ipsAuditNo != null
		                    && auditRefNo.equals(ipsAuditNo)
		                    && remarks != null
		                    && ("LOGGED IN SUCCESSFULLY".equalsIgnoreCase(remarks)
		                        || "LOGGED OUT SUCCESSFULLY".equalsIgnoreCase(remarks))) {

		                // Update existing pojo
		                updateAuditPojo(existingPojo, ipsAudit);
		                isUpdated = true;
		                break;
		            }
		        }

		        // If no existing entry was updated, create a new one
		        if (!isUpdated) {
		            AuditTablePojo auditTablePojo = new AuditTablePojo();
		            updateAuditPojo(auditTablePojo, ipsAudit);
		            auditPojoList.add(auditTablePojo);
		        }
		    }

		    return auditPojoList;
		}

		/**
		 * Helper method to populate or update AuditTablePojo safely
		 */
		private void updateAuditPojo(AuditTablePojo pojo, BGLSAuditTable entity) {
		    pojo.setAudit_date(entity.getAudit_date());
		    pojo.setAudit_table(entity.getAudit_table());
		    pojo.setFunc_code(entity.getFunc_code());
		    pojo.setEntry_user(entity.getEntry_user());
		    pojo.setEntry_time(entity.getEntry_time());
		    pojo.setAuth_user(entity.getAuth_user());
		    pojo.setAuth_time(entity.getAuth_time());
		    pojo.setRemarks(entity.getRemarks());

		    List<String> fieldName = new ArrayList<>();
		    List<String> oldValue = new ArrayList<>();
		    List<String> newValue = new ArrayList<>();

		    String modiDetails = entity.getModi_details();

		    // ✅ Null and empty check before splitting
		    if (modiDetails != null && !modiDetails.trim().isEmpty()) {
		        String[] dd = modiDetails.split("\\|\\|");
		        for (String str : dd) {
		            String[] str1 = str.split("\\+");
		            if (str1.length > 0) fieldName.add(str1[0]);
		            if (str1.length > 1) oldValue.add(str1[1]);
		            if (str1.length > 2) newValue.add(str1[2]);
		        }
		    } else {
		        // Optional fallback if modi_details is missing
		        fieldName.add("No Modification Details");
		        oldValue.add("-");
		        newValue.add("-");
		    }

		    pojo.setFieldName(fieldName);
		    pojo.setOldvalue(oldValue);
		    pojo.setNewvalue(newValue);
		}

			//service audit method
			public List<AuditTablePojo> getAuditInquries(Date date1) {
			    List<BGLSBusinessTable_Entity> auditList = BGLSBusinessTable_Rep.getauditListOpeartion(date1);
			    List<AuditTablePojo> auditPojoList = new ArrayList<>();
			    for (BGLSBusinessTable_Entity ipsAudit : auditList) {
			        AuditTablePojo auditTablePojo = new AuditTablePojo();
			        auditTablePojo.setAudit_date(ipsAudit.getAudit_date());
			        auditTablePojo.setAudit_table(ipsAudit.getAudit_table());
			        auditTablePojo.setFunc_code(ipsAudit.getFunc_code());
			        auditTablePojo.setEntry_user(ipsAudit.getEntry_user());
			        auditTablePojo.setEntry_time(ipsAudit.getEntry_time());
			        auditTablePojo.setAuth_user(ipsAudit.getAuth_user());
			        auditTablePojo.setRemarks(ipsAudit.getRemarks());
			        List<String> fieldName = new ArrayList<>();
			        List<String> oldvalue = new ArrayList<>();
			        List<String> newvalue = new ArrayList<>();
			        String modiDetails = ipsAudit.getModi_details();
			        // ✅ Check for null or empty before splitting
			        if (modiDetails != null && !modiDetails.trim().isEmpty()) {
			            String[] dd = modiDetails.split("\\|\\|");

			            for (String str : dd) {
			                String[] str1 = str.split("\\+");
			                if (str1.length > 0) {
			                    fieldName.add(str1[0]);
			                }
			                if (str1.length > 1) {
			                    oldvalue.add(str1[1]);
			                }
			                if (str1.length > 2) {
			                    newvalue.add(str1[2]);
			                }
			            }
			        } else {
			            // Optional: handle cases where modi_details is null or empty
			            fieldName.add("-");
			            oldvalue.add("-");
			            newvalue.add("-");
			        }
			        auditTablePojo.setFieldName(fieldName);
			        auditTablePojo.setOldvalue(oldvalue);
			        auditTablePojo.setNewvalue(newvalue);
			        auditPojoList.add(auditTablePojo);
			    }
			    return auditPojoList;
			}
			
		public void insertUserAudit(String userID, String username, String fun_code , String remarks , String table  ,String screen) {
		BGLSAuditTable audit = new BGLSAuditTable();
		audit.setAudit_date(new Date());
		audit.setEntry_time(new Date());
		audit.setEntry_user(userID);
		audit.setFunc_code(fun_code);
		audit.setRemarks(remarks);
		audit.setAudit_table(table);
		audit.setAudit_screen(screen);
		audit.setEvent_id(userID);
		audit.setEvent_name(username);
		audit.setModi_details("-");
		audit.setAudit_ref_no(sequence.generateRequestUUId());
		BGLSAuditTable_Rep.save(audit);
	}
		
		public void insertServiceAudit(String userID, String username, String fun_code , String remarks , String table  ,String screen,String field) {
			BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
			audit.setAudit_date(new Date());
			audit.setEntry_time(new Date());
			audit.setEntry_user(userID);
			audit.setFunc_code(fun_code);
			audit.setRemarks(remarks);
			audit.setAudit_table(table);
			audit.setAudit_screen(screen);
			audit.setEvent_id(userID);
			audit.setEvent_name(username);
			audit.setField_name(field);
			audit.setModi_details("-");
			audit.setAudit_ref_no(sequence.generateRequestUUId());
			BGLSBusinessTable_Rep.save(audit);
		}

}
