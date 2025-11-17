package com.bornfire.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bornfire.services.ExcelToOracleService;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelToOracleService excelService;

    @GetMapping("/import")
    public String importData(String excelPath) {
        try {
        	String userID = "SYSTEM";
	        String userName = "SYSTEM";
//            String excelPath = "C:/Users/SURESHKUMAR/TEST/transaction.xlsx";
            excelService.importExcel(excelPath,userID,userName);
            return "Excel Imported Successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
