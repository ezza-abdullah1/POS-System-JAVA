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
import utils.DatabaseConnection;

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
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
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

    @Test
    public void testUpdateVendor_Success() throws SQLException {
        // Arrange
        String originalName = "Original Vendor";
        String originalAddress = "456 Original Street";
        String originalContact = "9876543210";
        String updatedName = "Updated Vendor";
        String updatedAddress = "789 Updated Street";
        String updatedContact = "0123456789";

        vendorController.addVendor(originalName, originalAddress, originalContact);

        int vendorId;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT vendor_id FROM vendors WHERE vendor_name = '" + originalName + "'")) {
            assertTrue(rs.next(), "Test vendor should exist in the database.");
            vendorId = rs.getInt("vendor_id");
        }

        vendorController.updateVendor(vendorId, updatedName, updatedAddress, updatedContact);

        // Assert
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM vendors WHERE vendor_id = " + vendorId)) {
            assertTrue(rs.next(), "Updated vendor should exist in the database.");
            assertEquals(updatedName, rs.getString("vendor_name"));
            assertEquals(updatedAddress, rs.getString("vendor_address"));
            assertEquals(updatedContact, rs.getString("contact_no"));
        }

        vendorController.deleteVendor(vendorId);
    }

    @Test
    public void testDeleteVendor_Success() throws SQLException {
        String name = "Vendor To Delete";
        String address = "123 Delete Street";
        String contact = "1234567890";

        vendorController.addVendor(name, address, contact);

        int vendorId;
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT vendor_id FROM vendors WHERE vendor_name = '" + name + "'")) {
            assertTrue(rs.next(), "Vendor to delete should exist in the database.");
            vendorId = rs.getInt("vendor_id");
        }

        vendorController.deleteVendor(vendorId);

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM vendors WHERE vendor_id = " + vendorId)) {
            assertFalse(rs.next(), "Vendor should be deleted from the database.");
        }
    }
}
