package com.example.moroccanpizza.util;

import java.util.Calendar;

/**
 * Mock payment processor for demonstration purposes.
 * In a real app, this would integrate with a payment gateway.
 */
public class PaymentProcessor {

    /**
     * Process a card payment
     * 
     * @param cardNumber The credit card number
     * @param expiryDate The expiry date in MM/YY format
     * @param cvv The security code
     * @param amount The amount to charge
     * @return true if payment was successful, false otherwise
     */
    public static boolean processCardPayment(String cardNumber, String expiryDate, String cvv, double amount) {
        // This is a mock implementation
        // In a real app, you would integrate with a payment gateway
        
        // Basic validation
        if (cardNumber == null || cardNumber.length() < 16) {
            return false;
        }
        
        if (expiryDate == null || !expiryDate.contains("/")) {
            return false;
        }
        
        if (cvv == null || cvv.length() < 3) {
            return false;
        }
        
        // Check for expired card
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000; // Assuming 20xx
            
            Calendar expiryCalendar = Calendar.getInstance();
            expiryCalendar.set(year, month - 1, 1);
            
            Calendar currentCalendar = Calendar.getInstance();
            
            if (expiryCalendar.before(currentCalendar)) {
                return false; // Card expired
            }
        } catch (Exception e) {
            return false;
        }
        
        // For demonstration, let's say the payment is successful
        // unless the card number ends with "0000"
        return !cardNumber.endsWith("0000");
    }

    /**
     * Process a cash on delivery payment
     * 
     * @param amount The amount to be paid
     * @return true (always successful for COD)
     */
    public static boolean processCashOnDelivery(double amount) {
        // For cash on delivery, we just record the intent to pay
        // No actual processing happens until delivery
        return true;
    }
}
