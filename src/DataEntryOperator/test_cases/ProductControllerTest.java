package DataEntryOperator.test_cases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DataEntryOperator.controller.ProductController;
import utils.DatabaseConnection;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ProductControllerTest {
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        productController = new ProductController();
    }

    @Test
    public void testAddProduct_Success() throws SQLException {
        // Arrange
        String productId = "P001";
        int vendorId = 1;
        String name = "Test Product";
        String category = "Test Category";
        double originalPrice = 100.0;
        double salePrice = 80.0;
        double priceByUnit = 2.0;
        double priceByCarton = 20.0;
        double tax = 5.0;
        double weight = 1.5;
        int quantity = 50;

        // Act
        productController.addProduct(productId, vendorId, name, category, originalPrice, salePrice, priceByUnit,
                priceByCarton, tax, weight, quantity);

        // Assert
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE product_id = '" + productId + "'")) {

            assertTrue(rs.next(), "Product should be added to the database.");
            assertEquals(productId, rs.getString("product_id"));
            assertEquals(vendorId, rs.getInt("vendor_id"));
            assertEquals(name, rs.getString("name"));
            assertEquals(category, rs.getString("category"));
            assertEquals(originalPrice, rs.getDouble("original_price"));
            assertEquals(salePrice, rs.getDouble("sale_price"));
            assertEquals(priceByUnit, rs.getDouble("price_by_unit"));
            assertEquals(priceByCarton, rs.getDouble("price_by_carton"));
            assertEquals(tax, rs.getDouble("tax"));
            assertEquals(weight, rs.getDouble("weight"));
            assertEquals(quantity, rs.getInt("quantity"));

            // Cleanup: Delete the test product after checking
            stmt.executeUpdate("DELETE FROM Product WHERE product_id = '" + productId + "'");
        }
    }
}
