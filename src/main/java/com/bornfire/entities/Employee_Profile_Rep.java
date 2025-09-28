package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Employee_Profile_Rep extends JpaRepository<Employee_Profile, String>{
	
	@Query(value="SELECT EMPLOYEE_ID FROM BGLS_EMPLOYEE_PROFILE", nativeQuery = true)
	List<String> getexistingData();
	
	@Query(value="SELECT * FROM BGLS_EMPLOYEE_PROFILE", nativeQuery = true)
	List<Employee_Profile> getEmployeeList();
	
	@Query(value="SELECT * FROM BGLS_EMPLOYEE_PROFILE WHERE EMPLOYEE_ID =?1", nativeQuery = true)
	Employee_Profile getEmployeeData(String employee_id);
	
	@Query(value="SELECT branch_desc FROM BGLS_EMPLOYEE_PROFILE where BRANCH_ID =?1 AND DEL_FLG='N'", nativeQuery = true)
	String getBranchName(String branch_id);
	
	@Query(value = "SELECT MAX(\r\n"
			+ "         TO_NUMBER(\r\n"
			+ "           CASE \r\n"
			+ "             WHEN REGEXP_INSTR(EMPLOYEE_ID, '[0-9]') > 0 \r\n"
			+ "             THEN SUBSTR(EMPLOYEE_ID, REGEXP_INSTR(EMPLOYEE_ID, '[0-9]'))\r\n"
			+ "             ELSE '0'\r\n"
			+ "           END\r\n"
			+ "         )\r\n"
			+ "       ) AS max_numeric_part\r\n"
			+ "FROM BGLS_EMPLOYEE_PROFILE", nativeQuery = true)
	String getSrlNo();

	@Query(value="SELECT *\r\n"
			+ "FROM BGLS_EMPLOYEE_PROFILE e\r\n"
			+ "WHERE e.entity_flg = 'Y'\r\n"
			+ "  AND NOT EXISTS (\r\n"
			+ "        SELECT 1\r\n"
			+ "        FROM BGLS_USER_PROFILE_TABLE u\r\n"
			+ "        WHERE u.DEL_FLG = 'N'\r\n"
			+ "          AND u.USER_ID = e.employee_id\r\n"
			+ "      )", nativeQuery = true)
	List<Employee_Profile> getEmployeeVeifiedList();

	
}
