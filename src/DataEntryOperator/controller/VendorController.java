package DataEntryOperator.controller;

import DataEntryOperator.model.Vendor;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorController {

    // Fetch all vendors
    public List<Vendor> fetchVendors() {
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT * FROM vendors";

        try (Connection connection = DatabaseConnection.GETConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                vendors.add(new Vendor(
                        resultSet.getInt("vendor_id"),
                        resultSet.getString("vendor_name"),
                        resultSet.getString("vendor_address"),
                        resultSet.getString("contact_no")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vendors;
    }

    // Add a new vendor
    public void addVendor(String name, String address, String contact) {
        String query = "INSERT INTO vendors (vendor_name, vendor_address, contact_no) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.GETConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, contact);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch a vendor by its ID
    public String[] getVendorById(int id) {
        String query = "SELECT * FROM vendors WHERE vendor_id = ?";
        String[] vendorData = new String[4]; // Assuming we have 4 fields: id, name, address, phone

        try (Connection connection = DatabaseConnection.GETConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                vendorData[0] = String.valueOf(resultSet.getInt("vendor_id"));
                vendorData[1] = resultSet.getString("vendor_name");
                vendorData[2] = resultSet.getString("vendor_address");
                vendorData[3] = resultSet.getString("contact_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vendorData;
    }

    // Update a vendor's information
    public void updateVendor(int id, String name, String address, String phone) {
        String query = "UPDATE vendors SET vendor_name = ?, vendor_address = ?, contact_no = ? WHERE vendor_id = ?";

        try (Connection connection = DatabaseConnection.GETConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, phone);
            statement.setInt(4, id);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteVendor(int vendorId) {
        String query = "DELETE FROM vendors WHERE vendor_id = ?";

        try (Connection connection = DatabaseConnection.GETConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, vendorId);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
