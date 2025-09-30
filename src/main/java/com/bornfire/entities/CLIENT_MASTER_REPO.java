package com.bornfire.entities;

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
	CLIENT_MASTER_ENTITY  getid(String tr_his_id);
    
	@Query(value = "SELECT * FROM CLIENT_MASTER_TBL FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
    List<CLIENT_MASTER_ENTITY> getLoanActDet(@Param("limit") Long limit);
    
    @Query(value = "SELECT * FROM CLIENT_MASTER_TBL FETCH FIRST 1000 ROWS ONLY", nativeQuery = true)
    List<CLIENT_MASTER_ENTITY> getLoanActDetAll();

     @Query(value = "SELECT * FROM CLIENT_MASTER_TBL where  last_modified_date > approved_date", nativeQuery = true)
     List<CLIENT_MASTER_ENTITY> getLoanActFilterUnverified();
     
     @Query(value = "SELECT * FROM CLIENT_MASTER_TBL where  last_modified_date < approved_date", nativeQuery = true)
     List<CLIENT_MASTER_ENTITY> getLoanActFilterVerified();
     
     @Query(value = "SELECT CASE WHEN last_modified_date > approved_date THEN 1 ELSE 0 END " +
             "FROM CLIENT_MASTER_TBL WHERE CUSTOMER_ID = ?1", nativeQuery = true)
     Integer getUnverifiedStatus(String id);

     @Query(value = "SELECT " +
             "   L.ACCOUNT_HOLDERKEY, L.ID, " +
             "   L.LOAN_NAME, " +
             "   TO_CHAR(L.DISBURSEMENT_DATE, 'DD-MM-YYYY') AS DISBURSEMENT_DATE, " +
             "   L.LOAN_AMOUNT, " +
             "   NVL(B.ACCT_BAL, 0) AS ACCT_BAL " +
             "FROM " +
             "   LOAN_ACCOUNT_MASTER_TBL L " +
             "LEFT JOIN " +
             "   BGLS_CHART_OF_ACCOUNTS B " +
             "ON " +
             "   L.ID = B.ACCT_NUM " +
             "WHERE " +
             "   L.ACCOUNT_HOLDERKEY = ( " +
             "       SELECT ENCODED_KEY " +
             "       FROM CLIENT_MASTER_TBL " +
             "       WHERE customer_id = ?1 " +
             "   )",
     nativeQuery = true)
List<Object[]> getAccDet(String id);
     
    @Modifying(clearAutomatically = true, flushAutomatically = true)
 	@Transactional
 	@Query(value = "delete from CLIENT_MASTER_TBL where CUSTOMER_ID IN (:CUSTOMER_ID)", nativeQuery = true)
 	int delteid(@Param("CUSTOMER_ID") List<String> CUSTOMER_ID);
 	

  
}
