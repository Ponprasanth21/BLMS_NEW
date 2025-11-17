package com.bornfire.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import com.bornfire.entities.BGLSAuditTable;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.LOAN_ACT_MST_ENTITY;
import com.bornfire.entities.LOAN_ACT_MST_REPO;
import com.bornfire.entities.LOAN_REPAYMENT_ENTITY;
import com.bornfire.entities.LOAN_REPAYMENT_REPO;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_ENTITY;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_REP;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.apache.poi.hssf.util.CellReference;
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
	LOAN_REPAYMENT_REPO lrtRepo;
	
	@Autowired
	LOAN_ACT_MST_REPO loanMasterRepo;
	
	@Autowired
	DateParser DateParser;
	
	@Autowired
	BGLSAuditTable_Rep AuditTable_Rep;
	
	@Autowired
	CLIENT_MASTER_REPO clientMasterRepo;
	
	@Autowired
	DataSource srcdataSource;
	
	@Autowired
	TRAN_MAIN_TRM_WRK_REP tRAN_MAIN_TRM_WRK_REP;
	
	@Autowired
	AuditConfigure audit;
	

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
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
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
//				audit.insertServiceAudit(userID, userName, "CUSTOMER FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","CLIENT_MASTER_TBL", "CUSTOMER MASTRER");
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
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
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
	            List<LOAN_REPAYMENT_ENTITY> dataList = lrtRepo.getRepaymentDetails();

	            // Header
	            Row header = sheet.createRow(rowIdx++);
	            String[] headers = {
	            	    "ENCODEDKEY",
	            	    "PARENTACCOUNTKEY",
	            	    "ASSIGNEDBRANCHKEY",
	            	    "ASSIGNEDUSERKEY",
	            	    "DUEDATE",
	            	    "LASTPAIDDATE",
	            	    "REPAIDDATE",
	            	    "PAYMENTSTATE",
	            	    "ISPAYMENTHOLIDAY",
	            	    "PRINCIPALEXP",
	            	    "PRINCIPALPAID",
	            	    "PRINCIPALDUE",
	            	    "INTERESTEXP",
	            	    "INTERESTPAID",
	            	    "INTERESTDUE",
	            	    "FEEEXP",
	            	    "FEEPAID",
	            	    "FEEDUE",
	            	    "PENALTYEXP",
	            	    "PENALTYPAYED",
	            	    "PENALTYDUE"
	            	};


	            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

	            for (LOAN_REPAYMENT_ENTITY entity : dataList) {
	                Row excelRow = sheet.createRow(rowIdx++);
	                excelRow.createCell(0).setCellValue(entity.getEncoded_key());
	                excelRow.createCell(1).setCellValue(entity.getParent_account_key());
	                excelRow.createCell(2).setCellValue(entity.getAssigned_branch_key());
	                excelRow.createCell(3).setCellValue(entity.getAssigned_user_key());
	                excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDue_date()));
	                excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_paid_date()));
	                excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getRepaid_date()));
	                excelRow.createCell(7).setCellValue(entity.getPayment_state()); // payment_state
	                excelRow.createCell(8).setCellValue(entity.getIs_payment_holiday() != null ? "Yes" : "No"); 
	                excelRow.createCell(9).setCellValue(entity.getPrincipal_exp() == null ? "" : entity.getPrincipal_exp().toPlainString());
	                excelRow.createCell(10).setCellValue(entity.getPrincipal_paid() == null ? "" : entity.getPrincipal_paid().toPlainString());
	                excelRow.createCell(11).setCellValue(entity.getPrincipal_due() == null ? "" : entity.getPrincipal_due().toPlainString());
	                excelRow.createCell(12).setCellValue(entity.getInterest_exp() == null ? "" : entity.getInterest_exp().toPlainString());
	                excelRow.createCell(13).setCellValue(entity.getInterest_paid() == null ? "" : entity.getInterest_paid().toPlainString());
	                excelRow.createCell(14).setCellValue(entity.getInterest_due() == null ? "" : entity.getInterest_due().toPlainString());
	                excelRow.createCell(15).setCellValue(entity.getFee_exp() == null ? "" : entity.getFee_exp().toPlainString());
	                excelRow.createCell(16).setCellValue(entity.getFee_paid() == null ? "" : entity.getFee_paid().toPlainString());
	                excelRow.createCell(17).setCellValue(entity.getFee_due() == null ? "" : entity.getFee_due().toPlainString());
	                excelRow.createCell(18).setCellValue(entity.getPenalty_exp() == null ? "" : entity.getPenalty_exp().toPlainString());
	                excelRow.createCell(19).setCellValue(entity.getPenalty_paid() == null ? "" : entity.getPenalty_paid().toPlainString());
	                excelRow.createCell(20).setCellValue(entity.getPenalty_due() == null ? "" : entity.getPenalty_due().toPlainString());
	                

	            }

	            audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT","-");
	            response.setHeader("Content-Disposition", "inline; filename=Repayment_Data.xlsx");

	        } else if ("CUSTOMER".equalsIgnoreCase(type)) {
	        	System.out.println("this first");
	            List<CLIENT_MASTER_ENTITY> dataList = clientMasterRepo.getClientDetails();
	            System.out.println("data fetched");

	            Row header = sheet.createRow(rowIdx++);
	            String[] headers = {
	            	    "ENCODEDKEY",            // a.encoded_key
	            	    "CLIENTSTATE",           // initcap(a.client_state)
	            	    "CREATIONDATE",          // a.creation_date
	            	    "APPROVEDDATE",          // a.approved_date
	            	    "ACTIVATIONDATE",        // a.activation_date
	            	    "LASTMODIFIEDDATE",      // a.last_modified_date
	            	    "CUSTOMERID",            // a.customer_id
	            	    "FULLNAME",              // initcap(a.first_name)||' '||initcap(a.last_name)
	            	    "FIRSTNAME",             // initcap(a.first_name)
	            	    "LASTNAME",              // initcap(a.last_name)
	            	    "GENDER",                // initcap(a.gender)
	            	    "MOBILEPHONE",           // a.mobile_phone
	            	    "EMAILADDRESS",          // a.email_address
	            	    "SUBURB",                // a.suburb
	            	    "CITY",                  // a.city
	            	    "FULLADDRESS",           // a.address_line1||'-'||a.address_line2||'-'||address_line2
	            	    "PREFERREDLANGUAGE",     // a.preferred_language
	            	    "ASSIGNEDBRANCHKEY",     // a.assigned_branch_key
	            	    "ASSIGNEDUSERKEY",       // a.assigned_user_key
	            	    "CLIENTROLEKEY",         // a.client_role_key
	            	    "LOANCYCLE",             // a.loan_cycle
	            	    "GROUPLOANCYCLE",        // a.group_loan_cycle
	            	    "ASONDATE"               // a.asondate
	            	};

	            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
	            
	            System.out.println("mapping start");

	            for (CLIENT_MASTER_ENTITY entity : dataList) {
	                Row excelRow = sheet.createRow(rowIdx++);
	                excelRow.createCell(0).setCellValue(entity.getEncoded_key());
	                excelRow.createCell(1).setCellValue(entity.getClient_state());
	                excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreation_date()));
	                excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getApproved_date()));
	                excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getActivation_date()));
	                excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLast_modified_date()));
	                excelRow.createCell(6).setCellValue(entity.getCustomer_id());
	                excelRow.createCell(7).setCellValue(entity.getFirst_name() + " " + entity.getLast_name()); // Full name
	                excelRow.createCell(8).setCellValue(entity.getFirst_name());
	                excelRow.createCell(9).setCellValue(entity.getLast_name());
	                excelRow.createCell(10).setCellValue(entity.getGender());
	                excelRow.createCell(11).setCellValue(entity.getMobile_phone());
	                excelRow.createCell(12).setCellValue(entity.getEmail_address());
	                excelRow.createCell(13).setCellValue(entity.getSuburb());
	                excelRow.createCell(14).setCellValue(entity.getCity());
	                excelRow.createCell(15).setCellValue(entity.getAddress_line1() + "-" + entity.getAddress_line2() + "-" + entity.getAddress_line2()); // Full address
	                excelRow.createCell(16).setCellValue(entity.getPreferred_language());
	                excelRow.createCell(17).setCellValue(entity.getAssigned_branch_key());
	                excelRow.createCell(18).setCellValue(entity.getAssigned_user_key());
	                excelRow.createCell(19).setCellValue(entity.getClient_role_key());
	                excelRow.createCell(20).setCellValue(entity.getLoan_cycle() == null ? "" : entity.getLoan_cycle().toPlainString());
	                excelRow.createCell(21).setCellValue(entity.getGroup_loan_cycle() == null ? "" : entity.getGroup_loan_cycle().toPlainString());
	                excelRow.createCell(22).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getAsondate()));

	            }
	            audit.insertServiceAudit(userID, userName, "CUSTOMER FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","CLIENT_MASTER_TBL", "CUSTOMER MASTER","-");
	            response.setHeader("Content-Disposition", "inline; filename=Customer_Data.xlsx");

	        } else if ("LOAN".equalsIgnoreCase(type)) {
	        	System.out.println("start");
	            List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getloanDetails();
	            System.out.println("fetched");

	            Row header = sheet.createRow(rowIdx++);
	            String[] headers = {
	            	    "ENCODEDKEY",             // a.encoded_key
	            	    "ID",                     // a.id
	            	    "ACCOUNTHOLDERTYPE",      // a.account_holdertype
	            	    "ACCOUNTHOLDERKEY",       // a.account_holderkey
	            	    "APPROVEDDATE",           // a.approved_date
	            	    "CLOSEDDATE",             // a.closed_date
	            	    "ACCOUNTSTATE",           // a.account_state
	            	    "ACCOUNTSUBSTATE",        // a.account_substate
	            	    "PRODUCTTYPEKEY",         // a.product_typekey
	            	    "LOANNAME",               // a.loan_name
	            	    "PAYMENTMETHOD",          // a.payment_method
	            	    "ASSIGNEDBRANCHKEY",      // a.assigned_branchkey
	            	    "LOANAMOUNT",             // a.loan_amount
	            	    "INTERESTRATE",           // a.interest_rate
	            	    "PENALTYRATE",            // a.penalty_rate
	            	    "ACCRUEDINTEREST",        // a.accrued_interest
	            	    "ACCRUEDPENALTY",         // a.accrued_penalty
	            	    "PRINCIPALDUE",           // a.principal_due
	            	    "PRINCIPALPAID",          // a.principal_paid
	            	    "PRINCIPALBALANCE",       // a.principal_balance
	            	    "INTERESTDUE",            // a.interest_due
	            	    "INTERESTPAID",           // a.interest_paid
	            	    "INTERESTBALANCE",        // a.interest_balance
	            	    "FEESDUE",                // a.fees_due
	            	    "FEESPAID",               // a.fees_paid
	            	    "FEESBALANCE",            // a.fees_balance
	            	    "PENALTYDUE",             // a.penalty_due
	            	    "PENALTYPAYED",           // a.penalty_paid
	            	    "PENALTYBALANCE",         // a.penalty_balance
	            	    "DISBURSEMENTDATE",       // a.disbursement_date
	            	    "FIRSTREPAYMENTDATE",     // a.first_repaymentdate
	            	    "GRACEPERIOD",            // a.grace_period
	            	    "REPAYMENTINSTALLMENTS",  // a.repayment_installments
	            	    "REPAYMENTPERIODCOUNT",   // a.repayment_periodcount
	            	    "DAYSLATE"                // a.days_late
	            	};

	            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
	            System.out.println("started loop");

	            for (LOAN_ACT_MST_ENTITY entity : dataList) {
	                Row excelRow = sheet.createRow(rowIdx++);

	                excelRow.createCell(0).setCellValue(entity.getEncoded_key());
	                excelRow.createCell(1).setCellValue(entity.getId());
	                excelRow.createCell(2).setCellValue(entity.getAccount_holdertype());
	                excelRow.createCell(3).setCellValue(entity.getAccount_holderkey());
	                excelRow.createCell(4).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getApproved_date()));
	                excelRow.createCell(5).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getClosed_date()));
	                excelRow.createCell(6).setCellValue(entity.getAccount_state());
	                excelRow.createCell(7).setCellValue(entity.getAccount_substate());
	                excelRow.createCell(8).setCellValue(entity.getProduct_typekey());
	                excelRow.createCell(9).setCellValue(entity.getLoan_name());
	                excelRow.createCell(10).setCellValue(entity.getPayment_method());
	                excelRow.createCell(11).setCellValue(entity.getAssigned_branchkey());
	                excelRow.createCell(12).setCellValue(entity.getLoan_amount() == null ? "" : entity.getLoan_amount().toPlainString());
	                excelRow.createCell(13).setCellValue(entity.getInterest_rate() == null ? "" : entity.getInterest_rate().toPlainString());
	                excelRow.createCell(14).setCellValue(entity.getPenalty_rate() == null ? "" : entity.getPenalty_rate().toPlainString());
	                excelRow.createCell(15).setCellValue(entity.getAccrued_interest() == null ? "" : entity.getAccrued_interest().toPlainString());
	                excelRow.createCell(16).setCellValue(entity.getAccrued_penalty() == null ? "" : entity.getAccrued_penalty().toPlainString());
	                excelRow.createCell(17).setCellValue(entity.getPrincipal_due() == null ? "" : entity.getPrincipal_due().toPlainString());
	                excelRow.createCell(18).setCellValue(entity.getPrincipal_paid() == null ? "" : entity.getPrincipal_paid().toPlainString());
	                excelRow.createCell(19).setCellValue(entity.getPrincipal_balance() == null ? "" : entity.getPrincipal_balance().toPlainString());
	                excelRow.createCell(20).setCellValue(entity.getInterest_due() == null ? "" : entity.getInterest_due().toPlainString());
	                excelRow.createCell(21).setCellValue(entity.getInterest_paid() == null ? "" : entity.getInterest_paid().toPlainString());
	                excelRow.createCell(22).setCellValue(entity.getInterest_balance() == null ? "" : entity.getInterest_balance().toPlainString());
	                excelRow.createCell(23).setCellValue(entity.getFees_due() == null ? "" : entity.getFees_due().toPlainString());
	                excelRow.createCell(24).setCellValue(entity.getFees_paid() == null ? "" : entity.getFees_paid().toPlainString());
	                excelRow.createCell(25).setCellValue(entity.getFees_balance() == null ? "" : entity.getFees_balance().toPlainString());
	                excelRow.createCell(26).setCellValue(entity.getPenalty_due() == null ? "" : entity.getPenalty_due().toPlainString());
	                excelRow.createCell(27).setCellValue(entity.getPenalty_paid() == null ? "" : entity.getPenalty_paid().toPlainString());
	                excelRow.createCell(28).setCellValue(entity.getPenalty_balance() == null ? "" : entity.getPenalty_balance().toPlainString());
	                excelRow.createCell(29).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
	                excelRow.createCell(30).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getFirst_repaymentdate()));
	                excelRow.createCell(31).setCellValue(entity.getGrace_period() == null ? "" : entity.getGrace_period().toPlainString());
	                excelRow.createCell(32).setCellValue(entity.getRepayment_installments() == null ? "" : entity.getRepayment_installments().toPlainString());
	                excelRow.createCell(33).setCellValue(entity.getRepayment_periodcount() == null ? "" : entity.getRepayment_periodcount().toPlainString());
	                excelRow.createCell(34).setCellValue(entity.getDays_late() == null ? "" : entity.getDays_late().toPlainString());

	            }
	            System.out.println("download ready");
	            audit.insertServiceAudit(userID, userName, "LOAN FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","LOAN_ACT_MST_ENTITY", "LOAN ACCOUNT MASTER","-");
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
	
//	public void ExportExcel1(String type, String userID, String userName, String auditRefNo,
//			HttpServletResponse response,Date currentDate) {
//
//		try (Workbook workbook = new XSSFWorkbook()) {
//			Sheet sheet = workbook.createSheet("Data");
//			int rowIdx = 0;
//
//			if ("disbursement".equalsIgnoreCase(type)) {
//				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getDistval();
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
//					};
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size");
//					
//				int srlNo = 1;
//				
//				for (LOAN_ACT_MST_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					// String values
//					excelRow.createCell(0).setCellValue(srlNo++);
//					excelRow.createCell(1).setCellValue("1");
//					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
//					excelRow.createCell(3).setCellValue("DISRB");
//					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
//					excelRow.createCell(5).setCellValue(entity.getId());
//					excelRow.createCell(6).setCellValue(entity.getLoan_name());
//
//				}
//
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
//				response.setHeader("Content-Disposition", "inline; filename=disbursement.xlsx");
//
//			}   
//			else if ("interest".equalsIgnoreCase(type)) {
//				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval1(currentDate);
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
//					};
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size");
//					
//				int srlNo = 1;
//				
//				for (LOAN_ACT_MST_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					// String values
//					excelRow.createCell(0).setCellValue(srlNo++);
//					excelRow.createCell(1).setCellValue("1");
//					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
//					excelRow.createCell(3).setCellValue("INDEM");
//					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
//					excelRow.createCell(5).setCellValue(entity.getId());
//					excelRow.createCell(6).setCellValue(entity.getLoan_name());
//
//				}
//
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
//				response.setHeader("Content-Disposition", "inline; filename=interest.xlsx");
//				
//				
//				
//			} 
//			else if ("fees".equalsIgnoreCase(type)) {
//				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval31(currentDate);
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
//					};
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size");
//					
//				int srlNo = 1;
//				
//				for (LOAN_ACT_MST_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					// String values
//					excelRow.createCell(0).setCellValue(srlNo++);
//					excelRow.createCell(1).setCellValue("1");
//					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
//					excelRow.createCell(3).setCellValue("FEEDM");
//					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
//					excelRow.createCell(5).setCellValue(entity.getId());
//					excelRow.createCell(6).setCellValue(entity.getLoan_name());
//
//				}
//
//
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
//				response.setHeader("Content-Disposition", "inline; filename=fees.xlsx");
//				
//				
//				
//				
//			} 
//			else if ("recovery".equalsIgnoreCase(type)) {
//				
//				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval41(currentDate);
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
//					}; 
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size");
//					
//				int srlNo = 1;
//				
//				for (LOAN_ACT_MST_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					// String values
//					excelRow.createCell(0).setCellValue(srlNo++);
//					excelRow.createCell(1).setCellValue("1");
//					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
//					excelRow.createCell(3).setCellValue("DISRB");
//					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
//					excelRow.createCell(5).setCellValue(entity.getId());
//					excelRow.createCell(6).setCellValue(entity.getLoan_name());
//
//				}
//
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
//				response.setHeader("Content-Disposition", "inline; filename=recovery.xlsx");
//	
//	
//	
//	
//} 
//			else if ("booking".equalsIgnoreCase(type)) {
//	
//				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.getLoanActDetval21(currentDate);
//
//				// Header
//				Row header = sheet.createRow(rowIdx++);
//				String[] headers = {
//					    "SRL NO","FLOW ID","FLOW DATE","FLOW CODE","FLOW AMOUNT","ACCOUNT NUMBER","ACCOUNT NAME"
//					};
//
//				for (int i = 0; i < headers.length; i++) {
//					header.createCell(i).setCellValue(headers[i]);
//				}
//					System.out.println(dataList.size()+"   List Size");
//					
//				int srlNo = 1;
//				
//				for (LOAN_ACT_MST_ENTITY entity : dataList) {
//					Row excelRow = sheet.createRow(rowIdx++);
//
//					// String values
//					excelRow.createCell(0).setCellValue(srlNo++);
//					excelRow.createCell(1).setCellValue("1");
//					excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDisbursement_date()));
//					excelRow.createCell(3).setCellValue("DISRB");
//					excelRow.createCell(4).setCellValue(entity.getLoan_amount().doubleValue());
//					excelRow.createCell(5).setCellValue(entity.getId());
//					excelRow.createCell(6).setCellValue(entity.getLoan_name());
//
//				}
//
//				audit.insertServiceAudit(userID, userName, "REPAYMENT FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","ASPIRA_LOAN_REPAYMENT_TABLE", "LOAN REPAYMENT");
//				response.setHeader("Content-Disposition", "inline; filename=booking.xlsx");
//	
//	
//} 
//			
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
	
	
	
	public void ExportExcel1(String type, String userID, String userName, String auditRefNo,
            HttpServletResponse response) {
		
			System.out.println("entered to download");
				
				try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Data");
				int rowIdx = 0;
				
				String[] headers = {
				"SRL NO", "FLOW ID", "FLOW DATE", "FLOW CODE",
				"FLOW AMOUNT", "ACCOUNT NUMBER", "ACCOUNT NAME"
				};
				
				System.out.println("entered to header");
				
				Row header = sheet.createRow(rowIdx++);
				for (int i = 0; i < headers.length; i++) {
				header.createCell(i).setCellValue(headers[i]);
				}
				
				List<TRAN_MAIN_TRM_WRK_ENTITY> dataList = null;
				String filename = "";
				String flowCode = "";
				
				// ✅ Select data & setup parameters based on tab type
				switch (type.toLowerCase()) {
				case "disbursement":
				   dataList = tRAN_MAIN_TRM_WRK_REP.downloadDisbursementDetails();
				   filename = "disbursement.xlsx";
				   flowCode = "DISBT";
				   break;
				
				case "interest":
				   dataList = tRAN_MAIN_TRM_WRK_REP.downloadInterestDetails();
				   filename = "interest.xlsx";
				   flowCode = "INDEM";
				   break;
				
				case "fees":
				   dataList = tRAN_MAIN_TRM_WRK_REP.downloadFeeDetails();
				   filename = "fees.xlsx";
				   flowCode = "FEEDEM";
				   break;
				
				case "penalty":
				   dataList = tRAN_MAIN_TRM_WRK_REP.downloadPenaltyDetails();
				   filename = "penalty.xlsx";
				   flowCode = "PENDEM";
				   break;
				
				case "recovery":
				   dataList = tRAN_MAIN_TRM_WRK_REP.downloadRecoveryDetails();
				   filename = "recovery.xlsx";
				   flowCode = "COLL";
				   break;
				
				default:
				   response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
				   return;
				}
				
				
				System.out.println("Data Fetched for download");
				
				// ✅ Write data rows
				int srlNo = 1;
				for (TRAN_MAIN_TRM_WRK_ENTITY entity : dataList) {
				Row excelRow = sheet.createRow(rowIdx++);
				
				excelRow.createCell(0).setCellValue(srlNo++);
				excelRow.createCell(1).setCellValue(entity.getTran_id()); // FLOW ID
				excelRow.createCell(2).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getFlow_date())); // FLOW DATE
				excelRow.createCell(3).setCellValue(entity.getFlow_code()); // FLOW CODE
				excelRow.createCell(4).setCellValue(entity.getTran_amt().doubleValue()); // FLOW AMOUNT
				excelRow.createCell(5).setCellValue(entity.getAcct_num()); // ACCOUNT NUMBER
				excelRow.createCell(6).setCellValue(entity.getAcct_name()); // ACCOUNT NAME
				}
				
				System.out.println("ready download");
				
				// ✅ Save audit
				audit.insertServiceAudit(userID, userName, "FILE DOWNLOAD!", "DOWNLOADED SUCCESSFULLY","BGLS_TRM_WRK_TRANSACTIONS", "TRANSACTION MAINTENANCE","-");
				
				// ✅ Setup response
				response.setHeader("Content-Disposition", "inline; filename=" + filename);
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

	
	
	
	
	
	
	
	
	
	
	
	private String exportpath1 = System.getProperty("user.home") + File.separator + "exports";
	public File getSheduleDownload(String filetype, String acctNo) throws JRException, SQLException, IOException {
	    System.out.println("Generating report for account: " + acctNo);

	    File folder = new File(exportpath1);
	    if (!folder.exists()) folder.mkdirs();

	    String fileName = "SHEDULE -" + acctNo;
	    File outputFile;

	    try {
	        // Load and compile Jasper
	        Resource resource = new ClassPathResource("/static/jasper/SCHDULE.jrxml");
	        if (!resource.exists()) throw new FileNotFoundException("Jasper file not found: " + resource.getFilename());

	        InputStream jasperStream = resource.getInputStream();
	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

	        // Set parameters
	        HashMap<String, Object> parameters = new HashMap<>();
	        parameters.put("LOAN_ID", acctNo);

	        // Fill report
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, srcdataSource.getConnection());

	        // Export
	        if ("pdf".equalsIgnoreCase(filetype)) {
	            fileName += ".pdf";
	            outputFile = new File(exportpath1, fileName);
	            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile.getAbsolutePath());
	        } else {
	            fileName += ".xlsx";
	            outputFile = new File(exportpath1, fileName);

	            SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
	            reportConfig.setSheetNames(new String[]{fileName});

	            JRXlsxExporter exporter = new JRXlsxExporter();
	            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
	            exporter.setConfiguration(reportConfig);
	            exporter.exportReport();
	        }

	        System.out.println("Report exported successfully: " + outputFile.getAbsolutePath());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new JRException("Error generating Account Ledger report", e);
	    }

	    return outputFile;
	}
	
	public File getDetailDownload(String filetype, String acctNo) throws JRException, SQLException, IOException {
	    System.out.println("Generating report for account: " + acctNo);

	    File folder = new File(exportpath1);
	    if (!folder.exists()) folder.mkdirs();

	    String fileName = "DETAILS -" + acctNo;
	    File outputFile;

	    try {
	        // Load and compile Jasper
	        Resource resource = new ClassPathResource("/static/jasper/DETAILS.jrxml");
	        if (!resource.exists()) throw new FileNotFoundException("Jasper file not found: " + resource.getFilename());

	        InputStream jasperStream = resource.getInputStream();
	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

	        // Set parameters
	        HashMap<String, Object> parameters = new HashMap<>();
	        parameters.put("LOAN_NO", acctNo);

	        // Fill report
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, srcdataSource.getConnection());

	        // Export
	        if ("pdf".equalsIgnoreCase(filetype)) {
	            fileName += ".pdf";
	            outputFile = new File(exportpath1, fileName);
	            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile.getAbsolutePath());
	        } else {
	            fileName += ".xlsx";
	            outputFile = new File(exportpath1, fileName);

	            SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
	            reportConfig.setSheetNames(new String[]{fileName});

	            JRXlsxExporter exporter = new JRXlsxExporter();
	            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
	            exporter.setConfiguration(reportConfig);
	            exporter.exportReport();
	        }

	        System.out.println("Report exported successfully: " + outputFile.getAbsolutePath());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new JRException("Error generating Account Ledger report", e);
	    }

	    return outputFile;
	}
	
	@Autowired
	LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;
	
	public byte[] generateLoanDueExcel(List<Object[]> rawData, String dueDate) {
	    if (rawData == null || rawData.isEmpty()) {
	        System.out.println("No raw data found for due date: " + dueDate);
	        return new byte[0];
	    }

	    Collection<Map<String, ?>> mappedData = new ArrayList<>();
	    for (Object[] row : rawData) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("ACTIVATION_DATE", row[0]);
	        map.put("ACCOUNT_HOLDER_NAME", row[1]);
	        map.put("ACCOUNT_HOLDER_ID", row[2]);
	        map.put("ACCOUNT_ID", row[3]);
	        map.put("LOAN_AMOUNT", row[4]);
	        map.put("MANUALOVERRIDE_AMOUNT", row[5]);
	        map.put("REPAYMENT_INSTALLMENTS", row[6]);
	        map.put("INTEREST_RATE", row[7]);
	        map.put("PRINCIPAL_BALANCE", row[8]);
	        map.put("INTEREST_BALANCE", row[9]);
	        map.put("FEES_BALANCE", row[10]);
	        map.put("TOTAL_BALANCE", row[11]);
	        map.put("LOAN_CYCLE", row[12]);
	        map.put("EMAIL_ADDRESS", row[13]);
	        map.put("MOBILE_PHONE", row[14]);
	        map.put("EMPLOYMENT_STATUS", row[15]);
	        map.put("EMPLOYER", row[16]);
	        map.put("ACCOUNT_STATE", row[17]);
	        map.put("TOTAL_PAID", row[18]);
	        map.put("PRINCIPAL_PAID", row[19]);
	        map.put("INTEREST_PAID", row[20]);
	        map.put("FEES_PAID", row[21]);
	        map.put("RETAILER_NAME", row[22]);
	        map.put("DAYS_LATE", row[23]);
	        map.put("GENDER", row[24]);
	        map.put("BIRTH_DATE", row[25]);
	        mappedData.add(map);
	    }

	    try (InputStream jasperStream = getClass().getResourceAsStream("/static/jasper/EOM.jrxml")) {
	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

	        // Data source for Jasper
	        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(mappedData);

	        // Pass parameters if any
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("DUE_DATE", dueDate); // optional, only if used in jrxml

	        // Fill report
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

	        // Export to Excel
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

	        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
	        configuration.setOnePagePerSheet(false);
	        configuration.setDetectCellType(true);
	        configuration.setCollapseRowSpan(false);
	        exporter.setConfiguration(configuration);

	        exporter.exportReport();
	        System.out.println("✅ Excel generated with " + mappedData.size() + " rows.");
	        return baos.toByteArray();

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new byte[0];
	    }
	}

    public byte[] generateDABExcel(List<Object[]> rawData, String dueDate) {
        if (rawData == null || rawData.isEmpty()) {
            System.out.println("No raw data found for due date: " + dueDate);
            return new byte[0];
        }

        Collection<Map<String, ?>> mappedData = new ArrayList<>();
        for (Object[] row : rawData) {
            Map<String, Object> map = new HashMap<>();
            map.put("TRAN_DATE", row[0]);
            map.put("ACCT_NUM", row[1]);
            map.put("ACCT_NAME", row[2]);
            map.put("TRAN_DR_BAL", row[3]);
            map.put("TRAN_CR_BAL", row[4]);
            map.put("TRAN_TOT_NET", row[5]);
            mappedData.add(map);
        }

        try (InputStream jasperStream = getClass().getResourceAsStream("/static/jasper/DAB.jrxml")) {
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

            // Data source for Jasper
            JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(mappedData);

            // Pass parameters if any
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("TRAN_DATE", dueDate); // optional, only if used in jrxml

            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export to Excel
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(false);
            exporter.setConfiguration(configuration);

            exporter.exportReport();
            System.out.println("✅ Excel generated with " + mappedData.size() + " rows.");
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    
    
    
   

//        public byte[] generateTransactionExcel(List<Object[]> rawData, String dueDate) {
//            if (rawData == null || rawData.isEmpty()) {
//                return new byte[0];
//            }
//
//            try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                Sheet sheet = workbook.createSheet("Transactions_" + dueDate);
//
//                // Header Row
//                Row header = sheet.createRow(0);
//                String[] headers = {
//                    "ACCOUNT_STATE", "CUSTOMER_ID", "CUSTOMER_NAME", "ID", "TOTAL_PRODUCT_PRICE", "LOAN_AMOUNT",
//                    "INTEREST_RATE", "REPAYMENT_INSTALLMENTS", "PRINCIPAL_BALANCE", "INTEREST_BALANCE",
//                    "FEE_BALANCE", "PENALTY_BALANCE", "TOTAL_BALANCE", "ACCT_BAL",
//                    "PRINCIPAL_PAID", "INTEREST_PAID", "FEE_PAID", "PENALTY_PAID",
//                    "DAYS_LATE", "TOTAL_PRINCIPAL_PAID", "TOTAL_INTEREST_PAID",
//                    "TOTAL_FEE_PAID", "TOTAL_PENALTY_PAID"
//                };
//
//                CellStyle headerStyle = workbook.createCellStyle();
//                Font headerFont = workbook.createFont();
//                headerFont.setBold(true);
//                headerStyle.setFont(headerFont);
//                headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // Light blue
//                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                headerStyle.setAlignment(HorizontalAlignment.CENTER);
//                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//                headerStyle.setBorderBottom(BorderStyle.THIN);
//                headerStyle.setBorderTop(BorderStyle.THIN);
//                headerStyle.setBorderLeft(BorderStyle.THIN);
//                headerStyle.setBorderRight(BorderStyle.THIN);
//
//                for (int i = 0; i < headers.length; i++) {
//                    Cell cell = header.createCell(i);
//                    cell.setCellValue(headers[i]);
//                    cell.setCellStyle(headerStyle);
//                }
//
//                // Data Rows
//                int rowNum = 1;
//                for (Object[] rowData : rawData) {
//                    Row row = sheet.createRow(rowNum++);
//                    for (int i = 0; i < rowData.length; i++) {
//                        Cell cell = row.createCell(i);
//                        if (rowData[i] != null) {
//                            cell.setCellValue(rowData[i].toString());
//                        } else {
//                            cell.setCellValue("");
//                        }
//                    }
//                }
//
//                // Auto-size columns
//                for (int i = 0; i < headers.length; i++) {
//                    sheet.autoSizeColumn(i);
//                }
//
//                workbook.write(out);
//                return out.toByteArray();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new byte[0];
//            }
//        }
    
    
//    Consolidated Report
    public byte[] generateTransactionExcel(List<Object[]> rawData, String dueDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CONSOLIDATED_LOAN_REPORT_" + dueDate);

            // ==============================
            // 1️⃣ Title Row
            // ==============================
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("CONSOLIDATED LOAN REPORT");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            int totalColumns = 27;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColumns - 1));

            // ==============================
            // 2️⃣ Printed Date Row
            // ==============================
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            Cell dateCell2 = dateRow.createCell(1);
            dateCell.setCellValue("Printed Date");
            dateCell2.setCellValue(dueDate);

            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateFont.setFontHeightInPoints((short) 11);
            dateStyle.setFont(dateFont);
            dateCell.setCellStyle(dateStyle);
            dateCell2.setCellStyle(dateStyle);

            // ==============================
            // 3️⃣ Header Row
            // ==============================
            Row header = sheet.createRow(3);
            String[] headers = {
                "ACCOUNT_STATE", "CUSTOMER_ID", "CUSTOMER_NAME", "ID", "TOTAL_PRODUCT_PRICE", "LOAN_AMOUNT",
                "INTEREST_RATE", "REPAYMENT_INSTALLMENTS", "PRINCIPAL_BALANCE", "INTEREST_BALANCE",
                "FEE_BALANCE", "PENALTY_BALANCE", "TOTAL_BALANCE", "ACCT_BAL",
                "PRINCIPAL_PAID", "INTEREST_PAID", "FEE_PAID", "PENALTY_PAID",
                "DAYS_LATE", "TOTAL_PRINCIPAL_PAID", "TOTAL_INTEREST_PAID",
                "TOTAL_FEE_PAID", "TOTAL_PENALTY_PAID", "LAST_PAID_DATE", 
                "LAST PRINCIPAL PAID", "LAST INTEREST PAID", "LAST FEE PAID", "LAST PENALTY PAID",
                "PAYMENT AMOUNT","PRODUCT",
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ==============================
            // 4️⃣ Styles
            // ==============================
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            numberStyle.setAlignment(HorizontalAlignment.RIGHT);

            CellStyle integerRightStyle = workbook.createCellStyle();
            integerRightStyle.setAlignment(HorizontalAlignment.RIGHT);
            integerRightStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle textLeftStyle = workbook.createCellStyle();
            textLeftStyle.setAlignment(HorizontalAlignment.LEFT);
            textLeftStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            dateCellStyle.setAlignment(HorizontalAlignment.CENTER);

            // ==============================
            // 5️⃣ Data Rows
            // ==============================
            int rowNum = 4;
            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);

                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    Object value = rowData[i];

                    if (value == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    // CUSTOMER_ID → Left aligned text
                    if (i == 1) {
                        cell.setCellValue(value.toString());
                        cell.setCellStyle(textLeftStyle);
                        continue;
                    }

                    // REPAYMENT_INSTALLMENTS → Integer, right aligned
                    if (i == 7) {
                        try {
                            cell.setCellValue(Integer.parseInt(value.toString()));
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellStyle(integerRightStyle);
                        continue;
                    }
                    
                    

                    // DAYS_LATE → Integer, right aligned
                    if (i == 18) {
                        try {
                            cell.setCellValue(Integer.parseInt(value.toString()));
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellStyle(integerRightStyle);
                        continue;
                    }

                    // LAST_PAID_DATE → format dd-MM-yyyy
                    if (i == 23 || i == 30) {
                        try {
                            if (value instanceof java.util.Date) {
                                cell.setCellValue((Date) value);
                            } else {
                                // Try to parse manually if it's a string date
                                SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
                                cell.setCellValue(parseFormat.parse(value.toString()));
                            }
                            cell.setCellStyle(dateCellStyle);
                        } catch (Exception e) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(textLeftStyle);
                        }
                        continue;
                    }

                    if (i >= 25 && i <= 29) {
                        try {
                            cell.setCellValue(Double.parseDouble(value.toString()));
                            cell.setCellStyle(numberStyle);
                        } catch (Exception e) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(textLeftStyle);
                        }
                        continue;
                    }
                    
                    // Default numeric or text handling
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                        cell.setCellStyle(numberStyle);
                    } else {
                        try {
                            double numericValue = Double.parseDouble(value.toString().replaceAll(",", ""));
                            cell.setCellValue(numericValue);
                            cell.setCellStyle(numberStyle);
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(textLeftStyle);
                        }
                    }
                }
            }


            // ==============================
            // 6️⃣ Summary Row
            // ==============================
            int lastDataRow = rowNum;
            int labelRowIndex = rowNum + 1;
            int sumRowIndex = rowNum + 2;

            Row labelRow = sheet.createRow(labelRowIndex);
            Row sumRow = sheet.createRow(sumRowIndex);

            CellStyle turquoiseStyle = workbook.createCellStyle();
            turquoiseStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            turquoiseStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            turquoiseStyle.setAlignment(HorizontalAlignment.CENTER);
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            turquoiseStyle.setFont(boldFont);

            CellStyle numberStyle1 = workbook.createCellStyle();
            numberStyle1.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            numberStyle1.setAlignment(HorizontalAlignment.RIGHT);

            int principalCol = 8;     // "I"
            int interestCol = 9;      // "J"
            int feeCol = 10;          // "K"
            int penaltyCol = 11;      // "L"
            int totalBalanceCol = 12; // "M"
            int acctBalCol = 13;      // "N"
            int lastPrincipalPaidCol = 24;
            int lastInterestPaidCol = 25;
            int lastFeePaidCol = 26;
            int lastPenaltyPaidCol = 27;
            int paymentAmountCol = 28;

            labelRow.createCell(principalCol).setCellValue("PRINCIPAL");
            labelRow.getCell(principalCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(interestCol).setCellValue("INTEREST");
            labelRow.getCell(interestCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(feeCol).setCellValue("FEE");
            labelRow.getCell(feeCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(penaltyCol).setCellValue("PENALTY");
            labelRow.getCell(penaltyCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(totalBalanceCol).setCellValue("TOTAL BALANCE");
            labelRow.getCell(totalBalanceCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(acctBalCol).setCellValue("ACCT BAL");
            labelRow.getCell(acctBalCol).setCellStyle(turquoiseStyle);
            
            labelRow.createCell(lastPrincipalPaidCol).setCellValue("LAST PRINCIPAL PAID");
            labelRow.getCell(lastPrincipalPaidCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(lastInterestPaidCol).setCellValue("LAST INTEREST PAID");
            labelRow.getCell(lastInterestPaidCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(lastFeePaidCol).setCellValue("LAST FEE PAID");
            labelRow.getCell(lastFeePaidCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(lastPenaltyPaidCol).setCellValue("LAST PENALTY PAID");
            labelRow.getCell(lastPenaltyPaidCol).setCellStyle(turquoiseStyle);

            labelRow.createCell(paymentAmountCol).setCellValue("PAYMENT AMOUNT");
            labelRow.getCell(paymentAmountCol).setCellStyle(turquoiseStyle);

            String principalFormula = "SUM(I5:I" + lastDataRow + ")";
            String interestFormula = "SUM(J5:J" + lastDataRow + ")";
            String feeFormula = "SUM(K5:K" + lastDataRow + ")";
            String penaltyFormula = "SUM(L5:L" + lastDataRow + ")";
            String totalBalanceFormula = "SUM(M5:M" + lastDataRow + ")";
            String acctBalFormula = "SUM(N5:N" + lastDataRow + ")";
            String lastPrincipalSum = "SUM(Y5:Y" + lastDataRow + ")";
            String lastInterestSum = "SUM(Z5:Z" + lastDataRow + ")";
            String lastFeeSum = "SUM(AA5:AA" + lastDataRow + ")";
            String lastPenaltySum = "SUM(AB5:AB" + lastDataRow + ")";
            String paymentAmountSum = "SUM(AC5:AC" + lastDataRow + ")";


            sumRow.createCell(principalCol).setCellFormula(principalFormula);
            sumRow.createCell(interestCol).setCellFormula(interestFormula);
            sumRow.createCell(feeCol).setCellFormula(feeFormula);
            sumRow.createCell(penaltyCol).setCellFormula(penaltyFormula);
            sumRow.createCell(totalBalanceCol).setCellFormula(totalBalanceFormula);
            sumRow.createCell(acctBalCol).setCellFormula(acctBalFormula);
            sumRow.createCell(lastPrincipalPaidCol).setCellFormula(lastPrincipalSum);
            sumRow.createCell(lastInterestPaidCol).setCellFormula(lastInterestSum);
            sumRow.createCell(lastFeePaidCol).setCellFormula(lastFeeSum);
            sumRow.createCell(lastPenaltyPaidCol).setCellFormula(lastPenaltySum);
            sumRow.createCell(paymentAmountCol).setCellFormula(paymentAmountSum);


            sumRow.getCell(principalCol).setCellStyle(numberStyle1);
            sumRow.getCell(interestCol).setCellStyle(numberStyle1);
            sumRow.getCell(feeCol).setCellStyle(numberStyle1);
            sumRow.getCell(penaltyCol).setCellStyle(numberStyle1);
            sumRow.getCell(totalBalanceCol).setCellStyle(numberStyle1);
            sumRow.getCell(acctBalCol).setCellStyle(numberStyle1);
            sumRow.getCell(lastPrincipalPaidCol).setCellStyle(numberStyle1);
            sumRow.getCell(lastInterestPaidCol).setCellStyle(numberStyle1);
            sumRow.getCell(lastFeePaidCol).setCellStyle(numberStyle1);
            sumRow.getCell(lastPenaltyPaidCol).setCellStyle(numberStyle1);
            sumRow.getCell(paymentAmountCol).setCellStyle(numberStyle1);


            Cell summaryLabel = sumRow.createCell(7);
            summaryLabel.setCellValue("TOTAL :");
            summaryLabel.setCellStyle(turquoiseStyle);
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    
//    Transaction Report
    public byte[] generateTransactionExcelReport(List<Object[]> rawData, String dueDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("TRANSACTION_REPORT_" + dueDate);

            // 1️⃣ Title Row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("TRANSACTION REPORT");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 12);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

            // 2️⃣ Printed Date Row
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            Cell dateCell2 = dateRow.createCell(1);
            dateCell.setCellValue("Printed Date ");
            dateCell2.setCellValue(dueDate);

            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateFont.setFontHeightInPoints((short) 11);
            dateStyle.setFont(dateFont);
            dateCell.setCellStyle(dateStyle);
            dateCell2.setCellStyle(dateStyle);

            // 3️⃣ Header Row
            Row header = sheet.createRow(3);
            String[] headers = {
                "TRAN_DATE", "ACCT_NUM", "ACCT_NAME", "TRAN_TYPE", "TRAN_ID",
                "PART_TRAN_ID", "PART_TRAN_TYPE", "CREDIT", "DEBIT", "TRAN_PARTICULAR"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 4️⃣ Data Rows
            CellStyle numberStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            numberStyle.setDataFormat(format.getFormat("#,##0.00"));

            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(format.getFormat("dd-mm-yyyy")); // ✅ Set date format

            CellStyle leftAlignStyle = workbook.createCellStyle();
            leftAlignStyle.setAlignment(HorizontalAlignment.LEFT);

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            int rowNum = 4;
            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    Object value = rowData[i];

                    if (value == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    // ✅ Format TRAN_DATE column (index 0)
                    if (i == 0) {
                        try {
                            String dateStr = value.toString();
                            Date parsedDate = inputDateFormat.parse(dateStr);
                            cell.setCellValue(outputDateFormat.format(parsedDate));
                            cell.setCellStyle(dateCellStyle);
                        } catch (Exception e) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(leftAlignStyle);
                        }
                    }
                    // CREDIT and DEBIT columns are numeric
                    else if (i == 7 || i == 8) {
                        try {
                            double numericValue = Double.parseDouble(value.toString().replaceAll(",", ""));
                            cell.setCellValue(numericValue);
                            cell.setCellStyle(numberStyle);
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(leftAlignStyle);
                        }
                    } else {
                        cell.setCellValue(value.toString());
                        cell.setCellStyle(leftAlignStyle);
                    }
                }
            }

            // 5️⃣ Total SUM Row for CREDIT & DEBIT
            int lastDataRow = rowNum;
            Row sumRow = sheet.createRow(rowNum + 1);

            Cell sumLabel = sumRow.createCell(6);
            sumLabel.setCellValue("TOTAL :");
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            sumLabel.setCellStyle(boldStyle);

            Cell crSumCell = sumRow.createCell(7);
            crSumCell.setCellFormula("SUM(H5:H" + lastDataRow + ")");
            crSumCell.setCellStyle(numberStyle);

            Cell drSumCell = sumRow.createCell(8);
            drSumCell.setCellFormula("SUM(I5:I" + lastDataRow + ")");
            drSumCell.setCellStyle(numberStyle);

            // 6️⃣ Auto-size Columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    public byte[] generateDabExcelReport(List<Object[]> rawData, String tranDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DAB_REPORT_" + tranDate);

            // Title Row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("DAILY ACCOUNT BALANCE REPORT");
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 12);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Printed Date Row
            Row dateRow = sheet.createRow(1);
            Cell dateLabel = dateRow.createCell(0);
            Cell dateValue = dateRow.createCell(1);
            dateLabel.setCellValue("Printed Date");
            dateValue.setCellValue(tranDate);

            // Header Row
            Row header = sheet.createRow(3);
            String[] headers = {
                "GL DESC", "ACCOUNT NO", "ACCOUNT NAME", "OPENING BAL",
                "CR BAL", "DR BAL", "NET", "ACCR BAL", "ACDR BAL"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data + Total Calculation
            CellStyle numberStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            numberStyle.setDataFormat(format.getFormat("#,##0.00"));

            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setAlignment(HorizontalAlignment.LEFT);

            int rowNum = 4;
            double totalOp = 0, totalCr = 0, totalDr = 0, totalNet = 0, totalAccr = 0, totalAcdr = 0;

            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    Object value = rowData[i];
                    if (value == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    // Numeric columns: 3 to 8
                    if (i >= 3) {
                        try {
                            double num = Double.parseDouble(value.toString());
                            cell.setCellValue(num);
                            cell.setCellStyle(numberStyle);

                            switch (i) {
                            case 3:
                                totalOp += num;
                                break;
                            case 4:
                                totalCr += num;
                                break;
                            case 5:
                                totalDr += num;
                                break;
                            case 6:
                                totalNet += num;
                                break;
                            case 7:
                                totalAccr += num;
                                break;
                            case 8:
                                totalAcdr += num;
                                break;
                            default:
                                break;
                        }

                        } catch (NumberFormatException e) {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(textStyle);
                        }
                    } else {
                        cell.setCellValue(value.toString());
                        cell.setCellStyle(textStyle);
                    }
                }
            }

            // Total Row
            Row totalRow = sheet.createRow(rowNum + 1);
            Cell labelCell = totalRow.createCell(2);
            labelCell.setCellValue("TOTAL:");
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            labelCell.setCellStyle(boldStyle);

            double[] totals = {totalOp, totalCr, totalDr, totalNet, totalAccr, totalAcdr};
            for (int i = 0; i < totals.length; i++) {
                Cell totalCell = totalRow.createCell(i + 3);
                totalCell.setCellValue(totals[i]);
                totalCell.setCellStyle(numberStyle);
            }

            // Auto size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    
    
//    --------------------------------------------------------------------
   
public byte[] generateEndOfMonthExcel(List<Object[]> rawData, String dueDate) {
    	
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("END_OF_MONTH_LOAN_REPORT_" + dueDate);

            // --- Constants ---
            final int TOTAL_EXCEL_COLUMNS = 51;

            // ==============================
            // 1️⃣ Title Row
            // ==============================
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("END OF MONTH LOAN REPORT");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, TOTAL_EXCEL_COLUMNS - 1));

            // ==============================
            // 2️⃣ Printed Date Row
            // ==============================
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue("Printed Date:");
            dateRow.createCell(1).setCellValue(dueDate);

            // ==============================
            // 3️⃣ Header Row
            // ==============================
            String[] headers = {
                "Activation Date", "Relationship Manager", "Retailer", "Retailer Branch", "Processed By (Vanguard)",
                "Sale Originated By", "Account Holder Name", "Account Holder ID", "Account ID", "Acc", "Product",
                "Total Product Price", "Loan Amount", "App Approved Amount (Basic Limit)",
                "Manual Override Approved Amount", "TransUnion Score", "Number of Installments",
                "Interest Rate", "Principal Balance", "Interest Balance", "Fee Balance", "Total Balance",
                "Completed Loan Cycles (Client)", "Email (Client)", "Mobile Phone (Client)", "Employment Status",
                "Employer", "Sale Referred By (Old)", "Referrer Contact Number", "Current Month Target (Client)",
                "Full Account State", "Total Paid", "Principal Paid", "Interest Paid", "Fees Paid",
                "Vanguard Application ID", "Name", "Days Late", "Gender (Client)",
                "Post Activation Referral (Client)", "Deposit Amount", "Birth Date (Client)",
                "TransUnion Probability", "TransUnion Status", "ID Number (Client)",
                "Disposal Income", "Verified Income", "Capitalized Interest", "Interest Accrued",
                "Expiry Date", "Sale Processed For"
            };

            Row headerRow = sheet.createRow(3);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ==============================
            // 4️⃣ Styles
            // ==============================
            CellStyle textLeftStyle = workbook.createCellStyle();
            textLeftStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle numberRightStyle = workbook.createCellStyle();
            numberRightStyle.setAlignment(HorizontalAlignment.RIGHT);
            numberRightStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle integerRightStyle = workbook.createCellStyle();
            integerRightStyle.setAlignment(HorizontalAlignment.RIGHT);
            integerRightStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-MM-yyyy"));
            dateStyle.setAlignment(HorizontalAlignment.CENTER);

            // ==============================
            // 5️⃣ Simple Column Mapping
            // ==============================
            // 👉 (-1 means static empty)
            Map<Integer, Integer> columnMap = new LinkedHashMap<>();
            columnMap.put(0, 0);   // Activation Date = object[0]
            columnMap.put(1, -1);  // Relationship Manager = ""
            columnMap.put(2, 1);   // Retailer = object[1]
            columnMap.put(3, 2);   // Retailer Branch = object[2]
            columnMap.put(4, 3);   // Processed By Vanguard = object[3]
            columnMap.put(5, -1);  // Sale Originated By = ""
            columnMap.put(6, 4);   // Account Holder Name = object[4]
            columnMap.put(7, 5);   // Account Holder ID = object[5]
            columnMap.put(8, 6);   // Account ID = object[6]
            columnMap.put(9, -1);  // Acc = ""
            columnMap.put(10, 37); // Product = ""
            columnMap.put(11, 7);  // Total Product Price = object[7]
            columnMap.put(12, 8);  // Loan Amount = object[8]
            columnMap.put(13, -1); // App Approved Amount = ""
            columnMap.put(14, 9);  // Manual Override = object[9]
            columnMap.put(15, 10); // TransUnion Score
            columnMap.put(16, 11); // Installments
            columnMap.put(17, 12); // Interest Rate
            columnMap.put(18, 13); // Principal Balance
            columnMap.put(19, 14); // Interest Balance
            columnMap.put(20, 15); // Fees Balance
            columnMap.put(21, 16); // Total Balance
            columnMap.put(22, 17); // Loan Cycles
            columnMap.put(23, 18); // Email
            columnMap.put(24, 19); // Mobile
            columnMap.put(25, 20); // Employment Status
            columnMap.put(26, 21); // Employer
            columnMap.put(27, 22); // Sale Referred By
            columnMap.put(28, -1); // Referrer Contact No
            columnMap.put(29, -1); // Current Month Target
            columnMap.put(30, 23); // Account State
            columnMap.put(31, 24); // Total Paid
            columnMap.put(32, 25); // Principal Paid
            columnMap.put(33, 26); // Interest Paid
            columnMap.put(34, 27); // Fees Paid
            columnMap.put(35, 28); // VG App ID
            columnMap.put(36, -1); // Name (static)
            columnMap.put(37, 29); // Days Late
            columnMap.put(38, 30); // Gender
            columnMap.put(39, -1); // Post Activation Referral
            columnMap.put(40, 31); // Deposit Amount
            columnMap.put(41, 32); // Birth Date
            columnMap.put(42, 33); // TU Probability
            columnMap.put(43, -1); // TU Status
            columnMap.put(44, -1); // ID Number
            columnMap.put(45, 34); // Disposable Income
            columnMap.put(46, -1); // Verified Income
            columnMap.put(47, -1); // Capitalized Interest
            columnMap.put(48, 35); // Interest Accrued
            columnMap.put(49, -1); // Expiry Date
            columnMap.put(50, 36); // Sale Processed For

            // ==============================
            // 6️⃣ Data Population
            // ==============================
            int rowNum = 4;
            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);
                for (Map.Entry<Integer, Integer> entry : columnMap.entrySet()) {
                    int col = entry.getKey();
                    int src = entry.getValue();
                    Cell cell = row.createCell(col);

                    if (src == -1) {
                        cell.setCellValue("");
                        cell.setCellStyle(textLeftStyle);
                        continue;
                    }

                    Object value = rowData[src];
                    if (value == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    // try date format
                    if (col == 0 || col == 41) {
                        try {
                            if (value instanceof java.util.Date) {
                                cell.setCellValue((Date) value);
                            } else {
                                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").parse(value.toString()));
                            }
                            cell.setCellStyle(dateStyle);
                            continue;
                        } catch (Exception e) {
                            // not a date
                        }
                    }

                    // try numeric
                    if (col == 11 || col == 12 || col == 14 || col == 18 || col == 19 || col == 20 ||
                        col == 21 || col == 31 || col == 32 || col == 33 || col == 34 || col == 40 || col == 45 || col == 48) {
                        try {
                            cell.setCellValue(Double.parseDouble(value.toString()));
                            cell.setCellStyle(numberRightStyle);
                            continue;
                        } catch (Exception ignored) {}
                    }

                    // default text
                    cell.setCellValue(value.toString());
                    cell.setCellStyle(textLeftStyle);
                }
            }
            
            
         // ==============================
         // 7️⃣ Add Total Row at the Bottom
         // ==============================
            CellStyle numberRightStyle2 = workbook.createCellStyle();
            numberRightStyle2.setAlignment(HorizontalAlignment.RIGHT);
            numberRightStyle2.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            numberRightStyle2.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            numberRightStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            numberRightStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            numberRightStyle2.setBorderBottom(BorderStyle.THIN);
            numberRightStyle2.setBorderTop(BorderStyle.THIN);
            numberRightStyle2.setBorderLeft(BorderStyle.THIN);
            numberRightStyle2.setBorderRight(BorderStyle.THIN);
            
         Row totalRow = sheet.createRow(rowNum + 1);
         Cell totalLabelCell = totalRow.createCell(0);
         totalLabelCell.setCellValue("TOTAL");
         totalLabelCell.setCellStyle(headerStyle);

         // Columns to sum
         int[] amountColumns = {
             11, 12, 14, 18, 19, 20, 21, 31, 32, 33, 34, 40, 45, 48
         };

         for (int col : amountColumns) {
             Cell cell = totalRow.createCell(col);
             String colLetter = CellReference.convertNumToColString(col);
             String formula = String.format("SUM(%s5:%s%d)", colLetter, colLetter, rowNum);
             cell.setCellFormula(formula);
             cell.setCellStyle(numberRightStyle2);
         }


            // ==============================
            // 7️⃣ Auto-size Columns
            // ==============================
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
//    ---------------------------------------------------------------------------------------------------------------------------------------------

    public byte[] generateAccrualExcel(List<Object[]> rawData, String accrualDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ACCRUAL_INTEREST_" + accrualDate);

            // ===================== TITLE =====================
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("LOAN ACCRUAL INTEREST REPORT");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // ===================== PRINTED DATE =====================
            Row dateRow = sheet.createRow(1);
            Cell label = dateRow.createCell(0);
            Cell value = dateRow.createCell(1);
            label.setCellValue("Printed Date:");
            value.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateStyle.setFont(dateFont);
            label.setCellStyle(dateStyle);
            value.setCellStyle(dateStyle);

            // ===================== HEADERS =====================
            String[] headers = {
                "Account Number", "Account Name", "Account Type", "Due Date",
                "Days in Arrears", "Interest Amount", "Accrual Date", "Last Due Date", "Tran Date"
            };

            Row headerRow = sheet.createRow(3);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===================== STYLES =====================
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle numStyle = workbook.createCellStyle();
            numStyle.setAlignment(HorizontalAlignment.RIGHT);
            numStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle intStyle = workbook.createCellStyle();
            intStyle.setAlignment(HorizontalAlignment.RIGHT);
            intStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

            CellStyle dateCellStyle = workbook.createCellStyle();
            short dateFormat = workbook.createDataFormat().getFormat("dd-MM-yyyy");
            dateCellStyle.setDataFormat(dateFormat);
            dateCellStyle.setAlignment(HorizontalAlignment.CENTER);

            // ===================== DATA ROWS =====================
            int rowNum = 4;
            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    Object val = rowData[i];

                    if (val == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    switch (i) {
                        case 3: // Due Date
                        case 6: // Accrual Date
                        case 7: // Last Due Date
                        case 8:
                            cell.setCellStyle(dateCellStyle);
                            if (val instanceof Date) {
                                cell.setCellValue((Date) val);
                            } else {
                                cell.setCellValue(val.toString());
                            }
                            break;

                        case 4: // Days in Arrears (Integer)
                            try {
                                cell.setCellValue(Integer.parseInt(val.toString()));
                                cell.setCellStyle(intStyle);
                            } catch (Exception e) {
                                cell.setCellValue(val.toString());
                                cell.setCellStyle(textStyle);
                            }
                            break;

                        case 5: // Interest Amount (Decimal)
                            try {
                                cell.setCellValue(Double.parseDouble(val.toString()));
                                cell.setCellStyle(numStyle);
                            } catch (Exception e) {
                                cell.setCellValue(val.toString());
                                cell.setCellStyle(textStyle);
                            }
                            break;

                        default:
                            cell.setCellValue(val.toString());
                            cell.setCellStyle(textStyle);
                            break;
                    }
                }
            }

            // ===================== AUTO SIZE =====================
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    
    
    public byte[] generatePenaltyExcel(List<Object[]> rawData, String tranDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DAILY_PENALTY_" + tranDate);

            // ===================== TITLE =====================
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("LOAN DAILY PENALTY REPORT");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // ===================== PRINTED DATE =====================
            Row dateRow = sheet.createRow(1);
            Cell label = dateRow.createCell(0);
            Cell value = dateRow.createCell(1);
            label.setCellValue("Printed Date:");
            value.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

            CellStyle dateStyle = workbook.createCellStyle();
            Font dateFont = workbook.createFont();
            dateFont.setItalic(true);
            dateStyle.setFont(dateFont);
            label.setCellStyle(dateStyle);
            value.setCellStyle(dateStyle);

            // ===================== HEADERS =====================
            String[] headers = {
                "Account ID", "Loan Name", "Due Date", "Accrual Date","No of Days","Month No Of Days",
                "Penalty Per Day", "Penalty Per Month", "Tolerance Period", "Total Penalty", "Penalty Rate", "Tran Date"
            };

            Row headerRow = sheet.createRow(3);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===================== STYLES =====================
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle rightAlignStyle = workbook.createCellStyle();
            rightAlignStyle.setAlignment(HorizontalAlignment.RIGHT);

            CellStyle numStyle = workbook.createCellStyle();
            numStyle.setAlignment(HorizontalAlignment.RIGHT);
            numStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle intStyle = workbook.createCellStyle();
            intStyle.setAlignment(HorizontalAlignment.RIGHT);
            intStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

            CellStyle dateCellStyle = workbook.createCellStyle();
            short dateFormat = workbook.createDataFormat().getFormat("dd-MM-yyyy");
            dateCellStyle.setDataFormat(dateFormat);
            dateCellStyle.setAlignment(HorizontalAlignment.CENTER);

            // ===================== DATA ROWS =====================
            int rowNum = 4;
            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    Object val = rowData[i];

                    if (val == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    switch (i) {
                        case 2:
                        case 3:// Due Date
                        case 11:// Due Date
                            cell.setCellStyle(dateCellStyle);
                            if (val instanceof Date) {
                                cell.setCellValue((Date) val);
                            } else {
                                cell.setCellValue(val.toString());
                            }
                            break;

                        case 4: // No of Days
                        case 5: // Month No of Days
                        case 8: // Tolerance Period
                        case 10: // penalty Rate
                            try {
                                cell.setCellValue(Integer.parseInt(val.toString()));
                                cell.setCellStyle(intStyle);
                            } catch (Exception e) {
                                cell.setCellValue(val.toString());
                                cell.setCellStyle(textStyle);
                            }
                            break;

                        case 6: // Penalty Per Day
                        case 7: // Penalty Per Month
                        case 9: // Up to Date Penalty
                            try {
                                cell.setCellValue(Double.parseDouble(val.toString()));
                                cell.setCellStyle(numStyle);
                            } catch (Exception e) {
                                cell.setCellValue(val.toString());
                                cell.setCellStyle(textStyle);
                            }
                            break;

                        default:
                            cell.setCellValue(val.toString());
                            cell.setCellStyle(textStyle);
                            break;
                    }
                }
            }

            // ===================== AUTO SIZE =====================
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    
    
//    -----------------------------------------------------------------------------------------------------------------
    public byte[] TransactionverifyReport(List<Object[]> rawData, String tranDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("TRANSACTION_" + tranDate);

            // ====================== TITLE ======================
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("TRANSACTION REPORT");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

            // ====================== PRINTED DATE ======================
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue("Printed Date:");
            dateRow.createCell(1).setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

            // ====================== HEADERS ======================
            String[] headers = {
                "Tran Date", "Account Number", "Account Name", "Tran Type", "Tran ID",
                "Part Tran ID", "Part Tran Type", "Credit Amount", "Debit Amount", "Tran Particular"
            };

            Row headerRow = sheet.createRow(3);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ====================== CELL STYLES ======================
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle numStyle = workbook.createCellStyle();
            numStyle.setAlignment(HorizontalAlignment.RIGHT);
            numStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-MM-yyyy"));
            dateStyle.setAlignment(HorizontalAlignment.CENTER);

            // ====================== DATA ROWS ======================
            int rowNum = 4;
            for (Object[] rowData : rawData) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    Object val = rowData[i];

                    if (val == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    switch (i) {
                        case 0: // tran_date
                            cell.setCellStyle(dateStyle);
                            if (val instanceof Date)
                                cell.setCellValue((Date) val);
                            else
                                cell.setCellValue(val.toString());
                            break;

                        case 7: // Credit Amount
                        case 8: // Debit Amount
                            try {
                                cell.setCellValue(Double.parseDouble(val.toString()));
                                cell.setCellStyle(numStyle);
                            } catch (Exception e) {
                                cell.setCellValue(val.toString());
                                cell.setCellStyle(textStyle);
                            }
                            break;

                        default:
                            cell.setCellValue(val.toString());
                            cell.setCellStyle(textStyle);
                            break;
                    }
                }
            }

            // ====================== AUTO SIZE ======================
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    
    
    
    
    
    
    
}
