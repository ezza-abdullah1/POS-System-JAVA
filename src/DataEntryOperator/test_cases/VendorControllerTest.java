package DataEntryOperator.test_cases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

import DataEntryOperator.controller.VendorController;
import DataEntryOperator.model.Vendor;
import db.DatabaseConnection;

public class VendorControllerTest {
    private VendorController vendorController;

    @BeforeEach
    public void setUp() {
        vendorController = new VendorController();
    }

    @Test
    public void testFetchVendors_Success() throws SQLException {
        // Act
        List<Vendor> vendors = vendorController.fetchVendors();

        // Assert
        assertNotNull(vendors);
        assertFalse(vendors.isEmpty());

        // Optionally check the first vendor's details
        Vendor firstVendor = vendors.get(0);
        assertNotNull(firstVendor.getVendorName());
        assertNotNull(firstVendor.getVendorAddress());
        assertNotNull(firstVendor.getContactNo());
    }

    @Test
    public void testAddVendor_Success() throws SQLException {
        // Arrange
        String name = "Test Vendor";
        String address = "123 Test Street";
        String contact = "1234567890";

        // Act
        vendorController.addVendor(name, address, contact);

        // Assert
        try (Connection connection = DatabaseConnection.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM vendors WHERE vendor_name = '" + name + "'")) {

            assertTrue(rs.next(), "Vendor should be added to the database.");
            assertEquals(name, rs.getString("vendor_name"));
            assertEquals(address, rs.getString("vendor_address"));
            assertEquals(contact, rs.getString("contact_no"));

            // Cleanup: Delete the test vendor after checking
            stmt.executeUpdate("DELETE FROM vendors WHERE vendor_name = '" + name + "'");
        }
    }
}
