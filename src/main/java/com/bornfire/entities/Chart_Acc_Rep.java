package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Chart_Acc_Rep extends JpaRepository<Chart_Acc_Entity, String> {

	@Query(value = "SELECT SUM(TO_NUMBER(total_balance)) " + "FROM BGLS_GL_WORK "
			+ "WHERE TO_NUMBER(total_balance) < 0", nativeQuery = true)
	String getacctbaldebit();

	@Query(value = "SELECT SUM(TO_NUMBER(total_balance)) " + "FROM BGLS_GL_WORK "
			+ "WHERE TO_NUMBER(total_balance) > 0", nativeQuery = true)
	String getacctbalcredit();

	@Query(value = "SELECT SUM(ACCT_BAL) FROM BGLS_CHART_OF_ACCOUNTS WHERE ACCT_BAL < 0", nativeQuery = true)
	String getGLbaldebit();

	@Query(value = "SELECT SUM(ACCT_BAL) FROM BGLS_CHART_OF_ACCOUNTS WHERE ACCT_BAL > 0", nativeQuery = true)
	String getGLbalcredit();

	@Query(value = "SELECT COUNT(ACCT_BAL) AS ACCT_BAL, SUM(ACCT_BAL) AS SUM_BAL FROM BGLS_CHART_OF_ACCOUNTS WHERE ACCT_CLS_FLG='N'", nativeQuery = true)
	Object[] getaccbalance();

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE acct_num IN (:accountNumbers)", nativeQuery = true)
	List<Chart_Acc_Entity> getcoaaccunt_num(@Param("accountNumbers") List<String> accountNumbers);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' and OWN_TYPE = 'C' ORDER BY ACCT_NUM, CLASSIFICATION ASC", nativeQuery = true)
	List<Chart_Acc_Entity> getList();

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' AND own_type ='O' ORDER BY ACCT_NUM, CLASSIFICATION ASC", nativeQuery = true)
	List<Chart_Acc_Entity> getListoffice();

	@Query(value = "SELECT " + "a.gl_code, " + "a.ACCT_NAME, " + "SUM(b.TRAN_DATE_BAL) AS opening_bal, "
			+ "SUM(a.CR_AMT) AS credit, " + "SUM(a.DR_AMT) AS debit, "
			+ "ABS(SUM(a.DR_AMT) - SUM(a.CR_AMT)) AS net_change, "
			+ "ABS((SUM(a.DR_AMT) - SUM(a.CR_AMT)) + SUM(b.TRAN_DATE_BAL)) AS closing_bal "
			+ "FROM BGLS_CHART_OF_ACCOUNTS a " + "JOIN BGLS_DAILY_ACCT_BAL b ON a.ACCT_NUM = b.ACCT_NUM "
			+ "WHERE a.OWN_TYPE IN ('O', 'M') " + "GROUP BY a.gl_code, a.ACCT_NAME "
			+ "ORDER BY a.ACCT_NAME", nativeQuery = true)
	List<Object[]> getListtrail();
	
	@Query(value = "SELECT " + "a.gl_code, " + "a.acct_name, " + "SUM(b.tran_date_bal) AS opening_bal, "
			+ "SUM(a.cr_amt) AS credit, " + "SUM(a.dr_amt) AS debit, "
			+ "ABS(SUM(a.dr_amt) - SUM(a.cr_amt)) AS net_change, "
			+ "ABS(SUM(b.tran_date_bal) + (SUM(a.dr_amt) - SUM(a.cr_amt))) AS closing_bal "
			+ "FROM bgls_chart_of_accounts a " + "JOIN bgls_daily_acct_bal b ON a.ACCT_NUM = b.ACCT_NUM "
			+ "WHERE :balancedate BETWEEN b.tran_date AND b.end_tran_date " + "AND a.own_type IN ('O', 'M') "
			+ "GROUP BY a.gl_code, a.acct_name " + "ORDER BY a.acct_name", nativeQuery = true)
	List<Object[]> getListtraildate(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT GLSH_CODE, GLSH_DESC, COUNT(GLSH_CODE) as sum, acct_crncy, SUM(ACCT_BAL) FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' AND OWN_TYPE IN ('O','M') AND classification='Asset' GROUP BY GLSH_CODE, GLSH_DESC, acct_crncy ORDER BY GLSH_CODE ASC", nativeQuery = true)
	List<Object[]> getList1();

	@Query(value = "SELECT GLSH_CODE, GLSH_DESC, COUNT(GLSH_CODE) as sum, acct_crncy, SUM(ACCT_BAL) FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' AND classification='Liability' GROUP BY GLSH_CODE, GLSH_DESC, acct_crncy ORDER BY GLSH_CODE ASC", nativeQuery = true)
	List<Object[]> getList2();

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' AND classification='Income' ORDER BY CLASSIFICATION ASC", nativeQuery = true)
	List<Chart_Acc_Entity> getList3();

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' AND classification='Expenses' ORDER BY CLASSIFICATION ASC", nativeQuery = true)
	List<Chart_Acc_Entity> getList4();

	// NOTE: BETWEEN logic for date comparison is the same in Oracle
	@Query(value = "SELECT * FROM BGLS_DAILY_ACCT_BAL WHERE TRUNC(:balancedate) BETWEEN TRUNC(TRAN_DATE) AND TRUNC(END_TRAN_DATE)", nativeQuery = true)
	List<Chart_Acc_Entity> getfilteredrec(@Param("balancedate") Date balancedate);

	@Query(value = "SELECT ACCT_NUM FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N'", nativeQuery = true)
	List<String> getexistingData();

	@Query(value = "SELECT acct_bal FROM BGLS_CHART_OF_ACCOUNTS WHERE acct_num =?1", nativeQuery = true)
	BigDecimal getacctbal(String acct_num);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE acct_num =?1", nativeQuery = true)
	Chart_Acc_Entity getaedit(String acct_num);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS aa WHERE aa.classification =?3 AND aa.gl_code =?1 AND aa.acct_num = ?2 ", nativeQuery = true)
	Chart_Acc_Entity getValuepop(String gl_code, String acct_num, String classification);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS aa WHERE aa.acct_num = ?1 ", nativeQuery = true)
	List<Chart_Acc_Entity> getValuepoplist(String acct_num);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS ORDER BY acct_num ", nativeQuery = true)
	List<Chart_Acc_Entity> getlistpopup();

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE acct_num = ?1 ", nativeQuery = true)
	Chart_Acc_Entity getlistpopupvalues(String acct_num);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS aa WHERE aa.acct_num = ?1", nativeQuery = true)
	Chart_Acc_Entity getValuepopval(String acct_num);

	@Query(value = "SELECT acct_bal FROM BGLS_CHART_OF_ACCOUNTS aa WHERE aa.acct_num = ?1", nativeQuery = true)
	BigDecimal getbal(String acct_num);

	@Query(value = "SELECT SUM(CR_AMT) as TRANAMT, SUM(DR_AMT) as TRANAMT1 FROM BGLS_CHART_OF_ACCOUNTS WHERE OWN_TYPE IN('C','O') ", nativeQuery = true)
	Object[] getcheck2();

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE GLSH_CODE=?1", nativeQuery = true)
	List<Chart_Acc_Entity> getglsh(String glshCode);

	@Query(value = "SELECT DISTINCT GLSH_CODE, CLASSIFICATION, GL_DESC, ACCT_CRNCY, GL_CODE, GLSH_DESC, "
			+ "COUNT(GLSH_CODE) AS total_count, " + "SUM(CASE WHEN acct_bal > 0 THEN acct_bal ELSE 0 END) AS cr_amt, "
			+ "SUM(CASE WHEN acct_bal < 0 THEN ABS(acct_bal) ELSE 0 END) AS dr_amt " + "FROM BGLS_CHART_OF_ACCOUNTS "
			+ "WHERE del_flg = 'N' AND OWN_TYPE IN ('O','M')" + "GROUP BY GLSH_CODE, CLASSIFICATION, GL_DESC, ACCT_CRNCY, GL_CODE, GLSH_DESC "
			+ "ORDER BY GLSH_CODE ASC", nativeQuery = true)
	Object[] getglcode();

	@Query(value = "SELECT MODIFY_TIME FROM BGLS_CHART_OF_ACCOUNTS WHERE acct_num=?1 AND TRUNC(MODIFY_TIME)=TRUNC(?2)", nativeQuery = true)
	Object getaccnum(String acct_num, Date MODIFY_TIME);

	@Query(value = "SELECT ACCT_BAL FROM BGLS_CHART_OF_ACCOUNTS WHERE ACCT_NUM=?1", nativeQuery = true)
	Object getaccbal(String acc_num);

	@Query(value = "SELECT acct_partition FROM COA WHERE ACCT_NUM=?1", nativeQuery = true)
	String getpartitionFlag(String accountNum);

	@Query(value = "SELECT add_det_flg FROM COA WHERE ACCT_NUM=?1", nativeQuery = true)
	String getpointingDetail(String accountNum);

	@Query(value = "SELECT * FROM BGLS_CHART_OF_ACCOUNTS WHERE del_flg='N' AND ACCT_NUM =?1 ", nativeQuery = true)
	String getloanbal(String accountNum);

	@Query(value = "SELECT ACCT_BAL FROM BGLS_CHART_OF_ACCOUNTS WHERE ACCT_NUM=?1", nativeQuery = true)
	BigDecimal getaccountbal(String acc_num);

	@Query(value = "SELECT * " + "FROM BGLS_CHART_OF_ACCOUNTS " + "WHERE del_flg = 'N' "
			+ "  AND OWN_TYPE IN ('O', 'M') " + // <-- include 'M'
			"ORDER BY ACCT_NUM, CLASSIFICATION ASC", nativeQuery = true)
	List<Chart_Acc_Entity> getListoffice1();

	@Query(value = "SELECT * " + "FROM BGLS_CHART_OF_ACCOUNTS " + "WHERE del_flg = 'N' " + "  AND OWN_TYPE IN ('C') "
			+ "ORDER BY ACCT_NUM, CLASSIFICATION ASC", nativeQuery = true)
	List<Chart_Acc_Entity> getListofCustomer();
	
	
	@Query(value =
		    "SELECT " +
		    " b.account_state AS ACCOUNT_STATE, " +
		    " a.customer_id AS CUSTOMER_ID, " +
		    " a.customer_name AS CUSTOMER_NAME, " +
		    " b.id AS ID, " +
		    " b.total_product_price AS TOTAL_PRODUCT_PRICE, " +
		    " b.loan_amount AS LOAN_AMOUNT, " +
		    " b.interest_rate AS INTEREST_RATE, " +
		    " b.repayment_installments AS REPAYMENT_INSTALLMENTS, " +
		    " b.principal_balance AS PRINCIPAL_BALANCE, " +
		    " b.interest_balance AS INTEREST_BALANCE, " +
		    " b.fees_balance AS FEE_BALANCE, " +
		    " b.penalty_balance AS PENALTY_BALANCE, " +
		    " (b.principal_balance + b.interest_balance + b.fees_balance + b.penalty_balance) AS TOTAL_BALANCE, " +
		    " a.acct_bal AS ACCT_BAL, " +
		    " b.principal_paid AS PRINCIPAL_PAID, " +
		    " b.interest_paid AS INTEREST_PAID, " +
		    " b.fee_paid AS FEE_PAID, " +
		    " b.penalty_paid AS PENALTY_PAID, " +
		    " b.days_late AS DAYS_LATE, " +
		    " SUM(c.principal_paid) AS TOTAL_PRINCIPAL_PAID, " +
		    " SUM(c.interest_paid) AS TOTAL_INTEREST_PAID, " +
		    " SUM(c.fee_paid) AS TOTAL_FEE_PAID, " +
		    " SUM(c.penalty_paid) AS TOTAL_PENALTY_PAID " +
		    "FROM BGLS_CHART_OF_ACCOUNTS a " +
		    "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.encoded_key = b.encoded_key " +
		    "LEFT JOIN LOAN_REPAYMENT_TBL c ON b.encoded_key = c.parent_account_key " +
		    "WHERE a.own_type = 'C' " +
		    "GROUP BY b.account_state, a.customer_id, a.customer_name, b.id, " +
		    " b.total_product_price, b.loan_amount, b.interest_rate, b.repayment_installments, " +
		    " b.principal_balance, b.interest_balance, b.fees_balance, b.penalty_balance, " +
		    " b.principal_paid, b.interest_paid, b.fee_paid, b.penalty_paid, b.days_late, a.acct_bal " +
		    "ORDER BY b.account_state, b.id",
		    nativeQuery = true)
		List<Object[]> findLoanAccountsByDueDate();



}