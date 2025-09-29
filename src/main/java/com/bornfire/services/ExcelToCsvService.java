package com.bornfire.services;

import org.apache.poi.ss.usermodel.CellType;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.*;

@Service
public class ExcelToCsvService {

    public void convertExcelToCsv(String excelFilePath, String csvFilePath) throws IOException {

        Path excelPath = Paths.get(excelFilePath);
        Path csvPath = Paths.get(csvFilePath);
        System.out.println("CSV");

        try (InputStream is = new FileInputStream(excelPath.toFile());
             Workbook workbook = StreamingReader.builder()
                     .rowCacheSize(100)     // number of rows to keep in memory
                     .bufferSize(4096)      // buffer size to read from InputStream
                     .open(is);
             CSVWriter writer = new CSVWriter(new FileWriter(csvPath.toFile()))) {

            Sheet sheet = workbook.getSheetAt(0); // first sheet
            int count=0;
            for (Row row : sheet) {
                boolean allEmpty = true;
                String[] csvRow = new String[56];
                count++;
                System.out.println(count);

                for (int cn = 0; cn < 56; cn++) {
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String val = getCellValueAsString(cell);
                    if (!val.isEmpty()) allEmpty = false;
                    csvRow[cn] = val;
                }

                if (allEmpty) break; // stop when an entirely empty row is found
                writer.writeNext(csvRow);
            }
        }

        System.out.println("Excel converted to CSV successfully: " + csvFilePath);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) { // returns int in POI 3.17
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_ERROR:
            default:
                return "";
        }
    }


}

