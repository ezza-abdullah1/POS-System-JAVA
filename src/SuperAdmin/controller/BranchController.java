package SuperAdmin.controller;

import SuperAdmin.model.BranchModel;
import SuperAdmin.view.AddBranchManagerView;
import SuperAdmin.view.BranchManagerView;
import SuperAdmin.view.branchView;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class BranchController {
    public void loadbranchDataToView(branchView view) {
        List<BranchModel> allbranches = getAllBranches();

        for (BranchModel branch : allbranches) {
            view.addbranchToTable(branch);
        }
    }

    public void loadActiveBranchesToView(AddBranchManagerView view) {
        // Get active branch codes from BranchModel and load them into the view
        List<Integer> activeBranchCodes = BranchModel.getActiveBranchCodes();

        // Add active branches to the combo box in the view
        for (Integer branchCode : activeBranchCodes) {
            view.addBranchToComboBox("Branch " + branchCode, branchCode);
        }
    }

    public List<BranchModel> getAllBranches() {
        return BranchModel.getAllBranches();
    }

    public String saveBranch(String branchCode, String branchName, String city, String address, String phone,
            int numEmployees, boolean isActive) {
        try {
            BranchModel branch = new BranchModel(branchCode, branchName, city, address, phone, numEmployees, isActive);
            BranchModel.saveBranch(branch); // Assuming this method exists in the BranchModel class

            return "Branch saved successfully: " + branch.getBranchName();

        } catch (SQLIntegrityConstraintViolationException ex) {
            return "Error: Duplicate entry for Branch Code. Please use a different code.";
        } catch (Exception ex) {
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
            BranchModel.deleteBranch(branchCode);
            return "Branch deleted successfully."; // Return success message
        } catch (Exception e) {
            return "An error occurred"; // Return error message
        }
    }

    public String updateBranchStatus(int branchCode, boolean newStatus) {
        try {
            BranchModel.updateBranchStatus(branchCode, newStatus);
            return "Branch status updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
