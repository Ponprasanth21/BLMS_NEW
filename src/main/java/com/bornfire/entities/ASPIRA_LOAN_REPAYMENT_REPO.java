package com.bornfire.entities;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ASPIRA_LOAN_REPAYMENT_REPO  extends JpaRepository<ASPIRA_LOAN_REPAYMENT_ENTITY, String>{
	@Query(value = "select * from ASPIRA_LOAN_REPAYMENT_TABLE WHERE DEL_FLG ='N' and encodedkey =?1", nativeQuery = true)
	ASPIRA_LOAN_REPAYMENT_ENTITY  getid(String encodedkey);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
 	@Transactional
 	@Query(value = "delete from ASPIRA_LOAN_REPAYMENT_TABLE where encodedkey IN (:encodedkey)", nativeQuery = true)
 	int delteid(@Param("encodedkey") List<String> encodedkey);
}
