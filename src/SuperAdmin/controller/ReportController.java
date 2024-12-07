package SuperAdmin.controller;

import SuperAdmin.model.ReportModel;
import SuperAdmin.view.reportsView;

import java.sql.SQLException;
import java.util.List;

public class ReportController {
    private final reportsView view;
    private final ReportModel model;

    public ReportController(reportsView view) {
        this.view = view;
        this.model = new ReportModel();
        view.setController(this); // Setting the controller in the view
    }

    public void generateReport(String branchCode, String timePeriod, String startDate, String endDate) {
        try {
            List<ReportModel.SalesData> salesData = model.getSalesData(branchCode, timePeriod, startDate, endDate);
            view.updateSalesData(salesData, timePeriod);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateBranchCodes() {
        try {
            List<String> branchCodes = model.getBranchCodes();
            view.populateBranchCodes(branchCodes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
