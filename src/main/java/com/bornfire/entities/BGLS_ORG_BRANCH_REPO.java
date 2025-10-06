package com.bornfire.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BGLS_ORG_BRANCH_REPO extends JpaRepository<BGLS_ORG_BRANCH_ENTITY, String> {
	
	@Query(value = "SELECT * FROM BGLS_ORG_BRANCH BOB WHERE ENCODED_KEY = :branch_key", nativeQuery = true)
	BGLS_ORG_BRANCH_ENTITY getBranchName(@Param("branch_key") String branch_key);

}
