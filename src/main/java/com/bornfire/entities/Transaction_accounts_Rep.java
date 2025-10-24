package com.bornfire.entities;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Transaction_accounts_Rep extends JpaRepository<Transaction_accounts_entity, String>{
	@Query(value = "SELECT BGLS_TRANSACTION_ACCOUNTS_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	String gettrmRefUUID();
	
	@Query(value = "select * from BGLS_TRANSACTION_ACCOUNTS", nativeQuery = true)
	List<Transaction_accounts_entity> getLoanActDet1();
	
	@Query(value = "SELECT * FROM BGLS_TRANSACTION_ACCOUNTS WHERE product_key =?1", nativeQuery = true)
	Transaction_accounts_entity getLoanView(String id);
}
