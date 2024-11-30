package controller;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

import dao.ProductDAO;
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
    private final DecimalFormat df = new DecimalFormat("#.00");

     public CashierController(CashierView view) {
        this.view = view;
        this.productDAO = new ProductDAO();
        this.cartItems = new ArrayList<>();
        initializeListeners();
        
        // Set up the metro card payment callback
        view.setMetroCardPaymentCallback(new MetroCardPaymentCallback() {
            @Override
            public void onMetroCardPayment(String cardNumber, double deductedAmount) {
                lastUsedMetroCard = cardNumber;
                lastDeductedAmount = deductedAmount;
            }
        });
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

    private void checkout() {
        try {
            if (lastUsedMetroCard != null) {
                // Calculate points to add (1 point per 100 Rs)
                int pointsToAdd = (int) (total / 100);
                try {
                    productDAO.addPointsToMetroCard(lastUsedMetroCard, pointsToAdd);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(view, "Error adding points to metro card: " + e.getMessage());
                }
            }
            
            // Generate and download bill
            generateBill();
            
            // Reset the screen
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
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error during checkout: " + ex.getMessage());
        }
    }
 private void generateBill() {
    
        try {
            String fileName = "bill_" + System.currentTimeMillis() + ".txt";
            PrintWriter writer = new PrintWriter(fileName);
            writer.println("====== BILL ======");
            writer.println("Date: " + new java.util.Date());
            writer.println("\nItems:");
            for (CartItem item : cartItems) {
                writer.printf("%s x%d - Rs%.2f\n", 
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getSubtotal());
            }
            writer.println("\nSubtotal: Rs" + df.format(subtotal));
            writer.println("Discount: Rs" + df.format(overallDiscount));
            writer.println("Tax: Rs" + df.format(totalTax));
            writer.println("Total: Rs" + df.format(total));
            writer.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error generating bill: " + e.getMessage());
        }
    }


    private void showPaymentDialog() {
        view.showPaymentDialog(total, productDAO);
    }
}