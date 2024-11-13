package app.libmgmt.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class

DateTimeUtils {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM" +
            "/yyyy");

    public static final LocalDate currentLocalTime = LocalDate.now();

    public static final Locale locale = new Locale("en", "UK");

    public static LocalDate convertStringToDate(String originalDatum) {
        try {
            return LocalDate.parse(originalDatum, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String convertDateToString(LocalDate originalDatum) {
        return dateTimeFormatter.format(originalDatum);
    }

}
