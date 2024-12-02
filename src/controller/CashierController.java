package controller;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import dao.ProductDAO;
import dao.BillingDAO;
import model.CartItem;
import model.Product;
import view.CashierView;
import utils.*;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;

public class CashierController {
    private final CashierView view;
    private final ProductDAO productDAO;
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
        this.productDAO = new ProductDAO();
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

            writer.println("=============== BILL ===============");
            writer.println("Date: " + new java.util.Date());
            writer.println("\nItems:");
            writer.println("--------------------------------");

            for (CartItem item : cartItems) {
                writer.printf("%-20s x%d  Rs%.2f\n",
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getSubtotal());
            }

            writer.println("\nPayment Details:");
            writer.println("--------------------------------");
            writer.printf("Subtotal:         Rs%s\n", df.format(subtotal));
            writer.printf("Discount:         Rs%s\n", df.format(overallDiscount));
            writer.printf("Tax:              Rs%s\n", df.format(totalTax));
            writer.printf("Total:            Rs%s\n", df.format(total));

            // Only include Metro Card details if it was used
            if (lastUsedMetroCard != null) {
                writer.printf("Metro Card Used:   %s\n", lastUsedMetroCard);
                writer.printf("Points Redeemed:   Rs%s\n", df.format(lastDeductedAmount));
                writer.printf("Cash Paid:         Rs%s\n", df.format(cashPaid));
            } else {
                writer.printf("Cash Paid:         Rs%s\n", df.format(cashPaid));
            }

            writer.printf("Change:           Rs%s\n", df.format(cashPaid - finalTotal));

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
    }

    private void addItem() {
        try {
            String productId = view.getProductId();
            int quantity = view.getQuantity();
            Product product = productDAO.getProductById(productId);
            if (product != null) {
                CartItem item = new CartItem(product, quantity);
                cartItems.add(item);
                updateTotals();
                view.addTableRow(createTableRow(item));
                view.clearProductInput();
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
                df.format(item.getProduct().getPrice()),
                item.getProduct().getWeight(),
                df.format(item.getDiscountAmount()),
                df.format(item.getTaxAmount()),
                item.getQuantity(),
                df.format(item.getSubtotal())
        };
    }




    private void resetScreen() {
        cartItems.clear();
        view.clearCart();
        view.resetTotals();
        view.clearProductInput();
        subtotal = 0.0;
        total = 0.0;
        overallDiscount = 0.0;
        totalTax = 0.0;
        lastUsedMetroCard = null;
        lastDeductedAmount = 0.0;
    }
    private void showPaymentDialog() {
        view.showPaymentDialog(total, productDAO);
    }
}