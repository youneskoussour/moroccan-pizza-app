package com.example.moroccanpizza.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.Order;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class TrackingFragment extends Fragment {

    private TextView orderIdTextView;
    private TextView statusTextView;
    private TextView estimatedTimeTextView;
    private TextView deliveryPersonTextView;
    private TextView deliveryPhoneTextView;
    private LinearProgressIndicator progressIndicator;
    private Button contactDeliveryButton;
    private Button supportButton;
    private Button backButton;
    private ImageView statusImageView;

    private String orderId;
    private AppDatabase database;
    private Handler handler = new Handler();
    private int currentProgress = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        
        // Initialize views
        orderIdTextView = view.findViewById(R.id.tracking_order_id);
        statusTextView = view.findViewById(R.id.tracking_status);
        estimatedTimeTextView = view.findViewById(R.id.tracking_estimated_time);
        deliveryPersonTextView = view.findViewById(R.id.delivery_person_name);
        deliveryPhoneTextView = view.findViewById(R.id.delivery_person_phone);
        progressIndicator = view.findViewById(R.id.tracking_progress);
        contactDeliveryButton = view.findViewById(R.id.contact_delivery_button);
        supportButton = view.findViewById(R.id.contact_support_button);
        backButton = view.findViewById(R.id.back_button);
        statusImageView = view.findViewById(R.id.tracking_status_image);
        
        // Get database instance
        database = AppDatabase.getInstance(requireContext());
        
        // Get order ID from arguments
        Bundle args = getArguments();
        if (args != null) {
            orderId = args.getString("orderId");
            loadOrderDetails();
        }
        
        // Setup click listeners
        setupClickListeners();
        
        return view;
    }

    private void loadOrderDetails() {
        if (orderId == null) return;
        
        database.orderDao().getOrderById(orderId).observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                updateUI(order);
                
                // Simulate progress updates
                simulateDeliveryProgress(order);
            }
        });
    }

    private void updateUI(Order order) {
        orderIdTextView.setText(String.format("Order #%s", order.getOrderId()));
        statusTextView.setText(order.getOrderStatus());
        
        // Format estimated delivery time
        if (order.getEstimatedDeliveryTime() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
            String time = sdf.format(order.getEstimatedDeliveryTime());
            estimatedTimeTextView.setText(String.format("Estimated delivery: %s", time));
        }
        
        // Show delivery person details if available
        if (order.getDeliveryPersonName() != null && !order.getDeliveryPersonName().isEmpty()) {
            deliveryPersonTextView.setText(order.getDeliveryPersonName());
            deliveryPhoneTextView.setText(order.getDeliveryPersonPhone());
            contactDeliveryButton.setEnabled(true);
        } else {
            deliveryPersonTextView.setText("Not assigned yet");
            deliveryPhoneTextView.setText("");
            contactDeliveryButton.setEnabled(false);
        }
        
        // Update progress based on status
        updateProgressBasedOnStatus(order.getOrderStatus());
    }

    private void updateProgressBasedOnStatus(String status) {
        switch (status) {
            case "Pending":
                currentProgress = 0;
                break;
            case "Confirmed":
                currentProgress = 25;
                break;
            case "Preparing":
                currentProgress = 50;
                break;
            case "Out for Delivery":
                currentProgress = 75;
                break;
            case "Delivered":
                currentProgress = 100;
                break;
            default:
                currentProgress = 0;
                break;
        }
        
        // Animate progress
        ValueAnimator animator = ValueAnimator.ofInt(progressIndicator.getProgress(), currentProgress);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            progressIndicator.setProgress(value);
        });
        animator.start();
        
        // Update status image
        updateStatusImage(status);
    }

    private void updateStatusImage(String status) {
        // Here we would set different images based on status
        // For now, we'll just handle this with placeholder
        switch (status) {
            case "Pending":
            case "Confirmed":
                statusImageView.setImageResource(R.drawable.ic_menu);
                break;
            case "Preparing":
                statusImageView.setImageResource(R.drawable.ic_menu);
                break;
            case "Out for Delivery":
                statusImageView.setImageResource(R.drawable.ic_menu);
                break;
            case "Delivered":
                statusImageView.setImageResource(R.drawable.ic_menu);
                break;
        }
    }

    private void simulateDeliveryProgress(Order order) {
        // This is just for demo purposes
        // In a real app, you would get updates from the server
        
        // Only simulate if not already delivered or cancelled
        if ("Delivered".equals(order.getOrderStatus()) || "Cancelled".equals(order.getOrderStatus())) {
            return;
        }
        
        // Demo: Advance the order status every few seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentStatus = order.getOrderStatus();
                String nextStatus = getNextStatus(currentStatus);
                
                // Update order status in database
                if (!nextStatus.equals(currentStatus)) {
                    order.setOrderStatus(nextStatus);
                    
                    // If moving to "Out for Delivery", assign a delivery person
                    if ("Out for Delivery".equals(nextStatus)) {
                        order.setDeliveryPersonName("Mohammed Alaoui");
                        order.setDeliveryPersonPhone("+212 6XX-XXX-XXX");
                    }
                    
                    // Update order in database
                    new Thread(() -> {
                        database.orderDao().updateOrder(order);
                    }).start();
                    
                    // If not yet delivered, schedule another update
                    if (!"Delivered".equals(nextStatus)) {
                        handler.postDelayed(this, 10000); // Update every 10 seconds
                    }
                }
            }
        }, 10000); // First update after 10 seconds
    }

    private String getNextStatus(String currentStatus) {
        switch (currentStatus) {
            case "Pending":
                return "Confirmed";
            case "Confirmed":
                return "Preparing";
            case "Preparing":
                return "Out for Delivery";
            case "Out for Delivery":
                return "Delivered";
            default:
                return currentStatus;
        }
    }

    private void setupClickListeners() {
        contactDeliveryButton.setOnClickListener(v -> {
            // In a real app, this would open the phone dialer with the delivery person's number
            // For demo, just show a message
            // Toast.makeText(getContext(), "Calling delivery person...", Toast.LENGTH_SHORT).show();
        });
        
        supportButton.setOnClickListener(v -> {
            // Navigate to support fragment
            Navigation.findNavController(requireView()).navigate(R.id.navigation_support);
        });
        
        backButton.setOnClickListener(v -> {
            // Navigate back
            Navigation.findNavController(requireView()).navigateUp();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks
        handler.removeCallbacksAndMessages(null);
    }
}
