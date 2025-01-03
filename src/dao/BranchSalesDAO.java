package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utils.*;

public class BranchSalesDAO {
    public void saveBranchSale(int branchCode, double totalSalePrice, double profit) throws SQLException {
        String sql = "INSERT INTO BranchSales (BranchCode, TotalSalePrice, Profit) VALUES (?, ?, ?)";
        try (@SuppressWarnings("static-access")
        Connection conn = DatabaseConnection.getInstance().GETConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, (branchCode));
            stmt.setDouble(2, totalSalePrice);
            stmt.setDouble(3, profit);

            stmt.executeUpdate();
        }
    }
}
