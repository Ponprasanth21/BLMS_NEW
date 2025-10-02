package com.bornfire.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LOAN_REPAYMENT_REPO extends JpaRepository<LOAN_REPAYMENT_ENTITY, String> {
	@Query(value = "SELECT B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'INDEM' AS flow_code, "
			+ "       B.INTEREST_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, "
			+ "       A.LOAN_NAME AS acct_name, " + // ✅ Already included
			"       B.ENCODED_KEY AS encoded_key " + // ✅ Added encoded_key here
			"FROM LOAN_ACCOUNT_MASTER_TBL A " + "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE BETWEEN ?1 AND ?2 " + "AND B.PARENT_ACCOUNT_KEY = ?3 "
			+ "AND B.payment_state = 'PENDING' " + "AND B.due_date > '2025-01-17' " + // ✅ Hardcoded date filter
			"ORDER BY B.due_date", nativeQuery = true)
	List<Object[]> getloanflows(Date fromDate, Date toDate, String accountNum);

	@Query(value = "SELECT " + "    U.due_date,  " + "    CASE "
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '1' "
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2' " + "        WHEN U.flow_type = 'FEE_EXP' THEN '3' "
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4' " + "    END AS flow_id, " + "    CASE "
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM' "
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM' "
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM' "
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY' " + "    END AS flow_code, " + "    U.flow_amt, "
			+ "    U.loan_acct_no, " + "    U.acct_name, " + "    U.encoded_key " + "FROM ( " + "    SELECT "
			+ "        B.due_date,  " + "        B.PRINCIPAL_EXP, " + "        B.INTEREST_EXP, " + "        B.FEE_EXP, "
			+ "        B.PENALTY_EXP, " + "        A.ID AS loan_acct_no, " + "        A.LOAN_NAME AS acct_name, "
			+ "        A.ENCODED_KEY " + "    FROM LOAN_ACCOUNT_MASTER_TBL A " + "    JOIN LOAN_REPAYMENT_TBL B "
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " + "    WHERE B.DUE_DATE BETWEEN :fromDate AND :toDate "
			+ "      AND B.PARENT_ACCOUNT_KEY = :accountNum " + "      AND B.payment_state = 'PENDING' " + ") P "
			+ "UNPIVOT ( " + "    flow_amt FOR flow_type IN ( " + "        PRINCIPAL_EXP AS 'PRINCIPAL_EXP', "
			+ "        INTEREST_EXP AS 'INTEREST_EXP', " + "        FEE_EXP AS 'FEE_EXP', "
			+ "        PENALTY_EXP AS 'PENALTY_EXP' " + "    ) " + ") U " + "WHERE U.flow_amt > 0 "
			+ "ORDER BY U.due_date, flow_id", nativeQuery = true)
	List<Object[]> getloanflowsvalue(Date fromDate, Date toDate, String accountNum);

	@Query(value = "SELECT TOP 1 B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'INDEM' AS flow_code, "
			+ "       B.INTEREST_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, "
			+ "       A.LOAN_NAME AS acct_name, " + "       A.INTEREST_RATE AS interest_rate "
			+ "FROM LOAN_ACCOUNT_MASTER_TBL A " + "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE > ?1 " + // strictly after passed date
			"AND B.PARENT_ACCOUNT_KEY = ?2 " + "AND B.payment_state = 'PENDING' "
			+ "ORDER BY B.due_date ASC", nativeQuery = true)
	List<Object[]> getNextPendingFlow(Date fromDate, String accountNum);

	@Query(value = "SELECT TOP 1 B.DUE_DATE, B.INTEREST_DUE, A.ID, A.LOAN_NAME " + "FROM LOAN_REPAYMENT_TBL B "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL A ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE A.ID = ?1 AND B.PAYMENT_STATE = 'PAID' " + "ORDER BY B.DUE_DATE DESC", nativeQuery = true)
	List<Object[]> getLastPaidDueDate(String accountNum);

	@Query(value = "select * from LOAN_REPAYMENT_TBL where PARENT_ACCOUNT_KEY = ?1 and DUE_DATE = ?2", nativeQuery = true)
	LOAN_REPAYMENT_ENTITY getLoanFlowsValueDatas(String accountNum, String flowDate);

	@Query(value = "SELECT B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'FEEDEM' AS flow_code, "
			+ "       B.FEE_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, " + "       A.LOAN_NAME AS acct_name, "
			+ "       A.ENCODED_KEY AS encoded_key " + "FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.FEE_DUE > 0 AND B.DUE_DATE BETWEEN ?1 AND ?2 " + "AND B.PARENT_ACCOUNT_KEY = ?3 "
			+ "AND B.payment_state = 'PENDING' " + "ORDER BY B.due_date", nativeQuery = true)
	List<Object[]> getloanflowsdatas(Date fromDate, Date toDate, String accountNum);

	@Query(value = "select * from LOAN_REPAYMENT_TBL where PARENT_ACCOUNT_KEY = ?1 AND payment_state ='PENDING'", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatasVALUES(String accountNum);

	@Query(value = "SELECT " + "    U.due_date, " + "    CASE " + "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '1' "
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2' " + "        WHEN U.flow_type = 'FEE_EXP' THEN '3' "
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4' " + "    END AS flow_id, " + "    CASE "
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM' "
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM' "
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM' "
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY' " + "    END AS flow_code, " + "    U.flow_amt, "
			+ "    U.loan_acct_no, " + "    U.acct_name, " + "    U.encoded_key " + "FROM ( " + "    SELECT "
			+ "        B.due_date, " + "        B.PRINCIPAL_EXP, " + "        B.INTEREST_EXP, " + "        B.FEE_EXP, "
			+ "        B.PENALTY_EXP, " + "        A.ID AS loan_acct_no, " + "        A.LOAN_NAME AS acct_name, "
			+ "        A.ENCODED_KEY " + "    FROM LOAN_ACCOUNT_MASTER_TBL A " + "    JOIN LOAN_REPAYMENT_TBL B "
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " + "    WHERE B.PARENT_ACCOUNT_KEY = :accountNum "
			+ "      AND B.payment_state = 'PENDING' " + ") P " + "UNPIVOT ( " + "    flow_amt FOR flow_type IN ( "
			+ "        PRINCIPAL_EXP AS 'PRINCIPAL_EXP', " + "        INTEREST_EXP AS 'INTEREST_EXP', "
			+ "        FEE_EXP AS 'FEE_EXP', " + "        PENALTY_EXP AS 'PENALTY_EXP' " + "    ) " + ") U "
			+ "WHERE U.flow_amt > 0 " + "ORDER BY U.due_date, flow_id", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas(String accountNum);

	@Query(value = "SELECT B.due_date, '1' AS flow_id, 'FEEDEM' AS flow_code, (B.FEE_EXP - B.FEE_PAID) AS flow_amt, "
			+ "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " + "FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE <= :toDate AND A.ID = :accountNum AND (B.FEE_EXP - B.FEE_PAID) > 0 "
			+ "UNION ALL "
			+ "SELECT B.due_date, '2' AS flow_id, 'INDEM' AS flow_code, (B.INTEREST_EXP - B.INTEREST_PAID) AS flow_amt, "
			+ "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " + "FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE <= :toDate AND A.ID = :accountNum AND (B.INTEREST_EXP - B.INTEREST_PAID) > 0 "
			+ "UNION ALL "
			+ "SELECT B.due_date, '3' AS flow_id, 'PRDEM' AS flow_code, (B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS flow_amt, "
			+ "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " + "FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE <= :toDate AND A.ID = :accountNum AND (B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) > 0 "
			+ "UNION ALL "
			+ "SELECT B.due_date, '4' AS flow_id, 'P' AS flow_code, (B.PENALTY_EXP - B.PENALTY_PAID) AS flow_amt, "
			+ "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " + "FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE <= :toDate AND A.ID = :accountNum AND (B.PENALTY_EXP - B.PENALTY_PAID) > 0 "
			+ "ORDER BY due_date, flow_id", nativeQuery = true)
	List<Object[]> getloanflowsvaluedats(Date toDate, String accountNum);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = :accountNum AND DUE_DATE IN (:flowDates)", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas1(@Param("accountNum") String accountNum,
			@Param("flowDates") List<Timestamp> formattedFlowDates);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = ?1 AND DUE_DATE =?2", nativeQuery = true)
	LOAN_REPAYMENT_ENTITY getLoanFlowsValueDatas11(String encodedkey, String flow_date);

	@Query(value = "SELECT " + "    U.due_date, " + "    CASE "
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN '1'         " + // Fees First
			"        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'    " + // Interest Second
			"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '3'   " + // Principal Third
			"        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'     " + // Penalty Last
			"    END AS flow_id, " + "    CASE " + "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM' "
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM' "
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM' "
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY' " + "    END AS flow_code, " + "    U.flow_amt, "
			+ "    U.loan_acct_no, " + "    U.acct_name, " + "    U.encoded_key " + "FROM ( " + "    SELECT "
			+ "        B.due_date, " + "        A.ID AS loan_acct_no, " + "        A.LOAN_NAME AS acct_name, "
			+ "        A.ENCODED_KEY, " + "        (B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRINCIPAL_EXP, "
			+ "        (B.INTEREST_EXP - B.INTEREST_PAID) AS INTEREST_EXP, "
			+ "        (B.FEE_EXP - B.FEE_PAID) AS FEE_EXP, "
			+ "        (B.PENALTY_EXP - B.PENALTY_PAID) AS PENALTY_EXP " + "    FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "    JOIN LOAN_REPAYMENT_TBL B " + "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "    WHERE B.PARENT_ACCOUNT_KEY = :accountNum " + "      AND B.payment_state = 'PENDING' " + ") P "
			+ "UNPIVOT ( " + "    flow_amt FOR flow_type IN ( " + "        PRINCIPAL_EXP AS 'PRINCIPAL_EXP', "
			+ "        INTEREST_EXP AS 'INTEREST_EXP', " + "        FEE_EXP AS 'FEE_EXP', "
			+ "        PENALTY_EXP AS 'PENALTY_EXP' " + "    ) " + ") U " + "WHERE U.flow_amt > 0 "
			+ "ORDER BY U.due_date, " + "         CASE " + "            WHEN U.flow_type = 'FEE_EXP' THEN 1 "
			+ "            WHEN U.flow_type = 'INTEREST_EXP' THEN 2 "
			+ "            WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 3 "
			+ "            WHEN U.flow_type = 'PENALTY_EXP' THEN 4 " + "         END", nativeQuery = true)
	List<Object[]> getloanupdateList(@Param("accountNum") String accountNum);

	@Query(value = "WITH ControlDate AS ( " + "    SELECT MAX(TRAN_DATE) AS TRAN_DATE " + "    FROM BGLS_CONTROL_TABLE "
			+ "), " + "LoanDataBefore AS ( " + "    SELECT  " + "        B.DUE_DATE,  "
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL,  "
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL,  "
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL, " + "        A.ID AS loan_acct_no, "
			+ "        A.LOAN_NAME AS acct_name, " + "        A.ENCODED_KEY AS encoded_key "
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A " + "    JOIN LOAN_REPAYMENT_TBL B "
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " + "    CROSS JOIN ControlDate C "
			+ "    WHERE B.DUE_DATE < C.TRAN_DATE " + "      AND B.PARENT_ACCOUNT_KEY = :accountNum "
			+ "      AND B.payment_state = 'PENDING' " + "      AND B.DEL_FLG = 'N' "
			+ "    GROUP BY B.DUE_DATE, A.ID, A.LOAN_NAME, A.ENCODED_KEY " + "), " + "LoanDataAfter AS ( "
			+ "    SELECT  " + "        C.TRAN_DATE AS DUE_DATE,  "
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL,  "
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL,  "
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL, " + "        A.ID AS loan_acct_no, "
			+ "        A.LOAN_NAME AS acct_name, " + "        A.ENCODED_KEY AS encoded_key "
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A " + "    JOIN LOAN_REPAYMENT_TBL B "
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " + "    CROSS JOIN ControlDate C "
			+ "    WHERE B.DUE_DATE > C.TRAN_DATE " + "      AND B.PARENT_ACCOUNT_KEY = :accountNum "
			+ "      AND B.DEL_FLG = 'N' " + "    GROUP BY C.TRAN_DATE, A.ID, A.LOAN_NAME, A.ENCODED_KEY " + ") "
			+ "SELECT * FROM ( "
			+ "    SELECT LD.due_date, '3' AS flow_id, 'PRDEM' AS flow_code, LD.PRDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataBefore LD WHERE LD.PRDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.due_date, '2' AS flow_id, 'INDEM' AS flow_code, LD.INDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataBefore LD WHERE LD.INDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.due_date, '1' AS flow_id, 'FEEDEM' AS flow_code, LD.FEEDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataBefore LD WHERE LD.FEEDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.DUE_DATE, '3' AS flow_id, 'PRDEM' AS flow_code, LD.PRDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataAfter LD WHERE LD.PRDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.DUE_DATE, '2' AS flow_id, 'INDEM' AS flow_code, LD.INDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataAfter LD WHERE LD.INDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.DUE_DATE, '1' AS flow_id, 'FEEDEM' AS flow_code, LD.FEEDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataAfter LD WHERE LD.FEEDEM_TOTAL > 0 " + ") FinalResult "
			+ "ORDER BY due_date, TO_NUMBER(flow_id) ", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas511(@Param("accountNum") String accountNum);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = :accountNum AND PAYMENT_STATE ='PENDING'", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas21(@Param("accountNum") String accountNum);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = ?1 AND  PAYMENT_STATE IN ('PARTIALLY PAID' ,'PENDING') AND DUE_DATE >= ?2", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas31(String accountNum, LocalDate tranDate);

	@Query(value = "WITH ControlDate AS ( " + "    SELECT MAX(TRAN_DATE) AS TRAN_DATE " + "    FROM BGLS_CONTROL_TABLE "
			+ "), " + "LoanDataBefore AS ( " + "    SELECT " + "        B.DUE_DATE, "
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL, "
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL, "
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL, " + "        A.ID AS loan_acct_no, "
			+ "        A.LOAN_NAME AS acct_name, " + "        A.ENCODED_KEY AS encoded_key "
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A " + "    JOIN LOAN_REPAYMENT_TBL B "
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " + "    CROSS JOIN ControlDate C "
			+ "    WHERE B.DUE_DATE < C.TRAN_DATE " + "      AND B.PARENT_ACCOUNT_KEY = :accountNum "
			+ "      AND B.payment_state = 'PENDING' " + "    GROUP BY B.DUE_DATE, A.ID, A.LOAN_NAME, A.ENCODED_KEY "
			+ "), " + "LoanDataAfter AS ( " + "    SELECT "
			+ "        (SELECT TRAN_DATE FROM ControlDate) AS DUE_DATE, "
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL, "
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL, "
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL, " + "        A.ID AS loan_acct_no, "
			+ "        A.LOAN_NAME AS acct_name, " + "        A.ENCODED_KEY AS encoded_key "
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A " + "    JOIN LOAN_REPAYMENT_TBL B "
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " + "    CROSS JOIN ControlDate C "
			+ "    WHERE B.DUE_DATE >= C.TRAN_DATE " + "      AND B.PARENT_ACCOUNT_KEY = :accountNum "
			+ "    GROUP BY A.ID, A.LOAN_NAME, A.ENCODED_KEY " + ") " + "SELECT * FROM ( "
			+ "    SELECT LD.due_date, '3' AS flow_id, 'PRDEM' AS flow_code, LD.PRDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataBefore LD WHERE LD.PRDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.due_date, '2' AS flow_id, 'INDEM' AS flow_code, LD.INDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataBefore LD WHERE LD.INDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.due_date, '1' AS flow_id, 'FEEDEM' AS flow_code, LD.FEEDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataBefore LD WHERE LD.FEEDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.DUE_DATE, '3' AS flow_id, 'PRDEM' AS flow_code, LD.PRDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataAfter LD WHERE LD.PRDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.DUE_DATE, '2' AS flow_id, 'INDEM' AS flow_code, LD.INDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataAfter LD WHERE LD.INDEM_TOTAL > 0 " + "    UNION ALL "
			+ "    SELECT LD.DUE_DATE, '1' AS flow_id, 'FEEDEM' AS flow_code, LD.FEEDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key "
			+ "    FROM LoanDataAfter LD WHERE LD.FEEDEM_TOTAL > 0 " + ") FinalResult "
			+ "ORDER BY due_date, TO_NUMBER(flow_id)", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas5112(@Param("accountNum") String accountNum);

	@Query(value = "SELECT MAX(due_date) AS previous_due_date \r\n" + "FROM LOAN_REPAYMENT_TBL \r\n"
			+ "WHERE due_date < (\r\n" + "    SELECT MIN(due_date) \r\n" + "    FROM LOAN_REPAYMENT_TBL \r\n"
			+ "    WHERE due_date >= ?1\r\n" + "    AND PARENT_ACCOUNT_KEY = ?2\r\n" + ")\r\n"
			+ "AND PARENT_ACCOUNT_KEY = ?2\r\n" + "", nativeQuery = true)
	Date findPreviousDueDate(String dueDate, String encodedKey);

	@Query(value = "SELECT B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'INDEM' AS flow_code, "
			+ "       B.INTEREST_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, "
			+ "       A.LOAN_NAME AS acct_name, " + "       B.PARENT_ACCOUNT_KEY AS encoded_key "
			+ "FROM LOAN_ACCOUNT_MASTER_TBL A " + "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.interest_due > 0 AND B.DUE_DATE BETWEEN ?1 AND ?2 " + "AND B.PARENT_ACCOUNT_KEY = ?3 "
			+ "AND B.payment_state = 'PENDING' " + "ORDER BY B.due_date", nativeQuery = true)
	List<Object[]> getloanflowsdata(Date fromDate, Date toDate, String accountNum);

	@Query(value = "SELECT lr.*, lam.ID, lam.LOAN_NAME " + "FROM LOAN_REPAYMENT_TBL lr "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL lam ON lr.PARENT_ACCOUNT_KEY = lam.ENCODED_KEY "
			+ "WHERE lr.PARENT_ACCOUNT_KEY IN :keys " + "AND lr.DUE_DATE <= :date " + "AND lr.FEE_EXP > 0 "
			+ "AND lr.INTEREST_EXP > 0 " + "AND lr.PRINCIPAL_EXP > 0 " + "ORDER BY lr.DUE_DATE", nativeQuery = true)
	List<Object[]> findByParentAccountKeyInAndDueDateLessThanEqual(@Param("keys") List<String> keys,
			@Param("date") java.sql.Date date);

	@Query(value = "SELECT REPAID_DATE FROM LOAN_REPAYMENT_TBL WHERE DUE_DATE IN :dates ORDER BY DUE_DATE", nativeQuery = true)
	List<Date> findRepaidDatesForMultipleDates(@Param("dates") List<Date> dates);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY IN :encodedKeys and repaid_date is not null ORDER BY DUE_DATE", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> findRepaidDates(@Param("encodedKeys") List<String> encodedKeys);

	@Query(value = "select * from LOAN_REPAYMENT_TBL where due_date <= ?1 AND PAYMENT_STATE IN ('PENDING','PARTIALLY_PAID') ORDER BY due_date", nativeQuery = true)
	List<Object[]> getLoanActDetval41(Date creation_date);

	@Query(value = "SELECT lr.*, lam.ID, lam.LOAN_NAME " + "FROM LOAN_REPAYMENT_TBL lr "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL lam ON lr.PARENT_ACCOUNT_KEY = lam.ENCODED_KEY "
			+ "WHERE lr.PARENT_ACCOUNT_KEY IN :keys " + "AND lr.DUE_DATE <= :date " + "AND lr.FEE_EXP > 0 "
			+ "AND lr.INTEREST_EXP > 0 " + "AND lr.PRINCIPAL_EXP > 0 " + "AND lr.INTEREST_PAID = lr.INTEREST_EXP "
			+ "AND lr.PRINCIPAL_PAID = lr.PRINCIPAL_EXP " + "AND lr.FEE_PAID = lr.FEE_EXP "
			+ "ORDER BY lr.DUE_DATE", nativeQuery = true)
	List<Object[]> findByParentAccountKeyInAndDueDateLessThanEqualpaid(@Param("keys") List<String> keys,
			@Param("date") Date date);

	@Query(value = "SELECT \r\n" + "    lr.*,\r\n" + "    lam.ID,\r\n" + "    lam.LOAN_NAME\r\n"
			+ "FROM LOAN_REPAYMENT_TBL lr\r\n" + "JOIN LOAN_ACCOUNT_MASTER_TBL lam\r\n"
			+ "    ON lr.PARENT_ACCOUNT_KEY = lam.ENCODED_KEY\r\n" + "WHERE lr.DUE_DATE > CAST(?1 AS DATE)\r\n"
			+ "  AND lr.DUE_DATE <= DATEADD(MONTH, 1, CAST(?1 AS DATE))\r\n"
			+ "  AND lr.PAYMENT_STATE IN ('PENDING','PARTIALLY_PAID')\r\n" + "ORDER BY lr.DUE_DATE", nativeQuery = true)
	List<Object[]> findByParentAccountKeyInAndDueDateLessThanEqualdue(@Param("date") Date date);

	@Query(value = "select * from LOAN_REPAYMENT_TBL WHERE DEL_FLG ='N' and encoded_key =?1", nativeQuery = true)
	LOAN_REPAYMENT_ENTITY getid(String id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = "delete from LOAN_REPAYMENT_TBL where encoded_key IN (:encoded_key)", nativeQuery = true)
	int delteid(@Param("encoded_key") List<String> encoded_key);
	

	@Modifying
    @Transactional
    @Query( value =  "INSERT INTO LOAN_REPAYMENT_TBL (   " +
           " ENCODED_KEY,  " +                  
           " ASSIGNED_BRANCH_KEY,  " +  
           " ASSIGNED_USER_KEY,  " +  
           " DUE_DATE,  " +                       
           " INTEREST_DUE,  " +                   
           " INTEREST_PAID,  " +                  
           " LAST_PAID_DATE,  " +                 
           " LAST_PENALTY_APPLIED_DATE,  " +  
           " NOTES,  " +  
           " PARENT_ACCOUNT_KEY,  " +            
           " PRINCIPAL_DUE,  " +                 
           " PRINCIPAL_PAID,  " +                
           " REPAID_DATE,  " +                   
           " PAYMENT_STATE,  " +                 
           " ASSIGNED_CENTRE_KEY,  " +  
           " FEE_DUE,  " +                        
           " FEE_PAID,  " +                       
           " PENALTY_DUE,  " +                    
           " PENALTY_PAID,  " +                   
           " TAX_INTEREST_DUE,  " +  
           " TAX_INTEREST_PAID,  " +  
           " TAX_FEES_DUE,  " +  
           " TAX_FEES_PAID,  " +  
           " TAX_PENALTY_DUE,  " +  
           " TAX_PENALTY_PAID,  " +  
           " ORGANIZATION_COMMISSION_DUE,  " +  
           " FUNDERS_INTEREST_DUE,  " +  
           " CREATIONDATE,  " +                     
           " LASTMODIFIEDDATE,  " +                  
           " ADDITIONS,  " +
           " DEL_FLG " +
           " )   " +
            " SELECT    " +
            " ENCODED_KEY,  " +                  
            " ASSIGNED_BRANCH_KEY,  " +  
            " ASSIGNED_USER_KEY,  " +  
            " CASE WHEN REGEXP_LIKE(DUE_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(DUE_DATE,'YYYY-MM-DD') ELSE NULL END," +
            " CASE WHEN REGEXP_LIKE(TRIM(INTEREST_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(INTEREST_DUE) ELSE NULL END,   " +
            " CASE WHEN REGEXP_LIKE(TRIM(INTEREST_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(INTEREST_PAID) ELSE NULL END,   " +
            " CASE WHEN REGEXP_LIKE(LAST_PAID_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(LAST_PAID_DATE,'YYYY-MM-DD') ELSE NULL END,    " +
            " CASE WHEN REGEXP_LIKE(LAST_PENALTY_APPLIED_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(LAST_PENALTY_APPLIED_DATE,'YYYY-MM-DD') ELSE NULL END,    " +             
            " NOTES,  " +  
            " PARENT_ACCOUNT_KEY,  " +   
            " CASE WHEN REGEXP_LIKE(TRIM(PRINCIPAL_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(PRINCIPAL_DUE) ELSE NULL END,   " +         
            " CASE WHEN REGEXP_LIKE(TRIM(PRINCIPAL_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(PRINCIPAL_PAID) ELSE NULL END,   " +         
            " CASE WHEN REGEXP_LIKE(REPAID_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(REPAID_DATE,'YYYY-MM-DD') ELSE NULL END,    " +
            " PAYMENT_STATE,  " +                 
            " ASSIGNED_CENTRE_KEY,  " +  
            " CASE WHEN REGEXP_LIKE(TRIM(FEE_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(FEE_DUE) ELSE NULL END,   " +         
            " CASE WHEN REGEXP_LIKE(TRIM(FEE_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(FEE_PAID) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(PENALTY_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(PENALTY_DUE) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(PENALTY_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(PENALTY_PAID) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(TAX_INTEREST_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(TAX_INTEREST_DUE) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(TAX_INTEREST_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(TAX_INTEREST_PAID) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(TAX_FEES_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(TAX_FEES_DUE) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(TAX_FEES_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(TAX_FEES_PAID) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(TAX_PENALTY_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(TAX_PENALTY_DUE) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(TAX_PENALTY_PAID), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(TAX_PENALTY_PAID) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(ORGANIZATION_COMMISSION_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(ORGANIZATION_COMMISSION_DUE) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(TRIM(FUNDERS_INTEREST_DUE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(FUNDERS_INTEREST_DUE) ELSE NULL END,   " +                          
            " CASE WHEN REGEXP_LIKE(CREATIONDATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(CREATIONDATE,'YYYY-MM-DD') ELSE NULL END,    " +
            " CASE WHEN REGEXP_LIKE(LASTMODIFIEDDATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(LASTMODIFIEDDATE,'YYYY-MM-DD') ELSE NULL END,    " +        
            " ADDITIONS,  " +
            " 'N' " +
            " FROM LOAN_REPAYMENT_TBL_UPLOAD s "+
            " WHERE NOT EXISTS ( " +
	    		"     SELECT 1 " +
	    		"     FROM LOAN_REPAYMENT_TBL m " +
	    		"     WHERE m.PARENT_ACCOUNT_KEY = s.PARENT_ACCOUNT_KEY " +
	    		" ) " 
            , nativeQuery = true )
			int LoanRepaymnetCopyTempTableToMainTable();
	
			@Modifying
			@Transactional
			@Query(value = "DELETE FROM LOAN_REPAYMENT_TBL_UPLOAD", nativeQuery = true)
			int LoanRepaymetTempTableDelete();
			
			@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = :accountNum AND DUE_DATE IN (:flowDates)", nativeQuery = true)
			List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas21(@Param("accountNum") String accountNum,
					@Param("flowDates") String dueDate1);

}
