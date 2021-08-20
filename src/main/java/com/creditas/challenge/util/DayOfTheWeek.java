package com.creditas.challenge.util;

import java.time.DateTimeException;
import java.util.Locale;

public enum DayOfTheWeek {

    SUNDAY("domingo"), MONDAY("segunda"), TUESDAY("ter\u00E7a"), WEDNESDAY("quarta"), THURSDAY("quinta"), FRIDAY("sexta"), SATURDAY("s\u00E1bado");

    private String name;
    private DayOfTheWeek(String name) {
        this.name = name;
    }

	private static final DayOfTheWeek[] ENUMS = DayOfTheWeek.values();
	
	public static DayOfTheWeek of(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new DateTimeException("Invalid value for DayOfTheWeek: " + dayOfWeek);
        }
        return ENUMS[dayOfWeek - 1];
    }

    public String getName() {
        return name;
    }

    public String getNameLowerCase() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
