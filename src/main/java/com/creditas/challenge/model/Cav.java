package com.creditas.challenge.model;

import java.util.HashMap;

import javax.persistence.Convert;
import javax.persistence.Entity;

import com.creditas.challenge.util.OpenHoursConverter;
import com.fasterxml.jackson.annotation.JsonAlias;

@Entity
public class Cav extends BaseEntity<Cav>{
	
	private String name;

	@JsonAlias({"open_hours", "openHours"})
	@Convert(converter = OpenHoursConverter.class)
	private HashMap<String, OpenHours> open_hours;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, OpenHours> getOpen_hours() {
		return open_hours;
	}

	public void setOpen_hours(HashMap<String, OpenHours> open_hours) {
		this.open_hours = open_hours;
	}

	@Override
	public void merge(Cav another) {
		this.setName(another.getName() == null ? this.getName() : another.getName());
		this.setOpen_hours(another.getOpen_hours() == null ? this.getOpen_hours() : another.getOpen_hours());
	}

	public static class OpenHours {

		@JsonAlias({"inicio", "Inicio", "begin", "Begin"})
		private Integer begin;

		@JsonAlias({"fim", "Fim", "end", "End"})
		private Integer end;

		public Integer getBegin() {
			return begin;
		}
		public void setBegin(Integer begin) {
			this.begin = begin;
		}
		public Integer getEnd() {
			return end;
		}
		public void setEnd(Integer end) {
			this.end = end;
		}
	}
}
