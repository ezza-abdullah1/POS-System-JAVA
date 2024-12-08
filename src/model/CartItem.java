package model;

public class CartItem {
    private Product product;
    private int quantity;
    private double subtotal;
    private double discountAmount;
    private double taxAmount;

    public CartItem(Product product, int quantity) {
        this.product = product;
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity;
        calculateAmounts(); // Recalculate the amounts if the quantity changes
    }

    public void calculateAmounts() {
        this.discountAmount = product.getSalePrice() * (product.getDiscount() / 100);
        this.taxAmount = product.getSalePrice() * (product.getTax() / 100);
        double priceAfterDiscount = product.getSalePrice() - discountAmount;
        this.subtotal = (priceAfterDiscount + taxAmount) * quantity;
    }

    // Getters and setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity;
        calculateAmounts(); // Recalculate the amounts if the quantity changes
    }

    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTaxAmount() { return taxAmount; }
}