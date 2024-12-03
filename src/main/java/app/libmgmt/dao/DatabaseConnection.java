package app.libmgmt.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static HikariDataSource dataSource;
    public static boolean isTesting = false;

    private DatabaseConnection() {
    }

    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            String jdbcUrl;

            jdbcUrl = isTesting
                    ? "jdbc:sqlite:file:tmpDb?mode=memory&cache=shared"
                    : "jdbc:sqlite:src/main/resources/database/database.db";

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setMaximumPoolSize(10);
            dataSource = new HikariDataSource(config);
        }

        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
