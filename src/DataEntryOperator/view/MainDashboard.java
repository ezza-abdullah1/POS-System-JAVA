package DataEntryOperator.view;

import DataEntryOperator.controller.VendorController;
import DataEntryOperator.model.Vendor;

import javax.swing.*;
import java.util.List;

public class MainDashboard extends JFrame {
    public MainDashboard() {
        setTitle("Vendor and Product Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        VendorController vendorController = new VendorController();
        VendorTablePanel vendorPanel = new VendorTablePanel();

        // Fetch vendors from the database
        List<Vendor> vendors = vendorController.fetchVendors();
        vendorPanel.setVendors(vendors);

        // Add "Add New Vendor" button action
        vendorPanel.addVendorActionListener(e -> {
            AddVendorPanel addVendorPanel = new AddVendorPanel();
            addVendorPanel.addSaveListener(event -> {
                String name = addVendorPanel.getVendorName();
                String address = addVendorPanel.getVendorAddress();
                String contact = addVendorPanel.getContactNo();

                vendorController.addVendor(name, address, contact);

                // Refresh vendor list
                vendorPanel.setVendors(vendorController.fetchVendors());
                addVendorPanel.dispose();
            });

            addVendorPanel.setVisible(true);
        });

        add(vendorPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard().setVisible(true));
    }
}
