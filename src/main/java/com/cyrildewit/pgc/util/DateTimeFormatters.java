package com.cyrildewit.pgc.util;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Service
@Scope("singleton")
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