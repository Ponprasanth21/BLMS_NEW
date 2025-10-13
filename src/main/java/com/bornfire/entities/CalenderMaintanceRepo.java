package com.bornfire.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CalenderMaintanceRepo extends JpaRepository<CalenderMaintanceEntity, Integer> {

    @Query(value = "SELECT * FROM cal_master where YEAR = to_char(sysdate, 'YYYY') ORDER BY year DESC, TO_DATE(month, 'Mon') ASC", nativeQuery = true)
    List<CalenderMaintanceEntity> getAllCalenderMaintanceList();

}
