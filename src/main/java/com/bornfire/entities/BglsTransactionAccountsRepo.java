package com.bornfire.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BglsTransactionAccountsRepo extends JpaRepository<BglsTransactionAccountsEntity, Long> {
    List<BglsTransactionAccountsEntity> findAllByOrderByIdAsc();
    
    @Query(value = "SELECT * FROM BGLS_TRANSACTION_ACCOUNTS " +
            "WHERE DEL_FLG = 'N' " +
            "ORDER BY ID", nativeQuery = true)
    List<BglsTransactionAccountsEntity> getList();

}

