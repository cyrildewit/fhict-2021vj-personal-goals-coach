package com.cyrildewit.pgc.util;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Service
@Scope("singleton")
public class DateTimeFormatters
{
    public static final String dayMonthYearFormatPattern = "dd MMM yyyy";

    final DateTimeFormatter dayMonthYearFormatter;

    public DateTimeFormatters() {
        this.dayMonthYearFormatter = DateTimeFormatter.ofPattern(dayMonthYearFormatPattern);
    }

    public DateTimeFormatter getDayMonthYearFormatter()
    {
        return this.dayMonthYearFormatter;
    }
}