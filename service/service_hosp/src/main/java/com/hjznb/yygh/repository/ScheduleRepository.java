package com.hjznb.yygh.repository;

import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 21:02
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);
}
