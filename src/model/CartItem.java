package model;



public class CartItem {
    private Product product;
    private int quantity;
    private double subtotal;
    private double discountAmount;
    private double taxAmount;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        calculateAmounts();
    }

    public void calculateAmounts() {
        this.discountAmount = product.getPrice() * (product.getDiscount() / 100);
        this.taxAmount = product.getPrice() * (product.getTax() / 100);
        double priceAfterDiscount = product.getPrice() - discountAmount;
        this.subtotal = (priceAfterDiscount + taxAmount) * quantity;
    }

    // Getters and setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        calculateAmounts();
    }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTaxAmount() { return taxAmount; }
}