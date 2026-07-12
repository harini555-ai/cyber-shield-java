package com.cybershield.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages JDBC connection lifecycle to MySQL for Cyber Shield.
 */
public class DatabaseConnection {
    private static final String URL = resolveConfig(
            "DB_URL", "db.url",
            "jdbc:mysql://localhost:3306/cyber_shield?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = resolveConfig("DB_USER", "db.user", "root");
    private static final String PASSWORD = resolveConfig("DB_PASSWORD", "db.password", "Haru#123#"); // Default root password, adjustable via env vars, -D system properties, or below

    /**
     * Resolves a config value, checking (in order): environment variable, JVM system property, then the default.
     * This lets DB credentials be fixed per-machine (env vars or -Ddb.user=...) without editing source code.
            return fromProperty;
        }
        return defaultValue;
    }

    private static Connection connection = null;

    /**
     * Establishes and returns a connection to the database.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Ensure it is in the pom.xml dependency list.");
            e.printStackTrace();
        }

        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Successfully connected to MySQL database: cyber_shield");
            } catch (SQLException e) {
                System.err.println("Failed to connect to local database. Make sure MySQL is running on port 3306 and username/password match.");
                throw e;
            }
        }
        return connection;
    }

    /**
     * Closes the active database connection safely.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("MySQL connection closed successfully.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing MySQL connection.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates database and tables programmatically if they don't exist to ensure smooth execution.
     */
    public static void initializeDatabaseSchema() {
        String baseDbUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection conn = DriverManager.getConnection(baseDbUrl, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS cyber_shield");
            System.out.println("Database 'cyber_shield' verified/created.");
            
            // Reconnect directly to cyber_shield DB
            try (Connection dbConn = getConnection();
                 Statement dbStmt = dbConn.createStatement()) {
                
                // Create users table
                String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "username VARCHAR(50) NOT NULL UNIQUE," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                dbStmt.executeUpdate(createUsersTable);
                
                // Create scores table
                String createScoresTable = "CREATE TABLE IF NOT EXISTS scores (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "username VARCHAR(50) NOT NULL," +
                        "score INT NOT NULL," +
                        "badge VARCHAR(30) NOT NULL," +
                        "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE" +
                        ")";
                dbStmt.executeUpdate(createScoresTable);
                System.out.println("Tables 'users' and 'scores' verified/created in database.");
            }
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed (is your MySQL server running?): " + e.getMessage());
        }
    }
}
