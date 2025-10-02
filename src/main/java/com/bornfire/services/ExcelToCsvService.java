package com.bornfire.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.controller.ASPIRAUploadController;
import com.monitorjbl.xlsx.StreamingReader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;

@Service
public class ExcelToCsvService {
	private static final Logger logger = LoggerFactory.getLogger(ExcelToCsvService.class);

	private static final String CSV_SAVE_DIR = "C:/Temp/Files/";
	
    public File convertExcelToCsv(MultipartFile file,String fileName) throws IOException {
        // Ensure directory exists
        File dir = new File(CSV_SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            logger.info("directory Created");
        }

        // Create CSV file with same name as uploaded Excel (but .csv extension)
        String originalName = fileName;
        String baseName = (originalName != null && originalName.contains("."))
                ? originalName.substring(0, originalName.lastIndexOf('.'))
                : "upload";
        File csvFile = new File(dir, baseName + ".csv");
    	  

          try (InputStream is = file.getInputStream();
               Workbook workbook = StreamingReader.builder()
                       .rowCacheSize(100)     // keep 100 rows in memory
                       .bufferSize(4096)      // buffer size for reading
                       .open(is);
               BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {

              Sheet sheet = workbook.getSheetAt(0); // first sheet only
              DataFormatter formatter = new DataFormatter();

              for (Row row : sheet) {
                  StringBuilder sb = new StringBuilder();

                  for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                      Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                      String cellValue = formatter.formatCellValue(cell); 
                      
//                      if (cell != null) {
//                          cellValue = cell.toString();
//                      }

                      if (cellValue.contains(",") || cellValue.contains("\"") || cellValue.contains("\n")) {
                          // Escape double quotes and wrap the field in quotes
                          cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
                      }

                              sb.append(cellValue);
                             
                      if (cn < row.getLastCellNum() - 1) {
                          sb.append(",");
                      }
                  }

                  writer.write(sb.toString());
                  writer.newLine();
              }
          }
          
          logger.info("Excel converted to CSV");
          return csvFile;
    }
}
