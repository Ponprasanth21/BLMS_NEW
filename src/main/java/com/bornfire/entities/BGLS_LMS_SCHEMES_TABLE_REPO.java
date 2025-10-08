package com.bornfire.entities;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BGLS_LMS_SCHEMES_TABLE_REPO extends JpaRepository<BGLS_LMS_SCHEMES_TABLE_ENTITY, String>{
	
	@Modifying
    @Transactional
    @Query(value = "DELETE FROM BGLS_LMS_SCHEMES_TABLE WHERE ID = ?1", nativeQuery = true)
    int deleteByIdCUSTOM(String id);
	
	@Query(value = "SELECT * FROM BGLS_LMS_SCHEMES_TABLE WHERE DEL_FLG = 'N'", nativeQuery = true)
	List<BGLS_LMS_SCHEMES_TABLE_ENTITY> getSchemeList();



	@Modifying
	@Query(value = "UPDATE BGLS_LMS_SCHEMES_TABLE SET DEL_FLG = 'Y' WHERE SCHEME_ID = :id", nativeQuery = true)
	int softDeleteById(@Param("id") String id);

}
