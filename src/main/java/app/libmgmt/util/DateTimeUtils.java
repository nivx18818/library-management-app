package app.libmgmt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class

DateTimeUtils {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM" +
            "/yyyy");

    public static final LocalDate currentLocalTime = LocalDate.now();

    public static final Locale locale = Locale.of("en", "UK");

    public static LocalDate convertStringToLocalDate(String originalDatum) {
        try {
            return LocalDate.parse(originalDatum, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String convertLocalDateToString(LocalDate originalDatum) {
        return dateTimeFormatter.format(originalDatum);
    }

    public static Date convertStringToDate(String originalDatum) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(originalDatum);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String convertDateToString(Date originalDatum) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(originalDatum);
    }

    public static boolean isLessThanTwoWeeks(Date borrowedDate) {
        if (borrowedDate == null) {
            return false;
        }

        Date currentDate = new Date();
        long diffInMillis = currentDate.getTime() - borrowedDate.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        return diffInDays <= 14;
    }

}
