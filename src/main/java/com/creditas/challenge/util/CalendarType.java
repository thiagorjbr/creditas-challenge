package com.creditas.challenge.util;

public enum CalendarType {
	VISIT("visita"), INSPECTION("inspe\u00E7\u00E3o");

	private String name;
	CalendarType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
