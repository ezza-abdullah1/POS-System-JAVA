package SuperAdmin.test_cases;

import SuperAdmin.model.BranchModel;
import utils.DatabaseConnection;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BranchModelTest {

    private Connection connection;

    @BeforeAll
    void setupDatabase() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            try (Statement statement = connection.createStatement()) {
                String checkTableQuery = "SELECT 1 FROM branches LIMIT 1";
                try {
                    statement.executeQuery(checkTableQuery);
                } catch (Exception tableDoesNotExist) {
                    statement.execute("CREATE TABLE branches (" +
                            "BranchID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "BranchCode int NOT NULL UNIQUE, " +
                            "BranchName VARCHAR(100), " +
                            "City VARCHAR(50), " +
                            "Address VARCHAR(255), " +
                            "Phone VARCHAR(15), " +
                            "NumEmployees INT, " +
                            "IsActive BOOLEAN)");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error setting up the test database: " + e.getMessage());
        }
    }

    @Test
    void testSaveBranch() {

        // save functionality
        BranchModel branch = new BranchModel("1001", "Main Branch", "New York",
                "123 Main St", "123-456-7890", 50, true);

        assertDoesNotThrow(() -> BranchModel.saveBranch(branch));

        // delete functionality
        BranchModel.deleteBranch(1001);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1001");
        assertNull(deletedBranch);
    }

    @Test
    void testGetAllBranches() {
        List<BranchModel> oldbranches = BranchModel.getAllBranches();
        BranchModel branch1 = new BranchModel("1001", "Main Branch", "New York",
                "123 Main St", "123-456-7890", 50, true);
        assertDoesNotThrow(() -> BranchModel.saveBranch(branch1));

        List<BranchModel> branches = BranchModel.getAllBranches();
        assertEquals(oldbranches.size() + 1, branches.size());

        BranchModel.deleteBranch(1001);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1001");
        assertNull(deletedBranch);
    }

    @Test
    void testFindBranchByCode() {
        BranchModel branch = new BranchModel("1001", "Main Branch", "New York",
                "123 Main St", "123-456-7890", 50, true);
        assertDoesNotThrow(() -> BranchModel.saveBranch(branch));

        BranchModel foundBranch = BranchModel.findBranchByCode("1001");
        assertNotNull(foundBranch);
        assertEquals("Main Branch", foundBranch.getBranchName());

        BranchModel.deleteBranch(1001);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1001");
        assertNull(deletedBranch);
    }

    @Test
    void testUpdateBranch() {
        BranchModel branch = new BranchModel("1001", "Main Branch", "New York",
                "123 Main St", "123-456-7890", 50, true);
        assertDoesNotThrow(() -> BranchModel.saveBranch(branch));

        branch.setBranchName("Updated Branch");
        branch.setCity("Boston");
        assertDoesNotThrow(() -> BranchModel.updateBranch(branch));

        BranchModel updatedBranch = BranchModel.findBranchByCode("1001");
        assertEquals("Updated Branch", updatedBranch.getBranchName());
        assertEquals("Boston", updatedBranch.getCity());

        BranchModel.deleteBranch(1001);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1001");
        assertNull(deletedBranch);
    }

    @Test
    void testDeleteBranch() {

        BranchModel.deleteBranch(1002);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1002");
        assertNull(deletedBranch);
    }

    @Test
    void testUpdateBranchStatus() {
        BranchModel branch = new BranchModel("1001", "Main Branch", "New York",
                "123 Main St", "123-456-7890", 50, true);
        assertDoesNotThrow(() -> BranchModel.saveBranch(branch));
        // main start
        BranchModel.updateBranchStatus(1001, false);

        BranchModel updatedBranch = BranchModel.findBranchByCode("1001");
        assertFalse(updatedBranch.isActive()); // main end

        BranchModel.deleteBranch(1001);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1001");
        assertNull(deletedBranch);
    }

    @Test
    void testGetActiveBranchCodes() {
        List<Integer> oldactiveBranches = BranchModel.getActiveBranchCodes();

        BranchModel branch1 = new BranchModel("1001", "Main Branch", "New York",
                "123 Main St", "123-456-7890", 50, true);

        assertDoesNotThrow(() -> BranchModel.saveBranch(branch1));

        List<Integer> activeBranches = BranchModel.getActiveBranchCodes();
        assertEquals(oldactiveBranches.size() + 1, activeBranches.size());

        BranchModel.deleteBranch(1001);
        BranchModel deletedBranch = BranchModel.findBranchByCode("1001");
        assertNull(deletedBranch);

    }
}
