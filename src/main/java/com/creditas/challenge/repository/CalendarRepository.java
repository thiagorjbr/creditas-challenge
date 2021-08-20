package com.creditas.challenge.repository;

import java.sql.Date;
import java.util.List;

import com.creditas.challenge.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Schedule, Integer>{
	public List<Schedule> findByDayAndCavId(Date day, int cavId);
	
	public Schedule findByDayAndCavIdAndHourAndType(Date day, int cavId, int hour, String type);
}
