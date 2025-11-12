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
	
//	
//	@Query(value =
//		    "SELECT " +
//		    " b.account_state AS ACCOUNT_STATE, " +
//		    " a.customer_id AS CUSTOMER_ID, " +
//		    " a.customer_name AS CUSTOMER_NAME, " +
//		    " b.id AS ID, " +
//		    " b.total_product_price AS TOTAL_PRODUCT_PRICE, " +
//		    " b.loan_amount AS LOAN_AMOUNT, " +
//		    " b.interest_rate AS INTEREST_RATE, " +
//		    " b.repayment_installments AS REPAYMENT_INSTALLMENTS, " +
//		    " b.principal_balance AS PRINCIPAL_BALANCE, " +
//		    " b.interest_balance AS INTEREST_BALANCE, " +
//		    " b.fees_balance AS FEE_BALANCE, " +
//		    " b.penalty_balance AS PENALTY_BALANCE, " +
//		    " (b.principal_balance + b.interest_balance + b.fees_balance + b.penalty_balance) AS TOTAL_BALANCE, " +
//		    " a.acct_bal AS ACCT_BAL, " +
//		    " b.principal_paid AS PRINCIPAL_PAID, " +
//		    " b.interest_paid AS INTEREST_PAID, " +
//		    " b.fee_paid AS FEE_PAID, " +
//		    " b.penalty_paid AS PENALTY_PAID, " +
//		    " b.days_late AS DAYS_LATE, " +
//		    " SUM(c.principal_paid) AS TOTAL_PRINCIPAL_PAID, " +
//		    " SUM(c.interest_paid) AS TOTAL_INTEREST_PAID, " +
//		    " SUM(c.fee_paid) AS TOTAL_FEE_PAID, " +
//		    " SUM(c.penalty_paid) AS TOTAL_PENALTY_PAID, " +
//		    " c.last_paid_date AS LAST_PAID_DATE, " +
//		    " b.loan_name AS PRODUCT " +
//		    "FROM BGLS_CHART_OF_ACCOUNTS a " +
//		    "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.encoded_key = b.encoded_key " +
//		    "LEFT JOIN LOAN_REPAYMENT_TBL c ON b.encoded_key = c.parent_account_key " +
//		    "WHERE a.own_type = 'C' " +
//		    "GROUP BY b.account_state, a.customer_id, a.customer_name, b.id, " +
//		    " b.total_product_price, b.loan_amount, b.interest_rate, b.repayment_installments, " +
//		    " b.principal_balance, b.interest_balance, b.fees_balance, b.penalty_balance, " +
//		    " b.principal_paid, b.interest_paid, b.fee_paid, b.penalty_paid, b.days_late, a.acct_bal, c.last_paid_date, b.loan_name " +
//		    "ORDER BY b.account_state, b.id",
//		    nativeQuery = true)
//		List<Object[]> findLoanAccountsByDueDate();
		
		
		@Query(value =
			    "SELECT " +
			    "    b.tran_date AS TRDT, " +
			    "    a.acct_num AS ACNO, " +
			    "    a.acct_name AS ACNAME, " +
			    "    INITCAP(b.tran_type) AS TT, " +
			    "    b.tran_id AS TRID, " +
			    "    b.part_tran_id AS PTID, " +
			    "    INITCAP(b.part_tran_type) AS PTT, " +
			    "    DECODE(b.part_tran_type,'Credit',b.tran_amt,0.00) AS CR, " +
			    "    DECODE(b.part_tran_type,'Debit',b.tran_amt,0.00) AS DR, " +
			    "    b.tran_particular AS TP " +
			    "FROM BGLS_CHART_OF_ACCOUNTS a " +
			    "JOIN BGLS_TRM_WRK_TRANSACTIONS b ON a.acct_num = b.acct_num " +
			    "WHERE TO_CHAR(b.tran_date, 'DD-MM-YYYY') = :tranDate " +
			    "ORDER BY b.tran_date, b.tran_id, b.part_tran_id",
			    nativeQuery = true)
			List<Object[]> getTransactionReportByDate(@Param("tranDate") String tranDate);

		    @Query(value =
		            "SELECT " +
		            "    a.gl_desc AS GLDESC, " +
		            "    a.acct_num AS ACNO, " +
		            "    a.acct_name AS ACNAME, " +
		            "    b.opening_bal AS OPBAL, " +
		            "    b.tran_cr_bal AS CRBAL, " +
		            "    b.tran_dr_bal AS DRBAL, " +
		            "    b.tran_tot_net AS NET, " +
		            "    CASE WHEN b.tran_date_bal > 0 THEN b.tran_date_bal ELSE 0 END AS ACCRBAL, " +
		            "    CASE WHEN b.tran_date_bal < 0 THEN b.tran_date_bal ELSE 0 END AS ACDRBAL " +
		            "FROM BGLS_CHART_OF_ACCOUNTS a " +
		            "JOIN BGLS_DAILY_ACCT_BAL b ON a.acct_num = b.acct_num " +
		            "WHERE TO_CHAR(b.tran_date, 'DD-MM-YYYY') = :tranDate " +
		            "AND a.own_type IN ('C', 'O') " +
		            "ORDER BY a.gl_desc, a.acct_num",
		            nativeQuery = true)
		        List<Object[]> getDabReportByDate(@Param("tranDate") String tranDate);
		        
		        
		        @Query(value = "SELECT ACCT_NUM, ACCT_NAME, ACCT_TYPE, DUE_DATE, DAYS_INARREARS, " +
		                   "INTEREST_AMT, ACCRUAL_DATE, LAST_DUE_DATE " +
		                   "FROM LOAN_ACCRUAL_INTEREST_TBL " +
		                   "WHERE TO_CHAR(ACCRUAL_DATE, 'DD-MM-YYYY') = :accrualDate", nativeQuery = true)
		    List<Object[]> findLoanAccrualByDate(@Param("accrualDate") String accrualDate);
		    
		    @Query(
		    	    value = "SELECT " +
		    	            "b.account_state AS ACCOUNT_STATE, " +
		    	            "a.customer_id AS CUSTOMER_ID, " +
		    	            "a.customer_name AS CUSTOMER_NAME, " +
		    	            "b.id AS ID, " +
		    	            "b.total_product_price AS TOTAL_PRODUCT_PRICE, " +
		    	            "b.loan_amount AS LOAN_AMOUNT, " +
		    	            "b.interest_rate AS INTEREST_RATE, " +
		    	            "b.repayment_installments AS REPAYMENT_INSTALLMENTS, " +
		    	            "b.principal_balance AS PRINCIPAL_BALANCE, " +
		    	            "b.interest_balance AS INTEREST_BALANCE, " +
		    	            "b.fees_balance AS FEE_BALANCE, " +
		    	            "b.penalty_balance AS PENALTY_BALANCE, " +
		    	            "(b.principal_balance + b.interest_balance + b.fees_balance + b.penalty_balance) AS TOTAL_BALANCE, " +
		    	            "a.acct_bal AS ACCT_BAL, " +
		    	            "b.principal_paid AS PRINCIPAL_PAID, " +
		    	            "b.interest_paid AS INTEREST_PAID, " +
		    	            "b.fee_paid AS FEE_PAID, " +
		    	            "b.penalty_paid AS PENALTY_PAID, " +
		    	            "b.days_late AS DAYS_LATE, " +
		    	            "NVL(c.total_principal_paid, 0) AS TOTAL_PRINCIPAL_PAID, " +
		    	            "NVL(c.total_interest_paid, 0) AS TOTAL_INTEREST_PAID, " +
		    	            "NVL(c.total_fee_paid, 0) AS TOTAL_FEE_PAID, " +
		    	            "NVL(c.total_penalty_paid, 0) AS TOTAL_PENALTY_PAID, " +
		    	            "c.last_paid_date AS LAST_PAID_DATE, " +
		    	            "b.loan_name AS PRODUCT " +
		    	        "FROM BGLS_CHART_OF_ACCOUNTS a " +
		    	        "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.encoded_key = b.encoded_key " +
		    	        "LEFT JOIN ( " +
		    	        "    SELECT parent_account_key, " +
		    	        "           SUM(principal_paid) AS total_principal_paid, " +
		    	        "           SUM(interest_paid) AS total_interest_paid, " +
		    	        "           SUM(fee_paid) AS total_fee_paid, " +
		    	        "           SUM(penalty_paid) AS total_penalty_paid, " +
		    	        "           MAX(last_paid_date) AS last_paid_date " +
		    	        "    FROM LOAN_REPAYMENT_TBL " +
		    	        "    GROUP BY parent_account_key " +
		    	        ") c ON b.encoded_key = c.parent_account_key " +
		    	        "WHERE a.own_type = 'C' " +
		    	        "ORDER BY b.account_state, b.id",
		    	    nativeQuery = true
		    	)
		    	List<Object[]> findLoanAccountsByDueDate();
		    	
		    	@Query(value = "SELECT b.id, b.LOAN_NAME, a.due_date, a.tran_date, a.no_of_days, a.month_no_of_days, a.penalty_per_day, " +
		                   "a.penalty_per_month, a.tolerance_period, a.up_to_date_penalty, a.penalty_rate " +
		                   "FROM LOAN_DAILY_PENALTY_TBL a " +
		                   "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.encoded_key = b.ENCODED_KEY " +
		                   "WHERE TO_CHAR(a.TRAN_DATE, 'DD-MM-YYYY') = :tranDate", nativeQuery = true)
		    List<Object[]> findLoanDailyPenaltyByDate(@Param("tranDate") String tranDate);
		    
		    
		    
		    @Query(value = 
		    	    "SELECT " +
		    	    "    b.tran_date AS TRDT, " +
		    	    "    a.acct_num AS ACNO, " +
		    	    "    a.acct_name AS ACNAME, " +
		    	    "    INITCAP(b.tran_type) AS TT, " +
		    	    "    b.tran_id AS TRID, " +
		    	    "    b.part_tran_id AS PTID, " +
		    	    "    INITCAP(b.part_tran_type) AS PTT, " +
		    	    "    DECODE(b.part_tran_type, 'Credit', b.tran_amt, 0.00) AS CR, " +
		    	    "    DECODE(b.part_tran_type, 'Debit',  b.tran_amt, 0.00) AS DR, " +
		    	    "    b.tran_particular AS TP " +
		    	    "FROM BGLS_CHART_OF_ACCOUNTS a " +
		    	    "JOIN BGLS_TRM_WRK_TRANSACTIONS b ON a.acct_num = b.acct_num " +
		    	    "WHERE TRUNC(b.tran_date) = TO_DATE(:tranDate, 'DD-MM-YYYY') " +
		    	    "  AND b.FLOW_CODE IN ('INDEM', 'FEEDEM', 'PENDEM') " +
		    	    "ORDER BY b.tran_date, b.tran_id, b.part_tran_id",
		    	    nativeQuery = true)
		    	List<Object[]> getTransactionReport3ByDate(@Param("tranDate") String tranDate);

		    	
		    	@Query(value = 
		    		    "SELECT " +
		    		    "    b.tran_date AS TRDT, " +
		    		    "    a.acct_num AS ACNO, " +
		    		    "    a.acct_name AS ACNAME, " +
		    		    "    INITCAP(b.tran_type) AS TT, " +
		    		    "    b.tran_id AS TRID, " +
		    		    "    b.part_tran_id AS PTID, " +
		    		    "    INITCAP(b.part_tran_type) AS PTT, " +
		    		    "    DECODE(b.part_tran_type, 'Credit', b.tran_amt, 0.00) AS CR, " +
		    		    "    DECODE(b.part_tran_type, 'Debit',  b.tran_amt, 0.00) AS DR, " +
		    		    "    b.tran_particular AS TP " +
		    		    "FROM BGLS_CHART_OF_ACCOUNTS a " +
		    		    "JOIN BGLS_TRM_WRK_TRANSACTIONS b ON a.acct_num = b.acct_num " +
		    		    "WHERE TRUNC(b.tran_date) = TO_DATE(:tranDate, 'DD-MM-YYYY') " +
		    		    "  AND b.FLOW_CODE IN ('RECOVERY' , 'PRREC' , 'INREC', 'FEREC' , 'PLREC', 'EXREC', 'FRECOVERY') " +
		    		    "ORDER BY b.tran_date, b.tran_id, b.part_tran_id",
		    		    nativeQuery = true)
		    		List<Object[]> getTransactionReport2ByDate(@Param("tranDate") String tranDate);

			@Query(value = "SELECT " + "b.id AS ID, " + "b.loan_name AS LOAN_NAME "
					+ "FROM BGLS_CHART_OF_ACCOUNTS_DEMO1 a "
					+ "JOIN LOAN_ACCOUNT_MASTER_TBL_DEMO1 b ON a.encoded_key = b.encoded_key "
					+ "LEFT JOIN LOAN_REPAYMENT_TBL_DEMO1 c ON b.encoded_key = c.parent_account_key "
					+ "WHERE a.own_type = 'C' "
					+ "AND (b.principal_balance + b.interest_balance + b.fees_balance + b.penalty_balance) - ABS(a.acct_bal) <> 0 "
					+ "GROUP BY b.id, b.loan_name, b.principal_balance, b.interest_balance, b.fees_balance, b.penalty_balance, a.acct_bal "
					+ "ORDER BY b.id", nativeQuery = true)
			List<Object[]> getMismatchedAccounts();
			
			@Query(value = "SELECT DISTINCT " + "b.principal_balance, " + "b.interest_balance, " + "b.fees_balance, "
					+ "b.penalty_balance, "
					+ "(b.principal_balance + b.interest_balance + b.fees_balance + b.penalty_balance) AS total_balance "
					+ "FROM BGLS_CHART_OF_ACCOUNTS_DEMO1 a "
					+ "JOIN LOAN_ACCOUNT_MASTER_TBL_DEMO1 b ON a.encoded_key = b.encoded_key "
					+ "LEFT JOIN LOAN_REPAYMENT_TBL_DEMO1 c ON b.encoded_key = c.parent_account_key "
					+ "WHERE a.own_type = 'C' AND a.acct_num = :acctNum", nativeQuery = true)
			List<Object[]> getAccountBalanceDetails(@Param("acctNum") String acctNum);

}