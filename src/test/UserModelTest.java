package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import model.UserModel;
import db.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class UserModelTest {

    private UserModel userModel;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        userModel = new UserModel();
        connection = DatabaseConnection.getConnection(); // Use a mock or in-memory database connection here
    }

    @Test
    public void testGetAllUsersByRole() throws SQLException {
        // Arrange
        String role = "Cashier";  // Role to filter users by
    
        // Act
        List<UserModel> users = userModel.getAllUsersByRole(role);
    
        // Assert
        assertNotNull(users, "The result should not be null.");
        assertTrue(users.size() > 0, "There should be at least one user with the role: " + role);
    
        // Optionally, verify that all users have the correct role
        for (UserModel user : users) {
            assertEquals(role, user.getRole(), "User role mismatch.");
        }
    }
    
    @Test
    public void testAddUser_Cashier() {
        // Arrange
        UserModel newManager = new UserModel();
        newManager.setName("John Doe");
        newManager.setEmail("test1@example.com");
        newManager.setPassword("managerPass123");
        newManager.setRole("Cashier");
        newManager.setBranchCode(101);
        newManager.setSalary(6000.00);
        newManager.setEmpNumber(8002);
    
        // Act
        String result = userModel.addUser(newManager);
    
        // Assert
        assertEquals("Cashier added successfully, and employee count updated.", result, "Adding Cashier failed.");


    }
   
    
    @Test
    public void testUpdateUser() {
        // Arrange
        UserModel updatedUser = new UserModel();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated.email@example.com");
        updatedUser.setSalary(7000.00);
        updatedUser.setEmpNumber(8002);  // Assume this employee exists
        updatedUser.setBranchCode(101);
        updatedUser.setRole("Cashier");
    
        // Act
        String result = userModel.updateUser(updatedUser);
    
        // Assert
        assertEquals("Cashier updated successfully.", result, "User update failed.");
    }
   
    
    @Test
    public void testDeleteUserByEmailAndEmpNumber() {
        // Arrange
        String email = "updated.email@example.com";  // Use a valid email
        int empNumber = 8002;  // Use an existing employee number
    
        // Act
        String result = userModel.deleteUserByEmailAndEmpNumber(email, empNumber);
    
        // Assert
        assertEquals("User deleted successfully.", result, "User deletion failed.");
    }
    
    
}
