package app.libmgmt.util;

import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtils {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy");

    public static final LocalDate currentLocalTime = LocalDate.now();

    public static final Locale locale = Locale.of("en", "UK");

    public static LocalDate parseStringToLocalDate(String originalDatum) {
        try {
            return LocalDate.parse(originalDatum, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String parseLocalDateToString(LocalDate originalDatum) {
        return dateTimeFormatter.format(originalDatum);
    }

    public static Date parseLocalDateToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public static Date parseStringToDate(String originalDatum) {
        LocalDate localDate = parseStringToLocalDate(originalDatum);
        return localDate != null ? parseLocalDateToDate(localDate) : null;
    }
}
