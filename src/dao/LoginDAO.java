package dao;

import model.User;
import java.sql.*;
import model.User;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public User authenticateUser(String email, String password, String role) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ? AND Role = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));
                user.setBranchCode(rs.getInt("BranchCode"));
                user.setSalary(rs.getDouble("Salary"));
                user.setEmpNumber(rs.getInt("EmpNumber"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt"));
                user.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                return user;
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
