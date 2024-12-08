package SuperAdmin.test_cases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import SuperAdmin.model.ReportModel;

import java.sql.SQLException;
import java.util.List;

public class ReportModelTest {
    private ReportModel reportModel;

    @BeforeEach
    public void setUp() {
        reportModel = new ReportModel();
    }

    @Test
    public void testGetSalesData_Success() throws SQLException {
        // Arrange
        String branchCode = "107";
        String timePeriod = "Today"; // Change this as needed to test other time periods
        String startDate = "";
        String endDate = "";

        // Act
        List<ReportModel.SalesData> salesData = reportModel.getSalesData(branchCode, timePeriod, startDate, endDate);

        // Assert
        assertNotNull(salesData);
        assertFalse(salesData.isEmpty());

        // Check the first SalesData entry
        ReportModel.SalesData firstData = salesData.get(0);
        assertTrue(firstData.getProfit() >= 0.0); // Assuming profit should not be negative
    }

    @Test
    public void testGetSalesData_SpecifyRange_Success() throws SQLException {
        // Arrange
        String branchCode = "107";
        String timePeriod = "Specify Range";
        String startDate = "2024-12-01";
        String endDate = "2024-12-31";

        // Act
        List<ReportModel.SalesData> salesData = reportModel.getSalesData(branchCode, timePeriod, startDate, endDate);

        // Assert
        assertNotNull(salesData);
        assertFalse(salesData.isEmpty());

        // Check the first SalesData entry
        ReportModel.SalesData firstData = salesData.get(0);
        assertTrue(firstData.getProfit() >= 0.0);
    }

    @Test
    public void testGetBranchCodes_Success() throws SQLException {
        // Act
        List<String> branchCodes = reportModel.getBranchCodes();

        // Assert
        assertNotNull(branchCodes);
        assertFalse(branchCodes.isEmpty());

        // Check if a specific branch code exists
        assertTrue(branchCodes.contains("107"));
    }

    @Test
    public void testBuildQuery_Success() {
        // Arrange
        String timePeriod = "Today";
        String branchCode = "103";

        // Act
        String query = reportModel.buildQuery(timePeriod, "", "");

        // Assert
        assertEquals("SELECT * FROM branchsales WHERE BranchCode = ? AND Date >= CURDATE()", query);

        // Arrange for a range period
        timePeriod = "Specify Range";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        // Act
        query = reportModel.buildQuery(timePeriod, startDate, endDate);

        // Assert
        assertEquals("SELECT * FROM branchsales WHERE BranchCode = ? AND Date BETWEEN ? AND ?", query);
    }
}
