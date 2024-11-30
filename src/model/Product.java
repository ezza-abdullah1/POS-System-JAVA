package model;

public class Product {
    private String productId;
    private String name;
    private double price;
    private double tax;
    private double weight;
    private double discount;

    // Getters and setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}
