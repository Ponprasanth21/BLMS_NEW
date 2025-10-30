package com.bornfire.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface BGLS_CONTROL_TABLE_REP extends JpaRepository<BGLS_Control_Table,Date>{
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
	 
	  @Query(value = "SELECT GL_CON FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getGlConsolidation();
	  
	  @Query(value = "SELECT INTEREST_DEMAND_GEN FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getInterestDemand();
	  
	  @Query(value = "SELECT FEE_DEMAND_GEN FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getFeeDemand();
	  
	  @Query(value = "SELECT PENALTY FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getPenalty();
	  
	  @Query(value = "SELECT DCP_STATUS FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getDcpStatus();
	  
	  @Query(value = "SELECT INTEREST_ACCURAL FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	  String getinterestAccurual();
	  
	  
	  
	    @Query(value = "SELECT JOURNAL_CONS, LEDGER_CONS, ACCT_CONS, HOL_CHECK, MOV_DAC, MOV_JOURNAL FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	    List<Object[]> checkProcess();

	    // 2️⃣ Update TRAN_DATE and DCP_END_TIME
	    @Modifying
	    @Query(value = "UPDATE BGLS_CONTROL_TABLE SET TRAN_DATE = :nxtDate, DCP_END_TIME = CURRENT_TIMESTAMP,PREVIOUS_DATE =:tranDate", nativeQuery = true)
	    int updateTranDate(@Param("nxtDate") String nxtDate,@Param("tranDate") String tranDate);

	    // 3️⃣ Update flags (conditionally after date changed)
	    @Modifying
	    @Query(value = "UPDATE BGLS_CONTROL_TABLE SET DCP_FLG = 'Y', DCP_STATUS = 'OPEN'", nativeQuery = true)
	    int updateFlags();

	    // 4️⃣ Get current TRAN_DATE
	    @Query(value = "SELECT TRAN_DATE FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	    Date getCurrentTranDate();

	    // 5️⃣ Get EndDate (previous day)
	    @Query(value = "SELECT TO_CHAR(:trandate - 1, 'DD-MM-YYYY') FROM DUAL", nativeQuery = true)
	    String selectEndDate(@Param("trandate") Date trandate);
	    
	    
	    @Query(value = "SELECT DCP_STATUS FROM BGLS_CONTROL_TABLE", nativeQuery = true)
	    String getDcpstatus();
	
	    @Modifying
	    @Query(value = "UPDATE BGLS_CONTROL_TABLE SET TRAN_DATE = :nxtdate, DCP_END_TIME = CURRENT_TIMESTAMP,PREVIOUS_DATE =:trndate", nativeQuery = true)
	    int updateTranDates(@Param("nxtdate") Date nxtdate,@Param("trndate") Date trndate);

}
