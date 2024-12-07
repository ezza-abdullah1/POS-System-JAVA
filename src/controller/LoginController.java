package controller;

import model.UserModel;
import view.LoginView;
import view.ChangePasswordView;
import dao.LoginDAO;

import javax.swing.*;
import BranchManager.view.DashboardBR;
import DataEntryOperator.view.MainDashboard;
import SuperAdmin.view.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class LoginController {
    private LoginView loginView;
    private LoginDAO loginDAO;

    public LoginController(LoginView loginView, LoginDAO loginDAO) {
        this.loginView = loginView;
        this.loginDAO = loginDAO;

        this.loginView.addLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("here bef au");
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        String email = loginView.getEmail();
        String password = loginView.getPassword();
        String role = loginView.getRole();
        System.out.println("here in au");

        UserModel user = loginDAO.authenticateUserModel(email, password, role);

        if (user != null) {
            if ("123".equals(password)) {
                showChangePasswordScreen(email);
            } else {
                navigateToDashboard(role);
                loginView.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(loginView, "Invalid email or password!");
        }
    }

    private void showChangePasswordScreen(String email) {
        ChangePasswordView changePasswordView = new ChangePasswordView(email);
        changePasswordView.setVisible(true);

        changePasswordView.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = changePasswordView.getNewPassword();
                String confirmPassword = changePasswordView.getConfirmPassword();

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(changePasswordView, "Passwords do not match!");
                } else if (!isValidPassword(newPassword)) {
                    JOptionPane.showMessageDialog(changePasswordView, "Password must be at least 8 characters long, contain at least one uppercase letter, and one special character.");
                } else {
                    boolean isUpdated = loginDAO.updatePassword(email, newPassword);
                    if (isUpdated) {
                        JOptionPane.showMessageDialog(changePasswordView, "Password changed successfully!");
                        changePasswordView.dispose();
                    } else {
                        JOptionPane.showMessageDialog(changePasswordView, "Failed to update password. Please try again.");
                    }
                }
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9]");

        return upperCasePattern.matcher(password).find() && specialCharacterPattern.matcher(password).find();
    }


    private void navigateToDashboard(String role) {
        switch (role) {
            case "SuperAdmin":
                 new dashboard();
                break;
            case "BranchManager":
                new DashboardBR();
                break;
            case "Cashier":
                // new CashierDashboard().setVisible(true);
                break;
            case "DataEntryOperator":
                new MainDashboard();
                break;
            default:
                JOptionPane.showMessageDialog(loginView, "Role not found!");
                break;
        }
    }

    public static void main(String[] args) {
        LoginView loginView = new LoginView("BranchManager");
        LoginDAO loginDAO = new LoginDAO();
        new LoginController(loginView, loginDAO);
        loginView.setVisible(true);
    }
}
