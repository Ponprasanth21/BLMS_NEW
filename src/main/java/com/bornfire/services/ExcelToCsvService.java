//package com.bornfire.services;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.bornfire.controller.ASPIRAUploadController;
//import com.monitorjbl.xlsx.StreamingReader;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
//import java.io.*;
//
//@Service
//public class ExcelToCsvService {
//	private static final Logger logger = LoggerFactory.getLogger(ExcelToCsvService.class);
//
//	private static final String CSV_SAVE_DIR = "C:/Temp/Files/";
//	
//    @SuppressWarnings("deprecation")
//	public File convertExcelToCsv(MultipartFile file,String fileName) throws IOException {
//        // Ensure directory exists
//        File dir = new File(CSV_SAVE_DIR);
//        if (!dir.exists()) {
//            dir.mkdirs();
//            logger.info("directory Created");
//        }
//
//        // Create CSV file with same name as uploaded Excel (but .csv extension)
//        String originalName = fileName;
//        String baseName = (originalName != null && originalName.contains("."))
//                ? originalName.substring(0, originalName.lastIndexOf('.'))
//                : "upload";
//        File csvFile = new File(dir, baseName + ".csv");
//    	  
//
//          try (InputStream is = file.getInputStream();
//               Workbook workbook = StreamingReader.builder()
//                       .rowCacheSize(100)     // keep 100 rows in memory
//                       .bufferSize(4096)      // buffer size for reading
//                       .open(is);
//               BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
//
//              Sheet sheet = workbook.getSheetAt(0); // first sheet only
//              DataFormatter formatter = new DataFormatter();
//              int count=0;
//              for (Row row : sheet) {
//                  StringBuilder sb = new StringBuilder();
//
//                  for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//                	  
//                      Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                      String cellValue =  getCellValue(cell).trim();
//
//                      if (cellValue.contains(",") || cellValue.contains("\"") || cellValue.contains("\n")) {
//                          // Escape double quotes and wrap the field in quotes
//                          cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
//                      }
//                     
//                      sb.append(cellValue.replace(",", ""));
//                             
//                      if (cn < row.getLastCellNum() - 1) {
//                          sb.append(",");
//                      }
//                  }
//
//                  writer.write(sb.toString());
//                  writer.newLine();
//                  count++;
//                  System.out.println(count);
//              }
//          }
//          
//          logger.info("Excel converted to CSV");
//          return csvFile;
//    }
//    
//    private String getCellValue(Cell cell) {
//        DataFormatter formatter = new DataFormatter();
//
//        switch (cell.getCellTypeEnum()) {
//            case STRING:
//                
////            	return cell.getStringCellValue();
//            	 String textValue = cell.getStringCellValue().trim();
//
//                 // 1️⃣ Try ISO 8601 format first (e.g., 2025-09-25T09:50:15+03:00)
//                 try {
//                     java.time.OffsetDateTime odt = java.time.OffsetDateTime.parse(textValue);
//                     java.time.LocalDateTime ldt = odt.toLocalDateTime(); // preserve time
//                     java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                     return ldt.format(dtf); // e.g., 2025-09-24 12:53:44
//                 } catch (java.time.format.DateTimeParseException e) {
//                     // Not ISO timestamp, try other patterns
//                 }
//
//                 // 2️ Try common date patterns (date only)
//                 String[] datePatterns = {
//                     "yyyy-MM-dd", "dd-MM-yyyy", "MM/dd/yyyy", "dd/MM/yyyy",
//                     "dd-MMM-yyyy", "yyyy/MM/dd", "MM-dd-yyyy", "dd.MM.yyyy",
//                     "yyyy.MM.dd", "dd MMM yyyy"
//                 };
//
//                 for (String pattern : datePatterns) {
//                     try {
//                         java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat(pattern);
//                         java.util.Date parsedDate = inputFormat.parse(textValue);
//                         java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                         return outputFormat.format(parsedDate); // date + time
//                     } catch (java.text.ParseException e) {
//                         // ignore and try next
//                     }
//                 }
//
//                 return textValue; // fallback: plain string
//            case NUMERIC:
//            	
//            	String cellVslue = formatter.formatCellValue(cell);
//            	
//                if (DateUtil.isCellDateFormatted(cell)) {
//                	
//                    java.util.Date date = cell.getDateCellValue();
//                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
//                    
//                    return sdf.format(date);
//                } else if (cellVslue.matches("(?i).*[0-9]E[+-]?[0-9]+.*")){
//                    
//                	return new java.math.BigDecimal(cell.getNumericCellValue())
//                            .toPlainString();
//                }else {
//                	return formatter.formatCellValue(cell);
//                }
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return formatter.formatCellValue(cell);
//            case BLANK:
//                return "";
//            default:
//                return formatter.formatCellValue(cell);
//        }
//    }
//
//}


package com.bornfire.services;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelToCsvService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelToCsvService.class);
    private static final String CSV_SAVE_DIR = "C:/Temp/Files/";

    private static final SimpleDateFormat NUMERIC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter ISO_OUTPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public File convertExcelToCsv(MultipartFile file, String fileName) throws IOException {
        // Ensure directory exists
    	logger.info("Saved Start :" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        File dir = new File(CSV_SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            logger.info("Directory created: {}", CSV_SAVE_DIR);
        }

        // CSV file path
        String baseName = (fileName != null && fileName.contains("."))
                ? fileName.substring(0, fileName.lastIndexOf('.'))
                : "upload";
        File csvFile = new File(dir, baseName + ".csv");

        try (InputStream is = file.getInputStream();
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)     // rows in memory
                     .bufferSize(4096)      // read buffer
                     .open(is);
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {

            Sheet sheet = workbook.getSheetAt(0); // first sheet only
            DataFormatter formatter = new DataFormatter();
        	int count= 0;
            for (Row row : sheet) {
                // Skip completely empty rows
                if (isRowEmpty(row)) continue;

                StringBuilder sb = new StringBuilder();
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = getCellValue(cell);

                    // Escape commas, quotes, and newlines for CSV
                    if (cellValue.contains(",") || cellValue.contains("\"") || cellValue.contains("\n")) {
                        cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
                    }

                    sb.append(cellValue);
                    if (cn < row.getLastCellNum() - 1) sb.append(",");
                }
                count++;
                writer.write(sb.toString());
                writer.newLine();
                System.out.println(count);
            }
        }

        logger.info("Excel converted to CSV: {}", csvFile.getAbsolutePath());
        logger.info("Saved Start :" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return csvFile;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int cn = 0; cn < row.getLastCellNum(); cn++) {
            Cell cell = row.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK
                    && cell.getCellTypeEnum() != CellType._NONE
                    && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case STRING:
                String text = cell.getStringCellValue().trim();
                if (text.contains("T")) { // likely ISO timestamp
                    try {
                        OffsetDateTime odt = OffsetDateTime.parse(text);
                        return odt.toLocalDateTime().format(ISO_OUTPUT);
                    } catch (Exception e) {
                        return text; // fallback plain string
                    }
                }
                return text;

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return NUMERIC_DATE_FORMAT.format(cell.getDateCellValue());
                } else {
                    String val = new DataFormatter().formatCellValue(cell);
                    if (val.matches(".*[0-9]E[+-]?.*")) {
                        return new BigDecimal(cell.getNumericCellValue()).toPlainString();
                    } else return val;
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return new DataFormatter().formatCellValue(cell);

            case BLANK:
                return "";

            default:
                return new DataFormatter().formatCellValue(cell);
        }
    }
}
