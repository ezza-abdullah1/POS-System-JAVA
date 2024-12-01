package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DatabaseConnection;

public class BillingDAO {
    private Connection connection;

    public BillingDAO() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBill(double subtotal, double discount, double tax, double total, String metroCard) throws SQLException {
        String sql = "INSERT INTO billing (subtotal, discount, tax, total, metro_card) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, subtotal);
            statement.setDouble(2, discount);
            statement.setDouble(3, tax);
            statement.setDouble(4, total);
            statement.setString(5, metroCard);
            statement.executeUpdate();
        }
    }
}
