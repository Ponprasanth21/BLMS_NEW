package com.bornfire.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.bornfire.entities.LOAN_REPAYMENT_REPO;
import com.bornfire.entities.RepaymentScheduleEntity;
import com.ibm.icu.text.SimpleDateFormat;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Service
@ConfigurationProperties("output")
@Transactional
public class RepaymentScheduleServices {

	// @Scheduled(cron = "0 * * ? * *")

	// @Scheduled(cron = "0 */30 * ? * *")
	public List<RepaymentScheduleEntity> calculateRepay() throws ParseException {
		double principalAmount = 1000.0;
		String amtSpecific = "Fixed";
		// String installmentFrequency = "MONTHLY";
		// String installmentFrequency = "QUARTERLY";
		// String installmentFrequency = "HALFYEARLY";
		String installmentFrequency = "YEARLY";
		int numberOfInstallments = 12;
		int inst_id = 1;

		String sampleDateString = "05-03-2024";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date installmentStartDate = dateFormat.parse(sampleDateString);
		double installmentPer = 5.0;
		double installmentAmount = 100.0;
		List<RepaymentScheduleEntity> repaymentSchedule = calculateRepaymentSchedule(principalAmount, amtSpecific,
				installmentFrequency, numberOfInstallments, installmentStartDate, installmentPer, installmentAmount,
				inst_id);

		for (RepaymentScheduleEntity entry : repaymentSchedule) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String orgDate = sdf.format(entry.getOriginalDemandDate());
			String demDate = sdf.format(entry.getDemandDate());
			System.out.println("scheduleNo: " + entry.getScheduleNo() + "||" + "originalDemandDate: " + orgDate + "||"
					+ "demandDate: " + demDate + "||" + "demandType: " + entry.getDemandType() + "||" + "demandId: "
					+ entry.getDemandId() + "||" + "demandAmount: " + entry.getDemandAmount() + "||" + "installmentNo: "
					+ entry.getInstallmentNo() + "||" + "Remarks: " + entry.getRemarks());
		}

		return repaymentSchedule;
	}

	public List<RepaymentScheduleEntity> calculateRepaymentSchedule(double productAmount, String amtSpecific,
			String installmentFrequency, int numberOfInstallments, Date installmentStartDate, double installmentPer,
			double principleAmount, int inst_Id) {
		List<RepaymentScheduleEntity> repaymentSchedule = new ArrayList<>();
		for (int i = 1; i <= numberOfInstallments; i++) {
			RepaymentScheduleEntity entry = new RepaymentScheduleEntity();
			entry.setScheduleNo(inst_Id);
			entry.setDemandType("P");
			entry.setDemandId("PRDEM");
			entry.setInstallmentNo(i);
			entry.setRemarks("");
			entry.setDemandAmount(
					calculateDemandAmount(amtSpecific, installmentPer, numberOfInstallments, productAmount));

			// Determine demand date based on frequency
			Date originalDemandDate = installmentStartDate;
			Date demandDate = calculateDemandDate(installmentFrequency, originalDemandDate, i);

			entry.setOriginalDemandDate(demandDate);
			entry.setDemandDate(demandDate);

			repaymentSchedule.add(entry);

			// Calculate the next installment start date
			installmentStartDate = demandDate;
		}

		return repaymentSchedule;
	}

	public List<RepaymentScheduleEntity> calculateRepaymentScheduleAtMaturity(double productAmount, String amtSpecific,
			String installmentFrequency, int numberOfInstallments, Date installmentStartDate, double installmentPer,
			double principleAmount, int inst_Id) {
		List<RepaymentScheduleEntity> repaymentSchedule = new ArrayList<>();
		for (int i = 1; i <= numberOfInstallments; i++) {
			RepaymentScheduleEntity entry = new RepaymentScheduleEntity();

			if (i == numberOfInstallments) {
				entry.setScheduleNo(inst_Id);
				entry.setDemandType("P");
				entry.setDemandId("PRDEM");
				entry.setInstallmentNo(i);
				entry.setRemarks("");
				entry.setDemandAmount(productAmount);

				// Determine demand date based on frequency
				Date originalDemandDate = installmentStartDate;
				Date demandDate = calculateDemandDate(installmentFrequency, originalDemandDate, i);

				entry.setOriginalDemandDate(demandDate);
				entry.setDemandDate(demandDate);
				// installmentStartDate = demandDate;

			} else {
				entry.setScheduleNo(inst_Id);
				entry.setDemandType("P");
				entry.setDemandId("PRDEM");
				entry.setInstallmentNo(i);
				entry.setRemarks("");
				entry.setDemandAmount(0.0d);

				// Determine demand date based on frequency
				Date originalDemandDate = installmentStartDate;
				Date demandDate = calculateDemandDate(installmentFrequency, originalDemandDate, i);

				entry.setOriginalDemandDate(demandDate);
				entry.setDemandDate(demandDate);
				installmentStartDate = demandDate;
			}

			repaymentSchedule.add(entry);

			// Calculate the next installment start date

		}

		return repaymentSchedule;
	}

	private double calculateDemandAmount(String amtSpecific, double installmentPer, int numberOfInstallments,
			double productAmount) {
		return "Fixed".equals(amtSpecific) ? (productAmount / numberOfInstallments)
				: (productAmount * installmentPer / 100);
	}

	private Date calculateDemandDate(String installmentFrequency, Date originalDemandDate, int i) {

		// all months show
		/*
		 * Calendar calendar = Calendar.getInstance();
		 * calendar.setTime(originalDemandDate); if (i == 1) {
		 * calendar.add(Calendar.MONTH, 1); calendar.set(Calendar.DAY_OF_MONTH, 1);
		 * calendar.add(Calendar.DAY_OF_MONTH, -1); } else {
		 * calendar.add(Calendar.MONTH, 2); calendar.set(Calendar.DAY_OF_MONTH, 1);
		 * calendar.add(Calendar.DAY_OF_MONTH, -1); }
		 */

		// only frequency months show

		Calendar calendar = Calendar.getInstance();
		if ("Monthly".equals(installmentFrequency)) {

			calendar.setTime(originalDemandDate);
			calendar.add(Calendar.MONTH, 1);
			/*
			 * if (i == 1) {
			 * 
			 * calendar.add(Calendar.MONTH, 1); calendar.set(Calendar.DAY_OF_MONTH, 1);
			 * calendar.add(Calendar.DAY_OF_MONTH, -1); } else {
			 * calendar.add(Calendar.MONTH, 2); calendar.set(Calendar.DAY_OF_MONTH, 1);
			 * calendar.add(Calendar.DAY_OF_MONTH, -1); }
			 */

			// return calendar.getTime();
		} else if ("Quarterly".equals(installmentFrequency)) {
			// Calendar calendar = Calendar.getInstance();
			calendar.setTime(originalDemandDate);
			calendar.add(Calendar.MONTH, 3);

			/*
			 * if (i == 1) {
			 * 
			 * calendar.add(Calendar.MONTH, 3); calendar.add(Calendar.MONTH, 1);
			 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.add(Calendar.DAY_OF_MONTH,
			 * -1); } else { calendar.add(Calendar.MONTH, 4);
			 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.add(Calendar.DAY_OF_MONTH,
			 * -1); }
			 */

			// return calendar.getTime();
		} else if ("Halfyearly".equals(installmentFrequency)) {
			// Calendar calendar = Calendar.getInstance();
			calendar.setTime(originalDemandDate);
			calendar.add(Calendar.MONTH, 6);

			/*
			 * if (i == 1) {
			 * 
			 * calendar.add(Calendar.MONTH, 6); calendar.add(Calendar.MONTH, 1);
			 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.add(Calendar.DAY_OF_MONTH,
			 * -1); } else { calendar.add(Calendar.MONTH, 7);
			 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.add(Calendar.DAY_OF_MONTH,
			 * -1); }
			 */

			// return calendar.getTime();
		} else if ("Yearly".equals(installmentFrequency)) {
			// Calendar calendar = Calendar.getInstance();
			calendar.setTime(originalDemandDate);
			calendar.add(Calendar.MONTH, 12);

			/*
			 * if (i == 1) {
			 * 
			 * calendar.add(Calendar.MONTH, 12); calendar.add(Calendar.MONTH, 1);
			 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.add(Calendar.DAY_OF_MONTH,
			 * -1); } else { calendar.add(Calendar.MONTH, 13);
			 * calendar.set(Calendar.DAY_OF_MONTH, 1); calendar.add(Calendar.DAY_OF_MONTH,
			 * -1); }
			 */

		} else if ("Bullet".equals(installmentFrequency)) {
			// Calendar calendar = Calendar.getInstance();
			calendar.setTime(originalDemandDate);
			calendar.add(Calendar.MONTH, 1);

		}

		return calendar.getTime();
	}
	
	@Autowired
	LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;
	
	public byte[] generateLoanDueExcel(List<Object[]> rawData, String dueDate) {
	    if (rawData == null || rawData.isEmpty()) {
	        System.out.println("No raw data found for due date: " + dueDate);
	        return new byte[0];
	    }

	    Collection<Map<String, ?>> mappedData = new ArrayList<>();
	    for (Object[] row : rawData) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("ACTIVATION_DATE", row[0]);
	        map.put("ACCOUNT_HOLDER_NAME", row[1]);
	        map.put("ACCOUNT_HOLDER_ID", row[2]);
	        map.put("ACCOUNT_ID", row[3]);
	        map.put("LOAN_AMOUNT", row[4]);
	        map.put("MANUALOVERRIDE_AMOUNT", row[5]);
	        map.put("REPAYMENT_INSTALLMENTS", row[6]);
	        map.put("INTEREST_RATE", row[7]);
	        map.put("PRINCIPAL_BALANCE", row[8]);
	        map.put("INTEREST_BALANCE", row[9]);
	        map.put("FEES_BALANCE", row[10]);
	        map.put("TOTAL_BALANCE", row[11]);
	        map.put("LOAN_CYCLE", row[12]);
	        map.put("EMAIL_ADDRESS", row[13]);
	        map.put("MOBILE_PHONE", row[14]);
	        map.put("EMPLOYMENT_STATUS", row[15]);
	        map.put("EMPLOYER", row[16]);
	        map.put("ACCOUNT_STATE", row[17]);
	        map.put("TOTAL_PAID", row[18]);
	        map.put("PRINCIPAL_PAID", row[19]);
	        map.put("INTEREST_PAID", row[20]);
	        map.put("FEES_PAID", row[21]);
	        map.put("RETAILER_NAME", row[22]);
	        map.put("DAYS_LATE", row[23]);
	        map.put("GENDER", row[24]);
	        map.put("BIRTH_DATE", row[25]);
	        mappedData.add(map);
	    }

	    try (InputStream jasperStream = getClass().getResourceAsStream("/static/jasper/EOM.jrxml")) {
	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

	        // Data source for Jasper
	        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(mappedData);

	        // Pass parameters if any
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("DUE_DATE", dueDate); // optional, only if used in jrxml

	        // Fill report
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

	        // Export to Excel
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

	        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
	        configuration.setOnePagePerSheet(false);
	        configuration.setDetectCellType(true);
	        configuration.setCollapseRowSpan(false);
	        exporter.setConfiguration(configuration);

	        exporter.exportReport();
	        System.out.println("✅ Excel generated with " + mappedData.size() + " rows.");
	        return baos.toByteArray();

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new byte[0];
	    }
	}

}
