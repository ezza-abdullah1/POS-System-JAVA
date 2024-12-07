package DataEntryOperator.controller;

import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProductController {
    public void addProduct(int vendorId, String name, String category, double originalPrice, double salePrice,
            double priceByUnit, double priceByCarton) {
        String query = "INSERT INTO products (vendor_id, name, category, original_price, sale_price, price_by_unit, price_by_carton) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, vendorId);
            statement.setString(2, name);
            statement.setString(3, category);
            statement.setDouble(4, originalPrice);
            statement.setDouble(5, salePrice);
            statement.setDouble(6, priceByUnit);
            statement.setDouble(7, priceByCarton);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
