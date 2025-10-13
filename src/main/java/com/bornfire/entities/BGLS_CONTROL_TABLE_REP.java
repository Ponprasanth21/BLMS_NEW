package com.bornfire.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface BGLS_CONTROL_TABLE_REP extends JpaRepository<BGLS_Control_Table, Date>{
	@Query(value="SELECT * FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	BGLS_Control_Table getTranDate();
	
	@Query(value = "\r\n"
			+ "SELECT TRAN_DATE\r\n"
			+ "FROM (\r\n"
			+ "   SELECT TRAN_DATE\r\n"
			+ "   FROM BGLS_CONTROL_TABLE\r\n"
			+ "   ORDER BY TRAN_DATE DESC\r\n"
			+ ")\r\n"
			+ "WHERE ROWNUM = 1", nativeQuery = true)
    Date getLatestTranDate();  // Returns the latest TRAN_DATE
	
	 @Query(value = "SELECT JOURNAL_CONS, LEDGER_CONS, ACCT_CONS, HOL_CHECK, MOV_DAC, MOV_JOURNAL FROM CON", nativeQuery = true)
	 List<Object[]> checkProcess();
	 
	 // ✅ Update TRAN_DATE and set DCP_END_TIME
	 @Query(value = "UPDATE CON SET TRAN_DATE = :trandate, DCP_END_TIME = CURRENT_TIMESTAMP", nativeQuery = true)
	 int updateTranDate(@Param("trandate") Date trandate);

	    // ✅ Update flags after transaction
	  @Query(value = "UPDATE CON SET DCP_FLG = 'Y', DCP_STATUS = 'OPEN'", nativeQuery = true)
	  int updateFlags();

	  // ✅ Get EndDate (previous date)
	  @Query(value = "SELECT TO_CHAR(TO_DATE(:trandate, 'YYYY-MM-DD') - 1, 'DD-MM-YYYY') FROM DUAL", nativeQuery = true)
	  String selectEndDate(@Param("trandate") Date trandate);
	  
	  @Query(value = "SELECT JOURNAL_CONS FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getJournal();
	  
	  @Query(value = "SELECT LEDGER_CONS FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getAccountLedger();
	  
	  @Query(value = "SELECT HOL_CHECK FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getHolidayCheck();
	  
	  @Query(value = "SELECT MOV_DAC FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getDAB();
	  
	  @Query(value = "SELECT ACCT_CONS FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getConsistencyCheck();
	  
	  @Query(value = "SELECT MOV_JOURNAL FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getMovementofTransaction();
	  
	
	  
}
