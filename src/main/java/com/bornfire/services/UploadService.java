package com.bornfire.services;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.bornfire.entities.BGLSAuditTable;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.Chart_Acc_Entity;
import com.bornfire.entities.Chart_Acc_Rep;
import com.bornfire.entities.GeneralLedgerEntity;
import com.bornfire.entities.GeneralLedgerRep;
import com.bornfire.entities.LOAN_ACT_MST_ENTITY;
import com.bornfire.entities.LOAN_ACT_MST_REPO;
import com.bornfire.entities.LOAN_REPAYMENT_ENTITY;
import com.bornfire.entities.LOAN_REPAYMENT_REPO;

@Service
@ConfigurationProperties("output")
@Transactional
public class UploadService {
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	CLIENT_MASTER_REPO clientMasterRepo;

	@Autowired
	LOAN_ACT_MST_REPO lOAN_ACT_MST_REPO;

	@Autowired
    LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;

	@Autowired
	DateParser DateParser;

	@Autowired
	BGLSAuditTable_Rep AuditTable_Rep;
	
	@Autowired
	GeneralLedgerRep GeneralLedgerRep;
	
	@Autowired
	Chart_Acc_Rep chart_Acc_Rep;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int delteCustId(List<String> duplicateTr) {
		return clientMasterRepo.delteid(duplicateTr);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int delteLoanId(List<String> duplicateTr) {
		return lOAN_ACT_MST_REPO.delteid(duplicateTr);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int delteRepaymentId(List<String> duplicateTr) {
		return lOAN_REPAYMENT_REPO.delteid(duplicateTr);
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int delteGLId(List<String> duplicateTr) {
		return GeneralLedgerRep.delteid(duplicateTr);
	}
	
	public Map<String, Object> saveCustomerFile(MultipartFile file, String userID, String userName, boolean overwrite)
			throws SQLException {
		int successCount = 0, failureCount = 0;
		Map<String, Object> resultMap = new LinkedHashMap<>();
		logger.info("Start 1");
		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			logger.info("Start 2");
			List<HashMap<Integer, String>> mapList = new ArrayList<>();
			for (Sheet s : workbook) {
				for (Row r : s) {
					if (!isRowEmpty(r)) {
						if (r.getRowNum() < 1)
							continue;

						HashMap<Integer, String> map = new HashMap<>();
						for (int j = 0; j < 200; j++) {
							Cell cell = r.getCell(j);
							DataFormatter formatter1 = new DataFormatter();
							String text = formatter1.formatCellValue(cell);
							map.put(j, text);
						}
						mapList.add(map);
					}
				}
			}
			logger.info("Start 3");
			// ✅ Pre-check duplicates check
			List<String> duplicateid = new ArrayList<>();
			for (HashMap<Integer, String> item : mapList) {
				String cust_id = item.get(1); // <-- taking ARN from column index 23
				// System.out.println(arn);
				CLIENT_MASTER_ENTITY checkId = clientMasterRepo.getid(cust_id);
				logger.info("Start 3.1");
				if (checkId != null) {
					duplicateid.add(cust_id);
				}
			}

			if (!duplicateid.isEmpty() && !overwrite) {
				resultMap.put("status", "duplicate");
				resultMap.put("id", duplicateid);
				return resultMap;
			}

			if (!duplicateid.isEmpty() && overwrite) {
				// delete existing before inserting
				delteCustId(duplicateid);
			}

			// end duplicate check
			// upload start
			for (HashMap<Integer, String> item : mapList) {
				logger.info("Start 4");
				try {
					logger.info("Start 5");
					CLIENT_MASTER_ENTITY transaction = new CLIENT_MASTER_ENTITY();
					transaction.setEncoded_key(item.get(0));
					transaction.setCustomer_id(item.get(1));
					transaction.setClient_state(item.get(2));
					transaction.setCreation_date(DateParser.parseDateSafe(item.get(3)));
					transaction.setLast_modified_date(DateParser.parseDateSafe(item.get(4)));
					transaction.setActivation_date(DateParser.parseDateSafe(item.get(5)));
					transaction.setApproved_date(DateParser.parseDateSafe(item.get(6)));
					transaction.setFirst_name(item.get(7));
					transaction.setLast_name(item.get(8));
					transaction.setMobile_phone(item.get(9));
					transaction.setEmail_address(item.get(10));
					transaction.setPreferred_language(item.get(11));
					transaction.setBirth_date(DateParser.parseDateSafe(item.get(12)));
					transaction.setGender(item.get(13));
					transaction.setAssigned_branch_key(item.get(14));
					transaction.setClient_role_key(item.get(15));
					transaction.setLoan_cycle(DateParser.parseBigDecimal(item.get(16)));
					transaction.setGroup_loan_cycle(DateParser.parseBigDecimal(item.get(17)));
					transaction.setAddress_line1(item.get(18));
					transaction.setAddress_line2(item.get(19));
					transaction.setAddress_line3(item.get(20));
					transaction.setCity(item.get(21));
					transaction.setSuburb(item.get(22));
					transaction.setAssigned_user_key(item.get(23));
					transaction.setAsondate(DateParser.parseDateSafe(item.get(24)));
					transaction.setDel_flg("N");
					transaction.setEntry_user(userID);
					transaction.setEntry_time(new Date());

					logger.info("Start 7");
					clientMasterRepo.save(transaction);
					successCount++;
					// System.out.println("FINAL COUNTS -> Succeeded: " + successCount + ", Failed:
					// " + failureCount);
				} catch (Exception ex) {
					failureCount++;
					ex.printStackTrace();
				}
			}
			logger.info("Start 8");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "error");
			resultMap.put("message", "File upload failed: " + e.getMessage());
		}
		logger.info("Start 9");
		resultMap.put("status", "success");
		resultMap.put("TotalSucceeded", successCount);
		resultMap.put("TotalFailed", failureCount);
		resultMap.put("TotalProcessed", (successCount + failureCount));

		return resultMap;
	}

	// LOAN UPLOAD
	public Map<String, Object> saveLoanFile(MultipartFile file, String userID, String userName, boolean overwrite)
			throws SQLException {
		int successCount = 0, failureCount = 0;
		Map<String, Object> resultMap = new LinkedHashMap<>();
		logger.info("Start 1");
		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			logger.info("Start 2");
			List<HashMap<Integer, String>> mapList = new ArrayList<>();
			for (Sheet s : workbook) {
				for (Row r : s) {
					if (!isRowEmpty(r)) {
						if (r.getRowNum() < 1)
							continue;

						HashMap<Integer, String> map = new HashMap<>();
						for (int j = 0; j < 200; j++) {
							Cell cell = r.getCell(j);
							DataFormatter formatter1 = new DataFormatter();
							String text = formatter1.formatCellValue(cell);
							map.put(j, text);
						}
						mapList.add(map);
					}
				}
			}
			logger.info("Start 3");
			// ✅ Pre-check duplicates check
			List<String> duplicateid = new ArrayList<>();
			for (HashMap<Integer, String> item : mapList) {
				String cust_id = item.get(1); // <-- taking ARN from column index 23
				// System.out.println(arn);
				LOAN_ACT_MST_ENTITY checkId = lOAN_ACT_MST_REPO.getid(cust_id);
				logger.info("Start 3.1");
				if (checkId != null) {
					duplicateid.add(cust_id);
				}
			}

			if (!duplicateid.isEmpty() && !overwrite) {
				resultMap.put("status", "duplicate");
				resultMap.put("id", duplicateid);
				return resultMap;
			}

			if (!duplicateid.isEmpty() && overwrite) {
				// delete existing before inserting
				delteLoanId(duplicateid);
			}

			// end duplicate check
			// upload start
			for (HashMap<Integer, String> item : mapList) {
				logger.info("Start 4");
				try {
					logger.info("Start 5");
					LOAN_ACT_MST_ENTITY transaction = new LOAN_ACT_MST_ENTITY();

					transaction.setEncoded_key(item.get(0));
					transaction.setId(item.get(1));
					transaction.setAccount_holdertype(item.get(2));
					transaction.setAccount_holderkey(item.get(3));
					transaction.setCreation_date(DateParser.parseDateSafe(item.get(4)));
					transaction.setApproved_date(DateParser.parseDateSafe(item.get(5)));
					transaction.setLast_modified_date(DateParser.parseDateSafe((item.get(6))));
					transaction.setClosed_date(DateParser.parseDateSafe(item.get(7)));
					transaction.setLast_account_appraisaldate(DateParser.parseDateSafe(item.get(8)));
					transaction.setAccount_state(item.get(9));
					transaction.setAccount_substate(item.get(10));
					transaction.setProduct_typekey(item.get(11));
					transaction.setLoan_name(item.get(12));
					transaction.setPayment_method(item.get(13));
					transaction.setAssigned_branchkey(item.get(14));
					transaction.setLoan_amount(DateParser.parseBigDecimal(item.get(15)));
					transaction.setInterest_rate(DateParser.parseBigDecimal(item.get(16)));
					transaction.setPenalty_rate(DateParser.parseBigDecimal(item.get(17)));
					transaction.setAccrued_interest(DateParser.parseBigDecimal(item.get(18)));
					transaction.setAccrued_penalty(DateParser.parseBigDecimal(item.get(19)));
					transaction.setPrincipal_due(DateParser.parseBigDecimal(item.get(20)));
					transaction.setPrincipal_paid(DateParser.parseBigDecimal(item.get(21)));
					transaction.setPrincipal_balance(DateParser.parseBigDecimal(item.get(22)));
					transaction.setInterest_due(DateParser.parseBigDecimal(item.get(23)));
					transaction.setInterest_paid(DateParser.parseBigDecimal(item.get(24)));
					transaction.setInterest_balance(DateParser.parseBigDecimal(item.get(25)));
					transaction.setInterest_fromarrearsbalance(DateParser.parseBigDecimal(item.get(26)));
					transaction.setInterest_fromarrearsdue(DateParser.parseBigDecimal(item.get(27)));
					transaction.setInterest_fromarrearspaid(DateParser.parseBigDecimal(item.get(28)));
					transaction.setFees_due(DateParser.parseBigDecimal(item.get(29)));
					transaction.setFees_paid(DateParser.parseBigDecimal(item.get(30)));
					transaction.setFees_balance(DateParser.parseBigDecimal(item.get(31)));
					transaction.setPenalty_due(DateParser.parseBigDecimal(item.get(32)));
					transaction.setPenalty_paid(DateParser.parseBigDecimal(item.get(33)));
					transaction.setPenalty_balance(DateParser.parseBigDecimal(item.get(34)));
					transaction.setExpected_disbursementdate(DateParser.parseDateSafe(item.get(35)));
					transaction.setDisbursement_date(DateParser.parseDateSafe(item.get(36)));
					transaction.setFirst_repaymentdate(DateParser.parseDateSafe(item.get(37)));
					transaction.setApproved_date(new Date());
					transaction.setDisbursement_flg("N");
					transaction.setInterest_flg("N");
					transaction.setFees_flg("N");
					transaction.setRecovery_flg("N");
					transaction.setBooking_flg("N");
					transaction.setGrace_period(DateParser.parseBigDecimal(item.get(38)));
					transaction.setRepayment_installments(DateParser.parseBigDecimal(item.get(39)));
					transaction.setRepayment_periodcount(DateParser.parseBigDecimal(item.get(40)));
					transaction.setDays_late(DateParser.parseBigDecimal(item.get(41)));
					transaction.setDays_inarrears(DateParser.parseBigDecimal(item.get(42)));
					transaction.setRepayment_schedule_method(item.get(43));
					transaction.setCurrency_code(item.get(44));
					transaction.setSale_processedbyvgid(item.get(45));
					transaction.setSale_processedfor(item.get(46));
					transaction.setSale_referredby(item.get(47));
					transaction.setEmployment_status(item.get(48));
					transaction.setJob_title(item.get(49));
					transaction.setEmployer_name(item.get(50));
					transaction.setTuscore(DateParser.parseBigDecimal(item.get(51)));
					transaction.setTuprobability(DateParser.parseBigDecimal(item.get(52)));
					transaction.setTufullname(item.get(53));
					transaction.setTureason1(item.get(54));
					transaction.setTureason2(item.get(55));
					transaction.setTureason3(item.get(56));
					transaction.setTureason4(item.get(57));
					transaction.setDisposable_income(DateParser.parseBigDecimal(item.get(58)));
					transaction.setManualoverride_amount(DateParser.parseBigDecimal(item.get(59)));
					transaction.setManualoverride_expiry_date(DateParser.parseDateSafe(item.get(60)));
					transaction.setCpfees(DateParser.parseBigDecimal(item.get(61)));
					transaction.setDeposit_amount(DateParser.parseBigDecimal(item.get(62)));
					transaction.setTotal_product_price(DateParser.parseBigDecimal(item.get(63)));
					transaction.setRetailer_name(item.get(64));
					transaction.setRetailer_branch(item.get(65));
					transaction.setVg_application_id(item.get(66));
					transaction.setContract_signed(item.get(67));
					transaction.setDate_of_first_call(DateParser.parseDateSafe(item.get(68)));
					transaction.setLast_call_outcome(item.get(69));
					transaction.setAsondate(DateParser.parseDateSafe(item.get(70)));
					transaction.setDel_flg("N");
					transaction.setEntry_user(userID);
					transaction.setEntry_time(new Date());
					logger.info("Start 7");
					lOAN_ACT_MST_REPO.save(transaction);
					successCount++;
					// System.out.println("FINAL COUNTS -> Succeeded: " + successCount + ", Failed:
					// " + failureCount);
					
					Chart_Acc_Entity coa = new Chart_Acc_Entity();

					coa.setAcct_num(item.get(1));
					coa.setAcct_name(item.get(12));
					coa.setAcct_crncy("MUR");
					coa.setAcct_bal(BigDecimal.ZERO);
					coa.setCr_amt(BigDecimal.ZERO);
					coa.setDr_amt(BigDecimal.ZERO);
					coa.setGl_code("1000");
					coa.setGl_desc("Asset");
					coa.setGlsh_code("Asset");
					coa.setGlsh_desc("LOANS AND ADVANCES");
					coa.setSchm_code("LA");
					coa.setSchm_type("LOAN");
					coa.setAcct_status("Active");
					//coa.setMobile_no(new BigDecimal(up.getCa_mobile_number()));
					/* coa.setNational_id(up.getCa_idenditification_number()); */
					coa.setClassification("Asset");
					coa.setAdd_det_flg("N");
					coa.setEntity_flg("Y");
					coa.setDel_flg("N");
					coa.setAcct_status("Y");
					coa.setAcct_cls_flg("N");
					coa.setAcct_type("L");

					chart_Acc_Rep.save(coa);
					logger.info("Start 7.1");
				} catch (Exception ex) {
					failureCount++;
					ex.printStackTrace();
				}
			}
			logger.info("Start 8");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "error");
			resultMap.put("message", "File upload failed: " + e.getMessage());
		}
		logger.info("Start 9");
		resultMap.put("status", "success");
		resultMap.put("TotalSucceeded", successCount);
		resultMap.put("TotalFailed", failureCount);
		resultMap.put("TotalProcessed", (successCount + failureCount));

		return resultMap;
	}

	// REPAYMENT UPLOAD
		public Map<String, Object> saveRepaymentFile(MultipartFile file, String userID, String userName, boolean overwrite)
				throws SQLException {
			int successCount = 0, failureCount = 0;
			Map<String, Object> resultMap = new LinkedHashMap<>();
			long startTime = System.currentTimeMillis(); // Track total execution time

			logger.info("Start processing file: " + file.getOriginalFilename());

			try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
				logger.info("Workbook loaded");

				List<HashMap<Integer, String>> mapList = new ArrayList<>();
				int totalRows = 0;

				for (Sheet s : workbook) {
					for (Row r : s) {
						if (!isRowEmpty(r)) {
							if (r.getRowNum() < 1)
								continue; // skip header
							totalRows++;

							HashMap<Integer, String> map = new HashMap<>();
							for (int j = 0; j < 200; j++) {
								Cell cell = r.getCell(j);
								DataFormatter formatter1 = new DataFormatter();
								String text = formatter1.formatCellValue(cell);
								map.put(j, text);
							}
							mapList.add(map);
						}
					}
				}

				logger.info("Total rows found in file: " + totalRows);
				System.out.println("Total rows found in file: " + totalRows);

				// Batch Insert
				List<LOAN_REPAYMENT_ENTITY> batchList = new ArrayList<>();
				int batchSize = 1000;

				int currentRow = 0;
				for (HashMap<Integer, String> item : mapList) {
					currentRow++;
					try {
						LOAN_REPAYMENT_ENTITY transaction = new LOAN_REPAYMENT_ENTITY();
						transaction.setEncoded_key(item.get(0));
						transaction.setDue_date(com.bornfire.services.DateParser.parseDateSafe1(item.get(3)));
						transaction.setInterest_exp(DateParser.parseBigDecimal(item.get(4)));
						transaction.setInterest_due(DateParser.parseBigDecimal(item.get(4)));
						transaction.setInterest_paid(DateParser.parseBigDecimal(item.get(5)));
						transaction.setLast_paid_date(com.bornfire.services.DateParser.parseDateSafe1(item.get(6)));
						transaction.setParent_account_key(item.get(9));
						transaction.setPrincipal_exp(DateParser.parseBigDecimal(item.get(10)));
						transaction.setPrincipal_due(DateParser.parseBigDecimal(item.get(10)));
						transaction.setPrincipal_paid(DateParser.parseBigDecimal(item.get(11)));
						transaction.setRepaid_date(com.bornfire.services.DateParser.parseDateSafe1(item.get(12)));
						transaction.setPayment_state(item.get(13));
						transaction.setFee_exp(DateParser.parseBigDecimal(item.get(15)));
						transaction.setFee_due(DateParser.parseBigDecimal(item.get(15)));
						transaction.setFee_paid(DateParser.parseBigDecimal(item.get(16)));
						transaction.setPenalty_exp(DateParser.parseBigDecimal(item.get(17)));
						transaction.setPenalty_due(DateParser.parseBigDecimal(item.get(17)));
						transaction.setPenalty_paid(DateParser.parseBigDecimal(item.get(18)));
						transaction.setDel_flg("N");
						transaction.setEntry_user(userID);
						transaction.setEntry_time(new Date());

						batchList.add(transaction);

						if (batchList.size() == batchSize) {
							lOAN_REPAYMENT_REPO.saveAll(batchList);
							successCount += batchList.size();
							System.out.println("Uploaded rows: " + successCount + " | Current row: " + currentRow);
							batchList.clear();
						}
					} catch (Exception ex) {
						failureCount++;
						System.err.println("Failed row: " + currentRow + " | Error: " + ex.getMessage());
					}
				}

				// Save remaining rows
				if (!batchList.isEmpty()) {
					lOAN_REPAYMENT_REPO.saveAll(batchList);
					successCount += batchList.size();
					System.out.println("Uploaded remaining rows: " + batchList.size());
					batchList.clear();
				}

			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("status", "error");
				resultMap.put("message", "File upload failed: " + e.getMessage());
				return resultMap;
			}

			long endTime = System.currentTimeMillis();
			long durationSec = (endTime - startTime) / 1000;

			System.out.println("File processing completed.");
			System.out.println("Total succeeded: " + successCount);
			System.out.println("Total failed: " + failureCount);
			System.out.println("Total processed: " + (successCount + failureCount));
			System.out.println("Time taken (seconds): " + durationSec);

			resultMap.put("status", "success");
			resultMap.put("TotalSucceeded", successCount);
			resultMap.put("TotalFailed", failureCount);
			resultMap.put("TotalProcessed", (successCount + failureCount));
			resultMap.put("TimeTakenSeconds", durationSec);

			return resultMap;
		}
	
	 //GL UPLOAD
	
	public Map<String, Object> saveGLFile(MultipartFile file, String userID, String userName,boolean overwrite ,String auditRefNo) throws SQLException {
		int successCount = 0, failureCount = 0;
		Map<String, Object> resultMap = new LinkedHashMap<>();
		logger.info("Start 1");
		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			logger.info("Start 2");
			List<HashMap<Integer, String>> mapList = new ArrayList<>();
			for (Sheet s : workbook) {
			    for (Row r : s) {
			        if (!isRowEmpty(r)) {
			            if (r.getRowNum() < 1)
			                continue;

			            HashMap<Integer, String> map = new HashMap<>();
			            for (int j = 0; j < 200; j++) {
			                Cell cell = r.getCell(j);
			                DataFormatter formatter1 = new DataFormatter();
			                String text = formatter1.formatCellValue(cell);
			                map.put(j, text);
			            }
			            mapList.add(map);
			        }
			    }
			}
			logger.info("Start 3");
			// ✅ Pre-check duplicates check
			List<String> duplicateid = new ArrayList<>();
			for (HashMap<Integer, String> item : mapList) {
				String cust_id = item.get(4); // <-- taking ARN from column index 23
				// System.out.println(arn);
				GeneralLedgerEntity checkId = GeneralLedgerRep.getid(cust_id);
				logger.info("Start 3.1");
				if (checkId != null) {
					duplicateid.add(cust_id);
				}
			}

			if (!duplicateid.isEmpty() && !overwrite) {
				resultMap.put("status", "duplicate");
				resultMap.put("id", duplicateid);
				return resultMap;
			}

			
			if (!duplicateid.isEmpty() && overwrite) {
				// delete existing before inserting
				delteGLId(duplicateid);
			}
			 
			//end duplicate check
			//upload start
			for (HashMap<Integer, String> item : mapList) {
				logger.info("Start 4");
				try {
					logger.info("Start 5");
					GeneralLedgerEntity entity = new GeneralLedgerEntity();
					
					entity.setBranch_id(item.get(0));               // Branch Id
					entity.setBranch_desc(item.get(1));             // Branch Description
					entity.setGlCode(item.get(2));                  // GL CODE
					entity.setGlDescription(item.get(3));           // GL Description
					entity.setGlsh_code(item.get(4));               // GL Sub Head Code (PK)
					entity.setGlsh_desc(item.get(5));               // GLSH Description
					entity.setCrncy_code(item.get(6));              // Currency Code
					entity.setBal_sheet_group(item.get(7));         // Balance Sheet Group
					entity.setSeq_order(item.get(8));               // Sequence Order
					entity.setTotal_balance(item.get(9));           // Total Balance
					entity.setNo_acct_opened(item.get(10));         // No of Account Opened
					entity.setNo_acct_closed(item.get(11));         // No of Account Closed

					// Optional fields (if needed)
					/*
					 * entity.setRemarks(null); entity.setGl_type(null);
					 * entity.setGl_type_description(null); entity.setModule(null);
					 */
					entity.setEntity_flg("N");
					entity.setDelFlg("N");
					entity.setModifyFlg("N");
					entity.setEntry_user(userID);
					String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					entity.setEntry_time(now);

					logger.info("Start 7");
					GeneralLedgerRep.save(entity);
					successCount++;
					//System.out.println("FINAL COUNTS -> Succeeded: " + successCount + ", Failed: " + failureCount);
				} catch (Exception ex) {
					failureCount++;
					ex.printStackTrace();
				}
			}
			logger.info("Start 8");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "error");
			resultMap.put("message", "File upload failed: " + e.getMessage());
		}
		logger.info("Start 9");	
		saveAudit(userID, userName, "GL File Upload!", " BGLS_GENERAL_LED", auditRefNo);
		logger.info("Start 10");
		resultMap.put("status", "success");
		resultMap.put("TotalSucceeded", successCount);
		resultMap.put("TotalFailed", failureCount);
		resultMap.put("TotalProcessed", (successCount + failureCount));

		return resultMap;
	}

	private boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
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

}
