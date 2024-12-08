package TestCases.Cashier;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.CartItem;
import model.Product;

class CartItemTest {

    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        // Initialize a product with a default price, discount, and tax
        product = new Product();
        product.setProductId("P005");
        product.setVendorId(1);
        product.setName("Test Product");
        product.setCategory("Electronics");
        product.setOriginalPrice(120.0);
        product.setSalePrice(100.0); // Sale price
        product.setPriceByUnit(100.0); // Price per unit
        product.setPriceByCarton(950.0); // Price per carton
        product.setTax(5.0); // Tax rate
        product.setWeight(1.5); // Product weight
        product.setQuantity(10); // Available quantity
        product.setDiscount(10.0); // Discount
        cartItem = new CartItem(product, 1); // Initial CartItem with 1 quantity
    }

    @Test
    void testCartItemInitialization() {
        assertNotNull(cartItem, "CartItem should be initialized");
        assertEquals(100.0, cartItem.getProduct().getSalePrice(), "Product sale price should be 100");
        assertEquals(10.0, cartItem.getProduct().getDiscount(), "Product discount should be 10");
        assertEquals(5.0, cartItem.getProduct().getTax(), "Product tax should be 5");
        assertEquals(1, cartItem.getQuantity(), "Initial quantity should be 1");
        assertEquals(95.0, cartItem.getSubtotal(), "Subtotal should be 95 (after discount and tax for 1 unit)");
    }

    @Test
    void testCalculateAmountsWithQuantityUpdate() {
        // Update the quantity
        cartItem.setQuantity(2);

        // Recalculate the expected subtotal
        double expectedSubtotal = (product.getSalePrice() - product.getSalePrice() * (product.getDiscount() / 100) + product.getSalePrice() * (product.getTax() / 100)) * 2;

        assertEquals(expectedSubtotal, cartItem.getSubtotal(), "Subtotal should be updated correctly when quantity changes");
    }

    @Test
    void testNegativeQuantityThrowsException() {
        // Attempt to create a CartItem with a negative quantity
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CartItem(product, -2); // Negative quantity
        });
        assertEquals("Quantity must be greater than zero", exception.getMessage(), "Exception message should match the expected one");
    }

    @Test
    void testZeroQuantityThrowsException() {
        // Attempt to create a CartItem with zero quantity
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CartItem(product, 0); // Zero quantity
        });
        assertEquals("Quantity must be greater than zero", exception.getMessage(), "Exception message should match the expected one");
    }

    @Test
    void testCartItemWithZeroPriceProduct() {
        // Create a product with a zero price
        Product zeroPriceProduct = new Product();
        zeroPriceProduct.setSalePrice(0.0);
        zeroPriceProduct.setDiscount(10.0);
        zeroPriceProduct.setTax(5.0);
        CartItem zeroPriceCartItem = new CartItem(zeroPriceProduct, 1);

        assertEquals(0.0, zeroPriceCartItem.getSubtotal(), "Subtotal should be 0 when product price is 0");
        assertEquals(0.0, zeroPriceCartItem.getDiscountAmount(), "Discount should be 0 when product price is 0");
        assertEquals(0.0, zeroPriceCartItem.getTaxAmount(), "Tax should be 0 when product price is 0");
    }

    @Test
    void testCartItemWithZeroDiscountProduct() {
        // Create a product with zero discount
        Product zeroDiscountProduct = new Product();
        zeroDiscountProduct.setSalePrice(100.0);
        zeroDiscountProduct.setDiscount(0.0);
        zeroDiscountProduct.setTax(5.0);
        CartItem zeroDiscountCartItem = new CartItem(zeroDiscountProduct, 1);

        assertEquals(105.0, zeroDiscountCartItem.getSubtotal(), "Subtotal should be the price + tax when product has zero discount");
    }

    @Test
    void testCartItemWithZeroTaxProduct() {
        // Create a product with zero tax
        Product zeroTaxProduct = new Product();
        zeroTaxProduct.setSalePrice(100.0);
        zeroTaxProduct.setDiscount(10.0);
        zeroTaxProduct.setTax(0.0);
        CartItem zeroTaxCartItem = new CartItem(zeroTaxProduct, 1);

        assertEquals(90.0, zeroTaxCartItem.getSubtotal(), "Subtotal should be the price - discount when product has zero tax");
    }

    @Test
    void testCalculateAmountsWithDifferentDiscount() {
        // Change the product discount
        product.setDiscount(20.0); // 20% discount
        cartItem.calculateAmounts();

        double expectedSubtotal = (product.getSalePrice() - product.getSalePrice() * (product.getDiscount() / 100) + product.getSalePrice() * (product.getTax() / 100)) * cartItem.getQuantity();
        assertEquals(expectedSubtotal, cartItem.getSubtotal(), "Subtotal should be updated with the new discount");
    }

    @Test
    void testCartItemWithNegativePriceProduct() {
        // Create a product with a negative price
        Product negativePriceProduct = new Product();
        negativePriceProduct.setSalePrice(-100.0);
        negativePriceProduct.setDiscount(10.0);
        negativePriceProduct.setTax(5.0);
        CartItem negativePriceCartItem = new CartItem(negativePriceProduct, 1);

        assertEquals(-95.0, negativePriceCartItem.getSubtotal(), "Subtotal should reflect negative product price");
    }

    @Test
    void testProductWithLargeQuantity() {
        // Set a very large quantity
        cartItem.setQuantity(100000);

        double expectedSubtotal = (product.getSalePrice() - product.getSalePrice() * (product.getDiscount() / 100) + product.getSalePrice() * (product.getTax() / 100)) * 100000;
        assertEquals(expectedSubtotal, cartItem.getSubtotal(), "Subtotal should handle large quantities correctly");
    }

    @Test
    void testCartItemRevertingChanges() {
        // Store the original state of the cartItem
        double originalSubtotal = cartItem.getSubtotal();
        double originalDiscountAmount = cartItem.getDiscountAmount();
        double originalTaxAmount = cartItem.getTaxAmount();

        // Change the quantity to 3
        cartItem.setQuantity(3);

        // Assert the changed subtotal
        double newSubtotal = cartItem.getSubtotal();
        assertNotEquals(originalSubtotal, newSubtotal, "Subtotal should change when quantity is updated");

        // Revert the change
        cartItem.setQuantity(1);

        // Assert the reverted subtotal
        assertEquals(originalSubtotal, cartItem.getSubtotal(), "Subtotal should revert to the original value after resetting quantity");
        assertEquals(originalDiscountAmount, cartItem.getDiscountAmount(), "Discount should revert to the original value");
        assertEquals(originalTaxAmount, cartItem.getTaxAmount(), "Tax should revert to the original value");
    }
}

