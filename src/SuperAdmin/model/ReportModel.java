package SuperAdmin.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import utils.DatabaseConnection;

public class ReportModel {
    public List<SalesData> getSalesData(String branchCode, String timePeriod, String startDate, String endDate)
            throws SQLException {
        String query = buildQuery(timePeriod, startDate, endDate);
        List<SalesData> salesDataList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, branchCode);
            if ("Specify Range".equals(timePeriod)) {
                stmt.setString(2, startDate);
                stmt.setString(3, endDate);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("Date");
                double profit = rs.getDouble("Profit");
                salesDataList.add(new SalesData(date, profit));
            }
        }
        return salesDataList;
    }

    public String buildQuery(String timePeriod, String startDate, String endDate) {
        String query = "";
        if ("Specify Range".equals(timePeriod)) {
            query = "SELECT * FROM branchsales WHERE BranchCode = ? AND Date BETWEEN ? AND ?";
        } else {
            String intervalExpression = switch (timePeriod) {
                case "Today" -> "CURDATE()";
                case "Weekly" -> "CURDATE() - INTERVAL 7 DAY";
                case "Monthly" -> "CURDATE() - INTERVAL 1 MONTH";
                case "Yearly" -> "CURDATE() - INTERVAL 1 YEAR";
                default -> "";
            };
            query = "SELECT * FROM branchsales WHERE BranchCode = ? AND Date >= " + intervalExpression;
        }
        return query;
    }

    public List<String> getBranchCodes() throws SQLException {
        List<String> branchCodes = new ArrayList<>();
        String query = "SELECT BranchCode FROM branches WHERE IsActive = 1";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                branchCodes.add(rs.getString("BranchCode"));
            }
        }
        return branchCodes;
    }

    public static class SalesData {
        private final String date;
        private final double profit;

        public SalesData(String date, double profit) {
            this.date = date;
            this.profit = profit;
        }

        public String getDate() {
            return date;
        }

        public double getProfit() {
            return profit;
        }
    }
}
