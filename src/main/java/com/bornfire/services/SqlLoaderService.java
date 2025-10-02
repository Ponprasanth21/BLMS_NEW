package com.bornfire.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bornfire.controller.ASPIRAUploadController;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.LOAN_ACT_MST_REPO;
import com.bornfire.entities.LOAN_REPAYMENT_REPO;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

@Service
public class SqlLoaderService {
	private static final Logger logger = LoggerFactory.getLogger(SqlLoaderService.class);
	
	@Autowired
	LOAN_ACT_MST_REPO lOAN_ACT_MST_REPO;
	
	@Autowired
	CLIENT_MASTER_REPO clientMasterRepo;
	
	@Autowired
    LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;

    public String runSqlLoader(String uploadPage) throws Exception {
    	logger.info("SQL Loader Enter");
        String sqlLdrPath = "sqlldr";
        String userPass = "ASPIRA/aspira@ASPIRA";
        String controlFile = "";
        String logFile = "";
        
        if("LOAN".equalsIgnoreCase(uploadPage)) {
        	lOAN_ACT_MST_REPO.LoanMasterTempTableDelete();
        	controlFile = "C:\\Temp\\loan_master_data.ctl";
            logFile = "C:\\Temp\\loan_master_data.log";
        }else if("CUSTOMER".equalsIgnoreCase(uploadPage)) {
        	clientMasterRepo.CustomerMasterTempTableDelete();
        	controlFile = "C:\\Temp\\customer_master_data.ctl";
            logFile = "C:\\Temp\\customer_master_data.log";
        }else if("REPAYMENT".equalsIgnoreCase(uploadPage)) {
        	lOAN_REPAYMENT_REPO.LoanRepaymetTempTableDelete();
        	controlFile = "C:\\Temp\\loan_repayment_data.ctl";
            logFile = "C:\\Temp\\loan_repayment_data.log";
        }
        
        try {
            // Building the command with the full path and proper quotes
            ProcessBuilder pb = new ProcessBuilder(
                "cmd.exe", "/c", 
                "\"" + sqlLdrPath + "\" " + userPass + " control=" + controlFile + " log=" + logFile + " skip=1"
            );

            logger.info("Command Executed");

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read and print the output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("Output: " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("EXIT CODE : " +exitCode);
            if (exitCode == 0) {
            	
                if("LOAN".equalsIgnoreCase(uploadPage)) {
                	lOAN_ACT_MST_REPO.LoanMasterCopyTempTableToMainTableProcedure();
                }else if("CUSTOMER".equalsIgnoreCase(uploadPage)) {
                	clientMasterRepo.CustomerMasterCopyTempTableToMainTable();
                }else if("REPAYMENT".equalsIgnoreCase(uploadPage)) {
                	lOAN_REPAYMENT_REPO.LoanRepaymnetCopyTempTableToMainTable();
                }
                
                logger.info("SQL*Loader executed successfully!");
                
                return "SQL*Loader executed successfully!";
                
                
            } else {
            	logger.info("SQL*Loader failed with exit code: " + exitCode);
            	return  "SQL*Loader failed with exit code: " + exitCode;
            }

        } catch (IOException | InterruptedException e) {
        	logger.info("An exception occurred: " + e.getMessage());
        	return "An exception occurred: " + e.getMessage();
        }
    }
    
//    @Scheduled(fixedRate = 3600000)
//    public void cleanUpOldFiles() {
//    	File folder = new File("C:/Temp/Files");
//    	if (!folder.exists()) {
//    	    boolean created = folder.mkdirs();
//    	    System.out.println("Created folder? " + created);
//    	}
//
//    	File[] files = folder.listFiles();
//
//    	if (files != null) {
//    	    for (File file : files) {
//    	        if (file.isFile() && file.lastModified() < System.currentTimeMillis() - 3600000) {
//    	            file.delete();
//    	        }
//    	    }
//    	} else {
//    	    System.out.println("Directory is not accessible: " + folder.getAbsolutePath());
//    	}
//    }
}