package app.libmgmt.util;

import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class RegExPatterns {

    public static boolean namePattern(String name) {
        return Pattern.matches("^(?!\\s*$)(?=.*[a-zA-Z])[^0-9]*$", name);
    }

    public static boolean emailPattern(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
    }

    public static boolean passwordPattern(String password) {
        System.out.println("Password: " + password); // Debug line
        return Pattern.matches("^.{8,}$", password);
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

    public static boolean bookCoverUrlPattern(String path) {
        if (!path.startsWith("http")) {
            return false;
        }

        String urlWithoutParams = path.split("\\?")[0].toLowerCase();

        if (urlWithoutParams.endsWith(".jpg") ||
            urlWithoutParams.endsWith(".jpeg") ||
            urlWithoutParams.endsWith(".png") ||
            urlWithoutParams.endsWith(".gif") ||
            urlWithoutParams.endsWith(".bmp") ||
            urlWithoutParams.endsWith(".svg")) {
            return true;
        }
        
        try {
            URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            String contentType = connection.getContentType();
            return contentType != null && contentType.startsWith("image/");
        } catch (URISyntaxException e) {
            System.err.println("Invalid URL syntax: " + e.getMessage());
            return false;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean globalFormPattern(String text) {
        return Pattern.matches("^.*$\n", text);
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
