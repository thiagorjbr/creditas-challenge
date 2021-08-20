package com.creditas.challenge.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "Calendar")
public class Schedule extends BaseEntity<Schedule>{
	
	private Date day;
	
	private Integer hour;
	
	@ManyToOne
	private Cav cav;
	
	private String type;
	
	@ManyToOne
	private Car car;

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Cav getCav() {
		return cav;
	}

	public void setCav(Cav cav) {
		this.cav = cav;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@Override
	public void merge(Schedule another) {
		this.setCar(another.getCar() == null ? this.getCar() : another.getCar());
		this.setCav(another.getCav() == null ? this.getCav() : another.getCav());
		this.setHour(another.getHour() == null ? this.getHour() : another.getHour());
		this.setDay(another.getDay() == null ? this.getDay() : another.getDay());
		this.setType(another.getType() == null ? this.getType() : another.getType());		
	}
}
