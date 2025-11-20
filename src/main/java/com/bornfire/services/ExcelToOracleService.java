package com.bornfire.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
//import org.springframework.mock.web.MockHttpServletRequest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bornfire.config.SequenceGenerator;
import com.bornfire.controller.BGLSRestController;
import com.bornfire.entities.MULTIPLE_TRANSACTION_ENTITY;
import com.bornfire.entities.MULTIPLE_TRANSACTION_HISTORY_REPO;
import com.bornfire.entities.MULTIPLE_TRANSACTION_REPO;

@Service
public class ExcelToOracleService {

    @Autowired
    private MULTIPLE_TRANSACTION_REPO multiple_TRANSACTION_REPO;

    @Autowired
    private MULTIPLE_TRANSACTION_HISTORY_REPO mULTIPLE_TRANSACTION_HISTORY_REPO;

    @Autowired
    private SequenceGenerator sequence;

    @Autowired
    private AuditConfigure audit;

    @Autowired
    private DateParser dateParser;
    
    @Autowired
    private HttpServletRequest rq;

    // NEW SERVICE FOR AUTOMATIC CALL
    @Autowired
    private BGLSRestController transactionService;


    // ========================================================================
    // EXCEL IMPORT FUNCTION
    // ========================================================================
    public Map<String, Object> importExcel(String filePath, String userID, String userName,String fileName) throws Exception {

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        int successCount = 0;
        int failureCount = 0;

        System.out.println("======================================================");
        System.out.println("STARTING EXCEL IMPORT PROCESS");
        System.out.println("File Path: " + filePath);
        System.out.println("User: " + userID + " / " + userName);
        System.out.println("======================================================");

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("❌ ERROR: FILE NOT FOUND → " + filePath);
            resultMap.put("status", "error");
            resultMap.put("message", "File not found: " + filePath);
            return resultMap;
        }

        System.out.println("✔ File found. Opening workbook...");

        InputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);

        List<HashMap<Integer, String>> mapList = new ArrayList<HashMap<Integer, String>>();
        DataFormatter formatter = new DataFormatter();

        System.out.println("✔ Workbook opened. Reading sheets…");


        // ====================================================================
        // STEP 1 — READ EXCEL ROWS
        // ====================================================================
        for (int s = 0; s < workbook.getNumberOfSheets(); s++) {

            Sheet sheet = workbook.getSheetAt(s);
            System.out.println("Reading Sheet: " + sheet.getSheetName());

            for (int r = 0; r <= sheet.getLastRowNum(); r++) {

                Row row = sheet.getRow(r);
                if (row == null) {
                    System.out.println("Skipping Row " + r + " → NULL");
                    continue;
                }
                if (r < 2) {
                    System.out.println("Skipping Row " + r + " → HEADER");
                    continue;
                }
                if (isRowEmpty(row)) {
                    System.out.println("Skipping Row " + r + " → EMPTY");
                    continue;
                }

                HashMap<Integer, String> rowMap = new HashMap<Integer, String>();

                for (int col = 0; col < 50; col++) {
                    Cell cell = row.getCell(col);
                    String value = formatter.formatCellValue(cell);
                    rowMap.put(col, value);
                }

                System.out.println("✔ Row " + r + " OK: " + rowMap);
                mapList.add(rowMap);
            }
        }

        workbook.close();
        inputStream.close();

        System.out.println("✔ Total Rows Read: " + mapList.size());

        if (mapList.isEmpty()) {
            System.out.println("❌ ERROR: Excel file empty!");
            resultMap.put("status", "error");
            resultMap.put("message", "Excel is empty");
            return resultMap;
        }


        // ====================================================================
        // STEP 2 — DUPLICATE CHECK
        // ====================================================================
        System.out.println("Checking duplicates in database...");

        Set<String> duplicateIds = new HashSet<String>();

        for (HashMap<Integer, String> item : mapList) {

            String transactionId = item.get(0);

            if (transactionId != null && transactionId.trim().length() > 0) {

                List<String> exists = mULTIPLE_TRANSACTION_HISTORY_REPO.getdatavalues(transactionId);

                if (exists != null && !exists.isEmpty()) {
                    System.out.println("❌ DUPLICATE → " + transactionId);
                    duplicateIds.add(transactionId);
                }
            }
        }

        if (!duplicateIds.isEmpty()) {
            System.out.println("======================================================");
            System.out.println("❌ DUPLICATES FOUND → UPLOAD STOPPED");
            System.out.println(duplicateIds);
            System.out.println("======================================================");

            resultMap.put("status", "duplicate");
            resultMap.put("message", "Duplicate IDs: " + duplicateIds.toString());
            return resultMap;
        }

        System.out.println("✔ NO DUPLICATES — inserting records…");


        // ====================================================================
        // STEP 3 — SAVE EACH ROW TO DB
        // ====================================================================
        int rowNum = 2;

        for (HashMap<Integer, String> item : mapList) {

            try {
                System.out.println("-----------------------------------------------------");
                System.out.println("INSERTING ROW: " + rowNum);

                MULTIPLE_TRANSACTION_ENTITY e = new MULTIPLE_TRANSACTION_ENTITY();

                e.setTransaction_id(item.get(0));
                e.setNames(item.get(1));

                String rawRef = item.get(2);
                if (rawRef != null && rawRef.contains("/")) {
                    String[] parts = rawRef.split("/");
                    e.setReference(parts[parts.length - 1]);
                } else {
                    e.setReference(rawRef);
                }

                e.setMobile_number(item.get(3));
                e.setAmount(dateParser.parseBigDecimal(item.get(4)));
                e.setAllocated_amount(dateParser.parseBigDecimal(item.get(5)));
                e.setTrans_time(dateParser.parseDateSafe(item.get(6)));
                e.setStatus(item.get(7));

                e.setSrl_no(sequence.generateRequestUUId());
                e.setEntity_flg("Y");
                e.setDel_flg("N");
                e.setEntry_user(userID);
                e.setEntry_time(new Date());
                e.setRef_transaction_id(null);
                e.setAuth_user(fileName);

                multiple_TRANSACTION_REPO.save(e);

                System.out.println("✔ ROW SAVED: " + rowNum);
                successCount++;

            } catch (Exception ex) {
                System.out.println("❌ ERROR INSERTING ROW " + rowNum);
                ex.printStackTrace();
                failureCount++;
            }

            rowNum++;
        }


        // ====================================================================
        // STEP 4 — AUDIT
        // ====================================================================
        try {
            audit.insertServiceAudit(
                    userID,
                    userName,
                    "TRANSACTION UPLOAD",
                    "UPLOADED SUCCESSFULLY",
                    "MULTIPLE_TRANSACTION",
                    "TRANSACTION UPLOAD",
                    "-"
            );
            System.out.println("✔ AUDIT SAVED");
        } catch (Exception e) {
            System.out.println("❌ AUDIT FAILED");
            e.printStackTrace();
        }


        // ====================================================================
        // STEP 5 — AUTO CALL saveMultipleTransactions1
        // ====================================================================
        System.out.println("======================================================");
        System.out.println("🔥 AUTO BUILDING PAYLOAD FOR NEXT STEP…");

        List<Map<String, Object>> autoPayload = buildTransactionPayload(mapList);

        System.out.println("🔥 CALLING TransactionService.saveMultipleTransactions()…");
        
//        MockHttpServletRequest fakeRq = new MockHttpServletRequest();
//        fakeRq.setMethod("POST");
//        fakeRq.setRequestURI("/ASPIRA/saveMultipleTransactions1");
//        fakeRq.setAttribute("AUTO_UPLOAD", "TRUE");
//        fakeRq.setRemoteAddr("127.0.0.1");

        Map<String, Object> allocationResult = transactionService.saveMultipleTransactions1(autoPayload,null);

        System.out.println("🔥 AUTO ALLOCATION RESULT: " + allocationResult);
        resultMap.put("AllocationResult", allocationResult);


        // ====================================================================
        // STEP 6 — FINAL RESPONSE
        // ====================================================================
        resultMap.put("status", "success");
        resultMap.put("TotalSucceeded", successCount);
        resultMap.put("TotalFailed", failureCount);
        resultMap.put("TotalProcessed", successCount + failureCount);

        System.out.println("======================================================");
        System.out.println("✔ EXCEL IMPORT + AUTO ALLOCATION COMPLETE");
        System.out.println("======================================================");

        return resultMap;
    }


    // ========================================================================
    // PAYLOAD GENERATOR
    // ========================================================================
    public List<Map<String, Object>> buildTransactionPayload(List<HashMap<Integer, String>> mapList) {

        List<Map<String, Object>> payload = new ArrayList<Map<String, Object>>();

        for (HashMap<Integer, String> item : mapList) {

            Map<String, Object> row = new LinkedHashMap<String, Object>();

            row.put("tran_id", item.get(0));
            row.put("acct_num", item.get(1));
            row.put("acct_namedata", item.get(2));
            row.put("tran_amt", parseDouble(item.get(3)));
            row.put("tran_particulardata", parseDouble(item.get(4)));
            row.put("tran_remarks", item.get(5));
            row.put("transaction_date", item.get(6));
            row.put("rate", item.get(7));

            payload.add(row);
        }

        return payload;
    }


    // ========================================================================
    // DOUBLE PARSER
    // ========================================================================
    private Double parseDouble(String s) {
        if (s == null) return 0.0;
        try { return Double.parseDouble(s.replace(",", "").trim()); }
        catch (Exception e) { return 0.0; }
    }


    // ========================================================================
    // CHECK EMPTY ROW
    // ========================================================================
    private boolean isRowEmpty(Row row) {
        if (row == null) return true;

        DataFormatter fmt = new DataFormatter();

        for (int c = 0; c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);

            if (cell != null) {
                String text = fmt.formatCellValue(cell);
                if (text != null && text.trim().length() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
