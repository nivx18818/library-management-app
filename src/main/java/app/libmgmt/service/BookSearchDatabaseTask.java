package app.libmgmt.service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

public class BookSearchDatabaseTask implements Callable<JSONObject> {

    private final String title;
    private final Integer limit;
    private final String databaseUrl;

    public BookSearchDatabaseTask(String title, Integer limit, String databaseUrl) {
        this.title = title;
        this.limit = limit;
        this.databaseUrl = databaseUrl;
    }

    @Override
    public JSONObject call() throws Exception {
        return fetchBooksFromDatabase();
    }

    private JSONObject fetchBooksFromDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            String query = "SELECT isbn, title, published_date, publisher, cover_url, available_amount FROM Book WHERE title LIKE ? LIMIT ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + title + "%");
            statement.setInt(2, limit);

            ResultSet resultSet = statement.executeQuery();

            JSONArray booksArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject book = new JSONObject();
                book.put("isbn", resultSet.getString("isbn"));
                book.put("title", resultSet.getString("title"));
                book.put("published_date", resultSet.getString("published_date"));
                book.put("publisher", resultSet.getString("publisher"));
                book.put("cover_url", resultSet.getString("cover_url"));
                book.put("available_amount", resultSet.getInt("available_amount"));
                booksArray.put(book);
            }

            JSONObject result = new JSONObject();
            result.put("books", booksArray);
            return result;
        } catch (Exception e) {
            throw new Exception("Error fetching books from database: " + e.getMessage(), e);
        }
    }
}