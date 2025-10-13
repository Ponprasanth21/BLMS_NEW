package com.bornfire.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TRAN_MAIN_TRM_WRK_REP extends JpaRepository<TRAN_MAIN_TRM_WRK_ENTITY, String> {

	/* Barath */

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE acct_num = :accountNum AND TRAN_DATE = TO_DATE(:tranDate, 'YYYY-MM-DD')", nativeQuery = true)
	int checkTransactionDateExists(@Param("accountNum") String accountNum, @Param("tranDate") String tranDate);

	@Query(value = "SELECT TRAN_DATE_BAL FROM BGLS_DAILY_ACCT_BAL " + "WHERE ACCT_NUM = :accountNum "
			+ "AND TRAN_DATE = (SELECT MAX(TRAN_DATE) FROM BGLS_DAILY_ACCT_BAL WHERE ACCT_NUM = :accountNum AND ROWNUM <= 1)", nativeQuery = true)
	List<BigDecimal> findLatestTRAN_DATE_BALByAccountNumber(@Param("accountNum") String accountNum);

	@Modifying
	@Transactional
	@Query(value = "UPDATE BGLS_DAILY_ACCT_BAL SET end_tran_date = TO_DATE(:endDate, 'YYYY-MM-DD') "
			+ "WHERE ACCT_NUM = :accountNum AND END_TRAN_DATE = TO_DATE('2099-12-31', 'YYYY-MM-DD')", nativeQuery = true)
	void updateEndDateToYesterday1(@Param("accountNum") String accountNum, @Param("endDate") LocalDate endDate);

	@Modifying
	@Transactional
	// NOTE: Oracle doesn't have a 'yesterday' date literal. This is a placeholder
	// and should be updated to a calculated date (e.g., SYSDATE - 1)
	// or a passed parameter in a real application. Assuming 'yesterday' is meant to
	// be a simple string update here for literal conversion.
	@Query(value = "UPDATE BGLS_DAILY_ACCT_BAL SET end_tran_date = 'yesterday' "
			+ "WHERE ACCT_NUM = :accountNum AND END_TRAN_DATE = TO_DATE('2099-12-31', 'YYYY-MM-DD')", nativeQuery = true)
	void updateEndDateToYesterday(@Param("accountNum") String accountNum);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO BGLS_DAILY_ACCT_BAL "
			+ "(GL_CODE, GL_DESC, GLSH_CODE, GLSH_DESC, ACCT_NUM, ACCT_NAME, ACCT_CRNCY, "
			+ "TRAN_DR_BAL, TRAN_CR_BAL, TRAN_DATE_BAL, TRAN_DATE, TRAN_TOT_NET, END_TRAN_DATE, "
			+ "ENTRY_USER_ID, ENTRY_TIME, DEL_FLG) "
			+ "VALUES (:glCode, :glDesc, :glshCode, :glshDesc, :accountNum, :acctName, :acctCrncy, "
			+ ":totalDebit, :totalCredit, :tranDateBal, TO_DATE(:tranDate, 'YYYY-MM-DD'), :netAmount, TO_DATE('2099-12-31', 'YYYY-MM-DD'), 'SYSTEM', SYSDATE, 'N')", nativeQuery = true)
	void insertNewAccountBalance(@Param("glCode") String glCode, @Param("glDesc") String glDesc,
			@Param("glshCode") String glshCode, @Param("glshDesc") String glshDesc,
			@Param("accountNum") String accountNum, @Param("acctName") String acctName,
			@Param("acctCrncy") String acctCrncy, @Param("tranDateBal") BigDecimal tranDateBal,
			@Param("tranDate") String tranDate, // Corrected parameter name
			@Param("netAmount") BigDecimal netAmount, @Param("totalDebit") BigDecimal totalDebit,
			@Param("totalCredit") BigDecimal totalCredit);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO BGLS_DAILY_ACCT_BAL "
			+ "(GL_CODE, GL_DESC, GLSH_CODE, GLSH_DESC, ACCT_NUM, ACCT_NAME, ACCT_CRNCY, "
			+ "TRAN_DR_BAL, TRAN_CR_BAL, TRAN_DATE_BAL, TRAN_DATE, TRAN_TOT_NET, END_TRAN_DATE, "
			+ "ENTRY_USER_ID, ENTRY_TIME, DEL_FLG) "
			+ "VALUES (:glCode, :glDesc, :glshCode, :glshDesc, :accountNum, :acctName, :acctCrncy, "
			+ ":totalDebit, :totalCredit, :tranDateBal, SYSDATE + 3, :netAmount, TO_DATE('2099-12-31', 'YYYY-MM-DD'), 'SYSTEM', SYSDATE, 'N')", nativeQuery = true)
	void insertNewAccountBalance1(@Param("glCode") String glCode, @Param("glDesc") String glDesc,
			@Param("glshCode") String glshCode, @Param("glshDesc") String glshDesc,
			@Param("accountNum") String accountNum, @Param("acctName") String acctName,
			@Param("acctCrncy") String acctCrncy, @Param("tranDateBal") BigDecimal tranDateBal,
			@Param("netAmount") BigDecimal netAmount, @Param("totalDebit") BigDecimal totalDebit,
			@Param("totalCredit") BigDecimal totalCredit);

	@Query(value = "SELECT COUNT(TRAN_AMT) AS COUNTAMT, SUM(TRAN_AMT) AS TRANAMT " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE DEL_FLG != 'Y' " + "AND PART_TRAN_TYPE = 'debit' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE)", nativeQuery = true)
	List<Object[]> getWoforDebitValues();

	@Query(value = "SELECT COUNT(TRAN_AMT) AS COUNTAMT, SUM(TRAN_AMT) AS TRANAMT " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE DEL_FLG != 'Y' " + "AND PART_TRAN_TYPE = 'credit' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE)", nativeQuery = true)
	List<Object[]> getWoforcreditValues();

	@Query(value = "SELECT COUNT(DISTINCT acct_num) AS COUNTACCTNUM, " + "COUNT(TRAN_AMT) AS COUNTAMT, "
			+ "SUM(TRAN_AMT) AS TRANAMT " + "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' "
			+ "AND TRAN_STATUS = 'POSTED' " + "AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE)", nativeQuery = true)
	Object[] getWoforTotalValues();

	@Query(value = "SELECT acct_num, " + "acct_name, " + "COUNT(TRAN_AMT) AS COUNTAMT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT ELSE 0 END) AS TOTAL_CREDIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'debit' THEN TRAN_AMT ELSE 0 END) AS TOTAL_DEBIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT "
			+ "         WHEN PART_TRAN_TYPE = 'debit' THEN -TRAN_AMT " + "         ELSE 0 END) AS NETAMT "
			+ "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TO_DATE(:tranDate, 'YYYY-MM-DD') "
			+ "GROUP BY acct_num, acct_name", nativeQuery = true)
	List<Object[]> getNetDebitCreditWithCountForCurrentDate(@Param("tranDate") String tranDate);

	@Query(value = "SELECT acct_num, " + "acct_name, " + "COUNT(TRAN_AMT) AS COUNTAMT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT ELSE 0 END) AS TOTAL_CREDIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'debit' THEN TRAN_AMT ELSE 0 END) AS TOTAL_DEBIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT "
			+ "         WHEN PART_TRAN_TYPE = 'debit' THEN -TRAN_AMT " + "         ELSE 0 END) AS NETAMT "
			+ "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' " + "AND TRAN_STATUS = 'ENTERED' "
			+ "AND TRUNC(TRAN_DATE) = TO_DATE(:tranDate, 'YYYY-MM-DD') "
			+ "GROUP BY acct_num, acct_name", nativeQuery = true)
	List<Object[]> getNetDebitCreditWithCountForCurrentDatedemo(@Param("tranDate") String tranDate);

	@Query(value = "SELECT acct_num, " + "COUNT(TRAN_AMT) AS COUNTAMT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT ELSE 0 END) AS TOTAL_CREDIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'debit' THEN TRAN_AMT ELSE 0 END) AS TOTAL_DEBIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT "
			+ "         WHEN PART_TRAN_TYPE = 'debit' THEN -TRAN_AMT " + "         ELSE 0 END) AS NETAMT "
			+ "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' " + "AND TRAN_STATUS = 'ENTERED' "
			+ "AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE + 3) " + "GROUP BY acct_num", nativeQuery = true)
	List<Object[]> getNetDebitCreditWithCountForCurrentDate1();

	@Query(value = "SELECT " + "SUM(CASE WHEN PART_TRAN_TYPE = 'credit' THEN TRAN_AMT ELSE 0 END) - "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'debit' THEN TRAN_AMT ELSE 0 END) AS NET_TOTAL "
			+ "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TO_DATE(:tranDate, 'YYYY-MM-DD')", nativeQuery = true)
	BigDecimal getTotalValues(@Param("tranDate") String tranDate);

	@Query(value = "SELECT SUM(COUNTAMT) AS TOTAL_COUNT " + "FROM ( " + "    SELECT COUNT(TRAN_AMT) AS COUNTAMT "
			+ "    FROM BGLS_TRM_WRK_TRANSACTIONS " + "    WHERE DEL_FLG != 'Y' " + "    AND TRAN_STATUS = 'POSTED' "
			+ "    GROUP BY acct_num " + ") AccountCounts", nativeQuery = true)
	BigDecimal getTotalTransactionCount1();

	@Query(value = "SELECT SUM(COUNTAMT) AS TOTAL_COUNT " + "FROM ( " + "    SELECT COUNT(TRAN_AMT) AS COUNTAMT "
			+ "    FROM BGLS_TRM_WRK_TRANSACTIONS " + "    WHERE DEL_FLG != 'Y' " + "    AND TRAN_STATUS = 'POSTED' "
			+ "    AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE) " + "    GROUP BY acct_num "
			+ ") AccountCounts", nativeQuery = true)
	BigDecimal getTotalTransactionCount_before();

	@Query(value = "SELECT SUM(COUNTAMT) AS TOTAL_COUNT " + "FROM ( " + "    SELECT COUNT(TRAN_AMT) AS COUNTAMT "
			+ "    FROM BGLS_TRM_WRK_TRANSACTIONS " + "    WHERE DEL_FLG != 'Y' " + "    AND TRAN_STATUS = 'POSTED' "
			+ "    AND TRUNC(TRAN_DATE) = TO_DATE(:tranDate, 'YYYY-MM-DD') " + "    GROUP BY acct_num "
			+ ") AccountCounts", nativeQuery = true)
	Object getTotalTransactionCount(@Param("tranDate") String tranDate);

	@Query(value = "SELECT SUM(TRAN_AMT) AS TOTAL_CREDIT " + "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' "
			+ "AND PART_TRAN_TYPE = 'credit' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE)", nativeQuery = true)
	BigDecimal getTotalCredit_before();

	@Query(value = "SELECT SUM(TRAN_AMT) AS TOTAL_CREDIT " + "FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE DEL_FLG != 'Y' "
			+ "AND PART_TRAN_TYPE = 'credit' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TO_DATE(:tranDate, 'YYYY-MM-DD')", nativeQuery = true)
	BigDecimal getTotalCredit(@Param("tranDate") String tranDate);

	@Query(value = "SELECT SUM(TRAN_AMT * -1) AS TOTAL_DEBIT " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE DEL_FLG != 'Y' " + "AND PART_TRAN_TYPE = 'debit' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TRUNC(SYSDATE)", nativeQuery = true)
	BigDecimal getTotalDebit_before();

	@Query(value = "SELECT SUM(TRAN_AMT * -1) AS TOTAL_DEBIT " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE DEL_FLG != 'Y' " + "AND PART_TRAN_TYPE = 'debit' " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND TRUNC(TRAN_DATE) = TO_DATE(:tranDate, 'YYYY-MM-DD')", nativeQuery = true)
	BigDecimal getTotalDebit(@Param("tranDate") String tranDate);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE acct_num IN (:acctnum) ", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> set_dab_acc_num(@Param("acctnum") List<String> acctnum);
	/* till this Barath */

	@Query(value = "SELECT COUNT(TRAN_AMT) AS COUNTAMT, SUM(TRAN_AMT) AS TRANAMT " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE DEL_FLG <> 'Y' " + "AND UPPER(PART_TRAN_TYPE) = 'DEBIT' "
			+ "AND TRAN_STATUS = 'POSTED'", nativeQuery = true)
	Object[] getwofordebitvalues();

	@Query(value = "SELECT COUNT(TRAN_AMT) AS COUNTAMT, SUM(TRAN_AMT) AS TRANAMT " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE DEL_FLG <> 'Y' " + "AND UPPER(PART_TRAN_TYPE) = 'CREDIT' "
			+ "AND TRAN_STATUS = 'POSTED'", nativeQuery = true)
	Object[] getwoforcreditvalues();

	@Query(value = "SELECT COUNT(TRAN_AMT) AS COUNTAMT, " + "SUM(CASE WHEN PART_TRAN_TYPE = 'Debit' THEN -1 * TRAN_AMT "
			+ "WHEN PART_TRAN_TYPE = 'Credit' THEN 1 * TRAN_AMT ELSE 0 END) AS TRANAMT "
			+ "FROM BGLS_TRM_WRK_TRANSACTIONS WHERE DEL_FLG != 'Y' AND TRAN_STATUS='POSTED'", nativeQuery = true)
	Object[] getTransactionValues();

	@Query(value = "SELECT COUNT(PART_TRAN_TYPE) as TRAN_TYPE FROM BGLS_TRM_WRK_TRANSACTIONS WHERE DEL_FLG='Y'", nativeQuery = true)
	Object getdelvalues();

	@Query(value = "SELECT COUNT(DISTINCT(TRAN_ID)) as ID FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRAN_STATUS='ENTERED'", nativeQuery = true)
	Object getunpostedvalues();

	// NOTE: Replace 'OrderIDSequence' with your actual Oracle sequence name
	@Query(value = "SELECT OrderIDSequence.NEXTVAL FROM DUAL", nativeQuery = true)
	String gettrmRefUUID();

	@Query(value = "SELECT TRAN_ID_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	String gettrmRefUUID1();

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRAN_STATUS = 'ENTERED' ORDER BY TRAN_DATE, TRAN_ID, PART_TRAN_ID FETCH FIRST 1000 ROWS ONLY ", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> findByjournal();

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRUNC(tran_date)=TRUNC(?1) ORDER BY TRAN_DATE, TRAN_ID, PART_TRAN_ID", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> findByjournal1(Date date);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS aa WHERE aa.part_tran_id =?2 AND aa.tran_id =?1 ", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getmodifyjournal(String tran_id, String part_tran_id);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE tran_id=?1", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> findByjournalmodify();

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS aa WHERE aa.part_tran_id =?2 AND aa.tran_id =?1 ", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getValuepop(String tran_id, String part_tran_id);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS aa WHERE aa.part_tran_id =?3 AND aa.tran_id =?1 AND aa.acct_num = ?2 ", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getValuepopvalues(String tran_id, String acct_num, String part_tran_id);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE tran_id = ?1", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> findByjournalvalues(String tran_id);

	@Query(value = "SELECT tran_id, Part_tran_id FROM BGLS_TRM_WRK_TRANSACTIONS aa WHERE aa.acct_num = ?1 AND ROWNUM = 1", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY gettranpopvalues(String acct_num);

	@Query(value = "SELECT tran_amt FROM BGLS_TRM_WRK_TRANSACTIONS WHERE part_tran_type = 'CREDIT' AND TRAN_ID = ?1 AND PART_TRAN_ID=?2", nativeQuery = true)
	BigDecimal getcredit(String tran_id, BigDecimal partTranID);

	@Query(value = "SELECT tran_amt FROM BGLS_TRM_WRK_TRANSACTIONS WHERE part_tran_type = 'DEBIT' AND TRAN_ID = ?1 AND PART_TRAN_ID=?2", nativeQuery = true)
	BigDecimal getdebit(String tran_id, BigDecimal partTranID);

	@Query(value = "SELECT TRAN_DATE, TRAN_ID || '/' || TO_CHAR(PART_TRAN_ID) AS TRANSACTION_ID, TRAN_PARTICULAR, ACCT_CRNCY, TRAN_AMT, CASE PART_TRAN_TYPE WHEN 'Credit' \r\n"
			+ "THEN TRAN_AMT ELSE 0 END AS Credit, CASE PART_TRAN_TYPE WHEN 'Debit' THEN TRAN_AMT ELSE 0 END AS\r\n"
			+ "Debit FROM BGLS_TRM_WRK_TRANSACTIONS WHERE acct_num=?1 AND TRAN_STATUS='POSTED' ORDER BY TRAN_DATE, TRAN_ID, PART_TRAN_ID", nativeQuery = true)
	List<Object[]> getList(@Param("acct_num") String acct_num);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS aa WHERE aa.acct_num = ?1 AND ROWNUM = 1", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY gettranpopvaluesdata(String acct_num);

	@Query(value = "SELECT MAX(part_tran_id) AS part_tran_id FROM BGLS_TRM_WRK_TRANSACTIONS WHERE tran_id =?1", nativeQuery = true)
	String maxPartranID(String tranId);

	@Query(value = "SELECT MIN(part_tran_id) AS part_tran_id FROM BGLS_TRM_WRK_TRANSACTIONS WHERE tran_id = ?1", nativeQuery = true)
	String minPartTranID(String tranId);

	@Query(value = "SELECT COUNT(DISTINCT part_tran_id) FROM BGLS_TRM_WRK_TRANSACTIONS WHERE tran_id = ?1", nativeQuery = true)
	Integer countPartTranIDs(String tranId);

	@Query(value = "SELECT DISTINCT part_tran_id FROM BGLS_TRM_WRK_TRANSACTIONS WHERE tran_id = ?1", nativeQuery = true)
	List<Integer> currentTableRecords(String tranId);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE ACCT_NUM = ?1 AND TRAN_DATE BETWEEN TO_DATE(?2, 'YYYY-MM-DD') AND TO_DATE(?3, 'YYYY-MM-DD') AND TRAN_STATUS='POSTED' ORDER BY TRAN_ID", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> getTranList(String acct_num, String fromdate, String todate);

	@Query(value = "SELECT TRAN_DATE_BAL FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE ACCT_NUM = ?1 "
			+ "AND TRAN_DATE = TO_DATE(?2, 'YYYY-MM-DD') - 1", nativeQuery = true)
	BigDecimal getTranDateBAlance(String acct_num, String fromdateref);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE acct_num =?1 AND TRAN_STATUS='POSTED' ORDER BY TRAN_DATE", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> findbyacctno(String acct_num);

	/* Thanveer Consistency check */

	@Query(value = "SELECT " + "SUM(CASE WHEN PART_TRAN_TYPE = 'Credit' THEN TRAN_AMT ELSE 0 END) AS TRANAMT_CREDIT, "
			+ "SUM(CASE WHEN PART_TRAN_TYPE = 'Debit' THEN TRAN_AMT ELSE 0 END) AS TRANAMT_DEBIT "
			+ "FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRAN_STATUS= 'POSTED'", nativeQuery = true)
	Object[] getcheck1();

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRUNC(TRAN_DATE)=TRUNC(?1) AND ENTRY_USER=?2 AND ACCT_NUM LIKE '%LA%' ORDER BY TRAN_ID", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> getTransactionRecords(Date con_date, String user);

	@Query(value = "SELECT count(TRAN_ID) as TRAN_ID FROM BGLS_TRM_WRK_TRANSACTIONS where TRAN_DATE=?1 and TRAN_STATUS='ENTERED' and DEL_FLG='N'", nativeQuery = true)
	Object[] getvalueusingdate(String date);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRUNC(TRAN_DATE)=TRUNC(?1) AND tran_status='POSTED'", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> getswiftvalues(String date);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE TRUNC(TRAN_DATE)=TRUNC(?1)", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> getbalance(Date selectedDate);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE acct_num = ?1 "
			+ "AND tran_id = (SELECT MAX(tran_id) FROM BGLS_TRM_WRK_TRANSACTIONS WHERE acct_num = ?1) AND ROWNUM = 1", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getaedit1(String acct_num);

	@Query(value = "SELECT TRAN_DATE FROM BGLS_TRM_WRK_TRANSACTIONS WHERE ACCT_NUM = ?1 ORDER BY FLOW_DATE DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Object[] getLatestAccountBalanceAndType(String accNum);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE ACCT_NUM = :accountNo ORDER BY tran_id DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getLastTransactionByAccount(@Param("accountNo") String accountNo);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS WHERE ACCT_NUM = :acctNum", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> getAccountDetails(@Param("acctNum") String acctNum);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE TRUNC(TRAN_DATE) = TRUNC(:tranDate) "
			+ "AND TRAN_PARTICULAR LIKE :likePattern " + "AND TRAN_STATUS = 'POSTED' "
			+ "AND PART_TRAN_TYPE = 'Debit' AND ROWNUM = 1", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getInterestRecivable1(@Param("tranDate") Date tranDate,
			@Param("likePattern") String likePattern);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS " + "WHERE TRUNC(TRAN_DATE) = TRUNC(:tranDate) "
			+ "AND TRUNC(VALUE_DATE) = TRUNC(:valueDate) " + "AND TRAN_PARTICULAR LIKE :likePattern "
			+ "AND TRAN_STATUS = 'POSTED' " + "AND PART_TRAN_TYPE = 'Debit' AND ROWNUM = 1", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getInterestRecivable(@Param("tranDate") Date tranDate, @Param("valueDate") Date valueDate,
			@Param("likePattern") String likePattern);

	@Transactional
	@Modifying
	// NOTE: Oracle uses PL/SQL stored procedures for complex operations.
	// This EXEC statement is not directly portable. The equivalent would be a call
	// to a stored procedure or function.
	// Placeholder using a simplified call, assuming 'InsertBranchTransactions' is a
	// stored procedure.
	@Query(value = "{CALL InsertBranchTransactions(:transactions)}", nativeQuery = true)
	void insertBranchTransactions(@Param("transactions") List<Map<String, Object>> transactions);

	@Query(value = "SELECT * " + "FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE TRUNC(VALUE_DATE) = TO_DATE(:valueDate, 'YYYY-MM-DD') " + "AND TRAN_PARTICULAR LIKE :likePattern "
			+ "AND TRAN_STATUS = 'POSTED' " + "AND PART_TRAN_TYPE = 'Debit' "
			+ "AND FLOW_CODE != 'PRDEM'", nativeQuery = true)
	List<TRAN_MAIN_TRM_WRK_ENTITY> getInterestRecivable121(@Param("valueDate") String flowDate,
			@Param("likePattern") String likePattern);

	@Query(value = "SELECT * FROM BGLS_TRM_WRK_TRANSACTIONS "
			+ "WHERE TRAN_ID = :tranId AND PART_TRAN_ID = :partTranId", nativeQuery = true)
	TRAN_MAIN_TRM_WRK_ENTITY getTransactionById(@Param("tranId") String tranId, @Param("partTranId") String partTranId);

//	@Query(value = "select * from bgls_trm_wrk_transactions where flow_code = 'DISBT' AND TRAN_PARTICULAR = 'Loan Disbursed' fetch first 100 rows only", nativeQuery = true)
//	List<TRAN_MAIN_TRM_WRK_ENTITY> getRepaymentDetailsvalue();

//	@Query(value = "select * from bgls_trm_wrk_transactions where flow_code = 'INDEM' AND TRAN_PARTICULAR = 'Interest Applied'", nativeQuery = true)
//	List<TRAN_MAIN_TRM_WRK_ENTITY> getinterestDetailsvalue();
//	
//	@Query(value = "select * from bgls_trm_wrk_transactions where flow_code = 'FEEDEM' AND TRAN_PARTICULAR = 'Fee Charged'", nativeQuery = true)
//	List<TRAN_MAIN_TRM_WRK_ENTITY> getfeesDetailsvalue();
//	
//	@Query(value = "select * from bgls_trm_wrk_transactions where flow_code = 'PENDEM' AND TRAN_PARTICULAR = 'Penalty Charged'", nativeQuery = true)
//	List<TRAN_MAIN_TRM_WRK_ENTITY> getpenaltyDetailsvalue();
//	
//	@Query(value = "select * from bgls_trm_wrk_transactions where tran_particular in ('Principal Recovery','Interest Recovery','Fee Recovery','Penalty Recovery')", nativeQuery = true)
//	List<TRAN_MAIN_TRM_WRK_ENTITY> getrecoveryDetailsvalue();
	
	
			@Query(
					  value = "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'DISBT' AND TRAN_PARTICULAR = 'Loan Disbursed' " +
					          "FETCH FIRST 200 ROWS ONLY",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> getRepaymentDetailsvalue();
			
			@Query(
					  value = "SELECT * FROM ( " +
					          "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'INDEM' AND TRAN_PARTICULAR = 'Interest Applied' " +
					          ") WHERE ROWNUM <= 200",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> getInterestDetailsValue();

					@Query(
					  value = "SELECT * FROM ( " +
					          "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'FEEDEM' AND TRAN_PARTICULAR = 'Fee Charged' " +
					          ") WHERE ROWNUM <= 200",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> getFeesDetailsValue();

					@Query(
					  value = "SELECT * FROM ( " +
					          "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'PENDEM' AND TRAN_PARTICULAR = 'Penalty Charged' " +
					          ") WHERE ROWNUM <= 200",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> getPenaltyDetailsValue();

					@Query(
					  value = "SELECT * FROM ( " +
					          "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE tran_particular IN ('Principal Recovery','Interest Recovery','Fee Recovery','Penalty Recovery') " +
					          ") WHERE ROWNUM <= 200",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> getRecoveryDetailsValue();
					
					
//					// Search by Account Number
//					@Query(value = "SELECT * FROM bgls_trm_wrk_transactions " +
//					               "WHERE flow_code = :flowCode " +
//					               "AND LOWER(acct_num) LIKE '%' || :keyword || '%'",
//					       nativeQuery = true)
//					List<TRAN_MAIN_TRM_WRK_ENTITY> searchByAccountNumber(@Param("keyword") String keyword,
//					                                                     @Param("flowCode") String flowCode);
//
//					// Search by Account Name
//					@Query(value = "SELECT * FROM bgls_trm_wrk_transactions " +
//					               "WHERE flow_code = :flowCode " +
//					               "AND LOWER(acct_name) LIKE '%' || :keyword || '%'",
//					       nativeQuery = true)
//					List<TRAN_MAIN_TRM_WRK_ENTITY> searchByAccountName(@Param("keyword") String keyword,
//					                                                   @Param("flowCode") String flowCode);
					
					// Normal flow queries
					@Query(value = "SELECT * FROM bgls_trm_wrk_transactions " +
					               "WHERE flow_code = :flowCode AND LOWER(acct_num) LIKE '%' || :keyword || '%'", 
					       nativeQuery = true)
					List<TRAN_MAIN_TRM_WRK_ENTITY> searchByAccountNumber(@Param("keyword") String keyword, @Param("flowCode") String flowCode);

					@Query(value = "SELECT * FROM bgls_trm_wrk_transactions " +
					               "WHERE flow_code = :flowCode AND LOWER(acct_name) LIKE '%' || :keyword || '%'", 
					       nativeQuery = true)
					List<TRAN_MAIN_TRM_WRK_ENTITY> searchByAccountName(@Param("keyword") String keyword, @Param("flowCode") String flowCode);

					// Recovery (COLL) tab search
					@Query(value = "SELECT * FROM bgls_trm_wrk_transactions " +
					               "WHERE tran_particular IN ('Principal Recovery','Interest Recovery','Fee Recovery','Penalty Recovery') " +
					               "AND (:filterType = 'Account Number' AND LOWER(acct_num) LIKE '%' || :keyword || '%' OR " +
					               "     :filterType = 'Account Name' AND LOWER(acct_name) LIKE '%' || :keyword || '%') ",
					       nativeQuery = true)
					List<TRAN_MAIN_TRM_WRK_ENTITY> searchRecovery(@Param("keyword") String keyword, @Param("filterType") String filterType);


					// 🟩 Download all Disbursement (Loan Disbursed) records
					@Query(
					  value = "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'DISBT' AND TRAN_PARTICULAR = 'Loan Disbursed'",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> downloadDisbursementDetails();

					// 🟩 Download all Interest Applied records
					@Query(
					  value = "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'INDEM' AND TRAN_PARTICULAR = 'Interest Applied'",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> downloadInterestDetails();

					// 🟩 Download all Fee Charged records
					@Query(
					  value = "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'FEEDEM' AND TRAN_PARTICULAR = 'Fee Charged'",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> downloadFeeDetails();

					// 🟩 Download all Penalty Charged records
					@Query(
					  value = "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE flow_code = 'PENDEM' AND TRAN_PARTICULAR = 'Penalty Charged'",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> downloadPenaltyDetails();

					// 🟩 Download all Recovery records (Principal, Interest, Fee, Penalty)
					@Query(
					  value = "SELECT * FROM bgls_trm_wrk_transactions " +
					          "WHERE tran_particular IN ('Principal Recovery','Interest Recovery','Fee Recovery','Penalty Recovery')",
					  nativeQuery = true
					)
					List<TRAN_MAIN_TRM_WRK_ENTITY> downloadRecoveryDetails();

					
					@Modifying
					@Transactional
					@Query(value = "CALL DAYEND_INTEREST_DEMAND_1(:MIG_DATE, :ENTRY_USER)", nativeQuery = true)
					void runInterestDemand(@Param("MIG_DATE") String MIG_DATE, @Param("ENTRY_USER") String ENTRY_USER);
}