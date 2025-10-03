package com.bornfire.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.entities.ASPIRA_LOAN_REPAYMENT_ENTITY;
import com.bornfire.entities.ASPIRA_LOAN_REPAYMENT_REPO;
import com.bornfire.entities.BGLSAuditTable;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.LOAN_ACT_MST_ENTITY;
import com.bornfire.entities.LOAN_ACT_MST_REPO;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@ConfigurationProperties("output")
@Transactional
public class ExelDownloadService {
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	ASPIRA_LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;
	
	@Autowired
	LOAN_ACT_MST_REPO loanMasterRepo;
	
	@Autowired
	DateParser DateParser;
	
	@Autowired
	BGLSAuditTable_Rep AuditTable_Rep;
	
	@Autowired
	CLIENT_MASTER_REPO clientMasterRepo;
	

//	public void ExportExcel(String type, String userID, String userName, String auditRefNo,
//			HttpServletResponse response) {
//
//		try (Workbook workbook = new XSSFWorkbook()) {
//			Sheet sheet = workbook.createSheet("Data");
//			int rowIdx = 0;
//
//			if ("REPAYMENT".equalsIgnoreCase(type)) {
//				List<ASPIRA_LOAN_REPAYMENT_ENTITY> dataList = lOAN_REPAYMENT_REPO.findAll();
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "ENCODEDKEY","ASSIGNEDBRANCHKEY","ASSIGNEDUSERKEY","DUEDATE","INTERESTDUE","INTERESTPAID","LASTPAIDDATE",
//					    "LASTPENALTYAPPLIEDDATE", "NOTES","PARENTACCOUNTKEY","PRINCIPALDUE", "PRINCIPALPAID", "REPAIDDATE", "STATE",
//					    "ASSIGNEDCENTREKEY","FEESDUE","FEESPAID","PENALTYDUE","PENALTYPAID","TAXINTERESTDUE","TAXINTERESTPAID",
//					    "TAXFEESDUE", "TAXFEESPAID","TAXPENALTYDUE","TAXPENALTYPAID","ORGANIZATIONCOMMISSIONDUE",
//					    "FUNDERSINTERESTDUE","CREATIONDATE","LASTMODIFIEDDATE","ADDITIONS"
//					};
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size");
//				for (ASPIRA_LOAN_REPAYMENT_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					// String values
//					System.out.println(entity.getEncodedkey()+"  --Encode Key");
//					excelRow.createCell(0).setCellValue(entity.getEncodedkey());
//					excelRow.createCell(1).setCellValue(entity.getAssignedbranchkey());
//					excelRow.createCell(2).setCellValue(entity.getAssigneduserkey());
//
//					// Date values (format to String)
//					excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDuedate()));
//
//					// BigDecimal values (convert to String or double)
//					excelRow.createCell(4).setCellValue(entity.getInterestdue() == null ? "" : entity.getInterestdue().toPlainString());
//					excelRow.createCell(5).setCellValue(entity.getInterestpaid() == null ? "" : entity.getInterestpaid().toPlainString());
//
//					// More dates
//					excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpaiddate()));
//					excelRow.createCell(7).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpenaltyapplieddate()));
//
//					// Notes
//					excelRow.createCell(8).setCellValue(entity.getNotes());
//					excelRow.createCell(9).setCellValue(entity.getParentaccountkey());
//
//					// Principal
//					excelRow.createCell(10).setCellValue(entity.getPrincipaldue() == null ? "" : entity.getPrincipaldue().toPlainString());
//					excelRow.createCell(11).setCellValue(entity.getPrincipalpaid() == null ? "" : entity.getPrincipalpaid().toPlainString());
//
//					// Repaid date & state
//					excelRow.createCell(12).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getRepaiddate()));
//					excelRow.createCell(13).setCellValue(entity.getState());
//
//					// Centre key
//					excelRow.createCell(14).setCellValue(entity.getAssignedcentrekey());
//
//					// Fees
//					excelRow.createCell(15).setCellValue(entity.getFeesdue() == null ? "" : entity.getFeesdue().toPlainString());
//					excelRow.createCell(16).setCellValue(entity.getFeespaid() == null ? "" : entity.getFeespaid().toPlainString());
//
//					// Penalty
//					excelRow.createCell(17).setCellValue(entity.getPenaltydue() == null ? "" : entity.getPenaltydue().toPlainString());
//					excelRow.createCell(18).setCellValue(entity.getPenaltypaid() == null ? "" : entity.getPenaltypaid().toPlainString());
//
//					// Tax Interest
//					excelRow.createCell(19).setCellValue(entity.getTaxinterestdue() == null ? "" : entity.getTaxinterestdue().toPlainString());
//					excelRow.createCell(20).setCellValue(entity.getTaxinterestpaid() == null ? "" : entity.getTaxinterestpaid().toPlainString());
//
//					// Tax Fees
//					excelRow.createCell(21).setCellValue(entity.getTaxfeesdue() == null ? "" : entity.getTaxfeesdue().toPlainString());
//					excelRow.createCell(22).setCellValue(entity.getTaxfeespaid() == null ? "" : entity.getTaxfeespaid().toPlainString());
//
//					// Tax Penalty
//					excelRow.createCell(23).setCellValue(entity.getTaxpenaltydue() == null ? "" : entity.getTaxpenaltydue().toPlainString());
//					excelRow.createCell(24).setCellValue(entity.getTaxpenaltypaid() == null ? "" : entity.getTaxpenaltypaid().toPlainString());
//
//					// Org & Funders
//					excelRow.createCell(25).setCellValue(entity.getOrganizationcommissiondue() == null ? "" : entity.getOrganizationcommissiondue().toPlainString());
//					excelRow.createCell(26).setCellValue(entity.getFundersinterestdue() == null ? "" : entity.getFundersinterestdue().toPlainString());
//
//					// Creation & Last modified
//					excelRow.createCell(27).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreationdate()));
//					excelRow.createCell(28).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastmodifieddate()));
//
//					// Additions
//					excelRow.createCell(29).setCellValue(entity.getAdditions());
//
//				}
//
//				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
//				response.setHeader("Content-Disposition", "inline; filename=source_data.xlsx");
//
//			}   else if ("CUSTOMER".equalsIgnoreCase(type)) {
//				List<CLIENT_MASTER_ENTITY> dataList = clientMasterRepo.findAll();
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//						// Customer details
//						"ENCODEDKEY", "ID", "CLIENTSTATE", "CREATIONDATE", "LASTMODIFIEDDATE", "ACTIVATIONDATE",
//						"APPROVEDDATE", "FIRSTNAME", "LASTNAME", "MOBILEPHONE", "EMAILADDRESS", "PREFERREDLANGUAGE",
//						"BIRTHDATE", "GENDER", "ASSIGNEDBRANCHKEY", "CLIENTROLEKEY", "LOANCYCLE", "GROUPLOANCYCLE",
//						"ADDRESSLINE1", "ADDRESSLINE2", "ADDRESSLINE3", "CITY", "SUBURB", "ASSIGNEDUSERKEY",
//						"ASONDATE" };
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//
//				for (CLIENT_MASTER_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					excelRow.createCell(0).setCellValue(entity.getEncoded_key());
//					excelRow.createCell(1).setCellValue(entity.getCustomer_id());
//					excelRow.createCell(2).setCellValue(entity.getClient_state());
//					excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreation_date()));
//					excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_modified_date()));
//					excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getActivation_date()));
//					excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getApproved_date()));
//					excelRow.createCell(7).setCellValue(entity.getFirst_name());
//					excelRow.createCell(8).setCellValue(entity.getLast_name());
//					excelRow.createCell(9).setCellValue(entity.getMobile_phone());
//					excelRow.createCell(10).setCellValue(entity.getEmail_address());
//					excelRow.createCell(11).setCellValue(entity.getPreferred_language());
//					excelRow.createCell(12).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getBirth_date()));
//					excelRow.createCell(13).setCellValue(entity.getGender());
//					excelRow.createCell(14).setCellValue(entity.getAssigned_branch_key());
//					excelRow.createCell(15).setCellValue(entity.getClient_role_key());
//					excelRow.createCell(16)
//							.setCellValue(entity.getLoan_cycle() == null ? "" : entity.getLoan_cycle().toPlainString());
//					excelRow.createCell(17)
//					.setCellValue(entity.getGroup_loan_cycle() == null ? "" : entity.getGroup_loan_cycle().toPlainString());
//					excelRow.createCell(18).setCellValue(entity.getAddress_line1());
//					excelRow.createCell(19).setCellValue(entity.getAddress_line2());
//					excelRow.createCell(20).setCellValue(entity.getAddress_line3());
//					excelRow.createCell(21).setCellValue(entity.getCity());
//					excelRow.createCell(22).setCellValue(entity.getSuburb());
//					excelRow.createCell(23).setCellValue(entity.getAssigned_user_key());
//					excelRow.createCell(24)
//							.setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getAsondate()));
//				}
//
//				saveAudit(userID, userName, "Customer File Download!", "CLIENT_MASTER_TBL", auditRefNo);
//				response.setHeader("Content-Disposition", "inline; filename=source_data.xlsx");
//
//			} else if ("LOAN".equalsIgnoreCase(type)) {
//				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.findAll();
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "ENCODEDKEY","ID","ACCOUNTHOLDERTYPE","ACCOUNTHOLDERKEY","CREATIONDATE","APPROVEDDATE","LASTMODIFIEDDATE","CLOSEDDATE","LASTACCOUNTAPPRAISALDATE","ACCOUNTSTATE","ACCOUNTSUBSTATE",
//					    "PRODUCTTYPEKEY","LOANNAME","PAYMENTMETHOD","ASSIGNEDBRANCHKEY","LOANAMOUNT","INTERESTRATE","PENALTYRATE","ACCRUEDINTEREST","ACCRUEDPENALTY","PRINCIPALDUE",
//					    "PRINCIPALPAID","PRINCIPALBALANCE","INTERESTDUE","INTERESTPAID","INTERESTBALANCE","INTERESTFROMARREARSBALANCE","INTERESTFROMARREARSDUE","INTERESTFROMARREARSPAID",
//					    "FEESDUE","FEESPAID","FEESBALANCE","PENALTYDUE","PENALTYPAID","PENALTYBALANCE","EXPECTEDDISBURSEMENTDATE","DISBURSEMENTDATE","FIRSTREPAYMENTDATE","GRACEPERIOD",
//					    "REPAYMENTINSTALLMENTS","REPAYMENTPERIODCOUNT","DAYSLATE","DAYSINARREARS","REPAYMENTSCHEDULEMETHOD","CURRENCYCODE","SALEPROCESSEDBYVGID","SALEPROCESSEDFOR",
//					    "SALEREFERREDBY","EMPLOYMENTSTATUS","JOBTITLE","EMPLOYERNAME","TUSCORE","TUPROBABILITY","TUFULLNAME","TUREASON1","TUREASON2","TUREASON3","TUREASON4",
//					    "DISPOSABLEINCOME","MANUALOVERRIDEAMOUNT","MANUALOVERRIDEEXPIRYDATE","CPFEES","DEPOSITAMOUNT","TOTALPRODUCTPRICE","RETAILERNAME","RETAILERBRANCH","VGAPPLICATIONID",
//					    "CONTRACTSIGNED","DATEOFFIRSTCALL","LASTCALLOUTCOME","ASONDATE"
//					};
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size of Loan Master");
//				for (LOAN_ACT_MST_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					excelRow.createCell(0).setCellValue(entity.getEncoded_key());
//					excelRow.createCell(1).setCellValue(entity.getId());
//					excelRow.createCell(2).setCellValue(entity.getAccount_holdertype());
//					excelRow.createCell(3).setCellValue(entity.getAccount_holderkey());
//					excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreation_date()));
//					excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getApproved_date()));
//					excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_modified_date()));
//					excelRow.createCell(7).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getClosed_date()));
//					excelRow.createCell(8).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_account_appraisaldate()));
//					excelRow.createCell(9).setCellValue(entity.getAccount_state());
//					excelRow.createCell(10).setCellValue(entity.getAccount_substate());
//					excelRow.createCell(11).setCellValue(entity.getProduct_typekey());
//					excelRow.createCell(12).setCellValue(entity.getLoan_name());
//					excelRow.createCell(13).setCellValue(entity.getPayment_method());
//					excelRow.createCell(14).setCellValue(entity.getAssigned_branchkey());
//
//					// Numbers with null check
//					excelRow.createCell(15).setCellValue(entity.getLoan_amount() == null ? "" : entity.getLoan_amount().toPlainString());
//					excelRow.createCell(16).setCellValue(entity.getInterest_rate() == null ? "" : entity.getInterest_rate().toPlainString());
//					excelRow.createCell(17).setCellValue(entity.getPenalty_rate() == null ? "" : entity.getPenalty_rate().toPlainString());
//					excelRow.createCell(18).setCellValue(entity.getAccrued_interest() == null ? "" : entity.getAccrued_interest().toPlainString());
//					excelRow.createCell(19).setCellValue(entity.getAccrued_penalty() == null ? "" : entity.getAccrued_penalty().toPlainString());
//					excelRow.createCell(20).setCellValue(entity.getPrincipal_due() == null ? "" : entity.getPrincipal_due().toPlainString());
//					excelRow.createCell(21).setCellValue(entity.getPrincipal_paid() == null ? "" : entity.getPrincipal_paid().toPlainString());
//					excelRow.createCell(22).setCellValue(entity.getPrincipal_balance() == null ? "" : entity.getPrincipal_balance().toPlainString());
//					excelRow.createCell(23).setCellValue(entity.getInterest_due() == null ? "" : entity.getInterest_due().toPlainString());
//					excelRow.createCell(24).setCellValue(entity.getInterest_paid() == null ? "" : entity.getInterest_paid().toPlainString());
//					excelRow.createCell(25).setCellValue(entity.getInterest_balance() == null ? "" : entity.getInterest_balance().toPlainString());
//					excelRow.createCell(26).setCellValue(entity.getInterest_fromarrearsbalance() == null ? "" : entity.getInterest_fromarrearsbalance().toPlainString());
//					excelRow.createCell(27).setCellValue(entity.getInterest_fromarrearsdue() == null ? "" : entity.getInterest_fromarrearsdue().toPlainString());
//					excelRow.createCell(28).setCellValue(entity.getInterest_fromarrearspaid() == null ? "" : entity.getInterest_fromarrearspaid().toPlainString());
//					excelRow.createCell(29).setCellValue(entity.getFees_due() == null ? "" : entity.getFees_due().toPlainString());
//					excelRow.createCell(30).setCellValue(entity.getFees_paid() == null ? "" : entity.getFees_paid().toPlainString());
//					excelRow.createCell(31).setCellValue(entity.getFees_balance() == null ? "" : entity.getFees_balance().toPlainString());
//					excelRow.createCell(32).setCellValue(entity.getPenalty_due() == null ? "" : entity.getPenalty_due().toPlainString());
//					excelRow.createCell(33).setCellValue(entity.getPenalty_paid() == null ? "" : entity.getPenalty_paid().toPlainString());
//					excelRow.createCell(34).setCellValue(entity.getPenalty_balance() == null ? "" : entity.getPenalty_balance().toPlainString());
//
//					// Dates
//					excelRow.createCell(35).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getExpected_disbursementdate()));
//					excelRow.createCell(36).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
//					excelRow.createCell(37).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getFirst_repaymentdate()));
//
//					excelRow.createCell(38).setCellValue(entity.getGrace_period() == null ? "" : entity.getGrace_period().toPlainString());
//					excelRow.createCell(39).setCellValue(entity.getRepayment_installments() == null ? "" : entity.getRepayment_installments().toPlainString());
//					excelRow.createCell(40).setCellValue(entity.getRepayment_periodcount() == null ? "" : entity.getRepayment_periodcount().toPlainString());
//					excelRow.createCell(41).setCellValue(entity.getDays_late() == null ? "" : entity.getDays_late().toPlainString());
//					excelRow.createCell(42).setCellValue(entity.getDays_inarrears() == null ? "" : entity.getDays_inarrears().toPlainString());
//					excelRow.createCell(43).setCellValue(entity.getRepayment_schedule_method());
//					excelRow.createCell(44).setCellValue(entity.getCurrency_code());
//					excelRow.createCell(45).setCellValue(entity.getSale_processedbyvgid());
//					excelRow.createCell(46).setCellValue(entity.getSale_processedfor());
//					excelRow.createCell(47).setCellValue(entity.getSale_referredby());
//					excelRow.createCell(48).setCellValue(entity.getEmployment_status());
//					excelRow.createCell(49).setCellValue(entity.getJob_title());
//					excelRow.createCell(50).setCellValue(entity.getEmployer_name());
//
//					// TU (numbers with null check)
//					excelRow.createCell(51).setCellValue(entity.getTuscore() == null ? "" : entity.getTuscore().toPlainString());
//					excelRow.createCell(52).setCellValue(entity.getTuprobability() == null ? "" : entity.getTuprobability().toPlainString());
//
//					// Strings
//					excelRow.createCell(53).setCellValue(entity.getTufullname());
//					excelRow.createCell(54).setCellValue(entity.getTureason1());
//					excelRow.createCell(55).setCellValue(entity.getTureason2());
//					excelRow.createCell(56).setCellValue(entity.getTureason3());
//					excelRow.createCell(57).setCellValue(entity.getTureason4());
//
//					// Financial numbers
//					excelRow.createCell(58).setCellValue(entity.getDisposable_income() == null ? "" : entity.getDisposable_income().toPlainString());
//					excelRow.createCell(59).setCellValue(entity.getManualoverride_amount() == null ? "" : entity.getManualoverride_amount().toPlainString());
//
//					// Dates
//					excelRow.createCell(60).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getManualoverride_expiry_date()));
//
//					// Numbers
//					excelRow.createCell(61).setCellValue(entity.getCpfees() == null ? "" : entity.getCpfees().toPlainString());
//					excelRow.createCell(62).setCellValue(entity.getDeposit_amount() == null ? "" : entity.getDeposit_amount().toPlainString());
//					excelRow.createCell(63).setCellValue(entity.getTotal_product_price() == null ? "" : entity.getTotal_product_price().toPlainString());
//
//					// Strings
//					excelRow.createCell(64).setCellValue(entity.getRetailer_name());
//					excelRow.createCell(65).setCellValue(entity.getRetailer_branch());
//					excelRow.createCell(66).setCellValue(entity.getVg_application_id());
//					excelRow.createCell(67).setCellValue(entity.getContract_signed());
//
//					// Dates
//					excelRow.createCell(68).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDate_of_first_call()));
//
//					// Strings
//					excelRow.createCell(69).setCellValue(entity.getLast_call_outcome());
//
//					// Date
//					excelRow.createCell(70).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getAsondate()));
//
//				}
//
//				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
//				response.setHeader("Content-Disposition", "inline; filename=Loan_Master_Data.xlsx");
//				
//			}
//			else {
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
//				return;
//			}
//
//			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//			workbook.write(response.getOutputStream());
//
//		} catch (Exception e) {
//			try {
//				if (!response.isCommitted()) {
//					response.reset();
//					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//					response.setContentType("text/plain");
//					response.getWriter().write("Error generating Excel: " + e.getMessage());
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//	}
//	
	
	


	public void ExportExcel(String type, String userID, String userName, String auditRefNo,
	                        HttpServletResponse response) {

	    // SXSSFWorkbook with 100 rows in memory
	    try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
	        workbook.setCompressTempFiles(true); // compress temp files
	        Sheet sheet = workbook.createSheet("Data");
	        int rowIdx = 0;

	        if ("REPAYMENT".equalsIgnoreCase(type)) {
	            List<ASPIRA_LOAN_REPAYMENT_ENTITY> dataList = lOAN_REPAYMENT_REPO.findAll();

	            // Header
	            Row header = sheet.createRow(rowIdx++);
	            String[] headers = {
	                    "ENCODEDKEY","ASSIGNEDBRANCHKEY","ASSIGNEDUSERKEY","DUEDATE","INTERESTDUE","INTERESTPAID","LASTPAIDDATE",
	                    "LASTPENALTYAPPLIEDDATE", "NOTES","PARENTACCOUNTKEY","PRINCIPALDUE", "PRINCIPALPAID", "REPAIDDATE", "STATE",
	                    "ASSIGNEDCENTREKEY","FEESDUE","FEESPAID","PENALTYDUE","PENALTYPAID","TAXINTERESTDUE","TAXINTERESTPAID",
	                    "TAXFEESDUE", "TAXFEESPAID","TAXPENALTYDUE","TAXPENALTYPAID","ORGANIZATIONCOMMISSIONDUE",
	                    "FUNDERSINTERESTDUE","CREATIONDATE","LASTMODIFIEDDATE","ADDITIONS"
	            };
	            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

	            for (ASPIRA_LOAN_REPAYMENT_ENTITY entity : dataList) {
	                Row excelRow = sheet.createRow(rowIdx++);
	                excelRow.createCell(0).setCellValue(entity.getEncodedkey());
	                excelRow.createCell(1).setCellValue(entity.getAssignedbranchkey());
	                excelRow.createCell(2).setCellValue(entity.getAssigneduserkey());
	                excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDuedate()));
	                excelRow.createCell(4).setCellValue(entity.getInterestdue() == null ? "" : entity.getInterestdue().toPlainString());
	                excelRow.createCell(5).setCellValue(entity.getInterestpaid() == null ? "" : entity.getInterestpaid().toPlainString());
	                excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpaiddate()));
	                excelRow.createCell(7).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpenaltyapplieddate()));
	                excelRow.createCell(8).setCellValue(entity.getNotes());
	                excelRow.createCell(9).setCellValue(entity.getParentaccountkey());
	                excelRow.createCell(10).setCellValue(entity.getPrincipaldue() == null ? "" : entity.getPrincipaldue().toPlainString());
	                excelRow.createCell(11).setCellValue(entity.getPrincipalpaid() == null ? "" : entity.getPrincipalpaid().toPlainString());
	                excelRow.createCell(12).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getRepaiddate()));
	                excelRow.createCell(13).setCellValue(entity.getState());
	                excelRow.createCell(14).setCellValue(entity.getAssignedcentrekey());
	                excelRow.createCell(15).setCellValue(entity.getFeesdue() == null ? "" : entity.getFeesdue().toPlainString());
	                excelRow.createCell(16).setCellValue(entity.getFeespaid() == null ? "" : entity.getFeespaid().toPlainString());
	                excelRow.createCell(17).setCellValue(entity.getPenaltydue() == null ? "" : entity.getPenaltydue().toPlainString());
	                excelRow.createCell(18).setCellValue(entity.getPenaltypaid() == null ? "" : entity.getPenaltypaid().toPlainString());
	                excelRow.createCell(19).setCellValue(entity.getTaxinterestdue() == null ? "" : entity.getTaxinterestdue().toPlainString());
	                excelRow.createCell(20).setCellValue(entity.getTaxinterestpaid() == null ? "" : entity.getTaxinterestpaid().toPlainString());
	                excelRow.createCell(21).setCellValue(entity.getTaxfeesdue() == null ? "" : entity.getTaxfeesdue().toPlainString());
	                excelRow.createCell(22).setCellValue(entity.getTaxfeespaid() == null ? "" : entity.getTaxfeespaid().toPlainString());
	                excelRow.createCell(23).setCellValue(entity.getTaxpenaltydue() == null ? "" : entity.getTaxpenaltydue().toPlainString());
	                excelRow.createCell(24).setCellValue(entity.getTaxpenaltypaid() == null ? "" : entity.getTaxpenaltypaid().toPlainString());
	                excelRow.createCell(25).setCellValue(entity.getOrganizationcommissiondue() == null ? "" : entity.getOrganizationcommissiondue().toPlainString());
	                excelRow.createCell(26).setCellValue(entity.getFundersinterestdue() == null ? "" : entity.getFundersinterestdue().toPlainString());
	                excelRow.createCell(27).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreationdate()));
	                excelRow.createCell(28).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastmodifieddate()));
	                excelRow.createCell(29).setCellValue(entity.getAdditions());
	            }

	            saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
	            response.setHeader("Content-Disposition", "inline; filename=Repayment_Data.xlsx");

	        } else if ("CUSTOMER".equalsIgnoreCase(type)) {
	            List<CLIENT_MASTER_ENTITY> dataList = clientMasterRepo.findAll();

	            Row header = sheet.createRow(rowIdx++);
	            String[] headers = {
	                    "ENCODEDKEY", "ID", "CLIENTSTATE", "CREATIONDATE", "LASTMODIFIEDDATE", "ACTIVATIONDATE",
	                    "APPROVEDDATE", "FIRSTNAME", "LASTNAME", "MOBILEPHONE", "EMAILADDRESS", "PREFERREDLANGUAGE",
	                    "BIRTHDATE", "GENDER", "ASSIGNEDBRANCHKEY", "CLIENTROLEKEY", "LOANCYCLE", "GROUPLOANCYCLE",
	                    "ADDRESSLINE1", "ADDRESSLINE2", "ADDRESSLINE3", "CITY", "SUBURB", "ASSIGNEDUSERKEY", "ASONDATE"
	            };
	            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

	            for (CLIENT_MASTER_ENTITY entity : dataList) {
	                Row excelRow = sheet.createRow(rowIdx++);
	                excelRow.createCell(0).setCellValue(entity.getEncoded_key());
	                excelRow.createCell(1).setCellValue(entity.getCustomer_id());
	                excelRow.createCell(2).setCellValue(entity.getClient_state());
	                excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreation_date()));
	                excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_modified_date()));
	                excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getActivation_date()));
	                excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getApproved_date()));
	                excelRow.createCell(7).setCellValue(entity.getFirst_name());
	                excelRow.createCell(8).setCellValue(entity.getLast_name());
	                excelRow.createCell(9).setCellValue(entity.getMobile_phone());
	                excelRow.createCell(10).setCellValue(entity.getEmail_address());
	                excelRow.createCell(11).setCellValue(entity.getPreferred_language());
	                excelRow.createCell(12).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getBirth_date()));
	                excelRow.createCell(13).setCellValue(entity.getGender());
	                excelRow.createCell(14).setCellValue(entity.getAssigned_branch_key());
	                excelRow.createCell(15).setCellValue(entity.getClient_role_key());
	                excelRow.createCell(16).setCellValue(entity.getLoan_cycle() == null ? "" : entity.getLoan_cycle().toPlainString());
	                excelRow.createCell(17).setCellValue(entity.getGroup_loan_cycle() == null ? "" : entity.getGroup_loan_cycle().toPlainString());
	                excelRow.createCell(18).setCellValue(entity.getAddress_line1());
	                excelRow.createCell(19).setCellValue(entity.getAddress_line2());
	                excelRow.createCell(20).setCellValue(entity.getAddress_line3());
	                excelRow.createCell(21).setCellValue(entity.getCity());
	                excelRow.createCell(22).setCellValue(entity.getSuburb());
	                excelRow.createCell(23).setCellValue(entity.getAssigned_user_key());
	                excelRow.createCell(24).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getAsondate()));
	            }

	            saveAudit(userID, userName, "Customer File Download!", "CLIENT_MASTER_TBL", auditRefNo);
	            response.setHeader("Content-Disposition", "inline; filename=Customer_Data.xlsx");

	        } else if ("LOAN".equalsIgnoreCase(type)) {
	            List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.findAll();

	            Row header = sheet.createRow(rowIdx++);
	            String[] headers = { /* same as your Loan headers */ };
	            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

	            for (LOAN_ACT_MST_ENTITY entity : dataList) {
	                Row excelRow = sheet.createRow(rowIdx++);

	                excelRow.createCell(0).setCellValue(entity.getEncoded_key());
	                excelRow.createCell(1).setCellValue(entity.getId());
	                excelRow.createCell(2).setCellValue(entity.getAccount_holdertype());
	                excelRow.createCell(3).setCellValue(entity.getAccount_holderkey());
	                excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreation_date()));
	                excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getApproved_date()));
	                excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_modified_date()));
	                excelRow.createCell(7).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getClosed_date()));
	                excelRow.createCell(8).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_account_appraisaldate()));
	                excelRow.createCell(9).setCellValue(entity.getAccount_state());
	                excelRow.createCell(10).setCellValue(entity.getAccount_substate());
	                excelRow.createCell(11).setCellValue(entity.getProduct_typekey());
	                excelRow.createCell(12).setCellValue(entity.getLoan_name());
	                excelRow.createCell(13).setCellValue(entity.getPayment_method());
	                excelRow.createCell(14).setCellValue(entity.getAssigned_branchkey());

	                // BigDecimal / numeric columns
	                excelRow.createCell(15).setCellValue(entity.getLoan_amount() == null ? "" : entity.getLoan_amount().toPlainString());
	                excelRow.createCell(16).setCellValue(entity.getInterest_rate() == null ? "" : entity.getInterest_rate().toPlainString());
	                excelRow.createCell(17).setCellValue(entity.getPenalty_rate() == null ? "" : entity.getPenalty_rate().toPlainString());
	                excelRow.createCell(18).setCellValue(entity.getAccrued_interest() == null ? "" : entity.getAccrued_interest().toPlainString());
	                excelRow.createCell(19).setCellValue(entity.getAccrued_penalty() == null ? "" : entity.getAccrued_penalty().toPlainString());
	                excelRow.createCell(20).setCellValue(entity.getPrincipal_due() == null ? "" : entity.getPrincipal_due().toPlainString());
	                excelRow.createCell(21).setCellValue(entity.getPrincipal_paid() == null ? "" : entity.getPrincipal_paid().toPlainString());
	                excelRow.createCell(22).setCellValue(entity.getPrincipal_balance() == null ? "" : entity.getPrincipal_balance().toPlainString());
	                excelRow.createCell(23).setCellValue(entity.getInterest_due() == null ? "" : entity.getInterest_due().toPlainString());
	                excelRow.createCell(24).setCellValue(entity.getInterest_paid() == null ? "" : entity.getInterest_paid().toPlainString());
	                excelRow.createCell(25).setCellValue(entity.getInterest_balance() == null ? "" : entity.getInterest_balance().toPlainString());
	                excelRow.createCell(26).setCellValue(entity.getInterest_fromarrearsbalance() == null ? "" : entity.getInterest_fromarrearsbalance().toPlainString());
	                excelRow.createCell(27).setCellValue(entity.getInterest_fromarrearsdue() == null ? "" : entity.getInterest_fromarrearsdue().toPlainString());
	                excelRow.createCell(28).setCellValue(entity.getInterest_fromarrearspaid() == null ? "" : entity.getInterest_fromarrearspaid().toPlainString());
	                excelRow.createCell(29).setCellValue(entity.getFees_due() == null ? "" : entity.getFees_due().toPlainString());
	                excelRow.createCell(30).setCellValue(entity.getFees_paid() == null ? "" : entity.getFees_paid().toPlainString());
	                excelRow.createCell(31).setCellValue(entity.getFees_balance() == null ? "" : entity.getFees_balance().toPlainString());
	                excelRow.createCell(32).setCellValue(entity.getPenalty_due() == null ? "" : entity.getPenalty_due().toPlainString());
	                excelRow.createCell(33).setCellValue(entity.getPenalty_paid() == null ? "" : entity.getPenalty_paid().toPlainString());
	                excelRow.createCell(34).setCellValue(entity.getPenalty_balance() == null ? "" : entity.getPenalty_balance().toPlainString());

	                // Dates
	                excelRow.createCell(35).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getExpected_disbursementdate()));
	                excelRow.createCell(36).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
	                excelRow.createCell(37).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getFirst_repaymentdate()));

	                // More numeric columns
	                excelRow.createCell(38).setCellValue(entity.getGrace_period() == null ? "" : entity.getGrace_period().toPlainString());
	                excelRow.createCell(39).setCellValue(entity.getRepayment_installments() == null ? "" : entity.getRepayment_installments().toPlainString());
	                excelRow.createCell(40).setCellValue(entity.getRepayment_periodcount() == null ? "" : entity.getRepayment_periodcount().toPlainString());
	                excelRow.createCell(41).setCellValue(entity.getDays_late() == null ? "" : entity.getDays_late().toPlainString());
	                excelRow.createCell(42).setCellValue(entity.getDays_inarrears() == null ? "" : entity.getDays_inarrears().toPlainString());

	                // Strings / codes
	                excelRow.createCell(43).setCellValue(entity.getRepayment_schedule_method());
	                excelRow.createCell(44).setCellValue(entity.getCurrency_code());
	                excelRow.createCell(45).setCellValue(entity.getSale_processedbyvgid());
	                excelRow.createCell(46).setCellValue(entity.getSale_processedfor());
	                excelRow.createCell(47).setCellValue(entity.getSale_referredby());
	                excelRow.createCell(48).setCellValue(entity.getEmployment_status());
	                excelRow.createCell(49).setCellValue(entity.getJob_title());
	                excelRow.createCell(50).setCellValue(entity.getEmployer_name());

	                // TU numeric columns
	                excelRow.createCell(51).setCellValue(entity.getTuscore() == null ? "" : entity.getTuscore().toPlainString());
	                excelRow.createCell(52).setCellValue(entity.getTuprobability() == null ? "" : entity.getTuprobability().toPlainString());

	                // TU strings
	                excelRow.createCell(53).setCellValue(entity.getTufullname());
	                excelRow.createCell(54).setCellValue(entity.getTureason1());
	                excelRow.createCell(55).setCellValue(entity.getTureason2());
	                excelRow.createCell(56).setCellValue(entity.getTureason3());
	                excelRow.createCell(57).setCellValue(entity.getTureason4());

	                // Financial numbers
	                excelRow.createCell(58).setCellValue(entity.getDisposable_income() == null ? "" : entity.getDisposable_income().toPlainString());
	                excelRow.createCell(59).setCellValue(entity.getManualoverride_amount() == null ? "" : entity.getManualoverride_amount().toPlainString());

	                // Dates
	                excelRow.createCell(60).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getManualoverride_expiry_date()));

	                // Numbers
	                excelRow.createCell(61).setCellValue(entity.getCpfees() == null ? "" : entity.getCpfees().toPlainString());
	                excelRow.createCell(62).setCellValue(entity.getDeposit_amount() == null ? "" : entity.getDeposit_amount().toPlainString());
	                excelRow.createCell(63).setCellValue(entity.getTotal_product_price() == null ? "" : entity.getTotal_product_price().toPlainString());

	                // Strings
	                excelRow.createCell(64).setCellValue(entity.getRetailer_name());
	                excelRow.createCell(65).setCellValue(entity.getRetailer_branch());
	                excelRow.createCell(66).setCellValue(entity.getVg_application_id());
	                excelRow.createCell(67).setCellValue(entity.getContract_signed());

	                // Dates
	                excelRow.createCell(68).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDate_of_first_call()));

	                // Strings
	                excelRow.createCell(69).setCellValue(entity.getLast_call_outcome());

	                // Date
	                excelRow.createCell(70).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getAsondate()));
	            }


	            saveAudit(userID, userName, "Loan File Download!", "LOAN_ACT_MST_ENTITY", auditRefNo);
	            response.setHeader("Content-Disposition", "inline; filename=Loan_Data.xlsx");

	        } else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
	            return;
	        }

	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        workbook.write(response.getOutputStream());
	        workbook.dispose(); // delete temp files

	    } catch (Exception e) {
	        try {
	            if (!response.isCommitted()) {
	                response.reset();
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                response.setContentType("text/plain");
	                response.getWriter().write("Error generating Excel: " + e.getMessage());
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	
	
	
	
	
	
	
	
	private void saveAudit(String userID, String userName, String remarks, String table, String refNo) {
		BGLSAuditTable audit = new BGLSAuditTable();
		audit.setAudit_date(new Date());
		audit.setEntry_time(new Date());
		audit.setEntry_user(userID);
		audit.setFunc_code("DOWNLOAD");
		audit.setRemarks(remarks);
		audit.setAudit_table(table);
		audit.setAudit_screen("UPLOAD");
		audit.setEvent_id(userID);
		audit.setEvent_name(userName);
		audit.setModi_details("-");
		audit.setAudit_ref_no(refNo);
		AuditTable_Rep.save(audit);
	}
	
	public void ExportExcel1(String type, String userID, String userName, String auditRefNo,
			HttpServletResponse response,Date currentDate) {

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Data");
			int rowIdx = 0;

			if ("disbursement".equalsIgnoreCase(type)) {
				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getDistval();

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
					};

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size");
					
				int srlNo = 1;
				
				for (LOAN_ACT_MST_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

					// String values
					excelRow.createCell(0).setCellValue(srlNo++);
					excelRow.createCell(1).setCellValue("1");
					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
					excelRow.createCell(3).setCellValue("DISRB");
					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
					excelRow.createCell(5).setCellValue(entity.getId());
					excelRow.createCell(6).setCellValue(entity.getLoan_name());

				}

				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=disbursement.xlsx");

			}   
			else if ("interest".equalsIgnoreCase(type)) {
				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval1(currentDate);

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
					};

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size");
					
				int srlNo = 1;
				
				for (LOAN_ACT_MST_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

					// String values
					excelRow.createCell(0).setCellValue(srlNo++);
					excelRow.createCell(1).setCellValue("1");
					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
					excelRow.createCell(3).setCellValue("INDEM");
					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
					excelRow.createCell(5).setCellValue(entity.getId());
					excelRow.createCell(6).setCellValue(entity.getLoan_name());

				}

				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=interest.xlsx");
				
				
				
			} 
			else if ("fees".equalsIgnoreCase(type)) {
				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval31(currentDate);

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
					};

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size");
					
				int srlNo = 1;
				
				for (LOAN_ACT_MST_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

					// String values
					excelRow.createCell(0).setCellValue(srlNo++);
					excelRow.createCell(1).setCellValue("1");
					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
					excelRow.createCell(3).setCellValue("FEEDM");
					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
					excelRow.createCell(5).setCellValue(entity.getId());
					excelRow.createCell(6).setCellValue(entity.getLoan_name());

				}


				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=fees.xlsx");
				
				
				
				
			} 
			else if ("recovery".equalsIgnoreCase(type)) {
				
				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval41(currentDate);

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
					}; 

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size");
					
				int srlNo = 1;
				
				for (LOAN_ACT_MST_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

					// String values
					excelRow.createCell(0).setCellValue(srlNo++);
					excelRow.createCell(1).setCellValue("1");
					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
					excelRow.createCell(3).setCellValue("DISRB");
					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
					excelRow.createCell(5).setCellValue(entity.getId());
					excelRow.createCell(6).setCellValue(entity.getLoan_name());

				}

				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=recovery.xlsx");
	
	
	
	
} 
			else if ("booking".equalsIgnoreCase(type)) {
	
				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval21(currentDate);

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
					};

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size");
					
				int srlNo = 1;
				
				for (LOAN_ACT_MST_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

					// String values
					excelRow.createCell(0).setCellValue(srlNo++);
					excelRow.createCell(1).setCellValue("1");
					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
					excelRow.createCell(3).setCellValue("DISRB");
					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
					excelRow.createCell(5).setCellValue(entity.getId());
					excelRow.createCell(6).setCellValue(entity.getLoan_name());

				}

				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=booking.xlsx");
	
	
} 
			
			else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
				return;
			}

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			workbook.write(response.getOutputStream());

		} catch (Exception e) {
			try {
				if (!response.isCommitted()) {
					response.reset();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.setContentType("text/plain");
					response.getWriter().write("Error generating Excel: " + e.getMessage());
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	

}
