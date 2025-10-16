package com.bornfire.services;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.entities.BGLS_CONTROL_TABLE_REP;
import com.bornfire.entities.BGLS_Control_Table;

@Service
public class DateChangeService {

    @Autowired
    private BGLS_CONTROL_TABLE_REP bGLS_CONTROL_TABLE_REP;

    @Transactional
    public String dateChange(Date nxtDate,Date trandate,HttpServletRequest rq) {

    	System.out.println("trandate"+trandate);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedtrandate = sdf.format(trandate);
        String formattedDatenxtDate = sdf.format(nxtDate);
        System.out.println("the custome dates format");
        System.out.println("Formatted trandate: " + formattedtrandate);
        System.out.println("Formatted trandate: " + formattedDatenxtDate);

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
        int updated1 = bGLS_CONTROL_TABLE_REP.updateTranDate(formattedDatenxtDate,formattedtrandate);
        BGLS_Control_Table up1 = bGLS_CONTROL_TABLE_REP.getTranDate();
        rq.getSession().setAttribute("TRANDATE", up1.getTran_date());        
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
