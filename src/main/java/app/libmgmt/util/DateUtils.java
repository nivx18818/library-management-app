package app.libmgmt.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

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

    public static Date parseStringToDate(String originalDatum) {
        if (originalDatum == null || originalDatum.isEmpty()) {
            System.err.println("Invalid date format. Expected format: " + DATE_FORMAT);
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        try {
            return dateFormat.parse(originalDatum);
        } catch (ParseException e) {
            System.err.println("Invalid date format. Expected format: " + DATE_FORMAT);
            e.printStackTrace();
            return null;
        }
    }
}
