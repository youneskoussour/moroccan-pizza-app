package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.adapter.CartAdapter;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView cartRecyclerView;
    private TextView emptyCartTextView;
    private TextView subtotalTextView;
    private TextView deliveryFeeTextView;
    private TextView totalTextView;
    private Button checkoutButton;
    private Button continueShoppingButton;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private Executor executor = Executors.newSingleThreadExecutor();
    private AppDatabase database;

    private static final double DELIVERY_FEE = 15.00; // Fixed delivery fee in MAD

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        
        // Initialize views
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        emptyCartTextView = view.findViewById(R.id.empty_cart_text);
        subtotalTextView = view.findViewById(R.id.subtotal_text);
        deliveryFeeTextView = view.findViewById(R.id.delivery_fee_text);
        totalTextView = view.findViewById(R.id.total_text);
        checkoutButton = view.findViewById(R.id.checkout_button);
        continueShoppingButton = view.findViewById(R.id.continue_shopping_button);
        
        // Setup adapter
        cartAdapter = new CartAdapter(getContext(), cartItems, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);
        
        // Get database instance
        database = AppDatabase.getInstance(requireContext());
        
        // Display delivery fee
        deliveryFeeTextView.setText(String.format("%.2f MAD", DELIVERY_FEE));
        
        // Setup listeners
        setupClickListeners();
        
        // Load cart items
        loadCartItems();
        
        return view;
    }

    private void setupClickListeners() {
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Navigate to checkout
            Navigation.findNavController(requireView()).navigate(R.id.action_cart_to_checkout);
        });
        
        continueShoppingButton.setOnClickListener(v -> {
            // Navigate back to menu
            Navigation.findNavController(requireView()).navigate(R.id.navigation_menu);
        });
    }

    private void loadCartItems() {
        database.cartDao().getAllCartItems().observe(getViewLifecycleOwner(), items -> {
            cartItems.clear();
            if (items != null) {
                cartItems.addAll(items);
            }
            cartAdapter.updateCartItems(cartItems);
            
            // Update UI based on cart state
            updateCartUI();
            // Calculate totals
            calculateTotals();
        });
    }

    private void updateCartUI() {
        if (cartItems.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setEnabled(false);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(true);
        }
    }

    private void calculateTotals() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }
        
        double total = subtotal + DELIVERY_FEE;
        
        subtotalTextView.setText(String.format("%.2f MAD", subtotal));
        totalTextView.setText(String.format("%.2f MAD", total));
    }

    @Override
    public void onIncreaseQuantity(CartItem cartItem) {
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        updateCartItem(cartItem);
    }

    @Override
    public void onDecreaseQuantity(CartItem cartItem) {
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            updateCartItem(cartItem);
        } else {
            onRemoveItem(cartItem);
        }
    }

    @Override
    public void onRemoveItem(CartItem cartItem) {
        executor.execute(() -> {
            database.cartDao().deleteCartItem(cartItem);
        });
    }

    @Override
    public void onCartItemClick(CartItem cartItem) {
        // Show details or special instructions dialog if needed
    }

    private void updateCartItem(CartItem cartItem) {
        executor.execute(() -> {
            database.cartDao().updateCartItem(cartItem);
        });
    }
}
