package dao;

import java.sql.*;

import model.Product;
import utils.DatabaseConnection;

public class ProductDAO {
    private final DatabaseConnection dbConnection;

    public ProductDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public Product getProductById(String productId) throws SQLException {
        String productQuery = "SELECT p.name, p.price, p.tax, p.weight, COALESCE(d.discount, 0) as discount " +
                "FROM Product p LEFT JOIN Discount d ON p.product_id = d.product_id " +
                "WHERE p.product_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(productQuery)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProductId(productId);
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setTax(rs.getDouble("tax"));
                product.setWeight(rs.getDouble("weight"));
                product.setDiscount(rs.getDouble("discount"));
                return product;
            }
            return null;
        }
    }

    public boolean processCardPayment(String cardNumber, String securityCode, double amount) throws SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String cardQuery = "SELECT balance FROM Card WHERE card_number = ? AND security_code = ? FOR UPDATE";
                PreparedStatement cardStmt = conn.prepareStatement(cardQuery);
                cardStmt.setString(1, cardNumber);
                cardStmt.setString(2, securityCode);
                ResultSet cardResult = cardStmt.executeQuery();

                if (cardResult.next()) {
                    double balance = cardResult.getDouble("balance");
                    double totalWithFee = amount + 0.05;

                    if (balance >= totalWithFee) {
                        String updateQuery = "UPDATE Card SET balance = balance - ? WHERE card_number = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                        updateStmt.setDouble(1, totalWithFee);
                        updateStmt.setString(2, cardNumber);
                        updateStmt.executeUpdate();

                        conn.commit();
                        return true;
                    }
                }
                conn.rollback();
                return false;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void addPointsToMetroCard(String cardNumber, int points) throws SQLException {
        String query = "UPDATE MetroCard SET points = points + ? WHERE metCrdNum = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, points);
            stmt.setString(2, cardNumber);
            stmt.executeUpdate();
        }
    }

    public double processMetroCardPayment(String cardNumber, double amount) throws SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String cardQuery = "SELECT points FROM MetroCard WHERE metCrdNum = ? FOR UPDATE";
                PreparedStatement cardStmt = conn.prepareStatement(cardQuery);
                cardStmt.setString(1, cardNumber);
                ResultSet rs = cardStmt.executeQuery();

                if (rs.next()) {
                    int availablePoints = rs.getInt("points");
                    double maxDeductible = availablePoints / 30.0; // Points to Rs conversion
                    double deduction = Math.min(maxDeductible, amount);

                    // Update points
                    int pointsToDeduct = (int) (deduction * 30); // Rs to Points
                    String updateQuery = "UPDATE MetroCard SET points = points - ? WHERE metCrdNum = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, pointsToDeduct);
                    updateStmt.setString(2, cardNumber);
                    updateStmt.executeUpdate();

                    conn.commit();
                    return deduction;
                }
                conn.rollback();
                return 0.0;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }


}