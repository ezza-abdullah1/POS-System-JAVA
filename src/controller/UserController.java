package controller;

import model.UserModel;
import SuperAdmin.view.BranchManagerView; // Assuming you have different views for different roles
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;

public class UserController {
    private UserModel userModel;

    public UserController() {
        userModel = new UserModel(); // Assume UserModel handles the data manipulation
    }



    // Load user data based on role into the view
    public void loadUserDataToView(JTable viewTable,int branchCode, String role) {
        try {
            // Fetch data based on role from the model

            List<UserModel> users = userModel.getUserByBranchCodeAndRole(branchCode,role);

            // Clear the table before adding new data
            DefaultTableModel model = (DefaultTableModel) viewTable.getModel();
            model.setRowCount(0);

            for (UserModel user : users) {
                model.addRow(new Object[]{ user.getName(), user.getEmail(), user.getBranchCode(), user.getSalary(), user.getEmpNumber(),user.getCreatedAt(),user.getUpdatedAt()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add a new user based on role
    public void addUser(UserModel newUser) {
        try {
            String resultMessage = userModel.addUser(newUser);
            if (resultMessage.contains("added successfully")) {
                JOptionPane.showMessageDialog(null, resultMessage);
            } else {
                JOptionPane.showMessageDialog(null, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update a user's data based on role
    public void updateUser(UserModel updatedUser) {
        try {
            String resultMessage = userModel.updateUser(updatedUser);
            if (resultMessage.contains("updated successfully")) {
                JOptionPane.showMessageDialog(null, resultMessage);
            } else {
                JOptionPane.showMessageDialog(null, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String deleteUserByEmailAndEmpNumber(String email, int empNumber) {
        try {
            return userModel.deleteUserByEmailAndEmpNumber(email, empNumber);
        } catch (Exception e) {
            return "Error deleting user: " + e.getMessage();
        }
    }


    // Get user data based on branch code and role
    public String[] getUserByEmailAndRole(String email, String role) {
        try {
            return userModel.getUserByEmailAndRole(email, role);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching user by branch code and role: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Set the UserModel for the controller (for dependency injection)
    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
}
}
