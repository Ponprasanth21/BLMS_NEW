package com.bornfire.entities;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LOAN_ACT_MST_REPO extends JpaRepository<LOAN_ACT_MST_ENTITY, String> {
//	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL", nativeQuery = true)
    @Query(value = "SELECT *\r\n"
    		+ "FROM LOAN_ACCOUNT_MASTER_TBL\r\n"
    		+ "FETCH FIRST 100 ROWS ONLY", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDet();

	@Query(value = "SELECT * \r\n" + "FROM LOAN_ACCOUNT_MASTER_TBL \r\n" + "WHERE ENCODED_KEY IN (\r\n"
			+ "    SELECT PARENT_ACCOUNT_KEY \r\n" + "    FROM LOAN_REPAYMENT_TBL \r\n"
			+ "    WHERE PARENT_ACCOUNT_KEY IS NOT NULL\r\n" + ")", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActScd();

	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	LOAN_ACT_MST_ENTITY getLoanView(String id);

	@Query(value = "SELECT a.CUSTOMER_ID FROM CLIENT_MASTER_TBL a "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY "
			+ "WHERE b.ACCOUNT_HOLDERKEY = ?1", nativeQuery = true)
	List<String> getLoanValue(String holderKey);

	@Query(value = "WITH ZeroBalanceDue AS ( " +
            "    SELECT DUE_DATE AS LAST_ZERO_BAL_DATE " +
            "    FROM LOAN_REPAYMENT_TBL " +
            "    WHERE PARENT_ACCOUNT_KEY = :encodedKey " +
            "      AND (PRINCIPAL_EXP - PRINCIPAL_PAID = 0 " +
            "       AND INTEREST_EXP - INTEREST_PAID = 0 " +
            "       AND FEE_EXP - FEE_PAID = 0 " +
            "       AND PENALTY_EXP - PENALTY_PAID = 0) " +
            "      AND DUE_DATE < :tran_date " +
            "    ORDER BY DUE_DATE DESC " +
            "    FETCH FIRST 1 ROWS ONLY " +
            "), " +
            "NextDueDate AS ( " +
            "    SELECT DUE_DATE AS NEXT_DUE_DATE " +
            "    FROM LOAN_REPAYMENT_TBL " +
            "    WHERE PARENT_ACCOUNT_KEY = :encodedKey " +
            "      AND DUE_DATE > (SELECT LAST_ZERO_BAL_DATE FROM ZeroBalanceDue) " +
            "    ORDER BY DUE_DATE ASC " +
            "    FETCH FIRST 1 ROWS ONLY " +
            ") " +
            "SELECT " +
            "    a.CUSTOMER_ID, " +
            "    b.ENCODED_KEY, " +
            "    b.employer_name, " +
            "    b.CREATION_DATE, " +
            "    b.ID, " +
            "    b.INTEREST_RATE, " +
            "    b.DISBURSEMENT_DATE, " +
            "    b.REPAYMENT_INSTALLMENTS, " +
            "    b.LOAN_AMOUNT, " +
            "    b.LOAN_NAME, " +
            "    b.CURRENCY_CODE, " +
            "    d.ACCT_BAL, " +
            "    zbd.LAST_ZERO_BAL_DATE, " +
            "    (COALESCE(zbd.LAST_ZERO_BAL_DATE, :tran_date) + 1) AS NEXT_DAY, " +
            "    nd.NEXT_DUE_DATE, " +
            "    :tran_date AS TRAN_DATE, " +
            "    COALESCE(TRUNC(:tran_date) - TRUNC(COALESCE(zbd.LAST_ZERO_BAL_DATE, :tran_date) + 1), 0) AS NO_OF_DAYS, " +
            "    SUM( " +
            "        CASE WHEN c.DUE_DATE <= :tran_date THEN (c.PRINCIPAL_EXP + c.INTEREST_EXP + c.FEE_EXP) ELSE 0 END " +
            "    ) AS DEMAND_APPLY, " +
            "    SUM( " +
            "        CASE WHEN c.DUE_DATE <= :tran_date " +
            "          AND (c.PRINCIPAL_EXP = c.PRINCIPAL_PAID) " +
            "          AND (c.INTEREST_EXP = c.INTEREST_PAID) " +
            "          AND (c.FEE_EXP = c.FEE_PAID) " +
            "        THEN (c.PRINCIPAL_PAID + c.INTEREST_PAID + c.FEE_PAID) ELSE 0 END " +
            "    ) AS COLLECTION_APPLY, " +
            "    ( " +
            "        SUM(CASE WHEN c.DUE_DATE <= :tran_date THEN (c.PRINCIPAL_EXP + c.INTEREST_EXP + c.FEE_EXP) ELSE 0 END) - " +
            "        SUM(CASE WHEN c.DUE_DATE <= :tran_date " +
            "          AND (c.PRINCIPAL_EXP = c.PRINCIPAL_PAID) " +
            "          AND (c.INTEREST_EXP = c.INTEREST_PAID) " +
            "          AND (c.FEE_EXP = c.FEE_PAID) " +
            "        THEN (c.PRINCIPAL_PAID + c.INTEREST_PAID + c.FEE_PAID) ELSE 0 END) " +
            "    ) AS ARREARS_APPLY " +
            "FROM CLIENT_MASTER_TBL a " +
            "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY " +
            "LEFT JOIN LOAN_REPAYMENT_TBL c ON b.ENCODED_KEY = c.PARENT_ACCOUNT_KEY " +
            "JOIN BGLS_CHART_OF_ACCOUNTS d ON b.ID = d.ACCT_NUM " +
            "LEFT JOIN ZeroBalanceDue zbd ON 1 = 1 " +
            "LEFT JOIN NextDueDate nd ON 1 = 1 " +
            "WHERE a.ENCODED_KEY = :holderKey " +
            "  AND b.ID = :id " +
            "  AND (c.DUE_DATE <= :tran_date OR c.DUE_DATE IS NULL) " +
            "GROUP BY " +
            "    a.CUSTOMER_ID, " +
            "    b.ENCODED_KEY, " +
            "    b.employer_name, " +
            "    b.CREATION_DATE, " +
            "    b.ID, " +
            "    b.INTEREST_RATE, " +
            "    b.DISBURSEMENT_DATE, " +
            "    b.REPAYMENT_INSTALLMENTS, " +
            "    b.LOAN_AMOUNT, " +
            "    b.LOAN_NAME, " +
            "    b.CURRENCY_CODE, " +
            "    d.ACCT_BAL, " +
            "    zbd.LAST_ZERO_BAL_DATE, " +
            "    nd.NEXT_DUE_DATE",
       nativeQuery = true)
Object[] getCustomer(@Param("tran_date") Date tran_date,
                     @Param("holderKey") String holderKey,
                     @Param("id") String id,
                     @Param("encodedKey") String encodedKey);


	@Query(value = "SELECT a.ENCODED_KEY, b.DUE_DATE as dueDate, b.REPAID_DATE as repaidDate, "
	        + "b.PRINCIPAL_EXP as principalExp, b.PRINCIPAL_PAID as principalPaid, b.PRINCIPAL_DUE as principalDue, "
	        + "b.INTEREST_EXP as interestExp, b.INTEREST_PAID as interestPaid, b.INTEREST_DUE as interestDue, "
	        + "b.FEE_EXP as feeExp, b.FEE_PAID as feePaid, b.FEE_DUE as feeDue, "
	        + "b.PENALTY_EXP as penaltyExp, b.PENALTY_PAID as penaltyPaid, b.PENALTY_DUE as penaltyDue "
	        + "FROM LOAN_ACCOUNT_MASTER_TBL a "
	        + "JOIN LOAN_REPAYMENT_TBL b ON a.ENCODED_KEY = b.PARENT_ACCOUNT_KEY "
	        + "WHERE a.ENCODED_KEY = ?1 AND a.DEL_FLG = 'N' AND b.DEL_FLG = 'N' "
	        + "ORDER BY b.DUE_DATE ASC",
	       nativeQuery = true)
	List<Object> getDues(String encodedKey);


	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL where  last_modified_date > approved_date", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActFilterUnverified();

	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL where  last_modified_date < approved_date", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActFilterVerified();

	@Query(value = "SELECT CASE WHEN last_modified_date > approved_date THEN 1 ELSE 0 END "
			+ "FROM LOAN_ACCOUNT_MASTER_TBL WHERE id = ?1", nativeQuery = true)
	Integer getUnverifiedStatus(String id);

	@Query(value = "SELECT id, loan_name, encoded_key, currency_code " +
            "FROM loan_account_master_tbl " +
            "ORDER BY id FETCH FIRST 1000 ROWS ONLY", nativeQuery = true)
List<Object[]> getActNo();

	@Query(value = "SELECT encoded_key FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	String getLoanViewdatas(String id);

	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where disbursement_flg = 'N' ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDetval();
	
	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where disbursement_flg = 'Y' ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getDistval();

	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where DISBURSEMENT_DATE <= ?1 AND interest_flg = 'N' ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDetval1(Date creation_date);
	
	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where DISBURSEMENT_DATE <= ?1 AND fees_flg = 'N' ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDetval21(Date creation_date);
	
	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where DISBURSEMENT_DATE <= ?1 AND recovery_flg = 'N' ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDetval31(Date creation_date);
	
	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where DISBURSEMENT_DATE <= ?1  ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDetval11(Date creation_date);
	
	@Query(value = "SELECT ENCODED_KEY FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID = :id", nativeQuery = true)
	List<String> getEncodedKeysByAccountId(@Param("id") String id);
	
	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL where DISBURSEMENT_DATE <= ?1 AND booking_flg = 'N' ORDER BY CREATION_DATE", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDetval41(Date creation_date);
	
	@Query(value = "select * from LOAN_ACCOUNT_MASTER_TBL WHERE DEL_FLG ='N' and id =?1", nativeQuery = true)
	LOAN_ACT_MST_ENTITY  getid(String id);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
 	@Transactional
 	@Query(value = "delete from LOAN_ACCOUNT_MASTER_TBL where id IN (:id)", nativeQuery = true)
 	int delteid(@Param("id") List<String> id);
 	
}
