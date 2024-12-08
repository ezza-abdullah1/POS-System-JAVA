package DataEntryOperator.controller;

import DataEntryOperator.model.Vendor;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorController {
    public List<Vendor> fetchVendors() {
        List<Vendor> vendors = new ArrayList<>();
        String query = "SELECT * FROM vendors";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
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

    public void addVendor(String name, String address, String contact) {
        String query = "INSERT INTO vendors (vendor_name, vendor_address, contact_no) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, contact);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
