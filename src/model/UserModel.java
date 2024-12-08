package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import utils.DatabaseConnection;

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

    // Fetch all users based on role from the database
    public List<UserModel> getAllUsersByRole(String role) throws SQLException {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, role);
            ResultSet resultSet = stmt.executeQuery();

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
                users.add(user);
                System.out.println(resultSet.getTimestamp("CreatedAt"));
            }
        }

        return users;
    }

    // Add a new user to the database
    public String addUser(UserModel newUser) {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE Role = ? AND BranchCode = ?";
        String insertQuery = "INSERT INTO users (Name, Email, Password, Role, BranchCode, Salary, EmpNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateBranchQuery = "UPDATE branches SET NumEmployees = NumEmployees + 1 WHERE BranchCode = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            if (newUser.getRole().equals("BranchManager")) {
                // Check if a branch manager already exists for the branch code
                try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, newUser.getRole());
                    checkStmt.setInt(2, newUser.getBranchCode());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "A Branch Manager already exists for this branch.";
                    }
                }
            }

            // Add the user
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, newUser.getName());
                insertStmt.setString(2, newUser.getEmail());
                insertStmt.setString(3, newUser.getPassword());
                insertStmt.setString(4, newUser.getRole());
                insertStmt.setInt(5, newUser.getBranchCode());
                insertStmt.setDouble(6, newUser.getSalary());
                insertStmt.setInt(7, newUser.getEmpNumber());

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Increment NumEmployees in the branches table
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateBranchQuery)) {
                        updateStmt.setInt(1, newUser.getBranchCode());
                        int branchUpdate = updateStmt.executeUpdate();
                        if (branchUpdate > 0) {
                            return newUser.getRole() + " added successfully, and employee count updated.";
                        } else {
                            return newUser.getRole() + " added successfully, but failed to update employee count.";
                        }
                    }
                } else {
                    return "Failed to add " + newUser.getRole() + ".";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding user: " + e.getMessage();
        }
    }

    // Update a user's data
    public String updateUser(UserModel updatedUser) {
        String query = "UPDATE users SET Name = ?, Email = ?, Salary = ? WHERE BranchCode = ? AND Role = ? AND EmpNumber =?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, updatedUser.getName());
            stmt.setString(2, updatedUser.getEmail());
            stmt.setDouble(3, updatedUser.getSalary());
            stmt.setInt(4, updatedUser.getBranchCode());
            stmt.setString(5, updatedUser.getRole());
            stmt.setInt(6, updatedUser.getEmpNumber());


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return updatedUser.getRole() + " updated successfully.";
            } else {
                return "Failed to update " + updatedUser.getRole() + ".";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating user: " + e.getMessage();
        }
    }

    // Fetch user by email and role
    public String[] getUserByEmailAndRole(String email, String role) {
        String[] userData = new String[8]; // Adjust size based on the number of fields needed
        String query = "SELECT UserID, Name, Email, Password, Role, BranchCode, Salary, EmpNumber FROM users WHERE Email = ? AND Role = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set parameters
            stmt.setString(1, email);
            stmt.setString(2, role);

            // Execute query
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Populate the String array with user data
                userData[0] = String.valueOf(resultSet.getInt("UserID")); // UserID
                userData[1] = resultSet.getString("Name"); // Name
                userData[2] = resultSet.getString("Email"); // Email
                userData[3] = resultSet.getString("Password"); // Password
                userData[4] = resultSet.getString("Role"); // Role
                userData[5] = String.valueOf(resultSet.getInt("BranchCode")); // BranchCode
                userData[6] = String.valueOf(resultSet.getDouble("Salary")); // Salary
                userData[7] = String.valueOf(resultSet.getInt("EmpNumber")); // Employee Number
            } else {
                JOptionPane.showMessageDialog(null, "No user found with the provided email and role.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching user by email and role: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return userData;
    }

    public String deleteUserByEmailAndEmpNumber(String email, int empNumber) {
        String query = "DELETE FROM users WHERE Email = ? AND EmpNumber = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email); // Set the Email parameter
            stmt.setInt(2, empNumber); // Set the EmpNumber parameter

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "User deleted successfully."; // Success message
            } else {
                return "User not found with the provided email and employee number."; // No user found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting user: " + e.getMessage(); // Error handling
        }
    }

    // Delete a user by branch code and role
    public String deleteUserByCodeAndRole(int branchCode, String role) {
        String query = "DELETE FROM users WHERE BranchCode = ? AND Role = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, branchCode);
            stmt.setString(2, role);
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

    // Fetch user by branch code and role
    public String[] getUserByBranchCodeAndRole(int branchCode, String role) {
        String[] userData = new String[8]; // Adjust size based on the number of fields you need
        String query = "SELECT Name, Email, EmpNumber, Salary FROM users WHERE BranchCode = ? AND Role = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, branchCode);
            stmt.setString(2, role);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Populate the String array with user data
                userData[1] = resultSet.getString("Name"); // Name
                userData[2] = resultSet.getString("Email"); // Email
                userData[6] = resultSet.getString("EmpNumber"); // Employee Number
                userData[7] = resultSet.getString("Salary"); // Salary
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching user by branch code and role: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return userData;
    }
}
