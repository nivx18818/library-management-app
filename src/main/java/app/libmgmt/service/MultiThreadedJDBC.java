package app.libmgmt.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedJDBC {

    // JDBC URL
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/database/database.db";

    // Thread pool size
    private static final int THREAD_POOL_SIZE = 4;

    private final ExecutorService executorService;

    public MultiThreadedJDBC() {
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void executeQuery(String query) {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(() -> {
                try (Connection connection = DriverManager.getConnection(DATABASE_URL);
                     PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        resultSet.getObject(1); // Fetch the first column
                    }

                } catch (Exception e) {
                    System.err.println("Error executing query: " + e.getMessage());
                }
            });
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}