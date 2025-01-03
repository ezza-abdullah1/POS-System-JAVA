package dao;

import model.UserModel;
import java.sql.*;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public UserModel authenticateUserModel(String email, String password, String role) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ? AND Role = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UserModel UserModel = new UserModel();
                int UserId=rs.getInt("UserID");
                int BranchCode=rs.getInt("BranchCode");
                // UserModel.setUserID(rs.getInt("UserID"));
                UserModel.setUserID(UserId);
                UserModel.setName(rs.getString("Name"));
                UserModel.setEmail(rs.getString("Email"));
                UserModel.setPassword(rs.getString("Password"));
                UserModel.setRole(rs.getString("Role"));
                // UserModel.setBranchCode(rs.getInt("BranchCode"));
                UserModel.setBranchCode(BranchCode);
                UserModel.setSalary(rs.getDouble("Salary"));
                UserModel.setEmpNumber(rs.getInt("EmpNumber"));
                UserModel.setCreatedAt(rs.getTimestamp("CreatedAt"));
                UserModel.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                UserModel.setLoggedInUser(UserId, BranchCode);
                return UserModel;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePassword(String email, String newPassword) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
