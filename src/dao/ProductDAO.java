package dao;

import java.sql.*;

import model.Product;
import utils.DatabaseConnection;

public class ProductDAO {
    private final DatabaseConnection dbConnection;

    public ProductDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @SuppressWarnings("static-access")
    public Product getProductById(String productId) throws SQLException {
        String productQuery = "SELECT p.name, p.category, p.original_price, p.sale_price, p.price_by_unit, " +
                "p.price_by_carton, p.tax, p.weight, p.quantity, COALESCE(d.discount, 0) as discount " +
                "FROM Product p LEFT JOIN Discount d ON p.product_id = d.product_id " +
                "WHERE p.product_id = ?";

        try (Connection conn = dbConnection.GETConnection();
             PreparedStatement stmt = conn.prepareStatement(productQuery)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProductId(productId);
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setOriginalPrice(rs.getDouble("original_price"));
                product.setSalePrice(rs.getDouble("sale_price"));
                product.setPriceByUnit(rs.getDouble("price_by_unit"));
                product.setPriceByCarton(rs.getDouble("price_by_carton"));
                product.setTax(rs.getDouble("tax"));
                product.setWeight(rs.getDouble("weight"));
                product.setDiscount(rs.getDouble("discount"));
                product.setQuantity(rs.getInt("quantity"));
                return product;
            }
            return null;
        }
    }

    public boolean updateProductQuantity(String productId, int quantity) throws SQLException {
        String updateQuery = "UPDATE Product SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        try (@SuppressWarnings("static-access")
        Connection conn = dbConnection.GETConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, quantity);
            stmt.setString(2, productId);
            stmt.setInt(3, quantity); // Ensure enough stock is available
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean addNewProduct(Product product) throws SQLException {
        String insertQuery = "INSERT INTO Product (product_id, vendor_id, name, category, original_price, sale_price, " +
                "price_by_unit, price_by_carton, tax, weight, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.GETConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, product.getProductId());
            stmt.setInt(2, product.getVendorId());
            stmt.setString(3, product.getName());
            stmt.setString(4, product.getCategory());
            stmt.setDouble(5, product.getOriginalPrice());
            stmt.setDouble(6, product.getSalePrice());
            stmt.setDouble(7, product.getPriceByUnit());
            stmt.setDouble(8, product.getPriceByCarton());
            stmt.setDouble(9, product.getTax());
            stmt.setDouble(10, product.getWeight());
            stmt.setInt(11, product.getQuantity());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public boolean updateProductDetails(Product product) throws SQLException {
        String updateQuery = "UPDATE Product SET vendor_id = ?, name = ?, category = ?, original_price = ?, sale_price = ?, " +
                "price_by_unit = ?, price_by_carton = ?, tax = ?, weight = ?, quantity = ? WHERE product_id = ?";
        try (@SuppressWarnings("static-access")
        Connection conn = dbConnection.GETConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, product.getVendorId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setDouble(4, product.getOriginalPrice());
            stmt.setDouble(5, product.getSalePrice());
            stmt.setDouble(6, product.getPriceByUnit());
            stmt.setDouble(7, product.getPriceByCarton());
            stmt.setDouble(8, product.getTax());
            stmt.setDouble(9, product.getWeight());
            stmt.setInt(10, product.getQuantity());
            stmt.setString(11, product.getProductId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public boolean deleteProductById(String productId) throws SQLException {
        String deleteQuery = "DELETE FROM Product WHERE product_id = ?";
        try (@SuppressWarnings("static-access")
        Connection conn = dbConnection.GETConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setString(1, productId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
    public boolean processCardPayment(String cardNumber, String securityCode, double amount) throws SQLException {
        try (@SuppressWarnings("static-access")
        Connection conn = dbConnection.GETConnection()) {
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
        try (@SuppressWarnings("static-access")
        Connection conn = dbConnection.GETConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, points);
            stmt.setString(2, cardNumber);
            stmt.executeUpdate();
        }
    }

    public double processMetroCardPayment(String cardNumber, double amount) throws SQLException {
        try (Connection conn = dbConnection.GETConnection()) {
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