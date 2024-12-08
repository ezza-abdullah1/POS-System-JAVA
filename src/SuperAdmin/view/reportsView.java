package SuperAdmin.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.FontUIResource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import SuperAdmin.controller.ReportController;
import SuperAdmin.model.ReportModel;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class reportsView extends JFrame {
    private JTable dataTable;
    private JPanel chartPanel;
    private JButton generateReportButton;
    private JComboBox<String> branchCodeComboBox;
    private JComboBox<String> timePeriodComboBox;
    private JDateChooser startDateChooser, endDateChooser;
    private ReportController controller;

    public reportsView() {
        setTitle("Branch Sales Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1000, 700);
        controller = new ReportController(this);

        // Initialize Components
        initComponents();

        // Apply Modern Theme
        applyModernTheme();

        setVisible(true);
    }

    public void setController(ReportController controller) {
        this.controller = controller;
    }

    private void initComponents() {
        // Top Panel (Selection Options)
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel branchCodeLabel = new JLabel("Branch Code:");
        branchCodeComboBox = new JComboBox<>();
        topPanel.add(branchCodeLabel);
        topPanel.add(branchCodeComboBox);
        controller.populateBranchCodes();

        JLabel timePeriodLabel = new JLabel("Time Period:");
        timePeriodComboBox = new JComboBox<>(new String[] { "Today", "Weekly", "Monthly", "Yearly", "Specify Range" });
        topPanel.add(timePeriodLabel);
        topPanel.add(timePeriodComboBox);

        JLabel startDateLabel = new JLabel("Start Date:");
        startDateChooser = new JDateChooser();
        startDateChooser.setVisible(false);
        topPanel.add(startDateLabel);
        topPanel.add(startDateChooser);

        JLabel endDateLabel = new JLabel("End Date:");
        endDateChooser = new JDateChooser();
        endDateChooser.setVisible(false);
        topPanel.add(endDateLabel);
        topPanel.add(endDateChooser);

        // Show/Hide date choosers based on time period selection
        timePeriodComboBox.addActionListener(e -> {
            boolean specifyRange = "Specify Range".equals(timePeriodComboBox.getSelectedItem());
            startDateChooser.setVisible(specifyRange);
            endDateChooser.setVisible(specifyRange);
            startDateLabel.setVisible(specifyRange);
            endDateLabel.setVisible(specifyRange);
        });

        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Table and Chart)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        // Table Panel
        dataTable = new JTable(new DefaultTableModel(new Object[][] {}, new String[] { "Date", "Branch", "Profit" })) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes all cells in the table uneditable
            }
        };
        JScrollPane tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Sales Data Table",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(50, 50, 112)));
        centerPanel.add(tableScrollPane);

        // Chart Panel
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Profit Chart",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(50, 50, 112)));
        centerPanel.add(chartPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel (Generate Report Button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        generateReportButton = createGradientButton("Generate Report", new Color(70, 130, 180),
                new Color(30, 144, 255));
        bottomPanel.add(generateReportButton);
        add(bottomPanel, BorderLayout.SOUTH);
        // Add Action Listener for Generate Report Button
        generateReportButton.addActionListener(e -> generateReport());
    }

    private JButton createGradientButton(String text, Color startColor, Color endColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }

    private void applyModernTheme() {
        // Font customization
        Font font = new FontUIResource("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font.deriveFont(Font.BOLD));

        // Table customization
        dataTable.setRowHeight(30); // Increase row height for better aesthetics
        dataTable.setGridColor(new Color(200, 200, 200)); // Light gray grid lines
        dataTable.setIntercellSpacing(new Dimension(0, 1)); // Remove cell spacing
        dataTable.setShowGrid(false); // Hide default grid for a cleaner look

        // Alternate row colors
        dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255)); // Light blue
                return c;
            }
        });

        // Table Header
        dataTable.getTableHeader().setBackground(new Color(70, 130, 180)); // Steel blue
        dataTable.getTableHeader().setForeground(Color.WHITE);
    }

    private void generateReport() {
        String branchCode = (String) branchCodeComboBox.getSelectedItem();
        String timePeriod = (String) timePeriodComboBox.getSelectedItem();
        String startDate = null;
        String endDate = null;

        if ("Specify Range".equals(timePeriod)) {
            startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateChooser.getDate());
            endDate = new SimpleDateFormat("yyyy-MM-dd").format(endDateChooser.getDate());
        }

        controller.generateReport(branchCode, timePeriod, startDate, endDate);
    }

    public void updateSalesData(List<ReportModel.SalesData> salesDataList, String timePeriod) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[] { "Date",
                "Profit" }, 0);
        for (ReportModel.SalesData data : salesDataList) {
            tableModel.addRow(new Object[] { data.getDate(), data.getProfit() });
        }
        dataTable.setModel(tableModel);
        updateChart(salesDataList, timePeriod);
    }

    public void populateBranchCodes(List<String> branchCodes) {
        for (String branchCode : branchCodes) {
            branchCodeComboBox.addItem(branchCode);
        }
    }

    public void updateChart(List<ReportModel.SalesData> salesDataList, String timePeriod) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat dateFormat;

        // Determine the date format based on the time period
        switch (timePeriod) {
            case "Weekly":
                dateFormat = new SimpleDateFormat("EEE"); // Day of the week (Mon, Tue, etc.)
                break;
            case "Monthly":
                dateFormat = new SimpleDateFormat("MMM yyyy"); // Month and year
                break;
            case "Yearly":
                dateFormat = new SimpleDateFormat("yyyy"); // Year
                break;
            default: // Daily as default
                dateFormat = new SimpleDateFormat("dd MMM yyyy"); // Full date
                break;
        }

        // Populate dataset
        for (ReportModel.SalesData data : salesDataList) {
            try {
                // Parse and format date
                String formattedDate = dateFormat.format(
                        new SimpleDateFormat("yyyy-MM-dd").parse(data.getDate()));
                dataset.addValue(data.getProfit(), "Profit", formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Branch Sales Report",
                "Date",
                "Profit",
                dataset);

        // Customize chart appearance
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.GRAY); // Light gray grid lines
        plot.setBackgroundPaint(new Color(230, 230, 250)); // Lavender background
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Gradient colors for bars
        Color[] barColors = { Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN, Color.MAGENTA };
        for (int i = 0; i < barColors.length; i++) {
            renderer.setSeriesPaint(i, barColors[i]);
        }

        // Update chart panel
        ChartPanel chartPanelContent = new ChartPanel(chart);
        chartPanel.removeAll();
        chartPanel.add(chartPanelContent, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public JButton getGenerateReportButton() {
        return generateReportButton;
    }

    public JTable getDataTable() {
        return dataTable;
    }

    public JPanel getChartPanel() {
        return chartPanel;
    }

    public JComboBox<String> getBranchCodeComboBox() {
        return branchCodeComboBox;
    }

    public JComboBox<String> getTimePeriodComboBox() {
        return timePeriodComboBox;
    }

    public JDateChooser getStartDateChooser() {
        return startDateChooser;
    }

    public JDateChooser getEndDateChooser() {
        return endDateChooser;
    }

    public static void main(String[] args) {
        new reportsView();
    }
}
