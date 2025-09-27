package com.bornfire.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.entities.BGLS_CONTROL_TABLE_REP;

@Service
public class DateChangeService {

    @Autowired
    private BGLS_CONTROL_TABLE_REP bGLS_CONTROL_TABLE_REP;

    @Transactional
    public String dateChange(Date trandate) {
        List<Object[]> processFlags = bGLS_CONTROL_TABLE_REP.checkProcess();

        boolean hasPending = false;
        for (Object[] row : processFlags) {
            for (Object col : row) {
                if ("PENDING".equalsIgnoreCase(String.valueOf(col))) {
                    hasPending = true;
                    break;
                }
            }
            if (hasPending) break;
        }

        if (!hasPending) {
            int updated1 = bGLS_CONTROL_TABLE_REP.updateTranDate(trandate);
            int updated2 = bGLS_CONTROL_TABLE_REP.updateFlags();

            if (updated1 > 0 && updated2 > 0) {
                String endDate = bGLS_CONTROL_TABLE_REP.selectEndDate(trandate);
                System.out.println("End Date: " + endDate);
                return "Valid - EndDate: " + endDate;
            } else {
                return "NotChange";
            }
        } else {
            return "NotValid";
        }
    }
}
