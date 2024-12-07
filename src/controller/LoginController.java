package controller;

import model.User;
import view.LoginView;
import view.ChangePasswordView;
import dao.LoginDAO;

import javax.swing.*;
import BranchManager.view.DashboardBR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        User user = loginDAO.authenticateUser(email, password, role);

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

    private void navigateToDashboard(String role) {
        switch (role) {
            case "SuperAdmin":
                // new SuperAdminDashboard().setVisible(true);
                break;
            case "BranchManager":
                new DashboardBR();
                break;
            case "Cashier":
                // new CashierDashboard().setVisible(true);
                break;
            case "DataEntryOperator":
                // new DEODashboard().setVisible(true);
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
