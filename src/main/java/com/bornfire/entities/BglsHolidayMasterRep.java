package com.bornfire.entities;

import java.sql.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BglsHolidayMasterRep extends CrudRepository<BglsHolidayMaster,Integer> {

	@Query("SELECT COUNT(h) FROM BglsHolidayMaster h " +
		       "WHERE FUNCTION('TRUNC', h.recordDate) = FUNCTION('TRUNC', :recordDate) " +
		       "AND h.delFlg = :delFlg")
		int countByRecordDateAndDelFlg(@Param("recordDate") Date recordDate,
		                               @Param("delFlg") String delFlg);

	int countByRecordDateAndDelFlg(java.util.Date tRANDATE, String delFlg);



}