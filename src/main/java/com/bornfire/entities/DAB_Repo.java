package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DAB_Repo extends CrudRepository<DAB_Entity, String> {

	@Query(value = "SELECT * FROM BGLS_DAILY_ACCT_BAL WHERE ACCT_NUM IN (:accountNumbers)", nativeQuery = true)
	List<DAB_Entity> get_transaction_acc_num(@Param("accountNumbers") List<String> accountNumbers);

	/*
	 * Original SQL Server: DATEADD(day, -1, CAST(?2 AS DATE))
	 * Oracle Equivalent: ?2 - 1 (Subtracting 1 from a date results in the previous day)
	 */
	@Query(value = "SELECT TRAN_DATE_BAL FROM BGLS_DAILY_ACCT_BAL WHERE ACCT_NUM = ?1 AND (?2 - 1) BETWEEN TRAN_DATE AND END_TRAN_DATE", nativeQuery = true)
	BigDecimal getTranDateBAlance(String acct_num, String fromdateref);

	@Query(value = "SELECT gl_desc AS primary_gl_desc, gl_code, gl_desc AS secondary_gl_desc, glsh_code, glsh_desc, COUNT(GLSH_CODE) as sum, acct_crncy, "
			+ "SUM(CASE WHEN tran_date_bal > 0 THEN tran_date_bal ELSE 0 END) AS cr_amt, "
			+ "SUM(CASE WHEN tran_date_bal < 0 THEN ABS(tran_date_bal) ELSE 0 END) AS dr_amt "
			+ "FROM BGLS_DAILY_ACCT_BAL " + "WHERE :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE "
			+ "GROUP BY gl_desc, gl_code, glsh_code, glsh_desc, acct_crncy ORDER BY GLSH_CODE ASC", nativeQuery = true)
	List<Object[]> getbalance1(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT gl_desc AS primary_gl_desc, " + "       gl_code, " + "       glsh_code, "
			+ "       gl_desc AS secondary_gl_desc, " + "       COUNT(GLSH_CODE) AS sum, " + "       acct_crncy, "
			+ "       SUM(CASE WHEN tran_date_bal > 0 THEN tran_date_bal ELSE 0 END) AS total_credit, "
			+ "       SUM(CASE WHEN tran_date_bal < 0 THEN ABS (tran_date_bal) ELSE 0 END) AS total_debit "
			+ " FROM BGLS_DAILY_ACCT_BAL " + " WHERE :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE "
			+ " GROUP BY gl_desc, gl_code, glsh_code, acct_crncy " + " ORDER BY glsh_code ASC", nativeQuery = true)
	List<Object[]> getbalance2(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT gl_desc AS primary_gl_desc, " + "gl_code, " + "gl_desc, " + "glsh_code, "
			+ "gl_desc AS secondary_gl_desc, " + "COUNT(GLSH_CODE) AS sum, " + "acct_crncy, "
			+ "CASE WHEN SUM(tran_date_bal) > 0 THEN SUM(tran_date_bal) ELSE 0 END AS credit, "
			+ "CASE WHEN SUM(tran_date_bal) < 0 THEN ABS(SUM(tran_date_bal)) ELSE 0 END AS debit "
			+ "FROM BGLS_DAILY_ACCT_BAL " + "WHERE :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE "
			+ "GROUP BY gl_desc, gl_code, glsh_code, acct_crncy " + "ORDER BY glsh_code ASC", nativeQuery = true)
	List<Object[]> getbalance(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT ACCT_NUM, acct_name, glsh_code, "
            + "SUM(CASE WHEN tran_date_bal > 0 THEN tran_date_bal ELSE 0 END) AS total_credit, "
            + "SUM(CASE WHEN tran_date_bal < 0 THEN ABS(tran_date_bal) ELSE 0 END) AS total_debit "
            + "FROM BGLS_DAILY_ACCT_BAL "
            + "WHERE TO_DATE(:formattedDate, 'YYYY-MM-DD') BETWEEN tran_date AND end_tran_date AND glsh_code = :glshCode "
            + "GROUP BY ACCT_NUM, acct_name, glsh_code", nativeQuery = true)
	List<Object[]> getAccountBalancesByGlshCode(@Param("formattedDate") String formattedDate, @Param("glshCode") String glshCode);

	@Query(value = "SELECT GLSH_CODE, GLSH_DESC, COUNT(GLSH_CODE) as sum, acct_crncy, SUM(tran_date_bal) FROM BGLS_DAILY_ACCT_BAL WHERE gl_desc='Asset' AND :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE GROUP BY GLSH_CODE, GLSH_DESC, acct_crncy ORDER BY GLSH_CODE ASC", nativeQuery = true)
	List<Object[]> getfilteredrec(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT GLSH_CODE, GLSH_DESC, COUNT(GLSH_CODE) as sum, acct_crncy, SUM(tran_date_bal) FROM BGLS_DAILY_ACCT_BAL WHERE gl_desc='Liability' AND :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE GROUP BY GLSH_CODE, GLSH_DESC, acct_crncy ORDER BY GLSH_CODE ASC", nativeQuery = true)
	List<Object[]> getfilteredrec1(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT * FROM BGLS_DAILY_ACCT_BAL WHERE gl_desc='Income' AND :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE", nativeQuery = true)
	List<DAB_Entity> getfilteredrec2(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT * FROM BGLS_DAILY_ACCT_BAL WHERE gl_desc='Expense' AND :balancedate BETWEEN TRAN_DATE AND END_TRAN_DATE", nativeQuery = true)
	List<DAB_Entity> getfilteredrec3(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT SUM(TRAN_CR_BAL) as TRANAMT, SUM(TRAN_DR_BAL) as TRANAMT1 FROM BGLS_DAILY_ACCT_BAL", nativeQuery = true)
	Object[] getcheck3();

	// Removed \r\n and adjusted JOIN logic slightly for readability (though original join is fine)
	@Query(value = "SELECT a.customer_id, a.loan_accountno, a.customer_name, a.date_of_loan, a.loan_sanctioned, b.TRAN_DATE_BAL "
			+ "FROM Loan_AccountMaster a, BGLS_DAILY_ACCT_BAL b "
			+ "WHERE b.ACCT_NUM = a.Loan_AccountNo AND :date_of_loan BETWEEN TRAN_DATE AND END_TRAN_DATE "
			+ "ORDER BY a.loan_accountno", nativeQuery = true)
	List<Object[]> getLeaseBal(@Param("date_of_loan") Date date_of_loan);

	// Removed \r\n and adjusted JOIN logic slightly for readability
	@Query(value = "SELECT a.cust_id, a.depo_actno, a.cust_name, a.deposit_date, a.deposit_amt, a.deposit_period, a.rate_of_int, a.maturity_date, b.TRAN_DATE_BAL "
			+ "FROM Loan_AccountMaster a, BGLS_DAILY_ACCT_BAL b "
			+ "WHERE b.ACCT_NUM = a.depo_actno AND :deposit_period BETWEEN TRAN_DATE AND END_TRAN_DATE", nativeQuery = true)
	List<Object[]> getDepositBal(@Param("deposit_period") Date deposit_period);

    @Query(value = " SELECT  TRAN_DATE, " +
            "       ACCT_NUM," +
            "       ACCT_NAME," +
            "       TRAN_DR_BAL," +
            "       TRAN_CR_BAL," +
            "       TRAN_TOT_NET" +
            " FROM bgls_daily_acct_bal " +
            " WHERE TRUNC(TRAN_DATE) = TO_DATE(:dueDate, 'DD-MM-YYYY') ",nativeQuery = true)
    List<Object[]> findTranDate(@Param("dueDate") String dueDate);

    // Dynamic search by column and keyword
    @Query(value = "SELECT DISTINCT acct_num, acct_name FROM dab_demo " +
            "WHERE (:columnName = 'acct_num' AND LOWER(acct_num) LIKE LOWER('%' || :keyword || '%')) " +
            "   OR (:columnName = 'acct_name' AND LOWER(acct_name) LIKE LOWER('%' || :keyword || '%'))",
            nativeQuery = true)
    List<Object[]> searchDabAcctList(@Param("columnName") String columnName, @Param("keyword") String keyword);

    @Query(value = "SELECT * FROM BGLS_DAILY_ACCT_BAL FETCH FIRST 200 ROWS ONLY", nativeQuery = true)
    List<DAB_Entity> dabdata();
    
    
    @Query(value = "BEGIN PROC_UPDATE_DAILY_BALANCE(:tranDate); END;", nativeQuery = true)
    int UpdateDab(@Param("tranDate")Date tranDate);


}