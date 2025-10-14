package com.bornfire.entities;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralLedgerRep extends CrudRepository<GeneralLedgerEntity,String> {
	
	public Optional<GeneralLedgerEntity> findById(String refId);
	
	@Query(value = "SELECT * from BGLS_GENERAL_LED WHERE del_flg='N' ", nativeQuery = true)
	List<GeneralLedgerEntity> getRefCodelist();
	
	@Query(value = "SELECT * from BGLS_GENERAL_LED WHERE glsh_code=?1 ", nativeQuery = true)
	GeneralLedgerEntity getRefMaster(String glsh_code);
	
	@Query(value = "SELECT * from BGLS_GENERAL_LED_COPY where del_flg ='N'", nativeQuery = true)
	List<GeneralLedgerEntity> getlistvalue();
	
	@Query(value = "SELECT * from BGLS_GENERAL_LED WHERE GLSH_CODE=?1", nativeQuery = true)
	GeneralLedgerEntity getsinglevalue(String glsh_Code);
	
	@Query(value = "SELECT * from BGLS_GENERAL_LED WHERE GLSH_CODE=?1", nativeQuery = true)
	GeneralLedgerEntity getsinglevaluedata(String glsh_Code);

//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE BGLS_GENERAL_LED " +
//            "SET total_balance = COA_SUM.ACCT_BAL " +
//            "FROM BGLS_GENERAL_LED " +
//            "JOIN (SELECT GLSH_CODE, SUM(ACCT_BAL) AS ACCT_BAL FROM COA GROUP BY GLSH_CODE) AS COA_SUM " +
//            "ON BGLS_GENERAL_LED.GLSH_CODE = COA_SUM.GLSH_CODE", 
//    nativeQuery = true)
//	void getupdateglwork();
//	
//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE BGLS_GENERAL_LED " +
//	               "SET no_acct_opened = updateacct.NO_OF_ACCT_OPENED " +
//	               "FROM BGLS_GENERAL_LED " +
//	               "JOIN (SELECT GLSH_CODE, COUNT(ACCT_CLS_FLG) AS NO_OF_ACCT_OPENED " +
//	                     "FROM COA WHERE ACCT_CLS_FLG = 'N' " +
//	                     "GROUP BY GLSH_CODE) AS updateacct " +
//	               "ON BGLS_GENERAL_LED.GLSH_CODE = updateacct.GLSH_CODE", 
//	       nativeQuery = true)
//	void updateNoAcctOpened();	
//	
//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE BGLS_GENERAL_LED " +
//	               "SET no_acct_closed = updateacct.NO_OF_ACCT_CLOSED " +
//	               "FROM BGLS_GENERAL_LED " +
//	               "JOIN (SELECT GLSH_CODE, COUNT(ACCT_CLS_FLG) AS NO_OF_ACCT_CLOSED " +
//	                     "FROM COA WHERE ACCT_CLS_FLG = 'Y' " +
//	                     "GROUP BY GLSH_CODE) AS updateacct " +
//	               "ON BGLS_GENERAL_LED.GLSH_CODE = updateacct.GLSH_CODE", 
//	       nativeQuery = true)
//	void updateNoAcctClosed();
	
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE BGLS_GENERAL_LED g " +
	               "SET g.total_balance = (SELECT SUM(c.ACCT_BAL) " +
	               "                       FROM BGLS_CHART_OF_ACCOUNTS c " +
	               "                       WHERE c.GLSH_CODE = g.GLSH_CODE) " +
	               "WHERE EXISTS (SELECT 1 FROM BGLS_CHART_OF_ACCOUNTS c " +
	               "              WHERE c.GLSH_CODE = g.GLSH_CODE)",
	       nativeQuery = true)
	void getupdateglwork();

	
	@Modifying
	@Transactional
	@Query(value = "UPDATE BGLS_GENERAL_LED g " +
	               "SET g.no_acct_opened = (SELECT COUNT(c.ACCT_CLS_FLG) " +
	               "                        FROM BGLS_CHART_OF_ACCOUNTS c " +
	               "                        WHERE c.GLSH_CODE = g.GLSH_CODE " +
	               "                          AND c.ACCT_CLS_FLG = 'N') " +
	               "WHERE EXISTS (SELECT 1 FROM BGLS_CHART_OF_ACCOUNTS c " +
	               "              WHERE c.GLSH_CODE = g.GLSH_CODE " +
	               "                AND c.ACCT_CLS_FLG = 'N')",
	       nativeQuery = true)
	void updateNoAcctOpened();

	
	@Modifying
	@Transactional
	@Query(value = "UPDATE BGLS_GENERAL_LED g " +
	               "SET g.no_acct_closed = (SELECT COUNT(c.ACCT_CLS_FLG) " +
	               "                        FROM BGLS_CHART_OF_ACCOUNTS c " +
	               "                        WHERE c.GLSH_CODE = g.GLSH_CODE " +
	               "                          AND c.ACCT_CLS_FLG = 'Y') " +
	               "WHERE EXISTS (SELECT 1 FROM BGLS_CHART_OF_ACCOUNTS c " +
	               "              WHERE c.GLSH_CODE = g.GLSH_CODE " +
	               "                AND c.ACCT_CLS_FLG = 'Y')",
	       nativeQuery = true)
	void updateNoAcctClosed(); 

	
	
	@Query(value = "select * from BGLS_GENERAL_LED WHERE DEL_FLG ='N' and glsh_code =?1", nativeQuery = true)
	GeneralLedgerEntity  getid(String id);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
 	@Transactional
 	@Query(value = "delete from BGLS_GENERAL_LED where glsh_code IN (:glsh_code)", nativeQuery = true)
 	int delteid(@Param("glsh_code") List<String> glsh_code);
}
