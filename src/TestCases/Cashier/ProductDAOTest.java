package TestCases.Cashier;

import model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.ProductDAO;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

public class ProductDAOTest {

    private ProductDAO productDAO;
    private Product mockProduct;

    @BeforeEach
    public void setUp() {
        // Create a mock ProductDAO instance
        productDAO = mock(ProductDAO.class);

        // Create a mock Product object for some of the tests
        mockProduct = new Product();
        mockProduct.setProductId("P001");
        mockProduct.setName("Rice");
        mockProduct.setOriginalPrice(1200.0);
        mockProduct.setSalePrice(1000.0);
        mockProduct.setQuantity(85);
    }

    @Test
    public void testGetProductById_ValidId_ReturnsProduct() throws SQLException {
        String validProductId = "P001";  // Example of a valid product ID

        // Mock the behavior of getProductById
        when(productDAO.getProductById(validProductId)).thenReturn(mockProduct);

        Product product = productDAO.getProductById(validProductId);

        assertNotNull(product, "Product should not be null");
        assertEquals(validProductId, product.getProductId(), "Product ID should match");
        assertEquals("Rice", product.getName(), "Product name should match");
        assertEquals(1200.0, product.getOriginalPrice(), "Original price should match");
        assertEquals(1000.0, product.getSalePrice(), "Sale price should match");
        assertEquals(85, product.getQuantity(), "Quantity should match");
    }

    @Test
    public void testGetProductById_InvalidId_ReturnsNull() throws SQLException {
        String invalidProductId = "InvalidId";

        // Mock the behavior of getProductById for invalid ID
        when(productDAO.getProductById(invalidProductId)).thenReturn(null);

        Product product = productDAO.getProductById(invalidProductId);

        assertNull(product, "Product should be null for invalid ID");
    }

    @Test
    public void testAddNewProduct_ValidProduct_ReturnsTrue() throws SQLException {
        Product newProduct = new Product();
        newProduct.setProductId("P004");
        newProduct.setName("New Product");

        // Mock the behavior of addNewProduct
        when(productDAO.addNewProduct(newProduct)).thenReturn(true);

        boolean isAdded = productDAO.addNewProduct(newProduct);

        assertTrue(isAdded, "Product should be added successfully");
    }

    @Test
    public void testUpdateProductQuantity_ValidQuantity_ReturnsTrue() throws SQLException {
        String productId = "P001";
        int quantityToReduce = 5;

        // Mock the behavior of updateProductQuantity
        when(productDAO.updateProductQuantity(productId, quantityToReduce)).thenReturn(true);

        boolean isUpdated = productDAO.updateProductQuantity(productId, quantityToReduce);

        assertTrue(isUpdated, "Product quantity should be updated successfully");
    }

    @Test
    public void testUpdateProductQuantity_InsufficientStock_ReturnsFalse() throws SQLException {
        String productId = "P001";
        int quantityToReduce = 1000;  // Assuming stock is less than 1000

        // Mock the behavior of updateProductQuantity
        when(productDAO.updateProductQuantity(productId, quantityToReduce)).thenReturn(false);

        boolean isUpdated = productDAO.updateProductQuantity(productId, quantityToReduce);

        assertFalse(isUpdated, "Product quantity should not update when stock is insufficient");
    }

    @Test
    public void testUpdateProductDetails_ValidProduct_ReturnsTrue() throws SQLException {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("P001");
        updatedProduct.setName("Updated Product");

        // Mock the behavior of updateProductDetails
        when(productDAO.updateProductDetails(updatedProduct)).thenReturn(true);

        boolean isUpdated = productDAO.updateProductDetails(updatedProduct);

        assertTrue(isUpdated, "Product details should be updated successfully");
    }

    @Test
    public void testDeleteProductById_ValidId_ReturnsTrue() throws SQLException {
        String validProductId = "P003";  // Assuming P003 exists

        // Mock the behavior of deleteProductById
        when(productDAO.deleteProductById(validProductId)).thenReturn(true);

        boolean isDeleted = productDAO.deleteProductById(validProductId);

        assertTrue(isDeleted, "Product should be deleted successfully");
    }

    @Test
    public void testDeleteProductById_InvalidId_ReturnsFalse() throws SQLException {
        String invalidProductId = "InvalidId";

        // Mock the behavior of deleteProductById
        when(productDAO.deleteProductById(invalidProductId)).thenReturn(false);

        boolean isDeleted = productDAO.deleteProductById(invalidProductId);

        assertFalse(isDeleted, "Product deletion should fail for invalid product ID");
    }

    @Test
    public void testProcessCardPayment_SufficientBalance_ReturnsTrue() throws SQLException {
        String cardNumber = "1234567890123456";
        String securityCode = "123";
        double amount = 100.0;

        // Mock the behavior of processCardPayment
        when(productDAO.processCardPayment(cardNumber, securityCode, amount)).thenReturn(true);

        boolean paymentSuccessful = productDAO.processCardPayment(cardNumber, securityCode, amount);

        assertTrue(paymentSuccessful, "Card payment should be processed successfully");
    }

    @Test
    public void testProcessCardPayment_InsufficientBalance_ReturnsFalse() throws SQLException {
        String cardNumber = "1234567890123456";
        String securityCode = "123";
        double amount = 10000.0;  // Assuming insufficient balance

        // Mock the behavior of processCardPayment
        when(productDAO.processCardPayment(cardNumber, securityCode, amount)).thenReturn(false);

        boolean paymentSuccessful = productDAO.processCardPayment(cardNumber, securityCode, amount);

        assertFalse(paymentSuccessful, "Card payment should fail with insufficient balance");
    }

    @Test
    public void testProcessMetroCardPayment_SufficientPoints_ReturnsDeduction() throws SQLException {
        String cardNumber = "METRO1234";
        double amount = 90.0;

        // Mock the behavior of processMetroCardPayment
        when(productDAO.processMetroCardPayment(cardNumber, amount)).thenReturn(80.0);

        double deduction = productDAO.processMetroCardPayment(cardNumber, amount);

        assertTrue(deduction > 0, "Metro card payment should deduct points and return amount");
    }

    @Test
    public void testProcessMetroCardPayment_InsufficientPoints_ReturnsZero() throws SQLException {
        String cardNumber = "METRO1234";
        double amount = 1000.0;  // Assuming insufficient points

        // Mock the behavior of processMetroCardPayment
        when(productDAO.processMetroCardPayment(cardNumber, amount)).thenReturn(0.0);

        double deduction = productDAO.processMetroCardPayment(cardNumber, amount);

        assertEquals(0.0, deduction, "Metro card payment should return zero if insufficient points");
    }
}
