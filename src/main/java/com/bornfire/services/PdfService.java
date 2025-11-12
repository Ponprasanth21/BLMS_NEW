package com.bornfire.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PdfService {

    public byte[] generateTransactionPdfReport(List<Object[]> rawData, String dueDate) {
        if (rawData == null || rawData.isEmpty()) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("TRANSACTION REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Printed Date
            String todayDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
            Font genFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);
            Paragraph gendate = new Paragraph("Transaction Date : " + dueDate, genFont);
            gendate.setAlignment(Element.ALIGN_RIGHT);
            document.add(gendate);
            
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);
            Paragraph printedDate = new Paragraph("Printed Date : " + todayDate, dateFont);
            printedDate.setSpacingAfter(15);
            printedDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(printedDate);
            
            

            // Table Headers
         // Table Headers
            String[] headers = {
                "TRAN DATE", "ACCT NUM", "ACCT NAME", "TYPE", "TRAN ID",
                "PT TRAN", "IND", "CREDIT", "DEBIT", "TRAN PARTICULAR"
            };

            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setHeaderRows(1);

            // ✅ Adjust column widths (values are relative proportions)
            float[] columnWidths = {
                1.2f, 3.4f, 3.8f, 1.2f, 1.3f,
                1.0f, 1.0f, 1.2f, 1.2f, 2.2f
            };
            table.setWidths(columnWidths);

//            (0, 102, 204)
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 176, 240);

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(headerColor);
                headerCell.setPadding(5);
                table.addCell(headerCell);
            }

            // Row Fonts
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 6);
            Font numericFont = FontFactory.getFont(FontFactory.HELVETICA, 6);

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            double totalCredit = 0;
            double totalDebit = 0;

            for (Object[] rowData : rawData) {
                for (int i = 0; i < headers.length; i++) {
                    Object value = (i < rowData.length) ? rowData[i] : "";
                    String cellValue = "";

                    if (value == null) {
                        cellValue = "";
                    } else if (i == 0) { // TRAN_DATE
                        try {
                            Date parsed = inputDateFormat.parse(value.toString());
                            cellValue = outputDateFormat.format(parsed);
                        } catch (Exception e) {
                            cellValue = value.toString();
                        }
                    } else {
                        cellValue = value.toString();
                    }

                    PdfPCell cell = new PdfPCell(new Phrase(cellValue, rowFont));
                    cell.setPadding(5);

                    if (i == 5 || i == 7 || i == 8) { // PART_TRAN_ID, CREDIT, DEBIT → Right Align
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                        // Only CREDIT/DEBIT need numeric formatting
                        if (i == 7 || i == 8) {
                            try {
                                double num = Double.parseDouble(value.toString().replace(",", ""));
                                cell.setPhrase(new Phrase(String.format("%,.2f", num), numericFont));
                                if (i == 7) totalCredit += num;
                                if (i == 8) totalDebit += num;
                            } catch (Exception ignored) {}
                        }

                    } else {
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    }


                    table.addCell(cell);
                }
            }

            // Add total row
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL :", totalFont));
            totalLabelCell.setColspan(7);
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabelCell.setPadding(5);
            table.addCell(totalLabelCell);

            PdfPCell totalCreditCell = new PdfPCell(new Phrase(String.format("%,.2f", totalCredit), totalFont));
            totalCreditCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCreditCell.setPadding(5);
            table.addCell(totalCreditCell);

            PdfPCell totalDebitCell = new PdfPCell(new Phrase(String.format("%,.2f", totalDebit), totalFont));
            totalDebitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalDebitCell.setPadding(5);
            table.addCell(totalDebitCell);

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setPadding(5);
            table.addCell(emptyCell);

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
