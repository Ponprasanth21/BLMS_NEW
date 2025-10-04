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
public interface CLIENT_MASTER_REPO extends JpaRepository<CLIENT_MASTER_ENTITY, String> {

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getClientDet();

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL WHERE CUSTOMER_ID = ?1", nativeQuery = true)
	CLIENT_MASTER_ENTITY getClientView(String cust);

	@Query(value = "select * from CLIENT_MASTER_TBL WHERE DEL_FLG ='N' and CUSTOMER_ID =?1", nativeQuery = true)
	CLIENT_MASTER_ENTITY getid(String tr_his_id);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getLoanActDet(@Param("limit") Long limit);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL FETCH FIRST 1000 ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getLoanActDetAll();

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getLoanActDetPaged(@Param("offset") Long offset, @Param("limit") Long limit);

	// Total count for pagination
	@Query(value = "SELECT COUNT(*) FROM CLIENT_MASTER_TBL", nativeQuery = true)
	Long getTotalCount();

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL " + "WHERE (:status IS NULL OR client_state = :status) "
			+ "ORDER BY CUSTOMER_ID " + "OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getFilteredClients(@Param("status") String status, @Param("offset") Long offset,
			@Param("limit") Long limit);

	@Query(value = "SELECT COUNT(*) FROM CLIENT_MASTER_TBL WHERE (:status IS NULL OR client_state = :status)", nativeQuery = true)
	Long getFilteredClientsCount(@Param("status") String status);

//     @Query(value = "SELECT * FROM CLIENT_MASTER_TBL where  last_modified_date > approved_date", nativeQuery = true)
//     List<CLIENT_MASTER_ENTITY> getLoanActFilterUnverified();
//     
//     @Query(value = "SELECT * FROM CLIENT_MASTER_TBL where  last_modified_date < approved_date", nativeQuery = true)
//     List<CLIENT_MASTER_ENTITY> getLoanActFilterVerified();
//     
	@Query(value = "SELECT CASE WHEN last_modified_date > approved_date THEN 1 ELSE 0 END "
			+ "FROM CLIENT_MASTER_TBL WHERE CUSTOMER_ID = ?1", nativeQuery = true)
	Integer getUnverifiedStatus(String id);

	@Query(value = "SELECT " + "   L.ACCOUNT_HOLDERKEY, L.ID, " + "   L.LOAN_NAME, "
			+ "   TO_CHAR(L.DISBURSEMENT_DATE, 'DD-MM-YYYY') AS DISBURSEMENT_DATE, " + "   L.LOAN_AMOUNT, "
			+ "   NVL(B.ACCT_BAL, 0) AS ACCT_BAL " + "FROM " + "   LOAN_ACCOUNT_MASTER_TBL L " + "LEFT JOIN "
			+ "   BGLS_CHART_OF_ACCOUNTS B " + "ON " + "   L.ID = B.ACCT_NUM " + "WHERE "
			+ "   L.ACCOUNT_HOLDERKEY = ( " + "       SELECT ENCODED_KEY " + "       FROM CLIENT_MASTER_TBL "
			+ "       WHERE customer_id = ?1 " + "   )", nativeQuery = true)
	List<Object[]> getAccDet(String id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = "delete from CLIENT_MASTER_TBL where CUSTOMER_ID IN (:CUSTOMER_ID)", nativeQuery = true)
	int delteid(@Param("CUSTOMER_ID") List<String> CUSTOMER_ID);

	// Unverified - paginated
	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL WHERE last_modified_date > approved_date OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getLoanActFilterUnverifiedPaged(@Param("offset") Long offset,
			@Param("limit") Long limit);

	@Query(value = "SELECT COUNT(*) FROM CLIENT_MASTER_TBL WHERE last_modified_date > approved_date", nativeQuery = true)
	Long getUnverifiedCount();

	// Verified - paginated
	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL WHERE last_modified_date < approved_date OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getLoanActFilterVerifiedPaged(@Param("offset") Long offset, @Param("limit") Long limit);

	@Query(value = "SELECT COUNT(*) FROM CLIENT_MASTER_TBL WHERE last_modified_date < approved_date", nativeQuery = true)
	Long getVerifiedCount();

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL "
			+ "WHERE LOWER(CUSTOMER_ID) LIKE LOWER('%' || :customerId || '%')", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> searchByCustomerIdLike(@Param("customerId") String customerId);

	@Modifying
	@Transactional
	@Query(value = " INSERT INTO CLIENT_MASTER_TBL (   " + "    ENCODED_KEY,   " + "    CUSTOMER_ID,   "
			+ "    CLIENT_STATE,   " + "    CREATION_DATE,   " + "    LAST_MODIFIED_DATE,   "
			+ "    ACTIVATION_DATE,   " + "    APPROVED_DATE,    " + "    FIRST_NAME,   " + "    LAST_NAME,   "
			+ "    MOBILE_PHONE,   " + "    EMAIL_ADDRESS,   " + "    PREFERRED_LANGUAGE,   " + "    BIRTH_DATE,    "
			+ "    GENDER,   " + "    ASSIGNED_BRANCH_KEY,   " + "    CLIENT_ROLE_KEY,   " + "    LOAN_CYCLE,   "
			+ "    GROUP_LOAN_CYCLE,   " + "    ADDRESS_LINE1,   " + "    ADDRESS_LINE2,   " + "    ADDRESS_LINE3,   "
			+ "    CITY,   " + "    SUBURB,   " + "    ASSIGNED_USER_KEY,   " + "    ASONDATE   " + ")   "
			+ "SELECT    " + "    ENCODED_KEY,   " + "    CUSTOMER_ID,   " + "    CLIENT_STATE,    "
			+ "    CASE WHEN REGEXP_LIKE(CREATION_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(CREATION_DATE,'YYYY-MM-DD') ELSE NULL END,    "
			+ "    CASE WHEN REGEXP_LIKE(LAST_MODIFIED_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(LAST_MODIFIED_DATE,'YYYY-MM-DD') ELSE NULL END,    "
			+ "    CASE WHEN REGEXP_LIKE(ACTIVATION_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(ACTIVATION_DATE,'YYYY-MM-DD') ELSE NULL END,    "
			+ "    CASE WHEN REGEXP_LIKE(APPROVED_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(APPROVED_DATE,'YYYY-MM-DD') ELSE NULL END,    "
			+ "    FIRST_NAME,    " + "    LAST_NAME,   " + "    MOBILE_PHONE,   " + "    EMAIL_ADDRESS,   "
			+ "    PREFERRED_LANGUAGE,    "
			+ "    CASE WHEN REGEXP_LIKE(BIRTH_DATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(BIRTH_DATE,'YYYY-MM-DD') ELSE NULL END,    "
			+ "    GENDER,    " + "    ASSIGNED_BRANCH_KEY,   " + "    CLIENT_ROLE_KEY,   "
			+ "    CASE WHEN REGEXP_LIKE(TRIM(LOAN_CYCLE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(LOAN_CYCLE) ELSE NULL END,   "
			+ "    CASE WHEN REGEXP_LIKE(TRIM(GROUP_LOAN_CYCLE), '^\\d+(\\.\\d+)?$') THEN TO_NUMBER(GROUP_LOAN_CYCLE) ELSE NULL END,   "
			+ "    ADDRESS_LINE1,    " + "    ADDRESS_LINE2,   " + "    ADDRESS_LINE3,   " + "    CITY,   "
			+ "    SUBURB,   " + "    ASSIGNED_USER_KEY,    "
			+ "    CASE WHEN REGEXP_LIKE(ASONDATE,'^\\\\d{4}-\\\\d{2}-\\\\d{2}$') THEN TO_DATE(ASONDATE,'YYYY-MM-DD') ELSE NULL END    "
			+ " FROM CLIENT_MASTER_TBL_UPLOAD s " + " WHERE NOT EXISTS ( " + "     SELECT 1 "
			+ "     FROM CLIENT_MASTER_TBL m " + "     WHERE m.CUSTOMER_ID = s.CUSTOMER_ID "
			+ " ) ", nativeQuery = true)
	void CustomerMasterCopyTempTableToMainTable();

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM CLIENT_MASTER_TBL_UPLOAD", nativeQuery = true)
	int CustomerMasterTempTableDelete();

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL "
			+ "WHERE LOWER(MOBILE_PHONE) LIKE LOWER('%' || :mobile || '%')", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> searchByMobileLike(@Param("mobile") String customerId);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL "
			+ "WHERE LOWER(EMAIL_ADDRESS) LIKE LOWER('%' || :email || '%')", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> searchByEmailLike(@Param("email") String customerId);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL "
			+ "WHERE client_state = :status FETCH FIRST 2000 ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> findByStatus(@Param("status") String status);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL "
			+ "WHERE LOWER(CUSTOMER_ID) LIKE LOWER('%' || :customerId || '%') "
			+ "AND (:status IS NULL OR CLIENT_STATE = :status) FETCH FIRST 2000 ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> searchByCustomerIdAndStatus(@Param("customerId") String customerId,
			@Param("status") String status);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL " + "WHERE LOWER(MOBILE_PHONE) LIKE LOWER('%' || :mobile || '%') "
			+ "AND (:status IS NULL OR CLIENT_STATE = :status) FETCH FIRST 2000 ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> searchByMobileAndStatus(@Param("mobile") String mobile, @Param("status") String status);

	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL " + "WHERE LOWER(EMAIL_ADDRESS) LIKE LOWER('%' || :email || '%') "
			+ "AND (:status IS NULL OR CLIENT_STATE = :status) FETCH FIRST 2000 ROWS ONLY", nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> searchByEmailAndStatus(@Param("email") String email, @Param("status") String status);

	@Query(value = 
		    "SELECT B.due_date, '1' AS flow_id, 'PENDEM' AS flow_code, (B.PENALTY_EXP - B.PENALTY_PAID) AS flow_amt, " +
		    "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " +
		    "FROM CLIENT_MASTER_TBL C " +
		    "JOIN LOAN_ACCOUNT_MASTER_TBL A ON C.ENCODED_KEY = A.ACCOUNT_HOLDERKEY " +
		    "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " +
		    "WHERE B.DUE_DATE <= :toDate AND C.CUSTOMER_ID = :customerId AND (B.PENALTY_EXP - B.PENALTY_PAID) > 0 " +

		    "UNION ALL " +

		    "SELECT B.due_date, '2' AS flow_id, 'FEEDEM' AS flow_code, (B.FEE_EXP - B.FEE_PAID) AS flow_amt, " +
		    "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " +
		    "FROM CLIENT_MASTER_TBL C " +
		    "JOIN LOAN_ACCOUNT_MASTER_TBL A ON C.ENCODED_KEY = A.ACCOUNT_HOLDERKEY " +
		    "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " +
		    "WHERE B.DUE_DATE <= :toDate AND C.CUSTOMER_ID = :customerId AND (B.FEE_EXP - B.FEE_PAID) > 0 " +

		    "UNION ALL " +

		    "SELECT B.due_date, '3' AS flow_id, 'INDEM' AS flow_code, (B.INTEREST_EXP - B.INTEREST_PAID) AS flow_amt, " +
		    "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " +
		    "FROM CLIENT_MASTER_TBL C " +
		    "JOIN LOAN_ACCOUNT_MASTER_TBL A ON C.ENCODED_KEY = A.ACCOUNT_HOLDERKEY " +
		    "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " +
		    "WHERE B.DUE_DATE <= :toDate AND C.CUSTOMER_ID = :customerId AND (B.INTEREST_EXP - B.INTEREST_PAID) > 0 " +

		    "UNION ALL " +

		    "SELECT B.due_date, '4' AS flow_id, 'PRDEM' AS flow_code, (B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) AS flow_amt, " +
		    "A.ID AS loan_acct_no, A.LOAN_NAME AS acct_name, A.ENCODED_KEY " +
		    "FROM CLIENT_MASTER_TBL C " +
		    "JOIN LOAN_ACCOUNT_MASTER_TBL A ON C.ENCODED_KEY = A.ACCOUNT_HOLDERKEY " +
		    "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " +
		    "WHERE B.DUE_DATE <= :toDate AND C.CUSTOMER_ID = :customerId AND (B.PRINCIPAL_EXP - B.PRINCIPAL_PAID) > 0 " +

		    "ORDER BY due_date", 
		    nativeQuery = true)
		List<Object[]> getLoanFlowsByCustomer(Date toDate, String customerId);
		
		
		@Query(value = "SELECT *, " +
	               "FROM CLIENT_MASTER_TBL " +
	               "ORDER BY CLIENT_STATE, CUSTOMER_ID", 
	       nativeQuery = true)
	List<CLIENT_MASTER_ENTITY> getClientDetails();

	


}
