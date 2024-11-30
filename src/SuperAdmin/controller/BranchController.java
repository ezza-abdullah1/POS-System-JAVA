package SuperAdmin.controller;

import SuperAdmin.model.BranchModel;
import SuperAdmin.view.branchView;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.swing.JOptionPane;

public class BranchController {
    public void loadbranchDataToView(branchView view) {
        List<BranchModel> allbranches = getAllBranches();

        for (BranchModel branch : allbranches) {
            view.addbranchToTable(branch);
        }
    }

    public List<BranchModel> getAllBranches() {
        // Retrieve branch data from the model
        return BranchModel.getAllBranches();
    }

    public String saveBranch(String branchCode, String branchName, String city, String address, String phone,
            int numEmployees, boolean isActive) {
        try {
            // Call the model's save method with numEmployees set to 0 and isActive set to
            // true
            BranchModel branch = new BranchModel(branchCode, branchName, city, address, phone, numEmployees, isActive);
            BranchModel.saveBranch(branch); // Assuming this method exists in the BranchModel class

            return "Branch saved successfully: " + branch.getBranchName();

        } catch (SQLIntegrityConstraintViolationException ex) {
            // If there's a duplicate branch code, show this error message
            return "Error: Duplicate entry for Branch Code. Please use a different code.";
        } catch (Exception ex) {
            // Handle other exceptions
            return "Error: " + ex.getMessage();
        }
    }

    public void updateBranch(String branchCode, String name, String city, String address, String phone,
            int numEmployees, boolean isActive) {
        try {
            BranchModel branch = BranchModel.findBranchByCode(branchCode); // Fetch branch
            if (branch == null) {
                throw new IllegalArgumentException("Branch not found with code: " + branchCode);
            }

            // Update branch details
            branch.setBranchName(name);
            branch.setCity(city);
            branch.setAddress(address);
            branch.setPhone(phone);
            branch.setNumEmployees(numEmployees); // Ensure logical validation elsewhere if needed
            branch.setActive(isActive);

            // Save the updated branch data back to storage
            BranchModel.updateBranch(branch);
        } catch (Exception e) {
            throw new RuntimeException("Error updating branch: " + e.getMessage());
        }
    }

    public String[] getBranchByCode(String branchCode) {
        try {
            BranchModel branch = BranchModel.findBranchByCode(branchCode); // Fetch branch data
            if (branch == null) {
                throw new IllegalArgumentException("Branch not found with code: " + branchCode);
            }

            // Convert branch data into a string array to populate fields
            return new String[] {
                    branch.getBranchCode(),
                    branch.getBranchName(),
                    branch.getCity(),
                    branch.getAddress(),
                    branch.getPhone(),
                    String.valueOf(branch.getNumEmployees()),
                    String.valueOf(branch.isActive())
            };

        } catch (Exception e) {
            throw new RuntimeException("Error fetching branch: " + e.getMessage());
        }
    }

    public String deleteBranch(int branchCode) {
        try {
            // Attempt to delete the branch using the model
            BranchModel.deleteBranch(branchCode);
            return "Branch deleted successfully."; // Return success message
        } catch (Exception e) {
            // Catch any other general exceptions that may occur
            return "An error occurred"; // Return error message
        }
    }

    public String updateBranchStatus(int branchCode, boolean newStatus) {
        try {
            // Call the model to update the status
            BranchModel.updateBranchStatus(branchCode, newStatus);
            return "Branch status updated successfully.";
        } catch (Exception e) {
            // Return an error message if something goes wrong
            return "Error: " + e.getMessage();
        }
    }

}
