package com.example.moroccanpizza.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.moroccanpizza.R;
import com.example.moroccanpizza.database.AppDatabase;
import com.example.moroccanpizza.model.User;
import com.example.moroccanpizza.util.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private EditText phoneEditText;
    private EditText addressEditText;
    private Button updateProfileButton;
    private Button orderHistoryButton;
    private Button supportButton;
    private Button logoutButton;

    private AppDatabase database;
    private Executor executor = Executors.newSingleThreadExecutor();
    private SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // Initialize views
        profileImageView = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.profile_name);
        emailTextView = view.findViewById(R.id.profile_email);
        phoneEditText = view.findViewById(R.id.profile_phone);
        addressEditText = view.findViewById(R.id.profile_address);
        updateProfileButton = view.findViewById(R.id.update_profile_button);
        orderHistoryButton = view.findViewById(R.id.order_history_button);
        supportButton = view.findViewById(R.id.profile_support_button);
        logoutButton = view.findViewById(R.id.logout_button);
        
        // Get database and shared preferences
        database = AppDatabase.getInstance(requireContext());
        sharedPrefManager = new SharedPrefManager(requireContext());
        
        // Load user profile
        loadUserProfile();
        
        // Setup click listeners
        setupClickListeners();
        
        return view;
    }

    private void loadUserProfile() {
        // For demo purposes, create a mock user if not exists
        String userId = sharedPrefManager.getUserId();
        
        if (userId == null || userId.isEmpty()) {
            // Create a new user for demo
            userId = "user_" + System.currentTimeMillis();
            sharedPrefManager.setUserId(userId);
            
            User newUser = new User(
                    userId,
                    "Ahmed Ben Ali",
                    "ahmed.benali@example.com",
                    "+212 6XX-XXX-XXX",
                    "123 Avenue Mohammed V, Rabat, Morocco"
            );
            
            executor.execute(() -> {
                database.userDao().insertUser(newUser);
            });
        }
        
        // Load user data
        database.userDao().getUserById(userId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                updateUI(user);
            }
        });
    }

    private void updateUI(User user) {
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        phoneEditText.setText(user.getPhone());
        addressEditText.setText(user.getAddress());
        
        // Load profile image if available
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            // Load profile image with Glide
            // For now, we'll use a placeholder
            profileImageView.setImageResource(R.drawable.ic_profile);
        }
    }

    private void setupClickListeners() {
        updateProfileButton.setOnClickListener(v -> {
            updateUserProfile();
        });
        
        orderHistoryButton.setOnClickListener(v -> {
            // Navigate to order history
            Navigation.findNavController(requireView()).navigate(R.id.navigation_orders);
        });
        
        supportButton.setOnClickListener(v -> {
            // Navigate to support
            Navigation.findNavController(requireView()).navigate(R.id.navigation_support);
        });
        
        logoutButton.setOnClickListener(v -> {
            // In a real app, you would log out the user
            // For demo, just show a message
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            
            // Clear user data
            sharedPrefManager.clear();
            
            // Restart the app or navigate to login
            // For demo, just navigate to home
            Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
        });
    }

    private void updateUserProfile() {
        String userId = sharedPrefManager.getUserId();
        if (userId == null || userId.isEmpty()) return;
        
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        
        // Validate input
        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            return;
        }
        
        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            return;
        }
        
        // Update user in database
        executor.execute(() -> {
            database.userDao().updateUserPhone(userId, phone);
            database.userDao().updateUserAddress(userId, address);
            
            // Show success message on main thread
            requireActivity().runOnUiThread(() -> 
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            );
        });
    }
}
