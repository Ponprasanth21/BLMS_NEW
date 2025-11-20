package com.bornfire.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FileWatcherService {

    @Autowired
    private ExcelToOracleService excelService;

//    private static final String IN_FOLDER = "C:/Users/SURESHKUMAR/TEST/IN";
//    private static final String OUT_FOLDER = "C:/Users/SURESHKUMAR/TEST/OUT";
    
    @Value("${in.folder.path}")
    private String IN_FOLDER;

    @Value("${out.folder.path}")
    private String OUT_FOLDER;


    private static final String USER_ID = "SYSTEM";
    private static final String USER_NAME = "SYSTEM";

    // 🔥 Runs every 10 seconds — must be a NO-ARG method
//    @Scheduled(fixedDelay = 10000)
    public void checkIncomingFiles() {

        try {
            File inputFolder = new File(IN_FOLDER);

            if (!inputFolder.exists()) {
                System.out.println("❌ IN folder not found: " + IN_FOLDER);
                return;
            }

            File[] files = inputFolder.listFiles((dir, name) ->
                    name.endsWith(".xlsx") || name.endsWith(".xls"));

            if (files == null || files.length == 0) {
                System.out.println("✔ No new Excel files. Waiting…");
                return;
            }

            System.out.println("==========================================================");
            System.out.println("🔥 NEW FILES DETECTED:");
            Arrays.stream(files).forEach(f -> System.out.println("➡ " + f.getName()));
            System.out.println("==========================================================");
            for (File file : files) {
                processFile(file);
            }

        } catch (Exception e) {
            System.out.println("❌ Scheduler Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void processFile(File file) {
        try {
            System.out.println("-----------------------------------------------------");
            System.out.println("🔥 PROCESSING FILE: " + file.getName());

            String fileName = file.getName();
            if (fileName != null) {
                fileName = fileName.replaceFirst("[.][^.]+$", "");
            }

            System.out.println("THE GETTING FILENAME IS "+fileName);
            // Step 1: Import Excel
            Map<String, Object> response = excelService.importExcel(
                    file.getAbsolutePath(), USER_ID, USER_NAME, fileName
            );

            String newFileName;
            
            

            if ("duplicate".equals(response.get("status"))) {
                // 🔥 File contains duplicates → move as .duplicate
                newFileName = file.getName() + ".duplicate";
                System.out.println("❌ FILE HAS DUPLICATES → MARKING AS .duplicate");

            } else if (!"success".equals(response.get("status"))) {
                // 🔥 Processing failed → mark as .error
                newFileName = file.getName() + ".error";
                System.out.println("❌ FILE FAILED DURING PROCESS → MARKING AS .error");

            } else {
                // 🔥 Processing successful → mark as .complete
                newFileName = file.getName() + ".complete";
                System.out.println("✔ FILE PROCESSED SUCCESSFULLY → MARKING AS .complete");
            }

            // Step 2: Move file to OUT folder
            File outFile = new File(OUT_FOLDER + "/" + newFileName);

            Files.move(file.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("✔ File moved to OUT folder → " + outFile.getAbsolutePath());
            System.out.println("-----------------------------------------------------");

        } catch (Exception ex) {
            System.out.println("❌ ERROR processing file: " + file.getName());
            ex.printStackTrace();
        }
    }
}
