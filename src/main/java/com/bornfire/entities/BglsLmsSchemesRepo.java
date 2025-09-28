package com.bornfire.entities;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BglsLmsSchemesRepo extends JpaRepository<BglsLmsSchemesEntity, String>{

	@Query(value = "select * from BGLS_LMS_SCHEMES where DEL_FLG = 'N' ", nativeQuery = true)
	List<BglsLmsSchemesEntity> listofvalue();
	
	@Query(value = "SELECT * FROM BGLS_LMS_SCHEMES WHERE ID = ?1", nativeQuery = true)
	List<BglsLmsSchemesEntity> findByUniqueId(String id);

	@Modifying
    @Transactional
    @Query(value = "DELETE FROM BGLS_LMS_SCHEMES WHERE ID = ?1", nativeQuery = true)
    int deleteByUniqueId(String id);
}
