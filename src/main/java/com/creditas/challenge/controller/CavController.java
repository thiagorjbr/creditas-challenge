package com.creditas.challenge.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.management.InvalidAttributeValueException;
import javax.persistence.NoResultException;

import com.creditas.challenge.model.Car;
import com.creditas.challenge.model.Schedule;
import com.creditas.challenge.repository.CalendarRepository;
import com.creditas.challenge.repository.CarRepository;
import com.creditas.challenge.repository.CavRepository;
import com.creditas.challenge.util.CalendarType;
import com.creditas.challenge.util.DayOfTheWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.creditas.challenge.model.Cav;
import com.creditas.challenge.model.Cav.OpenHours;

@Controller
@RequestMapping("/cav")
public class CavController {
	
	@Autowired
	private CavRepository cavRepo;

	@Autowired
	private CarRepository carRepo;

	@Autowired
	private CalendarRepository calendarRepo;

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public List<Cav> listCavs() throws Exception {
		List<Cav> list = cavRepo.findAll();
		return list;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public void insertCav(@RequestBody Cav cav) throws Exception {
		if (cavRepo.findByName(cav.getName()) != null) {
			throw new IllegalArgumentException("Existing CAV");
		} else if (cav.getOpen_hours() == null || cav.getName() == null) {
			throw new IllegalArgumentException("All data must be fulfilled");
		}
		
		cavRepo.save(cav);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = {"/{cavId}"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public void updateCav(@PathVariable Integer cavId, @RequestBody Cav cav) throws Exception {
		Cav cavToUpdate = getCav(cavId);

		cavToUpdate.merge(cav);
		
		cavRepo.save(cavToUpdate);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = {"/{cavId}/{day}" }, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Map<String, SortedMap<Integer, Object>> showCalendar(@PathVariable Integer cavId, @PathVariable String day) throws ParseException, JsonProcessingException, InvalidAttributeValueException {
		Date date = parseToFormatDate(day);
		List<Schedule> calendars = calendarRepo.findByDayAndCavId(date, cavId);

		Cav cav;

		if (calendars.isEmpty()) {
			cav = getCav(cavId);
		} else {
			cav = calendars.get(0).getCav();
		}

		HashMap<String, OpenHours> openHours = cav.getOpen_hours();

		DayOfTheWeek weekDay = getWeekDay(date);

		verifyWeekDayAndOpenHours(openHours, weekDay);

		SortedMap<Integer, Object> resultVisit = new TreeMap<Integer, Object>();
		SortedMap<Integer, Object> resultInspection = new TreeMap<Integer, Object>();

		for (Schedule c : calendars) {
			if (c.getType().equalsIgnoreCase(CalendarType.INSPECTION.name())) {
				resultInspection.put(c.getHour(), c.getCar().getId());
			} else if (c.getType().equalsIgnoreCase(CalendarType.VISIT.name())) {
				resultVisit.put(c.getHour(), c.getCar().getId());
			}
		}

		for (int i = openHours.get(weekDay.getNameLowerCase()).getBegin(); i <= openHours.get(weekDay.getNameLowerCase()).getEnd(); i++) {
			if (!resultVisit.containsKey(i)) {
				resultVisit.put(i, "");
			}

			if (!resultInspection.containsKey(i)) {
				resultInspection.put(i, "");
			}
		}

		Map<String, SortedMap<Integer, Object>> ret = new HashMap<String, SortedMap<Integer, Object>>();

		ret.put("VISIT", resultVisit);
		ret.put("INSPECTION", resultInspection);

		return ret;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = {"/{cavId}/{day}/{hour}/{carId}/{type}"}, method = RequestMethod.POST)
	public void schedule(@PathVariable Integer cavId, @PathVariable String day, @PathVariable int hour, @PathVariable Integer carId, @PathVariable String type) throws ParseException, JsonProcessingException, InvalidAttributeValueException {
		CalendarType calType = getType(type);

		Date date = parseToFormatDate(day);
		DayOfTheWeek weekDay = getWeekDay(date);

		Cav cav = getCav(cavId);
		HashMap<String, OpenHours> openHours = cav.getOpen_hours();

		try {
			verifyWeekDayAndOpenHours(openHours, weekDay);
		} catch (InvalidAttributeValueException ex) {
			throw new IllegalArgumentException("Not a work day for this CAV");
		}

		validateHour(openHours, weekDay, hour);

		Schedule cal = new Schedule();
		cal.setDay(date);
		cal.setHour(hour);
		cal.setType(calType.name());
		cal.setCav(cav);
		cal.setCar(getCar(carId));

		calendarRepo.save(cal);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = {"/{cavId}/{day}/{hour}/{type}"}, method = RequestMethod.DELETE)
	public void deleteSchedule(@PathVariable Integer cavId, @PathVariable String day, @PathVariable int hour, @PathVariable String type) throws ParseException {
		CalendarType calType = getType(type);

		Date date = parseToFormatDate(day);

		Schedule cal = calendarRepo.findByDayAndCavIdAndHourAndType(date, cavId, hour, calType.name());

		if (cal == null) {
			throw new NoResultException("Schedule not found");
		}

		calendarRepo.deleteById(cal.getId());
	}

	private DayOfTheWeek getWeekDay(Date day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		return DayOfTheWeek.of(dow);
	}

	private Date parseToFormatDate(String day) throws ParseException  {
		Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(day).getTime());

		if (!day.equals(date.toString())) {
			throw new IllegalArgumentException("Incorrect date");
		}
		return date;
	}

	private <T> T validate(Optional<T> op) {
		if (!op.isPresent()) {
			if (op.get() instanceof Car) {
				throw new NoResultException("Car not found");
			} else if (op.get() instanceof Cav) {
				throw new NoResultException("Cav not found");
			} else {
				throw new NoResultException("Object not found");
			}

		}
		return op.get();
	}

	private Car getCar(Integer id) {
		return validate(carRepo.findById(id));
	}

	private Cav getCav(Integer id) {
		return validate(cavRepo.findById(id));
	}

	private CalendarType getType(String type) {
		try {
			return CalendarType.valueOf(type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Type not found. Allowed types: \"visit\" or \"inspection\"");
		}
	}

	private void verifyWeekDayAndOpenHours(HashMap<String, OpenHours> openHours, DayOfTheWeek weekDay) throws InvalidAttributeValueException {
		String weekDayNameNormalized = weekDay.getNameLowerCase();
		if (openHours.get(weekDayNameNormalized) == null || openHours.get(weekDayNameNormalized).getBegin() == null || openHours.get(weekDayNameNormalized).getEnd() == null) {
			throw new InvalidAttributeValueException();
		}
	}

	private void validateHour(HashMap<String, OpenHours> openHours, DayOfTheWeek weekDay, int hour) {
		String weekDayNameNormalized = weekDay.getNameLowerCase();
		int begin = openHours.get(weekDayNameNormalized).getBegin();
		int end = openHours.get(weekDayNameNormalized).getEnd();
		if (hour < begin || hour > end) {
			throw new IllegalArgumentException("Invalid hour");
		}
	}
}
