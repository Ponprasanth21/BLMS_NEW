package com.bornfire.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.entities.Access_Role_Repo;
import com.bornfire.entities.Account_Ledger_Entity;
import com.bornfire.entities.Account_Ledger_Rep;
import com.bornfire.entities.Assosiate_Profile_Entity;
import com.bornfire.entities.Assosiate_Profile_Repo;
import com.bornfire.entities.BACP_CUS_PROFILE_REPO;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.BGLSBusinessTable_Entity;
import com.bornfire.entities.BGLSBusinessTable_Rep;
import com.bornfire.entities.BGLS_BAMAssetFlows_Entity;
import com.bornfire.entities.BGLS_BAMAssetFlows_Rep;
import com.bornfire.entities.BGLS_BAMInventorymaster;
import com.bornfire.entities.BGLS_BAMInventryMastRep;
import com.bornfire.entities.BGLS_Bamcatcodemaintainrep;
import com.bornfire.entities.BGLS_Bamcategorycodemain_entity;
import com.bornfire.entities.BGLS_CONTROL_TABLE_REP;
import com.bornfire.entities.BGLS_Control_Table;
import com.bornfire.entities.BGLS_Journal_History;
import com.bornfire.entities.BGLS_Journal_History_Rep;
import com.bornfire.entities.BGLS_LMS_SCHEMES_TABLE_ENTITY;
import com.bornfire.entities.BGLS_LMS_SCHEMES_TABLE_REPO;
import com.bornfire.entities.BGLS_ORG_BRANCH_ENTITY;
import com.bornfire.entities.BGLS_ORG_BRANCH_REPO;
import com.bornfire.entities.Baj_Work_Repo;
import com.bornfire.entities.BamDocumentMasRep;
import com.bornfire.entities.Bamdocumentmanager;
import com.bornfire.entities.BglsLmsSchemesRepo;
import com.bornfire.entities.Budget_Maintanance_Repo;
import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.COAGL_Rep;
import com.bornfire.entities.CalenderMaintanceEntity;
import com.bornfire.entities.CalenderMaintanceRepo;
import com.bornfire.entities.CandEvalFormEntity;
import com.bornfire.entities.CandEvalFormRep;
import com.bornfire.entities.Chart_Acc_Entity;
import com.bornfire.entities.Chart_Acc_Rep;
import com.bornfire.entities.Collateral_management_Entity;
import com.bornfire.entities.Collateral_management_Repo;
import com.bornfire.entities.Collection_Process_Repo;
import com.bornfire.entities.CustomerRequest;
import com.bornfire.entities.DAB_Entity;
import com.bornfire.entities.DAB_Repo;
import com.bornfire.entities.DMD_TABLE;
import com.bornfire.entities.DMD_TABLE_REPO;
import com.bornfire.entities.DepositEntity;
import com.bornfire.entities.DepositRep;
import com.bornfire.entities.Employee_Profile;
import com.bornfire.entities.Employee_Profile_Rep;
import com.bornfire.entities.GeneralLedgerEntity;
import com.bornfire.entities.GeneralLedgerRep;
import com.bornfire.entities.GeneralLedgerWork_Entity;
import com.bornfire.entities.GeneralLedgerWork_Rep;
import com.bornfire.entities.HolidayMaster_Entity;
import com.bornfire.entities.HolidayMaster_Rep;
import com.bornfire.entities.LOAN_ACT_MST_ENTITY;
import com.bornfire.entities.LOAN_ACT_MST_REPO;
import com.bornfire.entities.LOAN_REPAYMENT_REPO;
import com.bornfire.entities.Lease_Loan_Master_Entity;
import com.bornfire.entities.Lease_Loan_Master_Repo;
import com.bornfire.entities.Lease_Loan_Work_Repo;
import com.bornfire.entities.MULTIPLE_TRANSACTION_ENTITY;
import com.bornfire.entities.MULTIPLE_TRANSACTION_REPO;
import com.bornfire.entities.NoticeDetailsGeneral0Rep;
import com.bornfire.entities.NoticeDetailsPayment0Rep;
import com.bornfire.entities.NoticeDetailsSlabDetails0Rep;
import com.bornfire.entities.Organization_Branch_Rep;
import com.bornfire.entities.Organization_Entity;
import com.bornfire.entities.Organization_Repo;
import com.bornfire.entities.ParticipatingBanks_Repo;
import com.bornfire.entities.PerdiemMasterRep;
import com.bornfire.entities.Principle_and_intrest_shedule_Entity;
import com.bornfire.entities.Principle_and_intrest_shedule_Rep;
import com.bornfire.entities.ProfileManagerRep1;
import com.bornfire.entities.Reference_code_Rep;
import com.bornfire.entities.SL_TRAN_ENTITY_REP;
import com.bornfire.entities.Salary_Pay_Rep;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_ENTITY;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_REP;
import com.bornfire.entities.Td_defn_Repo;
import com.bornfire.entities.Test_Collection_Entity;
import com.bornfire.entities.UserProfile;
import com.bornfire.entities.UserProfileRep;
import com.bornfire.entities.paystructureentity;
import com.bornfire.entities.paystructurerep;
import com.bornfire.services.*;
import com.monitorjbl.xlsx.exceptions.ParseException;

import net.sf.jasperreports.engine.JRException;

@Controller
@ConfigurationProperties("default")
public class BGLSNavigationController {

    private static final Logger logger = LoggerFactory.getLogger(BGLSNavigationController.class);

    @Autowired
    LoginServices loginServices;

    @Autowired
    BGLS_ORG_BRANCH_REPO BGLS_ORG_BRANCH_REPO;

    @Autowired
    Collateral_management_Repo collateral_management_Repo;

    @Autowired
    MULTIPLE_TRANSACTION_REPO MULTIPLE_TRANSACTION_REPO;

    @Autowired
    DataSource srcdataSource;

    @Autowired
    MultipleTransactionService multipleTransactionService;

    @Autowired
    LOAN_ACT_MST_REPO loan_act_mst_repo;

    @Autowired
    CLIENT_MASTER_REPO client_mst_repo;

    @Autowired
    Chart_Acc_Rep chart_Acc_Rep;

    @Autowired
    CandEvalFormRep candEvalFormRep;

    @Autowired
    Salary_Pay_Rep salary_Pay_Rep;

    @Autowired
    CalenderMaintanceRepo calenderMaintanceRepo;

    @Autowired
    Assosiate_Profile_Repo assosiate_Profile_Repo;

    @Autowired
    ProfileManagerRep1 profileManagerRep1;

    @Autowired
    paystructurerep Paystructurerep;

    SessionFactory sessionFactory;

    String pagesize;

    @Autowired
    UserProfileRep userProfileRep;

    @Autowired
    Account_Ledger_Rep account_Ledger_Rep;

    @Autowired
    PerdiemMasterRep perdiemMasterRep;

    @Autowired
    Baj_Work_Repo Baj_Work_Repo;

    @Autowired
    Employee_Profile_Rep employee_Profile_Rep;

    @Autowired
    Organization_Repo organization_Repo;

    @Autowired
    Reference_code_Rep reference_code_Rep;

    @Autowired
    Budget_Maintanance_Repo budget_Maintanance_Repo;

    @Autowired
    Organization_Branch_Rep organization_Branch_Rep;

    @Autowired
    Access_Role_Repo access_Role_Repo;

    @Autowired
    GeneralLedgerRep generalLedgerRep;

    @Autowired
    AdminOperServices adminOperServices;

    @Autowired
    AuditConfigure AuidtConfigure;

    @Autowired
    BamDocumentMasRep BAMDocmastrep;

    @Autowired
    BGLS_BAMInventryMastRep BGLS_BAMInventryMastRep;
    @Autowired
    BGLS_Bamcatcodemaintainrep BGLS_Bamcatcodemaintainrep;
    @Autowired
    BGLS_Inventeryservice BGLS_Inventeryservice;

    @Autowired
    BGLS_BAMAssetFlows_Rep BGLS_BAMAssetFlows_Rep;

    @Autowired
    BGLSAuditTable_Rep bGLSAuditTable_Rep;

    @Autowired
    DepositRep depositRep;

    @Autowired
    NoticeDetailsGeneral0Rep noticeDetailsGeneral0Rep;

    @Autowired
    NoticeDetailsPayment0Rep noticeDetailsPayment0Rep;

    @Autowired
    NoticeDetailsSlabDetails0Rep noticeDetailsSlabDetails0Rep;

    @Autowired
    BGLSBusinessTable_Rep bglsBusinessTable_Rep;

    @Autowired
    BGLS_Inventeryservice bGLS_Inventeryservice;

    @Autowired
    Td_defn_Repo td_defn_Repo;

    @Autowired
    SL_TRAN_ENTITY_REP sl_tran_rep;

    @Autowired
    Principle_and_intrest_shedule_Rep principle_and_intrest_shedule_Rep;

    @Autowired
    BACP_CUS_PROFILE_REPO bACP_CUS_PROFILE_REPO;

    @Autowired
    TRAN_MAIN_TRM_WRK_REP tRAN_MAIN_TRM_WRK_REP;

    @Autowired
    Lease_Loan_Work_Repo lease_Loan_Work_Repo;

    @Autowired
    Lease_Loan_Master_Repo lease_Loan_Master_Repo;

    @Autowired
    DAB_Repo dAB_Repo;

    @Autowired
    COAGL_Rep cOAGL_Rep;

    @Autowired
    BGLS_Inventeryservice bGLS_Inventeryservice2;

    @Autowired
    TRAN_MAIN_TRM_WRK_REP tranMainRep;

    @Autowired
    GeneralLedgerWork_Rep generalLedgerWork_Rep;

    @Autowired
    BGLS_CONTROL_TABLE_REP bGLS_CONTROL_TABLE_REP;

    @Autowired
    ParticipatingBanks_Repo participatingBanks_Repo;
    @Autowired
    com.bornfire.services.CustomerRequestService CustomerRequestService;
    @Autowired
    Collection_Process_Repo collection_Process_Repo;
    @Autowired
    DMD_TABLE_REPO dMD_TABLE_REPO;

    @Autowired
    BGLS_Journal_History_Rep bGLS_Journal_History_Rep;

    @Autowired
    AdminOperServices AdminOperServices;

    @Autowired
    HolidayMaster_Rep holidayMaster_Rep;

    @Autowired
    LOAN_ACT_MST_REPO LOAN_ACT_MST_REPO;

    @Autowired
    com.bornfire.entities.BglsHolidayMasterRep bglsHolidayMasterRep;

    @Autowired
    BglsLmsSchemesRepo bglsLmsSchemesRepo;

    @Autowired
    DateChangeService DateChangeService;

    @Autowired
    BGLS_LMS_SCHEMES_TABLE_REPO lmsschemerepo;

    @Autowired
    com.bornfire.services.ExelDownloadService ExelDownloadService;

    @Autowired
    LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;

    @Autowired
    TRAN_MAIN_TRM_WRK_REP TRAN_MAIN_TRM_WRK_REP;

    @Autowired
    AuditConfigure audit;



    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    @RequestMapping(value = "Dashboard", method = { RequestMethod.GET, RequestMethod.POST })
    public String getdashboard(Model md, HttpServletRequest req) {

        String userid = (String) req.getSession().getAttribute("USERID");
        System.out.println(userid + "userid");
        md.addAttribute("RoleMenu", access_Role_Repo.getRole(userid));
        md.addAttribute("menu", "BAJHeaderMenu");

        int completed = 0;
        int uncompleted = 0;
        md.addAttribute("reportList", "");
        md.addAttribute("completed", completed);
        md.addAttribute("uncompleted", uncompleted);
        md.addAttribute("menu", "Dashboard");
        return "BGLSDashboard";

    }

    @RequestMapping(value = "passwordReset", method = RequestMethod.POST)
    @ResponseBody
    public String passwordReset(@ModelAttribute UserProfile userprofile, Model md, HttpServletRequest rq) {
        String userid = (String) rq.getSession().getAttribute("USERID");
        String msg = loginServices.passwordReset(userprofile, userid);

        return msg;
    }

    @RequestMapping(value = "login?logout", method = RequestMethod.POST)
    @ResponseBody
    public String logoutUpdate(HttpServletRequest req) {

        String msg;

        String userid = (String) req.getSession().getAttribute("USERID");

        try {
            logger.info("Updating Logout");
            loginServices.SessionLogging("LOGOUT", "M0", req.getSession().getId(), userid, req.getRemoteAddr(),
                    "IN-ACTIVE");
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        return msg;
    }

    /* PRAVEEN */
    @RequestMapping(value = "organizationDetails", method = { RequestMethod.GET, RequestMethod.POST })
    public String organizationDetails(@RequestParam(required = false) String formmode,
                                      @RequestParam(required = false) String branch_name, String branch_code, Model md, HttpServletRequest req,
                                      @RequestParam(required = false) Long record_srl,
                                      @RequestParam(required = false) String month,
                                      @RequestParam(required = false) String year) {

        String userid = (String) req.getSession().getAttribute("USERID");
        md.addAttribute("menu", "BAJHeaderMenu");

        if (formmode == null || formmode.equals("add")) {
            md.addAttribute("formmode", "add");
            Organization_Entity organizationList = null;
            List<Organization_Entity> organization = organization_Repo.getAllList();
            if (!organization.isEmpty()) {
                organizationList = organization.get(0);
            }
            md.addAttribute("organization", organizationList);

            md.addAttribute("OrgBranch", organization_Branch_Rep.getbranchlist());

        } else if (formmode.equals("ModifyHead")) {
            md.addAttribute("formmode", "ModifyHead");
            Organization_Entity organizationList = null;
            List<Organization_Entity> organization = organization_Repo.getAllList();
            if (!organization.isEmpty()) {
                organizationList = organization.get(0);
            }
            md.addAttribute("organization", organizationList);
        } else if (formmode.equals("DeleteBranch")) {
            md.addAttribute("formmode", "DeleteBranch");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch1(branch_code));
        } else if (formmode.equals("AddBranch")) {
            md.addAttribute("formmode", "AddBranch");
        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch1(branch_code));

        } else if (formmode.equals("ModifyBranch")) {
            md.addAttribute("formmode", "ModifyBranch");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getbranchlist());

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch1(branch_code));

        } else if (formmode.equals("addholiday")) {
            md.addAttribute("formmode", "addholiday");

        } else if (formmode.equals("UploadHoliday")) {
            md.addAttribute("formmode", "UploadHoliday");

        }
//        else if (formmode.equals("ModifyHoliday")) {
//			md.addAttribute("formmode", "ModifyHoliday");
//
//		}
        else if (formmode.equals("listholiday") || formmode.equals("ModifyHoliday")) {
            md.addAttribute("formmode", "listholiday");
            md.addAttribute("Listofvalues", holidayMaster_Rep.getlistofHoliday());

        } else if (formmode.equals("viewrecord") || formmode.equals("modifyholidayrecord")) {

            md.addAttribute("formmode", formmode);
            md.addAttribute("holiday_id", record_srl);
            md.addAttribute("singlerecord", holidayMaster_Rep.getsinglevalueHoliday(record_srl));

        } else if (formmode.equals("calender")) {
            md.addAttribute("formmode", "calender");
            md.addAttribute("holidays_list",holidayMaster_Rep.holidayList(year,month));
        }
        List<CalenderMaintanceEntity> calenderMaintanceEntityList = calenderMaintanceRepo.getAllCalenderMaintanceList();
        md.addAttribute("calender_list", calenderMaintanceEntityList);
        return "OrganizationDetails";
    }

    /* PRAVEEN */
    @RequestMapping(value = "userProfile", method = { RequestMethod.GET, RequestMethod.POST })
    public String userprofile(@RequestParam(required = false) String formmode,
                              @RequestParam(required = false) String userid, Model md, HttpServletRequest req) {
        String user = (String) req.getSession().getAttribute("USERID");
        md.addAttribute("RoleMenu", access_Role_Repo.getRole(user));

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("userProfiles", userProfileRep.getAllList());

        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", formmode);

            md.addAttribute("currentDate", new Date());
            md.addAttribute("user", user);
            md.addAttribute("getEmployeeDetails", employee_Profile_Rep.getEmployeeVeifiedList());

        } else if (formmode.equals("view")) {

            md.addAttribute("formmode", formmode);

            md.addAttribute("userProfile", userProfileRep.getRole(userid));
            md.addAttribute("access", access_Role_Repo.getRole(userid));

        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", formmode);

            md.addAttribute("userProfile", userProfileRep.getRole(userid));
            md.addAttribute("access", access_Role_Repo.getRole(userid));

        } else if (formmode.equals("modify")) {

            md.addAttribute("formmode", formmode);

            md.addAttribute("userProfile", userProfileRep.getRole(userid));
            md.addAttribute("access", access_Role_Repo.getRole(userid));
        } else if (formmode.equals("delete") || formmode.equals("deleteUnverified")) {

            md.addAttribute("formmode", formmode);

            md.addAttribute("userProfile", userProfileRep.getRole(userid));
            md.addAttribute("access", access_Role_Repo.getRole(userid));

        }

        return "UserProfile";
    }

    /* THANVEER */
    @RequestMapping(value = "chartOfAccounts", method = RequestMethod.GET)
    public String chartOfAccounts(@RequestParam(required = false) String formmode,
                                  @RequestParam(required = false) String acct_num, String keyword, Model md, HttpServletRequest req) {
        String user = (String) req.getSession().getAttribute("USERID");
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("current_user", user);
            md.addAttribute("chartaccount", chart_Acc_Rep.getListoffice());
        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");
            md.addAttribute("Chart1", reference_code_Rep.getFullReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getFullReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getFullReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getFullReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getFullReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getFullReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getFullReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getFullReferenceCode("COA_08"));
        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
            md.addAttribute("Chart1", reference_code_Rep.getFullReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getFullReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getFullReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getFullReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getFullReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getFullReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getFullReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getFullReferenceCode("COA_08"));
        } else if (formmode.equals("verify")) {
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
            md.addAttribute("formmode", "verify");
            md.addAttribute("Chart1", reference_code_Rep.getFullReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getFullReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getFullReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getFullReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getFullReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getFullReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getFullReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getFullReferenceCode("COA_08"));
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
            md.addAttribute("Chart1", reference_code_Rep.getFullReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getFullReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getFullReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getFullReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getFullReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getFullReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getFullReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getFullReferenceCode("COA_08"));

        } else if (formmode.equals("delete")) {
            md.addAttribute("formmode", "delete");
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
            md.addAttribute("Chart1", reference_code_Rep.getFullReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getFullReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getFullReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getFullReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getFullReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getFullReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getFullReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getFullReferenceCode("COA_08"));

        }
        return "ChartOfAccounts";
    }

    /* PRAVEEN */
    @RequestMapping(value = "accountLedger", method = { RequestMethod.GET, RequestMethod.POST })
    public String accountLedger(@RequestParam(required = false) String formmode,
                                @RequestParam(required = false) String acct_num, String keyword, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("chartaccount", chart_Acc_Rep.getListoffice1());
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("dataList", tRAN_MAIN_TRM_WRK_REP.getList(acct_num));/* Journal Entries */
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));/* COA */

        }
        return "AccountLedger";
    }

    /* PRAVEEN */
    @RequestMapping(value = "budgetMaintanance", method = { RequestMethod.GET, RequestMethod.POST })
    public String budmain(@RequestParam(required = false) String formmode,
                          @RequestParam(required = false) String ref_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("budget", budget_Maintanance_Repo.getList());
            md.addAttribute("formmode", "list");

        }
        return "BudgetMaintenance";
    }

    @RequestMapping(value = "journalEntries", method = { RequestMethod.GET, RequestMethod.POST })
    public String journalEntries(@RequestParam(required = false) String formmode,
                                 @RequestParam(required = false) String acct_num, @RequestParam(required = false) String part_tran,
                                 @RequestParam(required = false) String tran_idss, @RequestParam(required = false) String part_transs,
                                 @RequestParam(required = false) String tran_id, @RequestParam(required = false) String part_tran_id,
                                 @RequestParam(required = false) String account_number,
                                 @RequestParam(required = false) String loan_sanctioned, @RequestParam(required = false) String account_name,
                                 @RequestParam(required = false) String schm_type, @RequestParam(required = false) String flow_code,
                                 @RequestParam(required = false) String flow_date, @RequestParam(required = false) String flow_amount,
                                 @RequestParam(required = false) String account_no, @RequestParam(required = false) String currency,
                                 @RequestParam(required = false) String accountName, Model md, HttpServletRequest rq) {

        String user = (String) rq.getSession().getAttribute("USERID");
        String user1 = (String) rq.getSession().getAttribute("BRANCH_ID");

        String fullTranID1 = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();
        md.addAttribute("plusonetran2", fullTranID1);

        if (formmode == null || formmode.equals("add")) {

            md.addAttribute("part_tran", part_tran);

            String fullTranID = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();

            /* NUmber part increment 1 */
            String tranIdNUm = fullTranID.substring(3);
            int newTranNUm = Integer.parseInt(tranIdNUm) + 1;

            /* Letter part get */
            String tranLetterPart = fullTranID.substring(0, 3);

            if (account_number != null) {
                md.addAttribute("accountnumbervalue", account_number);
            } else {

            }

            if (loan_sanctioned != null) {
                md.addAttribute("totalamountvalue", loan_sanctioned);
            } else {

                md.addAttribute("totalamountvalue", 0); // Set a default value
            }

            if (account_name != null) {
                md.addAttribute("accountnamevalue", account_name);
            } else {

            }

            /* from flow time */

            if (account_no != null) {
                md.addAttribute("accountnumber", account_no);
                md.addAttribute("currencyvalue", "SCR");

            } else {

            }

            if (flow_amount != null) {
                md.addAttribute("flowamount", flow_amount);
            } else {

            }

            if (flow_code != null) {
                md.addAttribute("flowcodes", flow_code);
            } else {

            }

            if (flow_date != null) {

                String[] flowTime = flow_date.split("-");
                String FlowDateSend = flowTime[0] + "/" + flowTime[1] + "/" + flowTime[2];
                md.addAttribute("flowdate", FlowDateSend);
            } else {

            }

            if (accountName != null) {
                md.addAttribute("accountnamevalue", accountName);
            } else {

            }

            if (currency != null) {
                md.addAttribute("currencyvalue", "SCR");
            } else {

            }

            if (schm_type != null) {
                if (schm_type.equals("LA")) {
                    md.addAttribute("accounttypevalue", "LeasyLoan");
                } else if (schm_type.equals("TD")) {
                    md.addAttribute("accounttypevalue", "TermLoan");
                } else {
                    md.addAttribute("accounttypevalue", ""); // Default case
                }
            } else {

            }

            // This will be reflected in your frontend

            md.addAttribute("plusonetran", tranLetterPart + newTranNUm);
            md.addAttribute("plusonetran1", fullTranID);
            md.addAttribute("popup", chart_Acc_Rep.getlistpopup());
            md.addAttribute("popupvalues", depositRep.getdatavalues());
            md.addAttribute("accountvalues", lease_Loan_Master_Repo.getCSlist());
            md.addAttribute("currentDate", new Date());
            md.addAttribute("partTranId", "1");
            md.addAttribute("user", user);
            md.addAttribute("tranStatus", "ENTERED");
            // md.addAttribute("popup", account_Ledger_Rep.popup());
            // System.out.println("123456789"+account_Ledger_Rep.popup());

            md.addAttribute("formmode", "add");
        } else if (formmode.equals("list1")) {

            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournal());
            md.addAttribute("formmode", "list1");

        } else if (formmode.equals("add1")) {

            /*
             * md.addAttribute("part_transs", part_transs); md.addAttribute("tran_idss",
             * tran_idss); md.addAttribute("part_tran", part_tran); String a =
             * account_Ledger_Rep.getlast(); String b = a.substring(3); int c =
             * Integer.parseInt(b) + 1; String d = a.substring(0, 3);
             * md.addAttribute("plusonetran", d + c);
             */
            md.addAttribute("formmode", "add1");
            // md.addAttribute("popup", account_Ledger_Rep.popup());
            // md.addAttribute("popup", chart_Acc_Rep.getlistpopup());

        } else if (formmode.equals("massentires")) {
            md.addAttribute("formmode", "massentires");
        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");
            md.addAttribute("ledgervalues", account_Ledger_Rep.getValuepop(tran_id, acct_num, part_tran_id));

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tran_id));
            md.addAttribute("Acctnum", acct_num);
            md.addAttribute("ledgervalues", tRAN_MAIN_TRM_WRK_REP.getValuepopvalues(tran_id, acct_num, part_tran_id));
            md.addAttribute("currentPartTran", part_tran_id);
            System.out.println("part_tran_id" + part_tran_id);
            md.addAttribute("maxPartTran", tRAN_MAIN_TRM_WRK_REP.maxPartranID(tran_id));
            md.addAttribute("gldetails", chart_Acc_Rep.getlistpopupvalues(acct_num));
        } else if (formmode.equals("modify")) {
            // md.addAttribute("jour", account_Ledger_Rep.getjourform(acct_num));
            md.addAttribute("formmode", "modify");
            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournal());
            md.addAttribute("ledgervalues", tRAN_MAIN_TRM_WRK_REP.getValuepopvalues(tran_id, acct_num, part_tran_id));
            md.addAttribute("currentPartTran", part_tran_id);
            System.out.println("part_tran_id" + part_tran_id);
            md.addAttribute("maxPartTran", tRAN_MAIN_TRM_WRK_REP.maxPartranID(tran_id));
            md.addAttribute("gldetails", chart_Acc_Rep.getlistpopupvalues(acct_num));
        } else if (formmode.equals("modify1")) {
            // md.addAttribute("jour", account_Ledger_Rep.getjourform(acct_num));
            md.addAttribute("formmode", "modify1");
            md.addAttribute("ledgervalues", tRAN_MAIN_TRM_WRK_REP.getValuepopvalues(tran_id, acct_num, part_tran_id));
            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tran_id));
            md.addAttribute("currentPartTran", part_tran_id);
            System.out.println("part_tran_id" + part_tran_id);
            md.addAttribute("maxPartTran", tRAN_MAIN_TRM_WRK_REP.maxPartranID(tran_id));
            System.out.println("maxPartTran" + tRAN_MAIN_TRM_WRK_REP.countPartTranIDs(tran_id));
            md.addAttribute("tableparttran", tRAN_MAIN_TRM_WRK_REP.currentTableRecords(tran_id));
            System.out.println(tRAN_MAIN_TRM_WRK_REP.currentTableRecords(tran_id) + "tableparttran");
            md.addAttribute("gldetails", chart_Acc_Rep.getlistpopupvalues(acct_num));
        } else if (formmode.equals("view2")) {
            // md.addAttribute("jour", account_Ledger_Rep.getjourform(acct_num));
            md.addAttribute("formmode", "view2");
            md.addAttribute("part_tran", part_tran);

            String fullTranID = "TR" + tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1();

            /* NUmber part increment 1 */
            String tranIdNUm = fullTranID.substring(3);
            int newTranNUm = Integer.parseInt(tranIdNUm) + 1;

            /* Letter part get */
            String tranLetterPart = fullTranID.substring(0, 3);

            md.addAttribute("plusonetran", tranLetterPart + newTranNUm);
            md.addAttribute("popup", chart_Acc_Rep.getlistpopup());
            md.addAttribute("currentDate", new Date());
            md.addAttribute("partTranId", "1");
            md.addAttribute("user", user);
            md.addAttribute("tranStatus", "ENTERED");
            // md.addAttribute("popup", account_Ledger_Rep.popup());
            // System.out.println("123456789"+account_Ledger_Rep.popup());
        }

        return "JournalEntries";
    }

    /* PRAVEEN */
    @RequestMapping(value = "accountLedgerPost", method = { RequestMethod.GET, RequestMethod.POST })
    public String accountLedgerPost(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String acct_num, @RequestParam(required = false) String part_tran,
                                    @RequestParam(required = false) String tran_idss, @RequestParam(required = false) String part_transs,
                                    @RequestParam(required = false) String tran_id, @RequestParam(required = false) String part_tran_id,
                                    Model md, HttpServletRequest rq) {

        String user = (String) rq.getSession().getAttribute("USERID");

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournal());
            md.addAttribute("user_id", user);
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", "add");
        } else if (formmode.equals("add1")) {

            md.addAttribute("formmode", "add1");

        } else if (formmode.equals("massentires")) {
            md.addAttribute("formmode", "massentires");
        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");
            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournalvalues(tran_id));
            md.addAttribute("Acctnum", acct_num);
            md.addAttribute("ledgervalues", tRAN_MAIN_TRM_WRK_REP.getValuepopvalues(tran_id, acct_num, part_tran_id));

            md.addAttribute("currentPartTran", part_tran_id);
            md.addAttribute("maxPartTran", tRAN_MAIN_TRM_WRK_REP.maxPartranID(tran_id));
            md.addAttribute("gldetails", chart_Acc_Rep.getlistpopupvalues(acct_num));

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("ledgervalues", account_Ledger_Rep.getValuepop(tran_id, acct_num, part_tran_id));
        } else if (formmode.equals("modify")) {
            // md.addAttribute("jour", account_Ledger_Rep.getjourform(acct_num));
            md.addAttribute("formmode", "modify");

        }

        return "AccountLedgerPost";
    }

    /* PRAVEEN */
    @RequestMapping(value = "dateChangeProcess", method = { RequestMethod.GET, RequestMethod.POST })
    public String dateChangeProcess(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");

        }
        return "DateChangeProcess";
    }

    /* PRAVEEN */
    @RequestMapping(value = "gstMaintanance", method = { RequestMethod.GET, RequestMethod.POST })
    public String gstMaintanance(@RequestParam(required = false) String formmode,
                                 @RequestParam(required = false) String tran_id, @RequestParam(required = false) String tran, Model md,
                                 HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            // md.addAttribute("gst", account_Ledger_Rep.findByjournalgst());

            md.addAttribute("formmode", "list");

        } else if (formmode.equals("view")) {

            // GSTMaintenanceEntity up = gSTMaintenanceRep.getGSTform(tran_id);

            // up.setTran_id(account_Ledger_Entity.getTran_id());

            // md.addAttribute("gstmain", gSTMaintenanceRep.getGSTform(tran_id));

            md.addAttribute("formmode", "view");
        }

        else if (formmode.equals("modify")) {

            // md.addAttribute("gstmain", gSTMaintenanceRep.getGSTform(tran_id));
            md.addAttribute("formmode", "modify");

        }
        return "GSTMaintenance";
    }

    /* PRAVEEN */
    @RequestMapping(value = "invoiceMaintance", method = { RequestMethod.GET, RequestMethod.POST })
    public String invoiceMaintance(@RequestParam(required = false) String formmode,
                                   @RequestParam(required = false) String inv_srl_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            // md.addAttribute("inv", invoiceMaintenanceRep.getInvoiceList());
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", "add");
        } else if (formmode.equals("view")) {

            // md.addAttribute("invoice", invoiceMaintenanceRep.getInvoiceForm(inv_srl_no));
            md.addAttribute("formmode", "view");
        } else if (formmode.equals("modify")) {

            md.addAttribute("formmode", "modify");
        }
        return "InvoiceMaintenance";
    }

    /* PRAVEEN */
    @RequestMapping(value = "voucherMaintanace", method = { RequestMethod.GET, RequestMethod.POST })
    public String voucherMaintanace(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");

        } else if (formmode.equals("generator")) {

            md.addAttribute("formmode", "generator");

        }
        return "VoucherMaintanace";
    }

    /* THANVEER */
    @RequestMapping(value = "candidateEvaluationForm", method = { RequestMethod.GET, RequestMethod.POST })
    public String candidateEvaluationForm(@RequestParam(required = false) String formmode,
                                          @RequestParam(required = false) String ref_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("del", candEvalFormRep.getCVFList());
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");

        } else if (formmode.equals("verify")) {

            md.addAttribute("cvfview", candEvalFormRep.getCVFform(ref_no));
            md.addAttribute("formmode", "verify");

        } else if (formmode.equals("view")) {

            md.addAttribute("cvfview", candEvalFormRep.getCVFform(ref_no));
            md.addAttribute("formmode", "view");

        } else if (formmode.equals("modify")) {

            md.addAttribute("cvfview", candEvalFormRep.getCVFform(ref_no));
            md.addAttribute("formmode", "modify");
        }
        return "CandidateEvaluationForm";
    }

    /* THANVEER */
    @RequestMapping(value = "offerLetter", method = { RequestMethod.GET, RequestMethod.POST })
    public String offerLetter(@RequestParam(required = false) String formmode,
                              @RequestParam(required = false) String userid, @RequestParam(required = false) Optional<Integer> page,
                              @ModelAttribute CandEvalFormEntity candEvalFormEntity, @RequestParam(required = false) String a,
                              @RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {
        // md.addAttribute("IssueMaster", issueMasterRep.findAllCustom());

        md.addAttribute("empty", "");
        md.addAttribute("kkkk", a);
        md.addAttribute("cvfverify", candEvalFormRep.getCVFLists());

        System.out.println(a);
        md.addAttribute("cvfverifys", candEvalFormRep.getCVFforms(a));

        return "OfferLetter";
    }

    /* THANVEER */
    @RequestMapping(value = "appointmentLetter", method = { RequestMethod.GET, RequestMethod.POST })
    public String appointmentLetter(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String userid, @RequestParam(required = false) String c,
                                    @RequestParam(required = false) String a, @RequestParam(required = false) String ref_no,
                                    CandEvalFormEntity candEvalFormEntity,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                    @RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

        md.addAttribute("menu", "projectmaster"); // To highlight the menu

        if (formmode == null || formmode.equals("list")) {
            System.out.println(a);

            md.addAttribute("cvfverifys", candEvalFormRep.getCVFforms(a));
            System.out.println(candEvalFormRep.getCVFforms(a));
            md.addAttribute("kkkk", a);
            md.addAttribute("cvfverify", candEvalFormRep.getCVFLists());
            md.addAttribute("empty", "");
            md.addAttribute("formmode", "list"); // to set which form - valid values are "edit" , "add" & "list"

        } else if (formmode.equals("edit")) {

            md.addAttribute("formmode", "edit");

        } else if (formmode.equals("view")) {

            md.addAttribute("formmode", "view");

        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", "add");

        } else {

            md.addAttribute("formmode", formmode);

        }

        return "Appointment_Letter";
    }

    /* THANVEER */
    @RequestMapping(value = "salaryStructure", method = { RequestMethod.GET, RequestMethod.POST })
    public String salaryStructure(@RequestParam(required = false) String formmode,
                                  @RequestParam(required = false) String emp_no, @RequestParam(required = false) String emp_no1,
                                  String keyword, Model md, HttpServletRequest req)

    {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("salarypay", salary_Pay_Rep.getList());
        } else if (formmode.equals("add")) {
            System.out.println(emp_no1);
            md.addAttribute("salarypay", salary_Pay_Rep.getsalfromcvf(emp_no));

            md.addAttribute("formmode", "add");

        } else if (formmode.equals("edit")) {
            md.addAttribute("formmode", "edit");
            md.addAttribute("salarypay", salary_Pay_Rep.getaedit(emp_no));
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("salarypay", salary_Pay_Rep.getaedit(emp_no));
        }
        return "salarystructure";
    }

    /* THANVEER */
    @RequestMapping(value = "assosiateProfile", method = { RequestMethod.GET, RequestMethod.POST })
    public String assosiateProfile(@RequestParam(required = false) String formmode,
                                   @RequestParam(required = false) String resource_id, Model md, HttpServletRequest rq,
                                   @ModelAttribute Assosiate_Profile_Entity assosiate_Profile_Entity) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("AssosiateList", assosiate_Profile_Repo.getAssosiateList());

        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");
        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");
            md.addAttribute("AssosiateVerify", assosiate_Profile_Repo.getSingleIdData(resource_id));

        } else if (formmode.equals("view")) {

            md.addAttribute("formmode", "view");
            md.addAttribute("AssosiateVerify", assosiate_Profile_Repo.getSingleIdData(resource_id));

        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("AssosiateVerify", assosiate_Profile_Repo.getSingleIdData(resource_id));
        }

        return "AssociateProfile";
    }

    /* THANVEER */
    @RequestMapping(value = "profileManager", method = { RequestMethod.GET, RequestMethod.POST })
    public String profileManager(@RequestParam(required = false) String formmode,
                                 @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("list", profileManagerRep1.getPMList());
            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "ProfileManager";
    }

    /* THANVEER */
    @GetMapping("payStructure")
    public String payStructure(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String emp_no, String keyword, Model md, HttpServletRequest req)

    {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("salarypay", Paystructurerep.getList());
        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");
        } else if (formmode.equals("edit")) {
            md.addAttribute("formmode", "edit");
            md.addAttribute("salarypay", Paystructurerep.getaedit(emp_no));
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("salarypay", Paystructurerep.getaedit(emp_no));
        } else if (formmode.equals("verify")) {
            md.addAttribute("formmode", "verify");
            md.addAttribute("salarypay", Paystructurerep.getaedit(emp_no));
        }
        return "paystructure";
    }

    /* THANVEER */
    @RequestMapping(value = "payMaster", method = { RequestMethod.GET, RequestMethod.POST })
    public String payMaster(@RequestParam(required = false) String formmode,
                            @RequestParam(required = false) String userid, @RequestParam(required = false) String record,
                            @RequestParam(required = false) String salaryMonth, @RequestParam(required = false) String empname,
                            @RequestParam(required = false) String a, @RequestParam(required = false) String salary_month,
                            @RequestParam(required = false) String empno, paystructureentity Paystructureentity,

                            @RequestParam(value = "page", required = false) Optional<Integer> page,
                            @RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

        md.addAttribute("menu", "projectmaster"); // To highlight the menu
        System.out.println("modiy pay master" + empno + salaryMonth);

        if (formmode == null || formmode.equals("list")) {
            System.out.println(Paystructurerep.getpays(salary_month));
            YearMonth currentYearMonth = YearMonth.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM"); // Corrected pattern for month
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy");
            String formattedMonth = currentYearMonth.format(formatter);
            String formattedYear = currentYearMonth.format(formatter1);
            System.out.println("Current Month and Year: " + formattedYear + formattedMonth);

            md.addAttribute("salarypay", Paystructurerep.getpayssdemo(formattedYear, formattedMonth));

            md.addAttribute("formmode", "list");

        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");
        } else if (formmode.equals("edit")) {
            md.addAttribute("salarypay", Paystructurerep.getaedits(empno, salaryMonth, empname));
            md.addAttribute("formmode", "edit");

        }

        return "PayMaster";
    }

    /* PRAVEEN */
    @RequestMapping(value = "transactionReport", method = { RequestMethod.GET, RequestMethod.POST })
    public String transactionReport(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");

        }
        return "TransactionReport";
    }

    /* PRAVEEN */
    @RequestMapping(value = "accountBalancing", method = { RequestMethod.GET, RequestMethod.POST })
    public String accountBalancing(@RequestParam(required = false) String formmode,
                                   @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");

        }
        return "AccountBalancing";
    }

    /* PRAVEEN */
    @RequestMapping(value = "generalLedgerReports", method = { RequestMethod.GET, RequestMethod.POST })
    public String generalLedgerReports(@RequestParam(required = false) String formmode,
                                       @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");

        }
        return "GeneralLedgerReports";
    }

    /* PRAVEEN */
    @RequestMapping(value = "trialBalanceReports", method = { RequestMethod.GET, RequestMethod.POST })
    public String trialBalanceReports(@RequestParam(required = false) String formmode,
                                      @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            System.out.println("balance");
            md.addAttribute("trialbal", chart_Acc_Rep.getList());
            // md.addAttribute("trialbal", chart_Acc_Rep.getList());
            // md.addAttribute("trialbal", cOAGL_Rep.getList());

        }

        return "TrialBalanceReports";
    }

    @RequestMapping(value = "assetliability", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Map<String, List<Object[]>> assetliability(@RequestParam(required = false) String formmode,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date balancedate,
                                                      @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        List<Object[]> msg = dAB_Repo.getfilteredrec(balancedate);
        List<Object[]> msg1 = dAB_Repo.getfilteredrec1(balancedate);
        Map<String, List<Object[]>> result = new HashMap<>();
        result.put("msg", msg);
        result.put("msg1", msg1);

        return result;
    }

    @RequestMapping(value = "incomexpenditure", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Map<String, List<DAB_Entity>> incomexpenditure(@RequestParam(required = false) String formmode,
                                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date balancedate,
                                                          @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        List<DAB_Entity> msg = dAB_Repo.getfilteredrec2(balancedate);
        List<DAB_Entity> msg1 = dAB_Repo.getfilteredrec3(balancedate);
        Map<String, List<DAB_Entity>> result = new HashMap<>();
        result.put("msg", msg);
        result.put("msg1", msg1);

        return result;
    }

    /* PRAVEEN */
    @RequestMapping(value = "profitAndLossAccountReports", method = { RequestMethod.GET, RequestMethod.POST })
    public String profitAndLossAccountReports(@RequestParam(required = false) String formmode,
                                              @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        Date TRANDATE = (Date) rq.getSession().getAttribute("TRANDATE");
        System.out.println(TRANDATE);
        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("balancesheet3", chart_Acc_Rep.getList3());
            md.addAttribute("balancesheet4", chart_Acc_Rep.getList4());
            md.addAttribute("TRANDATE", TRANDATE);

        }
        return "ProfitAndLossReports";
    }

    /* PRAVEEN */
    @RequestMapping(value = "profitAndLossAccount", method = { RequestMethod.GET, RequestMethod.POST })
    public String profitAndLossAccount(@RequestParam(required = false) String formmode,
                                       @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        Date TRANDATE = (Date) rq.getSession().getAttribute("TRANDATE");
        System.out.println(TRANDATE);
        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("balancesheet3", chart_Acc_Rep.getList3());
            md.addAttribute("balancesheet4", chart_Acc_Rep.getList4());
            md.addAttribute("TRANDATE", TRANDATE);

        }
        return "ProfitAndLossReports";
    }

    /* PRAVEEN */
    @RequestMapping(value = "balanceSheetReports", method = { RequestMethod.GET, RequestMethod.POST })
    public String balanceSheetReports(@RequestParam(required = false) String formmode,
                                      @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");

        }
        return "BalanceSheetReports";
    }

    @RequestMapping(value = "gstStatement", method = { RequestMethod.GET, RequestMethod.POST })
    public String gstStatement(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String resource_id, Model md, HttpServletRequest rq,
                               @ModelAttribute Assosiate_Profile_Entity assosiate_Profile_Entity) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("AssosiateList", assosiate_Profile_Repo.getAssosiateList());

        }

        return "GstStatement";
    }

    @RequestMapping(value = "tdsReports", method = { RequestMethod.GET, RequestMethod.POST })
    public String tdsReports(@RequestParam(required = false) String formmode,
                             @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "TDS_MAIN";
    }

    @RequestMapping(value = "spfStatement", method = { RequestMethod.GET, RequestMethod.POST })
    public String spfStatement(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "Audit_spf";
    }

    @RequestMapping(value = "esiStatement", method = { RequestMethod.GET, RequestMethod.POST })
    public String esiStatement(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "Audit_esi";
    }

    @RequestMapping(value = "generalLedger", method = { RequestMethod.GET, RequestMethod.POST })
    public String generalLedger(@RequestParam(required = false) String formmode,
                                @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "general_ledger";
    }

    /* THANVEER */
    @RequestMapping(value = "perdiemMaster", method = { RequestMethod.GET, RequestMethod.POST })
    public String perdiemMaster(@RequestParam(required = false) String formmode,
                                @RequestParam(required = false) String emp_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("list1", perdiemMasterRep.getPerMasList());
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");

        } else if (formmode.equals("view")) {

            System.out.println(emp_no);
            md.addAttribute("permasview", perdiemMasterRep.getPerMasform(emp_no));

            md.addAttribute("list5", perdiemMasterRep.getPerMasList3(emp_no));

            md.addAttribute("formmode", "view");

        }

        return "PerdiemMaster";
    }

    /* THANVEER */
    @RequestMapping(value = "batchJob", method = { RequestMethod.GET, RequestMethod.POST })
    public String batchJob(@RequestParam(required = false) String formmode, Model md, HttpServletRequest rq) {

        return "BatchJob";
    }

    /* THANVEER */
    @RequestMapping(value = "monthlySalaryWork", method = { RequestMethod.GET, RequestMethod.POST })
    public String monthlySalaryWork(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String userid, @RequestParam(required = false) String record,

                                    @RequestParam(required = false) String a, @RequestParam(required = false) String uniqueid,
                                    @RequestParam(required = false) String ref_no, @RequestParam(required = false) String emp_no,

                                    CandEvalFormEntity candEvalFormEntity,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                    @RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req) {

        md.addAttribute("menu", "projectmaster"); // To highlight the menu

        System.out.println(record);
        if (formmode == null || formmode.equals("add")) {
            md.addAttribute("formmode", "add");
        } else if (formmode.equals("list1")) {

            md.addAttribute("salarypay", Baj_Work_Repo.getpays(record));
            System.out.println(Baj_Work_Repo.getpays(record));
            md.addAttribute("formmode", "list1");
        } else if (formmode.equals("edit")) {
            md.addAttribute("salarypay", Baj_Work_Repo.getlisttab1(uniqueid));
            md.addAttribute("formmode", "edit");

        } else if (formmode.equals("add1")) {
            md.addAttribute("formmode", "add1");

        } else if (formmode.equals("add2")) {
            md.addAttribute("formmode", "add2");

        } else if (formmode.equals("view")) {
            md.addAttribute("salarypay", Baj_Work_Repo.getlisttab1(uniqueid));
            md.addAttribute("formmode", "view");

        } else if (formmode.equals("verify")) {
            md.addAttribute("salarypay", Baj_Work_Repo.getlisttab1(uniqueid));
            md.addAttribute("formmode", "verify");

        }

        return "monthlySalaryWork";
    }

    /* THANVEER */
    @RequestMapping(value = "monthlySalaryGenerator", method = { RequestMethod.GET, RequestMethod.POST })
    public String monthlySalaryGenerator(@RequestParam(required = false) String formmode, Model md,
                                         HttpServletRequest rq) {

        return "MonthlySalaryGenerator";
    }

    /* THANVEER */
    @RequestMapping(value = "salaryTransactionCreation", method = { RequestMethod.GET, RequestMethod.POST })
    public String salaryTransactionCreation(@RequestParam(required = false) String formmode, Model md,
                                            HttpServletRequest rq) {
        if (formmode == null || formmode.equals("add")) {
            md.addAttribute("formmode", "add");
        } else if (formmode.equals("journal")) {
            md.addAttribute("formmode", "journal");
        }

        return "SalaryTransactionCreation";
    }

    /* THANVEER */
    @RequestMapping(value = "salaryPaymentTransaction", method = { RequestMethod.GET, RequestMethod.POST })
    public String salaryPaymentTransaction(@RequestParam(required = false) String formmode, Model md,
                                           HttpServletRequest rq) {

        return "SalaryPaymentTransaction";
    }

    /* THANVEER */
    @RequestMapping(value = "bankFileDownload", method = { RequestMethod.GET, RequestMethod.POST })
    public String bankFileDownload(@RequestParam(required = false) String formmode,
                                   @RequestParam(required = false) String a, Model md, HttpServletRequest rq) {

        System.out.println("bankFileDownload" + a);
        if (formmode == null || formmode.equals("list")) {
            // System.out.println(Paystructurerep.getpays(record));
            // md.addAttribute("salarypay",
            // Paystructurerep.getpays(record));assosiate_Profile_Repo
            // md.addAttribute("salarypay", Baj_Work_Repo.getpays(record));
            // md.addAttribute("salarypay", Paystructurerep.getpay());

            md.addAttribute("formmode", "list");
            ;
        } else if (formmode.equals("add")) {

            Date currentDate = new Date();

            // Create a date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            // Format the current date
            String formattedDate = dateFormat.format(currentDate);

            // Print the formatted date
            System.out.println("Current Date: " + formattedDate);
            md.addAttribute("formattedDate", formattedDate);
            md.addAttribute("salarypay", Paystructurerep.bankjobicici(a));
            // md.addAttribute("ifsccode", Paystructurerep.bjicicinotpresent(a));

            System.out.println(Paystructurerep.bankjobicici(a));
            // System.out.println(Baj_Work_Repo.getpays(record));
            // md.addAttribute("salarypay", Paystructurerep.getpay());

            md.addAttribute("formmode", "add");
        }
        return "BankFileDownload";
    }

    /* THANVEER */
    @RequestMapping(value = "paySlipGeneration", method = { RequestMethod.GET, RequestMethod.POST })
    public String paySlipGeneration(@RequestParam(required = false) String formmode, Model md, HttpServletRequest rq) {

        return "PaySlipGeneration";
    }

    /* THANVEER */
    @RequestMapping(value = "perdiemGeneration", method = { RequestMethod.GET, RequestMethod.POST })
    public String perdiemGeneration(@RequestParam(required = false) String formmode, Model md, HttpServletRequest rq) {

        return "PerdiemGeneration";
    }

    @RequestMapping(value = "balanceSheet", method = { RequestMethod.GET, RequestMethod.POST })
    public String balanceSheet(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        Date TRANDATE = (Date) request.getSession().getAttribute("TRANDATE");
        System.out.println(TRANDATE);
        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
            model.addAttribute("balancesheet1", chart_Acc_Rep.getList1());
            model.addAttribute("balancesheet2", chart_Acc_Rep.getList2());
            model.addAttribute("TRANDATE", TRANDATE);

        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "balance_sheet.html";
    }

    @RequestMapping(value = "gstDetails", method = { RequestMethod.GET, RequestMethod.POST })
    public String gstDetails(@RequestParam(required = false) String formmode,
                             @RequestParam(required = false) String resource_id, Model md, HttpServletRequest rq,
                             @ModelAttribute Assosiate_Profile_Entity assosiate_Profile_Entity) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("AssosiateList", assosiate_Profile_Repo.getAssosiateList());

        }

        return "GstDetails";
    }

    @RequestMapping(value = "outsytandingItems", method = { RequestMethod.GET, RequestMethod.POST })
    public String outsytandingItems(@RequestParam(required = false) String formmode,
                                    @RequestParam(required = false) String resource_id, Model md, HttpServletRequest rq,
                                    @ModelAttribute Assosiate_Profile_Entity assosiate_Profile_Entity) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("AssosiateList", assosiate_Profile_Repo.getAssosiateList());

        }

        return "OutsytandingItems";
    }

    @RequestMapping(value = "outstandingItemsReport", method = { RequestMethod.GET, RequestMethod.POST })
    public String outstandingItemsReport(@RequestParam(required = false) String formmode,
                                         @RequestParam(required = false) String resource_id, Model md, HttpServletRequest rq,
                                         @ModelAttribute Assosiate_Profile_Entity assosiate_Profile_Entity) {

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            md.addAttribute("AssosiateList", assosiate_Profile_Repo.getAssosiateList());

        }

        return "outstanding_items.html";
    }

    @RequestMapping(value = "invoiceReport", method = { RequestMethod.GET, RequestMethod.POST })
    public String invoiceReport(@RequestParam(required = false) String formmode,
                                @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "invoice_report.html";
    }

    /* THANVEER */
    @RequestMapping(value = "employeeProfile", method = { RequestMethod.GET, RequestMethod.POST })
    public String employeeProfile(@RequestParam(required = false) String formmode,
                                  @RequestParam(required = false) String employee_id, Model md, HttpServletRequest rq) {

        String user = (String) rq.getSession().getAttribute("USERID");
        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            List<Employee_Profile> employees = employee_Profile_Rep.getEmployeeList();
            md.addAttribute("EmployeeList", employees);

        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");
            md.addAttribute("refCode2", reference_code_Rep.getReferenceCode("EMP_PRO_02"));
            md.addAttribute("refCode1", reference_code_Rep.getReferenceCode("EMP_PRO_01"));
            md.addAttribute("refCode3", reference_code_Rep.getReferenceCode("EMP_PRO_03"));
            md.addAttribute("refCode4", reference_code_Rep.getReferenceCode("EMP_PRO_04"));
            md.addAttribute("refCode5", reference_code_Rep.getReferenceCode("EMP_PRO_05"));
            md.addAttribute("refCode6", reference_code_Rep.getReferenceCode("EMP_PRO_06"));
            md.addAttribute("refCode7", reference_code_Rep.getReferenceCode("EMP_PRO_07"));
            md.addAttribute("refCode8", reference_code_Rep.getReferenceCode("EMP_PRO_08"));
            md.addAttribute("refCode9", reference_code_Rep.getReferenceCode("EMP_PRO_09"));
            md.addAttribute("refCode10", reference_code_Rep.getReferenceCode("EMP_PRO_10"));

            md.addAttribute("currentDate", new Date());
            md.addAttribute("user", user);
            md.addAttribute("employee", employee_Profile_Rep.getEmployeeData(employee_id));

            md.addAttribute("branch", organization_Branch_Rep.getbranchid());

            // md.addAttribute("seq", employee_Profile_Rep.getSrlNo());
            String notifyRef = employee_Profile_Rep.getSrlNo();
            String NotifyReference;
            if (notifyRef != null) {
                NotifyReference = "EMP0" + (Integer.valueOf(notifyRef) + 1);
            } else {
                NotifyReference = "EMP01";
            }
            System.out.println(NotifyReference);
            md.addAttribute("seq", NotifyReference);
        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");
            md.addAttribute("employee", employee_Profile_Rep.getEmployeeData(employee_id));

            md.addAttribute("user", user);
            md.addAttribute("currentDate", new Date());

        } else if (formmode.equals("view")) {

            md.addAttribute("formmode", "view");
            md.addAttribute("employee", employee_Profile_Rep.getEmployeeData(employee_id));

            md.addAttribute("user", user);
            md.addAttribute("currentDate", new Date());

        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            Employee_Profile existingProfile = employee_Profile_Rep.getEmployeeData(employee_id);
            byte[] fileData1 = existingProfile.getEmployee_Photo();
            String base64FileData1 = "";
            if (fileData1 != null) {
                System.out.println("hjihihihihi");
                base64FileData1 = Base64.getEncoder().encodeToString(fileData1);
                md.addAttribute("fileData1", base64FileData1);
            }

            md.addAttribute("employee", employee_Profile_Rep.getEmployeeData(employee_id));
            md.addAttribute("refCode2", reference_code_Rep.getReferenceCode("EMP_PRO_02"));
            md.addAttribute("refCode1", reference_code_Rep.getReferenceCode("EMP_PRO_01"));
            md.addAttribute("refCode3", reference_code_Rep.getReferenceCode("EMP_PRO_03"));
            md.addAttribute("refCode4", reference_code_Rep.getReferenceCode("EMP_PRO_04"));
            md.addAttribute("refCode5", reference_code_Rep.getReferenceCode("EMP_PRO_05"));
            md.addAttribute("refCode6", reference_code_Rep.getReferenceCode("EMP_PRO_06"));
            md.addAttribute("refCode7", reference_code_Rep.getReferenceCode("EMP_PRO_07"));
            md.addAttribute("refCode8", reference_code_Rep.getReferenceCode("EMP_PRO_08"));
            md.addAttribute("refCode9", reference_code_Rep.getReferenceCode("EMP_PRO_09"));
            md.addAttribute("refCode10", reference_code_Rep.getReferenceCode("EMP_PRO_10"));

            md.addAttribute("user", user);
            md.addAttribute("currentDate", new Date());
            md.addAttribute("branch", organization_Branch_Rep.getbranchid());
        }

        return "Employee_profile";
    }

    /* Thanveer */

    @RequestMapping(value = "refCodeMain", method = { RequestMethod.GET, RequestMethod.POST })
    public String refCodeMain(@RequestParam(required = false) String formmode,
                              @RequestParam(required = false) String emp_id, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
            model.addAttribute("refList", reference_code_Rep.getRefList());
        } else if (formmode.equals("nav")) {

            model.addAttribute("formmode", "nav");
            model.addAttribute("refType", reference_code_Rep.getReferenceType());
        }
        return "ReferenceCodeMaintenance";
    }

    /* Aishu */
    @RequestMapping(value = "collateralmanage", method = { RequestMethod.GET, RequestMethod.POST })
    public String collateralmanage(@RequestParam(required = false) String formmode, Model model, String customer_id,
                                   HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("USERID");
        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("formmode", "list");
            model.addAttribute("managelist", collateral_management_Repo.getlist());
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
            /*
             * String userNum = userProfileRep.getUSERIDNumMax(); String userNumStart; if
             * (userNum != null) { userNumStart = "USER00" + (Integer.valueOf(userNum) + 1);
             * } else { userNumStart = "USER001"; } model.addAttribute("userNumId",
             * userNumStart); model.addAttribute("currentDate", new Date());
             * model.addAttribute("user", user);
             */
        } else if (formmode.equals("view")) {
            model.addAttribute("formmode", "view");
            model.addAttribute("viewmanage", collateral_management_Repo.getbyid(customer_id));
        } else if (formmode.equals("modify")) {
            model.addAttribute("formmode", "modify");
            System.out.println("the output value is " + customer_id);
            model.addAttribute("modimanage", collateral_management_Repo.getbyid(customer_id));
        }
        return "Collateral_Management.html";
    }

    @PostMapping(value = "SubmitcollamanageModify")
    @ResponseBody
    public String SubmitcollamanageModify( // Change parameter name to user_id
                                           Model md, HttpServletRequest rq, @RequestParam(required = false) String customer_id,
                                           @ModelAttribute Collateral_management_Entity collateral_management_Entity) {
        String userID = (String) rq.getSession().getAttribute("USERID");

        Collateral_management_Entity existed = collateral_management_Repo
                .getbyid(collateral_management_Entity.getCustomer_id());

        if (existed == null) {
            return "No Date Available";
        } else {
            collateral_management_Entity.setDel_flg("N");
            collateral_management_Entity.setEntity_flg("N");
            collateral_management_Entity.setModify_flg("N");
            collateral_management_Entity.setGen_verify_flg("N");
            collateral_management_Entity.setEntry_user(userID);
            collateral_management_Entity.setEntry_time(new Date());
            collateral_management_Repo.save(collateral_management_Entity);
            System.out.println(collateral_management_Entity.getCustomer_id());
            return "Modifed Successfully";
        }
    }

    /* Thanveer */
    /* SURIYA */
    @RequestMapping(value = "glcode", method = { RequestMethod.GET, RequestMethod.POST })
    public String glcode(@RequestParam(required = false) String formmode, @RequestParam(required = false) String glcode,
                         Model md, HttpServletRequest request, @RequestParam(required = false) String glsh_Code) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            md.addAttribute("getvaluelist", generalLedgerRep.getlistvalue());
        } else if (formmode.equals("view1")) {

            md.addAttribute("formmode", "view1");
            // md.addAttribute("GeneralLedger", adminOperServices.getGeneralLedger(glcode));

            md.addAttribute("GeneralLedger", chart_Acc_Rep.getaedit(glcode));
            System.out.println(chart_Acc_Rep.getaedit(glcode) + "GLCODE" + glcode);
        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("BamGeneralLedger", generalLedgerRep.getlistvalue());

        } else if (formmode.equals("edit")) {

            md.addAttribute("formmode", "edit");
            md.addAttribute("GeneralLedger", generalLedgerRep.getsinglevalue(glsh_Code));
        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", formmode);

        } else if (formmode.equals("deleteList")) {
            md.addAttribute("formmode", "deleteList");
            md.addAttribute("BamGeneralLedger", generalLedgerRep.getRefCodelist());
        } else if (formmode.equals("delete")) {
            md.addAttribute("formmode", "delete");
            System.out.println("the gl code value is " + glcode);
            md.addAttribute("GeneralLedger", generalLedgerRep.getsinglevalue(glsh_Code));
        } else if (formmode.equals("upload")) {

            md.addAttribute("formmode", "upload");

        } else if (formmode.equals("uploadlist")) {

            md.addAttribute("formmode", "uploadlist");
            md.addAttribute("Listofvalues", generalLedgerWork_Rep.getlistvalue());
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("singlerecord", generalLedgerRep.getsinglevalue(glsh_Code));
        }

        return "GeneralLedger.html";
    }

    @RequestMapping(value = "documentManager", method = { RequestMethod.GET, RequestMethod.POST })
    public String documentManager(@RequestParam(required = false) String formmode, String headcode, Model md,
                                  HttpServletRequest req) {

        if (formmode == null || formmode.equals("view")) {

            List<Bamdocumentmanager> Bamdocumentmanager = BAMDocmastrep.findAll(); // Fetch your data
            md.addAttribute("Bamdocumentmanager", Bamdocumentmanager);
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("edit")) {

            md.addAttribute("formmode", "edit");

            md.addAttribute("Bamdocumentmanager", BAMDocmastrep.findById(headcode).get());

        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");

            md.addAttribute("Bamdocumentmanager", BAMDocmastrep.findById(headcode).get());

        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", "add");
            md.addAttribute("Bamdocumentmanager", new Bamdocumentmanager());

        } else {

            md.addAttribute("formmode", formmode);
        }

        return "BAMDocumentManager";
    }

    /* Praveen */
    @RequestMapping(value = "drawDownLoan", method = { RequestMethod.GET, RequestMethod.POST })
    public String LSDBDDSyndicatedLoan(@RequestParam(required = false) String formmode, Model md,
                                       HttpServletRequest req, @RequestParam(required = false) String dd_notice_ref,
                                       @RequestParam(required = false) String synd_fac_ref) {

        String user = (String) req.getSession().getAttribute("USERID");
        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(TRANDATE);

        if (formmode == null || formmode.equals("add")) {
            md.addAttribute("formmode", "add");

            List<CustomerRequest> custid1 = bACP_CUS_PROFILE_REPO.getcustidret();
            Set<String[]> cif = new HashSet<>();

            for (CustomerRequest a : custid1) {

                String[] values = { a.getCif_id(), a.getCa_cif_id_1(), a.getCa_preferred_name(),
                        a.getCa_full_name_1() };
                cif.add(values);
                System.out.println(Arrays.toString(values));
            }

            md.addAttribute("deposit", cif);

            md.addAttribute("currentDate", TRANDATE);
            md.addAttribute("ConDate", formattedDate);
            md.addAttribute("user", user);

        }
        return "DrawDownSyndicatedLoan";

    }

    /* pon prasanth */
    @RequestMapping(value = "deletescreen", method = RequestMethod.POST)
    @ResponseBody
    public String deletescreen(@RequestParam(required = false) String tran_id,
                               @RequestParam(required = false) String part_tran_id, @RequestParam(required = false) String acct_num) {
        String msg = null;

        try {
            Account_Ledger_Entity vv = account_Ledger_Rep.getValuepop(tran_id, acct_num, part_tran_id);
            account_Ledger_Rep.delete(vv);
            msg = "Deleted Successfully";
        } catch (Exception e) {
            msg = "Delete Unsuccessfull";
        }
        return msg;
    }

    @RequestMapping(value = "InventoryMaster1", method = { RequestMethod.GET, RequestMethod.POST })
    public String InventoryMaster1(@RequestParam(required = false) String formmode, String headcode, Model md,
                                   HttpServletRequest req) throws ParseException {

        String user = (String) req.getSession().getAttribute("USERID");
        // String userId = (String) req.getSession().getAttribute("USERID");
        // md.addAttribute("RoleMenu", resourceMasterRepo.getrole(userId));
        // md.addAttribute("menu", "BTMHeaderMenu");

        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Format the current date and time
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("Current Date and Time: " + formattedDateTime);
        if (formmode == null || formmode.equals("list")) {

            List<BGLS_BAMInventorymaster> BAMInventorymaster = BGLS_BAMInventryMastRep.getall(); // Fetch your data
            md.addAttribute("BAMInventorymaster", BAMInventorymaster);
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("add")) {
            System.out.println("Formmode :" + formmode);

            md.addAttribute("formmode", "add");
            md.addAttribute("BAMInventorymaster", new BGLS_BAMInventorymaster());
            List<BGLS_Bamcategorycodemain_entity> assetsrlno = BGLS_Bamcatcodemaintainrep.getall();
            md.addAttribute("user", user);
            md.addAttribute("assetsrlno", assetsrlno);
            md.addAttribute("formattedDateTime", formattedDateTime);
            md.addAttribute("currentDate", new Date());

        }

        else if (formmode.equals("edit")) {

            md.addAttribute("formmode", "edit");
            md.addAttribute("BAMInventorymaster", BGLS_BAMInventryMastRep.findById(headcode).get());
            md.addAttribute("user", user);
            md.addAttribute("formattedDateTime", formattedDateTime);
            md.addAttribute("currentDate", new Date());

        } else if (formmode.equals("view")) {

            md.addAttribute("formmode", "view");
            md.addAttribute("BAMInventorymaster", BGLS_BAMInventryMastRep.findById(headcode).get());
            md.addAttribute("user", user);
            md.addAttribute("formattedDateTime", formattedDateTime);
            md.addAttribute("currentDate", new Date());
            List<String> srl = BGLS_BAMInventryMastRep.getdatas();
            System.out.println(srl + "srlllllllllllllllll");
            md.addAttribute("lists", srl);

        }

        else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");
            md.addAttribute("BAMInventorymaster", BGLS_BAMInventryMastRep.findById(headcode).get());
            md.addAttribute("user", user);
            md.addAttribute("formattedDateTime", formattedDateTime);
            md.addAttribute("currentDate", new Date());

        } else {

            md.addAttribute("formmode", formmode);
        }

        return "BGLS_BAMInventoryMana";
    }

    @RequestMapping(value = "InvMastadd", method = RequestMethod.POST)
    @ResponseBody
    public String organizationMasterAdd(@RequestParam("formmode") String formmode,
                                        @RequestParam(required = false) String de_m, @RequestParam("depr_percent") String depr_percent,
                                        @RequestParam("Invtype") String Invtype, @ModelAttribute BGLS_BAMInventorymaster BAMInventorymaster,
                                        Model md, HttpServletRequest rq, @RequestParam(required = false) String headcode,
                                        @RequestParam(required = false) String category_code,
                                        @RequestParam(required = false) String sub_category_code)
            throws KeyManagementException, NoSuchAlgorithmException {

        String userId = (String) rq.getSession().getAttribute("USERID");
        System.out.println("The headcode control : " + headcode);
        System.out.println("The categorycode  : " + category_code);
        System.out.println("The subcategorycode  : " + sub_category_code);
        System.out.println(BAMInventorymaster.getVerify_user() + "hhhhh");

        System.out.println(de_m);
        String msg = BGLS_Inventeryservice.Invmastadd(BAMInventorymaster, formmode, Invtype, userId, de_m, depr_percent,
                headcode, category_code, sub_category_code);
        return msg;
    }

    @RequestMapping(value = "get_serial_main", method = RequestMethod.GET)
    @ResponseBody
    public BGLS_Bamcategorycodemain_entity get_serial_main(@RequestParam(required = false) String asset_category,
                                                           Model md) {

        System.out.println("Asset asset_category is :" + asset_category);
        BGLS_Bamcategorycodemain_entity getlist = BGLS_Bamcatcodemaintainrep.getbyId(asset_category);
        return getlist;
    }

    @RequestMapping(value = "Catcodemaintain", method = { RequestMethod.GET, RequestMethod.POST })
    public String Catcodemaintain(@RequestParam(required = false) String formmode, String headcode, Model md,
                                  HttpServletRequest req)

            throws ParseException {
//		String EmpId = "U72900TN2017PTC115892";
        // String userId = (String) req.getSession().getAttribute("USERID");
        // md.addAttribute("RoleMenu", resourceMasterRepo.getrole(userId));
        // md.addAttribute("menu", "BTMHeaderMenu");
        if (formmode == null || formmode.equals("view")) {

            List<BGLS_Bamcategorycodemain_entity> Bamcategorycodemain = BGLS_Bamcatcodemaintainrep
                    .findAllOrderedBySlNo(); // Fetch your data
            md.addAttribute("Bamcategorycodemain", Bamcategorycodemain);
            md.addAttribute("formmode", "list");

        } else if (formmode.equals("edit")) {

            md.addAttribute("formmode", "edit");

            md.addAttribute("Bamcategorycodemain", BGLS_Bamcatcodemaintainrep.getbyId(headcode));

        } else if (formmode.equals("add")) {

            md.addAttribute("formmode", "add");
            md.addAttribute("Bamcategorycodemain", new BGLS_Bamcategorycodemain_entity());
        } else {

            md.addAttribute("formmode", formmode);
        }

        return "BAMCatCodeMain";
    }

    @RequestMapping(value = "deleteserialno", method = RequestMethod.POST)
    @ResponseBody
    public String deleteserialno(@RequestParam("ASN") String ASN) {

        System.out.println("The Asset is :" + ASN);
        String msg = BGLS_Inventeryservice.deletesrn(ASN);
        return msg;
    }

    @RequestMapping(value = "Catcodemaintainadd", method = RequestMethod.POST)
    @ResponseBody
    public String organizationMasterAdd(@RequestParam("formmode") String formmode,
                                        @ModelAttribute BGLS_Bamcategorycodemain_entity Bamcategorycodemain, Model md, HttpServletRequest rq,
                                        @RequestParam(required = false) String headcode, @RequestParam(required = false) String categorycode,
                                        @RequestParam(required = false) String subcategorycode) {

        System.out.println("The headcode is :" + headcode);
        System.out.println("The categorycode is :" + categorycode);
        System.out.println("The subcategorycode is :" + subcategorycode);
        String msg = BGLS_Inventeryservice.Catecodemaintain(Bamcategorycodemain, formmode, headcode, categorycode,
                subcategorycode);
        return msg;
    }

    @RequestMapping(value = "BGLS_BamAssert", method = { RequestMethod.GET, RequestMethod.POST })
    public String asset_flows(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req)
            throws ParseException {

        String userId = (String) req.getSession().getAttribute("USERID");
        // md.addAttribute("RoleMenu", resourceMasterRepo.getrole(userId));

        // Fetch the list of serial numbers from BAMInvmastrep
        List<String> srl = BGLS_BAMInventryMastRep.getdatas();
        System.out.println(srl + "srlllllllllllllllll");
        md.addAttribute("lists", srl);

        List<String> lastTransferDate = BGLS_BAMInventryMastRep.getdatas();
        md.addAttribute("lastTransferDate", lastTransferDate);
        System.out.println("Last Transfer Date: " + lastTransferDate);

        return "BGLS_BamAssert";
    }

    @GetMapping("BGLS_BamAssert_collatral")
    public String bamAssertPage(@RequestParam("serialNo") String serialNo, Model md) {
        System.out.println("First entry to asset of collateral page");

        /*
         * List<String> srl = BGLS_BAMInventryMastRep.getdatas(); System.out.println(srl
         * + "srlllllllllllllllll"); md.addAttribute("lists", srl);
         *
         * List<String> lastTransferDate = BGLS_BAMInventryMastRep.getdatas();
         * md.addAttribute("lastTransferDate", lastTransferDate);
         * System.out.println("Last Transfer Date: " + lastTransferDate);
         */
        System.out.println(serialNo + "serialNo");
        md.addAttribute("serialNo", serialNo);

        // Return the view name that should be rendered
        return "BGLS_BamAssertcollatral"; // Make sure this matches your view template name
    }

    @PostMapping("/addAssets")
    @Transactional
    public ResponseEntity<String> addAssets(@RequestParam(required = false) String formmode,
                                            @RequestParam(required = false) BigDecimal srl_no,
                                            @RequestBody List<BGLS_BAMAssetFlows_Entity> BAM_AssetFlows_Entity) {
        ResponseEntity<String> msg = null;
        if ("add".equals(formmode)) {
            msg = BGLS_Inventeryservice.add_Assets(BAM_AssetFlows_Entity, formmode);
            System.out.println(BAM_AssetFlows_Entity + "BAM_AssetFlows_Entity");

        } else if ("edit".equals(formmode)) {
            msg = BGLS_Inventeryservice.add_Assets(BAM_AssetFlows_Entity, formmode);
            System.out.println("the msg :" + msg);
        }
        return msg;
    }

    @RequestMapping(value = "get_serial_Inv", method = RequestMethod.GET)
    @ResponseBody
    public BGLS_BAMInventorymaster get_serial_Inv(@RequestParam(required = false) String AN, Model md) {
        BGLS_BAMInventorymaster en = new BGLS_BAMInventorymaster();
        System.out.println("Asset Serial Number is :" + AN);
        BGLS_BAMInventorymaster getlist = BGLS_BAMInventryMastRep.getview(AN);

        return getlist;
    }

    @RequestMapping(value = "assets_flows", method = { RequestMethod.GET, RequestMethod.POST })
    public String markOnDuty(@RequestParam(required = false) String formmode, @RequestParam(required = false) String Id,
                             Model md, HttpServletRequest req) throws ParseException {
//	String userId = (String) req.getSession().getAttribute("USERID");
        // md.addAttribute("RoleMenu", resourceMasterRepo.getrole(userId));
//	md.addAttribute("menu", "BTMHeaderMenu");

        System.out.println("Asset is :" + formmode);
        if (formmode == null) {
            md.addAttribute("formmode", "list"); // List
            List<Object[]> BGLS_BAMAssetFlows_Entity = BGLS_BAMAssetFlows_Rep.findAssetsWithMinGenRollNo();
            System.out.println(BGLS_BAMAssetFlows_Entity + "BGLS_BAMAssetFlows_Entity");
            md.addAttribute("assets", BGLS_BAMAssetFlows_Entity);

            // md.addAttribute("assets",
            // BGLS_BAMAssetFlows_Rep.findAssetsWithMinGenRollNo());
        } else if ("view".equals(formmode)) {
            System.out.println("Asset is :" + Id);
            md.addAttribute("formmode", "view");

            System.out.println("Asset is :" + BGLS_BAMAssetFlows_Rep.getview(Id).size());
            md.addAttribute("paramview", BGLS_BAMAssetFlows_Rep.getview(Id));
        }
        return "BGLS_BamDEPRESSION";
    }

    /* Aishu */
    @RequestMapping(value = "collateralmaintance", method = { RequestMethod.GET, RequestMethod.POST })
    public String collateralmaintance(@RequestParam(required = false) String formmode, Model model, String customer_id,
                                      HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("USERID");
        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("formmode", "list");
            model.addAttribute("managelist", collateral_management_Repo.getlist());
        } else if (formmode.equals("modify")) {
            model.addAttribute("formmode", "modify");
            model.addAttribute("modimanage", collateral_management_Repo.getbyid(customer_id));
            // String userNum = userProfileRep.getUSERIDNumMax();
            /*
             * String userNumStart; if (user != null) { userNumStart = "USER00" +
             * (Integer.valueOf(user) + 1); } else { userNumStart = "USER001"; }
             */
            model.addAttribute("userNumId", user);
            model.addAttribute("currentDate", new Date());
            model.addAttribute("user", user);
        }
        return "Collateral_Maintance.html";
    }

    @PostMapping(value = "SubmitcollamanageModify1")
    @ResponseBody
    public String SubmitcollamanageModify1( // Change parameter name to user_id
                                            Model md, HttpServletRequest rq,
                                            @ModelAttribute Collateral_management_Entity collateral_management_Entity) {
        String userID = (String) rq.getSession().getAttribute("USERID");

        String msg = "";
        Collateral_management_Entity up = collateral_management_Repo
                .getbyid(collateral_management_Entity.getCustomer_id());
        if (Objects.nonNull(up)) {
            up = collateral_management_Entity;
            up.setDel_flg("N");
            up.setEntity_flg("N");
            up.setModify_flg("N");
            up.setGen_verify_flg("N");
            up.setEntry_user(userID);
            up.setEntry_time(new Date());
            collateral_management_Repo.save(up);
            msg = "Modify Successfully";
        } else {
            msg = "Data Not Found";
        }
        return msg;
    }

    /* pon prasanth */
    @RequestMapping(value = "useractivities", method = { RequestMethod.GET, RequestMethod.POST })
    public String useractivities(@RequestParam(required = false) String formmode,
                                 @RequestParam(required = false) String Fromdate, @RequestParam(required = false) String Todate,
                                 @RequestParam(required = false) String ListFlg, Model md, HttpServletRequest req)
            throws java.text.ParseException {

        String role_id = (String) req.getSession().getAttribute("ROLEID");
        md.addAttribute("IPSRoleMenu", adminOperServices.getRoleMenu(role_id));
        md.addAttribute("PdfViewer", "UserAudit");

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dbFormat = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();
        String currentDateDisplay = inputFormat.format(cal.getTime());
        String currentDateDB = dbFormat.format(cal.getTime());

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            try {
                if ("Y".equalsIgnoreCase(ListFlg) && Fromdate != null && !Fromdate.isEmpty()) {
                    Date fromDateParsed = inputFormat.parse(Fromdate);
                    String formattedFromDate = dbFormat.format(fromDateParsed);

                    md.addAttribute("Fromdate", Fromdate);
                    md.addAttribute("AuditList", AuidtConfigure.getauditListLocal(dbFormat.parse(formattedFromDate)));
                } else {
                    md.addAttribute("Fromdate", currentDateDisplay);
                    md.addAttribute("AuditList", AuidtConfigure.getauditListLocal(dbFormat.parse(currentDateDB)));
                }
            } catch (ParseException e) {
                md.addAttribute("error", "Invalid date format. Please use dd/MM/yyyy.");
                return "IPSAudit";
            }
        }

        md.addAttribute("menuname", "User Activity Audits");
        md.addAttribute("auditflag", "auditflag");
        md.addAttribute("formmode", "list");

        return "AuditTrailValues";
    }

    /* pon prasanth */
    @RequestMapping(value = "serviceactivities", method = { RequestMethod.GET, RequestMethod.POST })
    public String serviceactivities(@RequestParam(required = false) String Fromdate,
                                    @RequestParam(required = false) String formmode, @RequestParam(required = false) String userid,
                                    @RequestParam(required = false) Optional<Integer> page,
                                    @RequestParam(value = "size", required = false) Optional<Integer> size,
                                    @RequestParam(required = false) String ListFlg, Model md, HttpServletRequest req)
            throws java.text.ParseException {

        String roleId = (String) req.getSession().getAttribute("ROLEID");
        md.addAttribute("IPSRoleMenu", adminOperServices.getRoleMenu(roleId));
        md.addAttribute("PdfViewer", "ServiceAudit");

        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();
        String currentDateStr = inputFormat.format(cal.getTime());
        String currentFormattedDateStr = displayFormat.format(cal.getTime());

        Date targetDate;

        // ✅ Handle formmode and ListFlg safely
        if (formmode == null || formmode.equalsIgnoreCase("list")) {
            if ("Y".equalsIgnoreCase(ListFlg) && Fromdate != null && !Fromdate.trim().isEmpty()) {
                try {
                    Date fromDateParsed = inputFormat.parse(Fromdate);
                    String formattedFromDate = displayFormat.format(fromDateParsed);
                    targetDate = displayFormat.parse(formattedFromDate);

                    md.addAttribute("Fromdate", Fromdate);
                    md.addAttribute("AuditList", AuidtConfigure.getAuditInquries(targetDate));

                } catch (ParseException e) {
                    md.addAttribute("error", "Invalid From Date format. Please use dd/MM/yyyy.");
                    md.addAttribute("Fromdate", currentDateStr);
                    md.addAttribute("AuditList",
                            AuidtConfigure.getAuditInquries(displayFormat.parse(currentFormattedDateStr)));
                }
            } else {
                // Default to current date if no ListFlg or Fromdate provided
                md.addAttribute("Fromdate", currentDateStr);
                md.addAttribute("AuditList",
                        AuidtConfigure.getAuditInquries(displayFormat.parse(currentFormattedDateStr)));
            }
        }
        md.addAttribute("menuname", "Service Audits");
        md.addAttribute("auditflag", "auditflag");
        md.addAttribute("formmode", "list");

        return "BusinessTrail";
    }


    //suriya
    @RequestMapping(value = "dscr", method = { RequestMethod.GET, RequestMethod.POST })
    public String dscr(@RequestParam(required = false) String formmode,
                       @RequestParam(required = false) String branch_name, Model md, HttpServletRequest req) {

        String userid = (String) req.getSession().getAttribute("USERID");
        md.addAttribute("menu", "BAJHeaderMenu");

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

        }
        return "DSCR_RATIO";
    }

    /* PRAVEEN */
    @RequestMapping(value = "loanstatus", method = { RequestMethod.GET, RequestMethod.POST })
    public String loanstatus(@RequestParam(required = false) String formmode,
                             @RequestParam(required = false) String ref_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

        }
        return "LeaseLoanStatus";
    }

    /* PRAVEEN */
    @RequestMapping(value = "kychist", method = { RequestMethod.GET, RequestMethod.POST })
    public String kychist(@RequestParam(required = false) String formmode,
                          @RequestParam(required = false) String ref_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

        }
        return "KYCHistory";
    }

    /* PRAVEEN */
    @RequestMapping(value = "riskhist", method = { RequestMethod.GET, RequestMethod.POST })
    public String riskhist(@RequestParam(required = false) String formmode,
                           @RequestParam(required = false) String ref_no, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

        }
        return "RiskHistory";
    }

    /* Aishu */
    @RequestMapping(value = "custrisk", method = { RequestMethod.GET, RequestMethod.POST })
    public String custrisk(@RequestParam(required = false) String formmode, Model model, HttpServletRequest request) {
        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "Customer_Risk_Category.html";
    }

    /* Aishu */
    @RequestMapping(value = "kycdoc", method = { RequestMethod.GET, RequestMethod.POST })
    public String kycdoc(@RequestParam(required = false) String formmode, Model model, HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("formmode", "list");
        } else if (formmode.equals("add")) {
            model.addAttribute("formmode", "add");
        }
        return "Customer_KYC_Document.html";
    }

    /* Thanveer */
    @RequestMapping(value = "deposits", method = { RequestMethod.GET, RequestMethod.POST })
    public String deposits(@RequestParam(required = false) String formmode,
                           @RequestParam(required = false) String actno, Model md, HttpServletRequest rq) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            md.addAttribute("getdata", depositRep.getdata());

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("customerdata", depositRep.getCustdataact(actno));
            md.addAttribute("listact", td_defn_Repo.getactList(actno));

        } else if (formmode.equals("verify")) {
            md.addAttribute("formmode", "verify");
            md.addAttribute("customerdata", depositRep.getCustdataact(actno));
            md.addAttribute("listact", td_defn_Repo.getactList(actno));

        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("customerdata", depositRep.getCustdataact(actno));
            md.addAttribute("listact", td_defn_Repo.getactList(actno));

        }
        return "DepositsAct";
    }

    /* Aishu */
    @PostMapping(value = "Submitcollamanage")
    @ResponseBody
    public String SubmitMisAdmin(@RequestParam(required = false) String customer_id, // Change parameter name to user_id
                                 Model md, HttpServletRequest rq,
                                 @ModelAttribute Collateral_management_Entity collateral_management_Entity) {
        String userID = (String) rq.getSession().getAttribute("USERID");
        collateral_management_Entity.setDel_flg("N");
        collateral_management_Entity.setEntity_flg("N");
        collateral_management_Entity.setModify_flg("N");
        collateral_management_Entity.setGen_verify_flg("N");
        collateral_management_Entity.setEntry_user(userID);
        collateral_management_Entity.setEntry_time(new Date());

        collateral_management_Repo.save(collateral_management_Entity);
        System.out.println(customer_id); // Changed to user_id
        return "Added Successfully";
    }

    /* pon prasanth */
    @RequestMapping(value = "audittraildetails", method = { RequestMethod.GET, RequestMethod.POST })
    public String audittraildetails(@RequestParam(required = false) String formmode, Model model, String cust_id,
                                    HttpServletRequest request) {

        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("formmode", "list");
        }
        return "AuditTrail.html";
    }

    /* Thanveer */
    @RequestMapping(value = "depositsopen", method = { RequestMethod.GET, RequestMethod.POST })
    public String depositsopen(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String act, Model md, HttpServletRequest rq,
                               @ModelAttribute DepositEntity depositEntity) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            List<String> custid1 = bACP_CUS_PROFILE_REPO.getcustid();
            md.addAttribute("deposit", custid1);

            md.addAttribute("getdata", depositRep.getdata());

        } /*
         * else if (formmode.equals("flow")) { md.addAttribute("formmode", "flow");
         * md.addAttribute("customerdata", depositRep.getCustdataact(act));
         *
         * md.addAttribute("listact", td_defn_Repo.getactList(act));
         *
         * }
         */
        return "DepositsActOpening";
    }

    /* Thanveer */
    @RequestMapping(value = "depositSchedule", method = { RequestMethod.GET, RequestMethod.POST })
    public String depositSchedule(@RequestParam(required = false) String formmode,
                                  @RequestParam(required = false) String act, Model md, HttpServletRequest rq,
                                  @ModelAttribute DepositEntity depositEntity) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            List<String> custid1 = bACP_CUS_PROFILE_REPO.getcustid();
            /*
             * String combinedCustId = custid1.stream() .map(row -> row[0] + " " + row[1])
             * .collect(Collectors.joining("\n"));
             */
            md.addAttribute("deposit", custid1);

            md.addAttribute("getdata", depositRep.getdata());

        } else if (formmode.equals("flow")) {
            md.addAttribute("formmode", "flow");
            md.addAttribute("customerdata", depositRep.getCustdataact(act));

            md.addAttribute("listact", td_defn_Repo.getactList(act));

        }
        return "DepositsActOpening";
    }

    /* Praveen */
    @RequestMapping(value = "drawDownLoanMaintanace", method = { RequestMethod.GET, RequestMethod.POST })
    public String drawDownLoanMaintanace(@RequestParam(required = false) String formmode, Model md,
                                         HttpServletRequest req, @RequestParam(required = false) String loan_accountno) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("getDDDetails", lease_Loan_Work_Repo.getLeaseAccountList());

        } else if (formmode.equals("view")) {

            md.addAttribute("formmode", "view");
            md.addAttribute("loanDetails", lease_Loan_Work_Repo.getLeaseAccount(loan_accountno));
            md.addAttribute("paymentDetails", noticeDetailsPayment0Rep.getPaymentDetails(loan_accountno));

        } else if (formmode.equals("verify")) {

            md.addAttribute("formmode", "verify");
            md.addAttribute("loanDetails", lease_Loan_Work_Repo.getLeaseAccount(loan_accountno));
            md.addAttribute("paymentDetails", noticeDetailsPayment0Rep.getPaymentDetails(loan_accountno));

        }
        return "DrawDownSyndicatedLoan";
    }

    @RequestMapping(value = "accountLedgerLoan", method = { RequestMethod.GET, RequestMethod.POST })
    public String AccountLedger(@RequestParam(required = false) String formmode,
                                @RequestParam(required = false) String tran_id, @RequestParam(required = false) String part_tran,
                                @RequestParam(required = false) String loanAccount, @RequestParam(required = false) String part_tran_id,
                                @RequestParam(required = false) String foracid, String acct_num, Model md, HttpServletRequest req) {

        if (formmode == null || formmode.equals("list")) {
            /*
             * md.addAttribute("formmode", "list"); md.addAttribute("transactionList",
             * sl_tran_rep.getTransactionList()); md.addAttribute("accountList",
             * sl_tran_rep.getAccountNumberList());
             */
        } else if (formmode.equals("schedule")) {
            md.addAttribute("formmode", "schedule");
            md.addAttribute("accountList", sl_tran_rep.getAccountNumberList());
            md.addAttribute("loanAccount", loanAccount);
            String installmentId = principle_and_intrest_shedule_Rep.getInstallmentId(loanAccount);
            md.addAttribute("records",
                    principle_and_intrest_shedule_Rep.getInstallmentrecords(loanAccount, installmentId));
            List<Principle_and_intrest_shedule_Entity> demandRecords = principle_and_intrest_shedule_Rep
                    .getInstallmentrecords(loanAccount, installmentId);
            BigDecimal interestAmount = BigDecimal.ZERO;
            BigDecimal totalReapay = BigDecimal.ZERO;
            // BigDecimal totalReapay=demandRecords.get(0).getLoan_amt();
            try {
                if (demandRecords.size() > 0) {
                    for (Principle_and_intrest_shedule_Entity oneData : demandRecords) {
                        interestAmount = interestAmount.add(oneData.getInterest_amt());
                    }

                    totalReapay = demandRecords.get(0).getLoan_amt().add(interestAmount);
                } else {
                    // System.out.println("No records available for Account ID : " + loanAccount);
                    throw new RuntimeException("No records available for Account ID : " + loanAccount);

                }
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }
            md.addAttribute("totalReapay", totalReapay);
            md.addAttribute("interestAmount", interestAmount);

        }
        return "AccountLedgerLoan";
    }

    @RequestMapping(value = "accountLedger2", method = { RequestMethod.GET, RequestMethod.POST })
    public String accountLedger2(@RequestParam(required = false) String formmode,
                                 @RequestParam(required = false) String acct_num, String keyword, Model md, HttpServletRequest req) {

        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("chartaccount", chart_Acc_Rep.getList());
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("dataList", tRAN_MAIN_TRM_WRK_REP.getList(acct_num));/* Journal Entries */
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));/* COA */
            md.addAttribute("TRANDATE", TRANDATE);

        }
        return "AccountLedger_IR";
    }

    @RequestMapping(value = "day_end_operation", method = { RequestMethod.GET, RequestMethod.POST })
    public String day_end_operation(@RequestParam(required = false) String formmode, Model model,
                                    HttpServletRequest request) throws java.text.ParseException {
        // Journal
        Date TRANDATE = (Date) request.getSession().getAttribute("TRANDATE");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(TRANDATE);

        String Userid = (String) request.getSession().getAttribute("USERID");

        model.addAttribute("Userid", Userid);

        model.addAttribute("Valuesfordebit", tRAN_MAIN_TRM_WRK_REP.getwofordebitvalues());
        model.addAttribute("ValuesforCredit", tRAN_MAIN_TRM_WRK_REP.getwoforcreditvalues());
        model.addAttribute("Valuesfornotransaction", tRAN_MAIN_TRM_WRK_REP.getTransactionValues());

        model.addAttribute("ValuesforDel", tRAN_MAIN_TRM_WRK_REP.getdelvalues());
        model.addAttribute("Valuesforunpost", tRAN_MAIN_TRM_WRK_REP.getunpostedvalues());

        model.addAttribute("Valuesforaccbalance", chart_Acc_Rep.getaccbalance());


        // ACCOUNT BALANCE
        model.addAttribute("Valuesforaccopen", chart_Acc_Rep.getacctbalcredit());
        model.addAttribute("Valuesforacclose", chart_Acc_Rep.getacctbaldebit());

        // GL BALANCE
        model.addAttribute("Valuesforgldebit", chart_Acc_Rep.getGLbaldebit());
        model.addAttribute("Valuesforglcredit", chart_Acc_Rep.getGLbalcredit());

        // day end balance
        model.addAttribute("getWoforDebitValues", tRAN_MAIN_TRM_WRK_REP.getWoforDebitValues());
        model.addAttribute("getWoforcreditValues", tRAN_MAIN_TRM_WRK_REP.getWoforcreditValues());
        model.addAttribute("getWofortotalValues", tRAN_MAIN_TRM_WRK_REP.getWoforTotalValues());

        List<Object[]> debitCreditData = tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDate(formattedDate);

        if (debitCreditData.size() > 3) {
            Object[] elementAtFour = debitCreditData.get(3);
            System.out.println(Arrays.toString(elementAtFour) + " debitCreditData");
        } else {
            System.out.println("The list does not contain enough elements.");
        }
        // System.out.println(debitCreditData.get(3)+"debitCreditData");
        model.addAttribute("debitCreditData", debitCreditData);
        // model.addAttribute("getNetDebitCreditForCurrentDate",tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDate());

        BigDecimal getTotalValues = tRAN_MAIN_TRM_WRK_REP.getTotalValues(formattedDate);
        model.addAttribute("totalamount", getTotalValues);
        // model.addAttribute("gettotalValues",tRAN_MAIN_TRM_WRK_REP.getTotalValues());

        Object totalTransactionCount = tRAN_MAIN_TRM_WRK_REP.getTotalTransactionCount(formattedDate);
        model.addAttribute("totaltransaction", totalTransactionCount);

        model.addAttribute("getTotalCredit", tRAN_MAIN_TRM_WRK_REP.getTotalCredit(formattedDate));
        model.addAttribute("getTotalDebit", tRAN_MAIN_TRM_WRK_REP.getTotalDebit(formattedDate));


        model.addAttribute("Journal",bGLS_CONTROL_TABLE_REP.getJournal());
        System.out.println("Journal"+bGLS_CONTROL_TABLE_REP.getJournal());
        model.addAttribute("AccountLedger",bGLS_CONTROL_TABLE_REP.getAccountLedger());
        model.addAttribute("HolidayCheck",bGLS_CONTROL_TABLE_REP.getHolidayCheck());
        model.addAttribute("DAB",bGLS_CONTROL_TABLE_REP.getDAB());
        model.addAttribute("ConsistencyCheck",bGLS_CONTROL_TABLE_REP.getConsistencyCheck());
        model.addAttribute("MovementofTransaction",bGLS_CONTROL_TABLE_REP.getMovementofTransaction());
        model.addAttribute("GlConsolidation",bGLS_CONTROL_TABLE_REP.getGlConsolidation());
        model.addAttribute("InterestDemand",bGLS_CONTROL_TABLE_REP.getInterestDemand());
        model.addAttribute("FeeDemand",bGLS_CONTROL_TABLE_REP.getFeeDemand());
        model.addAttribute("Penalty",bGLS_CONTROL_TABLE_REP.getPenalty());
        model.addAttribute("dcpstatus",bGLS_CONTROL_TABLE_REP.getDcpStatus());
        model.addAttribute("interestAccurual",bGLS_CONTROL_TABLE_REP.getinterestAccurual());

        System.out.println("dcpstatus"+bGLS_CONTROL_TABLE_REP.getDcpStatus());
        return "Day_end_Operation.html";
    }

    /*
     * @RequestMapping(value = "Doatransactionpushdemo", method =
     * RequestMethod.POST)
     *
     * @ResponseBody public String Doatransactionpushdemo(Model md,
     * HttpServletRequest rq, @ModelAttribute DAB_Entity DAB_Entity) {
     * System.out.println("huhjhjhkjhkjhkjhkjh"); List<Object[]> debitCreditData =
     * tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDate();
     * List<String> accountNumbers = new ArrayList<>(); List<String> netAmounts =
     * new ArrayList<>(); // Corrected variable name
     *
     * // Extracting account num and net amount not submitting for (Object[] record
     * : debitCreditData) { String accountNum = record[0].toString(); // Get account
     * number from first column accountNumbers.add(accountNum); // Add account
     * number to the list
     *
     * // Assuming the net amount is in the fifth column (index 4) String netAmount
     * = record[4].toString(); netAmounts.add(netAmount); // Add the net amount to
     * the list
     *
     * // Print individual account numbers and net amounts
     * System.out.println(accountNum + " accountNumbers");
     * System.out.println("Net Amount: " + netAmount); }
     *
     * // Not important just for sysout for (int i = 0; i < accountNumbers.size();
     * i++) { String accountNum = accountNumbers.get(i); String netAmount =
     * netAmounts.get(i); System.out.println(accountNum + " accountNumbersforloop");
     * System.out.println(netAmount + " Net_amountforloop");
     *
     * // tRAN_MAIN_TRM_WRK_REP.insertNewAccountBalance(accountNum, netAmount); }
     *
     * List<DAB_Entity> exist = dAB_Repo.get_transaction_acc_num(accountNumbers);
     *
     * // Convert accountNumbers list to a Set for faster lookup Set<String>
     * accountNumbersSet = new HashSet<>(accountNumbers);
     *
     * String acctnum = ""; // Main for loop to set save if (accountNumbers != null)
     * { for (DAB_Entity entity : exist) { acctnum = entity.getAcct_num();
     *
     * if (accountNumbersSet.contains(acctnum)) { // Check if the account number is
     * in the passed list // Find the index of the existing account number int index
     * = accountNumbers.indexOf(acctnum); String netAmount = netAmounts.get(index);
     * // Get the corresponding net amount
     *
     * System.out.println(acctnum + " existaccuntnum");
     * System.out.println("Net Amount: " + netAmount);
     *
     * // List<TRAN_MAIN_TRM_WRK_ENTITY> up2 =
     * tRAN_MAIN_TRM_WRK_REP.set_dab_acc_num(acctnum); List<DAB_Entity> up3 = new
     * ArrayList<>();
     *
     *
     *
     * }
     *
     *
     * } }
     *
     * // Optionally, print the account numbers
     *
     * return "Successfully"; }
     *
     */

    @RequestMapping(value = "DoatransactionpushBatchJob", method = RequestMethod.POST)
    @ResponseBody
    public String DoatransactionpushBatchJob(Model md, HttpServletRequest rq, @ModelAttribute DAB_Entity DAB_Entity,
                                             @RequestParam("to_date") String to_date,@RequestParam("acct_num") String Acct_num,
                                             @RequestParam("from_date") String from_date) throws java.text.ParseException {
        // Get TRANDATE from session as a String
        System.out.println("Trndate"+to_date);
        Date toDateDAB = (Date) rq.getSession().getAttribute("TRANDATE");
        System.out.println("TRANDATE: " + toDateDAB);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(toDateDAB);

        System.out.println("formattedDate"+formattedDate);
        List<Object[]> debitCreditData = tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDateByAcctNum(from_date,to_date,Acct_num);

        List<String> accountNumbers = new ArrayList<>();
        List<Date>acctTranDate = new ArrayList<>();
        List<String> netAmounts = new ArrayList<>();
        List<String> accountNames = new ArrayList<>();

        // Lists for each field
        List<String> glshCodes = new ArrayList<>();
        List<String> glshDescs = new ArrayList<>();
        List<String> glCodes = new ArrayList<>();
        List<String> glDescs = new ArrayList<>();
        List<BigDecimal> totalCredits = new ArrayList<>();
        List<BigDecimal> totalDebits = new ArrayList<>();

        // Step 1: Get all account numbers from `debitCreditData`
        for (Object[] record : debitCreditData) {
            accountNumbers.add(record[0].toString()); // acct_num
            accountNames.add(record[1].toString()); // acct_name
            netAmounts.add(record[5].toString()); // NETAMT
            totalCredits.add((BigDecimal) record[3]); // TOTAL_CREDIT
            totalDebits.add((BigDecimal) record[4]); // TOTAL_DEBIT
            acctTranDate.add((Date) record[6]);
        }

        // Print accountNumbers, accountNames, netAmounts, totalCredits, and totalDebits
        System.out.println("Account Numbers: " + accountNumbers);
        System.out.println("Account Names: " + accountNames);
        System.out.println("Net Amounts: " + netAmounts);
        System.out.println("Total Credits: " + totalCredits);
        System.out.println("Total Debits: " + totalDebits);

        List<Chart_Acc_Entity> allAccounts = new ArrayList<>();
        int batchSize = 1000;
        for (int i = 0; i < accountNumbers.size(); i += batchSize) {
            List<String> batch = accountNumbers.subList(i, Math.min(i + batchSize, accountNumbers.size()));
            allAccounts.addAll(chart_Acc_Rep.getcoaaccunt_num(batch));
        }
        List<Chart_Acc_Entity> exist = allAccounts;

        // Step 3: Create a map to store GL details for each account number
        Map<String, Chart_Acc_Entity> glDetailsMap = new HashMap<>();
        for (Chart_Acc_Entity up1 : exist) {
            glDetailsMap.put(up1.getAcct_num(), up1);
        }

        // Step 4: Retrieve GL details in the same order as `accountNumbers`
        for (int i = 0; i < accountNumbers.size(); i++) {
            String acctNum = accountNumbers.get(i);
            String acctName = accountNames.get(i);

            Chart_Acc_Entity glDetails = glDetailsMap.get(acctNum);
            if (glDetails != null) {
                glshCodes.add(glDetails.getGlsh_code());
                glshDescs.add(glDetails.getGlsh_desc());
                glCodes.add(glDetails.getGl_code());
                glDescs.add(glDetails.getGl_desc());

                // Print account number, account name, and corresponding GL details
                System.out.println("Account Number: " + acctNum + ", Account Name: " + acctName + ", GL Code: "
                        + glDetails.getGl_code() + ", GLSH Code: " + glDetails.getGlsh_code() + ", GL Description: "
                        + glDetails.getGl_desc() + ", GLSH Description: " + glDetails.getGlsh_desc());
            } else {
//                 Handle cases where no GL details are found for the account number
                glshCodes.add(null);
                glshDescs.add(null);
                glCodes.add(null);
                glDescs.add(null);

                // Print account number and name with a message for missing GL details
                System.out.println(
                        "Account Number: " + acctNum + ", Account Name: " + acctName + " - No GL details found.");
            }
        }
        System.out.println("sdfdsf ---- "+accountNumbers+" ---- "+acctTranDate);
        String result = insertOrUpdateAccountBalancesBatchJob(md, rq, accountNumbers, netAmounts, glshCodes, glshDescs, glCodes,
                glDescs, accountNames, totalCredits, totalDebits, formattedDate,acctTranDate);

        // Return the result of the insertOrUpdateAccountBalances
        return result;
    }


    @RequestMapping(value = "Doatransactionpush", method = RequestMethod.POST)
    @ResponseBody
    public String VerifyScreens(Model md, HttpServletRequest rq, @ModelAttribute DAB_Entity DAB_Entity,
                                @RequestParam("trndate") String trndate) throws java.text.ParseException {
        // Get TRANDATE from session as a String

		/*
		 * System.out.println("Trndate"+trndate); Date TRANDATE = (Date)
		 * rq.getSession().getAttribute("TRANDATE"); System.out.println("TRANDATE: " +
		 * TRANDATE); SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 * String formattedDate = dateFormat.format(TRANDATE);
		 */
        
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date TRANDATE =sdf.parse("2025-10-02"); // manually assigned date
          System.out.println("TRANDATE: " + TRANDATE);
         
          String formattedDate = sdf.format(TRANDATE);

        System.out.println("formattedDate"+formattedDate);
        List<Object[]> debitCreditData = tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDate(formattedDate);
        // List<Object[]> debitCreditData =
        // tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDatedemo(formattedDate);

        List<String> accountNumbers = new ArrayList<>();
        List<String> netAmounts = new ArrayList<>();
        List<String> accountNames = new ArrayList<>();

        // Lists for each field
        List<String> glshCodes = new ArrayList<>();
        List<String> glshDescs = new ArrayList<>();
        List<String> glCodes = new ArrayList<>();
        List<String> glDescs = new ArrayList<>();
        List<BigDecimal> totalCredits = new ArrayList<>();
        List<BigDecimal> totalDebits = new ArrayList<>();

        // Step 1: Get all account numbers from `debitCreditData`
        for (Object[] record : debitCreditData) {
            accountNumbers.add(record[0].toString()); // acct_num
            accountNames.add(record[1].toString()); // acct_name
            netAmounts.add(record[5].toString()); // NETAMT
            totalCredits.add((BigDecimal) record[3]); // TOTAL_CREDIT
            totalDebits.add((BigDecimal) record[4]); // TOTAL_DEBIT
        }

        // Print accountNumbers, accountNames, netAmounts, totalCredits, and totalDebits
        System.out.println("Account Numbers: " + accountNumbers);
        System.out.println("Account Names: " + accountNames);
        System.out.println("Net Amounts: " + netAmounts);
        System.out.println("Total Credits: " + totalCredits);
        System.out.println("Total Debits: " + totalDebits);

        List<Chart_Acc_Entity> allAccounts = new ArrayList<>();
        int batchSize = 1000;
        for (int i = 0; i < accountNumbers.size(); i += batchSize) {
            List<String> batch = accountNumbers.subList(i, Math.min(i + batchSize, accountNumbers.size()));
            allAccounts.addAll(chart_Acc_Rep.getcoaaccunt_num(batch));
        }
        List<Chart_Acc_Entity> exist = allAccounts;
        // Step 2: Fetch GL details for these account numbers
        //List<Chart_Acc_Entity> exist = chart_Acc_Rep.getcoaaccunt_num(accountNumbers);

        // Step 3: Create a map to store GL details for each account number
        Map<String, Chart_Acc_Entity> glDetailsMap = new HashMap<>();
        for (Chart_Acc_Entity up1 : exist) {
            glDetailsMap.put(up1.getAcct_num(), up1);
        }

        // Step 4: Retrieve GL details in the same order as `accountNumbers`
        for (int i = 0; i < accountNumbers.size(); i++) {
            String acctNum = accountNumbers.get(i);
            String acctName = accountNames.get(i);

            Chart_Acc_Entity glDetails = glDetailsMap.get(acctNum);
            if (glDetails != null) {
                glshCodes.add(glDetails.getGlsh_code());
                glshDescs.add(glDetails.getGlsh_desc());
                glCodes.add(glDetails.getGl_code());
                glDescs.add(glDetails.getGl_desc());

                // Print account number, account name, and corresponding GL details
                System.out.println("Account Number: " + acctNum + ", Account Name: " + acctName + ", GL Code: "
                        + glDetails.getGl_code() + ", GLSH Code: " + glDetails.getGlsh_code() + ", GL Description: "
                        + glDetails.getGl_desc() + ", GLSH Description: " + glDetails.getGlsh_desc());
            } else {
                // Handle cases where no GL details are found for the account number
                glshCodes.add(null);
                glshDescs.add(null);
                glCodes.add(null);
                glDescs.add(null);

                // Print account number and name with a message for missing GL details
                System.out.println(
                        "Account Number: " + acctNum + ", Account Name: " + acctName + " - No GL details found.");
            }
        }

        String result = insertOrUpdateAccountBalances(md, rq, accountNumbers, netAmounts, glshCodes, glshDescs, glCodes,
                glDescs, accountNames, totalCredits, totalDebits, formattedDate);

        // System.out.println("Result of insertOrUpdateAccountBalances: " + result);

        // Return the result of the insertOrUpdateAccountBalances
        return result;
        // return "Demo Perpocess";
    }

    /*
     * @Transactional public String insertOrUpdateAccountBalances(List<String>
     * accountNumbers, List<String> netAmounts) { LocalDate today = LocalDate.now();
     *
     * for (int i = 0; i < accountNumbers.size(); i++) { String accountNum =
     * accountNumbers.get(i); BigDecimal netAmount = new
     * BigDecimal(netAmounts.get(i));
     *
     * // Step 1: Check if account number exists List<BigDecimal> existingBalances =
     * tRAN_MAIN_TRM_WRK_REP.findTRAN_DATE_BALByAccountNumber(accountNum);
     *
     * if (!existingBalances.isEmpty()) { // Account exists, update END_TRAN_DATE of
     * the latest record to yesterday System.out.println("Account exists: " +
     * accountNum + ", updating END_TRAN_DATE to yesterday.");
     * tRAN_MAIN_TRM_WRK_REP.updateEndDateToYesterday(accountNum,
     * today.minusDays(1));
     *
     * // Set TRAN_DATE_BAL to the sum of existing balance and netAmount BigDecimal
     * newTRAN_DATE_BAL = existingBalances.get(0).add(netAmount);
     * System.out.println("New TRAN_DATE_BAL for account " + accountNum + ": " +
     * newTRAN_DATE_BAL + " (Existing: " + existingBalances.get(0) +
     * " + Net Amount: " + netAmount + ")");
     * tRAN_MAIN_TRM_WRK_REP.insertNewAccountBalance(accountNum, newTRAN_DATE_BAL,
     * netAmount); } else { // Account does not exist, simply insert new row with
     * netAmount as TRAN_DATE_BAL System.out.println("Account does not exist: " +
     * accountNum + ", inserting new row with TRAN_DATE_BAL: " + netAmount);
     * tRAN_MAIN_TRM_WRK_REP.insertNewAccountBalance(accountNum, netAmount,
     * netAmount); } } return "successfully submited"; }
     */
    @Transactional
    public String insertOrUpdateAccountBalances(Model md, HttpServletRequest rq, List<String> accountNumbers,
                                                List<String> netAmounts, List<String> glshCodes, List<String> glshDescs, List<String> glCodes,
                                                List<String> glDescs, List<String> accunt_name, List<BigDecimal> totalCredits, List<BigDecimal> totalDebits,
                                                String TRANDATE) throws java.text.ParseException {
        // TRANDATE is now a String and will be used in the queries

		/*
		 * Date TRANDATE1 = (Date) rq.getSession().getAttribute("TRANDATE");
		 * System.out.println(TRANDATE1 + "TRANDATE"); SimpleDateFormat dateFormat = new
		 * SimpleDateFormat("yyyy-MM-dd"); String formattedDate
		 * =dateFormat.format(TRANDATE1);
		 */
        
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
          Date TRANDATE1 =sdf.parse("2025-10-02"); // manually assigned date
          System.out.println("TRANDATE: " + TRANDATE1);
         
          String formattedDate = sdf.format(TRANDATE1);
         
        System.out.println("Formatted Date: " + formattedDate);

        for (int i = 0; i < accountNumbers.size(); i++) {
            String accountNum = accountNumbers.get(i);
            BigDecimal netAmount = new BigDecimal(netAmounts.get(i));
            BigDecimal totalCredit = totalCredits.get(i);
            BigDecimal totalDebit = totalDebits.get(i);

            String currentGlshCode = glshCodes.get(i);
            String currentGlshDesc = glshDescs.get(i);
            String currentGlCode = glCodes.get(i);
            String currentGlDesc = glDescs.get(i);
            String accountName = accunt_name.get(i);

            System.out.println("Processing Account: " + accountNum);
            System.out.println(
                    "Net Amount: " + netAmount + ", Total Credit: " + totalCredit + ", Total Debit: " + totalDebit);
            System.out.println("GLSH Code: " + currentGlshCode + ", GLSH Desc: " + currentGlshDesc);
            System.out.println("GL Code: " + currentGlCode + ", GL Desc: " + currentGlDesc);
            System.out.println("Account Name: " + accountName);
            System.out.println("Checking if today's record exists for Account: " + accountNum);

            // Check if account number exists and fetch existing balances

            // boolean transactionDateExists =
            // tRAN_MAIN_TRM_WRK_REP.checkTransactionDateExists(accountNum, TRANDATE) == 1;

            /*
             * if (transactionDateExists) { // Show message that transaction date already
             * exists System.out.println("Transaction date " + TRANDATE +
             * " already exists for Account " + accountNum + ". Cannot be updated.");
             * continue; // Skip to the next account number }
             */

            List<BigDecimal> existingBalances = tRAN_MAIN_TRM_WRK_REP
                    .findLatestTRAN_DATE_BALByAccountNumber(accountNum);

            BigDecimal openbal = BigDecimal.ZERO;
            //BigDecimal TrandateBal = BigDecimal.ZERO;
            if (!existingBalances.isEmpty()) {
                System.out.println("Account " + accountNum + " exists. Updating END_TRAN_DATE to yesterday.");

                // Update END_TRAN_DATE of the latest record to yesterday
                try {
                    LocalDate tranDate = LocalDate.parse(formattedDate);
                    // Call the update method with the date minus one day
                    tRAN_MAIN_TRM_WRK_REP.updateEndDateToYesterday1(accountNum, tranDate.minusDays(1));
                    openbal=tRAN_MAIN_TRM_WRK_REP.getTrandateBal(accountNum, tranDate);

                    //TrandateBal=tRAN_MAIN_TRM_WRK_REP.getTrandateBal(accountNum, tranDate.minusDays(1));
                } catch (DateTimeParseException e) {
                    // Handle parsing error (e.g., log the error or set a default value)
                    System.err.println("Invalid date format for TRANDATE: " + TRANDATE);
                }

                // BigDecimal updatedTrandateBal = netAmount.subtract(TrandateBal);


                // Calculate new TRAN_DATE_BAL
                BigDecimal latestBalance = existingBalances.get(0);
                BigDecimal newTRAN_DATE_BAL = latestBalance.add(netAmount);

                //BigDecimal updatedOpenBal=openbal.add(netAmount);

                //BigDecimal updatedOpenBal = (openbal == null ? BigDecimal.ZERO : openbal).add(netAmount == null ? BigDecimal.ZERO : netAmount);





                System.out.println("Existing Balance: " + latestBalance);
                System.out.println("New TRAN_DATE_BAL: " + newTRAN_DATE_BAL);

                // Insert new account balance with debit and credit columns
                System.out.println("Inserting updated balance record for Account " + accountNum);
                tRAN_MAIN_TRM_WRK_REP.UpdateExsistAccountBalance(currentGlCode, currentGlDesc, currentGlshCode,
                        currentGlshDesc, accountNum, accountName, "KES", newTRAN_DATE_BAL, TRANDATE, // Ensure TRANDATE
                        // is in the
                        // correct
                        // format
                        netAmount, totalDebit, totalCredit,openbal);

            } else {
                // Insert new row with netAmount as TRAN_DATE_BAL
                System.out.println("Account " + accountNum + " does not exist. Inserting new row with TRAN_DATE_BAL: "
                        + netAmount);
                tRAN_MAIN_TRM_WRK_REP.insertNewAccountBalance(currentGlCode, currentGlDesc, currentGlshCode,
                        currentGlshDesc, accountNum, accountName, "KES", netAmount, TRANDATE, // Ensure TRANDATE is in
                        // the correct format
                        netAmount, totalDebit, totalCredit,openbal);

            }
        }
        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);
        if (existingRecord != null) {

            // 2️⃣ Log past value
            existingRecord.setMov_dac("Completed");
            existingRecord.setDcp_status("DCP-1");
            // 4️⃣ Save updated record
            bGLS_CONTROL_TABLE_REP.save(existingRecord);

        } else {
            System.out.println("No record found in BGLS_Control_Table");

        }
        return "Account balances successfully inserted";
    }


    @Transactional
    public String insertOrUpdateAccountBalancesBatchJob(Model md, HttpServletRequest rq, List<String> accountNumbers,
                                                        List<String> netAmounts, List<String> glshCodes, List<String> glshDescs, List<String> glCodes,
                                                        List<String> glDescs, List<String> accunt_name, List<BigDecimal> totalCredits, List<BigDecimal> totalDebits,
                                                        String TRANDATE2,List<Date> acctTranDate) throws java.text.ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < accountNumbers.size(); i++) {
            Date trandate = acctTranDate.get(i);
            String formattedDate =dateFormat.format(trandate);
            String accountNum = accountNumbers.get(i);
            BigDecimal netAmount = new BigDecimal(netAmounts.get(i));
            BigDecimal totalCredit = totalCredits.get(i);
            BigDecimal totalDebit = totalDebits.get(i);

            String currentGlshCode = glshCodes.get(i);
            String currentGlshDesc = glshDescs.get(i);
            String currentGlCode = glCodes.get(i);
            String currentGlDesc = glDescs.get(i);
            String accountName = accunt_name.get(i);

            System.out.println("Processing Account: " + accountNum);
            System.out.println(
                    "Net Amount: " + netAmount + ", Total Credit: " + totalCredit + ", Total Debit: " + totalDebit);
            System.out.println("GLSH Code: " + currentGlshCode + ", GLSH Desc: " + currentGlshDesc);
            System.out.println("GL Code: " + currentGlCode + ", GL Desc: " + currentGlDesc);
            System.out.println("Account Name: " + accountName);
            LocalDate tranDate = LocalDate.parse(formattedDate);


            SimpleDateFormat oracleFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate1 = tranDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println("Checking if today's record exists for Account: " + accountNum+" - "+formattedDate1+" -- "+tranDate + " --- "+formattedDate);
            List<BigDecimal> existingBalances = tRAN_MAIN_TRM_WRK_REP.findLatestTRAN_DATE_BALByAccountNumberBatchJob(accountNum,formattedDate1);

            System.out.println("After Result : "+existingBalances.isEmpty());
            BigDecimal openbal = BigDecimal.ZERO;
            if (!existingBalances.isEmpty()) {
                System.out.println("Account " + accountNum + " exists. Updating END_TRAN_DATE to yesterday.");


                // Update END_TRAN_DATE of the latest record to yesterday
                try {

                    openbal=tRAN_MAIN_TRM_WRK_REP.getTrandateBalBatchJob(accountNum, formattedDate1);

                    //TrandateBal=tRAN_MAIN_TRM_WRK_REP.getTrandateBal(accountNum, tranDate.minusDays(1));
                } catch (DateTimeParseException e) {
                    // Handle parsing error (e.g., log the error or set a default value)
                    System.err.println("Invalid date format for TRANDATE: " + tranDate);
                }

                // BigDecimal updatedTrandateBal = netAmount.subtract(TrandateBal);


                // Calculate new TRAN_DATE_BAL
                BigDecimal latestBalance = existingBalances.get(0);
                BigDecimal newTRAN_DATE_BAL = latestBalance.add(netAmount);

                //BigDecimal updatedOpenBal=openbal.add(netAmount);
//                BigDecimal updatedOpenBal = (openbal == null ? BigDecimal.ZERO : openbal).add(netAmount == null ? BigDecimal.ZERO : netAmount);

                System.out.println("Existing Balance: " + latestBalance);
                System.out.println("New TRAN_DATE_BAL: " + newTRAN_DATE_BAL);

                // Insert new account balance with debit and credit columns
                System.out.println("Inserting updated balance record for Account " + accountNum);
                tRAN_MAIN_TRM_WRK_REP.UpdateExsistAccountBalanceBatchJob(currentGlCode, currentGlDesc, currentGlshCode,
                        currentGlshDesc, accountNum, accountName, "KES", newTRAN_DATE_BAL, formattedDate1,
                        netAmount, totalDebit, totalCredit,openbal);
            }
        }
        return "Account balances successfully inserted";
    }


    /*
     * @RequestMapping(value = "journalvalid", method = { RequestMethod.GET,
     * RequestMethod.POST }) public String journalvalid(@RequestParam(required =
     * false) String formmode, Model model, HttpServletRequest
     * request,@RequestParam(required = false) @DateTimeFormat(pattern =
     * "yyyy-MM-dd") Date current) {
     *
     *
     * List<TRAN_MAIN_TRM_WRK_ENTITY> work =
     * tRAN_MAIN_TRM_WRK_REP.passdate(current);
     *
     * // Use the 'work' list as required for (TRAN_MAIN_TRM_WRK_ENTITY entity :
     * work) { // Do something with each entity }
     * System.out.println("passdate"+work);
     *
     * return ""; }
     */

    @PostMapping(value = "gluploadexcel")
    @ResponseBody
    public String gluploadexcel(@RequestParam("file") MultipartFile file, String screenId,
                                @ModelAttribute GeneralLedgerWork_Entity GeneralLedgerWork_Entity, Model md, HttpServletRequest rq)
            throws FileNotFoundException, SQLException, IOException, NullPointerException {

        System.out.println("the testing   GST EXCEL UPLOAD");

        System.out.println("fileSize" + file.getSize());

        if (file.getSize() < 50000000) {
            String userid = (String) rq.getSession().getAttribute("USERID");
            String msg = bGLS_Inventeryservice2.Uploadgstserviceone(screenId, file, userid, GeneralLedgerWork_Entity);
            return msg;
        } else {
            return "File has not been successfully uploaded. Requires less than 128 KB size.";
        }

    }

    @PostMapping("/multilinejournalentries")
    @ResponseBody
    public String upLoadForm1(@RequestBody List<TRAN_MAIN_TRM_WRK_ENTITY> transactionList, HttpServletRequest req,
                              @RequestParam(required = false) String tran_date, @RequestParam(required = false) String tran_type)
            throws java.text.ParseException {
        String userId = (String) req.getSession().getAttribute("USERID");
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        System.out.println(transactionList.toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // Adjust this pattern as needed
        Date date = dateFormat.parse(tran_date);
        java.util.Date utilDate = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Step 2: Convert java.util.Date to java.sql.Date
        Date entryDate = new Date(utilDate.getTime());

        System.out.println(tran_date + "tran_date");
        System.out.println(tran_type + "tran_type");
        List<TRAN_MAIN_TRM_WRK_ENTITY> savedRecords = new ArrayList<>();

        for (TRAN_MAIN_TRM_WRK_ENTITY dynamic : transactionList) {
            System.err.println(" size " + transactionList.size());
            System.out.println(dynamic);
            System.out.println(dynamic.getTran_id());
            System.out.println(dynamic.getPart_tran_type());
            // Create a new instance of TRAN_MAIN_TRM_WRK_ENTITY
            TRAN_MAIN_TRM_WRK_ENTITY bdcm = new TRAN_MAIN_TRM_WRK_ENTITY();

            // Copy fields from the dynamic object to bdcm
            bdcm.setTran_id(dynamic.getTran_id());
            String nextSerialNumber = tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID(); // Fetch the next SRLNO
            bdcm.setSrl_no(nextSerialNumber);
            bdcm.setPart_tran_id(dynamic.getPart_tran_id());
            bdcm.setAcct_num(dynamic.getAcct_num());
            bdcm.setAcct_name(dynamic.getAcct_name());
            bdcm.setPart_tran_type(dynamic.getPart_tran_type());
            bdcm.setTran_amt(dynamic.getTran_amt());
            bdcm.setTran_particular(dynamic.getTran_particular());
            bdcm.setTran_remarks(dynamic.getTran_remarks());
            bdcm.setRate_code(dynamic.getRate_code());
            bdcm.setRate(dynamic.getRate());
            bdcm.setAdd_details(dynamic.getAdd_details());
            bdcm.setTran_type(tran_type);
            bdcm.setTran_date(date);
            bdcm.setAcct_crncy("SCR");
            bdcm.setDel_flg("N");
            bdcm.setValue_date(date);
            bdcm.setEntry_user(userId);
            bdcm.setTran_status("ENTERED");
            bdcm.setEntry_time(entryDate);

            // Save the record
            tranMainRep.save(bdcm);

            // Add to the list of saved records
            savedRecords.add(bdcm);

            // Increment srlNo for the next record
        }

        // Return the list of saved records
        return "successfully Added";
    }

    /* Praveen */
    @RequestMapping(value = "interestBatchJob_test", method = { RequestMethod.GET, RequestMethod.POST })
    public String interestBatchJob_test(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {

        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("TRANDATE", TRANDATE);
            md.addAttribute("booking", LOAN_ACT_MST_REPO.getActNo());
            md.addAttribute("booking1", depositRep.getexistingData());

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
        }
        return "InterestBatchJob";
    }

    @RequestMapping(value = "interestBatchJob", method = { RequestMethod.GET, RequestMethod.POST })
    public String interestBatchJob(@RequestParam(required = false) String formmode, Model model,
                                   HttpServletRequest request) throws java.text.ParseException {
        // Journal
        Date TRANDATE = (Date) request.getSession().getAttribute("TRANDATE");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(TRANDATE);

        String Userid = (String) request.getSession().getAttribute("USERID");

        model.addAttribute("Userid", Userid);

        model.addAttribute("Valuesfordebit", tRAN_MAIN_TRM_WRK_REP.getwofordebitvalues());
        model.addAttribute("ValuesforCredit", tRAN_MAIN_TRM_WRK_REP.getwoforcreditvalues());
        model.addAttribute("Valuesfornotransaction", tRAN_MAIN_TRM_WRK_REP.getTransactionValues());

        model.addAttribute("ValuesforDel", tRAN_MAIN_TRM_WRK_REP.getdelvalues());
        model.addAttribute("Valuesforunpost", tRAN_MAIN_TRM_WRK_REP.getunpostedvalues());

        model.addAttribute("Valuesforaccbalance", chart_Acc_Rep.getaccbalance());


        // ACCOUNT BALANCE
        model.addAttribute("Valuesforaccopen", chart_Acc_Rep.getacctbalcredit());
        model.addAttribute("Valuesforacclose", chart_Acc_Rep.getacctbaldebit());

        // GL BALANCE
        model.addAttribute("Valuesforgldebit", chart_Acc_Rep.getGLbaldebit());
        model.addAttribute("Valuesforglcredit", chart_Acc_Rep.getGLbalcredit());

        // day end balance
        model.addAttribute("getWoforDebitValues", tRAN_MAIN_TRM_WRK_REP.getWoforDebitValues());
        model.addAttribute("getWoforcreditValues", tRAN_MAIN_TRM_WRK_REP.getWoforcreditValues());
        model.addAttribute("getWofortotalValues", tRAN_MAIN_TRM_WRK_REP.getWoforTotalValues());

        List<Object[]> debitCreditData = tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDate(formattedDate);

        if (debitCreditData.size() > 3) {
            Object[] elementAtFour = debitCreditData.get(3);
            System.out.println(Arrays.toString(elementAtFour) + " debitCreditData");
        } else {
            System.out.println("The list does not contain enough elements.");
        }
        // System.out.println(debitCreditData.get(3)+"debitCreditData");
        model.addAttribute("debitCreditData", debitCreditData);
        // model.addAttribute("getNetDebitCreditForCurrentDate",tRAN_MAIN_TRM_WRK_REP.getNetDebitCreditWithCountForCurrentDate());

        BigDecimal getTotalValues = tRAN_MAIN_TRM_WRK_REP.getTotalValues(formattedDate);
        model.addAttribute("totalamount", getTotalValues);
        // model.addAttribute("gettotalValues",tRAN_MAIN_TRM_WRK_REP.getTotalValues());

        Object totalTransactionCount = tRAN_MAIN_TRM_WRK_REP.getTotalTransactionCount(formattedDate);
        model.addAttribute("totaltransaction", totalTransactionCount);

        model.addAttribute("getTotalCredit", tRAN_MAIN_TRM_WRK_REP.getTotalCredit(formattedDate));
        model.addAttribute("getTotalDebit", tRAN_MAIN_TRM_WRK_REP.getTotalDebit(formattedDate));


        model.addAttribute("Journal",bGLS_CONTROL_TABLE_REP.getJournal());
        System.out.println("Journal"+bGLS_CONTROL_TABLE_REP.getJournal());
        model.addAttribute("AccountLedger",bGLS_CONTROL_TABLE_REP.getAccountLedger());
        model.addAttribute("HolidayCheck",bGLS_CONTROL_TABLE_REP.getHolidayCheck());
        model.addAttribute("DAB",bGLS_CONTROL_TABLE_REP.getDAB());
        model.addAttribute("ConsistencyCheck",bGLS_CONTROL_TABLE_REP.getConsistencyCheck());
        model.addAttribute("MovementofTransaction",bGLS_CONTROL_TABLE_REP.getMovementofTransaction());
        model.addAttribute("GlConsolidation",bGLS_CONTROL_TABLE_REP.getGlConsolidation());
        model.addAttribute("InterestDemand",bGLS_CONTROL_TABLE_REP.getInterestDemand());
        model.addAttribute("FeeDemand",bGLS_CONTROL_TABLE_REP.getFeeDemand());
        model.addAttribute("Penalty",bGLS_CONTROL_TABLE_REP.getPenalty());
        model.addAttribute("dcpstatus",bGLS_CONTROL_TABLE_REP.getDcpStatus());
        System.out.println("dcpstatus"+bGLS_CONTROL_TABLE_REP.getDcpStatus());
        return "Batch_job_Day_end_Operation.html";
    }


    //SURIYA
    @PostMapping("/uploadatatogled")
    @ResponseBody
    public String uploadatatogled(@RequestBody List<GeneralLedgerWork_Entity> generalLedgerWorkEntities,
                                  HttpServletRequest req) throws java.text.ParseException {
        String userid = (String) req.getSession().getAttribute("USERID");
        // List to store the saved records of GeneralLedgerEntity
        List<GeneralLedgerEntity> savedRecords = new ArrayList<>();
        System.out.println(savedRecords);
        // Iterate through the list of GeneralLedgerWork_Entity received from the
        // request
        for (GeneralLedgerWork_Entity glWorkEntity : generalLedgerWorkEntities) {

            // Create a new instance of GeneralLedgerEntity
            GeneralLedgerEntity newLedgerEntity = new GeneralLedgerEntity();

            // Copy fields from GeneralLedgerWork_Entity to GeneralLedgerEntity
            newLedgerEntity.setGlCode(glWorkEntity.getGlCode());
            newLedgerEntity.setGlDescription(glWorkEntity.getGlDescription());
            newLedgerEntity.setBranch_id(glWorkEntity.getBranch_id());
            newLedgerEntity.setBranch_desc(glWorkEntity.getBranch_desc());
            newLedgerEntity.setGlsh_code(glWorkEntity.getGlsh_code());
            newLedgerEntity.setGlsh_desc(glWorkEntity.getGlsh_desc());
            newLedgerEntity.setCrncy_code(glWorkEntity.getCrncy_code());
            newLedgerEntity.setBal_sheet_group(glWorkEntity.getBal_sheet_group());
            newLedgerEntity.setSeq_order(glWorkEntity.getSeq_order());
            newLedgerEntity.setGl_type(glWorkEntity.getGl_type());
            newLedgerEntity.setGl_type_description(glWorkEntity.getGl_type_description());
            newLedgerEntity.setModule(glWorkEntity.getModule());
            newLedgerEntity.setRemarks(glWorkEntity.getRemarks());
            newLedgerEntity.setNo_acct_closed(glWorkEntity.getNo_acct_closed());
            newLedgerEntity.setNo_acct_opened(glWorkEntity.getNo_acct_opened());
            newLedgerEntity.setTotal_balance(glWorkEntity.getTotal_balance());

            generalLedgerRep.save(newLedgerEntity);

            // Add to the list of saved records
            savedRecords.add(newLedgerEntity);
        }
        Optional<UserProfile> up1 = userProfileRep.findById(userid);
        UserProfile user = up1.get();

        audit.insertServiceAudit(user.getUserid(), user.getUsername(), "GENERAL LEDGER UPLOAD", "UPLOADED SUCCESSFULLY","BGLS_GL_WORK", "GENERAL LEDGER","-");
        // Return a response message or the list of saved records
        return "Records uploaded and saved successfully.";

    }

    @RequestMapping(value = "participatingJobs", method = { RequestMethod.GET, RequestMethod.POST })
    public String participatingJobs(@RequestParam(required = false) String formmode, Model model,
                                    @RequestParam(required = false) String acct_num,

                                    HttpServletRequest request) {
        if (formmode == null || formmode.equals("list")) {
            model.addAttribute("formmode", "list");
            model.addAttribute("chartaccount", participatingBanks_Repo.getList());
        } else if (formmode.equals("view")) {
            model.addAttribute("formmode", "view");
            model.addAttribute("chartaccount", participatingBanks_Repo.getList(acct_num));

        }

        return "Participating_Banks.html";
    }

    @RequestMapping(value = "leasecollection", method = { RequestMethod.GET, RequestMethod.POST })
    public String leasecollection(@RequestParam(required = false) String formmode, Model model,
                                  HttpServletRequest request) {
        if (formmode == null || formmode.equals("upload")) {

            model.addAttribute("formmode", "upload");

        } else if (formmode.equals("uploadlist")) {

            model.addAttribute("formmode", "uploadlist");
            model.addAttribute("Listofvalues", collection_Process_Repo.getlistvalue());

        }

        return "Lease_Collection.html";
    }

    @PostMapping(value = "leaseuploadexcel")
    @ResponseBody
    public String leaseuploadexcel(@RequestParam("file") MultipartFile file, String screenId,
                                   @ModelAttribute com.bornfire.entities.Collection_Process_Entity Collection_Process_Entity, Model md,
                                   HttpServletRequest rq) throws FileNotFoundException, SQLException, IOException, NullPointerException {

        System.out.println("the testing   GST EXCEL UPLOAD");

        System.out.println("fileSize" + file.getSize());

        if (file.getSize() < 50000000) {
            String userid = (String) rq.getSession().getAttribute("USERID");
            String msg = CustomerRequestService.UploadgstserviceCOLLECTION(screenId, file, userid,
                    Collection_Process_Entity);
            return msg;
        } else {
            return "File has not been successfully uploaded. Requires less than 128 KB size.";
        }
    }

    @RequestMapping(value = "getrecordfromcoa", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Test_Collection_Entity> getrecordfromcoa(@RequestParam(required = false) String nic_no,
                                                         HttpServletRequest req) {

        Date flowdate = (Date) req.getSession().getAttribute("TRANDATE");

        List<Test_Collection_Entity> return_records = new ArrayList<>();

        String customer_id = bACP_CUS_PROFILE_REPO.findBynic(nic_no);

        System.out.println(customer_id);

        List<Lease_Loan_Master_Entity> loanrecords = lease_Loan_Master_Repo.getcusrecords(customer_id);

        for (Lease_Loan_Master_Entity loan : loanrecords) {

            String acct_num = loan.getLoan_accountno();
            BigDecimal acct_balance = chart_Acc_Rep.getbal(acct_num);
            BigDecimal flowAmount = BigDecimal.ZERO;

            List<DMD_TABLE> demand_records = dMD_TABLE_REPO.getdemand(acct_num, flowdate);

            for (DMD_TABLE demand : demand_records) {
                flowAmount = flowAmount.add(demand.getFlow_amt());
            }

            Test_Collection_Entity testentity = new Test_Collection_Entity();
            testentity.setCustomer_id(loan.getCustomer_id());
            testentity.setLoan_accountno(loan.getLoan_accountno());
            testentity.setCustomer_name(loan.getCustomer_name());
            testentity.setLoan_outstanding(acct_balance);
            testentity.setFlow_code("RIDEM");
            testentity.setFlow_date(demand_records.get(0).getFlow_date());
            testentity.setFlow_amt(flowAmount);
            return_records.add(testentity);

        }

        return return_records;
    }

    @RequestMapping(value = "Account_Balances", method = { RequestMethod.GET, RequestMethod.POST })
    public String Account_Balances(@RequestParam(required = false) String formmode, Model model,
                                   HttpServletRequest request) {

        Date TRANDATE = (Date) request.getSession().getAttribute("TRANDATE");
        System.out.println(TRANDATE + "TRANDATE");

        /*
         * if (formmode == null || formmode.equals("list")) {
         * model.addAttribute("formmode", "list");
         *
         *
         * }
         */
        if (formmode == null || formmode.equals("Lease")) {
            model.addAttribute("formmode", "Lease");
//			model.addAttribute("leaseaccount", lease_Loan_Master_Repo.getLeaseBalTran(TRANDATE));

            model.addAttribute("leaseaccount", lease_Loan_Master_Repo.getLeaseBalTran());
            model.addAttribute("TRANDATE", TRANDATE);
        } else if (formmode.equals("Deposit")) {
            model.addAttribute("formmode", "Deposit");
            model.addAttribute("depsoitaccount", depositRep.getDepositBalTran(TRANDATE));
            model.addAttribute("TRANDATE", TRANDATE);
        }

        return "Account_Balances.html";
    }






    /* Praveen */
    @RequestMapping(value = "LeaseOperaions", method = { RequestMethod.GET, RequestMethod.POST })
    public String LeaseOperaions(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("booking", lease_Loan_Work_Repo.getActNo());

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");

        }
        return "LeaseOperations";
    }

    @RequestMapping(value = "trialBalanceReports1", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Object[]> trialBalanceReports1(@RequestParam(required = false) String formmode,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date balancedate,
                                               @RequestParam(required = false) String tran, Model md, HttpServletRequest rq) {

        List<Object[]> balances = chart_Acc_Rep.getListtraildate(balancedate);

        // Convert List<Object[]> to List<Map<String, Object>>
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : balances) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("gl_code", row[0]);          // GL code
            rowMap.put("acct_name", row[1]);        // Account name
            rowMap.put("opening_bal", row[2]);      // Opening balance
            rowMap.put("credit", row[3]);           // Total credit
            rowMap.put("debit", row[4]);            // Total debt
            rowMap.put("net_change", row[5]);       // Net change
            rowMap.put("closing_bal", row[6]);      // Closing balance
            result.add(rowMap);
        }
        return balances;
    }

    @RequestMapping(value = "trialBalanceReports2", method = { RequestMethod.GET, RequestMethod.POST })
    public String trialBalanceReports2(@RequestParam(required = false) String formmode,
                                       @RequestParam(required = false) String tran, Model md, HttpServletRequest rq,
                                       @RequestParam(required = false) String glshCode, HttpServletRequest request) {

        Date TRANDATE = (Date) request.getSession().getAttribute("TRANDATE");
        System.out.println(TRANDATE);
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            System.out.println("balance");
            md.addAttribute("trialbal", chart_Acc_Rep.getListtrail());
            //md.addAttribute("trialbalance", chart_Acc_Rep.getList());
            // Date TRANDATE = new Date(); // Replace with your actual date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(TRANDATE);
            md.addAttribute("TRANDATE", TRANDATE);

        }

        return "TrailBalanceReports2";
    }

    @RequestMapping(value = "trialBalance", method = { RequestMethod.GET, RequestMethod.POST })
    public String trialBalance(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String tran, Model md, HttpServletRequest rq,
                               @RequestParam(required = false) String glshCode, HttpServletRequest request) {

        Date TRANDATE = (Date) request.getSession().getAttribute("TRANDATE");
        System.out.println(TRANDATE);
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            System.out.println("balance");
            md.addAttribute("trialbal", chart_Acc_Rep.getListtrail());
            //md.addAttribute("trialbalance", chart_Acc_Rep.getList());
            // Date TRANDATE = new Date(); // Replace with your actual date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(TRANDATE);
            md.addAttribute("TRANDATE", TRANDATE);

        }

        return "TrailBalanceReports2";
    }

    @RequestMapping(value = "BGLS/ghlslistdata", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Chart_Acc_Entity> ghlslistdata(@RequestParam(required = false) String glshCode) {
        System.out.println("gld");
        System.out.println(glshCode);
        List<Chart_Acc_Entity> GLSHValue = chart_Acc_Rep.getglsh(glshCode);
        System.out.println("THE GLSH RECORD IS" + glshCode);
        return GLSHValue;
    }

    /* THANVEER */
    @RequestMapping(value = "BGLS/ghlslistdataDAB", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Map<String, Object>> ghlslistdataDAB(HttpServletRequest rq,
                                                     @RequestParam(required = false) String glshCode, @RequestParam(required = false) String reportdate) {
        Date TRANDATE1 = (Date) rq.getSession().getAttribute("TRANDATE");
        if (TRANDATE1 == null) {
            System.out.println("TRANDATE not found in session.");
            return Collections.emptyList(); // or handle as needed
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(TRANDATE1);

        // Print to check the format
        System.out.println("Formatted TRANDATE1: " + formattedDate);
        System.out.println("GLSH Code: " + glshCode);

        String formattedDateStr = dateFormat.format(TRANDATE1);

        List<Object[]> balances = dAB_Repo.getAccountBalancesByGlshCode(reportdate, glshCode);

        System.out.println(balances.size());
        // Convert List<Object[]> to List<Map<String, Object>>
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : balances) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("glsh_code", row[2]); // glsh_code
            rowMap.put("acct_num", row[0]); // acct_num
            rowMap.put("acct_name", row[1]); // acct_name
            rowMap.put("total_credit", row[3]); // totalCredit
            rowMap.put("total_debit", row[4]); // totalDebit
            result.add(rowMap);
        }
        System.out.println(result.size());
        return result;
    }


    @RequestMapping(value = "accountLedgerdayend", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> accountLedgerdayend(@RequestParam("trndate") String trndate) {
        System.out.println(trndate + " trndate");

        // Fetch count from repository
        Object result = tRAN_MAIN_TRM_WRK_REP.getvalueusingdate(trndate);

        Long count = 0L;
        if (result != null) {
            if (result instanceof Number) {
                count = ((Number) result).longValue();
            } else if (result instanceof Object[]) {
                Object[] arr = (Object[]) result;
                if (arr.length > 0 && arr[0] != null) {
                    count = Long.parseLong(arr[0].toString());
                }
            }
        }

        // Update ledger_cons
        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);
        if (existingRecord != null) {
            existingRecord.setLedger_cons("Completed");
            existingRecord.setDcp_status("PRE-DCP-2");
            bGLS_CONTROL_TABLE_REP.save(existingRecord);

            System.out.println("Ledger updated successfully.");
        }

        // Prepare JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("table", "TRM");
        response.put("count", count);
        response.put("message", "Successful Updation");

        return response;
    }


    @RequestMapping(value = "movementoftransaction", method = RequestMethod.POST)
    @ResponseBody
    public String movementoftransaction(Model md, HttpServletRequest rq,
                                        @RequestParam("trndate") String trndate) throws java.text.ParseException {
        Date TRANDATE = (Date) rq.getSession().getAttribute("TRANDATE");
        Date tranDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);

            // Parse the string to Date
            tranDate = sdf.parse(trndate);

            // Format it back to string for display
            String formattedDate = sdf.format(tranDate);
            System.out.println("Movement of Transaction: " + formattedDate); // 30-09-2025

        } catch (ParseException e) {
            e.printStackTrace();
            return "error: invalid date format";
        }

        List<TRAN_MAIN_TRM_WRK_ENTITY> listofrecord = tRAN_MAIN_TRM_WRK_REP.getswiftvalues(tranDate);
        if (listofrecord != null && !listofrecord.isEmpty()) {
            for (TRAN_MAIN_TRM_WRK_ENTITY records : listofrecord) {
                BGLS_Journal_History bGLS_Journal_History = new BGLS_Journal_History();

                bGLS_Journal_History.setSrl_no(records.getSrl_no());
                bGLS_Journal_History.setBranch_id(records.getBranch_id());
                bGLS_Journal_History.setAcid(records.getAcid());
                bGLS_Journal_History.setCust_id(records.getCust_id());
                bGLS_Journal_History.setTran_id(records.getTran_id());
                bGLS_Journal_History.setPart_tran_id(records.getPart_tran_id());
                bGLS_Journal_History.setAcct_num(records.getAcct_num());
                bGLS_Journal_History.setAcct_name(records.getAcct_name());
                bGLS_Journal_History.setTran_type(records.getTran_type());
                bGLS_Journal_History.setPart_tran_type(records.getPart_tran_type());
                bGLS_Journal_History.setAcct_crncy(records.getAcct_crncy());
                bGLS_Journal_History.setTran_amt(records.getTran_amt());
                bGLS_Journal_History.setTran_particular(records.getTran_particular());
                bGLS_Journal_History.setTran_remarks(records.getTran_remarks());
                bGLS_Journal_History.setTran_date(records.getTran_date());
                bGLS_Journal_History.setValue_date(records.getValue_date());
                bGLS_Journal_History.setTran_ref_no(records.getTran_ref_no());
                bGLS_Journal_History.setAdd_details(records.getAdd_details());
                bGLS_Journal_History.setPartition_type(records.getPartition_type());
                bGLS_Journal_History.setPartition_det(records.getPartition_det());
                bGLS_Journal_History.setInstr_num(records.getInstr_num());
                bGLS_Journal_History.setRef_crncy(records.getRef_crncy());
                bGLS_Journal_History.setRef_crncy_amt(records.getRef_crncy_amt());
                bGLS_Journal_History.setRate_code(records.getRate_code());
                bGLS_Journal_History.setRate(records.getRate());
                bGLS_Journal_History.setEntry_user(records.getEntry_user());
                bGLS_Journal_History.setPost_user(records.getPost_user());
                bGLS_Journal_History.setEntry_time(records.getEntry_time());
                bGLS_Journal_History.setPost_time(records.getPost_time());
                bGLS_Journal_History.setTran_status(records.getTran_status());
                bGLS_Journal_History.setDel_flg(records.getDel_flg());
                bGLS_Journal_History.setInstr_date(records.getInstr_date());
                bGLS_Journal_History.setTran_code(records.getTran_code());
                bGLS_Journal_History.setTran_rpt_code(records.getTran_rpt_code());
                bGLS_Journal_History.setModify_user(records.getModify_user());
                bGLS_Journal_History.setModify_time(records.getModify_time());
                bGLS_Journal_History.setFlow_code(records.getFlow_code());
                bGLS_Journal_History.setFlow_date(records.getFlow_date());

                bGLS_Journal_History_Rep.save(bGLS_Journal_History);

            }
        }
        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);

        if (existingRecord != null) {

            // 2️⃣ Log past value
            existingRecord.setMov_journal("Completed");
            existingRecord.setDcp_status("PRE-DCP-6");
            // 4️⃣ Save updated record
            bGLS_CONTROL_TABLE_REP.save(existingRecord);

        } else {
            System.out.println("No record found in BGLS_Control_Table");

        }

        return "Successfully Updated";
    }

    @RequestMapping(value = "journalbook", method = { RequestMethod.GET, RequestMethod.POST })
    public String journalbook(@RequestParam(required = false) String formmode, Model md, HttpServletRequest rq) {
        Date TRANDATE = (Date) rq.getSession().getAttribute("TRANDATE");

        if (formmode == null || formmode.equals("list1")) {
            md.addAttribute("formmode", "list1");
            md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournal1(TRANDATE));
            // md.addAttribute("jour", tRAN_MAIN_TRM_WRK_REP.findByjournal());

        } else if (formmode.equals("view")) {
        }
        return "JournalBook.html";
    }

    @RequestMapping(value = "journalbook2", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<TRAN_MAIN_TRM_WRK_ENTITY> journalbook2(@RequestParam(required = false) String formmode,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectedDate, Model md,
                                                       HttpServletRequest rq) {

        List<TRAN_MAIN_TRM_WRK_ENTITY> msg = tRAN_MAIN_TRM_WRK_REP.getbalance(selectedDate);

        return msg;
    }

    @RequestMapping(value = "calendermaintenance", method = { RequestMethod.GET, RequestMethod.POST })
    public String calendermaintenance(@RequestParam(required = false) String formmode, Model md,
                                      HttpServletRequest req) {

        if (formmode == null || formmode.equals("add")) {
            md.addAttribute("formmode", "add");
            List<Organization_Entity> organization = organization_Repo.getAllList();
            md.addAttribute("organization", organization.get(0));

            md.addAttribute("OrgBranch", organization_Branch_Rep.getbranchlist());

        } else if (formmode.equals("ModifyHoliday")) {
            md.addAttribute("formmode", "ModifyHoliday");

        } else if (formmode.equals("addholiday")) {
            md.addAttribute("formmode", "addholiday");

        } else if (formmode.equals("listholiday")) {
            md.addAttribute("formmode", "listholiday");

        } else if (formmode.equals("UploadHoliday")) {
            md.addAttribute("formmode", "UploadHoliday");

        }

        return "Calender_Maintenance";
    }

    @PostMapping(value = "calenderuploadexcel")
    @ResponseBody
    public String calenderuploadexcel(@RequestParam("file") MultipartFile file, String screenId,
                                      @ModelAttribute HolidayMaster_Entity HolidayMaster_Entity, Model md, HttpServletRequest rq)
            throws FileNotFoundException, SQLException, IOException, NullPointerException {

        System.out.println("the testing   GST EXCEL UPLOAD");

        System.out.println("fileSize" + file.getSize());

        if (file.getSize() < 50000000) {
            String userid = (String) rq.getSession().getAttribute("USERID");
            String msg = CustomerRequestService.Uploadholidaymaster(screenId, file, userid, HolidayMaster_Entity);
            return msg;
        } else {
            return "File has not been successfully uploaded. Requires less than 128 KB size.";
        }

    }

    @RequestMapping(value = "interest_summary", method = { RequestMethod.GET, RequestMethod.POST })
    public String interest_summary(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("fewvalues", lease_Loan_Master_Repo.getfewvalue());
        }

        List<Lease_Loan_Master_Entity> listvalues = lease_Loan_Master_Repo.getfewvalue();
        List<Object> allAccBalances = new ArrayList<>(); // Initialize list to store all account balances

        if (listvalues != null && !listvalues.isEmpty()) {
            for (Lease_Loan_Master_Entity records : listvalues) {
                String acc_num = records.getLoan_accountno();
                Object accBalance = chart_Acc_Rep.getaccbal(acc_num);
                allAccBalances.add(accBalance); // Add each balance to the list

            }
        }

        md.addAttribute("allAccBalances", allAccBalances);

        return "Interest_Summary";
    }

    /* Aishu */
    @RequestMapping(value = "aspiraLoanMaintanace", method = { RequestMethod.GET, RequestMethod.POST })
    public String aspiraLoanMaintanace(@RequestParam(required = false) String formmode, Model md,
                                       HttpServletRequest req, @RequestParam(required = false) String id,
                                       @RequestParam(required = false) String holder_key) {

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("list", LOAN_ACT_MST_REPO.getLoanActDet1());
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            System.out.println(holder_key);
            md.addAttribute("view", LOAN_ACT_MST_REPO.getLoanView(id));
            md.addAttribute("loan", LOAN_ACT_MST_REPO.getLoanValue(holder_key));
        }
        return "Aspira_Loan_Maintanace";
    }

    @Autowired
    private CLIENT_MASTER_REPO clientMasterRepo;

    @RequestMapping(value = "customerMaster", method = { RequestMethod.GET, RequestMethod.POST })
    public String customerMaster(@RequestParam(required = false) String formmode,
                                 @RequestParam(required = false) String module, @RequestParam(required = false) String id, Model md,
                                 String branch_key, HttpServletRequest req) {

        String user = (String) req.getSession().getAttribute("USERID");
        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(TRANDATE);

        if (formmode == null) {
            md.addAttribute("formmode", "home");
        } else if (formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            Long limit = 100L;
            md.addAttribute("list", clientMasterRepo.getLoanActDet(limit));
        } else if (formmode.equals("view")) {
            Integer unverifiedStatus = clientMasterRepo.getUnverifiedStatus(id);
            Boolean isUnverified = unverifiedStatus != null && unverifiedStatus == 1;
            md.addAttribute("Boolean", isUnverified);
            md.addAttribute("formmode", "view");
            CLIENT_MASTER_ENTITY client = clientMasterRepo.getClientView(id);
            md.addAttribute("customer", client);
            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));
        } else if (formmode.equals("modify") && "module2".equals(module)) {
            md.addAttribute("formmode", "modify");
            CLIENT_MASTER_ENTITY client = clientMasterRepo.getClientView(id);
            md.addAttribute("customer", client);
            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));
        } else if (formmode.equals("verify") && "module2".equals(module)) {
            // Allow verify only for module2
            md.addAttribute("formmode", "verify");
            CLIENT_MASTER_ENTITY client = clientMasterRepo.getClientView(id);
            md.addAttribute("customer", client);
            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));
        }
        md.addAttribute("currentDate", TRANDATE);
        md.addAttribute("ConDate", formattedDate);
        md.addAttribute("user", user);
        md.addAttribute("module", module);

        return "customerMaster";

    }


//	@RequestMapping(value = "loanMaster", method = { RequestMethod.GET, RequestMethod.POST })
//	public String loanMaster(@RequestParam(required = false) String formmode,
//			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "200") int limit, Model model,
//			Model md, HttpServletRequest request, @RequestParam(required = false) String id,
//			@RequestParam(required = false) String holder_key) {
//
//		String user = (String) request.getSession().getAttribute("USERID");
//
//		if (formmode == null || formmode.equals("loanscrn")) {
//			model.addAttribute("formmode", "loanscrn");
//
//		} else if (formmode.equals("viewloan")) {
//			model.addAttribute("formmode", "viewloan");
//			md.addAttribute("user", user);
//			md.addAttribute("view", LOAN_ACT_MST_REPO.getLoanView(id));
//			md.addAttribute("loan", LOAN_ACT_MST_REPO.getLoanValue(holder_key));
//		} else if (formmode.equals("list")) {
//			model.addAttribute("formmode", "list");
//			md.addAttribute("user", user);
//		}
//		System.out.println("returned");
//		return "Loan_Master.html";
//	}

    /* Aishu */
    @RequestMapping(value = "Loan_Maintenance", method = { RequestMethod.GET, RequestMethod.POST })
    public String Loan_Maintanance(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req,
                                   @RequestParam(required = false) String id, @RequestParam(required = false) String holder_key,
                                   @RequestParam(required = false) String branch_key, @RequestParam(defaultValue = "1") int page // page number
                                   // for
                                   // pagination
    ) {

        int limit = 200; // 200 rows per page
        int offset = (page - 1) * limit;

        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");

            // Fetch paginated loan list
            List<Object[]> loanList = LOAN_ACT_MST_REPO.getLoanActWithMobile(offset, limit);
            md.addAttribute("list", loanList);

            // Calculate total pages for pagination
            int totalItems = LOAN_ACT_MST_REPO.countAllLoans();
            int totalPages = (int) Math.ceil((double) totalItems / limit);
            md.addAttribute("currentPage", page);
            md.addAttribute("totalPages", totalPages);

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");

            // Fetch loan details for view
            md.addAttribute("view", LOAN_ACT_MST_REPO.getLoanView(id));
            md.addAttribute("cutomer_data", LOAN_ACT_MST_REPO.getLoanViewCustomerData(id));
            md.addAttribute("acct_bal", chart_Acc_Rep.getacctbal(id));
            md.addAttribute("loan", LOAN_ACT_MST_REPO.getLoanValue(holder_key));

            // Check unverified status
            Integer unverifiedStatus = LOAN_ACT_MST_REPO.getUnverifiedStatus(id);
            Boolean isUnverified = unverifiedStatus != null && unverifiedStatus == 1;
            md.addAttribute("Boolean", isUnverified);
            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));
        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("view", LOAN_ACT_MST_REPO.getLoanView(id));
            md.addAttribute("acct_bal", chart_Acc_Rep.getacctbal(id));
            md.addAttribute("loan", LOAN_ACT_MST_REPO.getLoanValue(holder_key));

            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));

        } else if (formmode.equals("verify")) {
            md.addAttribute("formmode", "verify");
            md.addAttribute("cutomer_data", LOAN_ACT_MST_REPO.getLoanViewCustomerData(id));
            md.addAttribute("view", LOAN_ACT_MST_REPO.getLoanView(id));
            md.addAttribute("acct_bal", chart_Acc_Rep.getacctbal(id));
            md.addAttribute("loan", LOAN_ACT_MST_REPO.getLoanValue(holder_key));
            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));
        }

        return "Loan_Maintenance";
    }

    @RequestMapping(value = "loanSchedule", method = { RequestMethod.GET, RequestMethod.POST })
    public String loanSchedule(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String customer_id, @RequestParam(required = false) String id,
                               @RequestParam(required = false) String holder_key, @RequestParam(required = false) String encodedKey,
                               Model model, HttpServletRequest request) {

        String user = (String) request.getSession().getAttribute("USERID");

        // Fetch the latest TRAN_DATE from the database
        Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
        System.out.println("The fetched TRAN_DATE is: " + tranDateObj);

        // Convert TRAN_DATE to LocalDate for easy comparison
        LocalDate tranDate = (tranDateObj instanceof java.sql.Date) ? ((java.sql.Date) tranDateObj).toLocalDate()
                : tranDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date Transaction_date = Date.from(tranDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (formmode == null || "viewloanschedule".equals(formmode)) {
            model.addAttribute("formmode", "viewloanschedule");
            LOAN_ACT_MST_REPO.getCustomer(Transaction_date, holder_key, id, encodedKey);
        } else if ("viewloanschedule1".equals(formmode)) {
            model.addAttribute("formmode", "viewloanschedule1");
            System.out.println(encodedKey);
            Object[] loanDetails = LOAN_ACT_MST_REPO.getCustomer(Transaction_date, holder_key, id, encodedKey);
            model.addAttribute("view", loanDetails);
            model.addAttribute("dues", LOAN_ACT_MST_REPO.getDues(encodedKey));

        } else if ("listschedule".equals(formmode)) {
            model.addAttribute("formmode", "listschedule");
            model.addAttribute("list", LOAN_ACT_MST_REPO.getLoanActScd());

            System.out.println("Listing Loan Schedules");
        }

        return "Loan_Schedule";
    }

    /* Aishu */
    @PostMapping(value = "Modifyloanmain")
    @ResponseBody
    public String Modifyloanmain(@RequestParam(required = false) String id, Model md, HttpServletRequest rq,
                                 @ModelAttribute LOAN_ACT_MST_ENTITY loan_act_mst_entity) {

        System.out.println(id);

        LOAN_ACT_MST_ENTITY up = loan_act_mst_repo.getLoanView(loan_act_mst_entity.getId());
        System.out.println(loan_act_mst_entity.getLoan_name());

        if (up != null) {
            // Update only the fields that need modification
            up.setAssigned_branchkey(loan_act_mst_entity.getAssigned_branchkey());
            up.setCreation_date(loan_act_mst_entity.getCreation_date());
            up.setAccount_substate(loan_act_mst_entity.getAccount_substate());
            up.setLoan_amount(loan_act_mst_entity.getLoan_amount());
            up.setPayment_method(loan_act_mst_entity.getPayment_method());
            up.setPenalty_rate(loan_act_mst_entity.getPenalty_rate());
            up.setInterest_rate(loan_act_mst_entity.getInterest_rate());
            up.setExpected_disbursementdate(loan_act_mst_entity.getExpected_disbursementdate());
            up.setFirst_repaymentdate(loan_act_mst_entity.getFirst_repaymentdate());
            up.setRepayment_installments(loan_act_mst_entity.getRepayment_installments());
            up.setPrincipal_due(loan_act_mst_entity.getPrincipal_due());
            up.setPrincipal_paid(loan_act_mst_entity.getPrincipal_paid());
            up.setPrincipal_balance(loan_act_mst_entity.getPrincipal_balance());
            up.setInterest_due(loan_act_mst_entity.getInterest_due());
            up.setInterest_paid(loan_act_mst_entity.getInterest_paid());
            up.setInterest_balance(loan_act_mst_entity.getInterest_balance());
            up.setFees_due(loan_act_mst_entity.getFees_due());
            up.setFees_paid(loan_act_mst_entity.getFees_paid());
            up.setFees_balance(loan_act_mst_entity.getFees_balance());
            up.setPenalty_paid(loan_act_mst_entity.getPenalty_paid());
            up.setPenalty_due(loan_act_mst_entity.getPenalty_due());
            up.setPenalty_balance(loan_act_mst_entity.getPenalty_balance());
            up.setDisposable_income(loan_act_mst_entity.getDisposable_income());
            up.setManualoverride_amount(loan_act_mst_entity.getManualoverride_amount());
            up.setManualoverride_expiry_date(loan_act_mst_entity.getManualoverride_expiry_date());
            up.setCpfees(loan_act_mst_entity.getCpfees());
            up.setDeposit_amount(loan_act_mst_entity.getDeposit_amount());
            up.setTotal_product_price(loan_act_mst_entity.getTotal_product_price());
            up.setRetailer_name(loan_act_mst_entity.getRetailer_name());
            up.setRetailer_branch(loan_act_mst_entity.getRetailer_branch());
            up.setVg_application_id(loan_act_mst_entity.getVg_application_id());
            up.setContract_signed(loan_act_mst_entity.getContract_signed());
            up.setDate_of_first_call(loan_act_mst_entity.getDate_of_first_call());
            up.setLast_call_outcome(loan_act_mst_entity.getLast_call_outcome());
            up.setAsondate(loan_act_mst_entity.getAsondate());
            up.setAccrued_interest(loan_act_mst_entity.getAccrued_interest());
            up.setAccrued_penalty(loan_act_mst_entity.getAccrued_penalty());

            // Update last modified date
            up.setLast_modified_date(new Date());

            // Save updated entity
            loan_act_mst_repo.save(up);

            return "Modified Successfully";
        } else {
            return "Loan record not found";
        }
    }

    @PostMapping(value = "Verifyloanmain")
    @ResponseBody
    public String Verifyloanmain(@RequestParam(required = false) String id, Model md, HttpServletRequest rq) {
        System.out.println(id);
        LOAN_ACT_MST_ENTITY verify = loan_act_mst_repo.getLoanView(id);
        verify.setApproved_date(new Date());

        loan_act_mst_repo.save(verify);

        return "Veified Successfully";
    }

    @RequestMapping(value = "loanOperation", method = { RequestMethod.GET, RequestMethod.POST })
    public String loanOperation(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {

        Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); // e.g., 21-SEP-2025
        String formattedDate = sdf.format(tranDateObj);

        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("TRANDATE", TRANDATE);
            md.addAttribute("booking", LOAN_ACT_MST_REPO.getActNo());
            md.addAttribute("collection", LOAN_ACT_MST_REPO.getActNo1());
            md.addAttribute("interest", LOAN_ACT_MST_REPO.getActNo21(formattedDate));
            md.addAttribute("fees", LOAN_ACT_MST_REPO.getActNo31(formattedDate));
            md.addAttribute("penalty", LOAN_ACT_MST_REPO.getPenaltyAccounts(formattedDate));
            md.addAttribute("bookingaccounts", LOAN_ACT_MST_REPO.getActNo41(formattedDate));

        } else if (formmode.equals("list1")) {
            md.addAttribute("formmode", "list1");
            md.addAttribute("getlist", MULTIPLE_TRANSACTION_REPO.getdata());

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");

        } else if (formmode.equals("view2")) {
            md.addAttribute("formmode", "view2");

            List<MULTIPLE_TRANSACTION_ENTITY> tranData = MULTIPLE_TRANSACTION_REPO.getDataValue();
            if (tranData == null) {
                tranData = new ArrayList<>();
            }
            md.addAttribute("tranData", tranData);

            int refNo = Integer.parseInt(tRAN_MAIN_TRM_WRK_REP.gettrmRefUUID1()); // convert String → int
            String tranId = "TR" + String.format("%05d", refNo);

            Date trandates = (Date) req.getSession().getAttribute("TRANDATE");
            md.addAttribute("TRANDATE", trandates);

            md.addAttribute("tranId", tranId);
        }

        return "LoanOperation";
    }

    @RequestMapping(value = "loanClosure", method = { RequestMethod.GET, RequestMethod.POST })
    public String loanClosure(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        Date tranDateObj = bGLS_CONTROL_TABLE_REP.getLatestTranDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); // e.g., 21-SEP-2025
        String formattedDate = sdf.format(tranDateObj);

        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("TRANDATE", TRANDATE);
            md.addAttribute("booking", LOAN_ACT_MST_REPO.getActNo());
            md.addAttribute("booking1", depositRep.getexistingData());
            md.addAttribute("chartaccount", chart_Acc_Rep.getListoffice());
            md.addAttribute("preclosure", LOAN_ACT_MST_REPO.getAccountDetails(formattedDate));
            md.addAttribute("closure", LOAN_ACT_MST_REPO.getAccountDetailsBeforeDueDate(formattedDate));
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");

        }
        return "LoanClosure";
    }

    @RequestMapping(value = "TransactionMigration", method = { RequestMethod.GET, RequestMethod.POST })
    public String TransactionMigration(@RequestParam(required = false) String formmode,
                                       @RequestParam(required = false) String branch_name, Model md, HttpServletRequest req,
                                       @RequestParam(required = false) BigDecimal record_srl) {

        String userid = (String) req.getSession().getAttribute("USERID");
        md.addAttribute("menu", "BAJHeaderMenu");

        if (formmode == null || formmode.equals("add")) {
            md.addAttribute("formmode", "add");
            List<Organization_Entity> organization = organization_Repo.getAllList();
            // md.addAttribute("organization", organization.get(0));
            md.addAttribute("OrgBranch", organization_Branch_Rep.getbranchlist());
            md.addAttribute("disbursement", TRAN_MAIN_TRM_WRK_REP.getRepaymentDetailsvalue());
            md.addAttribute("interest", TRAN_MAIN_TRM_WRK_REP.getInterestDetailsValue());
            md.addAttribute("fees", TRAN_MAIN_TRM_WRK_REP.getFeesDetailsValue());
            md.addAttribute("penalty", TRAN_MAIN_TRM_WRK_REP.getPenaltyDetailsValue());
            md.addAttribute("recovery", TRAN_MAIN_TRM_WRK_REP.getRecoveryDetailsValue());
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch(branch_name));

        } else if (formmode.equals("ModifyHead")) {
            md.addAttribute("formmode", "ModifyHead");
            List<Organization_Entity> organization = organization_Repo.getAllList();
            md.addAttribute("organization", organization.get(0));
        } else if (formmode.equals("DeleteBranch")) {
            md.addAttribute("formmode", "DeleteBranch");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch(branch_name));
        } else if (formmode.equals("AddBranch")) {
            md.addAttribute("formmode", "AddBranch");
        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch(branch_name));

        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("OrgBranch", organization_Branch_Rep.getOrgBranch(branch_name));

        } else if (formmode.equals("addholiday")) {
            md.addAttribute("formmode", "addholiday");

        } else if (formmode.equals("UploadHoliday")) {
            md.addAttribute("formmode", "UploadHoliday");

        } else if (formmode.equals("ModifyHoliday")) {
            md.addAttribute("formmode", "ModifyHoliday");

        } else if (formmode.equals("listholiday")) {
            md.addAttribute("formmode", "listholiday");
            md.addAttribute("Listofvalues", holidayMaster_Rep.getlistofHoliday());

        } else if (formmode.equals("viewrecord")) {

            md.addAttribute("formmode", "viewrecord");
            md.addAttribute("singlerecord", holidayMaster_Rep.getsinglevalue(record_srl));
        }
        return "TransactionMigrat";
    }

    @RequestMapping(value = "balancesheet", method = { RequestMethod.GET, RequestMethod.POST })
    public String balancesheet(@RequestParam(required = false) String formmode,
                               @RequestParam(required = false) String branch_name, Model model, HttpServletRequest req,
                               @RequestParam(required = false) BigDecimal record_srl) {

        // String userid = (String) req.getSession().getAttribute("USERID");

        if (formmode == null || formmode.equals("list")) {

            model.addAttribute("formmode", "list");
            model.addAttribute("refList", reference_code_Rep.getRefList());
        } else if (formmode.equals("nav")) {

            model.addAttribute("formmode", "nav");
            model.addAttribute("refType", reference_code_Rep.getReferenceType());
        }
        return "ReferenceCodeMaintenance";
    }

    @RequestMapping(value = "accountledgerdownload", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource accountLedgerDownload(HttpServletResponse response,
                                                    @RequestParam(required = false) String acct_num, @RequestParam(required = false) String fromdate,
                                                    @RequestParam(required = false) String todate) throws IOException, SQLException, JRException {

        String filetype = "Excel";
        File repfile = loginServices.getFileAccountLedger(filetype, acct_num);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());

        return new FileSystemResource(repfile);
    }

    @RequestMapping(value = "customer", method = { RequestMethod.GET, RequestMethod.POST })
    public String customer(@RequestParam(required = false) String id, Model md) {

        md.addAttribute("refdetails", reference_code_Rep.getRefById(id));
        md.addAttribute("refType", reference_code_Rep.getReferenceType());

        return "ReferenceCodeID";
    }

    @RequestMapping(value = "customer/refedit", method = { RequestMethod.GET, RequestMethod.POST })
    public String customerEdit(@RequestParam(required = false) String id, Model md) {

        md.addAttribute("refdetails", reference_code_Rep.getRefById(id));
        md.addAttribute("refType", reference_code_Rep.getReferenceType());

        return "ReferenceCodeIDEdit.html";
    }

    @RequestMapping(value = "customer/refdelete", method = { RequestMethod.GET, RequestMethod.POST })
    public String customerDelete(@RequestParam(required = false) String id, Model md) {

        md.addAttribute("refdetails", reference_code_Rep.getRefById(id));
        md.addAttribute("refType", reference_code_Rep.getReferenceType());

        return "ReferenceCodeIDDelete.html";
    }

    @GetMapping("/checkControlStatus")
    @ResponseBody
    public Map<String, Boolean> checkControlStatus() {
        Map<String, Boolean> response = new HashMap<>();

        // Fetch the only row (assuming only one row exists)
        List<BGLS_Control_Table> list = bGLS_CONTROL_TABLE_REP.findAll();
        if (list.isEmpty()) {
            response.put("allCompleted", false);
            return response;
        }

        BGLS_Control_Table control = list.get(0);


        boolean allCompleted = "COMPLETED".equalsIgnoreCase(control.getJournal_cons())
                && "COMPLETED".equalsIgnoreCase(control.getLedger_cons())
                && "COMPLETED".equalsIgnoreCase(control.getAcct_cons())
                && "COMPLETED".equalsIgnoreCase(control.getHol_check())
                && "COMPLETED".equalsIgnoreCase(control.getMov_dac())
                && "COMPLETED".equalsIgnoreCase(control.getMov_journal());

        response.put("allCompleted", allCompleted);
        return response;
    }



    @RequestMapping(value = "journalvalid", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Object> journalvalid(@RequestBody Map<String, String> request) {

        String trndate = request.get("trndate");
        System.out.println("Received trndate: " + trndate);
        System.out.println("Entered in Validation");

        // Update control table
        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);

        if (existingRecord != null) {
            System.out.println("Past Journal Cons: " + existingRecord.getJournal_cons());
            existingRecord.setJournal_cons("Completed");
            existingRecord.setDcp_status("PRE-DCP-1");
            bGLS_CONTROL_TABLE_REP.save(existingRecord);
            System.out.println("Updated Journal Cons: " + existingRecord.getJournal_cons());
        } else {
            System.out.println("No record found in BGLS_Control_Table");
        }

        // Fetch data from your repositories
        Object[] Valuesfornotransaction = tRAN_MAIN_TRM_WRK_REP.getTransactionValues();
        Object[] Valuesfordebit = tRAN_MAIN_TRM_WRK_REP.getwofordebitvalues();
        Object[] ValuesforCredit = tRAN_MAIN_TRM_WRK_REP.getwoforcreditvalues();
        Object ValuesforDel = tRAN_MAIN_TRM_WRK_REP.getdelvalues();
        Object Valuesforunpost = tRAN_MAIN_TRM_WRK_REP.getunpostedvalues();

        // Build response map
        Map<String, Object> response = new HashMap<>();
        response.put("Valuesfornotransaction", Valuesfornotransaction);
        response.put("Valuesfordebit", Valuesfordebit);
        response.put("ValuesforCredit", ValuesforCredit);
        response.put("ValuesforDel", ValuesforDel);
        response.put("Valuesforunpost", Valuesforunpost);

        System.out.println(ValuesforDel);
        System.out.println(Valuesforunpost);
        System.out.println(response);

        return response;
    }


    @PostMapping("consistencyCheck")
    @ResponseBody
    public Map<String, Object> consistencyCheck(@RequestParam("trndate") String trndate) {
        System.out.println("Received trndate from frontend: " + trndate);

        Object[] obj1 = tRAN_MAIN_TRM_WRK_REP.getcheck1();
        Object[] obj2 = chart_Acc_Rep.getcheck2();
        Object[] obj3 = dAB_Repo.getcheck3();

        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);
        if (existingRecord != null) {
            existingRecord.setAcct_cons("Completed");
            existingRecord.setDcp_status("PRE-DCP-4");
            bGLS_CONTROL_TABLE_REP.save(existingRecord);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("custcheck1", obj1);
        response.put("custcheck2", obj2);
        response.put("custcheck3", obj3);

        return response;
    }


    @PostMapping("BatchJobconsistencyCheck")
    @ResponseBody
    public Map<String, Object> BatchJobconsistencyCheck(@RequestParam("trndate") String trndate) {
        System.out.println("Received trndate from frontend: " + trndate);

        Object[] obj1 = tRAN_MAIN_TRM_WRK_REP.getcheck1();
        Object[] obj2 = chart_Acc_Rep.getcheck2();
        Object[] obj3 = dAB_Repo.getcheck3();

        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);
        if (existingRecord != null) {
            existingRecord.setAcct_cons("Completed");
            existingRecord.setDcp_status("PRE-DCP-4");
            bGLS_CONTROL_TABLE_REP.save(existingRecord);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("custcheck1", obj1);
        response.put("custcheck2", obj2);
        response.put("custcheck3", obj3);

        return response;
    }



    @RequestMapping(value = "dateChageProcess", method = RequestMethod.POST)
    @ResponseBody
    public String dateChangeProcess(
            Model md,
            HttpServletRequest rq,
            @RequestParam("nxtdate") String nxtdateStr,
            @RequestParam("trndate") String trndateStr) throws java.text.ParseException {

        // Get session transaction date
        Date sessionTranDate = (Date) rq.getSession().getAttribute("TRANDATE");

        try {
            // Strict date format: dd-MM-yyyy
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);

            // Optional: validate format using regex before parsing
            String datePattern = "^\\d{2}-\\d{2}-\\d{4}$";
            if (!nxtdateStr.matches(datePattern) || !trndateStr.matches(datePattern)) {
                return "Invalid date format. Expected format: dd-MM-yyyy";
            }

            // Parse strings to Date
            System.out.println(trndateStr);
            System.out.println(nxtdateStr);
            Date nxtDate = sdf.parse(nxtdateStr);
            Date tranDate = sdf.parse(trndateStr);

            System.out.println("Parsed trndate: " + tranDate);
            System.out.println("Parsed nxtdate: " + nxtDate);
            System.out.println("Session date: " + sessionTranDate);

            // Example: call service here
            return DateChangeService.dateChange(nxtDate, tranDate, rq);

//            return "Success";

        } catch (ParseException e) {
            e.printStackTrace();
            return "Error: Invalid date format or value (e.g., 32-01-2025)";
        }
    }
    



    @PostMapping("/dcpupdate")
    @ResponseBody
    public Map<String, String> dcpupdate(@RequestBody Map<String, String> payload,
                                         HttpServletRequest req) throws ParseException, java.text.ParseException {

        String userid = (String) req.getSession().getAttribute("USERID");
        if (userid == null) userid = "BFI001"; // default user

        System.out.println("✅ Started dcpupdate...");

        String trndate = payload.get("trndate");
        String nxtdate = payload.get("nxtdate");

        Map<String, String> response = new HashMap<>();

        if (trndate == null || nxtdate == null) {
            response.put("status", "error");
            response.put("message", "Transaction date or Next date missing");
            return response;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date trnDateObj = sdf.parse(trndate);
        Date nxtDateObj = sdf.parse(nxtdate);

        // ✅ Get DCP_STATUS from DB
        String dcpStatus = bGLS_CONTROL_TABLE_REP.getDcpstatus();
        System.out.println("🔍 Current DCP_STATUS: " + dcpStatus);

        // ✅ Proceed only if DCP_STATUS = OPEN
        if (!"OPEN".equalsIgnoreCase(dcpStatus)) {
            System.out.println("⚠️ DCP_STATUS is not OPEN. Update skipped.");
            response.put("status", "not_open");
            response.put("message", "DCP_STATUS is not OPEN, cannot update.");
            return response;
        }

        // ✅ Fetch table data
        List<BGLS_Control_Table> list = bGLS_CONTROL_TABLE_REP.findAll();
        if (list == null || list.isEmpty()) {
            System.out.println("⚠️ No rows found in BGLS_CONTROL_TABLE");
            response.put("status", "failure");
            return response;
        }

        BGLS_Control_Table controlTable = list.get(0);
        if (controlTable == null) {
            response.put("status", "failure");
            return response;
        }

        // ✅ Perform update if OPEN
        controlTable.setDcp_user(userid);
        controlTable.setTran_date(trnDateObj);
        controlTable.setNext_work_date(nxtDateObj);
        controlTable.setJournal_cons("PENDING");
        controlTable.setLedger_cons("PENDING");
        controlTable.setAcct_cons("PENDING");
        controlTable.setHol_check("PENDING");
        controlTable.setMov_dac("PENDING");
        controlTable.setMov_journal("PENDING");
        controlTable.setGl_con("PENDING");
        //dues
        controlTable.setInterest_demand_gen("PENDING");
        controlTable.setFee_demand_gen("PENDING");
        controlTable.setPenalty("PENDING");

        controlTable.setDcp_start_time(new Date());
        controlTable.setDcp_flg("N");
        controlTable.setDcp_status("CLOSE");

        bGLS_CONTROL_TABLE_REP.save(controlTable);

        System.out.println("✅ DCP update completed successfully");
        response.put("status", "success");

        return response;
    }




    @RequestMapping(value = "holidayCheck", method = RequestMethod.POST)
    @ResponseBody
    public String holidayCheck(Model md,
                               @RequestParam("trndate") String trndate) throws java.text.ParseException {

        System.out.println("Incoming Holiday Check");
        System.out.println(trndate + " trndate");

        // ✅ Convert trndate string to java.util.Date
        Date TRANDATE = null;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            TRANDATE = inputFormat.parse(trndate);

            System.out.println("Holiday TRANDATE"+TRANDATE);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Date Format";
        }

        // Fetch first record from control table
        BGLS_Control_Table existingRecord1 = bGLS_CONTROL_TABLE_REP.findAll().get(0);
        if (existingRecord1 != null) {
            existingRecord1.setHol_check("Completed");
            existingRecord1.setDcp_status("PRE-DCP-3");
            bGLS_CONTROL_TABLE_REP.save(existingRecord1);
            System.out.println("Updated Holiday Check: " + existingRecord1.getHol_check());
        } else {
            System.out.println("No record found in BGLS_Control_Table");
        }

        // Step 1: Check if holiday exists in HMT
        int holidayCount = bglsHolidayMasterRep.countByRecordDateAndDelFlg(TRANDATE);
        System.out.println("holidayCount: " + holidayCount);

        int cnt = holidayCount;

        // Step 2: If not found, check if weekend
        if (cnt == 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(TRANDATE);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                cnt = 1; // weekend = holiday
            }
            System.out.println("dayOfWeek"+dayOfWeek);
        }

        // Step 3: Return based on holiday/weekend
        if (cnt > 0) {
            System.out.println("Holiday/Weekend detected");
            return "Holiday/Weekend - Updation Done";
        } else {
            return "Working Day";
        }
    }

    @RequestMapping(value = "holidayCheckBatchJob", method = RequestMethod.POST)
    @ResponseBody
    public String holidayCheckBatchJob(Model md,
                                       @RequestParam("trndate") String trndate) throws java.text.ParseException {

        System.out.println("Incoming Holiday Check");
        System.out.println(trndate + " trndate");

        // ✅ Convert trndate string to java.util.Date
        Date TRANDATE = null;
        try {
            // Match the input format "dd-MM-yyyy"
            TRANDATE = new SimpleDateFormat("yyyy-MM-dd").parse(trndate);

            System.out.println("Holiday TRANDATE"+TRANDATE);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Date Format";
        }

        // Step 1: Check if holiday exists in HMT
        int holidayCount = bglsHolidayMasterRep.countByRecordDateAndDelFlg(TRANDATE);
        System.out.println("holidayCount: " + holidayCount);

        int cnt = holidayCount;

        // Step 2: If not found, check if weekend
        if (cnt == 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(TRANDATE);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                cnt = 1; // weekend = holiday
            }
            System.out.println("dayOfWeek"+dayOfWeek);
        }

        // Step 3: Return based on holiday/weekend
        if (cnt > 0) {
            System.out.println("Holiday/Weekend detected");
            return "Holiday/Weekend";
        } else {
            return "Working Day";
        }
    }



    @RequestMapping(value = "Parameteradd", method = { RequestMethod.GET, RequestMethod.POST })
    public String redirectschemeadd1(Model md) {


//		md.addAttribute("refdetails",reference_code_Rep.getRefById(id) );
//		md.addAttribute("refType", reference_code_Rep.getReferenceType());
        md.addAttribute("principle",chart_Acc_Rep.getListoffice1());

        return "BACP/PARAMETERADD";
    }

    @Autowired
    BGLS_ORG_BRANCH_REPO branchrepo;

    @RequestMapping(value = "Parameterview", method = { RequestMethod.GET, RequestMethod.POST })
    public String parameterEdit(@RequestParam(required = false) String id,
                                @RequestParam(required = false, defaultValue = "view") String formmode, // <--- add this
                                Model md) {
        System.out.println(id);
        // Fetch the entity/entities
        BGLS_LMS_SCHEMES_TABLE_ENTITY entity = lmsschemerepo.findById(id).orElse(null);
        md.addAttribute("parameters", entity);
        System.out.println("branch : " + entity.getBranches());
        List<BGLS_ORG_BRANCH_ENTITY> branch = branchrepo.findAll();
        md.addAttribute("branches", branch);
        System.out.println(entity.getProduct());

        // Pass the mode to Thymeleaf
        md.addAttribute("formMode", formmode);

        return "BACP/PARAMETERVIEW.html";
    }

    @RequestMapping(value = "Parameterdelete", method = { RequestMethod.GET, RequestMethod.POST })
    public String parameterDELETE(@RequestParam(required = false) String id, Model md) {
        BGLS_LMS_SCHEMES_TABLE_ENTITY entity = lmsschemerepo.findById(id).orElse(null);
        md.addAttribute("parameters", entity);
        List<BGLS_ORG_BRANCH_ENTITY> branch = branchrepo.findAll();
        md.addAttribute("branches", branch);
        return "BACP/PARAMETERDELETE.html";
    }

    @RequestMapping(value = "Parameterupdate", method = { RequestMethod.GET, RequestMethod.POST })
    public String parameterUpdate(@RequestParam(required = false) String id, Model md) {
        BGLS_LMS_SCHEMES_TABLE_ENTITY entity = lmsschemerepo.findById(id).orElse(null);
        md.addAttribute("parameters", entity);
        List<BGLS_ORG_BRANCH_ENTITY> branch = branchrepo.findAll();
        md.addAttribute("branches", branch);
        md.addAttribute("principle",chart_Acc_Rep.getListoffice1());
        return "BACP/PARAMETERUPDATE.html";
    }

    @RequestMapping(value = "Balancing_report", method = RequestMethod.GET)
    public String balanceReport(@RequestParam(required = false) String formmode,
                                @RequestParam(required = false) String acct_num, String keyword, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("chartaccount", chart_Acc_Rep.getListoffice());
        } else if (formmode.equals("add")) {
            md.addAttribute("formmode", "add");
            md.addAttribute("Chart1", reference_code_Rep.getReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getReferenceCode("COA_08"));
        } else if (formmode.equals("modify")) {
            md.addAttribute("formmode", "modify");
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
        } else if (formmode.equals("verify")) {
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
            md.addAttribute("formmode", "verify");
        } else if (formmode.equals("view")) {
            md.addAttribute("formmode", "view");
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));
            md.addAttribute("Chart1", reference_code_Rep.getReferenceCode("COA_01"));
            md.addAttribute("Chart2", reference_code_Rep.getReferenceCode("COA_02"));
            md.addAttribute("Chart3", reference_code_Rep.getReferenceCode("COA_03"));
            md.addAttribute("Chart4", reference_code_Rep.getReferenceCode("COA_04"));
            md.addAttribute("Chart5", reference_code_Rep.getReferenceCode("COA_05"));
            md.addAttribute("Chart6", reference_code_Rep.getReferenceCode("COA_06"));
            md.addAttribute("Chart7", reference_code_Rep.getReferenceCode("COA_07"));
            md.addAttribute("Chart8", reference_code_Rep.getReferenceCode("COA_08"));

        } else if (formmode.equals("delete")) {
            md.addAttribute("formmode", "delete");
            md.addAttribute("chartaccount", chart_Acc_Rep.getaedit(acct_num));

        }
        return "BalancingReport.html";
    }

    @RequestMapping(value = "loanMaster", method = { RequestMethod.GET, RequestMethod.POST })
    public String loanMaster(@RequestParam(required = false) String formmode,
                             @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "200") int limit, Model model,
                             Model md, HttpServletRequest request, @RequestParam(required = false) String id,
                             @RequestParam(required = false) String holder_key, String branch_key) {

        String user = (String) request.getSession().getAttribute("USERID");

        if (formmode == null || formmode.equals("loanscrn")) {
            model.addAttribute("formmode", "loanscrn");

        } else if (formmode.equals("viewloan")) {
            model.addAttribute("formmode", "viewloan");
            md.addAttribute("user", user);
            md.addAttribute("view", LOAN_ACT_MST_REPO.getLoanView(id));
            md.addAttribute("acct_bal", chart_Acc_Rep.getacctbal(id));
            Map<String, String> value = new HashMap<>();
//	        LOAN_ACT_MST_REPO.getLoanValueList(holder_key);
            md.addAttribute("customer_id", LOAN_ACT_MST_REPO.getLoanValueCUSTOMER_ID(holder_key));
            md.addAttribute("customer_name", LOAN_ACT_MST_REPO.getLoanValueCUSTOMER_NAME(holder_key));
            md.addAttribute("branchName1", BGLS_ORG_BRANCH_REPO.getBranchName(branch_key));

//	        List<CLIENT_MASTER_ENTITY> clients = LOAN_ACT_MST_REPO.getLoanValueList(holder_key);

//	        String customerId ="";
//	        String customerName ="";
//	        if (!clients.isEmpty()) {
//	            customerId = clients.get(0).getCustomer_id();
//	            customerName = clients.get(0).getFirst_name();
//	            System.out.println("Customer ID: " + customerId);
//	        }

//	        for (CLIENT_MASTER_ENTITY client : clients) {
//	            System.out.println("Client ID: " + clients[0].getCustomer_id());
//	            System.out.println("First Name: " + clients.getFirst_name());
//	            System.out.println("Last Name: " + client.getLastName());
//	            System.out.println("Mobile: " + client.getMobilePhone());
//	            System.out.println("---------------------");
//	        }

        } else if (formmode.equals("list")) {
            model.addAttribute("formmode", "list");
            md.addAttribute("user", user);
        }
        System.out.println("returned");
        return "Loan_Master.html";
    }

    @PostMapping("/submitBulkCollection")
    @ResponseBody
    public ResponseEntity<?> submitBulkCollection(HttpServletRequest request,
                                                  @RequestBody List<MULTIPLE_TRANSACTION_ENTITY> transactions) {
        String user = (String) request.getSession().getAttribute("USERID");
        String username = (String) request.getSession().getAttribute("USERNAME");
        try {
            multipleTransactionService.saveBulkCollection(transactions, user, username);
            return ResponseEntity.ok("Bulk collection saved successfully!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving bulk collection: " + e.getMessage());
        }
    }

    @RequestMapping(value = "credit_facility_report", method = { RequestMethod.GET, RequestMethod.POST })
    public String credit_facility_report(@RequestParam(required = false) String formmode,
                                         @RequestParam(required = false) String userid, Model md, HttpServletRequest req) {
        String user = (String) req.getSession().getAttribute("USERID");
        md.addAttribute("RoleMenu", access_Role_Repo.getRole(user));

        if (formmode == null || formmode.equals("list")) {

            md.addAttribute("formmode", "list");
            List<Object[]> results = LOAN_ACT_MST_REPO.getAllDetails();
            md.addAttribute("loanvalues", results);
            // Details List
            md.addAttribute("loanDetails", LOAN_ACT_MST_REPO.getLoanNo()); // <-- no arguments

        }

        return "CREDIT_FACILITY_REPORT.html";
    }

    @RequestMapping(value = "downloadShedulePdf", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadShedulePdf(HttpServletResponse response,
                                                 @RequestParam(required = false) String acctNo) throws IOException, SQLException, JRException {

        String filetype = "pdf";
        File repfile = ExelDownloadService.getSheduleDownload(filetype, acctNo);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());

        return new FileSystemResource(repfile);
    }

    @RequestMapping(value = "Parameter", method = { RequestMethod.GET, RequestMethod.POST })
    public String Parameters(@RequestParam(required = false) String formmode,
                             @RequestParam(required = false) String refnumber,
                             @RequestParam(value = "page", required = false) Optional<Integer> page,
                             @RequestParam(value = "size", required = false) Optional<Integer> size, Model md, HttpServletRequest req,
                             String Sort, String acct_open_form, String acc_temp, String cus_temp) {

        String userId = (String) req.getSession().getAttribute("USERID");
        md.addAttribute("Role", userProfileRep.getRole(userId));
        Date TRANDATE = (Date) req.getSession().getAttribute("TRANDATE");

        if (formmode == null || formmode.equals("list")) {
            System.out.println("Value=======================>>> " + refnumber);
            md.addAttribute("lms_schemes", lmsschemerepo.getSchemeList());
            md.addAttribute("menu", "AMLCustomerKYC");
            md.addAttribute("menuname", "CustomerKYC");
            md.addAttribute("formmode", "list");

        }

        return "BACP/PARAMETER";
    }

    @RequestMapping(value = "end_of_month", method = { RequestMethod.GET, RequestMethod.POST })
    public String end_of_month(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
        }
        return "End_Of_Month";
    }

    @RequestMapping(value = "dab", method = { RequestMethod.GET, RequestMethod.POST })
    public String dab(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
        }
        return "DAB";
    }

    @RequestMapping(value = "downloadDetailsPdf", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadDetailsPdf(HttpServletResponse response,
                                                 @RequestParam(required = false) String acctNo) throws IOException, SQLException, JRException {

        String filetype = "pdf";
        File repfile = ExelDownloadService.getDetailDownload(filetype, acctNo);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + repfile.getName());

        return new FileSystemResource(repfile);
    }



    @RequestMapping(value = "InterestDemandValidate", method = RequestMethod.POST)
    @ResponseBody
    public String InterestDemandValidate(Model md, HttpServletRequest rq,
                                         @RequestParam("trndate") String trndate) {
        Date TRANDATE = (Date) rq.getSession().getAttribute("TRANDATE");

        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);



        return "Successfully Updated";
    }


    @RequestMapping(value = "ConsolidatedLoanReport", method = { RequestMethod.GET, RequestMethod.POST })
    public String ConsolidatedLoanReport(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("date", bGLS_CONTROL_TABLE_REP.getCurrentTranDate());
        }
        return "ConsolidatedLoanReport.html";
    }

    
    @RequestMapping(value = "bacthJobdateChageProcess", method = RequestMethod.POST)
    @ResponseBody
    public String bacthJobdateChageProcess(
            Model md,
            HttpServletRequest rq,
            @RequestParam("nxtdate") String nxtdateStr,
            @RequestParam("trndate") String trndateStr) throws java.text.ParseException {
    	  Date sessionTranDate = (Date) rq.getSession().getAttribute("TRANDATE");
        // Define the expected date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        // Convert String to Date
        Date nxtdate = sdf.parse(nxtdateStr);
        Date trndate = sdf.parse(trndateStr);

        System.out.println("nxtdate"+nxtdate);
        System.out.println("trndate"+trndate);
        
        
        // (Optional) Get session transaction date
      

        // Example: call repository method with Date arguments if needed
        int result=bGLS_CONTROL_TABLE_REP.updateTranDates(nxtdate, trndate);
        
        if (result > 0) {
            return "Date Change Successfully";
        } else {
            return "Date Change Unsuccessful";
        }
    }

    
    
    @RequestMapping(value = "DabUpdate", method = RequestMethod.POST)
    @ResponseBody
    public String DabUpdate(HttpServletRequest rq, @RequestParam("trndate") String trndate) throws java.text.ParseException {
    	  Date sessionTranDate = (Date) rq.getSession().getAttribute("TRANDATE");
       
    	  System.out.println("trndate"+trndate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date trndate1 = sdf.parse(trndate);
        System.out.println("trndate1"+trndate1);
        int updated=dAB_Repo.UpdateDab(trndate1);
        
        
        BGLS_Control_Table existingRecord = bGLS_CONTROL_TABLE_REP.findAll().get(0);
        if (existingRecord != null) {

            // 2️⃣ Log past value
            existingRecord.setMov_dac("Completed");
            existingRecord.setDcp_status("DCP-1");
            // 4️⃣ Save updated record
            bGLS_CONTROL_TABLE_REP.save(existingRecord);

        } else {
            System.out.println("No record found in BGLS_Control_Table");

        }
      
        return "Account balances successfully inserted";
    }

    
    @RequestMapping(value = "TransactionReports", method = { RequestMethod.GET, RequestMethod.POST })
    public String TransactionReports(@RequestParam(required = false) String formmode, Model md, HttpServletRequest req) {
        if (formmode == null || formmode.equals("list")) {
            md.addAttribute("formmode", "list");
            md.addAttribute("date", bGLS_CONTROL_TABLE_REP.getCurrentTranDate());
        }
        return "TransactionReports.html";
    }




}