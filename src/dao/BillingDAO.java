
//updated:

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.DatabaseConnection;

public class BillingDAO {
    private final DatabaseConnection dbConnection;

    public BillingDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void saveBill(double subtotal, double discount, double tax, double total, String metroCard) throws SQLException {
        String sql = "INSERT INTO billing (subtotal, discount, tax, total, metro_card) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, subtotal);
            statement.setDouble(2, discount);
            statement.setDouble(3, tax);
            statement.setDouble(4, total);

            // Handle metro card as optional (NULL if not provided)
            if (metroCard != null && !metroCard.trim().isEmpty()) {
                statement.setString(5, metroCard);
            } else {
                statement.setNull(5, java.sql.Types.VARCHAR);
            }

            statement.executeUpdate();
        }
    }
    public void saveBranchSales(int branchCode, double totalSalePrice, double profit, int startStock, int endStock, int stockChange) throws SQLException {
        String query = "INSERT INTO branchSales (BranchCode, TotalSalePrice, Profit, StartStock, EndStock, StockChange) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, branchCode);
            stmt.setDouble(2, totalSalePrice);
            stmt.setDouble(3, profit);
            stmt.setInt(4, startStock);
            stmt.setInt(5, endStock);
            stmt.setInt(6, stockChange);
            stmt.executeUpdate();
        }
    }

}