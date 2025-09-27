package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BglsLmsSchemesRepo extends JpaRepository<BglsLmsSchemesEntity, String>{

	@Query(value = "select * from BGLS_LMS_SCHEMES where DEL_FLG = 'N' ", nativeQuery = true)
	List<BglsLmsSchemesEntity> listofvalue();
}
