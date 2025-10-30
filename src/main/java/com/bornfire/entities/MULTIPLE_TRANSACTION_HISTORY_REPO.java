package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MULTIPLE_TRANSACTION_HISTORY_REPO extends JpaRepository<MULTIPLE_TRANSACTION_HISTORY_ENTITY, String>{
	@Query(value="SELECT * FROM MULTIPLE_TRANSACTION_HISTORY_TBL", nativeQuery = true)
	List<MULTIPLE_TRANSACTION_ENTITY> getdata();
	
	@Query(value = "SELECT SRL_NO_SEQ_1.NEXTVAL FROM DUAL", nativeQuery = true)
	String getNextSrlNo();
	
	@Query(value = "SELECT * FROM MULTIPLE_TRANSACTION_HISTORY_TBL WHERE STATUS = 'UNALLOCATED'", nativeQuery = true)
	List<MULTIPLE_TRANSACTION_ENTITY> getDataValue();

    @Query(value = "SELECT TRANSACTION_ID FROM MULTIPLE_TRANSACTION_HISTORY_TBL WHERE TRANSACTION_ID = ?1", nativeQuery = true)
    List<String> getdatavalues(String tran_id);
}
