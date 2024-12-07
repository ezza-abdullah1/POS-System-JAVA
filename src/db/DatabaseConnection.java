package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/metro"; // Replace with your DB URL
    private static final String DB_USERNAME = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "4820"; // Replace with your DB password

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver (optional in newer JDBC versions)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Return a connection object
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found", e);
        }
    }
}
