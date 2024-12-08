package app.libmgmt.service.external;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksApiService {
    private static final String API_KEY = "AIzaSyCTL9h6r-BxBZ5_GRe0Gu5r2Ef8YWJWoGo"; 
       
    public static JSONObject searchBook(String title, Integer limit) {
        if (title == null || title.isEmpty()) {
            System.err.println("[ERROR] Title must not be null or empty.");
            return null;
        }

        try {
            String apiUrl = buildUrlWithIntitleFilter(title, limit);

            HttpURLConnection connection = createHttpConnection(apiUrl);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("[ERROR] Failed to fetch data: HTTP error code " + responseCode);
                return null;
            }

            try (InputStream inputStream = connection.getInputStream()) {
                String response = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray items = jsonResponse.optJSONArray("items");
                if (items != null) {
                    JSONArray uniqueItems = removeDuplicateBooks(items);
                    jsonResponse.put("items", uniqueItems);
                }
                return jsonResponse;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception occurred while fetching data: " + e.getMessage());
            return null;
        }
    }

    private static String buildUrlWithIntitleFilter(String title, Integer limit) {
        StringBuilder urlBuilder = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=intitle:");
        urlBuilder.append(title.replace(" ", "+"));
        if (limit != null) {
            urlBuilder.append("&maxResults=").append(limit);
        }
        urlBuilder.append("&key=").append(API_KEY);
        return urlBuilder.toString();
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

    private static JSONArray removeDuplicateBooks(JSONArray booksArray) {
        Set<String> seenIds = new HashSet<>();
        JSONArray filteredBooks = new JSONArray();

        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject book = booksArray.getJSONObject(i);
            String bookId = book.optString("id"); // Unique identifier from API
            if (!seenIds.contains(bookId)) {
                seenIds.add(bookId);
                filteredBooks.put(book);
            }
        }
        return filteredBooks;
    }
}
