package com.bornfire.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.config.SequenceGenerator;
import com.bornfire.entities.Access_Role_Entity;
import com.bornfire.entities.Account_Ledger_Rep;
import com.bornfire.entities.BACP_CUS_PROFILE_REPO;
import com.bornfire.entities.BGLSAuditTable;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.BGLSBusinessTable_Entity;
import com.bornfire.entities.BGLSBusinessTable_Rep;
import com.bornfire.entities.BGLS_CONTROL_TABLE_REP;
import com.bornfire.entities.BGLS_Control_Table;
import com.bornfire.entities.BglsLmsSchemesEntity;
import com.bornfire.entities.BglsLmsSchemesRepo;
import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.Chart_Acc_Entity;
import com.bornfire.entities.Chart_Acc_Rep;
import com.bornfire.entities.CustomerRequest;
import com.bornfire.entities.DAB_Repo;
import com.bornfire.entities.DMD_TABLE;
import com.bornfire.entities.DMD_TABLE_REPO;
import com.bornfire.entities.DepositEntity;
import com.bornfire.entities.DepositRep;
import com.bornfire.entities.Employee_Profile;
import com.bornfire.entities.Employee_Profile_Rep;
import com.bornfire.entities.GeneralLedgerEntity;
import com.bornfire.entities.GeneralLedgerRep;
import com.bornfire.entities.GeneralLedgerWork_Rep;
import com.bornfire.entities.LOAN_ACT_MST_ENTITY;
import com.bornfire.entities.LOAN_ACT_MST_REPO;
import com.bornfire.entities.LOAN_REPAYMENT_ENTITY;
import com.bornfire.entities.LOAN_REPAYMENT_REPO;
import com.bornfire.entities.LeaseData;
import com.bornfire.entities.Lease_Loan_Master_Entity;
import com.bornfire.entities.Lease_Loan_Master_Repo;
import com.bornfire.entities.Lease_Loan_Work_Entity;
import com.bornfire.entities.Lease_Loan_Work_Repo;
import com.bornfire.entities.NoticeDetailsPayment0Entity;
import com.bornfire.entities.NoticeDetailsPayment0Rep;
import com.bornfire.entities.Organization_Branch_Entity;
import com.bornfire.entities.Organization_Branch_Rep;
import com.bornfire.entities.Organization_Entity;
import com.bornfire.entities.Organization_Repo;
import com.bornfire.entities.Principle_and_intrest_shedule_Entity;
import com.bornfire.entities.Reference_Code_Entity;
import com.bornfire.entities.Reference_code_Rep;
import com.bornfire.entities.RepaymentScheduleEntity;
import com.bornfire.entities.Settlement_Collection_Entity;
import com.bornfire.entities.TRAN_MAIN_TRM_REP;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_ENTITY;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_REP;
import com.bornfire.entities.Td_defn_Repo;
import com.bornfire.entities.Td_defn_table;
import com.bornfire.entities.TestPrincipalCalculation;
import com.bornfire.entities.Transaction_Partition_Detail_Entity;
import com.bornfire.entities.Transaction_Partition_Detail_Repo;
import com.bornfire.entities.Transaction_Pointing_Table_Entity;
import com.bornfire.entities.Transaction_Pointing_Table_Repo;
import com.bornfire.entities.Transaction_Reversed_Table_Entity;
import com.bornfire.entities.Transaction_Reversed_Table_Repo;
import com.bornfire.entities.UserProfile;
import com.bornfire.entities.UserProfileRep;
import com.bornfire.services.AdminOperServices;
import com.bornfire.services.DepositServices;
import com.bornfire.services.InterestCalculationServices;
import com.bornfire.services.LeaseLoanService;
import com.bornfire.services.LoginServices;
import com.bornfire.services.RepaymentScheduleServices;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

@RestController
@Transactional

public class BGLSRestController {
//	
//	@Autowired
//	Organization_Branch_Rep organization_Branch_Rep;
	
	@Autowired
	BglsLmsSchemesRepo bgls_lms_scheme_repo;

	@Autowired
	Employee_Profile_Rep employee_Profile_Rep;

	@Autowired
	LoginServices loginServices;

	@Autowired
	DAB_Repo dab_Repo;

	@Autowired
	Chart_Acc_Rep chart_Acc_Rep;

	@Autowired
	Account_Ledger_Rep account_Ledger_Rep;

	@Autowired
	Organization_Branch_Rep organization_Branch_Rep;

	@Autowired
	Organization_Repo organization_Repo;

	@Autowired
	Reference_code_Rep reference_code_Rep;

	@Autowired
	AdminOperServices adminOperServices;

	@Autowired
	DMD_TABLE_REPO dmdRepo;

	@Autowired
	NoticeDetailsPayment0Rep noticeDetailsPayment0Rep;

	@Autowired
	RepaymentScheduleServices repaymentScheduleServices;

	@Autowired
	SequenceGenerator sequence;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	BGLSBusinessTable_Rep bGLSBusinessTable_Rep;

	@Autowired
	BGLSAuditTable_Rep bGLSAuditTable_Rep;

	@Autowired
	DepositRep depositRep;

	@Autowired
	BACP_CUS_PROFILE_REPO bACP_CUS_PROFILE_REPO;

	@Autowired
	InterestCalculationServices interestCalculationServices;

	@Autowired
	TRAN_MAIN_TRM_REP transactionMaintenanceRepository;

	@Autowired
	TRAN_MAIN_TRM_WRK_REP tranMainRep;

	@Autowired
	Td_defn_Repo td_defn_Repo;

	@Autowired
	DepositServices depositServices;

	@Autowired
	Lease_Loan_Master_Repo lease_Loan_Master_Repo;

	@Autowired
	LeaseLoanService leaseLoanService;

	@Autowired
	Lease_Loan_Work_Repo lease_Loan_Work_Repo;

	@Autowired
	TRAN_MAIN_TRM_WRK_REP tRAN_MAIN_TRM_WRK_REP;

	@Autowired
	DMD_TABLE_REPO dMD_TABLE_REPO;

	@Autowired
	GeneralLedgerWork_Rep generalLedgerWork_Rep;

	@Autowired
	BGLSBusinessTable_Rep bglsBusinessTable_Rep;

	@Autowired
	GeneralLedgerRep generalLedgerRep;

	@Autowired
	Transaction_Partition_Detail_Repo transaction_Partition_Detail_Repo;

	@Autowired
	Transaction_Pointing_Table_Repo transaction_Pointing_Table_Repo;

	@Autowired
	Transaction_Reversed_Table_Repo transaction_Reversed_Table_Repo;

	@Autowired
	CLIENT_MASTER_REPO cLIENT_MASTER_REPO;

	@Autowired
	LOAN_ACT_MST_REPO lOAN_ACT_MST_REPO;

	@Autowired
	LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;

	@Autowired
	BGLS_CONTROL_TABLE_REP bGLS_CONTROL_TABLE_REP;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* THANVEER */
	@RequestMapping(value = "employeeAdd", method = RequestMethod.POST)
	@ResponseBody
	public String employeeAdd(HttpServletRequest rq, @ModelAttribute Employee_Profile employee_Profile) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		List<String> existingdata = employee_Profile_Rep.getexistingData();
		if (existingdata.contains(employee_Profile.getEmployee_id())) {
			return "Employee Id Already Exist";
		} else {
			employee_Profile.setEntry_user(userid);
			employee_Profile.setEntry_time(new Date());
			employee_Profile.setModify_time(new Date());
			employee_Profile.setModify_user(userid);
			employee_Profile.setEntity_flg("Y");
			employee_Profile.setModify_flg("N");
			employee_Profile.setVerify_flg("Y");
			employee_Profile.setDel_flg("N");
			
			// FOR AUIDT
			BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
			Long auditID = bGLSBusinessTable_Rep.getAuditRefUUID();
			Optional<UserProfile> up1 = userProfileRep.findById(userid);
			UserProfile user = up1.get();

			LocalDateTime currentDateTime = LocalDateTime.now();
			Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
			audit.setAudit_date(new Date());
			audit.setEntry_time(dateValue);
			audit.setEntry_user(user.getUserid());
			audit.setFunc_code("Employee Id");
			audit.setRemarks("Sucessfully Saved");
			audit.setAudit_table("BGLS_EMPLOYEE_PROFILE");
			audit.setAudit_screen("EMPLOYEE PROFILE - ADD");
			audit.setEvent_id(user.getUserid());
			audit.setEvent_name(user.getUsername());
			// audit.setModi_details("Login Successfully");
			UserProfile auth_user = userProfileRep.getRole(user.getUserid());
			String auth_user_val = auth_user.getAuth_user();
			Date auth_user_date = auth_user.getAuth_time();
			audit.setAuth_user(auth_user_val);
			audit.setAuth_time(auth_user_date);
			audit.setAudit_ref_no(auditID.toString());
			audit.setField_name("-");
			employee_Profile_Rep.save(employee_Profile);
			bGLSBusinessTable_Rep.save(audit);
			return "Sucessfully Saved";
		}

	}

	@RequestMapping(value = "employeeAdddemo", method = RequestMethod.POST)
	@ResponseBody
	public String employeeAdddemo(HttpServletRequest rq, @ModelAttribute Employee_Profile employee_Profile) {
		String user = (String) rq.getSession().getAttribute("USERID");
		List<String> existingData = employee_Profile_Rep.getexistingData();

		if (existingData.contains(employee_Profile.getEmployee_id())) {
			return "Employee Id Already Exist";
		} else {
			employee_Profile.setEntry_user(user);
			employee_Profile.setEntry_time(new Date());
			employee_Profile.setEntity_flg("N");
			employee_Profile.setModify_flg("N");
			employee_Profile.setDel_flg("N");

			employee_Profile_Rep.save(employee_Profile);
			return "Successfully Saved";
		}
	}

	@RequestMapping(value = "employeeAdd1", method = RequestMethod.POST)
	@ResponseBody
	public String employeeAddPhoto(@RequestParam("photoFile") MultipartFile photoFile, @RequestParam String employeeId,
			HttpServletRequest rq) {

		System.out.println("Uploading photo for employee ID: " + employeeId);

		// Check if the photo file is provided
		if (photoFile == null || photoFile.isEmpty()) {
			return "Please upload an image.";
		}

		// Fetch the Employee_Profile record by employeeId
		Employee_Profile employee_Profile = employee_Profile_Rep.getEmployeeData(employeeId);
		if (employee_Profile == null) {
			return "Employee not found.";
		}

		try {
			byte[] photoBytes = photoFile.getBytes();
			employee_Profile.setEmployee_Photo(photoBytes);

			// Save the updated profile
			employee_Profile_Rep.save(employee_Profile);
			return "Photo uploaded successfully.";
		} catch (IOException e) {
			e.printStackTrace();
			return "Error processing the image.";
		}
	}

	/* Praveen */
	@RequestMapping(value = "createUser", method = RequestMethod.POST)
	public String createUser(@RequestParam("formmode") String formmode,
			@RequestParam("accountExpiryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accountExpiryDate,
			@RequestBody UserProfile userprofile, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		System.out.println("accountExpiryDate " + accountExpiryDate);
		userprofile.setAcc_exp_date(accountExpiryDate);
		String msg = loginServices.addUser(userprofile, formmode, userid);

		return msg;
	}

	/* Praveen */
	@RequestMapping(value = "verifyUser", method = RequestMethod.POST)
	public String verifyUser(@RequestParam("userId") String userId, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		String msg = loginServices.verifyUser(userId, userid);

		return msg;

	}

	/* Praveen */
	@RequestMapping(value = "mosifyUser", method = RequestMethod.POST)
	public String mosifyUser(@RequestParam("formmode") String formmode,
			@RequestParam("accountExpiryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date accountExpiryDate,
			@RequestBody UserProfile userprofile, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		String userid = (String) rq.getSession().getAttribute("USERID");
		System.out.println("accountExpiryDate " + accountExpiryDate);
		userprofile.setAcc_exp_date(accountExpiryDate);
		String msg = loginServices.addUser(userprofile, formmode, userid);

		return msg;
	}

	/* Praveen */
	@RequestMapping(value = "deleteUser", method = RequestMethod.POST)
	public String deleteUser(@RequestParam("userid") String userid, @RequestParam("deleteType") String deleteType,
			Model md, HttpServletRequest rq) {
		String userid1 = (String) rq.getSession().getAttribute("USERID");
		String msg = loginServices.deleteUser(userid, deleteType, userid1);

		return msg;

	}

	/* THANVEER */
	@RequestMapping(value = "employeeVerify", method = RequestMethod.POST)
	@ResponseBody
	public String employeeVerify(HttpServletRequest rq, @ModelAttribute Employee_Profile employee_Profile) {

		System.out.println(employee_Profile + "verify");
		Employee_Profile up = employee_Profile_Rep.getEmployeeData(employee_Profile.getEmployee_id());
		String userid = (String) rq.getSession().getAttribute("USERID");
		employee_Profile.setEntity_flg("Y");
		employee_Profile.setModify_flg("Y");
		employee_Profile.setVerify_flg("N");
		employee_Profile.setDel_flg("N");
		employee_Profile.setEntry_time(up.getEntry_time());
		employee_Profile.setEntry_user(up.getEntry_user());
		employee_Profile.setEmployee_Photo(up.getEmployee_Photo());
		employee_Profile.setVerify_time(new Date());
		employee_Profile.setVerify_user(userid);
		employee_Profile_Rep.save(employee_Profile);
		System.out.println("Sucessfully Verified");

		// FOR AUIDT
		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		Long auditID = bGLSBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());
		audit.setFunc_code("Employee Id");
		audit.setRemarks("Sucessfully Verified");
		audit.setAudit_table("BGLS_EMPLOYEE_PROFILE");
		audit.setAudit_screen("EMPLOYEE PROFILE - VERIFY");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bGLSBusinessTable_Rep.save(audit);
		return "Sucessfully Verified";

	}

	/* THANVEER */
	@RequestMapping(value = "employeeModify", method = RequestMethod.POST)
	@ResponseBody
	public String employeeModify(HttpServletRequest request, @ModelAttribute Employee_Profile employeeProfile) {

		// Retrieve current user ID from session
		String userId = (String) request.getSession().getAttribute("USERID");

		// Get existing employee data
		Employee_Profile existingProfile = employee_Profile_Rep.getEmployeeData(employeeProfile.getEmployee_id());

		// Preserve original entry and verification details
		employeeProfile.setEntry_time(existingProfile.getEntry_time());
		employeeProfile.setEntry_user(existingProfile.getEntry_user());
		employeeProfile.setVerify_time(existingProfile.getVerify_time());
		employeeProfile.setVerify_user(existingProfile.getVerify_user());

		// Set modification details
		employeeProfile.setModify_time(new Date());
		employeeProfile.setModify_user(userId);
		employeeProfile.setEntity_flg("Y");
		employeeProfile.setModify_flg("N");
		employeeProfile.setVerify_flg("Y");
		employeeProfile.setDel_flg("N");

		// Handle employee photo: retain existing if not provided in request
		if (employeeProfile.getEmployee_Photo() == null) {
			employeeProfile.setEmployee_Photo(existingProfile.getEmployee_Photo());
		}

		// Save updated profile to the repository
		employee_Profile_Rep.save(employeeProfile);

		return "Successfully Modified";
	}

	/* THANVEER */
	@RequestMapping(value = "employeedelete", method = RequestMethod.POST)
	@ResponseBody
	public String employeedelete(@RequestParam(required = false) String employeeId, HttpServletRequest rq,
			@ModelAttribute Employee_Profile employee_Profile) {

		Employee_Profile data = employee_Profile_Rep.getEmployeeData(employeeId);
		employee_Profile_Rep.delete(data);
		String userid = (String) rq.getSession().getAttribute("USERID");
		// FOR AUIDT
		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		Long auditID = bGLSBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());
		audit.setFunc_code("Employee Id");
		audit.setRemarks("Sucessfully Deleted");
		audit.setAudit_table("BGLS_EMPLOYEE_PROFILE");
		audit.setAudit_screen("EMPLOYEE PROFILE - DELETE");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bGLSBusinessTable_Rep.save(audit);
		return "Sucessfully Deleted";

	}

	/* PRAVEEN */
	@GetMapping("/getTransactionBalance")
	@ResponseBody
	public BigDecimal getTransactionBalance(@RequestParam(required = false) String acctnum,
			@RequestParam(required = false) String fromdate, Model md) {

		System.out.println("Acct number: " + acctnum + " From date: " + fromdate);

		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		Date fromDateParsed = null;
		String formattedDate = null;

		try {

			if (fromdate != null) {
				fromDateParsed = inputDateFormat.parse(fromdate);/* 08-12-2024 */
			}

			if (fromDateParsed != null) {

				formattedDate = outputDateFormat.format(fromDateParsed); /* 08-DEC-2024 */
			}
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Error parsing 'fromdate': " + e.getMessage());
		}

		// Now pass the formatted date to the repository query
		BigDecimal tranDateBal = dab_Repo.getTranDateBAlance(acctnum, formattedDate);
		System.out.println("THE VALUE OF tran_date_bal: " + tranDateBal);

		return tranDateBal;
	}

	/* PRAVEEN */
	@GetMapping("/getTransactionRecords")
	public List<TRAN_MAIN_TRM_WRK_ENTITY> getTransactionRecords(@RequestParam(required = false) String acctnum,
			@RequestParam(required = false) String fromdate, @RequestParam(required = false) String todate) {

		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");/* 08-12-2024 */
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");/* 08-DEC-2024 */

		Date fromDateParsed = null;
		Date toDateParsed = null;

		try {
			if (fromdate != null) {
				fromDateParsed = inputDateFormat.parse(fromdate);
			}
			if (todate != null) {
				toDateParsed = inputDateFormat.parse(todate);
			}
		} catch (ParseException e) {
			e.printStackTrace();

		}

		String formattedFromDate = (fromDateParsed != null) ? outputDateFormat.format(fromDateParsed) : null;
		String formattedToDate = (toDateParsed != null) ? outputDateFormat.format(toDateParsed) : null;

		System.out.println("Formatted Dates: From = " + formattedFromDate + ", To = " + formattedToDate);

		List<TRAN_MAIN_TRM_WRK_ENTITY> records = tRAN_MAIN_TRM_WRK_REP.getTranList(acctnum, formattedFromDate,
				formattedToDate);
		for (TRAN_MAIN_TRM_WRK_ENTITY i : records) {
			System.out.println(i + "records");
		}
		return records;
	}

	/* Thanveer */
	@RequestMapping(value = "AddScreens", method = RequestMethod.POST)

	@ResponseBody
	public String AddScreens(Model md, HttpServletRequest rq, @ModelAttribute Chart_Acc_Entity chart_Acc_Entity) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		List<String> existingdata = chart_Acc_Rep.getexistingData();
		if (existingdata.contains(chart_Acc_Entity.getAcct_num())) {
			return "Employee Id Already Exist";
		} else {
			Chart_Acc_Entity up = chart_Acc_Entity;
			up.setEntity_flg("N");
			up.setDel_flg("N");
			chart_Acc_Rep.save(up);
			// FOR AUIDT
			Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
			Optional<UserProfile> up1 = userProfileRep.findById(userid);
			UserProfile user = up1.get();

			BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
			LocalDateTime currentDateTime = LocalDateTime.now();
			Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
			audit.setAudit_date(new Date());
			audit.setEntry_time(dateValue);
			audit.setEntry_user(user.getUserid());

			audit.setRemarks("Saved Successfully");
			audit.setAudit_table("BGLS_CHART_OF_ACCOUNTS");
			audit.setAudit_screen("CHART OF ACCOUNTS - ADD");
			audit.setEvent_id(user.getUserid());
			audit.setEvent_name(user.getUsername());
			// audit.setModi_details("Login Successfully");
			UserProfile auth_user = userProfileRep.getRole(user.getUserid());
			String auth_user_val = auth_user.getAuth_user();
			Date auth_user_date = auth_user.getAuth_time();
			audit.setAuth_user(auth_user_val);
			audit.setAuth_time(auth_user_date);
			audit.setAudit_ref_no(auditID.toString());
			audit.setField_name("-");

			bglsBusinessTable_Rep.save(audit);

			return "Saved Successfully";
		}
	}

	/* Thanveer */
	@RequestMapping(value = "ModifyScreens", method = RequestMethod.POST)

	@ResponseBody
	public String ModifyScreens(Model md, HttpServletRequest rq, @ModelAttribute Chart_Acc_Entity chart_Acc_Entity,
			@RequestParam(required = false) String acct_num) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		System.out.println("THE GETT");
		String msg = "";

		Chart_Acc_Entity up = chart_Acc_Rep.getaedit(acct_num);

		if (Objects.nonNull(up)) {
			up = chart_Acc_Entity;
			up.setDel_flg("N");
			up.setEntity_flg("N");
			chart_Acc_Rep.save(up);
			msg = "Modify Successfully";
		} else {
			msg = "Data Not Found";
		}
		// FOR AUIDT
		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();

		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());

		audit.setRemarks("Modify Successfully");
		audit.setAudit_table("BGLS_CHART_OF_ACCOUNTS");
		audit.setAudit_screen("CHART OF ACCOUNTS - MODIFY");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bglsBusinessTable_Rep.save(audit);

		return msg;
	}

	/* Thanveer */
	@RequestMapping(value = "VerifyScreens", method = RequestMethod.POST)

	@ResponseBody
	public String VerifyScreens(Model md, HttpServletRequest rq, @ModelAttribute Chart_Acc_Entity chart_Acc_Entity) {
		Chart_Acc_Entity up = chart_Acc_Entity;
		up.setEntity_flg("Y");
		up.setDel_flg("N");
		chart_Acc_Rep.save(up);
		String userid = (String) rq.getSession().getAttribute("USERID");
		// FOR AUIDT
		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();

		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());

		audit.setRemarks("Verified Successfully");
		audit.setAudit_table("BGLS_CHART_OF_ACCOUNTS");
		audit.setAudit_screen("CHART OF ACCOUNTS - VERIFY");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bglsBusinessTable_Rep.save(audit);
		return "Verified Successfully";

	}

	/* Thanveer */
	@RequestMapping(value = "DeleteScreens", method = RequestMethod.POST)

	@ResponseBody
	public String DeleteScreens(Model md, HttpServletRequest rq, @ModelAttribute Chart_Acc_Entity chart_Acc_Entity,
			@RequestParam(required = false) String acct_num) {
		String msg = "";
		Chart_Acc_Entity up = chart_Acc_Rep.getaedit(acct_num);
		System.out.println("the getting account no is " + acct_num);
		if (Objects.nonNull(up)) {
			up = chart_Acc_Entity;
			up.setDel_flg("Y");
			chart_Acc_Rep.save(up);
			msg = "Deleted Successfully";
		} else {
			msg = "Data Not Found";
		}
		String userid = (String) rq.getSession().getAttribute("USERID");
		// FOR AUIDT
		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();

		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());

		audit.setRemarks("Deleted Successfully");
		audit.setAudit_table("BGLS_CHART_OF_ACCOUNTS");
		audit.setAudit_screen("CHART OF ACCOUNTS - DELETE");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bglsBusinessTable_Rep.save(audit);
		return msg;

	}

	/* Thanveer */
	@RequestMapping(value = "OrgBranchAdd", method = RequestMethod.POST)
	@ResponseBody
	public String OrgBranchAdd(Model md, HttpServletRequest rq,
	        @ModelAttribute Organization_Branch_Entity organization_Branch_Entity) {
	    
	    String userid = (String) rq.getSession().getAttribute("USERID");
	    List<String> existingdata = organization_Branch_Rep.getexistingData();

	    if (existingdata.contains(organization_Branch_Entity.getBranch_code())) {
	        return "Branch Name Already Exist";
	    } else {
	        try {
	            Organization_Branch_Entity up = organization_Branch_Entity;

	            up.setEntity_flg("N");
	            up.setModify_flg("N");
	            up.setDel_flg("N");
	            up.setEntry_user(userid);
	            up.setEntry_time(new Date());
	            organization_Branch_Rep.save(up);

	            // --- Audit Logging ---
	            BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
	            audit.setFunc_code("BRANCH");
	            Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
	            Optional<UserProfile> up1 = userProfileRep.findById(userid);
	            UserProfile user = up1.get();

	            LocalDateTime currentDateTime = LocalDateTime.now();
	            Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
	            audit.setAudit_date(new Date());
	            audit.setEntry_time(dateValue);
	            audit.setEntry_user(user.getUserid());

	            audit.setRemarks("Branch Added Successfully");
	            audit.setAudit_table("BGLS_ORG_BRANCH");
	            audit.setAudit_screen("Organization Details");
	            audit.setEvent_id(user.getUserid());
	            audit.setEvent_name(user.getUsername());

	            UserProfile auth_user = userProfileRep.getRole(user.getUserid());
	            audit.setAuth_user(auth_user.getAuth_user());
	            audit.setAuth_time(auth_user.getAuth_time());
	            audit.setAudit_ref_no(auditID.toString());
	            audit.setField_name("-");

	            bglsBusinessTable_Rep.save(audit);

	            return "Added successfully.";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error while adding branch.";
	        }
	    }
	}

	/* tab2Del */

	@RequestMapping(value = "tab2Del", method = RequestMethod.POST)
	@ResponseBody
	public String tab2Del(Model md, HttpServletRequest rq,
			@ModelAttribute Organization_Branch_Entity organization_Branch_Entity) {
		String userid = (String) rq.getSession().getAttribute("USERID");

		Organization_Branch_Entity up = organization_Branch_Entity;
		up.setEntity_flg("N");
		up.setModify_flg("N");
		up.setDel_flg("Y");
		up.setEntry_user(userid);
		up.setEntry_time(new Date());
		organization_Branch_Rep.save(up);

		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		audit.setFunc_code("BRANCH-DELETE");
		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();

		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());

		audit.setRemarks("Branch Added Successfully");
		audit.setAudit_table("BGLS_ORG_BRANCH");
		audit.setAudit_screen("Organization Details");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bglsBusinessTable_Rep.save(audit);

		return "Deleted Successfully";

	}

	/* Thanveer */
	@RequestMapping(value = "tab1modify", method = RequestMethod.POST)
	@ResponseBody
	public String tab1modify(Model md, HttpServletRequest rq, @ModelAttribute Organization_Entity organization_Entity) {

		Optional<Organization_Entity> up = organization_Repo.findById(organization_Entity.getOrg_name());
		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		String userid = (String) rq.getSession().getAttribute("USERID");
		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();

		// ✅ Handle null safely
		Date asOn = organization_Entity.getAs_on();
		if (asOn != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String formattedDate = dateFormat.format(asOn);
			try {
				Date parsedDate = dateFormat.parse(formattedDate);
				System.out.println("Parsed As_on Date: " + parsedDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("As_on is null for Org: " + organization_Entity.getOrg_name());
		}

		String msg = "";
		if (up.isPresent()) {
			Organization_Entity us1 = up.get();

			// ✅ Null-safe equals check
			boolean noChange = Objects.equals(us1.getOrg_type(), organization_Entity.getOrg_type())
					&& Objects.equals(us1.getDate_of_regn(), organization_Entity.getDate_of_regn())
					&& Objects.equals(us1.getReg_no(), organization_Entity.getReg_no())
					&& Objects.equals(us1.getPan_card(), organization_Entity.getPan_card())
					&& Objects.equals(us1.getTan_card(), organization_Entity.getTan_card())
					&& Objects.equals(us1.getNo_of_emp(), organization_Entity.getNo_of_emp())
					&& Objects.equals(us1.getAs_on(), organization_Entity.getAs_on())
					&& Objects.equals(us1.getReg_addr_1(), organization_Entity.getReg_addr_1())
					&& Objects.equals(us1.getReg_addr_2(), organization_Entity.getReg_addr_2())
					&& Objects.equals(us1.getCorp_addr_1(), organization_Entity.getCorp_addr_1())
					&& Objects.equals(us1.getCor_addr_2(), organization_Entity.getCor_addr_2())
					&& Objects.equals(us1.getWeb_site(), organization_Entity.getWeb_site())
					&& Objects.equals(us1.getEmail(), organization_Entity.getEmail());

			if (noChange) {
				msg = "No modification done";
			} else {
				organization_Entity.setModify_flg("Y");
				organization_Entity.setDel_flg("N");
				organization_Entity.setModify_time(new Date());
				organization_Entity.setModify_user(userid);

				// build modification details
				StringBuilder sb = new StringBuilder();
				if (!Objects.equals(us1.getOrg_type(), organization_Entity.getOrg_type())) {
					sb.append("Organization Type+").append(us1.getOrg_type()).append("+")
							.append(organization_Entity.getOrg_type()).append("||");
				}
				if (!Objects.equals(us1.getDate_of_regn(), organization_Entity.getDate_of_regn())) {
					sb.append("Date of Registration+").append(us1.getDate_of_regn()).append("+")
							.append(organization_Entity.getDate_of_regn()).append("||");
				}
				if (!Objects.equals(us1.getReg_no(), organization_Entity.getReg_no())) {
					sb.append("Certificate and Registration+").append(us1.getReg_no()).append("+")
							.append(organization_Entity.getReg_no()).append("||");
				}
				if (!Objects.equals(us1.getPan_card(), organization_Entity.getPan_card())) {
					sb.append("Business Registration Card+").append(us1.getPan_card()).append("+")
							.append(organization_Entity.getPan_card()).append("||");
				}
				if (!Objects.equals(us1.getTan_card(), organization_Entity.getTan_card())) {
					sb.append("VAT Reference+").append(us1.getTan_card()).append("+")
							.append(organization_Entity.getTan_card()).append("||");
				}
				if (!Objects.equals(us1.getNo_of_emp(), organization_Entity.getNo_of_emp())) {
					sb.append("No of Employees+").append(us1.getNo_of_emp()).append("+")
							.append(organization_Entity.getNo_of_emp()).append("||");
				}
				if (!Objects.equals(us1.getAs_on(), organization_Entity.getAs_on())) {
					sb.append("As On+").append(us1.getAs_on()).append("+").append(organization_Entity.getAs_on())
							.append("||");
				}
				if (!Objects.equals(us1.getReg_addr_1(), organization_Entity.getReg_addr_1())) {
					sb.append("Registered Office Address 1+").append(us1.getReg_addr_1()).append("+")
							.append(organization_Entity.getReg_addr_1()).append("||");
				}
				if (!Objects.equals(us1.getCorp_addr_1(), organization_Entity.getCorp_addr_1())) {
					sb.append("Corporate Office Address 1+").append(us1.getCorp_addr_1()).append("+")
							.append(organization_Entity.getCorp_addr_1()).append("||");
				}
				if (!Objects.equals(us1.getCor_addr_2(), organization_Entity.getCor_addr_2())) {
					sb.append("Corporate Office Address 2+").append(us1.getCor_addr_2()).append("+")
							.append(organization_Entity.getCor_addr_2()).append("||");
				}
				if (!Objects.equals(us1.getWeb_site(), organization_Entity.getWeb_site())) {
					sb.append("Website+").append(us1.getWeb_site()).append("+")
							.append(organization_Entity.getWeb_site()).append("||");
				}
				if (!Objects.equals(us1.getEmail(), organization_Entity.getEmail())) {
					sb.append("Email+").append(us1.getEmail()).append("+").append(organization_Entity.getEmail())
							.append("||");
				}

				// audit
				audit.setAudit_date(new Date());
				audit.setEntry_time(new Date());
				audit.setEntry_user(userid);
				audit.setFunc_code("HEAD OFFICE MODIFICATION");
				audit.setRemarks(userid + " : User Modified Successfully");
				audit.setAudit_table("BGLS_ORG_MASTER");
				audit.setAudit_screen("HEAD OFFICE - MODIFY");

				Optional<UserProfile> up1 = userProfileRep.findById(userid);
				up1.ifPresent(user -> {
					audit.setEvent_name(user.getUsername());
					audit.setEvent_id(user.getUserid());
				});

				audit.setModi_details(sb.toString());
				audit.setAudit_ref_no(auditID.toString());

				UserProfile auth_user = userProfileRep.getRole(userid);
				if (auth_user != null) {
					audit.setAuth_user(auth_user.getAuth_user());
					audit.setAuth_time(auth_user.getAuth_time());
				}

				bglsBusinessTable_Rep.save(audit);
				organization_Repo.save(organization_Entity);

				msg = "User Modified Successfully";
			}
		}
		return msg;
	}

	/* Thanveer */
	@RequestMapping(value = "tab2modify", method = RequestMethod.POST)
	@ResponseBody
	public String tab2modify(Model md, HttpServletRequest rq,
			@ModelAttribute Organization_Branch_Entity organization_Branch_Entity) {

		/*
		 * Organization_Branch_Entity up = organization_Branch_Entity;
		 * up.setEntity_flg("N"); up.setDel_flg("N");
		 * 
		 * organization_Branch_Rep.save(up);
		 */

		Optional<Organization_Branch_Entity> up = organization_Branch_Rep
				.findById(organization_Branch_Entity.getBranch_code());
		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
		String userid = (String) rq.getSession().getAttribute("USERID");
		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();

		// organization_Branch_Entity up = organization_Branch_Entity;
		String msg = "";
		if (up.isPresent()) {
			Organization_Branch_Entity us1 = up.get();
			if ((us1.getBranch_name().equals(organization_Branch_Entity.getBranch_name())
					&& us1.getDesignation().equals(organization_Branch_Entity.getDesignation())
					&& us1.getSwift_code().equals(organization_Branch_Entity.getSwift_code())
					//&& us1.getPic_no().equals(organization_Branch_Entity.getPic_no())
					&& us1.getLand_line().equals(organization_Branch_Entity.getLand_line())
					&& us1.getFax().equals(organization_Branch_Entity.getFax()) && us1.getMobile() != null
					&& us1.getMobile().equals(organization_Branch_Entity.getMobile())
					&& us1.getCont_person().equals(organization_Branch_Entity.getCont_person())
					&& us1.getBranch_head().equals(organization_Branch_Entity.getBranch_head())
					&& us1.getWebsite().equals(organization_Branch_Entity.getWebsite())
					&& us1.getMail_id().equals(organization_Branch_Entity.getMail_id())
					&& us1.getAdd_1().equals(organization_Branch_Entity.getAdd_1())
					&& us1.getAdd_2().equals(organization_Branch_Entity.getAdd_2())
					&& us1.getCity().equals(organization_Branch_Entity.getCity())
					&& us1.getState().equals(organization_Branch_Entity.getState())
					&& us1.getCountry().equals(organization_Branch_Entity.getCountry())
					&& us1.getRemarks().equals(organization_Branch_Entity.getRemarks())
					&& us1.getZip_code().equals(organization_Branch_Entity.getZip_code()))) {
				msg = "No any Modification done";
			} else {
				System.out.println(organization_Branch_Entity.getMobile() + "UYIU");

				organization_Branch_Entity.setModify_flg("Y");
				organization_Branch_Entity.setDel_flg("N");
				organization_Branch_Entity.setModify_time(new Date());
				organization_Branch_Entity.setModify_user(userid);

				// for audit
				StringBuilder stringBuilder = new StringBuilder();

				if ((us1.getBranch_name().equals(organization_Branch_Entity.getBranch_name())
						&& us1.getBranch_head().equals(organization_Branch_Entity.getBranch_head())
						&& us1.getDesignation().equals(organization_Branch_Entity.getDesignation())
						&& us1.getSwift_code().equals(organization_Branch_Entity.getSwift_code())
						//&& us1.getPic_no().equals(organization_Branch_Entity.getPic_no())
						&& us1.getLand_line().equals(organization_Branch_Entity.getLand_line())
						&& us1.getFax().equals(organization_Branch_Entity.getFax()) && us1.getMobile() != null
						&& us1.getMobile().equals(organization_Branch_Entity.getMobile())
						&& us1.getCont_person().equals(organization_Branch_Entity.getCont_person())
						&& us1.getWebsite().equals(organization_Branch_Entity.getWebsite())
						&& us1.getMail_id().equals(organization_Branch_Entity.getMail_id())
						&& us1.getAdd_1().equals(organization_Branch_Entity.getAdd_1())
						&& us1.getAdd_2().equals(organization_Branch_Entity.getAdd_2())
						&& us1.getCity().equals(organization_Branch_Entity.getCity())
						&& us1.getState().equals(organization_Branch_Entity.getState())
						&& us1.getCountry().equals(organization_Branch_Entity.getCountry())
						&& us1.getRemarks().equals(organization_Branch_Entity.getRemarks())
						&& us1.getZip_code().equals(organization_Branch_Entity.getZip_code()))) {

				}
				if (!us1.getBranch_name().equals(organization_Branch_Entity.getBranch_name())) {
					stringBuilder = stringBuilder.append("Branch Name+" + us1.getBranch_name() + "+"
							+ organization_Branch_Entity.getBranch_name() + "||");
				}
				if (!us1.getBranch_head().equals(organization_Branch_Entity.getBranch_head())) {
					stringBuilder = stringBuilder.append("Branch Head+" + us1.getBranch_head() + "+"
							+ organization_Branch_Entity.getBranch_head() + "||");
				}
				if (!us1.getDesignation().equals(organization_Branch_Entity.getDesignation())) {
					stringBuilder = stringBuilder.append("Designation+" + us1.getDesignation() + "+"
							+ organization_Branch_Entity.getDesignation() + "||");
				}
				if (!us1.getSwift_code().equals(organization_Branch_Entity.getSwift_code())) {
					stringBuilder = stringBuilder.append("Swift Code+" + us1.getSwift_code() + "+"
							+ organization_Branch_Entity.getSwift_code() + "||");
				}
				/*
				 * if (!us1.getPic_no().equals(organization_Branch_Entity.getPic_no())) {
				 * stringBuilder = stringBuilder .append("Pic No+" + us1.getPic_no() + "+" +
				 * organization_Branch_Entity.getPic_no() + "||"); }
				 */
				if (!us1.getLand_line().equals(organization_Branch_Entity.getLand_line())) {
					stringBuilder = stringBuilder.append(
							"Land Line+" + us1.getLand_line() + "+" + organization_Branch_Entity.getLand_line() + "||");
				}
				if (!us1.getFax().equals(organization_Branch_Entity.getFax())) {
					stringBuilder = stringBuilder
							.append("Fax+" + us1.getFax() + "+" + organization_Branch_Entity.getFax() + "||");
				}
				if (!(us1.getMobile() != null && us1.getMobile().equals(organization_Branch_Entity.getMobile()))) {
					stringBuilder = stringBuilder
							.append("Mobile+" + us1.getMobile() + "+" + organization_Branch_Entity.getMobile() + "||");
				}if (!(us1.getRemarks() != null && us1.getRemarks().equals(organization_Branch_Entity.getRemarks()))) {
					stringBuilder = stringBuilder
							.append("Mobile+" + us1.getRemarks() + "+" + organization_Branch_Entity.getRemarks() + "||");
				}
				if (!us1.getCont_person().equals(organization_Branch_Entity.getCont_person())) {
					stringBuilder = stringBuilder.append("Contact Person+" + us1.getCont_person() + "+"
							+ organization_Branch_Entity.getCont_person() + "||");
				}
				if (!us1.getWebsite().equals(organization_Branch_Entity.getWebsite())) {
					stringBuilder = stringBuilder.append(
							"Web Site+" + us1.getWebsite() + "+" + organization_Branch_Entity.getWebsite() + "||");
				}
				if (!us1.getMail_id().equals(organization_Branch_Entity.getMail_id())) {
					stringBuilder = stringBuilder.append(
							"Mail Id+" + us1.getMail_id() + "+" + organization_Branch_Entity.getMail_id() + "||");
				}
				if (!us1.getAdd_1().equals(organization_Branch_Entity.getAdd_1())) {
					stringBuilder = stringBuilder
							.append("Address 1+" + us1.getAdd_1() + "+" + organization_Branch_Entity.getAdd_1() + "||");
				}

				if (!us1.getAdd_2().equalsIgnoreCase(organization_Branch_Entity.getAdd_2())) {
					stringBuilder = stringBuilder
							.append("Address 2+" + us1.getAdd_2() + "+" + organization_Branch_Entity.getAdd_2() + "||");

				}
				if (!us1.getCity().equalsIgnoreCase(organization_Branch_Entity.getCity())) {
					stringBuilder = stringBuilder
							.append("City+" + us1.getCity() + "+" + organization_Branch_Entity.getCity() + "||");

				}
				if (!us1.getCountry().equalsIgnoreCase(organization_Branch_Entity.getCountry())) {
					stringBuilder = stringBuilder
							.append("State+" + us1.getCountry() + "+" + organization_Branch_Entity.getCountry() + "||");
				}
				if (!us1.getState().equalsIgnoreCase(organization_Branch_Entity.getState())) {
					stringBuilder = stringBuilder
							.append("Country+" + us1.getState() + "+" + organization_Branch_Entity.getState() + "||");
				}

				if (!us1.getZip_code().equalsIgnoreCase(organization_Branch_Entity.getZip_code())) {
					stringBuilder = stringBuilder.append(
							"Zip Code+" + us1.getZip_code() + "+" + organization_Branch_Entity.getZip_code() + "||");
				}

				audit.setAudit_date(new Date());
				audit.setEntry_time(new Date());
				audit.setEntry_user(userid);
				audit.setFunc_code("BRANCH MODIFICATION");
				audit.setRemarks(userid + " : Modified Successfully");
				audit.setAudit_table("BGLS_ORG_BRANCH");
				audit.setAudit_screen("BRANCH - MODIFY");
				Optional<UserProfile> up1 = userProfileRep.findById(userid);
				UserProfile user = up1.get();
				audit.setEvent_name(user.getUsername());
				audit.setEvent_id(user.getUserid());
				// audit.setEvent_name(up.getUsername());
				String modiDetails = stringBuilder.toString();

				audit.setModi_details(modiDetails);
				audit.setAudit_ref_no(auditID.toString());
				UserProfile auth_user = userProfileRep.getRole(userid);
				String auth_user_val = auth_user.getAuth_user();
				Date auth_user_date = auth_user.getAuth_time();
				audit.setAuth_user(auth_user_val);
				audit.setAuth_time(auth_user_date);
				bglsBusinessTable_Rep.save(audit);
				organization_Branch_Rep.save(organization_Branch_Entity);
				msg = "User Modified Successfully";

			}

		}
		return "Modified Successfully";

	}

	/* pon prasanth */
	@GetMapping("transactionaccountdetails")
	public Chart_Acc_Entity transactionaccountdetails(@RequestParam(required = false) String acct_num) {
		Chart_Acc_Entity accountvalue = chart_Acc_Rep.getlistpopupvalues(acct_num);
		System.out.println("THE ACCOUNT NUMBER IS" + acct_num);
		return accountvalue;
	}

	/* Praveen */
	@RequestMapping(value = "accessRoleSubmit", method = RequestMethod.POST)
	public String accessRoleSubmit(@RequestBody Access_Role_Entity Access_Role_Entity, HttpServletRequest rq)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		String userid = (String) rq.getSession().getAttribute("USERID");

		String msg = loginServices.addAccessAndRole(Access_Role_Entity, userid);

		return msg;
	}

	/* Thanveer */
	@RequestMapping(value = "getTypeDescription", method = RequestMethod.GET)
	@ResponseBody
	public String getTypeDescription(Model md, HttpServletRequest rq, @RequestParam(required = false) String refType,
			@ModelAttribute Reference_Code_Entity reference_Code_Entity) {

		List<Object[]> typeDescList = reference_code_Rep.getTypeDesc(refType);
		System.out.println(typeDescList);

		String typeDesc = "";
		if (typeDescList != null && !typeDescList.isEmpty()) {
			Object[] firstRow = typeDescList.get(0);
			if (firstRow.length >= 2) {
				// Assuming the fields you need are at index 0 and 1
				typeDesc = firstRow[0] + " - " + firstRow[1];
			}
		}

		md.addAttribute("typeDesc", typeDesc);
		return typeDesc;

	}

	/* Thanveer */
	@RequestMapping(value = "refAdd", method = RequestMethod.POST)
	@ResponseBody
	public String refAdd(HttpServletRequest rq, @ModelAttribute Reference_Code_Entity reference_Code_Entity) {
		String userid = (String) rq.getSession().getAttribute("USERID");
		reference_Code_Entity.setEntity_flg("N");
		reference_Code_Entity.setModify_flg("N");
		reference_Code_Entity.setDel_flg("N");
		reference_Code_Entity.setEntry_user(userid);
		reference_Code_Entity.setEntry_time(new Date());
		reference_code_Rep.save(reference_Code_Entity);

		BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();

		Long auditID = bglsBusinessTable_Rep.getAuditRefUUID();
		Optional<UserProfile> up1 = userProfileRep.findById(userid);
		UserProfile user = up1.get();

		LocalDateTime currentDateTime = LocalDateTime.now();
		Date dateValue = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		audit.setAudit_date(new Date());
		audit.setEntry_time(dateValue);
		audit.setEntry_user(user.getUserid());
		audit.setFunc_code("REFERENCE CODE");
		audit.setRemarks("Sucessfully Saved");
		audit.setAudit_table("BGLS_REF_MASTER");
		audit.setAudit_screen("REFERENCE CODE MAINTENANCE - ADD");
		audit.setEvent_id(user.getUserid());
		audit.setEvent_name(user.getUsername());
		// audit.setModi_details("Login Successfully");
		UserProfile auth_user = userProfileRep.getRole(user.getUserid());
		String auth_user_val = auth_user.getAuth_user();
		Date auth_user_date = auth_user.getAuth_time();
		audit.setAuth_user(auth_user_val);
		audit.setAuth_time(auth_user_date);
		audit.setAudit_ref_no(auditID.toString());
		audit.setField_name("-");

		bglsBusinessTable_Rep.save(audit);
		return "Sucessfully Saved";
	}

	/* Thanveer */

	@RequestMapping(value = "GeneralLedgerAdd", method = RequestMethod.POST)
	@ResponseBody
	public String GeneralLedgerAdd(@RequestParam("formmode") String formmode,
			@RequestParam(required = false) String glcode, @ModelAttribute GeneralLedgerEntity generalLedgerEntity,
			@RequestParam(required = false) String glsh_code, Model md, HttpServletRequest rq) {
		String userid = (String) rq.getSession().getAttribute("USERID");

		String value1 = generalLedgerEntity.getGlCode();

		System.out.println("the getting gl code is " + glcode);
		System.out.println("the getting glsh code 234324is " + glsh_code);

		String msg = adminOperServices.addGeneralLedger(generalLedgerEntity, formmode, glsh_code, glcode, userid);
		return msg;
	}

	/* pon prasanth */
	@RequestMapping(value = "addtransactiondata", method = RequestMethod.POST)
	@ResponseBody
	public String addTransactionDatas(Model md, HttpServletRequest rq,
			@RequestBody List<TRAN_MAIN_TRM_WRK_ENTITY> transactionDetails) {
		List<TRAN_MAIN_TRM_WRK_ENTITY> transactionsToSave = new ArrayList<>();
		String tranid = "";
		Transaction_Partition_Detail_Entity partitionRecord = new Transaction_Partition_Detail_Entity();
		Transaction_Pointing_Table_Entity pointingRecord = new Transaction_Pointing_Table_Entity();
		Transaction_Reversed_Table_Entity reversedRecord = new Transaction_Reversed_Table_Entity();

		for (TRAN_MAIN_TRM_WRK_ENTITY transaction : transactionDetails) {

			tranid = transaction.getTran_id();
			String nextSerialNumber = tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID(); // Fetch the next SRLNO
			transaction.setSrl_no(nextSerialNumber); // Set the serial number manually

			String accountNumber = transaction.getAcct_num();
			System.out.println("Account Number : " + accountNumber);
			String partitionFlag = chart_Acc_Rep.getpartitionFlag(accountNumber);
			String pointingDetail = chart_Acc_Rep.getpointingDetail(accountNumber);

			/* partition table update */
			if (partitionFlag != null) {
				if (partitionFlag.equalsIgnoreCase("Y")) {

					partitionRecord.setAcct_name(transaction.getAcct_name());
					partitionRecord.setAcct_num(transaction.getAcct_num());
					partitionRecord.setTran_id(transaction.getTran_id());
					partitionRecord.setPart_tran_id(transaction.getPart_tran_id());
					partitionRecord.setTran_type(transaction.getTran_type());
					partitionRecord.setPart_tran_type(transaction.getPart_tran_type());
					partitionRecord.setAcct_crncy(transaction.getAcct_crncy());
					partitionRecord.setTran_amt(transaction.getTran_amt());
					partitionRecord.setTran_particular(transaction.getTran_particular());
					partitionRecord.setTran_remarks(transaction.getTran_remarks());
					partitionRecord.setTran_date(transaction.getTran_date());
					partitionRecord.setValue_date(transaction.getValue_date());

					partitionRecord.setTran_ref_no(transaction.getTran_ref_no());
					partitionRecord.setAdd_details(transaction.getAdd_details());
					partitionRecord.setPartition_det(transaction.getPartition_det());
					partitionRecord.setPartition_type(transaction.getPartition_type());

					transaction_Partition_Detail_Repo.save(partitionRecord);
				} else {
					System.out.println("partition flag N");
				}

			} else {
				System.out.println("Not a partition account");
			}

			/* pointing table update */

			if (pointingDetail != null) {

				if (pointingDetail.equalsIgnoreCase("C") || pointingDetail.equalsIgnoreCase("D")) {

					String pointingType = pointingDetail.equalsIgnoreCase("C") ? "Credit" : "Debit";

					if (pointingType.equalsIgnoreCase(transaction.getPart_tran_type())) {

						/*
						 * pointingType = credit and part_tran_type=credit or pointingType = debit and
						 * part_tran_type=debit
						 */

						pointingRecord.setOrg_tran_id(transaction.getTran_id());
						pointingRecord.setOrg_part_tran_id(transaction.getPart_tran_id());
						pointingRecord.setOrg_acct_num(transaction.getAcct_num());
						pointingRecord.setOrg_acct_name(transaction.getAcct_name());
						pointingRecord.setOrg_tran_type(transaction.getTran_type());
						pointingRecord.setOrg_part_tran_type(transaction.getPart_tran_type());
						pointingRecord.setOrg_acct_crncy(transaction.getAcct_crncy());
						pointingRecord.setOrg_tran_amt(transaction.getTran_amt());
						pointingRecord.setOrg_tran_particular(transaction.getTran_particular());
						pointingRecord.setOrg_tran_remarks(transaction.getTran_remarks());
						pointingRecord.setOrg_tran_date(transaction.getTran_date());
						pointingRecord.setOrg_value_date(transaction.getValue_date());

						pointingRecord.setOrg_tran_ref_no(transaction.getTran_ref_no());
						pointingRecord.setOrg_add_details(transaction.getAdd_details());
						pointingRecord.setOffset_tran_amt(BigDecimal.ZERO);
						pointingRecord.setBal_outstd_amt(transaction.getTran_amt());

						transaction_Pointing_Table_Repo.save(pointingRecord);

					} else {

						/*
						 * pointingType = credit and part_tran_type=debit or pointingType = debit and
						 * part_tran_type=credit
						 */

					}
				} else {
					System.out.println("POINTING DETAIL NOT MENTIONED ");
				}

			} else {
				System.out.println("Not a pointing account");
			}

			/* reversed table update */
			if (reversedRecord != null) {
				System.out.println("Reversed function Start");

				// Determine the pointing type as "Credit" or "Debit"
				String pointingType = reversedRecord.equalsIgnoreCase("C") ? "Credit" : "Debit";

				// Only proceed if pointingType and part_tran_type do not match
				if (!pointingType.equalsIgnoreCase(transaction.getPart_tran_type())) {
					reversedRecord.setRev_acct_name(transaction.getAcct_name());
					reversedRecord.setRev_acct_num(transaction.getAcct_num());
					reversedRecord.setRev_tran_id(transaction.getTran_id());
					reversedRecord.setRev_part_tran_id(transaction.getPart_tran_id());
					reversedRecord.setRev_tran_type(transaction.getTran_type());
					reversedRecord.setRev_part_tran_type(transaction.getPart_tran_type());
					reversedRecord.setRev_acct_crncy(transaction.getAcct_crncy());
					reversedRecord.setRev_tran_amt(transaction.getTran_amt());
					reversedRecord.setRev_tran_particular(transaction.getTran_particular());
					reversedRecord.setRev_tran_remarks(transaction.getTran_remarks());
					reversedRecord.setRev_tran_date(transaction.getTran_date());
					reversedRecord.setRev_value_date(transaction.getValue_date());
					reversedRecord.setRev_tran_ref_no(transaction.getTran_ref_no());
					reversedRecord.setRev_add_details(transaction.getAdd_details());

					// Save the reversed record
					transaction_Reversed_Table_Repo.save(reversedRecord);
					System.out.println("Reversed record saved successfully.");
				} else {
					System.out.println("The function will not work. Values match.");
				}
			} else {
				System.out.println("Not a Reversed account");
			}

			transactionsToSave.add(transaction);

		}

		tRAN_MAIN_TRM_WRK_REP.saveAll(transactionsToSave);
		return "TRAN ID " + tranid + " Saved Successfully";
	}

	@RequestMapping(value = "addtransactiondatamodiy", method = RequestMethod.POST)
	@ResponseBody
	public String addtransactiondatamodiy(Model md, HttpServletRequest rq,
			@RequestBody List<TRAN_MAIN_TRM_WRK_ENTITY> transactionDetails,
			@RequestParam(required = false) List<String> tran_id, // Accept multiple tran_id values
			@RequestParam(required = false) List<String> part_tran_id) { // Accept multiple part_tran_id values

		System.out.println("addtransactiondatamodiy" + " first step to modify");

		// Check if both lists are not null and have the same size
		if (tran_id != null && part_tran_id != null && tran_id.size() == part_tran_id.size()) {
			List<TRAN_MAIN_TRM_WRK_ENTITY> transactionsToSave = new ArrayList<>();

			for (int i = 0; i < tran_id.size(); i++) {
				String currentTranId = tran_id.get(i);
				String currentPartTranId = part_tran_id.get(i);

				TRAN_MAIN_TRM_WRK_ENTITY existingDevice = tRAN_MAIN_TRM_WRK_REP.getmodifyjournal(currentTranId,
						currentPartTranId);
				String existsrlno = existingDevice.getSrl_no();
				if (existingDevice != null) { // Check if existingDevice is not null
					System.out.println("Processing transaction with tran_id: " + currentTranId + " and part_tran_id: "
							+ currentPartTranId);

					for (TRAN_MAIN_TRM_WRK_ENTITY transaction : transactionDetails) {
						System.out.println("Second step to modify");
						System.out.println("Third step to modify: " + transaction.getFlow_code());
						System.out.println(transaction.getValue_date() + "trandate" + transaction.getTran_date());
						transaction.setSrl_no(existsrlno);
						transaction.setDel_flg("Y"); // Set the delete flag manually
						tRAN_MAIN_TRM_WRK_REP.save(transaction); // Add each transaction to the list
					}

					// Save all transactions after processing
					// tRAN_MAIN_TRM_WRK_REP.saveAll(transactionsToSave);
				} else {
					// Handle the case where existingDevice is null
					System.out.println("No value previously stored for tran_id: " + currentTranId
							+ " and part_tran_id: " + currentPartTranId);
				}
			}
		} else {
			System.out.println("tran_id and part_tran_id lists are not of the same size or are null.");
			return "Error: Transaction IDs and Part Transaction IDs must be the same size and not null.";
		}

		return "Transaction modification completed.";
	}

	/* pon prasanth */
	@Transactional
	@GetMapping("postedTrmRecords")
	public String postedTrmRecords(Model md, HttpServletRequest rq,
	        @RequestParam(required = false) String acct_num,
	        @RequestParam(required = false) String tran_id,
	        @RequestParam(required = false) String part_tran_id,
	        @RequestParam(required = false) String entry_user,
	        HttpServletRequest request) throws ParseException {

	    String userSession = (String) rq.getSession().getAttribute("USERID");

	    // Prevent same user from verifying
	    if (entry_user != null && entry_user.equalsIgnoreCase(userSession)) {
	        return "Same user cannot verify";
	    } else {
	        System.out.println("Tran id : " + tran_id);

	        List<TRAN_MAIN_TRM_WRK_ENTITY> allTransactions = tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tran_id);

	        List<String> partTranTypes = allTransactions.stream()
	                .map(TRAN_MAIN_TRM_WRK_ENTITY::getPart_tran_type)
	                .collect(Collectors.toList());

	        if (!partTranTypes.contains("Debit") || !partTranTypes.contains("Credit")) {
	            return "Error: Please select at least one credit and one debit transaction.";
	        }

	        String user = (String) rq.getSession().getAttribute("USERID");
	        Date conDate = (Date) rq.getSession().getAttribute("TRANDATE");

	        List<TRAN_MAIN_TRM_WRK_ENTITY> savedTransactions = new ArrayList<>();

	        // --- Step 1: Update account balances for debit/credit ---
	        for (TRAN_MAIN_TRM_WRK_ENTITY entity : allTransactions) {
	            entity.setPost_user(user);
	            entity.setPost_time(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
	            entity.setDel_flg("N");
	            entity.setTran_status("POSTED");
	            savedTransactions.add(entity);

	            Chart_Acc_Entity account = chart_Acc_Rep.getaedit(entity.getAcct_num());
	            BigDecimal balance = Optional.ofNullable(account.getAcct_bal()).orElse(BigDecimal.ZERO);
	            BigDecimal crAmt = Optional.ofNullable(account.getCr_amt()).orElse(BigDecimal.ZERO);
	            BigDecimal drAmt = Optional.ofNullable(account.getDr_amt()).orElse(BigDecimal.ZERO);

	            BigDecimal tranAmt = Optional.ofNullable(entity.getTran_amt()).orElse(BigDecimal.ZERO);

	            if ("Credit".equalsIgnoreCase(entity.getPart_tran_type())) {
	                balance = balance.add(tranAmt);
	                crAmt = crAmt.add(tranAmt);
	            } else {
	                balance = balance.subtract(tranAmt);
	                drAmt = drAmt.add(tranAmt);
	            }

	            account.setAcct_bal(balance);
	            account.setCr_amt(crAmt);
	            account.setDr_amt(drAmt);
	            account.setModify_time(conDate);
	            account.setModify_user(user);

	            chart_Acc_Rep.save(account);

	            // Handle disbursement separately
	            if ("DISBT".equalsIgnoreCase(entity.getFlow_code())) {
	                Lease_Loan_Master_Entity lease = lease_Loan_Master_Repo.findByref_no(entity.getAcct_num());
	                if (lease != null) {
	                    lease.setDisbursement(tranAmt);
	                    lease_Loan_Master_Repo.save(lease);
	                }
	            }
	        }

	        // --- Step 2: Aggregate all credit transactions by flow_code and due date ---
	        SimpleDateFormat sdfDB = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH); // e.g., 30-SEP-2025

	        Map<String, BigDecimal> totalPrdem = new HashMap<>();
	        Map<String, BigDecimal> totalIndem = new HashMap<>();
	        Map<String, BigDecimal> totalFeedem = new HashMap<>();
	        Map<String, BigDecimal> totalPendem = new HashMap<>();

	        List<TRAN_MAIN_TRM_WRK_ENTITY> creditTransactions = allTransactions.stream()
	                .filter(t -> "Credit".equalsIgnoreCase(t.getPart_tran_type()))
	                .collect(Collectors.toList());

	        for (TRAN_MAIN_TRM_WRK_ENTITY entity : creditTransactions) {
	            String dueDateStr = sdfDB.format(entity.getFlow_date());
	            BigDecimal tranAmt = Optional.ofNullable(entity.getTran_amt()).orElse(BigDecimal.ZERO);

	            switch (entity.getFlow_code().toUpperCase()) {
	                case "PRDEM":
	                    totalPrdem.put(dueDateStr, totalPrdem.getOrDefault(dueDateStr, BigDecimal.ZERO).add(tranAmt));
	                    break;
	                case "INDEM":
	                    totalIndem.put(dueDateStr, totalIndem.getOrDefault(dueDateStr, BigDecimal.ZERO).add(tranAmt));
	                    break;
	                case "FEEDEM":
	                    totalFeedem.put(dueDateStr, totalFeedem.getOrDefault(dueDateStr, BigDecimal.ZERO).add(tranAmt));
	                    break;
	                case "PENDEM":
	                    totalPendem.put(dueDateStr, totalPendem.getOrDefault(dueDateStr, BigDecimal.ZERO).add(tranAmt));
	                    break;
	                default:
	                    System.out.println("Unknown flow code: " + entity.getFlow_code());
	            }
	        }

	        // --- Step 3: Update demand records using aggregated totals ---
	        for (TRAN_MAIN_TRM_WRK_ENTITY entity : creditTransactions) {
	            String accountNumber = entity.getAcct_num();
	            String dueDateStr = sdfDB.format(entity.getFlow_date());

	            String datavalue = lOAN_ACT_MST_REPO.getLoanViewdatas(accountNumber);

	            List<LOAN_REPAYMENT_ENTITY> demandRecordsList = Optional
	                    .ofNullable(lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas21(datavalue, dueDateStr))
	                    .orElse(Collections.emptyList());

	            for (LOAN_REPAYMENT_ENTITY demandRecord : demandRecordsList) {
	                BigDecimal principalExp = Optional.ofNullable(demandRecord.getPrincipal_exp()).orElse(BigDecimal.ZERO);
	                BigDecimal interestExp = Optional.ofNullable(demandRecord.getInterest_exp()).orElse(BigDecimal.ZERO);
	                BigDecimal feeExp = Optional.ofNullable(demandRecord.getFee_exp()).orElse(BigDecimal.ZERO);
	                BigDecimal penaltyExp = Optional.ofNullable(demandRecord.getPenalty_exp()).orElse(BigDecimal.ZERO);

	                BigDecimal principalPaid = totalPrdem.getOrDefault(dueDateStr, BigDecimal.ZERO);
	                BigDecimal interestPaid = totalIndem.getOrDefault(dueDateStr, BigDecimal.ZERO);
	                BigDecimal feePaid = totalFeedem.getOrDefault(dueDateStr, BigDecimal.ZERO);
	                BigDecimal penaltyPaid = totalPendem.getOrDefault(dueDateStr, BigDecimal.ZERO);

	                demandRecord.setPrincipal_paid(demandRecord.getPrincipal_paid().add(principalPaid).min(principalExp));
	                demandRecord.setPrincipal_due(principalExp.subtract(demandRecord.getPrincipal_paid()).max(BigDecimal.ZERO));

	                demandRecord.setInterest_paid(demandRecord.getInterest_paid().add(interestPaid).min(interestExp));
	                demandRecord.setInterest_due(interestExp.subtract(demandRecord.getInterest_paid()).max(BigDecimal.ZERO));

	                demandRecord.setFee_paid(demandRecord.getFee_paid().add(feePaid).min(feeExp));
	                demandRecord.setFee_due(feeExp.subtract(demandRecord.getFee_paid()).max(BigDecimal.ZERO));

	                demandRecord.setPenalty_paid(demandRecord.getPenalty_paid().add(penaltyPaid).min(penaltyExp));
	                demandRecord.setPenalty_due(penaltyExp.subtract(demandRecord.getPenalty_paid()).max(BigDecimal.ZERO));

	                if (demandRecord.getPrincipal_due().compareTo(BigDecimal.ZERO) == 0
	                        && demandRecord.getInterest_due().compareTo(BigDecimal.ZERO) == 0
	                        && demandRecord.getFee_due().compareTo(BigDecimal.ZERO) == 0
	                        && demandRecord.getPenalty_due().compareTo(BigDecimal.ZERO) == 0) {
	                    demandRecord.setPayment_state("PAID");
	                }

	                demandRecord.setRepaid_date(entity.getTran_date());
	                lOAN_REPAYMENT_REPO.save(demandRecord);
	            }
	        }

	        // --- Step 4: Save audit and posted transactions ---
	        BGLSBusinessTable_Entity audit = new BGLSBusinessTable_Entity();
	        audit.setAudit_date(new Date());
	        audit.setEntry_time(new Date());
	        audit.setEntry_user(user);
	        audit.setFunc_code("VERIFIED");
	        audit.setAudit_table("BGLSBUSINESSTABLE");
	        audit.setAudit_screen("VERIFIED");
	        audit.setEvent_id(user);
	        audit.setEvent_name((String) request.getSession().getAttribute("USERNAME"));
	        audit.setModi_details("Verified Successfully");
	        audit.setAudit_ref_no(sequence.generateRequestUUId());

	        UserProfile userProfile = userProfileRep.getRole(user);
	        audit.setAuth_user(userProfile.getAuth_user());
	        audit.setAuth_time(userProfile.getAuth_time());

	        bGLSBusinessTable_Rep.save(audit);
	        tRAN_MAIN_TRM_WRK_REP.saveAll(savedTransactions);

	        return "Posted Successfully";
	    }
	}

	/* pon prasanth */
	@GetMapping("transactionaccountdetailsvalues")
	public TRAN_MAIN_TRM_WRK_ENTITY transactionaccountdetailsvalues(@RequestParam(required = false) String acct_num,
			@RequestParam(required = false) String tran_id, @RequestParam(required = false) String part_tran_id) {
		TRAN_MAIN_TRM_WRK_ENTITY accountvalue = tRAN_MAIN_TRM_WRK_REP.getValuepopvalues(tran_id, acct_num,
				part_tran_id);
		return accountvalue;
	}

	@GetMapping("calculateRepayment")
	public List<RepaymentScheduleEntity> calculateRepayment(@RequestParam(defaultValue = "0") int inst_id,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inst_start_dt, @RequestParam String inst_freq,
			@RequestParam(defaultValue = "0") int no_of_inst, @RequestParam(defaultValue = "0") double inst_amount,
			@RequestParam String inst_amt_spec, @RequestParam(defaultValue = "0") double principalAmount,
			@RequestParam(defaultValue = "0") double inst_pct, @RequestParam String maturity_flg,
			@RequestParam String interest_frequency) throws ParseException {

		System.out.println(principalAmount + " " + inst_amt_spec + " " + inst_freq + " " + no_of_inst + " "
				+ inst_start_dt + " " + inst_pct + " " + inst_amount + " " + inst_id + " " + maturity_flg);
		if (maturity_flg.equals("Y")) {
			return repaymentScheduleServices.calculateRepaymentScheduleAtMaturity(principalAmount, inst_amt_spec,
					interest_frequency, no_of_inst, inst_start_dt, inst_pct, inst_amount, inst_id);
		} else {
			return repaymentScheduleServices.calculateRepaymentSchedule(principalAmount, inst_amt_spec, inst_freq,
					no_of_inst, inst_start_dt, inst_pct, inst_amount, inst_id);
		}
	}

	/* pon prasanth */
	@GetMapping("datepickervalueaudit")
	public List<BGLSAuditTable> datepickervalueaudit(@RequestParam(required = false) Date audit_date) {
		List<BGLSAuditTable> accountvalue = bGLSAuditTable_Rep.getauditListLocalvalues(audit_date);
		System.out.println("THE ACCOUNT NUMBER IS" + audit_date);
		return accountvalue;
	}

	/* Thanveer */
	/*
	 * @RequestMapping(value = "getBranchName", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public String getBranchName(Model md, HttpServletRequest
	 * rq, @RequestParam(required = false) String branchId) {
	 * 
	 * String branch = employee_Profile_Rep.getBranchName(branchId);
	 * System.out.println(branch); md.addAttribute("branchName", branch); return
	 * branch;
	 * 
	 * }
	 */

	/* Thanveer */
	@RequestMapping(value = "getBranchName", method = RequestMethod.GET)
	@ResponseBody
	public String getBranchName(Model md, HttpServletRequest rq, @RequestParam(required = false) String branchId) {

		String branch = bGLSAuditTable_Rep.getBranchName(branchId);
		System.out.println(branch);
		md.addAttribute("branchName", branch);
		return branch;

	}

	/* THANVEER */
	@RequestMapping(value = "depositAdd", method = RequestMethod.POST)
	@ResponseBody
	public String depositAdd(HttpServletRequest rq, Model md, @ModelAttribute DepositEntity depositEntity,
			@ModelAttribute Td_defn_table td_defn_table) {

		String user = (String) rq.getSession().getAttribute("USERID");
		List<String> existingdata = depositRep.getexistingData();
		if (existingdata.contains(depositEntity.getDepo_actno())) {
			return "Account Num Already Exist";
		}
		String msg = depositServices.deposit(td_defn_table, depositEntity);

		return msg;
	}

	/* THANVEER */
	@RequestMapping(value = "depositAddCust", method = RequestMethod.POST)
	@ResponseBody
	public String depositAddCust(HttpServletRequest rq, Model md, @RequestBody DepositEntity depositEntity) {
		Td_defn_table td_defn_table = new Td_defn_table();
		String user = (String) rq.getSession().getAttribute("USERID");
		List<String> existingdata = depositRep.getexistingData();
		if (existingdata.contains(depositEntity.getDepo_actno())) {
			return "Account Num Already Exist";
		}
		String msg = depositServices.deposit(td_defn_table, depositEntity);

		return msg;
	}

	/* THANVEER */
	@RequestMapping(value = "/depositVerify", method = RequestMethod.POST)
	@ResponseBody
	public String depositVerify(HttpServletRequest rq, Model md, @ModelAttribute DepositEntity depositEntity,
			@ModelAttribute Td_defn_table td_dfn_table, @RequestParam(required = false) String actno) {
		System.out.println(depositEntity);
		System.out.println("hello");

		DepositEntity up = depositRep.getCustdataact(actno);
		System.out.println("up" + up.getDepo_actno());
		depositEntity.setDepo_actno(actno);
		depositEntity.setEntity_flg("Y");
		depositEntity.setModify_flg("N");
		depositEntity.setDel_flg("N");

		depositRep.save(depositEntity);

		return "Verified Successfully";
	}

	/* THANVEER */
	@RequestMapping(value = "/verifyDeposit", method = RequestMethod.POST)

	public String verifyDeposit(@RequestParam(required = false) String accountNo) {

		DepositEntity up = depositRep.getCustdataact(accountNo);
		System.out.println("up" + up.getDepo_actno());

		up.setEntity_flg("Y");
		up.setModify_flg("N");
		up.setDel_flg("N");

		depositRep.save(up);

		return "Verified Successfully";
	}

	/* THANVEER */
	@RequestMapping(value = "/depositModify", method = RequestMethod.POST)
	@ResponseBody
	public String depositModify(HttpServletRequest rq, Model md, @ModelAttribute DepositEntity depositEntity,
			@ModelAttribute Td_defn_table td_defn_table) {

		String user = (String) rq.getSession().getAttribute("USERID");
		System.out.println("save");
		String msg = depositServices.deposit(td_defn_table, depositEntity);
		return msg;
	}

	/* Thanveer */
	@RequestMapping(value = "getCustId", method = RequestMethod.GET)
	@ResponseBody
	public Object[] getCustId(Model md, HttpServletRequest rq, @RequestParam(required = false) String custId) {
		System.out.println(custId);
		Object[] cust = bACP_CUS_PROFILE_REPO.getCustList(custId);
		List<Object> custList = Arrays.asList(cust);

		// Object[] cust = bACP_CUS_PROFILE_REPO.getCustList(custId);

		if (cust != null && cust.length > 0) {
			for (Object cust1 : cust) {
				// Assuming cust1 is an Object[] (array), then access the 8th element (index 7)
				if (cust1 instanceof Object[]) {
					Object[] custArray = (Object[]) cust1; // Cast to Object[] to access the 8th element
					if (custArray.length > 6 && "INDIVIDUAL".equals(custArray[7])) {
						System.out.println("CIF_ID is null, calling getCustList1");
						cust = bACP_CUS_PROFILE_REPO.getCustList(custId); // Re-fetch the data
						break; // Break the loop if condition is met
					}
				}
			}
		} else {
			System.out.println("it is in else");
			cust = bACP_CUS_PROFILE_REPO.getCustList1(custId); // Fetch data from an alternative method
		}

		md.addAttribute("cust", cust);
		return cust;

	}

	/* THANVEER */
	@GetMapping("getDepositFlow")
	public List<Td_defn_table> td_defn_table(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date deposit_date,
			@RequestParam String deposit_type, @RequestParam String depo_actno, @RequestParam String deposit_period,
			@RequestParam BigDecimal deposit_amt, @RequestParam String rate_of_int, @RequestParam String frequency)
			throws ParseException {

		// Retrieve existing data
		List<String> existingdata = depositRep.getexistingData();

		// Initialize the list to store the Td_defn_table entities
		List<Td_defn_table> newTdDefnTables = new ArrayList<>();

		String depotype = deposit_type;

		if (depotype.equals("Fixed")) {
			BigDecimal flowAmount1 = null;
			BigDecimal flowAmount2 = null;
			BigDecimal depositPeriod = new BigDecimal(deposit_period);
			BigDecimal one = BigDecimal.ONE;
			System.out.println("Deposit Period: " + depositPeriod);

			String Frequency = frequency;
			Date depositDate = deposit_date;
			System.out.println(depositDate);
			BigDecimal flowAmount = deposit_amt;
			System.out.println(flowAmount + " flowAmount");

			BigDecimal rate = new BigDecimal(rate_of_int);
			int scale = 2;
			BigDecimal percentageRate = rate.divide(BigDecimal.valueOf(100), scale, RoundingMode.DOWN);

			if (Frequency.equals("Monthly")) {
				flowAmount1 = flowAmount.multiply(percentageRate)
						.divide(BigDecimal.valueOf(12), scale, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
				System.out.println("amt1: " + flowAmount1);
			} else {
				flowAmount2 = flowAmount.multiply(percentageRate).setScale(2, RoundingMode.DOWN);
				System.out.println("amt2: " + flowAmount2);
			}

			Date lastIoDepositDate = null;
			BigDecimal rowCount = depositPeriod.add(new BigDecimal("2"));

			// Loop through each period
			for (BigDecimal i = BigDecimal.ONE; i.compareTo(rowCount) <= 0; i = i.add(BigDecimal.ONE)) {
				Td_defn_table newTdDefnTable = new Td_defn_table();

				newTdDefnTable.setAcid(depo_actno);
				// newTdDefnTable.setFlow_date(depositDate);

				if (i.equals(BigDecimal.ONE)) {
					// First row
					newTdDefnTable.setFlow_code("PI");
					newTdDefnTable.setFlow_amt(flowAmount);
					newTdDefnTable.setClr_bal_amt(flowAmount);
				} else if (i.equals(rowCount)) {
					// Last row

					newTdDefnTable.setFlow_code("PO");

					depositDate = lastIoDepositDate;

					newTdDefnTable.setFlow_amt(flowAmount);
					newTdDefnTable.setClr_bal_amt(BigDecimal.ZERO);
				} else {
					// Middle rows

					newTdDefnTable.setFlow_code("IO");

					newTdDefnTable.setFlow_amt(Frequency.equals("Monthly") ? flowAmount1 : flowAmount2);
					newTdDefnTable.setClr_bal_amt(flowAmount);
					lastIoDepositDate = depositDate;
				}

				newTdDefnTable.setFlow_date(depositDate);

				// Generate serial number and flow ID
				String notifyRef = td_defn_Repo.getTranNo();
				int notifyReference = notifyRef != null ? Integer.parseInt(notifyRef) + 1 : 1;
				newTdDefnTable.setSrl_no(BigDecimal.valueOf(notifyReference));
				newTdDefnTable.setFlow_id(i);

				newTdDefnTables.add(newTdDefnTable);

				// td_defn_Repo.save(newTdDefnTable);

				// Update the depositDate for the next iteration based on frequency
				if (!i.equals(rowCount) && !i.equals(rowCount.add(one))) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(depositDate);
					if (Frequency.equals("Monthly")) {
						calendar.add(Calendar.MONTH, 1);
					} else {
						calendar.add(Calendar.YEAR, 1);
					}
					depositDate = calendar.getTime();
				}
			}
		} else if (depotype.equals("Reinvestment")) {

			System.out.println("deposit");
			BigDecimal previousBalance = BigDecimal.ZERO;
			BigDecimal balance1 = BigDecimal.ZERO;
			BigDecimal balance2 = BigDecimal.ZERO;
			BigDecimal depositPeriod = new BigDecimal(deposit_period);
			BigDecimal one = BigDecimal.ONE;
			System.out.println("Deposit Period: " + depositPeriod);

			String Frequency = frequency;
			Date depositDate = deposit_date;
			System.out.println(depositDate + " maturityDate");

			BigDecimal rate = new BigDecimal(rate_of_int);
			int scale = 2;
			BigDecimal percentageRate = rate.divide(BigDecimal.valueOf(100), scale, RoundingMode.DOWN);

			Date lastIoDepositDate = null;
			BigDecimal rowCount = depositPeriod.add(new BigDecimal("2"));

			// Loop through each period
			for (BigDecimal i = BigDecimal.ONE; i.compareTo(rowCount) <= 0; i = i.add(one)) {
				Td_defn_table newTdDefnTable = new Td_defn_table();

				newTdDefnTable.setAcid(depo_actno);
				newTdDefnTable.setFlow_date(depositDate);

				BigDecimal flowAmount = previousBalance.equals(BigDecimal.ZERO) ? deposit_amt : previousBalance;
				System.out.println("flowAmountfinal" + flowAmount);

				if (i.equals(BigDecimal.ONE)) {
					// First row
					newTdDefnTable.setFlow_code("PI");
					newTdDefnTable.setFlow_amt(flowAmount);
					newTdDefnTable.setClr_bal_amt(flowAmount);
				} else if (i.equals(rowCount)) {
					// Last row

					newTdDefnTable.setFlow_code("TO");

					depositDate = lastIoDepositDate;

					newTdDefnTable.setFlow_amt(flowAmount);
					System.out.println("flowAmountfinal" + flowAmount);
					newTdDefnTable.setClr_bal_amt(BigDecimal.ZERO);
				} else {
					// Middle rows
					newTdDefnTable.setFlow_code("II");
					if (Frequency.equals("Monthly")) {
						BigDecimal flowAmount1 = flowAmount.multiply(percentageRate)
								.divide(BigDecimal.valueOf(12), scale, RoundingMode.DOWN)
								.setScale(2, RoundingMode.DOWN);
						balance1 = flowAmount.add(flowAmount1);

						newTdDefnTable.setFlow_amt(flowAmount1);
						newTdDefnTable.setClr_bal_amt(balance1);
						previousBalance = balance1;

						lastIoDepositDate = depositDate;
					} else {
						BigDecimal flowAmount2 = flowAmount.multiply(percentageRate).setScale(2, RoundingMode.DOWN);
						balance2 = flowAmount.add(flowAmount2);

						newTdDefnTable.setFlow_amt(flowAmount2);
						newTdDefnTable.setClr_bal_amt(balance2);
						previousBalance = balance2;
					}
				}

				newTdDefnTable.setFlow_date(depositDate);

				// Generate serial number and flow ID
				String notifyRef = td_defn_Repo.getTranNo();
				int notifyReference = notifyRef != null ? Integer.parseInt(notifyRef) + 1 : 1;
				newTdDefnTable.setSrl_no(BigDecimal.valueOf(notifyReference));
				newTdDefnTable.setFlow_id(i);

				newTdDefnTables.add(newTdDefnTable);

				// td_defn_Repo.save(newTdDefnTable);

				// Update the depositDate for the next iteration based on frequency
				if (!i.equals(rowCount) && !i.equals(rowCount.add(one))) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(depositDate);
					if (Frequency.equals("Monthly")) {
						calendar.add(Calendar.MONTH, 1);
					} else {
						calendar.add(Calendar.YEAR, 1);
					}
					depositDate = calendar.getTime();
				}
			}
		}
		for (Td_defn_table up : newTdDefnTables) {
			System.out.println(up.getFlow_code());
		}
		return newTdDefnTables;

	}

	@GetMapping("FlowForDateloan")
	public Map<String, Object> FlowForDateloan(@RequestParam(required = false) String actno1,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date todate) throws ParseException {

		System.out.println("===== Input Parameters =====");
		System.out.println("Account Number: " + actno1);
		System.out.println("From Date: " + dateFrom);
		System.out.println("To Date: " + todate);

		// Convert single date to LocalDate
		LocalDate inputDate = dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate monthStart = inputDate.withDayOfMonth(1);
		LocalDate monthEnd = inputDate.with(TemporalAdjusters.lastDayOfMonth());

		if (!inputDate.equals(monthEnd)) {
			// Not month-end → show popup / message
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Booking allowed only at month-end.");
			return response;
		}
		System.out.println("===== Month Start/End =====");
		System.out.println("Month Start: " + monthStart);
		System.out.println("Month End: " + monthEnd);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String monthStartStr = monthStart.format(formatter);
		String monthEndStr = monthEnd.format(formatter);

		System.out.println("Formatted Month Start: " + monthStartStr);
		System.out.println("Formatted Month End: " + monthEndStr);

		// Fetch next pending flow
		List<Object[]> defnList = lOAN_REPAYMENT_REPO.getNextPendingFlow(todate, actno1);

		if (defnList == null || defnList.isEmpty()) {
			System.out.println("No records found for account: " + actno1);
			return Collections.emptyMap();
		}

		Object[] defn = defnList.get(0);

		// Convert flowdate
		Date flowdate = null;
		if (defn[0] instanceof java.sql.Timestamp) {
			flowdate = new Date(((java.sql.Timestamp) defn[0]).getTime());
		} else if (defn[0] instanceof java.sql.Date) {
			flowdate = new Date(((java.sql.Date) defn[0]).getTime());
		} else if (defn[0] instanceof java.util.Date) {
			flowdate = (Date) defn[0];
		}

		System.out.println("===== Flow Data from DB =====");
		System.out.println("Flow Date: " + flowdate);
		System.out.println("Flow ID: " + defn[1]);
		System.out.println("Flow Code: " + defn[2]);
		System.out.println("Flow Amount (raw): " + defn[3]);
		System.out.println("Loan Account No: " + defn[4]);
		System.out.println("Account Name: " + defn[5]);
		System.out.println("Interest Rate: " + defn[6]);

		// Convert amount safely
		BigDecimal amt = (defn[3] instanceof Integer) ? BigDecimal.valueOf((Integer) defn[3])
				: (defn[3] instanceof BigDecimal) ? (BigDecimal) defn[3] : new BigDecimal(defn[3].toString());

		System.out.println("Flow Amount (BigDecimal): " + amt);

		LocalDate localDate = flowdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Subtract one month from the LocalDate
		LocalDate previousMonthSameDay = localDate.minusMonths(1);

		// Convert the LocalDate back to Date
		Date resultDate = Date.from(previousMonthSameDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

		System.out.println("Previous month, same day: " + resultDate);

		LocalDate startDate = convertToLocalDate(resultDate);
		LocalDate endDate = convertToLocalDate(todate);

		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		System.out.println("Days Between Start and ToDate: " + daysBetween);

		BigDecimal daysInMonthDecimal = new BigDecimal(ChronoUnit.DAYS.between(monthStart, monthEnd) + 1);
		BigDecimal singledayamount = amt.divide(daysInMonthDecimal, 2, RoundingMode.HALF_UP);
		System.out.println("Total Days in Month: " + daysInMonthDecimal);
		System.out.println("Single Day Interest Amount: " + singledayamount);

		BigDecimal betweendaysDecimal = new BigDecimal(daysBetween);
		BigDecimal finalamount = singledayamount.multiply(betweendaysDecimal);
		System.out.println("Final Amount Before Rounding: " + finalamount);

		BigDecimal roundedFinalAmount = finalamount.remainder(BigDecimal.ONE).compareTo(new BigDecimal("0.50")) < 0
				? finalamount.setScale(0, RoundingMode.FLOOR)
				: finalamount.setScale(0, RoundingMode.CEILING);

		System.out.println("Final Rounded Amount: " + roundedFinalAmount);

		// Prepare response
		Map<String, Object> response = new HashMap<>();
		response.put("flow_date", flowdate);
		response.put("flow_id", defn[1]);
		response.put("flow_code", defn[2]);
		response.put("flow_amt", roundedFinalAmount);
		response.put("loan_acct_no", defn[4]);
		response.put("acct_name", defn[5]);
		response.put("no_of_days", betweendaysDecimal);
		response.put("per_day_interest", singledayamount);
		response.put("interest_rate", defn[6]);

		System.out.println("===== Response Map =====");
		response.forEach((k, v) -> System.out.println(k + " : " + v));

		return response;
	}

	private static LocalDate convertToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@GetMapping("FlowForDate")
	public Td_defn_table FlowForDate(@RequestParam(required = false) String actno2,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom)
			throws ParseException {

		System.out.println("hiiihihihihihihihi");
		System.out.println("dateFrom" + dateFrom);
		// Retrieve existing data
		Td_defn_table defn = td_defn_Repo.getflowcode(actno2, dateFrom);
		System.out.println(defn + "defncdscv");
		if (defn == null) {
			defn = td_defn_Repo.getPreviousFlowCode(actno2, dateFrom);
			System.out.println(defn + "defn2132");
		}
		BigDecimal amt = defn.getFlow_amt();
		Date flowdate = defn.getFlow_date();

		LocalDate localDate = flowdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Subtract one month from the LocalDate
		LocalDate previousMonthSameDay = localDate.minusMonths(1);

		// Convert the LocalDate back to Date
		Date resultDate = Date.from(previousMonthSameDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

		System.out.println("Previous month, same day: " + resultDate);

		LocalDate startDate = convertToLocalDate(resultDate);
		LocalDate endDate = convertToLocalDate(flowdate);
		long monthsBetween = ChronoUnit.DAYS.between(startDate, endDate);
		System.out.println(monthsBetween + "monthsBetween");

		LocalDate startDate1 = convertToLocalDate(resultDate);
		LocalDate endDate1 = convertToLocalDate(dateFrom);
		long monthsBetween1 = ChronoUnit.DAYS.between(startDate1, endDate1);
		System.out.println(monthsBetween1 + "monthsBetween1");

		BigDecimal daysInMonthDecimal = new BigDecimal(monthsBetween);
		BigDecimal singledayamount = amt.divide(daysInMonthDecimal, 2, RoundingMode.HALF_UP);
		System.out.println(singledayamount + "singledayamount");

		BigDecimal betweendaysDecimal = new BigDecimal(monthsBetween1);
		// betweendaysDecimal=betweendaysDecimal.add(BigDecimal.ONE);
		BigDecimal finalamount = singledayamount.multiply(betweendaysDecimal);
		System.out.println(finalamount + "finalamount");

		defn.setTran_amt(finalamount);
		defn.setResidual_bal(betweendaysDecimal);

		return defn;

	}

	@PostMapping("addLeaseAccount")
	@Transactional
	public String addLeaseAccount(HttpServletRequest req, @RequestBody LeaseData leaseData) {
		String userID = (String) req.getSession().getAttribute("USERID");
		String response = leaseLoanService.addLeaseLoan(leaseData, userID);
		return response;
	}

	@GetMapping("getInterestDetailsView")
	public List<Principle_and_intrest_shedule_Entity> getInterestDetailsView(@RequestParam String accountNo)
			throws ParseException {

		Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(accountNo);
		NoticeDetailsPayment0Entity paymentDetails = noticeDetailsPayment0Rep.getPaymentDetails(accountNo);

		BigDecimal product = loandetails.getLoan_sanctioned();
		BigDecimal productAmt = loandetails.getLoan_sanctioned();
		BigDecimal intRate = loandetails.getEffective_interest_rate();
		Date creation_Date = loandetails.getDate_of_loan();

		int no_of_inst = Integer.valueOf(paymentDetails.getNo_of_inst());
		Date start_date = paymentDetails.getInst_start_dt();

		LocalDate startDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = startDate.plus(no_of_inst, ChronoUnit.MONTHS);
		Date calculatedEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		BigDecimal instmentAmount = paymentDetails.getInst_amount();
		String principle_frequency = paymentDetails.getInst_freq();
		String interestFrequency = paymentDetails.getInterest_frequency();

		List<TestPrincipalCalculation> InterestAmount = interestCalculationServices.calculatePrincialPaymentNotice(
				start_date, calculatedEndDate, product, productAmt, principle_frequency, intRate, no_of_inst,
				instmentAmount, interestFrequency);

		int toltalInstallment = InterestAmount.size();

		List<Principle_and_intrest_shedule_Entity> principleEntity = new ArrayList<>();
		int noOfInstallment = 1;

		if (toltalInstallment > 0) {
			for (TestPrincipalCalculation record : InterestAmount) {
				Principle_and_intrest_shedule_Entity entity = new Principle_and_intrest_shedule_Entity();

				entity.setLoan_amt(productAmt);
				entity.setNo_of_instalment(BigDecimal.valueOf(noOfInstallment));
				entity.setAccount_creation_date(creation_Date);
				entity.setEffective_interest_rate(intRate);
				entity.setTotal_installment(BigDecimal.valueOf(toltalInstallment));
				entity.setFrom_date(record.getInstallmentFromDate());
				entity.setInstallment_date(record.getInstallmentDate());
				// entity.setInstallment_amt(record.getInterestAmount());
				entity.setInterest_amt(record.getInterestAmount());
				entity.setPrincipal_amt(record.getPrincipalAmount());
				entity.setPrincipal_outstanding(record.getPrincipalAmountOutstanding());
				entity.setInstallment_description(record.getInstallmentDescription());
				entity.setCharges_amt(BigDecimal.ZERO);
				if (record.getInstallmentDescription().equalsIgnoreCase("Regular Installment")) {
					entity.setInstallment_amt(record.getInterestAmount().add(record.getPrincipalAmount()));
				} else {
					entity.setInstallment_amt(record.getInterestAmount());
				}

				noOfInstallment++;
				principleEntity.add(entity);
			}

		} else {

		}

		return principleEntity;
	}

	@GetMapping("getdemandflow")
	public List<DMD_TABLE> getdemandflow(@RequestParam String accountNo) throws ParseException {

		Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(accountNo);
		NoticeDetailsPayment0Entity paymentDetails = noticeDetailsPayment0Rep.getPaymentDetails(accountNo);

		BigDecimal product = loandetails.getLoan_sanctioned();
		BigDecimal productAmt = loandetails.getLoan_sanctioned();
		BigDecimal intRate = loandetails.getEffective_interest_rate();
		Date creation_Date = loandetails.getDate_of_loan();

		int no_of_inst = Integer.valueOf(paymentDetails.getNo_of_inst());
		Date start_date = paymentDetails.getInst_start_dt();

		LocalDate startDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = startDate.plus(no_of_inst, ChronoUnit.MONTHS);
		Date calculatedEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		BigDecimal instmentAmount = paymentDetails.getInst_amount();
		String principle_frequency = paymentDetails.getInst_freq();
		String interestFrequency = paymentDetails.getInterest_frequency();

		List<TestPrincipalCalculation> InterestAmount = interestCalculationServices.calculatePrincialPaymentNotice(
				start_date, calculatedEndDate, product, productAmt, principle_frequency, intRate, no_of_inst,
				instmentAmount, interestFrequency);

		int toltalInstallment = InterestAmount.size();

		List<DMD_TABLE> principleAndInterest = new ArrayList<>();

		int noOfInstallment = 1;
		int noOfprincipalInstallment = 1;

		if (toltalInstallment > 0) {
			for (TestPrincipalCalculation record : InterestAmount) {
				DMD_TABLE entity = new DMD_TABLE();

				entity.setFlow_id(BigDecimal.valueOf(noOfInstallment));

				entity.setFlow_frq(record.getInstallmentFrequency());
				entity.setFlow_date(record.getInstallmentDate());

				entity.setFlow_amt(record.getInterestAmount().add(record.getPrincipalAmount()));
				entity.setFlow_code("RIDEM");

				noOfInstallment++;
				principleAndInterest.add(entity);
			}

			/*
			 * for (TestPrincipalCalculation record : InterestAmount) { DMD_TABLE entity =
			 * new DMD_TABLE();
			 * 
			 * entity.setFlow_id(BigDecimal.valueOf(noOfprincipalInstallment));
			 * 
			 * entity.setFlow_frq(record.getInstallmentFrequency());
			 * entity.setFlow_date(record.getInstallmentDate());
			 * 
			 * entity.setFlow_amt(record.getPrincipalAmount());
			 * entity.setFlow_code("PRDEM");
			 * 
			 * noOfprincipalInstallment++; principleAndInterest.add(entity); }
			 */
		} else {

		}

		// principleAndInterest.sort(Comparator.comparing(DMD_TABLE::getFlow_id));

		return principleAndInterest;
	}

	@PostMapping("verifyLeaseLoan")
	@Transactional
	public String verifyLeaseLoan(HttpServletRequest req, @RequestParam String accountNo) {
		String userID = (String) req.getSession().getAttribute("USERID");
		String response = leaseLoanService.verifyleaseloan(accountNo, userID);
		return response;
	}

	/* pon prasanth */
	@GetMapping("transactionaccountdetailsvaluesed")
	public Td_defn_table transactionaccountdetailsvaluesed(@RequestParam(required = false) String acid) {
		Td_defn_table accountvalue = td_defn_Repo.getactListval(acid);
		System.out.println("THE ACCOUNT NUMBER IS" + acid);
		return accountvalue;
	}

	/* pon prasanth */
	@GetMapping("transactionaccountdetailsvaluesed1")
	public Lease_Loan_Master_Entity transactionaccountdetailsvaluesed1(@RequestParam(required = false) String acid) {
		Lease_Loan_Master_Entity accountvalue = lease_Loan_Master_Repo.findByref_no(acid);
		System.out.println("THE ACCOUNT NUMBER IS" + acid);
		return accountvalue;
	}

	/* pon prasanth */
	@GetMapping("transactionaccountdetailsdepositedebit")
	public DepositEntity transactionaccountdetailsdepositedebit(@RequestParam(required = false) String DEPO_ACTNO) {
		DepositEntity accountvalue = depositRep.getCustdataact(DEPO_ACTNO);
		System.out.println("THE debit account ACCOUNT NUMBER IS" + DEPO_ACTNO);
		return accountvalue;
	}

	/* pon prasanth */
	@GetMapping("transactionaccountdetailsvaluesdep")
	public TRAN_MAIN_TRM_WRK_ENTITY transactionaccountdetailsvaluesdep(
			@RequestParam(required = false) String acct_num) {
		TRAN_MAIN_TRM_WRK_ENTITY accountvalue = tRAN_MAIN_TRM_WRK_REP.gettranpopvaluesdata(acct_num);
		System.out.println("the Entered Account Num Is  " + acct_num);
		return accountvalue;
	}

	/* pon prasanth */
	@GetMapping("demandloanvalues")
	public DMD_TABLE demandloanvalues(@RequestParam(required = false) String acct_num) {
		DMD_TABLE accountvalue = dmdRepo.gettranpopvalues(acct_num);
		System.out.println("the Entered Account Num Is  " + acct_num);
		return accountvalue;
	}

	/* praveen */
	@GetMapping("transactionValues")
	public TRAN_MAIN_TRM_WRK_ENTITY transactionValues(@RequestParam(required = false) String tran_id,
			@RequestParam(required = false) String part_tran_id) {
		TRAN_MAIN_TRM_WRK_ENTITY accountvalue = tRAN_MAIN_TRM_WRK_REP.getValuepop(tran_id, part_tran_id);
		return accountvalue;
	}

	@GetMapping("validateAccountStatus")
	public String validateAccountStatus(Model md, HttpServletRequest rq, @RequestParam(required = false) String tran_id,
			HttpServletRequest request) {

		System.out.println("Tran id : " + tran_id);
		String msg = "";

		BigDecimal cr_amt = BigDecimal.ZERO;
		BigDecimal dr_amt = BigDecimal.ZERO;

		/* list of tranId values */
		List<TRAN_MAIN_TRM_WRK_ENTITY> values = tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tran_id);

		for (TRAN_MAIN_TRM_WRK_ENTITY entity : values) {
			if (entity.getPart_tran_type().equals("Credit")) {
				cr_amt = cr_amt.add(entity.getTran_amt());
			} else {
				dr_amt = dr_amt.add(entity.getTran_amt());
			}
		}

		if (cr_amt.compareTo(dr_amt) == 0) {
			msg = "Validation successful: Credit and Debit amounts are equal.";
		} else {
			msg = "Validation failed: Credit and Debit amounts are not equal.";
		}

		return msg;
	}

	private final BigDecimalParser bigDecimalParser = new BigDecimalParser();

	@PostMapping("/uploadxml")
	@ResponseBody
	public String uploadxml(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		// Initialize BigDecimal variables to store the sum of credit and debit
		// transactions
		BigDecimal creditSum = BigDecimal.ZERO;
		BigDecimal debitSum = BigDecimal.ZERO;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		LocalDate localDate = LocalDate.now();
		String userId = (String) req.getSession().getAttribute("USERID");
		// String user=userId;
		System.out.println(userId + "userId");

		LocalDateTime localDateTime = LocalDateTime.now(); // Get current date and time
		java.util.Date utilDate = java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date entryDate = new Date(utilDate.getTime());
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// List to store transactions temporarily
		List<TRAN_MAIN_TRM_WRK_ENTITY> transactions = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet contains data
			int rowCount = 1; // Initialize row count
			String tranId = tranMainRep.gettrmRefUUID1();
			String commonTranId = "TR" + tranId;
			String del_flag = "N";
			// Process each row in the Excel sheet
			for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Starting from row 1, skipping the header
				Row row = sheet.getRow(i);
				TRAN_MAIN_TRM_WRK_ENTITY transaction = new TRAN_MAIN_TRM_WRK_ENTITY();

				// Branch ID logic
				String branchId = getCellValueAsString(row.getCell(0));
				if (branchId != null && branchId.contains(".")) {
					branchId = new BigDecimal(branchId).stripTrailingZeros().toPlainString();
				}
				transaction.setBranch_id(branchId);

				// Other field values
				transaction.setAcid(getValidValue(getCellValueAsString(row.getCell(1))));
				transaction.setCust_id(getValidValue(getCellValueAsString(row.getCell(2))));

				// Transaction ID logic
				transaction.setTran_id(commonTranId);
				transaction.setPart_tran_id(BigDecimal.valueOf(rowCount));
				setCellValue(row, 7, String.valueOf(rowCount));
				rowCount++;

				String acctNum = getCellValueAsString(row.getCell(5));
				transaction.setAcct_num(getValidValue(acctNum));

				transaction.setAcct_name(getValidValue(getCellValueAsString(row.getCell(6))));
				transaction.setTran_type(getValidValue(getCellValueAsString(row.getCell(7))));

				// Process credit/debit type in cell 8
				String partTranType = getValidValue(getCellValueAsString(row.getCell(8)));
				transaction.setPart_tran_type(partTranType);

				transaction.setAcct_crncy(getValidValue(getCellValueAsString(row.getCell(9))));

				BigDecimal tranAmt = bigDecimalParser.getBigDecimalValue(row.getCell(10));
				if (tranAmt != null) {
					tranAmt = tranAmt.stripTrailingZeros(); // Remove trailing zeros
					if (tranAmt.scale() > 2) {
						tranAmt = tranAmt.setScale(2, RoundingMode.HALF_UP); // Set scale to 2
					}
				}

				transaction.setTran_amt(isZero(tranAmt) ? null : tranAmt);

				// Add to either credit or debit sum based on the value in cell 8
				if ("Credit".equalsIgnoreCase(partTranType)) {
					creditSum = creditSum.add(tranAmt != null ? tranAmt : BigDecimal.ZERO);
				} else if ("Debit".equalsIgnoreCase(partTranType)) {
					debitSum = debitSum.add(tranAmt != null ? tranAmt : BigDecimal.ZERO);
				}

				transaction.setTran_particular(getValidValue(getCellValueAsString(row.getCell(11))));
				transaction.setTran_remarks(getValidValue(getCellValueAsString(row.getCell(12))));

				// Set transaction dates
				transaction.setTran_date(date);
				transaction.setValue_date(date);
				transaction.setEntry_user(userId);
				System.out.println(transaction.getEntry_user() + "entry_user");
				transaction.setEntry_time(entryDate);

				parseAndSetDate(row, 27, transaction::setPost_time);
				parseAndSetDate(row, 30, transaction::setInstr_date);
				parseAndSetDate(row, 34, transaction::setModify_time);

				// Set other remaining fields
				transaction.setTran_ref_no(getValidValue(getCellValueAsString(row.getCell(15))));
				transaction.setAdd_details(getValidValue(getCellValueAsString(row.getCell(16))));
				transaction.setPartition_type(getValidValue(getCellValueAsString(row.getCell(17))));
				transaction.setPartition_det(getValidValue(getCellValueAsString(row.getCell(18))));
				transaction.setInstr_num(getValidValue(getCellValueAsString(row.getCell(19))));
				transaction.setRef_crncy(getValidValue(getCellValueAsString(row.getCell(20))));

				BigDecimal refCrncyAmt = bigDecimalParser.getBigDecimalValue(row.getCell(21));
				transaction.setRef_crncy_amt(isZero(refCrncyAmt) ? null : refCrncyAmt.stripTrailingZeros());

				BigDecimal rate = bigDecimalParser.getBigDecimalValue(row.getCell(23));
				transaction.setRate(isZero(rate) ? null : rate.stripTrailingZeros());

				// transaction.setEntry_user(getValidValue(getCellValueAsString(row.getCell(24))));
				transaction.setPost_user(getValidValue(getCellValueAsString(row.getCell(25))));
				transaction.setTran_status(getValidValue(getCellValueAsString(row.getCell(28))));
				transaction.setDel_flg(getValidValue(getCellValueAsString(row.getCell(29))));
				transaction.setTran_code(getValidValue(getCellValueAsString(row.getCell(31))));
				transaction.setTran_rpt_code(getValidValue(getCellValueAsString(row.getCell(32))));
				transaction.setModify_user(getValidValue(getCellValueAsString(row.getCell(33))));
				transaction.setFlow_code(getValidValue(getCellValueAsString(row.getCell(35))));
				transaction.setDel_flg(del_flag);

				// Serial Number
				transaction.setSrl_no(tranMainRep.gettrmRefUUID());

				// Add the transaction to the temporary list
				transactions.add(transaction);
			}

			// Check if the sums of credit and debit are equal after processing all rows
			if (creditSum.compareTo(debitSum) == 0) {
				// Save all transactions to the database if credit and debit sums match
				for (TRAN_MAIN_TRM_WRK_ENTITY transaction : transactions) {
					tranMainRep.save(transaction);
				}
				return "Credit sum: " + creditSum + "\nDebit sum: " + debitSum
						+ "\nTransactions successfully uploaded.";
			} else {
				// Do not save if credit and debit sums do not match
				return "Credit sum: " + creditSum + "\nDebit sum: " + debitSum
						+ "\nError: Debit sum does not equal Credit sum! RE Upload";
			}

		} catch (Exception e) {
			e.printStackTrace(); // Consider using a logging framework
			return "Error: " + e.getMessage();
		}
	}

	// Utility method to return null if the value is invalid
	private String getValidValue(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value;
	}

	// Utility method to check if a BigDecimal value is zero
	private boolean isZero(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) == 0;
	}

	private void setCellValue(Row row, int cellIndex, String tranId) {
		// TODO Auto-generated method stub

	}

	// Utility method to set cell values safely
	private void setCellValue(Row row, int cellIndex, Consumer<String> setter) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			setter.accept(getCellValueAsString(cell));
		}
	}

	// Utility method to parse date and set the value if successful
	private void parseAndSetDate(Row row, int cellIndex, Consumer<Date> setter) throws java.text.ParseException {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			try {
				String dateStr = getCellValueAsString(cell);
				if (!dateStr.isEmpty()) {
					Date parsedDate = parseDate(dateStr);
					setter.accept(parsedDate);
				}
			} catch (ParseException e) {
				System.err.println("Error parsing date for cell index " + cellIndex + ": " + e.getMessage());
			}
		}
	}

	// Custom method to parse date string into Date object
	public Date parseDate(String dateStr) throws ParseException, java.text.ParseException {
		if (dateStr == null || dateStr.isEmpty()) {
			return null; // Return null for empty strings
		}

		// Handle different date formats here as needed
		SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
		Date date = inputFormat.parse(dateStr);
		return date; // Return the parsed date directly
	}

	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		}

		CellType cellType = cell.getCellTypeEnum(); // Use getCellType() for compatibility with POI 3.7

		switch (cellType) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "Unsupported Cell Type";
		}
	}

	/* SURIYA */
	@GetMapping("AllApproved")
	public List<CustomerRequest> AllApproved(@RequestParam(required = false) String tran_id,
			@RequestParam(required = false) String part_tran_id) {
		return bACP_CUS_PROFILE_REPO.getApprovelist();
	}

	@GetMapping("Approved")
	public List<CustomerRequest> Approved(@RequestParam(required = false) String tran_id,
			@RequestParam(required = false) String part_tran_id) {
		return bACP_CUS_PROFILE_REPO.getapproved();
	}

	@GetMapping("NotApproved")
	public List<CustomerRequest> NotApproved(@RequestParam(required = false) String tran_id,
			@RequestParam(required = false) String part_tran_id) {
		return bACP_CUS_PROFILE_REPO.getnotapproved();
	}

//	@GetMapping("AllApprovedLoanMain")
//	public List<LOAN_ACT_MST_ENTITY> AllApprovedLoanMain(@RequestParam(required = false) String tran_id,
//			@RequestParam(required = false) String part_tran_id) {
//		return lOAN_ACT_MST_REPO.getLoanActDet();
//	}
//
//	@GetMapping("NotApprovedLoanMain")
//	public List<LOAN_ACT_MST_ENTITY> NotApprovedLoanMain() {
//		return lOAN_ACT_MST_REPO.getLoanActFilterUnverified();
//	}
//
//	@GetMapping("ApprovedLoanMain")
//	public List<LOAN_ACT_MST_ENTITY> ApprovedLoanMain() {
//		return lOAN_ACT_MST_REPO.getLoanActFilterVerified();
//	}

	
	@GetMapping("/AllApprovedLoanMain")
    public Map<String, Object> getAllLoans(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "200") int limit) {

        int totalItems = lOAN_ACT_MST_REPO.countAllLoans();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int offset = (page - 1) * limit;

        List<Object[]> data = lOAN_ACT_MST_REPO.getLoanActWithMobile(offset, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        return response;
    }

    @GetMapping("/NotApprovedLoanMain")
    public Map<String, Object> getNotApprovedLoans(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "200") int limit) {

        int totalItems = lOAN_ACT_MST_REPO.countUnverifiedLoans();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int offset = (page - 1) * limit;

        List<LOAN_ACT_MST_ENTITY> data = lOAN_ACT_MST_REPO.getLoanActFilterUnverified(offset, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        return response;
    }

    @GetMapping("/ApprovedLoanMain")
    public Map<String, Object> getApprovedLoans(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "200") int limit) {

        int totalItems = lOAN_ACT_MST_REPO.countVerifiedLoans();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int offset = (page - 1) * limit;

        List<LOAN_ACT_MST_ENTITY> data = lOAN_ACT_MST_REPO.getLoanActFilterVerified(offset, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        return response;
    }
    
    
	@GetMapping("loanflowDetails")
	public List<Map<String, Object>> loanflowDetails(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date todate,
			@RequestParam(required = false) String accountNumber) {

		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsdata(fromDate, todate, accountNumber);

		// Convert List<Object[]> to List<Map<String, Object>>
		List<Map<String, Object>> formattedRecords = new ArrayList<>();
		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			map.put("flow_date", record[0]); // Due Date
			map.put("flow_id", record[1]); // Static '1'
			map.put("flow_code", record[2]); // Static 'INDEM'
			map.put("flow_amt", record[3]); // Interest Due
			map.put("loan_acct_no", record[4]); // Loan Account ID
			map.put("acct_name", record[5]); // Loan Name
			map.put("encoded_key", record[6]); // Loan Name
			formattedRecords.add(map);
		}
		return formattedRecords;
	}

	@GetMapping("loanflowDetails1")
	public List<Map<String, Object>> loanflowDetails1(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date todate,
			@RequestParam(required = false) String accountNumber) {

		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsvalue(fromDate, todate, accountNumber);

		// Convert List<Object[]> to List<Map<String, Object>>
		List<Map<String, Object>> formattedRecords = new ArrayList<>();
		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			map.put("flow_date", record[0]); // Due Date
			map.put("flow_id", record[1]); // Flow ID
			map.put("flow_code", record[2]); // Flow Code
			map.put("flow_amt", record[3]); // Flow Amount
			map.put("loan_acct_no", record[4]); // Loan Account ID
			map.put("acct_name", record[5]); // Loan Name
			map.put("encoded_key", record[6]); // Encoded Key
			formattedRecords.add(map);
		}

		return formattedRecords; // ✅ Return formatted records instead of raw List<Object[]>
	}

	/* praveen */
	@GetMapping("depositflowDetails")
	public List<Td_defn_table> depositflowDetails(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date todate,
			@RequestParam(required = false) String accountNumber) {

		List<Td_defn_table> depositFlowRecords = td_defn_Repo.getDepositflows(fromDate, todate, accountNumber);

		return depositFlowRecords;
	}

	/* praveen */
	@GetMapping("depositAcctName")
	public String depositAcctName(@RequestParam(required = false) String acctNo) {

		String accountName = depositRep.getCustName(acctNo);

		return accountName;
	}

	@GetMapping("transactionBooking")
	public String transactionBooking(@RequestParam(required = false) String flow_code,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date flow_date,
			@RequestParam(required = false) String flow_amount, @RequestParam(required = false) String flow_id,
			@RequestParam(required = false) String account_no, @RequestParam(required = false) String accountName,
			@RequestParam(required = false) String operation, @RequestParam(required = false) String days,
			@RequestParam(required = false) BigDecimal interest,
			@RequestParam(required = false) String interest_percentage, HttpServletRequest rq) throws ParseException {

		String user = (String) rq.getSession().getAttribute("USERID");

		if (flow_date == null) {
			throw new IllegalArgumentException("Flow date cannot be null");
		}

		System.out.println("FLOW_CODE: " + flow_code);
		System.out.println("FLOW_DATE: " + flow_date);

		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();

		// Fetch the latest TRAN_DATE from the database
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE to LocalDate for easy comparison
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		BigDecimal partTranId1 = BigDecimal.valueOf(1);
		BigDecimal partTranId2 = BigDecimal.valueOf(2);
		System.out.println("the getting tran_id is here 1" + tranId);
		String ActNo = "";

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(flow_date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		Date monthEndDate = calendar.getTime();

		if ("Booking".equals(operation)) {
			System.out.println("Processing Booking");

			if ("PRDEM".equals(flow_code) || "INDEM".equals(flow_code)) {
				Lease_Loan_Work_Entity dep = lease_Loan_Work_Repo.getLeaseAccount(account_no);
				BigDecimal intrate = dep != null ? dep.getEffective_interest_rate() : BigDecimal.ZERO;

				String acct_num = "1200001220";
				String tranParticulars = "Booking: " + account_no + " for " + days + " days @ " + interest_percentage
						+ "%";

				Chart_Acc_Entity depositevalue = chart_Acc_Rep.getaedit(acct_num);
				ActNo = acct_num;

				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId1);
				debitTrm.setAcct_num(depositevalue.getAcct_num());
				debitTrm.setAcct_name(depositevalue.getAcct_name());
				debitTrm.setAcct_crncy(depositevalue.getAcct_crncy());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setTran_amt(interest);
				debitTrm.setTran_particular(tranParticulars);
				debitTrm.setTran_remarks(tranParticulars);
				debitTrm.setTran_date(Transaction_date);
				debitTrm.setValue_date(monthEndDate);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");

				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				String acct_num1 = "4100004110";
				Chart_Acc_Entity termdeposite = chart_Acc_Rep.getaedit(acct_num1);

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId2);
				creditTrm.setAcct_num(termdeposite.getAcct_num());
				creditTrm.setAcct_name(termdeposite.getAcct_name());
				creditTrm.setTran_type("TRANSFER");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(termdeposite.getAcct_crncy());
				creditTrm.setTran_amt(interest);
				creditTrm.setTran_particular(tranParticulars);
				creditTrm.setTran_remarks(tranParticulars);
				creditTrm.setTran_date(Transaction_date);
				creditTrm.setValue_date(monthEndDate);
				creditTrm.setFlow_code(flow_code);
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");

				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);
			}
		}
		System.out.println("the getting tran_id is here 2" + tranId);
		return tranId + "," + ActNo;
	}

	/* praveen - Interest (Application) */
	@GetMapping("transactionInterest")
	public String transactionInterest(@RequestParam(required = false) String flow_code,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date flow_date,
			@RequestParam(required = false) String flow_amount, @RequestParam(required = false) String flow_id,
			@RequestParam(required = false) String account_no, @RequestParam(required = false) String accountName,
			@RequestParam(required = false) String operation, HttpServletRequest rq) {

		String user = (String) rq.getSession().getAttribute("USERID");

		/* tranId sequence */
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();

		String account_value = "1200001220";
		Object[] result = tRAN_MAIN_TRM_WRK_REP.getLatestAccountBalanceAndType(account_value);
		String datavalues = "Interest";

		List<TRAN_MAIN_TRM_WRK_ENTITY> intrestcollections = tRAN_MAIN_TRM_WRK_REP.getAccountDetails(account_value);

		for (TRAN_MAIN_TRM_WRK_ENTITY valueses1 : intrestcollections) {
			Date flow_dates = valueses1.getFlow_date();
			System.out.println("THE DATABASE GET  FLOW_DATES RE HERE " + flow_dates);
		}

		// Fetch the latest TRAN_DATE from the database
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE to LocalDate for easy comparison
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		if (result != null && result.length > 0 && result[0] != null) {
			Date firstValue = (Date) result[0];
			if (firstValue.compareTo(flow_date) == 0) {
				datavalues = "InterestBooking";
				System.out.println("THE GETTING IF CONDITION VALUES ARE HERE " + firstValue);
			}
		}
		System.out.println("The getting values are here: " + datavalues);

		BigDecimal partTranId1 = BigDecimal.valueOf(1);
		BigDecimal partTranId2 = BigDecimal.valueOf(2);
		BigDecimal partTranId3 = BigDecimal.valueOf(3);

		System.out.println("Interest Part");
		System.out.println("THE GETTING FLOW_CODE IS HERE " + flow_code);
		System.out.println("THE GETTING datavalues IS HERE " + datavalues);

		if ("INDEM".equals(flow_code) && "InterestBooking".equals(datavalues)) {
			String tranParticulars = "PRDEM".equals(flow_code) ? "Principle Debited" : "Interest Debited";
			System.out.println("Loan Account Interest");

			LocalDate localDate = flow_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate previousMonthSameDay = localDate.minusMonths(1);
			Date resultDate = Date.from(previousMonthSameDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(resultDate);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date bookingDate = calendar.getTime();

			long daysBetween = ChronoUnit.DAYS.between(convertToLocalDate(resultDate), convertToLocalDate(flow_date));
			long daysBetween1 = ChronoUnit.DAYS.between(convertToLocalDate(resultDate),
					convertToLocalDate(bookingDate));

			BigDecimal flowAmount = new BigDecimal(flow_amount);
			BigDecimal roundedFlowAmount = flowAmount.setScale(0, RoundingMode.HALF_UP);
			BigDecimal singledayamount = flowAmount.divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_UP);
			BigDecimal finalamount = singledayamount.multiply(BigDecimal.valueOf(daysBetween1));
			BigDecimal roundedFinalAmount = finalamount.setScale(0, RoundingMode.HALF_UP);

			String acct_num11 = "1200001220";
			TRAN_MAIN_TRM_WRK_ENTITY leasydebitval = tRAN_MAIN_TRM_WRK_REP.getaedit1(acct_num11);
			BigDecimal reveral_amt = leasydebitval.getTran_amt();
			BigDecimal creditAmount = flowAmount.subtract(reveral_amt).setScale(0, RoundingMode.HALF_UP);

			LOAN_ACT_MST_ENTITY loandetails = lOAN_ACT_MST_REPO.getLoanView(account_no);

			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId1);
			debitTrm.setAcct_num(loandetails.getId());
			debitTrm.setAcct_name(loandetails.getLoan_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setTran_amt(roundedFlowAmount);
			debitTrm.setTran_particular(loandetails.getId() + " " + tranParticulars);
			debitTrm.setTran_remarks(loandetails.getId() + " " + tranParticulars);
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setFlow_code(flow_code);
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

			Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit("4100004110");
			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId2);
			creditTrm.setAcct_num(leasydebit.getAcct_num());
			creditTrm.setAcct_name(leasydebit.getAcct_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setTran_amt(creditAmount);
			creditTrm.setTran_particular(loandetails.getId() + " " + tranParticulars);
			creditTrm.setTran_remarks(loandetails.getId() + " " + tranParticulars);
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setFlow_code(flow_code);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

			DMD_TABLE demandRecords = dMD_TABLE_REPO.getDemandData(account_no, flow_code, flow_id);
			if (demandRecords != null) {
				demandRecords.setTran_date(flow_date);
				demandRecords.setTran_id(tranId);
				demandRecords.setPart_tran_id(partTranId1);
				demandRecords.setTran_amt(new BigDecimal(flow_amount));
				demandRecords.setModify_flg("Y");
				demandRecords.setModify_user(user);
				dMD_TABLE_REPO.save(demandRecords);
				System.out.println("✅ Demand record updated successfully!");
			} else {
				System.out.println("❌ No demand record found for flow_code: " + flow_code);
			}
		} else if (flow_code.equals("INDEM") && datavalues.equals("Interest")) {

			String tranParticulars = null;

			if (flow_code.equals("PRDEM")) {
				tranParticulars = "Principle Debited";

			} else {
				tranParticulars = "Interest Debited";
			}

			System.out.println("Loan Account Interest");

			LocalDate localDate = flow_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();/* 15-04-2024 */
			LocalDate previousMonthSameDay = localDate.minusMonths(1);
			/* flow date -1 month */
			Date resultDate = Date
					.from(previousMonthSameDay.atStartOfDay(ZoneId.systemDefault()).toInstant());/* 15-03-2024 */

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(resultDate);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

			Date bookingDate = calendar.getTime();/* 30-03-2024 */

			LocalDate startDate = convertToLocalDate(resultDate);/* 15-03-2024 */
			LocalDate endDate = convertToLocalDate(flow_date);/* 15-04-2024 */
			long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);/* 30 days */

			LocalDate startDate1 = convertToLocalDate(resultDate);/* 15-03-2024 */
			System.out.println("THE REVERSAL PROCESS START DATE IS HERE 2" + startDate1);
			LocalDate endDate1 = convertToLocalDate(bookingDate);/* 30-03-2024 */
			System.out.println("THE REVERSAL PROCESS END DATE IS HERE 2" + endDate1);
			long daysBetween1 = ChronoUnit.DAYS.between(startDate1, endDate1);/* 15 days */

			// Example input value
			BigDecimal flowAmount = new BigDecimal(flow_amount); // Replace with actual flow_amount

			// Apply conditional rounding (2.49 → 2, 2.50 → 3)
			BigDecimal roundedFlowAmount = flowAmount.setScale(0, RoundingMode.HALF_UP);

			// Output results
			System.out.println("Flow Amount (Original): " + flowAmount);
			System.out.println("Flow Amount (Rounded): " + roundedFlowAmount);
			BigDecimal daysInMonthDecimal = new BigDecimal(daysBetween);
			BigDecimal singledayamount = flowAmount.divide(daysInMonthDecimal, 2,
					RoundingMode.HALF_UP);/* 6000/30=200 */
			System.out.println("SINGLE DAY AMOUNT IS " + singledayamount);
			System.out.println("DAYS IN MONTH DECIMAL IS " + daysInMonthDecimal);
			BigDecimal betweendaysDecimal = new BigDecimal(daysBetween1);
			System.out.println("DAYS BETWEEN IN MONTH DECIMAL IS " + betweendaysDecimal);
			// Multiply the values (Exact value, no rounding yet)
			BigDecimal finalamount = singledayamount.multiply(betweendaysDecimal);

			// Apply conditional rounding for further calculations (2.49 → 2, 2.50 → 3)
			BigDecimal roundedFinalAmount = finalamount.setScale(0, RoundingMode.HALF_UP);

			String acct_num11 = "1200001220";
			TRAN_MAIN_TRM_WRK_ENTITY leasydebitval = tRAN_MAIN_TRM_WRK_REP.getaedit1(acct_num11);

			/*
			 * BigDecimal reveral_amt = leasydebitval.getTran_amt();
			 * System.out.println("THE REVERSAL AMOUNT IS HERE " + reveral_amt); // Subtract
			 * using the rounded amount BigDecimal creditAmount =
			 * flowAmount.subtract(reveral_amt).setScale(0, RoundingMode.HALF_UP);
			 */
			// Output results
			/*
			 * System.out.println("Final Amount (Exact): " + finalamount); // No rounding
			 * applied System.out.println("Final Amount (Rounded for Calculation): " +
			 * roundedFinalAmount); System.out.println("Credit Amount (Rounded): " +
			 * creditAmount);
			 */
			/* TRM table entry set here */

			LOAN_ACT_MST_ENTITY loandetails = lOAN_ACT_MST_REPO.getLoanView(account_no);

			/* First Transaction - customer loan account DEBIT */
			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId1);
			debitTrm.setAcct_num(loandetails.getId());
			debitTrm.setAcct_name(loandetails.getLoan_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setAcct_crncy(loandetails.getCurrency_code());
			debitTrm.setTran_amt(new BigDecimal(flow_amount));
			debitTrm.setTran_particular(loandetails.getId() + " " + tranParticulars);
			debitTrm.setTran_remarks(loandetails.getId() + " " + tranParticulars);
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setValue_date(flow_date);
			debitTrm.setFlow_code(flow_code);
			debitTrm.setFlow_date(flow_date);
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			debitTrm.setEntry_time(flow_date);
			debitTrm.setDel_flg("N");
			tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

			/* Second Transaction - office Loan INTEREST Account CREDIT */
			/* this account already existed in COA */
			String acct_num = "4100004110";
			Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId2);
			creditTrm.setAcct_num(leasydebit.getAcct_num());
			creditTrm.setAcct_name(leasydebit.getAcct_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setAcct_crncy(leasydebit.getAcct_crncy());
			creditTrm.setTran_amt(new BigDecimal(flow_amount));
			creditTrm.setTran_particular(loandetails.getId() + " " + tranParticulars);
			creditTrm.setTran_remarks(loandetails.getId() + " " + tranParticulars);
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flow_date);
			creditTrm.setFlow_code(flow_code);
			creditTrm.setFlow_date(flow_date);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flow_date);
			creditTrm.setDel_flg("N");
			tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

			/*
			 * third Transaction - interest receivable CREDIT this account already existed
			 * in COA String acct_num1 = "1200001220"; Chart_Acc_Entity leasydebit1 =
			 * chart_Acc_Rep.getaedit(acct_num1);
			 * 
			 * TRAN_MAIN_TRM_WRK_ENTITY creditTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();
			 * 
			 * creditTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			 * creditTrm1.setTran_id(tranId); creditTrm1.setPart_tran_id(partTranId3);
			 * creditTrm1.setAcct_num(leasydebit1.getAcct_num());
			 * creditTrm1.setAcct_name(leasydebit1.getAcct_name());
			 * creditTrm1.setTran_type("TRANSFER"); creditTrm1.setPart_tran_type("Credit");
			 * creditTrm1.setAcct_crncy(leasydebit1.getAcct_crncy());
			 * creditTrm1.setTran_amt(reveral_amt);
			 * creditTrm1.setTran_particular(loandetails.getId() + " " +
			 * "Reversal of Booking"); creditTrm1.setTran_remarks(loandetails.getId() + " "
			 * + "Reversal of Booking"); creditTrm1.setTran_date(flow_date);
			 * creditTrm1.setValue_date(flow_date); creditTrm1.setFlow_code(flow_code);
			 * creditTrm1.setFlow_date(flow_date); creditTrm1.setTran_status("ENTERED");
			 * creditTrm1.setEntry_user(user); creditTrm1.setEntry_time(flow_date);
			 * creditTrm1.setDel_flg("N"); tRAN_MAIN_TRM_WRK_REP.save(creditTrm1);
			 */

			/* Update demand table interest tran details */
			DMD_TABLE demandRecords = dMD_TABLE_REPO.getDemandData(account_no, flow_code, flow_id);

			if (demandRecords != null) {
				demandRecords.setTran_date(flow_date);
				demandRecords.setTran_id(tranId);
				demandRecords.setPart_tran_id(partTranId1);
				demandRecords.setPart_tran_type("Debit");
				demandRecords.setTran_crncy(loandetails.getCurrency_code());
				demandRecords.setTran_amt(new BigDecimal(flow_amount));
				demandRecords.setModify_time(flow_date);
				demandRecords.setModify_flg("Y");
				demandRecords.setModify_user(user);

				dMD_TABLE_REPO.save(demandRecords); // Save the first update
				System.out.println("✅ Demand record updated successfully!");
			} else {
				System.out.println("❌ No demand record found for flow_code: " + flow_code);
			}

			// Update second record for flow_code1 = "PRDEM"
			String flow_code1 = "PRDEM";
			DMD_TABLE demandRecords1 = dMD_TABLE_REPO.getDemandData(account_no, flow_code1, flow_id);

			if (demandRecords1 != null) {
				String flow_codeval = demandRecords1.getFlow_code();
				System.out.println("THE GETTING FLOW CODE VALUE IS HERE: " + flow_codeval);

				demandRecords1.setModify_flg("Y");
				demandRecords1.setModify_user(user);

				dMD_TABLE_REPO.save(demandRecords1); // Save the second update
				System.out.println("✅ Demand record (PRDEM) updated successfully!");
			} else {
				System.out.println("❌ No demand record found for flow_code: PRDEM");
			}

		} else if (flow_code.equals("BCDEM - FEES")) {
			System.out.println("The fees transaction will be worked " + flow_code);

			/* Second Transaction - office Loan INTEREST Account CREDIT */
			/* this account already existed in COA */
			String acct_num = "4200004210";
			Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

			Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(account_no);

			/* First Transaction - customer loan account DEBIT */
			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId1);
			debitTrm.setAcct_num(loandetails.getLoan_accountno());
			debitTrm.setAcct_name(loandetails.getCustomer_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setAcct_crncy(loandetails.getLoan_currency());
			// Convert String to BigDecimal safely
			debitTrm.setTran_amt(new BigDecimal(flow_amount));
			debitTrm.setTran_particular(account_no + " " + "Service Fees Charges");
			debitTrm.setTran_remarks(account_no + " " + "Service Fees Charges");
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setValue_date(flow_date);
			debitTrm.setFlow_code(flow_code);
			debitTrm.setFlow_date(flow_date);
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			debitTrm.setEntry_time(flow_date);
			debitTrm.setDel_flg("N");
			tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId2);
			creditTrm.setAcct_num(leasydebit.getAcct_num());
			creditTrm.setAcct_name(leasydebit.getAcct_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setAcct_crncy(leasydebit.getAcct_crncy());
			// Convert String to BigDecimal safely
			creditTrm.setTran_amt(new BigDecimal(flow_amount));
			creditTrm.setTran_particular(account_no + " " + "Service Fees Charges");
			creditTrm.setTran_remarks(account_no + " " + "Service Fees Charges");
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flow_date);
			creditTrm.setFlow_code(flow_code);
			creditTrm.setFlow_date(flow_date);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flow_date);
			creditTrm.setDel_flg("N");
			tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

			try {
				DMD_TABLE demandRecords = new DMD_TABLE();

				// Assign values
				BigDecimal srlNo = new BigDecimal(String.valueOf(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID()));
				BigDecimal flowAmt = (flow_amount != null) ? new BigDecimal(String.valueOf(flow_amount))
						: BigDecimal.ZERO;

				demandRecords.setSrl_no(srlNo);
				demandRecords.setLoan_acid(account_no);
				demandRecords.setLoan_acct_no(account_no);
				demandRecords.setAcct_name(accountName);
				demandRecords.setFlow_id(partTranId1);
				demandRecords.setFlow_date(flow_date);
				demandRecords.setFlow_code(flow_code);
				demandRecords.setFlow_amt(flowAmt);
				demandRecords.setFlow_crncy_code(leasydebit.getAcct_crncy());
				demandRecords.setAdj_dt(flow_date);
				demandRecords.setAdj_amt(flowAmt);
				demandRecords.setTran_amt(flowAmt);
				demandRecords.setTran_date(flow_date);
				demandRecords.setTran_id(tranId);
				demandRecords.setPart_tran_type("Credit");
				demandRecords.setPart_tran_id(partTranId1);
				demandRecords.setModify_time(flow_date);
				demandRecords.setDel_flg("N");
				demandRecords.setModify_flg("Y");
				demandRecords.setModify_user(user);

				dMD_TABLE_REPO.save(demandRecords);
				System.out.println("✅ Record successfully added to the database!");

			} catch (Exception e) {
				System.err.println("❌ Error while saving demand record: " + e.getMessage());
				e.printStackTrace();
			}
		} else if (operation.equals("Fees")) {

			System.out.println("Fees A/C Collection Part");

			if (flow_code.equals("FEEDEM")) {
				System.out.println("FEEDEM A/C PROCESS WORKING");

				LOAN_ACT_MST_ENTITY loandetails = lOAN_ACT_MST_REPO.getLoanView(account_no);

				/* TRM table entry set here */

				/* First Transaction - customer loan account credit */
				BigDecimal flowAmt = (flow_amount != null) ? new BigDecimal(String.valueOf(flow_amount))
						: BigDecimal.ZERO;

				String tranParticularsvalues1 = "Fees Debited";

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm2 = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm2.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm2.setTran_id(tranId);
				creditTrm2.setPart_tran_id(partTranId1);
				creditTrm2.setAcct_num(loandetails.getId());
				creditTrm2.setAcct_name(loandetails.getLoan_name());
				creditTrm2.setTran_type("TRANSFER");
				creditTrm2.setPart_tran_type("Debit");
				creditTrm2.setAcct_crncy(loandetails.getCurrency_code());
				creditTrm2.setTran_amt(flowAmt);
				creditTrm2.setTran_particular(loandetails.getId() + " " + tranParticularsvalues1);
				creditTrm2.setTran_remarks(loandetails.getId() + " " + tranParticularsvalues1);
				creditTrm2.setTran_date(Transaction_date);
				creditTrm2.setValue_date(flow_date);
				creditTrm2.setFlow_code(flow_code);
				creditTrm2.setFlow_date(flow_date);
				creditTrm2.setTran_status("ENTERED");
				creditTrm2.setEntry_user(user);
				creditTrm2.setEntry_time(flow_date);
				creditTrm2.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm2);

				/* Second Transaction - office Loan Account Debit */
				/* this account already existed in COA */
				String acct_num = "4200004210";
				Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId2);
				debitTrm.setAcct_num(leasydebit.getAcct_num());
				debitTrm.setAcct_name(leasydebit.getAcct_name());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Credit");
				debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
				debitTrm.setTran_amt(flowAmt);
				debitTrm.setTran_particular(loandetails.getId() + " " + tranParticularsvalues1);
				debitTrm.setTran_remarks(loandetails.getId() + " " + tranParticularsvalues1);
				debitTrm.setTran_date(Transaction_date);
				debitTrm.setValue_date(flow_date);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				/*
				 * update demand table interest tran details DMD_TABLE demandRecords =
				 * dMD_TABLE_REPO.getDemandData(account_no, flow_code, flow_id);
				 * 
				 * demandRecords.setAdj_dt(flow_date); demandRecords.setAdj_amt(new
				 * BigDecimal(flow_amount)); demandRecords.setTran_amt(new
				 * BigDecimal(flow_amount)); demandRecords.setTran_date(flow_date);
				 * demandRecords.setTran_id(tranId); demandRecords.setPart_tran_type("Credit");
				 * demandRecords.setPart_tran_id(partTranId1);
				 * demandRecords.setModify_time(flow_date); demandRecords.setModify_flg("Y");
				 * demandRecords.setModify_user(user);
				 * 
				 * dMD_TABLE_REPO.save(demandRecords);
				 */

			}
		} else {

			System.out.println("Deposit Account Interest");

			String tranParticulars = null;

			if (flow_code.equals("II")) {
				tranParticulars = "Interest Credited";

			} else {
				tranParticulars = "Interest Credited";

			}

			LocalDate localDate = flow_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();/* 15-04-2024 */
			LocalDate previousMonthSameDay = localDate.minusMonths(1);
			/* flow date -1 month */
			Date resultDate = Date
					.from(previousMonthSameDay.atStartOfDay(ZoneId.systemDefault()).toInstant());/* 15-03-2024 */

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(resultDate);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

			Date bookingDate = calendar.getTime();/* 30-03-2024 */

			LocalDate startDate = convertToLocalDate(resultDate);/* 15-03-2024 */
			LocalDate endDate = convertToLocalDate(flow_date);/* 15-04-2024 */
			long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);/* 30 days */

			LocalDate startDate1 = convertToLocalDate(resultDate);/* 15-03-2024 */
			LocalDate endDate1 = convertToLocalDate(bookingDate);/* 30-03-2024 */
			long daysBetween1 = ChronoUnit.DAYS.between(startDate1, endDate1);/* 15 days */

			BigDecimal flowAmount = new BigDecimal(flow_amount);

			BigDecimal daysInMonthDecimal = new BigDecimal(daysBetween);
			BigDecimal singledayamount = flowAmount.divide(daysInMonthDecimal, 2,
					RoundingMode.HALF_UP);/* 6000/30=200 */

			BigDecimal betweendaysDecimal = new BigDecimal(daysBetween1);
			BigDecimal finalamount = singledayamount.multiply(betweendaysDecimal);/* 200*15=3000 */

			BigDecimal creditAmount = flowAmount.subtract(finalamount);/* 6000-3000=3000 */

			DepositEntity depositevalue = depositRep.getCustdataactval(account_no);

			/* TRM table entry set here */

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

			/* First Transaction - deposit customer account CREDIT */
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId1);
			creditTrm.setAcct_num(depositevalue.getDepo_actno());
			creditTrm.setAcct_name(depositevalue.getCust_name());
			creditTrm.setAcct_crncy(depositevalue.getCurrency());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setTran_amt(flowAmount);
			creditTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
			creditTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flow_date);
			creditTrm.setFlow_code(flow_code);
			creditTrm.setFlow_date(flow_date);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flow_date);
			creditTrm.setDel_flg("N");

			tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

			/* Second Transaction - office Deposit INTEREST Account DEBIT */
			/* this account already existed in COA */
			String acct_num = "6100006110";
			Chart_Acc_Entity termdeposite = chart_Acc_Rep.getaedit(acct_num);

			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId2);
			debitTrm.setAcct_num(termdeposite.getAcct_num());
			debitTrm.setAcct_name(termdeposite.getAcct_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setAcct_crncy(termdeposite.getAcct_crncy());
			debitTrm.setTran_amt(creditAmount);
			debitTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
			debitTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setValue_date(flow_date);
			debitTrm.setFlow_code(flow_code);
			debitTrm.setFlow_date(flow_date);
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			debitTrm.setEntry_time(flow_date);
			debitTrm.setDel_flg("N");

			tRAN_MAIN_TRM_WRK_REP.save(debitTrm); // Save the second transaction

			/* THIRD Transaction - INTEREST PAYABLE Account DEBIT */
			/* this account already existed in COA */
			String acct_num1 = "2200002220";
			Chart_Acc_Entity termdeposit = chart_Acc_Rep.getaedit(acct_num1);

			TRAN_MAIN_TRM_WRK_ENTITY debitTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();

			debitTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm1.setTran_id(tranId);
			debitTrm1.setPart_tran_id(partTranId3);
			debitTrm1.setAcct_num(termdeposit.getAcct_num());
			debitTrm1.setAcct_name(termdeposit.getAcct_name());
			debitTrm1.setTran_type("TRANSFER");
			debitTrm1.setPart_tran_type("Debit");
			debitTrm1.setAcct_crncy(termdeposit.getAcct_crncy());
			debitTrm1.setTran_amt(finalamount);
			debitTrm1.setTran_particular(depositevalue.getDepo_actno() + " " + "Reversal Of Booking");
			debitTrm1.setTran_remarks(depositevalue.getDepo_actno() + " " + "Reversal Of Booking");
			debitTrm1.setTran_date(Transaction_date);
			debitTrm1.setValue_date(flow_date);
			debitTrm1.setFlow_code(flow_code);
			debitTrm1.setFlow_date(flow_date);
			debitTrm1.setTran_status("ENTERED");
			debitTrm1.setEntry_user(user);
			debitTrm1.setEntry_time(flow_date);
			debitTrm1.setDel_flg("N");

			tRAN_MAIN_TRM_WRK_REP.save(debitTrm1);

			/* update demand table interest tran details */
			Td_defn_table flowRecords = td_defn_Repo.getFlowrecords(account_no, flow_code, flow_id);
			flowRecords.setTran_date(flow_date);
			flowRecords.setTran_id(tranId);
			flowRecords.setPart_tran_id(partTranId1);
			flowRecords.setTran_amt(new BigDecimal(flow_amount));
			flowRecords.setModify_user(user);
			flowRecords.setModify_time(flow_date);
			flowRecords.setModify_flg("Y");

			td_defn_Repo.save(flowRecords);

		}

		return tranId;
	}

	/* praveen - Collection */
	@GetMapping("transactionCollection")
	public String transactionCollection(@RequestParam(required = false) String flow_code,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date flow_date,
			@RequestParam(required = false) String flow_amount, @RequestParam(required = false) String tran_amount,
			@RequestParam(required = false) String flow_id, @RequestParam(required = false) String account_no,
			@RequestParam(required = false) String accountName, @RequestParam(required = false) String operation,
			@RequestParam(required = false) String encodedkey, HttpServletRequest rq) {

		System.out.println("THE GETTING TRAN AMOUNT IS HERE " + tran_amount);
		String user = (String) rq.getSession().getAttribute("USERID");

		/* tranId sequence */
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();

		System.out.println("THE GETTIGG FLOW DATE IS " + flow_date);

		System.out.println("THE GETTING ENCODED KEY IS " + encodedkey);

		BigDecimal partTranId1 = BigDecimal.valueOf(1);
		BigDecimal partTranId2 = BigDecimal.valueOf(2);
		BigDecimal partTranId3 = BigDecimal.valueOf(3);
		BigDecimal partTranId4 = BigDecimal.valueOf(4);

		if (operation.equals("CollectionCash")) {

			System.out.println("Cash Collection Part");

			if (flow_code.equals("PRDEM") || flow_code.equals("INDEM") || flow_code.equals("BCDEM - FEES")) {

				String tranParticulars = null;

				if (flow_code.equals("PRDEM")) {
					tranParticulars = "Principle Installment Recovered";

				} else {
					tranParticulars = "Interest Installment Recovered";

				}

				System.out.println("Loan Account Collection");

				Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(account_no);

				/* TRM table entry set here */

				/* First Transaction - customer loan account credit */
				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId1);
				creditTrm.setAcct_num(loandetails.getLoan_accountno());
				creditTrm.setAcct_name(loandetails.getCustomer_name());
				creditTrm.setTran_type("CASH");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(loandetails.getLoan_currency());
				creditTrm.setTran_amt(new BigDecimal(flow_amount));
				creditTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
				creditTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
				creditTrm.setTran_date(flow_date);
				creditTrm.setValue_date(flow_date);
				creditTrm.setFlow_code(flow_code);
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

				/* Second Transaction - cash on hand account Debit */
				/* this account already existed in COA */
				String acct_num = "1100001120";
				Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId2);
				debitTrm.setAcct_num(leasydebit.getAcct_num());
				debitTrm.setAcct_name(leasydebit.getAcct_name());
				debitTrm.setTran_type("CASH");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
				debitTrm.setTran_amt(new BigDecimal(flow_amount));
				debitTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
				debitTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
				debitTrm.setTran_date(flow_date);
				debitTrm.setValue_date(flow_date);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				/* update demand table interest tran details */
				DMD_TABLE demandRecords = dMD_TABLE_REPO.getDemandData(account_no, flow_code, flow_id);

				demandRecords.setAdj_dt(flow_date);
				demandRecords.setAdj_amt(new BigDecimal(flow_amount));
				demandRecords.setTran_amt(new BigDecimal(flow_amount));
				demandRecords.setTran_date(flow_date);
				demandRecords.setTran_id(tranId);
				demandRecords.setPart_tran_type("Credit");
				demandRecords.setPart_tran_id(partTranId1);
				demandRecords.setModify_time(flow_date);
				demandRecords.setModify_flg("Y");
				demandRecords.setModify_user(user);

				dMD_TABLE_REPO.save(demandRecords);

			} else {

				System.out.println("Deposit Account Collection");

				String tranParticulars = null;

				if (flow_code.equals("II")) {
					tranParticulars = "Interest Debited";

				} else {
					tranParticulars = "Interest Debited";

				}

				DepositEntity depositevalue = depositRep.getCustdataactval(account_no);

				/* TRM table entry set here */

				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				/* First Transaction - deposit customer account debit */
				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId1);
				debitTrm.setAcct_num(depositevalue.getDepo_actno());
				debitTrm.setAcct_name(depositevalue.getCust_name());
				debitTrm.setAcct_crncy(depositevalue.getCurrency());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setTran_amt(new BigDecimal(flow_amount));
				debitTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
				debitTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
				debitTrm.setTran_date(flow_date);
				debitTrm.setValue_date(flow_date);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");

				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				/* Second Transaction - office Deposit Account credit */
				/* this account already existed in COA */
				String acct_num = "1700001750";
				Chart_Acc_Entity termdeposite = chart_Acc_Rep.getaedit(acct_num);

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId2);
				creditTrm.setAcct_num(termdeposite.getAcct_num());
				creditTrm.setAcct_name(termdeposite.getAcct_name());
				creditTrm.setTran_type("TRANSFER");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(termdeposite.getAcct_crncy());
				creditTrm.setTran_amt(new BigDecimal(flow_amount));
				creditTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
				creditTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
				creditTrm.setTran_date(flow_date);
				creditTrm.setValue_date(flow_date);
				creditTrm.setFlow_code(flow_code);
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");

				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

			}

		} else if (operation.equals("CollectionRouting")) {

			System.out.println("Routing A/C Collection Part");

			if (flow_code.equals("PRDEM") || flow_code.equals("INDEM") || flow_code.equals("FEEDEM")) {

				String tranParticulars = null;

				System.out.println("Loan Account Interest");

				LOAN_ACT_MST_ENTITY loandetails = lOAN_ACT_MST_REPO.getLoanView(account_no);

				/* TRM table entry set here */

				/* First Transaction - customer loan account credit */
				System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + account_no);
				System.out.println("THE GETTING FLOW DATE IS HERE " + flow_date);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String formattedDate = formatter.format(flow_date);
				System.out.println("THE FOMTED DATE IS " + formattedDate);
				LOAN_REPAYMENT_ENTITY demandRecordsvaldatas = lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas(encodedkey,
						formattedDate);

				tranParticulars = "Principle Installment Recovered";

				String principle_flow_code = "PRDEM";
				BigDecimal principle_due_amt = demandRecordsvaldatas.getPrincipal_due();
				System.out.println("PRINCIPLE FLOW_AMOUNT " + principle_flow_code);
				System.out.println("PRINCIPLE FLOW_CODE " + principle_flow_code);

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId1);
				creditTrm.setAcct_num(loandetails.getId());
				creditTrm.setAcct_name(loandetails.getLoan_name());
				creditTrm.setTran_type("TRANSFER");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(loandetails.getCurrency_code());
				creditTrm.setTran_amt(principle_due_amt);
				creditTrm.setTran_particular(loandetails.getId() + " " + tranParticulars);
				creditTrm.setTran_remarks(loandetails.getId() + " " + tranParticulars);
				creditTrm.setTran_date(flow_date);
				creditTrm.setValue_date(flow_date);
				creditTrm.setFlow_code(principle_flow_code);
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

				/* SECOND Transaction - customer loan account credit */

				/*
				 * LOAN_REPAYMENT_ENTITY demandRecordsvaldatas1 =
				 * lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas(account_no, flow_date);
				 */

				String tranParticularsvalue = "Interest Installment Recovered";

				String intrest_flow_code = "INDEM";
				BigDecimal intrest_due_amt = demandRecordsvaldatas.getInterest_due();
				System.out.println("INTEREST FLOW_AMOUNT " + intrest_flow_code);
				System.out.println("INTEREST FLOW_CODE " + intrest_due_amt);

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm1.setTran_id(tranId);
				creditTrm1.setPart_tran_id(partTranId2);
				creditTrm1.setAcct_num(loandetails.getId());
				creditTrm1.setAcct_name(loandetails.getLoan_name());
				creditTrm1.setTran_type("TRANSFER");
				creditTrm1.setPart_tran_type("Credit");
				creditTrm1.setAcct_crncy(loandetails.getCurrency_code());
				creditTrm1.setTran_amt(intrest_due_amt);
				creditTrm1.setTran_particular(loandetails.getId() + " " + tranParticularsvalue);
				creditTrm1.setTran_remarks(loandetails.getId() + " " + tranParticularsvalue);
				creditTrm1.setTran_date(flow_date);
				creditTrm1.setValue_date(flow_date);
				creditTrm1.setFlow_code(intrest_flow_code);
				creditTrm1.setFlow_date(flow_date);
				creditTrm1.setTran_status("ENTERED");
				creditTrm1.setEntry_user(user);
				creditTrm1.setEntry_time(flow_date);
				creditTrm1.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm1);

				/* THIRD Transaction - customer loan account credit */

				/*
				 * LOAN_REPAYMENT_ENTITY demandRecordsvaldatas2 =
				 * lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas(account_no, flow_date);
				 */

				tranParticulars = "Principle Installment Recovered";

				String fees_flow_code = "FEEDEM";
				BigDecimal fees_due_amt = demandRecordsvaldatas.getFee_due();
				System.out.println("FEES FLOW_AMOUNT " + fees_flow_code);
				System.out.println("FEES FLOW_CODE " + fees_due_amt);

				String tranParticularsvalue2 = "Fees Charges Recovered";

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm2 = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm2.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm2.setTran_id(tranId);
				creditTrm2.setPart_tran_id(partTranId3);
				creditTrm2.setAcct_num(loandetails.getId());
				creditTrm2.setAcct_name(loandetails.getLoan_name());
				creditTrm2.setTran_type("TRANSFER");
				creditTrm2.setPart_tran_type("Credit");
				creditTrm2.setAcct_crncy(loandetails.getCurrency_code());
				creditTrm2.setTran_amt(fees_due_amt);
				creditTrm2.setTran_particular(loandetails.getId() + " " + tranParticularsvalue2);
				creditTrm2.setTran_remarks(loandetails.getId() + " " + tranParticularsvalue2);
				creditTrm2.setTran_date(flow_date);
				creditTrm2.setValue_date(flow_date);
				creditTrm2.setFlow_code(fees_flow_code);
				creditTrm2.setFlow_date(flow_date);
				creditTrm2.setTran_status("ENTERED");
				creditTrm2.setEntry_user(user);
				creditTrm2.setEntry_time(flow_date);
				creditTrm2.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm2);

				/* Second Transaction - office Loan Account Debit */
				/* this account already existed in COA */
				String acct_num = "2700002750";
				Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				// Get flow_amountvalues from demandRecordsvaldatas (assuming it's already a
				// BigDecimal)
				BigDecimal amount1 = (principle_due_amt != null) ? principle_due_amt : null;

				// Ensure flow_amountval and flow_amountval1 are not null before addition
				BigDecimal amount2 = (intrest_due_amt != null) ? intrest_due_amt : null;
				BigDecimal amount3 = (fees_due_amt != null) ? fees_due_amt : null;

				// Perform addition only if values are not null
				BigDecimal tran_amt = BigDecimal.ZERO;
				if (amount1 != null)
					tran_amt = tran_amt.add(amount1);
				if (amount2 != null)
					tran_amt = tran_amt.add(amount2);
				if (amount3 != null)
					tran_amt = tran_amt.add(amount3);

				// Print for debugging
				System.out.println("principle_due_amt (amount1): " + amount1);
				System.out.println("intrest_due_amt (amount2): " + amount2);
				System.out.println("fees_due_amt (amount3): " + amount3);
				System.out.println("Total tran_amt: " + tran_amt);

				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId4);
				debitTrm.setAcct_num(leasydebit.getAcct_num());
				debitTrm.setAcct_name(leasydebit.getAcct_name());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
				debitTrm.setTran_amt(tran_amt);
				debitTrm.setTran_particular(loandetails.getId() + " " + tranParticulars);
				debitTrm.setTran_remarks(loandetails.getId() + " " + tranParticulars);
				debitTrm.setTran_date(flow_date);
				debitTrm.setValue_date(flow_date);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				/*
				 * LOAN_REPAYMENT_ENTITY demandRecordsvaldatas1 =
				 * lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas(encodedkey, formattedDate);
				 * 
				 * demandRecordsvaldatas1.setPrincipal_paid(principle_due_amt);
				 * demandRecordsvaldatas1.setPrincipal_due(BigDecimal.ZERO);
				 * demandRecordsvaldatas1.setInterest_paid(intrest_due_amt);
				 * demandRecordsvaldatas1.setInterest_due(BigDecimal.ZERO);
				 * demandRecordsvaldatas1.setFee_paid(fees_due_amt);
				 * demandRecordsvaldatas1.setFee_due(BigDecimal.ZERO);
				 * demandRecordsvaldatas1.setPayment_state("PAID"); LocalDate localDate =
				 * LocalDate.now(); Date currentDate =
				 * Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				 * 
				 * demandRecordsvaldatas1.setRepaid_date(currentDate);
				 * 
				 * lOAN_REPAYMENT_REPO.save(demandRecordsvaldatas1);
				 */

			} else {

				System.out.println("Deposit Account Interest");

				String tranParticulars = null;

				if (flow_code.equals("II")) {
					tranParticulars = "Interest Debited";

				} else {
					tranParticulars = "Interest Debited";

				}

				DepositEntity depositevalue = depositRep.getCustdataactval(account_no);

				/* TRM table entry set here */

				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				/* First Transaction - deposit customer account debit */
				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId1);
				debitTrm.setAcct_num(depositevalue.getDepo_actno());
				debitTrm.setAcct_name(depositevalue.getCust_name());
				debitTrm.setAcct_crncy(depositevalue.getCurrency());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setTran_amt(new BigDecimal(flow_amount));
				debitTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
				debitTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
				debitTrm.setTran_date(flow_date);
				debitTrm.setValue_date(flow_date);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");

				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				/* Second Transaction - office Deposit Account credit */
				/* this account already existed in COA */
				String acct_num = "1700001750";
				Chart_Acc_Entity termdeposite = chart_Acc_Rep.getaedit(acct_num);

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId2);
				creditTrm.setAcct_num(termdeposite.getAcct_num());
				creditTrm.setAcct_name(termdeposite.getAcct_name());
				creditTrm.setTran_type("TRANSFER");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(termdeposite.getAcct_crncy());
				creditTrm.setTran_amt(new BigDecimal(flow_amount));
				creditTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
				creditTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
				creditTrm.setTran_date(flow_date);
				creditTrm.setValue_date(flow_date);
				creditTrm.setFlow_code(flow_code);
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");

				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

			}
		}
		return tranId;
	}

	@GetMapping("getFlowDetails")
	public List<DMD_TABLE> getFlowDetails(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date creation_Date,
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date start_date,
			@RequestParam(defaultValue = "0") double Product_value, @RequestParam String principle_frequency,
			@RequestParam(defaultValue = "0") int int_rate, @RequestParam(defaultValue = "0") int no_of_inst,
			@RequestParam(defaultValue = "0") double int_amt, @RequestParam String interestFrequency)
			throws ParseException {

		LocalDate startDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = startDate.plus(no_of_inst, ChronoUnit.MONTHS);
		Date calculatedEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		BigDecimal product = BigDecimal.valueOf(Product_value);
		BigDecimal productAmt = BigDecimal.valueOf(Product_value);
		BigDecimal intRate = BigDecimal.valueOf(int_rate);
		BigDecimal instmentAmount = BigDecimal.valueOf(int_amt);

		List<TestPrincipalCalculation> InterestAmount = interestCalculationServices.calculatePrincialPaymentNotice(
				start_date, calculatedEndDate, product, productAmt, principle_frequency, intRate, no_of_inst,
				instmentAmount, interestFrequency);

		int toltalInstallment = InterestAmount.size();

		List<DMD_TABLE> principleAndInterest = new ArrayList<>();

		int noOfInstallment = 1;
		int noOfprincipalInstallment = 1;

		if (toltalInstallment > 0) {
			for (TestPrincipalCalculation record : InterestAmount) {
				DMD_TABLE entity = new DMD_TABLE();

				entity.setFlow_id(BigDecimal.valueOf(noOfInstallment));

				entity.setFlow_frq(record.getInstallmentFrequency());
				entity.setFlow_date(record.getInstallmentDate());

				entity.setFlow_amt(record.getInterestAmount());
				entity.setFlow_code("INDEM");

				noOfInstallment++;
				principleAndInterest.add(entity);
			}

			for (TestPrincipalCalculation record : InterestAmount) {
				DMD_TABLE entity = new DMD_TABLE();

				entity.setFlow_id(BigDecimal.valueOf(noOfprincipalInstallment));

				entity.setFlow_frq(record.getInstallmentFrequency());
				entity.setFlow_date(record.getInstallmentDate());

				entity.setFlow_amt(record.getPrincipalAmount());
				entity.setFlow_code("PRDEM");

				noOfprincipalInstallment++;
				principleAndInterest.add(entity);
			}

		} else {

		}

		principleAndInterest.sort(Comparator.comparing(DMD_TABLE::getFlow_id));

		return principleAndInterest;
	}

	@GetMapping("LeaseBalance")
	public List<Object[]> LeaseBalance(
			@RequestParam("date_of_loan") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date_of_loan) {

		return dab_Repo.getLeaseBal(date_of_loan);
	}

	@GetMapping("DepositBalance")
	public List<Object[]> DepositBalance(
			@RequestParam("deposit_period") @DateTimeFormat(pattern = "dd-MM-yyyy") Date deposit_period) {

		return dab_Repo.getDepositBal(deposit_period);
	}
	/*
	 * @RequestMapping(value = "glconsolidation", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String glconsolidation(@RequestParam(required =
	 * false) String formmode, Model model, HttpServletRequest request) {
	 * 
	 * // Perform the update without expecting a return value
	 * generalLedgerWork_Rep.getupdateglwork();
	 * generalLedgerWork_Rep.updateNoAcctOpened();
	 * generalLedgerWork_Rep.updateNoAcctClosed();
	 * 
	 * return "Successful"; }
	 */

	@RequestMapping(value = "glconsolidation", method = { RequestMethod.GET, RequestMethod.POST })
	public String glconsolidation(@RequestParam(required = false) String formmode, Model model,
			HttpServletRequest request) {

		// Perform the update without expecting a return value
		generalLedgerRep.getupdateglwork();
		generalLedgerRep.updateNoAcctOpened();
		generalLedgerRep.updateNoAcctClosed();

		
		return "Successful";
	}

	@GetMapping("getAccountName")
	public List<Lease_Loan_Master_Entity> getAccountName(@RequestParam(required = false) String accountNum) {

		List<Lease_Loan_Master_Entity> acountName = lease_Loan_Master_Repo.accountNames(accountNum);
		return acountName;
	}

	@GetMapping("loanCollection")
	public String getrecordfromcoa(@RequestParam(required = false) String accountNum,
			@RequestParam(required = false) BigDecimal amount, @RequestParam(required = false) String operationType,
			HttpServletRequest req) {

		Date flowdate = (Date) req.getSession().getAttribute("TRANDATE");

		String user = (String) req.getSession().getAttribute("USERID");

		/* tranId sequence */
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		String msg = "";

		String part_tran_id = "1";
		String Acct_num = accountNum;

		List<DMD_TABLE> demand_records = dMD_TABLE_REPO.getAlldemand(accountNum, flowdate);

		if (demand_records.size() > 0) {

			/* Any Demand available only collection operation will be done */
			BigDecimal insterestAmuont = BigDecimal.ZERO;
			BigDecimal principleAmount = BigDecimal.ZERO;

			BigDecimal partTranId1 = BigDecimal.valueOf(1);
			BigDecimal partTranId2 = BigDecimal.valueOf(2);

			for (DMD_TABLE demand : demand_records) {

				if (demand.getFlow_code().equalsIgnoreCase("INDEM")) {
					insterestAmuont = insterestAmuont.add(demand.getFlow_amt());
				} else {
					principleAmount = principleAmount.add(demand.getFlow_amt());
				}
			}

			String flow_code = "RIDEM";
			Date flow_date = demand_records.get(0).getFlow_date();
			BigDecimal flow_amount = amount;
			String flow_id = demand_records.get(0).getFlow_id().toString();

			if (operationType.equals("CollectionCash")) {

				System.out.println("Cash Collection Part");

				if (flow_code.equals("PRDEM") || flow_code.equals("INDEM") || flow_code.equals("RIDEM")) {

					String tranParticulars = null;

					if (flow_code.equals("PRDEM") || flow_code.equals("RIDEM")) {
						tranParticulars = "Installment Recovered";

					} else {
						tranParticulars = "Interest Installment Recovered";

					}

					System.out.println("Loan Account Collection");

					Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(accountNum);

					/* TRM table entry set here */
					TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
					/* First Transaction - customer loan account credit */

					debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					debitTrm.setTran_id(tranId);
					debitTrm.setPart_tran_id(partTranId1);
					debitTrm.setAcct_num(loandetails.getLoan_accountno());
					debitTrm.setAcct_name(loandetails.getCustomer_name());
					debitTrm.setTran_type("CASH");
					debitTrm.setPart_tran_type("Debit");
					debitTrm.setAcct_crncy(loandetails.getLoan_currency());
					debitTrm.setTran_amt(flow_amount);
					debitTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
					debitTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
					debitTrm.setTran_date(flow_date);
					debitTrm.setValue_date(flow_date);
					debitTrm.setFlow_code("COLLECT");
					debitTrm.setFlow_date(flow_date);
					debitTrm.setTran_status("ENTERED");
					debitTrm.setEntry_user(user);
					debitTrm.setEntry_time(flow_date);
					debitTrm.setDel_flg("N");
					tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

					/* Second Transaction - cash on hand account Debit */
					/* this account already existed in COA */
					String acct_num = "1100001120";
					Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

					TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
					creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					creditTrm.setTran_id(tranId);
					creditTrm.setPart_tran_id(partTranId2);
					creditTrm.setAcct_num(leasydebit.getAcct_num());
					creditTrm.setAcct_name(leasydebit.getAcct_name());
					creditTrm.setTran_type("CASH");
					creditTrm.setPart_tran_type("Credit");
					creditTrm.setAcct_crncy(leasydebit.getAcct_crncy());
					creditTrm.setTran_amt(flow_amount);
					creditTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
					creditTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
					creditTrm.setTran_date(flow_date);
					creditTrm.setValue_date(flow_date);
					creditTrm.setFlow_code("COLLECT");
					creditTrm.setFlow_date(flow_date);
					creditTrm.setTran_status("ENTERED");
					creditTrm.setEntry_user(user);
					creditTrm.setEntry_time(flow_date);
					creditTrm.setDel_flg("N");
					tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

					/* update demand table interest tran details */
					BigDecimal principalAdjAmt = amount.subtract(insterestAmuont);
					String flowPrinCode = "PRDEM";

					DMD_TABLE demandRecords = dMD_TABLE_REPO.getDemandData(accountNum, flowPrinCode, flow_id);

					demandRecords.setAdj_dt(flow_date);
					demandRecords.setAdj_amt(principalAdjAmt);
					demandRecords.setTran_amt(principalAdjAmt);
					demandRecords.setTran_date(flow_date);
					demandRecords.setTran_id(tranId);
					demandRecords.setPart_tran_type("Credit");
					demandRecords.setPart_tran_id(partTranId1);
					demandRecords.setModify_time(flow_date);
					demandRecords.setModify_flg("Y");
					demandRecords.setModify_user(user);

					dMD_TABLE_REPO.save(demandRecords);

					String flowIntCode = "INDEM";

					DMD_TABLE demandRecordsInt = dMD_TABLE_REPO.getDemandData(accountNum, flowIntCode, flow_id);

					demandRecordsInt.setAdj_dt(flow_date);
					demandRecordsInt.setAdj_amt(insterestAmuont);

					dMD_TABLE_REPO.save(demandRecordsInt);

				} else {

					System.out.println("Deposit Account Collection");

					String tranParticulars = null;

					if (flow_code.equals("II")) {
						tranParticulars = "Interest Debited";

					} else {
						tranParticulars = "Interest Debited";

					}

					DepositEntity depositevalue = depositRep.getCustdataactval(accountNum);

					/* TRM table entry set here */

					TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

					/* First Transaction - deposit customer account debit */
					debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					debitTrm.setTran_id(tranId);
					debitTrm.setPart_tran_id(partTranId1);
					debitTrm.setAcct_num(depositevalue.getDepo_actno());
					debitTrm.setAcct_name(depositevalue.getCust_name());
					debitTrm.setAcct_crncy(depositevalue.getCurrency());
					debitTrm.setTran_type("TRANSFER");
					debitTrm.setPart_tran_type("Debit");
					debitTrm.setTran_amt(flow_amount);
					debitTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
					debitTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
					debitTrm.setTran_date(flow_date);
					debitTrm.setValue_date(flow_date);
					debitTrm.setFlow_code(flow_code);
					debitTrm.setFlow_date(flow_date);
					debitTrm.setTran_status("ENTERED");
					debitTrm.setEntry_user(user);
					debitTrm.setEntry_time(flow_date);
					debitTrm.setDel_flg("N");

					tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

					/* Second Transaction - office Deposit Account credit */
					/* this account already existed in COA */
					String acct_num = "1700001750";
					Chart_Acc_Entity termdeposite = chart_Acc_Rep.getaedit(acct_num);

					TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

					creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					creditTrm.setTran_id(tranId);
					creditTrm.setPart_tran_id(partTranId2);
					creditTrm.setAcct_num(termdeposite.getAcct_num());
					creditTrm.setAcct_name(termdeposite.getAcct_name());
					creditTrm.setTran_type("TRANSFER");
					creditTrm.setPart_tran_type("Credit");
					creditTrm.setAcct_crncy(termdeposite.getAcct_crncy());
					creditTrm.setTran_amt(flow_amount);
					creditTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
					creditTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
					creditTrm.setTran_date(flow_date);
					creditTrm.setValue_date(flow_date);
					creditTrm.setFlow_code(flow_code);
					creditTrm.setFlow_date(flow_date);
					creditTrm.setTran_status("ENTERED");
					creditTrm.setEntry_user(user);
					creditTrm.setEntry_time(flow_date);
					creditTrm.setDel_flg("N");

					tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

				}

			} else if (operationType.equals("CollectionRouting")) {

				System.out.println("Routing A/C Collection Part");

				if (flow_code.equals("PRDEM") || flow_code.equals("INDEM") || flow_code.equals("RIDEM")) {

					String tranParticulars = null;

					if (flow_code.equals("PRDEM") || flow_code.equals("RIDEM")) {
						tranParticulars = "Installment Recovered";

					} else {
						tranParticulars = "Interest Installment Recovered";

					}

					System.out.println("Loan Account Interest");

					Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(accountNum);

					/* TRM table entry set here */

					/* First Transaction - customer loan account credit */

					TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
					debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					debitTrm.setTran_id(tranId);
					debitTrm.setPart_tran_id(partTranId1);
					debitTrm.setAcct_num(loandetails.getLoan_accountno());
					debitTrm.setAcct_name(loandetails.getCustomer_name());
					debitTrm.setTran_type("CASH");
					debitTrm.setPart_tran_type("Debit");
					debitTrm.setAcct_crncy(loandetails.getLoan_currency());
					debitTrm.setTran_amt(flow_amount);
					debitTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
					debitTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
					debitTrm.setTran_date(flow_date);
					debitTrm.setValue_date(flow_date);
					debitTrm.setFlow_code("COLLECT");
					debitTrm.setFlow_date(flow_date);
					debitTrm.setTran_status("ENTERED");
					debitTrm.setEntry_user(user);
					debitTrm.setEntry_time(flow_date);
					debitTrm.setDel_flg("N");
					tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

					/* Second Transaction - office Loan Account Debit */
					/* this account already existed in COA */
					String acct_num = "2700002750";
					Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

					TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
					creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					creditTrm.setTran_id(tranId);
					creditTrm.setPart_tran_id(partTranId2);
					creditTrm.setAcct_num(leasydebit.getAcct_num());
					creditTrm.setAcct_name(leasydebit.getAcct_name());
					creditTrm.setTran_type("CASH");
					creditTrm.setPart_tran_type("Credit");
					creditTrm.setAcct_crncy(leasydebit.getAcct_crncy());
					creditTrm.setTran_amt(flow_amount);
					creditTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
					creditTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
					creditTrm.setTran_date(flow_date);
					creditTrm.setValue_date(flow_date);
					creditTrm.setFlow_code("COLLECT");
					creditTrm.setFlow_date(flow_date);
					creditTrm.setTran_status("ENTERED");
					creditTrm.setEntry_user(user);
					creditTrm.setEntry_time(flow_date);
					creditTrm.setDel_flg("N");
					tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

					/* update demand table interest tran details */
					BigDecimal principalAdjAmt = amount.subtract(insterestAmuont);
					String flowPrinCode = "PRDEM";
					DMD_TABLE demandRecords = dMD_TABLE_REPO.getDemandData(accountNum, flowPrinCode, flow_id);

					demandRecords.setAdj_dt(flow_date);
					demandRecords.setAdj_amt(principalAdjAmt);
					demandRecords.setTran_amt(principalAdjAmt);
					demandRecords.setTran_date(flow_date);
					demandRecords.setTran_id(tranId);
					demandRecords.setPart_tran_type("Credit");
					demandRecords.setPart_tran_id(partTranId1);
					demandRecords.setModify_time(flow_date);
					demandRecords.setModify_flg("Y");
					demandRecords.setModify_user(user);

					dMD_TABLE_REPO.save(demandRecords);

					String flowIntCode = "INDEM";

					DMD_TABLE demandRecordsInt = dMD_TABLE_REPO.getDemandData(accountNum, flowIntCode, flow_id);

					demandRecordsInt.setAdj_dt(flow_date);
					demandRecordsInt.setAdj_amt(insterestAmuont);

					dMD_TABLE_REPO.save(demandRecordsInt);

				} else {

					System.out.println("Deposit Account Interest");

					String tranParticulars = null;

					if (flow_code.equals("II")) {
						tranParticulars = "Interest Debited";

					} else {
						tranParticulars = "Interest Debited";

					}

					DepositEntity depositevalue = depositRep.getCustdataactval(accountNum);

					/* TRM table entry set here */

					TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

					/* First Transaction - deposit customer account debit */
					debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					debitTrm.setTran_id(tranId);
					debitTrm.setPart_tran_id(partTranId1);
					debitTrm.setAcct_num(depositevalue.getDepo_actno());
					debitTrm.setAcct_name(depositevalue.getCust_name());
					debitTrm.setAcct_crncy(depositevalue.getCurrency());
					debitTrm.setTran_type("TRANSFER");
					debitTrm.setPart_tran_type("Debit");
					debitTrm.setTran_amt(flow_amount);
					debitTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
					debitTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
					debitTrm.setTran_date(flow_date);
					debitTrm.setValue_date(flow_date);
					debitTrm.setFlow_code(flow_code);
					debitTrm.setFlow_date(flow_date);
					debitTrm.setTran_status("ENTERED");
					debitTrm.setEntry_user(user);
					debitTrm.setEntry_time(flow_date);
					debitTrm.setDel_flg("N");

					tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

					/* Second Transaction - office Deposit Account credit */
					/* this account already existed in COA */
					String acct_num = "1700001750";
					Chart_Acc_Entity termdeposite = chart_Acc_Rep.getaedit(acct_num);

					TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

					creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
					creditTrm.setTran_id(tranId);
					creditTrm.setPart_tran_id(partTranId2);
					creditTrm.setAcct_num(termdeposite.getAcct_num());
					creditTrm.setAcct_name(termdeposite.getAcct_name());
					creditTrm.setTran_type("TRANSFER");
					creditTrm.setPart_tran_type("Credit");
					creditTrm.setAcct_crncy(termdeposite.getAcct_crncy());
					creditTrm.setTran_amt(flow_amount);
					creditTrm.setTran_particular(depositevalue.getDepo_actno() + " " + tranParticulars);
					creditTrm.setTran_remarks(depositevalue.getDepo_actno() + " " + tranParticulars);
					creditTrm.setTran_date(flow_date);
					creditTrm.setValue_date(flow_date);
					creditTrm.setFlow_code(flow_code);
					creditTrm.setFlow_date(flow_date);
					creditTrm.setTran_status("ENTERED");
					creditTrm.setEntry_user(user);
					creditTrm.setEntry_time(flow_date);
					creditTrm.setDel_flg("N");

					tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

				}
			}

			msg = "Transaction Entered Successfully and TRANID : " + tranId;

		} else {
			msg = "No data available for Account Number : " + accountNum;
		}

		return msg;
	}

	@GetMapping("getAbstractionRecords")
	public List<TRAN_MAIN_TRM_WRK_ENTITY> getAbstractionRecords(@RequestParam(required = false) String operationType,
			HttpServletRequest req) {

		Date flow_date = (Date) req.getSession().getAttribute("TRANDATE");
		String user = (String) req.getSession().getAttribute("USERID");

		List<TRAN_MAIN_TRM_WRK_ENTITY> transaction = tranMainRep.getTransactionRecords(flow_date, user);

		return transaction;
	}

	@GetMapping("getLoanPosition")
	public List<DMD_TABLE> getLoanPosition(@RequestParam(required = false) String accountNum, HttpServletRequest req) {

		Date flow_date = (Date) req.getSession().getAttribute("TRANDATE");

		List<DMD_TABLE> returnRecord = dmdRepo.getloanposition(accountNum, flow_date);

		return returnRecord;
	}

	@PostMapping("/settlementCollection")
	public String settlementCollection(@RequestBody Settlement_Collection_Entity[] settlementRecords,
			HttpServletRequest req) {

		Date flowdate = (Date) req.getSession().getAttribute("TRANDATE");

		String user = (String) req.getSession().getAttribute("USERID");

		/* tranId sequence */
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		String msg = "";

		int partTranId1 = 1;
		BigDecimal totalAllocation = BigDecimal.ZERO;

		/* final save data added in allTransaction */
		List<TRAN_MAIN_TRM_WRK_ENTITY> allTransaction = new ArrayList<>();

		/* Credit legs Transaction only set here for loan account */
		for (Settlement_Collection_Entity record : settlementRecords) {

			/* from the list get account number and allocation amount */
			String accountNum = record.getAcct_num();
			BigDecimal amountPaid = record.getAllocation();

			totalAllocation = totalAllocation.add(amountPaid);

			/* get PRDEM and IRDEM Demand of account */
			List<DMD_TABLE> demand_records = dMD_TABLE_REPO.getAlldemand(accountNum, flowdate);

			if (demand_records.size() > 0) {

				/* Any Demand available only collection operation will be done */
				BigDecimal insterestAmuont = BigDecimal.ZERO;
				BigDecimal principleAmount = BigDecimal.ZERO;

				for (DMD_TABLE demand : demand_records) {

					if (demand.getFlow_code().equalsIgnoreCase("INDEM")) {
						insterestAmuont = insterestAmuont.add(demand.getFlow_amt());
					} else {
						principleAmount = principleAmount.add(demand.getFlow_amt());
					}
				}

				Date flow_date = demand_records.get(0).getFlow_date();
				BigDecimal flow_amount = amountPaid;
				String flow_id = demand_records.get(0).getFlow_id().toString();

				String tranParticulars = "Installment Recovered";

				Lease_Loan_Work_Entity loandetails = lease_Loan_Work_Repo.getLeaseAccount(accountNum);

				/* TRM table entry set here */

				/* customer loan account credit */
				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(BigDecimal.valueOf(partTranId1));
				creditTrm.setAcct_num(loandetails.getLoan_accountno());
				creditTrm.setAcct_name(loandetails.getCustomer_name());
				creditTrm.setTran_type("CASH");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(loandetails.getLoan_currency());
				creditTrm.setTran_amt(flow_amount);
				creditTrm.setTran_particular(loandetails.getLoan_accountno() + " " + tranParticulars);
				creditTrm.setTran_remarks(loandetails.getLoan_accountno() + " " + tranParticulars);
				creditTrm.setTran_date(flow_date);
				creditTrm.setValue_date(flow_date);
				creditTrm.setFlow_code("COLLECT");
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");
				allTransaction.add(creditTrm);

				/* update demand table interest tran details */
				/* principal adjustment */
				BigDecimal principalAdjAmt = amountPaid.subtract(insterestAmuont);
				String flowPrinCode = "PRDEM";

				DMD_TABLE demandRecords = dMD_TABLE_REPO.getDemandData(accountNum, flowPrinCode, flow_id);

				demandRecords.setAdj_dt(flow_date);
				demandRecords.setAdj_amt(principalAdjAmt);
				demandRecords.setTran_amt(principalAdjAmt);
				demandRecords.setTran_date(flow_date);
				demandRecords.setTran_id(tranId);
				demandRecords.setPart_tran_type("Credit");
				demandRecords.setPart_tran_id(BigDecimal.valueOf(partTranId1));
				demandRecords.setModify_time(flow_date);
				demandRecords.setModify_flg("Y");
				demandRecords.setModify_user(user);

				// dMD_TABLE_REPO.save(demandRecords);

				/* interest adjustment */
				String flowIntCode = "INDEM";

				DMD_TABLE demandRecordsInt = dMD_TABLE_REPO.getDemandData(accountNum, flowIntCode, flow_id);

				demandRecordsInt.setAdj_dt(flow_date);
				demandRecordsInt.setAdj_amt(insterestAmuont);

				// dMD_TABLE_REPO.save(demandRecordsInt);

			}

			partTranId1++;
		}

		/* Debit leg Transaction set here for Settlement account */
		BigDecimal partTranId2 = BigDecimal.valueOf(partTranId1);

		/* Final Transaction - Settlement Account Debit */
		/* this account already existed in COA */
		String acct_num = "1100001115";
		Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);

		TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

		debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm.setTran_id(tranId);
		debitTrm.setPart_tran_id(partTranId2);
		debitTrm.setAcct_num(leasydebit.getAcct_num());
		debitTrm.setAcct_name(leasydebit.getAcct_name());
		debitTrm.setTran_type("TRANSFER");
		debitTrm.setPart_tran_type("Debit");
		debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
		debitTrm.setTran_amt(totalAllocation);
		debitTrm.setTran_particular("Absa Bank Settlement");
		debitTrm.setTran_remarks("Absa Bank Settlement");
		debitTrm.setTran_date(flowdate);
		debitTrm.setValue_date(flowdate);
		debitTrm.setFlow_code("COLLECT");
		debitTrm.setFlow_date(flowdate);
		debitTrm.setTran_status("ENTERED");
		debitTrm.setEntry_user(user);
		debitTrm.setEntry_time(flowdate);
		debitTrm.setDel_flg("N");
		allTransaction.add(debitTrm);

		System.out.println(allTransaction);
		tRAN_MAIN_TRM_WRK_REP.saveAll(allTransaction);

		msg = "Transaction Entered Successfully and TRANID : " + tranId;

		return msg;
	}

	@GetMapping("getPartitionFlag")
	public String getPartitionFlag(@RequestParam(required = false) String accountNum) {

		String partitionFlag = chart_Acc_Rep.getpartitionFlag(accountNum);

		return partitionFlag;
	}

	@GetMapping("getPointingDetail")
	public String getPointingDetail(@RequestParam(required = false) String accountNum) {

		String pointingDetail = chart_Acc_Rep.getpointingDetail(accountNum);

		return pointingDetail;
	}

	@GetMapping("getTranRefDetails")
	public List<Transaction_Pointing_Table_Entity> getTranRefDetails(
			@RequestParam(required = false) String accountNum) {

		List<Transaction_Pointing_Table_Entity> tranRefRecords = transaction_Pointing_Table_Repo
				.getTranRefRecords(accountNum);

		return tranRefRecords;
	}

	@GetMapping("getInterestDetails")
	public List<Principle_and_intrest_shedule_Entity> getInterestDetails(
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date creation_Date,
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date start_date,
			@RequestParam(defaultValue = "0") double Product_value, @RequestParam String principle_frequency,
			@RequestParam(defaultValue = "0") double int_rate, @RequestParam(defaultValue = "0") int no_of_inst,
			@RequestParam(defaultValue = "0") double int_amt, @RequestParam String interestFrequency)
			throws ParseException {

		System.out.println(creation_Date);
		System.out.println(start_date);
		System.out.println(int_rate + "intrest ratrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
		LocalDate startDate = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = startDate.plus(no_of_inst, ChronoUnit.MONTHS);
		Date calculatedEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		BigDecimal product = BigDecimal.valueOf(Product_value);
		BigDecimal productAmt = BigDecimal.valueOf(Product_value);
		BigDecimal intRate = BigDecimal.valueOf(int_rate);
		BigDecimal instmentAmount = BigDecimal.valueOf(int_amt);

		List<TestPrincipalCalculation> InterestAmount = interestCalculationServices.calculatePrincialPaymentNotice(
				start_date, calculatedEndDate, product, productAmt, principle_frequency, intRate, no_of_inst,
				instmentAmount, interestFrequency);

		int toltalInstallment = InterestAmount.size();

		List<Principle_and_intrest_shedule_Entity> principleEntity = new ArrayList<>();
		int noOfInstallment = 1;

		if (toltalInstallment > 0) {
			for (TestPrincipalCalculation record : InterestAmount) {
				Principle_and_intrest_shedule_Entity entity = new Principle_and_intrest_shedule_Entity();

				entity.setLoan_amt(productAmt);
				entity.setNo_of_instalment(BigDecimal.valueOf(noOfInstallment));
				entity.setAccount_creation_date(creation_Date);
				entity.setEffective_interest_rate(intRate);
				entity.setTotal_installment(BigDecimal.valueOf(toltalInstallment));
				entity.setFrom_date(record.getInstallmentFromDate());
				entity.setInstallment_date(record.getInstallmentDate());
				// entity.setInstallment_amt(record.getInterestAmount());
				entity.setInterest_amt(record.getInterestAmount());
				entity.setPrincipal_amt(record.getPrincipalAmount());
				entity.setPrincipal_outstanding(record.getPrincipalAmountOutstanding());
				entity.setInstallment_description(record.getInstallmentDescription());
				entity.setCharges_amt(BigDecimal.ZERO);
				if (record.getInstallmentDescription().equalsIgnoreCase("Regular Installment")) {
					entity.setInstallment_amt(record.getInterestAmount().add(record.getPrincipalAmount()));
				} else {
					entity.setInstallment_amt(record.getInterestAmount());
				}

				noOfInstallment++;
				principleEntity.add(entity);
			}

		} else {

		}

		return principleEntity;
	}

	@GetMapping("getAccountNamedatas")
	public LOAN_ACT_MST_ENTITY getAccountNamedatas(@RequestParam(required = false) String accountNum) {
		System.out.println("the getting account number is " + accountNum);
		LOAN_ACT_MST_ENTITY acountName = lOAN_ACT_MST_REPO.getLoanView(accountNum);
		return acountName;
	}

	/*
	 * @PostMapping("/uploadxmldata1")
	 * 
	 * @ResponseBody public String uploadxmldata1(@RequestParam("file")
	 * MultipartFile file, HttpServletRequest req) { BigDecimal creditSum =
	 * BigDecimal.ZERO; BigDecimal debitSum = BigDecimal.ZERO; SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("dd-MM-yyyy"); LocalDate localDate =
	 * LocalDate.now(); String userId = (String)
	 * req.getSession().getAttribute("USERID");
	 * 
	 * LocalDateTime localDateTime = LocalDateTime.now(); java.util.Date utilDate =
	 * java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
	 * ; Date entryDate = new Date(utilDate.getTime()); Date date =
	 * Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	 * 
	 * List<CLIENT_MASTER_ENTITY> transactions = new ArrayList<>();
	 * 
	 * try (InputStream inputStream = file.getInputStream(); Workbook workbook = new
	 * XSSFWorkbook(inputStream)) { Sheet sheet = workbook.getSheetAt(0); for (int i
	 * = 1; i <= sheet.getLastRowNum(); i++) { Row row = sheet.getRow(i); if (row ==
	 * null) continue; // Skip null rows CLIENT_MASTER_ENTITY transaction = new
	 * CLIENT_MASTER_ENTITY();
	 * 
	 * transaction.setEncoded_key(getCellValueAsString(row.getCell(0))); // Fetch
	 * mobile number as a String String customerid = ""; Cell cell = row.getCell(1);
	 * // Get cell
	 * 
	 * if (cell != null) { if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	 * customerid = cell.getStringCellValue().trim(); } else if (cell.getCellType()
	 * == Cell.CELL_TYPE_NUMERIC) { customerid = String.format("%.0f",
	 * cell.getNumericCellValue()).trim(); // Convert without // scientific notation
	 * } }
	 * 
	 * // Set customer_id as a String transaction.setCustomer_id(customerid);
	 * 
	 * transaction.setClient_state(getCellValueAsString(row.getCell(2)));
	 * 
	 * Cell cell31 = row.getCell(3); // Column index 3 LocalDateTime
	 * creationDateTime = parseDateCell(cell31); if (creationDateTime != null) {
	 * Date creationDate =
	 * Date.from(creationDateTime.atZone(ZoneId.systemDefault()).toInstant());
	 * transaction.setCreation_date(new java.sql.Date(creationDate.getTime())); }
	 * 
	 * Cell cell41 = row.getCell(4); // Column index 4 LocalDateTime
	 * lastModifiedDateTime = parseDateCell(cell41); if (lastModifiedDateTime !=
	 * null) { Date lastModifiedDate =
	 * Date.from(lastModifiedDateTime.atZone(ZoneId.systemDefault()).toInstant());
	 * transaction.setLast_modified_date(new
	 * java.sql.Date(lastModifiedDate.getTime())); }
	 * 
	 * Cell cell51 = row.getCell(5); // Column index 5 (Activation Date)
	 * LocalDateTime activationDateTime = parseDateCell(cell51); if
	 * (activationDateTime != null) { Date activationDate =
	 * Date.from(activationDateTime.atZone(ZoneId.systemDefault()).toInstant());
	 * transaction.setActivation_date(new java.sql.Date(activationDate.getTime()));
	 * }
	 * 
	 * Cell cell61 = row.getCell(6); // Column index 6 (Approved Date) - Changed
	 * from 5 to 6 LocalDateTime approvedDateTime = parseDateCell(cell61); if
	 * (approvedDateTime != null) { Date approvedDate =
	 * Date.from(approvedDateTime.atZone(ZoneId.systemDefault()).toInstant());
	 * transaction.setApproved_date(new java.sql.Date(approvedDate.getTime())); }
	 * 
	 * transaction.setFirst_name(getCellValueAsString(row.getCell(7))); // Corrected
	 * transaction.setLast_name(getCellValueAsString(row.getCell(8))); // Corrected
	 * 
	 * transaction.setMobile_phone(getCellValueAsString(row.getCell(9)));
	 * transaction.setEmail_address(getCellValueAsString(row.getCell(10)));
	 * transaction.setPreferred_language(getCellValueAsString(row.getCell(11))); //
	 * Corrected transaction.setApproved_date(new Date());
	 * 
	 * // Get the date value from column index 12 Cell cell21 = row.getCell(12);
	 * LocalDateTime birthDateTime = parseDateCell(cell21);
	 * 
	 * if (birthDateTime != null) { // Remove the time part by converting to
	 * LocalDate LocalDate birthDate = birthDateTime.toLocalDate(); // This strips
	 * the time part
	 * 
	 * // Convert LocalDate to java.sql.Date java.sql.Date sqlBirthDate =
	 * java.sql.Date.valueOf(birthDate); // This will set only the date part
	 * 
	 * transaction.setBirth_date(sqlBirthDate); }
	 * 
	 * transaction.setGender(getCellValueAsString(row.getCell(13)));
	 * transaction.setAssigned_branch_key(getCellValueAsString(row.getCell(14)));
	 * transaction.setClient_role_key(getCellValueAsString(row.getCell(15)));
	 * 
	 * transaction.setLoan_cycle(parseBigDecimal(row.getCell(16)));
	 * transaction.setGroup_loan_cycle(parseBigDecimal(row.getCell(17)));
	 * 
	 * transaction.setAddress_line1(getCellValueAsString(row.getCell(18)));
	 * transaction.setAddress_line2(getCellValueAsString(row.getCell(19)));
	 * transaction.setAddress_line3(getCellValueAsString(row.getCell(20)));
	 * 
	 * transaction.setCity(getCellValueAsString(row.getCell(21)));
	 * transaction.setSuburb(getCellValueAsString(row.getCell(22)));
	 * transaction.setAssigned_user_key(getCellValueAsString(row.getCell(23)));
	 * 
	 * // Get the date value from column index 24 Cell cell11 = row.getCell(24);
	 * LocalDateTime asOnDateTime = parseDateCell(cell11);
	 * 
	 * if (asOnDateTime != null) { Date asOnDate =
	 * Date.from(asOnDateTime.atZone(ZoneId.systemDefault()).toInstant());
	 * transaction.setAsondate(new java.sql.Date(asOnDate.getTime())); // Convert to
	 * java.sql.Date }
	 * 
	 * transactions.add(transaction); }
	 * 
	 * cLIENT_MASTER_REPO.saveAll(transactions); // Ensure `clientMasterRepo` is
	 * autowired properly.
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return "Error: " +
	 * e.getMessage(); } return "FILE UPLOADED SUCCESSFULLY"; }
	 */
	@PostMapping("/uploadxmldata2")
	@ResponseBody
	public String uploadxmldata2(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		BigDecimal creditSum = BigDecimal.ZERO;
		BigDecimal debitSum = BigDecimal.ZERO;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		LocalDate localDate = LocalDate.now();
		String userId = (String) req.getSession().getAttribute("USERID");

		// Check if file is empty
		if (file == null || file.isEmpty()) {
			return "Error: Uploaded file is empty!";
		}

		// Validate file type (Only .xlsx)
		if (!file.getOriginalFilename().endsWith(".xlsx")) {
			return "Error: Only .xlsx files are supported!";
		}

		System.out.println("THE SECOND FUNCTION WILL BE WORKED " + userId);
		LocalDateTime localDateTime = LocalDateTime.now();
		java.util.Date utilDate = java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date entryDate = new Date(utilDate.getTime());
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<LOAN_ACT_MST_ENTITY> transactions = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue; // Skip null rows

				LOAN_ACT_MST_ENTITY transaction = new LOAN_ACT_MST_ENTITY();

				transaction.setEncoded_key(getCellValueAsString(row.getCell(0)));
				transaction.setId(getCellValueAsString(row.getCell(1)));
				transaction.setAccount_holdertype(getCellValueAsString(row.getCell(2)));
				transaction.setAccount_holderkey(getCellValueAsString(row.getCell(3)));

				// Get the creation date from column index 4
				Cell cell11 = row.getCell(4);
				LocalDateTime creationDateTime = parseDateCell(cell11);

				if (creationDateTime != null) {
					Date creationDate = Date.from(creationDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setCreation_date(new java.sql.Date(creationDate.getTime()));
				}

				// Get the approved date from column index 5
				Cell cell12 = row.getCell(5);
				LocalDateTime approvedDateTime = parseDateCell(cell12);

				if (approvedDateTime != null) {
					Date approvedDate = Date.from(approvedDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setApproved_date(new java.sql.Date(approvedDate.getTime()));
				}

				// Get the last modified date from column index 6
				Cell cell13 = row.getCell(6);
				LocalDateTime lastModifiedDateTime = parseDateCell(cell13);

				if (lastModifiedDateTime != null) {
					Date lastModifiedDate = Date.from(lastModifiedDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setLast_modified_date(new java.sql.Date(lastModifiedDate.getTime()));
				}

				// Get the closed date from column index 7
				Cell cell14 = row.getCell(7);
				LocalDateTime closedDateTime = parseDateCell(cell14);

				if (closedDateTime != null) {
					Date closedDate = Date.from(closedDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setClosed_date(new java.sql.Date(closedDate.getTime()));
				}

				// Get the last account appraisal date from column index 8
				Cell cell15 = row.getCell(8);
				LocalDateTime lastAppraisalDateTime = parseDateCell(cell15);

				if (lastAppraisalDateTime != null) {
					Date lastAppraisalDate = Date
							.from(lastAppraisalDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setLast_account_appraisaldate(new java.sql.Date(lastAppraisalDate.getTime()));
				}

				transaction.setAccount_state(getCellValueAsString(row.getCell(9)));
				transaction.setAccount_substate(getCellValueAsString(row.getCell(10)));
				transaction.setProduct_typekey(getCellValueAsString(row.getCell(11)));
				transaction.setLoan_name(getCellValueAsString(row.getCell(12)));
				transaction.setPayment_method(getCellValueAsString(row.getCell(13)));
				transaction.setAssigned_branchkey(getCellValueAsString(row.getCell(14)));
				transaction.setLoan_amount(parseBigDecimal(row.getCell(15)));
				transaction.setInterest_rate(parseBigDecimal(row.getCell(16)));
				transaction.setPenalty_rate(parseBigDecimal(row.getCell(17)));
				transaction.setAccrued_interest(parseBigDecimal(row.getCell(18)));
				transaction.setAccrued_penalty(parseBigDecimal(row.getCell(19)));
				transaction.setPrincipal_due(parseBigDecimal(row.getCell(20)));
				transaction.setPrincipal_paid(parseBigDecimal(row.getCell(21)));
				transaction.setPrincipal_balance(parseBigDecimal(row.getCell(22)));
				transaction.setInterest_due(parseBigDecimal(row.getCell(23)));
				transaction.setInterest_paid(parseBigDecimal(row.getCell(24)));
				transaction.setInterest_balance(parseBigDecimal(row.getCell(25)));

				transaction.setInterest_fromarrearsbalance(parseBigDecimal(row.getCell(26)));
				transaction.setInterest_fromarrearsdue(parseBigDecimal(row.getCell(27)));
				transaction.setInterest_fromarrearspaid(parseBigDecimal(row.getCell(28)));
				transaction.setFees_due(parseBigDecimal(row.getCell(29)));
				transaction.setFees_paid(parseBigDecimal(row.getCell(30)));
				transaction.setFees_balance(parseBigDecimal(row.getCell(31)));
				transaction.setPenalty_due(parseBigDecimal(row.getCell(32)));
				transaction.setPenalty_paid(parseBigDecimal(row.getCell(33)));
				transaction.setPenalty_balance(parseBigDecimal(row.getCell(34)));
				transaction.setApproved_date(new Date());
				transaction.setDisbursement_flg("N");
				transaction.setInterest_flg("N");
				transaction.setFees_flg("N");
				transaction.setRecovery_flg("N");
				transaction.setBooking_flg("N");

				// Get the expected disbursement date from column index 35
				Cell cell16 = row.getCell(35);
				LocalDateTime expectedDisbursementDateTime = parseDateCell(cell16);

				if (expectedDisbursementDateTime != null) {
					Date expectedDisbursementDate = Date
							.from(expectedDisbursementDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setExpected_disbursementdate(new java.sql.Date(expectedDisbursementDate.getTime()));
				}

				// Get the disbursement date from column index 36
				Cell cell17 = row.getCell(36);
				LocalDateTime disbursementDateTime = parseDateCell(cell17);

				if (disbursementDateTime != null) {
					Date disbursementDate = Date.from(disbursementDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setDisbursement_date(new java.sql.Date(disbursementDate.getTime()));
				}

				// Get the first repayment date from column index 37
				Cell cell18 = row.getCell(37);
				LocalDateTime firstRepaymentDateTime = parseDateCell(cell18);

				if (firstRepaymentDateTime != null) {
					Date firstRepaymentDate = Date
							.from(firstRepaymentDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setFirst_repaymentdate(new java.sql.Date(firstRepaymentDate.getTime()));
				}

				transaction.setGrace_period(parseBigDecimal(row.getCell(38)));
				transaction.setRepayment_installments(parseBigDecimal(row.getCell(39)));
				transaction.setRepayment_periodcount(parseBigDecimal(row.getCell(40)));
				transaction.setDays_late(parseBigDecimal(row.getCell(41)));
				transaction.setDays_inarrears(parseBigDecimal(row.getCell(42)));
				transaction.setRepayment_schedule_method(getCellValueAsString(row.getCell(43)));
				transaction.setCurrency_code(getCellValueAsString(row.getCell(44)));
				transaction.setSale_processedbyvgid(getCellValueAsString(row.getCell(45)));
				transaction.setSale_processedfor(getCellValueAsString(row.getCell(46)));
				transaction.setSale_referredby(getCellValueAsString(row.getCell(47)));
				transaction.setEmployment_status(getCellValueAsString(row.getCell(48)));
				transaction.setJob_title(getCellValueAsString(row.getCell(49)));
				transaction.setEmployer_name(getCellValueAsString(row.getCell(50)));
				transaction.setTuscore(parseBigDecimal(row.getCell(51)));
				transaction.setTuprobability(parseBigDecimal(row.getCell(52)));
				transaction.setTufullname(getCellValueAsString(row.getCell(53)));
				transaction.setTureason1(getCellValueAsString(row.getCell(54)));
				transaction.setTureason2(getCellValueAsString(row.getCell(55)));
				transaction.setTureason3(getCellValueAsString(row.getCell(56)));
				transaction.setTureason4(getCellValueAsString(row.getCell(57)));
				transaction.setDisposable_income(parseBigDecimal(row.getCell(58)));
				transaction.setManualoverride_amount(parseBigDecimal(row.getCell(59)));

				// Get the date value from column index 60
				Cell cell19 = row.getCell(60);
				LocalDateTime manualOverrideExpiryDateTime = parseDateCell(cell19);

				if (manualOverrideExpiryDateTime != null) {
					Date manualOverrideExpiryDate = Date
							.from(manualOverrideExpiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setManualoverride_expiry_date(new java.sql.Date(manualOverrideExpiryDate.getTime()));
				}

				transaction.setCpfees(parseBigDecimal(row.getCell(61)));
				transaction.setDeposit_amount(parseBigDecimal(row.getCell(62)));
				transaction.setTotal_product_price(parseBigDecimal(row.getCell(63)));
				transaction.setRetailer_name(getCellValueAsString(row.getCell(64)));
				transaction.setRetailer_branch(getCellValueAsString(row.getCell(65)));
				transaction.setVg_application_id(getCellValueAsString(row.getCell(66)));
				transaction.setContract_signed(getCellValueAsString(row.getCell(67)));

				// Get the date value from column index 68
				Cell cell20 = row.getCell(68);
				LocalDateTime dateOfFirstCallDateTime = parseDateCell(cell20);

				if (dateOfFirstCallDateTime != null) {
					Date dateOfFirstCall = Date
							.from(dateOfFirstCallDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setDate_of_first_call(new java.sql.Date(dateOfFirstCall.getTime()));
				}

				// Get the value from column index 69 as a string
				transaction.setLast_call_outcome(getCellValueAsString(row.getCell(69)));

				// Get the date value from column index 70
				Cell cell21 = row.getCell(70);
				LocalDateTime asOnDateTime = parseDateCell(cell21);

				if (asOnDateTime != null) {
					Date asOnDate = Date.from(asOnDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setAsondate(new java.sql.Date(asOnDate.getTime()));
				}

				transactions.add(transaction);
			}

			lOAN_ACT_MST_REPO.saveAll(transactions); // Ensure `clientMasterRepo` is autowired properly.

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
		return "FILE UPLOADED SUCCESSFULLY";
	}

	@PostMapping("/uploadxmldata3")
	@ResponseBody
	public String uploadxmldata3(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		BigDecimal creditSum = BigDecimal.ZERO;
		BigDecimal debitSum = BigDecimal.ZERO;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		LocalDate localDate = LocalDate.now();
		String userId = (String) req.getSession().getAttribute("USERID");

		System.out.println("THIRD FUNCTION WILL BE WORKED " + userId);
		// Check if file is empty
		if (file == null || file.isEmpty()) {
			return "Error: Uploaded file is empty!";
		}

		// Validate file type (Only .xlsx)
		if (!file.getOriginalFilename().endsWith(".xlsx")) {
			return "Error: Only .xlsx files are supported!";
		}

		LocalDateTime localDateTime = LocalDateTime.now();
		java.util.Date utilDate = java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date entryDate = new Date(utilDate.getTime());
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<LOAN_REPAYMENT_ENTITY> transactions = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue; // Skip null rows

				LOAN_REPAYMENT_ENTITY transaction = new LOAN_REPAYMENT_ENTITY();

				transaction.setEncoded_key(getCellValueAsString(row.getCell(0)));
				transaction.setParent_account_key(getCellValueAsString(row.getCell(1)));

				// Get cell from column index 2
				Cell cell1 = row.getCell(2);
				LocalDateTime dueDateTime = parseDateCell(cell1);
				if (dueDateTime != null) {
					LocalDate dueDateOnly = dueDateTime.toLocalDate(); // strip time part
					transaction.setDue_date(java.sql.Date.valueOf(dueDateOnly));
				}

				// Get cell from column index 3
				Cell cell2 = row.getCell(3);
				LocalDateTime lastPaidDateTime = parseDateCell(cell2);
				if (lastPaidDateTime != null) {
					Date lastPaidDate = Date.from(lastPaidDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setLast_paid_date(new java.sql.Date(lastPaidDate.getTime()));
				}

				// Get cell from column index 4
				Cell cell3 = row.getCell(4);
				LocalDateTime repaidDateTime = parseDateCell(cell3);
				if (repaidDateTime != null) {
					Date repaidDate = Date.from(repaidDateTime.atZone(ZoneId.systemDefault()).toInstant());
					transaction.setRepaid_date(new java.sql.Date(repaidDate.getTime()));
				}

				transaction.setPayment_state(getCellValueAsString(row.getCell(5)));
				transaction.setIs_payment_holiday(getCellValueAsString(row.getCell(6)));
				transaction.setPrincipal_exp(parseBigDecimal(row.getCell(7)));
				transaction.setPrincipal_paid(parseBigDecimal(row.getCell(8)));
				transaction.setPrincipal_due(parseBigDecimal(row.getCell(9)));
				transaction.setInterest_exp(parseBigDecimal(row.getCell(10)));
				transaction.setInterest_paid(parseBigDecimal(row.getCell(11)));
				transaction.setInterest_due(parseBigDecimal(row.getCell(12)));
				transaction.setFee_exp(parseBigDecimal(row.getCell(13)));
				transaction.setFee_paid(parseBigDecimal(row.getCell(14)));
				transaction.setFee_due(parseBigDecimal(row.getCell(15)));
				transaction.setPenalty_exp(parseBigDecimal(row.getCell(16)));
				transaction.setPenalty_paid(parseBigDecimal(row.getCell(17)));
				transaction.setPenalty_due(parseBigDecimal(row.getCell(18)));
				transaction.setDel_flg("N");

				// Get cell from column index 19 (not 24)
				Cell cell4 = row.getCell(19);

				// Parse LocalDateTime from the cell
				LocalDateTime localDateTime1 = parseDateCell(cell4);

				if (localDateTime1 != null) {
					// Convert LocalDateTime to java.util.Date
					Date dateValue4 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());

					// Convert java.util.Date to java.sql.Date for database storage
					transaction.setAsondate(new java.sql.Date(dateValue4.getTime()));
				}

				transactions.add(transaction);
			}

			lOAN_REPAYMENT_REPO.saveAll(transactions); // Ensure `clientMasterRepo` is autowired properly.

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
		return "FILE UPLOADED SUCCESSFULLY";
	}

	private LocalDateTime parseDateCell(Cell cell) {
		if (cell == null)
			return null;

		try {
			int cellType = cell.getCellType(); // Use int instead of CellType enum

			if (cellType == Cell.CELL_TYPE_NUMERIC) { // ✅ Use int constant
				if (DateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); // ✅ Convert Date to
																								// LocalDateTime
				}
			} else if (cellType == Cell.CELL_TYPE_STRING) { // ✅ Use int constant
				String dateStr = cell.getStringCellValue().trim();
				if (dateStr.isEmpty() || dateStr.equalsIgnoreCase("NULL"))
					return null;

				// Date formats to try
				String[] dateFormats = { "yyyy-MM-dd'T'HH:mm:ssXXX", // ISO 8601 with timezone
						"yyyy-MM-dd HH:mm:ss", // Common SQL Server datetime
						"MM/dd/yyyy HH:mm:ss", // US format with time
						"dd/MM/yyyy HH:mm:ss", // EU format with time
						"MM/dd/yyyy", // US format (date only)
						"dd/MM/yyyy", // EU format (date only)
						"yyyy-MM-dd" // Simple date format
				};

				for (String format : dateFormats) {
					try {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);

						if (format.contains("HH")) {
							// If format contains time, parse as LocalDateTime
							return LocalDateTime.parse(dateStr, formatter);
						} else {
							// If format contains only date, parse as LocalDate and set time to 00:00:00
							LocalDate localDate = LocalDate.parse(dateStr, formatter);
							return localDate.atStartOfDay();
						}
					} catch (Exception e) {
						System.err.println("Failed to parse date: " + dateStr + " using format: " + format);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private BigDecimal parseBigDecimal(Cell cell) {
		try {
			if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
				return BigDecimal.ZERO; // Return 0 if the cell is empty or null
			}

			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return BigDecimal.valueOf(cell.getNumericCellValue());

			case STRING:
				String value = cell.getStringCellValue().trim();
				if (!value.isEmpty() && value.matches("-?\\d+(\\.\\d+)?")) { // Validate number format
					return new BigDecimal(value);
				}
				break;

			case FORMULA:
				try {
					return BigDecimal.valueOf(cell.getNumericCellValue());
				} catch (IllegalStateException e) {
					String formulaValue = cell.getStringCellValue().trim();
					if (!formulaValue.isEmpty() && formulaValue.matches("-?\\d+(\\.\\d+)?")) {
						return new BigDecimal(formulaValue);
					}
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			System.err.println("Error parsing BigDecimal from cell at row " + cell.getRowIndex() + ", column "
					+ cell.getColumnIndex() + ": " + e.getMessage());
		}
		return BigDecimal.ZERO; // Return 0 instead of throwing an exception
	}

	@Autowired
	private CLIENT_MASTER_REPO clientMasterRepo;

	@PostMapping("/modifySubmit")
	public ResponseEntity<String> modifySubmit(@ModelAttribute CLIENT_MASTER_ENTITY customer) {
		try {
			// Fetch existing customer from the database
			Optional<CLIENT_MASTER_ENTITY> existingCustomerOpt = clientMasterRepo.findById(customer.getCustomer_id());

			CLIENT_MASTER_ENTITY existingCustomer = existingCustomerOpt.get();
//
//            // Retain existing values if form values are null
//            customer.setFirst_name(customer.getFirst_name() != null ? customer.getFirst_name() : existingCustomer.getFirst_name());
//            customer.setLast_name(customer.getLast_name() != null ? customer.getLast_name() : existingCustomer.getLast_name());
//            customer.setMobile_phone(customer.getMobile_phone() != null ? customer.getMobile_phone() : existingCustomer.getMobile_phone());
//            customer.setEmail_address(customer.getEmail_address() != null ? customer.getEmail_address() : existingCustomer.getEmail_address());
//            customer.setPreferred_language(customer.getPreferred_language() != null ? customer.getPreferred_language() : existingCustomer.getPreferred_language());
//            customer.setBirth_date(customer.getBirth_date() != null ? customer.getBirth_date() : existingCustomer.getBirth_date());
//            customer.setGender(customer.getGender() != null ? customer.getGender() : existingCustomer.getGender());
//            customer.setAssigned_branch_key(customer.getAssigned_branch_key() != null ? customer.getAssigned_branch_key() : existingCustomer.getAssigned_branch_key());
//            customer.setClient_role_key(customer.getClient_role_key() != null ? customer.getClient_role_key() : existingCustomer.getClient_role_key());
//            customer.setLoan_cycle(customer.getLoan_cycle() != null ? customer.getLoan_cycle() : existingCustomer.getLoan_cycle());
//            customer.setGroup_loan_cycle(customer.getGroup_loan_cycle() != null ? customer.getGroup_loan_cycle() : existingCustomer.getGroup_loan_cycle());
//            customer.setAddress_line1(customer.getAddress_line1() != null ? customer.getAddress_line1() : existingCustomer.getAddress_line1());
//            customer.setAddress_line2(customer.getAddress_line2() != null ? customer.getAddress_line2() : existingCustomer.getAddress_line2());
//            customer.setAddress_line3(customer.getAddress_line3() != null ? customer.getAddress_line3() : existingCustomer.getAddress_line3());
//            customer.setCity(customer.getCity() != null ? customer.getCity() : existingCustomer.getCity());
//            customer.setSuburb(customer.getSuburb() != null ? customer.getSuburb() : existingCustomer.getSuburb());
//            customer.setAssigned_user_key(customer.getAssigned_user_key() != null ? customer.getAssigned_user_key() : existingCustomer.getAssigned_user_key());
//            customer.setAsondate(customer.getAsondate() != null ? customer.getAsondate() : existingCustomer.getAsondate());
//
//            
			customer.setLast_modified_date(new Date());
			customer.setEncoded_key(
					customer.getEncoded_key() != null ? customer.getEncoded_key() : existingCustomer.getEncoded_key());
			customer.setCustomer_id(
					customer.getCustomer_id() != null ? customer.getCustomer_id() : existingCustomer.getCustomer_id());
			customer.setClient_state(customer.getClient_state() != null ? customer.getClient_state()
					: existingCustomer.getClient_state());
			customer.setCreation_date(customer.getCreation_date() != null ? customer.getCreation_date()
					: existingCustomer.getCreation_date());
			customer.setApproved_date(customer.getApproved_date() != null ? customer.getApproved_date()
					: existingCustomer.getApproved_date());
			customer.setActivation_date(customer.getActivation_date() != null ? customer.getActivation_date()
					: existingCustomer.getActivation_date());
			customer.setFirst_name(
					customer.getFirst_name() != null ? customer.getFirst_name() : existingCustomer.getFirst_name());
			customer.setLast_name(
					customer.getLast_name() != null ? customer.getLast_name() : existingCustomer.getLast_name());
			customer.setMobile_phone(customer.getMobile_phone() != null ? customer.getMobile_phone()
					: existingCustomer.getMobile_phone());
			customer.setEmail_address(customer.getEmail_address() != null ? customer.getEmail_address()
					: existingCustomer.getEmail_address());
			customer.setPreferred_language(customer.getPreferred_language() != null ? customer.getPreferred_language()
					: existingCustomer.getPreferred_language());
			customer.setBirth_date(
					customer.getBirth_date() != null ? customer.getBirth_date() : existingCustomer.getBirth_date());
			customer.setGender(customer.getGender() != null ? customer.getGender() : existingCustomer.getGender());
			customer.setAssigned_branch_key(
					customer.getAssigned_branch_key() != null ? customer.getAssigned_branch_key()
							: existingCustomer.getAssigned_branch_key());
			customer.setClient_role_key(customer.getClient_role_key() != null ? customer.getClient_role_key()
					: existingCustomer.getClient_role_key());
			customer.setLoan_cycle(
					customer.getLoan_cycle() != null ? customer.getLoan_cycle() : existingCustomer.getLoan_cycle());
			customer.setGroup_loan_cycle(customer.getGroup_loan_cycle() != null ? customer.getGroup_loan_cycle()
					: existingCustomer.getGroup_loan_cycle());
			customer.setAddress_line1(customer.getAddress_line1() != null ? customer.getAddress_line1()
					: existingCustomer.getAddress_line1());
			customer.setAddress_line2(customer.getAddress_line2() != null ? customer.getAddress_line2()
					: existingCustomer.getAddress_line2());
			customer.setAddress_line3(customer.getAddress_line3() != null ? customer.getAddress_line3()
					: existingCustomer.getAddress_line3());
			customer.setCity(customer.getCity() != null ? customer.getCity() : existingCustomer.getCity());
			customer.setSuburb(customer.getSuburb() != null ? customer.getSuburb() : existingCustomer.getSuburb());
			customer.setAssigned_user_key(customer.getAssigned_user_key() != null ? customer.getAssigned_user_key()
					: existingCustomer.getAssigned_user_key());
			customer.setAsondate(
					customer.getAsondate() != null ? customer.getAsondate() : existingCustomer.getAsondate());

//            System.out.println(existingCustomer.toString());
//            
//            System.out.println("\n\n\n\n\n\n\n");
//            System.out.println(customer.toString());

			// Save updated customer
			clientMasterRepo.save(customer);

			return ResponseEntity.ok("Customer updated and verified successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating customer: " + e.getMessage());
		}
	}

	@PostMapping("/verifyUserById")
	public ResponseEntity<String> verifyUser(@RequestParam String UserId) {
		try {
			// Fetch existing customer from the database using UserId
			Optional<CLIENT_MASTER_ENTITY> existingCustomerOpt = clientMasterRepo.findById(UserId);

			CLIENT_MASTER_ENTITY existingCustomer = existingCustomerOpt.get();
			existingCustomer.setApproved_date(new Date());

			// Save updated customer
			clientMasterRepo.save(existingCustomer);

			return ResponseEntity.ok("Customer updated and verified successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating customer: " + e.getMessage());
		}
	}

//	@GetMapping("AllApprovedCust")
//	public List<CLIENT_MASTER_ENTITY> AllApprovedCust(
//	        @RequestParam(required = false) String tran_id,
//	        @RequestParam(required = false) String part_tran_id,
//	        @RequestParam(required = false) Long limit) {
//
//	    // If limit is null or zero, return all 1000 rows
//	    if (limit == null || limit == 0) {
//	        return clientMasterRepo.getLoanActDetAll();
//	    }
//
//	    return clientMasterRepo.getLoanActDet(limit);
//	}
	
	
	@GetMapping("AllApprovedCust")
	public Map<String, Object> AllApprovedCust(
	        @RequestParam(defaultValue = "1") int page,  // current page number
	        @RequestParam(defaultValue = "200") int limit) {  // records per page
		
		System.out.println(page+":"+limit);
	    if (page < 1) page = 1;

	    Long offset = (long) ((page - 1) * limit);
	    List<CLIENT_MASTER_ENTITY> data = clientMasterRepo.getLoanActDetPaged(offset, (long) limit);
	    Long totalRecords = clientMasterRepo.getTotalCount();
	    int totalPages = (int) Math.ceil((double) totalRecords / limit);

	    Map<String, Object> response = new HashMap<>(); 
	    response.put("data", data);
	    response.put("currentPage", page);
	    response.put("totalPages", totalPages);
	    response.put("totalRecords", totalRecords);

	    return response;
	}


	@GetMapping("getAccDet")
	public List<Object[]> getAccDet(@RequestParam(required = false) String id) {
		System.out.println(id);

		System.out.println(clientMasterRepo.getAccDet(id));
		return clientMasterRepo.getAccDet(id);
	}

//	@GetMapping("NotApprovedCust")
//	public List<CLIENT_MASTER_ENTITY> NotApprovedCust() {
//		return clientMasterRepo.getLoanActFilterUnverified();
//	}
//
//	@GetMapping("ApprovedCust")
//	public List<CLIENT_MASTER_ENTITY> ApprovedCust() {
//		return clientMasterRepo.getLoanActFilterVerified();
//	}
	
	
	@GetMapping("NotApprovedCust")
	public Map<String, Object> NotApprovedCust(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "200") int limit) {

	    if (page < 1) page = 1;
	    long offset = (long) ((page - 1) * limit);

	    List<CLIENT_MASTER_ENTITY> data = clientMasterRepo.getLoanActFilterUnverifiedPaged(offset, (long) limit);
	    long totalRecords = clientMasterRepo.getUnverifiedCount();
	    int totalPages = (int) Math.ceil((double) totalRecords / limit);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", data);
	    response.put("currentPage", page);
	    response.put("totalPages", totalPages);
	    response.put("totalRecords", totalRecords);

	    return response;
	}

	@GetMapping("ApprovedCust")
	public Map<String, Object> ApprovedCust(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "200") int limit) {

	    if (page < 1) page = 1;
	    long offset = (long) ((page - 1) * limit);

	    List<CLIENT_MASTER_ENTITY> data = clientMasterRepo.getLoanActFilterVerifiedPaged(offset, (long) limit);
	    long totalRecords = clientMasterRepo.getVerifiedCount();
	    int totalPages = (int) Math.ceil((double) totalRecords / limit);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", data);
	    response.put("currentPage", page);
	    response.put("totalPages", totalPages);
	    response.put("totalRecords", totalRecords);

	    return response;
	}


	@GetMapping("loanflowDetailsvalues")
	public String loanflowDetailsvalues(@RequestParam(required = false) String accountNum) {
		String acountName = lOAN_ACT_MST_REPO.getLoanViewdatas(accountNum);
		return acountName;
	}

	@GetMapping("loanflowDetailsfees")
	public List<Map<String, Object>> loanflowDetailsfees(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date todate,
			@RequestParam(required = false) String accountNumber) {

		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsdatas(fromDate, todate, accountNumber);

		// Convert List<Object[]> to List<Map<String, Object>>
		List<Map<String, Object>> formattedRecords = new ArrayList<>();
		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			map.put("flow_date", record[0]); // Due Date
			map.put("flow_id", record[1]); // Static '1'
			map.put("flow_code", record[2]); // Static 'INDEM'
			map.put("flow_amt", record[3]); // Interest Due
			map.put("loan_acct_no", record[4]); // Loan Account ID
			map.put("acct_name", record[5]); // Loan Name
			map.put("encoded_key", record[6]); // Loan Name
			formattedRecords.add(map);
		}
		return formattedRecords;
	}

	@GetMapping("fetchacctbalance")
	public ResponseEntity<String> fetchacctbalance(@RequestParam String acctnum) {
		System.out.println("Fetching account balance for: " + acctnum);

		Object balanceObj = chart_Acc_Rep.getaccbal(acctnum);

		if (balanceObj == null) {
			return ResponseEntity.notFound().build(); // Return 404 if no balance found
		}

		String balance = balanceObj.toString(); // Convert Object to String
		return ResponseEntity.ok(balance);
	}

	@GetMapping("getloanclosetdatas")
	public List<Map<String, Object>> getloanclosetdatas(@RequestParam(required = false) String accountNumber) {

		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsvaluedatas(accountNumber);

		// Convert List<Object[]> to List<Map<String, Object>>
		List<Map<String, Object>> formattedRecords = new ArrayList<>();
		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			map.put("flow_date", record[0]); // Due Date
			map.put("flow_id", record[1]); // Flow ID
			map.put("flow_code", record[2]); // Flow Code
			map.put("flow_amt", record[3]); // Flow Amount
			map.put("loan_acct_no", record[4]); // Loan Account ID
			map.put("acct_name", record[5]); // Loan Name
			map.put("encoded_key", record[6]); // Encoded Key
			formattedRecords.add(map);
		}

		return formattedRecords; // ✅ Return formatted records instead of raw List<Object[]>
	}

	@GetMapping("loanflowDetails11")
	public Map<String, Object> loanflowDetails11(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date todate,
			@RequestParam(required = false) String accountNumber) {

		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE: " + accountNumber);

		// Fetch loan flow records
		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsvaluedats(todate, accountNumber);

		// Initialize total sum
		double totalFlowAmount = 0.0;

		// Convert List<Object[]> to List<Map<String, Object>>
		List<Map<String, Object>> formattedRecords = new ArrayList<>();

		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			map.put("flow_date", record[0]); // Due Date
			map.put("flow_id", record[1]); // Flow ID
			map.put("flow_code", record[2]); // Flow Code
			map.put("flow_amt", record[3]); // Flow Amount
			map.put("tran_amt", record[3]); // Flow Amount
			map.put("loan_acct_no", record[4]); // Loan Account ID
			map.put("acct_name", record[5]); // Loan Name
			map.put("encoded_key", record[6]); // Encoded Key

			// Sum up all flow_amt values, ensuring null check
			if (record[3] != null) {
				totalFlowAmount += ((Number) record[3]).doubleValue();
			}

			formattedRecords.add(map);
		}

		// Return total amount separately without adding it to the list
		Map<String, Object> response = new HashMap<>();
		response.put("flow_total_amt", totalFlowAmount); // Send only total amount
		response.put("loan_flows", formattedRecords); // Loan flow records

		return response;
	}

	@PostMapping("transactionCollectiondatas")
	public String transactionCollectiondatas(@RequestBody List<Map<String, String>> transactions, HttpServletRequest rq)
			throws ParseException {

		System.out.println("Received transactions: " + transactions);

		String user = (String) rq.getSession().getAttribute("USERID");
		// Get the next sequence value as String
		String seqStr = tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1(); // e.g., "1", "50", "123"

		// Convert to long (or int)
		long seq = Long.parseLong(seqStr);

		// Format with leading zeros
		String tranId = String.format("TR%05d", seq);

		System.out.println(tranId);
		// Output examples:
		// seq = 1   => TR00001
		// seq = 50  => TR00050
		// seq = 123 => TR00123

		System.out.println(tranId);
		BigDecimal partTranId = BigDecimal.valueOf(1);

		// Fetch the latest TRAN_DATE from the database
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE to Timestamp
		Timestamp transactionTimestamp;
		if (tranDateObj instanceof java.sql.Timestamp) {
			transactionTimestamp = (Timestamp) tranDateObj;
		} else {
			transactionTimestamp = new Timestamp(tranDateObj.getTime());
		}

		String encodedKey = transactions.get(0).get("encoded_key");
		String fromDate = transactions.get(0).get("from_date");
		String toDate = transactions.get(0).get("to_date");

		if (toDate == null || toDate.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid request: to_date is missing or empty");
		}

		// Convert toDate to Timestamp
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date parsedToDate = formatter.parse(toDate);
		Timestamp transactionDate = new Timestamp(parsedToDate.getTime());

		// Extract unique flow_dates and convert to Timestamp
		Set<String> uniqueFlowDates = new HashSet<>();
		for (Map<String, String> transaction : transactions) {
			uniqueFlowDates.add(transaction.get("flow_date"));
		}

		List<Timestamp> formattedFlowDates = new ArrayList<>();
		for (String flowDateKey : uniqueFlowDates) {
			if (flowDateKey != null && !flowDateKey.trim().isEmpty()) {
				Date parsedDate = formatter.parse(flowDateKey); // dd-MM-yyyy
				formattedFlowDates.add(new Timestamp(parsedDate.getTime()));
			}
		}

		// Process transactions
		if (transactions.size() == 1) {
			Map<String, String> transaction = transactions.get(0);
			processSingleTransaction(transaction, tranId, partTranId, user, transactionTimestamp, encodedKey);
		} else {
			processMultipleTransactions(transactions, tranId, partTranId, user, transactionTimestamp, encodedKey,
					formattedFlowDates);
		}

		return tranId;
	}

	/**
	 * Process a single transaction entry.
	 */
	private void processSingleTransaction(Map<String, String> transaction, String tranId, BigDecimal partTranId,
			String user, Date Transaction_date, String encodedKey) throws ParseException {

		// Parsing and extracting transaction details
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date flowDate = dateFormat.parse(transaction.get("flow_date"));

		String flowCode = transaction.get("flow_code");
		BigDecimal tranAmt = new BigDecimal(transaction.get("tran_amt").trim());
		String accountNo = transaction.get("loan_acct_no");

		// Fetch loan details
		LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

		// Fetch demand records for the given flow_date
		SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedFlowDate = sqlDateFormat.format(flowDate);

		LOAN_REPAYMENT_ENTITY demandRecordsList1 = lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas11(encodedKey,
				formattedFlowDate);

		// Current database values
		BigDecimal principleExp = demandRecordsList1.getPrincipal_exp();
		BigDecimal principlePaid = demandRecordsList1.getPrincipal_paid();
		BigDecimal principleDue = principleExp.subtract(principlePaid);

		BigDecimal interestExp = demandRecordsList1.getInterest_exp();
		BigDecimal interestPaid = demandRecordsList1.getInterest_paid();
		BigDecimal interestDue = interestExp.subtract(interestPaid);

		BigDecimal feeExp = demandRecordsList1.getFee_exp();
		BigDecimal feePaid = demandRecordsList1.getFee_paid();
		BigDecimal feeDue = feeExp.subtract(feePaid);

		TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

		// Processing payments based on flow code
		if (flowCode.equals("PRDEM")) {
			BigDecimal newPrincipalPaid = principlePaid.add(tranAmt);
			if (newPrincipalPaid.compareTo(principleExp) > 0) {
				newPrincipalPaid = principleExp;
			}

			demandRecordsList1.setPrincipal_paid(newPrincipalPaid);
			demandRecordsList1.setPrincipal_due(principleExp.subtract(newPrincipalPaid).max(BigDecimal.ZERO));

			// Update transaction remarks
			creditTrm.setTran_particular(accountNo + " " + "Principal Recovered");
			creditTrm.setTran_remarks("Principal amount recovered for loan account: " + accountNo);
		} else if (flowCode.equals("INDEM")) {
			BigDecimal newInterestPaid = interestPaid.add(tranAmt);
			if (newInterestPaid.compareTo(interestExp) > 0) {
				newInterestPaid = interestExp;
			}

			demandRecordsList1.setInterest_paid(newInterestPaid);
			demandRecordsList1.setInterest_due(interestExp.subtract(newInterestPaid).max(BigDecimal.ZERO));

			// Update transaction remarks
			creditTrm.setTran_particular(accountNo + " " + "Interest Recovered");
			creditTrm.setTran_remarks("Interest amount recovered for loan account: " + accountNo);
		} else if (flowCode.equals("FEEDEM")) {
			BigDecimal newFeePaid = feePaid.add(tranAmt);
			if (newFeePaid.compareTo(feeExp) > 0) {
				newFeePaid = feeExp;
			}

			demandRecordsList1.setFee_paid(newFeePaid);
			demandRecordsList1.setFee_due(feeExp.subtract(newFeePaid).max(BigDecimal.ZERO));

			// Update transaction remarks
			creditTrm.setTran_particular(accountNo + " " + "Fees Recovered");
			creditTrm.setTran_remarks("Fee amount recovered for loan account: " + accountNo);
		}

		// Save updated repayment details
		lOAN_REPAYMENT_REPO.save(demandRecordsList1);

		// Create credit transaction entry

		creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		creditTrm.setTran_id(tranId);
		creditTrm.setPart_tran_id(partTranId);
		creditTrm.setAcct_num(loanDetails.getId());
		creditTrm.setAcct_name(loanDetails.getLoan_name());
		creditTrm.setTran_type("TRANSFER");
		creditTrm.setPart_tran_type("Credit");
		creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
		creditTrm.setTran_amt(tranAmt);
		creditTrm.setTran_date(Transaction_date);
		creditTrm.setValue_date(flowDate);
		creditTrm.setFlow_code(flowCode);
		creditTrm.setFlow_date(flowDate);
		creditTrm.setTran_status("ENTERED");
		creditTrm.setEntry_user(user);
		creditTrm.setEntry_time(flowDate);
		creditTrm.setDel_flg("N");

		tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

		// Office Loan Account Debit (second transaction)
		String acct_num = "2700002750";
		Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);
		LOAN_ACT_MST_ENTITY loanDetails1 = lOAN_ACT_MST_REPO.getLoanView(accountNo);

		TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();

		debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm.setTran_id(tranId);
		debitTrm.setPart_tran_id(partTranId); // Assign the next sequential partTranId
		debitTrm.setAcct_num(leasydebit.getAcct_num());
		debitTrm.setAcct_name(leasydebit.getAcct_name());
		debitTrm.setTran_type("TRANSFER");
		debitTrm.setPart_tran_type("Debit");
		debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
		debitTrm.setTran_amt(tranAmt);
		debitTrm.setTran_particular(loanDetails1.getId() + " " + "Recovery Amount");
		debitTrm.setTran_remarks(loanDetails1.getId() + " " + "Recovery Amount");
		debitTrm.setTran_date(Transaction_date);
		debitTrm.setValue_date(flowDate);
		debitTrm.setFlow_code("RECOVERY");
		debitTrm.setFlow_date(flowDate);
		debitTrm.setTran_status("ENTERED");
		debitTrm.setEntry_user(user);
		debitTrm.setEntry_time(flowDate);
		debitTrm.setDel_flg("N");

		tRAN_MAIN_TRM_WRK_REP.save(debitTrm);
	}

	/**
	 * Process multiple transactions efficiently in bulk.
	 */
	private void processMultipleTransactions(List<Map<String, String>> transactions, String tranId,
	        BigDecimal partTranId, String user, Date transactionDate, String encodedKey,
	        List<Timestamp> formattedFlowDates) throws ParseException {

	    List<TRAN_MAIN_TRM_WRK_ENTITY> transactionList = new ArrayList<>();

	    // Initialize Maps for total amounts per flow_date
	    Map<String, BigDecimal> totalPrdem = new HashMap<>();
	    Map<String, BigDecimal> totalIndem = new HashMap<>();
	    Map<String, BigDecimal> totalFeedem = new HashMap<>();
	    Map<String, BigDecimal> totalPendem = new HashMap<>();

	    List<LOAN_REPAYMENT_ENTITY> demandRecordsList = Optional
	            .ofNullable(lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas1(encodedKey, formattedFlowDates))
	            .orElse(Collections.emptyList());

	    for (Map<String, String> transaction : transactions) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	        Date flowDate = dateFormat.parse(transaction.get("flow_date"));
	        String flowCode = transaction.get("flow_code");
	        BigDecimal tranAmt = new BigDecimal(transaction.get("tran_amt").trim());

	        if (tranAmt.compareTo(BigDecimal.ZERO) == 0) continue;

	        String accountNo = transaction.get("loan_acct_no");
	        String flowDateKey = transaction.get("flow_date");

	        LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

	        // --- CREDIT ENTRY ---
	        TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
	        creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
	        creditTrm.setTran_id(tranId);
	        creditTrm.setPart_tran_id(partTranId);
	        creditTrm.setAcct_num(loanDetails.getId());
	        creditTrm.setAcct_name(loanDetails.getLoan_name());
	        creditTrm.setTran_type("TRANSFER");
	        creditTrm.setPart_tran_type("Credit");
	        creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
	        creditTrm.setTran_amt(tranAmt);
	        creditTrm.setTran_date(transactionDate);
	        creditTrm.setValue_date(flowDate);
	        creditTrm.setFlow_code(flowCode);
	        creditTrm.setFlow_date(flowDate);
	        creditTrm.setTran_status("ENTERED");
	        creditTrm.setEntry_user(user);
	        creditTrm.setModify_user(user);
	        creditTrm.setEntry_time(flowDate);
	        creditTrm.setModify_time(flowDate);
	        creditTrm.setDel_flg("N");

	        switch (flowCode) {
	            case "PRDEM":
	                totalPrdem.merge(flowDateKey, tranAmt, BigDecimal::add);
	                creditTrm.setTran_particular(loanDetails.getId() + " Principal Recovery");
	                creditTrm.setTran_remarks("Principal amount recovered on " + flowDateKey);
	                break;
	            case "INDEM":
	                totalIndem.merge(flowDateKey, tranAmt, BigDecimal::add);
	                creditTrm.setTran_particular(loanDetails.getId() + " Interest Recovery");
	                creditTrm.setTran_remarks("Interest amount recovered on " + flowDateKey);
	                break;
	            case "FEEDEM":
	                totalFeedem.merge(flowDateKey, tranAmt, BigDecimal::add);
	                creditTrm.setTran_particular(loanDetails.getId() + " Fees Recovery");
	                creditTrm.setTran_remarks("Fees amount recovered on " + flowDateKey);
	                break;
	            case "PENDEM":
	                totalPendem.merge(flowDateKey, tranAmt, BigDecimal::add);
	                creditTrm.setTran_particular(loanDetails.getId() + " Penalty Recovery");
	                creditTrm.setTran_remarks("Penalty amount recovered on " + flowDateKey);
	                break;
	            default:
	                creditTrm.setTran_particular("Unknown Transaction");
	                creditTrm.setTran_remarks("Unrecognized transaction type for " + flowDateKey);
	        }

	        transactionList.add(creditTrm);

	        // --- DEBIT ENTRY FOR THE SAME TRANSACTION ---
	        String debitAcctNum = "2700002750"; // Example debit account
	        Chart_Acc_Entity debitAccount = chart_Acc_Rep.getaedit(debitAcctNum);

	        TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
	        debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
	        debitTrm.setTran_id(tranId);
	        debitTrm.setPart_tran_id(partTranId.add(BigDecimal.ONE)); // Increment partTranId
	        debitTrm.setAcct_num(debitAccount.getAcct_num());
	        debitTrm.setAcct_name(debitAccount.getAcct_name());
	        debitTrm.setTran_type("TRANSFER");
	        debitTrm.setPart_tran_type("Debit");
	        debitTrm.setAcct_crncy(debitAccount.getAcct_crncy());
	        debitTrm.setTran_amt(tranAmt);
	        debitTrm.setTran_particular(loanDetails.getId() + " Recovery Amount");
	        debitTrm.setTran_remarks(loanDetails.getId() + " Recovery Amount");
	        debitTrm.setTran_date(transactionDate);
	        debitTrm.setValue_date(flowDate);
	        debitTrm.setFlow_code("RECOVERY");
	        debitTrm.setFlow_date(flowDate);
	        debitTrm.setTran_status("ENTERED");
	        debitTrm.setEntry_user(user);
	        debitTrm.setEntry_time(flowDate);
	        debitTrm.setModify_user(user);
	        debitTrm.setModify_time(flowDate);
	        debitTrm.setDel_flg("N");

	        transactionList.add(debitTrm);

	        // Increment partTranId for next transaction
	        partTranId = partTranId.add(BigDecimal.valueOf(2));
	    }

	    tRAN_MAIN_TRM_WRK_REP.saveAll(transactionList);
	}

	@RequestMapping(value = "FetchLoanDetails", method = RequestMethod.GET)
	@ResponseBody
	public LOAN_ACT_MST_ENTITY FetchLoanDetails(Model md, HttpServletRequest rq,
			@RequestParam(required = false) String id) {

		LOAN_ACT_MST_ENTITY loandetails = lOAN_ACT_MST_REPO.getLoanView(id);

		return loandetails;

	}

	@GetMapping("loanflowDetailsupdate")
	public Map<String, Object> loanflowDetailsupdate(@RequestParam(required = false) String id, int amount) {

		System.out.println("Amount to process: " + amount);

		// Fetch loan flow records
		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanupdateList(id);

		// Convert List<Object[]> to List<Map<String, Object>>
		List<Map<String, Object>> formattedRecords = new ArrayList<>();

		// Running sum to track the cumulative flow amount
		double runningSum = 0;

		for (Object[] record : loanFlowRecords) {
			double flowAmt = ((Number) record[3]).doubleValue(); // Flow Amount as double
			double remainingAmt = amount - runningSum;

			// Check if the running sum already meets or exceeds the target amount
			if (runningSum >= amount) {
				break; // Stop adding records if the condition is met
			}

			// Create map to hold current record data
			Map<String, Object> map = new HashMap<>();
			map.put("flow_date", record[0]); // Due Date
			map.put("flow_id", record[1]); // Flow ID
			map.put("flow_code", record[2]); // Flow Code

			// If the remaining amount is less than the flow amount, adjust the transaction
			// amount
			double tranAmt = (flowAmt > remainingAmt) ? remainingAmt : flowAmt;
			map.put("flow_amt", flowAmt);
			map.put("tran_amt", tranAmt); // Transaction amount to be considered

			map.put("loan_acct_no", record[4]); // Loan Account ID
			map.put("acct_name", record[5]); // Loan Name
			map.put("encoded_key", record[6]); // Encoded Key

			// Add record to the list
			formattedRecords.add(map);

			// Update the running sum with the amount added
			runningSum += tranAmt;
		}

		// Wrap the list inside a map
		Map<String, Object> result = new HashMap<>();
		result.put("loanFlowDetails", formattedRecords);

		return result;

	}

	public static BigDecimal customRound(BigDecimal value) {
		return value.setScale(0, RoundingMode.HALF_UP); // Standard rounding (0.50 and above rounds up, below rounds
														// down)
	}

	@GetMapping("getloanclosetdatas51")
	public Map<String, Object> getloanclosetdatas51(@RequestParam(required = false) String accountNumber) {
		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		double totalFlowAmount = 0.0; // Initialize total sum

		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsvaluedatas511(accountNumber);
		List<Map<String, Object>> formattedRecords = new ArrayList<>();

		// Fetch latest TRAN_DATE from BGLS_CONTROL_TABLE
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		if (tranDateObj == null) {
			throw new IllegalStateException("TRAN_DATE cannot be null.");
		}

		// Convert TRAN_DATE correctly
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Find latest due_date before tranDate and its corresponding INDEM amount
		LocalDate latestDueDateBeforeTranDate = null;
		BigDecimal indemAmount = BigDecimal.ZERO;
		BigDecimal perDayInterest = BigDecimal.ZERO;

		for (Object[] record : loanFlowRecords) {
			LocalDate dueDate = ((Date) record[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String flowCode = (String) record[2];

			if (dueDate.isBefore(tranDate) && "INDEM".equals(flowCode)) {
				BigDecimal flowAmt = convertToBigDecimal(record[3]);

				if (latestDueDateBeforeTranDate == null || dueDate.isAfter(latestDueDateBeforeTranDate)) {
					latestDueDateBeforeTranDate = dueDate;
					indemAmount = flowAmt;
				}
			}
		}

		// Calculate per-day interest
		if (latestDueDateBeforeTranDate != null) {
			YearMonth yearMonth = YearMonth.from(latestDueDateBeforeTranDate);
			int totalDaysInMonth = yearMonth.lengthOfMonth();
			perDayInterest = indemAmount.divide(BigDecimal.valueOf(totalDaysInMonth), 2, RoundingMode.HALF_UP);
		}

		System.out.println("Latest due_date before tranDate: " + latestDueDateBeforeTranDate);
		System.out.println("Monthly INDEM Amount: " + indemAmount);
		System.out.println("Per-Day Interest: " + perDayInterest);

		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			LocalDate flowDate = ((Date) record[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String flowCode = (String) record[2];
			BigDecimal flowAmt = convertToBigDecimal(record[3]);

			// Apply interest calculation **ONLY for INDEM where flow_date == tran_date**
			if ("INDEM".equals(flowCode) && flowDate.equals(tranDate) && latestDueDateBeforeTranDate != null) {
				long noOfDays = ChronoUnit.DAYS.between(latestDueDateBeforeTranDate, tranDate);
				noOfDays = Math.max(noOfDays, 1);

				BigDecimal calculatedInterest = perDayInterest.multiply(BigDecimal.valueOf(noOfDays));
				calculatedInterest = customRound(calculatedInterest);

				// Replace INDEM amount with calculated interest
				map.put("flow_amt", calculatedInterest);
				totalFlowAmount += calculatedInterest.doubleValue();

				System.out.println("Replacing INDEM amount with calculated interest: " + calculatedInterest);
			} else {
				map.put("flow_amt", flowAmt);
				totalFlowAmount += flowAmt.doubleValue();
			}

			map.put("flow_date", flowDate);
			map.put("flow_id", record[1]);
			map.put("flow_code", flowCode);
			map.put("loan_acct_no", record[4]);
			map.put("acct_name", record[5]);
			map.put("encoded_key", record[6]);

			formattedRecords.add(map);
		}

		// Return the response with total flow amount and loan flow records
		Map<String, Object> response = new HashMap<>();
		response.put("flow_total_amt", totalFlowAmount);
		response.put("loan_flows", formattedRecords);

		return response;
	}

	// Utility method for safe conversion to BigDecimal
	private BigDecimal convertToBigDecimal(Object value) {
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		} else if (value instanceof Integer) {
			return BigDecimal.valueOf((Integer) value);
		} else if (value instanceof Double) {
			return BigDecimal.valueOf((Double) value);
		}
		return BigDecimal.ZERO;
	}

	@PostMapping("transactionCollectiondatas121")
	public String transactionCollectiondatas121(@RequestBody List<Map<String, String>> transactions,
			HttpServletRequest rq) throws ParseException {
		System.out.println("Received transactions: " + transactions);

		String user = (String) rq.getSession().getAttribute("USERID");
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		BigDecimal partTranId = BigDecimal.valueOf(1);

		String encodedKey = transactions.get(0).get("encoded_key");
		System.out.println("THE GETTING VALUES ARE HERE ENCODED KEY " + encodedKey);
		String flowdates = transactions.get(0).get("flow_date");

		if (flowdates == null || flowdates.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid request: to_date is missing or empty");
		}

		// Extract unique flow_dates
		Set<String> uniqueFlowDates = new HashSet<>();
		for (Map<String, String> transaction : transactions) {
			uniqueFlowDates.add(transaction.get("flow_date"));
		}

		// Convert flow_dates to SQL format
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<String> formattedFlowDates = new ArrayList<>();
		for (String flowDateKey : uniqueFlowDates) {
			if (flowDateKey != null && !flowDateKey.trim().isEmpty()) {
				Date parsedDate = inputDateFormat.parse(flowDateKey);
				formattedFlowDates.add(sqlDateFormat.format(parsedDate));
			}
		}

		if (transactions.size() == 1) {
			processSingleTransaction1(transactions.get(0), tranId, partTranId, user, encodedKey);
		} else {
			processMultipleTransactions1(transactions, tranId, partTranId, user, encodedKey, formattedFlowDates);
		}

		return tranId;
	}

	private void processSingleTransaction1(Map<String, String> transaction, String tranId, BigDecimal partTranId,
			String user, String encodedKey) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date flowDate = dateFormat.parse(transaction.get("flow_date"));

		String flowCode = transaction.get("flow_code");
		BigDecimal flowAmt = new BigDecimal(transaction.get("flow_amt").trim());
		String accountNo = transaction.get("loan_acct_no");

		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE correctly to LocalDate
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Convert LocalDate back to java.util.Date
		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

		TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
		creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		creditTrm.setTran_id(tranId);
		creditTrm.setPart_tran_id(partTranId);
		creditTrm.setAcct_num(loanDetails.getId());
		creditTrm.setAcct_name(loanDetails.getLoan_name());
		creditTrm.setTran_type("TRANSFER");
		creditTrm.setPart_tran_type("Credit");
		creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
		creditTrm.setTran_amt(flowAmt);
		creditTrm.setTran_date(Transaction_date);
		creditTrm.setValue_date(flowDate);
		creditTrm.setFlow_code(flowCode);
		creditTrm.setFlow_date(flowDate);
		creditTrm.setTran_status("ENTERED");
		creditTrm.setEntry_user(user);
		creditTrm.setEntry_time(flowDate);
		creditTrm.setDel_flg("N");

		tRAN_MAIN_TRM_WRK_REP.save(creditTrm);
	}

	private void processMultipleTransactions1(List<Map<String, String>> transactions, String tranId,
			BigDecimal partTranId, String user, String encodedKey, List<String> formattedFlowDates)
			throws ParseException {

		List<TRAN_MAIN_TRM_WRK_ENTITY> transactionList = new ArrayList<>();

		// Initialize Maps for total amounts per flow_date
		Map<String, BigDecimal> totalPrdem = new HashMap<>();
		Map<String, BigDecimal> totalIndem = new HashMap<>();
		Map<String, BigDecimal> totalFeedem = new HashMap<>();

		// Initialize total amounts as BigDecimal (not maps)
		BigDecimal totalPrdem11 = BigDecimal.ZERO;
		BigDecimal totalIndem12 = BigDecimal.ZERO;
		BigDecimal totalFeedem13 = BigDecimal.ZERO;

		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE correctly to LocalDate
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Convert LocalDate back to java.util.Date
		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Fetch demand records once for all flow_dates
		List<LOAN_REPAYMENT_ENTITY> demandRecordsList1 = Optional
				.ofNullable(lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas21(encodedKey)).orElse(Collections.emptyList());

		for (Map<String, String> transaction : transactions) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date flowDate = dateFormat.parse(transaction.get("flow_date"));

			String flowCode = transaction.get("flow_code");
			BigDecimal flowAmt = new BigDecimal(transaction.get("flow_amt").trim()); // Replacing tran_amt with flow_amt

			// Skip processing if flow_amt is 0
			if (flowAmt.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			String accountNo = transaction.get("loan_acct_no");
			String flowDateKey = transaction.get("flow_date");

			LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId);
			creditTrm.setAcct_num(loanDetails.getId());
			creditTrm.setAcct_name(loanDetails.getLoan_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
			creditTrm.setTran_amt(flowAmt); // Replacing tran_amt with flow_amt
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flowDate);
			creditTrm.setFlow_code(flowCode);
			creditTrm.setFlow_date(flowDate);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flowDate);
			creditTrm.setDel_flg("N");

			switch (flowCode) {
			case "PRDEM":
				totalPrdem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Principal Recovery");
				creditTrm.setTran_remarks("Principal amount recovered on " + flowDateKey);
				break;
			case "INDEM":
				totalIndem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Interest Recovery");
				creditTrm.setTran_remarks("Interest amount recovered on " + flowDateKey);
				break;
			case "FEEDEM":
				totalFeedem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Fees Recovery");
				creditTrm.setTran_remarks("Fees amount recovered on " + flowDateKey);
				break;
			default:
				creditTrm.setTran_particular("Unknown Transaction");
				creditTrm.setTran_remarks("Unrecognized transaction type for " + flowDateKey);
			}

			if (Transaction_date.equals(flowDate)) {
				switch (flowCode) {
				case "PRDEM":
					totalPrdem11 = totalPrdem11.add(flowAmt);
					break;

				case "INDEM":
					totalIndem12 = totalIndem12.add(flowAmt);
					break;

				case "FEEDEM":
					totalFeedem13 = totalFeedem13.add(flowAmt);
					break;

				default:
					// Ignore other flow types
					return;
				}
			}

			transactionList.add(creditTrm);
			partTranId = partTranId.add(BigDecimal.ONE);
		}

		// Skip debit transaction if total amount is 0
		BigDecimal totalTranAmt = totalPrdem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add)
				.add(totalIndem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add))
				.add(totalFeedem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));

		if (totalTranAmt.compareTo(BigDecimal.ZERO) > 0) {
			String acct_num = "2700002750";
			Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);
			LOAN_ACT_MST_ENTITY loanDetails1 = lOAN_ACT_MST_REPO.getLoanView(transactions.get(0).get("loan_acct_no"));

			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId);
			debitTrm.setAcct_num(leasydebit.getAcct_num());
			debitTrm.setAcct_name(leasydebit.getAcct_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
			debitTrm.setTran_amt(totalTranAmt);
			debitTrm.setTran_particular(loanDetails1.getId() + " Recovery Amount");
			debitTrm.setTran_remarks(loanDetails1.getId() + " Recovery Amount");
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setValue_date(new SimpleDateFormat("dd-MM-yyyy").parse(transactions.get(0).get("flow_date")));
			debitTrm.setFlow_code("RECOVERY");
			debitTrm.setFlow_date(new SimpleDateFormat("dd-MM-yyyy").parse(transactions.get(0).get("flow_date")));
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			debitTrm.setEntry_time(new SimpleDateFormat("dd-MM-yyyy").parse(transactions.get(0).get("flow_date")));
			debitTrm.setDel_flg("N");

			tRAN_MAIN_TRM_WRK_REP.save(debitTrm);
		}

		// Process and update repayment records
		// Sort demand records by Due Date (ascending order)
		demandRecordsList1.sort(Comparator.comparing(LOAN_REPAYMENT_ENTITY::getDue_date));

		// UPDATE BEFORE TRAN_DATE UPDATE
		for (LOAN_REPAYMENT_ENTITY demandRecord : demandRecordsList1) {
			String dueDate = new SimpleDateFormat("dd-MM-yyyy").format(demandRecord.getDue_date());

			// Get expected and due amounts
			BigDecimal principalExp = demandRecord.getPrincipal_exp();
			BigDecimal interestExp = demandRecord.getInterest_exp();
			BigDecimal feeExp = demandRecord.getFee_exp();

			BigDecimal existingPrincipalPaid = demandRecord.getPrincipal_paid();
			BigDecimal existingInterestPaid = demandRecord.getInterest_paid();
			BigDecimal existingFeePaid = demandRecord.getFee_paid();

			BigDecimal principalDue = demandRecord.getPrincipal_due();
			BigDecimal interestDue = demandRecord.getInterest_due();
			BigDecimal feeDue = demandRecord.getFee_due();

			// Remaining total paid amounts (carry forward if excess)
			BigDecimal principalPaid = totalPrdem.getOrDefault(dueDate, BigDecimal.ZERO);
			BigDecimal interestPaid = totalIndem.getOrDefault(dueDate, BigDecimal.ZERO);
			BigDecimal feePaid = totalFeedem.getOrDefault(dueDate, BigDecimal.ZERO);

			System.out.println("Processing demand record for Due Date: " + dueDate);
			System.out.println(
					"Expected Amounts: Principal=" + principalExp + ", Interest=" + interestExp + ", Fee=" + feeExp);
			System.out.println("Existing Paid: Principal=" + existingPrincipalPaid + ", Interest="
					+ existingInterestPaid + ", Fee=" + existingFeePaid);
			System.out.println(
					"Newly Paid: Principal=" + principalPaid + ", Interest=" + interestPaid + ", Fee=" + feePaid);

			// **First clear Principal Due**
			if (principalDue.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal amountToPay = principalPaid.min(principalDue);
				demandRecord.setPrincipal_paid(existingPrincipalPaid.add(amountToPay));
				demandRecord.setPrincipal_due(principalDue.subtract(amountToPay));
				principalPaid = principalPaid.subtract(amountToPay);
			}

			// **Then clear Interest Due (only after Principal is cleared)**
			if (interestDue.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal amountToPay = interestPaid.min(interestDue);
				demandRecord.setInterest_paid(existingInterestPaid.add(amountToPay));
				demandRecord.setInterest_due(interestDue.subtract(amountToPay));
				interestPaid = interestPaid.subtract(amountToPay);
			}

			// **Finally clear Fee Due (only after Interest is cleared)**
			if (feeDue.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal amountToPay = feePaid.min(feeDue);
				demandRecord.setFee_paid(existingFeePaid.add(amountToPay));
				demandRecord.setFee_due(feeDue.subtract(amountToPay));
				feePaid = feePaid.subtract(amountToPay);
			}

			// **Update Payment State**
			if (demandRecord.getPrincipal_due().compareTo(BigDecimal.ZERO) == 0
					&& demandRecord.getInterest_due().compareTo(BigDecimal.ZERO) == 0
					&& demandRecord.getFee_due().compareTo(BigDecimal.ZERO) == 0) {
				demandRecord.setPayment_state("PAID");
				System.out.println("Payment State: PAID");
			} else {
				demandRecord.setPayment_state("PARTIALLY PAID");
				System.out.println("Payment State: PARTIALLY PAID");
			}

			// **Ensure Paid Amounts Are Applied to Future Dates After Transaction Date**
			if (principalPaid.compareTo(BigDecimal.ZERO) > 0 || interestPaid.compareTo(BigDecimal.ZERO) > 0
					|| feePaid.compareTo(BigDecimal.ZERO) > 0) {
				totalPrdem.put(dueDate, principalPaid);
				totalIndem.put(dueDate, interestPaid);
				totalFeedem.put(dueDate, feePaid);
			}

			// **Update Repaid Date (Regardless of Transaction Date)**
			demandRecord.setRepaid_date(Transaction_date);
			lOAN_REPAYMENT_REPO.save(demandRecord);
			System.out.println("Demand record saved.\n");
		}

		// TRANSACTION DATE UPDATE ADD NEW ROWS
		LOAN_REPAYMENT_ENTITY newRecord = new LOAN_REPAYMENT_ENTITY();

		newRecord.setEncoded_key(encodedKey); // Keep the same key
		newRecord.setParent_account_key(encodedKey);
		newRecord.setDue_date(Transaction_date); // New row should have Transaction_date
		newRecord.setRepaid_date(Transaction_date);
		newRecord.setPrincipal_exp(totalPrdem11);
		newRecord.setPrincipal_paid(totalPrdem11);
		newRecord.setPrincipal_due(BigDecimal.ZERO);
		newRecord.setInterest_exp(totalIndem12);
		newRecord.setInterest_paid(totalIndem12);
		newRecord.setInterest_due(BigDecimal.ZERO);
		newRecord.setFee_exp(totalFeedem13);
		newRecord.setFee_paid(totalFeedem13);
		newRecord.setFee_due(BigDecimal.ZERO);
		newRecord.setAsondate(Transaction_date);
		newRecord.setDel_flg("N");

		// **Save the new record**
		lOAN_REPAYMENT_REPO.save(newRecord);
		System.out.println("New demand record inserted with Due Date: " + Transaction_date + "\n");

		// PENDING REPAYMENT VALUES UPDATE FLG
		System.out.println("THE PASSED ENCODED_KEY AFTER PASSING DATE " + encodedKey);
		System.out.println("THE PASSED TRAN_DATE AFTER PASSING DATE " + tranDate);
		List<LOAN_REPAYMENT_ENTITY> recordValues = lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas31(encodedKey, tranDate);

		for (LOAN_REPAYMENT_ENTITY values : recordValues) {
			values.setDel_flg("Y");
			System.out.println("THE FL UPDATON FUNCTION WILL BE WORK " + values.getDue_date());
			lOAN_REPAYMENT_REPO.save(values);
		}
		tRAN_MAIN_TRM_WRK_REP.saveAll(transactionList);
	}

	@PostMapping("transactionCollectiondatas1211")
	public String transactionCollectiondatas1211(@RequestBody List<Map<String, String>> transactions,
			HttpServletRequest rq) throws ParseException {
		System.out.println("Received transactions: " + transactions);

		System.out.println("THE GETTING TRASACTION transactionCollectiondatas1212 VALUE IS " + transactions);

		String user = (String) rq.getSession().getAttribute("USERID");
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		BigDecimal partTranId = BigDecimal.valueOf(1);

		String encodedKey = transactions.get(0).get("encoded_key");
		System.out.println("THE GETTING VALUES ARE HERE ENCODED KEY " + encodedKey);
		String flowdates = transactions.get(0).get("flow_date");

		if (flowdates == null || flowdates.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid request: flow_date is missing or empty");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		// Fetch the latest TRAN_DATE from the database
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE to LocalDate for easy comparison
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// ✅ **Filter transactions where flow_date == tran_date**
		List<Map<String, String>> filteredTransactions = transactions.stream().filter(transaction -> {
			try {
				Date flowDate = dateFormat.parse(transaction.get("flow_date"));
				LocalDate flowDateLocal = flowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				return flowDateLocal.equals(tranDate);
			} catch (ParseException e) {
				return false;
			}
		}).collect(Collectors.toList());

		if (filteredTransactions.isEmpty()) {
			return "No matching transactions found.";
		}

		processFilteredTransactions(filteredTransactions, tranId, partTranId, user, encodedKey);

		return tranId;
	}

	private void processFilteredTransactions(List<Map<String, String>> transactions, String tranId,
			BigDecimal partTranId, String user, String encodedKey) throws ParseException {

		List<TRAN_MAIN_TRM_WRK_ENTITY> transactionList = new ArrayList<>();
		Map<String, BigDecimal> totalIndem = new HashMap<>();
		Map<String, BigDecimal> totalFeedem = new HashMap<>();
		Map<String, BigDecimal> totalPendem = new HashMap<>();

		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		System.out.println("THE GETTING TRANSACTION DATE IS " + Transaction_date);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		for (Map<String, String> transaction : transactions) {
			Date flowDate = dateFormat.parse(transaction.get("flow_date"));
			String flowDateKey = new SimpleDateFormat("yyyy-MM-dd").format(flowDate);

			String flowCode = transaction.get("flow_code");
			BigDecimal flowAmt = new BigDecimal(transaction.get("flow_amt").trim());
			String accountNo = transaction.get("loan_acct_no");

			if ("PRDEM".equals(flowCode)) {
				continue;
			}

			LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId);
			creditTrm.setAcct_num(loanDetails.getId());
			creditTrm.setAcct_name(loanDetails.getLoan_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Debit");
			creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
			creditTrm.setTran_amt(flowAmt);
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flowDate);
			creditTrm.setFlow_code(flowCode);
			creditTrm.setFlow_date(flowDate);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flowDate);
			creditTrm.setDel_flg("N");

			switch (flowCode) {
		    case "INDEM":
		        totalIndem.merge(flowDateKey, flowAmt, BigDecimal::add);
		        creditTrm.setTran_particular(loanDetails.getId() + " Interest Demand");
		        creditTrm.setTran_remarks("Interest amount recovered on " + flowDateKey);
		        break;

		    case "FEEDEM":
		        totalFeedem.merge(flowDateKey, flowAmt, BigDecimal::add);
		        creditTrm.setTran_particular(loanDetails.getId() + " Fees Demand");
		        creditTrm.setTran_remarks("Fees amount recovered on " + flowDateKey);
		        break;

		    case "PENDEM":
		        totalPendem.merge(flowDateKey, flowAmt, BigDecimal::add);
		        creditTrm.setTran_particular(loanDetails.getId() + " Penalty Demand");
		        creditTrm.setTran_remarks("Penalty amount recovered on " + flowDateKey);
		        break;
		}

			transactionList.add(creditTrm);
			partTranId = partTranId.add(BigDecimal.ONE);
		}

		String acct_num = "4100004110";
		Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);
		LOAN_ACT_MST_ENTITY loanDetails1 = lOAN_ACT_MST_REPO.getLoanView(transactions.get(0).get("loan_acct_no"));

		TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
		debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm.setTran_id(tranId);
		debitTrm.setPart_tran_id(partTranId);
		debitTrm.setAcct_num(leasydebit.getAcct_num());
		debitTrm.setAcct_name(leasydebit.getAcct_name());
		debitTrm.setTran_type("TRANSFER");
		debitTrm.setPart_tran_type("Credit");
		debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
		debitTrm.setTran_amt(totalIndem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		debitTrm.setTran_particular(loanDetails1.getId() + " Demand Amount");
		debitTrm.setTran_remarks(loanDetails1.getId() + " Demand Amount");
		debitTrm.setTran_date(Transaction_date);
		debitTrm.setValue_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm.setFlow_code("RECOVERY");
		debitTrm.setFlow_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm.setTran_status("ENTERED");
		debitTrm.setEntry_user(user);
		debitTrm.setEntry_time(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm.setDel_flg("N");
		transactionList.add(debitTrm);
		partTranId = partTranId.add(BigDecimal.ONE);

		String acct_num1 = "4200004210";
		Chart_Acc_Entity leasydebit1 = chart_Acc_Rep.getaedit(acct_num1);
		TRAN_MAIN_TRM_WRK_ENTITY debitTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();
		debitTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm1.setTran_id(tranId);
		debitTrm1.setPart_tran_id(partTranId);
		debitTrm1.setAcct_num(leasydebit1.getAcct_num());
		debitTrm1.setAcct_name(leasydebit1.getAcct_name());
		debitTrm1.setTran_type("TRANSFER");
		debitTrm1.setPart_tran_type("Credit");
		debitTrm1.setAcct_crncy(leasydebit1.getAcct_crncy());
		debitTrm1.setTran_amt(totalFeedem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		debitTrm1.setTran_particular(loanDetails1.getId() + " Demand Amount");
		debitTrm1.setTran_remarks(loanDetails1.getId() + " Demand Amount");
		debitTrm1.setTran_date(Transaction_date);
		debitTrm1.setValue_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm1.setFlow_code("RECOVERY");
		debitTrm1.setFlow_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm1.setTran_status("ENTERED");
		debitTrm1.setEntry_user(user);
		debitTrm1.setEntry_time(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm1.setDel_flg("N");
		transactionList.add(debitTrm1);

		tRAN_MAIN_TRM_WRK_REP.saveAll(transactionList);
	}

	@GetMapping("getloanclosetdatas521")
	public Map<String, Object> getloanclosetdatas521(@RequestParam(required = false) String accountNumber) {
		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		double totalFlowAmount = 0.0;
		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsvaluedatas5112(accountNumber);
		List<Map<String, Object>> formattedRecords = new ArrayList<>();

		// Fetch latest TRAN_DATE from BGLS_CONTROL_TABLE
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		if (tranDateObj == null) {
			throw new IllegalStateException("TRAN_DATE cannot be null.");
		}

		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Initialize variables for latest INDEM calculation
		LocalDate latestDueDateBeforeTranDate = null;
		BigDecimal indemAmount = BigDecimal.ZERO;
		BigDecimal perDayInterest = BigDecimal.ZERO;
		String acctName = "";
		String loanAcctNo = accountNumber;

		boolean isExistingIndemOnTranDate = false;

		// Process loan flow records
		for (Object[] record : loanFlowRecords) {
			LocalDate dueDate = ((Date) record[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String flowCode = (String) record[2];

			if (dueDate.equals(tranDate) && "INDEM".equals(flowCode)) {
				isExistingIndemOnTranDate = true;
			}

			if (dueDate.isBefore(tranDate) && "INDEM".equals(flowCode)) {
				BigDecimal flowAmt = convertToBigDecimal(record[3]);
				if (latestDueDateBeforeTranDate == null || dueDate.isAfter(latestDueDateBeforeTranDate)) {
					latestDueDateBeforeTranDate = dueDate;
					indemAmount = flowAmt;
					acctName = (String) record[5];
					loanAcctNo = (String) record[4];
				}
			}
		}

		// Calculate per-day interest with better precision
		if (latestDueDateBeforeTranDate != null) {
			int totalDaysInMonth = YearMonth.from(latestDueDateBeforeTranDate).lengthOfMonth();
			perDayInterest = indemAmount.divide(BigDecimal.valueOf(totalDaysInMonth), 4, RoundingMode.HALF_UP);
		}

		// Process and format transaction records
		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			LocalDate flowDate = ((Date) record[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String flowCode = (String) record[2];
			BigDecimal flowAmt = convertToBigDecimal(record[3]);

			map.put("flow_date", flowDate);
			map.put("flow_id", record[1]);
			map.put("flow_code", flowCode);
			map.put("loan_acct_no", record[4]);
			map.put("acct_name", record[5]);
			map.put("encoded_key", record[6]);
			map.put("flow_amt", flowAmt);
			totalFlowAmount += flowAmt.doubleValue();

			formattedRecords.add(map);
		}

		// Add interest transaction if needed
		if (!isExistingIndemOnTranDate && latestDueDateBeforeTranDate != null) {
			long noOfDays = ChronoUnit.DAYS.between(latestDueDateBeforeTranDate, tranDate);
			noOfDays = Math.max(noOfDays, 1);

			// Multiply and round off
			BigDecimal calculatedInterest = perDayInterest.multiply(BigDecimal.valueOf(noOfDays));
			calculatedInterest = customRound(calculatedInterest);

			Map<String, Object> interestRow = new HashMap<>();
			interestRow.put("flow_date", tranDate);
			interestRow.put("flow_id", "1");
			interestRow.put("flow_code", "INDEM");
			interestRow.put("loan_acct_no", loanAcctNo);
			interestRow.put("acct_name", acctName);
			interestRow.put("encoded_key", "Generated");
			interestRow.put("flow_amt", calculatedInterest);

			formattedRecords.add(interestRow);
			totalFlowAmount += calculatedInterest.doubleValue();
		}

		// Ensure the balance is exactly zero if needed
		BigDecimal finalBalance = new BigDecimal(totalFlowAmount).subtract(indemAmount).setScale(2,
				RoundingMode.HALF_UP);
		if (finalBalance.abs().compareTo(BigDecimal.ONE) < 0) {
			totalFlowAmount = totalFlowAmount - finalBalance.doubleValue();
		}

		// Prepare final response
		Map<String, Object> response = new HashMap<>();
		response.put("flow_total_amt", totalFlowAmount);
		response.put("loan_flows", formattedRecords);

		return response;
	}

	private final AtomicInteger flowIdCounter = new AtomicInteger(2); // Start from 2

	@GetMapping("getloanclosetdatas511")
	public synchronized Map<String, Object> getLoanData(@RequestParam(required = false) String accountNumber) {
		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		// Fetch latest TRAN_DATE from BGLS_CONTROL_TABLE
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		if (tranDateObj == null) {
			throw new IllegalStateException("TRAN_DATE cannot be null.");
		}

		// Convert TRAN_DATE to LocalDate
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Format as "DD-MM-YYYY"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedTranDate = tranDate.format(formatter);

		// Fetch loan account master entity data
		LOAN_ACT_MST_ENTITY loanData = lOAN_ACT_MST_REPO.getLoanView(accountNumber);

		// Create response map
		Map<String, Object> response = new HashMap<>();
		response.put("tran_date", formattedTranDate);
		response.put("loan_data", loanData);
		response.put("flow_id", flowIdCounter.getAndIncrement()); // Ensure correct sequence

		return response;
	}

	@PostMapping("transactionCollectiondatas1221")
	public String transactionCollectiondatas1221(@RequestBody List<Map<String, String>> transactions,
			HttpServletRequest rq) throws ParseException {
		System.out.println("Received transactions: " + transactions);

		System.out.println("THE GETTING TRASACTION transactionCollectiondatas1221 VALUE IS " + transactions);

		String user = (String) rq.getSession().getAttribute("USERID");
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		BigDecimal partTranId = BigDecimal.valueOf(1);

		String encodedKey = transactions.get(0).get("encoded_key");
		System.out.println("THE GETTING VALUES ARE HERE ENCODED KEY " + encodedKey);
		String flowdates = transactions.get(0).get("flow_date");

		if (flowdates == null || flowdates.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid request: flow_date is missing or empty");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		// Fetch the latest TRAN_DATE from the database
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE to LocalDate for easy comparison
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// ✅ **Filter transactions where flow_date == tran_date**
		List<Map<String, String>> filteredTransactions = transactions.stream().filter(transaction -> {
			try {
				Date flowDate = dateFormat.parse(transaction.get("flow_date"));
				LocalDate flowDateLocal = flowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				return flowDateLocal.equals(tranDate);
			} catch (ParseException e) {
				return false;
			}
		}).collect(Collectors.toList());

		if (filteredTransactions.isEmpty()) {
			return "No matching transactions found.";
		}

		processFilteredTransactions21(filteredTransactions, tranId, partTranId, user, encodedKey);

		return tranId;
	}

	private void processFilteredTransactions21(List<Map<String, String>> transactions, String tranId,
			BigDecimal partTranId, String user, String encodedKey) throws ParseException {

		List<TRAN_MAIN_TRM_WRK_ENTITY> transactionList = new ArrayList<>();
		Map<String, BigDecimal> totalIndem = new HashMap<>();
		Map<String, BigDecimal> totalFeedem = new HashMap<>();

		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		for (Map<String, String> transaction : transactions) {
			Date flowDate = dateFormat.parse(transaction.get("flow_date"));
			String flowDateKey = new SimpleDateFormat("yyyy-MM-dd").format(flowDate);

			String flowCode = transaction.get("flow_code");
			BigDecimal flowAmt = new BigDecimal(transaction.get("flow_amt").trim());
			String accountNo = transaction.get("loan_acct_no");

			if ("PRDEM".equals(flowCode)) {
				continue;
			}

			LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId);
			creditTrm.setAcct_num(loanDetails.getId());
			creditTrm.setAcct_name(loanDetails.getLoan_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Debit");
			creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
			creditTrm.setTran_amt(flowAmt);
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flowDate);
			creditTrm.setFlow_code(flowCode);
			creditTrm.setFlow_date(flowDate);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flowDate);
			creditTrm.setDel_flg("N");

			switch (flowCode) {
			case "INDEM":
				totalIndem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Interest Demand");
				creditTrm.setTran_remarks("Interest amount recovered on " + flowDateKey);
				break;
			case "FEEDEM":
				totalFeedem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Fees Demand");
				creditTrm.setTran_remarks("Fees amount recovered on " + flowDateKey);
				break;
			}

			transactionList.add(creditTrm);
			partTranId = partTranId.add(BigDecimal.ONE);
		}

		String acct_num = "4100004110";
		Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);
		LOAN_ACT_MST_ENTITY loanDetails1 = lOAN_ACT_MST_REPO.getLoanView(transactions.get(0).get("loan_acct_no"));

		TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
		debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm.setTran_id(tranId);
		debitTrm.setPart_tran_id(partTranId);
		debitTrm.setAcct_num(leasydebit.getAcct_num());
		debitTrm.setAcct_name(leasydebit.getAcct_name());
		debitTrm.setTran_type("TRANSFER");
		debitTrm.setPart_tran_type("Credit");
		debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
		debitTrm.setTran_amt(totalIndem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		debitTrm.setTran_particular(loanDetails1.getId() + " Demand Amount");
		debitTrm.setTran_remarks(loanDetails1.getId() + " Demand Amount");
		debitTrm.setTran_date(Transaction_date);
		debitTrm.setValue_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm.setFlow_code("RECOVERY");
		debitTrm.setFlow_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm.setTran_status("ENTERED");
		debitTrm.setEntry_user(user);
		debitTrm.setEntry_time(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm.setDel_flg("N");
		transactionList.add(debitTrm);
		partTranId = partTranId.add(BigDecimal.ONE);

		String acct_num1 = "4200004210";
		Chart_Acc_Entity leasydebit1 = chart_Acc_Rep.getaedit(acct_num1);
		TRAN_MAIN_TRM_WRK_ENTITY debitTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();
		debitTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm1.setTran_id(tranId);
		debitTrm1.setPart_tran_id(partTranId);
		debitTrm1.setAcct_num(leasydebit1.getAcct_num());
		debitTrm1.setAcct_name(leasydebit1.getAcct_name());
		debitTrm1.setTran_type("TRANSFER");
		debitTrm1.setPart_tran_type("Credit");
		debitTrm1.setAcct_crncy(leasydebit1.getAcct_crncy());
		debitTrm1.setTran_amt(totalFeedem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		debitTrm1.setTran_particular(loanDetails1.getId() + " Demand Amount");
		debitTrm1.setTran_remarks(loanDetails1.getId() + " Demand Amount");
		debitTrm1.setTran_date(Transaction_date);
		debitTrm1.setValue_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm1.setFlow_code("RECOVERY");
		debitTrm1.setFlow_date(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm1.setTran_status("ENTERED");
		debitTrm1.setEntry_user(user);
		debitTrm1.setEntry_time(dateFormat.parse(transactions.get(0).get("flow_date")));
		debitTrm1.setDel_flg("N");
		transactionList.add(debitTrm1);

		// TRANSACTION DATE UPDATE ADD NEW ROWS
		LOAN_REPAYMENT_ENTITY newRecord = new LOAN_REPAYMENT_ENTITY();

		newRecord.setEncoded_key(encodedKey); // Keep the same key
		newRecord.setParent_account_key(encodedKey);
		newRecord.setDue_date(Transaction_date); // New row should have Transaction_date
		newRecord.setRepaid_date(null);
		newRecord.setPrincipal_exp(BigDecimal.ZERO);
		newRecord.setPrincipal_paid(BigDecimal.ZERO);
		newRecord.setPrincipal_due(BigDecimal.ZERO);
		newRecord.setInterest_exp(totalIndem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		newRecord.setInterest_paid(BigDecimal.ZERO);
		newRecord.setInterest_due(totalIndem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		newRecord.setFee_exp(totalFeedem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		newRecord.setFee_paid(BigDecimal.ZERO);
		newRecord.setFee_due(totalFeedem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
		newRecord.setAsondate(Transaction_date);
		newRecord.setDel_flg("N");

		// **Save the new record**
		lOAN_REPAYMENT_REPO.save(newRecord);
		System.out.println("New demand record inserted with Due Date: " + Transaction_date + "\n");

		tRAN_MAIN_TRM_WRK_REP.saveAll(transactionList);
	}

	// closer account demand and repayment schedule update account balance matched

	@PostMapping("transactionCollectiondatas1212")
	public String transactionCollectiondatas1212(@RequestBody List<Map<String, String>> transactions,
			HttpServletRequest rq) throws ParseException {
		System.out.println("Received transactions: " + transactions);

		String user = (String) rq.getSession().getAttribute("USERID");
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		BigDecimal partTranId = BigDecimal.valueOf(1);

		String encodedKey = transactions.get(0).get("encoded_key");
		System.out.println("THE GETTING VALUES ARE HERE ENCODED KEY " + encodedKey);
		String flowdates = transactions.get(0).get("flow_date");

		if (flowdates == null || flowdates.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid request: to_date is missing or empty");
		}

		// Extract unique flow_dates
		Set<String> uniqueFlowDates = new HashSet<>();
		for (Map<String, String> transaction : transactions) {
			uniqueFlowDates.add(transaction.get("flow_date"));
		}

		// Convert flow_dates to SQL format
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<String> formattedFlowDates = new ArrayList<>();
		for (String flowDateKey : uniqueFlowDates) {
			if (flowDateKey != null && !flowDateKey.trim().isEmpty()) {
				Date parsedDate = inputDateFormat.parse(flowDateKey);
				formattedFlowDates.add(sqlDateFormat.format(parsedDate));
			}
		}

		if (transactions.size() == 1) {
			processSingleTransaction101(transactions.get(0), tranId, partTranId, user, encodedKey);
		} else {
			processMultipleTransactions101(transactions, tranId, partTranId, user, encodedKey, formattedFlowDates);
		}

		return tranId;
	}

	private void processSingleTransaction101(Map<String, String> transaction, String tranId, BigDecimal partTranId,
			String user, String encodedKey) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date flowDate = dateFormat.parse(transaction.get("flow_date"));

		String flowCode = transaction.get("flow_code");
		BigDecimal flowAmt = new BigDecimal(transaction.get("flow_amt").trim());
		String accountNo = transaction.get("loan_acct_no");

		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE correctly to LocalDate
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Convert LocalDate back to java.util.Date
		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

		TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
		creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		creditTrm.setTran_id(tranId);
		creditTrm.setPart_tran_id(partTranId);
		creditTrm.setAcct_num(loanDetails.getId());
		creditTrm.setAcct_name(loanDetails.getLoan_name());
		creditTrm.setTran_type("TRANSFER");
		creditTrm.setPart_tran_type("Credit");
		creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
		creditTrm.setTran_amt(flowAmt);
		creditTrm.setTran_date(Transaction_date);
		creditTrm.setValue_date(flowDate);
		creditTrm.setFlow_code(flowCode);
		creditTrm.setFlow_date(flowDate);
		creditTrm.setTran_status("ENTERED");
		creditTrm.setEntry_user(user);
		creditTrm.setEntry_time(flowDate);
		creditTrm.setDel_flg("N");

		tRAN_MAIN_TRM_WRK_REP.save(creditTrm);
	}

	private void processMultipleTransactions101(List<Map<String, String>> transactions, String tranId,
			BigDecimal partTranId, String user, String encodedKey, List<String> formattedFlowDates)
			throws ParseException {

		List<TRAN_MAIN_TRM_WRK_ENTITY> transactionList = new ArrayList<>();

		// Initialize Maps for total amounts per flow_date
		Map<String, BigDecimal> totalPrdem = new HashMap<>();
		Map<String, BigDecimal> totalIndem = new HashMap<>();
		Map<String, BigDecimal> totalFeedem = new HashMap<>();

		// Initialize total amounts as BigDecimal (not maps)
		BigDecimal totalPrdem11 = BigDecimal.ZERO;
		BigDecimal totalIndem12 = BigDecimal.ZERO;
		BigDecimal totalFeedem13 = BigDecimal.ZERO;

		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE correctly to LocalDate
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Convert LocalDate back to java.util.Date
		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Fetch demand records once for all flow_dates
		List<LOAN_REPAYMENT_ENTITY> demandRecordsList1 = Optional
				.ofNullable(lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas21(encodedKey)).orElse(Collections.emptyList());

		for (Map<String, String> transaction : transactions) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date flowDate = dateFormat.parse(transaction.get("flow_date"));

			String flowCode = transaction.get("flow_code");
			BigDecimal flowAmt = new BigDecimal(transaction.get("flow_amt").trim()); // Replacing tran_amt with flow_amt

			// Skip processing if flow_amt is 0
			if (flowAmt.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			String accountNo = transaction.get("loan_acct_no");
			String flowDateKey = transaction.get("flow_date");

			LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNo);

			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId);
			creditTrm.setAcct_num(loanDetails.getId());
			creditTrm.setAcct_name(loanDetails.getLoan_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setAcct_crncy(loanDetails.getCurrency_code());
			creditTrm.setTran_amt(flowAmt); // Replacing tran_amt with flow_amt
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setValue_date(flowDate);
			creditTrm.setFlow_code(flowCode);
			creditTrm.setFlow_date(flowDate);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			creditTrm.setEntry_time(flowDate);
			creditTrm.setDel_flg("N");

			switch (flowCode) {
			case "PRDEM":
				totalPrdem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Principal Recovery");
				creditTrm.setTran_remarks("Principal amount recovered on " + flowDateKey);
				break;
			case "INDEM":
				totalIndem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Interest Recovery");
				creditTrm.setTran_remarks("Interest amount recovered on " + flowDateKey);
				break;
			case "FEEDEM":
				totalFeedem.merge(flowDateKey, flowAmt, BigDecimal::add);
				creditTrm.setTran_particular(loanDetails.getId() + " Fees Recovery");
				creditTrm.setTran_remarks("Fees amount recovered on " + flowDateKey);
				break;
			default:
				creditTrm.setTran_particular("Unknown Transaction");
				creditTrm.setTran_remarks("Unrecognized transaction type for " + flowDateKey);
			}

			if (Transaction_date.equals(flowDate)) {
				switch (flowCode) {
				case "PRDEM":
					totalPrdem11 = totalPrdem11.add(flowAmt);
					break;

				case "INDEM":
					totalIndem12 = totalIndem12.add(flowAmt);
					break;

				case "FEEDEM":
					totalFeedem13 = totalFeedem13.add(flowAmt);
					break;

				default:
					// Ignore other flow types
					return;
				}
			}

			transactionList.add(creditTrm);
			partTranId = partTranId.add(BigDecimal.ONE);
		}

		// Skip debit transaction if total amount is 0
		BigDecimal totalTranAmt = totalPrdem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add)
				.add(totalIndem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add))
				.add(totalFeedem.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));

		if (totalTranAmt.compareTo(BigDecimal.ZERO) > 0) {
			String acct_num = "2700002750";
			Chart_Acc_Entity leasydebit = chart_Acc_Rep.getaedit(acct_num);
			LOAN_ACT_MST_ENTITY loanDetails1 = lOAN_ACT_MST_REPO.getLoanView(transactions.get(0).get("loan_acct_no"));

			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId);
			debitTrm.setAcct_num(leasydebit.getAcct_num());
			debitTrm.setAcct_name(leasydebit.getAcct_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setAcct_crncy(leasydebit.getAcct_crncy());
			debitTrm.setTran_amt(totalTranAmt);
			debitTrm.setTran_particular(loanDetails1.getId() + " Recovery Amount");
			debitTrm.setTran_remarks(loanDetails1.getId() + " Recovery Amount");
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setValue_date(new SimpleDateFormat("dd-MM-yyyy").parse(transactions.get(0).get("flow_date")));
			debitTrm.setFlow_code("RECOVERY");
			debitTrm.setFlow_date(new SimpleDateFormat("dd-MM-yyyy").parse(transactions.get(0).get("flow_date")));
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			debitTrm.setEntry_time(new SimpleDateFormat("dd-MM-yyyy").parse(transactions.get(0).get("flow_date")));
			debitTrm.setDel_flg("N");

			tRAN_MAIN_TRM_WRK_REP.save(debitTrm);
		}

		// Process and update repayment records
		// Sort demand records by Due Date (ascending order)
		demandRecordsList1.sort(Comparator.comparing(LOAN_REPAYMENT_ENTITY::getDue_date));

		// UPDATE BEFORE TRAN_DATE UPDATE
		for (LOAN_REPAYMENT_ENTITY demandRecord : demandRecordsList1) {
			String dueDate = new SimpleDateFormat("dd-MM-yyyy").format(demandRecord.getDue_date());

			// Get expected and due amounts
			BigDecimal principalExp = demandRecord.getPrincipal_exp();
			BigDecimal interestExp = demandRecord.getInterest_exp();
			BigDecimal feeExp = demandRecord.getFee_exp();

			BigDecimal existingPrincipalPaid = demandRecord.getPrincipal_paid();
			BigDecimal existingInterestPaid = demandRecord.getInterest_paid();
			BigDecimal existingFeePaid = demandRecord.getFee_paid();

			BigDecimal principalDue = demandRecord.getPrincipal_due();
			BigDecimal interestDue = demandRecord.getInterest_due();
			BigDecimal feeDue = demandRecord.getFee_due();

			// Remaining total paid amounts (carry forward if excess)
			BigDecimal principalPaid = totalPrdem.getOrDefault(dueDate, BigDecimal.ZERO);
			BigDecimal interestPaid = totalIndem.getOrDefault(dueDate, BigDecimal.ZERO);
			BigDecimal feePaid = totalFeedem.getOrDefault(dueDate, BigDecimal.ZERO);

			System.out.println("Processing demand record for Due Date: " + dueDate);
			System.out.println(
					"Expected Amounts: Principal=" + principalExp + ", Interest=" + interestExp + ", Fee=" + feeExp);
			System.out.println("Existing Paid: Principal=" + existingPrincipalPaid + ", Interest="
					+ existingInterestPaid + ", Fee=" + existingFeePaid);
			System.out.println(
					"Newly Paid: Principal=" + principalPaid + ", Interest=" + interestPaid + ", Fee=" + feePaid);

			// **First clear Principal Due**
			if (principalDue.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal amountToPay = principalPaid.min(principalDue);
				demandRecord.setPrincipal_paid(existingPrincipalPaid.add(amountToPay));
				demandRecord.setPrincipal_due(principalDue.subtract(amountToPay));
				principalPaid = principalPaid.subtract(amountToPay);
			}

			// **Then clear Interest Due (only after Principal is cleared)**
			if (interestDue.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal amountToPay = interestPaid.min(interestDue);
				demandRecord.setInterest_paid(existingInterestPaid.add(amountToPay));
				demandRecord.setInterest_due(interestDue.subtract(amountToPay));
				interestPaid = interestPaid.subtract(amountToPay);
			}

			// **Finally clear Fee Due (only after Interest is cleared)**
			if (feeDue.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal amountToPay = feePaid.min(feeDue);
				demandRecord.setFee_paid(existingFeePaid.add(amountToPay));
				demandRecord.setFee_due(feeDue.subtract(amountToPay));
				feePaid = feePaid.subtract(amountToPay);
			}

			// **Update Payment State**
			if (demandRecord.getPrincipal_due().compareTo(BigDecimal.ZERO) == 0
					&& demandRecord.getInterest_due().compareTo(BigDecimal.ZERO) == 0
					&& demandRecord.getFee_due().compareTo(BigDecimal.ZERO) == 0) {
				demandRecord.setPayment_state("PAID");
				System.out.println("Payment State: PAID");
			} else {
				demandRecord.setPayment_state("PARTIALLY PAID");
				System.out.println("Payment State: PARTIALLY PAID");
			}

			// **Ensure Paid Amounts Are Applied to Future Dates After Transaction Date**
			if (principalPaid.compareTo(BigDecimal.ZERO) > 0 || interestPaid.compareTo(BigDecimal.ZERO) > 0
					|| feePaid.compareTo(BigDecimal.ZERO) > 0) {
				totalPrdem.put(dueDate, principalPaid);
				totalIndem.put(dueDate, interestPaid);
				totalFeedem.put(dueDate, feePaid);
			}

			// **Update Repaid Date (Regardless of Transaction Date)**
			demandRecord.setRepaid_date(Transaction_date);
			lOAN_REPAYMENT_REPO.save(demandRecord);
			System.out.println("Demand record saved.\n");
		}

		// TRANSACTION DATE UPDATE ADD NEW ROWS
		LOAN_REPAYMENT_ENTITY newRecord = new LOAN_REPAYMENT_ENTITY();

		newRecord.setEncoded_key(encodedKey); // Keep the same key
		newRecord.setParent_account_key(encodedKey);
		newRecord.setDue_date(Transaction_date); // New row should have Transaction_date
		newRecord.setRepaid_date(Transaction_date);
		newRecord.setPrincipal_exp(totalPrdem11);
		newRecord.setPrincipal_paid(totalPrdem11);
		newRecord.setPrincipal_due(BigDecimal.ZERO);
		newRecord.setInterest_exp(totalIndem12);
		newRecord.setInterest_paid(totalIndem12);
		newRecord.setInterest_due(BigDecimal.ZERO);
		newRecord.setFee_exp(totalFeedem13);
		newRecord.setFee_paid(totalFeedem13);
		newRecord.setFee_due(BigDecimal.ZERO);
		newRecord.setAsondate(Transaction_date);
		newRecord.setDel_flg("N");

		// **Save the new record**
		lOAN_REPAYMENT_REPO.save(newRecord);
		System.out.println("New demand record inserted with Due Date: " + Transaction_date + "\n");

		// PENDING REPAYMENT VALUES UPDATE FLG
		System.out.println("THE PASSED ENCODED_KEY AFTER PASSING DATE " + encodedKey);
		System.out.println("THE PASSED TRAN_DATE AFTER PASSING DATE " + tranDate);
		List<LOAN_REPAYMENT_ENTITY> recordValues = lOAN_REPAYMENT_REPO.getLoanFlowsValueDatas31(encodedKey, tranDate);

		for (LOAN_REPAYMENT_ENTITY values : recordValues) {
			values.setDel_flg("Y");
			System.out.println("THE FL UPDATON FUNCTION WILL BE WORK " + values.getDue_date());
			lOAN_REPAYMENT_REPO.save(values);
		}
		tRAN_MAIN_TRM_WRK_REP.saveAll(transactionList);
	}

	/* praveen - Interest (Application) */
	/* praveen - Interest (Application) */
	@GetMapping("transactionInterest11")
	public String transactionInterest11(@RequestParam(required = false) String flow_code,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date flow_date,
			@RequestParam(required = false) String flow_amount, @RequestParam(required = false) String flow_id,
			@RequestParam(required = false) String account_no, @RequestParam(required = false) String accountName,
			@RequestParam(required = false) String operation, @RequestParam(required = false) String encoded_key,
			HttpServletRequest rq) {

		String user = (String) rq.getSession().getAttribute("USERID");
		System.out.println("USERID: " + user);

		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		System.out.println("Generated TranId: " + tranId);
		System.out.println("Frontend Flow Date: " + flow_date);
		System.out.println("Encoded Key: " + encoded_key);

		// Format flow date for DB
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String flow_f_dates = sdf.format(flow_date);
		System.out.println("Formatted Flow Date: " + flow_f_dates);

		// Get previous transaction date
		Date previousFlowDate = lOAN_REPAYMENT_REPO.findPreviousDueDate(flow_f_dates, encoded_key);
		System.out.println("Matched Previous Flow Date: " + previousFlowDate);

		BigDecimal partTranId1 = BigDecimal.valueOf(1);
		BigDecimal partTranId2 = BigDecimal.valueOf(2);
		BigDecimal partTranId3 = BigDecimal.valueOf(3);

		// Latest TRAN_DATE
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("Latest TRAN_DATE: " + tranDateObj);

		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		System.out.println("Transaction Date: " + Transaction_date);

		// Previous month-end date
		LocalDate previousMonthEnd = tranDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		Date previousMonthEndDate = java.sql.Date.valueOf(previousMonthEnd);
		

		String likePattern = "%Booking: " + account_no + "%";  
		System.out.println("LIKE Pattern for Interest Receivable: " + likePattern);
		

		// Assume flow_date is java.util.Date (already bound with @DateTimeFormat)
		Date inputDate = flow_date;

		// Step 1: Convert Date → LocalDate
		LocalDate localDate = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Date finalDate = java.sql.Date.valueOf(localDate);
		
		System.out.println("THE GETTING previousMonthEndDate IS "+previousMonthEndDate);
		System.out.println("LIKE Pattern for Interest Receivable: " + likePattern);
		System.out.println("THE GETTING FLOW_DATE IS "+finalDate);

		// Interest receivable
		TRAN_MAIN_TRM_WRK_ENTITY recivableAmount = tRAN_MAIN_TRM_WRK_REP.getInterestRecivable1(previousMonthEndDate,likePattern);
		BigDecimal ReciveAmount = (recivableAmount != null && recivableAmount.getTran_amt() != null)
				? recivableAmount.getTran_amt()
				: BigDecimal.ZERO;
		System.out.println("Previous Month Interest Receivable (ReciveAmount): " + ReciveAmount);

		// Input flow amount
		BigDecimal flowAmountBD = (flow_amount != null) ? new BigDecimal(flow_amount) : BigDecimal.ZERO;
		System.out.println("Frontend Flow Amount: " + flowAmountBD);

		// Transaction amounts
		BigDecimal debitAmount = flowAmountBD;
		BigDecimal creditAmount = flowAmountBD.subtract(ReciveAmount).setScale(0, RoundingMode.HALF_UP);
		BigDecimal receivableCreditAmount = ReciveAmount;

		System.out.println("Debit Amount: " + debitAmount);
		System.out.println("Credit Amount: " + creditAmount);
		System.out.println("Receivable Credit Amount: " + receivableCreditAmount);

		// Loan details
		LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(account_no);
		System.out.println("Loan Account: " + loanDetails.getId() + ", Loan Name: " + loanDetails.getLoan_name());

		String tranParticulars = "PRDEM".equals(flow_code) ? "Principle Debited" : "Interest Debited";

		// --- First Transaction - Customer Loan Account DEBIT ---
		TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
		debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		debitTrm.setTran_id(tranId);
		debitTrm.setPart_tran_id(partTranId1);
		debitTrm.setAcct_num(loanDetails.getId());
		debitTrm.setAcct_name(loanDetails.getLoan_name());
		debitTrm.setTran_type("TRANSFER");
		debitTrm.setPart_tran_type("Debit");
		debitTrm.setAcct_crncy(loanDetails.getCurrency_code());
		debitTrm.setTran_amt(debitAmount);
		debitTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars);
		debitTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars);
		debitTrm.setTran_date(Transaction_date);
		debitTrm.setValue_date(flow_date);
		debitTrm.setFlow_code(flow_code);
		debitTrm.setFlow_date(flow_date);
		debitTrm.setTran_status("ENTERED");
		debitTrm.setEntry_user(user);
		debitTrm.setEntry_time(flow_date);
		debitTrm.setDel_flg("N");
		tRAN_MAIN_TRM_WRK_REP.save(debitTrm);
		System.out.println("Saved First Transaction (Debit)");

		// --- Second Transaction - Office Loan Interest Account CREDIT ---
		Chart_Acc_Entity leaseDebit = chart_Acc_Rep.getaedit("4100004110");
		TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
		creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		creditTrm.setTran_id(tranId);
		creditTrm.setPart_tran_id(partTranId2);
		creditTrm.setAcct_num(leaseDebit.getAcct_num());
		creditTrm.setAcct_name(leaseDebit.getAcct_name());
		creditTrm.setTran_type("TRANSFER");
		creditTrm.setPart_tran_type("Credit");
		creditTrm.setAcct_crncy(leaseDebit.getAcct_crncy());
		creditTrm.setTran_amt(creditAmount);
		creditTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars);
		creditTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars);
		creditTrm.setTran_date(Transaction_date);
		creditTrm.setValue_date(flow_date);
		creditTrm.setFlow_code(flow_code);
		creditTrm.setFlow_date(flow_date);
		creditTrm.setTran_status("ENTERED");
		creditTrm.setEntry_user(user);
		creditTrm.setEntry_time(flow_date);
		creditTrm.setDel_flg("N");
		tRAN_MAIN_TRM_WRK_REP.save(creditTrm);
		System.out.println("Saved Second Transaction (Credit)");

		// --- Third Transaction - Interest Receivable CREDIT ---
		Chart_Acc_Entity leaseDebit1 = chart_Acc_Rep.getaedit("1200001220");
		TRAN_MAIN_TRM_WRK_ENTITY creditTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();
		creditTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
		creditTrm1.setTran_id(tranId);
		creditTrm1.setPart_tran_id(partTranId3);
		creditTrm1.setAcct_num(leaseDebit1.getAcct_num());
		creditTrm1.setAcct_name(leaseDebit1.getAcct_name());
		creditTrm1.setTran_type("TRANSFER");
		creditTrm1.setPart_tran_type("Credit");
		creditTrm1.setAcct_crncy(leaseDebit1.getAcct_crncy());
		creditTrm1.setTran_amt(receivableCreditAmount);
		creditTrm1.setTran_particular(loanDetails.getId() + " Reversal of Booking");
		creditTrm1.setTran_remarks(loanDetails.getId() + " Reversal of Booking");
		creditTrm1.setTran_date(Transaction_date);
		creditTrm1.setValue_date(flow_date);
		creditTrm1.setFlow_code(flow_code);
		creditTrm1.setFlow_date(flow_date);
		creditTrm1.setTran_status("ENTERED");
		creditTrm1.setEntry_user(user);
		creditTrm1.setEntry_time(flow_date);
		creditTrm1.setDel_flg("N");
		tRAN_MAIN_TRM_WRK_REP.save(creditTrm1);
		System.out.println("Saved Third Transaction (Interest Receivable Credit)");

		return tranId;
	}

	/* praveen - Interest (Application) */
	@GetMapping("transactionFees11")
	public String transactionFees11(@RequestParam(required = false) String flow_code,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date flow_date,
			@RequestParam(required = false) String flow_amount, @RequestParam(required = false) String flow_id,
			@RequestParam(required = false) String account_no, @RequestParam(required = false) String accountName,
			@RequestParam(required = false) String operation, HttpServletRequest rq) {

		String user = (String) rq.getSession().getAttribute("USERID");

		/* tranId sequence */
		String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
		System.out.println("THE FRONTEND GETTING FLOW DATE IS  " + flow_date);

		BigDecimal partTranId1 = BigDecimal.valueOf(1);
		BigDecimal partTranId2 = BigDecimal.valueOf(2);
		BigDecimal partTranId3 = BigDecimal.valueOf(3);

		String account_value = "1200001220";
		Object[] result = tRAN_MAIN_TRM_WRK_REP.getLatestAccountBalanceAndType(account_value);
		String datavalues = "Interest";

		// Fetch the latest TRAN_DATE from the database
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		// Convert TRAN_DATE to LocalDate for easy comparison
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Define date formats
		SimpleDateFormat backendFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat frontendFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

		List<TRAN_MAIN_TRM_WRK_ENTITY> interestCollections = tRAN_MAIN_TRM_WRK_REP.getAccountDetails(account_value);

		BigDecimal flowAmount = (flow_amount != null) ? new BigDecimal(flow_amount) : BigDecimal.ZERO;

		BigDecimal flow_amt_values = BigDecimal.ZERO;

		if (!interestCollections.isEmpty() && interestCollections.get(0).getTran_amt() != null) {
			flow_amt_values = interestCollections.get(0).getTran_amt();
		}

		System.out.println("The getting flow_amt is here " + flow_amt_values);

		BigDecimal creditAmount = flowAmount.subtract(flow_amt_values).setScale(0, RoundingMode.HALF_UP);
		BigDecimal finalAmount = flowAmount.subtract(creditAmount).setScale(0, RoundingMode.HALF_UP);

		String partrantype = null;
		Date flow_dates = null;

		for (TRAN_MAIN_TRM_WRK_ENTITY values : interestCollections) {
			flow_dates = values.getFlow_date(); // Store last flow_date
			System.out.println("THE DATABASE GET FLOW_DATES HERE " + flow_dates);
			partrantype = values.getPart_tran_type(); // Store last part_tran_type
		}

		// Now, flow_dates and partrantype contain the last values from the loop

		// Convert Date to LocalDateTime safely
		LocalDateTime frontendDateTime = (flow_date != null) ? convertToLocalDateTime(flow_date) : null;
		LocalDateTime backendDateTime = (flow_dates != null) ? convertToLocalDateTime(flow_dates) : null;

		// Compare Dates and Check Transaction Type
		if (frontendDateTime != null && backendDateTime != null && frontendDateTime.isEqual(backendDateTime)) {
			if ("debit".equalsIgnoreCase(partrantype)) {
				String tranParticulars = "PRDEM".equals(flow_code) ? "Principle Debited" : "Interest Debited";

				LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(account_no);

				// First Transaction - Customer Loan Account DEBIT
				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId1);
				debitTrm.setAcct_num(loanDetails.getId());
				debitTrm.setAcct_name(loanDetails.getLoan_name());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setAcct_crncy(loanDetails.getCurrency_code());
				debitTrm.setTran_amt(flowAmount);
				debitTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars);
				debitTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars);
				debitTrm.setTran_date(Transaction_date);
				debitTrm.setValue_date(flow_date);
				debitTrm.setFlow_code(flow_code);
				debitTrm.setFlow_date(flow_date);
				debitTrm.setTran_status("ENTERED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flow_date);
				debitTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				// Second Transaction - Office Loan Interest Account CREDIT
				Chart_Acc_Entity leaseDebit = chart_Acc_Rep.getaedit("4100004110");

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId2);
				creditTrm.setAcct_num(leaseDebit.getAcct_num());
				creditTrm.setAcct_name(leaseDebit.getAcct_name());
				creditTrm.setTran_type("TRANSFER");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(leaseDebit.getAcct_crncy());
				creditTrm.setTran_amt(creditAmount);
				creditTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars);
				creditTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars);
				creditTrm.setTran_date(Transaction_date);
				creditTrm.setValue_date(flow_date);
				creditTrm.setFlow_code(flow_code);
				creditTrm.setFlow_date(flow_date);
				creditTrm.setTran_status("ENTERED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flow_date);
				creditTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

				// Third Transaction - Interest Receivable CREDIT
				Chart_Acc_Entity leaseDebit1 = chart_Acc_Rep.getaedit("1200001220");

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm1 = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm1.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm1.setTran_id(tranId);
				creditTrm1.setPart_tran_id(partTranId3);
				creditTrm1.setAcct_num(leaseDebit1.getAcct_num());
				creditTrm1.setAcct_name(leaseDebit1.getAcct_name());
				creditTrm1.setTran_type("TRANSFER");
				creditTrm1.setPart_tran_type("Credit");
				creditTrm1.setAcct_crncy(leaseDebit1.getAcct_crncy());
				creditTrm1.setTran_amt(finalAmount);
				creditTrm1.setTran_particular(loanDetails.getId() + " " + "Reversal of Booking");
				creditTrm1.setTran_remarks(loanDetails.getId() + " " + "Reversal of Booking");
				creditTrm1.setTran_date(Transaction_date);
				creditTrm1.setValue_date(flow_date);
				creditTrm1.setFlow_code(flow_code);
				creditTrm1.setFlow_date(flow_date);
				creditTrm1.setTran_status("ENTERED");
				creditTrm1.setEntry_user(user);
				creditTrm1.setEntry_time(flow_date);
				creditTrm1.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm1);
			}
		} else {
			String tranParticulars = "PRDEM".equals(flow_code) ? "Principle Debited" : "Interest Debited";

			TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(account_no);

			debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			debitTrm.setTran_id(tranId);
			debitTrm.setPart_tran_id(partTranId1);
			debitTrm.setAcct_num(loanDetails.getId());
			debitTrm.setAcct_name(loanDetails.getLoan_name());
			debitTrm.setTran_type("TRANSFER");
			debitTrm.setPart_tran_type("Debit");
			debitTrm.setTran_amt(flowAmount);
			debitTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars);
			debitTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars);
			debitTrm.setTran_date(Transaction_date);
			debitTrm.setFlow_code(flow_code);
			debitTrm.setTran_status("ENTERED");
			debitTrm.setEntry_user(user);
			tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

			Chart_Acc_Entity leaseDebit = chart_Acc_Rep.getaedit("4100004110");
			TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
			creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
			creditTrm.setTran_id(tranId);
			creditTrm.setPart_tran_id(partTranId2);
			creditTrm.setAcct_num(leaseDebit.getAcct_num());
			creditTrm.setAcct_name(leaseDebit.getAcct_name());
			creditTrm.setTran_type("TRANSFER");
			creditTrm.setPart_tran_type("Credit");
			creditTrm.setTran_amt(flowAmount);
			creditTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars);
			creditTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars);
			creditTrm.setTran_date(Transaction_date);
			creditTrm.setFlow_code(flow_code);
			creditTrm.setTran_status("ENTERED");
			creditTrm.setEntry_user(user);
			tRAN_MAIN_TRM_WRK_REP.save(creditTrm);
		}

		return tranId;
	}

	// ✅ Method to convert java.util.Date to LocalDateTime
	private LocalDateTime convertToLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	@RequestMapping(value = "/getBranchData", method = RequestMethod.GET)
	@ResponseBody
	public List<LOAN_ACT_MST_ENTITY> getBranchData(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate) {

		if (currentDate != null) {
			// Convert java.util.Date to java.sql.Date (removes time part)
			java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
			System.out.println("Formatted SQL Date for DB: " + sqlDate); // 2025-04-24
			return lOAN_ACT_MST_REPO.getLoanActDetval();
		} else {
			System.out.println("No date provided.");
			return new ArrayList<>();
		}
	}

	@RequestMapping(value = "/getBranchData1", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> getBranchData1(
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate) {

	    if (currentDate == null) {
	        System.out.println("No date provided.");
	        return new ArrayList<>();
	    }

	    // Convert to SQL date (removes time)
	    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
	    System.out.println("Formatted SQL Date for DB: " + sqlDate);

	    // Fetch loan account data
	    List<LOAN_ACT_MST_ENTITY> loanActList = lOAN_ACT_MST_REPO.getLoanActDetval1(sqlDate);

	    // Collect encoded keys
	    List<String> encodedKeyList = loanActList.stream()
	            .map(LOAN_ACT_MST_ENTITY::getEncoded_key)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    if (encodedKeyList.isEmpty()) {
	        System.out.println("No Encoded Keys Found.");
	        return new ArrayList<>();
	    }

	    // Split keys into batches of 1000 to avoid ORA-01795
	    List<Object[]> repaymentList = new ArrayList<>();
	    int batchSize = 1000;
	    for (int i = 0; i < encodedKeyList.size(); i += batchSize) {
	        List<String> batch = encodedKeyList.subList(i, Math.min(i + batchSize, encodedKeyList.size()));
	        repaymentList.addAll(lOAN_REPAYMENT_REPO.findByParentAccountKeyInAndDueDateLessThanEqual(batch, sqlDate));
	    }

	    System.out.println("Repayment Size: " + repaymentList.size());
	    return repaymentList;
	}


	@PostMapping("/saveBranchData")
	public ResponseEntity<Map<String, Object>> saveBranchData(@RequestBody List<Map<String, Object>> branchDataList,
			HttpServletRequest rq) {
		
		int totalRecords = 0, failedRecords = 0 ;
		 BigDecimal totalAmount = BigDecimal.ZERO;
		 List<TRAN_MAIN_TRM_WRK_ENTITY> savedatas = new ArrayList<>();
		 Map<String, Object> response = new LinkedHashMap<>();
		if (branchDataList != null && !branchDataList.isEmpty()) {
			// Iterating over all rows (branchDataList)
			for (Map<String, Object> data : branchDataList) {
				 try {
				//START REPORT
				 String flowAmountStr = data.get("flow_amount").toString()
	                        .replace(",", "").trim();
	                BigDecimal flowAmount1 = new BigDecimal(flowAmountStr);
				
				/*
				 * // Logging the incoming data for each row
				 * System.out.println("Received Data:"); System.out.println("SRL No: " +
				 * data.get("srl_no")); System.out.println("Flow ID: " + data.get("flow_id"));
				 * System.out.println("Flow Date: " + data.get("flow_date"));
				 * System.out.println("Flow Code: " + data.get("flow_code"));
				 * System.out.println("Flow Amount: " + data.get("flow_amount"));
				 * System.out.println("Account Number: " + data.get("account_number"));
				 * System.out.println("Account Name: " + data.get("account_name"));
				 */
				// Extract user from session
				String user = (String) rq.getSession().getAttribute("USERID");

				// Convert values to correct data types
				String flowDateStr = (String) data.get("flow_date");
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Adjusted format to dd-MM-yyyy
				Date flowDate = null;
				try {
					flowDate = sdf.parse(flowDateStr); // Parsing the flowDateStr with the given format
					
				} catch (Exception e) {
					e.printStackTrace(); // Log exception
					ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format.");

				}

				BigDecimal flowAmount = new BigDecimal((String) data.get("flow_amount").toString().replace(",", ""));
				String flowCode = (String) data.get("flow_code");
				String accountNumber = (String) data.get("account_number");

				// Example of creating a UUID or TranId
				String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();

				// Assuming these BigDecimal values for part transaction IDs
				BigDecimal partTranId1 = BigDecimal.valueOf(1);
				BigDecimal partTranId2 = BigDecimal.valueOf(2);

				// Fetch loan details
				LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNumber);

				loanDetails.setDisbursement_flg("Y");
				// Setting tranParticulars to "Loan Disbursement"
				String tranParticulars = "Loan Disbursement";
				
				Date disbursement_date = loanDetails.getCreation_date();

				// First Transaction - Customer Loan Account DEBIT
				TRAN_MAIN_TRM_WRK_ENTITY debitTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				debitTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitTrm.setTran_id(tranId);
				debitTrm.setPart_tran_id(partTranId1);
				debitTrm.setAcct_num(loanDetails.getId());
				debitTrm.setAcct_name(loanDetails.getLoan_name());
				debitTrm.setTran_type("TRANSFER");
				debitTrm.setPart_tran_type("Debit");
				debitTrm.setAcct_crncy(loanDetails.getCurrency_code());
				debitTrm.setTran_amt(flowAmount); // Use the flowAmount for debit
				debitTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars); // Setting loan disbursement
				debitTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars); // Setting loan disbursement
				debitTrm.setTran_date(disbursement_date); // Set the flow date
				debitTrm.setValue_date(flowDate); // Set the value date
				debitTrm.setFlow_code(flowCode);
				debitTrm.setFlow_date(flowDate);
				debitTrm.setTran_status("POSTED");
				debitTrm.setEntry_user(user);
				debitTrm.setEntry_time(flowDate);
				debitTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debitTrm);

				// Second Transaction - Office Loan Interest Account CREDIT
				Chart_Acc_Entity leaseDebit = chart_Acc_Rep.getaedit("2700002750");

				TRAN_MAIN_TRM_WRK_ENTITY creditTrm = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditTrm.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditTrm.setTran_id(tranId);
				creditTrm.setPart_tran_id(partTranId2);
				creditTrm.setAcct_num(leaseDebit.getAcct_num());
				creditTrm.setAcct_name(leaseDebit.getAcct_name());
				creditTrm.setTran_type("TRANSFER");
				creditTrm.setPart_tran_type("Credit");
				creditTrm.setAcct_crncy(leaseDebit.getAcct_crncy());
				creditTrm.setTran_amt(flowAmount); // Use the same flowAmount for credit
				creditTrm.setTran_particular(loanDetails.getId() + " " + tranParticulars); // Setting loan disbursement
				creditTrm.setTran_remarks(loanDetails.getId() + " " + tranParticulars); // Setting loan disbursement
				creditTrm.setTran_date(disbursement_date);
				creditTrm.setValue_date(flowDate);
				creditTrm.setFlow_code(flowCode);
				creditTrm.setFlow_date(flowDate);
				creditTrm.setTran_status("POSTED");
				creditTrm.setEntry_user(user);
				creditTrm.setEntry_time(flowDate);
				creditTrm.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(creditTrm);

				// Now update the account balances for both debit and credit transactions
				List<TRAN_MAIN_TRM_WRK_ENTITY> values = tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tranId);
				

				// Iterate over all transactions and apply updates
				for (TRAN_MAIN_TRM_WRK_ENTITY entity : values) {
					entity.setPost_user(user);
					entity.setPost_time(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
					entity.setDel_flg("N");
					entity.setTran_status("POSTED");
					savedatas.add(entity);

					// Fetch Chart of Accounts entry
					Chart_Acc_Entity chartAccount = chart_Acc_Rep.getValuepopval(entity.getAcct_num());
					if (chartAccount == null) {
						System.out.println("Account not found for acct_num: " + entity.getAcct_num());
						continue;
					}

					// ✅ Update DR_AMT for Debit and CR_AMT for Credit
					if (entity.getPart_tran_type().equalsIgnoreCase("Debit")) {
						BigDecimal newDrAmt = chartAccount.getDr_amt().add(entity.getTran_amt());
						chartAccount.setDr_amt(newDrAmt);

						BigDecimal newBalance = chartAccount.getAcct_bal().subtract(entity.getTran_amt());
						chartAccount.setAcct_bal(newBalance);

					} else if (entity.getPart_tran_type().equalsIgnoreCase("Credit")) {
						BigDecimal newCrAmt = chartAccount.getCr_amt().add(entity.getTran_amt());
						chartAccount.setCr_amt(newCrAmt);

						BigDecimal newBalance = chartAccount.getAcct_bal().add(entity.getTran_amt());
						chartAccount.setAcct_bal(newBalance);
					}

					chart_Acc_Rep.save(chartAccount);

					// ✅ Handle Loan disbursement update
					if (entity.getFlow_code().equalsIgnoreCase("DISBT")) {
						String accountNumber1 = entity.getAcct_num();
						String accountName = lease_Loan_Master_Repo.accountName(accountNumber1);
						if (Objects.nonNull(accountName)) {
							Lease_Loan_Master_Entity leaseRecord = lease_Loan_Master_Repo.findByref_no(accountNumber1);
							leaseRecord.setDisbursement(entity.getTran_amt());
							lease_Loan_Master_Repo.save(leaseRecord);
						} else {
							System.out.println("Not a loan account: " + accountNumber1);
						}
					}
				}
				    totalRecords++;
	                totalAmount = totalAmount.add(flowAmount1);
				 } catch (Exception e) {
		                failedRecords++;
		                e.printStackTrace();
		            }
				// Save updated transactions
				tRAN_MAIN_TRM_WRK_REP.saveAll(savedatas);
				
				   
			}
			
			 
		        response.put("totalAmount", totalAmount);
		        response.put("totalRecords", totalRecords);
		        response.put("failedRecords", failedRecords);
		        return ResponseEntity.ok(response);
		}
		response.put("error", "Invalid data or empty list.");

		return ResponseEntity
		        .status(HttpStatus.BAD_REQUEST)
		        .body(response);
	}

	@PostMapping("/saveBranchData1")
	public ResponseEntity<Map<String, Object>> saveBranchData1(@RequestBody List<Map<String, Object>> branchDataList,
			HttpServletRequest request) {
		
		int totalRecords = 0, failedRecords = 0 ;
		BigDecimal totalAmount = BigDecimal.ZERO;
		Map<String, Object> response = new LinkedHashMap<>();
		
		if (branchDataList == null || branchDataList.isEmpty()) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data or empty list.");
		}

		String user = (String) request.getSession().getAttribute("USERID");
		System.out.println("Logged-in User ID: " + user);

		for (Map<String, Object> data : branchDataList) {
			
			try {
				
				//START REPORT
				String flowAmountStr1 = data.get("flow_amount").toString()
	                    .replace(",", "").trim();
	        BigDecimal flowAmount1 = new BigDecimal(flowAmountStr1);
	        System.out.println(flowAmount1);
				
				System.out.println("========================================");
				System.out.println("Processing New Record: " + data);

				// Extract required fields
				String flowDateStr = (String) data.get("flow_date");
				String flowCode = (String) data.get("flow_code");
				String accountNumber = (String) data.get("account_number");
				String flowamount = (String) data.get("flow_amount");

				// Parse Flow Date
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				LocalDate flowLocalDate = LocalDate.parse(flowDateStr, dateFormatter);
				Date flowDate = Date.from(flowLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				// --- Previous Month Dates ---
				LocalDate prevMonthSameDate = flowLocalDate.minusMonths(1);
				LocalDate prevMonthEnd = flowLocalDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

				// Days between
				long daysBetween = ChronoUnit.DAYS.between(prevMonthSameDate, prevMonthEnd) + 1; // inclusive

				// keep formatter for logs if you want
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String prevMonthSameDateStr = prevMonthSameDate.format(formatter);
				String prevMonthEndStr = prevMonthEnd.format(formatter);
				System.out.println("Formatted Previous Month Same Date: " + prevMonthSameDateStr);
				System.out.println("Formatted Previous Month End Date : " + prevMonthEndStr);

				// convert LocalDate -> java.util.Date
				Date prevMonthSameDateDate = Date
						.from(prevMonthSameDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				Date prevMonthEndDate = Date.from(prevMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

				// ===== Interest Calculation =====
				BigDecimal indemAmount = new BigDecimal(flowamount); // flow_amount as base
				BigDecimal daysInMonthDecimal = new BigDecimal(
						ChronoUnit.DAYS.between(prevMonthSameDate.withDayOfMonth(1), prevMonthEnd) + 1);

				// Per day interest
				BigDecimal perDayInterest = indemAmount.divide(daysInMonthDecimal, 2, RoundingMode.HALF_UP);

				// Total interest for daysBetween
				BigDecimal betweendaysDecimal = new BigDecimal(daysBetween);
				BigDecimal totalInterest = perDayInterest.multiply(betweendaysDecimal);

				// Rounding logic
				BigDecimal roundedTotalInterest = totalInterest.remainder(BigDecimal.ONE)
						.compareTo(new BigDecimal("0.50")) < 0 ? totalInterest.setScale(0, RoundingMode.FLOOR)
								: totalInterest.setScale(0, RoundingMode.CEILING);

				// Create transaction ID
				String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
				System.out.println("Generated Transaction ID: " + tranId);

				// Fetch loan details
				LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNumber);
				if (loanDetails == null) {
					System.out.println("Loan details not found for account: " + accountNumber);
					ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loan account not found:."  + accountNumber);
				}
				System.out.println("Fetched Loan Details: " + loanDetails);

				// change booking flg
				loanDetails.setBooking_flg("Y");

				// Calculate Month-End Date from Flow Date
				LocalDate flowLocalDateEnd = flowLocalDate.withDayOfMonth(flowLocalDate.lengthOfMonth());
				Date flowMonthEndDate = Date.from(flowLocalDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());
				System.out.println("THE MONTHEND DATE IS " + flowMonthEndDate);

				// Prepare Debit Entry
				Chart_Acc_Entity loanDetails1 = chart_Acc_Rep.getaedit("1200001220");
				TRAN_MAIN_TRM_WRK_ENTITY debitEntry = new TRAN_MAIN_TRM_WRK_ENTITY();
				debitEntry.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debitEntry.setTran_id(tranId);
				debitEntry.setPart_tran_id(BigDecimal.ONE);
				debitEntry.setAcct_num(loanDetails1.getAcct_num());
				debitEntry.setAcct_name(loanDetails1.getAcct_name());
				debitEntry.setTran_type("TRANSFER");
				debitEntry.setPart_tran_type("Debit");
				debitEntry.setAcct_crncy(loanDetails.getCurrency_code());
				debitEntry.setTran_amt(roundedTotalInterest);
				debitEntry.setTran_particular(loanDetails.getId() + " Interest Booking");
				debitEntry.setTran_remarks(loanDetails.getId() + " Interest Booking");
				debitEntry.setTran_date(prevMonthEndDate);
				debitEntry.setValue_date(flowDate);
				debitEntry.setFlow_code(flowCode);
				debitEntry.setFlow_date(flowDate);
				debitEntry.setTran_status("POSTED");
				debitEntry.setEntry_user(user);
				debitEntry.setEntry_time(new Date());
				debitEntry.setDel_flg("N");
				System.out.println("Prepared Debit Entry: " + debitEntry);
				tRAN_MAIN_TRM_WRK_REP.save(debitEntry);

				// Prepare Credit Entry
				Chart_Acc_Entity leaseDebit = chart_Acc_Rep.getaedit("4100004110");
				System.out.println("Fetched Lease Debit Account: " + leaseDebit);

				TRAN_MAIN_TRM_WRK_ENTITY creditEntry = new TRAN_MAIN_TRM_WRK_ENTITY();
				creditEntry.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				creditEntry.setTran_id(tranId);
				creditEntry.setPart_tran_id(BigDecimal.valueOf(2));
				creditEntry.setAcct_num(leaseDebit.getAcct_num());
				creditEntry.setAcct_name(leaseDebit.getAcct_name());
				creditEntry.setTran_type("TRANSFER");
				creditEntry.setPart_tran_type("Credit");
				creditEntry.setAcct_crncy(leaseDebit.getAcct_crncy());
				creditEntry.setTran_amt(roundedTotalInterest);
				creditEntry.setTran_particular(loanDetails.getId() + " Interest Booking");
				creditEntry.setTran_remarks(loanDetails.getId() + " Interest Booking");
				creditEntry.setTran_date(prevMonthEndDate);
				creditEntry.setValue_date(flowDate);
				creditEntry.setFlow_code(flowCode);
				creditEntry.setFlow_date(flowDate);
				creditEntry.setTran_status("POSTED");
				creditEntry.setEntry_user(user);
				creditEntry.setEntry_time(new Date());
				creditEntry.setDel_flg("N");
				System.out.println("Prepared Credit Entry: " + creditEntry);
				tRAN_MAIN_TRM_WRK_REP.save(creditEntry);

				// Update balances
				List<TRAN_MAIN_TRM_WRK_ENTITY> transactions = tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tranId);
				System.out.println("Fetched Transactions for Posting: " + transactions);

				for (TRAN_MAIN_TRM_WRK_ENTITY transaction : transactions) {
					transaction.setPost_user(user);
					transaction.setPost_time(new Date());
					transaction.setTran_status("POSTED");
					transaction.setDel_flg("N");

					Chart_Acc_Entity account = chart_Acc_Rep.getValuepopval(transaction.getAcct_num());
					if (account != null) {
						if ("Debit".equalsIgnoreCase(transaction.getPart_tran_type())) {
							// Update account balance
							account.setAcct_bal(account.getAcct_bal().subtract(transaction.getTran_amt()));

							// Update Debit amount
							if (account.getDr_amt() == null) {
								account.setDr_amt(transaction.getTran_amt());
							} else {
								account.setDr_amt(account.getDr_amt().add(transaction.getTran_amt()));
							}

						} else if ("Credit".equalsIgnoreCase(transaction.getPart_tran_type())) {
							// Update account balance
							account.setAcct_bal(account.getAcct_bal().add(transaction.getTran_amt()));

							// Update Credit amount
							if (account.getCr_amt() == null) {
								account.setCr_amt(transaction.getTran_amt());
							} else {
								account.setCr_amt(account.getCr_amt().add(transaction.getTran_amt()));
							}
						}
						// Save account after update
						chart_Acc_Rep.save(account);
					}

					if ("DISBT".equalsIgnoreCase(transaction.getFlow_code())) {
						Lease_Loan_Master_Entity leaseRecord = lease_Loan_Master_Repo
								.findByref_no(transaction.getAcct_num());
						if (leaseRecord != null) {
							leaseRecord.setDisbursement(transaction.getTran_amt());
							lease_Loan_Master_Repo.save(leaseRecord);
							System.out.println("Updated Lease Disbursement: " + leaseRecord);
						}
					}
				}

				tRAN_MAIN_TRM_WRK_REP.saveAll(transactions);
				System.out.println("Saved all transaction postings.");

				System.out.println("========================================");
				totalRecords++;
                totalAmount = totalAmount.add(flowAmount1);

			} catch (Exception e) {
				e.printStackTrace();
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while saving branch data:");
				response.put("error", "Invalid data or empty list.");
				failedRecords++;
			}
		}
		    response.put("totalAmount", totalAmount);
	        response.put("totalRecords", totalRecords);
	        response.put("failedRecords", failedRecords);
	        
		return ResponseEntity
		        .status(HttpStatus.BAD_REQUEST)
		        .body(response);
	}

	@PostMapping("/saveBranchData2")
	public  ResponseEntity<Map<String, Object>> saveBranchData2(@RequestBody List<Map<String, Object>> branchDataList,
			HttpServletRequest request) {
		
		 int totalRecords = 0, failedRecords = 0 ;
		 BigDecimal totalAmount = BigDecimal.ZERO;
		 Map<String, Object> response = new LinkedHashMap<>();
		 
		if (branchDataList == null || branchDataList.isEmpty()) {
			System.out.println("Invalid or empty data list received.");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data or empty list.");
		}

		String user = (String) request.getSession().getAttribute("USERID");
		if (user == null || user.trim().isEmpty()) {
			System.out.println("User not logged in.");
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in.");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<String> tranIdList = new ArrayList<>();

		for (Map<String, Object> data : branchDataList) {
			try {
				
				String flowAmountStr1 = data.get("flow_amount").toString()
                        .replace(",", "").trim();
                BigDecimal flowAmount1 = new BigDecimal(flowAmountStr1);
				// Check if any required field is null or empty and skip the row if it is
				String flowDateStr = (String) data.get("flow_date");
				String flowCode = (String) data.get("flow_code");
				String accountNumber = (String) data.get("account_number");
				String flowAmountStr = (String) data.get("flow_amount");

				
				  if (flowDateStr == null || flowCode == null || accountNumber == null ||
				  flowAmountStr == null || flowDateStr.trim().isEmpty() ||
				 flowCode.trim().isEmpty() || accountNumber.trim().isEmpty() ||
				 flowAmountStr.trim().isEmpty()) { // Skipping row due to null or empty values
				 System.out.println("Skipping record with missing or empty fields.");
				  continue; }
				

				flowCode = flowCode.trim().toUpperCase();
				if (!flowCode.equals("INDEM") && !flowCode.equals("FEEDM")) {
					// Skipping row due to invalid flow code
					System.out.println("Skipping record: flow_code not INDEM or FEEDM -> " + flowCode);
					continue;
				}

				System.out.println("Processing transaction with FLOW_CODE: " + flowCode);

				BigDecimal flowAmount = new BigDecimal(flowAmountStr.trim());
				LocalDate flowLocalDate = LocalDate.parse(flowDateStr.trim(), formatter);
				Date flowDate = Date.from(flowLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

				String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
				tranIdList.add(tranId);

				LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNumber.trim());
				
				  if (loanDetails == null) {
				  System.out.println("Loan account not found for Account Number: " +
				 accountNumber); continue; }
				 

				String flowDateStr1 = (String) data.get("flow_date"); // example: "19-09-2025"
				DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

				// ✅ Parse directly into java.util.Date without LocalDate
				Date flowDate11 = new SimpleDateFormat("dd-MM-yyyy").parse(flowDateStr1.trim());

				// If you need java.sql.Date for DB
				java.sql.Date sqlFlowDate = new java.sql.Date(flowDate.getTime());

				System.out.println("Flow Date (util.Date): " + flowDate);
				System.out.println("Flow Date (sql.Date): " + sqlFlowDate);

				// ✅ Set flags based on FLOW_CODE
				if ("INDEM".equals(flowCode)) {
					loanDetails.setInterest_flg("Y");
				} else if ("FEEDM".equals(flowCode)) {
					loanDetails.setFees_flg("Y");
				}
				lOAN_ACT_MST_REPO.save(loanDetails); // ✅ persist updated flags

				LocalDate monthEnd = flowLocalDate.withDayOfMonth(flowLocalDate.lengthOfMonth());
				Date tranDate = Date.from(monthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

				String flowParticular = loanDetails.getId()
						+ (flowCode.equals("INDEM") ? " Interest Demand" : " Fees Demand");

				// Debit Transaction
				TRAN_MAIN_TRM_WRK_ENTITY debit = new TRAN_MAIN_TRM_WRK_ENTITY();
				debit.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debit.setTran_id(tranId);
				debit.setPart_tran_id(BigDecimal.ONE);
				debit.setAcct_num(loanDetails.getId());
				debit.setAcct_name(loanDetails.getLoan_name());
				debit.setTran_type("TRANSFER");
				debit.setPart_tran_type("Debit");
				debit.setAcct_crncy(loanDetails.getCurrency_code());
				debit.setTran_amt(flowAmount);
				debit.setTran_particular(flowParticular);
				debit.setTran_remarks(flowParticular);
				debit.setTran_date(flowDate11);
				debit.setValue_date(flowDate);
				debit.setFlow_code(flowCode);
				debit.setFlow_date(flowDate);
				debit.setTran_status("POSTED");
				debit.setEntry_user(user);
				debit.setEntry_time(new Date());
				debit.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debit);
				System.out.println("Debit transaction saved: " + debit.getTran_id());

				// Credit Transaction
				Chart_Acc_Entity creditAcc = null;
				if (flowCode.equals("INDEM")) {
					creditAcc = chart_Acc_Rep.getaedit("4100004110");
				} else if (flowCode.equals("FEEDM")) {
					creditAcc = chart_Acc_Rep.getaedit("4200004210");
				}

				if (creditAcc == null) {
					System.out.println("Skipping record: Credit account not found for FLOW_CODE: " + flowCode);
					continue;
				}

				TRAN_MAIN_TRM_WRK_ENTITY credit = new TRAN_MAIN_TRM_WRK_ENTITY();
				credit.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				credit.setTran_id(tranId);
				credit.setPart_tran_id(BigDecimal.valueOf(2));
				credit.setAcct_num(creditAcc.getAcct_num());
				credit.setAcct_name(creditAcc.getAcct_name());
				credit.setTran_type("TRANSFER");
				credit.setPart_tran_type("Credit");
				credit.setAcct_crncy(creditAcc.getAcct_crncy());
				credit.setTran_amt(flowAmount);
				credit.setTran_particular(flowParticular);
				credit.setTran_remarks(flowParticular);
				credit.setTran_date(flowDate11);
				credit.setValue_date(flowDate);
				credit.setFlow_code(flowCode);
				credit.setFlow_date(flowDate);
				credit.setTran_status("POSTED");
				credit.setEntry_user(user);
				credit.setEntry_time(new Date());
			   credit.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(credit);
				System.out.println("Credit transaction saved: " + credit.getTran_id());
				    
	                totalAmount = totalAmount.add(flowAmount1);
	                totalRecords++;
			} catch (Exception e) {
				System.out.println("Exception while processing data: " + e.getMessage());
				e.printStackTrace();
				failedRecords++;
			}
			
		}
		 System.out.println(totalAmount );
		 System.out.println(totalRecords + failedRecords);
		 response.put("totalAmount", totalAmount);
	     response.put("totalRecords", totalRecords);
	     response.put("failedRecords", failedRecords);
	     response.put("error", "Invalid data or empty list.");
	    

		// Balance update
		for (String tranId : tranIdList) {
			List<TRAN_MAIN_TRM_WRK_ENTITY> transactions = tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tranId);

			for (TRAN_MAIN_TRM_WRK_ENTITY transaction : transactions) {
				transaction.setPost_user(user);
				transaction.setPost_time(new Date());
				transaction.setTran_status("POSTED");
				transaction.setDel_flg("N");

				Chart_Acc_Entity account = chart_Acc_Rep.getValuepopval(transaction.getAcct_num());
				if (account != null) {
					BigDecimal oldBalance = account.getAcct_bal();
					BigDecimal tranAmt = transaction.getTran_amt();

					if ("Debit".equalsIgnoreCase(transaction.getPart_tran_type())) {
						// ✅ Update DR_AMT
						BigDecimal oldDrAmt = account.getDr_amt() == null ? BigDecimal.ZERO : account.getDr_amt();
						account.setDr_amt(oldDrAmt.add(tranAmt));

						// ✅ Update Balance
						account.setAcct_bal(oldBalance.subtract(tranAmt));

						System.out.println("Account debited: " + account.getAcct_num() + ", Old Balance: " + oldBalance
								+ ", New Balance: " + account.getAcct_bal() + ", DR_AMT updated to: "
								+ account.getDr_amt());

					} else if ("Credit".equalsIgnoreCase(transaction.getPart_tran_type())) {
						// ✅ Update CR_AMT
						BigDecimal oldCrAmt = account.getCr_amt() == null ? BigDecimal.ZERO : account.getCr_amt();
						account.setCr_amt(oldCrAmt.add(tranAmt));

						// ✅ Update Balance
						account.setAcct_bal(oldBalance.add(tranAmt));

						System.out.println("Account credited: " + account.getAcct_num() + ", Old Balance: " + oldBalance
								+ ", New Balance: " + account.getAcct_bal() + ", CR_AMT updated to: "
								+ account.getCr_amt());
					}

					chart_Acc_Rep.save(account);
				} else {
					System.out.println("Account not found for balance update: " + transaction.getAcct_num());
				}
			}

			tRAN_MAIN_TRM_WRK_REP.saveAll(transactions);
		}
		 return ResponseEntity.ok(response);
	}

	@PostMapping("/saveBranchData3")
	public ResponseEntity<Map<String, Object>> saveBranchData3(@RequestBody List<Map<String, Object>> branchDataList,
			HttpServletRequest request) {
		if (branchDataList == null || branchDataList.isEmpty()) {

			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data or empty list.");
		}

		String user = (String) request.getSession().getAttribute("USERID");
		if (user == null || user.trim().isEmpty()) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not logged in.");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<String> tranIdList = new ArrayList<>();

		// --- Convert all flow_date strings to List<LocalDate> ---
		List<LocalDate> flowLocalDateList = branchDataList.stream().map(d -> (String) d.get("flow_date"))
				.filter(Objects::nonNull).map(s -> LocalDate.parse(s.trim(), formatter)).collect(Collectors.toList());

		System.out.println("All flow dates passed from frontend:");
		flowLocalDateList.forEach(ld -> System.out.println(ld.format(formatter)));

		// --- (optional) If you have a method to fetch repaid dates by those flow
		// dates, keep it.
		// (Kept from your original code though not strictly required for per-account
		// lookup)
		List<Date> repaymentDates = lOAN_REPAYMENT_REPO.findRepaidDatesForMultipleDates(
				flowLocalDateList.stream().map(ld -> Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()))
						.collect(Collectors.toList()));

		Map<LocalDate, LocalDate> flowDateToRepaidDateMap = new HashMap<>();
		for (LocalDate flowDate : flowLocalDateList) {
			LocalDate matchedRepaidDate = repaymentDates.stream()
					.map(d -> d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
					.filter(d -> !d.isAfter(flowDate)) // <= flowDate
					.findFirst().orElse(flowDate);
			flowDateToRepaidDateMap.put(flowDate, matchedRepaidDate);
		}

		System.out.println("Flow Date → Matched REPAID_DATE:");
		flowDateToRepaidDateMap.forEach(
				(flow, repaid) -> System.out.println(flow.format(formatter) + " → " + repaid.format(formatter)));

		// --- Process each branch data record ---

		int totalRecords = 0, failedRecords = 0 ;
		 BigDecimal totalAmount = BigDecimal.ZERO;
		 
		 Map<String, Object> response = new LinkedHashMap<>();
		for (Map<String, Object> data : branchDataList) {
			try {
				   String flowAmountStr1 = data.get("flow_amount").toString()
	                        .replace(",", "").trim();
	                BigDecimal flowAmount1 = new BigDecimal(flowAmountStr1);
	                
				String flowDateStr = (String) data.get("flow_date");
				String flowCode = (String) data.get("flow_code");
				String accountNumber = (String) data.get("account_number");
				String flowAmountStr = (String) data.get("flow_amount");

				
				  if (flowDateStr == null || flowCode == null || accountNumber == null ||
				 flowAmountStr == null || flowDateStr.trim().isEmpty() ||
				  flowCode.trim().isEmpty() || accountNumber.trim().isEmpty() ||
				 flowAmountStr.trim().isEmpty()) { continue; }
				 

				flowCode = flowCode.trim().toUpperCase();
				if (!flowCode.equals("INDEM") && !flowCode.equals("FEEDM") && !flowCode.equals("PRDEM")) {
					continue;
				}

				BigDecimal flowAmount = new BigDecimal(flowAmountStr.trim());
				LocalDate flowLocalDate = LocalDate.parse(flowDateStr.trim(), formatter);
				Date flowDate = Date.from(flowLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

				// --- REPAID DATE FETCH ---
				// DEFAULT: use flowLocalDate as fallback (converted to Date)
				Date tranDate2 = Date.from(flowLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				List<String> encodedKeys = lOAN_ACT_MST_REPO.getEncodedKeysByAccountId(accountNumber);
				List<LOAN_REPAYMENT_ENTITY> repaymentList = lOAN_REPAYMENT_REPO.findRepaidDates(encodedKeys);

				System.out.println(
						"Processing Account: " + accountNumber + ", Flow Date: " + flowLocalDate.format(formatter));

				for (LOAN_REPAYMENT_ENTITY repayment : repaymentList) {
					Date dueDateRaw = repayment.getDue_date();
					Date repaidDateRaw = repayment.getRepaid_date();

					if (dueDateRaw == null)
						continue;

					LocalDate dueDate = dueDateRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					if (dueDate.equals(flowLocalDate)) {
						// if REPAID_DATE available, convert and assign to tranDate2 (java.util.Date)
						if (repaidDateRaw != null) {
							LocalDate repaidLocal = repaidDateRaw.toInstant().atZone(ZoneId.systemDefault())
									.toLocalDate();
							tranDate2 = Date.from(repaidLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

							System.out.println("Matched Flow Date: " + flowLocalDate.format(formatter));
							System.out.println(" - DUE_DATE: " + dueDate.format(formatter) + ", REPAID_DATE: "
									+ repaidLocal.format(formatter));
							System.out
									.println("TranDate2 (Stored as java.util.Date): " + repaidLocal.format(formatter));
						} else {
							// repaid date null: keep fallback tranDate2 (flowLocalDate)
							System.out
									.println("Matched DUE_DATE but REPAID_DATE is null; using flow date as tranDate2");
						}
						break; // if you want first match only, else remove break
					}
				}

				// --- Transaction ID ---
				String tranId = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
				tranIdList.add(tranId);

				// --- Prepare Flow Particular ---
				String flowParticular = accountNumber + " - ";
				switch (flowCode) {
				case "INDEM":
					flowParticular += "Interest Recovery";
					break;
				case "FEEDM":
					flowParticular += "Fees Recovery";
					break;
				case "PRDEM":
					flowParticular += "Principle Recovery";
					break;
				}

				// --- Debit Transaction ---
				Chart_Acc_Entity debitAcc = chart_Acc_Rep.getValuepopval("2700002750");
				if (debitAcc == null)
					continue;

				TRAN_MAIN_TRM_WRK_ENTITY debit = new TRAN_MAIN_TRM_WRK_ENTITY();
				debit.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				debit.setTran_id(tranId);
				debit.setPart_tran_id(BigDecimal.ONE);
				debit.setTran_type("TRANSFER");
				debit.setPart_tran_type("Debit");
				debit.setAcct_num("2700002750");
				debit.setAcct_name("Loan Parking Account");
				debit.setAcct_crncy("INR");
				debit.setTran_amt(flowAmount);
				debit.setTran_particular(flowParticular);
				debit.setTran_remarks(flowParticular);
				debit.setTran_date(tranDate2); // <-- java.util.Date
				debit.setValue_date(flowDate);
				debit.setFlow_code(flowCode);
				debit.setFlow_date(flowDate);
				debit.setTran_status("POSTED");
				debit.setEntry_user(user);
				debit.setEntry_time(new Date());
				debit.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(debit);

				// --- Update Loan Recovery Flag ---
				LOAN_ACT_MST_ENTITY loanDetails = lOAN_ACT_MST_REPO.getLoanView(accountNumber.trim());
				if (loanDetails != null) {
					loanDetails.setRecovery_flg("Y");
					lOAN_ACT_MST_REPO.save(loanDetails);
				}

				// --- Credit Transaction ---
				Chart_Acc_Entity creditAcc = chart_Acc_Rep.getValuepopval(accountNumber.trim());
				if (creditAcc == null)
					continue;

				TRAN_MAIN_TRM_WRK_ENTITY credit = new TRAN_MAIN_TRM_WRK_ENTITY();
				credit.setSrl_no(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID());
				credit.setTran_id(tranId);
				credit.setPart_tran_id(BigDecimal.valueOf(2));
				credit.setTran_type("TRANSFER");
				credit.setPart_tran_type("Credit");
				credit.setAcct_num(creditAcc.getAcct_num());
				credit.setAcct_name(creditAcc.getAcct_name());
				credit.setAcct_crncy(creditAcc.getAcct_crncy());
				credit.setTran_amt(flowAmount);
				credit.setTran_particular(flowParticular);
				credit.setTran_remarks(flowParticular);
				credit.setTran_date(tranDate2); // <-- java.util.Date
				credit.setValue_date(flowDate);
				credit.setFlow_code(flowCode);
				credit.setFlow_date(flowDate);
				credit.setTran_status("POSTED");
				credit.setEntry_user(user);
				credit.setEntry_time(new Date());
				credit.setDel_flg("N");
				tRAN_MAIN_TRM_WRK_REP.save(credit);
				 totalRecords++;
				 totalAmount = totalAmount.add(flowAmount1);

			} catch (Exception e) {
				e.printStackTrace();
				response.put("error", "Invalid data or empty list.");
				failedRecords++;
			}
		}

		// --- Update Balances ---
		for (String tranId : tranIdList) {
		    List<TRAN_MAIN_TRM_WRK_ENTITY> transactions = tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tranId);
		    for (TRAN_MAIN_TRM_WRK_ENTITY transaction : transactions) {
		        transaction.setPost_user(user);
		        transaction.setPost_time(new Date());
		        transaction.setTran_status("POSTED");
		        transaction.setDel_flg("N");

		        Chart_Acc_Entity account = chart_Acc_Rep.getValuepopval(transaction.getAcct_num());
		        if (account != null) {
		            BigDecimal oldBalance = account.getAcct_bal() == null ? BigDecimal.ZERO : account.getAcct_bal();
		            BigDecimal tranAmt = transaction.getTran_amt() == null ? BigDecimal.ZERO : transaction.getTran_amt();

		            // Handle Debit
		            if ("Debit".equalsIgnoreCase(transaction.getPart_tran_type())) {
		                account.setDr_amt(
		                        (account.getDr_amt() == null ? BigDecimal.ZERO : account.getDr_amt()).add(tranAmt));
		                account.setAcct_bal(oldBalance.subtract(tranAmt)); // decrease balance
		            } 
		            // Handle Credit
		            else if ("Credit".equalsIgnoreCase(transaction.getPart_tran_type())) {
		                account.setCr_amt(
		                        (account.getCr_amt() == null ? BigDecimal.ZERO : account.getCr_amt()).add(tranAmt));
		                account.setAcct_bal(oldBalance.add(tranAmt)); // increase balance
		            }

		            chart_Acc_Rep.save(account);
		        }
		    }
		    tRAN_MAIN_TRM_WRK_REP.saveAll(transactions);
		}


		

		    response.put("totalAmount", totalAmount);
	        response.put("totalRecords", totalRecords);
	        response.put("failedRecords", failedRecords);
	        return ResponseEntity.ok(response);
	}

	// FEES LIST SHOW
	@RequestMapping(value = "/getBranchData21", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> getBranchData21(
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate) {

	    if (currentDate == null) {
	        System.out.println("No date provided.");
	        return new ArrayList<>();
	    }

	    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
	    System.out.println("Formatted SQL Date for DB: " + sqlDate);

	    // Fetch loan account data
	    List<LOAN_ACT_MST_ENTITY> loanActList = lOAN_ACT_MST_REPO.getLoanActDetval21(sqlDate);

	    // Collect encoded keys
	    List<String> encodedKeyList = loanActList.stream()
	            .map(LOAN_ACT_MST_ENTITY::getEncoded_key)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    if (encodedKeyList.isEmpty()) {
	        System.out.println("No Encoded Keys Found.");
	        return new ArrayList<>();
	    }

	    // Batch size for Oracle IN clause
	    int batchSize = 1000;
	    List<Object[]> repaymentList = new ArrayList<>();

	    for (int i = 0; i < encodedKeyList.size(); i += batchSize) {
	        List<String> batch = encodedKeyList.subList(i, Math.min(i + batchSize, encodedKeyList.size()));
	        repaymentList.addAll(lOAN_REPAYMENT_REPO.findByParentAccountKeyInAndDueDateLessThanEqual(batch, sqlDate));
	    }

	    System.out.println("Repayment Size: " + repaymentList.size());
	    return repaymentList;
	}


	// LIST SHOW
	@RequestMapping(value = "/getBranchData31", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> getBranchData31(
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate) {

	    if (currentDate == null) {
	        System.out.println("No date provided.");
	        return new ArrayList<>();
	    }

	    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
	    System.out.println("Formatted SQL Date for DB: " + sqlDate);

	    // Fetch loan account data
	    List<LOAN_ACT_MST_ENTITY> loanActList = lOAN_ACT_MST_REPO.getLoanActDetval31(sqlDate);

	    // Collect encoded keys
	    List<String> encodedKeyList = loanActList.stream()
	            .map(LOAN_ACT_MST_ENTITY::getEncoded_key)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    if (encodedKeyList.isEmpty()) {
	        System.out.println("No Encoded Keys Found.");
	        return new ArrayList<>();
	    }

	    // Oracle IN clause supports max 1000 expressions
	    int batchSize = 1000;
	    List<Object[]> repaymentList = new ArrayList<>();

	    for (int i = 0; i < encodedKeyList.size(); i += batchSize) {
	        List<String> batch = encodedKeyList.subList(i, Math.min(i + batchSize, encodedKeyList.size()));
	        repaymentList.addAll(lOAN_REPAYMENT_REPO.findByParentAccountKeyInAndDueDateLessThanEqualpaid(batch, sqlDate));
	    }

	    System.out.println("Repayment Size: " + repaymentList.size());
	    return repaymentList;
	}

	@RequestMapping(value = "/getBranchData11", method = RequestMethod.GET)
	@ResponseBody
	public List<Object[]> getBranchData11(
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate) {

		if (currentDate != null) {
			// Convert to SQL date to remove time
			java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
			System.out.println("Formatted SQL Date for DB: " + sqlDate);

			// Get loan account data
			List<LOAN_ACT_MST_ENTITY> loanActList = lOAN_ACT_MST_REPO.getLoanActDetval41(sqlDate);

			// Create list of encoded keys
			List<String> encodedKeyList = new ArrayList<>();
			for (LOAN_ACT_MST_ENTITY entity : loanActList) {
				if (entity.getEncoded_key() != null) {
					System.out.println("Encoded Key: " + entity.getEncoded_key());
					encodedKeyList.add(entity.getEncoded_key());
				}
			}

			// If keys are found, fetch repayment data
			if (!encodedKeyList.isEmpty()) {
				List<Object[]> repaymentList = lOAN_REPAYMENT_REPO
						.findByParentAccountKeyInAndDueDateLessThanEqualdue(sqlDate);

				// Log result
				System.out.println("Repayment Size: " + repaymentList.size());
				return repaymentList;
			} else {
				System.out.println("No Encoded Keys Found.");
				return new ArrayList<>();
			}
		} else {
			System.out.println("No date provided.");
			return new ArrayList<>();
		}
	}

	@GetMapping("getloanclosetdatas5211")
	public Map<String, Object> getloanclosetdatas5211(@RequestParam(required = false) String accountNumber) {
		System.out.println("THE GETTING ACCOUNT NUMBER IS HERE " + accountNumber);

		double totalFlowAmount = 0.0; // Initialize total sum

		List<Object[]> loanFlowRecords = lOAN_REPAYMENT_REPO.getloanflowsvaluedatas511(accountNumber);
		List<Map<String, Object>> formattedRecords = new ArrayList<>();

		// Fetch latest TRAN_DATE from BGLS_CONTROL_TABLE
		Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
		System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

		if (tranDateObj == null) {
			throw new IllegalStateException("TRAN_DATE cannot be null.");
		}

		// Convert TRAN_DATE correctly
		LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
				: tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Find latest due_date before tranDate and its corresponding INDEM amount
		LocalDate latestDueDateBeforeTranDate = null;
		BigDecimal indemAmount = BigDecimal.ZERO;
		BigDecimal perDayInterest = BigDecimal.ZERO;

		for (Object[] record : loanFlowRecords) {
			LocalDate dueDate = ((Date) record[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String flowCode = (String) record[2];

			if (dueDate.isBefore(tranDate) && "INDEM".equals(flowCode)) {
				BigDecimal flowAmt = convertToBigDecimal(record[3]);

				if (latestDueDateBeforeTranDate == null || dueDate.isAfter(latestDueDateBeforeTranDate)) {
					latestDueDateBeforeTranDate = dueDate;
					indemAmount = flowAmt;
				}
			}
		}

		// Calculate per-day interest
		if (latestDueDateBeforeTranDate != null) {
			YearMonth yearMonth = YearMonth.from(latestDueDateBeforeTranDate);
			int totalDaysInMonth = yearMonth.lengthOfMonth();
			perDayInterest = indemAmount.divide(BigDecimal.valueOf(totalDaysInMonth), 2, RoundingMode.HALF_UP);
		}

		System.out.println("Latest due_date before tranDate: " + latestDueDateBeforeTranDate);
		System.out.println("Monthly INDEM Amount: " + indemAmount);
		System.out.println("Per-Day Interest: " + perDayInterest);

		for (Object[] record : loanFlowRecords) {
			Map<String, Object> map = new HashMap<>();
			LocalDate flowDate = ((Date) record[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String flowCode = (String) record[2];
			BigDecimal flowAmt = convertToBigDecimal(record[3]);

			// Apply interest calculation **ONLY for INDEM where flow_date == tran_date**
			if ("INDEM".equals(flowCode) && flowDate.equals(tranDate) && latestDueDateBeforeTranDate != null) {
				long noOfDays = ChronoUnit.DAYS.between(latestDueDateBeforeTranDate, tranDate);
				noOfDays = Math.max(noOfDays, 1);

				BigDecimal calculatedInterest = perDayInterest.multiply(BigDecimal.valueOf(noOfDays));
				calculatedInterest = customRound(calculatedInterest);

				// Replace INDEM amount with calculated interest
				map.put("flow_amt", calculatedInterest);
				totalFlowAmount += calculatedInterest.doubleValue();

				System.out.println("Replacing INDEM amount with calculated interest: " + calculatedInterest);
			} else {
				map.put("flow_amt", flowAmt);
				totalFlowAmount += flowAmt.doubleValue();
			}

			map.put("flow_date", flowDate);
			map.put("flow_id", record[1]);
			map.put("flow_code", flowCode);
			map.put("loan_acct_no", record[4]);
			map.put("acct_name", record[5]);
			map.put("encoded_key", record[6]);

			formattedRecords.add(map);
		}

		// Return the response with total flow amount and loan flow records
		Map<String, Object> response = new HashMap<>();
		response.put("flow_total_amt", totalFlowAmount);
		response.put("loan_flows", formattedRecords);

		return response;
	}


// SURESH
@RequestMapping(value = "customer/refUpdate", method = RequestMethod.POST)
@ResponseBody
public String refUpdate(@ModelAttribute Reference_Code_Entity formEntity) {
    // Fetch existing record using your custom repository method
	System.out.println(formEntity.getRef_id());
    Reference_Code_Entity existing = reference_code_Rep.getRefById(formEntity.getRef_id());
    if (existing == null) {
        return "Record not found";
    }

    // Update editable fields
    existing.setRef_type(formEntity.getRef_type());
    existing.setRef_type_desc(formEntity.getRef_type_desc());
    existing.setRef_id_desc(formEntity.getRef_id_desc());
    existing.setModule_id(formEntity.getModule_id());
    existing.setRemarks(formEntity.getRemarks());

    // Save the updated record
    reference_code_Rep.save(existing);

    return "Successfully Updated";
}


@RequestMapping(value = "customer/refDelete", method = RequestMethod.POST)
@ResponseBody
public String refDelete(@ModelAttribute Reference_Code_Entity formEntity) {

    // Fetch existing record using your custom repository method
    System.out.println("Deleting ref_id: " + formEntity.getRef_id());
    Reference_Code_Entity existing = reference_code_Rep.getRefById(formEntity.getRef_id());
    if (existing == null) {
        return "Record not found";
    }

    // Delete the record
    reference_code_Rep.delete(existing);

    return "Successfully Deleted";
}

@RequestMapping(value = "lms_scheme_Add", method = RequestMethod.POST)
public String lmsSchemesAdd(@RequestBody BglsLmsSchemesEntity request,HttpServletRequest session) throws ParseException {
	
	String SessionUserId = (String) session.getSession().getAttribute("USERID"); 
	request.setEntryUser(SessionUserId);
	request.setEntryTime(new Date());
	request.setDelFlg("N");    
	request.setEntityFlg("Y");
	request.setModifyFlg("N");
	request.setVerifyFlg("Y");
	request.setCreationDate(new Date());
	
	
	
	try {
		bgls_lms_scheme_repo.save(request);
	}catch (Exception e) {
        e.printStackTrace(); // Log full error
        return "Error while saving: " + e.getMessage(); // Return to frontend
    }
 
    return "Successfully Added";
}

@RequestMapping(value = "parametersdelete", method = RequestMethod.POST)
@ResponseBody
public String parameterDelete(@RequestParam("id") String id) {
    System.out.println("Deleting parameter with ID: " + id);

    // Check if record exists
    List<BglsLmsSchemesEntity> existing = bgls_lms_scheme_repo.findByUniqueId(id);
    if (existing == null || existing.isEmpty()) {
        return "Record not found";
    }

    // Perform delete
    int rows = bgls_lms_scheme_repo.deleteByUniqueId(id);

    if (rows > 0) {
        return "Successfully Deleted";
    } else {
        return "Delete failed";
    }
}




//
//@RequestMapping(value = "parametersupdate", method = RequestMethod.POST)
//@ResponseBody
//public String updateParameter(@ModelAttribute BglsLmsSchemesEntity formEntity) {
//    System.out.println("Updating parameter with ID: " + formEntity.getId());
//
//    // Fetch existing record by ID
//    List<BglsLmsSchemesEntity> existingList = bgls_lms_scheme_repo.findByUniqueId(formEntity.getId());
//    if (existingList == null || existingList.isEmpty()) {
//        return "Record not found in PARAMETER ";
//    }
//
//    BglsLmsSchemesEntity existing = existingList.get(0);
//
//    try {
//        // Dynamically update only non-null fields
//        Field[] fields = BglsLmsSchemesEntity.class.getDeclaredFields();
//        for (Field field : fields) {
//            field.setAccessible(true);
//            Object value = field.get(formEntity);
//
//            // Skip ID and autogenerated field
//            if (value != null && !field.getName().equals("unique_id")) {
//                field.set(existing, value);
//            }
//        }
//
//        // Always update lastModifiedDate
//        existing.setLastModifiedDate(new Date());
//
//        // Save the updated entity
//        bgls_lms_scheme_repo.save(existing);
//
//    } catch (IllegalAccessException e) {
//        e.printStackTrace();
//        return "Update failed: " + e.getMessage();
//    }
//
//    return "Successfully Updated";
//}


@RequestMapping(value = "parametersupdate", method = RequestMethod.POST)
@ResponseBody
public String updateParameter(@ModelAttribute BglsLmsSchemesEntity formEntity) {
    System.out.println("Updating parameter with ID: " + formEntity.getId());

    // Fetch existing record by ID
    List<BglsLmsSchemesEntity> existingList = bgls_lms_scheme_repo.findByUniqueId(formEntity.getId());
    if (existingList == null || existingList.isEmpty()) {
        return "Record not found in PARAMETER ";
    }

    BglsLmsSchemesEntity existing = existingList.get(0);

    try {
        Field[] fields = BglsLmsSchemesEntity.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(formEntity);

            if (value != null && !field.getName().equals("unique_id")) {

                // Handle Integer fields
                if (field.getType() == Integer.class && value instanceof String) {
                    field.set(existing, Integer.parseInt((String) value));
                }
                // Handle Double fields
                else if (field.getType() == Double.class && value instanceof String) {
                    field.set(existing, Double.parseDouble((String) value));
                }
                // Handle Date fields
                else if (field.getType() == Date.class && value instanceof String) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    field.set(existing, sdf.parse((String) value));
                }
                // Default: String or other types
                else {
                    field.set(existing, value);
                }
            }
        }

        // Always update lastModifiedDate
        existing.setLastModifiedDate(new Date());

        // Save updated entity
        bgls_lms_scheme_repo.save(existing);

    } catch (Exception e) {
        e.printStackTrace();
        return "Update failed: " + e.getMessage();
    }

    return "Successfully Updated";
}


@RequestMapping(value = "verifyScheme", method = RequestMethod.POST)
@ResponseBody
public Map<String, Object> verifyScheme(@RequestBody Map<String, String> payload) {
    Map<String, Object> response = new HashMap<>();
    try {
        String id = payload.get("id");

        if (id == null || id.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid scheme ID"); 
            return response;
        }

        // Fetch entity by ID (take the first result if list not empty)
        List<BglsLmsSchemesEntity> schemes = bgls_lms_scheme_repo.findByUniqueId(id);
        if (schemes == null || schemes.isEmpty()) {
            response.put("success", false);
            response.put("message", "Scheme not found");
            return response;
        }

        BglsLmsSchemesEntity scheme = schemes.get(0);

        // Update flags
        scheme.setModifyFlg("N");
        scheme.setVerifyFlg("Y");

        // Save entity
        bgls_lms_scheme_repo.save(scheme);

        response.put("success", true);
    } catch (Exception e) {
        response.put("success", false);
        response.put("message", e.getMessage());
    }
    return response;
}



@RequestMapping("/api/loans")
public Map<String, Object> getLoans(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "200") int limit) {

    if (page < 1) page = 1;
    int offset = (page - 1) * limit;

    List<Object[]> rows = lOAN_ACT_MST_REPO.getLoanActWithMobile(offset, limit);
    Long totalRecords = lOAN_ACT_MST_REPO.getTotalLoans();
    int totalPages = (int) Math.ceil((double) totalRecords / limit);

//    for (Object[] row : rows) {
//        for (int i = 0; i < row.length; i++) {
//            System.out.println("Column " + i + ": " + row[i]);
//        }
//        System.out.println("=================================");
//    }
    
    List<Map<String, Object>> result = new ArrayList<>();

    for (Object[] row : rows) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", row[1]);               // l.ID
        map.put("account_holderkey", row[3]); // adjust index to match your query
        map.put("loan_name", row[12]);
        map.put("retailer_name", row[64]);
        map.put("retailer_branch", row[65]);
        map.put("account_state", row[9]);
        map.put("last_name", row[row.length - 1]);
        map.put("mobile_phone", row[row.length - 3]);
        map.put("first_name", row[row.length - 2]);
      
        
        System.out.println(row[row.length - 2]);
        result.add(map);
    }
    // Prepare JSON response
    Map<String, Object> response = new HashMap<>();
    response.put("data", result);
    response.put("currentPage", page);
    response.put("totalPages", totalPages);
    response.put("totalRecords", totalRecords);

    return response;
}


//@GetMapping("customers/search")
//public List<CLIENT_MASTER_ENTITY> searchCustomers(@RequestParam String customerId) {
//    if (customerId == null || customerId.trim().isEmpty()) {
//        return clientMasterRepo.findAll(); // return all if nothing entered
//    }
//    return clientMasterRepo.searchByCustomerIdLike(customerId);
//}


@GetMapping("loan/search")
public List<Map<String, Object>> searchLoan(@RequestParam String loanId) {
	List<Object[]> rows;
    if (loanId == null || loanId.trim().isEmpty()) {
    	rows= lOAN_ACT_MST_REPO.getLoanActWithMobile(0, 200); // default 200 rows
    }else {
    	rows = lOAN_ACT_MST_REPO.searchByLoanIdLike(loanId.trim());
    }
    
    List<Map<String, Object>> result = new ArrayList<>();

    for (Object[] row : rows) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", row[1]);               // l.ID
        map.put("account_holderkey", row[3]); // adjust index to match your query
        map.put("loan_name", row[12]);
        map.put("retailer_name", row[64]);
        map.put("retailer_branch", row[65]);
        map.put("account_state", row[9]);
        map.put("last_name", row[row.length - 1]);
        map.put("mobile_phone", row[row.length - 3]);
        map.put("first_name", row[row.length - 2]);
        result.add(map);
    }

    return result;
}

//@GetMapping("customers/mobilesearch")
//public List<CLIENT_MASTER_ENTITY> searchMobileCustomers(@RequestParam String mobile) {
//    if (mobile == null || mobile.trim().isEmpty()) {
//        return clientMasterRepo.findAll(); // return all if nothing entered
//    }
//    return clientMasterRepo.searchByMobileLike(mobile);
//}
//
//@GetMapping("customers/emailsearch")
//public List<CLIENT_MASTER_ENTITY> searchEmailCustomers(@RequestParam String email) {
//    if (email == null || email.trim().isEmpty()) {
//        return clientMasterRepo.findAll(); // return all if nothing entered
//    }
//    return clientMasterRepo.searchByEmailLike(email);
//}

@GetMapping("customers/statusSearch")
public List<CLIENT_MASTER_ENTITY> searchByStatus(@RequestParam String status) {
    return clientMasterRepo.findByStatus(status);
}

@GetMapping("loan/statusSearch")
public List<Map<String, Object>> searchByStatusLoan(@RequestParam String status) {
	List<Object[]> rows;
    	rows= lOAN_ACT_MST_REPO.getLoanActWithStatus(status); // default 200 rows
    
    List<Map<String, Object>> result = new ArrayList<>();

    for (Object[] row : rows) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", row[1]);               // l.ID
        map.put("account_holderkey", row[3]); // adjust index to match your query
        map.put("loan_name", row[12]);
        map.put("retailer_name", row[64]);
        map.put("retailer_branch", row[65]);
        map.put("account_state", row[9]);
        map.put("last_name", row[row.length - 1]);
        map.put("mobile_phone", row[row.length - 3]);
        map.put("first_name", row[row.length - 2]);
        result.add(map);
    }

    return result;
}

@GetMapping("chartaccounts/filter")
public List<Chart_Acc_Entity> filterChartAccounts(@RequestParam String type) {
    if (type.equals("O")) {
        return chart_Acc_Rep.getListoffice1();
    } else if (type.equals("C")) {
        return chart_Acc_Rep.getListofCustomer();
    }
    return chart_Acc_Rep.findAll();
}

@GetMapping("customers/mobilesearch")
public List<CLIENT_MASTER_ENTITY> searchMobileCustomers(@RequestParam String mobile,
                                                        @RequestParam(required = false) String status) {
    if (((status == null || status.trim().isEmpty() || status.equals("Status")))) {
        return clientMasterRepo.searchByMobileLike(mobile); // default fetch first 2000
    }
    return clientMasterRepo.searchByMobileAndStatus(mobile, status);
}

@GetMapping("customers/emailsearch")
public List<CLIENT_MASTER_ENTITY> searchEmailCustomers(@RequestParam String email,
                                                       @RequestParam(required = false) String status) {
    if (((status == null || status.trim().isEmpty() || status.equals("Status")))) {
        return clientMasterRepo.searchByEmailLike(email);
    }
    return clientMasterRepo.searchByEmailAndStatus(email, status);
}

@GetMapping("customers/search")
public List<CLIENT_MASTER_ENTITY> searchCustomers(@RequestParam String customerId,
                                                  @RequestParam(required = false) String status) {
    if (((status == null || status.trim().isEmpty() || status.equals("Status")))) {
        return clientMasterRepo.searchByCustomerIdLike(customerId);
    }
    return clientMasterRepo.searchByCustomerIdAndStatus(customerId, status);
}



}
