package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HolidayMaster_Rep extends CrudRepository<HolidayMaster_Entity, Long> {

	@Query(value = "select * from HOLIDAY_MASTER WHERE CAL_YEAR  = to_char(sysdate, 'YYYY') AND DEL_FLG = 'N' ORDER BY TO_DATE(cal_month, 'Mon')  ASC", nativeQuery = true)
	List<HolidayMaster_Entity> getlistofHoliday();
	
	@Query(value = "select * from HOLIDAY_MASTER WHERE id=?1", nativeQuery = true)
	HolidayMaster_Entity getsinglevalue(BigDecimal record_srl);

    @Query(value ="select CAL_YEAR,CAL_MONTH,to_char(RECORD_DATE,'dd-mm-yyyy')RECORD_DATE,TO_CHAR(RECORD_DATE, 'Day') AS DAY,HOLIDAY_DESC,HOLIDAY_REMARKS from HOLIDAY_MASTER WHERE CAL_YEAR= :year AND CAL_MONTH= :month AND DEL_FLG='N'",nativeQuery = true)
    List<Object[]> holidayList(@Param("year") String year,@Param("month") String month);

    @Query(value = "SELECT COUNT(*) FROM HOLIDAY_MASTER WHERE RECORD_DATE = :dateStr", nativeQuery = true)
    int getExistingLeave(@Param("dateStr") Date dateStr);

    @Query(value = "select * from HOLIDAY_MASTER WHERE id=?1", nativeQuery = true)
    HolidayMaster_Entity getsinglevalueHoliday(Long recordSrl);
}
