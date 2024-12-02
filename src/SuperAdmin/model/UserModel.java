package SuperAdmin.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import db.DatabaseConnection;

public class UserModel {
    private int userID;
    private String name;
    private String email;
    private String password;
    private String role;
    private Integer branchCode;
    private Double salary;
    private Integer empNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(Integer empNumber) {
        this.empNumber = empNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Fetch all branch managers from the database
    public List<UserModel> getAllBranchManagers() throws SQLException {
        List<UserModel> branchManagers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'BranchManager'";

        try (Connection connection = DatabaseConnection.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                UserModel user = new UserModel();
                user.setUserID(resultSet.getInt("UserID"));
                user.setName(resultSet.getString("Name"));
                user.setEmail(resultSet.getString("Email"));
                user.setPassword(resultSet.getString("Password"));
                user.setRole(resultSet.getString("Role"));
                user.setBranchCode(resultSet.getInt("BranchCode"));
                user.setSalary(resultSet.getDouble("Salary"));
                user.setEmpNumber(resultSet.getInt("EmpNumber"));
                user.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
                user.setUpdatedAt(resultSet.getTimestamp("UpdatedAt"));
                branchManagers.add(user);
            }
        }

        return branchManagers;
    }

    // Add a new branch manager to the database
    public String addBranchManager(UserModel newBranchManager) {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE Role = 'BranchManager' AND BranchCode = ?";
        String insertQuery = "INSERT INTO users (Name, Email, Password, Role, BranchCode, Salary, EmpNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateBranchQuery = "UPDATE branches SET NumEmployees = NumEmployees + 1 WHERE BranchCode = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if a branch manager already exists for the branch code
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, newBranchManager.getBranchCode());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "A Branch Manager already exists for this branch.";
                }
            }

            // Add the branch manager
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, newBranchManager.getName());
                insertStmt.setString(2, newBranchManager.getEmail());
                insertStmt.setString(3, newBranchManager.getPassword());
                insertStmt.setString(4, newBranchManager.getRole());
                insertStmt.setInt(5, newBranchManager.getBranchCode());
                insertStmt.setDouble(6, newBranchManager.getSalary());
                insertStmt.setInt(7, newBranchManager.getEmpNumber());

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Increment NumEmployees in the branches table
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateBranchQuery)) {
                        updateStmt.setInt(1, newBranchManager.getBranchCode());
                        int branchUpdate = updateStmt.executeUpdate();
                        if (branchUpdate > 0) {
                            return "Branch Manager added successfully, and employee count updated.";
                        } else {
                            return "Branch Manager added successfully, but failed to update employee count.";
                        }
                    }
                } else {
                    return "Failed to add Branch Manager.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding branch manager: " + e.getMessage();
        }
    }

    // Update a branch manager's data
    public String updateBranchManager(UserModel updatedBranchManager) {
        String query = "UPDATE users SET Name = ?, Email = ?, Salary = ?, EmpNumber = ? WHERE  BranchCode = ? AND Role = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, updatedBranchManager.getName());
            stmt.setString(2, updatedBranchManager.getEmail());
            stmt.setString(6, updatedBranchManager.getRole());
            stmt.setInt(5, updatedBranchManager.getBranchCode());
            stmt.setDouble(3, updatedBranchManager.getSalary());
            stmt.setInt(4, updatedBranchManager.getEmpNumber());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Branch Manager updated successfully.";
            } else {
                return "Failed to update Branch Manager.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating branch manager: " + e.getMessage();
        }
    }

    // Delete a branch manager by email
    public String deleteBranchManagerByCode(int branchCode) {
        String query = "DELETE FROM users WHERE BranchCode = ? AND Role = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, branchCode);
            stmt.setString(2, "BranchManager");
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Branch Manager deleted successfully.";
            } else {
                return "Branch Manager not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting branch manager: " + e.getMessage();
        }
    }

    public String[] getBranchManagerByBranchCode(int branchCode) {
        String[] managerData = new String[8]; // Adjust size based on the number of fields you need
        String query = "SELECT Name, Email, EmpNumber, Salary FROM users WHERE BranchCode = ? AND Role = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, branchCode); // Assuming branchCode is a String in your system
            stmt.setString(2, "BranchManager");
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Populate the String array with manager data
                managerData[1] = resultSet.getString("Name"); // Name
                managerData[2] = resultSet.getString("Email"); // Email
                managerData[6] = resultSet.getString("EmpNumber"); // Employee Number
                managerData[7] = resultSet.getString("Salary"); // Salary
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching branch manager by branch code: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return managerData;
    }
}
