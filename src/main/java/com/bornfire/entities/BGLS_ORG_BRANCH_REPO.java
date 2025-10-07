package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BGLS_ORG_BRANCH_REPO extends JpaRepository<BGLS_ORG_BRANCH_ENTITY, String> {

	@Query(value = "SELECT CASE " + "WHEN ENCODED_KEY = '8a858eaa6d00215f016d005b083916a9' THEN 'MOMBASA' "
			+ "WHEN ENCODED_KEY = '8a858f3e8851f7a30188528a071a5228' THEN 'B2B' "
			+ "WHEN ENCODED_KEY = '8a858f6d62bdb6fc0162be4f775e5792' THEN 'NAIROBI_HEADOFFICE' "
			+ "ELSE 'UNKNOWN' END AS BRANCH_NAME " + "FROM BGLS_ORG_BRANCH "
			+ "WHERE ENCODED_KEY = :branch_key", nativeQuery = true)
	String getBranchName(@Param("branch_key") String branch_key);
	
	@Query(value = "SELECT BRANCH_CODE FROM BGLS_ORG_BRANCH WHERE DEL_FLG='N'", nativeQuery = true)
    List<String> getAllBranchCodes();

}
