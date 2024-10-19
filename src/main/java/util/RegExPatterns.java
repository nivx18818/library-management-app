package util;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.regex.Pattern;

public class RegExPatterns {

    public static boolean namePattern(String name) {
        return Pattern.matches("^(?!\\s*$)(?=.*[a-zA-Z])[^0-9]*$", name);
    }

    public static boolean emailPattern(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
    }

    public static boolean passwordPattern(String password) {
        // "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
        return Pattern.matches("^(?!\\s*$).+", password);
    }

    public static boolean citizenIDPattern(String citizenID) {
        return Pattern.matches("^[0-9]{12}$", citizenID);
    }

    public static boolean phoneNumberPattern(String phoneNumber) {
        return Pattern.matches("^[0-9]{10}$", phoneNumber);
    }

    public static boolean studentIDPattern(String studentID) {
        return Pattern.matches("^[0-9]{8}$", studentID);
    }

    public static boolean bookIDPattern(String bookID) {
        return Pattern.matches("^[0-9]{1,8}$", bookID);
    }

    public static void checkUrlAsync(String urlString, Label label) {
        Task<Boolean> urlCheckTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("HEAD");
                    int responseCode = connection.getResponseCode();
                    return (responseCode >= 200 && responseCode < 400);
                } catch (Exception e) {
                    label.setText("Invalid URL");
                    Animation.playNotificationTimeline(label, 3, "red");
                }
                return null;
            }
        };

        urlCheckTask.setOnSucceeded(event -> {
            try {
                if (urlCheckTask.getValue()) {
                    System.out.println("URL is valid");
                } else {
                    System.out.println("URL is invalid");
                }
            } catch (Exception e) {
                System.out.println("URL is invalid or null");
            }
        });

        urlCheckTask.setOnFailed(event -> {
            System.out.println("URL check failed");
        });

        new Thread(urlCheckTask).start();
    }

    public static boolean globalFormPattern(String text) {
        return Pattern.matches("^.*$\n", text);
    }

    public static boolean datePattern(String dateStr) {
        DateTimeFormatter formatter = DateTimeUtils.dateTimeFormatter;

        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);

            int day = date.get(ChronoField.DAY_OF_MONTH);
            int month = date.get(ChronoField.MONTH_OF_YEAR);
            int year = date.get(ChronoField.YEAR);

            return true;

        } catch (DateTimeParseException e) {

            return false;
        }
    }

    public static void invalidPromptText(boolean status, TextField field, String errorMessage) {

        if (!status) {
            String originalPrompt = field.getPromptText();
            field.setText("");
            field.setPromptText(errorMessage);
            field.setStyle("-fx-prompt-text-fill: red; -fx-font-style: italic;");
            field.setOpacity(1);

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                field.setPromptText(originalPrompt);
                field.setStyle("-fx-prompt-text-fill: gray; -fx-font-style: normal;");
                field.setOpacity(0);
            });
            pause.play();
        }
    }
}
