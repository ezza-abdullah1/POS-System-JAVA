package TestCases.Cashier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.BillingDAO;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDAOTest {

    private BillingDAO billingDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize the BillingDAO and get a database connection
        billingDAO = new BillingDAO();
        connection = DatabaseConnection.getInstance().getConnection();

        // Start a transaction for each test
        connection.setAutoCommit(false);
    }

    // Utility method to clean up any inserted data after each test
    private void cleanupBillEntry(double subtotal, double discount, double tax, double total, String metroCard) throws SQLException {
        String sql = "DELETE FROM billing WHERE subtotal = ? AND discount = ? AND tax = ? AND total = ? AND metro_card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, subtotal);
            stmt.setDouble(2, discount);
            stmt.setDouble(3, tax);
            stmt.setDouble(4, total);
            stmt.setString(5, metroCard);
            stmt.executeUpdate();
        }
    }

    @Test
    public void testSaveBill_ValidData_StoresDataSuccessfully() throws SQLException {
        // Arrange
        double subtotal = 199.0;
        double discount = 10.0;
        double tax = 5.0;
        double total = 95.0;
        String metroCard = "CARD0001";

        // Act
        billingDAO.saveBill(subtotal, discount, tax, total, metroCard);

        // Verify the data was inserted
        String verifySql = "SELECT COUNT(*) FROM billing WHERE subtotal = ? AND discount = ? AND tax = ? AND total = ? AND metro_card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(verifySql)) {
            stmt.setDouble(1, subtotal);
            stmt.setDouble(2, discount);
            stmt.setDouble(3, tax);
            stmt.setDouble(4, total);
            stmt.setString(5, metroCard);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1), "Bill should be inserted successfully.");
        }

        // Cleanup the inserted data
        cleanupBillEntry(subtotal, discount, tax, total, metroCard);
    }

    @Test
    public void testSaveBill_NoMetroCard_StoresDataSuccessfully() throws SQLException {
        // Arrange
        double subtotal = 159.0;
        double discount = 10.0;
        double tax = 5.0;
        double total = 95.0;
        String metroCard = null;  // No metro card provided

        // Act
        billingDAO.saveBill(subtotal, discount, tax, total, metroCard);

        // Verify the data was inserted
        String verifySql = "SELECT COUNT(*) FROM billing WHERE subtotal = ? AND discount = ? AND tax = ? AND total = ? AND metro_card IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(verifySql)) {
            stmt.setDouble(1, subtotal);
            stmt.setDouble(2, discount);
            stmt.setDouble(3, tax);
            stmt.setDouble(4, total);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            assertEquals(1, rs.getInt(1), "Bill should be inserted successfully without metro card.");
        }

        // Cleanup the inserted data
        cleanupBillEntry(subtotal, discount, tax, total, metroCard);
    }


    @Test
    public void tearDown() throws SQLException {
        // Rollback the transaction to ensure changes are reverted
        connection.rollback();
    }
}
