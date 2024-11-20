package app.libmgmt.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class GoogleBooksApiService {

    public static JSONObject searchBook(String title, Integer limit) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());

            String apiUrl = String.format("https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=%d", encodedTitle, limit);

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}