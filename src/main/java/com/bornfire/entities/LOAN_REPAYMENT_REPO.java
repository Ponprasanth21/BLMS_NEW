package com.bornfire.entities;

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

	@Query(value = "SELECT \r\n" + "    U.due_date,  \r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '1'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN '3'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'\r\n" + "    END AS flow_id,\r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + "    END AS flow_code,\r\n"
			+ "    U.flow_amt,\r\n" + "    U.loan_acct_no AS loan_acct_no,\r\n" + "    U.acct_name AS acct_name,\r\n"
			+ "    U.encoded_key \r\n" + "FROM \r\n" + "(\r\n" + "    SELECT \r\n" + "        B.due_date,  \r\n"
			+ "        B.PRINCIPAL_EXP,\r\n" + "        B.INTEREST_EXP,\r\n" + "        B.FEE_EXP,\r\n"
			+ "        B.PENALTY_EXP,\r\n" + "        A.ID AS loan_acct_no,              \r\n"
			+ "        A.LOAN_NAME AS acct_name,  \r\n" + "        A.ENCODED_KEY \r\n"
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + "    JOIN LOAN_REPAYMENT_TBL B \r\n"
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + "    WHERE \r\n"
			+ "        B.DUE_DATE BETWEEN :fromDate AND :toDate \r\n"
			+ "        AND B.PARENT_ACCOUNT_KEY = :accountNum\r\n" + "        AND B.payment_state = 'PENDING'\r\n"
			+ ") AS P\r\n" + "UNPIVOT \r\n" + "(\r\n"
			+ "    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + ") AS U\r\n"
			+ "WHERE U.flow_amt > 0 \r\n" + "ORDER BY U.due_date, flow_id", nativeQuery = true)
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

	@Query(value = "SELECT \r\n" + "    U.due_date,  \r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '1'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN '3'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'\r\n" + "    END AS flow_id,\r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + "    END AS flow_code,\r\n"
			+ "    U.flow_amt,\r\n" + "    U.loan_acct_no AS loan_acct_no,\r\n" + "    U.acct_name AS acct_name,\r\n"
			+ "    U.encoded_key \r\n" + "FROM \r\n" + "(\r\n" + "    SELECT \r\n" + "        B.due_date,  \r\n"
			+ "        B.PRINCIPAL_EXP,\r\n" + "        B.INTEREST_EXP,\r\n" + "        B.FEE_EXP,\r\n"
			+ "        B.PENALTY_EXP,\r\n" + "        A.ID AS loan_acct_no,              \r\n"
			+ "        A.LOAN_NAME AS acct_name,  \r\n" + "        A.ENCODED_KEY \r\n"
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + "    JOIN LOAN_REPAYMENT_TBL B \r\n"
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + "    WHERE \r\n"
			+ "       B.PARENT_ACCOUNT_KEY = :accountNum\r\n" + "        AND B.payment_state = 'PENDING'\r\n"
			+ ") AS P\r\n" + "UNPIVOT \r\n" + "(\r\n"
			+ "    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + ") AS U\r\n"
			+ "WHERE U.flow_amt > 0 \r\n" + "ORDER BY U.due_date, flow_id", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas(String accountNum);

	@Query(value = "SELECT \r\n" + "    U.due_date,  \r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN '1'         -- Fees First\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'    -- Interest Second\r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '3'   -- Principal Third\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'     -- Penalty Last\r\n" + "    END AS flow_id,\r\n"
			+ "    CASE \r\n" + "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + "    END AS flow_code,\r\n"
			+ "    U.flow_amt,\r\n" + "    U.loan_acct_no,\r\n" + "    U.acct_name,\r\n" + "    U.encoded_key \r\n"
			+ "FROM (\r\n" + "    SELECT \r\n" + "        B.due_date,  \r\n"
			+ "        A.ID AS loan_acct_no,              \r\n" + "        A.LOAN_NAME AS acct_name,  \r\n"
			+ "        A.ENCODED_KEY,\r\n" + "        -- Calculate remaining due amount\r\n"
			+ "        B.PRINCIPAL_EXP - B.PRINCIPAL_PAID AS PRINCIPAL_EXP,\r\n"
			+ "        B.INTEREST_EXP - B.INTEREST_PAID AS INTEREST_EXP,\r\n"
			+ "        B.FEE_EXP - B.FEE_PAID AS FEE_EXP,\r\n"
			+ "        B.PENALTY_EXP - B.PENALTY_PAID AS PENALTY_EXP\r\n" + "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n"
			+ "    JOIN LOAN_REPAYMENT_TBL B \r\n" + "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n"
			+ "    WHERE \r\n" + "        B.DUE_DATE <= ?1  -- Replace with actual date\r\n"
			+ "        AND B.PARENT_ACCOUNT_KEY = ?2 -- Replace with actual account number\r\n"
			+ "        AND B.payment_state = 'PENDING'\r\n" + ") P\r\n" + "UNPIVOT (\r\n"
			+ "    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + ") AS U\r\n"
			+ "-- Only show amounts where remaining due > 0\r\n" + "WHERE U.flow_amt > 0  \r\n"
			+ "ORDER BY U.due_date, \r\n" + "         CASE \r\n" + "            WHEN U.flow_type = 'FEE_EXP' THEN 1\r\n"
			+ "            WHEN U.flow_type = 'INTEREST_EXP' THEN 2\r\n"
			+ "            WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 3\r\n"
			+ "            WHEN U.flow_type = 'PENALTY_EXP' THEN 4\r\n" + "         END\r\n" + "", nativeQuery = true)
	List<Object[]> getloanflowsvaluedats(Date toDate, String accountNum);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = :accountNum AND DUE_DATE IN (:flowDates)", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas1(@Param("accountNum") String accountNum,
			@Param("flowDates") List<String> flowDates);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = ?1 AND DUE_DATE =?2", nativeQuery = true)
	LOAN_REPAYMENT_ENTITY getLoanFlowsValueDatas11(String encodedkey, String flow_date);

	@Query(value = "SELECT\r\n" + //
			"    U.due_date,\r\n" + //
			"    CASE\r\n" + //
			"        WHEN U.flow_type = 'FEE_EXP' THEN '1'         -- Fees First\r\n" + //
			"        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'    -- Interest Second\r\n" + //
			"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '3'   -- Principal Third\r\n" + //
			"        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'     -- Penalty Last\r\n" + //
			"    END AS flow_id,\r\n" + //
			"    CASE\r\n" + //
			"        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n" + //
			"        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n" + //
			"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n" + //
			"        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + //
			"    END AS flow_code,\r\n" + //
			"    U.flow_amt,\r\n" + //
			"    U.loan_acct_no,\r\n" + //
			"    U.acct_name,\r\n" + //
			"    U.encoded_key\r\n" + //
			"FROM (\r\n" + //
			"    SELECT\r\n" + //
			"        B.due_date,\r\n" + //
			"        A.ID AS loan_acct_no,\r\n" + //
			"        A.LOAN_NAME AS acct_name,\r\n" + //
			"        A.ENCODED_KEY,\r\n" + //
			"        -- Calculate remaining due amount\r\n" + //
			"        B.PRINCIPAL_EXP - B.PRINCIPAL_PAID AS PRINCIPAL_EXP,\r\n" + //
			"        B.INTEREST_EXP - B.INTEREST_PAID AS INTEREST_EXP,\r\n" + //
			"        B.FEE_EXP - B.FEE_PAID AS FEE_EXP,\r\n" + //
			"        B.PENALTY_EXP - B.PENALTY_PAID AS PENALTY_EXP\r\n" + //
			"    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + //
			"    JOIN LOAN_REPAYMENT_TBL B\r\n" + //
			"        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + //
			"    WHERE\r\n" + //
			"         B.PARENT_ACCOUNT_KEY = ?1 \r\n" + //
			"        AND B.payment_state = 'PENDING'\r\n" + //
			") P\r\n" + //
			"UNPIVOT (\r\n" + //
			"    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + //
			") AS U\r\n" + //
			"-- Only show amounts where remaining due > 0\r\n" + //
			"WHERE U.flow_amt > 0\r\n" + //
			"ORDER BY U.due_date,\r\n" + //
			"         CASE\r\n" + //
			"            WHEN U.flow_type = 'FEE_EXP' THEN 1\r\n" + //
			"            WHEN U.flow_type = 'INTEREST_EXP' THEN 2\r\n" + //
			"            WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 3\r\n" + //
			"            WHEN U.flow_type = 'PENALTY_EXP' THEN 4\r\n" + //
			"         END\r\n" + //
			"", nativeQuery = true)
	List<Object[]> getloanupdateList(String accountNum);

	@Query(value = "WITH ControlDate AS (\r\n" + "    -- Fetch the latest TRAN_DATE from BGLS_CONTROL_TABLE\r\n"
			+ "    SELECT MAX(TRAN_DATE) AS TRAN_DATE \r\n" + "    FROM BGLS_CONTROL_TABLE\r\n" + "),\r\n"
			+ "LoanDataBefore AS (\r\n" + "    -- Get all rows before the control date (Show individually)\r\n"
			+ "    SELECT  \r\n" + "        B.DUE_DATE,  \r\n"
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL,  \r\n"
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL,  \r\n"
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL,\r\n"
			+ "        A.ID AS loan_acct_no,              \r\n" + "        A.LOAN_NAME AS acct_name,  \r\n"
			+ "        A.ENCODED_KEY AS encoded_key \r\n" + "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n"
			+ "    JOIN LOAN_REPAYMENT_TBL B \r\n" + "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n"
			+ "    CROSS JOIN ControlDate C\r\n" + "    WHERE B.DUE_DATE < C.TRAN_DATE  -- Dynamic Date Condition\r\n"
			+ "        AND B.PARENT_ACCOUNT_KEY = ?1\r\n" + "        AND B.payment_state = 'PENDING'\r\n"
			+ "        AND B.DEL_FLG = 'N' -- New condition added\r\n"
			+ "    GROUP BY B.DUE_DATE, A.ID, A.LOAN_NAME, A.ENCODED_KEY\r\n" + "),\r\n" + "LoanDataAfter AS (\r\n"
			+ "    -- Summarize all transactions after the control date and set DUE_DATE = Control Date\r\n"
			+ "    SELECT  \r\n" + "        C.TRAN_DATE AS DUE_DATE,  -- Fetch Date Dynamically\r\n"
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL,  \r\n"
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL,  \r\n"
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL,\r\n"
			+ "        A.ID AS loan_acct_no,              \r\n" + "        A.LOAN_NAME AS acct_name,  \r\n"
			+ "        A.ENCODED_KEY AS encoded_key \r\n" + "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n"
			+ "    JOIN LOAN_REPAYMENT_TBL B \r\n" + "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n"
			+ "    CROSS JOIN ControlDate C\r\n" + "    WHERE B.DUE_DATE > C.TRAN_DATE  -- Dynamic Date Condition\r\n"
			+ "        AND B.PARENT_ACCOUNT_KEY = ?1\r\n" + "        AND B.DEL_FLG = 'N' -- New condition added\r\n"
			+ "    GROUP BY C.TRAN_DATE, A.ID, A.LOAN_NAME, A.ENCODED_KEY\r\n" + ")\r\n" + "\r\n"
			+ "SELECT * FROM (\r\n" + "    -- UNION ALL for PRDEM, INDEM, FEEDEM flows\r\n"
			+ "    SELECT LD.due_date, '3' AS flow_id, 'PRDEM' AS flow_code, LD.PRDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key\r\n"
			+ "    FROM LoanDataBefore LD WHERE LD.PRDEM_TOTAL > 0\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT LD.due_date, '2' AS flow_id, 'INDEM' AS flow_code, LD.INDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key\r\n"
			+ "    FROM LoanDataBefore LD WHERE LD.INDEM_TOTAL > 0\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT LD.due_date, '1' AS flow_id, 'FEEDEM' AS flow_code, LD.FEEDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key\r\n"
			+ "    FROM LoanDataBefore LD WHERE LD.FEEDEM_TOTAL > 0\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT LD.DUE_DATE, '3' AS flow_id, 'PRDEM' AS flow_code, LD.PRDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key\r\n"
			+ "    FROM LoanDataAfter LD WHERE LD.PRDEM_TOTAL > 0\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT LD.DUE_DATE, '2' AS flow_id, 'INDEM' AS flow_code, LD.INDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key\r\n"
			+ "    FROM LoanDataAfter LD WHERE LD.INDEM_TOTAL > 0\r\n" + "\r\n" + "    UNION ALL\r\n" + "\r\n"
			+ "    SELECT LD.DUE_DATE, '1' AS flow_id, 'FEEDEM' AS flow_code, LD.FEEDEM_TOTAL AS total_amount, LD.loan_acct_no, LD.acct_name, LD.encoded_key\r\n"
			+ "    FROM LoanDataAfter LD WHERE LD.FEEDEM_TOTAL > 0\r\n" + ") AS FinalResult\r\n"
			+ "ORDER BY due_date, CAST(flow_id AS INT)\r\n" + "" + "", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas511(String accountNum);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = :accountNum AND PAYMENT_STATE ='PENDING'", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas21(@Param("accountNum") String accountNum);

	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = ?1 AND  PAYMENT_STATE IN ('PARTIALLY PAID' ,'PENDING') AND DUE_DATE >= ?2", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas31(String accountNum, LocalDate tranDate);

	@Query(value = "WITH ControlDate AS (\r\n" + "    -- Fetch the latest TRAN_DATE from BGLS_CONTROL_TABLE\r\n"
			+ "    SELECT MAX(TRAN_DATE) AS TRAN_DATE FROM BGLS_CONTROL_TABLE\r\n" + "),\r\n"
			+ "LoanDataBefore AS (\r\n" + "    -- Get all rows before the control date (Show individually)\r\n"
			+ "    SELECT  \r\n" + "        B.DUE_DATE,  \r\n"
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL,  \r\n"
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL,  \r\n"
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL,\r\n"
			+ "        A.ID AS loan_acct_no,              \r\n" + "        A.LOAN_NAME AS acct_name,  \r\n"
			+ "        A.ENCODED_KEY AS encoded_key \r\n" + "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n"
			+ "    JOIN LOAN_REPAYMENT_TBL B \r\n" + "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n"
			+ "    JOIN ControlDate C\r\n" + "        ON B.DUE_DATE < C.TRAN_DATE  -- Dynamic Date Condition\r\n"
			+ "    WHERE \r\n" + "        B.PARENT_ACCOUNT_KEY = ?1\r\n" + "        AND B.payment_state = 'PENDING'\r\n"
			+ "    GROUP BY B.DUE_DATE, A.ID, A.LOAN_NAME, A.ENCODED_KEY\r\n" + "),\r\n" + "LoanDataAfter AS (\r\n"
			+ "    -- Summarize all transactions after the control date and set DUE_DATE = Control Date\r\n"
			+ "    SELECT  \r\n"
			+ "        (SELECT TRAN_DATE FROM ControlDate) AS DUE_DATE,  -- Fetch Date Dynamically\r\n"
			+ "        SUM(B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS PRDEM_TOTAL,  \r\n"
			+ "        MAX(B.INTEREST_EXP - B.INTEREST_PAID) AS INDEM_TOTAL,  \r\n"
			+ "        MAX(B.FEE_EXP - B.FEE_PAID) AS FEEDEM_TOTAL,\r\n"
			+ "        A.ID AS loan_acct_no,              \r\n" + "        A.LOAN_NAME AS acct_name,  \r\n"
			+ "        A.ENCODED_KEY AS encoded_key \r\n" + "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n"
			+ "    JOIN LOAN_REPAYMENT_TBL B \r\n" + "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n"
			+ "    JOIN ControlDate C\r\n" + "        ON B.DUE_DATE >= C.TRAN_DATE  -- Dynamic Date Condition\r\n"
			+ "    WHERE \r\n" + "        B.PARENT_ACCOUNT_KEY = ?1\r\n"
			+ "    GROUP BY A.ID, A.LOAN_NAME, A.ENCODED_KEY\r\n" + ")\r\n" + "SELECT \r\n" + "    LD.due_date,\r\n"
			+ "    '3' AS flow_id,\r\n" + "    'PRDEM' AS flow_code,\r\n" + "    LD.PRDEM_TOTAL AS total_amount,\r\n"
			+ "    LD.loan_acct_no,\r\n" + "    LD.acct_name,\r\n" + "    LD.encoded_key\r\n"
			+ "FROM LoanDataBefore LD\r\n" + "WHERE LD.PRDEM_TOTAL > 0  -- Exclude rows where PRDEM_TOTAL = 0\r\n"
			+ "\r\n" + "UNION ALL\r\n" + "\r\n" + "SELECT \r\n" + "    LD.due_date,\r\n" + "    '2' AS flow_id,\r\n"
			+ "    'INDEM' AS flow_code,\r\n" + "    LD.INDEM_TOTAL AS total_amount,\r\n" + "    LD.loan_acct_no,\r\n"
			+ "    LD.acct_name,\r\n" + "    LD.encoded_key\r\n" + "FROM LoanDataBefore LD\r\n"
			+ "WHERE LD.INDEM_TOTAL > 0  -- Exclude rows where INDEM_TOTAL = 0\r\n" + "\r\n" + "UNION ALL\r\n" + "\r\n"
			+ "SELECT \r\n" + "    LD.due_date,\r\n" + "    '1' AS flow_id,\r\n" + "    'FEEDEM' AS flow_code,\r\n"
			+ "    LD.FEEDEM_TOTAL AS total_amount,\r\n" + "    LD.loan_acct_no,\r\n" + "    LD.acct_name,\r\n"
			+ "    LD.encoded_key\r\n" + "FROM LoanDataBefore LD\r\n"
			+ "WHERE LD.FEEDEM_TOTAL > 0  -- Exclude rows where FEEDEM_TOTAL = 0\r\n" + "\r\n" + "UNION ALL\r\n"
			+ "\r\n"
			+ "-- After the control date, show only 3 summarized rows with fixed DUE_DATE from Control Table\r\n"
			+ "SELECT \r\n" + "    LD.DUE_DATE,\r\n" + "    '3' AS flow_id,\r\n" + "    'PRDEM' AS flow_code,\r\n"
			+ "    LD.PRDEM_TOTAL AS total_amount,\r\n" + "    LD.loan_acct_no,\r\n" + "    LD.acct_name,\r\n"
			+ "    LD.encoded_key\r\n" + "FROM LoanDataAfter LD\r\n"
			+ "WHERE LD.PRDEM_TOTAL > 0  -- Exclude rows where PRDEM_TOTAL = 0\r\n" + "\r\n" + "UNION ALL\r\n" + "\r\n"
			+ "SELECT \r\n" + "    LD.DUE_DATE,\r\n" + "    '2' AS flow_id,\r\n" + "    'INDEM' AS flow_code,\r\n"
			+ "    LD.INDEM_TOTAL AS total_amount,\r\n" + "    LD.loan_acct_no,\r\n" + "    LD.acct_name,\r\n"
			+ "    LD.encoded_key\r\n" + "FROM LoanDataAfter LD\r\n"
			+ "WHERE LD.INDEM_TOTAL > 0  -- Exclude rows where INDEM_TOTAL = 0\r\n" + "\r\n" + "UNION ALL\r\n" + "\r\n"
			+ "SELECT \r\n" + "    LD.DUE_DATE,\r\n" + "    '1' AS flow_id,\r\n" + "    'FEEDEM' AS flow_code,\r\n"
			+ "    LD.FEEDEM_TOTAL AS total_amount,\r\n" + "    LD.loan_acct_no,\r\n" + "    LD.acct_name,\r\n"
			+ "    LD.encoded_key\r\n" + "FROM LoanDataAfter LD\r\n"
			+ "WHERE LD.FEEDEM_TOTAL > 0  -- Exclude rows where FEEDEM_TOTAL = 0\r\n" + "\r\n"
			+ "ORDER BY due_date, flow_id\r\n" + "", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas5112(String accountNum);

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
			@Param("date") Date date);

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
	LOAN_REPAYMENT_ENTITY  getid(String id);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
 	@Transactional
 	@Query(value = "delete from LOAN_REPAYMENT_TBL where encoded_key IN (:encoded_key)", nativeQuery = true)
 	int delteid(@Param("encoded_key") List<String> encoded_key);

}
