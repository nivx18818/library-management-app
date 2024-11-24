package app.libmgmt.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedJDBC {

    // JDBC URL - Make sure that the path to the database is correct
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/database/database.db";

    // Thread pool size
    private static final int THREAD_POOL_SIZE = 4;

    private final ExecutorService executorService;

    public MultiThreadedJDBC() {
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    // Method to execute a query with multiple threads
    public void executeQuery(String query) {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(() -> {
                String threadName = Thread.currentThread().getName();
                try (Connection connection = DriverManager.getConnection(DATABASE_URL);
                     PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    System.out.println("Thread " + threadName + " started executing the query.");

                    // Read data from ResultSet to ensure the query is executed
                    while (resultSet.next()) {
                        resultSet.getObject(1); 
                    }

                    System.out.println("Thread " + threadName + " finished executing the query.");

                } catch (Exception e) {
                    System.err.println("Thread " + threadName + " - Error executing query: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    // Method to shut down the ExecutorService after tasks are completed
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in the specified time.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
