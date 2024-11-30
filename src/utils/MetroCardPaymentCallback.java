package utils;

public interface MetroCardPaymentCallback {
    void onMetroCardPayment(String cardNumber, double deductedAmount);
}