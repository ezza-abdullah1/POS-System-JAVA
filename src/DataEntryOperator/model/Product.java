package DataEntryOperator.model;

public class Product {
    private String productName;
    private String category;
    private double originalPrice;
    private double salePrice;
    private double priceByUnit;
    private double priceByCarton;

    public Product(String productName, String category, double originalPrice, double salePrice, double priceByUnit,
                   double priceByCarton) {
        this.productName = productName;
        this.category = category;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.priceByUnit = priceByUnit;
        this.priceByCarton = priceByCarton;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public double getPriceByUnit() {
        return priceByUnit;
    }

    public double getPriceByCarton() {
        return priceByCarton;
    }
}
