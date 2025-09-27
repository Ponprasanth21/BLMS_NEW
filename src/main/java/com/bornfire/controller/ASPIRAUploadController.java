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
import com.bornfire.services.ExelDownloadService;
import com.bornfire.services.UploadService;

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
	            resultMap = UploadService.saveCustomerFile(file, userID, userName,overwrite);
	        } else if ("LOAN".equalsIgnoreCase(fileInput)) {
	            resultMap = UploadService.saveLoanFile(file, userID, userName, overwrite);
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
		
}
 
	


