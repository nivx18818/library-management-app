package app.libmgmt.service;
import org.json.JSONObject;

import java.util.concurrent.*;

public class BooksSearchDatabaseService {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void searchBookAsync(String title, Integer limit, SearchResultCallback callback) {
        BookSearchDatabaseTask task = new BookSearchDatabaseTask(title, limit, "jdbc:sqlite:src/main/resources/database/database.db");
        Future<JSONObject> future = executorService.submit(task);

        new Thread(() -> {
            try {
                JSONObject result = future.get();
                if (result != null && result.getJSONArray("books").length() > 0) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure("No books found for the given title.");
                }
            } catch (InterruptedException | ExecutionException e) {
                callback.onFailure("Error occurred while fetching data: " + e.getMessage());
            }
        }).start();
    }

    public static void shutdownExecutorService() {
        executorService.shutdown();
    }

    public interface SearchResultCallback {
        void onSuccess(JSONObject result);
        void onFailure(String errorMessage);
    }
}
