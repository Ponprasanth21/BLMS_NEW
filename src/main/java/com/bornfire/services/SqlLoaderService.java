package com.bornfire.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.LOAN_ACT_MST_REPO;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

@Service
public class SqlLoaderService {
	
	@Autowired
	LOAN_ACT_MST_REPO lOAN_ACT_MST_REPO;
	
	@Autowired
	CLIENT_MASTER_REPO clientMasterRepo;

    public void runSqlLoader(String uploadPage) throws Exception {
    	  // IMPORTANT: Replace this with the actual, full path to your sqlldr.exe
        String sqlLdrPath = "sqlldr";
        String userPass = "ASPIRA/aspira@ASPIRA";
        String controlFile = "";
        String logFile = "";
        
        if(uploadPage == "LOAN") {
        	controlFile = "C:\\Temp\\loan_master_data.ctl";
            logFile = "C:\\Temp\\loan_master_data.log";
        }else if(uploadPage == "CUSTOMER") {
        	controlFile = "C:\\Temp\\customer_master_data.ctl";
            logFile = "C:\\Temp\\customer_master_data.log";
        }
        
        try {
            // Building the command with the full path and proper quotes
            ProcessBuilder pb = new ProcessBuilder(
                "cmd.exe", "/c", 
                "\"" + sqlLdrPath + "\" " + userPass + " control=" + controlFile + " log=" + logFile
            );

            System.out.println("Executing command: " + String.join(" ", pb.command()));

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read and print the output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Output: " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
            	
                if("LOAN".equalsIgnoreCase(uploadPage)) {
                	lOAN_ACT_MST_REPO.insertFromOldTable();
                }else if("CUSTOMER".equalsIgnoreCase(uploadPage)) {
                	clientMasterRepo.insertFromClientMasterTest();
                }
                
                System.out.println("SQL*Loader executed successfully!");
                
                
            } else {
                System.err.println("SQL*Loader failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Scheduled(fixedRate = 3600000) // every hour
    public void cleanUpOldFiles() {
    	File folder = new File("C:/Temp/Files");
    	if (!folder.exists()) {
    	    boolean created = folder.mkdirs();
    	    System.out.println("Created folder? " + created);
    	}

    	File[] files = folder.listFiles();

    	if (files != null) {
    	    for (File file : files) {
    	        if (file.isFile() && file.lastModified() < System.currentTimeMillis() - 3600000) {
    	            file.delete();
    	        }
    	    }
    	} else {
    	    System.out.println("Directory is not accessible: " + folder.getAbsolutePath());
    	}
    }
}

