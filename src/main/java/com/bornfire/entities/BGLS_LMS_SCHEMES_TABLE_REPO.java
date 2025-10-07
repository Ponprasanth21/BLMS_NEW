package com.bornfire.entities;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BGLS_LMS_SCHEMES_TABLE_REPO extends JpaRepository<BGLS_LMS_SCHEMES_TABLE_ENTITY, String>{
	
	@Modifying
    @Transactional
    @Query(value = "DELETE FROM BGLS_LMS_SCHEMES_TABLE WHERE ID = ?1", nativeQuery = true)
    int deleteByIdCUSTOM(String id);

}
