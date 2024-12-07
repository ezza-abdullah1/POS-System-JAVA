package controller;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import dao.ProductDAOTemp;
import dao.BillingDAO;
import db.BranchSalesDAO;
import model.CartItem;
import model.Product;
import view.CashierView;

import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;

public class CashierController {
    private final CashierView view;
    private final ProductDAOTemp productDAO;
    private final List<CartItem> cartItems;
    private String lastUsedMetroCard = null;
    private double lastDeductedAmount = 0.0;
    private double subtotal = 0.0;
    private double total = 0.0;
    private double overallDiscount = 0.0;
    private double totalTax = 0.0;
    private double remainingBalance = 0.0;
    private final DecimalFormat df = new DecimalFormat("#.00");

    public CashierController(CashierView view) {
        this.view = view;
        this.productDAO = new ProductDAOTemp();
        this.cartItems = new ArrayList<>();
        initializeListeners();

        // Set up the metro card payment callback
        view.setMetroCardPaymentCallback((cardNumber, deductedAmount) -> {
            lastUsedMetroCard = cardNumber;
            lastDeductedAmount = deductedAmount;
            remainingBalance = total - deductedAmount;
            updateUIAfterMetroCardPayment();
        });
    }
    private void updateUIAfterMetroCardPayment() {
        view.updateFinalTotal(df.format(remainingBalance));
    }
    private void checkout() {

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Cannot checkout with empty cart. Please add items first.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Check if cash amount is entered (payment is done)
        String cashAmntStr = view.getCashAmount().trim();
        if (cashAmntStr.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Please process payment before checkout.",
                    "Payment Required",
                    JOptionPane.WARNING_MESSAGE);
            showPaymentDialog();
            return;
        }
        try {
            BillingDAO billingDAO = new BillingDAO();
            double finalTotal = total;
            String paymentMethod = "CASH"; // Default payment method

            // Step 1: Process Metro Card Deduction if used (optional)
            if (lastUsedMetroCard != null) {
                paymentMethod = "METRO_CARD_AND_CASH";
                finalTotal = remainingBalance;
            }

            // Step 2: Validate and process cash payment
            String cashAmountStr = view.getCashAmount().trim();
            if (cashAmountStr.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Please enter the cash amount",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Parse cash amount with proper error handling
            double cashPaid;
            try {
                cashPaid = Double.parseDouble(cashAmountStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view,
                        "Invalid cash amount. Please enter a valid number",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Validate payment amount
            if (cashPaid < finalTotal) {
                JOptionPane.showMessageDialog(view,
                        "Insufficient payment. Please pay the remaining balance of Rs" + df.format(finalTotal),
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Step 3: Save the bill with payment details
            billingDAO.saveBill(subtotal, overallDiscount, totalTax, total, lastUsedMetroCard);

            // Step 4: Add new points to Metro Card if used (optional)
            if (lastUsedMetroCard != null) {
                int pointsEarned = calculatePoints(total);
                productDAO.addPointsToMetroCard(lastUsedMetroCard, pointsEarned);
                JOptionPane.showMessageDialog(view,
                        "Added " + pointsEarned + " points to your Metro Card!",
                        "Points Added",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Calculate profit
            double profit = calculateProfit();

            // Save sales data in BranchSales table
            BranchSalesDAO branchSalesDAO = new BranchSalesDAO();
            int branchCode = view.getBranchCode(); // Retrieve branch code from UI
            branchSalesDAO.saveBranchSale(branchCode, total, profit);
            // Step 5: Generate bill file
            generateDetailedBill(paymentMethod, cashPaid, finalTotal);

            // Step 6: Show change amount
            double change = cashPaid - finalTotal;
            String message = "Transaction Complete\n" +
                    "Total Paid: Rs" + df.format(cashPaid) + "\n" +
                    "Change to return: Rs" + df.format(change);
            if (lastUsedMetroCard != null) {
                message = "Transaction Complete\n" +
                        "Metro Card Payment: Rs" + df.format(lastDeductedAmount) + "\n" +
                        "Cash Payment: Rs" + df.format(cashPaid) + "\n" +
                        "Change to return: Rs" + df.format(change);
            }
            JOptionPane.showMessageDialog(view, message, "Payment Complete", JOptionPane.INFORMATION_MESSAGE);

            // Step 7: Reset for next transaction
            resetScreen();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Database error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Unexpected error: " + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private int calculatePoints(double total)
    {
        int points= (int) (total/30);
        return points;
    }
    private void generateDetailedBill(String paymentMethod, double cashPaid, double finalTotal) {
        try {
            String fileName = "bill_" + System.currentTimeMillis() + ".txt";
            PrintWriter writer = new PrintWriter(fileName);
            writer.println("=============== METRO ===============");
            writer.println("=====================================");
            writer.println("\n=============== BILL ===============");
            writer.println("Date: " + new java.util.Date());
            writer.println("\nItems:");
            writer.println("--------------------------------");

            for (CartItem item : cartItems) {
                writer.printf("%-20s x%d  Rs %.2f\n",
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getSubtotal());
            }
            writer.println("\nPayment Details:");
            writer.println("--------------------------------");
            writer.printf("Subtotal:         Rs %s\n", df.format(subtotal));
            writer.printf("Discount:         Rs %s\n", df.format(overallDiscount));
            writer.printf("Tax:              Rs %s\n", df.format(totalTax));
            writer.printf("Total:            Rs %s\n", df.format(total));

            // Only include Metro Card details if it was used
            if (lastUsedMetroCard != null) {
                writer.printf("Metro Card Used:   %s\n", lastUsedMetroCard);
                writer.printf("Points Redeemed:   Rs %s\n", df.format(lastDeductedAmount));
                writer.printf("Cash Paid:         Rs %s\n", df.format(cashPaid));
            } else {
                writer.printf("Cash Paid:         Rs %s\n", df.format(cashPaid));
            }

            writer.printf("Change:           Rs %s\n", df.format(cashPaid - finalTotal));
            if (lastUsedMetroCard != null) {
                writer.printf("Points Earned:     %d\n", calculatePoints(total));
            }
            writer.println("\n================================");
            writer.println("Thank you for shopping with us!");
            writer.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error generating bill: " + e.getMessage());
        }
    }
    private void initializeListeners() {
        view.setAddItemListener(e -> addItem());

        view.setAddItemKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addItem();
                }
            }
        });
        // Improved cell edit listener implementation
        view.setCartTableCellEditListener((row, newValue) -> {
            try {
                int quantity = Integer.parseInt(newValue);
                if (quantity > 0) {
                    CartItem item = cartItems.get(row);
                    item.setQuantity(quantity);
                    updateTotals();
                    Object[] updatedRow = createTableRow(item);
                    view.updateTableRow(row, updatedRow);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view,
                        "Please enter a valid quantity",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                // Revert to original value
                CartItem item = cartItems.get(row);
                view.updateTableRow(row, createTableRow(item));
            }
        });
        view.setUpdateItemListener(e -> updateItem());
        view.setDeleteItemListener(e -> deleteItem());
        view.setCheckoutListener(e -> checkout());
        view.setPaymentListener(e -> showPaymentDialog());
        // Add Metro Card button listener
        view.setMetroCardButtonListener(e -> handleMetroCardPayment());
    }
    private double calculateProfit() throws SQLException {
        double manufacturingCost = 0.0;
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            manufacturingCost += product.getOriginalPrice() * item.getQuantity();
        }
        return total - manufacturingCost; // Profit = Total bill - Manufacturing cost
    }

    private void handleMetroCardPayment() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Cannot process payment with empty cart. Please add items first.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metroCardNumber = JOptionPane.showInputDialog(view,
                "Enter Metro Card Number:",
                "Metro Card Payment",
                JOptionPane.PLAIN_MESSAGE);

        if (metroCardNumber != null && !metroCardNumber.trim().isEmpty()) {
            try {
                double deductedAmount = productDAO.processMetroCardPayment(
                        metroCardNumber,
                        total
                );
                if (deductedAmount > 0) {
                    view.updateFinalTotal(df.format(remainingBalance));
                    lastUsedMetroCard = metroCardNumber;
                    lastDeductedAmount = deductedAmount;
                    remainingBalance = total - deductedAmount;
                    updateUIAfterMetroCardPayment();

                    JOptionPane.showMessageDialog(view,
                            String.format("Payment Successful!\nPoints used: Rs%.2f", deductedAmount),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view,
                            "Invalid Metro Card details or insufficient points.",
                            "Payment Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Error processing payment: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void addItem() {
        try {
            String productId = view.getProductId();
            int quantity = view.getQuantity();
            Product product = productDAO.getProductById(productId);
            if (product != null) {
                if (productDAO.updateProductQuantity(productId, quantity)) {
                    CartItem item = new CartItem(product, quantity);
                    cartItems.add(item);
                    updateTotals();
                    view.addTableRow(createTableRow(item));
                    view.clearProductInput();
                } else {
                    JOptionPane.showMessageDialog(view, "Insufficient stock for the selected product.");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Product not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error adding item: " + ex.getMessage());
        }
    }

    private void updateItem() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow >= 0) {
            CartItem item = cartItems.get(selectedRow);
            item.setQuantity(view.getQuantity());
            updateTotals();
            view.updateTableRow(selectedRow, createTableRow(item));
        }
    }
    private void deleteItem() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow >= 0) {
            cartItems.remove(selectedRow);
            updateTotals();
            view.removeTableRow(selectedRow);
        }
    }
    private void updateTotals() {
        subtotal = 0.0;
        overallDiscount = 0.0;
        totalTax = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
            overallDiscount += item.getDiscountAmount() * item.getQuantity();
            totalTax += item.getTaxAmount() * item.getQuantity();
        }
        total = subtotal + totalTax - overallDiscount;
        view.updateTotals(df.format(subtotal), df.format(overallDiscount),
                df.format(totalTax), df.format(total));
    }
    private Object[] createTableRow(CartItem item) {
        return new Object[]{
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                df.format(item.getProduct().getSalePrice()),
                item.getProduct().getWeight(),
                df.format(item.getDiscountAmount()),
                df.format(item.getTaxAmount()),
                item.getQuantity(),
                df.format(item.getSubtotal())
        };
    }


    private void resetScreen() {
        // Clear cart items
        cartItems.clear();

        // Clear the cart table
        view.clearCart();

        // Reset all totals to 0.0
        subtotal = 0.0;
        total = 0.0;
        overallDiscount = 0.0;
        totalTax = 0.0;
        remainingBalance = 0.0;

        // Reset Metro Card related values
        lastUsedMetroCard = null;
        lastDeductedAmount = 0.0;

        // Update all UI elements to show 0.00
        view.updateTotals(df.format(0.0), df.format(0.0), df.format(0.0), df.format(0.0));
        view.updateFinalTotal(df.format(0.0));

        // Clear product input fields
        view.clearProductInput();


        // Reset payment-related UI elements
        view.setMetroCardButtonVisible(false);

    }
    private void showPaymentDialog() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Cannot process payment with empty cart. Please add items first.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        view.setMetroCardButtonVisible(true); // Show Metro Card button when payment dialog is shown
        view.showPaymentDialog(remainingBalance > 0 ? remainingBalance : total, productDAO);
        /*view.showPaymentDialog(total, productDAO);*/
    }
}