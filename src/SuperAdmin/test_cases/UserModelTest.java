package SuperAdmin.test_cases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import SuperAdmin.model.UserModel;
import db.DatabaseConnection;

import java.sql.*;

public class UserModelTest {

    private UserModel userModel;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        userModel = new UserModel();
        connection = DatabaseConnection.getConnection(); // Use a mock or in-memory database connection here
    }

    @Test
    public void testAddBranchManager_Success() throws SQLException {
        UserModel newManager = new UserModel();
        newManager.setName("Test Manager");
        newManager.setEmail("test.manager@example.com");
        newManager.setPassword("password");
        newManager.setRole("BranchManager");
        newManager.setBranchCode(103);
        newManager.setSalary(50000.0);
        newManager.setEmpNumber(1001);

        String result = userModel.addBranchManager(newManager);

        assertEquals("Branch Manager added successfully, and employee count updated.", result);

        // Verify that the manager was added correctly
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE Email = ?")) {
            stmt.setString(1, newManager.getEmail());
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(newManager.getName(), rs.getString("Name"));
            assertEquals(newManager.getEmail(), rs.getString("Email"));
            assertEquals(newManager.getBranchCode(), rs.getInt("BranchCode"));
        }
        // Now, delete the manager
        String result1 = userModel.deleteBranchManagerByCode(103);

        assertEquals("Branch Manager deleted successfully.", result1);

    }

    @Test
    public void testUpdateBranchManager_Success() throws SQLException {
        // First, add a manager to update
        UserModel manager = new UserModel();
        manager.setName("Update Test");
        manager.setEmail("update.test@example.com");
        manager.setPassword("password");
        manager.setRole("BranchManager");
        manager.setBranchCode(103);
        manager.setSalary(60000.0);
        manager.setEmpNumber(1002);

        userModel.addBranchManager(manager);

        // Update the manager
        manager.setName("Updated Name");
        manager.setSalary(65000.0);

        String result = userModel.updateBranchManager(manager);

        assertEquals("Branch Manager updated successfully.", result);

        // Verify the update
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE Email = ?")) {
            stmt.setString(1, manager.getEmail());
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("Updated Name", rs.getString("Name"));
            assertEquals(65000.0, rs.getDouble("Salary"));
        }
        // Now, delete the manager
        String result1 = userModel.deleteBranchManagerByCode(103);

        assertEquals("Branch Manager deleted successfully.", result1);

    }

    @Test
    public void testDeleteBranchManager_Success() throws SQLException {
        // First, add a manager to delete
        UserModel manager = new UserModel();
        manager.setName("Delete Test");
        manager.setEmail("delete.test@example.com");
        manager.setPassword("password");
        manager.setRole("BranchManager");
        manager.setBranchCode(103);
        manager.setSalary(55000.0);
        manager.setEmpNumber(1003);

        userModel.addBranchManager(manager);

        // Now, delete the manager
        String result = userModel.deleteBranchManagerByCode(manager.getBranchCode());

        assertEquals("Branch Manager deleted successfully.", result);

        // Verify deletion
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE Email = ?")) {
            stmt.setString(1, manager.getEmail());
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next());
        }
    }

    @Test
    public void testGetBranchManagerByBranchCode_Success() throws SQLException {
        // First, add a manager to retrieve
        UserModel manager = new UserModel();
        manager.setName("Retrieve Test");
        manager.setEmail("retrieve.test@example.com");
        manager.setPassword("password");
        manager.setRole("BranchManager");
        manager.setBranchCode(103);
        manager.setSalary(75000.0);
        manager.setEmpNumber(1004);

        userModel.addBranchManager(manager);

        // Now, retrieve the manager by branch code
        String[] managerData = userModel.getBranchManagerByBranchCode(manager.getBranchCode());

        // Adjust the assertion to compare salary as a double
        assertEquals(manager.getName(), managerData[1]);
        assertEquals(manager.getEmail(), managerData[2]);
        assertEquals(manager.getEmpNumber().toString(), managerData[6]);
        assertEquals(manager.getSalary(), Double.parseDouble(managerData[7]), 0.01); // Allow a small delta for
                                                                                     // floating-point comparison

        // Now, delete the manager
        String result1 = userModel.deleteBranchManagerByCode(103);

        assertEquals("Branch Manager deleted successfully.", result1);
    }

}
