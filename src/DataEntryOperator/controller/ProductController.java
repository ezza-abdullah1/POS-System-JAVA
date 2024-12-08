package DataEntryOperator.controller;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProductController {
    public void addProduct(String productId, int vendorId, String name, String category, double originalPrice,
            double salePrice, double priceByUnit, double priceByCarton, double tax, double weight,
            int quantity) {
        String query = "INSERT INTO Product (product_id, vendor_id, name, category, original_price, sale_price, price_by_unit, price_by_carton, tax, weight, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, productId);
            statement.setInt(2, vendorId);
            statement.setString(3, name);
            statement.setString(4, category);
            statement.setDouble(5, originalPrice);
            statement.setDouble(6, salePrice);
            statement.setDouble(7, priceByUnit);
            statement.setDouble(8, priceByCarton);
            statement.setDouble(9, tax);
            statement.setDouble(10, weight);
            statement.setInt(11, quantity);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
