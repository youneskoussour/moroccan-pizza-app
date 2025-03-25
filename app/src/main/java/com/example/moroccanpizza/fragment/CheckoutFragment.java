package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.adapter.CartAdapter;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.CartItem;
import com.example.moroccanpizza.model.Order;
import com.example.moroccanpizza.model.User;
import com.example.moroccanpizza.util.PaymentProcessor;
import com.example.moroccanpizza.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CheckoutFragment extends Fragment {

    private EditText addressEditText;
    private EditText phoneEditText;
    private RadioGroup paymentMethodRadioGroup;
    private RadioButton cashRadioButton;
    private RadioButton cardRadioButton;
    private TextView subtotalTextView;
    private TextView deliveryFeeTextView;
    private TextView totalTextView;
    private RecyclerView cartItemsRecyclerView;
    private Button placeOrderButton;
    private Button editCartButton;
    private View cardDetailsLayout;
    private EditText cardNumberEditText;
    private EditText cardExpiryEditText;
    private EditText cardCvvEditText;

    private List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private AppDatabase database;
    private SharedPrefManager sharedPrefManager;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static final double DELIVERY_FEE = 15.00; // Fixed delivery fee in MAD
    private double subtotal = 0;
    private double total = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        
        // Initialize views
        addressEditText = view.findViewById(R.id.checkout_address);
        phoneEditText = view.findViewById(R.id.checkout_phone);
        paymentMethodRadioGroup = view.findViewById(R.id.payment_method_group);
        cashRadioButton = view.findViewById(R.id.cash_payment);
        cardRadioButton = view.findViewById(R.id.card_payment);
        subtotalTextView = view.findViewById(R.id.checkout_subtotal);
        deliveryFeeTextView = view.findViewById(R.id.checkout_delivery_fee);
        totalTextView = view.findViewById(R.id.checkout_total);
        cartItemsRecyclerView = view.findViewById(R.id.checkout_cart_items);
        placeOrderButton = view.findViewById(R.id.place_order_button);
        editCartButton = view.findViewById(R.id.edit_cart_button);
        cardDetailsLayout = view.findViewById(R.id.card_details_layout);
        cardNumberEditText = view.findViewById(R.id.card_number);
        cardExpiryEditText = view.findViewById(R.id.card_expiry);
        cardCvvEditText = view.findViewById(R.id.card_cvv);
        
        // Get database and shared preferences
        database = AppDatabase.getInstance(requireContext());
        sharedPrefManager = new SharedPrefManager(requireContext());
        
        // Setup cart adapter
        cartAdapter = new CartAdapter(getContext(), cartItems, null);
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemsRecyclerView.setAdapter(cartAdapter);
        
        // Set delivery fee
        deliveryFeeTextView.setText(String.format(Locale.getDefault(), "%.2f MAD", DELIVERY_FEE));
        
        // Setup listeners
        setupListeners();
        
        // Load cart items and user info
        loadCartItems();
        loadUserInfo();
        
        return view;
    }

    private void setupListeners() {
        paymentMethodRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.card_payment) {
                cardDetailsLayout.setVisibility(View.VISIBLE);
            } else {
                cardDetailsLayout.setVisibility(View.GONE);
            }
        });
        
        placeOrderButton.setOnClickListener(v -> {
            if (validateInputs()) {
                processOrder();
            }
        });
        
        editCartButton.setOnClickListener(v -> {
            // Navigate back to cart
            Navigation.findNavController(requireView()).navigateUp();
        });
    }

    private void loadCartItems() {
        database.cartDao().getAllCartItems().observe(getViewLifecycleOwner(), items -> {
            cartItems.clear();
            if (items != null && !items.isEmpty()) {
                cartItems.addAll(items);
            } else {
                // If cart is empty, go back to cart
                Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
                return;
            }
            
            cartAdapter.updateCartItems(cartItems);
            
            // Calculate totals
            calculateTotals();
        });
    }

    private void loadUserInfo() {
        String userId = sharedPrefManager.getUserId();
        if (userId != null && !userId.isEmpty()) {
            database.userDao().getUserById(userId).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    addressEditText.setText(user.getAddress());
                    phoneEditText.setText(user.getPhone());
                }
            });
        }
    }

    private void calculateTotals() {
        subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }
        
        total = subtotal + DELIVERY_FEE;
        
        subtotalTextView.setText(String.format(Locale.getDefault(), "%.2f MAD", subtotal));
        totalTextView.setText(String.format(Locale.getDefault(), "%.2f MAD", total));
    }

    private boolean validateInputs() {
        boolean isValid = true;
        
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        
        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            isValid = false;
        }
        
        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            isValid = false;
        }
        
        int selectedPaymentMethod = paymentMethodRadioGroup.getCheckedRadioButtonId();
        if (selectedPaymentMethod == R.id.card_payment) {
            // Validate card details
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String cardExpiry = cardExpiryEditText.getText().toString().trim();
            String cardCvv = cardCvvEditText.getText().toString().trim();
            
            if (cardNumber.isEmpty() || cardNumber.length() < 16) {
                cardNumberEditText.setError("Enter a valid card number");
                isValid = false;
            }
            
            if (cardExpiry.isEmpty() || !cardExpiry.contains("/")) {
                cardExpiryEditText.setError("Enter a valid expiry date (MM/YY)");
                isValid = false;
            }
            
            if (cardCvv.isEmpty() || cardCvv.length() < 3) {
                cardCvvEditText.setError("Enter a valid CVV");
                isValid = false;
            }
        }
        
        return isValid;
    }

    private void processOrder() {
        String userId = sharedPrefManager.getUserId();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        
        // Determine payment method
        String paymentMethod = cashRadioButton.isChecked() ? "Cash on Delivery" : "Credit Card";
        
        // Process payment if using card
        if (cardRadioButton.isChecked()) {
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String cardExpiry = cardExpiryEditText.getText().toString().trim();
            String cardCvv = cardCvvEditText.getText().toString().trim();
            
            // Use the payment processor to process card payment
            boolean paymentSuccess = PaymentProcessor.processCardPayment(
                    cardNumber, cardExpiry, cardCvv, total);
            
            if (!paymentSuccess) {
                Toast.makeText(getContext(), "Payment failed. Please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        }
        
        // Create order
        String orderId = "MOR-" + UUID.randomUUID().toString().substring(0, 8);
        Date currentDate = new Date();
        
        // Calculate estimated delivery time (current time + 45 minutes)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MINUTE, 45);
        Date estimatedDeliveryTime = calendar.getTime();
        
        Order order = new Order(
                orderId,
                userId,
                new ArrayList<>(cartItems),
                total,
                address,
                phone,
                paymentMethod,
                cardRadioButton.isChecked() ? "Paid" : "Pending",
                "Pending",
                currentDate,
                estimatedDeliveryTime
        );
        
        // Save order to database
        executor.execute(() -> {
            // Insert order
            database.orderDao().insertOrder(order);
            
            // Clear cart
            database.cartDao().clearCart();
            
            // Update user address and phone if changed
            database.userDao().updateUserAddress(userId, address);
            database.userDao().updateUserPhone(userId, phone);
            
            // Show success message and navigate to tracking on main thread
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_LONG).show();
                
                // Navigate to order tracking
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                Navigation.findNavController(requireView()).navigate(R.id.action_checkout_to_tracking, bundle);
            });
        });
    }
}
