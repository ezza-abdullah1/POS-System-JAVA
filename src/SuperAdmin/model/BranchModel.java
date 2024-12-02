package SuperAdmin.model;

import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class BranchModel {
    private int branchID;
    private String branchCode;
    private String branchName;
    private String city;
    private String address;
    private String phone;
    private int numEmployees;
    private boolean isActive;

    // Constructor
    public BranchModel(String branchCode, String branchName, String city,
            String address, String phone, int numEmployees, boolean isActive) {
        this.branchID = branchID;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.numEmployees = numEmployees;
        this.isActive = isActive;
    }

    // Getters and setters
    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(int numEmployees) {
        this.numEmployees = numEmployees;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static List<BranchModel> getAllBranches() {
        List<BranchModel> branches = new ArrayList<>();
        String query = "SELECT BranchID, BranchCode, BranchName, City, Address, Phone, NumEmployees, IsActive FROM branches";

        try (Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int branchID = resultSet.getInt("BranchID");
                String branchCode = resultSet.getString("BranchCode");
                String branchName = resultSet.getString("BranchName");
                String city = resultSet.getString("City");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                int numEmployees = resultSet.getInt("NumEmployees");
                boolean isActive = resultSet.getBoolean("IsActive");

                branches.add(new BranchModel(branchCode, branchName, city, address, phone, numEmployees,
                        isActive));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching branches from the database: " + e.getMessage());
        }

        return branches;
    }

    public static BranchModel findBranchByCode(String branchCode) {
        String query = "SELECT BranchID, BranchCode, BranchName, City, Address, Phone, NumEmployees, IsActive FROM branches WHERE BranchCode = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, branchCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int branchID = resultSet.getInt("BranchID");
                String branchName = resultSet.getString("BranchName");
                String city = resultSet.getString("City");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                int numEmployees = resultSet.getInt("NumEmployees");
                boolean isActive = resultSet.getBoolean("IsActive");

                return new BranchModel(branchCode, branchName, city, address, phone, numEmployees, isActive);
            } else {
                System.out.println("No branch found with code: " + branchCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error finding branch by code: " + e.getMessage());
        }

        return null;
    }

    public static void saveBranch(BranchModel branch) throws SQLException {
        String query = "INSERT INTO branches (BranchCode, BranchName, City, Address, Phone, NumEmployees, IsActive) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, branch.getBranchCode());
        preparedStatement.setString(2, branch.getBranchName());
        preparedStatement.setString(3, branch.getCity());
        preparedStatement.setString(4, branch.getAddress());
        preparedStatement.setString(5, branch.getPhone());
        preparedStatement.setInt(6, branch.getNumEmployees());
        preparedStatement.setBoolean(7, branch.isActive());

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Branch saved successfully: " + branch.getBranchName());
        } else {
            System.out.println("Failed to save branch: " + branch.getBranchName());
        }

    }

    public static void deleteBranch(int branchcode) {
        String query = "DELETE FROM branches WHERE BranchCode = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, branchcode);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Branch deleted successfully with ID: " + branchcode);
            } else {
                System.out.println("No branch found with ID: " + branchcode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting branch: " + e.getMessage());
        }
    }

    public static void updateBranch(BranchModel branch) {
        String query = "UPDATE branches SET BranchName = ?, City = ?, Address = ?, Phone = ?, NumEmployees = ?, IsActive = ? WHERE BranchCode = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(7, branch.getBranchCode());
            preparedStatement.setString(1, branch.getBranchName());
            preparedStatement.setString(2, branch.getCity());
            preparedStatement.setString(3, branch.getAddress());
            preparedStatement.setString(4, branch.getPhone());
            preparedStatement.setInt(5, branch.getNumEmployees());
            preparedStatement.setBoolean(6, branch.isActive());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Branch updated successfully: " + branch.getBranchName());
            } else {
                System.out.println("Failed to update branch. No branch found with ID: " + branch.getBranchID());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating branch: " + e.getMessage());
        }
    }

    public static void updateBranchStatus(int branchCode, boolean newStatus) {
        String query = "UPDATE branches SET IsActive = ? WHERE BranchCode = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, newStatus);
            preparedStatement.setInt(2, branchCode);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Branch status updated successfully for branch code: " + branchCode);
            } else {
                System.out.println("No branch found with code: " + branchCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating branch status: " + e.getMessage());
        }
    }

    public static List<Integer> getActiveBranchCodes() {
        List<Integer> activeBranchCodes = new ArrayList<>();
        String query = "SELECT BranchCode FROM branches WHERE IsActive = '1'";

        try (Connection connection = DatabaseConnection.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int branchCode = resultSet.getInt("BranchCode");
                activeBranchCodes.add(branchCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching active branches: " + e.getMessage());
        }

        return activeBranchCodes;
    }

}
