package SuperAdmin.controller;

import SuperAdmin.model.UserModel;
import SuperAdmin.view.BranchManagerView;
import javax.swing.*;
import java.util.List;

public class UserController {
    private UserModel userModel;

    public UserController() {
        userModel = new UserModel(); // Assume UserModel handles the data manipulation
    }

    // Load branch manager data into the view
    public void loadBranchManagerDataToView(BranchManagerView view) {
        try {
            // Simulating data fetch from the model (e.g., from a database)
            List<UserModel> branchManagers = userModel.getAllBranchManagers(); // Assume getAllBranchManagers fetches
                                                                               // the data

            for (UserModel branchManager : branchManagers) {
                view.addBranchManagerToTable(branchManager); // Add each branch manager to the table in the view
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error loading data: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add a new branch manager
    public void addBranchManager(UserModel newBranchManager) {
        try {
            String resultMessage = userModel.addBranchManager(newBranchManager);
            if (resultMessage.equals("Branch Manager added successfully, and employee count updated.")) {
                JOptionPane.showMessageDialog(null, resultMessage);
            } else {
                JOptionPane.showMessageDialog(null, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding branch manager: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update a branch manager's data
    public void updateBranchManager(UserModel updatedBranchManager) {
        try {
            String resultMessage = userModel.updateBranchManager(updatedBranchManager);
            if (resultMessage.equals("Branch Manager updated successfully.")) {
                JOptionPane.showMessageDialog(null, resultMessage);
            } else {
                JOptionPane.showMessageDialog(null, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating branch manager: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete a branch manager
    public String deleteBranchManager(int branchcode) {
        try {
            return userModel.deleteBranchManagerByCode(branchcode); // Assume this method returns a success/error
                                                                    // message
        } catch (Exception e) {
            return "Error deleting branch manager: " + e.getMessage();
        }
    }

    // Set the UserModel for the controller (for dependency injection)
    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String[] getBranchManagerByBranchCode(int branchCode) {
        try {
            // Assume the getBranchManagerByBranchCode method in the UserModel returns a
            // String array
            return userModel.getBranchManagerByBranchCode(branchCode); // This should return a String array
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching branch manager by branch code: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
