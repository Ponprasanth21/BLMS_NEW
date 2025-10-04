package com.bornfire.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bornfire.entities.*;

@Service
@ConfigurationProperties("output")
@Transactional
public class MultipleTransactionService {
	
	    @Autowired
	    private MULTIPLE_TRANSACTION_REPO multipleTransactionRepo;

	    public void saveBulkCollection(List<MULTIPLE_TRANSACTION_ENTITY> transactions, String user, String username) {
	        for (MULTIPLE_TRANSACTION_ENTITY tx : transactions) {

	            // ✅ Use frontend-provided transaction ID
	            if (tx.getTransaction_id() == null || tx.getTransaction_id().trim().isEmpty()) {
	                throw new IllegalArgumentException("Transaction ID is required from frontend");
	            }

	            // Generate unique serial number
	            String uniqueSrlNo = multipleTransactionRepo.getNextSrlNo();
	            tx.setSrl_no(uniqueSrlNo);

	            // Default flags
	            tx.setDel_flg("N");
	            tx.setEntity_flg("N");
	            tx.setAuth_flg("N");
	            tx.setModify_flg("N");
	            tx.setStatus("UNALLOCATED");

	            // Set timestamps
	            Date currentTime = new Date();
	            tx.setEntry_time(currentTime);
	            tx.setModify_time(currentTime);
	            tx.setTrans_time(currentTime);

	            // User info
	            tx.setEntry_user(user);
	            tx.setModify_user(user);

	            // Allocated amount
	            if (tx.getAllocated_amount() == null) {
	                tx.setAllocated_amount(BigDecimal.ZERO);
	            }

	            multipleTransactionRepo.save(tx);
	        }
	    }
	}
