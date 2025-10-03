package com.bornfire.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MULTIPLE_TRANSACTION_REPO extends JpaRepository<MULTIPLE_TRANSACTION_ENTITY, String> {

}
