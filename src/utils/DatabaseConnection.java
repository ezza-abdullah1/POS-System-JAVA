package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/METRO";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "abc@123";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace(); // Or log the exception
        }
    }
    
    // Singleton pattern to get the instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    // Method to get the connection from the instance
    public Connection getConnection() {
        return connection;
    }
}
