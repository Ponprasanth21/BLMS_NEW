package com.bornfire.services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bornfire.entities.BGLS_CONTROL_TABLE_REP;
import com.bornfire.entities.BGLS_Control_Table;
import com.bornfire.entities.DAB_Repo;
import com.bornfire.entities.DayEnd_LogEntity;
import com.bornfire.entities.DayEnd_LogRepo;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_ENTITY;
import com.bornfire.entities.TRAN_MAIN_TRM_WRK_REP;
import com.ibm.icu.text.SimpleDateFormat;
import com.monitorjbl.xlsx.exceptions.ParseException;

@Service
@EnableScheduling
public class Scheduler_Service {
	@Autowired
	TRAN_MAIN_TRM_WRK_REP tRAN_MAIN_TRM_WRK_REP;

	@Autowired
	DayEnd_LogRepo dayEnd_LogRepo;

	@Autowired
	DAB_Repo dAB_Repo;

	@Autowired
	BGLS_CONTROL_TABLE_REP bGLS_CONTROL_TABLE_REP;

//	@Scheduled(cron = "0 1 0 * * ?") 
//	@Scheduled(cron = "0 10 17 * * ?", zone = "Asia/Kolkata")
	public String ExecuteInterestDemand() throws SQLException, ParseException {
		System.out.println("Enter into InterestDemand");
		int values = tRAN_MAIN_TRM_WRK_REP.getResultindem();

		if (values == 0) {
			tRAN_MAIN_TRM_WRK_REP.SystemInterestDemand("SYSTEM");

			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
			logtab.setProcessName("Interest Demand");
			logtab.setStatus("Success");
			logtab.setStepDesc("Interest Demand Process Completed.");
			logtab.setCreatedAt(new Date());
			dayEnd_LogRepo.save(logtab);

		} else {
			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
			logtab.setProcessName("Interest Demand");
			logtab.setStatus("INFO");
			logtab.setStepDesc("Interest Demand Process already Exists.");
			logtab.setCreatedAt(new Date());
			dayEnd_LogRepo.save(logtab);
		}

		return "Success";
	}

//	@Scheduled(cron = "0 2 0 * * ?")
//	@Scheduled(cron = "0 15 17 * * ?", zone = "Asia/Kolkata")
	public String ExecutefeeDemand() throws SQLException, ParseException {
		System.out.println("Enter into InterestDemand");
		int values = tRAN_MAIN_TRM_WRK_REP.getResultfeedem();

		if (values == 0) {
			tRAN_MAIN_TRM_WRK_REP.SystemFeeDemand("SYSTEM");

			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
			logtab.setProcessName("Fee Demand");
			logtab.setStatus("Success");
			logtab.setStepDesc("Fee Demand Process Completed.");
			logtab.setCreatedAt(new Date());
			dayEnd_LogRepo.save(logtab);

		} else {
			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
			logtab.setProcessName("Fee Demand");
			logtab.setStatus("INFO");
			logtab.setStepDesc("Fee Demand Process already Exists.");
			logtab.setCreatedAt(new Date());
			dayEnd_LogRepo.save(logtab);

		}

		return "Success";

	}

//	@Scheduled(cron = "0 3 0 * * ?")
//	@Scheduled(cron = "0 20 17 * * ?", zone = "Asia/Kolkata")
	public void ExecutepenaltyDemand() throws SQLException, ParseException {

		System.out.println("Enter into Penalty Demand");
		int values = tRAN_MAIN_TRM_WRK_REP.getResultpenaltydem();

		System.out.println("Scheduler triggered at: " + new java.util.Date());

		// Uncomment this block when needed
		if (values == 0) {
			tRAN_MAIN_TRM_WRK_REP.SystemPenaltyDemand("SYSTEM");

			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
			logtab.setProcessName("Penalty Demand");
			logtab.setStatus("Success");
			logtab.setStepDesc("Penalty Demand Process Completed.");
			logtab.setCreatedAt(new Date());
			dayEnd_LogRepo.save(logtab);
			System.out.println("Enter into Penalty Success");
		} else {
			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
			logtab.setProcessName("Penalty Demand");
			logtab.setStatus("INFO");
			logtab.setStepDesc("Penalty Demand Process already Exists.");
			logtab.setCreatedAt(new Date());
			dayEnd_LogRepo.save(logtab);
			System.out.println("Enter into Penalty error");
		}
	}

//	 @Scheduled(cron = "0 55 9 * * ?", zone = "UTC") // Runs every day at 3:20 PM
//	public void ExecuteDAB() throws SQLException, ParseException, java.text.ParseException {
//
//		System.out.println("Scheduler triggered at: " + new java.util.Date());
//
//		Date trandate = bGLS_CONTROL_TABLE_REP.getTranDateforScheduler();
//
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		String formattedDate = sdf.format(new Date());
//
//		SimpleDateFormat sd1f = new SimpleDateFormat("dd-MM-yyyy");
//
//		Date trndate1 = sd1f.parse(formattedDate);
//
//		String result = tRAN_MAIN_TRM_WRK_REP.checkDemandAndRecovery(trndate1);
//
//		String output = "";
//
//		if (result.equals("BOTH_MISSING") || result.equals("RECOVERY_MISSING") || result.equals("DEMAND_MISSING")) {
//			// Prepare the output message
//			switch (result) {
//			case "BOTH_MISSING":
//				output = "Demand and Recovery were not applied.";
//				break;
//			case "RECOVERY_MISSING":
//				output = "Recovery was not applied.";
//				break;
//			case "DEMAND_MISSING":
//				output = "Demand was not applied.";
//				break;
//			default:
//				output = "Unknown error.";
//			}
//
//			// Log the failure
//			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
//			logtab.setProcessName("DAB Update");
//			logtab.setStatus("FAILED");
//			logtab.setStepDesc(output);
//			logtab.setCreatedAt(new Date());
//			dayEnd_LogRepo.save(logtab);
//
//		} else {
//			// Proceed with DAB update if everything is okay
//			dAB_Repo.UpdateDab(trandate);
//
//			output = "DAB Demand Process Completed Successfully.";
//			DayEnd_LogEntity logtab = new DayEnd_LogEntity();
//			logtab.setProcessName("DAB Update");
//			logtab.setStatus("SUCCESS");
//			logtab.setStepDesc("DAB Process Completed.");
//			logtab.setCreatedAt(new Date());
//			dayEnd_LogRepo.save(logtab);
//
//		}
//
//	}
//
//	

}
