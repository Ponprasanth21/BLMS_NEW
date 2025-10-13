package com.bornfire.entities;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BglsHolidayMasterRep extends CrudRepository<BglsHolidayMaster,Integer> {

	@Query(value = "SELECT COUNT(HOLIDAY_DESC) FROM BGLS_HOLIDAY_MASTER WHERE RECORD_DATE = :recordDate AND DEL_FLG = 'N'", nativeQuery = true)
	int countByRecordDateAndDelFlg(@Param("recordDate") java.util.Date tRANDATE);

	



}