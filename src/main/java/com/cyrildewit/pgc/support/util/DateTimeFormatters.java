package com.cyrildewit.pgc.support.util;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFormatters
{
    public static final String mariaDbDateTimePattern = "yyyy-LL-dd HH:mm:ss.S";
    public static final String dayMonthYearFormatPattern = "dd MMM yyyy";

    final DateTimeFormatter mariaDbDateTimeFormatter;
    final DateTimeFormatter dayMonthYearFormatter;

    public DateTimeFormatters() {
        this.mariaDbDateTimeFormatter = DateTimeFormatter.ofPattern(mariaDbDateTimePattern);
        this.dayMonthYearFormatter = DateTimeFormatter.ofPattern(dayMonthYearFormatPattern);
    }

    public DateTimeFormatter getMariaDbDateTimeFormatter() {return mariaDbDateTimeFormatter;}
    public DateTimeFormatter getDayMonthYearFormatter()
    {
        return dayMonthYearFormatter;
    }
}