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
    private static final String DISPLAY_FORMAT = "dd/MM/yyyy";

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

    public static Date parseStringToDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_FORMAT);
                return sdf.parse(dateStr);
            } catch (ParseException ex) {
                System.out.println("Invalid date format. Expected format: yyyy-MM-dd or dd/MM/yyyy");
                return null;
            }
        }
    }

    public static String parseDateToString(Date originalDatum) {
        if (originalDatum == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(originalDatum);
    }
}
