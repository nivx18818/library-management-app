package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtils {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final LocalDate currentLocalTime = LocalDate.now();

    public static final Locale locale = new Locale("en", "UK");
}
