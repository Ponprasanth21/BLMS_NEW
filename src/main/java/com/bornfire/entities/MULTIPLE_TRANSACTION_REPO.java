package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MULTIPLE_TRANSACTION_REPO extends JpaRepository<MULTIPLE_TRANSACTION_ENTITY, String> {
	
	@Query(value="SELECT * FROM MULTIPLE_TRANSACTION_TBL", nativeQuery = true)
	List<MULTIPLE_TRANSACTION_ENTITY> getdata();
	
	@Query(value = "SELECT SRL_NO_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	String getNextSrlNo();
	
	@Query(value = "SELECT * FROM MULTIPLE_TRANSACTION_TBL", nativeQuery = true)
	List<MULTIPLE_TRANSACTION_ENTITY> getDataValue();
	
	@Query(value = "SELECT * FROM MULTIPLE_TRANSACTION_TBL WHERE transaction_id = ?1", nativeQuery = true)
	List<MULTIPLE_TRANSACTION_ENTITY> getDataValue1(@Param("tranId") String tranId);
}
