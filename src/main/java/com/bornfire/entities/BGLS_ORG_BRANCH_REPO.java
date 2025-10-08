package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BGLS_ORG_BRANCH_REPO extends JpaRepository<BGLS_ORG_BRANCH_ENTITY, String> {

	@Query(value = "SELECT BRANCH_NAME FROM BGLS_ORG_BRANCH WHERE ENCODED_KEY = :branch_key", nativeQuery = true)
	String getBranchName(@Param("branch_key") String branch_key);
	
	@Query(value = "SELECT BRANCH_CODE FROM BGLS_ORG_BRANCH WHERE DEL_FLG='N'", nativeQuery = true)
    List<String> getAllBranchCodes();
}
