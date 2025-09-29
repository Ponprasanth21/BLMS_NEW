package com.bornfire.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.config.SequenceGenerator;
import com.bornfire.entities.LOAN_ACT_MST_REPO;
import com.bornfire.services.ExcelToCsvService;
import com.bornfire.services.ExelDownloadService;
import com.bornfire.services.SqlLoaderService;
import com.bornfire.services.UploadService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@ConfigurationProperties("default")
public class ASPIRAUploadController {
	private static final Logger logger = LoggerFactory.getLogger(ASPIRAUploadController.class);
	
	@Autowired
	UploadService UploadService;
	
	@Autowired
	ExelDownloadService excelDownloadService;
	
	@Autowired
	SequenceGenerator sequence;
	
	@Autowired
	private ExcelToCsvService excelToCsvService;
	
	@Autowired
	LOAN_ACT_MST_REPO lOAN_ACT_MST_REPO;
	

	private final SqlLoaderService sqlLoaderService;
	
	
	public ASPIRAUploadController(SqlLoaderService sqlLoaderService) {
        this.sqlLoaderService = sqlLoaderService;
    }
	
	 @PostMapping(value = "/UploadFileData")
	 public ResponseEntity<Map<String, Object>> uploadExcel(@RequestParam("file") MultipartFile file,
	                                                           @RequestParam("fileInput") String fileInput,
	                                                           HttpServletRequest request,
	                                                           Model model, boolean overwrite)
	            throws FileNotFoundException, SQLException, IOException {

	        Map<String, Object> resultMap = new LinkedHashMap<>();

	        String userID = (String) request.getSession().getAttribute("USERID");
	        String userName = (String) request.getSession().getAttribute("USERNAME");
	        String auditRefNo = sequence.generateRequestUUId();
	        if ("CUSTOMER".equalsIgnoreCase(fileInput)) {
        		  String fileName = file.getOriginalFilename().toLowerCase();
	        	  String CSVfileName= "customer_master_file.csv";
	        	    if (fileName.endsWith(".xlsx")) {
	        	    	resultMap = UploadService.saveCustomerFile(file, userID, userName,overwrite);
	        	    } else if (fileName.endsWith(".csv")) {
	        	    	 uploadExcel(file,CSVfileName,"CUSTOMER");
	        	    } 
	        } else if ("LOAN".equalsIgnoreCase(fileInput)) {
	        	  String fileName = file.getOriginalFilename().toLowerCase();
	        	  String CSVfileName= "loan_master_file.csv";
	        	    if (fileName.endsWith(".xlsx")) {
	        	    	System.out.println("df!!!!!");
	        	    	resultMap = UploadService.saveLoanFile(file, userID, userName, overwrite);
	        	    } else if (fileName.endsWith(".csv")) {
	        	    	System.out.println("df");
	        	    	 uploadExcel(file,CSVfileName,"LOAN");
	        	    } 
	        } else if ("REPAYMENT".equalsIgnoreCase(fileInput)) {
	            resultMap = UploadService.saveRepaymentFile(file, userID, userName, overwrite);
	        }else if ("GL_CODE".equalsIgnoreCase(fileInput)) {
	            resultMap = UploadService.saveGLFile(file, userID, userName, overwrite,  auditRefNo);
	        }  else {
	            resultMap.put("message", "Invalid file type specified");
	        }

	        return ResponseEntity.ok(resultMap);
	    }
		@GetMapping("/DisplayExcel")
		public void loanMasterListExcelDownload(HttpServletRequest request, HttpServletResponse response,String type) {
		    String userID = (String) request.getSession().getAttribute("USERID");
		    String userName = (String) request.getSession().getAttribute("USERNAME");
		    String auditRefNo = sequence.generateRequestUUId();
		    excelDownloadService.ExportExcel( type, userID, userName, auditRefNo, response);
		}
		
		@GetMapping("/DisplayExcel1")
		public void loanMasterListExcelDownload1(HttpServletRequest request,
		                                         HttpServletResponse response,
		                                         @RequestParam String type,
		                                         @RequestParam("currentDate")
		                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date currentDate) {

		    System.out.println("Type: " + type + ", Date: " + currentDate);

		    String userID = (String) request.getSession().getAttribute("USERID");
		    String userName = (String) request.getSession().getAttribute("USERNAME");
		    String auditRefNo = sequence.generateRequestUUId();

		    excelDownloadService.ExportExcel1(type, userID, userName, auditRefNo, response, currentDate);
		}
		
		private static final String UPLOAD_DIR = "C:/Temp/Files/";

	    public String uploadExcel(@RequestParam("file") MultipartFile file,String fileName,String uploadPage) {
	        if (file.isEmpty()) {
	            return "File is empty!";
	        }

	        File dir = new File(UPLOAD_DIR);
	        if (!dir.exists()) dir.mkdirs();
	        String orginalFileName = file.getOriginalFilename();
	        File destFile = new File(UPLOAD_DIR + fileName);
	        System.out.println(destFile);
        	System.out.println(file.getOriginalFilename()+"---------------");
	        try (InputStream is = file.getInputStream();
	             OutputStream os = new FileOutputStream(destFile)) {

	            byte[] buffer = new byte[8192];
	            int bytesRead;
	            while ((bytesRead = is.read(buffer)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }

//	            excelToCsvService.convertExcelToCsv("C:/Temp_File/"+orginalFileName, "C:/Test_Temp/csv/temp_csv.csv");
	            
	            loadCsvToOracle(uploadPage);

	            return "File uploaded successfully";

	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Failed to save file";
	        }
	    }
	    
	    public String loadCsvToOracle(String uploadPage) {
	        try {
	            sqlLoaderService.runSqlLoader(uploadPage);
	            return "CSV loaded successfully into Oracle!";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error while loading CSV: " + e.getMessage();
	        }
	    }
}
 
	


