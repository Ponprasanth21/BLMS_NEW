package com.bornfire.services;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    	System.out.println("trandate"+trandate);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(trandate);
        System.out.println("Formatted trandate: " + formattedDate);
        List<Object[]> processFlags = bGLS_CONTROL_TABLE_REP.checkProcess();
        boolean hasPending = processFlags.stream()
            .flatMap(row -> Arrays.stream(row))
            .anyMatch(col -> "PENDING".equalsIgnoreCase(String.valueOf(col)));
        System.out.println("Not valid");

        if (hasPending) return "NotValid";
        
        System.out.println("Not valid1");

        // 2️⃣ Get old TRAN_DATE
        Date oldDate = bGLS_CONTROL_TABLE_REP.getCurrentTranDate();
        
        System.out.println("oldDate"+oldDate);
        // 3️⃣ Update TRAN_DATE and DCP_END_TIME
        int updated1 = bGLS_CONTROL_TABLE_REP.updateTranDate(formattedDate);
        
        System.out.println("updated1"+updated1);

        // 4️⃣ Compare old vs new date before updating flags
        Date newDate = bGLS_CONTROL_TABLE_REP.getCurrentTranDate();
        if (!oldDate.equals(newDate)) {
            int updated2 = bGLS_CONTROL_TABLE_REP.updateFlags();
            System.out.println("flag Updated"+updated2);
            if (updated1 > 0 && updated2 > 0) {
                String endDate = bGLS_CONTROL_TABLE_REP.selectEndDate(trandate);
                System.out.println("End Date: " + endDate);
                return "Valid - EndDate: " + endDate;
            } else {
                return "NotChange";
            }
        } else {
            return "NotChange"; // No change in date, so no flags updated
        }
    }
}
