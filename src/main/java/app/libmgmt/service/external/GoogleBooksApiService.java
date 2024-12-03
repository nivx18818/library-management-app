package app.libmgmt.service.external;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class GoogleBooksApiService {
    private static final String API_KEY = "AIzaSyCTL9h6r-BxBZ5_GRe0Gu5r2Ef8YWJWoGo"; 
       
    public static JSONObject searchBook(String title, Integer limit) {
        if (title == null || title.isEmpty()) {
            System.err.println("[ERROR] Title must not be null or empty.");
            return null;
        }

        try {
            String apiUrl = buildUrl(title, limit);

            HttpURLConnection connection = createHttpConnection(apiUrl);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("[ERROR] Failed to fetch data: HTTP error code " + responseCode);
                return null;
            }

            try (InputStream inputStream = connection.getInputStream()) {
                String response = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                return new JSONObject(response);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception occurred while fetching data: " + e.getMessage());
            return null;
        }
    }

    private static String buildUrl(String title, Integer limit) {
        String query = title.replace(" ", "+");
        return "https://www.googleapis.com/books/v1/volumes?q=" + query 
               + "&maxResults=" + limit 
               + "&key=" + API_KEY;
    }

    private static HttpURLConnection createHttpConnection(String apiUrl) throws Exception {
       
        URI uri = new URI(apiUrl); 
        URL url = uri.toURL(); 
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000); 
        connection.setReadTimeout(5000);    
        return connection;
    }
    
}
